package com.enjoyf.webapps.tools.weblogic.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentField;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.text.ResolveContent;
import com.enjoyf.platform.text.processor.RemoveMediaTagProcessor;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-1-4
 * Time: 上午10:09
 * To change this template use File | Settings | File Templates.
 */
abstract public class StatusStrategy {
    private ContentWebLogic contentWebLogic;
    private String contentId;
    private String uno;
    //是否是有选择性的屏蔽一条记录中的图片
    private boolean selectable;
    //是否是屏蔽操作
    private boolean filter;
    private Integer originalValue;
    private String[] selected;
    private boolean allValidStatusSame = true;

    public StatusStrategy(ContentWebLogic contentWebLogic, String contentId, String uno, String[] selected, Integer originalValue){
        this.contentWebLogic = contentWebLogic;
        this.contentId = contentId;
        this.uno = uno;
        this.selected = selected;
        this.originalValue = originalValue;
    }

    //对外接口，关于图片修改文章内容的模板
    public Map<ObjectField, Object> modifyIMGContentTemplate(boolean removable){
        //准备Content
        Content content = contentWebLogic.queryContentByIdUno(contentId, uno);

        Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
        map.putAll(prepareMapValue(content, !removable));

        return doModifyAuditColumn(map, removable);
    }

    /**
     * 修改审核过程中需要更改状态或值的字段，具体逻辑交给子类实现
     * @param map 要给修改成这样的值
     * @param removable 删除标记
     * @return Map<ObjectField, Object> map
     */
    abstract protected Map<ObjectField, Object> doModifyAuditColumn(Map<ObjectField, Object> map, boolean removable);

    //在修改Content之前准备好Map<ObjectField, Object>数据
    private Map<ObjectField, Object> prepareMapValue(Content content, boolean validStatus) {
        Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
        //
        Content newContent = setImageContentSetValidStatusValue(content, selected, validStatus);
        //prepare images map values
        map.put(ContentField.IMAGES, newContent.getImages().toJsonStr());

        map.putAll(prepareContentRemoveStatusValue(content, !validStatus));

        return map;
    }

    //准备关于RemoveStatus的Map<ObjectField, Object>
    private Map<ObjectField, Object> prepareContentRemoveStatusValue(Content content, boolean removable) {
        Map<ObjectField, Object> map = new HashMap<ObjectField, Object>();
            //屏蔽操作
            if(removable){
                //如果所有的图片的valid标记都相同，并且为false，并且 文章里除了图片什么都没发
                if(isAllValidStatusSame() && !content.getImages().getImages().iterator().next().getValidStatus() && isContentEmpty(content)){
                    map.put(ContentField.REMOVESTATUS, ActStatus.ACTED.getCode());
                }
            }else {//通过操作
                //如果如果所有的图片的valid标记都相同，并且为true，并且文章里除了图片什么都没有
                if(isAllValidStatusSame() && content.getImages().getImages().iterator().next().getValidStatus() && isContentEmpty(content)){
                    map.put(ContentField.REMOVESTATUS, ActStatus.UNACT.getCode());
                }
            }
        return map;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    protected boolean isAllValidStatusSame() {
        return allValidStatusSame;
    }

    protected void setAllValidStatusSame(boolean allValidStatusSame) {
        this.allValidStatusSame = allValidStatusSame;
    }

    //判断文章除去格式以外的内容是否为空，
    private boolean isContentEmpty(Content content) {
        ResolveContent resolveContent= ResolveContent.transferByContent(content);
        String onlyContent= new RemoveMediaTagProcessor().process(resolveContent).getContent();

        return StringUtil.isEmpty(onlyContent)
                && (null == content.getAudios() || content.getAudios().getAudios().size() == 0)
                && (null == content.getVideos() || content.getVideos().getVideos().size() == 0);
    }

    //修改ImageContentSet的ValidStatus值
    private Content setImageContentSetValidStatusValue(Content content, String[] selected, boolean value) {
        ImageContentSet imageContentSet = content.getImages();
        if(imageContentSet != null){
            Set<ImageContent> set = imageContentSet.getImages();
            if(set != null){
                /***循环更改Json串中ValidStatus标记***/
                for(ImageContent imageContent : set){
                    if(selected == null){
                        imageContent.setValidStatus(value);
                    } else {
                        for(String selectedSngl : selected){
                            if(imageContent.getUrl() != null && selectedSngl.equals(imageContent.getUrl())){
                                imageContent.setValidStatus(value);
                                set.add(imageContent);
                            }
                        }
                    }
                }
            }
            //是否所有的ValidStatus全是一致的
            assert set != null;
            Iterator<ImageContent> it = set.iterator();
            Boolean temp = null;
            while(it.hasNext()){
                ImageContent imageContent = it.next();
                if(temp != null && !temp.equals(imageContent.getValidStatus())){
                    setAllValidStatusSame(false);
                }
                temp = imageContent.getValidStatus();
            }
        }

        return content;
    }

}
