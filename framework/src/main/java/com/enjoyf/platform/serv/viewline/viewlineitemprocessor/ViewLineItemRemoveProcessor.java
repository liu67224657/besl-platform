package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.service.event.system.ViewLineItemRemoveEvent;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-1-8
 * Time: 上午9:31
 * To change this template use File | Settings | File Templates.
 */
public interface ViewLineItemRemoveProcessor {

    //
    public void process(ViewLineItemRemoveEvent removeEvent);
}
