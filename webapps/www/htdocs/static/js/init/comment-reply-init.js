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

        //发评论下拉
        $('#send_box_body').focus(function () {
            if (window.uno == null || window.uid == null) {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            $('#send_box_small').hide();
            $('#div_post_area').show();
            $('#textarea_comment_body').focus();
            $('#comment_submit').attr('class', 'publishon');
        });

        //发表评论
        $('#form_comment').submit(function () {
            if (window.uno == null || window.uid == null) {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var text = $('#textarea_comment_body').val();
            if (text == null || common.strLen(text) == 0) {
                $('#reply_error').html('评论内容不能为空!');
                return false;
            } else if (common.strLen(text) > 300) {
                $('#reply_error').html('评论内容长度不能超过300字符!');
                return false;
            }
            var body = {
                text: text,
                pic: ""
            }
            $('#input_hidden_body').val(JSON.stringify(body));
            return true;
        });

        //点赞
        $('a[id^=agreelink_]').live('click', function () {
            if (window.uno == null || window.uid == null) {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var rid = $(this).attr('data-rid');
            agreeComment(uniKey, domain, rid);
        });

        //切换回复收起的方法
        $('a[name=link_toggle_recomment]').live('click', function () {
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
        $('a[name=replypost_mask]').live('click', function () {
            var id = $(this).attr('id').replace('replypost_mask_', '');
            $(this).hide();
            $(this).next().show();
            $('#textarea_recomment_body_' + id).AutoHeight({maxHeight: 200});
            $('#textarea_recomment_body_' + id).focus();
        });

        $('a[name=link_recommentparent_mask]').live('click', function () {
            if (window.uno == null || window.uid == null) {
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

        //回复
        $('a[name=submit_recomment]').live('click', function () {
            if (window.uno == null || window.uid == null) {
                loginBiz.maskLoginByJsonObj();
                return false;
            } else {
                var pname = $(this).attr('data-pname');
                var oid = $(this).attr('data-oid');
                var text = $('#textarea_recomment_body_' + oid).val();
                var pid = $(this).attr('data-pid');
                if (pid == null || pid.length <= 0) {
                    pid = 0;
                }
                text = Trim(text, 'g');
                if (pname != null && pname.length != 0) {
                    text = text.replace('@' + pname + '：', '');
                }
                if (common.strLen(text) == 0) {
                    $('#post_callback_msg_' + oid).html('评论内容不能为空!');
                    $('#textarea_recomment_body_' + oid).val("");
                    $('#textarea_recomment_body_' + oid).height('26px');
                    $('#textarea_recomment_body_' + oid).focus();
                    return false;
                } else if (common.strLen(text) > 300) {
                    $('#post_callback_msg_' + rid).html('评论内容长度不能超过300个字符!');
                    return false;
                }

                //全角
                var body = {
                    text: text,
                    pic: ""
                }
                postComment(uniKey, domain, body, oid, pid);
            }
        });

        function Trim(str, is_global) {
            var result;
            result = str.replace(/(^\s+)|(\s+$)/g, "");
            return result;
        }

        //楼中楼翻页
        $('a[name=chidren_reply_page]').live('click', function () {
            var oid = $(this).attr('data-oid');
            var pnum = $(this).attr('data-pno');
            getSubCommentPage($(this), uniKey, domain, oid, pnum);
        });

        //删除
        $('a.remove').live('click', function () {
            if (window.uno == null || window.uid == null) {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            var rid = $(this).attr('data-rid');
            var oid = $(this).attr('data-oid');
            alertOption.title = '';
            alertOption.text = '确定删除该评论？';
            alertOption.submitButtonText = "是";
            alertOption.cancelButtonText = "否";
            alertOption.submitFunction = function bind() {
                removeComment(uniKey, domain, rid, oid);
            };
            joymealert.confirm(alertOption);
        });

        $('a.repost').live('click', function () {
            var oid = $(this).attr('data-oid');
            var rid = $(this).attr('data-rid');
            var name = $(this).attr('data-name');
            $('#replypost_mask_' + oid).hide();
            $('#replypost_mask_' + oid).next().show();
            $('#textarea_recomment_body_' + oid).focus();
            $('#textarea_recomment_body_' + oid).val('@' + name + '：');
            $('#submit_recomment_' + oid).attr('data-pname', name).attr('data-pid', rid);
        });

        $('a[id^=childrenreply_mood_]').live('click', function () {
            var rootid = $(this).attr('id').replace('childrenreply_mood_', '');
            var textareaID = 'textarea_recomment_body_' + rootid
            popzindex = 10062;
            var te = new TextareaEditor(document.getElementById(textareaID));
            var config = {
                allowmultiple: true,
                isremovepop: true,
                isfocus: true
            };
            if (popzindex != undefined || popzindex != null) {
                config.popzindex = popzindex
            }
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);

        });

        $('#faceShow').live('click', function () {
            var textareaID = 'textarea_comment_body';
            popzindex = 10062;
            var te = new TextareaEditor(document.getElementById(textareaID));
            var config = {
                allowmultiple: true,
                isremovepop: true,
                isfocus: true
            };
            if (popzindex != undefined || popzindex != null) {
                config.popzindex = popzindex
            }
            moodBiz.docFaceBindOnReply($(this), textareaID, config, te);
        });

    });

    function agreeComment(unikey, domain, rid) {
        $.ajax({
            url: window.api + "jsoncomment/reply/agree",
            type: "post",
            async: false,
            data: {unikey:unikey,domain:domain,rid:rid},
            dataType: "jsonp",
            jsonpCallback: "agreecallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-10102') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40000') {
                    alertOption.text = '您的请求缺少unikey参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40001') {
                    alertOption.text = '您的请求缺少domain参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40002') {
                    alertOption.text = '您的请求缺少jsonparam参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40003') {
                    alertOption.text = '您的请求中jsonparam参数格式错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40013') {
                    alertOption.text = '您的请求中domain参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40005') {
                    alertOption.text = '评论内容未填写~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40008') {
                    alertOption.text = '评论对象不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40006') {
                    alertOption.text = 'oid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40009') {
                    alertOption.text = '主楼评论已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40007') {
                    alertOption.text = 'pid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40010') {
                    alertOption.text = '上级回复已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40016') {
                    alertOption.text = '您已经赞过了~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40017') {
                    alertOption.text = '内容含有敏感词~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40018') {
                    alertOption.text = '评论不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40019') {
                    alertOption.text = '您已被禁言~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40020') {
                    alertOption.text = '一分钟内不能重复发送相同的内容~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40022') {
                    alertOption.text = '两次评论间隔不能少于15秒，请稍后再试~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-99999') {
                    alertOption.text = '系统维护~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-1') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '1') {
                    var numObj = $('a[name=agree_num][data-rid=' + rid + ']');
                    var objStr = numObj.html().trim();
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
                    alertOption.text = resMsg.msg;
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
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
            data: {unikey:unikey,domain:domain,body:JSON.stringify(body),oid:oid,pid:pid},
            dataType: "jsonp",
            jsonpCallback: "postcallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-10102') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40000') {
                    alertOption.text = '您的请求缺少unikey参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40001') {
                    alertOption.text = '您的请求缺少domain参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40002') {
                    alertOption.text = '您的请求缺少jsonparam参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40003') {
                    alertOption.text = '您的请求中jsonparam参数格式错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40013') {
                    alertOption.text = '您的请求中domain参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40005') {
                    alertOption.text = '评论内容未填写~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40008') {
                    alertOption.text = '评论对象不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40006') {
                    alertOption.text = 'oid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40009') {
                    alertOption.text = '主楼评论已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40007') {
                    alertOption.text = 'pid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40010') {
                    alertOption.text = '上级回复已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40016') {
                    alertOption.text = '您已经赞过了~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40017') {
                    alertOption.text = '内容含有敏感词~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40018') {
                    alertOption.text = '评论不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40019') {
                    alertOption.text = '您已被禁言~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40020') {
                    alertOption.text = '一分钟内不能重复发送相同的内容~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                }else if (resMsg.rs == '-40022') {
                    alertOption.text = '两次评论间隔不能少于15秒，请稍后再试~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                }else if (resMsg.rs == '-99999') {
                    alertOption.text = '系统维护~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-1') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '1') {
                    var result = resMsg.result;
                    if (result == null) {
                        return;
                    } else {
                        var html = getReCommentHtml(result);
                        $('#children_reply_list_' + oid).find('p').remove();
                        if ($('#children_reply_list_' + oid).find('div.clearfix').length > 0) {
                            $('#children_reply_list_' + oid + ' div:first').before(html);
                        } else {
                            $('#children_reply_list_' + oid).append(html);
                        }

                        $('#submit_recomment_' + oid).removeAttr('data-pname').removeAttr('data-pid');
                        $('#textarea_recomment_body_' + oid).val('');
                        $('#post_callback_msg_' + oid).html("");
                        var closeSum = parseInt($('#link_toggle_recomment_close_' + oid).attr('data-reply-sum'));
                        $('#link_toggle_recomment_close_' + oid).attr('data-reply-sum', closeSum + 1);
                        var openSum = parseInt($('#link_toggle_recomment_open_' + oid).attr('data-reply-sum'));
                        $('#link_toggle_recomment_open_' + oid).attr('data-reply-sum', openSum + 1);
                        $('#replypost_mask_' + oid).next().hide();
                        $('#replypost_mask_' + oid).show();
                    }
                } else {
                    alertOption.text = resMsg.msg;
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
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
            data: {unikey:unikey,domain:domain,rid:rid},
            dataType: "jsonp",
            jsonpCallback: "removecallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-10102') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40000') {
                    alertOption.text = '您的请求缺少unikey参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40001') {
                    alertOption.text = '您的请求缺少domain参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40002') {
                    alertOption.text = '您的请求缺少jsonparam参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40003') {
                    alertOption.text = '您的请求中jsonparam参数格式错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40013') {
                    alertOption.text = '您的请求中domain参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40005') {
                    alertOption.text = '评论内容未填写~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40008') {
                    alertOption.text = '评论对象不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40006') {
                    alertOption.text = 'oid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40009') {
                    alertOption.text = '主楼评论已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40007') {
                    alertOption.text = 'pid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40010') {
                    alertOption.text = '上级回复已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40016') {
                    alertOption.text = '您已经赞过了~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40017') {
                    alertOption.text = '内容含有敏感词~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40018') {
                    alertOption.text = '评论不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40019') {
                    alertOption.text = '您已被禁言~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40020') {
                    alertOption.text = '一分钟内不能重复发送相同的内容~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40022') {
                    alertOption.text = '两次评论间隔不能少于15秒，请稍后再试~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-99999') {
                    alertOption.text = '系统维护~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-1') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '1') {
                    $('div[name=cont_cmt_list_' + rid + ']').remove();
                    var sum = $('#link_toggle_recomment_close_' + oid).attr('data-reply-sum');
                    if (parseInt(sum) > 0) {
                        $('#link_toggle_recomment_close_' + oid).attr('data-reply-sum', sum - 1);
                        $('#link_toggle_recomment_open_' + oid).attr('data-reply-sum', sum - 1);
                    }
                } else {
                    alertOption.text = resMsg.msg;
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                }
            },
            error: function () {
                alert('获取失败，请刷新');
            }
        });
    }

    function getSubCommentPage(pageDom, unikey, domain, oid, pnum) {
        $.ajax({
            url: window.api + "jsoncomment/reply/sublist",
            type: "post",
            data: {unikey:unikey,domain:domain,oid:oid,pnum:pnum},
            dataType: "jsonp",
            jsonpCallback: "sublistcallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '0') {
                    return;
                } else if (resMsg.rs == '-1001') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-10102') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40000') {
                    alertOption.text = '您的请求缺少unikey参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40001') {
                    alertOption.text = '您的请求缺少domain参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40002') {
                    alertOption.text = '您的请求缺少jsonparam参数~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40003') {
                    alertOption.text = '您的请求中jsonparam参数格式错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40013') {
                    alertOption.text = '您的请求中domain参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40005') {
                    alertOption.text = '评论内容未填写~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40008') {
                    alertOption.text = '评论对象不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40006') {
                    alertOption.text = 'oid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40009') {
                    alertOption.text = '主楼评论已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40007') {
                    alertOption.text = 'pid参数错误~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40010') {
                    alertOption.text = '上级回复已删除~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40016') {
                    alertOption.text = '您已经赞过了~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40017') {
                    alertOption.text = '内容含有敏感词~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40018') {
                    alertOption.text = '评论不存在~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40019') {
                    alertOption.text = '您已被禁言~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40020') {
                    alertOption.text = '一分钟内不能重复发送相同的内容~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-40022') {
                    alertOption.text = '两次评论间隔不能少于15秒，请稍后再试~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-99999') {
                    alertOption.text = '系统维护~';
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '-1') {
                    alertOption.text = '请先保存您的内容，登录之后再回来~';
                    alertOption.title = '未登录';
                    joymealert.alert(alertOption);
                    return;
                } else if (resMsg.rs == '1') {
                    var result = resMsg.result;
                    if (result == null) {
                        return;
                    } else {
                        var html = getReCommentListCallBack(result);
                        $('#children_reply_list_' + oid).html(html);
                        var page = result.page;
                        calPageDom(page, pageDom);
                    }
                } else {
                    alertOption.text = resMsg.msg;
                    alertOption.title = '错误';
                    joymealert.alert(alertOption);
                    return;
                }
            },
            error: function () {
                alert('获取失败，请刷新');
            }
        });
    }

    function getReCommentListCallBack(commentObj) {
        var reCommentArray = null;
        if (commentObj != null && commentObj.rows != null && commentObj.rows.length > 0) {
            reCommentArray = commentObj.rows;
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

    var calPageDom = function (page, pageDom) {
        var pno = parseInt(pageDom.attr('data-pno'));
        var oid = pageDom.attr('data-oid');

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
                pageNoHtml += '<a name="chidren_reply_page"  href="javascript:void(0)" data-pno="' + i + '" data-oid="' + oid + '">' + i + '</a> ';
            }
        }

        //高亮显示
        var headPageHtml = '';
        if (pno > pageStart) {
            headPageHtml += '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="1" data-oid="' + oid + '">首页</a> ' +
            '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="' + (pno - 1 < 1 ? 1 : pno - 1) + '" data-oid="' + oid + '">上一页</a> ';
        }

        var footPageHtml = '';
        if (pno < pageEnd) {
            footPageHtml += '<a name="chidren_reply_page" href="javascript:void(0)" data-pno="' + (pno + 1 > page.maxPage ? page.maxPage : pno + 1) + '" data-oid="' + oid + '">下一页</a> ' +
            '<a name="chidren_reply_page" href="javascript:void(0);" data-pno="' + page.maxPage + '" data-oid="' + oid + '">末页</a> ';
        }

        var pageHtml = headPageHtml + pageNoHtml + footPageHtml;
        pageAreaDom.html(pageHtml);
    }

    function getReCommentHtml(reCommentObj) {
        var vipHtml = "";
        if (reCommentObj.user.verify != null && reCommentObj.user.verify != 'n') {
            if (reCommentObj.user.verify == 'c') {
                vipHtml += '<a title="着迷机构认证" class="cvip" href="javascript:void(0);"></a>';
            } else if (reCommentObj.user.verify == 'p') {
                vipHtml += '<a title="着迷达人认证" class="pvip" href="javascript:void(0);"></a>';
            } else if (reCommentObj.user.verify == 'i') {
                vipHtml += '<a title="着迷人物认证" class="ivip" href="javascript:void(0);"></a>';
            }
        }
        var pNameHtml = "";
        if (reCommentObj.puser != null) {
            pNameHtml = "@" + reCommentObj.puser.name;
        }
        var agreeHtml = "";
        var agreeNumHtml = "";
        if (parseInt(reCommentObj.reply.agree_sum) > 0) {
            agreeNumHtml += '(' + reCommentObj.reply.agree_sum + ')';
        }
        agreeHtml += '<a href="javascript:void(0);" id="agreelink_' + reCommentObj.reply.rid + '" data-rid="' + reCommentObj.reply.rid + '" class="dianzan"></a><span id="agreenum_' + reCommentObj.reply.rid + '">' +
        '<a href="javascript:void(0);" name="agree_num" data-rid="' + reCommentObj.reply.rid + '">' + agreeNumHtml + '</a></span>&nbsp;';

        var removeHtml = '';
        if (window.uno == reCommentObj.user.uno) {
            removeHtml += '<a href="javascript:void(0);" class="remove" data-rid="' + reCommentObj.reply.rid + '" data-oid="' + reCommentObj.reply.oid + '">删除</a>&nbsp;';
        }
        var reCommentHtml = '<div style="" name="cont_cmt_list_' + reCommentObj.reply.rid + '" class="conmenttx clearfix">' +
            '<div class="conmentface">' +
            '<div class="commenfacecon">' +
            '<a href="javascript:void(0);" title="' + reCommentObj.user.name + '" name="atLink" class="cont_cl_left">' +
            '<img width="33px" height="33px" src="' + reCommentObj.user.icon + '">' +
            '</a>' +
            '</div>' +
            '</div>' +
            '<div class="conmentcon">' +
            '<a title="' + reCommentObj.user.name + '" name="atLink" href="javascript:void(0);">' + reCommentObj.user.name + '</a>' +
            vipHtml +
            pNameHtml +
            '：' + (reCommentObj.reply.body == null ? '' : reCommentObj.reply.body.text) + '<div class="commontft clearfix"><span class="reply_time">' + reCommentObj.reply.post_date + '</span>' +
            '<span class="delete">' +
            agreeHtml +
            removeHtml +
            '<a href="javascript:void(0);" class="repost" data-oid="' + reCommentObj.reply.oid + '" data-rid="' + reCommentObj.reply.rid + '" data-name="' + reCommentObj.user.name + '">回复</a>' +
            '</span>' +
            '</div>' +
            '</div>' +
            '</div>';
        return reCommentHtml;
    }

    var replylivemood = function (moodDom, textareaID, popzindex) {
        moodDom.live('click', function () {

        });
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

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});