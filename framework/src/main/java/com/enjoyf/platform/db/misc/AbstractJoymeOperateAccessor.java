package com.enjoyf.platform.db.misc;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.misc.JoymeOperate;
import com.enjoyf.platform.service.misc.JoymeOperateField;
import com.enjoyf.platform.service.misc.JoymeOperateType;
import com.enjoyf.platform.service.misc.RegCode;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryCriterions;
import com.enjoyf.platform.util.sql.QueryExpress;
import com.enjoyf.platform.util.sql.QuerySort;
import com.enjoyf.platform.util.sql.QuerySortOrder;
import org.apache.commons.net.ntp.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * @Auther: <a mailto="EricLiu@staff.joyme.com">Eric Liu</a>
 * Create time:  13-9-3 下午5:32
 * Description:
 */
public abstract class AbstractJoymeOperateAccessor extends AbstractBaseTableAccessor<JoymeOperate> implements JoymeOperateAccessor {

    private static Logger logger = LoggerFactory.getLogger(AbstractJoymeOperateAccessor.class);

    private static final String KEY_TABLE_NAME = "joyme_operate";

    @Override
    public JoymeOperate insert(JoymeOperate joymeOperate, Connection conn) throws DbException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS);

            //operate_type, operate_content, create_server_id, create_time, create_userid
            pstmt.setInt(1, joymeOperate.getOperateType().getCode());
            pstmt.setString(2, joymeOperate.getContent());
            pstmt.setString(3, joymeOperate.getServerId());
            pstmt.setTimestamp(4, new Timestamp(joymeOperate.getCreateTime() == null ? System.currentTimeMillis() : joymeOperate.getCreateTime().getTime()));
            pstmt.setString(5, joymeOperate.getCreateUserId());


            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                joymeOperate.setOperateId(rs.getLong(1));
            }

            return joymeOperate;
        } catch (SQLException e) {
            GAlerter.lab(this.getClass().getName() + "On insert , a SQLException occurred.", e);
            throw new DbException(e);
        } finally {
            DataBaseUtil.closeStatment(pstmt);
            DataBaseUtil.closeResultSet(rs);
        }
    }

    private String getInsertSql() {
        String sqlScript = "INSERT INTO " + KEY_TABLE_NAME + "(operate_type, operate_content, create_server_id, create_time, create_userid) VALUES (? ,?, ?, ?, ?)";

        if (logger.isDebugEnabled()) {
            logger.debug("insert script: " + sqlScript);
        }

        return sqlScript;
    }


    @Override
    public List<JoymeOperate> queryUndoOperate(long operateId, JoymeOperateType operateType, Connection conn) throws DbException {
        QueryExpress queryExpress = new QueryExpress()
                .add(QueryCriterions.gt(JoymeOperateField.OPERATE_ID, operateId))
                .add(QueryCriterions.eq(JoymeOperateField.OPERATE_TYPE, operateType.getCode()))
                     .add(QuerySort.add(JoymeOperateField.OPERATE_ID, QuerySortOrder.DESC));

        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    public List<JoymeOperate> query(QueryExpress queryExpress, Connection conn) throws DbException {
        return super.query(KEY_TABLE_NAME, queryExpress, conn);
    }

    @Override
    protected JoymeOperate rsToObject(ResultSet rs) throws SQLException {
        JoymeOperate operate = new JoymeOperate();

        operate.setOperateId(rs.getLong("operate_id"));
        operate.setOperateType(JoymeOperateType.getByCode(rs.getInt("operate_type")));
        operate.setServerId(rs.getString("create_server_id"));
        operate.setContent(rs.getString("operate_content"));
        operate.setCreateTime(rs.getTimestamp("create_time"));
        operate.setCreateUserId(rs.getString("create_userid"));

        return operate;
    }

}
