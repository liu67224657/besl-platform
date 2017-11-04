/**
 * (C) 2009 Fivewh platform platform.com
 */
package com.enjoyf.platform.service.stats;

import java.io.Serializable;

/**
 * @Auther: <a mailto:yinpengyi@platform.com>Yin Pengyi</a>
 */
public interface StatDomain extends Serializable {
    public StatDomainPrefix getDomainPrefix();

    public String getCode();

    public boolean isMultiSection();
}