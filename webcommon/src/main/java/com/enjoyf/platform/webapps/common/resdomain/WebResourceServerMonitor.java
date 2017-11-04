package com.enjoyf.platform.webapps.common.resdomain;

import com.enjoyf.platform.props.hotdeploy.ResourceDomainHotdeployConfig;
import com.enjoyf.platform.service.naming.ResourceServerMonitor;
import com.enjoyf.platform.webapps.common.encode.JsonBinder;



/**
 * Resource server monitor
 */
public class WebResourceServerMonitor {

    //
    private static volatile WebResourceServerMonitor instance;

    private ResourceDomainHotdeployConfig config;


    public static WebResourceServerMonitor get() {
        if (instance == null) {
            synchronized (WebResourceServerMonitor.class) {
                instance = new WebResourceServerMonitor();
            }
        }

        return instance;
    }

    private WebResourceServerMonitor() {}


    public String getDownResourceDomainJson() {
        return JsonBinder.buildNonNullBinder().toJson(ResourceServerMonitor.get().getDownResourceDomainMap().values());
    }


}
