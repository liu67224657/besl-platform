package com.enjoyf.platform.serv.billing;

import com.enjoyf.platform.service.billing.BillingConstants;
import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.memcached.MemCachedConfig;
import com.enjoyf.platform.util.reflect.ReflectUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-1-10
 * Time: 下午3:40
 * To change this template use File | Settings | File Templates.
 */
public class BillingConfig {
    //the props.
    private FiveProps props;

    //the attributes
    private String writeableDataSourceName = "writeable";
    private Set<String> readonlyDataSourceNames = new HashSet<String>();

    //queue
    private String queueDiskStorePath;
    private int eventQueueThreadNum = 8;

    //configure file keys
    private static final String KEY_WRITEABLE_DSN = "writeable.datasource.name";
    private static final String KEY_READONLY_DSNS = "readonly.datasource.names";

    private static final String KEY_EVENT_QUEUE_THREAD_NUM = "event.queue.thread.num";


    private MemCachedConfig memCachedConfig;
    private String failedDepositLogCronExp = "0 0/15 * * * ?";
    private boolean failedDepositLogTriggerIsOpen=false;


    private static final String KEY_FAILED_DEPOSIT_CRON_EXP = "failed.depositlog.cron.exp";
    private static final String KEY_FAILED_DEPOSIT_TRIGGER_ISOPEN = "failed.depositlog.trigger.isopen";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    public BillingConfig(FiveProps servProps) {
        props = servProps;

        init();
    }

    private void init() {
        //db configure
        if (props == null) {
            GAlerter.lab("BillingConfig props is null.");

            return;
        }

        //read the datasource setting.
        writeableDataSourceName = props.get(KEY_WRITEABLE_DSN, writeableDataSourceName);

        List<String> dsns = props.getList(KEY_READONLY_DSNS);
        for (String dsn : dsns) {
            readonlyDataSourceNames.add(dsn);
        }

        //queue
        queueDiskStorePath = props.get(BillingConstants.SERVICE_PREFIX + ".NAME");
        eventQueueThreadNum = props.getInt(KEY_EVENT_QUEUE_THREAD_NUM, eventQueueThreadNum);

        memCachedConfig = new MemCachedConfig(props);

        failedDepositLogCronExp = props.get(KEY_FAILED_DEPOSIT_CRON_EXP, failedDepositLogCronExp);
        failedDepositLogTriggerIsOpen= props.getBoolean(KEY_FAILED_DEPOSIT_TRIGGER_ISOPEN, failedDepositLogTriggerIsOpen);
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

    public String getQueueDiskStorePath() {
        return queueDiskStorePath;
    }

    public MemCachedConfig getMemCachedConfig() {
        return memCachedConfig;
    }

    public String getFailedDepositLogCronExp() {
        return failedDepositLogCronExp;
    }

    public boolean isFailedDepositLogTriggerIsOpen() {
        return failedDepositLogTriggerIsOpen;
    }

    /////////////////////////////////////////////////
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
