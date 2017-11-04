/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.RPacket;

/**
 * This logicProcess strategy is used to only try a transaction once; it does
 * not fail-over.
 */
public class ProcessStrategyOnce implements ProcessStrategy {
    /**
     * Process the request. This is a wrapper method for catching
     * exceptions. See logicProcess.
     *
     * @param req     The request to send off.
     * @param chooser The ConnChooser used to choose the
     *                ServiceConn to use.
     * @return rp    The RPacket returned by the transaction *if* it's
     *         a blocking request. If non-blocking, a null is returned.
     * @throws ServiceException Thrown if there is a network problem
     *                          or a business logic problem.
     */
    public RPacket process(Request req, ConnChooser chooser) throws ServiceException {
        ServiceConn sconn = chooser.get(req);

        if (sconn == null) {
            throw new ServiceException(ServiceException.CONNECT, "ProcessStrategyOnce.process(): Could not find the service. Are you sure the servers are up for: " + chooser.getId() + "?");
        }

        return sconn.send(req);
    }
}
