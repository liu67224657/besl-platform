package com.enjoyf.webapps.tools.webpage.controller.base;

import com.enjoyf.platform.cloudfile.BucketInfo;
import com.enjoyf.platform.cloudfile.FileHandlerFactory;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.account.TokenInfo;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.PrivilegeUser;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.service.usercenter.AuthProfile;
import com.enjoyf.platform.service.usercenter.LoginDomain;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.userservice.client.ApiException;
import com.enjoyf.platform.userservice.client.ApiResponse;
import com.enjoyf.platform.userservice.client.api.UserAccountResourceApi;
import com.enjoyf.platform.userservice.client.model.AccountDTO;
import com.enjoyf.platform.userservice.client.model.SocialAuthDTO;
import com.enjoyf.platform.util.HTTPUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.tools.weblogic.log.LogWebLogic;
import com.enjoyf.webapps.tools.weblogic.privilege.CacheUtil;
import com.enjoyf.webapps.tools.webpage.controller.SessionConstants;
import net.sf.json.JSONObject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: zhaoxin
 * Date: 11-10-27
 * Time: 上午9:25
 * Desc: controller基础类
 */
public class ToolsBaseController {

    @Resource(name = "logWebLogic")
    private LogWebLogic logWebLogic;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    protected static final String USER_UPLOAD_PATH = "/upload";
    protected static final String USER_UPLOAD_STATIC_PATH = "/static";

    protected String getStaticRealPath(HttpServletRequest request) {
        return HTTPUtil.getServerBaseUrl(request, USER_UPLOAD_STATIC_PATH);
    }

    protected String getUploadPath() {
        return WebappConfig.get().getUploadRootpath() + USER_UPLOAD_PATH;
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public HttpSession getSession() {
        return getRequest().getSession();
    }

    public PrivilegeUser getCurrentUser() {
        return (PrivilegeUser) getSession().getAttribute(SessionConstants.CURRENT_USER);
    }


    protected void writeToolsLog(LogOperType type, String description) {
        ToolsLog log = new ToolsLog();
        log.setOpUserId(getCurrentUser().getUserid());//用户的ID
        log.setOperType(type);//操作的类型
        log.setOpTime(new Date());//操作时间
        log.setOpIp(getIp());//用户IP
        log.setOpAfter(description); //描述 推荐用中文
        addLog(log);
    }

    public void addLog(ToolsLog log) {
        try {
            logWebLogic.saveLog(log);
        } catch (ServiceException e) {
            GAlerter.lab("ToolsBaseController addLog a log Exception :", e);
        }
    }

    protected Map<String, String> putErrorMap(String errorName, String errorContent) {
        Map<String, String> errorMsgMap = new HashMap<String, String>();
        errorMsgMap.put(errorName, errorContent);
        return errorMsgMap;
    }


    protected Map<String, Object> putErrorMessage(Map<String, Object> map, String error) {
        map.put("errorMsg", error);
        return map;
    }

    /**
     * 得到ip，（通过Nginx的ip需要从请求头部取）
     *
     * @return
     */
    protected String getIp() {
        return HTTPUtil.getRemoteAddr(getRequest());
    }

    protected String getQiniuToken() {
        BucketInfo bucketInfo = BucketInfo.getByCode(WebappConfig.get().getDefaultUploadBucket());
        String token = null;
        try {
            token = FileHandlerFactory.getToken(bucketInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }


}
