package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.ValidStatus;
import com.enjoyf.platform.service.gameres.*;
import com.enjoyf.platform.util.Pagination;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.UpdateExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 13-8-21
 * Time: 上午10:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractNewReleaseAccessor extends AbstractBaseTableAccessor<NewRelease> implements NewReleaseAccessor {
    private final Logger logger = LoggerFactory.getLogger(AbstractNewReleaseAccessor.class);

    private static final String KEY_TABLE_NAME = "new_game_info";

    @Override
    public NewRelease insert(NewRelease newRelease, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            //new_game_info_id,game_name,game_icon,game_desc,game_pics,display_order,valid_status,company_name,people_num,cooprate_type,contacts,email,phone,qq,pub_date,pub_area,create_time,create_ip,last_modify_time,last_modify_ip,last_modify_type
            pstmt.setString(1, newRelease.getNewGameName());
            pstmt.setString(2, newRelease.getNewGameIcon());
            pstmt.setString(3, newRelease.getNewGameDesc());
            pstmt.setString(4, newRelease.getNewGamePicSet().toJsonStr());
            pstmt.setInt(5, newRelease.getDisplayOrder());
            pstmt.setString(6, newRelease.getValidStatus().getCode());
            pstmt.setString(7, newRelease.getCompanyName());
            pstmt.setInt(8, newRelease.getPeopleNumType().getValue());
            pstmt.setInt(9, newRelease.getCooprateType().getValue());
            pstmt.setString(10, newRelease.getContacts());
            pstmt.setString(11, newRelease.getEmail());
            pstmt.setString(12, newRelease.getPhone());
            pstmt.setString(13, newRelease.getQq());
            pstmt.setTimestamp(14, new Timestamp(newRelease.getPublishDate() == null ? null : newRelease.getPublishDate().getTime()));
            pstmt.setInt(15, newRelease.getPublishArea().getValue());
            pstmt.setTimestamp(16, new Timestamp(newRelease.getCreateDate() == null ? System.currentTimeMillis() : newRelease.getCreateDate().getTime()));
            pstmt.setString(17, newRelease.getCreateIp());
            pstmt.setInt(18, newRelease.getFocusNum());
            pstmt.setString(19, newRelease.getCreateUno());
            pstmt.setString(20, newRelease.getNotice());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                newRelease.setNewReleaseId(rs.getLong(1));
            }
        } catch (SQLException e) {
            GAlerter.lab("On Insert newRelease, SQLException:"+e);
            throw  new DbException(e);
        } finally {
            DataBaseUtil.closeResultSet(rs);
            DataBaseUtil.closeStatment(pstmt);
        }
        return newRelease;
    }

    @Override
    public int update(QueryExpress queryExpress, UpdateExpress updateExpress, Connection conn) throws DbException {
        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);
    }

    @Override
    public NewRelease get(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.get(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<NewRelease> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<NewRelease> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);
    }

    @Override
    protected NewRelease rsToObject(ResultSet rs) throws SQLException {
        NewRelease newRelease = new NewRelease();
        newRelease.setNewReleaseId(rs.getLong("new_game_info_id"));
        newRelease.setNewGameName(rs.getString("game_name"));
        newRelease.setNewGameIcon(rs.getString("game_icon"));
        newRelease.setNewGameDesc(rs.getString("game_desc"));
        newRelease.setNewGamePicSet(NewReleasePicSet.parse(rs.getString("game_pics")));
        newRelease.setDisplayOrder(rs.getInt("display_order"));
        newRelease.setValidStatus(ValidStatus.getByCode(rs.getString("valid_status")));
        newRelease.setCompanyName(rs.getString("company_name"));
        newRelease.setPeopleNumType(PeopleNumType.getByValue(rs.getInt("people_num")));
        newRelease.setCooprateType(CooprateType.getByValue(rs.getInt("cooprate_type")));
        newRelease.setContacts(rs.getString("contacts"));
        newRelease.setEmail(rs.getString("email"));
        newRelease.setPhone(rs.getString("phone"));
        newRelease.setQq(rs.getString("qq"));
        newRelease.setPublishDate(rs.getTimestamp("pub_date"));
        newRelease.setPublishArea(PublishArea.getByValue(rs.getInt("pub_area")));
        newRelease.setCreateDate(rs.getTimestamp("create_time"));
        newRelease.setCreateIp(rs.getString("create_ip"));
        newRelease.setVerifyDate(rs.getTimestamp("verify_time"));
        newRelease.setLastModifyDate(rs.getTimestamp("last_modify_time"));
        newRelease.setLastModifyIp(rs.getString("last_modify_ip"));
        newRelease.setLastModifyType(LastModifyType.getByCode(rs.getInt("last_modify_type")));
        newRelease.setFocusNum(rs.getInt("focus_num"));
        newRelease.setCreateUno(rs.getString("create_uno"));
        newRelease.setShareId(rs.getLong("share_id"));
        newRelease.setNotice(rs.getString("notice"));
        return newRelease;
    }

    private String getInsertSql() {
        //new_game_info_id,game_name,game_icon,game_desc,game_pics,display_order,valid_status,company_name,people_num,cooprate_type,contacts,email,phone,qq,pub_date,pub_area,create_time,create_ip,verify_time,last_modify_time,last_modify_ip,last_modify_type,focus_num
        String insertSql = "INSERT INTO "+KEY_TABLE_NAME+"(game_name,game_icon,game_desc,game_pics,display_order,valid_status,company_name,people_num,cooprate_type,contacts,email,phone,qq,pub_date,pub_area,create_time,create_ip,focus_num,create_uno,notice) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if(logger.isDebugEnabled()){
            logger.debug("NewRelease getInsertSql:"+insertSql);
        }
        return insertSql;
    }
}
