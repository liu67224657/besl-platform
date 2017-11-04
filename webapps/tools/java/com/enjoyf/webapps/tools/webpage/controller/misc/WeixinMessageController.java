package com.enjoyf.webapps.tools.webpage.controller.misc;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.misc.InterFlowAccount;
import com.enjoyf.platform.service.misc.InterFlowAccountField;
import com.enjoyf.platform.service.misc.InterFlowType;
import com.enjoyf.platform.service.misc.MiscServiceSngl;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.platform.webapps.common.dto.weixin.WeixinTextDTO;
import com.enjoyf.util.StringUtil;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-10-28
 * Time: 下午12:18
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/misc/weixin/message")
public class WeixinMessageController extends ToolsBaseController {

    private static RedisManager redisManager = new RedisManager(WebappConfig.get().getProps());
    private static final String KEY_CACHE = "weixin_user_messages";

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "100") int pageSize,
                             @RequestParam(value = "starttime", required = false) String startTime,
                             @RequestParam(value = "endtime", required = false) String endTime
    ) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {

            SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = null;
            Date endDate = null;

            if (StringUtil.isEmpty(startTime)) {
            } else {
                startDate = df3.parse(startTime);
            }
            if (StringUtil.isEmpty(endTime)) {
            } else {
                endDate = df3.parse(endTime);
            }

            mapMessage.put("startTime", startTime);
            mapMessage.put("endTime", endTime);

            int curPage = pageStartIndex / pageSize + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            Set<String> userMessageList;
            if (startDate == null && endDate == null) {
                userMessageList = redisManager.zrange(KEY_CACHE, pagination.getStartRowIdx(), pagination.getEndRowIdx(), RedisManager.RANGE_ORDERBY_DESC);
            } else {
                userMessageList = redisManager.zrevrangeByScore(KEY_CACHE, (endDate == null ? "+inf" : String.valueOf(endDate.getTime() / 1000)), (startDate == null ? "-inf" : String.valueOf(startDate.getTime() / 1000)), pagination.getStartRowIdx(), pagination.getPageSize());
            }
            if (!CollectionUtil.isEmpty(userMessageList)) {
                List<WeixinTextDTO> list = new ArrayList<WeixinTextDTO>();
                for (String obj : userMessageList) {
                    JSONObject jsonObject = JSONObject.fromObject(obj);
                    WeixinTextDTO dto = new WeixinTextDTO();
                    dto.setNickname((String) jsonObject.get("nickname"));
                    dto.setCity((String) jsonObject.get("city"));
                    dto.setCountry((String) jsonObject.get("country"));
                    dto.setOpenid((String) jsonObject.get("openid"));
                    dto.setProvince((String) jsonObject.get("province"));
                    dto.setContent((String) jsonObject.get("content"));
                    dto.setMessageDate(new Date((Long) jsonObject.get("timemillis")));
                    list.add(dto);
                }
                mapMessage.put("list", list);
            }
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/misc/weixin/messagelist", mapMessage);
    }

    @RequestMapping(value = "/outexcel")
    public ModelAndView output(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "starttime", required = false) String startTime,
                               @RequestParam(value = "endtime", required = false) String endTime
    ) {
        WritableWorkbook workbook = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            response.reset();

            OutputStream os = response.getOutputStream();

            response.setContentType("application/msexcel");
            response.addHeader("Content-Disposition", "attachment; filename=" + new String("微信用户消息.xls".getBytes("GB2312"), "iso-8859-1"));

            //设置 编码（双方约定好统一编码）
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setEncoding("GB2312");
            //读取 工作表
            workbook = Workbook.createWorkbook(os, workbookSettings);
            WritableSheet writableSheet = workbook.createSheet("Sheet1", 0);

            Date startDate = null;
            Date endDate = null;

            if (StringUtil.isEmpty(startTime)) {
            } else {
                startDate = sdf.parse(startTime);
            }
            if (StringUtil.isEmpty(endTime)) {
            } else {
                endDate = sdf.parse(endTime);
            }

            Set<String> userMessageList;
            if (startDate == null && endDate == null) {
                userMessageList = redisManager.zrange(KEY_CACHE, 0, -1, RedisManager.RANGE_ORDERBY_DESC);
            } else {
                userMessageList = redisManager.zrevrangeByScore(KEY_CACHE, (endDate == null ? "+inf" : String.valueOf(endDate.getTime() / 1000)), (startDate == null ? "-inf" : String.valueOf(startDate.getTime() / 1000)));
            }

            Label contentLabel1 = new Label(0, 0, "消息");
            writableSheet.addCell(contentLabel1);
            Label nameLabel1 = new Label(1, 0, "昵称");
            writableSheet.addCell(nameLabel1);
            Label cityLabel1 = new Label(2, 0, "城市");
            writableSheet.addCell(cityLabel1);
            Label provinceLabel1 = new Label(3, 0, "省份");
            writableSheet.addCell(provinceLabel1);
            Label countryLabel1 = new Label(4, 0, "国家");
            writableSheet.addCell(countryLabel1);
            Label dateLabel1 = new Label(5, 0, "时间");
            writableSheet.addCell(dateLabel1);

            int row = 1;
            if (!CollectionUtil.isEmpty(userMessageList)) {
                for (String obj : userMessageList) {
                    JSONObject jsonObject = JSONObject.fromObject(obj);

                    Label contentLabel = new Label(0, row, (String) jsonObject.get("content"));
                    writableSheet.addCell(contentLabel);
                    Label nameLabel = new Label(1, row, (String) jsonObject.get("nickname"));
                    writableSheet.addCell(nameLabel);
                    Label cityLabel = new Label(2, row, (String) jsonObject.get("city"));
                    writableSheet.addCell(cityLabel);
                    Label provinceLabel = new Label(3, row, (String) jsonObject.get("province"));
                    writableSheet.addCell(provinceLabel);
                    Label countryLabel = new Label(4, row, (String) jsonObject.get("country"));
                    writableSheet.addCell(countryLabel);
                    Label dateLabel = new Label(5, row, sdf.format(new Date((Long) jsonObject.get("timemillis"))));
                    writableSheet.addCell(dateLabel);
                    row = row + 1;
                }
            }
            workbook.write();
        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                }
            }
        }
        return new ModelAndView("redirect:/misc/weixin/message/list?starttime="+startTime+"&endtime="+endTime);
    }
}
