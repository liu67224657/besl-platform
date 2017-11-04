var myScroll,myScroll1,
        pullDownEl, pullDownOffset,
        pullUpEl, pullUpOffset,
        pullDownEl1, pullDownOffset1,
        pullUpEl1, pullUpOffset1,
        gCount = 0,
        generatedCount = 0;

/**
 * 下拉刷新 （自定义实现此方法）
 * myScroll.refresh();        // 数据加载完成后，调用界面更新方法
 */
function pullDownAction() {
    var uid = $("#uid").val();
    var logindomain = $("#logindomain").val();
    window.location.replace("http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/list?uid=" + uid + "&retype=miyou&logindomain=" + logindomain);

}

/**
 * 滚动翻页 （自定义实现此方法）
 * myScroll.refresh();        // 数据加载完成后，调用界面更新方法
 */
var bool = true;
function pullUpAction() {
    var uid = $("#uid").val();
    var curNum = $("#miyouCurPage").val();
    var maxNum = $("#miyouMaxPage").val();
    if (curNum == '') {
        return;
    }
    if (parseInt(maxNum) <= parseInt(curNum)) {
        return;
    }
    curNum = parseInt(curNum) + parseInt(1);
    if (bool) {
        bool = false;
        $.ajax({
                    type : "post",
                    url:"/joymeapp/gameclient/json/miyou/miyoulist",
                    data:{uid:uid,count:10,pnum: curNum},
                    success : function(req) {
                        var result = eval('(' + req + ')');
                        if (result.rs == '1') {
                            for (var i = 0; i < result.result.miyoulist.length; i++) {
                                var content = ("<li><div href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.miyoulist[i].commentId + "' class='mi-tit-box'><h2>");
                                for (var j = 0; j < result.result.profiles.length; j++) {
                                    if (result.result.profiles[j].profileId == result.result.miyoulist[i].uri) {
                                        var profileIcon = result.result.profiles[j].icon;
                                        if (profileIcon == '') {
                                            if ("1" == result.result.profiles[j].sex) {
                                                profileIcon = "http://lib.joyme.com/static/theme/default/img/head_boy_m.jpg";
                                            } else if ("1" == result.result.profiles[j].sex) {
                                                profileIcon = "http://lib.joyme.com/static/theme/default/img/head_girl_m.jpg";
                                            } else {
                                                profileIcon = "http://lib.joyme.com/static/theme/default/img/head_is_m.jpg";
                                            }
                                        }
                                        content += " <cite><img src='" + profileIcon + "'  ></cite><span>" + result.result.profiles[j].nick + "</span>"
                                    }
                                }
                                content += "</h2> <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.miyoulist[i].commentId + "'> <p> " + result.result.miyoulist[i].description + "</p><em><b class='ll-num'>" + result.result.miyoulist[i].longCommentSum + "</b><img src=" + result.result.miyoulist[i].pic + "></em>";
                                if (result.result.hotReply != "" && result.result.hotReply != null) {
                                    content += ""
                                    for (var n = 0; n < result.result.hotReply.length; n++) {
                                        if (result.result.miyoulist[i].commentId == result.result.hotReply[n].commentId) {
                                            content += "<div class='mi-text-sp'>";
                                            for (var j = 0; j < result.result.profiles.length; j++) {
                                                if (result.result.profiles[j].profileId == result.result.hotReply[n].replyProfileId) {
                                                    var profileIcon = result.result.profiles[j].icon;
                                                    if (profileIcon == '') {
                                                        if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_boy_m.jpg";
                                                        } else if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_girl_m.jpg";
                                                        } else {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_is_m.jpg";
                                                        }
                                                    }
                                                    content += " <cite><img src='" + profileIcon + "'></cite>"
                                                }
                                            }
                                            content += "<span>" + result.result.hotReply[n].body.text + "</span></div>"
                                        }
                                    }
                                }
                                content += "</a></div>"
                                content += "<p class='mi-tab-btn'>"
                                content += "<a href=\"javascript:nativeShare('" + result.result.miyoulist[i].description + "','" + result.result.miyoulist[i].description + "','" + result.result.miyoulist[i].pic + "','" + result.result.miyoulist[i].commentId + "')\"><span>分享</span> </a>" +
                                        "<a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.miyoulist[i].commentId + "'><span class='ll-num'>";
                                if (result.result.miyoulist[i].commentSum == 0) {
                                    content += "评论</span></a>";
                                } else {
                                    content += "" + result.result.miyoulist[i].commentSum + "</span></a>";
                                }

                                content += "<a href=javascript:agreeMiyou(" + "'" + result.result.miyoulist[i].commentId + "'" + ")  name=" + result.result.miyoulist[i].commentId + " class='zan'>" +
                                        "<span class='ll-num' id='agreesum" + result.result.miyoulist[i].commentId + "' name='agreesum" + result.result.miyoulist[i].commentId + "'>";
                                if (result.result.miyoulist[i].scoreCommentSum == 0) {
                                    content += "赞</span></a></p>";
                                } else {
                                    content += result.result.miyoulist[i].scoreCommentSum + "</span></a></p>";
                                }


                                $("#miyouul").append(content);
                                myScroll.refresh();
                            }
                            $("#miyouCurPage").val(result.result.page.curPage);
                            $("#miyouMaxPage").val(result.result.page.maxPage);
                            for (var i = 0; i < result.result.commentHistories.length; i++) {
                                var commentId = result.result.commentHistories[i].objectId;
                                $("[name='" + commentId + "']").each(function() {
                                    $(this).addClass("active");
                                    $(this).attr("href", "javascript:void(0);");
                                });
                            }
                            browseNum();
                        }
                        bool = true;
                    }});
    }

}

