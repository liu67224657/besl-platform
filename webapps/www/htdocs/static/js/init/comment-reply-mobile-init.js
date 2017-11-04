/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var card = require('../page/card');
    var moodBiz = require('../biz/mood-biz');
    var common = require('../common/common');
    require('../common/tips');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var alertOption = {
        tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    require('../common/jquery.autotextarea')($);

    $(document).ready(function () {
        var joymeEnv = window.location.host.substring(window.location.host.lastIndexOf('.')+1,window.location.host.length);
        var joymeEnvNoPort = joymeEnv;
        if(joymeEnv.indexOf(':') > 0){
            joymeEnvNoPort = joymeEnv.substring(0, joymeEnv.indexOf(':'));
        }
        window.www = 'http://www.joyme.' + joymeEnvNoPort + '/';
        window.api = 'http://api.joyme.' + joymeEnvNoPort + '/';
        window.passport = 'http://passport.joyme.' + joymeEnvNoPort + '/';
        
        window.uno = getCookie('jmuc_uno');
        window.uid = getCookie('jmuc_u');
        window.token = getCookie('jmuc_token');
        window.timestamp = getCookie('jmuc_t');

        var uniKey = $('#input_hidden_unikey').val();
        var domain = $('#input_hidden_domain').val();

        addDocmentListerner();
        header.noticeSearchReTopInit();
        /*
         mwiki点击回复判断是否登录
         */
        $('[name=clickreply]').live('click', function () {
            var cid = $('#input_hidden_cid').val();
            if (window.uno == null || window.uid == null) {
                alert("请注意保存您的内容，登录之后再回来~");
                return false;
            }
        });
        $('#form_mobile_comment').submit(function () {
            // var contentid = $("#contentid").val();
            var comentbody = document.getElementById('comentbody').value;
            comentbody = Trim(comentbody, 'g');
            if (common.strLen(comentbody) == 0) {
                $("#reply_error").text('评论内容不能为空!');
                return false;
            } else if (common.strLen(comentbody) > 300) {
                $("#reply_error").text('评论内容长度不能超过300个字符!');
                return false;
            }
            var body = {
                text: comentbody,
                pic: ""
            }
            $("#input_hidden_body").val(JSON.stringify(body));
            // alert( $("#input_hidden_body").val());
            return true;
        });

        /*
         mwiki发送评论JS
         */
        $('[name=m_replay]').live('click', function () {
            if (window.uno == null || window.uid == null) {
                alert("请注意保存您的内容，登录之后再回来~");
                return false;
            }

            $("[id^=child_reply_error_]").html("");
            $("[id^=reply_error_]").html("");
            var oid = $(this).attr('data-oid');
            var pid = $(this).attr('data-pid');
            var text = '';

            if (pid == '') {
                text = $('#textarea_' + oid).val();
                if (common.strLen(text) == 0) {
                    return replyErrory(1, "reply_error_", oid);
                } else if (common.strLen(text) > 300) {
                    return replyErrory(2, "reply_error_", oid);
                }
            } else {
                var pname = $(this).attr('data-pname');
                text = $("#child_textarea_" + pid).val();
                text = Trim(text, 'g');
                if (pname != null && pname.length != 0) {
                    text = text.replace('@' + pname + '：', '');
                }

                if (common.strLen(text) == 0) {
                    return replyErrory(1, "child_reply_error_", pid);
                } else if (common.strLen(text) > 300) {
                    return replyErrory(2, "child_reply_error_", pid);
                }
            }
            var body = {
                text: text,
                pic: ""
            }
            postComment(uniKey, domain, body, oid, pid);
        });

        function Trim(str, is_global) {
            var result;
            result = str.replace(/(^\s+)|(\s+$)/g, "");
            return result;
        }

        /*
         Mwiki删除操作
         */
        $("[name=deleteReply]").live("click", function () {
            if (window.uno == null || window.uid == null) {
                alert("请注意保存您的内容，登录之后再回来~");
                return false;
            }
            var rid = $(this).attr('data-rid');
            var oid = $(this).attr('data-oid');
            if (window.confirm("确定删除该评论？")) {
                removeComment(uniKey, domain, rid, oid);
            }
        });

        /*
         Mwiki点赞
         */
        $("[name=replayagree]").live("click", function () {
            var rid = $(this).attr("data-rid");
            if (window.uno == null || window.uid == null) {
                alert("请注意保存您的内容，登录之后再回来~");
                return false;
            } else {
                agreeComment(uniKey, domain, rid);
            }

        });

        /*
         mwiki楼中楼翻页
         */
        $('a[name=childReplay]').live('click', function () {
            var oid = $(this).attr('data-oid');
            var pnum = $(this).attr('data-p');
            getSubCommentPage($(this), uniKey, domain, oid, pnum);
        });
    });

    function getSubCommentPage(pageDom, unikey, domain, oid, pnum) {
        $.ajax({
            url: window.api + "jsoncomment/reply/sublist",
            type: "post",
            async: false,
            data: {unikey: unikey, domain: domain, oid: oid, pnum: pnum},
            dataType: "jsonp",
            jsonpCallback: "sublistcallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    alert('请先保存您的内容，登录之后再回来~');
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
                    alert('您的请求中jsonparam参数格式错误~');
                    return;
                } else if (resMsg.rs == '-40013') {
                    alert('您的请求中domain参数错误~');
                    return;
                } else if (resMsg.rs == '-40005') {
                    alert('评论内容未填写~');
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
                    alert('内容含有敏感词~');
                    return;
                } else if (resMsg.rs == '-40018') {
                    alert('评论不存在~');
                    return;
                } else if (resMsg.rs == '-40019') {
                    alert('您已被禁言~');
                    return;
                } else if (resMsg.rs == '-40020') {
                    alert('一分钟内不能重复发送相同的内容~');
                    return;
                } else if (resMsg.rs == '-40022') {
                    alert('两次评论间隔不能少于15秒，请稍后再试~');
                    return;
                } else if (resMsg.rs == '-99999') {
                    alert('系统维护~');
                    return;
                } else if (resMsg.rs == '-1') {
                    alert('请先保存您的内容，登录之后再回来~');
                    return;
                } else if (resMsg.rs == '1') {
                    var result = resMsg.result;
                    if (result == null) {
                        return;
                    } else {
                        var replyList = result.rows;
                        if (replyList != null && replyList.length > 0) {
                            $("#replyed_" + oid).empty();
                            for (var i = 0; i < replyList.length; i++) {
                                $("#replyed_" + oid).append(getMobileReplyList(replyList[i], replyList[i].reply.pid, replyList[i].reply.oid));
                            }
                        } else {
                            $("#replyed_" + oid).empty();
                            var html = '<div class="replyed" id="replyed_' + oid + '">' +
                                '<div class="replyed-list">' +
                                '<p>' +
                                '当前页的评论已经被删除~' +
                                '</p>' +
                                '</div>' +
                                '</div>';
                            $("#replyed_" + oid).append(html);
                        }
                        //回复追加的标志位
                        var appenddiv = '<div id="appenddiv_' + oid + '"></div>';
                        $("#replyed_" + oid).append(appenddiv);
                        var pagehtml = replypage(result.page, oid);
                        $("#replyed_" + oid).append(pagehtml);
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

    function postComment(unikey, domain, body, oid, pid) {
        $.ajax({
            url: window.api + "jsoncomment/reply/post",
            type: "post",
            async: false,
            data: {unikey: unikey, domain: domain, body: JSON.stringify(body), oid: oid, pid: pid},
            dataType: "jsonp",
            jsonpCallback: "postcallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('请先保存您的内容，登录之后再回来~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('请先保存您的内容，登录之后再回来~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-10102') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('请先保存您的内容，登录之后再回来~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('请先保存您的内容，登录之后再回来~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40000') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('您的请求缺少unikey参数~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('您的请求缺少unikey参数~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40001') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('您的请求缺少domain参数~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('您的请求缺少domain参数~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40002') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('您的请求缺少jsonparam参数~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('您的请求缺少jsonparam参数~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40003') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('您的请求中jsonparam参数格式错误~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('您的请求中jsonparam参数格式错误~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40013') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('您的请求中domain参数错误~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('您的请求中domain参数错误~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40005') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('评论内容未填写~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('评论内容未填写~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40008') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('评论对象不存在~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('评论对象不存在~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40006') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('oid参数错误~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('oid参数错误~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40009') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('主楼评论已删除~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('主楼评论已删除~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40007') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('pid参数错误~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('pid参数错误~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40010') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('上级回复已删除~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('上级回复已删除~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40016') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('您已经赞过了~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('您已经赞过了~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40017') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('内容含有敏感词：' + resMsg.result[0]);
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('内容含有敏感词：' + resMsg.result[0]);
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40018') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('评论不存在~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('评论不存在~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40019') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('您已被禁言~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('您已被禁言~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-40020') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('一分钟内不能重复发送相同的内容~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('一分钟内不能重复发送相同的内容~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                }else if (resMsg.rs == '-40022') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('两次评论间隔不能少于15秒，请稍后再试~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('两次评论间隔不能少于15秒，请稍后再试~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-99999') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('系统维护~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('系统维护~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '-1') {
                    if (pid == "") {
                        $("#reply_error_" + oid).html('请先保存您的内容，登录之后再回来~');
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html('请先保存您的内容，登录之后再回来~');
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                } else if (resMsg.rs == '1') {
                    var result = resMsg.result;
                    if (result == null) {
                        return;
                    }
                    $("#appenddiv_" + oid + "").before(getMobileReplyList(result, pid, oid));
                    if (pid == "") {
                        $("#textarea_" + oid).val("");
                        $("#replybox_" + oid).hide();
                        $("#reply_error_" + oid).html("");
                    } else {
                        $("#child_textarea_" + pid).val("@" + result.puser.name + "：");
                        $("#m_replybox_" + pid).hide();
                        $("#child_reply_error_" + pid).html("");
                    }
                } else {
                    if (pid == "") {
                        $("#reply_error_" + oid).html(resMsg.msg);
                        $("#textarea_" + oid).focus();
                    } else {
                        $("#child_reply_error_" + pid).html(resMsg.msg);
                        $("#child_textarea_" + pid).focus();
                    }
                    return;
                }
            },
            error: function () {
                alert('获取失败，请刷新');
            }
        });
    }

    function removeComment(unikey, domain, rid, oid) {
        $.ajax({
            url: window.api + "jsoncomment/reply/remove",
            type: "post",
            async: false,
            data: {unikey: unikey, domain: domain, rid: rid},
            dataType: "jsonp",
            jsonpCallback: "removecallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    alert('请先保存您的内容，登录之后再回来~');
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
                    alert('您的请求中jsonparam参数格式错误~');
                    return;
                } else if (resMsg.rs == '-40013') {
                    alert('您的请求中domain参数错误~');
                    return;
                } else if (resMsg.rs == '-40005') {
                    alert('评论内容未填写~');
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
                    alert('内容含有敏感词~');
                    return;
                } else if (resMsg.rs == '-40018') {
                    alert('评论不存在~');
                    return;
                } else if (resMsg.rs == '-40019') {
                    alert('您已被禁言~');
                    return;
                } else if (resMsg.rs == '-40020') {
                    alert('一分钟内不能重复发送相同的内容~');
                    return;
                } else if (resMsg.rs == '-40022') {
                    alert('两次评论间隔不能少于15秒，请稍后再试~');
                    return;
                } else if (resMsg.rs == '-99999') {
                    alert('系统维护~');
                    return;
                } else if (resMsg.rs == '-1') {
                    alert('请先保存您的内容，登录之后再回来~');
                    return;
                } else if (resMsg.rs == '1') {
                    $('[name=remove_reply_' + rid + ']').remove();
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

    function agreeComment(unikey, domain, rid) {
        $.ajax({
            url: window.api + "jsoncomment/reply/agree",
            type: "post",
            async: false,
            data: {unikey: unikey, domain: domain, rid: rid},
            dataType: "jsonp",
            jsonpCallback: "agreecallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    alert('请先保存您的内容，登录之后再回来~');
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
                    alert('您的请求中jsonparam参数格式错误~');
                    return;
                } else if (resMsg.rs == '-40013') {
                    alert('您的请求中domain参数错误~');
                    return;
                } else if (resMsg.rs == '-40005') {
                    alert('评论内容未填写~');
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
                    alert('内容含有敏感词~');
                    return;
                } else if (resMsg.rs == '-40018') {
                    alert('评论不存在~');
                    return;
                } else if (resMsg.rs == '-40019') {
                    alert('您已被禁言~');
                    return;
                } else if (resMsg.rs == '-40020') {
                    alert('一分钟内不能重复发送相同的内容~');
                    return;
                } else if (resMsg.rs == '-40022') {
                    alert('两次评论间隔不能少于15秒，请稍后再试~');
                    return;
                } else if (resMsg.rs == '-99999') {
                    alert('系统维护~');
                    return;
                } else if (resMsg.rs == '-1') {
                    alert('请先保存您的内容，登录之后再回来~');
                    return;
                } else if (resMsg.rs == '1') {
                    var num = $('#replyagree_' + rid).text();
                    num = parseInt(num);
                    $("#replyagree_" + rid).text(num + 1)
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

    function getMobileReplyList(result, pid, oid) {
        var puser = result.puser;
        var pname = '';
        if (puser != "" && puser != null) {
            pname = "@" + puser.name + "：";
        } else {
            pname = "";
        }
        var deleteurl = '';
        if (window.uno == result.user.uno) {
            deleteurl = '<span class="sc" name="deleteReply"  data-rid="' + result.reply.rid + '">删除</span>'
        }
        var appendhtml = '<div class="replyed-list" name="remove_reply_' + result.reply.rid + '">' +
            '<div class="aut">' +
            '<div class="fl">' +
            '<span class="user"><i>•</i>' + result.user.name + ':</span>' +
            '</div>' +
            '<div class="fr">' +
            '<span class="dz" name="replayagree" id="replyagree_' + result.reply.rid + '" data-rid="' + result.reply.rid + '">' + result.reply.agree_sum + '</span>' +
            deleteurl +
            '<span class="reply" onclick="changeReply(this)" name="clickreply">回复</span>' +
            '</div>' +
            '</div>' +
            '<div class="comment-content">' + pname + (result.reply.body == null ? '' : result.reply.body.text) +
            '</div>' +
            '<div class="replybox" id="m_replybox_' + result.reply.rid + '">' +
            '<div style="margin-top:6px;"><textarea placeholder="说点儿什么吧" id="child_textarea_' + result.reply.rid + '">@' + result.user.name + '：</textarea></div>' +
            '<span style="color: #f00; " id="child_reply_error_' + result.reply.rid + '"></span>' +
            '<input type="submit" value="回复" data-oid="' + oid + '" ' +
            'data-pid="' + result.reply.rid + '" ' +
            'data-pname="' + result.user.name + '"' +
            'id="m_reply_' + oid + '"' +
            'name="m_replay"/>' +
            '</div>' +
            '</div>';

        return appendhtml;
    }

    function replypage(page, oid) {
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
            'data-oid="' + oid + '" ' +
            'data-p="1">首页</a></div>' +
            '<div><a href="javascript:void(0)" class="' + preclassname + '" name="' + preclickname + '" ' +
            'data-oid="' + oid + '" ' +
            'data-p="' + prepage + '">上一页</a></div>' +
            '<div>' + page.curPage + '/' + maxpage + '  </div>' +
            '<div><a href="javascript:void(0)" class="' + lastclassname + '" name="' + lastclickname + '" ' +
            'data-oid="' + oid + '" ' +
            'data-p="' + lastpage + '">下一页</a></div>' +
            '<div><a href="javascript:void(0)" class="' + lastclassname + '" name="' + lastclickname + '" ' +
            'data-oid="' + oid + '" ' +
            'data-p="' + maxpage + '">尾页</a></div>';
        return html;
    }

    function addDocmentListerner() {
        $(document).click(function (event) {
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

    //require.async('../common/google-statistics');
    //require.async('../common/bdhm')
});