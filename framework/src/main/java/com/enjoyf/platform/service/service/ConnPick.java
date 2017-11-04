/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.service.service;

/**
 * Class to hold the result of calling the abstract ConnChooser.getPick()
 * method. While this can grow to support all sorts of behavior, currently
 * it just returns the conn we want to use, and optionally a conn that
 * we no longer want to use (for whatever reason). This latter conn will then
 * be shut down.
 */
public class ConnPick {
    //
    private ConnInfo connToUse;
    private ConnInfo connToKill;

    ///////////////////////////////////////////////
    ConnPick(ConnInfo toUse) {
        this(toUse, null);
    }

    ConnPick(ConnInfo toUse, ConnInfo toKill) {
        connToUse = toUse;
        connToKill = toKill;
    }

    ConnInfo getConnInfo() {
        return connToUse;
    }

    ConnInfo getConnToKill() {
        return connToKill;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("conn=" + connToUse);

        if (connToKill != null) {
            sb.append(":connToKill=" + connToKill);
        }

        return new String(sb);
    }
}
