package com.enjoyf.platform.service.profile;

import com.enjoyf.platform.service.JsonBinder;
import com.enjoyf.platform.service.content.ContentPublishType;
import com.enjoyf.platform.service.social.RelationType;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.google.common.base.Strings;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.Serializable;

/**
 * Author: zhaoxin
 * Date: 11-10-19
 * Time: 下午6:33
 * Desc:
 */
public class ProfileSettingAt implements Serializable {

     private boolean isNotice = true;//是否需要通知
     private String authorRelation = RelationType.ALL.getCode(); //作者关系类型 all fans focus
     private String contentType = ContentPublishType.ALL.getCode();  //文章类型是否转贴 org fwd

    public ProfileSettingAt(){

    }

    public boolean getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(boolean notice) {
        isNotice = notice;
    }

    public String getAuthorRelation() {
        return authorRelation;
    }

    public void setAuthorRelation(String authorRelation) {
        this.authorRelation = authorRelation;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * to json
     */
    public String toJson() {
        return JsonBinder.buildNormalBinder().toJson(this);
    }

    public static ProfileSettingAt parse(String jsonStr){
        ProfileSettingAt returnValue = new ProfileSettingAt();
        returnValue.setIsNotice(true);
        returnValue.setAuthorRelation(RelationType.ALL.getCode());
        returnValue.setContentType(ContentPublishType.ALL.getCode());
        if (!Strings.isNullOrEmpty(jsonStr)) {
            try {
                returnValue = JsonBinder.buildNormalBinder().getMapper().readValue(jsonStr, new TypeReference<ProfileSettingAt>() {
                });
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
