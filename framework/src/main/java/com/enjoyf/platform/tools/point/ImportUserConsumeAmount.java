package com.enjoyf.platform.tools.point;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.db.content.ContentHandler;
import com.enjoyf.platform.db.point.PointHandler;
import com.enjoyf.platform.db.sync.SyncHandler;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.TemplateHotdeployConfig;
import com.enjoyf.platform.service.content.Activity;
import com.enjoyf.platform.service.content.ActivityField;
import com.enjoyf.platform.service.point.*;
import com.enjoyf.platform.service.service.ServiceException;
import com.enjoyf.platform.service.sync.ShareBaseInfo;
import com.enjoyf.platform.service.sync.ShareBaseInfoField;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.util.*;
import com.enjoyf.platform.util.sql.*;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-11-5
 * Time: 下午12:12
 * To change this template use File | Settings | File Templates.
 */
public class ImportUserConsumeAmount {

    private static final Logger logger = LoggerFactory.getLogger(ImportUserConsumeAmount.class);

    private static final TemplateHotdeployConfig templateHotdeployConfig = HotdeployConfigFactory.get().getConfig(TemplateHotdeployConfig.class);

    private static PointHandler pointHandler;

    private static SyncHandler syncHandler;

    private static ContentHandler contentHandler;

    private static UserCenterHandler userCenterHandler;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            pointHandler = new PointHandler("point", servProps);
//            syncHandler = new SyncHandler("sync", servProps);
//            contentHandler = new ContentHandler("content", servProps);
            userCenterHandler = new UserCenterHandler("usercenter", servProps);
        } catch (DbException e) {
            System.exit(0);
            logger.error("update pointHandler error.");
        }
