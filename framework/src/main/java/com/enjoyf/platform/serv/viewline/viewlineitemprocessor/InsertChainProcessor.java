package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.service.event.system.ViewLineItemInsertEvent;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class InsertChainProcessor implements ViewLineItemInsertProcessor {
    //
    private List<ViewLineItemInsertProcessor> processorChain = new ArrayList<ViewLineItemInsertProcessor>();


    public void addProcessor(ViewLineItemInsertProcessor processor) {
        processorChain.add(processor);
    }

    @Override
    public void process(ViewLineItemInsertEvent insertEvent) {
        if (processorChain.isEmpty()) {
            return;
        }

        for (ViewLineItemInsertProcessor p : processorChain) {
            try {
                p.process(insertEvent);
            } catch (Exception e) {
                GAlerter.lab(getClass().getName() + " processor removeEvent occured Exception.e", e);
            }
        }
    }
}
