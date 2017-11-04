/**
 * CopyRight 2012 joyme.com
 */
package com.enjoyf.webapps.joyme.webpage.base.mvc;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.webapps.common.util.WebUtil;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Author: zhaoxin
 * Date: 12-3-5
 * Time: 下午2:44
 * Desc:
 */
public class EhCacheManagerFactoryBeanImpl extends EhCacheManagerFactoryBean {

    public void setConfigLocation(Resource configLocation) {

        FiveProps servProps = Props.instance().getServProps();

        if(servProps != null){
            String webappsName = servProps.get("webapps.NAME");
            String ehcacheDir = servProps.get("ehcache.disk.store.dir");
            System.setProperty("ehcache.disk.store.dir", ehcacheDir + "/" + webappsName);
        }
        super.setConfigLocation(configLocation);
    }
}
