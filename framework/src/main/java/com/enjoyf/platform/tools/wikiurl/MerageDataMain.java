package com.enjoyf.platform.tools.wikiurl;

import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.conn.DataSourceManager;
import com.enjoyf.platform.db.conn.DbConnFactory;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/5/16
 * Description:
 */
public class MerageDataMain {


    public static void main(String[] args) throws DbException, SQLException {
        FiveProps props = Props.instance().getServProps();
        DataSourceManager.get().append("wikialy", props);
        DataSourceManager.get().append("wikiidc", props);
        Connection alyconn = null;

        long now = System.currentTimeMillis();
        System.out.println("now start merage wikidata contains wiki_key wiki_template wiki_item wiki_page.now: " + now);
        try {
            alyconn = DbConnFactory.factory("wikialy");
            List<String> idcWikikeys = getIdcWikikey();
            System.out.println("=====wiki key size:" + idcWikikeys.size());
            for (String wikiKey : idcWikikeys) {
                deleteJoymeWiki(wikiKey);
                insertJoymeWiki(wikiKey);

                //template dc kdmx papasg 这3个不用导
                deleteJoymeTemplate(wikiKey);
                insertJoymeTemplate(wikiKey);

                deleteJoymeItem(wikiKey);
                insertJoymeItem(wikiKey);

                deleteWikiCardOpinion(wikiKey);
                insertWikiCardOpinion(wikiKey);

                deleteWikiPage(wikiKey);
                insertWikiPage(wikiKey);
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeConnection(alyconn);
        }

        System.out.println("=====finish all===== spent :" + ((System.currentTimeMillis() - now) / 1000));

    }

    private static int deleteJoymeWiki(String key) {
        //from idc
        String sql = "DELETE FROM joyme_wiki WHERE joyme_wiki_key='" + key + "'";

        PreparedStatement alyPstamt = null;
        Connection alyconn = null;
        try {
            alyconn = DbConnFactory.factory("wikialy");

            alyPstamt = alyconn.prepareStatement(sql);

            return alyPstamt.executeUpdate();

        } catch (Exception e) {

        } finally {
            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return 0;
    }

    private static int insertJoymeWiki(String key) {
        //from idc
        String sql = "SELECT * FROM joyme_wiki WHERE joyme_wiki_key='" + key + "'";

        PreparedStatement idcPstamt = null;
        PreparedStatement alyPstamt = null;
        ResultSet rs = null;
        Connection idcconn = null;
        Connection alyconn = null;
        int i = 0;
        try {
            idcconn = DbConnFactory.factory("wikiidc");
            alyconn = DbConnFactory.factory("wikialy");

            idcPstamt = idcconn.prepareStatement(sql);
            //
            rs = idcPstamt.executeQuery();

            while (rs.next()) {
                i++;
                String joyme_wiki_key = rs.getString("joyme_wiki_key");
                int joyme_wiki_id = rs.getInt("joyme_wiki_id");
                String joyme_wiki_domain = rs.getString("joyme_wiki_domain");
                String joyme_wiki_path = rs.getString("joyme_wiki_path");
                String joyme_wiki_name = rs.getString("joyme_wiki_name");
                String joyme_android_path = rs.getString("joyme_android_path");
                String joyme_ios_path = rs.getString("joyme_ios_path");
                String context_path = rs.getString("context_path");
                int support_subdomain = rs.getInt("support_subdomain");
                String joyme_wiki_factory = rs.getString("joyme_wiki_factory");


                try {
                    String insertSql = "INSERT INTO joyme_wiki (joyme_wiki_domain, joyme_wiki_path, joyme_wiki_name, joyme_android_path,joyme_ios_path,context_path,support_subdomain,joyme_wiki_key,joyme_wiki_factory)"
                            + " VALUES (?, ?, ?, ?, ?,?,?,?,?)";
                    alyPstamt = alyconn.prepareStatement(insertSql);
                    alyPstamt.setString(1, joyme_wiki_domain);
                    alyPstamt.setString(2, joyme_wiki_path);
                    alyPstamt.setString(3, joyme_wiki_name);
                    alyPstamt.setString(4, joyme_android_path);
                    alyPstamt.setString(5, joyme_ios_path);
                    alyPstamt.setString(6, context_path);
                    alyPstamt.setInt(7, support_subdomain);
                    alyPstamt.setString(8, joyme_wiki_key);
                    alyPstamt.setString(9, joyme_wiki_factory);

                    int b = alyPstamt.executeUpdate();
                    if (b < 1) {
                        System.out.println(joyme_wiki_key);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("error wikikey:" + joyme_wiki_key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(idcPstamt);
            DataBaseUtil.closeConnection(idcconn);

            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return i;
    }

    private static List<String> getIdcWikikey() throws SQLException, DbException {

        String sql = "SELECT DISTINCT(joyme_wiki_key) as joyme_wiki_key FROM joyme_wiki WHERE joyme_wiki_key NOT IN('angrybirds','bwlb','dczg','dmyx','dqz','hdzb','hlddz','klyx','lt','ltyx','mjzr','mlyys','ms','mt2','mtwz','nbayx','qjnn','qmfjdz','qmtj','rzds','ttaxc','ttcq','ttfc','ttfw','ttkp','ttltf','wiki','xxjqxz','yzdmx','zdbjl','zjsn','zyzz')";
        List<String> list = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection idcconn = null;
        try {
            idcconn = DbConnFactory.factory("wikiidc");
            pstmt = idcconn.prepareStatement(sql);

            //
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("joyme_wiki_key"));
            }

        } catch (Exception e) {

        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeConnection(idcconn);
        }
        return list;
    }

    private static int deleteJoymeTemplate(String key) {
        //from idc
        String sql = "DELETE FROM joyme_template WHERE wiki='" + key + "'";

        PreparedStatement alyPstamt = null;
        Connection alyconn = null;
        try {
            alyconn = DbConnFactory.factory("wikialy");

            alyPstamt = alyconn.prepareStatement(sql);

            return alyPstamt.executeUpdate();

        } catch (Exception e) {

        } finally {
            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return 0;
    }

    private static int insertJoymeTemplate(String key) {
        //from idc
        String sql = "SELECT * FROM joyme_template WHERE wiki='" + key + "'";

        PreparedStatement idcPstamt = null;
        PreparedStatement alyPstamt = null;
        ResultSet rs = null;
        Connection idcconn = null;
        Connection alyconn = null;
        int i = 0;
        try {
            idcconn = DbConnFactory.factory("wikiidc");
            alyconn = DbConnFactory.factory("wikialy");

            idcPstamt = idcconn.prepareStatement(sql);
            //
            rs = idcPstamt.executeQuery();

            while (rs.next()) {
                i++;
                String template_name = rs.getString("template_name");
                String channel = rs.getString("channel");
                String wiki = rs.getString("wiki");
                int is_index = rs.getInt("is_index");
                String template_context = rs.getString("template_context");
                Timestamp create_time = rs.getTimestamp("create_time");
                int is_enable = rs.getInt("is_enable");
                String parse_factory = rs.getString("prase_factory");
                String context_path = rs.getString("context_path");

                String insertSql = "INSERT INTO joyme_template (template_name, channel, wiki, is_index, template_context, create_time, is_enable, prase_factory, context_path)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                alyPstamt = alyconn.prepareStatement(insertSql);
                alyPstamt.setString(1, template_name);
                alyPstamt.setString(2, channel);
                alyPstamt.setString(3, wiki);
                alyPstamt.setInt(4, is_index);
                alyPstamt.setString(5, template_context);
                alyPstamt.setTimestamp(6, create_time);
                alyPstamt.setInt(7, is_enable);
                alyPstamt.setString(8, parse_factory);
                alyPstamt.setString(9, context_path);

                int b = alyPstamt.executeUpdate();
                if (b < 1) {
                    System.out.println(wiki);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(idcPstamt);
            DataBaseUtil.closeConnection(idcconn);

            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return i;
    }


    private static int deleteJoymeItem(String key) {
        //from idc
        String sql = "DELETE FROM joyme_item WHERE wiki='" + key + "'";

        PreparedStatement alyPstamt = null;
        Connection alyconn = null;
        try {
            alyconn = DbConnFactory.factory("wikialy");

            alyPstamt = alyconn.prepareStatement(sql);

            return alyPstamt.executeUpdate();

        } catch (Exception e) {

        } finally {
            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return 0;
    }

    private static int insertJoymeItem(String key) {
        //from idc
        String sql = "SELECT * FROM joyme_item WHERE wiki='" + key + "'";

        PreparedStatement idcPstamt = null;
        PreparedStatement alyPstamt = null;
        ResultSet rs = null;
        Connection idcconn = null;
        Connection alyconn = null;
        int i = 0;
        try {
            idcconn = DbConnFactory.factory("wikiidc");
            alyconn = DbConnFactory.factory("wikialy");

            idcPstamt = idcconn.prepareStatement(sql);
            //
            rs = idcPstamt.executeQuery();

            while (rs.next()) {
                i++;
                String channel = rs.getString("channel");
                String context_path = rs.getString("context_path");
                Timestamp create_date = rs.getTimestamp("create_date");
                int is_index = rs.getInt("is_index");
                String item_context = rs.getString("item_context");
                String item_description = rs.getString("item_description");
                String item_key = rs.getString("item_key");
                String item_properties = rs.getString("item_properties");
                String item_type = rs.getString("item_type");
                String wiki = rs.getString("wiki");

                String insertSql = "INSERT INTO joyme_item (channel, context_path, create_date, is_index, item_context, item_description, item_key, item_properties,item_type,wiki)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                alyPstamt = alyconn.prepareStatement(insertSql);
                alyPstamt.setString(1, channel);
                alyPstamt.setString(2, context_path);
                alyPstamt.setTimestamp(3, create_date);
                alyPstamt.setInt(4, is_index);
                alyPstamt.setString(5, item_context);
                alyPstamt.setString(6, item_description);
                alyPstamt.setString(7, item_key);
                alyPstamt.setString(8, item_properties);
                alyPstamt.setString(9, item_type);
                alyPstamt.setString(10, wiki);

                int b = alyPstamt.executeUpdate();
                if (b < 1) {
                    System.out.println(wiki);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(idcPstamt);
            DataBaseUtil.closeConnection(idcconn);

            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return i;
    }

    private static int deleteWikiCardOpinion(String key) {
        //from idc
        String sql = "DELETE FROM wiki_card_opinion WHERE wiki='" + key + "'";

        PreparedStatement alyPstamt = null;
        Connection alyconn = null;
        try {
            alyconn = DbConnFactory.factory("wikialy");

            alyPstamt = alyconn.prepareStatement(sql);

            return alyPstamt.executeUpdate();

        } catch (Exception e) {

        } finally {
            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return 0;
    }

    private static int insertWikiCardOpinion(String key) {
        //from idc
        String sql = "SELECT * FROM wiki_card_opinion WHERE wiki='" + key + "'";

        PreparedStatement idcPstamt = null;
        PreparedStatement alyPstamt = null;
        ResultSet rs = null;
        Connection idcconn = null;
        Connection alyconn = null;
        int i = 0;
        try {
            idcconn = DbConnFactory.factory("wikiidc");
            alyconn = DbConnFactory.factory("wikialy");

            idcPstamt = idcconn.prepareStatement(sql);
            //
            rs = idcPstamt.executeQuery();

            while (rs.next()) {
                i++;
                String contacts = rs.getString("contacts");
                String nick_name = rs.getString("nick_name");
                Timestamp createtime = rs.getTimestamp("createtime");
                String opinion_key = rs.getString("opinion_key");
                String opinion_value = rs.getString("opinion_value");
                int remove_state = rs.getInt("remove_state");
                String title = rs.getString("title");
                String wiki = rs.getString("wiki");
                String wiki_source = rs.getString("wiki_source");

                String insertSql = "INSERT INTO wiki_card_opinion (contacts, nick_name, createtime, opinion_key, opinion_value, remove_state, title, wiki,wiki_source)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                alyPstamt = alyconn.prepareStatement(insertSql);
                alyPstamt.setString(1, contacts);
                alyPstamt.setString(2, nick_name);
                alyPstamt.setTimestamp(3, createtime);
                alyPstamt.setString(4, opinion_key);
                alyPstamt.setString(5, opinion_value);
                alyPstamt.setInt(6, remove_state);
                alyPstamt.setString(7, title);
                alyPstamt.setString(8, wiki);
                alyPstamt.setString(9, wiki_source);

                int b = alyPstamt.executeUpdate();
                if (b < 1) {
                    System.out.println(wiki);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(idcPstamt);
            DataBaseUtil.closeConnection(idcconn);

            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return i;
    }

    private static int deleteWikiPage(String key) {
        //from idc
        String sql = "DELETE FROM wiki_page WHERE wiki_key='" + key + "'";

        PreparedStatement alyPstamt = null;
        Connection alyconn = null;
        try {
            alyconn = DbConnFactory.factory("wikialy");

            alyPstamt = alyconn.prepareStatement(sql);

            return alyPstamt.executeUpdate();

        } catch (Exception e) {

        } finally {
            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return 0;
    }

    private static int insertWikiPage(String key) {
        //from idc
        String sql = "SELECT * FROM wiki_page WHERE wiki_key='" + key + "'";

        PreparedStatement idcPstamt = null;
        PreparedStatement alyPstamt = null;
        ResultSet rs = null;
        Connection idcconn = null;
        Connection alyconn = null;
        int i = 0;
        try {
            idcconn = DbConnFactory.factory("wikiidc");
            alyconn = DbConnFactory.factory("wikialy");

            idcPstamt = idcconn.prepareStatement(sql);
            //
            rs = idcPstamt.executeQuery();

            while (rs.next()) {
                i++;
                int page_id = rs.getInt("page_id");
                Timestamp create_time = rs.getTimestamp("create_time");
                String page_status = rs.getString("page_status");
                String wiki_url = rs.getString("wiki_url");
                String wiki_key = rs.getString("wiki_key");

                try {
                    String insertSql = "INSERT INTO wiki_page (page_id, create_time, page_status, wiki_url, wiki_key)"
                            + " VALUES (?, ?, ?, ?, ?)";
                    alyPstamt = alyconn.prepareStatement(insertSql);
                    alyPstamt.setInt(1, page_id);
                    alyPstamt.setTimestamp(2, create_time);
                    alyPstamt.setString(3, page_status);
                    alyPstamt.setString(4, wiki_url);
                    alyPstamt.setString(5, wiki_key);

                    int b = alyPstamt.executeUpdate();
                } catch (SQLException e) {


                    String insertSql = "INSERT INTO wiki_page ( create_time, page_status, wiki_url, wiki_key)"
                            + " VALUES (?, ?, ?, ?)";
                    alyPstamt = alyconn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                    alyPstamt.setTimestamp(1, create_time);
                    alyPstamt.setString(2, page_status);
                    alyPstamt.setString(3, wiki_url);
                    alyPstamt.setString(4, wiki_key);
                    alyPstamt.executeUpdate();
                    ResultSet insertRs = alyPstamt.getGeneratedKeys();
                    if (insertRs.next()) {
                        System.out.println("error wikikey " + wiki_key + " " + page_id + " " + insertRs.getInt(1));
                    }
                    DataBaseUtil.closeResultSet(insertRs);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(idcPstamt);
            DataBaseUtil.closeConnection(idcconn);

            DataBaseUtil.closeStatment(alyPstamt);
            DataBaseUtil.closeConnection(alyconn);
        }
        return i;
    }

}
