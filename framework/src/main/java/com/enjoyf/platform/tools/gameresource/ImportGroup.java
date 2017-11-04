package com.enjoyf.platform.tools.gameresource;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.gameres.GameResourceHandler;
import com.enjoyf.platform.service.gameres.GameResourceServiceSngl;
import com.enjoyf.platform.service.gameres.GroupUser;
import com.enjoyf.platform.service.gameres.GroupValidStatus;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-26 上午11:59
 * Description:
 */
public class ImportGroup {


    private static final String sqlContent = "SELECT t1.UNO , t2.RELATIONID FROM CONTENT t1 INNER JOIN CONTENT_RELATION t2 ON t2.CONTENTID = t1.CONTENTID WHERE t1.REMOVESTATUS = 'n' GROUP BY t1.UNO , t2.RELATIONID;";


    public static void main(String[] args) {

        FiveProps servProps = Props.instance().getServProps();
        String contenturl = servProps.get("content.url");
        String contentUserName = servProps.get("content.userName");
        String contentPwd = servProps.get("content.password");

        GameResourceHandler gameResourceHandler = null;
        try {
            gameResourceHandler = new GameResourceHandler("gameres", servProps);
        } catch (DbException e) {
            System.exit(0);
        }


        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        Set<String> set = new HashSet<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(contenturl, contentUserName, contentPwd);
            pstmt = con.prepareStatement(sqlContent);


            rs = pstmt.executeQuery();
            while (rs.next()) {
                String uno = rs.getString(1);
                String groupId = rs.getString(2);
                set.add(uno + "|" + groupId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeConnection(con);
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(contenturl, contentUserName, contentPwd);


            for (int i = 0; i < 100; i++) {
                String tableNo = i < 10 ? "0" + i : String.valueOf(i);

                String sqlReply = "SELECT t1.INTERACTIONUNO , t2.RELATIONID FROM CONTENT_INTERACTION_" + tableNo + " t1 INNER JOIN CONTENT_RELATION t2 ON t1.CONTENTID = t2.CONTENTID WHERE t1.REMOVESTATUS = 'n' GROUP BY t1.INTERACTIONUNO , t2.RELATIONID;";
                pstmt = con.prepareStatement(sqlReply);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    String uno = rs.getString(1);
                    String groupId = rs.getString(2);
                    set.add(uno + "|" + groupId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeConnection(con);
        }

        for (String s : set) {
            try {
                System.out.println(s);
                String[] array = s.split("\\|");
                if (array.length < 2 || array[0].length() == 0 || array[1].length() == 0) {
                    continue;
                }
                GroupUser groupUser = new GroupUser();
                groupUser.setGroupId(Long.parseLong(array[1]));
                groupUser.setUno(array[0]);
                groupUser.setCreateTime(new Date());
                groupUser.setValidStatus(GroupValidStatus.VALID);
                groupUser.setValidUserid("sysadmin");
                try {
                    groupUser.setValidTime(DateUtil.formatStringToDate("2013-09-01", "yyyy-MM-dd"));
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                groupUser.setCreateIp("127.0.0.0.1");

                gameResourceHandler.insertGroupUser(groupUser);
            } catch (Exception e) {
            }
        }
        System.out.println("==================this is finish:" + set.size());

    }
}
