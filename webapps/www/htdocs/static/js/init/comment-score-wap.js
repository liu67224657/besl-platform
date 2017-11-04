$(document).ready(function () {
    var joymeEnv = window.location.host.substring(window.location.host.lastIndexOf('.') + 1, window.location.host.length);
    var joymeEnvNoPort = joymeEnv;
    if (joymeEnv.indexOf(':') > 0) {
        joymeEnvNoPort = joymeEnv.substring(0, joymeEnv.indexOf(':'));
    }
    window.www = 'http://www.joyme.' + joymeEnvNoPort + '/';
    window.api = 'http://api.joyme.' + joymeEnvNoPort + '/';
    window.passport = 'http://passport.joyme.' + joymeEnvNoPort + '/';

    var rurl = encodeURIComponent(window.location.href);
    var uniKey = $('#input_hidden_unikey').val();
    var domain = $('#input_hidden_domain').val();

    var jsonParam = {
        title: '',
        pic: '',
        description: '',
        uri: rurl,
        star: 0
    }

    $('span.pf_btn').on('click', function () {
        var score = 0;
        $('span.grade_icon cite').each(function () {
            if ($(this).attr('class') == 'on') {
                score = score + 2;
            }
        });
        if (score > 0) {
            postScore(uniKey, domain, score, rurl, jsonParam);
        }
    });

    $('a.pf_comment_btn').on('click', function () {
        var text = $('input.pf_comment_text').val();
        if (text.length <= 0) {
            $('div.pf_tips span:first').html('内容不能为空')
            $('div.pf_tips').attr("style", "display: block; z-index: 9;");
            return;
        }
        if (text.length > 8) {
            $('div.pf_tips span:first').html('不能超过8个字')
            $('div.pf_tips').attr("style", "display: block; z-index: 9;");
            return;
        }

        var body = {
            text: text,
            pic: ""
        }
        postReply(uniKey, domain, 0, 0, JSON.stringify(body), rurl);
    });

    $('em.details_link cite').on('click', function () {
        var rid = $(this).attr('id').replace('agree_reply_', '');
        agreeReply(uniKey, domain, rid);
    });
});


function postScore(uniKey, domain, score, rurl, jsonParam) {
    $.ajax({
        url: window.api + "jsoncomment/score/post",
        type: "post",
        async: false,
        data: {unikey: uniKey, domain: domain, score: score, jsonparam: JSON.stringify(jsonParam)},
        dataType: "jsonp",
        jsonpCallback: "commentscorepostcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '-1001') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + window.passport + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-10102') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + window.passport + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40000') {
                $('div.pf_tips span:first').html('您的请求缺少unikey参数~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40001') {
                $('div.pf_tips span:first').html('您的请求缺少domain参数~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40002') {
                $('div.pf_tips span:first').html('您的请求缺少jsonparam参数~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40003') {
                $('div.pf_tips span:first').html('您的请求中jsonparam参数格式错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40013') {
                $('div.pf_tips span:first').html('您的请求中domain参数错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40014') {
                $('div.pf_tips span:first').html('您今天已经对该卡牌进行了20次评分~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40005') {
                $('div.pf_tips span:first').html('评论内容未填写~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40008') {
                $('div.pf_tips span:first').html('评论对象不存在~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40006') {
                $('div.pf_tips span:first').html('oid参数错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40009') {
                $('div.pf_tips span:first').html('主楼评论已删除~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40007') {
                $('div.pf_tips span:first').html('pid参数错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40010') {
                $('div.pf_tips span:first').html('上级回复已删除~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40016') {
                $('div.pf_tips span:first').html('您已经赞过了~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40017') {
                $('div.pf_tips span:first').html('内容含有敏感词:' + resMsg.result[0]);
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40018') {
                $('div.pf_tips span:first').html('评论不存在~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40019') {
                $('div.pf_tips span:first').html('您已被禁言~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40020') {
                $('div.pf_tips span:first').html('一分钟内不能重复发送相同的内容~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-99999') {
                $('div.pf_tips span:first').html('系统维护~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-1') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + window.passport + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '1') {
                var num = parseInt($('span.pf_user b').html());
                $('span.pf_user b').html(num + 1)
                $('div.pf_tips span:first').html('恭喜您，评分成功~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
            } else {
                $('div.pf_tips span:first').html(resMsg.msg);
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
            return;
        }
    });
}

function postReply(uniKey, domain, oid, pid, body, rurl) {
    $.ajax({
        url: window.api + "jsoncomment/reply/post",
        type: "post",
        async: false,
        data: {unikey: uniKey, domain: domain, oid: oid, pid: pid, body: body},
        dataType: "jsonp",
        jsonpCallback: "postcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '-1001') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + window.passport + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-10102') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + window.passport + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40000') {
                $('div.pf_tips span:first').html('您的请求缺少unikey参数~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40001') {
                $('div.pf_tips span:first').html('您的请求缺少domain参数~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40002') {
                $('div.pf_tips span:first').html('您的请求缺少jsonparam参数~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40003') {
                $('div.pf_tips span:first').html('您的请求中jsonparam参数格式错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40013') {
                $('div.pf_tips span:first').html('您的请求中domain参数错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40014') {
                $('div.pf_tips span:first').html('您今天已经对该卡牌进行了20次评分~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40005') {
                $('div.pf_tips span:first').html('评论内容未填写~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40008') {
                $('div.pf_tips span:first').html('评论对象不存在~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40006') {
                $('div.pf_tips span:first').html('oid参数错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40009') {
                $('div.pf_tips span:first').html('主楼评论已删除~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40007') {
                $('div.pf_tips span:first').html('pid参数错误~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40010') {
                $('div.pf_tips span:first').html('上级回复已删除~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40016') {
                $('div.pf_tips span:first').html('您已经赞过了~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40017') {
                $('div.pf_tips span:first').html('内容含有敏感词:' + resMsg.result[0]);
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40018') {
                $('div.pf_tips span:first').html('评论不存在~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40019') {
                $('div.pf_tips span:first').html('您已被禁言~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-40020') {
                $('div.pf_tips span:first').html('一分钟内不能重复发送相同的内容~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-99999') {
                $('div.pf_tips span:first').html('系统维护~')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-1') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + window.passport + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null || result.reply == null || result.user == null) {
                    return;
                }
                var html = '<div class="pf_comment_list">' +
                    '<span>' +
                    '<b><img alt="" src="' + result.user.icon + '" title=""></b>' +
                    '<em>' + result.user.name + '</em>' +
                    '</span>' +
                    '<em class="details_link fr"><cite id="agree_reply_' + result.reply.rid + '">赞+' + result.reply.agree_sum + '</cite></em>' +
                    '<div>' + result.reply.body.text + '</div>' +
                    '</div>';

                if ($('div.pf_comment_list').length > 0) {
                    $('div.con_pet_comment div:first').before(html);
                } else {
                    $('div.con_pet_comment').append(html);
                }

            } else {
                $('div.pf_tips span:first').html(resMsg.msg);
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
            }
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
}

function agreeReply(uniKey, domain, rid) {
    $.ajax({
        url: window.api + "jsoncomment/reply/agree",
        type: "post",
        async: false,
        data: {unikey: uniKey, domain: domain, rid: rid},
        dataType: "jsonp",
        jsonpCallback: "agreecallback",
        success: function (req) {
            var numStr = $('#agree_reply_' + rid).html().replace('赞+', '');
            var num = parseInt(numStr) + 1
            $('#agree_reply_' + rid).html('赞+' + num);
        },
        error: function () {
            alert('获取失败，请刷新');
        }
    });
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