package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.joymeapp.anime.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import com.enjoyf.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhimingli
 * Date: 14-10-26
 * Time: 下午2:46
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAnimeTagAccessor extends AbstractBaseTableAccessor<AnimeTag> implements AnimeTagAccessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractAppTipsAccessor.class);
    private static final String KEY_TABLE_NAME = "anime_tag";

    @Override
    public AnimeTag insert(AnimeTag animeTag, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getSql(), Statement.RETURN_GENERATED_KEYS);

            //tag_name,tag_desc,picjson,ch_name,en_name,type,model,ch_desc,en_desc,play_num,favorite_num,remove_status,update_date,create_date,create_user
            pstmt.setString(1, animeTag.getTag_name());
            pstmt.setLong(2, animeTag.getParent_tag_id() == null ? 0 : animeTag.getParent_tag_id());
            pstmt.setString(3, animeTag.getTag_desc());


            pstmt.setString(4, animeTag.getPicjson() == null ? "" : animeTag.getPicjson().toJson());
            pstmt.setString(5, animeTag.getCh_name());
            pstmt.setString(6, animeTag.getEn_name());
            pstmt.setString(7, animeTag.getReserved());

            pstmt.setInt(8, animeTag.getAnimeTagType() == null ? 0 : animeTag.getAnimeTagType().getCode());
            pstmt.setInt(9, animeTag.getAnimeTagModel() == null ? 0 : animeTag.getAnimeTagModel().getCode());
            pstmt.setString(10, animeTag.getVolume());
            pstmt.setString(11, animeTag.getCh_desc());
            pstmt.setString(12, animeTag.getEn_desc());

            pstmt.setLong(13, animeTag.getPlay_num());
            //
            pstmt.setLong(14, animeTag.getFavorite_num());
            pstmt.setString(15, animeTag.getRemove_status().getCode());
            pstmt.setTimestamp(16, new Timestamp(animeTag.getCreate_date() == null ? System.currentTimeMillis() : animeTag.getCreate_date().getTime()));
            pstmt.setTimestamp(17, animeTag.getUpdate_date() == null ? null : new Timestamp(animeTag.getUpdate_date().getTime()));

            pstmt.setString(18, StringUtil.isEmpty(animeTag.getCreate_user()) ? "" : animeTag.getCreate_user());

            pstmt.setInt(19, animeTag.getDisplay_order() == null ? 0 : animeTag.getDisplay_order());
            pstmt.setInt(20, animeTag.getAnimeTagSearchType() == null ? 0 : animeTag.getAnimeTagSearchType().getCode());
            pstmt.setInt(21, animeTag.getApp_type().getCode());
            pstmt.setInt(22, animeTag.getTotal_sum()==null?0:animeTag.getTotal_sum());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                animeTag.setTag_id(rs.getLong(1));
            }

        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + " insert occur Exception.e:", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return animeTag;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getSql() {
        String sql = "INSERT INTO " + KEY_TABLE_NAME + "(tag_name,parent_tag_id,tag_desc,picjson,ch_name," +
                "en_name,reserved,type,model,volume,ch_desc,en_desc,play_num,favorite_num,remove_status,create_date,update_date,create_user,display_order,search_type,app_type,total_sum) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " insert sql:" + sql);
        }
        return sql;
    }

    @Override
    public AnimeTag get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public List<AnimeTag> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<AnimeTag> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected AnimeTag rsToObject(ResultSet rs) throws SQLException {
        AnimeTag animeTag = new AnimeTag();

        animeTag.setTag_id(rs.getLong("tag_id"));

        animeTag.setPicjson(rs.getString("picjson") == null ? new AnimeTagPicParam() : AnimeTagPicParam.fromJson(rs.getString("picjson")));
        animeTag.setAnimeTagType(AnimeTagType.getByCode(rs.getInt("type")));
        animeTag.setAnimeTagModel(AnimeTagModel.getByCode(rs.getInt("model")));

        animeTag.setVolume(rs.getString("volume"));

        animeTag.setTag_name(rs.getString("tag_name"));
        animeTag.setParent_tag_id(rs.getLong("parent_tag_id"));
        animeTag.setTag_desc(rs.getString("tag_desc"));


        animeTag.setReserved(rs.getString("reserved"));

        animeTag.setCh_name(rs.getString("ch_name"));
        animeTag.setCh_desc(rs.getString("ch_desc"));
        animeTag.setEn_name(rs.getString("en_name"));
        animeTag.setEn_desc(rs.getString("en_desc"));

        animeTag.setPlay_num(rs.getLong("play_num"));
        animeTag.setFavorite_num(rs.getLong("favorite_num"));
        animeTag.setRemove_status(ValidStatus.getByCode(rs.getString("remove_status")));
        animeTag.setCreate_date(rs.getTimestamp("create_date"));
        animeTag.setUpdate_date(rs.getTimestamp("update_date"));
        animeTag.setCreate_user(rs.getString("create_user"));

        animeTag.setDisplay_order(rs.getInt("display_order"));
        animeTag.setAnimeTagSearchType(AnimeTagSearchType.getByCode(rs.getInt("search_type")));

        animeTag.setApp_type(AnimeTagAppType.getByCode(rs.getInt("app_type")));
        animeTag.setTotal_sum(rs.getInt("total_sum"));
        return animeTag;
    }
}
