package com.enjoyf.platform.serv.usercenter;

import java.io.Serializable;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 14/11/20
 * Description:
 */
public class UidPoolWrap implements Serializable{
    private long start;
    private long end;

    public UidPoolWrap(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
