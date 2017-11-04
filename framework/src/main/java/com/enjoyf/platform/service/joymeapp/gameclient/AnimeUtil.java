package com.enjoyf.platform.service.joymeapp.gameclient;

import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;

/**
 * Created by zhimingli
 * Date: 2014/12/31
 * Time: 17:01
 */
public class AnimeUtil {

    public static final long TAG_ID_MIYOU_ARTICLE = -2;

    public static String genTagArchiveId(long tagId, String dede_archive_id) {
        return Md5Utils.md5(tagId + dede_archive_id);
    }
}
