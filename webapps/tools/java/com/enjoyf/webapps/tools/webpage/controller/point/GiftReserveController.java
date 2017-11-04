package com.enjoyf.webapps.tools.webpage.controller.point;

import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserCenterServiceSngl;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: pengxu
 * Date: 14-6-25
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/gift/reserve")
public class GiftReserveController extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize,
                             @RequestParam(value = "startdate", required = false) String startDate,
                             @RequestParam(value = "enddate", required = false) String endDate,
                             @RequestParam(value = "type", required = false) String type) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();
        try {
            if (!StringUtil.isEmpty(type)) {
                mapMessage.put("type", type);
            }
            int curPage = (pageStartIndex / pageSize) + 1;
            QueryExpress queryExpress = new QueryExpress();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)) {
                return new ModelAndView("/point/giftreservelist", mapMessage);
            }
            Date date = sdf.parse(startDate);
            queryExpress.add(QueryCriterions.geq(GiftReserveField.CREATE_TIME, date));
            mapMessage.put("startdate", date);

            Date date2 = sdf.parse(endDate);
            queryExpress.add(QueryCriterions.leq(GiftReserveField.CREATE_TIME, date2));
            mapMessage.put("enddate", date);

            queryExpress.add(QuerySort.add(GiftReserveField.CREATE_TIME, QuerySortOrder.DESC));
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<GiftReserve> pageRows = PointServiceSngl.get().queryGiftReserveByPage(queryExpress, pagination);
            if (pageRows != null) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
            Map<String, Profile> mapProfile = new HashMap<String, Profile>();
            if (!CollectionUtil.isEmpty(pageRows.getRows())) {
                List<GiftReserve> list = pageRows.getRows();
                Set<String> unos = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (!StringUtil.isEmpty(list.get(i).getUno())) {
                        unos.add(list.get(i).getProfileId());
                    }
                }

                mapProfile = UserCenterServiceSngl.get().queryProfiles(unos);
            }
            mapMessage.put("mapProfile", mapProfile);
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }
        return new ModelAndView("/point/giftreservelist", mapMessage);
    }

    @RequestMapping(value = "/export")
    public ModelAndView export(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                               @RequestParam(value = "maxPageItems", required = false, defaultValue = "40") int pageSize) {

        // 准备设置excel工作表的标题
        String[] title = {"编号", "礼包名称", "预定时间", "预定渠道", "预定人"};
        try {
            // 填充产品编号
            QueryExpress queryExpress = new QueryExpress();

            String replyids = request.getParameter("replyids");
            String arrids[] = replyids.split("@");
            Long giftId[] = new Long[arrids.length];
            for (int i = 0; i < arrids.length; i++) {
                giftId[i] = Long.parseLong(arrids[i]);
            }
            queryExpress.add(QuerySort.add(GiftReserveField.CREATE_TIME, QuerySortOrder.DESC));
            List<GiftReserve> list = PointServiceSngl.get().queryGiftReserveByList(queryExpress.add(QueryCriterions.in(GiftReserveField.GIFT_RESERVE_ID, giftId)));
            if (!CollectionUtil.isEmpty(list)) {
                // 获得开始时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                long start = System.currentTimeMillis();

                String path = WebappConfig.get().getUploadRootpath() + "/upload/excel/";
                if (!FileUtil.isFileOrDirExist(path)) {
                    try {
                        boolean bIsCreate = FileUtil.createDirectory(path);
                        GAlerter.lab("create path:" + path + " is success：" + bIsCreate);
                    } catch (FileNotFoundException e) {
                        GAlerter.lab(e.getLocalizedMessage(), e);
                        return null;
                    }
                }
                String fileName = "giftReserveList" + sdf.format(new Date(start)) + ".cvs";
                String filePath = path + fileName;

                System.out.println(path);
                System.out.println(filePath);
                // 输出的excel的路径

//            filePath = "e:\\testJXL.xls";
                // 创建Excel工作薄
                WritableWorkbook wwb;
                // 新建立一个jxl文件,即在e盘下生成testJXL.xls
                OutputStream os = new FileOutputStream(filePath);
                wwb = Workbook.createWorkbook(os);
                // 添加第一个工作表并设置第一个Sheet的名字
                WritableSheet sheet = wwb.createSheet("report清单", 0);
                Label label;
                for (int i = 0; i < title.length; i++) {
                    // Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
                    // 在Label对象的子对象中指明单元格的位置和内容
                    label = new Label(i, 0, title[i]);
                    // 将定义好的单元格添加到工作表中
                    sheet.addCell(label);
                }
                // 下面是填充数据
                /*
               * 保存数字到单元格，需要使用jxl.write.Number
               * 必须使用其完整路径，否则会出现错误
               * */

                Map<String, Profile> mapProfile = new HashMap<String, Profile>();
                Set<String> profileIds = new HashSet<String>();
                for (int i = 0; i < list.size(); i++) {
                    if (!StringUtil.isEmpty(list.get(i).getUno())) {
                        profileIds.add(list.get(i).getProfileId());
                    }
                }

                mapProfile = UserCenterServiceSngl.get().queryProfiles(profileIds);
                for (int i = 0; i < list.size(); i++) {
                    // 填充产品编号
                    jxl.write.Number number = new jxl.write.Number(0, i + 1, list.get(i).getGiftReserveId());
                    sheet.addCell(number);
                    // 填充产品名称
                    label = new Label(1, i + 1, list.get(i).getGiftName());
                    sheet.addCell(label);
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sdf.format(list.get(i).getCreateTime());
                    label = new Label(2, i + 1, date);
                    sheet.addCell(label);
                    label = new Label(3, i + 1, list.get(i).getLoginDomain().getCode());
                    sheet.addCell(label);
                    Profile profile = mapProfile.get(list.get(i).getProfileId());
                    label = new Label(4, i + 1, profile.getNick());
                    sheet.addCell(label);
                }
                // 写入数据
                wwb.write();
                // 关闭文件
                wwb.close();
                os.close();

                File file = new File(filePath);
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// 把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
                response.setContentType("application/octet-stream");// 告诉浏览器输出内容为流
                response.addHeader("Content-Disposition", "attachment;filename="
                        + fileName);// Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
                String len = String.valueOf(file.length());
                response.setHeader("Content-Length", len);// 设置内容长度
                OutputStream out = response.getOutputStream();
                FileInputStream in = new FileInputStream(file);
                byte[] b = new byte[1024];
                int n;
                while ((n = in.read(b)) != -1) {
                    out.write(b, 0, n);
                }
                in.close();
                out.close();

                return new ModelAndView("redirect:/gift/reserve/list?type=1");
            }
        } catch (Exception e) {
            System.out.println("---出现异常---");
            return new ModelAndView("redirect:/gift/reserve/list?type=-1");
        }

        return new ModelAndView("redirect:/gift/reserve/list?type=0");
    }
}
