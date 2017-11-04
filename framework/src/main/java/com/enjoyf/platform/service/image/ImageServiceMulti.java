/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.image;

import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.ResourceDomainHotdeployConfig;
import com.enjoyf.platform.service.service.ServiceConfigNaming;
import com.enjoyf.platform.service.service.ServiceConfigNamingFactory;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.ResourceFilePathUtil;
import com.enjoyf.platform.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 12-1-12 下午6:14
 * Description:
 */
public class ImageServiceMulti implements ImageService {
    //
    private Map<String, ImageService> services = new HashMap<String, ImageService>();

    //
    private ResourceDomainHotdeployConfig config = HotdeployConfigFactory.get().getConfig(ResourceDomainHotdeployConfig.class);

    //
    public ImageServiceMulti() {
        for (int i = 1; i <= config.getTotalNum(); i++) {
            synchronized (services) {
                String serviceType = config.getDomainPrefixServiceType() + StringUtil.appendZore(i, config.getNumLength());
                ImageService service = createService(serviceType, serviceType);

                services.put(serviceType, service);
            }
        }
    }

    @Override
    public boolean removeResourceFile(String fileId) throws ServiceException {
        return this.getService(fileId).removeResourceFile(fileId);
    }

    private ImageService getService(String fileId) {
        String serverName = ResourceFilePathUtil.getServerNoByFilePath(fileId);
        String serverNoStr = serverName.substring(serverName.length() - config.getNumLength());
        int serverNo = Integer.valueOf(serverNoStr);

        String serviceType = config.getDomainPrefixServiceType() + StringUtil.appendZore(serverNo, config.getNumLength());
        ImageService returnValue = null;

        //
        synchronized (services) {
            returnValue = services.get(serviceType);

            if (returnValue == null) {
                returnValue = createService(serviceType, serviceType);

                services.put(serviceType, returnValue);
            }
        }

        return returnValue;
    }

    private ImageService createService(String serviceType, String serviceSection) {
        ImageService returnValue;

        ServiceConfigNaming cfg = ServiceConfigNamingFactory.getDefaultServiceCfgNaming(serviceSection, serviceType);

        returnValue = new ImageServiceImpl(cfg);

        return returnValue;
    }
}
