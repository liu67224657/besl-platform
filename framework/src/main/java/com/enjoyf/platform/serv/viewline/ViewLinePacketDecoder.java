package com.enjoyf.platform.serv.viewline;


import com.enjoyf.platform.io.RPacket;
import com.enjoyf.platform.io.WPacket;
import com.enjoyf.platform.serv.thrserver.ConnThreadBase;
import com.enjoyf.platform.serv.thrserver.PacketDecoder;
import com.enjoyf.platform.service.event.Event;
import com.enjoyf.platform.service.service.ServiceConstants;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.Rangination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;

import java.io.Serializable;
import java.util.Set;

/**
 * @author litaijun
 * @since 2012-2-9 17:36:20
 */
public class ViewLinePacketDecoder extends PacketDecoder {

    private ViewLineLogic processLogic;


    ViewLinePacketDecoder(ViewLineLogic logic) {
        processLogic = logic;

        setTransContainer(ViewLineConstants.getTransContainer());
    }

    /**
     * This function should be implemented to call the server's logic.
     */
    @Override
    protected WPacket logicProcess(ConnThreadBase conn, RPacket rPacket) throws ServiceException {
        byte type = rPacket.getType();

        WPacket wp = new WPacket();
        wp.writeByteNx(ServiceConstants.OK);

        switch (type) {

            // category
            case ViewLineConstants.CATEGORY_CREATE:
                wp.writeSerializable(processLogic.createCategory((ViewCategory) rPacket.readSerializable()));
                break;
            case ViewLineConstants.CATEGORY_GET:
                wp.writeSerializable(processLogic.getCategory((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.CATEGORY_TREE_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryCategoryTreeByAspectParent((ViewCategoryAspect) rPacket.readSerializable(), rPacket.readIntNx()));
                break;
            case ViewLineConstants.CATEGORY_MODIFY:
                wp.writeBooleanNx(processLogic.modifyCategory((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.CATEGORY_REMOVE:
                wp.writeBooleanNx(processLogic.deleteCategory((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.CATEGORY_TREE_QUERY_PAGE:
                wp.writeSerializable(processLogic.queryCategoryTreeByAspectParentPaging((ViewCategoryAspect) rPacket.readSerializable(), rPacket.readIntNx(), rPacket.readStringUTF(), (Pagination) rPacket.readSerializable()));
                break;
            case ViewLineConstants.CATEGORY_QUERY_BY_PAGE:
                wp.writeSerializable(processLogic.queryCategory((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;

            //line
            case ViewLineConstants.LINE_CREATE:
                wp.writeSerializable(processLogic.createViewLine((ViewLine) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINE_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryViewLines((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINE_QUERY_BY_PAGE:
                wp.writeSerializable((Serializable) processLogic.queryViewLinesByPage((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINE_QUERY_BY_RANGE:
                wp.writeSerializable((Serializable) processLogic.queryViewLinesByRange((QueryExpress) rPacket.readSerializable(), (Rangination) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINE_GET:
                wp.writeSerializable(processLogic.getViewLine((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINE_MODIFY:
                wp.writeBooleanNx(processLogic.modifyViewLine((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINE_REMOVE:
                wp.writeBooleanNx(processLogic.deleteViewLine((QueryExpress) rPacket.readSerializable()));
                break;

            //lineitem
            case ViewLineConstants.LINEITEM_ADD:
                wp.writeSerializable(processLogic.addLineItem((ViewLineItem) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINEITEM_GET:
                wp.writeSerializable(processLogic.getLineItem((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINEITEM_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryLineItems((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINEITEM_QUERY_PAGE:
                wp.writeSerializable(processLogic.queryLineItems((QueryExpress) rPacket.readSerializable(), (Pagination) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINEITEM_QUERY_RANGE:
                wp.writeSerializable(processLogic.queryLineItems((QueryExpress) rPacket.readSerializable(), (Rangination) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINEITEM_MODIFY:
                wp.writeSerializable(processLogic.modifyLineItem((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINEITEM_REMOVE:
                wp.writeBooleanNx(processLogic.removeLineItem((QueryExpress) rPacket.readSerializable()));
                break;

            //
            case ViewLineConstants.CATEGORY_QUERY_TREE_BY_ITEM:
                wp.writeSerializable((Serializable) processLogic.queryCategoryTreeByItem((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.CATEGORY_QUERY_TREE_BY_PRIVACY:
                wp.writeSerializable((Serializable) processLogic.queryCategoryTreeByPrivacy((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.LINEITEM_GATHER:
                wp.writeBooleanNx(processLogic.gatherLineItem(
                        (ViewItemType) rPacket.readSerializable(), (ViewLineItem) rPacket.readSerializable(),
                        (Set<Integer>) rPacket.readSerializable(), (Set<Integer>) rPacket.readSerializable()
                ));
                break;
            case ViewLineConstants.CATEGORY_QUERY_TREE_BY_ID:
                wp.writeSerializable(processLogic.getCategoryByCategoryIdFromCache(rPacket.readIntNx()));
                break;
            //the privacy
            case ViewLineConstants.PRIVACY_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryCategoryPrivacies((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.PRIVACY_CREATE:
                wp.writeSerializable(processLogic.createCategoryPrivacy((ViewCategoryPrivacy) rPacket.readSerializable()));
                break;
            case ViewLineConstants.PRIVACY_GET:
                wp.writeSerializable(processLogic.getCategoryPrivacy((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.PRIVACY_REMOVE:
                wp.writeSerializable(processLogic.deleteCategoryPrivacy((QueryExpress) rPacket.readSerializable()));
                break;
            case ViewLineConstants.PRIVACY_MODIFY:
                wp.writeSerializable(processLogic.modifyCategoryPrivacy((UpdateExpress) rPacket.readSerializable(), (QueryExpress) rPacket.readSerializable()));
                break;

            //feint content favorite
            case ViewLineConstants.FEINT_CONTENT_FAVORITE:
                wp.writeBooleanNx(processLogic.feintContentFavorite(rPacket.readStringUTF(), (FeintCache) rPacket.readSerializable()));
                break;
            //
            case ViewLineConstants.SUMDATE_GET:
                wp.writeSerializable(processLogic.getViewLineSumData((QueryExpress) rPacket.readSerializable()));
                break;

            case ViewLineConstants.VIEWLINE_LOG_CREATE:
                wp.writeSerializable(processLogic.createLog((ViewLineLog) rPacket.readSerializable()));
                break;
            case ViewLineConstants.VIEWLINE_LOG_GET_BY_LOGID:
                wp.writeSerializable(processLogic.getLineLog(rPacket.readLongNx()));
                break;
            case ViewLineConstants.VIEWLINE_LOG_QUERY_BY_SRCID:
                wp.writeSerializable((Serializable) processLogic.queryLineLogs(rPacket.readIntNx(), (ViewLineLogDomain) rPacket.readSerializable()));
                break;

            case ViewLineConstants.VIEWLINE_ITEM_ARTICLE_IDS_QUERY:
                wp.writeSerializable((Serializable) processLogic.queryArticleIds((Set<Integer>) rPacket.readSerializable(), (Set<Integer>) rPacket.readSerializable()));
                break;
            //event receive.
            case ViewLineConstants.RECIEVE_EVENT:
                wp.writeBooleanNx(processLogic.receiveEvent((Event) rPacket.readSerializable()));
                break;

            default:
                GAlerter.lab("ViewLinePacketDecoder.logicProcess: Unrecognized type: " + type);
                throw new ServiceException(ServiceException.BAD_PACKET);
        }

        return wp;
    }
}
