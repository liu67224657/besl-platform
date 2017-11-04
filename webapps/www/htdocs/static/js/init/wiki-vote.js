$(document).ready(function () {
    window.uno = getCookie('joyme_u');

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

    var name = '';
    if ($('#card_name').length > 0) {
        name = $('#card_name').html();
    }

    var pic = '';
    if ($('.pet_img img').length > 0) {
        pic = $('.pet_img img').attr('src');
    }
    if (pic.length == 0) {
        if ($('#card_pic img').length > 0) {
            pic = $('#card_pic img').attr('src');
        }
    }
    var num = '';
    if ($('#card_no').length > 0) {
        num = $('#card_no').html();
    }
    var sum = 0;
    if ($('#card_votes').length > 0) {
        sum = parseInt($('#card_votes').html());
    }
    if (uri.length > 0 && name.length > 0 && pic.length > 0) {
        getVote(uri, name, pic, num, sum);
    }

    //投票
    $('#click_to_vote').on('click', function () {
        var sum = parseInt($('#card_votes').html());
        incVote(uri, sum);
    });

    var x = 0;
    $('div.skill_menu span').each(function () {
        $(this).attr('id', 'span_shift_' + x);
        x = x + 1;
    });
    var y = 0;
    $('div.recommend').each(function () {
        $(this).attr('id', 'div_recommend_' + y);
        y = y + 1;
    });


    $('div.skill_menu span').on('touchstart', function () {
        var idx = $(this).attr('id').replace('span_shift_', '');
        if (idx == '1') {
            $('#div_recommend_0').attr('style', 'display: none;');
            $('#div_recommend_1').attr('style', 'display: block;');
//            queryScoreByAvg(uniKey, "avgscore");
//            queryVoteByPage(url, id);
        } else if (idx == '0') {
            $('#div_recommend_1').attr('style', 'display: none;');
            $('#div_recommend_0').attr('style', 'display: block;');
//            var jsonArr = new Array();
//            $('code[data-group = card_votes]').each(function () {
//                if ($(this).length > 0) {
//                    var pageId = $(this).attr('data-url');
//                    if (pageId.length > 0) {
//                        var obj = {
//                            'url': wikiKey + "|" + pageId
//                        }
//                        jsonArr.push(obj);
//                    }
//                }
//            });
//            if (jsonArr.length > 0) {
//                queryScores(JSON.stringify(jsonArr), "score", idx);
////                queryVotes(JSON.stringify(jsonArr), id);
//            }
        }
    });

//    var index = 0;
//    if ($('div.recommend[style = "display:block"]').length > 0) {
//        index = $('div.recommend[style = "display:block"]').attr('id').replace('div_recommend_', '');
//    }
//    if (index == '1') {
//        queryScoreByAvg(uniKey, "avgscore");
//    } else if (index == '0') {
//    var jsonArr = new Array();
//    $('code[data-group = card_votes]').each(function () {
//        if ($(this).length > 0) {
//            var pageId = $(this).attr('data-url');
//            if (pageId.length > 0) {
//                var obj = {
//                    'url': wikiKey + "|" + pageId
//                }
//                jsonArr.push(obj);
//            }
//        }
//    });
//    if (jsonArr.length > 0) {
//        queryScores(JSON.stringify(jsonArr), "score", index);
//    }
//    }
});

var callback = {
    getVoteCallback: function (resMsg) {
        if (resMsg != null) {
            if (resMsg.rs == '0') {
            } else {
                if (resMsg.result != null) {
                    var votesum = resMsg.result.votesum;
                    $('#card_votes').html(votesum);
                }
            }
        }
    },

    incVoteCallback: function (resMsg, sum) {
        if (resMsg != null) {
            if (resMsg.rs == '0') {
            } else {
//                var votesum = sum + 1;
//                $('#card_votes').html(votesum);
            }
        }
    },

    queryVotesCallback: function (resMsg, id) {
        if (resMsg != null) {
            if (resMsg.rs == '0') {
            } else {
                if (resMsg.result != null && resMsg.result.length > 0) {
                    var html = "";
                    for (var i = 0; i < resMsg.result.length; i++) {
                        var althtml = resMsg.result[i].pic.substring(resMsg.result[i].pic.lastIndexOf('/') + 1, resMsg.result[i].pic.length);
                        var ahtml = '<a href="' + resMsg.result[i].url + '">' +
                            '<div class="skill_link">' +
                            '<b><img alt="' + althtml + '" src="' + resMsg.result[i].pic + '" style="width: 100%;max-width: 120px;height:auto"/></b>' +
                            '<span><em>' + resMsg.result[i].name + '</em><cite class="No_num">' + resMsg.result[i].nonum + '</cite><code data-group="card_votes" data-url="' + resMsg.result[i].url + '">票数：' + resMsg.result[i].votesum + '</code></span>' +
                            '<i class="details_link fr">投我一票</i>' +
                            '</div>' +
                            '</a>';
                        html = html + ahtml;
                    }
                    $('#div_recommend_' + id).html(html);
                }
            }
        }
    },

    queryVoteByPageCallback: function (resMsg, id) {
        if (resMsg != null) {
            if (resMsg.rs == '0') {
            } else {
                if (resMsg.result != null && resMsg.result.length > 0) {
                    var html = "";
                    for (var i = 0; i < resMsg.result.length; i++) {
                        var althtml = resMsg.result[i].pic.substring(resMsg.result[i].pic.lastIndexOf('/') + 1, resMsg.result[i].pic.length);
                        var ahtml = '<a href="' + resMsg.result[i].url + '">' +
                            '<div class="skill_link">' +
                            '<b><img alt="' + althtml + '" src="' + resMsg.result[i].pic + '" style="width: 100%;max-width: 120px;height:auto"/></b>' +
                            '<span><em>' + resMsg.result[i].name + '</em><cite class="No_num">' + resMsg.result[i].nonum + '</cite><code data-group="card_votes" data-url="' + resMsg.result[i].url + '">票数：' + resMsg.result[i].votesum + '</code></span>' +
                            '<i class="details_link fr">投我一票</i>' +
                            '</div>' +
                            '</a>';
                        html = html + ahtml;
                    }
                    $('#div_recommend_' + id).html(html);
                }
            }
        }
    }
}

