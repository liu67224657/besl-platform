window.addEventListener('DOMContentLoaded', function() {
    var w = parseInt($(window).width() - 20);
    $('.inner').on('touchstart', function(ev) {
        ev.stopPropagation();
    });
    var drop = $('#inner').dropload({
                domUp : {
                    domClass   : 'dropload-up',
                    domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
                    domUpdate  : '<div class="dropload-update">↑释放更新</div>',
                    domLoad    : '<div class="dropload-load"><span class="load-icon"></span>加载中...</div>'
                },
                loadUpFn : function(me) {
                    setTimeout(function() {
                        window.location.href = "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/list?retype=miyou"
                    }, 1000);
                }
            });

    var drop = $('#inner2').dropload({
                domUp : {
                    domClass   : 'dropload-up',
                    domRefresh : '<div class="dropload-refresh">↓下拉刷新</div>',
                    domUpdate  : '<div class="dropload-update">↑释放更新</div>',
                    domLoad    : '<div class="dropload-load"><span class="load-icon"></span>加载中...</div>'
                },
                loadUpFn : function(me) {
                    setTimeout(function() {
                        window.location.href = "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/list?retype=mymiyou"
                    }, 1000);
                }
            });

    var pageCount;
    var totalPages = $("#miyouMaxPage").val();
    var loadingbox = $('<div>').addClass("dropload-load");
    loadingbox.html('<span class="load-icon"></span>加载中...');
    var bool = true;

    var loader = new Loadmore($('#inner')[0], {
                loadMore: function(page, done) {
                    // load ajax
                    pageCount = $("#miyouCurPage").val();
                    // 模拟数据(联调时去掉延迟)
                    $('#inner').append(loadingbox);
                    setTimeout(function() {
                        loadingbox.remove();
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
                        var postcommentId = $("#postcommentId").val();

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
                                                    if (postcommentId == result.result.miyoulist[i].commentId) {
                                                        continue;
                                                    }
                                                    var stick = '';
                                                    if (result.result.miyoulist[i].oneUserSum == '-1') {
                                                        stick = "stick";
                                                    }
                                                    var content = ("<li class='item'><div href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.miyoulist[i].commentId + "' class='mi-tit-box " + stick + "'><h2>");
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
                                                            content += " <cite><img class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src='" + profileIcon + "'  ></cite><span>" + result.result.profiles[j].nick + "</span>"
                                                        }
                                                    }
                                                    content += "</h2> <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.miyoulist[i].commentId + "'> <p> " + result.result.miyoulist[i].description + "</p>";
                                                    if (result.result.miyoulist[i].pic != "") {
                                                        content += "<em><b class='ll-num'>" + result.result.miyoulist[i].longCommentSum + "</b><img class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src=" + result.result.miyoulist[i].pic + "></em>";
                                                    }
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
                                                                        content += " <cite><img  class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src='" + profileIcon + "'></cite>"
                                                                    }
                                                                }
                                                                content += "<span>" + result.result.hotReply[n].body.text + "</span></div>"
                                                            }
                                                        }
                                                    }
                                                    content += "</a>"
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
                                                        content += "赞</span></a></p></div>";
                                                    } else {
                                                        content += result.result.miyoulist[i].scoreCommentSum + "</span></a></p></div>";
                                                    }


                                                    $("#lists").append(content);
                                                }
                                                $("#miyouCurPage").val(result.result.page.curPage);
                                                $("#miyouMaxPage").val(result.result.page.maxPage);
                                                if (result.result.commentHistories != null) {
                                                    for (var i = 0; i < result.result.commentHistories.length; i++) {
                                                        var commentId = result.result.commentHistories[i].objectId;
                                                        $("[name='" + commentId + "']").each(function() {
                                                            $(this).addClass("active");

                                                        });
                                                    }
                                                }
                                                browseNum();
