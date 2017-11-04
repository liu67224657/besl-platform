package com.enjoyf.platform.service.event.system.wiki;

import com.enjoyf.platform.service.ask.search.WikiappSearchEntry;
import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.service.event.system.SystemEventType;

/**
 * Created by zhimingli on 2017-3-29 0029.
 */
public class WikiappSearchEvent extends SystemEvent {
    private WikiappSearchEntry wikiappSearchEntry;

    public WikiappSearchEntry getWikiappSearchEntry() {
        return wikiappSearchEntry;
    }

    public void setWikiappSearchEntry(WikiappSearchEntry wikiappSearchEntry) {
        this.wikiappSearchEntry = wikiappSearchEntry;
    }

    public WikiappSearchEvent() {
        super(SystemEventType.WIKIAPP_ADDSEARCH_EVENT);
    }

}