function pullDownAction1() {
    var uid = $("#uid").val();
    var logindomain = $("#logindomain").val();
    window.location.replace("http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/list?uid=" + uid + "&retype=mymiyou&logindomain=" + logindomain);
}

/**
 * 滚动翻页 （自定义实现此方法）
 * myScroll.refresh();        // 数据加载完成后，调用界面更新方法
 */
function pullUpAction1() {
    var uid = $("#uid").val();
    var curNum = $("#mymiyouCurPage").val();
    var maxNum = $("#mymiyouMaxPage").val();
    if (curNum == '') {
        return;
    }
    if (parseInt(maxNum) <= parseInt(curNum)) {
        return;
    }
    curNum = parseInt(curNum) + parseInt(1);
    if (bool) {
        bool = false;
        $.ajax({
                    type : "post",
                    url:"/joymeapp/gameclient/json/miyou/mymiyoulist",
                    data:{uid:uid,count:10,pnum: curNum},
                    success : function(req) {
                        var result = eval('(' + req + ')');
                        if (result.rs == '1') {
                            if (result.result.todayList.length > 0) {
                                if ($(".t1").length == 0) {
                                    var day = $("<h2 class='mi-tit t1'><span><b>今天</b></span></h2>");
                                    $("#day").before(day);
                                }
                                for (var i = 0; i < result.result.todayList.length; i++) {
                                    var now = result.result.todayList[i].createTime;
                                    var time = new Date(now);
                                    var content = "<li><div class='mi-tit-box'><h2 class='mi-tit-time'>昨天" + time.getHours() + ":" + time.getMinutes() + "</h2>";
                                    content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.todayList[i].commentId + "'>";
                                    content += " <p>" + result.result.todayList[i].description + "</p><em><b class='ll-num'>" + result.result.todayList[i].longCommentSum + "</b><img src=" + result.result.todayList[i].pic + "></em>";
                                    if (result.result.hotReply != "" && result.result.hotReply != null) {
                                        content += ""
                                        for (var n = 0; n < result.result.hotReply.length; n++) {
                                            if (result.result.todayList[i].commentId == result.result.hotReply[n].commentId) {
                                                content += "<div class='mi-text-sp'>";
                                                for (var j = 0; j < result.result.profiles.length; j++) {
                                                    if (result.result.profiles[j].profileId == result.result.hotReply[n].replyProfileId) {
                                                        var profileIcon = result.result.profiles[j].icon;
                                                        if (profileIcon == '') {
                                                            if ("1" == result.result.profiles[j].sex) {
                                                                profileIcon = "http://lib.joyme.com/static/theme/default/img/head_boy_m.jpg";
                                                            } else if ("1" == result.result.profiles[j].sex) {
                                                                profileIcon = "http://lib.joyme.com/static/theme/default/img/head_girl_m.jpg";
                                                            } else {
                                                                profileIcon = "http://lib.joyme.com/static/theme/default/img/head_is_m.jpg";
                                                            }
                                                        }
                                                        content += " <cite><img src='" + profileIcon + "'></cite>";
                                                    }
                                                }
                                                content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                            }
                                        }
                                    }
                                    content += "</a></div>";
                                    content += "<p class='mi-tab-btn'>"
                                    content += "<a href=\"javascript:nativeShare('" + result.result.todayList[i].description + "','" + result.result.todayList[i].description + "','" + result.result.todayList[i].pic + "','" + result.result.todayList[i].commentId + "')\"><span>分享</span> </a>" + "<a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.todayList[i].commentId + "'><span class='ll-num'>";
                                    if (result.result.todayList[i].commentSum == 0) {
                                        content += "评论</span></a>";
                                    } else {
                                        content += "" + result.result.todayList[i].commentSum + "</span></a>";
                                    }
                                    content += "<a href=javascript:agreeMiyou(" + "'" + result.result.todayList[i].commentId + "'" + ")  name=" + result.result.todayList[i].commentId + " class='zan'>" +
                                            "<span class='ll-num'  id='agreesum" + result.result.todayList[i].commentId + "' name='agreesum" + result.result.todayList[i].commentId + "'>";
                                    if (result.result.todayList[i].scoreCommentSum == 0) {
                                        content += "赞</span></a></p>";
                                    } else {
                                        content += result.result.todayList[i].scoreCommentSum + "</span></a></p>";
                                    }
                                    $("#thelist1").append(content);
                                }

                            }
                        }
                        if (result.result.yesterdayList.length > 0) {
                            if ($(".t2").length == 0) {
                                var yesterday = $("<h2 class='mi-tit t2'><span><b>昨天</b></span></h2>");
                                $("#thelist2").before(yesterday);
                            }
                            for (var i = 0; i < result.result.yesterdayList.length; i++) {
                                var now = result.result.yesterdayList[i].createTime;
                                var time = new Date(now);
                                var content = "<li><div class='mi-tit-box'><h2 class='mi-tit-time'>昨天" + time.getHours() + ":" + time.getMinutes() + "</h2>";
                                content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.yesterdayList[i].commentId + "'>";
                                content += " <p>" + result.result.yesterdayList[i].description + "</p><em><b class='ll-num'>" + result.result.yesterdayList[i].longCommentSum + "</b><img src=" + result.result.yesterdayList[i].pic + "></em>";
                                if (result.result.hotReply != "" && result.result.hotReply != null) {
                                    content += ""
                                    for (var n = 0; n < result.result.hotReply.length; n++) {
                                        if (result.result.yesterdayList[i].commentId == result.result.hotReply[n].commentId) {
                                            content += "<div class='mi-text-sp'>";
                                            for (var j = 0; j < result.result.profiles.length; j++) {
                                                if (result.result.profiles[j].profileId == result.result.hotReply[n].replyProfileId) {
                                                    var profileIcon = result.result.profiles[j].icon;
                                                    if (profileIcon == '') {
                                                        if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_boy_m.jpg";
                                                        } else if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_girl_m.jpg";
                                                        } else {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_is_m.jpg";
                                                        }
                                                    }
                                                    content += " <cite><img src='" + profileIcon + "'></cite>";
                                                }
                                            }
                                            content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                        }
                                    }
                                }
                                content += "</a></div>";
                                content += "<p class='mi-tab-btn'>"
                                content += "<a href=\"javascript:nativeShare('" + result.result.yesterdayList[i].description + "','" + result.result.yesterdayList[i].description + "','" + result.result.yesterdayList[i].pic + "','" + result.result.yesterdayList[i].commentId + "')\"><span>分享</span> </a>" + "<a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.yesterdayList[i].commentId + "'><span class='ll-num'>";
                                if (result.result.yesterdayList[i].commentSum == 0) {
                                    content += "评论</span></a>";
                                } else {
                                    content += "" + result.result.yesterdayList[i].commentSum + "</span></a>";
                                }
                                content += "<a href=javascript:agreeMiyou(" + "'" + result.result.yesterdayList[i].commentId + "'" + ")  name=" + result.result.yesterdayList[i].commentId + " class='zan'>" +
                                        "<span class='ll-num' id='agreesum" + result.result.yesterdayList[i].commentId + "' name='agreesum" + result.result.yesterdayList[i].commentId + "'>";
                                if (result.result.yesterdayList[i].scoreCommentSum == 0) {
                                    content += "赞</span></a></p>";
                                } else {
                                    content += result.result.yesterdayList[i].scoreCommentSum + "</span></a></p>";
                                }
                                $("#thelist2").append(content);
                            }
                        }
                        if (result.result.dayList.length > 0) {
                            if ($(".t3").length == 0) {
                                var day = $("<h2 class='mi-tit t3'><span><b>前天</b></span></h2>");
                                $("#thelist3").before(day);
                            }
                            for (var i = 0; i < result.result.dayList.length; i++) {
                                var now = result.result.dayList[i].createTime;
                                var time = new Date(now);
                                var content = "<li><div  class='mi-tit-box'><h2 class='mi-tit-time'>前天" + time.getHours() + ":" + time.getMinutes() + "</h2>";
                                content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.dayList[i].commentId + "'>";
                                content += " <p>" + result.result.dayList[i].description + "</p><em><b class='ll-num'>" + result.result.dayList[i].longCommentSum + "</b><img src=" + result.result.dayList[i].pic + "></em>";
                                if (result.result.hotReply != "" && result.result.hotReply != null) {
                                    content += ""
                                    for (var n = 0; n < result.result.hotReply.length; n++) {
                                        if (result.result.dayList[i].commentId == result.result.hotReply[n].commentId) {
                                            content += "<div class='mi-text-sp'>";
                                            for (var j = 0; j < result.result.profiles.length; j++) {
                                                if (result.result.profiles[j].profileId == result.result.hotReply[n].replyProfileId) {
                                                    var profileIcon = result.result.profiles[j].icon;
                                                    if (profileIcon == '') {
                                                        if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_boy_m.jpg";
                                                        } else if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_girl_m.jpg";
                                                        } else {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_is_m.jpg";
                                                        }
                                                    }
                                                    content += " <cite><img src='" + profileIcon + "'></cite>";
                                                }
                                            }
                                            content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                        }
                                    }
                                }
                                content += "</a></div>";
                                content += "<p class='mi-tab-btn'>"
                                content += "<a href=\"javascript:nativeShare('" + result.result.dayList[i].description + "','" + result.result.dayList[i].description + "','" + result.result.dayList[i].pic + "','" + result.result.dayList[i].commentId + "')\"><span>分享</span> </a>" + "<a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.dayList[i].commentId + "'><span class='ll-num'>";
                                if (result.result.dayList[i].commentSum == 0) {
                                    content += "评论</span></a>";
                                } else {
                                    content += "" + result.result.dayList[i].commentSum + "</span></a>";
                                }
                                content += "<a href=javascript:agreeMiyou(" + "'" + result.result.dayList[i].commentId + "'" + ")  name=" + result.result.dayList[i].commentId + " class='zan'>" +
                                        "<span class='ll-num'  id='agreesum" + result.result.dayList[i].commentId + "' name='agreesum" + result.result.dayList[i].commentId + "'>";
                                if (result.result.dayList[i].scoreCommentSum == 0) {
                                    content += "赞</span></a></p>";
                                } else {
                                    content += result.result.dayList[i].scoreCommentSum + "</span></a></p>";
                                }
                                $("#thelist3").append(content);
                            }
                        }

                        if (result.result.moredayList.length > 0) {
                            if ($(".t4").length == 0) {
                                var day = $("<h2 class='mi-tit t4'><span><b>较早之前</b></span></h2>");
                                $("#thelist4").before(day);
                            }
                            for (var i = 0; i < result.result.moredayList.length; i++) {
                                var now = result.result.moredayList[i].createTime;
                                var time = new Date(now);
                                var hours = time.getHours();
                                var minutes = time.getMinutes();
                                minutes = "" + minutes;
                                hours = "" + hours;
                                if (hours.length == 1) {
                                    hours = "0" + hours;
                                }
//                      ;
                                if (minutes.length == 1) {
                                    minutes = "0" + minutes;
                                }
                                var content = "<li><div  class='mi-tit-box'><h2 class='mi-tit-time'>" + time.getFullYear() + "-" + (time.getMonth() + 1) + "-" + time.getDate() + " " + hours + ":" + minutes + "</h2>";
//                            var content = "<li><div href='#' class='mi-tit-box'><h2 class='mi-tit-time'>" + time + "</h2>";
                                content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.moredayList[i].commentId + "'>";
                                content += " <p>" + result.result.moredayList[i].description + "</p><em><b class='ll-num'>" + result.result.moredayList[i].longCommentSum + "</b><img src=" + result.result.moredayList[i].pic + "></em>";
                                if (result.result.hotReply != "" && result.result.hotReply != null) {
                                    content += ""
                                    for (var n = 0; n < result.result.hotReply.length; n++) {
                                        if (result.result.moredayList[i].commentId == result.result.hotReply[n].commentId) {
                                            content += "<div class='mi-text-sp'>";
                                            for (var j = 0; j < result.result.profiles.length; j++) {
                                                if (result.result.profiles[j].profileId == result.result.hotReply[n].replyProfileId) {
                                                    var profileIcon = result.result.profiles[j].icon;
                                                    if (profileIcon == '') {
                                                        if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_boy_m.jpg";
                                                        } else if ("1" == result.result.profiles[j].sex) {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_girl_m.jpg";
                                                        } else {
                                                            profileIcon = "http://lib.joyme.com/static/theme/default/img/head_is_m.jpg";
                                                        }
                                                    }
                                                    content += " <cite><img src='" + profileIcon + "'></cite>";
                                                }
                                            }
                                            content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                        }
                                    }
                                }
                                content += "</a></div>";
                                content += "<p class='mi-tab-btn'>"
                                content += "<a href=\"javascript:nativeShare('" + result.result.moredayList[i].description + "','" + result.result.moredayList[i].description + "','" + result.result.moredayList[i].pic + "','" + result.result.moredayList[i].commentId + "')\"><span>分享</span> </a>" + "<a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.moredayList[i].commentId + "'><span class='ll-num'>";
                                if (result.result.moredayList[i].commentSum == 0) {
                                    content += "评论</span></a>";
                                } else {
                                    content += "" + result.result.moredayList[i].commentSum + "</span></a>";
                                }
                                content += "<a href=javascript:agreeMiyou(" + "'" + result.result.moredayList[i].commentId + "'" + ")  name=" + result.result.moredayList[i].commentId + " class='zan'>" +
                                        "<span class='ll-num' id='agreesum" + result.result.moredayList[i].commentId + "' name='agreesum" + result.result.moredayList[i].commentId + "'>";
                                if (result.result.moredayList[i].scoreCommentSum == 0) {
                                    content += "赞</span></a></p>";
                                } else {
                                    content += result.result.moredayList[i].scoreCommentSum + "</span></a></p>";
                                }
                                $("#thelist4").append(content);
                            }
                        }
                        myScroll1.refresh();
                        $("#mymiyouCurPage").val(result.result.mymiyoupage.curPage);
                        $("#mymiyouMaxrPage").val(result.result.mymiyoupage.maxPage);
                          browseNum();
                        for (var i = 0; i < result.result.commentHistories.length; i++) {
                            var commentId = result.result.commentHistories[i].objectId;
                            $("[name='" + commentId + "']").each(function() {
                                $(this).addClass("active");
                                $(this).attr("href", "javascript:void(0);");
                            });
                        }
                        bool = true;
                    }
                });
    }


}
/**
 * 初始化iScroll控件
 */