//                                                AutoImage(w, 0, '.mi-tit-box em img');
                                            }
                                            bool = true;
                                            $("img.lazy").lazyImg();
                                        }});
                        }


                    }, 1000);
                    if (pageCount == totalPages) {
                        setTimeout(function() {
                            $('#inner').append('<div class="loading_end">数据加载完毕</div>');
                        }, 1000);
                        loader.destroy();
                    }
                    done();
                },
                bottomBuffer:10//预加载临界值
            });


    var pageCount2;
    var totalPages2 = $("#mymiyouMaxPage").val();
    var mymiyouBool = true;

    var logindomain = $("#logindomain").val();
    if (logindomain != '' && logindomain != 'client') {

        var loader2 = new Loadmore($('#inner2')[0], {
                    loadMore: function(page, done) {
                        // load ajax

                        pageCount2 = $("#mymiyouCurPage").val();
                        // 模拟数据(联调时去掉延迟)
                        var uid = $("#uid").val();
                        var curNum = $("#mymiyouCurPage").val();
                        var maxNum = $("#mymiyouMaxPage").val();

                        if (curNum == '') {
                            return;
                        }
                        if (parseInt(maxNum) <= parseInt(curNum)) {
                            return;
                        }
                        if (mymiyouBool) {
                            mymiyouBool = false;
                            $('#inner2').append(loadingbox);
                            setTimeout(function() {
                                loadingbox.remove();
                                curNum = parseInt(curNum) + parseInt(1);
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
                                                            var content = "<li class='item'><div class='mi-tit-box'><h2 class='mi-tit-time'>今天" + hours + ":" + minutes + "</h2>";
                                                            content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.todayList[i].commentId + "'>";
                                                            content += " <p>" + result.result.todayList[i].description + "</p>";
                                                            if (result.result.todayList[i].pic != "") {
                                                                content += "<em><b class='ll-num'>" + result.result.todayList[i].longCommentSum + "</b><img class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src=" + result.result.todayList[i].pic + "></em>";
                                                            }
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
                                                                                content += " <cite><img class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src='" + profileIcon + "'></cite>";
                                                                            }
                                                                        }
                                                                        content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                                                    }
                                                                }
                                                            }
                                                            content += "</a>";
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
                                                                content += "赞</span></a></p></div>";
                                                            } else {
                                                                content += result.result.todayList[i].scoreCommentSum + "</span></a></p></div>";
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
                                                        var content = "<li class='item'><div class='mi-tit-box'><h2 class='mi-tit-time'>昨天" + hours + ":" + minutes + "</h2>";
                                                        content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.yesterdayList[i].commentId + "'>";
                                                        content += " <p>" + result.result.yesterdayList[i].description + "</p>";
                                                        if (result.result.yesterdayList[i].pic != "") {
                                                            content += "<em><b class='ll-num'>" + result.result.yesterdayList[i].longCommentSum + "</b><img class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src=" + result.result.yesterdayList[i].pic + "></em>";
                                                        }
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
                                                                            content += " <cite><img  class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src='" + profileIcon + "'></cite>";
                                                                        }
                                                                    }
                                                                    content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                                                }
                                                            }
                                                        }
                                                        content += "</a>";
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
                                                            content += "赞</span></a></p></div>";
                                                        } else {
                                                            content += result.result.yesterdayList[i].scoreCommentSum + "</span></a></p></div>";
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
                                                        var content = "<li class='item'><div  class='mi-tit-box'><h2 class='mi-tit-time'>前天" + hours + ":" + minutes + "</h2>";
                                                        content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.dayList[i].commentId + "'>";
                                                        content += " <p>" + result.result.dayList[i].description + "</p>";
                                                        if (result.result.dayList[i].pic != "") {
                                                            content += "<em><b class='ll-num'>" + result.result.dayList[i].longCommentSum + "</b><img  class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src=" + result.result.dayList[i].pic + "></em>";
                                                        }
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
                                                                            content += " <cite><img  class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src='" + profileIcon + "'></cite>";
                                                                        }
                                                                    }
                                                                    content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                                                }
                                                            }
                                                        }
                                                        content += "</a>";
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
                                                            content += "赞</span></a></p></div>";
                                                        } else {
                                                            content += result.result.dayList[i].scoreCommentSum + "</span></a></p></div>";
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
                                                        var content = "<li class='item'><div  class='mi-tit-box'><h2 class='mi-tit-time'>" + time.getFullYear() + "-" + (time.getMonth() + 1) + "-" + time.getDate() + " " + hours + ":" + minutes + "</h2>";
//                            var content = "<li><div href='#' class='mi-tit-box'><h2 class='mi-tit-time'>" + time + "</h2>";
                                                        content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + result.result.moredayList[i].commentId + "'>";
                                                        content += " <p>" + result.result.moredayList[i].description + "</p>";
                                                        if (result.result.moredayList[i].pic != "") {
                                                            content += "<em><b class='ll-num'>" + result.result.moredayList[i].longCommentSum + "</b><img  class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src=" + result.result.moredayList[i].pic + "></em>";
                                                        }
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
                                                                            content += " <cite><img  class='lazy' src='http://lib.joyme.com/static/theme/default/images/data-bg.gif' data-src='" + profileIcon + "'></cite>";
                                                                        }
                                                                    }
                                                                    content += "<span>" + result.result.hotReply[n].body.text + "</span></div>";
                                                                }
                                                            }
                                                        }
                                                        content += "</a>";
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
                                                            content += "赞</span></a></p></div>";
                                                        } else {
                                                            content += result.result.moredayList[i].scoreCommentSum + "</span></a></p></div>";
                                                        }
                                                        $("#thelist4").append(content);
                                                    }
                                                }

                                                $("#mymiyouCurPage").val(result.result.mymiyoupage.curPage);
                                                $("#mymiyouMaxPage").val(result.result.mymiyoupage.maxPage);
                                                if (parseInt(result.result.mymiyoupage.maxPage) <= parseInt(result.result.mymiyoupage.curPage)) {
                                                    setTimeout(function() {
                                                        $('#inner2').append('<div class="loading_end">数据加载完毕</div>');
                                                    }, 1000);
                                                    loader.destroy();
                                                }
                                                browseNum();
                                                if (result.result.commentHistories != null) {
                                                    for (var i = 0; i < result.result.commentHistories.length; i++) {
                                                        var commentId = result.result.commentHistories[i].objectId;
                                                        $("[name='" + commentId + "']").each(function() {
                                                            $(this).addClass("active");

                                                        });

                                                    }
                                                }
                                                mymiyouBool = true;
                                                $("img.lazy").lazyImg();
                                            }
                                        });
//                                AutoImage(w, 0, '.mi-tit-box em img');


                            }, 1000);
                        }
                        if (pageCount2 == totalPages2) {
                            setTimeout(function() {
                                $('#inner2').append('<div class="loading_end">数据加载完毕</div>');
                            }, 1000);
                            loader.destroy();
                        }
                        done();

                    },
                    bottomBuffer:10//预加载临界值
                });

    }
    ;


}, false);



