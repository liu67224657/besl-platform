package com.enjoyf.webapps.joyme.weblogic.chinajoy;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.profile.ProfileMobileDevice;
import com.enjoyf.platform.service.profile.ProfileServiceSngl;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.viewline.ViewCategory;
import com.enjoyf.platform.service.viewline.ViewCategoryField;
import com.enjoyf.platform.service.viewline.ViewItemType;
import com.enjoyf.platform.service.viewline.ViewLine;
import com.enjoyf.platform.service.viewline.ViewLineField;
import com.enjoyf.platform.service.viewline.ViewLineItem;
import com.enjoyf.platform.service.viewline.ViewLineItemDisplayType;
import com.enjoyf.platform.service.viewline.ViewLineItemField;
import com.enjoyf.platform.service.viewline.ViewLineServiceSngl;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.TextProcessorFatctory;
import com.enjoyf.platform.text.WordProcessorKey;
import com.enjoyf.platform.text.processor.SubStringTextProcessor;
import com.enjoyf.platform.text.processor.TextProcessor;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.sql.QueryCriterionRelation;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-23
 * Time: 下午6:05
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "chinajoyWeblogic")
public class ChinajoyWebLogic {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String FOOT_RELOAD = "w";
    private static final String HEAD_RELOAD = "x";

