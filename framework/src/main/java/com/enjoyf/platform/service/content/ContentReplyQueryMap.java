/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.props.EnvConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-26 下午5:34
 * Description:
 */
public class ContentReplyQueryMap implements Serializable {
    //
    private Map<Integer, Map<String, List<String>>> queryMap = new HashMap<Integer, Map<String, List<String>>>();
    private Integer partitionNum;

    public ContentReplyQueryMap() {
        partitionNum = EnvConfig.get().getServicePartitionNum(ContentConstants.SERVICE_SECTION);
    }

    public void add(List<ContentReplyQueryEntry> entries) {
        for (ContentReplyQueryEntry entry : entries) {
            int idx = Math.abs(entry.getUno().hashCode()) % partitionNum;

            Map<String, List<String>> m = queryMap.get(idx);
            if (m == null) {
                m = new HashMap<String, List<String>>();
                queryMap.put(idx, m);
            }

            List<String> l = m.get(entry.getContentId());
            if (l == null) {
                l = new ArrayList<String>();
                m.put(entry.getContentId(), l);
            }

            l.add(entry.getReplyId());
        }
    }

    public void add(ContentReplyQueryEntry entry) {
        int idx = Math.abs(entry.getUno().hashCode()) % partitionNum;

        Map<String, List<String>> m = queryMap.get(idx);
        if (m == null) {
            m = new HashMap<String, List<String>>();
            queryMap.put(idx, m);
        }

        List<String> l = m.get(entry.getContentId());
        if (l == null) {
            l = new ArrayList<String>();
            m.put(entry.getContentId(), l);
        }

        l.add(entry.getReplyId());
    }

    public void add(String uno, String contentId, String replyId) {
        add(new ContentReplyQueryEntry(uno, contentId, replyId));
    }

    public Set<Integer> getKeys() {
        return queryMap.keySet();
    }

    public Map<String, List<String>> getMapByIndex(Integer idx) {
        return queryMap.get(idx);
    }
}
