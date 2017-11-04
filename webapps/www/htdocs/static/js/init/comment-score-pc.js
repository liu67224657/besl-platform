var joymeEnv = window.location.host.substring(window.location.host.lastIndexOf('.')+1,window.location.host.length);
var joymeEnvNoPort = joymeEnv;
if(joymeEnv.indexOf(':') > 0){
    joymeEnvNoPort = joymeEnv.substring(0, joymeEnv.indexOf(':'));
}
var wwwDomain = 'http://www.joyme.' + joymeEnvNoPort + '/';
var apiDomain = 'http://api.joyme.' + joymeEnvNoPort + '/';
var passportDomain = 'http://passport.joyme.' + joymeEnvNoPort + '/';

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

$(document).ready(function () {
    $('#average_score').html('0分').attr('data-scoresum', '0');
    $('#score_times').html('0');
    $('#short_comment_list').html('<li><a href="javascript:void(0);"><span>发表下您的看法吧...</span></a></li>');
    $('#short_comment_post').html('<input type="text" id="Text" placeholder="少于8个汉字"><button id="Btn">发表<buttom></buttom></button>');

    var jsonParam = {
        title: '',
        pic: '',
        description: '',
        uri: uri,
        star: 0
    }

    getScore(uniKey, domain, jsonParam);

    $('#score_star cite').on('click', function () {
        var score = 0;
        $('#score_star cite').each(function () {
            if ($(this).attr('class') == 'on') {
                score = score + 2;
            }
        });
        if (score > 0) {
            postScore(uniKey, domain, score, encodeURIComponent(uri), jsonParam);
        }
    });

    $('#Btn').on('click', function () {
        var text = trimStr($('#Text').val());
        if (text.length <= 0) {
            alert('内容不能为空');
            return;
        }
        if (text.length > 8) {
            alert('不能超过8个字');
            return;
        }
        var body = {
            text: text,
            pic: ""
        }
        postReply(uniKey, domain, 0, 0, JSON.stringify(body), encodeURIComponent(uri));
    });
});

function checkAgreeReply(rid) {
    //var rid = $(this).attr('id').replace('reply_id_', '');
    agreeReply(uniKey, domain, rid);
}

function trimStr(str) {
    return str.replace(/(^\s*)|(\s*$)/g, "");
}

function getScore(uniKey, domain, jsonParam) {
    $.ajax({
        url: apiDomain + "jsoncomment/score/query",
        type: "post",
        async: true,
        data: {unikey:uniKey,domain:domain,jsonparam:JSON.stringify(jsonParam),pnum:1,psize:100},
        dataType: "jsonp",
        jsonpCallback: "commentscorelistcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '-1001') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-10102') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
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
            } else if (resMsg.rs == '-40004') {
                $('div.pf_tips span:first').html('分数不能为空~')
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
                $('div.pf_tips span:first').html('内容还有敏感词:'+resMsg.result[0]);
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
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null) {
                    return;
                }
                if ($('#average_score').length > 0) {
                    $('#average_score').html(result.score.average_score + '分');
                    $('#average_score').attr('data-scoresum', result.score.score_sum);
                }
                if ($('#score_times').length > 0) {
                    $('#score_times').html(result.score.times_sum);
                }

                if ($('#short_comment_list').length > 0) {
                    if (result.mainreplys != null && result.mainreplys.rows != null && result.mainreplys.rows.length > 0) {
                        var liHtml = '';
                        for (var i = 0; i < result.mainreplys.rows.length; i++) {
                            liHtml = liHtml + '<li><a href="javascript:void(0);"><span id="reply_id_' + result.mainreplys.rows[i].reply.reply.rid + '" onclick="checkAgreeReply(' + result.mainreplys.rows[i].reply.reply.rid + ')">' + result.mainreplys.rows[i].reply.reply.body.text + '(' + result.mainreplys.rows[i].reply.reply.agree_sum + ')</span></a></li>';
                        }
                        $('#short_comment_list').html(liHtml);
                    }
                }
            }else{
                $('div.pf_tips span:first').html(resMsg.msg)
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

function postScore(uniKey, domain, score, rurl, jsonParam) {
    $.ajax({
        url: apiDomain + "jsoncomment/score/post",
        type: "post",
        async: true,
        data: {unikey:uniKey,domain:domain,score:score,jsonparam:JSON.stringify(jsonParam)},
        dataType: "jsonp",
        jsonpCallback: "commentscorepostcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '-1001') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-10102') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
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
            } else if (resMsg.rs == '-40004') {
                $('div.pf_tips span:first').html('分数不能为空~')
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
                $('div.pf_tips span:first').html('内容还有敏感词:'+resMsg.result[0])
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
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '1') {
                var times = parseInt($('#score_times').html());
                $('#score_times').html(times + 1);
                var sum = parseInt($('#average_score').attr('data-scoresum'));
                sum += score;
                var avg = sum / (times + 1);
                avg = Math.round(avg * Math.pow(10, 1)) / Math.pow(10, 1);
                $('#average_score').html(avg + '分').attr('data-scoresum', sum);
                return;
            }else{
                $('div.pf_tips span:first').html(resMsg.msg)
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
        url: apiDomain + "jsoncomment/reply/post",
        type: "post",
        async: true,
        data: {unikey:uniKey,domain:domain,oid:oid,pid:pid,body:body},
        dataType: "jsonp",
        jsonpCallback: "commentscorepostreplycallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '-1001') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-10102') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
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
            } else if (resMsg.rs == '-40004') {
                $('div.pf_tips span:first').html('分数不能为空~')
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
                $('div.pf_tips span:first').html('内容还有敏感词:'+resMsg.result[0])
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
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null || result.reply == null || result.user == null) {
                    return;
                }
                var html = '<li><a href="javascript:void(0);">' +
                    '<span id="reply_id_' + result.reply.rid + '">' + result.reply.body.text + '(' + result.reply.agree_sum + ')</span>' +
                    '</a></li>';

                if ($('#short_comment_list').length > 0) {
                    $('#short_comment_list').append(html);
                }
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

function agreeReply(uniKey, domain, rid) {
    $.ajax({
        url: apiDomain + "jsoncomment/reply/agree",
        type: "post",
        async: true,
        data: {unikey:uniKey,domain:domain,rid:rid},
        dataType: "jsonp",
        jsonpCallback: "agreecallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '-1001') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '-10102') {
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
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
            } else if (resMsg.rs == '-40004') {
                $('div.pf_tips span:first').html('分数不能为空~')
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
                $('div.pf_tips span:first').html('内容还有敏感词:'+resMsg.result[0])
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
                $('div.pf_tips span:first').html('您需要先 <a href="' + passportDomain + 'auth/loginwappage?reurl=' + rurl + '">登录</a>')
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            } else if (resMsg.rs == '1') {
                var text = $('#reply_id_' + rid).html();
                text = text.substring(0, text.lastIndexOf(')'));
                var num = text.substring(text.lastIndexOf('(') + 1, text.length);
                text = text.substring(0, text.lastIndexOf('('));
                num = parseInt(num) + 1;
                $('#reply_id_' + rid).html(text + '(' + num + ')');
            }else {
                $('div.pf_tips span:first').html(resMsg.msg);
                $('div.pf_tips').attr("style", "display: block; z-index: 21;");
                return;
            }
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