    /**
     * 获取内容list
     *
     * @param num
     * @param order
     * @param dt
     * @return
     */
    public List<ContentDTO> getContentInfoList(Integer num, String order, String dt, String categoryCode) throws ServiceException {
        List<ContentDTO> returnValue = new ArrayList<ContentDTO>();

        // 查询分类
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode));
        ViewCategory category = ViewLineServiceSngl.get().getCategory(queryExpress);

        if (category == null) {
            return returnValue;
        }

        //get the view line
        QueryExpress queryLineExpress = new QueryExpress();
        queryLineExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, category.getCategoryId()));
        queryLineExpress.add(QueryCriterions.eq(ViewLineField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        Map<String, ViewLine> viewLineMap = ViewLineServiceSngl.get().queryViewLines(queryLineExpress);

        ViewLine viewLine = null;
        for (ViewLine line : viewLineMap.values()) {
            if (line.getValidStatus().equals(ValidStatus.VALID)) {
                viewLine = line;
                break;
            }
        }

        if (viewLine == null) {
            return returnValue;
        }

        QueryExpress queryLineItemExpress = new QueryExpress();
        queryLineItemExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, viewLine.getLineId()));
        queryLineItemExpress.add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()));

        if (!StringUtil.isEmpty(order) && !StringUtil.isEmpty(dt)) {
            if (FOOT_RELOAD.equals(dt)) {
                queryLineItemExpress.add(QueryCriterions.gt(ViewLineItemField.DISPLAYORDER, Integer.valueOf(order)));
            } else {
                queryLineItemExpress.add(QueryCriterions.lt(ViewLineItemField.DISPLAYORDER, Integer.valueOf(order)));
            }
        }
        queryLineItemExpress.add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.ASC));

        Pagination pagination = new Pagination(0, 1, num);
        PageRows<ViewLineItem> itemPageRows = ViewLineServiceSngl.get().queryLineItems(queryLineItemExpress, pagination);

        for (ViewLineItem lineItem : itemPageRows.getRows()) {
            ContentDTO contentDTO = parseContentDTOByLineItem(lineItem);
            if (contentDTO != null) {
                returnValue.add(contentDTO);
            }
        }

        return returnValue;
    }

    public SpecialDTO getCjHomeSpecial(String categoryCode) throws ServiceException {
        //第一个子分类
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode));
        ViewCategory category = ViewLineServiceSngl.get().getCategory(queryExpress);

        category = ViewLineServiceSngl.get().getCategoryByCategoryIdFromCache(category.getCategoryId());

        if (category == null || category.getChildrenCategories() == null || category.getChildrenCategories().size() < 1) {
            return null;
        }

        ViewCategory specialCategory = category.getChildrenCategories().get(0);
        SpecialDTO specialDTO = new SpecialDTO();

        specialDTO.setImg(ImageURLTag.parseWallThumbImgLink(specialCategory.getDisplaySetting().getIconURL()));
        specialDTO.setSid(specialCategory.getCategoryCode());
        specialDTO.setTitle(specialCategory.getCategoryName());

        return specialDTO;
    }

    /**
     * 图片花絮
     *
     * @param num
     * @param order
     * @param dt
     * @param categoryCode
     * @return
     * @throws ServiceException
     */
    public CjPhotoMsgDTO getCjPhotoMsgDTO(Integer num, String order, String dt, String categoryCode) throws ServiceException {
        CjPhotoMsgDTO cjPhotoMsgDTO = new CjPhotoMsgDTO(CjPhotoMsgDTO.CODE_E);

        // 查询分类
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode));
        ViewCategory category = ViewLineServiceSngl.get().getCategory(queryExpress);

        if (category == null) {
            return cjPhotoMsgDTO;
        }

        //get the view line
        QueryExpress queryLineExpress = new QueryExpress();
        queryLineExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, category.getCategoryId()));
        queryLineExpress.add(QueryCriterions.eq(ViewLineField.ITEMTYPE, ViewItemType.CONTENT.getCode()));
        Map<String, ViewLine> viewLineMap = ViewLineServiceSngl.get().queryViewLines(queryLineExpress);

        ViewLine viewLine = null;
        for (ViewLine line : viewLineMap.values()) {
            if (line.getItemType().equals(ViewItemType.CONTENT)) {
                viewLine = line;
                break;
            }
        }

        if (viewLine == null) {
            return cjPhotoMsgDTO;
        }

        QueryExpress queryLineItemExpress = new QueryExpress();
        queryLineItemExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, viewLine.getLineId()));
        queryLineItemExpress.add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        queryLineItemExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE, QueryCriterionRelation.LT, ViewLineItemDisplayType.TOP, 1));

        if (!StringUtil.isEmpty(order) && !StringUtil.isEmpty(dt)) {
            if (FOOT_RELOAD.equals(dt)) {
                queryLineItemExpress.add(QueryCriterions.gt(ViewLineItemField.DISPLAYORDER, Integer.valueOf(order)));
            } else {
                queryLineItemExpress.add(QueryCriterions.lt(ViewLineItemField.DISPLAYORDER, Integer.valueOf(order)));
            }
        }
        queryLineItemExpress.add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.ASC));

        Pagination pagination = new Pagination(0, 1, num);
        PageRows<ViewLineItem> itemPageRows = ViewLineServiceSngl.get().queryLineItems(queryLineItemExpress, pagination);

        List<PhotoDTO> photoDTOList = new ArrayList<PhotoDTO>();
        for (ViewLineItem lineItem : itemPageRows.getRows()) {
            PhotoDTO photoDTO = parsePhotoDTOByLineItem(lineItem);
            if (photoDTO != null) {
                photoDTOList.add(photoDTO);
            }
        }

        //顶部信息
        if (StringUtil.isEmpty(dt) || HEAD_RELOAD.equals(dt)) {
            QueryExpress queryLineTopItemExpress = new QueryExpress();
            queryLineTopItemExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, viewLine.getLineId()));
            queryLineTopItemExpress.add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()));
            queryLineTopItemExpress.add(QueryCriterions.bitwiseAnd(ViewLineItemField.DISPLAYTYPE, QueryCriterionRelation.GT, ViewLineItemDisplayType.TOP, 0));
            queryLineTopItemExpress.add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.DESC));

            Pagination topPage = new Pagination(0, 1, 4);
            PageRows<ViewLineItem> itemtopPageRows = ViewLineServiceSngl.get().queryLineItems(queryLineTopItemExpress, topPage);

            List<PhotoDTO> topPhotoDTOList = new ArrayList<PhotoDTO>();
            for (ViewLineItem lineItem : itemtopPageRows.getRows()) {
                PhotoDTO photoDTO = parsePhotoDTOByLineItem(lineItem);
                if (photoDTO != null) {
                    topPhotoDTOList.add(photoDTO);
                }
            }

            cjPhotoMsgDTO.setTops(topPhotoDTOList);
        }

        cjPhotoMsgDTO.setResults(photoDTOList);
        cjPhotoMsgDTO.setStatus_code(CjPhotoMsgDTO.CODE_S);
        return cjPhotoMsgDTO;
    }

    public ViewCategory getViewCategoryFromCache(String categoryCode) throws ServiceException {
        QueryExpress queryExpress = new QueryExpress();

        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode));
        ViewCategory category = ViewLineServiceSngl.get().getCategory(queryExpress);

        category = ViewLineServiceSngl.get().getCategoryByCategoryIdFromCache(category.getCategoryId());
        return category;
    }

    public Content getLastContentByCategory(String categoryCode) throws ServiceException {
        Content content = null;
        // 查询分类
        QueryExpress queryExpress = new QueryExpress();
        queryExpress.add(QueryCriterions.eq(ViewCategoryField.CATEGORYCODE, categoryCode));
        ViewCategory category = ViewLineServiceSngl.get().getCategory(queryExpress);

        if (category == null) {
            return content;
        }

        //get the view line
        QueryExpress queryLineExpress = new QueryExpress();
        queryLineExpress.add(QueryCriterions.eq(ViewLineField.CATEGORYID, category.getCategoryId()));
        queryLineExpress.add(QueryCriterions.eq(ViewLineField.ITEMTYPE, ViewItemType.CONTENT.getCode()));
        Map<String, ViewLine> viewLineMap = ViewLineServiceSngl.get().queryViewLines(queryLineExpress);

        ViewLine viewLine = null;
        for (ViewLine line : viewLineMap.values()) {
            if (line.getItemType().equals(ViewItemType.CONTENT)) {
                viewLine = line;
                break;
            }
        }

        if (viewLine == null) {
            return content;
        }

        QueryExpress queryLineItemExpress = new QueryExpress();
        queryLineItemExpress.add(QueryCriterions.eq(ViewLineItemField.LINEID, viewLine.getLineId()));
        queryLineItemExpress.add(QueryCriterions.eq(ViewLineItemField.VALIDSTATUS, ValidStatus.VALID.getCode()));
        queryLineItemExpress.add(QuerySort.add(ViewLineItemField.DISPLAYORDER, QuerySortOrder.ASC));

        Pagination pagination = new Pagination(0, 1, 10);
        PageRows<ViewLineItem> itemPageRows = ViewLineServiceSngl.get().queryLineItems(queryLineItemExpress, pagination);

        for (ViewLineItem lineItem : itemPageRows.getRows()) {
            content = ContentServiceSngl.get().getContentById(lineItem.getDirectId());
            if (content != null) {
                break;
            }
        }

        return content;
    }

    public boolean increaseProfileMobileDevice(ProfileMobileDevice profileMobileDevice) throws ServiceException {

        profileMobileDevice = ProfileServiceSngl.get().increaseProfileMobileDevice(profileMobileDevice);

        if (profileMobileDevice != null) {
            return true;
        }

        return false;
    }

    private ContentDTO parseContentDTOByLineItem(ViewLineItem lineItem) {
        ContentDTO contentDTO = new ContentDTO();

        contentDTO.setCid(lineItem.getDirectId());
        contentDTO.setCuno(lineItem.getDirectUno());
        contentDTO.setOrder(String.valueOf(lineItem.getDisplayOrder()));

        if (lineItem.getDisplayInfo() != null && lineItem.getDisplayInfo().getIconUrl() != null
                && lineItem.getDisplayInfo().getSubject() != null && lineItem.getDisplayInfo().getDesc() != null) {
            contentDTO.setImg(ImageURLTag.parseWallThumbImgLink(lineItem.getDisplayInfo().getIconUrl()));
            contentDTO.setTitle(lineItem.getDisplayInfo().getSubject());
            contentDTO.setDes(lineItem.getDisplayInfo().getDesc());
        } else {
            try {
                Content content = ContentServiceSngl.get().getContentById(lineItem.getDirectId());

                if (lineItem.getDisplayInfo() != null && lineItem.getDisplayInfo().getSubject() != null) {
                    contentDTO.setTitle(lineItem.getDisplayInfo().getSubject());
                } else {
                    if (content != null) {
                        contentDTO.setTitle(content.getSubject());
                    }
                }

                if (lineItem.getDisplayInfo() != null && lineItem.getDisplayInfo().getIconUrl() != null) {
                    contentDTO.setImg(ImageURLTag.parseWallThumbImgLink(lineItem.getDisplayInfo().getIconUrl()));
                }

                if (lineItem.getDisplayInfo() != null && lineItem.getDisplayInfo().getDesc() != null) {
                    contentDTO.setDes(lineItem.getDisplayInfo().getDesc());
                } else {
                    ResolveContent resolveContent = new ResolveContent();
                    if (content != null) {
                        resolveContent.setContent(content.getContent());

                        TextProcessor textProcessor = TextProcessorFatctory.get().getProcessorByKey(WordProcessorKey.KEY_DISCOVERY_CHAT);
                        if (textProcessor != null) {
                            resolveContent = textProcessor.process(resolveContent);
                        }
                        SubStringTextProcessor subStringTextProcessor = new SubStringTextProcessor(30, "...", 1);
                        subStringTextProcessor.process(resolveContent);
                        contentDTO.setDes(resolveContent.getContent());
                    }

                }

            } catch (ServiceException e) {
                logger.error("parseContentDTOByLineItem caught an exception.", e);
                return null;
            }
        }
        return contentDTO;
    }

    private PhotoDTO parsePhotoDTOByLineItem(ViewLineItem lineItem) throws ServiceException {
        PhotoDTO photoDTO = new PhotoDTO();

        photoDTO.setCid(lineItem.getDirectId());
        photoDTO.setCuno(lineItem.getDirectUno());
        photoDTO.setOrder(String.valueOf(lineItem.getDisplayOrder()));

        if (lineItem.getDisplayInfo() != null && lineItem.getDisplayInfo().getIconUrl() != null
                && lineItem.getDisplayInfo().getSubject() != null) {
            photoDTO.setImg(ImageURLTag.parseWallThumbImgLink(lineItem.getDisplayInfo().getIconUrl()));
            photoDTO.setTitle(lineItem.getDisplayInfo().getSubject());
        } else {
            try {

                if (lineItem.getDisplayInfo() != null && lineItem.getDisplayInfo().getIconUrl() != null) {
                    photoDTO.setImg(ImageURLTag.parseWallThumbImgLink(lineItem.getDisplayInfo().getIconUrl()));
                }

                Content content = ContentServiceSngl.get().getContentById(lineItem.getDirectId());

                if (lineItem.getDisplayInfo() != null && lineItem.getDisplayInfo().getSubject() != null) {
                    photoDTO.setTitle(lineItem.getDisplayInfo().getSubject());
                } else {
                    photoDTO.setTitle(content.getSubject());
                }

            } catch (ServiceException e) {
                logger.error("parsePhotoDTOByLineItem caught an exception.", e);
                return null;
            }
        }
        return photoDTO;
    }

}

