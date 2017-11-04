package com.enjoyf.webapps.joyme.weblogic.discovery;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.content.ContentServiceSngl;
import com.enjoyf.platform.service.content.wall.WallBlock;
import com.enjoyf.platform.service.content.wall.WallLayout;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.webapps.common.html.tag.ImageURLTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a> ,zx
 */
@Service(value = "discoveryWebLogic")
public class DiscoveryWebLogic {
    private Logger logger = LoggerFactory.getLogger(DiscoveryWebLogic.class);

    private String DEFAULT_TEMPLATE_NAME = "default";

    @Resource(name = "wallLayoutEngine")
    private WallLayoutEngine wallLayoutEngine;



    public List<WallLayoutDTO> queryWallLayouts(int idx) throws ServiceException {
        List<WallLayoutDTO> list = new ArrayList<WallLayoutDTO>();
        Map<Integer, WallLayout> wallLayoutMap = ContentServiceSngl.get().queryWallLayoutMap(idx);

        for (int index : wallLayoutMap.keySet()) {
            WallLayout wallLayout = wallLayoutMap.get(index);
            WallLayoutDTO layoutDTO = new WallLayoutDTO();
            layoutDTO.setIdx(index);
            layoutDTO.setLayoutType(wallLayout.getLayoutType());
            List<WallBlockDTO> blockDTOList = new ArrayList<WallBlockDTO>();
            for (WallBlock block : wallLayout.getWallBlockList()) {
                String templateName = WebappConfig.get().getWallBlockTemplatePath() + block.getWallContentType().getCode() + "/"
                        + block.getWallBlockType().getCode() + "/" + block.getDiscoveryWallContent().getWallContentRule().getCode() + "/"
                        + block.getStyle() + ".ftl";

                Map<String, Object> map = new HashMap<String, Object>();

                if (!StringUtil.isEmpty(block.getDiscoveryWallContent().getThumbImgLink())) {
                    block.getDiscoveryWallContent().setThumbImgLink(ImageURLTag.parseWallThumbImgLink(block.getDiscoveryWallContent().getThumbImgLink()));
                }

                block.getDiscoveryWallContent().setDateStr(DateUtil.parseDate(block.getDiscoveryWallContent().getPublishDate()));
                block.getDiscoveryWallContent().setScreenName(parseNickName(block.getDiscoveryWallContent().getScreenName()));
                map.put("block", block);
                map.put(WebappConfig.KEY_URL_WWW, WebappConfig.get().getUrlWww());
                map.put(WebappConfig.KEY_DOMAIN,WebappConfig.get().getDomain());
                String blockHTML = wallLayoutEngine.generateWallBlockHtml(templateName, map);
                WallBlockDTO wallBlockDTO = new WallBlockDTO();

                wallBlockDTO.setWallBlockType(block.getWallBlockType());
                wallBlockDTO.setWallContentType(block.getWallContentType());
                wallBlockDTO.setStyleConfig(block.getStyleConfig());
                blockDTOList.add(wallBlockDTO);
                if (blockHTML != null) {
                    wallBlockDTO.setHtml(blockHTML);
                } else {
                    templateName = WebappConfig.get().getWallBlockTemplatePath() + DEFAULT_TEMPLATE_NAME + "/" + block.getWallBlockType().getCode() + ".ftl";
                    blockHTML = wallLayoutEngine.generateWallBlockHtml(templateName, map);
                    wallBlockDTO.setHtml(blockHTML);
                }

            }
            layoutDTO.setWallBlockDTOList(blockDTOList);
            list.add(layoutDTO);
        }

        return list;

    }
    
    private String parseNickName(String nick){
        nick = StringUtil.length(nick) <= 10 ? nick : StringUtil.subString(nick, 10) + "...";
        return nick;
    }
}
