package com.enjoyf.platform.tools.stats;

import com.enjoyf.platform.db.DbException;
import com.enjoyf.platform.db.stats.StatHandler;
import com.enjoyf.platform.serv.stats.StatConfig;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.Props;

/**
 * Created by zhitaoshi on 2015/12/25.
 */
public class ImportGameEventLog {

    private static StatHandler statHandler;
    private static StatConfig statConfig;

    public static void main(String[] args) {
        FiveProps servProps = Props.instance().getServProps();
        try {
            statHandler = new StatHandler("stats", servProps);
            statConfig = new StatConfig(servProps);
        } catch (DbException e) {
            System.exit(0);
            e.printStackTrace();
        }

    }


}
