package com.enjoyf.platform.serv.naming;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.enjoyf.platform.util.FiveProps;
import com.enjoyf.platform.util.log.GAlerter;

class IpConfig {
    private List<InetAddress> allowedIpList = new ArrayList<InetAddress>();

    public IpConfig(FiveProps props) {
        List<String> ips = props.getList("server.allowedIps");

        for (Iterator<String> itr = ips.iterator(); itr.hasNext();) {
            InetAddress iaddr = null;
            String ip = itr.next();
            try {
                iaddr = InetAddress.getByName(ip);
            }
            catch (Exception e) {
                GAlerter.lab("Could not get InetAddress for: " + ip);
            }
            if (iaddr != null) {
                allowedIpList.add(iaddr);
            }
        }
    }

    public boolean isAllowed(String ip) {
        if (allowedIpList.size() == 0) {
            return true;
        }

        InetAddress iaddr = null;
        try {
            iaddr = InetAddress.getByName(ip);
        }
        catch (Exception e) {
            GAlerter.lab("Caught exception while dns lookup of: ", ip, e);
        }

        if (iaddr == null) {
            return true;
        }

        return allowedIpList.contains(iaddr);
    }

    public boolean isDupeCheckingEnabled() {
        return allowedIpList.size() > 0;
    }

    public String toString() {
        return "allowedIps=" + allowedIpList;
    }
}
