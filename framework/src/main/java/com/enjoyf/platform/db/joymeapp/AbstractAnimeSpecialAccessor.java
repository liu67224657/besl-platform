package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.AnimeRedirectType;
import com.enjoyf.platform.service.joymeapp.AnimeSpecial;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialDisplayType;
import com.enjoyf.platform.service.joymeapp.AnimeSpecialType;
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
public abstract class AbstractAnimeSpecialAccessor extends AbstractBaseTableAccessor<AnimeSpecial> implements AnimeSpecialAccessor {

    private Logger logger = LoggerFactory.getLogger(AbstractAnimeSpecialAccessor.class);

    private static final String TABLE_NAME = "anime_special";

    @Override
    public AnimeSpecial insert(AnimeSpecial animeSpecial, Connection connection) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = connection.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, animeSpecial.getSpecialName());
            pstmt.setString(2, animeSpecial.getSpecialDesc());
            pstmt.setInt(3, animeSpecial.getPlatform());
            pstmt.setInt(4, animeSpecial.getSpecialType().getCode());
            pstmt.setString(5, animeSpecial.getRemoveStatus().getCode());
            pstmt.setTimestamp(6, new Timestamp(animeSpecial.getCreateDate() == null ? System.currentTimeMillis() : animeSpecial.getCreateDate().getTime()));
            pstmt.setTimestamp(7, new Timestamp(animeSpecial.getUpdateDate() == null ? System.currentTimeMillis() : animeSpecial.getUpdateDate().getTime()));
            pstmt.setString(8, animeSpecial.getCreateUser());
            pstmt.setString(9, animeSpecial.getSpecialTypeBgColor());
            pstmt.setString(10, animeSpecial.getAppkey());
            pstmt.setString(11, animeSpecial.getCoverPic());
            pstmt.setString(12, animeSpecial.getSpecialPic());
            pstmt.setInt(13, animeSpecial.getDisplayOrder());
            pstmt.setInt(14, animeSpecial.getAnimeRedirectType().getCode());
            pstmt.setString(15, animeSpecial.getLinkUrl());
            pstmt.setString(16, animeSpecial.getSpecialTtile());
            pstmt.setInt(17, animeSpecial.getRead_num());
            pstmt.setInt(18, animeSpecial.getDisplay_type().getCode());
            pstmt.executeUpdate();

            resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()) {
                animeSpecial.setSpecialId(resultSet.getLong(1));
            }

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur SQLException.e", e);
        } finally {
            DataBaseUtil.closeResultSet(resultSet);
            DataBaseUtil.closeStatment(pstmt);
        }
        return animeSpecial;
    }

    @Override
    public AnimeSpecial get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection connection) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
    }

    @Override
    public List<AnimeSpecial> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AnimeSpecial> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(TABLE_NAME, queryExpress, page, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + "(special_name,special_desc,platform,special_type,remove_status,create_date,update_date,create_user,special_type_bgcolor,appkey,cover_pic,special_pic,display_order,anime_redirect,link_url,special_title,read_num,display_type)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    @Override
    protected AnimeSpecial rsToObject(ResultSet rs) throws SQLException {
        AnimeSpecial animeSpecial = new AnimeSpecial();
        animeSpecial.setSpecialId(rs.getLong("special_id"));
        animeSpecial.setSpecialName(rs.getString("special_name"));
        animeSpecial.setSpecialDesc(rs.getString("special_desc"));
        animeSpecial.setPlatform(rs.getInt("platform"));
        animeSpecial.setSpecialType(AnimeSpecialType.getByCode(rs.getInt("special_type")));
        animeSpecial.setRemoveStatus(ValidStatus.getByCode(rs.getString("remove_status")));
        animeSpecial.setCreateDate(rs.getTimestamp("create_date"));
        animeSpecial.setUpdateDate(rs.getTimestamp("update_date"));
        animeSpecial.setCreateUser(rs.getString("create_user"));
        animeSpecial.setSpecialTypeBgColor(rs.getString("special_type_bgcolor"));
        animeSpecial.setAppkey(rs.getString("appkey"));
        animeSpecial.setCoverPic(rs.getString("cover_pic"));
        animeSpecial.setSpecialPic(rs.getString("special_pic"));
        animeSpecial.setDisplayOrder(rs.getInt("display_order"));
        animeSpecial.setAnimeRedirectType(AnimeRedirectType.getByCode(rs.getInt("anime_redirect")));
        animeSpecial.setLinkUrl(rs.getString("link_url"));
        animeSpecial.setSpecialTtile(rs.getString("special_title"));
        animeSpecial.setRead_num(rs.getInt("read_num"));
        animeSpecial.setDisplay_type(AnimeSpecialDisplayType.getByCode(rs.getInt("display_type")));
        return animeSpecial;
    }
}
