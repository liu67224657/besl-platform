package com.enjoyf.platform.db.gameres;

import com.enjoyf.platform.db.AbstractBaseTableAccessor;
import com.enjoyf.platform.db.DataBaseUtil;
import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.service.gameres.GameProperty;
import com.enjoyf.platform.service.gameres.GamePropertyDomain;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.sql.QueryExpress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>ericliu</a>
 */
public interface GamePropertyAccessor {

    public GameProperty insert(GameProperty gameProperty, Connection conn) throws DbException;

    public List<GameProperty> insert(List<GameProperty> gamePropertyList, Connection conn) throws DbException;

    public GameProperty get(QueryExpress queryExpress, Connection conn) throws DbException;

    public List<GameProperty> query(QueryExpress queryExpress, Connection conn) throws DbException;

    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;

}
