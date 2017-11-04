package com.enjoyf.platform.serv.content.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */
public class ChainProcessor implements ContentProcessor {
    //
    private List<ContentProcessor> processorChain = new ArrayList<ContentProcessor>();


    public void addProcessor(ContentProcessor processor) {
        processorChain.add(processor);
    }

    @Override
    public void process(Object obj) {
        if (processorChain.isEmpty()) {
            return;
        }

        for (ContentProcessor p : processorChain) {
            p.process(obj);
        }
    }
}
