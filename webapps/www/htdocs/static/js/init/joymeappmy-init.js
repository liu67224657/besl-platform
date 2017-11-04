define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var alertOption = {
        tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    $(document).ready(function () {

        var appkey = $("[name='appkey']").val();
        var profileId = $("[name='profileId']").val();
        var aid = $("[name='aid']").val();
        var gid = $("[name='gid']").val();
        var type = $("[name='type']").val();
        var uno = $("[name='uno']").val();
        var clientid = $("[name='clientid']").val();
        var platform = $("[name='platform']").val();
        var wallname = $("[name='wallname']").val();
        var loadingbool = false;
        var bool = false;
        $("#reserve-btn").click(function () {
            var logindomain = $("[name='logindomain']").val();
            if (logindomain == '' || logindomain == 'client') {
                $("#reserveSuccess").addClass("close");
                $("#reserveSuccess").text("您还没有登录，请先登录");
                var t = setInterval(function () {
                    $("#reserveSuccess").removeClass("close");
                    clearTimeout(t);
                }, 3000);
                showLoginWindow();
            } else {
                $("#reservediv").addClass("close");
                $(".mark-box").css("display", "block");
            }

        });

        $("#syhb-exchange-btn").click(function() {
            var logindomain = $("[name='logindomain']").val();
            if (logindomain == '' || logindomain == 'client') {
                if (!bool) {
                    bool = true;
                    cancelReserve();
                    $("#reserveSuccess").css("width", "55%");
                    $("#reserveSuccess").css("line-height", "normal");
                    $("#reserveSuccess").html("<div style='padding-top:20px;'>请您登录后再兑换商品<br/>3秒后自动跳转</div>");
                    $("#reserveSuccess").addClass("close");
                    var i = 3;
                    var t = setInterval(function () {
                        i--;
                        $("#reserveSuccess").html("<div style='padding-top:20px;'>请您登录后再兑换商品<br/>" + i + "秒后自动跳转</div>");
                        if (i == 0) {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                            bool = false;
                            showLoginWindow();
                        }

                    }, 1000);
                }
            } else {
                var sn = $("[name='sn']").val();
                var dtopoint = $("[name='dtopoint']").val();
                var userpoint = $("[name='userpoint']").val();
                var allowExchangeStatus = $("[name='allowExchangeStatus']").val();
                if (allowExchangeStatus == '-1') {
                    $("#reserveSuccess").text("您已经兑换过该商品～");
                    $("#reserveSuccess").addClass("close");

                    var t = setInterval(function () {
                        $("#reserveSuccess").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                } else if (allowExchangeStatus == '-2') {
                    $("#reserveSuccess").text("今天已经兑换过该商品～");
                    $("#reserveSuccess").addClass("close");

                    var t = setInterval(function () {
                        $("#reserveSuccess").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                } else if (allowExchangeStatus == '-3') {
                    $("#reserveSuccess").text("前不久已经兑换过该商品，过一会再来看看吧～");
                    $("#reserveSuccess").addClass("close");

                    var t = setInterval(function () {
                        $("#reserveSuccess").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                }
                if (parseInt(sn) < parseInt(1)) {
                    $("#reserveSuccess").text("该商品被兑换光了哦～");
                    $("#reserveSuccess").addClass("close");

                    var t = setInterval(function () {
                        $("#reserveSuccess").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                }
                if (parseInt(userpoint) < parseInt(dtopoint)) {
                    $("#gopointwall").addClass("close");
                    $(".mark-box").css("display", "block");
                    return;
                }
                $("#cofirmshopdiv").addClass("close");
                $(".mark-box").css("display", "block");
            }
        });
        $("#exchange-btn").click(function () {
            $("#cofirmshopdiv").addClass("close");
            $(".mark-box").css("display", "block");
        });
        $("#submitAddress").click(function () {
            var name = $("[name='name']").val();
            var phone = $("[name='phone']").val();
            var zipcode = $("[name='zipcode']").val();
            var province = $("[name='province']").val();
            var city = $("[name='city']").val();
            var town = $("[name='town']").val();
            var address = $("[name='address']").val();
            if (town == '区县') {
                town = '';
            }
            if (name == "" || phone == '' || zipcode == '' || province == '省份' || city == '地级市' || address == '') {
                $("#reserveSuccess").text("请填写完整信息");
                $("#reserveSuccess").addClass("close");
                var t = setInterval(function () {
                    $("#reserveSuccess").removeClass("close");
                    clearTimeout(t);
                }, 3000);
                return;
            } else {
//                address = province + city + town + address;
                $.ajax({
                            type: "POST",
                            url: "/joymeapp/my/modifyaddress",
//                            async:false,
                            data: {
                                name: name,
                                phone: phone,
                                zipcode: zipcode,
                                address: address,
                                profileid: profileId,
                                province: province,
                                city: city,
                                county: town
                            },
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                if (result.rs == 0) {
                                    alert("添加失败");
                                    return;
                                } else {
                                    $("#wrapper").css("display", "block");
                                    $(".new-address").css("display", "none");
                                    $(".mark-box").css("display", "block");
//                                    $("#reserveSuccess").text("地址已添加成功");
//                                    $("#reserveSuccess").addClass("close");
//                                    var t = setInterval(function() {
//                                        $("#reserveSuccess").removeClass("close");
//                                        clearTimeout(t);
//                                    }, 3000);
                                    $("[name='name']").val("");
                                    $("[name='phone']").val("");
                                    $("[name='zipcode']").val("");
                                    $("[name='address']").val("");
                                    $("#s1").get(0).selectedIndex = 0;
                                    $("#s2").get(0).selectedIndex = 0;
                                    $("#s3").get(0).selectedIndex = 0;
                                    exchangeOfGoods();
                                }
                            }
                        });
            }
        });

        $("#reserveGift").click(function () {
            $.ajax({
                        type: "POST",
                        url: "/joymeapp/my/reserve",
//                        async:false,
                        data: {appkey: appkey, profileid: profileId, aid: aid, gid: gid},
                        success: function (req) {
                            var result = eval('(' + req + ')');

                            if (result.rs == 1) {
                                $("#reserveSuccess").text("预约成功");
                                $("#reserve-btn").text("已预约");
                                $("#reserve-btn").unbind("click");
                            } else {
                                if (result.msg == 'reserve.is.exsits') {
                                    $("#reserveSuccess").text("您已经预约过该礼包");
                                } else {
                                    $("#reserveSuccess").text("预约失败");
                                }
                            }
                            $("#reserveSuccess").addClass("close");
                            var t = setInterval(function () {
                                $("#reserveSuccess").removeClass("close");
                                clearTimeout(t);
                            }, 3000);
                            $("#reservediv").removeClass("close");
                            $(".mark-box").css("display", "none");
                        }
                    });
        });
        //迷豆商城 无登陆
        $("#getcode").click(function () {
            if (type != 1) {
                getAddress();
            } else {
                exchangeOfGoods();
            }


        });


        $("#oldaddress").click(function () {
            exchangeOfGoods();
        });
        $('#sign').click(function () {
            $('#sign').unbind("click")
            biz.sign();
        });
        //【我的】礼包中心和迷豆商城加载更多
        $("#loading-btn").live('click', function () {
            biz.jsonGiftList(callback.jsonGiftListCallBack);
        });
        //【我的】礼包中心和迷豆商城已领取 加载更多
        $("#mygift-loading-btn").click(function () {
            biz.jsonMygiftList(callback.jsonMyGiftListCallBack);
        });
        //手游画报礼包中心加载更多
//        $("#giftload").click(function() {
//            biz.jsonLoadGiftList(callback.jsonLoadGiftListCallBack);
//        });
        //手游画报迷豆商城加载更多
//        $("#midouload").click(function() {
//            biz.jsonLoadMidouList(callback.jsonLoadMidouListCallBack);
//        });
        //手游画报迷豆商城带登陆
        $("#getgoods").click(function () {
            var goodstype = $("[name='goodstype']").val();
            var logindomain = $("[name='logindomain']").val();
            if (logindomain == '' || logindomain == 'client') {
                cancelReserve();
                $("#reserveSuccess").addClass("close");
                $("#reserveSuccess").text("您还没有登录，请先登录");
                var t = setInterval(function () {
                    $("#reserveSuccess").removeClass("close");
                    clearTimeout(t);
                }, 3000);
                showLoginWindow();
            } else {
                if (goodstype != 1) {

                    getAddress();
                } else {
                    exchangeOfGoods();
                }
            }
        });

        //手游画报领取礼包
        $("#reserve").click(function () {
            var logindomain = $("[name='logindomain']").val();
            if (logindomain == '' || logindomain == 'client') {
//                $("#reserveSuccess").css("max-width", "500px");
//                $("#reserveSuccess").css("margin-left", "-90px");
                if (!bool) {
                    bool = true;
                    cancelReserve();
                    $("#reserveSuccess").css("width", "55%");
                    $("#reserveSuccess").css("line-height", "normal");
                    $("#reserveSuccess").html("<div style='padding-top:20px;'>请您登录后领取礼包<br/>3秒后自动跳转</div>");
                    $("#reserveSuccess").addClass("close");
                    var i = 3;
                    var t = setInterval(function () {
                        i--;
                        $("#reserveSuccess").html("<div style='padding-top:20px;'>请您登录后领取礼包<br/>" + i + "秒后自动跳转</div>");
                        if (i == 0) {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                            bool = false;
                            showLoginWindow();
                        }

                    }, 1000);
                }
            } else {
                biz.syhbGetcode();
            }

        });


        //手游画报淘号
        $("#Forno").click(function () {
            biz.syhbTaocode();
        });
        //手游画报蜜豆商城已领取加载更多
        $("#mymidouload").click(function () {
            biz.jsonSyhbMycoinList(callback.jsonSyhbMycoinListCallBack);
        });

        $("#syhbmygiftload").click(function () {
            biz.jsonSyhbMygiftList(callback.jsonSyhbMygiftCallBack);
        });
        var biz = {
            sign: function () {
                if (appkey == '') {
                    return;
                }
                if (profileId == '') {
                    return;
                }
                $.ajax({
                            type: "POST",
                            url: "/joymeapp/my/tasksign",
                            data: {appkey: appkey, profileid: profileId},
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                if (result.rs == 1) {
                                    $("#reserveSuccess").addClass("close");
                                    $("#reserveSuccess").text("签到成功");
                                    $("#sign").text("已签到");
                                    var midou = $("#midou").text();
                                    var point = parseInt(midou) + result.result;
                                    $("#midou").text(point);
                                    var t = setInterval(function () {
                                        $("#reserveSuccess").removeClass("close");
                                        clearTimeout(t);
                                    }, 3000);
                                } else {
                                    if (result.msg == 'today.is.not.sign') {
                                        $("#reserveSuccess").addClass("close");
                                        $("#reserveSuccess").text("您今天已经签过到了");
                                        var t = setInterval(function () {
                                            $("#reserveSuccess").removeClass("close");
                                            clearTimeout(t);
                                        }, 3000);
                                    } else {
                                        $("#reserveSuccess").addClass("close");
                                        $("#reserveSuccess").text("签到失败。");
                                        var t = setInterval(function () {
                                            $("#reserveSuccess").removeClass("close");
                                            clearTimeout(t);
                                        }, 3000);
                                    }
                                }
                                $('#sign').click(function () {
                                    biz.sign();
                                });
                            }
                        });
            }, jsonGiftList: function (callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();
                if (parseInt(maxNum) <= parseInt(curNum)) {
//                    $("#loading-btn").css("display", "none");
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                            type: "POST",
                            url: "/joymeapp/my/jsongiftlist",
                            data: {count: 10, pnum: curNum, profileid: profileId, appkey: appkey, type: type},
                            timeout:5000,
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            },error:function() {
                                toAlert("网络异常，请稍后重试");
                                var loading = document.getElementById('loading-btn'),
                                        icon = loading.getElementsByTagName('i')[0],
                                        txt = loading.getElementsByTagName('b')[0];
                                icon.style.display = 'none';
                                txt.innerHTML = '点击加载更多';
                            }
                        });
            }, jsonMygiftList: function (callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();
                if (parseInt(maxNum) <= parseInt(curNum)) {
//                    $("#mygift-loading-btn").css("display", "none");
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                            type: "POST",
                            url: "/joymeapp/my/jsonmygiftlist",
                            data: {count: 10, pnum: curNum, profileid: profileId, appkey: appkey, type: type},
                            timeout:5000,
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            },error:function() {
                                toAlert("网络异常，请稍后重试");
                                var loading = document.getElementById('mygift-loading-btn'),
                                        icon = loading.getElementsByTagName('i')[0],
                                        txt = loading.getElementsByTagName('b')[0];
                                icon.style.display = 'none';
                                txt.innerHTML = '点击加载更多';
                            }
                        });
            }, jsonLoadGiftList: function (callback) {

                var hotcurNum = $("#hotWapCurPage").val();
                var hotmaxNum = $("#hotWapMaxPage").val();
                var newcurNum = $("#newWapCurPage").val();
                var newmaxNum = $("#newWapMaxPage").val();
                if (newcurNum != '' && newmaxNum != '') {
                    if (parseInt(newmaxNum) <= parseInt(newcurNum)) {
                        return 'no';
                    }
                    newcurNum = parseInt(newcurNum) + parseInt(1);
                }
                $('.lb-main').after("<div id='loadTips'><cite></cite><b>加载更多</b></div>");
                $.ajax({
                            type: "POST",
                            url: "/json/gameclient/webview/giftmarket/newsclientgiftlist",
                            data: {count: 15, pnum: hotcurNum,npnum:newcurNum},
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            }
                        });
            }, jsonLoadMidouList: function (callback) {
                var curNum = $("#midouWapCurPage").val();
                var maxNum = $("#midouWapMaxPage").val();
                if (parseInt(maxNum) <= parseInt(curNum)) {
//                    $("#loading-btn").css("display", "none");
                    return 'no';
                }
                $('.shop').after("<div id='loadTips'><cite></cite><b>加载更多</b></div>");
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                            type: "POST",
                            url: "/joymeapp/my/jsongiftlist",
                            data: {count: 15, pnum: curNum, profileid: profileId, appkey: appkey, type: type},
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            }
                        });
            }, syhbGetcode: function () {
                var endTime = $("#timeOut").val();
                var sl = 16 * 60 * 60 * 1000;
                endTime = new Date(endTime);
                var time = endTime.getTime() + sl;
                endTime = new Date(time);
                var now = new Date();
                if (now > endTime) {
                    $("#reserveSuccess").text("该礼包已过期");
                    $("#reserveSuccess").addClass("close");
                    var t = setInterval(function () {
                        $("#reserveSuccess").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                }
                var rn = $("[name='rn']").val();
                if (rn == '0') {
                    $("#reserveSuccess").text("礼包已经被领光啦！");
                    $("#reserveSuccess").addClass("close");
                    var t = setInterval(function () {
                        $("#reserveSuccess").removeClass("close");
                        clearTimeout(t);
                    }, 3000);
                    return;
                }
                var gameId = $("[name='gameid']").val();
                if (gameId != '' && gameId != null) {
                    var content = '<div class="dialog close" id="reservediv"> <h1>是否关注该游戏？</h1><p class="confirm-text2"><span class="all">关注该游戏才能第一时间领到礼包</span></p>' +
                            '<div class="dialog-btn"><a href="javascript:unLikeGameButton();" class="cancel">以后再说</a><a href="javascript:likeGameButtion();" id="reserveGift"class="gain">马上关注</a></div>';
                    $("#wrapper").append(content);
                    $(".mark-box").show();
                } else {
                    getcode();
                }

            }, syhbTaocode: function () {
                $.ajax({
                            type: "POST",
                            url: "/joymeapp/activity/gift/taocode",
                            data: {appkey: appkey, profileid: profileId, gid: gid, uno: uno},
//                            async:false,
                            success: function (req) {
                                var result = eval('(' + req + ')');
//                                $("#reserveSuccess").css("width", "55%");
//                                $("#reserveSuccess").css("line-height", "normal");
                                if (result.msg == 'time.is.out') {
                                    $("#reserveSuccess").html("该礼包已过期");
                                    $("#reserveSuccess").addClass("close");
                                    var t = setInterval(function () {
                                        $("#reserveSuccess").removeClass("close");
                                        clearTimeout(t);
                                    }, 3000);
                                } else if (result.msg == 'taocode.is.null') {
                                    $("#reserveSuccess").html("还没有开始淘号哦！");
                                    $("#reserveSuccess").addClass("close");
                                    var t = setInterval(function () {
                                        $("#reserveSuccess").removeClass("close");
                                        clearTimeout(t);
                                    }, 3000);
                                } else if (result.msg == 'taocode.is.success') {
                                    $("#wrapper").css("display", "none");
                                    $(".th-result").css("display", "block");
                                    var content = "";
                                    for (var i = 0; i < result.result.value.length; i++) {
                                        content += "<div class='code'>  <div class='codebox'> <div><span class='fl cut_out5'>" + result.result.value[i] + "</span><a href='javascript:taocodecopy(" + '"' + result.result.value[i] + '"' + ");' class='copyBtn fr'>复制</a></div></div></div>";
                                    }
                                    $("#taocodevalue").append(content);
                                    $.each($('.cut_out5'), function () {
                                        cut_out($(this), 19)
                                    })
                                }
                            }
                        });
            }, jsonSyhbMycoinList: function (callback) {
                var curNum = $("#midouWapCurPage").val();
                var maxNum = $("#midouWapMaxPage").val();
                if (parseInt(maxNum) <= parseInt(curNum)) {
//                    $("#mygift-loading-btn").css("display", "none");
                    return 'no';
                }
                curNum = parseInt(curNum) + parseInt(1);
                $('.giftwrapper').after("<div id='loadTips'><cite></cite><b>加载更多</b></div>");
                $.ajax({
                            type: "POST",
                            url:  "/joymeapp/my/jsonmygiftlist",
                            data: {count: 8, pnum: curNum, profileid: profileId, appkey: appkey, type: 2},
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                                alert(XMLHttpRequest.status);
                                alert(XMLHttpRequest.readyState);
                                alert(textStatus);
                            }
                        });
            }, jsonSyhbMygiftList: function (callback) {
                var curNum = $("#giftWapCurPage").val();
                var maxNum = $("#giftWapMaxPage").val();
                if (parseInt(maxNum) <= parseInt(curNum)) {
//                    $("#mygift-loading-btn").css("display", "none");
                    return 'no';
                }
                $('.giftwrappers').after("<div id='loadTips'><cite></cite><b>加载更多</b></div>");
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                            type: "POST",
                            url: "/json/gameclient/webview/giftmarket/newsclientmygiftlist",
                            data: {count: 8, pnum: curNum, profileid: profileId, appkey: appkey, type: 2},
                            success: function (req) {
                                var result = eval('(' + req + ')');
                                callback(result);
                            }
                        });
            }
        }

        var callback = {
            jsonGiftListCallBack: function (result) {
                var content = "";
                if (type == '1') {
                    for (var index = 0; index < result.result.rows.length; index++) {
                        content += "<li>";
                        if (result.result.rows[index].reserveType == 1) {
                            content += "<cite class='hot'><img src='" + joyconfig.URL_LIB + "/static/theme/default/images/my/reserve.png' ></cite>"
                        }
                        content += "<a href='http://api." + joyconfig.DOMAIN + "/my/giftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=1&clientid=" + clientid + "&platform=" + platform + "&moneyname=" + wallname + "'><div class='main-list'><cite class='title-pic'><img src='" + result.result.rows[index].gipic + "'></cite>" +
                                "<div class='title-text'><h1 class='cut_out1'>" + result.result.rows[index].title + "</h1><p  class='cut_out1'>" + result.result.rows[index].desc + "</p>";
                        if (result.result.rows[index].reserveType == 1) {

                            if (result.result.reservelist == "") {
                                content += "</div></div> <span class='mian-btn reserveing'>预定</span>";
                            } else {
                                var bool;
                                for (var i = 0; i < result.result.reservelist.length; i++) {
                                    if (result.result.reservelist[i].aid == result.result.rows[index].gid) {
                                        content += "</div></div> <span class='reserve'>已预定</span>";
                                        bool = true;
                                    }
                                }
                                if (!bool) {
                                    content += "</div></div> <span class='mian-btn reserveing'>预定</span>";
                                }
                                bool = false;
                            }

                        } else {
                            content += "<p>所需" + wallname + "：<span>" + result.result.rows[index].point + "</span></p></div></div>";
                            if (result.result.rows[index].sn == 0) {
                                content += "<span class='mian-btn sack'>抢光了</span>";
                            } else {
                                content += "<span class='mian-btn exchange'>兑换</span>";
                            }
                        }
                        content += "</a></li>";

                    }
                } else {
                    for (var index = 0; index < result.result.rows.length; index++) {
                        content += " <a href='http://api." + joyconfig.DOMAIN + "/my/giftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=2&clientid=" + clientid + "&platform=" + platform + "&moneyname=" + wallname + "'>" +
                                "<div class='shop-box'> <cite><img src='" + result.result.rows[index].gipic + "'></cite>" +
                                " <div class='shop-box-text'><h2 class='cut_out2'>" + result.result.rows[index].title + "</h2> <h3><span>" + result.result.rows[index].point + "</span>" + wallname + "</h3></div>";
                        if (result.result.rows[index].sn == 0) {
                            content += " <div class='shop-btn s2'>已售罄</div>";
                        } else {
                            content += " <div class='shop-btn s1'>奖品有限，速来参加</div>";
                        }
                        content += "</div></a>";
                    }
                }

                $("#giftul").append(content);
                if (type == 1) {
                    $.each($('.cut_out1'), function () {
                        cut_out($(this), 13)
                    })
                } else {
                    $.each($('.cut_out2'), function () {
                        cut_out($(this), 9)
                    })
                }
                $("#wapCurPage").val(result.result.page.curPage);
                $("#wapMaxPage").val(result.result.page.maxPage);
                if (result.result.page.curPage >= result.result.page.maxPage) {
                    $("#loading-btn").css("display", "none");
                }
                var loading = document.getElementById('loading-btn'),
                        icon = loading.getElementsByTagName('i')[0],
                        txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '点击加载更多';
            }, jsonMyGiftListCallBack: function (result) {
                var content = "";
                if (type == '1') {
                    for (var index = 0; index < result.result.rows.length; index++) {
                        content += "<li>";
                        content += "<a href='http://api." + joyconfig.DOMAIN + "/my/mygiftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=1&moneyname=" + wallname + "'><div class='main-list'><cite class='title-pic'><img src='" + result.result.rows[index].gipic + "'></cite>" +
                                "<div class='title-text'><h1>" + result.result.rows[index].title + "</h1><p>有效期&nbsp;&nbsp;<span class='color'>" + result.result.rows[index].exDate + "</span></p></div> <span class='mian-btn reserveing'>查号</span></div>";

                        content += "</a></li>";
                    }
                    $("#giftul").append(content);
                } else {
                    for (var index = 0; index < result.result.rows.length; index++) {
                        content += " <a href='http://api." + joyconfig.DOMAIN + "/my/mygiftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=2&consumeorder=" + result.result.rows[index].consumeOrder + "&moneyname=" + wallname + "'>" +
                                " <div class='order clearfix'><h2><em  class='fr'>已兑换</em><p>订单号：<span>" + result.result.rows[index].consumeOrder + "</span></p></h2>" +
                                " <dl class='clearfix'><dt class='fl'><img src='" + result.result.rows[index].gipic + "' ></dt><dd class='fl'><h3>" + result.result.rows[index].title + " " +
                                "<p>消耗<span>" + result.result.rows[index].point + "</span>" + wallname + "</p> </dd></dl>";

                        content += "</div></a>";

                    }
                    $("#wrapper").append(content);
                }


                $("#wapCurPage").val(result.result.page.curPage);
                $("#wapMaxPage").val(result.result.page.maxPage);
                if (result.result.page.curPage >= result.result.page.maxPage) {
                    $("#mygift-loading-btn").css("display", "none");
                }
                var loading = document.getElementById('mygift-loading-btn'),
                        icon = loading.getElementsByTagName('i')[0],
                        txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '点击加载更多';
            }, jsonLoadMidouListCallBack: function (result) {
                var content = "";
                for (var index = 0; index < result.result.rows.length; index++) {
                    content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=2" + "'>" +
                            "<div class='shop-box'> <cite><img src='" + result.result.rows[index].gipic + "'></cite>" +
                            " <div class='shop-box-text'><h2 class='cut_out2'>" + result.result.rows[index].title + "</h2> <h3><span>" + result.result.rows[index].point + "</span>迷豆</h3></div>";
                    if (result.result.rows[index].sn == 0) {
                        content += " <div class='shop-btn s2'>已售罄</div>";
                    } else {
                        content += " <div class='shop-btn s1'>奖品有限，速来参加</div>";
                    }
                    content += "</div></a>";
                }
                $(".shop").append(content);
                $.each($('.cut_out2'), function () {
                    cut_out($(this), 9)
                });
                $("#midouWapCurPage").val(result.result.page.curPage);
                $("#midouWapMaxPage").val(result.result.page.maxPage);
                $('#loadTips').remove();
//                if (result.result.page.curPage >= result.result.page.maxPage) {
//                    $("#midouload").css("display", "none");
//                }
//                var loading = document.getElementById('midouload'),
//                        icon = loading.getElementsByTagName('i')[0],
//                        txt = loading.getElementsByTagName('b')[0];
//                icon.style.display = 'none';
//                txt.innerHTML = '点击加载更多';
            }, jsonLoadGiftListCallBack: function (result) {

                if (result.result.hotrows != '') {
                    var curPage = result.result.hotpage.curPage;
                    if (curPage == '1') {
                        var div = "<div class='lb-main-classify lb-main-rm' id='hot-Main'><p>热门</p></div>";
                        $("#libao-Main").before(div);
                    }
                    var content = "";
                    for (var index = 0; index < result.result.hotrows.length; index++) {
                        var content = "<dl><a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.hotrows[index].gid + "&type=1'><dt class='fl'>";
                        if (result.result.hotrows[index].reserveType == 1) {
                            content += "<cite class='yyz'>预约中</cite>"
                        } else {
                            if (result.result.hotrows[index].weixinExclusive == 2) {
                                content += "<cite class='dj'>独家</cite>"
                            }
                        }
                        content += "<p><img src='" + result.result.hotrows[index].gipic + "'></p></dt>" +
                                " <dd class='fl'><div class='fl'><h1 class='cut_out2'>" + result.result.hotrows[index].title + "</h1> <span> " + result.result.hotrows[index].desc + "<b>&nbsp;</b></span>";

                        if (result.result.hotrows[index].reserveType != 1) {
                            content += " <span class='lb-mian-surplus'>剩余：<b>" + result.result.hotrows[index].sn + "/" + result.result.hotrows[index].cn + "</b></span>";
                        }
                        content += "</div>";
                        if (result.result.hotrows[index].reserveType == 1) {
                            content += " <span class='min_btn yy'>约</span>";
                        } else {
                            if (result.result.hotrows[index].sn == 0) {
                                content += "<span class='min_btn th'>淘</span>";
                            } else {
                                content += "<span class='min_btn lh'>领</span>";
                            }
                        }
                        content += " </dd></a></dl>";
                        $("#hot-Main").append(content);
                    }
                    $("#hotWapCurPage").val(result.result.hotpage.curPage);
                    $("#hotWapMaxPage").val(result.result.hotpage.maxPage);

                }
                if (result.result.newrows != '') {
                    var curPage = result.result.newpage.curPage;
                    if (curPage == '1') {
                        var div = "<div class='lb-main-classify lb-main-zx' id='libao-Main'><p>最新</p></div>";
                        $("#div-Main").append(div);
                    }
                    var content = "";
                    for (var index = 0; index < result.result.newrows.length; index++) {
                        var content = "<dl><a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.newrows[index].gid + "&type=1'><dt class='fl'>";
                        if (result.result.newrows[index].reserveType == 1) {
                            content += "<cite class='yyz'>预约中</cite>"
                        } else {
                            if (result.result.newrows[index].weixinExclusive == 2) {
                                content += "<cite class='dj'>独家</cite>"
                            }
                        }
                        content += "<p><img src='" + result.result.newrows[index].gipic + "'></p></dt>" +
                                " <dd class='fl'><div class='fl'><h1 class='cut_out2'>" + result.result.newrows[index].title + "</h1> <span> " + result.result.newrows[index].desc + "<b>&nbsp;</b></span>";

                        if (result.result.newrows[index].reserveType != 1) {
                            content += " <span class='lb-mian-surplus'>剩余：<b>" + result.result.newrows[index].sn + "/" + result.result.newrows[index].cn + "</b></span>";
                        }
                        content += "</div>";
                        if (result.result.newrows[index].reserveType == 1) {
                            content += " <span class='min_btn yy'>约</span>";
                        } else {
                            if (result.result.newrows[index].sn == 0) {
                                content += "<span class='min_btn th'>淘</span>";
                            } else {
                                content += "<span class='min_btn lh'>领</span>";
                            }
                        }
                        content += " </dd></a></dl>";
                        $("#libao-Main").append(content);
                    }
                    $("#newWapCurPage").val(result.result.newpage.curPage);
                    $("#newWapMaxPage").val(result.result.newpage.maxPage);
                }
//                for (var index = 0; index < result.result.rows.length; index++) {
//                    content += "<dl> <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=1&uno=" + uno + "'><dt class='fl'>";
//                    if (result.result.rows[index].reserveType == 1) {
//                        content += "<cite class='yyz'></cite>"
//                    }
//                    content += "<p><img src='" + result.result.rows[index].gipic + "'></p></dt>" +
//                            " <dd class='fl'><div class='fl'><h1 class='cut_out2'>" + result.result.rows[index].title + "</h1><p>有效期：<span>" + result.result.rows[index].exDate + "</span></p></div>";
//                    if (result.result.rows[index].reserveType == 1) {
//
////                        if (result.result.reservelist == "") {
////                            content += "</div></div> <span class='mian-btn reserveing'>预定</span>";
////                        } else {
//                        var bool;
//                        for (var i = 0; i < result.result.reservelist.length; i++) {
//                            if (result.result.reservelist[i].aid == result.result.rows[index].gid) {
//                                content += "<span class='min_btn yyy'>已预约</span>";
//                                bool = true;
//                            }
//                        }
//                        if (!bool) {
//                            content += "<span class='min_btn yy'>预约</span>";
//                        }
//                        bool = false;
////                        }
//
//                    } else {
//                        if (result.result.rows[index].sn == 0) {
//                            content += "  <span class='min_btn th'>淘号</span>";
//                        } else {
//                            content += "<span class='min_btn lh'>领号</span>";
//                        }
//                    }
//                    content += " </dd></a></dl>";
//                }

//                $.each($('.cut_out2'), function () {
//                    cut_out($(this), 9)
//                });
                loadingbool = false;
                $('#loadTips').remove();

//                if (result.result.page.curPage >= result.result.page.maxPage) {
//                    $("#giftload").css("display", "none");
//                }
//                var loading = document.getElementById('giftload'),
//                        icon = loading.getElementsByTagName('i')[0],
//                        txt = loading.getElementsByTagName('b')[0];
//                icon.style.display = 'none';
//                txt.innerHTML = '点击加载更多';
            }, jsonSyhbMycoinListCallBack: function (result) {
                var content = '';
                for (var index = 0; index < result.result.rows.length; index++) {
                    content += " <a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/giftmarket/mygiftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=2&consumeorder=" + result.result.rows[index].consumeOrder + "'>" +
                            " <div><h1><p class='fl'>订单号：<span>" + result.result.rows[index].consumeOrder + "</span></p><p class='fr'>" + result.result.rows[index].exchangeTime + "</p></h1>" +
                            "<div class='baby-box-li'> <span class='fl'><img src='" + result.result.rows[index].gipic + "' ></span><div class='baby-box-li-text fl'> <h2 class='cut_out2'>" + result.result.rows[index].title + "</h2><p>数量：<span>1</span></p> " +
                            "<p class='color'><img src='" + joyconfig.URL_LIB + "/static/theme/wap/images/midou-min.png'>消耗<span>" + result.result.rows[index].point + "</span>迷豆</p></div><span class='fr'></span>";

                    content += "</div></div></a>";
                }

                $(".baby-box").append(content);
//                $.each($('.cut_out2'), function () {
//                    cut_out($(this), 9)
//                });
                $("#midouWapCurPage").val(result.result.page.curPage);
                $("#midouWapMaxPage").val(result.result.page.maxPage);
//                if (result.result.page.curPage >= result.result.page.maxPage) {
//                    $("#mymidouload").css("display", "none");
//                }
//                var loading = document.getElementById('mymidouload'),
//                        icon = loading.getElementsByTagName('i')[0],
//                        txt = loading.getElementsByTagName('b')[0];
//                icon.style.display = 'none';
//                txt.innerHTML = '点击加载更多';
                $('#loadTips').remove();
            }, jsonSyhbMygiftCallBack: function (result) {
                var content = '';
                for (var index = 0; index < result.result.rows.length; index++) {
                    content += " <dl><a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/giftmarket/mygiftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].aid + "&type=1&lid=" + result.result.rows[index].lid + "'>" +
                            "<dt class='fl'> <p><img src='" + result.result.rows[index].gipic + "' ></p></dt> <dd class='fl'>  <div class='fl'>" +
                            "<h1 class='cut_out2'>" + result.result.rows[index].title + "</h1><p>有效期：<span>" + result.result.rows[index].endTime + "</span></p></div><span class='min_btn ch'>查</span></dd></a></dl> ";
                }

                $(".lb-main").append(content);
                $.each($('.cut_out2'), function () {
                    cut_out($(this), 9)
                });
                $("#giftWapCurPage").val(result.result.page.curPage);
                $("#giftWapMaxPage").val(result.result.page.maxPage);
                $('#loadTips').remove();
//                if (result.result.page.curPage >= result.result.page.maxPage) {
//                    $("#syhbmygiftload").css("display", "none");
//                }
//                var loading = document.getElementById('syhbmygiftload'),
//                        icon = loading.getElementsByTagName('i')[0],
//                        txt = loading.getElementsByTagName('b')[0];
//                icon.style.display = 'none';
//                txt.innerHTML = '点击加载更多';
            }
        }

        var loadToole = {
            'isLoading': false,
            'loadTime': null,
            //这里是要加载的内容
            'loadComment': function (config) {

                loadToole.isLoading = false;
                clearInterval(loadToole.loadTime);

            },
            //滚动加载
            'scroll_load': function (className, parentBox) {

                var className = className, parentBox = parentBox;
                $("." + className).scroll(function (ev) {
                    ev.stopPropagation();
                    ev.preventDefault();
                    var sTop = $("." + className)[0].scrollTop + 45;
                    var sHeight = $("." + className)[0].scrollHeight;
                    var sMainHeight = $("." + className).height();
                    var sNum = sHeight - sMainHeight;
                    var loadTips = "<div id='loadTips'><cite></cite><b>加载更多</b></div>";
                    if (sTop >= sNum && !loadToole.isLoading) {
                        if (className == 'wrapper') {
                            if (!loadingbool) {
                                loadingbool = true;
                                var bool = biz.jsonLoadGiftList(callback.jsonLoadGiftListCallBack);
                                if (bool == 'no') {
                                    return;
                                }
                            }

                        } else if (className == 'wrappers') {
                            var bool = biz.jsonLoadMidouList(callback.jsonLoadMidouListCallBack);
                            if (bool == 'no') {
                                return;
                            }

                        } else if (className == 'giftwrappers') {
                            var bool = biz.jsonSyhbMycoinList(callback.jsonSyhbMycoinListCallBack);
                            if (bool == 'no') {
                                return;
                            }
                        } else if (className == 'giftwrapper') {
                            var bool = biz.jsonSyhbMygiftList(callback.jsonSyhbMygiftCallBack);
                            if (bool == 'no') {
                                return;
                            }
                        } else {
                            return;
                        }

                        loadToole.isLoading = true;

//                        $('.shop').append(loadTips);
//                        $('.giftwrappers').append("<div id='loadTips'><cite></cite><b>加载更多</b></div>");
//                        $('.giftwrappers').append(loadTips);
                        loadToole.loadTime = setTimeout(function () {
                            loadToole.loadComment(parentBox);
                        }, 1000);

                    }
                    ;
                });
            }
        }
        loadToole.scroll_load('wrapper', 'lb-main');
        loadToole.scroll_load('wrappers', 'shop');
        loadToole.scroll_load('giftwrapper', 'giftwrapper');
        loadToole.scroll_load('giftwrappers', 'giftwrappers');
    });
    function cut_out($target, length) {
        var text = $target.text();
        var len = text.length;
        if (len > length) {
            return $target.text(text.substr(0, length) + '...');
        } else {
            return $target.text();
        }
    }


    $.each($('.cut_out'), function () {
        cut_out($(this), 8)
    })
