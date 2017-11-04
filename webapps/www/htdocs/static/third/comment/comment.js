$(document).ready(function() {
    window.uno = getCookie('joyme_u');
    getCommentList(contentid, 'desc');
    getErrorInfo();
    initPostMask();

    $('#comment_select>a').click(function() {
        var label = $(this).html().replace('<i>√</i>', '').replace('<I>√</I>', '');
        var currentLael = $('#comment_label').html();
        if (label == currentLael) {
            return;
        }

        $('#comment_select>a').removeClass('current');
        $(this).addClass('current');
        $('#comment_label').html(label);

        if ($(this).hasClass('hotest')) {
            getHotCommentList(contentid)
        } else if ($(this).hasClass('lastest')) {
            getCommentList(contentid, 'desc');
        } else if ($(this).hasClass('oldest')) {
            getCommentList(contentid, 'asc');
        }
    });

    //发表评论
    $('#comment_submit').click(function() {
        postComment();
    });

    //点赞
    $(document).on('click', 'a[id^=agreelink_]', function() {
        var replyid = $(this).attr('data-commentid');
        agreeComment(replyid);
    });

    $(document).on('click', 'a[name=togglechildrenreply_area]', function() {
        if ($(this).hasClass('putaway')) {
            $(this).parent().next().fadeOut();
            var replyNum = parseInt($(this).attr('data-replynum'));
            var html = '回复';
            if (replyNum > 0) {
                html += '(' + replyNum + ')'
            }
            $(this).html(html).removeClass();
        } else {
            $(this).parent().next().fadeIn();
            $(this).html('收起回复').attr('class', 'putaway');
        }
    });

    //回复
    $(document).on('click', 'a[name=replypost_mask]', function() {
        $(this).hide();
        var obj = $(this).next();
        obj.show();
        var textAreaObj = obj.find('textarea[id^=textarea_recomment_body_]');
        var body = textAreaObj.val();
        textAreaObj.val("").focus().val(body);
        textAreaObj.AutoHeight({maxHeight:200});
    });

    $(document).on('click', 'a[name=link_recommentparent_mask]', function() {
        var commentid = $(this).attr('data-rootid');
        var nickname = $(this).attr('data-nick');
        var postCommentArea = $('#post_recomment_area_' + commentid);
        var replyMaskLink = postCommentArea.find('a[name=replypost_mask]');
        var submitReComment = postCommentArea.find('a[name=submit_recomment]');
        submitReComment.attr("data-pid", $(this).attr('data-pid'));
        $('#textarea_recomment_body_' + commentid).val('@' + nickname + '：');
        replyMaskLink.click();
    });


    $(document).on('click', 'a[name=submit_recomment]', function() {
        var rid = $(this).attr('data-rid');
        var body = $('#textarea_recomment_body_' + rid).val().trim();
        var pid = $(this).attr('data-pid');
        postreComment(rid, pid, body, $(this));
    });

    $(document).on('click', 'a[name=remove_comment]', function() {
        var rid = $(this).attr('data-rid');
        if (confirm('确定要删除吗？')) {
            removeComment(rid);
        }
    });

    $(document).click(function(event) {
        var event = window.event ? window.event : event;
        var target = event.srcElement || event.target;

        if (target.tagName == null || target.name == 'replypost_mask' || target.tagName.toLowerCase() == 'a') {
            return;
        }

        var textAreas = $('textarea[id^=textarea_recomment_body_]:visible');
        $.each(textAreas, function(i, val) {
            var textArea = $(val);
            var id = textArea.attr('id');
            var rid = id.substr(id.lastIndexOf('_') + 1, id.length);
            if (textArea.val() != null && textArea.val().length > 0 && $('#post_callback_msg_' + rid).length > 0) {
                return;
            }
            textArea.parent().hide();
            textArea.parent().prev().show();
            textArea.val('').css('heigth', '22px');
        });
    })


    $('#textarea_body').bind('focusin',
            function() {
                var val = $(this).val();
                if (val == '我来说两句…') {
                    $(this).val('').css('color', '#333');
                }
            }).bind('focusout', function() {
                var val = $(this).val();
                if (val == null || val == '') {
                    $(this).val('我来说两句…').css('color', '#ccc');
                }
            });
});

function getErrorInfo() {
    var url = window.location.href;
    var pos = url.indexOf("error=");
    if (pos >= 0) {
        var errorInfo = url.substring(pos + 'error='.length, url.length);
        pos = errorInfo.indexOf("#");
        if (pos >= 0) {
            errorInfo = errorInfo.substring(0, pos);
        }

        var info = errorArray[errorInfo];
        if (info != null && info.length > 0) {
            if (errorInfo == 'reply.not.exist' || errorInfo == 'comment.body.illegl') {
                alert(errorArray[errorInfo])
                return;
            }

            $('#reply_error').html(info);
        }
    }
}

