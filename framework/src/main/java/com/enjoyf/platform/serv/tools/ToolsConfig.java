package com.enjoyf.platform.serv.tools;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: zhaoxin
 * Date: 11-10-28
 * Time: 下午2:17
 * Desc:
 */
public class ToolsConfig {

    //the props.
    private FiveProps props;

    //
    private boolean openStatTrigger = false;
    private String editorStatCronExp = "0 0 3 * * ?";

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();
    private int eventQueueThreadNum = 8;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";
    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";

    //
    private final static String KEY_STAT_TRIGGER_OPEN = "tools.stat.trigger.open";
    private final static String KEY_EDITOR_STAT_CRON_EXP = "tools.editor.stat.cron.exp";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public ToolsConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("ToolsConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //others
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        //
        openStatTrigger = props.getBoolean(KEY_STAT_TRIGGER_OPEN, openStatTrigger);
        editorStatCronExp = props.get(KEY_EDITOR_STAT_CRON_EXP, editorStatCronExp);
    }

    public FiveProps getProps() {
        return props;
    }

    public String getWriteableDataSourceName() {
        return writeableDataSourceName;
    }

    public Set<String> getReadonlyDataSourceNames() {
        return readonlyDataSourceNames;
    }

    public int getEventQueueThreadNum() {
        return eventQueueThreadNum;
    }

    public boolean isOpenStatTrigger() {
        return openStatTrigger;
    }

    public String getEditorStatCronExp() {
        return editorStatCronExp;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
