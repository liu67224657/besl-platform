/**
 * @author xinzhao
 * @d2011-6-18
 */
package com.enjoyf.webapps.joyme.webpage.util;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ThirdApiHotdeployConfig;
import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.webapps.common.resdomain.WebResourceServerMonitor;
import com.enjoyf.webapps.joyme.webpage.cache.SysCommCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xinzhao
 */
public class ConstantTag extends TagSupport {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    static Logger logger = LoggerFactory.getLogger(ConstantTag.class);
    public String clazz = ConstantTag.class.getName();
    protected String scope = null;
    protected String var = null;

    // 三个常量域名
    public String URL_WWW = WebappConfig.get().getUrlWww();
    public String URL_LIB = WebappConfig.get().getUrlLib();
    public String URL_STATIC = WebappConfig.get().getUrlStatic();
    public String URL_M = WebappConfig.get().getUrlM();
    public String DOMAIN = WebappConfig.get().getDomain();
    public String YK_DOMAIN = WebappConfig.get().getUrlYouku();


    public String UrlUpload = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.DOMAIN;
    public String shutDownRDomain = WebResourceServerMonitor.get().getDownResourceDomainJson();

    public String URL_UC = WebappConfig.get().getUrlUc();

    /**
     * 通过ID取城市名称
     */
    public static String regionById(int id) {
        if (logger.isDebugEnabled()) {
            logger.debug("cityId：" + id);
        }
        String sReStr = null;
        try {
            sReStr = SysCommCache.get().findRegionById(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sReStr;
    }

    public static String getRandUploadDomain() {
        return "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.DOMAIN;
    }

    public static String getDownResourceDomainsJson() {
        return WebResourceServerMonitor.get().getDownResourceDomainJson();
    }

    public static boolean openSyncProvider() {
        return HotdeployConfigFactory.get().getConfig(ThirdApiHotdeployConfig.class).getProviderMap().size() > 0;
    }

    public int doStartTag() throws JspException {
        Class c = null;
        int toScope = PageContext.PAGE_SCOPE;
        if (scope != null) {
            toScope = getScope(scope);
        }
        try {
            c = Class.forName(clazz);
        } catch (ClassNotFoundException cnf) {
            logger.error("ClassNotFound - maybe a typo?");
            throw new JspException(cnf.getMessage());
        }

        try {
            if (var == null || var.length() == 0) {
                throw new JspException("常量参数var必须填写！");
            } else {
                try {
                    Object value = c.getField(var).get(this);
                    pageContext.setAttribute(c.getField(var).getName(), value, toScope);
                } catch (NoSuchFieldException nsf) {
                    logger.error(nsf.getMessage());
                    throw new JspException(nsf);
                }
            }
        } catch (IllegalAccessException iae) {
            logger.error("Illegal Access Exception - maybe a classloader issue?");
            throw new JspException(iae);
        }
        return (SKIP_BODY);
    }

    /**
     * @jsp.attribute
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return (this.scope);
    }

    /**
     * @jsp.attribute
     */
    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return (this.var);
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        clazz = null;
        scope = ConstantTag.class.getName();
    }

    private static final Map scopes = new HashMap();

    static {
        scopes.put("page", new Integer(PageContext.PAGE_SCOPE));
        scopes.put("request", new Integer(PageContext.REQUEST_SCOPE));
        scopes.put("session", new Integer(PageContext.SESSION_SCOPE));
        scopes.put("application", new Integer(PageContext.APPLICATION_SCOPE));
    }

    public int getScope(String scopeName) throws JspException {
        Integer scope = (Integer) scopes.get(scopeName.toLowerCase());

        if (scope == null) {
            throw new JspException("Scope '" + scopeName
                    + "' not a valid option");
        }

        return scope.intValue();
    }


    public String getURL_WWW() {
        return URL_WWW;
    }

    public void setURL_WWW(String urlWww) {
        this.URL_WWW = urlWww;
    }

    public String getURL_LIB() {
        return URL_LIB;
    }

    public void setURL_LIB(String urlLib) {
        this.URL_LIB = urlLib;
    }

    public String getURL_STATIC() {
        return URL_STATIC;
    }

    public void setURL_STATIC(String URL_STATIC) {
        this.URL_STATIC = URL_STATIC;
    }

    public String getURL_M() {
        return URL_M;
    }

    public void setURL_M(String URL_M) {
        this.URL_M = URL_M;
    }

    public String getDOMAIN() {
        return DOMAIN;
    }

    public void setDOMAIN(String domain) {
        this.DOMAIN = domain;
    }

    public String getUrlUpload() {
        return UrlUpload;
    }

    public void setUrlUpload(String urlUpload) {
        this.UrlUpload = urlUpload;
    }

    public String getYK_DOMAIN() {
        return YK_DOMAIN;
    }

    public void setYK_DOMAIN(String YK_DOMAIN) {
        this.YK_DOMAIN = YK_DOMAIN;
    }

    public String getShutDownRDomain() {
        return shutDownRDomain;
    }

    public void setShutDownRDomain(String shutDownRDomain) {
        this.shutDownRDomain = shutDownRDomain;
    }


    public String getURL_UC() {
        return URL_UC;
    }

    public void setURL_UC(String URL_UC) {
        this.URL_UC = URL_UC;
    }
}
