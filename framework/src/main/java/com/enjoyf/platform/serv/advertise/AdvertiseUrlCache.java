/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.serv.advertise;

import com.enjoyf.platform.service.advertise.AdvertiseAppUrl;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.service.content.ContentConstants;
import com.enjoyf.platform.service.content.ForignContent;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.memcached.MemCachedManager;

import java.util.List;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-9-25 下午5:18
 * Description:
 */
class AdvertiseUrlCache {
    private static final long TIME_OUT_SEC = 60l * 60l * 6l;

      private static final String PREFIX_ADVERTISE_APPURL = "advertise_appurl_";


      private MemCachedConfig config;

      private MemCachedManager manager;

    AdvertiseUrlCache(MemCachedConfig config) {
          this.config = config;
          manager = new MemCachedManager(this.config);
      }

      //////////////////////////////////////////////////////////
      public void putAppUrl(AdvertiseAppUrl advertiseAppUrl) {
          manager.put(PREFIX_ADVERTISE_APPURL + advertiseAppUrl.getCode(), advertiseAppUrl, TIME_OUT_SEC);
      }

      public AdvertiseAppUrl getAppUrl(String code) {
          Object advertiseAppUrl = manager.get(PREFIX_ADVERTISE_APPURL + code);
          if (advertiseAppUrl == null) {
              return null;
          }
          return (AdvertiseAppUrl) advertiseAppUrl;
      }

      public boolean deleteAppUrl(String code) {
          return manager.remove(PREFIX_ADVERTISE_APPURL + code);
      }

}
