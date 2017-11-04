/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.alert;

import com.enjoyf.platform.util.log.Alert;

class ServerAlert {
    private Client client;
    private Alert alert;

    ServerAlert(Client cl, Alert alt) {
        client = cl;
        alert = alt;
    }

    Alert getAlert() {
        return alert;
    }

    Client getClient() {
        return client;
    }
}