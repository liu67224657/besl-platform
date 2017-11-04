$(document).ready(function () {
    var joymeEnv = window.location.host.substring(window.location.host.lastIndexOf('.')+1,window.location.host.length);
    var joymeEnvNoPort = joymeEnv;
    if(joymeEnv.indexOf(':') > 0){
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
        if (uri.indexOf('http://www.joyme.'+joymeEnv+'/') == 0 || uri.indexOf('https://www.joyme.'+joymeEnv+'/') == 0) {
            var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
            var removeFid = uri.replace('/' + fid, '');
            var wikiKey = removeFid.substring(removeFid.lastIndexOf("/") + 1, removeFid.length);
            uniKey = wikiKey + "|" + fid;
        } else if (uri.indexOf('http://wiki.joyme.'+joymeEnv+'/') == 0 || uri.indexOf('https://wiki.joyme.'+joymeEnv+'/') == 0) {
            var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
            var removeFid = uri.replace('/' + fid, '');
            var wikiKey = removeFid.substring(removeFid.lastIndexOf("/") + 1, removeFid.length);
            uniKey = wikiKey + "|" + fid;
        } else if (uri.indexOf('http://m.wiki.joyme.'+joymeEnv+'/') == 0 || uri.indexOf('https://m.wiki.joyme.'+joymeEnv+'/') == 0) {
            var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
            var removeFid = uri.replace('/' + fid, '');
            var wikiKey = removeFid.substring(removeFid.lastIndexOf("/") + 1, removeFid.length);
            uniKey = wikiKey + "|" + fid;
        } else {
            var host = window.location.host;
            var reg = /^[a-zA-Z0-9]+\.joyme\.(test|alpha|beta|com):{0,1}[0-9]*$/;
            if(reg.test(host)){
                var fid = uri.substring(uri.lastIndexOf("/") + 1, uri.length);
                var wikiKey = host.substring(0, host.indexOf('.'));
                uniKey = wikiKey + "|" + fid;
            }
        }
    }
    var domain = 5;

    if ($('div.pf_tag div i').length > 0) {
        $('div.pf_tag div i').html(0);
    }
    if ($('div.pf_user b').length > 0) {
        $('div.pf_user b').html(0);
    }
    if ($('div.pf_tag').length > 0) {
        $('div.pf_tag').parent('a').attr('href', window.www + 'comment/score/page?unikey=' + uniKey + '&domain=' + domain + '&pnum=1&psize=100');
    }
    if ($('div.pic_comment').length > 0) {
        $('div.pic_comment').html('<span>快来评价吧...</span><a class="pic_commentLink" href="' + window.www + 'comment/score/page?unikey=' + uniKey + '&domain=' + domain + '&pnum=1&psize=100">去评价&gt;&gt;</a>');
    }

    var pic = '';
    if ($('b.pet_img img').length > 0) {
        pic = $('b.pet_img img').attr('src');
    }
    var title = '';
    if ($('#card_name').length > 0) {
        title = $('#card_name').text();
    }
    if(title.length > 32){
        title = title.substring(0, 32);
    }
    var description = '';
    if ($('#card_no').length > 0) {
        description = $('#card_no').text();
    }

    var starNum = 0;
    if ($('#card_xingxing').length > 0) {
        starNum = $('#card_xingxing').find('cite').length;
    }

    var jsonParam = {
        title: title,
        pic: pic,
        description: description,
        uri: uri,
        star: starNum
    }
    if (uniKey.length > 0 && title.length > 0 && pic.length > 0 && uri.length > 0) {
        getScore(uniKey, domain, jsonParam);
    }

    $('div.pic_comment span').on('click', function () {
        var rid = $(this).attr('id').replace('reply_id_', '');
        agreeReply(uniKey, domain, rid);
    });

});

function getScore(uniKey, domain, jsonParam) {
    $.ajax({
        url: window.api + "jsoncomment/score/query",
        type: "post",
        async: false,
        data: {unikey: uniKey, domain: domain, jsonparam: JSON.stringify(jsonParam), pnum: 1, psize: 100},
        dataType: "jsonp",
        jsonpCallback: "commentscorelistcallback",
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
            } else if (resMsg.rs == '-40014') {
                alert('您今天已经对该卡牌进行了20次评分~');
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
                }

                if ($('div.pf_tag').length > 0) {
                    if (result.score != null) {
                        var html = '<div><i>' + result.score.average_score + '</i></div><span class="pf_user"><cite></cite><b>' + result.score.times_sum + '</b></span>';
                        $('div.pf_tag').html(html);
                    }
                }
                if ($('div.pic_comment').length > 0) {
                    if (result.mainreplys != null && result.mainreplys.rows != null && result.mainreplys.rows.length > 0) {
                        var spanHtml = '';
                        var maxLength = (result.mainreplys.rows.length > 5 ? 5 : result.mainreplys.rows.length);
                        for (var i = 0; i < maxLength; i++) {
                            spanHtml = spanHtml + '<span id="reply_id_' + result.mainreplys.rows[i].reply.reply.rid + '">' + result.mainreplys.rows[i].reply.reply.body.text + '(<code>' + result.mainreplys.rows[i].reply.reply.agree_sum + '</code>)</span>'
                        }
                        $('div.pic_comment').html(spanHtml + '<a class="pic_commentLink" href="' + window.www + 'comment/score/page?unikey=' + uniKey + '&domain=' + domain + '&pnum=1&psize=100">去评价&gt;&gt;</a>');
                    }
                }
            }else{
                alert(resMsg.msg);
                return;
            }
        },
        error: function () {
            alert('获取失败，请刷新');
            return;
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
//            var numStr = $('#agree_reply_' + rid).html().replace('赞+', '');
//            var num = parseInt(numStr) + 1
//            $('#agree_reply_' + rid).html('赞+' + num);
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