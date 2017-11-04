$(function () {
    //积分兑换宝箱
    $(".duihuan").click(function () {
        $(".joyme-dialog-popup-mc").addClass("on");
    });
    var parent = $('.new-window-content');
    var step = 500; // 宝箱一个消耗的积分
    var surnum = parent.find('.sur-num').text() * 1 // 剩余积分
    $('.fuhao').unbind().click(function () {
        var times = parent.find('.fuhao-times');
        var value = times.val();
        var showPoint = value * step;
        if ($(this).hasClass('fuhao-plus')) {
            if ($(this).hasClass("noclick")) return;
            if (showPoint < surnum) {
                // 积分充足
                var finalNum = surnum - showPoint;
                if (finalNum > step) {
                    $(this).siblings('.fuhao-unplus').removeClass('noclick');
                    value++;
                    showPoint = value * step;
                    times.val(value);
                    parent.find('.showPoint').text(showPoint);
                }
                else {
                    // 积分不足
                    $(this).addClass('noclick');
                }
            } else {
                // 积分不足
                $(this).addClass('noclick');
            }
        } else {
            if ($(this).hasClass("noclick")) return;
            if (value >= 2) {
                $(this).siblings('.fuhao-plus').removeClass('noclick');
                value--;
                times.val(value);
                showPoint = value * step;
                parent.find('.showPoint').text(showPoint);
            } else {
                // 膜拜次数不小于1次
                $(this).addClass('noclick');

            }
        }

    });
    $(".joyme-dialog-cancel").click(function () {
        $(".joyme-dialog-popup-mc").removeClass("on");
    });
    var exchangeGiftBoxLock = false;

    $(".joyme-dialog-confirm").click(function () {
        var point = $("input[name='point']").val() * 1;
        if (step > point) {
            $(".joyme-dialog-popup-mc").removeClass("on");
            $(".box-pop-02-wrap").find(".wain-dec").text("您的积分不足，请赚取积分再来吧～ ");
            $(".box-pop-02-wrap").addClass("on");
            return;
        }

        if (exchangeGiftBoxLock) {
            return;
        }
        exchangeGiftBoxLock = true;
        var num = $("input[name='times']").val();
        $.ajax({
            type: "POST",
            url: "/joyme/api/point/exchangegiftbox",
            data: {num: num},
            dataType: "json",
            success: function (req) {
                if (req.rs == 1) {
                    var inputNum = $("input[name='giftnum']").val() * 1;
                    $("#giftBoxNum").text(inputNum + req.result);
                    $("input[name='giftnum']").val(inputNum + req.result);
                    $("input[name='point']").val(point - ( req.result * step));
                    $(".sur-num").text(point - ( req.result * step));
                    $(".joyme-dialog-popup-mc").removeClass("on");
                    $(".box-pop-02-wrap").find(".wain-dec").text("兑换成功 ");
                    $(".box-pop-02-wrap").addClass("on");
                    $("input[name='times']").val(1);
                    $(".showPoint").text(step);
                } else if (req.rs == -2000) {
                    $(".joyme-dialog-popup-mc").removeClass("on");
                    $(".box-pop-02-wrap").find(".wain-dec").text("您的积分不足，请赚取积分再来吧～ ");
                    $(".box-pop-02-wrap").addClass("on");
                } else {
                    $(".joyme-dialog-popup-mc").removeClass("on");
                    $(".box-pop-02-wrap").find(".wain-dec").text("兑换失败，请稍后重试。");
                    $(".box-pop-02-wrap").addClass("on");
                }
                exchangeGiftBoxLock = false;

            }, error: function (req) {
                $(".joyme-dialog-popup-mc").removeClass("on");
                $(".box-pop-02-wrap").find(".wain-dec").text("兑换超时，请检查您的网络状态");
                $(".box-pop-02-wrap").addClass("on");
                exchangeGiftBoxLock = false;
            }
        });
    });
    // 装饰Tab 切换
    $(".choose-con .tab li").click(function () {

        $(this).addClass("on").siblings().removeClass("on");
        var liIndex = $(this).index();
        $(".decorate-con .decorate1").eq(liIndex).addClass("on").siblings().removeClass("on");
    });
    //兑换宝箱弹出层 延迟2秒或者点击自动关闭
    var exchangelock = false;
    $(".get-box .bbx-box .red-box").click(function () {
        var giftnum = $("input[name='giftnum']").val();
        console.log(giftnum);
        if (giftnum == 0 || giftnum == '') {
            $(".joyme-dialog-popup-mc").removeClass("on");
            $(".box-pop-02-wrap").find(".wain-dec").text("您还没有宝箱，请先去兑换吧！");
            $(".box-pop-02-wrap").addClass("on")
            return;
        }
        if (exchangelock) {
            return;
        }
        exchangelock = true;

        $.ajax({
            type: "POST",
            url: "/joyme/api/point/opengiftbox",
            dataType: "json",
            success: function (req) {
                if (req.rs == 1) {
                    var id = req.result;
                    $("#dis" + id).css("display", "none");
                    $("#none" + id).css("display", "block");
                    var inputNum = $("input[name='giftnum']").val() * 1;
                    $("#giftBoxNum").text(inputNum - 1);
                    $("input[name='giftnum']").val(inputNum - 1);
                    var imgurl = $("#none" + id).find(".dec-con").find("img").attr("src");
                    $(".get-box .box-pop .pop-con").find("img").attr("src", imgurl);
                    $(".get-box .box-pop").addClass("on");

                    var bool = setTimeout(function () {
                        $(".get-box .box-pop").removeClass("on");
                        exchangelock = false;
                    }, 2000);

                } else if (req.rs == -2002) {
                    $(".joyme-dialog-popup-mc").removeClass("on");
                    $(".box-pop-02-wrap").find(".wain-dec").text("您还没有宝箱，请先去兑换吧！");
                    $(".box-pop-02-wrap").addClass("on")
                    exchangelock = false;
                } else if (req.rs == -2004) {
                    var result = req.result;
                    var inputNum = $("input[name='giftnum']").val() * 1;
                    $("#giftBoxNum").text(inputNum - 1);
                    $("input[name='giftnum']").val(inputNum - 1);
                    var imgurl = $("#skin" + result.giftLotteryId).val();
                    $(".get-box .box-pop .pop-con").find("img").attr("src", imgurl);
                    $(".get-box .box-pop").addClass("on");

                    var bool = setTimeout(function () {
                        $(".get-box .box-pop").removeClass("on");
                        $(".joyme-dialog-popup-mc").removeClass("on");
                        $(".box-pop-02-wrap").find(".wain-dec").text("道具您已拥有，本次抽取将会折算为" + result.returnPoint + "分返还给您！");
                        $(".box-pop-02-wrap").addClass("on");
                        exchangelock = false;
                    }, 1000);
                } else {
                    $(".joyme-dialog-popup-mc").removeClass("on");
                    $(".box-pop-02-wrap").find(".wain-dec").text("兑换失败，请稍后重试。");
                    $(".box-pop-02-wrap").addClass("on");
                    exchangelock = false;
                }

            }, error: function (req) {
                $(".joyme-dialog-popup-mc").removeClass("on");
                $(".box-pop-02-wrap").find(".wain-dec").text("兑换超时，请检查您的网络状态");
                $(".box-pop-02-wrap").addClass("on");
                exchangelock = false;
            }
        });


    });
    //宝箱关闭层
    $(".get-box .box-pop").click(function () {
        $(this).removeClass("on");
    });
    //确认积分关闭弹层
    $(".box-pop-02 p .make-sure").click(function () {
        $(".box-pop-02-wrap").removeClass("on");
    });
    //可选装饰点击装饰后变成选中状态，不可选的则不
    $(".decorate-con .dec-list li .dec-con").click(function () {
        if ($(this).hasClass("owner")) {
            if (!$(this).hasClass("choose")) {
                var logid = $(this).find("img").attr("data-id");
                $(this).addClass('choose').parent().siblings().find(".dec-con").removeClass('choose');
                $.ajax({
                    type: "POST",
                    url: "/joyme/api/point/choosegift",
                    data: {logid: logid, flag: 'true'},
                    dataType: "json",
                    success: function (req) {

                    }, error: function (req) {

                    }
                });
            } else {
                var logid = $(this).find("img").attr("data-id");
                $.ajax({
                    type: "POST",
                    url: "/joyme/api/point/choosegift",
                    data: {logid: logid, flag: 'false'},
                    dataType: "json",
                    success: function (req) {

                    }, error: function (req) {

                    }
                });
                $(this).removeClass('choose');
            }
        }
    });

    $("#notice_delete").click(function () {
        if (confirm("删除内容不可恢复,确认要清空所有吗？")) {
            var noticeType = $("#noticetype").val();
            if (noticeType == '') {
                return;
            }
            window.location.href = "http://uc." + joyconfig.DOMAIN + "/usercenter/notice/deletenotice?ntype=" + noticeType;
        }
    });

    if (!commonFn.isPC()) {
        if (commonFn.isIpad()) {
            $(window).on('orientationchange', function () {
                if (commonFn.iosHeng()) {
                    // ios 横屏
                    $('.zan-list-box').dropload({
                        scrollArea: window,
                        loadDownFn: function (me) {
                            dropLoad();
                            // 横屏上拉不执行
                        }
                    });
                } else if (commonFn.iosShu()) {
                    dropLoad();
                }
            })
            if (commonFn.iosHeng()) {
                dropLoad();
                // ios 横屏不执行；
                return;
            }

        }
        dropLoad();
    }
    function dropLoad(argument) {
        // dropload
        $('.zan-list-box ').dropload({
            scrollArea: window,
            loadDownFn: function (me) {
                var curpage = $("#curpage").val();
                var maxpage = $("#maxpage").val();
                var type = $("#noticetype").val();

                if (parseInt(curpage) >= parseInt(maxpage)) {
                    me.lock();
                    me.noData();
                    me.resetload();
                    return;
                }
                var desttype = "";
                curpage = parseInt(curpage) + 1;
                $.ajax({
                    type: 'post',
                    url: '/joyme/api/notice/list',
                    data: {p: curpage, ntype: type},
                    dataType: 'json',
                    success: function (data) {
                        if (data.rs == 1) {
                            var result = data.result;
                            var list = result.list;
                            var profileMap = result.profileMap;

                            var html = '';
                            for (var i = 0; i < list.length; i++) {
                                var notice = list[i];
                                if (typeof(notice.body.wikiNoticeDestType) != "undefined") {
                                    desttype = notice.body.wikiNoticeDestType.code;
                                }
                                var switchType = getSwitchType(type, desttype);//通过type获得不同列表的提示语
                                if (switchType == "otherprofile") {
                                    switchType = '回复了<a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + profileMap[notice.body.otherProfileId].profileId + '" target="_blank">' + profileMap[notice.body.otherProfileId].nick + '</a>：';
                                } else if (switchType == "follow") {
                                    switchType = '关注了你，去他的<a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + profileMap[notice.body.destProfileId].profileId + '"  target="_blank">个人中心</a>看看';
                                }
                                /**
                                 * 系统消息加载
                                 */
                                if (type == "sys") {
                                    if (switchType == "prestige") {
                                        html += '<li>' +
                                            '<div class="list-item-l">' +
                                            '<cite>' +
                                            '<img src="http://static.' + joyconfig.DOMAIN + '/pc/userEncourageSys/images/sys-img.jpg">' +
                                            '</cite>' +
                                            '</div>' +
                                            '<div class="list-item-r"><div class="item-r-name fn-clear"><span class="fn-left"></span>' +
                                            '<b class="time-stamp fn-right">' + notice.timeString +
                                            '</b></div>' +
                                            '<div class="item-r-text">' +
                                            '<div class="shengwang-box"> <div class="shengwang-top"> <div class="shengwang-middle"> <div class="shengwang-inner">' +
                                            '<p class="sw-inner-num">  ' + notice.body.prestige + ' </p> <span class="sw-inner-update">  更新日期：' + notice.noticeTimeString + '  </span><div class="sw-liner"> </div> ' +
                                            '<p class="sw-title">着迷声望</p></div></div></div>' +
                                            '<p class="sw-tellothers">您的声望已经超越<span>' + notice.body.desc + '</span>的用户</p></div></div></div></li>';
                                    } else {
                                        html += '<li><div class="list-item-l"><cite><a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + profileMap[notice.body.destProfileId].profileId + '"  target="_blank"><img src="' + profileMap[notice.body.destProfileId].icon + '"></a>';
                                        if (typeof(profileMap[notice.body.destProfileId]) != "undefined") {
                                            if (typeof(profileMap[notice.body.destProfileId].headskin) != "undefined") {
                                                html += '<span class="dianzan-def focus-dec-0' + profileMap[notice.body.destProfileId].headskin + '"></span>';
                                            }
                                        }

                                        html += '</cite></div> <div class="list-item-r situatio-one"><div class="item-r-name ">' +
                                            '<a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + profileMap[notice.body.destProfileId].profileId + '"  target="_blank">' + profileMap[notice.body.destProfileId].nick + '</a></span> <b class="time-stamp fn-right">' + notice.timeString + '</b></div>' +
                                            '<div class="item-r-text"> ' + profileMap[notice.body.destProfileId].nick + '对你在';
                                        if (notice.body.pageurl != "") {
                                            html += '【' + notice.body.pageurl + '】';
                                        }
                                        if (notice.body.contenturl != "") {
                                            html += '【' + notice.body.contenturl + '】';
                                        }
                                        if (notice.body.wikiNoticeDestType.code == 6) {
                                            html += '中的贡献表示感谢，声望+' + notice.body.desc;
                                        } else {
                                            html += '中的贡献表示膜拜，声望+' + notice.body.desc;
                                        }
                                    }

                                } else { //其他消息加载
                                    html += '<li><div class="list-item-l"><cite><a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + profileMap[notice.body.destProfileId].profileId + '"  target="_blank"><img src="' + profileMap[notice.body.destProfileId].icon + '"></a>';
                                    if (typeof(profileMap[notice.body.destProfileId]) != "undefined") {
                                        if (typeof(profileMap[notice.body.destProfileId].headskin) != "undefined") {
                                            html += '<span class="dianzan-def focus-dec-0' + profileMap[notice.body.destProfileId].headskin + '"></span>';
                                        }
                                    }

                                    html += '</cite></div> <div class="list-item-r situatio-one"><div class="item-r-name ">' +
                                        '<a href="http://uc.' + joyconfig.DOMAIN + '/usercenter/page?pid=' + profileMap[notice.body.destProfileId].profileId + '"  target="_blank">' + profileMap[notice.body.destProfileId].nick + '</a>';
                                    if (type != "agree" && type != "follow") {
                                        html += '在“' + notice.body.pageurl + '”';
                                    }
                                    html += switchType + '</div>';

                                    if (type != 'follow') {
                                        html += '<div class="item-r-text">' + notice.body.contenturl + '</div>' +
                                            '<div class="item-r-other fn-clear">' +
                                            '<b class="from-wiki">出自：' + notice.body.desc + '</b>' +
                                            '<b class="time-stamp">' + notice.timeString + '</b></div></div></li>';
                                    } else {
                                        html += '<div class="item-r-other fn-clear"><b class="time-stamp">' + notice.timeString + '</b></div></div></li>';
                                    }
                                }
                            }
                            $(".list-item").append(html);

                            $("#curpage").val(result.page.curPage);

                            me.resetload();
                        }
                    },
                    error: function (xhr, type) {
                        alert('Ajax error!');
                        // 即使加载出错，也得重置
                        me.resetload();
                    }
                });
            }
        });
    }
});


function getSwitchType(type, desttype) {
    switch (type) {
        case "at":
            return "@了你";
            break;
        case "reply":
            if (desttype == 1) {
                return "发表了评论：";
            } else if (desttype == 2) {
                return "回复了我：";
            } else if (desttype == 3) {
                return "otherprofile";
            }
            break;
        case "agree":
            if (desttype == 4) {
                return "赞了我的内容：";
            } else if (desttype == 5) {
                return "赞了我的评论：";
            }
            break;
        case "follow":
            return "follow";
            break;
        case "sys":
            if (desttype == 6) {
                return "thanks";
            } else if (desttype == 7) {
                return "worship";
            } else if (desttype == 8) {
                return "prestige";
            }
            break;
    }
}

