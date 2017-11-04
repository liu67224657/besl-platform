package com.enjoyf.platform.service.content.social;

import com.enjoyf.platform.util.reflect.ReflectUtil;
import com.enjoyf.platform.util.sql.AbstractObjectField;
import com.enjoyf.platform.util.sql.ObjectFieldDBType;

/**
 * Created by IntelliJ IDEA.
 * User: zhitaoshi
 * Date: 14-5-13
 * Time: 下午7:54
 * To change this template use File | Settings | File Templates.
 */
public class SocialBackgroundAudioField extends AbstractObjectField{

    public static final SocialBackgroundAudioField AUDIO_ID = new SocialBackgroundAudioField("audio_id", ObjectFieldDBType.LONG, true, true);
    public static final SocialBackgroundAudioField AUDIO_NAME = new SocialBackgroundAudioField("audio_name", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField AUDIO_PIC = new SocialBackgroundAudioField("audio_pic", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField AUDIO_DESCRIPTION = new SocialBackgroundAudioField("audio_description", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField SINGER = new SocialBackgroundAudioField("singer", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField MP3_SRC = new SocialBackgroundAudioField("mp3_src", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField WAV_SRC = new SocialBackgroundAudioField("wav_src", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField DISPLAY_ORDER = new SocialBackgroundAudioField("display_order", ObjectFieldDBType.INT, true, false);
    public static final SocialBackgroundAudioField USE_SUM = new SocialBackgroundAudioField("use_sum", ObjectFieldDBType.INT, true, false);
    public static final SocialBackgroundAudioField REMOVE_STATUS = new SocialBackgroundAudioField("remove_status", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField CREATE_DATE = new SocialBackgroundAudioField("create_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialBackgroundAudioField CREATE_USERID = new SocialBackgroundAudioField("create_userid", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField MODIFY_DATE = new SocialBackgroundAudioField("modify_date", ObjectFieldDBType.TIMESTAMP, true, false);
    public static final SocialBackgroundAudioField MODIFY_IP = new SocialBackgroundAudioField("modify_ip", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField MODIFY_USERID = new SocialBackgroundAudioField("modify_userid", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField SUBSCRIPT = new SocialBackgroundAudioField("subscript", ObjectFieldDBType.STRING, true, false);
    public static final SocialBackgroundAudioField SUBSCRIPT_TYPE = new SocialBackgroundAudioField("subscript_type", ObjectFieldDBType.INT, true, false);

    public SocialBackgroundAudioField(String column, ObjectFieldDBType type, boolean modify, boolean uniquene) {
        super(column, type, modify, uniquene);
    }

    public SocialBackgroundAudioField(String column, ObjectFieldDBType type) {
        super(column, type);
    }

    @Override
    public String toString() {
        return ReflectUtil.fieldsToString(this);
    }
}
