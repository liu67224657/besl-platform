/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service;


import com.google.common.base.Strings;
import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-18 下午3:24
 * Description:
 */
@JsonAutoDetect
public class ContentTag implements Serializable {
    public static final ContentTag GUIDE = new ContentTag("攻略");
    public static final ContentTag JOKE = new ContentTag("搞笑");
    public static final ContentTag HELP = new ContentTag("求助");

    //the tag split key
    public static final String TAG_SPLIT = ",";

    //the single tag store linked hash set.
    private Set<String> tags = new LinkedHashSet<String>();

    //
    public ContentTag() {

    }

    public ContentTag(String tagStr) {
        this.add(tagStr);
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public static ContentTag parse(String tagStr) {
        return new ContentTag(tagStr);
    }

    //
    public ContentTag add(String tagStr) {
        if (!Strings.isNullOrEmpty(tagStr)) {
            StringTokenizer tokenizer = new StringTokenizer(tagStr.trim(), TAG_SPLIT);

            while (tokenizer.hasMoreTokens()) {
                tags.add(tokenizer.nextToken());
            }
        }

        return this;
    }

    public String stringValue() {
        StringBuffer returnValue = new StringBuffer();

        int i = 1;
        for (String tag : tags) {
            returnValue.append(tag);
            if (i < tags.size()) {
                returnValue.append(TAG_SPLIT);
            }

            i++;
        }

        return returnValue.toString();
    }

    @Override
    public String toString() {
        return stringValue();
    }
}