function loaded() {
    pullDownEl = document.getElementById('pullDown');
    pullDownOffset = pullDownEl.offsetHeight;
    pullUpEl = document.getElementById('pullUp');
    pullUpOffset = pullUpEl.offsetHeight;
    /*我的迷友*/
    pullDownEl1 = document.getElementById('pullDown1');
    pullDownOffset1 = pullDownEl1.offsetHeight;
    pullUpEl1 = document.getElementById('pullUp1');
    pullUpOffset1 = pullUpEl1.offsetHeight;

    //迷友
    myScroll = new iScroll('js-scroller', {
                vScroll:true,
                vScrollbar:false,
                useTransition: true,
                checkDOMChanges:true,
                topOffset: 52,
                onRefresh: function () {
                    var curNum = $("#miyouCurPage").val();
                    var maxNum = $("#miyouMaxPage").val();
                    if (parseInt(maxNum) <= parseInt(curNum)) {
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '没有更多数据了';
                        pullUpEl.querySelector('.spinner').style.display = "none";

                        return;
                    }
                    if (pullDownEl.className.match('loading')) {
                        pullDownEl.className = '';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉可以刷新';
                    } else if (pullUpEl.className.match('loading')) {
                        pullUpEl.className = '';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载也是个正经事儿';
                    }
                },
                onScrollMove: function () {
                    if (this.y > 5 && !pullDownEl.className.match('flip')) {

                        pullDownEl.className = 'flip';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '释放立即刷新';
                        this.minScrollY = 0;
                    } else if (this.y < 5 && pullDownEl.className.match('flip')) {
                        pullDownEl.className = '';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '下拉可以刷新';
                        this.minScrollY = -pullDownOffset;
                    } else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
                        var curNum = $("#miyouCurPage").val();
                        var maxNum = $("#miyouMaxPage").val();
                        if (parseInt(maxNum) <= parseInt(curNum)) {
                            pullUpEl.querySelector('.pullUpLabel').innerHTML = '没有更多数据了';
                            pullUpEl.querySelector('.spinner').style.display = "none";

                            return;
                        }
                        pullUpEl.className = 'flip';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放立即刷新';
                        this.maxScrollY = this.maxScrollY;
                    } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
                        pullUpEl.className = '';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '加载也是个正经事儿';
                        this.maxScrollY = pullUpOffset;
                    }
                },
                onScrollEnd: function () {
                    if (pullDownEl.className.match('flip')) {
                        pullDownEl.className = 'loading';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '别闹走心加载中';
                        pullDownAction();
                    } else if (pullUpEl.className.match('flip')) {
                        pullUpEl.className = 'loading';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '别闹走心加载中';
                        pullUpAction();
                    }
                }
            });
    setTimeout(function () {
        document.getElementById('js-scroller').style.left = '0';
    }, 800);

    //我的迷友圈
    myScroll1 = new iScroll('js-scroller-two', {
                vScroll:true,
                vScrollbar:false,
                useTransition: true,
                checkDOMChanges:true,
                topOffset: 52,
                onRefresh: function () {
                    var curNum = $("#mymiyouCurPage").val();
                    var maxNum = $("#mymiyouMaxPage").val();
                    if (parseInt(maxNum) <= parseInt(curNum)) {
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '没有更多数据了';
                        pullUpEl1.querySelector('.spinner').style.display = "none";
                        return;
                    }
                    if (pullDownEl1.className.match('loading')) {
                        pullDownEl1.className = '';
                        pullDownEl1.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
                    } else if (pullUpEl1.className.match('loading')) {
                        pullUpEl1.className = '';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
                    }
                },
                onScrollMove: function () {
                    if (this.y > 5 && !pullDownEl.className.match('flip')) {
                        pullDownEl1.className = 'flip';
                        pullDownEl1.querySelector('.pullDownLabel').innerHTML = '松手开始更新...';
                        this.minScrollY = 0;
                    } else if (this.y < 5 && pullDownEl1.className.match('flip')) {
                        pullDownEl1.className = '';
                        pullDownEl1.querySelector('.pullDownLabel').innerHTML = '下拉刷新...';
                        this.minScrollY = -pullDownOffset1;
                    } else if (this.y < (this.maxScrollY - 5) && !pullUpEl1.className.match('flip')) {
                        var curNum = $("#mymiyouCurPage").val();
                        var maxNum = $("#mymiyouMaxPage").val();
                        if (parseInt(maxNum) <= parseInt(curNum)) {
                            pullUpEl1.querySelector('.pullUpLabel').innerHTML = '没有更多数据了';
                            pullUpEl1.querySelector('.spinner').style.display = "none";
                            return;
                        }
                        pullUpEl1.className = 'flip';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '松手开始更新...';
                        this.maxScrollY = this.maxScrollY;
                    } else if (this.y > (this.maxScrollY + 5) && pullUpEl1.className.match('flip')) {
                        pullUpEl1.className = '';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '上拉加载更多...';
                        this.maxScrollY = pullUpOffset1;
                    }
                },
                onScrollEnd: function () {
                    if (pullDownEl1.className.match('flip')) {
                        pullDownEl1.className = 'loading';
                        pullDownEl1.querySelector('.pullDownLabel').innerHTML = '加载中...';
                        pullDownAction1();
                    } else if (pullUpEl1.className.match('flip')) {
                        pullUpEl1.className = 'loading';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '加载中...';
                        pullUpAction1();
                    }
                }
            });
    setTimeout(function () {
        document.getElementById('js-scroller-two').style.left = '0';
    }, 800);
    $('.mi-tab-tit>span').on('touchstart', function() {
        $(this).addClass('active').siblings().removeClass('active');
        var ind = $(this).index();
        $('.mi-tab-cont>div.ui-scroller').eq(ind).addClass('active').siblings().removeClass('active');
        myScroll1.refresh();
        myScroll.refresh();
    });

}
//初始化绑定iScroll控件 
document.addEventListener('touchmove', function (e) {
    e.preventDefault();
}, false);
window.addEventListener('load', loaded);

