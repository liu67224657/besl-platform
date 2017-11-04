package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialItem;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-8-21
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAnimeSpecialItemAccessor extends AbstractBaseTableAccessor<AnimeSpecialItem> implements AnimeSpecialItemAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractAnimeSpecialItemAccessor.class);

    private static final String TABLE_NAME = "anime_special_item";

    @Override
    public AnimeSpecialItem insert(AnimeSpecialItem animeSpecialitem, Connection connection) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = connection.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, animeSpecialitem.getSpecialId());
            pstmt.setLong(2, animeSpecialitem.getTvId() == null ? 0 : animeSpecialitem.getTvId());
            pstmt.setString(3, animeSpecialitem.getLinkUrl());
            pstmt.setString(4, animeSpecialitem.getTitle());
            pstmt.setString(5, animeSpecialitem.getDesc());
            pstmt.setString(6, animeSpecialitem.getPic());
            pstmt.setInt(7, animeSpecialitem.getDisplayOrder());
            pstmt.setTimestamp(8, new Timestamp(animeSpecialitem.getUpdateTime() == null ? System.currentTimeMillis() : animeSpecialitem.getUpdateTime().getTime()));
            pstmt.setString(9, animeSpecialitem.getUpdateUser());
            pstmt.setString(10, animeSpecialitem.getRemoveStatus().getCode());
            pstmt.setInt(11, animeSpecialitem.getReplyNum());
            pstmt.executeUpdate();

            resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()) {
                animeSpecialitem.setSpecialItemId(resultSet.getLong(1));
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur SQLException.e", e);
        } finally {
            DataBaseUtil.closeResultSet(resultSet);
            DataBaseUtil.closeStatment(pstmt);
        }
        return animeSpecialitem;
    }

    @Override
    public AnimeSpecialItem get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection connection) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
    }

    @Override
    public List<AnimeSpecialItem> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AnimeSpecialItem> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(TABLE_NAME, queryExpress, page, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + "(anime_special_id,anime_tv_id,link_url,title,anime_desc,pic,display_order,update_time,update_user,remove_status,reply_num)VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    @Override
    protected AnimeSpecialItem rsToObject(ResultSet rs) throws SQLException {
        AnimeSpecialItem animeSpecialitem = new AnimeSpecialItem();
        animeSpecialitem.setSpecialItemId(rs.getLong("anime_special_item_id"));
        animeSpecialitem.setSpecialId(rs.getLong("anime_special_id"));
        animeSpecialitem.setTvId(rs.getLong("anime_tv_id"));
        animeSpecialitem.setLinkUrl(rs.getString("link_url"));
        animeSpecialitem.setTitle(rs.getString("title"));
        animeSpecialitem.setDesc(rs.getString("anime_desc"));
        animeSpecialitem.setPic(rs.getString("pic"));
        animeSpecialitem.setDisplayOrder(rs.getInt("display_order"));
        animeSpecialitem.setUpdateTime(rs.getTimestamp("update_time"));
        animeSpecialitem.setUpdateUser(rs.getString("update_user"));
        animeSpecialitem.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        animeSpecialitem.setReplyNum(rs.getInt("reply_num"));
//        animeSpecialitem.setSpecialAttr(AnimeSpecialAttr.getByCode(rs.getInt("special_attr")));
        return animeSpecialitem;
    }
}
