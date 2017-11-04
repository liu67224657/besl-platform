package com.enjoyf.platform.tools.profile;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-3-22
 * Time: 下午12:09
 * To change this template use File | Settings | File Templates.
 */
public class AddProfileTableNum {
    public static void main(String[] args) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List<String> unoList=new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://db001.web:3311/VOTE?useUnicode=true&characterEncoding=UTF-8", "liuhao", "ef2011");
           con.setAutoCommit(false);
            pstmt = con.prepareStatement("SELECT UNO FROM PROFILE_BLOG");

            rs=pstmt.executeQuery();

            while (rs.next()){
                unoList.add(rs.getString("UNO"));
            }
//            pstmt.
//
//           con.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            rs.close();
            pstmt.close();
            con.close();
        }


    }
}
