package com.enjoyf.webapps.joyme.weblogic.sync;

import com.enjoyf.platform.service.account.AccountDomain;
import com.enjoyf.platform.service.profile.Profile;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.ThirdApiProps;
import com.enjoyf.platform.thirdapi.oauth.IOauthApi;
import com.enjoyf.platform.thirdapi.oauth.OAuthInfo;
import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.webapps.joyme.webpage.base.mvc.UserSession;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-1-7
 * Time: 下午4:22
 * To change this template use File | Settings | File Templates.
 */
public class OAuthInfoDomain implements Serializable{

    ////////////////////
    private Map<String,String> map;

    private AccountDomain accountDomain;
    
    private ThirdApiProps thirdApiProps;

    private String returnUrl = "";
    
    private String authParamUrl = "";
    
    private UserSession userSession;

    private IOauthApi oauthInterface;

    private AuthParam authParam;

    private OAuthInfo oAuthInfo;


    private Profile profile;
    /////////////////////

    ///////////accountThirdIsNull////////////
    private boolean  currentSessionIsNull;
    private boolean ignorCheckNameIsNull;
    private Map<String, Object> mapMessage;
    //////////////////



    //////accountThirdIsNotNull/////
    private int  returnValue;
    ////////////


    public int getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    public boolean isIgnorCheckNameIsNull() {
        return ignorCheckNameIsNull;
    }

    public void setIgnorCheckNameIsNull(boolean ignorCheckNameIsNull) {
        this.ignorCheckNameIsNull = ignorCheckNameIsNull;
    }

    public Map<String, Object> getMapMessage() {
        return mapMessage;
    }

    public void setMapMessage(Map<String, Object> mapMessage) {
        this.mapMessage = mapMessage;
    }

    public boolean isCurrentSessionIsNull() {
        return currentSessionIsNull;
    }

    public void setCurrentSessionIsNull(boolean currentSessionIsNull) {
        this.currentSessionIsNull = currentSessionIsNull;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public AccountDomain getAccountDomain() {
        return accountDomain;
    }

    public void setAccountDomain(AccountDomain accountDomain) {
        this.accountDomain = accountDomain;
    }

    public ThirdApiProps getThirdApiProps() {
        return thirdApiProps;
    }

    public void setThirdApiProps(ThirdApiProps thirdApiProps) {
        this.thirdApiProps = thirdApiProps;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAuthParamUrl() {
        return authParamUrl;
    }

    public void setAuthParamUrl(String authParamUrl) {
        this.authParamUrl = authParamUrl;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public IOauthApi getOauthInterface() {
        return oauthInterface;
    }

    public void setOauthInterface(IOauthApi oauthInterface) {
        this.oauthInterface = oauthInterface;
    }

    public AuthParam getAuthParam() {
        return authParam;
    }

    public void setAuthParam(AuthParam authParam) {
        this.authParam = authParam;
    }

    public OAuthInfo getoAuthInfo() {
        return oAuthInfo;
    }

    public void setoAuthInfo(OAuthInfo oAuthInfo) {
        this.oAuthInfo = oAuthInfo;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