function getCommentList(cid, sort) {
    $.ajax({
                url : "http://www.joyme.com/jsonp/api/comment/commentlist",
                type : "post",
                async:false,
                data:"cid=" + cid + "&sort=" + sort,
                dataType : "jsonp",
                jsonpCallback:"commentlistcallback",
                success : function(req) {
                    var resMsg = req[0];
                    if (resMsg.rs == '0') {
                        return;
                    } else {
                        var result = resMsg.result;
                        var replyNum = '<a href="' + morelink + '">' + result.replyNum + '<span style="color:#494949">条评论</span></a>'
                        $('#reply_num').html(replyNum);
                        if (result.replyList == null || result.replyList.length == 0) {
                            $('#comment_list_area').html('<p style="height:240px; line-height:160px; text-align:center">目前没有评论，欢迎你发表评论</p>')
                        } else {
                            var html = '';
                            for (var i = 0; i < result.replyList.length; i++) {
                                html += getCommentListCallBack(result.replyList[i]);
                            }
                            var moreLink = '<div class="more-comment"><a href="' + morelink + '">更多评论&gt;&gt;</a></div>';
                            html += moreLink;
                            $('#comment_list_area').html(html);
                            $('.comment-sort').show();
                        }
                    }
                },
                error:function() {
                    alert('获取失败，请刷新');
                }
            });
}

function getHotCommentList(contentid) {
    $.ajax({
                url : "http://www.joyme.com/jsonp/api/comment/hotlist",
                type : "post",
                async:false,
                data:{cid:contentid},
                dataType : "jsonp",
                jsonpCallback:"hotlistcallback",
                success : function(req) {
                    var resMsg = req[0];
                    if (resMsg.rs == '0') {
                        return;
                    } else {
                        var result = resMsg.result;
                        var replyNum = '<a href="' + morelink + '">' + result.replyNum + '<span style="color:#494949">条评论</span></a>'
                        $('#reply_num').html(replyNum);
                        if (result.replyList == null || result.replyList.length == 0) {
                            $('#comment_list_area').html('<p style="height:240px; line-height:160px; text-align:center">目前没有评论，欢迎你发表评论</p>')
                        } else {
                            var html = '';
                            for (var i = 0; i < result.replyList.length; i++) {
                                html += getCommentListCallBack(result.replyList[i]);
                            }
                            var moreLink = '<div class="more-comment"><a href="' + morelink + '">更多评论&gt;&gt;</a></div>';
                            html += moreLink;
                            $('#comment_list_area').html(html);
                            $('.comment-sort').show();
                        }
                    }
                },
                error:function() {
                    alert('获取失败，请刷新');
                }
            });
}

function postComment() {
    if (window.uno == null || window.uno.length == 0) {
        var loginHref = 'http://www.joyme.com/loginpage?reurl=' + window.location.href;
        window.location.href = loginHref;
        return;
    }

    var body = $('#textarea_body').val();
    if (body == null || body.length == 0 || body == '我来说两句…') {
        $('#reply_error').html('评论内容不能为空');
        return false;
    }
    if (getStrlen(body) > 300) {
        $('#reply_error').html('评论内容长度不能超过300字符!');
        return false;
    }

    $('#post_comment').submit();
}

function postreComment(rid, pid, body, reCommentDom) {
    if (window.uno == null || window.uno.length == 0) {
        var loginHref = 'http://www.joyme.com/loginpage?reurl=' + window.location.href;
        window.location.href = loginHref;
        return;
    }

    if (body == null || body.length == 0 || body == '我来说两句…') {
        $('#post_callback_msg_' + rid).remove();
        reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + rid + '">评论内容不能为空!</span>');
        return false;
    }

    if (getStrlen(body) > 300) {
        $('#post_callback_msg_' + rid).remove();
        reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + rid + '">评论内容长度不能超过300个字符!</span>');
        return false;
    }

    $.ajax({
                url : "http://www.joyme.com/jsonp/api/comment/post",
                type : "post",
                async:false,
                data:{cid:contentid,pid:pid,rid:rid,body:body},
                dataType : "jsonp",
                jsonpCallback:"postcomment",
                success : function(req) {
                    var resMsg = req[0];
                    if (resMsg.rs == '0') {
                        $('#post_callback_msg_' + rid).remove();
                        reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + rid + '">'+resMsg.msg+'</span>');
                        return;
                    } else {
                        var result = resMsg.result;
                        var html = getReCommentHtml(result);
                        $('#children_reply_list_' + rid).append(html);
                    }
                },
                error:function() {
                    alert('获取失败，请刷新');
                }
            });


