package com.enjoyf.webapps.joyme.weblogic.advertise;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.advertise.AdvertiseServiceSngl;
import com.enjoyf.platform.service.advertise.app.AppAdvertise;
import com.enjoyf.platform.service.advertise.app.AppAdvertiseInfo;
import com.enjoyf.platform.service.advertise.app.AppAdvertisePublishType;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.webapps.joyme.dto.joymeapp.advertise.AdvertiseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  2014/6/10 11:42
 * Description:
 */
@Service(value = "appAdvertiseWebLogic")
public class AppAdvertiseWebLogic {

    private static final String URL = "httpL//api." + WebappConfig.get().getDomain() + "/joymeapp/app/advertise/click";

    public List<AdvertiseDTO> queryAdvertise(String appKey, AppAdvertisePublishType type, Date now) throws ServiceException {

        List<AdvertiseDTO> returnList = new ArrayList<AdvertiseDTO>();

        List<AppAdvertiseInfo> appAdvertiseList = AdvertiseServiceSngl.get().queryPublishingAdvertise(appKey, type, now);

        for (AppAdvertiseInfo info : appAdvertiseList) {
            AdvertiseDTO dto = new AdvertiseDTO();
            dto.setName(info.getAppAdvertise().getAdvertiseName());
            dto.setDesc(info.getAppAdvertise().getAdvertiseDesc());
            dto.setPic_url(info.getAppAdvertise().getPicUrl1());
            dto.setPic_url2(info.getAppAdvertise().getPicUrl2());
            dto.setEnd_time(info.getEndDate().getTime());
            dto.setStart_time(info.getStartDate().getTime());
            dto.setUrl(URL + "/" + info.getPublishId() + "/" + info.getAppAdvertise().getAdvertiseId());
        }

        return returnList;
    }

    public String getReidrectUrl(long advertiseId) throws ServiceException {
        AppAdvertise appAdvertise = AdvertiseServiceSngl.get().getAppAdvertise(advertiseId);

        if (appAdvertise == null) {
            return null;
        }
        return appAdvertise.getUrl();
    }

}
