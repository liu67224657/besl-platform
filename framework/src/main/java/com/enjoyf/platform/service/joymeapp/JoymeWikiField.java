package com.enjoyf.platform.service.joymeapp;

import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

import java.io.Serializable;

/**
 * Created by zhitaoshi on 2015/4/14.
 */
public class JoymeWikiField extends AbstractObjectField {
    public static final JoymeWikiField WIKI_ID = new JoymeWikiField("joyme_wiki_id", ObjectFieldDBType.INT, true, true);
    public static final JoymeWikiField WIKI_KEY = new JoymeWikiField("joyme_wiki_key", ObjectFieldDBType.STRING, true, false);
    public static final JoymeWikiField WIKI_DOMAIN = new JoymeWikiField("joyme_wiki_domain", ObjectFieldDBType.STRING, true, false);
    public static final JoymeWikiField WIKI_NAME = new JoymeWikiField("joyme_wiki_name", ObjectFieldDBType.STRING, true, false);
    public static final JoymeWikiField WIKI_PATH = new JoymeWikiField("joyme_wiki_path", ObjectFieldDBType.STRING, true, false);
    public static final JoymeWikiField WIKI_FACTORY = new JoymeWikiField("joyme_wiki_factory", ObjectFieldDBType.STRING, true, false);
    public static final JoymeWikiField WIKI_CONTEXT_PATH = new JoymeWikiField("context_path", ObjectFieldDBType.STRING, true, false);
    public static final JoymeWikiField WIKI_SUPPORT_SUBDOMAIN = new JoymeWikiField("support_subdomain", ObjectFieldDBType.INT, true, false);
    public static final JoymeWikiField WIKI_PARAM = new JoymeWikiField("exp_param", ObjectFieldDBType.STRING, true, false);

    public JoymeWikiField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public JoymeWikiField(String column, ObjectFieldDBType type) {
        super(column, type);
    }
}
