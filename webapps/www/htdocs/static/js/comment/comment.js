$(document).ready(function () {
    var joymeEnv = window.location.host.substring(window.location.host.lastIndexOf('.') + 1, window.location.host.length);
    var joymeEnvNoPort = joymeEnv;
    if (joymeEnv.indexOf(':') > 0) {
        joymeEnvNoPort = joymeEnv.substring(0, joymeEnv.indexOf(':'));
    }
    window.www = 'http://www.joyme.' + joymeEnvNoPort + '/';
    window.api = 'http://api.joyme.' + joymeEnvNoPort + '/';
    window.passport = 'http://passport.joyme.' + joymeEnvNoPort + '/';

    var uri = window.location.href;
    var uniKey = '';
    if (uri.indexOf('http://') == 0 || uri.indexOf('https://') == 0) {
        if (uri.indexOf("?") > 0) {
            uri = uri.substring(0, uri.indexOf("?"));
        }
        if (uri.indexOf('http://www.joyme.' + joymeEnv + '/') == 0 || uri.indexOf('https://www.joyme.' + joymeEnv + '/') == 0) {
            var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
            var removeFid = uri.replace('/' + fid, '');
            var wikiKey = removeFid.substring(removeFid.lastIndexOf("/") + 1, removeFid.length);
            uniKey = wikiKey + "|" + fid;
        } else if (uri.indexOf('http://wiki.joyme.' + joymeEnv + '/') == 0 || uri.indexOf('https://wiki.joyme.' + joymeEnv + '/') == 0) {
            var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
            var removeFid = uri.replace('/' + fid, '');
            var wikiKey = removeFid.substring(removeFid.lastIndexOf("/") + 1, removeFid.length);
            uniKey = wikiKey + "|" + fid;
        } else if (uri.indexOf('http://m.wiki.joyme.' + joymeEnv + '/') == 0 || uri.indexOf('https://m.wiki.joyme.' + joymeEnv + '/') == 0) {
            var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
            var removeFid = uri.replace('/' + fid, '');
            var wikiKey = removeFid.substring(removeFid.lastIndexOf("/") + 1, removeFid.length);
            uniKey = wikiKey + "|" + fid;
        } else {
            var host = window.location.host;
            var reg = /^[a-zA-Z0-9]+\.joyme\.(test|dev|alpha|beta|com):{0,1}[0-9]*$/;
            if (reg.test(host)) {
                var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
                var wikiKey = host.substring(0, host.indexOf('.'));
                uniKey = wikiKey + "|" + fid;
            }
        }
    }

    window.uno = getCookie('jmuc_uno');
    window.uid = getCookie('jmuc_u');
    window.token = getCookie('jmuc_token');
    window.timestamp = getCookie('jmuc_t');

    initPostMask();

    var uniKey = $('#hidden_unikey').val();
    var domain = $('#hidden_domain').val();
    var uri = $('#hidden_uri').val();
    var title = $('#hidden_title').val();

    var jsonParam = {
        title: title,
        pic: "",
        description: "",
        uri: uri
    }

    getCommentList(uniKey, domain, jsonParam);

    $('#comment_select>a').click(function () {
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
    $('#comment_submit').click(function () {
        if (window.uno == null || window.uid == null) {
            alert('请先保存您的内容，登录之后再回来~');
            return;
        }

        var text = $('#textarea_body').val();
        if (text == null || text.length == 0 || text == '我来说两句…') {
            $('#reply_error').html('评论内容不能为空');
            return false;
        }
        if (getStrlen(text) > 300) {
            $('#reply_error').html('评论内容长度不能超过300字符!');
            return false;
        }
        //全角
        //text = ToDBC(text);

        var body = {
            text: text,
            pic: ""
        }
        postComment(uniKey, domain, body, 0, 0);
    });

    //点赞
    $(document).on('click', 'a[id^=agreelink_]', function () {
        if (window.uno == null || window.uid == null) {
            alert('请先保存您的内容，登录之后再回来~');
            return;
        }

        var rid = $(this).attr('data-commentid');
        agreeComment(uniKey, domain, rid);
    });

    //点赞
    $(document).on('click', 'a[name=agree_num]', function () {
        if (window.uno == null || window.uid == null) {
            alert('请先保存您的内容，登录之后再回来~');
            return;
        }

        var replyid = $(this).attr('data-commentid');
        agreeComment(uniKey, domain, replyid);
    });

    //回复遮罩
    $(document).on('click', 'a[name=togglechildrenreply_area]', function () {
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
    $(document).on('click', 'a[name=replypost_mask]', function () {
        $(this).hide();
        var obj = $(this).next();
        obj.show();
        var textAreaObj = obj.find('textarea[id^=textarea_recomment_body_]');
        var body = textAreaObj.val();
        textAreaObj.val("").focus().val(body);
        textAreaObj.AutoHeight({maxHeight: 200});
    });

    $(document).on('click', 'a[name=link_recommentparent_mask]', function () {
        var oid = $(this).attr('data-oid');
        var pid = $(this).attr('data-pid');
        var nickname = $(this).attr('data-nick');
        var postCommentArea = $('#post_recomment_area_' + oid);
        var replyMaskLink = postCommentArea.find('a[name=replypost_mask]');
        var submitReComment = postCommentArea.find('a[name=submit_recomment]');
        submitReComment.attr("data-pid", pid);
        submitReComment.attr("data-nick", nickname);
        $('#textarea_recomment_body_' + oid).val('@' + nickname + ':');
        replyMaskLink.click();
    });

    //回复
    $(document).on('click', 'a[name=submit_recomment]', function () {
        if (window.uno == null || window.uid == null) {
            alert('请先保存您的内容，登录之后再回来~');
            return;
        }

        var oid = $(this).attr('data-oid');
        var text = $('#textarea_recomment_body_' + oid).val();
        var submitReComment = $('#post_recomment_area_' + oid).find('a[name=submit_recomment]');
        if (submitReComment.length > 0) {
            var pname = submitReComment.attr('data-nick');
            text = text.replace('@' + pname + ":", '');
        }
        if (text == null || text.length == 0 || text == '我来说两句…') {
            $('#reply_error').html('评论内容不能为空');
            return false;
        }
        if (getStrlen(text) > 300) {
            $('#reply_error').html('评论内容长度不能超过300字符!');
            return false;
        }

        //全角
        //text = ToDBC(text);

        var body = {
            text: text,
            pic: ""
        }
        var pid = $(this).attr('data-pid');
        if (pid == null || pid.length <= 0) {
            pid = 0;
        }
        postSubComment(uniKey, domain, body, oid, pid, $(this));
    });

    //删除
    $(document).on('click', 'a[name=remove_comment]', function () {
        if (window.uno == null || window.uid == null) {
            alert('请先保存您的内容，登录之后再回来~');
            return;
        }

        var rid = $(this).attr('data-rid');
        var oid = $(this).attr('data-oid');
        if (oid == null || oid.length <= 0) {
            oid = 0;
        }
        if (confirm('确定要删除吗？')) {
            removeComment(uniKey, domain, rid, oid);
        }
    });

//    $(document).click(function (event) {
//        var event = window.event ? window.event : event;
//        var target = event.srcElement || event.target;
//
//        if (target.tagName == null || target.name == 'replypost_mask' || target.name == 'submit_recomment' || target.textContent == '评 论' || target.name == 'content' || target.tagName.toLowerCase() == 'a') {
//            return;
//        }
//
//        var textAreas = $('textarea[id^=textarea_recomment_body_]:visible');
//        $.each(textAreas, function (i, val) {
//            var textArea = $(val);
//            var id = textArea.attr('id');
//            var rid = id.substr(id.lastIndexOf('_') + 1, id.length);
//            if ((textArea.val() != null && textArea.val().length > 0) || $('#post_callback_msg_' + rid).length > 0) {
//                return;
//            }
//            textArea.parent().hide();
//            textArea.parent().prev().show();
//            textArea.val('').css('heigth', '22px');
//        });
//    })

    $('#textarea_body').bind('focusin',
        function () {
            var val = $(this).val();
            if (val == '我来说两句…') {
                $(this).val('').css('color', '#333');
            }
        }).bind('focusout', function () {
            var val = $(this).val();
            if (val == null || val == '') {
                $(this).val('我来说两句…').css('color', '#ccc');
            }
        });
});

function getCommentList(unikey, domain, jsonparam) {
    $.ajax({
        url: window.api + "jsoncomment/reply/query",
        type: "post",
        data: {unikey: unikey, domain: domain, jsonparam: JSON.stringify(jsonparam), pnum: 1, psize: 10},
        dataType: "jsonp",
        jsonpCallback: "querycallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    $('#comment_list_area').html('<p style="height:30px; line-height:30px; text-align:center">目前没有评论，欢迎你发表评论</p>');
                    return;
                }

                var replyNum = '<a href="' + window.www + 'comment/reply/page?unikey=' + unikey + '&domain=' + domain + '&pnum=1&psize=10"><span>' + result.comment_sum + '条评论</span></a>'
                //$('#reply_num').html(replyNum);

                if (result.mainreplys == null || result.mainreplys.rows == null || result.mainreplys.rows.length == 0) {
                    if (result.mainreplys.page != null && result.mainreplys.page.maxPage > 1) {
                        $('#comment_list_area').html('<p style="height:30px; line-height:30px; text-align:center">当前页的评论已经被删除~</p><div class="more-comment"><a href="' + window.www + 'comment/reply/page?unikey=' + unikey + '&domain=' + domain + '&pnum=1&psize=10">更多评论&gt;&gt;</a></div>');
                    } else {
                        $('#comment_list_area').html('<p style="height:30px; line-height:30px; text-align:center">目前没有评论，欢迎你发表评论</p>');
                    }

                } else {
                    var html = '';
                    for (var i = 0; i < result.mainreplys.rows.length; i++) {
                        html += getCommentListCallBack(result.mainreplys.rows[i], unikey, domain);
                    }
                    var moreLink = '<div class="more-comment"><a href="' + window.www + 'comment/reply/page?unikey=' + unikey + '&domain=' + domain + '&pnum=1&psize=10">更多评论&gt;&gt;</a></div>';
                    html += moreLink;
                    $('#comment_list_area').html(html);
                }
            } else {
                $('#reply_error').html(resMsg.msg);
                return;
            }
        }
    });
}

function postComment(unikey, domain, body, oid, pid) {
    $.ajax({
        url: window.api + "jsoncomment/reply/post",
        type: "post",
        data: {unikey: unikey, domain: domain, body: JSON.stringify(body), oid: oid, pid: pid},
        dataType: "jsonp",
        jsonpCallback: "postcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                $('#reply_error').html(resMsg.msg);
                return;
            } else if (resMsg.rs == '-1001') {
                $('#reply_error').html('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-10102') {
                $('#reply_error').html('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-100') {
                $('#reply_error').html('app不存在~');
                return;
            } else if (resMsg.rs == '-10104') {
                $('#reply_error').html('用户不存在~');
                return;
            } else if (resMsg.rs == '-40011') {
                $('#reply_error').html('评论不存在~');
                return;
            } else if (resMsg.rs == '-40012') {
                $('#reply_error').html('评论不存在~');
                return;
            } else if (resMsg.rs == '-40000') {
                $('#reply_error').html('您的请求缺少unikey参数~');
                return;
            } else if (resMsg.rs == '-40001') {
                $('#reply_error').html('您的请求缺少domain参数~');
                return;
            } else if (resMsg.rs == '-40002') {
                $('#reply_error').html('您的请求缺少jsonparam参数~');
                return;
            } else if (resMsg.rs == '-40003') {
                $('#reply_error').html('您的请求中jsonparam参数格式错误~');
                return;
            } else if (resMsg.rs == '-40013') {
                $('#reply_error').html('您的请求中domain参数错误~');
                return;
            } else if (resMsg.rs == '-40005') {
                $('#reply_error').html('评论内容未填写~');
                return;
            } else if (resMsg.rs == '-40008') {
                $('#reply_error').html('评论对象不存在~');
                return;
            } else if (resMsg.rs == '-40006') {
                $('#reply_error').html('oid参数错误~');
                return;
            } else if (resMsg.rs == '-40009') {
                $('#reply_error').html('主楼评论已删除~');
                return;
            } else if (resMsg.rs == '-40007') {
                $('#reply_error').html('pid参数错误~');
                return;
            } else if (resMsg.rs == '-40010') {
                $('#reply_error').html('上级回复已删除~');
                return;
            } else if (resMsg.rs == '-40016') {
                $('#reply_error').html('您已经赞过了~');
                return;
            } else if (resMsg.rs == '-40017') {
                $('#reply_error').html('内容含有敏感词：' + resMsg.result[0]);
                return;
            } else if (resMsg.rs == '-40018') {
                $('#reply_error').html('评论不存在~');
                return;
            } else if (resMsg.rs == '-40019') {
                $('#reply_error').html('您已被禁言~');
                return;
            } else if (resMsg.rs == '-40020') {
                $('#reply_error').html('一分钟内不能重复发送相同的内容~');
                return;
            } else if (resMsg.rs == '-40022') {
                $('#reply_error').html('两次评论间隔不能少于15秒，请稍后再试~');
                return;
            }else if (resMsg.rs == '-1') {
                $('#reply_error').html('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                }

                var oid = result.reply.oid;
                var rid = result.reply.rid;
                if (oid == 0) {
                    var spanHtml = '条评论';
                    var numHtml = $('#reply_num a span').text();
                    var numStr = numHtml.replace(spanHtml, '');
                    var num = parseInt(numStr) + 1;
                   // $('#reply_num a span').html(num + spanHtml);
                    $('#cont_cmt_list_' + rid).remove();
                } else {
                    var numStr = $('#togglechildrenreply_area_' + oid).attr('data-replynum');
                    $('#togglechildrenreply_area_' + oid).attr('data-replynum', parseInt(numStr) + 1);
                    $('#cont_recmt_list_' + rid).remove();
                }

                var commentHtml = '<div id="cont_cmt_list_' + result.reply.rid + '" class="area blogopinion clearfix ">' +
                    '<dl>' +
                    '<dt class="personface">' +
                    '<a title="' + result.user.name + '" name="atLink" href="javascript:void(0);">' +
                    '<img width="58" height="58" class="user" src="' + result.user.icon + '"></a>' +
                    '</dt>' +
                    '<dd class="textcon discuss_building">' +
                    '<a title="' + result.user.name + '" class="author" name="atLink" href="javascript:void(0);">' + result.user.name + '</a>' +
                    '<p>' + result.reply.body.text + '</p>' +
                    '<div class="discuss_bdfoot">' + result.reply.post_date + '&nbsp;' +
                    '<a href="javascript:void(0);" id="agreelink_' + result.reply.rid + '" data-commentid="' + result.reply.rid + '" class="dianzan"></a>' +
                    '<span id="agreenum_' + result.reply.rid + '">' +
                    '<a href="javascript:void(0);" name="agree_num" data-commentid="' + result.reply.rid + '"></a>' +
                    '</span><a name="remove_comment" href="javascript:void(0);" data-oid="0" data-rid="' + result.reply.rid + '">删除</a>' +
                    '<a name="togglechildrenreply_area" href="javascript:void(0);" id="togglechildrenreply_area_' + result.reply.rid + '" data-replynum="0">回复</a>' +
                    '</div>' +
                    '<div class="discuss_bd_list discuss_border" style="display:none">' +
                    '<div id="children_reply_list_' + result.reply.rid + '"></div>' +
                    '<div id="post_recomment_area_' + result.reply.rid + '" class="discuss_reply">' +
                    '<a class="discuss_text01" href="javascript:void(0);" name="replypost_mask">我也说一句</a>' +
                    '<div style="display:none" class="discuss_reply reply_box01">' +
                    '<textarea warp="off" style="font-family:Tahoma, ' + "宋体" + ';" id="textarea_recomment_body_' + result.reply.rid + '" class="discuss_text focus" rows="" cols="" name="content"></textarea>' +
                    '<div class="related clearfix">' +
                    '<div class="transmit clearfix">' +
                    '<a class="submitbtn fr" name="submit_recomment" data-oid="' + result.reply.rid + '">' +
                    '<span name="span_pinglun">评 论</span></a></div></div></div></div></div></dd></dl></div>';

                if ($('#comment_list_area div.area').length > 0) {
                    $('#comment_list_area div:first').before(commentHtml);
                } else {
                    var moreHtml = '<div class="more-comment"><a href="' + window.www + 'comment/reply/page?unikey=' + unikey + '&domain=' + domain + '&pnum=1&psize=10">更多评论&gt;&gt;</a></div>';
                    $('#comment_list_area').html(commentHtml + moreHtml);
                }
                $("#textarea_body").val('我来说两句…').css('color', '#ccc');
                $('#reply_error').html('');
            } else {
                $('#reply_error').html(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

function postSubComment(unikey, domain, body, oid, pid, reCommentDom) {
    $.ajax({
        url: window.api + "jsoncomment/reply/post",
        type: "post",
        data: {unikey: unikey, domain: domain, body: JSON.stringify(body), oid: oid, pid: pid},
        dataType: "jsonp",
        jsonpCallback: "postcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">' + resMsg.msg + '</span>');
                return;
            } else if (resMsg.rs == '-1001') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">请先保存您的内容，登录之后再回来~</span>');
                return;
            } else if (resMsg.rs == '-10102') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">请先保存您的内容，登录之后再回来~</span>');
                return;
            } else if (resMsg.rs == '-100') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">app不存在~</span>');
                return;
            } else if (resMsg.rs == '-10104') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">用户不存在~</span>');
                return;
            } else if (resMsg.rs == '-40011') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论不存在~</span>');
                return;
            } else if (resMsg.rs == '-40012') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论不存在~</span>');
                return;
            } else if (resMsg.rs == '-40000') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求缺少unikey参数~</span>');
                return;
            } else if (resMsg.rs == '-40001') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求缺少domain参数~</span>');
                return;
            } else if (resMsg.rs == '-40002') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求缺少jsonparam参数~</span>');
                return;
            } else if (resMsg.rs == '-40003') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求中jsonparam参数格式错误~</span>');
                return;
            } else if (resMsg.rs == '-40013') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您的请求中domain参数错误~</span>');
                return;
            } else if (resMsg.rs == '-40005') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论内容未填写~</span>');
                return;
            } else if (resMsg.rs == '-40008') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论对象不存在~</span>');
                return;
            } else if (resMsg.rs == '-40006') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">oid参数错误~</span>');
                return;
            } else if (resMsg.rs == '-40009') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">主楼评论已删除~</span>');
                return;
            } else if (resMsg.rs == '-40007') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">pid参数错误~</span>');
                return;
            } else if (resMsg.rs == '-40010') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">上级回复已删除~</span>');
                return;
            } else if (resMsg.rs == '-40016') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您已经赞过了~</span>');
                return;
            } else if (resMsg.rs == '-40017') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">内容含有敏感词：' + resMsg.result[0] + '</span>');
                return;
            } else if (resMsg.rs == '-40018') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">评论不存在~</span>');
                return;
            } else if (resMsg.rs == '-40019') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">您已被禁言~</span>');
                return;
            } else if (resMsg.rs == '-40020') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">一分钟内不能重复发送相同的内容~</span>');
                return;
            } else if (resMsg.rs == '-40022') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">两次评论间隔不能少于15秒，请稍后再试~</span>');
                return;
            } else if (resMsg.rs == '-1') {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">请先保存您的内容，登录之后再回来~</span>');
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                }
                var html = getReCommentHtml(result);
                $('#children_reply_list_' + oid).find('p').remove();
                var moreDom = $('#children_reply_list_' + oid).find('div.more-comment');
                if (moreDom == null || moreDom.length == 0 || moreDom.html().length == 0) {
                    $('#children_reply_list_' + oid).append(html);
                } else {
                    moreDom.before(html);
                }

                var spanHtml = '条评论';
                var numHtml = $('#reply_num a span').text();
                var numStr = numHtml.replace(spanHtml, '');
                var num = parseInt(numStr) + 1;
               // $('#reply_num a span').html(num + spanHtml);

                var num = parseInt($('#togglechildrenreply_area_' + oid).attr('data-replynum'));
                $('#togglechildrenreply_area_' + oid).attr('data-replynum', (num + 1));
                $('#textarea_recomment_body_' + oid).val("");
                $('#post_callback_msg_' + oid).remove();
            } else {
                $('#post_callback_msg_' + oid).remove();
                reCommentDom.before('<span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;" id="post_callback_msg_' + oid + '">' + resMsg.msg + '</span>');
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

function agreeComment(unikey, domain, rid) {
    $.ajax({
        url: window.api + "jsoncomment/reply/agree",
        type: "post",
        data: {unikey: unikey, domain: domain, rid: rid},
        dataType: "jsonp",
        jsonpCallback: "agreecallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else if (resMsg.rs == '-1001') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-10102') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-100') {
                alert('app不存在~');
                return;
            } else if (resMsg.rs == '-10104') {
                alert('用户不存在~');
                return;
            } else if (resMsg.rs == '-40011') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-40012') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-40000') {
                alert('您的请求缺少unikey参数~');
                return;
            } else if (resMsg.rs == '-40001') {
                alert('您的请求缺少domain参数~');
                return;
            } else if (resMsg.rs == '-40002') {
                alert('您的请求缺少jsonparam参数~');
                return;
            } else if (resMsg.rs == '-40003') {
                alert('您的请求中jsonparam参数格式错误~')
                return;
            } else if (resMsg.rs == '-40013') {
                alert('您的请求中domain参数错误~')
                return;
            } else if (resMsg.rs == '-40005') {
                alert('评论内容未填写~')
                return;
            } else if (resMsg.rs == '-40008') {
                alert('评论对象不存在~');
                return;
            } else if (resMsg.rs == '-40006') {
                alert('oid参数错误~');
                return;
            } else if (resMsg.rs == '-40009') {
                alert('主楼评论已删除~');
                return;
            } else if (resMsg.rs == '-40007') {
                alert('pid参数错误~');
                return;
            } else if (resMsg.rs == '-40010') {
                alert('上级回复已删除~');
                return;
            } else if (resMsg.rs == '-40016') {
                alert('您已经赞过了~');
                return;
            } else if (resMsg.rs == '-40017') {
                alert('内容含有敏感词：' + resMsg.result[0]);
                return;
            } else if (resMsg.rs == '-40018') {
                alert('评论不存在~');
                return;
            } else if (resMsg.rs == '-40019') {
                alert('您已被禁言~');
                return;
            } else if (resMsg.rs == '-1') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '1') {
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
            } else {
                alert(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

function initPostMask() {
    if (window.uno == null || window.uno.length == 0 || window.uid == null || window.uid.length == 0 || window.token == null || window.token.length == 0 || window.timestamp == null || window.timestamp.length == 0) {
        $('#textarea_body').after('<div class="wrapper_unlogin" style="text-align:center; margin:-65px 0 65px">您需要<a id="login_link" href="javascript:loginDiv();">登录</a>后才能评论</div>');
        $('#textarea_body').attr('disabled', 'disabled');
    } else {
        $('#textarea_body').val('我来说两句…').css('color', '#ccc');
    }
}

function removeComment(unikey, domain, rid, oid) {
    $.ajax({
        url: window.api + "jsoncomment/reply/remove",
        type: "post",
        data: {unikey: unikey, domain: domain, rid: rid},
        dataType: "jsonp",
        jsonpCallback: "removecallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                alert(resMsg.msg);
                return;
            } else if (resMsg.rs == '-1001') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-100') {
                alert('app不存在~');
                return;
            } else if (resMsg.rs == '-10104') {
                alert('用户不存在~');
                return;
            } else if (resMsg.rs == '-40011') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-40012') {
                alert('评论不存在~~');
                return;
            } else if (resMsg.rs == '-10102') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '-40000') {
                alert('您的请求缺少unikey参数~');
                return;
            } else if (resMsg.rs == '-40001') {
                alert('您的请求缺少domain参数~');
                return;
            } else if (resMsg.rs == '-40002') {
                alert('您的请求缺少jsonparam参数~');
                return;
            } else if (resMsg.rs == '-40003') {
                alert('您的请求中jsonparam参数格式错误~')
                return;
            } else if (resMsg.rs == '-40013') {
                alert('您的请求中domain参数错误~')
                return;
            } else if (resMsg.rs == '-40005') {
                alert('评论内容未填写~')
                return;
            } else if (resMsg.rs == '-40008') {
                alert('评论对象不存在~');
                return;
            } else if (resMsg.rs == '-40006') {
                alert('oid参数错误~');
                return;
            } else if (resMsg.rs == '-40009') {
                alert('主楼评论已删除~');
                return;
            } else if (resMsg.rs == '-40007') {
                alert('pid参数错误~');
                return;
            } else if (resMsg.rs == '-40010') {
                alert('上级回复已删除~');
                return;
            } else if (resMsg.rs == '-40016') {
                alert('您已经赞过了~');
                return;
            } else if (resMsg.rs == '-40017') {
                alert('内容含有敏感词：' + resMsg.result[0]);
                return;
            } else if (resMsg.rs == '-40018') {
                alert('评论不存在~');
                return;
            } else if (resMsg.rs == '-40019') {
                alert('您已被禁言~');
                return;
            } else if (resMsg.rs == '-1') {
                alert('请先保存您的内容，登录之后再回来~');
                return;
            } else if (resMsg.rs == '1') {
                if (oid == 0) {
                    var spanHtml = '条评论';
                    var numHtml = $('#reply_num a span').text();
                    var numStr = numHtml.replace(spanHtml, '');
                    var num = parseInt(numStr) - 1;
                    //$('#reply_num a span').html(num + spanHtml);
                    $('#cont_cmt_list_' + rid).remove();
                } else {
                    var numStr = $('#togglechildrenreply_area_' + oid).attr('data-replynum');
                    $('#togglechildrenreply_area_' + oid).attr('data-replynum', parseInt(numStr) - 1);
                    $('#cont_recmt_list_' + rid).remove();
                }
            } else {
                alert(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}


function getCommentListCallBack(commentObj, unikey, domain) {
    var reply = commentObj.reply;
    var reReplyArray = null;
    if (commentObj.subreplys != null) {
        reReplyArray = commentObj.subreplys.rows;
    }

    var hasRe = (reReplyArray != null && reReplyArray.length > 0);

    var reCommentListHtml = '<div id="children_reply_list_' + reply.reply.rid + '">';
    if (hasRe) {
        for (var i = 0; i < reReplyArray.length; i++) {
            var reCommentObj = reReplyArray[i];
            reCommentListHtml += getReCommentHtml(reCommentObj);
        }
        if (commentObj.subreplys.page != null && commentObj.subreplys.page.maxPage > 1) {
            reCommentListHtml += '<div class="more-comment"><a href="' + window.www + 'comment/reply/page?unikey=' + unikey + '&domain=' + domain + '&pnum=1&psize=10">更多回复&gt;&gt;</a></div>';
        }

    } else {
        if (commentObj.subreplys != null && commentObj.subreplys.page != null && commentObj.subreplys.page.maxPage > 1) {
            reCommentListHtml += '<p style="text-align:center">当前页的评论已经被删除~</p><div class="more-comment"><a href="' + window.www + 'comment/reply/page?unikey=' + uniKey + '&domain=' + domain + '&pnum=1&psize=10">更多回复&gt;&gt;</a></div>';
        }
    }
    reCommentListHtml += '</div>';

    var removeHtml = '';
    if (window.uno != null && window.uid != null && window.uno == reply.user.uno && window.uid == reply.user.uid) {
        removeHtml = '<a name="remove_comment" href="javascript:void(0);" data-oid="' + reply.reply.oid + '" data-rid="' + reply.reply.rid + '" >删除</a>'
    }

    var toogleReHtml = '<a name="togglechildrenreply_area" href="javascript:void(0);" id="togglechildrenreply_area_' + reply.reply.rid + '" data-replynum="' + reply.reply.sub_reply_sum + '">回复' + (reply.reply.sub_reply_sum > 0 ? ('(' + reply.reply.sub_reply_sum + ')') : '') + '</a>';

    var postReCommentHtml = '<div id="post_recomment_area_' + reply.reply.rid + '" class="discuss_reply">' +
        ' <a class="discuss_text01" href="javascript:void(0);" name="replypost_mask">我也说一句</a>' +
        '<div style="display:none" class="discuss_reply reply_box01">' +
        '<textarea warp="off" style="font-family:Tahoma, \'宋体\';" id="textarea_recomment_body_' + reply.reply.rid + '" class="discuss_text focus" rows="" cols="" name="content"></textarea>' +
        '<div class="related clearfix">' +
        '<div class="transmit clearfix">' +
        ' <a class="submitbtn fr" name="submit_recomment"  data-oid="' + reply.reply.rid + '" name="childreply_submit"><span name="span_pinglun">评 论</span></a>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';

    var puserHtml = '';
    if (reply.puser != null && reply.puser.name != null) {
        puserHtml = '@' + reply.puser.name + ":";
    }
    var html = '<div id="cont_cmt_list_' + reply.reply.rid + '" class="area blogopinion clearfix ">' +
        '<dl>' +
        '<dt class="personface">' +
        '<a title="' + reply.user.name + '" name="atLink" href="javascript:void(0);">' +
        '<img width="58" height="58" class="user" src="' + reply.user.icon + '">' +
        '</a>' +
        '</dt>' +
        '<dd class="textcon discuss_building">' +
        '<a title="' + reply.user.name + '" class="author" name="atLink" href="javascript:void(0);">' + reply.user.name + '</a>' +
        '<p>' + puserHtml + reply.reply.body.text + '</p>' +
        '<div class="discuss_bdfoot">' + reply.reply.post_date + '&nbsp;<a href="javascript:void(0);" id="agreelink_' + reply.reply.rid + '" data-commentid="' + reply.reply.rid + '" class="dianzan"></a><span id="agreenum_' + reply.reply.rid + '"><a href="javascript:void(0);" name="agree_num" data-commentid="' + reply.reply.rid + '">' + ((reply.reply.agree_sum == null || reply.reply.agree_sum == 0) ? '' : ('(' + reply.reply.agree_sum + ')')) + '</a></span>' + removeHtml + toogleReHtml + '</div>' +
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
    if (window.uno != null && window.uid != null && window.uno == reCommentObj.user.uno && window.uid == reCommentObj.user.uid) {
        removeHtml = '<a name="remove_comment" href="javascript:void(0);" data-rid="' + reCommentObj.reply.rid + '" data-oid="' + reCommentObj.reply.oid + '">删除</a>'
    }

    var puserHtml = '';
    if (reCommentObj.puser != null && reCommentObj.puser.name != null) {
        puserHtml = '@' + reCommentObj.puser.name + ":";
    }

    var reCommentHtml = '<div style="" id="cont_recmt_list_' + reCommentObj.reply.rid + '" class="conmenttx clearfix">' +
        '<div class="conmentface">' +
        '<div class="commenfacecon">' +
        '<a href="javascript:void(0);" title="' + reCommentObj.user.name + '" name="atLink" class="cont_cl_left">' +
        '<img width="33px" height="33px" src="' + reCommentObj.user.icon + '">' +
        '</a>' +
        '</div>' +
        '</div>' +
        '<div class="conmentcon">' +
        '<a title="' + reCommentObj.user.name + '" name="atLink" href="javascript:void(0);">' + reCommentObj.user.name + '</a>' +
        ':' + puserHtml + reCommentObj.reply.body.text + '<div class="commontft clearfix"><span class="reply_time">' + reCommentObj.reply.post_date + '</span>' +
        '<span class="delete">' +
        '<a href="javascript:void(0);" id="agreelink_' + reCommentObj.reply.rid + '" data-commentid="' + reCommentObj.reply.rid + '" class="dianzan"></a>' +
        '<span id="agreenum_' + reCommentObj.reply.rid + '"><a href="javascript:void(0);" name="agree_num" data-commentid="' + reCommentObj.reply.rid + '">' + ((reCommentObj.reply.agree_sum == null || reCommentObj.reply.agree_sum == 0) ? '' : ('(' + reCommentObj.reply.agree_sum + ')')) + '</a></span>' +
        removeHtml +
        '<a href="javascript:void(0);"  name="link_recommentparent_mask" data-nick="' + reCommentObj.user.name + '" data-oid="' + reCommentObj.reply.oid + '" data-pid="' + reCommentObj.reply.rid + '">回复</a>' +
        '</span>' +
        '</div>' +
        '</div>' +
        '</div>';
    return reCommentHtml;
}

var errorArray = {
    'comment.post.limit': '您说话的频率太快了，请歇一歇哦~',
    'user.has.agree': '你已经支持过了',
    'comment.body.empty': '评论内容不能为空',
    'content.isnot.exists': '文章不存在！',
    'reply.not.exist': '该评论已经被删除，无法回复！',
    'comment.body.illegl': '评论中含有不适当的内容！'
}

function getCookie(objName) {
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
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

function ToDBC(txtstring) {
    var tmp = "";
    for (var i = 0; i < txtstring.length; i++) {
        if (txtstring.charCodeAt(i) == 32) {
            tmp = tmp + String.fromCharCode(12288);
        }
        if (txtstring.charCodeAt(i) < 127) {
            tmp = tmp + String.fromCharCode(txtstring.charCodeAt(i) + 65248);
        }
    }
    return tmp;
}

$.fn.AutoHeight = function (options) {
    var defaults = {
        maxHeight: null,
        minHeight: $(this).height()
    };
    var opts = (typeof (options) === 'object') ? $.extend({}, defaults, options) : {};
    this.each(function () {
        $(this).bind("paste cut keydown keyup focus blur", function () {
            var height, style = this.style;
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
