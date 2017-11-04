package com.enjoyf.platform.thirdapi.oauth;


import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.thirdapi.friends.FriendUrlParam;

import javax.servlet.http.HttpServletRequest;

/**
 * <p/>
 * Description:第三方接口服务
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public interface IOauthApi {

    /**
     * 得到认证地址
     * @return String
     * @throws ServiceException
     */
    public String getAuthorizeUrl(AuthUrlParam authParam) throws ServiceException;

    /**
     * 得到认证的token
     * @param authParam
     * @return SyncInfo
     * @throws ServiceException
     */
    public OAuthInfo getAccessToken(AuthParam authParam) throws ServiceException;

    /**
     * oauth2.0认证，在accesstoken过期后重新刷新token需要用到tokenInfo中的refreshtoken属性
     * @param tokenInfo
     * @return
     * @throws ServiceException
     */
    public TokenInfo refreshToken(TokenInfo tokenInfo) throws ServiceException;

     /**
     * 想第三方帐号同步文章时候调用
     * @param tokenInfo
     * @return
     * @throws ServiceException
     */
    public OAuthStatus syncContent(TokenInfo tokenInfo, SyncContent syncContent)  throws ServiceException;

    public AuthParam getAuthParamByRequest(HttpServletRequest request, String rurl, boolean requireLogin, String redirectType, String appKey) throws ServiceException;

    public void followJoyme(TokenInfo tokenInfo)  throws ServiceException;

    
     /**
     * 得到第三方好友列表
     * http://open.weibo.com/wiki/2/friendships/friends
     */
    public String getFriends(FriendUrlParam friendUrlParam) throws ServiceException;


    /**
     * 用户时的联想建议
     * http://open.weibo.com/wiki/2/search/suggestions/at_users
     */
    public String atUsers(FriendUrlParam friendUrlParam) throws ServiceException;

    /**
     * 获取新浪微博互粉好友
     *
     * @param friendUrlParam
     * @return
     * @throws ServiceException
     */
    public String getBilateral(FriendUrlParam friendUrlParam) throws ServiceException;

    /**
     * 获取用户信息
     * @param friendUrlParam
     * @return
     * @throws ServiceException
     */
    public String getShowUser(FriendUrlParam friendUrlParam)throws ServiceException;

}
