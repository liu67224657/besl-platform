/**
 * (c) 2008 Fivewh.com
 */
package com.enjoyf.platform.util;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enjoyf.platform.util.log.GAlerter;
import com.enjoyf.platform.util.reflect.ReflectUtil;

/**
 * @Auther: <a mailto:yinpengyi@gmail.com>Yin Pengyi</a>
 */

/**
 * The hotdeploy file is used to check hotdeploy property files' lastmodifydate.
 * The HotdeployFile doesn't means a single files, it means a set of files in a hierarchy.
 */
public class HotdeployFile {
	
	private static final Logger logger = LoggerFactory.getLogger(HotdeployFile.class);
	
    private static final String DEFNAME = "def.properties";
    private static final String PREFIX = "def";
    private static final String UNDERLINE = "_";
    private static final String EXTENSION = ".properties";
    private static final String DIR_SEPARATOR = "/";

    private String fileName;
    private Locale locale;
    private Map<String, Long> fileLastModifiedTimeMap = new HashMap<String, Long>();

    public HotdeployFile(String fname) {
        this(fname, null);
    }

    public HotdeployFile(String fname, Locale l) {
        fileName = fname;
        locale = l;

        reload();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * put the file's last modified date into map
     * and before put into the map, check the old last modify date.
     * if the date is not same, it means the file been changed.
     *
     * @param fname
     * @return
     */
    private boolean recordToMap(String fname) {

        if (logger.isDebugEnabled()) {
        	logger.debug("HotdeployFile: Try to check the file: " + fname);
        }
        
        boolean modified = false;
        URL url = HotdeployFile.class.getResource(fname);

        if (url != null) {
            File f = null;
            long lastModifiedTime = 0;
            long oldValue = 0;

            try {
                f = new File(url.getFile());
                if (f.exists()) {
                    lastModifiedTime = f.lastModified();

                    if (fileLastModifiedTimeMap.containsKey(fname)) {
                        oldValue = fileLastModifiedTimeMap.get(fname);
                    }

                    if (lastModifiedTime > oldValue) {
                        modified = true;
                        fileLastModifiedTimeMap.put(fname, lastModifiedTime);
                    }
                }
            } catch (Exception e) {
                GAlerter.lab("HotdeployFile check file error." + fname, e);
            }
        }

        return modified;
    }

    /**
     * load all the files in hierarchy with the hotdeploy file.
     * for example: if you want to read /props/env/dev/a.properties
     * the following files will be checked:
     * /props/def.properties
     * /props/def_<language>_<country>.properties
     * /props/def_<country>.properties
     * /props/env/def.properties
     * /props/env/def_<language>_<country>.properties
     * /props/env/def_<country>.properties
     * /props/env/dev/def.properties
     * /props/env/dev/def_<language>_<country>.properties
     * /props/env/dev/def_<country>.properties
     * /props/env/dev/a_<language>_<country>.properties
     * /props/env/dev/a_<country>.properties
     *
     * @return
     */
    private boolean reload() {
        boolean modified = false;

        boolean wasDefault = false;
        PathIterator pathItr = new PathIterator(fileName);
        String path;
        String name;

        while ((path = pathItr.nextPath()) != null) {
            name = path + DIR_SEPARATOR + DEFNAME;
            if (name.equals(fileName)) {
                wasDefault = true;
            }
            modified = recordToMap(name) || modified;

            if (locale != null) {
                String lang = locale.getLanguage();
                if (lang != null && lang.length() > 0) {
                    name = path + DIR_SEPARATOR + PREFIX + UNDERLINE + lang + EXTENSION;
                }

                modified = recordToMap(name) || modified;

                String country = locale.getCountry();
                if (country != null && country.length() > 0) {
                    name = path + DIR_SEPARATOR + PREFIX + UNDERLINE + lang +
                            UNDERLINE + country + EXTENSION;
                    modified = recordToMap(name) || modified;
                }
            }
        }

        if (!wasDefault) {
            modified = recordToMap(fileName) || modified;
        }

        if (locale != null) {
            String lang = locale.getLanguage();
            if (lang != null && lang.length() > 0) {
                String pre = fileName;
                int idx = fileName.lastIndexOf(EXTENSION);
                if (idx > 0) {
                    pre = fileName.substring(0, fileName.lastIndexOf(EXTENSION));
                }

                name = pre + UNDERLINE + lang + EXTENSION;
                modified = recordToMap(name) || modified;

                String country = locale.getCountry();
                if (country != null && country.length() > 0) {
                    name = pre + UNDERLINE + lang + UNDERLINE + country + EXTENSION;

                    if (!name.equals(fileName)) {
                        modified = recordToMap(name) || modified;
                    }
                }
            }
        }

        return modified;
    }

    public boolean isModified() {
        return reload();
    }

    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }

//    public static void main(String[] args) {
//        HotdeployFile f = new HotdeployFile("/hotdeploy/props/webapps/sso/def.properties", Locale.CHINA);
//
//        for (int i = 0; i < 100; i++) {
//            System.out.println("%%%%%%%%%%%%" + f.isModified());
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//    }
}
