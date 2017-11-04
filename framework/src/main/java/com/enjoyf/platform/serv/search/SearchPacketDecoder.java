/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.search;

import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.search.*;
import com.enjoyf.platform.service.search.solr.ProfileSolrjBean;
import com.enjoyf.platform.service.search.solr.SolrCore;
import com.enjoyf.platform.service.search.solr.SolrField;
import com.enjoyf.platform.service.search.solr.SolrOrder;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import org.apache.solr.common.SolrInputDocument;

import java.util.Collection;
/**
 * @author <a href=mailto:yinpengyi@fivewh.com>Yin Pengyi</a>
 */

/**
 * This interface receives packets from the remote clients. It
 * translates them into method calls into the business logic.
 * <p/>
 * The logicProcess() method may be called reentrantly, so it and any
 * handlers must be thread-safe.
 */
class SearchPacketDecoder extends PacketDecoder {
    private SearchLogic processLogic;

    /**
     * Constructor takes the UserPropsLogic object that we're going to use
     * to logicProcess the packets.
     *
     * @param logic our logical friend
     */
    SearchPacketDecoder(SearchLogic logic) {
        processLogic = logic;

        setTransContainer(SearchConstants.getTransContainer());
    }

    /**
     * Called when ThreadSampleInfo packet arrives. This routine will
     * just forward the call to the logic object which
     * will take care of actually decoding the packet.
     */
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {
            case SearchConstants.RECIEVE_EVENT:
                wp.writeSerializable(rPacket.readSerializable());
                break;
            case SearchConstants.SEARCH_GIFT:
                wp.writeSerializable(processLogic.searchGiftByText((SearchGiftCriteria) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;

            default:
                GAlerter.lab("SearchPacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
