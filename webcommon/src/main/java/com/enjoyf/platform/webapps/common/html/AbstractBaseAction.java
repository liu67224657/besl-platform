package com.enjoyf.platform.webapps.common.html;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.enjoyf.platform.util.HTTPUtil;
import org.apache.struts2.ServletActionContext;

import com.google.common.base.Strings;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
@SuppressWarnings("serial") //Struts2 sucks, ActionSupport implements Serializable???
public abstract class AbstractBaseAction extends ActionSupport {

    protected Map<String, String> formErrorsMap = new LinkedHashMap<String, String>();

    public Collection<String> getAllFormErrors() {
        return formErrorsMap.values();
    }

    /**
     * Context relations
     */
    public ActionContext getActionContext() {
        return ActionContext.getContext();
    }

    public ServletContext getServletContext() {
        return ServletActionContext.getServletContext();
    }

    //request
    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    public void setRequestAttr(String key, Object value) {
        this.getRequest().setAttribute(key, value);
    }

    public void removeRequestAttr(String key) {
        this.getRequest().removeAttribute(key);
    }

    public Object getRequestAttr(String key) {
        return this.getRequest().getAttribute(key);
    }

    public String getRequestParam(String key) {
        return this.getRequest().getParameter(key);
    }

    //response
    public HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    //cookie
    public Cookie[] getCookie() {
        return getRequest().getCookies();
    }

    public void setCookie(Cookie c) {
        HTTPUtil.setCookie(getResponse(), c);
    }

    public void setCookie(String name, String value) {
        HTTPUtil.setCookie(getResponse(), new Cookie(name, value));
    }

    public void removeCookie(Cookie c) {
        HTTPUtil.removeCookie(this.getResponse(), c);
    }

    public String getCookieValue(String key) {
        return HTTPUtil.getCookieValue(getRequest(), key);
    }

    //session
    public Map<String, Object> getSessionMap() {
        return getActionContext().getSession();
    }

    public HttpSession getSession() {
        return getRequest().getSession();
    }
    
    public HttpSession getSession(boolean b) {
        return getRequest().getSession(b);
    }

    public void setSessionValue(String key, Object value) {
        getSessionMap().put(key, value);
    }

    public void removeSessionValue(String key) {
        this.getSessionMap().remove(key);
    }

    public Object getSessionValue(String key) {
        return this.getSessionMap().get(key);
    }

    public Map<String, String> getFormErrorsMap() {
        return formErrorsMap;
    }

    public String getRequestRemoteAddr() {
        return HTTPUtil.getRemoteAddr(this.getRequest());
    }

    public String getRequestedUrl() {
        StringBuffer urlBuf = new StringBuffer(16);
        HttpServletRequest request = this.getRequest();
        urlBuf.append(request.getRequestURL());
        if (!Strings.isNullOrEmpty(request.getQueryString())) {
            urlBuf.append("?").append(request.getQueryString());
        }
        return urlBuf.toString();
    }

    public String getRequestedServerHost() {
        StringBuffer stb = new StringBuffer();
        stb.append(getRequest().getServerName());
        if (getRequest().getServerPort() != 80) {
            stb.append(":").append(getRequest().getServerPort());
        }
        return stb.toString();
    }
}