function queryScores(jsonarr, flag, index) {
    $.ajax({
        url: window.api + "jsoncomment/reply/query",
        type: "post",
        async: false,
        data: {flag:flag,jsonparam:jsonarr,pnum:1,psize:50},
        dataType: "jsonp",
        jsonpCallback: "queryscorebyidcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null || result.length <= 0) {
                    return;
                }
                $('#div_recommend_1').attr('style', 'display: none;');
                $('#div_recommend_0').attr('style', 'display: block;');
                var html = '';
                for (var i = 0; i < result.length; i++) {
                    var starHtml = '';
                    if (result[i].longCommentSum > 0) {
                        for (var j = 0; j < result[i].longCommentSum; j++) {
                            starHtml += '★';
                        }
                    } else {
                        starHtml += '★★★★★';
                    }

                    var pageId = result[i].uniqueKey.substring(result[i].uniqueKey.lastIndexOf("|") + 1, result[i].uniqueKey.length);
                    html = '<div class="skill_link"><b>' +
                        '<img alt="' + result[i].pic + '" src="' + result[i].pic + '" original="' + result[i].pic + '">' +
                        '</b><span><em>' + result[i].title + '</em><cite class="No_num">' + result[i].description + '</cite>' +
                        '<code data-group="card_votes" data-url="' + pageId + '">' + starHtml + '</code></span>' +
                        '<div class="vote_box">' +
                        '<cite>' + result[i].averageScore + '</cite>' +
                        '<span>评分</span></div></div>';
                    var id = result[i].uniqueKey.substring(result[i].uniqueKey.lastIndexOf("|") + 1, result[i].uniqueKey.lastIndexOf("."));
                    $('#' + id).html(html);
                }
            }
        },
        error: function () {
            return;
        }
    });
}

function queryScoreByAvg(unikey, flag) {
    $.ajax({
        url: window.api + "jsoncomment/reply/query",
        type: "post",
        async: false,
        data: {flag:flag,unikey:unikey,pnum:1,psize:50},
        dataType: "jsonp",
        jsonpCallback: "queryscorebyavgcallback",
        success: function (req) {
            var resMsg = req[0];
            if (resMsg.rs == '0') {
                return;
            } else if (resMsg.rs == '1') {
                var result = resMsg.result;
                if (result == null || result.length <= 0) {
                    return;
                }
                $('#div_recommend_0').attr('style', 'display: none;');
                $('#div_recommend_1').attr('style', 'display: block;');
                var html = '';
                for (var i = 0; i < result.length; i++) {
                    var starHtml = '';
                    if (result[i].longCommentSum > 0) {
                        for (var j = 0; j < result[i].longCommentSum; j++) {
                            starHtml += '★';
                        }
                    } else {
                        starHtml += '★★★★★';
                    }

                    var pageId = result[i].uniqueKey.substring(result[i].uniqueKey.lastIndexOf("|") + 1, result[i].uniqueKey.length);
                    html += '<a href="' + pageId + '"><div class="skill_link">' +
                        '<b><img alt="' + result[i].pic + '" src="' + result[i].pic + '" original="' + result[i].pic + '"></b>' +
                        '<span><em>' + result[i].title + '</em><cite class="No_num">' + result[i].description + '</cite>' +
                        '<code data-group="card_votes" data-url="' + pageId + '">' + starHtml + '</code></span>' +
                        '<div class="vote_box"><cite>' + result[i].averageScore + '</cite>' +
                        '<span >评分</span></div></div></a></div>';
                }
                $('#div_recommend_1').html(html);
            }
        },
        error: function () {
            return;
        }
    });
}

function getVote(url, name, pic, num, sum) {
    $.post(window.www + "wiki/vote/get", {url: url, name: name, pic: pic, num: num, sum: sum}, function (req) {
        var resMsg = eval('(' + req + ')');
        callback.getVoteCallback(resMsg);
    });
}

function incVote(url, sum) {
    $.post(window.www + "wiki/vote/inc", {url: url}, function (req) {
        var resMsg = eval('(' + req + ')');
        callback.incVoteCallback(resMsg, sum);
    });
}

function queryVotes(jsonArr, id) {
    $.post(window.www + "wiki/vote/query", {jsonarr: jsonArr}, function (req) {
        var resMsg = eval('(' + req + ')');
        callback.queryVotesCallback(resMsg, id);
    });
}

function queryVoteByPage(url, id) {
    $.post(window.www + "wiki/vote/query", {url: url}, function (req) {
        var resMsg = eval('(' + req + ')');
        callback.queryVoteByPageCallback(resMsg, id);
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