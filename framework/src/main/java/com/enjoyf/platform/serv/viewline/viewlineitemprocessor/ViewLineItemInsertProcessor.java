package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.service.event.system.ViewLineItemInsertEvent;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 11-10-12
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public interface ViewLineItemInsertProcessor {
    //
    public void process(ViewLineItemInsertEvent insertEvent);
}
