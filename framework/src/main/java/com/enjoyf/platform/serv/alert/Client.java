/**
 * CopyRight 2007 Fivewh.com
 */
package com.enjoyf.platform.serv.alert;

/**
 * Class represents a client ( a connection ) to the server.
 */
class Client {
    private String clientInfo;
    private String clientIp;

    Client(String client, String ip) {
        clientInfo = client;
        clientIp = ip;
    }

    String getIp() {
        return clientIp;
    }

    String getClientInfo() {
        return clientInfo;
    }
}