//    $.each($('.cut_out1'), function () {
//        cut_out($(this), 13)
//    })
//    $.each($('.cut_out2'), function () {
//
//        cut_out($(this), 9)
//    })
    $.each($('.midou_cut'), function () {
        cut_out($(this), 5)
    })
    $.each($('.cut_out3'), function () {
        cut_out($(this), 9)
    })
    $.each($('.cut_out5'), function () {
        cut_out($(this), 19)
    });


});

function oldAddress() {
    exchangeOfGoods();
    $(".mark-box").css("display", "none");
    $(".dialog").removeClass("close");

}

//迷豆商城领取
var flag = false;
function exchangeOfGoods() {
    var appkey = $("[name='appkey']").val();
    var profileId = $("[name='profileId']").val();
    var aid = $("[name='aid']").val();
    var gid = $("[name='gid']").val();
    var type = $("[name='type']").val();
    var template = $("[name='template']").val();
    var dtopoint = $("[name='dtopoint']").val();
    if (!flag) {
        flag = true;
        $.ajax({
                    type: "POST",
                    url: "/joymeapp/my/exchange",
                    data: {appkey: appkey, profileid: profileId, aid: aid, gid: gid, type: type},
//                async:false,
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        if (result.rs == '-99999') {
                            $(".mark-box").css("display", "none");
                            $("#reserveSuccess").text("系统维护中");
                            $("#reserveSuccess").addClass("close");
                            var t = setInterval(function () {
                                $("#reserveSuccess").removeClass("close");
                                clearTimeout(t);
                            }, 3000);
                        }
                        if (result.status_code == '0') {
                            $("#cofirmshopdiv").removeClass("close");
                            $(".mark-box").css("display", "none");
                            if (result.msg == '您的节扌…积分余额不足，请加油赚积分吧！') {
                                $("#gopointwall").addClass("close");
                                $(".mark-box").css("display", "block");
                            } else {
                                $("#reserveSuccess").text(result.msg);
                                $("#reserveSuccess").addClass("close");
//                            $("#reserveSuccess").css("line-height", "normal");
                                var t = setInterval(function () {
                                    $("#reserveSuccess").removeClass("close");
                                    clearTimeout(t);
                                }, 3000);
                            }

                        } else {
                            var dto = result.result[0];
                            if (dto.goodsType == 1) {
                                $("#cofirmshopdiv").removeClass("close");
                                $(".mark-box").css("display", "none");
                                $("#exchange").css("display", "block");
                                $("#wrapper").css("display", "none");

                                $(".codebox").children("div").children("span").text(dto.itemValue1);
                                $(".codebox").children("div").children("a").attr("href", "javascript:copy('" + dto.itemValue1 + "')");
                                if (dto.itemValue2 != '' && dto.itemValue2 != null) {
                                    $("#kahao").css("display", "block");
                                    $("#password").css("display", "block");
                                    $("#password").children("div").children("div").children("span").text(dto.itemValue2);
                                    $("#password").children("div").children("div").children("a").attr("href", "javascript:copy('" + dto.itemValue2 + "')");
                                    $(".done").css("display", "none");
                                }
                                $.each($('.cut_out5'), function () {
                                    var text = $(this).text();
                                    var len = text.length;
                                    if (len > 19) {
                                        return $(this).text(text.substr(0, 19) + '...');
                                    } else {
                                        return $(this).text();
                                    }
                                });

                            } else {
                                $(".mark-box").css("display", "block");
                                var address = "";
                                if (template == null || template == "") {
                                    address = '<div class="dialog close" > <h1>下单成功</h1>' +
                                            '<p class="confirm-text2">兑换成功，我们会尽快为您发货，订单详情请到【我的宝贝查询】' +
                                            '<div class="dialog-btn" ><a href="javascript:cancelReserve();"  style="border-left:none;" class="cancel">知道了</a>' +
                                            '</div></div>'
                                } else {
                                    address = '<div class="dialog close" > <h1>下单成功</h1>';
                                    if (template == 3) {
                                        address += '<p class="confirm-text2">兑换成功，我们会尽快为您发货，订单详情请到【兑换记录】中查询';
                                    } else {
                                        address += '<p class="confirm-text2">兑换成功，我们会尽快为您发货，订单详情请到【我的宝贝查询】';
                                    }
                                    address += '<div class="dialog-btn" style="padding-left:75px;"><a href="javascript:cancelReserve();" class="cancel">知道了</a>' + '</div></div>'
                                }
                                $("#wrapper").append(address);
                                var sn = $("[name='sn']").val();
                                var sn2 = parseInt(sn) - 1;
                                $("[name='sn']").val(sn2);
                                var status = $("[name='exchangeStatus']").val();
                                if (status != '1') {
                                    $("[name='allowExchangeStatus']").val(status);
                                }
                            }
                            getPoint(dtopoint);
                        }
                        flag = false;
                    }
                });
    }
}


