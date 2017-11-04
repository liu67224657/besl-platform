package com.enjoyf.platform.service.event.system.wiki;

import com.enjoyf.platform.service.ask.search.WikiappSearchDeleteEntry;
import com.enjoyf.platform.service.event.system.SystemEvent;
import com.enjoyf.platform.service.event.system.SystemEventType;

/**
 * Created by zhimingli on 2017-3-29 0029.
 */
public class WikiappSearchDeleteEvent extends SystemEvent {
    private WikiappSearchDeleteEntry wikiappSearchDeleteEntry;

    public WikiappSearchDeleteEntry getWikiappSearchDeleteEntry() {
        return wikiappSearchDeleteEntry;
    }

    public void setWikiappSearchDeleteEntry(WikiappSearchDeleteEntry wikiappSearchDeleteEntry) {
        this.wikiappSearchDeleteEntry = wikiappSearchDeleteEntry;
    }

    public WikiappSearchDeleteEvent() {
        super(SystemEventType.WIKIAPP_DELSEARCH_EVENT);
    }
}
