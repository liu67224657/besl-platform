package com.enjoyf.webapps.tools.weblogic.privilege;

import com.enjoyf.platform.service.tools.PrivilegeResource;
import com.enjoyf.webapps.tools.webpage.controller.SessionConstants;

import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Map;

/**
 * 控制页面根据action名称是否显示相应的按钮或者连接
 *
 * @author zx
 */
@SuppressWarnings("serial")
public class PrivilegeTag extends BodyTagSupport {
    private String name;

    public int doStartTag() {
        if (name == null || name.equals("")) {//如果页面传进来的name为null或者为空字符串。
            return SKIP_BODY;
        }
        Object obj = pageContext.getSession().getAttribute(SessionConstants.USER_SESSION_PRIVILEGE_ID);

        if (obj != null) {
            Map privilegeMapIsUser = (Map) obj;
            PrivilegeResource prs = CacheUtil.getSysRsByURL(name);
            if (prs != null && privilegeMapIsUser.get(prs.getRsid()) != null)
                return EVAL_BODY_BUFFERED;

        }
        return SKIP_BODY;
    }

    public int doEndTag() {
        try {
            if (bodyContent != null) {
                pageContext.getOut().write(bodyContent.getString().trim());
            }
            bodyContent = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
