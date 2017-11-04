package com.enjoyf.webapps.joyme.weblogic.youku;

import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.gamedb.*;
import com.enjoyf.platform.service.joymeapp.AppPlatform;
import com.enjoyf.platform.service.point.pointwall.PointwallWallApp;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.joyme.dto.youku.YoukuAppDTO;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/19
 * Description:
 */
@Service(value = "youkuGameDbWebLogic")
public class YoukuGameWebLogic {


    public List<YoukuAppDTO> queryYoukuGameDTO(List<PointwallWallApp> apps) throws ServiceException {
        List<YoukuAppDTO> youkuAppDTOs = new ArrayList<YoukuAppDTO>();
        for (PointwallWallApp app : apps) {
            YoukuAppDTO youkuAppDTO = builAppDTO(app);
            if (youkuAppDTO != null) {
                youkuAppDTOs.add(youkuAppDTO);
            }
        }

        return youkuAppDTOs;
    }


    /**
     * @Param app
     * @return
     * @throws ServiceException
     */
    public YoukuAppDTO builAppDTO(PointwallWallApp app) throws ServiceException {
        YoukuAppDTO gameDTO = new YoukuAppDTO();

        gameDTO.setGameid(app.getAppId());
        gameDTO.setLogo(StringUtil.isEmpty(app.getAppIcon()) ? "" : app.getAppIcon());
        gameDTO.setAppname(StringUtil.isEmpty(app.getAppName()) ? "" : app.getAppName());
        gameDTO.setDesc(app.getAppDesc());
        gameDTO.setRec_words((StringUtil.isEmpty(app.getAppDesc()) ? "" : app.getAppDesc()));

        gameDTO.setUrl(app.getDownloadUrl());
        gameDTO.setItunesid("");

        gameDTO.setScore(app.getScore());
        gameDTO.setPlatform(1);
        gameDTO.setSize(app.getSize());
        gameDTO.setRec_words(app.getRecommend());


        return gameDTO;
    }

}