//    $('#hidden_recomment_rid').val(rid);
//    if (pid != null) {
//        $('#hidden_recomment_pid').val(pid);
//    }
//    $('#hidden_recomment_body').val(body);
//    $('#post_recomment').submit();
}

function agreeComment(rid) {
    if (window.uno == null || window.uno.length == 0) {
        var loginHref = 'http://www.joyme.com/loginpage?reurl=' + window.location.href;
        window.location.href = loginHref;
    }

    $.ajax({
                url : "http://www.joyme.com/jsonp/api/comment/agree",
                type : "post",
                async:false,
                data:{rid:rid},
                dataType : "jsonp",
                jsonpCallback:"agreecallback",
                success : function(req) {
                    var resMsg = req[0];
                    if (resMsg.rs == '-1') {
                        var loginHref = 'http://www.joyme.com/loginpage?reurl=' + window.location.href;
                        window.location.href = loginHref;
                        return;
                    } else if (resMsg.rs == '0') {
                        var info = errorArray[resMsg.msg];
                        if (info != null && info.length > 0) {
                            alert(info)
                        }
                        return;
                    } else {
                        var numStr = $('#agreenum_' + rid).html();
                        if (numStr == null || numStr.length == 0) {
                            numStr = '(1)';
                        } else {
                            var num = parseInt(numStr.replace('(', '').replace(')', '').trim());
                            numStr = '(' + (num + 1) + ')';
                        }
                        $('#agreenum_' + rid).html(numStr);
                    }
                },
                error:function() {
                    alert('获取失败，请刷新');
                }
            });
}

function initPostMask() {
    if (window.uno == null || window.uno.length == 0) {
        $('#textarea_body').after('<div class="wrapper_unlogin" style="text-align:center; margin:-65px 0 65px">您需要<a id="login_link" href="http://www.joyme.com/loginpage?reurl=' + window.location.href + '">登录</a>后才能评论</div>');
        $('#textarea_body').attr('disabled', 'disabled');
    } else {
        $('#textarea_body').val('我来说两句…').css('color', '#ccc');
    }
}

function removeComment(rid) {
    if (window.uno == null || window.uno.length == 0) {
        var loginHref = 'http://www.joyme.com/loginpage?reurl=' + window.location.href;
        window.location.href = loginHref;
    }

    $('#hidden_del_comment_rid').val(rid);
    $('#delete_recomment').submit();
}


function getCommentListCallBack(commentObj) {
    var comment = commentObj.reply;
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
        if (commentObj.reReplys.page != null && commentObj.reReplys.page.maxPage > 1) {
            reCommentListHtml += '<div class="more-comment"><a href="' + morelink + '">更多回复&gt;&gt;</a></div>';
        }
        reCommentListHtml += '</div>';
    }

    var removeHtml = '';
    if (window.uno != '' && window.uno == comment.reply.replyUno) {
        removeHtml = '<a name="remove_comment" href="javascript:void(0);" data-rid="' + comment.reply.replyId + '" >删除</a>'
    }

    var toogleReHtml = '<a name="togglechildrenreply_area" href="javascript:void(0);" data-replynum="' + comment.reply.replyNum + '">回复' + (comment.reply.replyNum > 0 ? ('(' + comment.reply.replyNum + ')') : '') + '</a>';

    var postReCommentHtml = '<div id="post_recomment_area_' + comment.reply.replyId + '" class="discuss_reply">' +
            ' <a class="discuss_text01" href="javascript:void(0);" name="replypost_mask">我也说一句</a>' +
            '<div style="display:none" class="discuss_reply reply_box01">' +
            '<textarea warp="off" style="font-family:Tahoma, \'宋体\';" id="textarea_recomment_body_' + comment.reply.replyId + '" class="discuss_text focus" rows="" cols="" name="content"></textarea>' +
            '<div class="related clearfix">' +
            '<div class="transmit clearfix">' +
            ' <a class="submitbtn fr" name="submit_recomment"  data-rid="' + comment.reply.replyId + '" name="childreply_submit"><span>评 论</span></a>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>';


    var html = '<div id="cont_cmt_list_' + comment.reply.replyId + '" class="area blogopinion clearfix ">' +
            '<dl>' +
            '<dt class="personface">' +
            '<a title="' + comment.profile.name + '" name="atLink" href="http://www.joyme.com/people/' + comment.profile.domain + '">' +
            '<img width="58" height="58" class="user" src="' + comment.profile.headicon + '">' +
            '</a>' +
            '</dt>' +
            '<dd class="textcon discuss_building">' +
            '<a title="' + comment.profile.name + '" class="author" name="atLink" href="http://www.joyme.com/people/' + comment.profile.domain + '">' + comment.profile.name + '</a>' +
            '<p>' + comment.reply.body + '</p>' +
            '<div class="discuss_bdfoot">' + comment.reply.publistDate + '&nbsp;<a href="javascript:void(0);" id="agreelink_' + comment.reply.replyId + '" data-commentid="' + comment.reply.replyId + '" class="dianzan"></a><span id="agreenum_' + comment.reply.replyId + '">' + ((comment.reply.agreeNum == null || comment.reply.agreeNum == 0) ? '' : ('(' + comment.reply.agreeNum + ')')) + '</span>' + removeHtml + toogleReHtml + '</div>' +
            '<div class="discuss_bd_list discuss_border" style="display:none"> ' +
            reCommentListHtml +
            postReCommentHtml +
            '</div> ' +
            '</dd>' +
            '</dl>' +
            '</div>';
    return html;
}

