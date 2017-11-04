/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

import com.enjoyf.platform.io.RPacket;

/**
 * A strategy interface for deciding how to logicProcess requests.
 */
public interface ProcessStrategy {
    /**
     * @param req     The request object to logicProcess.
     * @param chooser The ConnChooser object to use.
     * @return Returns an RPacket object *if* the request is a blocking
     *         request. Otherwise a null is returned.
     * @throws ServiceException Thrown if there is a problem in
     *                          communicating over the network.
     */
    public RPacket process(Request req, ConnChooser chooser) throws ServiceException;
}
