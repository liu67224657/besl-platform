package com.enjoyf.webapps.joyme.weblogic.blog;

/**
 * <p/>
 * Description:发布类型的悲剧类
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public enum BlogTypes {

    CHAT("chat"),
    TEXT("text"),
    PHOTO("photo"),
    LINK("link"),
    VIDEO("video"),
    AUDIO("audio");

    private String code;

    private BlogTypes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
