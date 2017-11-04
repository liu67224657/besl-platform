/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.DbLockException;
import com.enjoyf.platform.db.NoConnsException;
import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.RPacketBase;
import com.enjoyf.platform.io.SerializationException;
import com.enjoyf.platform.io.StreamBadException;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.io.WPacketBase;
import com.enjoyf.platform.service.service.PerformanceDataCollector;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.service.TransProfile;
import com.enjoyf.platform.service.service.TransProfileContainer;
import com.enjoyf.platform.util.Refresher;
import com.enjoyf.platform.util.log.GAlerter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * Utility class to logicProcess packets in most java servers that use
 * the com.enjoyf.platform.service.service framework. Rather than trapping events
 * directly, you can derive from this class, implement logicProcess,
 * and all generic errors will be handled by this class.
 */

public abstract class PacketDecoder implements PacketProcessor, PerfDataAccessor {

    private static Logger logger = LoggerFactory.getLogger(PacketDecoder.class);

    private PerformanceDataCollector performanceDataCollector = new PerformanceDataCollector();
    private TransProfileContainer transProfileContainer = null;
    private Refresher refresher = new Refresher(5 * 60 * 1000);
    private ResourceLimitMgr resourceLimitMgr;

    /**
     * Call this to register a container of info about the
     * transaction types that we expect to see.
     */
    public PacketDecoder() {
    }

    public void setTransContainer(TransProfileContainer transContainer) {
        transProfileContainer = transContainer;
    }

    /**
     * If the cfg allows, we enable the trapping of resource limit errors
     * and rate limit them according to the passed in cfg.
     */
    public void setResourceTrapping(ResourceLimitCfg cfg) {
        if (cfg != null) {
            resourceLimitMgr = new ResourceLimitMgr(cfg);
        }
    }

    /**
     * Called when a packet arrives. For the most part, this method
     * should just be left alone. It will perform generic
     * error processing, as well as performance gathering.
     */

    public WPacketBase process(ConnThreadBase conn, RPacketBase p) {
        long t1 = System.currentTimeMillis();

        WPacket wp = null;
        RPacket rp = (RPacket) p;

        byte type = rp.getType();
        int metricsType = (int) rp.getMetricsType();

        //--
        // Let's see if it's one of our special packets.
        //--
        if (type == ServiceConstants.GET_PERF_DATA) {
            if (logger.isTraceEnabled()) {
                logger.trace("NamingPacketDecoder.logicProcess: Received GET_PERF_DATA");
            }
            wp = new WPacket();
            wp.setTid(rp.getTid());
            wp.writeByteNx(ServiceConstants.OK);
            wp.writeSerializable(performanceDataCollector.getPerfDataMap());
            return wp;
        }

        TransProfile tp = getTransProfile(metricsType);
        logTransStart(conn, tp, rp);

        try {
            if (resourceLimitMgr != null && resourceLimitMgr.isLimited(tp)) {
                throw new ServiceException(ServiceException.FAST_FAIL_RESOURCE_LIMIT,
                        "rate limit hit for resource limit failed transaction: " + tp);
            }

            wp = logicProcess(conn, rp);
        } catch (ServiceException e) {
            wp = new WPacket();
            wp.writeByteNx(ServiceConstants.NOTOK);
            wp.writeSerializable(e);

            long ttime = System.currentTimeMillis() - t1;
            logTransEnd(ttime, tp, true);

            performanceDataCollector.incTrans(tp, (int) ttime);
            wp.setTid(rp.getTid());

            if (e instanceof DbException && resourceLimitMgr != null) {
                resourceLimitMgr.gotError(tp, (DbException) e);
            }

            //
            if (e instanceof DbException) {
                DbException dbe = (DbException) e;

                if (dbe.equals(DbException.GENERIC)) {
                    //--
                    // This is an unhandled DbException.GENERIC.
                    // In this case, we want to record as much information
                    // as we have available into the bug log. This will
                    // help us in tracking down the problem at a later
                    // date since the DbException.GENERIC category
                    // doesn't give us much to work with.
                    //--
                    GAlerter.lab("PackedDecoder: Unhandled DbException: " + tp + ":exc=",
                            //+ DBConn.formatSqlException(dbe.getSQLException()),
                            ":packet from:" + conn.toString(), e);
                } else if (dbe.shouldAlert()) {
                    //--
                    // This is an unhandled DbException with a known subtype.
                    // In this case, we will simply log the details
                    // of the DbException, without dumping the details
                    // of the underlying SQLException. Because we already
                    // have a known translation for this particular
                    // SQLException, we are not losing any valuable data.
                    // But only if the exception is "alertable".
                    //--
                    //--
                    // We are getting some exceptions here that
                    // are being alerted and should not be. Let's
                    // print out the error code for more info.
                    //--
                    SQLException sqle = dbe.getSQLException();
                    int errCode = sqle != null ? sqle.getErrorCode() : -1;
                    GAlerter.lab("PackedDecoder: Unhandled DbException(2): "
                            + tp + ":exc=" + dbe + ":errcode=" + errCode,
                            ":packet from:" + conn.toString(), dbe);
                } else {
                    logger.error("Got a DbException: ", dbe);
                }
            } else if (e instanceof DbLockException) {
                DbLockException dble = (DbLockException) e;

                if (dble.equals(DbLockException.INVALID_DBLOCK_ID)) {
                    GAlerter.lab("PackedDecoder: DbLockException:  " + tp + ":exc=" + e, ":packet from:" + conn.toString(), e);
                } else {
                    logger.error("DbLockException: ", dble);
                }
            }

            return wp;
        } catch (NoConnsException nce) {
            //--
            // This is an unchecked exception thrown by DbConnPool when
            // no conns are available. It could mean that we are in the
            // midst of re-establishing a conn to a db; so technically
            // it's not a bug.
            //--
            return processException(new ServiceException(ServiceException.GENERIC, nce.toString()),
                    rp.getTid(), t1, tp, conn, false, null);
        } catch (StreamBadException sbe) {
            //--
            // These are due to the stream being corrupted while we
            // are trying to read a serializable object from a packet.
            // These exceptions are rare, but do happen in prod. Since
            // we can't do much about a corrupted stream, we don't
            // want to bug alert it.
            //--
            logger.error(sbe.getMessage(), sbe);
            ServiceException se = new ServiceException(ServiceException.GENERIC, sbe.toString(), null, sbe);

            return processException(se, rp.getTid(), t1, tp, conn, false, null);
        } catch (Exception e) {
            ServiceException se = new ServiceException(ServiceException.GENERIC, e.getMessage(), null, e);

            if (e.getClass() == SerializationException.class) {
                //--
                // Shut down the conn so we don't have a conn leak
                //--
                conn.die();
                se = new ServiceException(ServiceException.SERIALIZATION_ERROR, e.toString(), null, e);
            }

            return processException(se, rp.getTid(), t1, tp, conn, true, e);
        } catch (Error err) {
            String errmsg = err.toString();
            if (errmsg.indexOf("OutOfMemoryError") != -1) {
                errmsg = "Out of mem error on the server";
            }

            ServiceException se = new ServiceException(ServiceException.GENERIC, errmsg);

            return processException(se, rp.getTid(), t1, tp, conn, true, err);
        }

        long ttime = System.currentTimeMillis() - t1;
        logTransEnd(ttime, tp, false);
        performanceDataCollector.incTrans(tp, (int) ttime);

        if (refresher.shouldRefresh()) {
            String strings[] = performanceDataCollector.getDisplay();
            for (int i = 0; i < strings.length; i++) {
                logger.info("Stats : " + strings[i]);
            }
        }

        if (wp != null) {
            wp.setTid(rp.getTid());
        }

        return wp;
    }

