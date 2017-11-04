/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.stats;

import java.io.Serializable;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public interface StatSection extends Serializable {
    public static final String DEFAULT_SECTION_KEY = "all";

    public static final String SECTION_KEY_TOTAL = "total";
    public static final String SECTION_KEY_AVG = "avg";

    public String getCode();
}
