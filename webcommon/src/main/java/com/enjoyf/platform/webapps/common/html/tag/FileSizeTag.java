package com.enjoyf.platform.webapps.common.html.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.enjoyf.platform.util.Utility;
import com.enjoyf.platform.util.log.GAlerter;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

@SuppressWarnings("serial")
public class FileSizeTag extends TagSupport {

    private String size;

    @Override
    public int doStartTag() throws JspException {

        try {
            JspWriter out = pageContext.getOut();
            Long s = (Long) ExpressionEvaluatorManager.evaluate(
                    "size", this.size, Long.class,
                    this, super.pageContext);

            out.println(Utility.getReadableFileSize(s));
        } catch (IOException e) {
            GAlerter.lan("Cannot output file size in tag.", e);
        }
        return SKIP_BODY;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}