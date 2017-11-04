var pid = getCookie('jmuc_pid');
$(function () {
    $('.user-head-img').mouseenter(function (e) {
        if (commonFn.isPC()) {

            $(this).unbind("mousemove").mousemove(function (e) {
                var that = this;
                var tempProfileId = $(this).data('id');

                if (e.target == $(this).find('.vip').get(0) || e.target == $(this).find('.user-vip').get(0)) {
                    this.willOpen = false;
                    $('.user-info-box').remove();
                    this.hadOpen = false;
                    clearTimeout(this.timer1);

                    return;
                }
                if (this.hadOpen) {
                    clearTimeout(this.timer);
                    clearTimeout(this.timer2);
                    return;
                }
                if (this.willOpen) {
                    return
                }
                this.willOpen = true;
                this.timer1 = setTimeout(function () {
                    $.ajax({
                        url: "http://api." + joyconfig.DOMAIN + "/joyme/json/usercenter/userinfo",
                        type: "get",
                        data: {profileId: tempProfileId},
                        dataType: "jsonp",
                        jsonp: "callback",
                        jsonpCallback: "userinfocallback",
                        success: function (req) {
                            var resMsg = req[0];
                            if (resMsg.rs == '1') {
                                var rs = resMsg.result;
                                var nick = rs.nick;
                                var iconurl = rs.icon;
                                var prestige = rs.prestige;
                                var worship = rs.worship;
                                var fans = rs.fans;
                                var desc = rs.desc;
                                var follows = rs.follows;
                                var edits = rs.edits;
                                if (rs.followStatus == 0) {
                                    relation = '关注';
                                } else if (rs.followStatus == '1') {
                                    relation = '已关注';
                                } else if (rs.followStatus == '2') {
                                    relation = '互相关注';
                                }
                                that.hadOpen = true;
                                that.inImg = true;
                                that.index = parseInt(Math.random() * 1000000);

                                var w = parseInt($(that).width()) / 2;
                                var screen_w = document.documentElement.clientWidth || document.body.clientWidth;
                                var screen_h = document.documentElement.clientHeight || document.body.clientHeight;
                                var el_w = 307;
                                var el_h = 324;
                                var pageX = e.pageX;
                                var pageY = e.pageY;
                                var offsetL = $(that).offset().left;
                                var offsetT = $(that).offset().top;
                                // var elLeft = pageX - offsetL;
                                // var elRight = pageY - offsetT;
                                var elLeft = offsetL + w;
                                var elTop = offsetT + w;
                                var doc_scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
                                var doc_scrollTop = document.documentElement.scrollTop || document.body.scrollTop;

                                that.inOpen = false;
                                if ((doc_scrollLeft + screen_w) < (elLeft + el_w)) {
                                    // 超出屏幕右侧
                                    elLeft = elLeft - el_w;
                                }


                                var homeurl = "http://uc." + joyconfig.DOMAIN + "/usercenter/home";
                                var followurl = "http://uc." + joyconfig.DOMAIN + "/usercenter/follow/mylist";
                                var fansurl = "http://uc." + joyconfig.DOMAIN + "/usercenter/fans/mylist";
                                var editsurl = "http://wiki." + joyconfig.DOMAIN + "/home/index.php?title=%E7%89%B9%E6%AE%8A:%E7%9D%80%E8%BF%B7%E8%B4%A1%E7%8C%AE&userid=" + rs.uid;
                                if (pid != tempProfileId) {
                                    homeurl = "http://uc." + joyconfig.DOMAIN + "/usercenter/page?pid=" + tempProfileId;
                                    followurl = "http://uc." + joyconfig.DOMAIN + "/usercenter/follow/list?profileid=" + tempProfileId;
                                    fansurl = "http://uc." + joyconfig.DOMAIN + "/usercenter/fans/list?profileid=" + tempProfileId;
                                }


                                var html = "";

                                html = '<div class="user-info-box" data-num="' + that.index + '" style="position:absolute;left:' + elLeft + 'px;top:' + elTop + 'px;">';
                                if (rs.cardskin != "") {
                                    html += '<div class="ui-top ui-top' + rs.cardskin + '">';
                                } else {
                                    html += '<div class="ui-top">';
                                }

                                html += '<div class="ui-top-img">';
                                if (rs.headskin != "") {
                                    html += '<a href="' + homeurl + '" target="_blank"><div class="ui-top-decorate decorate' + rs.headskin + '"></div></a>';
                                } else {
                                    html += '<a href="' + homeurl + '" target="_blank"><div class="ui-top-decorate"></div></a>';
                                }

                                if (rs.vtype != "" && rs.vtype > 0) {
                                    html += '<div class="ui-vip"></div>';
                                }
                                html += '<a class="ui-top-img-a" href=""><img src="' + iconurl + '" alt=""></a>' +
                                    '</div>' +
                                    '<div class="ui-name-box">' +
                                    '<a class="ui-name" href="' + homeurl + '" target="_blank">' + nick + '</a>';
                                if (rs.sex != "" && rs.sex != null) {
                                    if (rs.sex == '1') {
                                        html += '<b class="user-sexes user-male"></b>';
                                    } else {
                                        html += '<b class="user-sexes user-female"></b>';
                                    }
                                }
                                html += '</div>' +
                                    '<p class="ui-top-title">简介：' + desc + '</p>' +
                                    '</div>' +
                                    '<div class="ui-bottom">' +
                                    '<ul class="focus-funs-box">' +
                                    '<li class="f-f-li">关注<span class="funs-value"><a href="' + followurl + '" target="_blank">' + follows + '</a></span></li>|' +
                                    '<li class="f-f-li">粉丝<span class="funs-value"><a href="' + fansurl + '" target="_blank">' + fans + '</a></span></li>|' +
                                    '<li class="f-f-li">编辑<span class="funs-value"><a href="' + editsurl + '" target="_blank">' + edits + '</a></span></li>' +
                                    '</ul>';

                                html += '<div class="btn-focus-letter-box">';
                                if (pid != tempProfileId) {
                                    if (rs.followStatus == '0') {
                                        html += '<div class="focus-btn letter-pre" id="follow' + rs.profileId + '" onclick=followProfile("' + rs.profileId + '"); >' + relation + '</div>';
                                    } else {
                                        html += '<div class="focus-btn focused" id="follow' + rs.profileId + '" onclick=unfollowProfile("' + rs.profileId + '") >' + relation + '</div>';
                                    }
                                    html += '<div class="focus-btn letter-pre" onclick="postMessge(' + rs.uid + ')">私信</div>';
                                }

                                html += '</div>' +
                                    '<div class="shegwag-box clearfix">' +
                                    '<span class="fr fot-colo be-mobai">被膜拜：' + worship + '</span>' +
                                    '<span class="fl fot-colo be-shegwag">声望：' + prestige + '</span>' +
                                    '</div>';
                                if (rs.userMWikiDTO.mWikiInfo != "") {
                                    html += '<div class="admin-box fot-colo">' +
                                        '管理wiki：' +
                                        '<ul class="admin-wiki clearfix">';
                                    for (var i = 0; i < rs.userMWikiDTO.mWikiInfo.length; i++) {
                                        var result = rs.userMWikiDTO.mWikiInfo[i];
                                        html += '<li class="wiki-li-box fl">' +
                                            '<a class="wiki-li-a" href="' + result.url + '" target="_blank"><img src="' + result.icon + '" alt=""></a>' +
                                            '</li>';
                                    }

                                    html += '</ul>' +
                                        '共<span class="fot-colo admin-sum-num">' + rs.userMWikiDTO.count + '</span>个' +
                                        '</div>';
                                }

                                html += '</div></div>';

                                $(html).appendTo($("body"));
                                that.openWindow = $('.user-info-box[data-num="' + that.index + '"]');
                                that.openWindow.outerhei = that.openWindow.outerHeight();
                                if ((doc_scrollTop + screen_h) < (elTop + that.openWindow.outerhei)) {
                                    // 超出屏幕底部
                                    elTop = elTop - that.openWindow.outerhei;
                                }
                                that.openWindow.css({
                                    top: elTop + "px"
                                }).show(100);
                                that.openWindow.unbind("mouseenter").mouseenter(function () {
                                    that.inOpen = true;
                                }).unbind("mouseleave").mouseleave(function () {
                                    var that2 = this;
                                    that.inOpen = false;
                                    clearTimeout(that.timer);
                                    that.timer2 = setTimeout(function () {
                                        if (that.inImg) {
                                            return;
                                        }
                                        $('.user-info-box').remove();
                                        that.hadOpen = false;
                                        that.willOpen = false;
                                    }, 100)
                                });

                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            that.willOpen = false;
                        }
                    })
                }, 900);
            })
        }

    }).mouseleave(function () {
        // 移出
        if (commonFn.isPC()) {
            $(this).unbind("mousemove");
            var that = this;
            this.inImg = false;
            this.willOpen = false;
            clearTimeout(that.timer1);
            clearTimeout(that.timer2);
            this.timer = setTimeout(function () {
                if (that.inOpen) {
                    // 进入浮层
                    return;
                }
                $('.user-info-box').remove();
                that.hadOpen = false;
                that.willOpen = false;
            }, 100);
        }
    })


})

function userinfocallback(data) {

}
//私信
function postMessge(uid) {

    //如果没有登录弹登录框
    if (pid == null) {
        loginDiv();
        return;
    }
    var url = "http://wiki." + joyconfig.DOMAIN + "/home/index.php?title=%E7%89%B9%E6%AE%8A:%E7%A7%81%E4%BF%A1&fid=" + uid;
    window.open(url);
}

//我管理的WIKI加载更多
var managerWikiLock = false;
var moreMangageWikiPage = 2;
function loadMoreManageWiki() {
    if (!managerWikiLock) {
        managerWikiLock = true;
        $.ajax({
            url: url,
            type: "post",
            async: false,
            data: {'action': 'userwikiinfo', 'userid': userid, 'wtype': 'mwiki', page: moreMangageWikiPage},
            dataType: "jsonp",
            jsonpCallback: "testwiki",
            success: function (data) {
                var userwikis = data.msg;
                var hasnext = data.hasnext;
                if (userwikis.length > 0) {
                    var innerHtml = '';
                    for (var i = 0; i < userwikis.length; i++) {
                        innerHtml += '<li class="col-md-4">' +
                            '<div class="manage-wiki">' +
                            '<a href="' + userwikis[i].site_url + '" target="_blank" class="mg-wiki-main fn-clear col-md-12">' +
                            '<b class="manager web-hide">' + userwikis[i].site_name + '</b>' +
                            '<div class="col-md-12"><cite>' +
                            '<img src="' + userwikis[i].site_icon + '" alt=""> <i> ' + userwikis[i].site_name + '</i></cite></div>' +
                            '<div class="manager-text col-md-12"> <font> ' + userwikis[i].site_name + '</font><span>页面总数量:' +
                            userwikis[i].page_count + '</span><span>编辑总次数:' +
                            userwikis[i].edit_count + '</span></div></a>' +
                            '<a href="javascript:;" class="web-hide add-edit"><span> 关注人数：' +
                            userwikis[i].follow_usercount + ' </span> <span> 编辑人数：' +
                            userwikis[i].edituser_count + ' </span><span> 昨日编辑：' +
                            userwikis[i].yes_editcount + ' </span></a>' +
                            '<i class="caret  count-icon web-hide"></i></div></li>';
                    }
                    $('#manageUl').append(innerHtml);
                    moreMangageWikiPage++;
                    if (hasnext == '0') {
                        $('#manageWikiMore').css("display", "none");
                    }

                    managerWikiLock = false;
                } else {
                    $('#manageWikiMore').css("display", "none");
                }
            }
        });
    }
}

//我贡献的WIKI加载 更多
var moreCntributeWikiPage = 2;
var moreCntributeWikiLock = false;
function loadMoreoCntributeWiki() {
    if (!moreCntributeWikiLock) {
        moreCntributeWikiLock = true;
        $.ajax({
            url: url,
            type: "post",
            async: false,
            data: {'action': 'userwikiinfo', 'userid': userid, 'wtype': 'cwiki', page: moreCntributeWikiPage},
            dataType: "jsonp",
            jsonpCallback: "testwiki",
            success: function (data) {
                var userwikis = data.msg
                var hasnext = data.hasnext;
                if (userwikis.length > 0) {
                    var innerHtml = '';
                    for (var i = 0; i < userwikis.length; i++) {
                        innerHtml += '<li class="col-md-4">' +
                            '<a href="' + userwikis[i].site_url + '" target="_blank" >' +
                            '<cite>' +
                            '<img src="' + userwikis[i].site_icon + '" alt="img"> </cite>' +
                            '<span><font>' + userwikis[i].site_name +
                            '</font> <b>贡献总数:' +
                            userwikis[i].offer_count + '</b>' +
                            '<b> 昨日编辑：' +
                            userwikis[i].yes_editcount + ' </b></span></a>' +
                            '</li>';
                    }
                    $('#contributeUl').append(innerHtml);
                    moreCntributeWikiPage++;
                    if (hasnext == '0') {
                        $('#contributewikiMore').css("display", "none");
                    }
                    moreCntributeWikiLock = false;
                } else {
                    $('#contributewikiMore').css("display", "none");
                }
            }
        });
    }
}

//我关注的WIKI加载更多
var moreFollowWikiPage = 2;
var moreFollowWikiLock = false;
function loadMoreFollowWiki() {
    if (!moreFollowWikiLock) {
        moreFollowWikiLock = true;
        $.ajax({
            url: url,
            type: "post",
            async: false,
            data: {'action': 'userwikiinfo', 'userid': userid, 'wtype': 'fwiki', page: moreFollowWikiPage},
            dataType: "jsonp",
            jsonpCallback: "testwiki",
            success: function (data) {
                var userwikis = data.msg
                var hasnext = data.hasnext;
                if (userwikis.length > 0) {
                    var innerHtml = '';
                    for (var i = 0; i < userwikis.length; i++) {
                        innerHtml += '<li class="col-md-4">' +
                            '<a href="' + userwikis[i].site_url + '" target="_blank" >' +
                            '<cite>' +
                            '<img src="' + userwikis[i].site_icon + '" alt="img"> </cite>' +
                            '<span><font>' + userwikis[i].site_name +
                            '</font> <b>页面总数:' +
                            userwikis[i].page_count + '</b>' +
                            '<b> 昨日编辑：' +
                            userwikis[i].yes_editcount + ' </b></span></a>' +
                            '</li>';
                    }
                    $('#followUl').append(innerHtml);
                    moreFollowWikiPage++;
                    if (hasnext == '0') {
                        $('#followwikiMore').css("display", "none");
                    }
                    moreFollowWikiLock = false;
                } else {
                    $('#followwikiMore').css("display", "none");
                }
            }
        });
    }

}

//关注
var followLock = false;
function followProfile(profileIdObj) {
    if (!followLock) {
        followLock = true;
        //如果没有登录弹登录框
        if (pid == null) {
            loginDiv();
            followLock = false;
            return;
        }
        var srcprofileid = getCookie('jmuc_pid');
        if (srcprofileid != null && srcprofileid != "" && profileIdObj != null && profileIdObj != "") {
            $.ajax({
                url: "/api/usercenter/relation/follow",
                type: "post",
                async: false,
                data: {destprofileid: profileIdObj},
                dataType: "jsonp",
                jsonpCallback: "callback",
                success: function (data) {
                    if (data != "") {
                        var sendResult = data[0];
                        var rs = sendResult.rs;
                        if (rs == "1") {
                            $("#follow" + profileIdObj).removeClass("letter-pre");
                            $("#follow" + profileIdObj).addClass("focused")
                            $("#follow" + profileIdObj).attr("onclick", "unfollowProfile('" + profileIdObj + "')");
                            if (sendResult.result == 'e') {
                                $("#follow" + profileIdObj).text("互相关注");
                                if ($("#ufollow" + profileIdObj).length > 0) {
                                    $("#ufollow" + profileIdObj).text("互相关注");
                                }
                            } else if (sendResult.result == 't') {
                                $("#follow" + profileIdObj).text("已关注");
                                if ($("#ufollow" + profileIdObj).length > 0) {
                                    $("#ufollow" + profileIdObj).text("已关注");
                                }
                            }
                            if ($("#ufollow" + profileIdObj).length > 0) {
                                $("#ufollow" + profileIdObj).removeClass("private");
                                $("#ufollow" + profileIdObj).addClass("focused");
                                $("#ufollow" + profileIdObj).attr("onclick", "");
                            }
                        } else if (rs == "-1001") {
                            mw.ugcwikiutil.autoCloseDialog("参数不能为空");
                        } else if (rs == "-1000") {
                            mw.ugcwikiutil.autoCloseDialog("系统错误");
                        } else if (rs == "-10128") {
                            mw.ugcwikiutil.autoCloseDialog("该用户未开启关注权限");
                        }
                        followLock = false;
                    }
                }, error: function () {
                    followLock = false;
                }
            });
        }
    }
}

var unfollowLock = false;
function unfollowProfile(profileIdObj) {
    if (!unfollowLock) {
        unfollowLock = true;
        //如果没有登录弹登录框
        if (pid == null) {
            loginDiv();
            unfollowLock = false;
            return;
        }
        var srcprofileid = getCookie('jmuc_pid');
        if (srcprofileid != null && srcprofileid != "" && profileIdObj != null && profileIdObj != "") {
            $.ajax({
                url: "/api/usercenter/relation/unfollow",
                type: "post",
                async: false,
                data: {destprofileid: profileIdObj},
                dataType: "jsonp",
                jsonpCallback: "callback",
                success: function (data) {
                    if (data != "") {
                        var sendResult = data[0];
                        var rs = sendResult.rs;
                        if (rs == "1") {
                            $("#follow" + profileIdObj).removeClass("focused");
                            $("#follow" + profileIdObj).addClass("letter-pre");
                            $("#follow" + profileIdObj).attr("onclick", "followProfile('" + profileIdObj + "')");
                            $("#follow" + profileIdObj).text("关注");
                            if ($("#ufollow" + profileIdObj).length > 0) {
                                $("#ufollow" + profileIdObj).removeClass("focused");
                                $("#ufollow" + profileIdObj).addClass("private");
                                $("#ufollow" + profileIdObj).text("关注");
                                $("#ufollow" + profileIdObj).attr("onclick", "followProfile('" + profileIdObj + "')");
                            }
                        } else if (rs == "-1001") {
                            alert("参数不能为空");
                        } else if (rs == "-1000") {
                            alert("系统错误");
                        }
                        unfollowLock = false;
                    }
                }, error: function () {
                    unfollowLock = false;
                }
            });
        }
    }

}

var myTimelineLock = false;
var myTimelinePage = 2;
function myTimeLine(pid) {
    var name = "我";
    if (pid != '') {
        name = "他";
    }
    if (!myTimelineLock) {
        myTimelineLock = true;
        $.ajax({
            url: '/api/usertimeline/list',
            type: "post",
            data: {page: myTimelinePage, pid: pid},
            dataType: "json",
            success: function (data) {
                if (data.rs == 1) {
                    var timeline = data.result.rows;
                    if (timeline.length > 0) {
                        var innerHtml = "";
                        for (var i = 0; i < timeline.length; i++) {
                            var line = timeline[i];
                            var date = new Date(line.createTime.time);
                            innerHtml += "<li><p><i>" + name + "</i> ";
                            if (line.actionType.code == 'focus_user') {
                                if (line.extendBody.indexOf("关注了") > -1) {
                                    innerHtml += line.extendBody;
                                } else {
                                    var userinfoMap = data.userinfoMap[line.extendBody];
                                    innerHtml += line.actionType.value + " <a href='http://uc." + joyconfig.DOMAIN + "/usercenter/page?pid=" + line.extendBody + "' target='_blank'>" + userinfoMap.nick + "</a>"
                                }
                            } else {
                                innerHtml += line.extendBody;
                            }
                            innerHtml += "<b class='time-stamp'>" + date.format('yyyy-MM-dd hh:mm:ss') + "</b></p></li>";
                        }
                        $('#myTimeLine').before(innerHtml);
                        myTimelinePage++;
                        myTimelineLock = false;
                        var curPage = parseInt(data.result.page.curPage);
                        var maxPage = parseInt(data.result.page.maxPage);
                        if (curPage >= maxPage) {
                            $('#myTimeLine').css("display", "none");
                        }
                    } else {
                        $('#myTimeLine').css("display", "none");
                    }
                }
            }, error: function () {
                var myTimelineLock = false;
                alert("网络异常");
            }
        });
    }
}

var friendlineLock = false;
var friendTimelinePage = 2;
function friendTimeLine() {
    if (!friendlineLock) {
        friendlineLock = true;
        $.ajax({
            url: '/api/usertimeline/friendlist',
            type: "post",
            data: {page: friendTimelinePage},
            dataType: "json",
            success: function (data) {
                if (data.rs == 1) {
                    var timeline = data.result.rows;
                    if (timeline.length > 0) {
                        var innerHtml = '';
                        for (var i = 0; i < timeline.length; i++) {
                            var line = timeline[i];
                            var date = new Date(line.createTime.time);
                            var userinfoMap = data.userinfoMap[line.destProfileid];
                            innerHtml += '<li class="fn-clear"><div class="user-img"><cite> <img src="' + userinfoMap.icon + '" alt="img" />';
                            if (userinfoMap.headskin != "") {
                                innerHtml += '<span class="focus-def focus-dec-0' + userinfoMap.headskin + '"></span> ';
                            }
                            if (userinfoMap.vtype != null && userinfoMap.vtype > 0) {
                                innerHtml += '<span class="vip icon-vip" title="' + userinfoMap.vdest + '"></span>';
                            }
                            innerHtml += '</cite></div><div class="col-md-12 user-text"><p><i class="user-name"><a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + line.destProfileid + '" target="_blank">' + userinfoMap.nick + '</a>' +
                                ' </i>';
                            if (line.actionType.code == 'focus_user') {
                                if (line.extendBody.indexOf("关注了") > -1) {
                                    innerHtml += line.extendBody;
                                } else {
                                    var userinfoMap = data.userinfoMap[line.extendBody];
                                    innerHtml += line.actionType.value + " <a href='http://uc." + joyconfig.DOMAIN + "/usercenter/page?pid=" + line.extendBody + "' target='_blank'>" + userinfoMap.nick + "</a>"
                                }
                            } else {
                                innerHtml += line.extendBody;
                            }
                            innerHtml += '<b class="time-stamp">' + date.format("yyyy-MM-dd hh:mm:ss") + '</b></p></div>';
                        }
                        $('#friendTimeLine').before(innerHtml);
                        friendTimelinePage++;
                        friendlineLock = false;
                        var curPage = parseInt(data.result.page.curPage);
                        var maxPage = parseInt(data.result.page.maxPage);
                        if (curPage >= maxPage) {
                            $('#friendTimeLine').css("display", "none");
                        }
                    } else {
                        $('#friendTimeLine').css("display", "none");
                    }
                }
            }, error: function () {
                friendlineLock = false;
                alert("网络异常");
            }
        });
    }
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

Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(),    //day
        "h+": this.getHours(),   //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
            RegExp.$1.length == 1 ? o[k] :
                ("00" + o[k]).substr(("" + o[k]).length));
    return format;
}