    private TransProfile getTransProfile(int metricsType) {
        TransProfile profile = null;
        if (transProfileContainer != null) {
            profile = transProfileContainer.get(metricsType);
        }

        if (profile == null) {
            profile = new TransProfile(metricsType, "UNKNOWN");
        }

        return profile;
    }

    private void logTransStart(ConnThreadBase conn, TransProfile tp, RPacket rp) {
        if (logger.isTraceEnabled()) {
            logger.trace("NamingPacketDecoder.logicProcess: Received transaction: "
                    + tp.toString()
                    + ":tid=" + rp.getTid()
                    + " over: " + conn);
        }
        return;
    }

    private void logTransEnd(long ttime, TransProfile tp, boolean inError) {
        String partial = "trans succeeded";
        if (inError) {
            partial = "trans in_error";
        }

        logger.info("Logic.logicProcess: " + tp.getMetricsName() + " " + partial + " took: " + ttime + " msecs");
    }

    /**
     * Process an exception.
     *
     * @param se        The ServiceException we are returning to the client.
     * @param tid       The return tid.
     * @param startTime The start time of the transaction.
     * @param tp        The current transaction profile.
     * @param conn      The conn we are working with.
     * @param bugAlert  Set to true if we want to bug alert this exception.
     * @param throwable If not null, this is the throwable containing
     *                  the stack trace we want to log.
     */
    private WPacket processException(ServiceException se, int tid, long startTime,
                                     TransProfile tp, ConnThreadBase conn, boolean bugAlert, Throwable throwable) {
        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.NOTOK);
        wp.writeSerializable(se);

        long ttime = System.currentTimeMillis() - startTime;

        String logMsg = "NamingPacketDecoder:" + tp + ":" + se.toString();
        String varMsg = ":Packet from: " + conn.toString();

        if (bugAlert) {
            GAlerter.lab(logMsg, varMsg, throwable);
        } else {
            GAlerter.lan(logMsg + varMsg);
        }

        if (logger.isTraceEnabled()) {
            logger.trace("NamingPacketDecoder.logicProcess: trans in_error: took: " + ttime + " msecs");
        }

        performanceDataCollector.incTrans(tp, (int) ttime);
        wp.setTid(tid);

        return wp;
    }

    public PerformanceDataCollector getPerfDataCollector() {
        return performanceDataCollector;
    }

    /**
     * This function should be implemented to call the server's logic.
     */
    protected abstract WPacket logicProcess(ConnThreadBase conn, RPacket rp) throws ServiceException;
}
