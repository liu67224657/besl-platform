package com.enjoyf.webapps.tools.webpage.controller.point.pointwall;

import com.enjoyf.platform.service.point.PointServiceSngl;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklog;
import com.enjoyf.platform.service.point.pointwall.PointwallTasklogField;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.tools.LogOperType;
import com.enjoyf.platform.service.tools.ToolsLog;
import com.enjoyf.platform.util.PageRows;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.webapps.tools.webpage.controller.base.ToolsBaseController;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonydiao on 2014/11/27.
 */

@Controller
@RequestMapping(value = "/point/pointwall/tasklog")
public class PointwallTasklogController extends ToolsBaseController {


    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "pager.offset", required = false, defaultValue = "0") int pageStartIndex,      //数据库记录索引
                             @RequestParam(value = "maxPageItems", required = false, defaultValue = "20") int pageSize,
                             @RequestParam(value = "taskId", required = false) String taskId,
                             @RequestParam(value = "clientId", required = false) String clientId,
                             @RequestParam(value = "profileid", required = false) String profileid,
                             @RequestParam(value = "appId", required = false) String appId,
                             @RequestParam(value = "packageName", required = false) String packageName,
                             @RequestParam(value = "appkey", required = false) String appkey,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "createIp", required = false) String createIp,
                             @RequestParam(value = "platform", required = false) String platform,
                             @RequestParam(value = "createTimeLeft", required = false) String createTimeLeft,
                             @RequestParam(value = "createTimeRight", required = false) String createTimeRight) {
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(taskId)) {
                mapMessage.put("taskId", taskId);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.TASK_ID, taskId));
            }
            if (!StringUtil.isEmpty(clientId)) {
                mapMessage.put("clientId", clientId);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.CLIENTID, clientId));
            }
            if (!StringUtil.isEmpty(profileid)) {
                mapMessage.put("profileid", profileid);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.PROFILEID, profileid));
            }
            if (!StringUtil.isEmpty(appId)) {
                mapMessage.put("appId", appId);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.APP_ID, Long.valueOf(appId)));
            }
            if (!StringUtil.isEmpty(packageName)) {
                mapMessage.put("packageName", packageName);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.PACKAGE_NAME, packageName));
            }
            if (!StringUtil.isEmpty(appkey)) {
                mapMessage.put("appkey", appkey);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.APPKEY, appkey));
            }
            if (!StringUtil.isEmpty(status)) {
                mapMessage.put("status", status);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.STATUS, Integer.valueOf(status)));
            }
            if (!StringUtil.isEmpty(createIp)) {
                mapMessage.put("createIp", createIp);
                queryExpress.add(QueryCriterions.like(PointwallTasklogField.CREATE_IP, '%' + createIp + '%'));
            }
            if (!StringUtil.isEmpty(platform)) {
                mapMessage.put("platform", platform);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.PLATFORM, Integer.valueOf(platform)));
            }
            if (!StringUtil.isEmpty(createTimeLeft)) {
                mapMessage.put("createTimeLeft", createTimeLeft);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(createTimeLeft);
                queryExpress.add(QueryCriterions.geq(PointwallTasklogField.CREATE_TIME, new Timestamp(date.getTime())));
            }
            if (!StringUtil.isEmpty(createTimeRight)) {
                mapMessage.put("createTimeRight", createTimeRight);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(createTimeRight);
                queryExpress.add(QueryCriterions.leq(PointwallTasklogField.CREATE_TIME, new Timestamp(date.getTime())));
            }

            int curPage = (pageStartIndex / pageSize) + 1;
            Pagination pagination = new Pagination(pageSize * curPage, curPage, pageSize);
            PageRows<PointwallTasklog> pageRows = PointServiceSngl.get().queryPointwallTasklogByPage(queryExpress, pagination);
            if (pageRows != null) {
                mapMessage.put("list", pageRows.getRows());
                mapMessage.put("page", pageRows.getPage());
            }
        } catch (ServiceException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (ParseException e) {
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            mapMessage = putErrorMessage(mapMessage, "system.error");
        }


        return new ModelAndView("/point/pointwall/taskloglist", mapMessage);
    }

    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(value = "taskId", required = false) String taskId,
                       @RequestParam(value = "clientId", required = false) String clientId,
                       @RequestParam(value = "profileid", required = false) String profileid,
                       @RequestParam(value = "appId", required = false) String appId,
                       @RequestParam(value = "packageName", required = false) String packageName,
                       @RequestParam(value = "appkey", required = false) String appkey,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "createIp", required = false) String createIp,
                       @RequestParam(value = "platform", required = false) String platform,
                       @RequestParam(value = "createTimeLeft", required = false) String createTimeLeft,
                       @RequestParam(value = "createTimeRight", required = false) String createTimeRight) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        String fileName = System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        Map<String, Object> mapMessage = new HashMap<String, Object>();

        try {
            QueryExpress queryExpress = new QueryExpress();
            if (!StringUtil.isEmpty(taskId)) {
                mapMessage.put("taskId", taskId);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.TASK_ID, taskId));
            }
            if (!StringUtil.isEmpty(clientId)) {
                mapMessage.put("clientId", clientId);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.CLIENTID, clientId));
            }
            if (!StringUtil.isEmpty(profileid)) {
                mapMessage.put("profileid", profileid);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.PROFILEID, profileid));
            }
            if (!StringUtil.isEmpty(appId)) {
                mapMessage.put("appId", appId);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.APP_ID, Long.valueOf(appId)));
            }
            if (!StringUtil.isEmpty(packageName)) {
                mapMessage.put("packageName", packageName);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.PACKAGE_NAME, packageName));
            }
            if (!StringUtil.isEmpty(appkey)) {
                mapMessage.put("appkey", appkey);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.APPKEY, appkey));
            }
            if (!StringUtil.isEmpty(status)) {
                mapMessage.put("status", status);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.STATUS, Integer.valueOf(status)));
            }
            if (!StringUtil.isEmpty(createIp)) {
                mapMessage.put("createIp", createIp);
                queryExpress.add(QueryCriterions.like(PointwallTasklogField.CREATE_IP, '%' + createIp + '%'));
            }
            if (!StringUtil.isEmpty(platform)) {
                mapMessage.put("platform", platform);
                queryExpress.add(QueryCriterions.eq(PointwallTasklogField.PLATFORM, Integer.valueOf(platform)));
            }
            if (!StringUtil.isEmpty(createTimeLeft)) {
                mapMessage.put("createTimeLeft", createTimeLeft);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(createTimeLeft);
                queryExpress.add(QueryCriterions.geq(PointwallTasklogField.CREATE_TIME, new Timestamp(date.getTime())));
            }
            if (!StringUtil.isEmpty(createTimeRight)) {
                mapMessage.put("createTimeRight", createTimeRight);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(createTimeRight);
                queryExpress.add(QueryCriterions.leq(PointwallTasklogField.CREATE_TIME, new Timestamp(date.getTime())));
            }

            int totalNum = PointServiceSngl.get().countPointwallTasklog(queryExpress);
            OutputStream out = response.getOutputStream();

            SXSSFWorkbook wb = new SXSSFWorkbook(-1); // turn off auto-flushing and accumulate all rows in memory
            wb.setCompressTempFiles(true);      //使用gzip压缩,减小空间占用
            Sheet sh = wb.createSheet("下载日志");

            sh.setColumnWidth(0, 40 * 256);
            sh.setColumnWidth(1, 40 * 256);
            sh.setColumnWidth(2, 22 * 256);
            sh.setColumnWidth(3, 16 * 256);
            sh.setColumnWidth(4, 14 * 256);
            sh.setColumnWidth(5, 12 * 256);
            Row rowHeader = sh.createRow(0);

            Cell cellHeader = rowHeader.createCell(0);
            cellHeader.setCellValue("clientid");
            cellHeader = rowHeader.createCell(1);
            cellHeader.setCellValue("package_name");
            cellHeader = rowHeader.createCell(2);
            cellHeader.setCellValue("create_time");
            cellHeader = rowHeader.createCell(3);
            cellHeader.setCellValue("create_ip");
            cellHeader = rowHeader.createCell(4);
            cellHeader.setCellValue("point_amount");
            cellHeader = rowHeader.createCell(5);
            cellHeader.setCellValue("platform");

            if (totalNum <= 20000) {
                List<PointwallTasklog> list = PointServiceSngl.get().queryPointwallTasklogAll(queryExpress, 0, totalNum);
                writeToSheet(sh, 1, list);

            } else if (totalNum < 500000) {
                int times = 0;
                while (times * 20000 < totalNum) {
                    List<PointwallTasklog> list = PointServiceSngl.get().queryPointwallTasklogAll(queryExpress, times * 20000, 20000);
                    writeToSheet(sh, 1 + times * 20000, list);
                    times += 1;
                }
            } else {
                Row row = sh.createRow(1);
                Cell cell = row.createCell(0);
                cell.setCellValue("符合条件的日志数目大于500,000,无法导出,请更改查询条件,使之小于50万");
            }
            ((SXSSFSheet) sh).flushRows();
            wb.write(out);
            out.close();
            // dispose of temporary files backing this workbook on disk
            wb.dispose();

       /*           byte[] b=new byte[1024];
                  int length;
                  while((length=inputStream.read(b))>0){
                      os.write(b,0,length);
                  }
         */


            ToolsLog log = new ToolsLog();
            log.setOpUserId(getCurrentUser().getUserid());//用户的ID
            log.setOperType(LogOperType.POINT_TASKLOG_EXPORT);  //操作的类型
            log.setOpTime(new Date());//操作时间
            log.setOpIp(getIp());//用户IP
            log.setOpAfter("pw_tasklog表导出clientid:"+clientId+";profileid:"+profileid+";appId:"+appId+";appkey:"+appkey+";platform:"+platform+";createTimeLeft:"+createTimeLeft+";createTimeRight"+createTimeRight); //描述 推荐用中文
            addLog(log);

        } catch (ServiceException e) {
            e.printStackTrace();
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //    mapMessage = putErrorMessage(mapMessage, "system.error");
        } catch (Exception e) {
            e.printStackTrace();
            GAlerter.lab(this.getClass().getName() + " occured ServiceExcpetion.e:", e);
            //      mapMessage = putErrorMessage(mapMessage, "system.error");
        }

    }

    private void writeToSheet(Sheet sh, int startRowNum, List<PointwallTasklog> list) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int rownum = 0; rownum < list.size(); rownum++) {
            Row row = sh.createRow(rownum + startRowNum);

            Cell cell = row.createCell(0);
            cell.setCellValue(list.get(rownum).getClientId());
            cell = row.createCell(1);
            cell.setCellValue(list.get(rownum).getPackageName());
            cell = row.createCell(2);
            cell.setCellValue(simpleDateFormat.format(list.get(rownum).getCreateTime()));
            cell = row.createCell(3);
            cell.setCellValue(list.get(rownum).getCreateIp());
            cell = row.createCell(4);
            cell.setCellValue(list.get(rownum).getPointAmount());
            cell = row.createCell(5);
            cell.setCellValue(list.get(rownum).getPlatform());

            if (rownum % 100 == 0) {
                ((SXSSFSheet) sh).flushRows();  //is a shortcut for ((SXSSFSheet)sh).flushRows(0),
            }
        }
    }
}
