package com.enjoyf.platform.util.apk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel
 */
public class AndroidVersions {

	private static Map<Integer, String> versions;
	static {
		Map<Integer, String> tmp = new LinkedHashMap<Integer, String>();
		tmp.put(3, "1.5");
		tmp.put(4, "1.6");
		tmp.put(7, "2.1");
		tmp.put(8, "2.2");
		tmp.put(9, "2.3");
		tmp.put(10, "2.3.3");
		tmp.put(11, "3.0");
		versions = Collections.unmodifiableMap(tmp);
	}
	
	public static List<String> getSupportedVersions(Integer sdkVer) {
		List<String> result = new ArrayList<String>();
		for (Integer ver : versions.keySet()) {
			if (sdkVer <= ver) {
				result.add(versions.get(ver));
			}
		}
		return result;
	}
	
	public static List<String> getAllKnownVersions() {
		return new ArrayList<String>(versions.values());
	}
	
	public static String getVersion(Integer sdkVer) {
		return versions.get(sdkVer);
	}
	
}
