package com.enjoyf.platform.webapps.common.html.action;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author <a href=mailto:yinpy@platform.com>Yin Pengyi</a>
 *         this is a action like struts1.3 's ForwardAction
 */
@SuppressWarnings("serial")
public class ForwardAction extends ActionSupport {

    @Override
    public String execute() {
        return SUCCESS;
    }

}
