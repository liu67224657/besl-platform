package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.RPacket;

/**
 * Our standard way of processing requests. Handles fail-over.
 */
public class ProcessStrategyStandard implements ProcessStrategy {
	
    //the fail over try times.
    private int MAX_RETRY_COUNT = 3;

    /**
     * Process the request.
     *
     * @param req     The request to send off.
     * @param chooser The ConnChooser to use for finding the
     *                connection to send the request to.
     * @return rp    The RPacket returned by the transaction. Note that
     *         non-blocking transactions return a null.
     * @throws ServiceException Thrown if there is a problem
     *                          processing the request typically due to the service being
     *                          down, or a timeout.
     */
    public RPacket process(Request req, ConnChooser chooser) throws ServiceException {
        RPacket rp = null;
        ServiceException returnException = null;

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            returnException = null;

            try {
                // Process the request, if successful, we break out of the loop.
                rp = p_process(req, chooser);
                break;
            } catch (NoConnsException nce) {
                // If we could not find any conns at all, just bail.
                String id = chooser == null ? "UNKNOWN" : chooser.getId();

                throw new ServiceException(ServiceException.CONNECT,
                        "ProcessStrategyStandard.logicProcess():"
                                + "Could not find the service. "
                                + "Are you sure the servers are up for: " + id + "?");
            } catch (ServiceException se) {
                // Here we catch any ServiceException thrown by logicProcess()
                returnException = se;
                if (!se.equals(ServiceException.CONNECT) && !se.equals(ServiceException.CONNECT_IN_PROGRESS)) {
                    // If we had an error that was not of the CONNECT variety,
                    // we just rethrow it. Otherwise we keep looping looking for conns.
                    throw se;
                }
            }
        }

        // If we break out of the loop, and returnException is not null,
        // it means we failed to logicProcess the transaction after trying
        // the maximum number of times.
        if (returnException != null) {
            throw returnException;
        }

        return rp;
    }

    private RPacket p_process(Request req, ConnChooser chooser) throws ServiceException, NoConnsException {
        ServiceConn sconn = chooser.get(req);

        if (sconn == null) {
            throw new NoConnsException();
        }

        if (sconn.isInProgress()) {
            throw new ServiceException(ServiceException.CONNECT_IN_PROGRESS);
        }

        // Send the packet. If we get a connect error, we toss the conn as
        // a bad conn and pass the exception up.
        RPacket rp = null;
        try {
            rp = sconn.send(req);
        } catch (ServiceException se) {
            if (se.equals(ServiceException.CONNECT)) {
                synchronized (this) {
                    chooser.remove(sconn);
                }
            }

            throw se;
        }

        return rp;
    }

    @SuppressWarnings("serial")
	private class NoConnsException extends Exception {
        public NoConnsException() {
            super();
        }
    }
}
