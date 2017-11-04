package com.enjoyf.platform.tools.cms;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DataSourceException;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.DbTypeException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.service.stats.StatItem;
import com.enjoyf.platform.util.DateUtil;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ericliu on 16/5/10.
 */
public class GenXinWenUrl {

    public static void main(String[] args) throws DbTypeException, DataSourceException {

//        System.out.println(new Date(1426033854l *1000l));
        FiveProps servProps = Props.instance().getServProps();
        DataSourceManager.get().append("test1", servProps);

        Map<Integer, Date> idToDateMap = new HashMap<Integer, Date>();
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            conn = DbConnFactory.factory("test1");

            preparedStatement = conn.prepareStatement("select id ,senddate from dede_archives as t where t.typeid=368");

            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                idToDateMap.put(rs.getInt(1), new Date(rs.getLong(2)*1000l));
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(preparedStatement);
            DataBaseUtil.closeConnection(conn);
        }

        List<String> result = new ArrayList<String>();
        for (Map.Entry<Integer, Date> entry : idToDateMap.entrySet()) {
            result.add("http://xinwen.joyme.com/wenzhang/" + DateUtil.formatDateToString(entry.getValue(), "yyyyMM/dd") + entry.getKey() + ".html");
        }

        for (String url : result) {
            System.out.println(url);
        }

    }
}
