/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.service.event.client;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-8-17 下午1:23
 * Description:
 */
public class ClientEventType implements Serializable {
    private static Map<String, ClientEventType> map = new HashMap<String, ClientEventType>();

    //new accoun register.
    public static final ClientEventType INSTALL_EVENT = new ClientEventType("client.install.event");
    public static final ClientEventType PAGE_VEIW_EVENT = new ClientEventType("client.page.event");
    public static final ClientEventType ERROR_EVENT = new ClientEventType("client.error.event");

    //
    private String code;


    public ClientEventType(String c) {
        this.code = c.toLowerCase();

        map.put(code, this);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "SystemEventType: code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof ClientEventType)) {
            return false;
        }

        return code.equalsIgnoreCase(((ClientEventType) obj).getCode());
    }

    public static ClientEventType getByCode(String c) {
        if (Strings.isNullOrEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<ClientEventType> getAll() {
        return map.values();
    }
}
