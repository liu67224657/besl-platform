package com.enjoyf.platform.db.comment;

import com.enjoyf.platform.crypto.MD5Util;
import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.props.WebappConfig;
import com.enjoyf.platform.props.hotdeploy.HotdeployConfigFactory;
import com.enjoyf.platform.props.hotdeploy.WebHotdeployConfig;
import com.enjoyf.platform.service.ActStatus;
import com.enjoyf.platform.service.comment.CommentBean;
import com.enjoyf.platform.service.comment.CommentDomain;
import com.enjoyf.platform.util.CollectionUtil;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.StringUtil;
import com.enjoyf.platform.util.http.URLUtils;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-11-11
 * Time: 上午11:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCommentBeanAccessor extends AbstractBaseTableAccessor<CommentBean> implements CommentBeanAccessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCommentBeanAccessor.class);

    private static final String KEY_TABLE_NAME = "comment_bean";

    @Override
    public CommentBean insert(CommentBean commentBean, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql());

            pstmt.setString(1, StringUtil.isEmpty(commentBean.getCommentId()) ? MD5Util.Md5(commentBean.getUniqueKey() + commentBean.getDomain().getCode()) : commentBean.getCommentId());
            pstmt.setString(2, commentBean.getUniqueKey() == null ? "" : commentBean.getUniqueKey());
            pstmt.setString(3, commentBean.getUri() == null ? "" : commentBean.getUri());
            pstmt.setInt(4, commentBean.getDomain() == null ? 0 : commentBean.getDomain().getCode());
            pstmt.setString(5, commentBean.getTitle() == null ? "" : commentBean.getTitle());
            pstmt.setString(6, commentBean.getPic() == null ? "" : commentBean.getPic());
            pstmt.setString(7, commentBean.getDescription() == null ? "" : commentBean.getDescription());
            pstmt.setTimestamp(8, new Timestamp(commentBean.getCreateTime() == null ? System.currentTimeMillis() : commentBean.getCreateTime().getTime()));
            pstmt.setString(9, commentBean.getRemoveStatus() == null ? ActStatus.UNACT.getCode() : commentBean.getRemoveStatus().getCode());
            pstmt.setInt(10, commentBean.getTotalRows());
            pstmt.setInt(11, commentBean.getCommentSum());
            pstmt.setInt(12, commentBean.getLongCommentSum());
            pstmt.setDouble(13, commentBean.getAverageScore());
            pstmt.setLong(14, commentBean.getDisplayOrder());
            pstmt.setInt(15, commentBean.getScoreCommentSum());
            pstmt.setDouble(16, commentBean.getScoreSum());
            pstmt.setInt(17, commentBean.getScoreTimes());
            pstmt.setInt(18, commentBean.getFiveUserSum());
            pstmt.setInt(19, commentBean.getFourUserSum());
            pstmt.setInt(20, commentBean.getThreeUserSum());
            pstmt.setInt(21, commentBean.getTwoUserSum());
            pstmt.setInt(22, commentBean.getOneUserSum());
            pstmt.setInt(23, commentBean.getShareSum());
            pstmt.setLong(24, commentBean.getGroupId() == null ? 0l : commentBean.getGroupId());
            pstmt.setString(25, commentBean.getExpandstr());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            GAlerter.lab("On insert CommentBean, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
        return commentBean;
    }


    @Override
    public CommentBean get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CommentBean> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<CommentBean> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<CommentBean> queryByAvgScore(String keyWords, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT comment_id,unikey,uri,domain,title,pic,description,create_time,remove_status,total_rows," +
                    "comment_sum,long_comment_sum,average_score,display_order,score_comment_sum,score_sum," +
                    "score_times,five_user_sum,four_user_sum,three_user_sum,two_user_sum," +
                    "one_user_sum,(score_sum/score_times) AS psc FROM comment_bean WHERE domain=5 AND unikey LIKE ? ORDER BY psc DESC LIMIT 0,10";
            if (logger.isDebugEnabled()) {
                logger.debug("The get sql:" + sql);
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, keyWords + "%");
            rs = pstmt.executeQuery();
            List<CommentBean> list = new ArrayList<CommentBean>();
            while (rs.next()) {
                CommentBean commentBean = new CommentBean();
                commentBean.setCommentId(rs.getString("comment_id"));
                commentBean.setUniqueKey(rs.getString("unikey"));
                commentBean.setUri(rs.getString("uri"));
                commentBean.setDomain(CommentDomain.getByCode(rs.getInt("domain")));
                commentBean.setTitle(rs.getString("title"));
                commentBean.setPic(rs.getString("pic"));
                commentBean.setDescription(rs.getString("description"));
                commentBean.setCreateTime(rs.getTimestamp("create_time"));
                commentBean.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
                commentBean.setTotalRows(rs.getInt("total_rows"));
                commentBean.setLongCommentSum(rs.getInt("long_comment_sum"));
                commentBean.setCommentSum(rs.getInt("comment_sum"));
                commentBean.setDisplayOrder(rs.getLong("display_order"));
                commentBean.setScoreCommentSum(rs.getInt("score_comment_sum"));
                commentBean.setScoreSum(rs.getDouble("score_sum"));
                commentBean.setScoreTimes(rs.getInt("score_times"));
                commentBean.setFiveUserSum(rs.getInt("five_user_sum"));
                commentBean.setFourUserSum(rs.getInt("four_user_sum"));
                commentBean.setThreeUserSum(rs.getInt("three_user_sum"));
                commentBean.setTwoUserSum(rs.getInt("two_user_sum"));
                commentBean.setOneUserSum(rs.getInt("one_user_sum"));
                commentBean.setGroupId(rs.getLong("group_id"));

                DecimalFormat df = new DecimalFormat("#.0");
                commentBean.setAverageScore(Double.valueOf(df.format(rs.getDouble("psc"))));

                list.add(commentBean);
            }
            return list;
        } catch (SQLException e) {
            GAlerter.lab("On insert CommentBean, occur SQLException.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
        }
    }

    @Override
    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.delete(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected CommentBean rsToObject(ResultSet rs) throws SQLException {
        CommentBean commentBean = new CommentBean();
        commentBean.setCommentId(rs.getString("comment_id"));
        String uniKey = rs.getString("unikey");
        commentBean.setUniqueKey(uniKey);
        CommentDomain domain = CommentDomain.getByCode(rs.getInt("domain"));
        commentBean.setDomain(domain);
        commentBean.setUri(getWikiUri(uniKey, domain, rs.getString("uri")));
        commentBean.setTitle(rs.getString("title"));
        commentBean.setPic(URLUtils.getJoymeDnUrl(rs.getString("pic")));
        commentBean.setDescription(rs.getString("description"));
        commentBean.setCreateTime(rs.getTimestamp("create_time"));
        commentBean.setModifyTime(rs.getTimestamp("modify_time"));
        commentBean.setRemoveStatus(ActStatus.getByCode(rs.getString("remove_status")));
        commentBean.setTotalRows(rs.getInt("total_rows"));
        commentBean.setCommentSum(rs.getInt("comment_sum"));
        commentBean.setLongCommentSum(rs.getInt("long_comment_sum"));
        commentBean.setAverageScore(rs.getDouble("average_score"));
        commentBean.setDisplayOrder(rs.getLong("display_order"));
        commentBean.setScoreCommentSum(rs.getInt("score_comment_sum"));
        commentBean.setScoreSum(rs.getDouble("score_sum"));
        commentBean.setScoreTimes(rs.getInt("score_times"));
        commentBean.setFiveUserSum(rs.getInt("five_user_sum"));
        commentBean.setFourUserSum(rs.getInt("four_user_sum"));
        commentBean.setThreeUserSum(rs.getInt("three_user_sum"));
        commentBean.setTwoUserSum(rs.getInt("two_user_sum"));
        commentBean.setOneUserSum(rs.getInt("one_user_sum"));
        commentBean.setShareSum(rs.getInt("share_sum"));
        commentBean.setGroupId(rs.getLong("group_id"));
        commentBean.setExpandstr(rs.getString("expandstr"));
        return commentBean;
    }

    private String getWikiUri(String uniKey, CommentDomain domain, String uri) {
        if (!CommentDomain.DIGITAL_COMMENT.equals(domain)) {
            return uri;
        }
        if(uniKey.indexOf("|") <= 0){
            return uri;
        }
        String wikiKey = uniKey.substring(0, uniKey.indexOf("|"));
        List<String> wikiWikiList = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getWikiWikiList();
        if (!CollectionUtil.isEmpty(wikiWikiList) && wikiWikiList.contains(wikiKey)) {
//        if (wikiKey.equals("lt") || wikiKey.equals("ttkp") || wikiKey.equals("qjnn") || wikiKey.equals("krsma") || wikiKey.equals("zjsn")) {
            String wikiHost = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getWikiHost();
//            String wikiHost = "http://wiki." + WebappConfig.get().getDomain() + "/";
            return wikiHost + wikiKey + "/" + uniKey.substring(uniKey.indexOf("|") + 1, uniKey.length());
        }
        List<String> wwwWikiList = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getWwwWikiList();
        if (!CollectionUtil.isEmpty(wwwWikiList) && wwwWikiList.contains(wikiKey)) {
            String wwwHost = HotdeployConfigFactory.get().getConfig(WebHotdeployConfig.class).getWwwHost();
//            String wwwHost = "http://www." + WebappConfig.get().getDomain() + "/";
            return wwwHost + "wiki/" + wikiKey + "/" + uniKey.substring(uniKey.indexOf("|") + 1, uniKey.length());
        }
        return "http://" + wikiKey + "." + WebappConfig.get().getDomain() + "/" + uniKey.substring(uniKey.indexOf("|") + 1, uniKey.length());
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(comment_id,unikey,uri,domain,title," +
                "pic,description,create_time,remove_status,total_rows,comment_sum,long_comment_sum,average_score," +
                "display_order,score_comment_sum,score_sum,score_times,five_user_sum,four_user_sum,three_user_sum," +
                "two_user_sum,one_user_sum,share_sum,group_id,expandstr) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug("AbstractCommentBeanAccessor insertSql:" + sql);
        }
        return sql;
    }
}
