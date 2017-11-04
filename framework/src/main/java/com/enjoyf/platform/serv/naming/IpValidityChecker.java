package com.enjoyf.platform.serv.naming;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.enjoyf.platform.io.ServiceAddress;
import com.enjoyf.platform.util.log.GAlerter;

/**
 * Utility class to validate ip's using the backchannel connection.
 */
class IpValidityChecker {
    private Config config;

    IpValidityChecker(Config cfg) {
        config = cfg;
    }

    boolean isValid(String ip) {
        try {
            return p_isValid(ip);
        }
        catch (UnknownHostException uhe) {
            GAlerter.lab("Unknown host exception: ip = ", ip, uhe);
            return false;
        }
    }

    boolean p_isValid(String bchIpRaw) throws UnknownHostException {
        InetAddress bchIp = InetAddress.getByName(bchIpRaw);

        ServiceAddress[] addresses = config.getNamingServices();
        if (addresses == null || addresses.length == 0) {
            return false;
        }

        for (int i = 0; i < addresses.length; i++) {
            InetAddress ip = addresses[i].getInetAddress();
            if (ip.equals(bchIp)) {
                return true;
            }
        }
        return false;
    }
}
