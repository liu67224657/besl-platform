package com.enjoyf.webapps.tools.weblogic.content;

import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.content.Content;
import com.enjoyf.platform.service.content.ContentField;
import com.enjoyf.platform.service.content.ImageContent;
import com.enjoyf.platform.service.content.ImageContentSet;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.ContentAuditStatus;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.ObjectField;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yujiaxiu
 * Date: 12-1-4
 * Time: 上午10:27
 * To change this template use File | Settings | File Templates.
 */
public class UnCheckStrategy extends StatusStrategy{
    private ContentWebLogic contentWebLogic;
    private String contentId;
    private String uno;
    private Integer originalValue;
    //是否是屏蔽操作
    private boolean filter;
    //
    private String[] selected;

    public UnCheckStrategy(ContentWebLogic contentWebLogic, String contentId, String uno, String[] selected, Integer originalValue) {
        super(contentWebLogic, contentId, uno,selected, originalValue);
        this.contentWebLogic = contentWebLogic;
        this.contentId = contentId;
        this.uno = uno;
        this.selected = selected;
        this.originalValue = originalValue;
    }

    @Override
    protected Map<ObjectField, Object> doModifyAuditColumn(Map<ObjectField, Object> map, boolean removable) {
        Map<ObjectField, Object> newMap = new HashMap<ObjectField, Object>();
        newMap.putAll(map);
        if(isAllValidStatusSame()){
            if(removable){
                newMap.put(ContentField.AUDITSTATUS, (originalValue + ContentAuditStatus.AUDIT_IMG + ContentAuditStatus.ILLEGAL_IMG));
            }else {
                newMap.put(ContentField.AUDITSTATUS, (originalValue + ContentAuditStatus.AUDIT_IMG));
            }
        }else {
            newMap.put(ContentField.AUDITSTATUS, (originalValue + ContentAuditStatus.AUDIT_IMG));
        }
        try {
            contentWebLogic.changeIMGStatus(contentId, uno ,newMap);
        } catch (ServiceException e) {
            GAlerter.lab("UnCheckStrategy method doSelectableOpt caught an Exception");
            e.printStackTrace();
        }
        return newMap;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }


}
