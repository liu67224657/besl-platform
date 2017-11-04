package com.enjoyf.webapps.joyme.webpage.controller.wechat;

import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.*;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.joyme.dto.platinum.PlatinumDTO;
import com.enjoyf.webapps.joyme.weblogic.joymeapp.client.ClientWebLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-6-26
 * Time: 上午9:46
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/wechat/bbs/ac")
public class WechatBBSActivityController extends AbstractWechatController {

    @RequestMapping
    public ModelAndView bbsAc(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            //顶部轮播
            ClientLine topLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.WECHAT_BBSAC.getCode()))
                    .add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.WECHAT_BBSAC_TOP.getCode()))
                    .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
            if (topLine == null) {
                return new ModelAndView("");
            }
            List<ClientLineItem> topList = JoymeAppServiceSngl.get().queryClientLineItemList(topLine.getCode());
            if (CollectionUtil.isEmpty(topList)) {
                return new ModelAndView("");
            }
            mapMessage.put("topList", topList);
            //列表
            ClientLine listLine = JoymeAppServiceSngl.get().getClientLine(new QueryExpress()
                    .add(QueryCriterions.eq(ClientLineField.LINE_TYPE, ClientLineType.WECHAT_BBSAC.getCode()))
                    .add(QueryCriterions.eq(ClientLineField.ITEM_TYPE, ClientItemType.WECHAT_BBSAC_LIST.getCode()))
                    .add(QueryCriterions.eq(ClientLineField.VALID_STATUS, ValidStatus.VALID.getCode())));
            if (listLine == null) {
                return new ModelAndView("");
            }
            List<ClientLineItem> itemList = JoymeAppServiceSngl.get().queryClientLineItemList(listLine.getCode());
            if (CollectionUtil.isEmpty(itemList)) {
                return new ModelAndView("");
            }
            mapMessage.put("itemList", itemList);
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured Excpetion.e:", e);
            return new ModelAndView("");
        }
        return new ModelAndView("/views/jsp/wechat/wechat-bbsac", mapMessage);
    }

}
