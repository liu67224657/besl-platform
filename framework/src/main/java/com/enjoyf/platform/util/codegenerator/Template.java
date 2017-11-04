package com.enjoyf.platform.util.codegenerator;

/**
 * Created by ericliu on 16/3/7.
 */
public class Template {
    public static final String TEMPLATE_INTERFACE_ACCESSOR ="\n" +
            "\n" +
            "import com.enjoyf.platform.db.DbException;\n" +
            "import com.enjoyf.platform.util.Pagination;\n" +
            "import com.enjoyf.platform.util.sql.QueryExpress;\n" +
            "import com.enjoyf.platform.util.sql.UpdateExpress;\n" +
            "\n" +
            "import java.sql.Connection;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public interface [joyme-className]Accessor {\n" +
            "\n" +
            "    public [joyme-className] insert([joyme-className] [joyme-objcet], Connection conn) throws DbException;\n" +
            "\n" +
            "    public [joyme-className] get(QueryExpress queryExpress, Connection conn) throws DbException;\n" +
            "\n" +
            "    public List<[joyme-className]> query(QueryExpress queryExpress, Connection conn) throws DbException;\n" +
            "\n" +
            "    public List<[joyme-className]> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException;\n" +
            "\n" +
            "    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException;\n" +
            "\n" +
            "    public int delete(QueryExpress queryExpress, Connection conn) throws DbException;\n" +
            "}\n";

    public static final String TEMPLATE_DB_FIELD ="import com.enjoyf.platform.util.sql.AbstractObjectField;\n" +
            "import com.enjoyf.platform.util.sql.ObjectFieldDBType;\n" +
            "\n" +
            "\n" +
            "\n" +
            "public class [joyme-className]Field extends AbstractObjectField {\n" +
            "    [joyme-fieldlist]\n" +
            "\n" +
            "\n" +
            "\n" +
            "    public [joyme-className]Field(String column, ObjectFieldDBType type) {\n" +
            "        super(column, type);\n" +
            "    }\n" +
            "}";

    public static final String TEMPLATE_ACCESSOR ="public class [joyme-className]Accessor[joyme-sqlType] extends Abstract[joyme-className]Accessor {\n" +
            "\n" +
            "}\n";

    public static final String TEMPLATE_ABSTRACT_ACCESSOR ="import com.enjoyf.platform.db.AbstractBaseTableAccessor;\n" +
            "import com.enjoyf.platform.db.DataBaseUtil;\n" +
            "import com.enjoyf.platform.db.DbException;\n" +
            "import com.enjoyf.platform.util.Pagination;\n" +
            "import com.enjoyf.platform.util.sql.QueryExpress;\n" +
            "import com.enjoyf.platform.util.sql.UpdateExpress;\n" +
            "import org.slf4j.Logger;\n" +
            "import org.slf4j.LoggerFactory;\n" +
            "\n" +
            "import java.sql.*;\n" +
            "import java.util.Date;\n" +
            "import java.util.List;\n" +
            "\n" +
            "public abstract class Abstract[joyme-className]Accessor extends AbstractBaseTableAccessor<[joyme-className]> implements [joyme-className]Accessor {\n" +
            "\n" +
            "    private static final Logger logger = LoggerFactory.getLogger(Abstract[joyme-className]Accessor.class);\n" +
            "\n" +
            "    private static final String KEY_TABLE_NAME = \"[joyme-talbeName]\";\n" +
            "\n" +
            "    @Override\n" +
            "    public [joyme-className] insert([joyme-className] [joyme-objcet], Connection conn) throws DbException {\n" +
            "        PreparedStatement pstmt = null;\n" +
            "        try {\n" +
            "            pstmt = conn.prepareStatement(getInsertSql());\n" +
            "            [joyme-insert]\n" +
            "            pstmt.executeUpdate();\n" +
            "        } catch (SQLException e) {\n" +
            "            logger.error(\"On insert profile, a SQLException occured.\", e);\n" +
            "            throw new DbException(e);\n" +
            "        } finally {\n" +
            "            DataBaseUtil.closeStatment(pstmt);\n" +
            "        }\n" +
            "\n" +
            "        return [joyme-objcet];\n" +
            "    }\n" +
            "\n" +
            "    private String getInsertSql() {\n" +
            "        String sql = \"INSERT INTO \" + KEY_TABLE_NAME + \"[joyme-replaceInsertsql]\";\n" +
            "        if (logger.isDebugEnabled()) {\n" +
            "            logger.debug(\"[joyme-className] insert sql\" + sql);\n" +
            "        }\n" +
            "        return sql;\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public [joyme-className] get(QueryExpress queryExpress, Connection conn) throws DbException {\n" +
            "        return super.get(KEY_TABLE_NAME, queryExpress, conn);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public int update(UpdateExpress updateExpress, QueryExpress queryExpress, Connection conn) throws DbException {\n" +
            "        return super.update(KEY_TABLE_NAME, updateExpress, queryExpress, conn);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public List<[joyme-className]> query(QueryExpress queryExpress, Connection conn) throws DbException {\n" +
            "        return super.query(KEY_TABLE_NAME, queryExpress, conn);\n" +
            "    }\n" +
            "\n" +
            "    @Override\n" +
            "    public List<[joyme-className]> query(QueryExpress queryExpress, Pagination pagination, Connection conn) throws DbException {\n" +
            "        return super.query(KEY_TABLE_NAME, queryExpress, pagination, conn);\n" +
            "    }\n" +
            "    \n" +
            "    @Override\n" +
            "    public int delete(QueryExpress queryExpress, Connection conn) throws DbException {\n" +
            "        return super.delete(KEY_TABLE_NAME, queryExpress, conn);\n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    @Override\n" +
            "    protected [joyme-className] rsToObject(ResultSet rs) throws SQLException {\n" +
            "\n" +
            "        [joyme-className] returnObject = new [joyme-className]();\n" +
            "\n" +
            "        [joyme-rstoobject]\n" +
            "\n" +
            "        return returnObject;\n" +
            "    }\n" +
            "}";
}
