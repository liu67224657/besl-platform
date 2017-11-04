/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.naming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.collection.QueueListener;
import com.enjoyf.platform.util.collection.QueueThread;
import com.enjoyf.platform.util.thin.DieThread;

/**
 * This class is a wrapper around BackChannelService that adds
 * the ping thread for that service. It also sends out the
 * requests to the remote NS on a separate thread so that our
 * business logic doesn't block in case there's some network
 * problem.
 */
class BackChannelServiceWrap {
	
	private static final Logger logger = LoggerFactory.getLogger(BackChannelServiceWrap.class);
	
    private BackChannelService backChannelService;
    private QueueThread queueThread;
    private DieThread dieThread;
    private int pingInterval;
    private LoadInfo.Getter loadInfoGetter;

    BackChannelServiceWrap(LoadInfo.Getter getter, int ping, BackChannelService service) {
        loadInfoGetter = getter;
        pingInterval = ping;
        backChannelService = service;

        queueThread = new QueueThread(
                new QueueListener() {
                    public void process(Object obj) {
                        p_process((BackChannelRequest) obj);
                    }
                }
        );

        dieThread = new DieThread() {
            public void run() {
                setName("BchPingThread:" + getName());

                BackChannelRequest req = new BackChannelRequest.Ping(
                        loadInfoGetter.getLoadInfo());

                while (!shouldDie()) {
                    try {
                        backChannelService.send(req);
                    }
                    catch (ServiceException se) {
                        if (se.getValue() == ServiceException.CONNECT) {
                            logger.error("Could not establish connection to remote naming server at: "
                                    + backChannelService.getAddress(), se);
                        } else {
                            logger.error("BackChannelPingThread: caught exc: ", se);
                        }
                    }
                    catch (Exception e) {
                    	logger.error("BackChannelPingThread: caught exc: " + e);
                    }

                    Utility.sleep(pingInterval);
                }
            }
        };

        dieThread.start();
    }

    private void p_process(BackChannelRequest req) {
        try {
            backChannelService.send(req);
        }
        catch (ServiceException se) {
            logger.error("BackChannelServiceWrap.logicProcess: "
                    + "Error sending: " + req, se);
        }
        catch (Exception e) {
            logger.error("BackChannelServiceWrap.logicProcess: "
                    + "Error sending: " + req, e);
        }
    }

    void send(BackChannelRequest req) {
        queueThread.add(req);
    }

    void close() {
        backChannelService.close();
        dieThread.die();
        queueThread.die();
    }
}
