package com.enjoyf.platform.service.comment;

import com.enjoyf.platform.util.oauth.renren.api.client.utils.Md5Utils;

import java.util.UUID;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/6/29
 * Description:
 */
public class CommentUtil {

    /**
     * 生成unique,通过UUID
     *
     * @return
     */
    public static String genUniqueByUUID() {
        return Md5Utils.md5(UUID.randomUUID().toString());
    }

    public static String genCommentId(String uniqueKey, CommentDomain commentDomain) {
        return Md5Utils.md5(uniqueKey + commentDomain.getCode());
    }

    public static String genCommentSumId(String commentId, long rootId) {
        return Md5Utils.md5(commentId + rootId);
    }
}
