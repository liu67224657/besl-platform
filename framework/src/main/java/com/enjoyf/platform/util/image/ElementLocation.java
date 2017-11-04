package com.enjoyf.platform.util.image;

import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class ElementLocation {
    private static Map<String, ElementLocation> map = new HashMap<String, ElementLocation>();

    public static final ElementLocation LOCATION_NORTH = new ElementLocation("north");
    public static final ElementLocation LOCATION_NORTHWEST = new ElementLocation("northwest");
    public static final ElementLocation LOCATION_NORTHEAST = new ElementLocation("northeast");
    public static final ElementLocation LOCATION_SOURTH = new ElementLocation("sourth");
    public static final ElementLocation LOCATION_SOURTHWEST = new ElementLocation("sourthwest");
    public static final ElementLocation LOCATION_SOURTHEAST = new ElementLocation("sourtheast");
    public static final ElementLocation LOCATION_WEST = new ElementLocation("west");
    public static final ElementLocation LOCATION_EAST = new ElementLocation("east");
    private String code;

    public ElementLocation(String code) {
        this.code = code;

        map.put(code, this);
    }

    public static ElementLocation getByCode(String code) {
        if (!map.containsKey(code)) {
            return null;
        }

        return map.get(code);
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof ElementLocation) {
            return code.equalsIgnoreCase(((ElementLocation) obj).getCode());
        } else {
            return false;
        }
    }
}
