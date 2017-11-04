package com.enjoyf.webapps.joyme.webpage.filter;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.util.log.GAlerter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * Servlet Filter implementation class URLFilter
 */
public class URLFilter implements Filter {
    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;
        doURLFilter(req, resp, chain, response, request);
    }

    private void doURLFilter(ServletRequest req, ServletResponse resp, FilterChain chain, HttpServletResponse response, HttpServletRequest request)
            throws IOException, ServletException {
        String requestURL = request.getRequestURL().toString();
        URL url = new URL(requestURL);
        //sitemap
        if (url.getPath().endsWith("/sitemap.xml") || url.getPath().endsWith("/sitemap.txt")) {
            String path = "";
            if (requestURL.contains("gift") || requestURL.contains("collection")) {
                path = WebappConfig.get().getSitemapFolder() + "/" + url.getHost() + "/" + url.getPath().substring(0, url.getPath().lastIndexOf("/"));
            } else {
                path = WebappConfig.get().getSitemapFolder() + url.getPath().substring(0, url.getPath().lastIndexOf("/"));
            }

            File fileXml = new File(path + "/sitemap.xml");
            if (fileXml.exists()) {
                displayFileXML(response, fileXml);
            }
            return;
        } else {
            chain.doFilter(req, resp);
        }
    }

    private void displayFileXML(HttpServletResponse response, File file) throws IOException, FileNotFoundException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/xml");
        FileInputStream is = new FileInputStream(file);

        OutputStream os = null;
        try {
            os = response.getOutputStream();
            byte[] buff = new byte[8192];
            int len = -1;
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }

        } finally {
            if (is != null) {
                is.close();
                is = null;
            }

            if (os != null) {
                os.flush();
                os.close();
                os = null;
            }
        }
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }
}
