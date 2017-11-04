package com.enjoyf.platform.serv.viewline.viewlineitemprocessor;

import com.enjoyf.platform.service.event.system.ViewLineItemRemoveEvent;
import com.enjoyf.platform.util.log.GAlerter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class RemoveChainProcessor implements ViewLineItemRemoveProcessor {
    //
    private List<ViewLineItemRemoveProcessor> processorChain = new ArrayList<ViewLineItemRemoveProcessor>();


    public void addProcessor(ViewLineItemRemoveProcessor processor) {
        processorChain.add(processor);
    }

    @Override
    public void process(ViewLineItemRemoveEvent removeEvent) {
        if (processorChain.isEmpty()) {
            return;
        }

        for (ViewLineItemRemoveProcessor p : processorChain) {
            try {
                p.process(removeEvent);
            } catch (Exception e) {
                GAlerter.lab(getClass().getName()+" processor removeEvent occured Exception.e", e);
            }
        }
    }
}
