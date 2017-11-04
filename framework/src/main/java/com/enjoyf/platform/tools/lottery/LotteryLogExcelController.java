package com.enjoyf.platform.tools.lottery;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.lottery.LotteryHandler;
import com.enjoyf.platform.db.usercenter.UserCenterHandler;
import com.enjoyf.platform.service.lottery.UserLotteryLog;
import com.enjoyf.platform.service.lottery.UserLotteryLogField;
import com.enjoyf.platform.service.usercenter.Profile;
import com.enjoyf.platform.service.usercenter.UserAccount;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import com.enjoyf.platform.util.redis.RedisManager;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import jxl.*;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhitaoshi on 2015/12/29.
 */
public class LotteryLogExcelController {

    private static LotteryHandler lotteryHandler;
    private static UserCenterHandler userCenterHandler;
    private static RedisManager manager;

    private static Set<Long> lotteryIdSet = new HashSet<Long>();
    static {
        lotteryIdSet.add(10050l);
        lotteryIdSet.add(10051l);
        lotteryIdSet.add(10052l);
        lotteryIdSet.add(10053l);
        lotteryIdSet.add(10054l);
        lotteryIdSet.add(10055l);
        lotteryIdSet.add(10060l);
        lotteryIdSet.add(10061l);
        lotteryIdSet.add(10062l);
        lotteryIdSet.add(10063l);
        lotteryIdSet.add(10064l);
    }

    public static void main(String[] args){
        FiveProps servProps = Props.instance().getServProps();
        try {
            lotteryHandler = new LotteryHandler("lottery", servProps);
            manager = new RedisManager(servProps);
            userCenterHandler = new UserCenterHandler("usercenter", servProps);
        } catch (DbException e) {
            System.exit(0);
        }
        outPutExcel();
    }

    private static void outPutExcel() {
        try {
            List<UserLotteryLog> logList = lotteryHandler.queryUserLotteryLogByQuery(new QueryExpress()
                    .add(QueryCriterions.gt(UserLotteryLogField.LOTTERY_AWARD_LEVEL, 0))
                    .add(QueryCriterions.in(UserLotteryLogField.LOTTERY_ID, lotteryIdSet.toArray()))
                    .add(QuerySort.add(UserLotteryLogField.LOTTERY_AWARD_LEVEL, QuerySortOrder.ASC)));

            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = wb.createSheet("抽奖记录表");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

            HSSFCell cell = row.createCell(0);
            cell.setCellValue("着迷昵称");
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue("奖品");
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue("奖级");
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue("姓名");
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("电话");
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue("地址");
            cell.setCellStyle(style);
            for (int i = 0; i < logList.size(); i++) {
                Profile profile = userCenterHandler.getProfile(logList.get(i).getUno());
                UserAccount userAccount = userCenterHandler.getAccount(profile.getUno());
                row = sheet.createRow(i + 1);
                // 第四步，创建单元格，并设置值
                row.createCell(0).setCellValue(profile.getNick());
                row.createCell(1).setCellValue(logList.get(i).getLotteryAwardName());
                row.createCell(2).setCellValue(logList.get(i).getLotteryAwardLevel());
                row.createCell(3).setCellValue(userAccount.getAddress() == null ? "" : userAccount.getAddress().getContact());
                row.createCell(4).setCellValue(userAccount.getAddress() == null ? "" : userAccount.getAddress().getPhone());
                row.createCell(5).setCellValue(userAccount.getAddress() == null ? "" : userAccount.getAddress().getAddress());
            }

            FileOutputStream fout = new FileOutputStream("C:\\Users\\zhitaoshi\\Desktop\\抽奖记录.xls");
            wb.write(fout);
            fout.close();
        } catch (DbException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
