package com.enjoyf.platform.db.joymeapp;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheat;
import com.enjoyf.platform.service.joymeapp.gameclient.TagDedearchiveCheatType;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.List;

/**
 * Created by zhimingli
 * Date: 2015/01/08
 * Time: 9:58
 */
public abstract class AbstractTagDedearchiveCheatAccessor extends AbstractBaseTableAccessor<TagDedearchiveCheat> implements TagDedearchiveCheatAccessor {
    private Logger logger = LoggerFactory.getLogger(AbstractSocialShareAccessor.class);

    private static final String TABLE_NAME = "tag_dede_archive_cheat";

    @Override
    public TagDedearchiveCheat insert(TagDedearchiveCheat cheat, Connection connection) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            //dede_archives_id,read_num,agree_num,agree_percent,cheating_time
            pstmt = connection.prepareStatement(getInsertSql());
            pstmt.setLong(1, cheat.getDede_archives_id());
            pstmt.setInt(2, cheat.getRead_num());
            pstmt.setInt(3, cheat.getAgree_num());
            pstmt.setDouble(4, cheat.getAgree_percent());
            pstmt.setTimestamp(5, new Timestamp(cheat.getCheating_time() == null ? System.currentTimeMillis() : cheat.getCheating_time().getTime()));
            pstmt.setInt(6, cheat.getReal_read_num() == null ? 0 : cheat.getReal_read_num());
            pstmt.setInt(7, cheat.getReal_agree_num() == null ? 0 : cheat.getReal_agree_num());
            pstmt.setInt(8, cheat.getArchive_type().getCode());
            pstmt.executeUpdate();

            // resultSet = pstmt.getGeneratedKeys();

        } catch (Exception e) {
            GAlerter.lab(this.getClass().getName() + " occur SQLException.e", e);
        } finally {
            DataBaseUtil.closeResultSet(resultSet);
            DataBaseUtil.closeStatment(pstmt);
        }
        return cheat;
    }

    @Override
    public TagDedearchiveCheat get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection connection) throws DbException {
        return super.update(TABLE_NAME, updateExpress, queryExpress, connection);
    }

    @Override
    public List<TagDedearchiveCheat> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<TagDedearchiveCheat> query(QueryExpress queryExpress, Pagination page, Connection conn) throws DbException {
        return query(TABLE_NAME, queryExpress, page, conn);
    }

    private String getInsertSql() {
        String sql = "INSERT INTO " + TABLE_NAME + "(dede_archives_id,read_num,agree_num,agree_percent,cheating_time,real_read_num,real_agree_num,archive_type)VALUES(?,?,?,?,?,?,?,?)";

        if (logger.isDebugEnabled()) {
            logger.debug("the insert sql script:" + sql);
        }
        return sql;
    }

    @Override
    protected TagDedearchiveCheat rsToObject(ResultSet rs) throws SQLException {
        TagDedearchiveCheat cheat = new TagDedearchiveCheat();
        cheat.setDede_archives_id(rs.getLong("dede_archives_id"));
        cheat.setRead_num(rs.getInt("read_num"));
        cheat.setAgree_num(rs.getInt("agree_num"));
        cheat.setAgree_percent(rs.getDouble("agree_percent"));
        cheat.setCheating_time(rs.getTimestamp("cheating_time"));
        cheat.setReal_agree_num(rs.getInt("real_agree_num"));
        cheat.setReal_read_num(rs.getInt("real_read_num"));
        cheat.setArchive_type(TagDedearchiveCheatType.getByCode(rs.getInt("archive_type")));
        return cheat;
    }
}