function getAddress() {
    $(".dialog").removeClass("close");
    var profileId = $("[name='profileId']").val();
    $.ajax({
                type: "POST",
                url: "/joymeapp/my/getaddress",
                data: {profileid: profileId},
                success: function (req) {
                    var data = eval('(' + req + ')');
                    if (data.rs == 0) {
                        alert("兑换失败");
                        return;
                    } else {
                        if (data.msg == 'address.is.null') {
                            var address = '<div class="dialog close" > <h1>提示</h1>' +
                                    '<p class="confirm-text2">检测到您还没有填写收货地址，是否填写？' +
                                    '<div class="dialog-btn"><a href="javascript:cancelReserve();" class="cancel">取消</a><a href="javascript:writeAddress();" class="gain">填写</a>' +
                                    '</div></div>'
                            $("#wrapper").append(address);
                        } else {

                            var address = '<div class="dialog close" style="height:200px;"> <h1>使用默认地址</h1>' +
                                    '<p class="confirm-text2" style="text-align:left;">收货人：' + data.result.address.contact + '' +
                                    '<p class="confirm-text2" style="text-align:left;">地址:' + data.result.address.province + data.result.address.city + data.result.address.county + data.result.address.address + '' +
                                    '<p class="confirm-text2" style="text-align:left;">电话:' + data.result.address.phone + '' +
                                    '<div class="dialog-btn"><a href="javascript:oldAddress();"  class="gain">使用此地址</a><a href="javascript:writeAddress();"class="cancel" >使用新地址</a></a>' +
                                    '</div></div>'
                            $("#wrapper").append(address);
                        }
                    }
                }
            });
}

