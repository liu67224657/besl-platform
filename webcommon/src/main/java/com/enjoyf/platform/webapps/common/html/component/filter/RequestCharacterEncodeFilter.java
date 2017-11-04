/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.webapps.common.html.component.filter;

import com.enjoyf.platform.util.StringUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * the filter for request character encode
 */
public class RequestCharacterEncodeFilter implements Filter {
    private FilterConfig config = null;
    private String encode = null;

    private static final String PARAM_ENCODE_KEY = "encode";
    private static final String DEFAULT_ENCODE = "utf-8";

    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        encode = filterConfig.getInitParameter(PARAM_ENCODE_KEY);

        if (StringUtil.isEmpty(encode)) {
            encode = DEFAULT_ENCODE;
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(encode);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        config = null;
    }
}
