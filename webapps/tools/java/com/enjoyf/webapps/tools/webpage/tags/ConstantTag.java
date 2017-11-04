/**
 * @author xinzhao
 * @d2011-6-18
 */
package com.enjoyf.webapps.tools.webpage.tags;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.webapps.common.resdomain.WebResourceServerMonitor;
import com.enjoyf.webapps.tools.webpage.controller.Constant;
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
    public String URL_TOOLS = WebappConfig.get().getUrlTools();
    public String URL_WWW = WebappConfig.get().getUrlWww();
    public String URL_LIB = WebappConfig.get().getUrlLib();
    public String URL_STATIC = WebappConfig.get().getUrlStatic();
    public String URL_M = WebappConfig.get().getUrlM();
    public String DOMAIN = WebappConfig.get().getDomain();
    public String uploaddomain = "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.R_DOMAIN;
    public String shutDownRDomain = WebResourceServerMonitor.get().getDownResourceDomainJson();



    public static String getRandUploadDomain() {
        return "http://" + ResourceServerMonitor.get().getRandomUploadDomainPrefix() + "." + Constant.R_DOMAIN;
    }

    public static String getDownResourceDomainsJson() {
        return WebResourceServerMonitor.get().getDownResourceDomainJson();
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

    public String getURL_TOOLS() {
        return URL_TOOLS;
    }

    public void setURL_TOOLS(String URL_TOOLS) {
        this.URL_TOOLS = URL_TOOLS;
    }

    public String getURL_WWW() {
        return URL_WWW;
    }

    public void setURL_WWW(String URL_WWW) {
        this.URL_WWW = URL_WWW;
    }

    public String getURL_LIB() {
        return URL_LIB;
    }

    public void setURL_LIB(String URL_LIB) {
        this.URL_LIB = URL_LIB;
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

    public void setDOMAIN(String DOMAIN) {
        this.DOMAIN = DOMAIN;
    }

    public String getUploaddomain() {
        return uploaddomain;
    }

    public void setUploaddomain(String uploaddomain) {
        this.uploaddomain = uploaddomain;
    }

    public String getShutDownRDomain() {
        return shutDownRDomain;
    }

    public void setShutDownRDomain(String shutDownRDomain) {
        this.shutDownRDomain = shutDownRDomain;
    }
}