function getReCommentHtml(reCommentObj) {
    var removeHtml = '';
    if (window.uno != '' && window.uno == reCommentObj.reply.replyUno) {
        removeHtml = '<a name="remove_comment" href="javascript:void(0);" data-rid="' + reCommentObj.reply.replyId + '" >删除</a>'
    }

    var reCommentHtml = '<div style="" id="cont_recmt_list_' + reCommentObj.reply.replyId + '" class="conmenttx clearfix">' +
            '<div class="conmentface">' +
            '<div class="commenfacecon">' +
            '<a href="http://www.joyme.com/people/' + reCommentObj.profile.domain + '" title="' + reCommentObj.profile.name + '" name="atLink" class="cont_cl_left">' +
            '<img width="33px" height="33px" src="' + reCommentObj.profile.headicon + '">' +
            '</a>' +
            '</div>' +
            '</div>' +
            '<div class="conmentcon">' +
            '<a title="' + reCommentObj.profile.name + '" name="atLink" href="http://www.joyme.com/people/' + reCommentObj.profile.domain + '">' + reCommentObj.profile.name + '</a>' +
            '：' + reCommentObj.reply.body + '<div class="commontft clearfix"><span class="reply_time">' + reCommentObj.reply.publistDate + '</span>' +
            '<span class="delete">' +
            '<a href="javascript:void(0);" id="agreelink_' + reCommentObj.reply.replyId + '" data-commentid="' + reCommentObj.reply.replyId + '" class="dianzan"></a>' +
            '<span id="agreenum_' + reCommentObj.reply.replyId + '">' + ((reCommentObj.reply.agreeNum == null || reCommentObj.reply.agreeNum == 0) ? '' : ('(' + reCommentObj.reply.agreeNum + ')')) + '</span>' +
            removeHtml +
            '<a href="javascript:void(0);"  name="link_recommentparent_mask" data-nick="' + reCommentObj.profile.name + '" data-rootid="' + reCommentObj.reply.rootId + '" data-pid="' + reCommentObj.reply.replyId + '">回复</a>' +
            '</span>' +
            '</div>' +
            '</div>' +
            '</div>';
    return reCommentHtml;
}

var errorArray = {'comment.post.limit':'您说话的频率太快了，请歇一歇哦~',
    'user.has.agree':'你已经支持过了',
    'comment.body.empty': '评论内容不能为空',
    'content.isnot.exists':'文章不存在！',
    'reply.not.exist':'该评论已经被删除，无法回复！',
    'comment.body.illegl':'评论中含有不适当的内容！'}

function getCookie(objName) {
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i ++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName && temp[1] != '\'\'' && temp[1] != "\"\"") {
            return unescape(temp[1]);
        }
    }
    return null;
}

function getStrlen(str) {
    if (str == null || str.length == 0) {
        return 0;
    }
    var len = str.length;
    var reLen = 0;
    for (var i = 0; i < len; i++) {
        if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) {
            // 全角
            reLen += 1;
        } else {
            reLen += 0.5;
        }
    }
    return Math.ceil(reLen);
}

$.fn.AutoHeight = function(options) {
    var defaults = {
        maxHeight:null,
        minHeight:$(this).height()
    };
    var opts = (typeof (options) === 'object') ? $.extend({}, defaults, options) : {};
    this.each(function() {
        $(this).bind("paste cut keydown keyup focus blur", function() {
            var height,style = this.style;
            this.style.height = opts.minHeight + 'px';
            if (this.scrollHeight > opts.minHeight) {
                if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
                    height = opts.maxHeight;
                    style.overflowY = 'scroll';
                } else {
                    if ('\v' == 'v') {
                        height = this.scrollHeight - 2;
                    } else {
                        height = this.scrollHeight;
                    }
                    style.overflowY = 'hidden';
                }
                style.height = height + 'px';
            }
        });
    });
    return this;
};
