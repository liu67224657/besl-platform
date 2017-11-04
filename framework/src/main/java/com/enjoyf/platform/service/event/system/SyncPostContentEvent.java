/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.system;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.usercenter.ProfileFlag;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">Eric Liu</a>
 * Create time: 11-8-25 下午8:15
 * Description: 往第三方接口同步博文
 */
public class SyncPostContentEvent extends SystemEvent {
    //
    private String contentUno;

    private String contentId;

    private String relationId;
    private String relationUno;

//    private Set<AccountDomain> syncProviderSet=new HashSet<AccountDomain>();
    private ProfileFlag profileFlag;

    public SyncPostContentEvent() {
        super(SystemEventType.SYNC_POST_CONTENT);
    }

    public String getContentUno() {
        return contentUno;
    }

    public void setContentUno(String contentUno) {
        this.contentUno = contentUno;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationUno() {
        return relationUno;
    }

    public void setRelationUno(String relationUno) {
        this.relationUno = relationUno;
    }

//    public Set<AccountDomain> getSyncDomainSet() {
//        return syncProviderSet;
//    }
//
//    public void setSyncProviderSet(Set<AccountDomain> syncProviderSet) {
//        this.syncProviderSet = syncProviderSet;
//    }


    public ProfileFlag getProfileFlag() {
        return profileFlag;
    }

    public void setProfileFlag(ProfileFlag profileFlag) {
        this.profileFlag = profileFlag;
    }

    //
    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

    @Override
    public int hashCode() {
        return contentUno.hashCode();
    }
}
