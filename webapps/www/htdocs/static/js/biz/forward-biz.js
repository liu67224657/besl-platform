define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('./login-biz');
    var ajaxverify = require('./ajaxverify');
    var common = require('../common/common');
    require('../common/tips');
    window.forwardLock = false;

    var forwardBiz = {
        getForwardBlog:function(blogid, bloguno, isForward, loadingCallback, callback, forwardSubmitCallback) {
            $.ajax({
                        url:'/json/content/forward/getblog',
                        type:'post',
                        data: {blogid:blogid,bloguno:bloguno,isforward:isForward},
                        success:function(req) {
                            var resultMsg = eval('(' + req + ')');
                            callback(resultMsg, forwardSubmitCallback);
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(blogid);
                            }
                        }
                    });

        },

        editForward:function(forwardParam) {
            var len = common.getInputLength(forwardParam.content)
            if (len > tipsText.comment.blog_reply_length) {
                return tipsText.turnPost.blog_zt_content_maxlength;
            }

            if (!ajaxverify.verifyPost(forwardParam.content)) {
                return tipsText.turnPost.forward_content_illegl;
            }

            if (!ajaxverify.verifyTags(forwardParam.tags)) {
                return tipsText.turnPost.forward_tag_illegl;
            }

            $.ajax({
                        url:'/json/content/forward/edit',
                        type:'post',
                        data: {cid:forwardParam.cid,
                            cuno:forwardParam.cuno,
                            content:forwardParam.content,
                            tags:forwardParam.tags},
                        success:function(req) {
                            var resultMsg = eval('(' + req + ')');
                            forwardParam.submitCallback(forwardParam,resultMsg);
                        },
                        beforeSend:function() {
                            if (forwardParam.loadingCallback != null) {
                                forwardParam.loadingCallback();
                            }
                        },
                        complete:function() {
                            if (forwardParam.completeCallback != null) {
                                forwardParam.completeCallback();
                            }
                        }
                    });
        },

        forward:function(forwardParam) {
            if (window.forwardLock) {
                return false;
            }


            var len = common.getInputLength(forwardParam.content)
            if (len > tipsText.comment.blog_reply_length) {
                return tipsText.turnPost.blog_zt_content_maxlength;
            }

            if (!ajaxverify.verifyPost(forwardParam.content)) {
                return tipsText.turnPost.forward_content_illegl;
            }

            if (!ajaxverify.verifyTags(forwardParam.tags)) {
                return tipsText.turnPost.forward_tag_illegl;
            }

            $.ajax({
                        url:'/json/content/forward',
                        type:'post',
                        data: {blogid:forwardParam.cid,
                            bloguno:forwardParam.cuno,
                            content:forwardParam.content,
                            replypcid:forwardParam.replypcid,
                            replyrcid:forwardParam.replyrcid,
                            tags:forwardParam.tags,
                            sync:forwardParam.sync},
                        success:function(req) {
                            var resultMsg = eval('(' + req + ')');
                            forwardParam.submitCallback(forwardParam,resultMsg);
                        },
                        beforeSend:function() {
                            window.forwardLock = true;
                            if (forwardParam.loadingCallback != null) {
                                forwardParam.loadingCallback();
                            }
                        },
                        complete:function() {
                            if (forwardParam.completeCallback != null) {
                                forwardParam.completeCallback();
                            }
                            window.forwardLock = false;
                        }

                    });
        }
    }

    return forwardBiz;
});