/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.tools.tablehashcode;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.tools.tablehashcode.HashCodeHandler;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-30 上午11:38
 * Description:
 */
public class GenerateTableHashCode {
    private static final Logger logger = LoggerFactory.getLogger(GenerateTableHashCode.class);

    //
    public static void main(String[] args) {
        //param, the table name, the src column, the dest column.
        FiveProps servProps = Props.instance().getServProps();

        String dataSourceName = args[0];

        for (HashCodeTableColumn tableColumn : HashCodeTableColumn.getAll()) {
            System.out.println("Start to update the hashcode of table " + tableColumn.getTableName());

            try {
                HashCodeHandler handler = new HashCodeHandler(dataSourceName, servProps);

                handler.update(tableColumn.getTableName(), tableColumn.getSrcColumnName(), tableColumn.getDestColumnName());
            } catch (DbException e) {
                e.printStackTrace();
            }

            System.out.println("Complete to update the hashcode of table " + tableColumn.getTableName());
        }
    }
}
