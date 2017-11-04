/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.content;

import com.enjoyf.platform.props.EnvConfig;

import java.io.Serializable;
import java.util.*;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-26 下午5:34
 * Description:
 */
public class ContentQueryMap implements Serializable {
    private Map<Integer, Set<String>> queryMap = new HashMap<Integer, Set<String>>();
    private int partitionNum;

    public ContentQueryMap() {
        partitionNum = EnvConfig.get().getServicePartitionNum(ContentConstants.SERVICE_SECTION);
    }

    public void add(Set<String> contentIds) {
        for (String contentId : contentIds) {
            int idx = Math.abs(contentId.hashCode()) % partitionNum;

            Set<String> set = queryMap.get(idx);
            if (set == null) {
                set = new HashSet<String>();
                queryMap.put(idx, set);
            }

            set.add(contentId);
        }
    }

    public void add(String contentId) {
        int idx = Math.abs(contentId.hashCode()) % partitionNum;

        Set<String> set = queryMap.get(idx);
        if (set == null) {
            set = new HashSet<String>();
            queryMap.put(idx, set);
        }

        set.add(contentId);
    }

    public Set<Integer> getKeys() {
        return queryMap.keySet();
    }

    public Set<String> getSetByIndex(Integer idx) {
        return queryMap.get(idx);
    }
}
