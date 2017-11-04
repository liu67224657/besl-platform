package com.enjoyf.platform.text;

import com.enjoyf.platform.util.StringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p/>
 * Description:图片的尺寸
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class WordProcessorKey {
    private static final Map<String, WordProcessorKey> map = new HashMap<String, WordProcessorKey>();

    public static final WordProcessorKey KEY_PRIVIEW_TEXT = new WordProcessorKey("textpriview");
    public static final WordProcessorKey KEY_SIMPLE_TEXT = new WordProcessorKey("simpletextpriview");
    public static final WordProcessorKey KEY_INDEX_TEXT = new WordProcessorKey("indextext");
    public static final WordProcessorKey KEY_PRIVIEW_CHAT = new WordProcessorKey("chatprivew");

    public static final WordProcessorKey KEY_REMOVE_BREAKLINE_CHAT = new WordProcessorKey("removebreaklinechat");
    public static final WordProcessorKey KEY_REMOVE_BREAKLINE_TEXT = new WordProcessorKey("removebreaklinetext");
    public static final WordProcessorKey KEY_SIMPLE_CHAT = new WordProcessorKey("simplechatpriview");
    public static final WordProcessorKey KEY_DISCOVERY_CHAT = new WordProcessorKey("discoverypriview");
    public static final WordProcessorKey KEY_DISCOVERY_SUBJECT = new WordProcessorKey("discoverysubject");
    public static final WordProcessorKey KEY_ALL_TEXT_HIDE = new WordProcessorKey("alltexthide");
    public static final WordProcessorKey KEY_ALL_TEXT_DISPLAY = new WordProcessorKey("alltextdisplay");
    public static final WordProcessorKey KEY_ALL_CHAT = new WordProcessorKey("allchat");
    public static final WordProcessorKey KEY_EDIT_TEXT = new WordProcessorKey("edittext");
    public static final WordProcessorKey KEY_EDIT_CHAT = new WordProcessorKey("editchat");
    public static final WordProcessorKey KEY_PRIVIEW_MESSAGE = new WordProcessorKey("privatemessage");
    public static final WordProcessorKey KEY_PRIVIEW_REPLAY = new WordProcessorKey("replypriview");
    public static final WordProcessorKey KEY_PRIVIEW_REPLY_AFTER_POST = new WordProcessorKey("afterpostreply");
    public static final WordProcessorKey KEY_SIMPLE_REPLAY_CONTENT = new WordProcessorKey("simplyreplycontent");
    public static final WordProcessorKey KEY_POST_TEXT = new WordProcessorKey("posttext");
    public static final WordProcessorKey KEY_POST_CHAT = new WordProcessorKey("postchat");
    public static final WordProcessorKey KEY_POST_FORWARD = new WordProcessorKey("postforward");
    public static final WordProcessorKey KEY_POST_REPLY = new WordProcessorKey("postreply");
    public static final WordProcessorKey KEY_SYNC_CONTENT = new WordProcessorKey("synccontent");
    public static final WordProcessorKey KEY_SYNC_RELATION_CONTENT = new WordProcessorKey("syncrelationcontent");

    // search
    public static final WordProcessorKey KEY_SEARCH_PREVIEW_TEXT = new WordProcessorKey("searchtextpreview");
    public static final WordProcessorKey KEY_SEARCH_PREVIEW_CHAT = new WordProcessorKey("searchchatpreview");

    // ios
    public static final WordProcessorKey KEY_IOS_TIMELINE_PRIVIEW_TEXT = new WordProcessorKey("iostimelinepriviewtext");
    public static final WordProcessorKey KEY_IOS_TIMELINE_TEXT = new WordProcessorKey("iostimelinetext");

    public static final WordProcessorKey KEY_ONLY_TEXT = new WordProcessorKey("onlytext");

    public static final WordProcessorKey KEY_MOBILE_ALL_TEXT = new WordProcessorKey("mobilealltext");

    private String code;


    public WordProcessorKey(String c) {
        code = c.toLowerCase();

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
        return "ImageSize: tag=" + this.code;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof WordProcessorKey)) {
            return false;
        }

        return code.equalsIgnoreCase(((WordProcessorKey) obj).getCode());
    }

    public static WordProcessorKey getByCode(String c) {
        if (StringUtil.isEmpty(c)) {
            return null;
        }

        return map.get(c.toLowerCase());
    }

    public static Collection<WordProcessorKey> getAll() {
        return map.values();
    }
}