//        importConsumeAmount();
//        updatePointHistoryDescription();
//        importContentFirstLetter();
        //查迷豆的
        queryPointRankByPointKey("syhb");
    }

    private static void queryPointRankByPointKey(String syhb) {
        WritableWorkbook workbook = null;
        try {
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("学生表一");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

            HSSFCell cell = row.createCell(0);
            cell.setCellValue("昵称");
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue("迷豆");
            cell.setCellStyle(style);
            // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
            int j = 0;

            int cp = 0;
            Pagination page = null;
            do {
                cp += 1;
                page = new Pagination(1000 * cp, cp, 1000);
                PageRows<UserPoint> pageRows = pointHandler.queryUserPoint(new QueryExpress().add(QueryCriterions.eq(UserPointField.POINTKEY, syhb)).add(QueryCriterions.gt(UserPointField.USERPOINT, 0)).add(QuerySort.add(UserPointField.USERPOINT, QuerySortOrder.DESC)), page);
                page = pageRows.getPage();
                if (pageRows != null && !CollectionUtil.isEmpty(pageRows.getRows())) {
                    for (UserPoint userPoint : pageRows.getRows()) {
                        if (userPoint.getUserPoint() > 0) {
                            Profile profile = userCenterHandler.getProfile(userPoint.getProfileId());
                            if (profile != null) {
                                row = sheet.createRow(j + 1);
                                // 第四步，创建单元格，并设置值
                                row.createCell(0).setCellValue(profile.getNick());
                                row.createCell(1).setCellValue(userPoint.getUserPoint());
                                System.out.println("-------" + j + "--------");
                                j += 1;
                            }
                        }
                    }
                }
            } while (!page.isLastPage());
            // 第六步，将文件存到指定位置
            FileOutputStream fout = new FileOutputStream("E:/用户迷豆.xls");
            wb.write(fout);
            fout.close();
            System.out.println("---------------------end---------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importConsumeAmount() {
        Map<String, Integer> map = null;
        try {
            map = queryUserConsumeLog(pointHandler);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                modifyUserPoint(pointHandler, new QueryExpress().add(QueryCriterions.eq(UserPointField.USERNO, entry.getKey())), new UpdateExpress().set(UserPointField.CONSUME_AMOUNT, entry.getValue()).set(UserPointField.CONSUME_EXCHANGE, entry.getValue()));
                logger.info("================cal cal user consumecount ===================== " + entry.getKey() + " :" + entry.getValue());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        logger.info("================finish map size ===================== " + map.size());
    }

    public static void updatePointHistoryDescription() {
        try {


            List<PointActionHistory> list = queryPointActionHistory(pointHandler);
            for (PointActionHistory history : list) {
                try {
                    if (history.getActionType().equals(PointActionType.EXCHANGE_GIFT)) {
                        Goods goods = getGoodsById(pointHandler, Long.parseLong(history.getDestId()));

                        Map<String, String> paramMap = new HashMap<String, String>();
                        String descriptionTemplate = templateHotdeployConfig.getExchangePointActionHistoryTemplate();

                        paramMap.put("goodsname", goods.getGoodsName());
                        String descriptionBody = NamedTemplate.parse(descriptionTemplate).format(paramMap);

                        modifyPointActionHistory(pointHandler, history.getActionHistoryId(), new UpdateExpress().set(PointActionHistoryField.ACTIONDESCRIPTION, descriptionBody));
                    } else if (history.getActionType().equals(PointActionType.SHARE)) {
                        ShareBaseInfo shareBaseInfo = getShareInfoById(syncHandler, Long.parseLong(history.getDestId()));

                        Map<String, String> paramMap = new HashMap<String, String>();
                        String descriptionTemplate = templateHotdeployConfig.getSharePointActionHistoryTemplate();

                        paramMap.put("wikiname", shareBaseInfo.getShareKey());
                        String descriptionBody = NamedTemplate.parse(descriptionTemplate).format(paramMap);
                        modifyPointActionHistory(pointHandler, history.getActionHistoryId(), new UpdateExpress().set(PointActionHistoryField.ACTIONDESCRIPTION, descriptionBody));
                    }
                } catch (DbException e) {
                    logger.info("================ error DbException===================== " + history.getActionHistoryId());
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    logger.info("================error NumberFormatException===================== " + history.getActionHistoryId());
                    e.printStackTrace();
                } catch (Exception e) {
                    logger.info("================error NumberFormatException===================== " + history.getActionHistoryId());
                    e.printStackTrace();
                }
                logger.info("================finished import data===================== " + history.getActionHistoryId());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public static void importContentFirstLetter() {
        try {
            List<Activity> listActivity = queryActivity(contentHandler);
            for (Activity activity : listActivity) {
                if (!StringUtil.isEmpty(activity.getGameName())) {
                    modifyActivityFirstLetter(new UpdateExpress()
                            .set(ActivityField.FIRST_LETTER, PinYinUtil.getFirstWordLetter(activity.getGameName())), new QueryExpress()
                            .add(QueryCriterions.eq(ActivityField.ACTIVITY_ID, activity.getActivityId())));
                }
                logger.info("================finished import data===================== " + activity.getActivityId());
            }
        } catch (DbException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static boolean modifyUserPoint(PointHandler pointHandler, QueryExpress add, UpdateExpress increase) throws DbException {
        return pointHandler.updateUserPoint(increase, add);
    }

    private static Map<String, Integer> queryUserConsumeLog(PointHandler pointHandler) throws DbException {

        Map<String, Integer> returnMap = new HashMap<String, Integer>();

        String sql = " select t1.user_no,SUM(t1.consume_amount) FROM point.user_consume_log t1 WHERE t1.consume_amount GROUP BY t1.user_no";

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = DbConnFactory.factory("point");

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                returnMap.put(rs.getString(1), rs.getInt(2));
            }

        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeConnection(conn);
        }
        return returnMap;
    }

    private static ShareBaseInfo getShareInfoById(SyncHandler syncHandler, long id) throws DbException {
        return syncHandler.getShareInfo(new QueryExpress().add(QueryCriterions.eq(ShareBaseInfoField.SHAREID, id)));
    }

    private static boolean modifyPointActionHistory(PointHandler pointHandler, long actionHistoryId, UpdateExpress updateExpress) throws DbException {
        return pointHandler.updatePointActionHistory(updateExpress, new QueryExpress().add(QueryCriterions.eq(PointActionHistoryField.ACTIONHISTORYID, actionHistoryId)));
    }

    private static Goods getGoodsById(PointHandler pointHandler, long id) throws DbException {
        return pointHandler.getGoodsById(id);
    }

    private static List<PointActionHistory> queryPointActionHistory(PointHandler pointHandler) throws DbException {
        return pointHandler.queryPointActionHistory(new QueryExpress());
    }

    private static List<Activity> queryActivity(ContentHandler handler) throws DbException {
        return handler.listActivity(new QueryExpress());
    }

    private static boolean modifyActivityFirstLetter(UpdateExpress updateExpress, QueryExpress queryExpress) throws DbException {
        return contentHandler.modifyActivity(updateExpress, queryExpress);
    }
}


