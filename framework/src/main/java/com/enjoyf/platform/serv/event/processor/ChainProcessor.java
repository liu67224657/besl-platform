package com.enjoyf.platform.serv.event.processor;

import com.enjoyf.platform.service.event.user.UserEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class ChainProcessor implements UserEventProcessor {
    //
    private List<UserEventProcessor> processorChain = new ArrayList<UserEventProcessor>();

    //
    public void process(UserEvent event) {
        if (processorChain.isEmpty()) {
            return;
        }

        for (UserEventProcessor p : processorChain) {
            p.process(event);
        }
    }

    public void addProcessor(UserEventProcessor p) {
        processorChain.add(p);
    }
}
