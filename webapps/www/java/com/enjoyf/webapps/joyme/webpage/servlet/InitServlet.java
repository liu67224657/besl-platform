package com.enjoyf.webapps.joyme.webpage.servlet;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.webapps.joyme.webpage.quartz.SiteMapQuartzCronTrigger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-8-28 下午5:07
 * Description:
 */
public class InitServlet extends HttpServlet {
    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws javax.servlet.ServletException if an error occurs
     */
    public void init(ServletConfig config) throws ServletException {
//        try {
//            IndexHtmlCache.get();
//        } catch (Exception e) {
//            GAlerter.lab(this.getClass().getName() + " occured ServiceException.e:", e);
//        }

        try {
            SiteMapQuartzCronTrigger siteMapQuartzCronTrigger = new SiteMapQuartzCronTrigger();
            siteMapQuartzCronTrigger.init();
            siteMapQuartzCronTrigger.start();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured SchedulerException.e:", e);
        }
    }
}
