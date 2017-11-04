/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var card = require('../page/card');
    var moodBiz = require('../biz/mood-biz');
    var comment = require('../page/comment');
    var common = require('../common/common');
    require('../common/tips');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    require('../common/jquery.autotextarea')($);

    $(document).ready(function() {

        addDocmentListerner();
        header.noticeSearchReTopInit();

        //发评论下拉
        $('#send_box_body').focus(function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            $('#send_box_small').hide();
            $('#div_post_area').show();
            $('#textarea_comment_body').focus();
            $('#comment_submit').attr('class', 'publishon');
        });

        //发表评论
        $('#form_comment').submit(function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var body = $('#textarea_comment_body').val();
            if (body == null || common.strLen(body) == 0) {
                $('#reply_error').html('评论内容不能为空!');
                return false;
            } else if (common.strLen(body) > 300) {
                $('#reply_error').html('评论内容长度不能超过300字符!');
                return false;
            }
            return true;
        });

        //点赞
        $('a[id^=agreelink_]').live('click', function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var replyid = $(this).attr('data-commentid');
            comment.agree(replyid, callback.agreeCallback);
        });

        //点赞
        $('a[name=agree_num]').live('click', function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var replyid = $(this).attr('data-commentid');
            comment.agree(replyid, callback.agreeCallback);
        });

        //切换回复收起的方法
        $('a[name=link_toggle_recomment]').live('click', function() {
            var sum = $(this).attr('data-reply-sum');

            if ($(this).hasClass('putaway')) {
                $(this).parent().next().fadeOut();
                if (parseInt(sum) > 0) {
                    $(this).html('回复(' + sum + ')').removeClass();
                } else {
                    $(this).html('回复').removeClass();
                }


            } else {
                $(this).parent().next().fadeIn();
                $(this).html('收起回复').attr('class', 'putaway');
            }
        });

        //回复遮罩
        $('a[name=replypost_mask]').live('click', function() {
            var id = $(this).attr('id').replace('replypost_mask_', '');
            $(this).hide();
            $(this).next().show();
            $('#textarea_recomment_body_' + id).AutoHeight({maxHeight:200});
            $('#textarea_recomment_body_' + id).focus();
        });

        $('a[name=link_recommentparent_mask]').live('click', function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var commentid = $(this).attr('data-commentid');
            var replyMaskLink = $('#post_recomment_area_' + commentid).find('a[name=replypost_mask]');
            var submitReComment = $('#post_recomment_area_' + commentid).find('a[name=submit_recomment]');
            submitReComment.attr("data-pid", $(this).attr('data-pid'));
            submitReComment.attr('data-pname', '');
            replyMaskLink.click();
        });

        $('a[name=submit_recomment]').live('click', function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            } else {
                var pname = $(this).attr('data-pname');
                var rid = $(this).attr('data-rootid');
                var cid = $('#input_hidden_cid').val();
                var body = $('#textarea_recomment_body_' + rid).val();
                var pid = $(this).attr('data-pid');
                body = Trim(body, 'g');
                if (pname != null && pname.length != 0) {
                    body = body.replace('@' + pname + '：', '');
                }
                if (common.strLen(body) == 0) {
                    $('#post_callback_msg_' + rid).html('评论内容不能为空!');
                    $('#textarea_recomment_body_' + rid).val("");
                    $('#textarea_recomment_body_' + rid).height('26px');
                    $('#textarea_recomment_body_' + rid).focus();
                    return false;
                } else if (common.strLen(body) > 300) {
                    $('#post_callback_msg_' + rid).html('评论内容长度不能超过300个字符!');
                    return false;
                }
                comment.post({cid:cid,pid:pid,body:body,rid:rid}, callback.postReCallback);
            }
        });

        function Trim(str, is_global) {
            var result;
            result = str.replace(/(^\s+)|(\s+$)/g, "");
//            if (is_global.toLowerCase() == "g")
//                result = result.replace(/\s/g, "");
            return result;
        }

        $('a[name=chidren_reply_page]').live('click', function() {
            var cid = $('#input_hidden_cid').val();
            var rid = $(this).attr('data-rid');
            var repsum = $(this).attr('data-repsum');
            var p = $(this).attr('data-pno');
            comment.rePage($(this), {cid:cid,rid:rid,repsum:repsum,p:p}, callback.pageCallback);
        });

        $('a.remove').live('click', function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var rid = $(this).attr('data-rid');
            var rootId = $(this).attr('data-roid');
            alertOption.title = '';
            alertOption.text = '确定删除该评论？';
            alertOption.submitButtonText = "是";
            alertOption.cancelButtonText = "否";
            alertOption.submitFunction = function bind() {
                comment.remove(rid, rootId, callback.removeCallback);
            };
            joymealert.confirm(alertOption);
        });

        $('a.repost').live('click', function() {
            var roid = $(this).attr('data-roid');
            var rid = $(this).attr('data-rid');
            var name = $(this).attr('data-name');
            $('#replypost_mask_' + roid).hide();
            $('#replypost_mask_' + roid).next().show();
            $('#textarea_recomment_body_' + roid).focus();
            $('#textarea_recomment_body_' + roid).val('@' + name + '：');
            $('#submit_recomment_' + roid).attr('data-pname', name).attr('data-pid', rid);
        });

        $('a[id^=childrenreply_mood_]').live('click', function() {
            var rootid = $(this).attr('id').replace('childrenreply_mood_', '');
            var textareaID = 'textarea_recomment_body_' + rootid
            popzindex = 10062;
            var te = new TextareaEditor(document.getElementById(textareaID));
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            if (popzindex != undefined || popzindex != null) {
                config.popzindex = popzindex
            }
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);

        });

        $('#faceShow').live('click', function() {
            var textareaID = 'textarea_comment_body';
            popzindex = 10062;
            var te = new TextareaEditor(document.getElementById(textareaID));
            var config = {
                allowmultiple:true,
                isremovepop:true,
                isfocus:true
            };
            if (popzindex != undefined || popzindex != null) {
                config.popzindex = popzindex
            }
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);
        });
        /*
         mwiki点击回复判断是否登录
         */
        $('[name=clickreply]').live('click', function() {
            var cid = $('#input_hidden_cid').val();
            if (joyconfig.joyuserno == '') {
                window.location.href = "/mloginpage?reurl=" + encodeURIComponent("/mobile/comment/recentlist?cid=" + cid);
                return false;
            }
        });
        $('#form_mobile_comment').submit(function() {
            var contentid = $("#contentid").val();
            var comentbody = $("#comentbody").val();
            if (common.strLen(comentbody) == 0) {
                $("#reply_error").text('评论内容不能为空!');
                return false;
            } else if (common.strLen(comentbody) > 300) {
                $("#reply_error").text('评论内容长度不能超过300个字符!');
                return false;
            }
        });

        /*
         mwiki发送评论JS
         */
        $('[name=m_replay]').live('click', function() {
            $("[id^=child_reply_error_]").html("");
            $("[id^=reply_error_]").html("");
            var rid = $(this).attr('data-rootid');
            var cid = $('#input_hidden_cid').val();
            var pid = $(this).attr('data-pid');
            var body = '';

            if (joyconfig.joyuserno == '') {
                window.location.href = "/mloginpage?reurl=" + encodeURIComponent("/mobile/comment/recentlist?cid=" + cid);
                return false;
            } else {
                if (pid == '') {
                    body = $('#textarea_' + rid).val();
                    if (common.strLen(body) == 0) {
                        return replyErrory(1, "reply_error_", rid);
                    } else if (common.strLen(body) > 300) {
                        return  replyErrory(2, "reply_error_", rid);
                    }
                } else {
                    var pname = $(this).attr('data-pname');
                    body = $("#child_textarea_" + pid).val();
                    body = Trim(body, 'g');
                    if (pname != null && pname.length != 0) {
                        body = body.replace('@' + pname + '：', '');
                    }


                    if (common.strLen(body) == 0) {
                        return replyErrory(1, "child_reply_error_", pid);
                    } else if (common.strLen(body) > 300) {
                        return  replyErrory(2, "child_reply_error_", pid);
                    }

                }

                comment.post({cid:cid,pid:pid,body:body,rid:rid}, callback.mPostReCallback);
            }

        });
        /*
         Mwiki删除操作
         */
        $("[name=deleteReply]").live("click", function() {
            if (joyconfig.joyuserno == '') {
                window.location.href = "/mloginpage?reurl=" + encodeURIComponent("/mobile/comment/recentlist?cid=" + cid);
                return false;
            } else {
                var rid = $(this).attr('data-rid');
                if (window.confirm("确定删除该评论？")) {
                    comment.remove(rid, 0, callback.mremoveCallback);
                }
            }
        });

        /*
         Mwiki点赞
         */
        $("[name=replayagree]").live("click", function() {

            var rid = $(this).attr("data-rid");
            if (joyconfig.joyuserno == '') {
                window.location.href = "/mloginpage?reurl=" + encodeURIComponent("/mobile/comment/recentlist?cid=" + cid);
                return false;
            } else {
                comment.agree(rid, callback.magreeCallback);
            }

        });

        /*
         mwiki楼中楼翻页
         */
        $('a[name=childReplay]').live('click', function() {
            var cid = $('#input_hidden_cid').val();
            var rid = $(this).attr('data-rid');
            var repsum = $(this).attr('data-repsum');
            var p = $(this).attr('data-p');
            comment.rePage($(this), {cid:cid,rid:rid,repsum:repsum,p:p}, callback.mpageCallback);
        });
    });


    //MWIKI评论错误提示
    /**
     * param status 1为评论为空 2是评论内容过长
     * param category   reply_error_为主楼错误   child_reply_error_为楼中楼
     * param id 楼层ID
     */

    function replyErrory(status, category, id) {
        if (status == 1) {
            $('#' + category + id).html('评论内容不能为空!');
            $('#' + category + id).focus();
            return false;
        } else {
            $('#' + category + id).html('评论内容长度不能超过300个字符!');
            return false;
        }
    }

    var callback = {
        postReCallback:function(rid, pid, resMsg) {
            if (resMsg.rs == '0') {
                $('#post_callback_msg_' + rid).html(resMsg.msg);
                return;
            } else {
                var result = resMsg.result;
                if (result == null) {

                } else {
                    var html = getReCommentHtml(result);
                    $('#children_reply_list_' + result.reply.rootId).find('p').remove();
                    $('#children_reply_list_' + result.reply.rootId).append(html);
                    $('#submit_recomment_' + result.reply.rootId).removeAttr('data-pname').removeAttr('data-pid');
                    $('#textarea_recomment_body_' + result.reply.rootId).val('');
                    $('#post_callback_msg_' + rid).html("");
                    var closeSum = parseInt($('#link_toggle_recomment_close_' + result.reply.rootId).attr('data-reply-sum'));
                    $('#link_toggle_recomment_close_' + result.reply.rootId).attr('data-reply-sum', closeSum + 1);
                    var openSum = parseInt($('#link_toggle_recomment_open_' + result.reply.rootId).attr('data-reply-sum'));
                    $('#link_toggle_recomment_open_' + result.reply.rootId).attr('data-reply-sum', openSum + 1);
                    $('#replypost_mask_' + result.reply.rootId).next().hide();
                    $('#replypost_mask_' + result.reply.rootId).show();

                }
            }
        },
        //MWIKI 评论回调
        mPostReCallback:function(rid, pid, resMsg) {
            if (resMsg.rs == '0') {
                if (pid == "") {
                    $("#reply_error_" + rid).html(resMsg.msg);
                    $("#textarea_" + rid).focus();
                } else {
                    $("#child_reply_error_" + pid).html(resMsg.msg);
                    $("#child_textarea_" + pid).focus();

                }

                return false;
            } else {
                var result = resMsg.result;
                if (result == null) {

                } else {


                    $("#appenddiv_" + rid + "").before(getMobileReplyList(result, pid, rid));

                    if (pid == "") {
                        $("#textarea_" + rid).val("");
                        $("#replybox_" + rid).hide();
                        $("#reply_error_" + rid).html("");
                    } else {
                        $("#child_textarea_" + pid).val("@" + result.parentProfile.name + "：");
                        $("#m_replybox_" + pid).hide();
                        $("#child_reply_error_" + pid).html("");
                    }

                }
            }
        },
        agreeCallback:function(rid, resMsg) {
            if (resMsg.rs == '0') {
                alertOption.text = resMsg.msg;
                alertOption.title = '';
                joymealert.alert(alertOption);
                return;
            } else {
                var numObj = $('a[name=agree_num][data-commentid=' + rid + ']');
                var objStr = numObj.html();
                var num;
                if (numObj.length == 0 || objStr == null || objStr.length == 0) {
                    num = parseInt(1);
                } else {
                    var num = numObj.html().replace("(", '').replace(')', '');
                    num = parseInt(num);
                    num = num + 1;
                }
                numObj.html('(' + num + ')');
            }
        },magreeCallback:function(rid, resMsg) {
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else {
                var num = $('#replyagree_' + rid).text();
                num = parseInt(num);
                $("#replyagree_" + rid).text(num + 1)
            }
        },
        pageCallback:function(pageDom, resMsg) {
            if (resMsg.rs == '0') {
                alertOption.text = resMsg.msg;
                alertOption.title = '';
                joymealert.alert(alertOption);
                return;
            } else {
                var result = resMsg.result;
                if (result == null) {

                } else {
                    var html = getReCommentListCallBack(result);
                    $('#children_reply_list_' + result.reply.reply.replyId).html(html);
                    var page = result.reReplys.page;
                    calPageDom(page, pageDom);
                }
            }
        },
        mpageCallback:function(pageDom, resMsg) {
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else {
                var result = resMsg.result;
                if (result == null) {

                } else {
                    var replyList = result.reReplys.rows;
                    if (replyList != null && replyList.length > 0) {

                        $("#replyed_" + result.reply.reply.replyId).empty();
                        for (var i = 0; i < replyList.length; i++) {
                            $("#replyed_" + replyList[i].reply.rootId).append(getMobileReplyList(replyList[i], replyList[i].reply.partentId, replyList[i].reply.rootId));
                        }
                    } else {
                        $("#replyed_" + result.reply.reply.replyId).empty();
                        var html = '<div class="replyed" id="replyed_' + result.reply.reply.replyId + '">' +
                                '<div class="replyed-list">' +
                                '<p>' +
                                '当前页的评论已经被删除~' +
                                '</p>' +
                                '</div>' +
                                '</div>';
                        $("#replyed_" + result.reply.reply.replyId).append(html);
                    }
                    //回复追加的标志位
                    var appenddiv = '<div id="appenddiv_' + result.reply.reply.replyId + '"></div>';
                    $("#replyed_" + result.reply.reply.replyId).append(appenddiv);
                    var pagehtml = replypage(result.reReplys.page, result.reply.reply.replyId);
                    $("#replyed_" + result.reply.reply.replyId).append(pagehtml);

                }
            }
        },
        removeCallback:function(rid, rootId, resMsg) {
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else if (resMsg.rs == '1') {
                $('div[name=cont_cmt_list_' + rid + ']').remove();
                var sum = $('#link_toggle_recomment_close_' + rootId).attr('data-reply-sum');
                if (parseInt(sum) > 0) {
                    $('#link_toggle_recomment_close_' + rootId).attr('data-reply-sum', sum - 1);
                    $('#link_toggle_recomment_open_' + rootId).attr('data-reply-sum', sum - 1);
                }
            }
        },
        mremoveCallback:function(rid, rootId, resMsg) {
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else if (resMsg.rs == '1') {
                $('[name=remove_reply_' + rid + ']').remove();

            }
        }
    }

    function getMobileReplyList(result, pid, rid) {

        var profilename = result.parentProfile;

        if (profilename != "" && profilename != null) {
            profilename = "@" + result.parentProfile.name + "：";
        } else {
            profilename = "";
        }
        var deleteurl = '';
        if (joyconfig.joyuserno == result.reply.replyUno) {
            deleteurl = '<span class="sc" name="deleteReply"  data-rid="' + result.reply.replyId + '">删除</span>'
        }
        var appendhtml = '<div class="replyed-list" name="remove_reply_' + result.reply.replyId + '">' +
                '<div class="aut">' +
                '<div class="fl">' +
                '<span class="user"><i>•</i>' + result.profile.name + ':</span>' +
                '</div>' +
                '<div class="fr">' +
                '<span class="dz" name="replayagree" id="replyagree_' + result.reply.replyId + '" data-rid="' + result.reply.replyId + '">' + result.reply.agreeNum + '</span>' +
                deleteurl +
                '<span class="reply" onclick="changeReply(this)" name="clickreply">回复</span>' +
                '</div>' +
                '</div>' +
                '<div class="comment-content">' + profilename + result.reply.body +
                '</div>' +
                '<div class="replybox" id="m_replybox_' + result.reply.replyId + '">' +
                '<div style="margin-top:6px;"><textarea placeholder="说点儿什么吧" id="child_textarea_' + result.reply.replyId + '">@' + result.profile.name + '：</textarea></div>' +
                '<span style="color: #f00; " id="child_reply_error_' + result.reply.replyId + '"></span>' +
                '<input type="submit" value="回复" data-rootid="' + rid + '" ' +
                'data-cid="' + result.reply.contentId + '"' +
                'data-pid="' + result.reply.replyId + '" ' +
                'data-pname="' + result.profile.name + '"' +
                'id="m_reply_' + rid + '"' +
                'name="m_replay"/>' +
                '</div>' +
                '</div>';

        return appendhtml;
    }

    function replypage(page, rid) {
        var maxpage = page.maxPage;
        var repsum = page.totalRows;
        var prepage = page.curPage > 1 ? page.curPage - 1 : 1;
        var lastpage = page.curPage < page.maxPage ? page.curPage + 1 : page.maxPage;
        var preclickname = "childReplay";
        var lastclickname = "childReplay";
        var preclassname = "";
        var lastclassname = "";
        if (page.curPage <= 1) {
            preclickname = "";
            preclassname = "disabled"
        }
        if (page.curPage >= page.maxPage) {
            lastclickname = "";
            lastclassname = "disabled"
        }


        var html = '<div class="pages pages2">' +
                '<div><a href="javascript:void(0)" class="' + preclassname + '" name="' + preclickname + '" ' +
                'data-rid="' + rid + '" ' +
                'data-p="1" ' +
                'data-repsum="' + repsum + '">首页</a></div>' +
                '<div><a href="javascript:void(0)" class="' + preclassname + '" name="' + preclickname + '" ' +
                'data-rid="' + rid + '" ' +
                'data-p="' + prepage + '" ' +
                'data-repsum="' + repsum + '">上一页</a></div>' +
                '<div>' + page.curPage + '/' + maxpage + '  </div>' +
                '<div><a href="javascript:void(0)" class="' + lastclassname + '" name="' + lastclickname + '" ' +
                'data-rid="' + rid + '" ' +
                'data-p="' + lastpage + '" ' +
                'data-repsum="' + repsum + '">下一页</a></div>' +
                '<div><a href="javascript:void(0)" class="' + lastclassname + '" name="' + lastclickname + '" ' +
                'data-rid="' + rid + '" ' +
                'data-p="' + maxpage + '" ' +
                'data-repsum="' + repsum + '">尾页</a></div>';
        return html;
    }

    function getCommentListCallBack(commentObj) {
        var comment = commentObj.reply;
        var page = commentObj.reReplys.page;
        var reCommentArray = null;
        if (commentObj.reReplys != null) {
            reCommentArray = commentObj.reReplys.rows;
        }

        var hasRe = (reCommentArray != null && reCommentArray.length > 0);

        var reCommentListHtml = '';
        if (hasRe) {
            reCommentListHtml += '<div id="children_reply_list_' + comment.reply.replyId + '">';
            for (var i = 0; i < reCommentArray.length; i++) {
                var reCommentObj = reCommentArray[i];
                reCommentListHtml += getReCommentHtml(reCommentObj);
            }
            reCommentListHtml += '</div>';

            calPageDom(page, $('a[name=chidren_reply_page]'));

        }

        var toogleReHtml = '<a name="link_toggle_recomment" href="javascript:void(0);" class="putaway">收起回复</a>';
        if (!hasRe) {
            toogleReHtml = '<a name="link_toggle_recomment" href="javascript:void(0);" >回复</a>';
        }

        var postReCommentHtml = '<div id="post_recomment_area_' + comment.reply.replyId + '" class="discuss_reply">' +
                ' <a class="discuss_text01" href="javascript:void(0);" name="replypost_mask">我也说一句</a>' +
                '<div style="display:none" class="discuss_reply reply_box01">' +
                '<textarea warp="off" style="font-family:Tahoma, \'宋体\';" id="textarea_recomment_body_' + comment.reply.replyId + '" class="discuss_text focus" rows="" cols="" name="content"></textarea>' +
                '<div class="related clearfix">' +
                '<div class="transmit clearfix">' +
                ' <a class="submitbtn fr" name="submit_recomment"  data-rootid="' + comment.reply.replyId + '" name="childreply_submit"><span>评 论</span></a>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>';


        var html = '<div name="cont_cmt_list_' + comment.reply.replyId + '" class="area blogopinion clearfix ">' +
                '<dl>' +
                '<dt class="personface">' +
                '<a title="' + comment.profile.name + '" name="atLink" href="http://www.joyme.com/people/' + comment.profile.name + '">' +
                '<img width="58" height="58" class="user" src="' + comment.profile.headicon + '">' +
                '</a>' +
                '</dt>' +
                '<dd class="textcon discuss_building">' +
                '<em>#1</em>' +
                '<a title="' + comment.profile.name + '" class="author" name="atLink" href="http://www.joyme.com/people/' + comment.profile.domain + '">' + comment.profile.name + '</a>' +
                '<p>' + comment.reply.body + '</p>' +
                '<div class="discuss_bdfoot">' + comment.reply.publistDate + '&nbsp;<a href="javascript:void(0);" id="agreelink_' + comment.reply.replyId + '" data-commentid="' + comment.reply.replyId + '" class="dianzan"></a><span id="agreenum_' + comment.reply.replyId + '">' + ((comment.reply.agreeNum == null || comment.reply.agreeNum == 0) ? '' : ('(' + comment.reply.agreeNum + ')')) + '</span>' + toogleReHtml + '</div>' +
                '<div class="discuss_bd_list discuss_border" ' + (hasRe ? '' : 'style="display:none"') + '> ' +
                reCommentListHtml +
                postReCommentHtml +
                '</div> ' +
                '</dd>' +

                '</dl>' +
                '</div>';
        return html;
    }

    function getReCommentListCallBack(commentObj) {
        var reCommentArray = null;
        if (commentObj.reReplys != null) {
            reCommentArray = commentObj.reReplys.rows;
        }

        var hasRe = (reCommentArray != null && reCommentArray.length > 0);

        var reCommentListHtml = '';
        if (hasRe) {
            for (var i = 0; i < reCommentArray.length; i++) {
                var reCommentObj = reCommentArray[i];
                reCommentListHtml += getReCommentHtml(reCommentObj);
            }
        } else {
            reCommentListHtml += '<p>当前页的评论已经被删除~</p>';
        }

        return reCommentListHtml;
    }

    var calPageDom = function(page, pageDom) {
        var pno = parseInt(pageDom.attr('data-pno'));
        var cid = pageDom.attr('data-cid');
        var repsum = pageDom.attr('data-repsum');
        var rid = pageDom.attr('data-rid');

        var pageAreaDom = pageDom.parent();

        var lastPageNo = parseInt(pageAreaDom.attr('data-end'));
        var startPageNo = parseInt(pageAreaDom.attr('data-start'));

        var pageEnd = lastPageNo;
        var pageStart = startPageNo;
        if (page.curPage >= lastPageNo) {
            pageEnd = page.curPage + 1 > page.maxPage ? page.maxPage : page.curPage + 1;
            pageStart = pageEnd - 5 < 1 ? 1 : pageEnd - 5;
        } else if (page.curPage <= startPageNo) {
            pageStart = page.curPage <= 1 ? 1 : page.curPage - 1;
            pageEnd = pageStart + 5 > page.maxPage ? page.maxPage : pageStart + 5;
        }

        pageAreaDom.attr('data-end', pageEnd);
        pageAreaDom.attr('data-start', pageStart);


        var pageNoHtml = '';
        for (var i = pageStart; i <= pageEnd; i++) {
            if (i == page.curPage) {
                pageNoHtml += '<b>' + i + '</b> ';
            } else {
                pageNoHtml += '<a name="chidren_reply_page"  href="javascript:void(0)" data-pno="' + i + '" data-cid="' + cid + '" data-repsum="' + repsum + '" data-rid="' + rid + '">' + i + '</a> ';
            }
        }

        //高亮显示
        var headPageHtml = '';
        if (pno > pageStart) {
            headPageHtml += '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="1" data-cid="' + cid + '" data-repsum="' + repsum + '" data-rid="' + rid + '">首页</a> ' +
                    '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="' + (pno - 1 < 1 ? 1 : pno - 1) + '" data-cid="' + cid + '" data-repsum="' + repsum + '" data-rid="' + rid + '">上一页</a> ';
        }

        var footPageHtml = '';
        if (pno < pageEnd) {
            footPageHtml += '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="' + (pno + 1 > page.maxPage ? page.maxPage : pno + 1) + '" data-cid="' + cid + '" data-repsum="' + repsum + '" data-rid="' + rid + '">下一页</a> ' +
                    '<a name="chidren_reply_page" href="javascript:void(0);" data-pno="' + page.maxPage + '" data-cid="' + cid + '" data-repsum="' + repsum + '" data-rid="' + rid + '">末页</a> ';
        }

        var pageHtml = headPageHtml + pageNoHtml + footPageHtml;
        pageAreaDom.html(pageHtml);
    }

    function getReCommentHtml(reCommentObj) {
        var vipHtml = "";
        if (reCommentObj.profile.verifyType != null && reCommentObj.profile.verifyType.code != 'n') {
            if (reCommentObj.profile.verifyType.code == 'c') {
                vipHtml += '<a title="着迷机构认证" class="cvip" href="http://www.joyme.com/people/' + reCommentObj.profile.domain + '"></a>';
            } else if (reCommentObj.profile.verifyType.code == 'p') {
                vipHtml += '<a title="着迷达人认证" class="pvip" href="http://www.joyme.com/people/' + reCommentObj.profile.domain + '"></a>';
            } else if (reCommentObj.profile.verifyType.code == 'i') {
                vipHtml += '<a title="着迷人物认证" class="ivip" href="http://www.joyme.com/people/' + reCommentObj.profile.domain + '"></a>';
            }
        }
        var pNameHtml = "";
        if (reCommentObj.parentProfile != null) {
            pNameHtml = "@" + reCommentObj.parentProfile.name;
        }
        var agreeHtml = "";
        var agreeNumHtml = "";
        if (parseInt(reCommentObj.reply.agreeNum) > 0) {
            agreeNumHtml += '(' + reCommentObj.reply.agreeNum + ')';
        }
        agreeHtml += '<a href="javascript:void(0);" id="agreelink_' + reCommentObj.reply.replyId + '" data-commentid="' + reCommentObj.reply.replyId + '" class="dianzan"></a><span id="agreenum_' + reCommentObj.reply.replyId + '"><a href="javascript:void(0);" name="agree_num" data-commentid="' + reCommentObj.reply.replyId + '">' +
                agreeNumHtml +
                '</a></span>&nbsp;';

        var removeHtml = '';
        if (joyconfig.joyuserno == reCommentObj.reply.replyUno) {

            removeHtml += '<a href="javascript:void(0);" class="remove" data-rid="' + reCommentObj.reply.replyId + '" data-roid="' + reCommentObj.reply.rootId + '">删除</a>&nbsp;';
        }
        var reCommentHtml = '<div style="" name="cont_cmt_list_' + reCommentObj.reply.replyId + '" class="conmenttx clearfix">' +
                '<div class="conmentface">' +
                '<div class="commenfacecon">' +
                '<a href="http://www.joyme.com/people/' + reCommentObj.profile.domain + '" title="' + reCommentObj.profile.name + '" name="atLink" class="cont_cl_left">' +
                '<img width="33px" height="33px" src="' + reCommentObj.profile.headicon + '">' +
                '</a>' +
                '</div>' +
                '</div>' +
                '<div class="conmentcon">' +
                '<a title="' + reCommentObj.profile.name + '" name="atLink" href="http://www.joyme.com/people/' + reCommentObj.profile.domain + '">' + reCommentObj.profile.name + '</a>' +
                vipHtml +
                pNameHtml +
                '：' + reCommentObj.reply.body + '<div class="commontft clearfix"><span class="reply_time">' + reCommentObj.reply.publistDate + '</span>' +
                '<span class="delete">' +
                agreeHtml +
                removeHtml +
                '<a href="javascript:void(0);" class="repost" data-roid="' + reCommentObj.reply.rootId + '" data-rid="' + reCommentObj.reply.replyId + '" data-name="' + reCommentObj.profile.name + '">回复</a>' +
                '</span>' +
                '</div>' +
                '</div>' +
                '</div>';
        return reCommentHtml;
    }

    var replylivemood = function(moodDom, textareaID, popzindex) {
        moodDom.live('click', function() {

        });
    }

    function addDocmentListerner() {
        $(document).click(function(event) {
            var event = window.event ? window.event : event;
            var target = event.srcElement || event.target;

            if (target.tagName == null || target.id == 'mood') {
                return;
            } else if (target.tagName.toLowerCase() == 'a') {
                if (target.id != '' && $('#' + target.id).parents('.pop').length > 0) {
                    return;
                }
            }
            moodBiz.hideFace();
        })
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});