// 关注游戏
function likeGameButtion() {
    $(".dialog").removeClass("close");
    $(".mark-box").css("display", "none");
    var appkey = $("[name='appkey']").val();
    var token = $("[name='token']").val();
    var uno = $("[name='uno']").val();
    var uid = $("[name='uid']").val();
    var gid = $("[name='gameid']").val();
    var platform = $("[name='platform']").val();
    $.ajax({
                type: "POST",
                url: "/joymeapp/gameclient/json/game/like",
                data: {appkey: appkey, token: token, uno: uno, uid: uid, gid: gid,platform:platform},
//                            async:false,
                success: function (req) {
                    var result = eval('(' + req + ')');
                    if (result.rs == '1') {
                        $("[name='gameid']").val("");
                        $("#reserveSuccess").css("width", "55%");
                        $("#reserveSuccess").text("关注成功！正在领号请稍后。");
                        $("#reserveSuccess").addClass("close");
                        var t = setInterval(function () {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                            getcode();
                        }, 3000);

                        //跟客户端交互
                        jump(gid);
                    } else {
                        $("#reserveSuccess").text("关注失败！");
                        $("#reserveSuccess").addClass("close");
                        var t = setInterval(function () {
                            $("#reserveSuccess").removeClass("close");
                            clearTimeout(t);
                        }, 3000);
                        getcode();
                    }

                }
            });
}
//领取礼包
var flag = false;
function getcode() {
    var appkey = $("[name='appkey']").val();
    var profileId = $("[name='profileId']").val();
    var uno = $("[name='uno']").val();
    var gid = $("[name='gid']").val();
    var platform = $("[name='platform']").val();
    var token = $("[name='token']").val();
    var uid = $("[name='uid']").val();
    var logindomain = $("[name='logindomain']").val();
    var dtopoint = $("[name='dtopoint']").val();
    var otherid = $("[name='otherid']").val();
    var channelid = $("[name='channelid']").val();
    var clientid = $("[name='clientid']").val();

    if (!flag) {
        flag = true;
        $.ajax({
                    type: "POST",
                    url: "/json/gameclient/webview/giftmarket/getcode",
                    data: {appkey: appkey, profileid: profileId, gid: gid, uno: uno,token:token,uid:uid,logindomain:logindomain,otherid:otherid,channelid:channelid,clientid:clientid},
//                            async:false,
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        if (result.rs == "-99999") {
                            $("#reserveSuccess").html("系统维护中。");
                            $("#reserveSuccess").addClass("close");
                            var t = setInterval(function () {
                                $("#reserveSuccess").removeClass("close");
                                clearTimeout(t);
                            }, 3000);
                        } else if (result.rs == "-201") {
                            $('.Threebox').find("span").text("需要先升级，才能领礼包哦");
                            $('.Threebox').show().delay(3000).hide(0);
                        } else if (result.rs == '-20004') {
                            $('.Threebox').find("span").text("您的设备领取该礼包已达上限");
                            $('.Threebox').show().delay(3000).hide(0);
                        } else if (result.rs == 1) {
                            $("#cofirmshopdiv").removeClass("close");
                            $(".mark-box").css("display", "none");
                            $("#exchange").css("display", "block");
                            $("#wrapper").css("display", "none");
                            $(".codebox").children("div").children("span").text(result.result.value);
                            getPoint(dtopoint);
                        } else {
                            if (result.msg == 'user.gift.getcode.one') {
                                var times_type = $("[name='times_type']").val();
                                if (times_type != null && times_type != undefined) {
                                    var code = $("[name='code']").val();
                                    if (times_type == 2) {
                                        $("#codemsg").html("该礼包每天只能领取一次");
                                    }
                                    $(".mark-box").css("display", "none");
                                    $("#exchange").css("display", "block");
                                    $("#wrapper").css("display", "none");
                                    $("#codemsg").css("display", "block");
                                    $(".codebox").children("div").children("span").text(code);
                                } else {
                                    $("#reserveSuccess").html("对不起,每个账号只能领1次,<br/>学长只能帮你到这里了");
                                    $("#reserveSuccess").addClass("close");
                                    $("#reserveSuccess").css("line-height", "normal");
                                    $("#reserveSuccess").css("max-width", "295px");
                                    $("#reserveSuccess").css("margin-left", "-90px");
                                    $("#reserveSuccess").css("width", "55%");
                                    var t = setInterval(function () {
                                        $("#reserveSuccess").removeClass("close");
                                        clearTimeout(t);
                                    }, 3000);
                                }

                            } else if (result.msg == 'user.gift.getcode.by.day') {
                                var times_type = $("[name='times_type']").val();
                                if (times_type != null && times_type != undefined) {
                                    var code = $("[name='code']").val();
                                    if (times_type == 2) {
                                        $("#codemsg").html("该礼包每天只能领取一次");
                                    }
                                    $(".mark-box").css("display", "none");
                                    $("#exchange").css("display", "block");
                                    $("#wrapper").css("display", "none");
                                    $("#codemsg").css("display", "block");
                                    $(".codebox").children("div").children("span").text(code);
                                } else {
                                    $("#reserveSuccess").css("width", "55%");
                                    $("#reserveSuccess").css("line-height", "normal");
                                    $("#reserveSuccess").html("<div style='padding-top:20px;'>今天您已经领取过该礼包了哦，请明天再来吧！</div>");
                                    $("#reserveSuccess").addClass("close");
                                    var t = setInterval(function () {
                                        $("#reserveSuccess").removeClass("close");
                                        clearTimeout(t);
                                    }, 3000);
                                }
                            } else if (result.msg == 'user.gift.getcode.by.intaval') {
                                var times_type = $("[name='times_type']").val();
                                if (times_type != null && times_type != undefined) {
                                    var code = $("[name='code']").val();

                                    $("#codemsg").html("不久前您已领取过该礼包，请过一会再来看看。");

                                    $(".mark-box").css("display", "none");
                                    $("#exchange").css("display", "block");
                                    $("#wrapper").css("display", "none");
                                    $("#codemsg").css("display", "block");
                                    $(".codebox").children("div").children("span").text(code);
                                } else {
                                    $("#reserveSuccess").css("width", "55%");
                                    $("#reserveSuccess").css("line-height", "normal");
                                    $("#reserveSuccess").html("<div style='padding-top:20px;'>不久前您已领取过该礼包<br/>请过一会再来看看。</div>");
                                    $("#reserveSuccess").addClass("close");
                                    var t = setInterval(function () {
                                        $("#reserveSuccess").removeClass("close");
                                        clearTimeout(t);
                                    }, 3000);
                                }
                            } else if (result.msg == 'time.is.out') {
                                $("#reserveSuccess").text("该礼包已过期");
                                $("#reserveSuccess").addClass("close");
                                var t = setInterval(function () {
                                    $("#reserveSuccess").removeClass("close");
                                    clearTimeout(t);
                                }, 3000);
                            } else if (result.msg == 'getcode.is.null') {
                                $("#reserveSuccess").text("礼包已经被领光啦！");
                                $("#reserveSuccess").addClass("close");
                                var t = setInterval(function () {
                                    $("#reserveSuccess").removeClass("close");
                                    clearTimeout(t);
                                }, 3000);
                            } else if (result.msg == 'user.not.login') {
                                $("#reserveSuccess").text("用户未登录！");
                                $("#reserveSuccess").addClass("close");
                                var t = setInterval(function () {
                                    $("#reserveSuccess").removeClass("close");
                                    clearTimeout(t);
                                }, 3000);
                            } else if (result.msg == 'is.not.newsclient.exclusive') {
                                $("#reserveSuccess").text("该礼包不是玩霸礼包！");
                                $("#reserveSuccess").addClass("close");
                                var t = setInterval(function () {
                                    $("#reserveSuccess").removeClass("close");
                                    clearTimeout(t);
                                }, 3000);
                            } else if (result.msg == 'point.not.enough') {
                                $("#gopointwall").addClass("close");
                                $(".mark-box").css("display", "block");
                            } else if (result.msg == 'user.point.update.failed') {
                                $("#reserveSuccess").text("迷豆扣除失败，请重试");
                                $("#reserveSuccess").addClass("close");
                                var t = setInterval(function () {
                                    $("#reserveSuccess").removeClass("close");
                                    clearTimeout(t);
                                }, 3000);
                            } else if (result.msg == 'system.error') {
                                $("#reserveSuccess").text("系统繁忙，请重试");
                                $("#reserveSuccess").addClass("close");
                                var t = setInterval(function () {
                                    $("#reserveSuccess").removeClass("close");
                                    clearTimeout(t);
                                }, 3000);
                            }
                        }
                        flag = false;
                    }
                });
    }
}

function cancelReserve() {
    $(".mark-box").css("display", "none");
    $(".dialog").removeClass("close");

}

function unLikeGameButton() {
    getcode();
    $(".mark-box").css("display", "none");
    $(".dialog").removeClass("close");
}

function writeAddress() {
    $("#wrapper").css("display", "none");
    $(".new-address").css("display", "block");
    $(".mark-box").css("display", "none");
    $(".dialog").removeClass("close");
}

function cancelAddress() {
    $("#wrapper").css("display", "block");
    $(".new-address").css("display", "none");
    $(".mark-box").css("display", "none");
    $(".dialog").removeClass("close");
}

function goTask(url) {
    $(".mark-box").css("display", "none");
    $(".dialog").removeClass("close");
    window.location.href = url;
}

function jump(gid) {
    _jclient.jump("jt=32&ji=" + gid);
}

function showLoginWindow() {
    _jclient.showLogin();
}

function getPoint(point) {
    _jclient.jump("jt=33&ji=" + (-point));
}


function toAlert(content) {
    $("#popup").text(content);
    $("#popup").addClass("close");
    var t = setInterval(function () {
        $("#popup").removeClass("close");
        clearTimeout(t);
    }, 3000);

}