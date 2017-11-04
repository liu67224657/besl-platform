package com.enjoyf.platform.thirdapi.oauth;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.serv.sync.SyncContent;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.AuthParam;
import com.enjoyf.platform.service.sync.SyncServiceException;
import com.enjoyf.platform.thirdapi.AuthUrlParam;
import com.enjoyf.platform.thirdapi.friends.FriendUrlParam;


/**
 * <p/>
 * Description:同步第三方方接口的模板类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public  abstract class AbstractOauthApi implements IOauthApi {
        protected ThirdApiHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class);

    /**
     * 得到微博验证的authorizeUrl跳转地址
     */
    public String getAuthorizeUrl(AuthUrlParam authUrlParam) throws ServiceException {
          throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_METHOD_SERVICE_EXCEPTION,"method :getAuthorizeUrl");
    }

    /**
     * 得到认证信息的接口
     * @param authParam
     * @return
     * @throws com.enjoyf.platform.service.service.ServiceException
     */
    public OAuthInfo getAccessToken(AuthParam authParam)  throws ServiceException{
          throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_METHOD_SERVICE_EXCEPTION,"method :authorizeToken");
    }

    /**
     * 得到测试接口
     * @return
     * @throws com.enjoyf.platform.service.service.ServiceException
     */
    @Override
    public TokenInfo refreshToken(TokenInfo tokenInfo) throws ServiceException {
        throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_METHOD_SERVICE_EXCEPTION,"method :refreshToken");
    }

    /**
     * 发文章测试接口
     * @param tokenInfo
     * @throws com.enjoyf.platform.service.service.ServiceException
     */
    @Override
    public OAuthStatus syncContent(TokenInfo tokenInfo,SyncContent syncContent)  throws ServiceException{
       throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_METHOD_SERVICE_EXCEPTION,"method :syncContent");
    }

    public void followJoyme(TokenInfo tokenInfo)  throws ServiceException{
       throw new ServiceException(SyncServiceException.SYNC_UNSUPPORT_METHOD_SERVICE_EXCEPTION,"method :followJoyme");
    }

    @Override
    public String getFriends(FriendUrlParam friendUrlParam) throws ServiceException {
       throw new ServiceException(SyncServiceException.SYNC_GETFRIENDS_EXCEPTION,"method :getFriends");
    }

    @Override
    public String atUsers(FriendUrlParam friendUrlParam) throws ServiceException {
        throw new ServiceException(SyncServiceException.SYNC_ATUSERS_EXCEPTION,"method :atUsers");
    }

    @Override
    public String getBilateral(FriendUrlParam friendUrlParam) throws ServiceException {
       throw new ServiceException(SyncServiceException.SYNC_ATUSERS_EXCEPTION,"method :getBilateral");
    }

    @Override
    public String getShowUser(FriendUrlParam friendUrlParam) throws ServiceException {
        throw new ServiceException(SyncServiceException.SYNC_ATUSERS_EXCEPTION,"method :getShowUser");
    }
}
