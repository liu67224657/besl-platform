/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.thrserver;

import java.io.IOException;

import com.enjoyf.platform.io.WPacketBase;

/**
 * An implementation of WriteStrategy that simply delegates to the
 * ConnThread for writing.
 */
public class WriteStrategyDef implements WriteStrategy {

    public void write(ConnThreadBase conn, WPacketBase wp) throws IOException {
        conn.writeInternal(wp);
    }
}
