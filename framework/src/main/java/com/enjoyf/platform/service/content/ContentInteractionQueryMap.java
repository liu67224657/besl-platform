/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.props.EnvConfig;

import java.io.Serializable;
import java.util.*;

/**
 * @Auther: <a mailto="ericliu@enjoyfound.com">Liu Hao</a>
 * Create time: 12-5-04 下午5:34
 * Description:
 */
public class ContentInteractionQueryMap implements Serializable {
    //index-cid-interactionids
    private Map<Integer, Map<String, List<String>>> queryMap = new HashMap<Integer, Map<String, List<String>>>();
    private Integer partitionNum;

    public ContentInteractionQueryMap() {
        partitionNum = EnvConfig.get().getServicePartitionNum(ContentConstants.SERVICE_SECTION);
    }

    public void add(List<ContentInteractionQueryEntry> entries) {
        for (ContentInteractionQueryEntry entry : entries) {
            int idx = Math.abs(entry.getContentId().hashCode()) % partitionNum;

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

            l.add(entry.getInteractionId());
        }
    }

    public void add(ContentInteractionQueryEntry entry) {
        int idx = Math.abs(entry.getContentId().hashCode()) % partitionNum;

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

        l.add(entry.getInteractionId());
    }

    public void add(String uno, String contentId, String replyId) {
        add(new ContentInteractionQueryEntry(uno, contentId, replyId));
    }

    public Set<Integer> getKeys() {
        return queryMap.keySet();
    }

    public Map<String, List<String>> getMapByIndex(Integer idx) {
        return queryMap.get(idx);
    }
}
