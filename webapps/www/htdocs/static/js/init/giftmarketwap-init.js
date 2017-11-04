define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var alertOption = {
        tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    var interval;
    $(document).ready(function () {

        if ($('#sendinput').attr('disabled')) {
            interval = setInterval(calSendTime, 1000);
        }
        $("#logdingWap").live('click', function () {
            biz.waploadinglist(callback.waploadinglistCallBack);
        });
        $("#hotactivityloadding").live('click', function () {
            biz.hotloadding(callback.hotloaddingCallBack);
        });
        $("#logdingSearch").live('click', function () {
            biz.searchResultList(callback.searchResultListCallBack);
        });
        $("#getcode").click(function () {
            biz.checkBindPhone(callback.checkBindPhoneCallBack);
        });
        $("#bind").click(function () {
            biz.BindPhone(callback.BindPhoneCallBack);
        });
        $("#taocode").click(function () {
            biz.taoCode(callback.taoCodeCallBack);
        });
        $("#mygiftloading").click(function () {
            biz.myGiftLoading(callback.myGiftLoadingCallBack);
        });
        $("#giftReserveSubmit").click(function () {
            var reserve = $("#reserve").val();
            var aid = $("#activityid").val();
            $.post("/json/giftmarket/wap/creategiftreserve", {reserve: reserve, aid: aid}, function (data) {
                if (data.msg == '0') {
                    alert("您已经预定过该礼包");
                    return;
                }
                if (data.msg == '-11') {
                    window.location.href = "http://mp.weixin.qq.com/s?__biz=MjM5MzA3MzgwMA==&mid=200744934&idx=1&sn=e8f9df7021c8d7936ec824c8d1aea87f#rd";
                    return;
                }
                if (data.rs == '0') {
                    window.location.href = joyconfig.URL_WWW + "/giftmarket/wap";
                    return;
                }
                if (data.rs == '1') {
                    alert("谢谢预定，我们会尽快联系厂商准备礼包！");
                    return;
                }


            }, "json");
        });

        var biz = {
            waploadinglist: function (callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();

                if (parseInt(maxNum) <= parseInt(curNum)) {
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/joymeapp/activity/gift/list",
                    data: {count: 15, type: 1, pnum: curNum},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    }
                });
            }, hotloadding: function (callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();

                if (parseInt(maxNum) <= parseInt(curNum)) {
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/json/giftmarket/wap/hotactivity",
                    data: {count: 15, type: 1, pnum: curNum},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    }
                });
            }, searchResultList: function (callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();
                var cond = $("#cond").val();
                var token = $("#token").val().length == 0 ? "" : $("#token").val();
                var openid = $("#openid").val().length == 0 ? "" : $("#openid").val();

                if (parseInt(maxNum) <= parseInt(curNum)) {
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/gift/wap/jsonsearch",
                    data: {searchtext: cond, pnum: curNum, token: token, openid: openid},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    }
                });
            }, checkBindPhone: function (callback) {
                var endTime = $("#timeOut").val();
                var sl = 16 * 60 * 60 * 1000;
                endTime = new Date(endTime);
                var time = endTime.getTime() + sl;
                endTime = new Date(time);
                var now = new Date();
                if (now > endTime) {
                    alert("对不起，该礼包已经过期了哦～请看看别的礼包吧");
                    return false;
                }
                var rn = $("#resetNum").val();

                if (rn == 0) {
                    alert("对不起，号被领光了，别灰心，有空再来看看吧！");
                    return false;
                }

                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/json/giftmarket/wap/checkbindphone",
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    }
                });
            }, BindPhone: function (callback) {
                var phone = $("#phone").val();
                var verifycode = $("#verifycode").val();
                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/giftmarket/wap/verify",
                    data: {phone: phone, verifycode: verifycode},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(XMLHttpRequest.status);
                        alert(XMLHttpRequest.readyState);
                        alert(textStatus);
                    }
                });
            }, taoCode: function (callback) {
                var aid = $("#activityid").val();
                var gid = $("#gid").val();
                var openid = $("#openid").val();
                var token = $("#token").val();
                if (openid.length == 0 || token.length == 0) {
                    window.location.href = "http://mp.weixin.qq.com/s?__biz=MjM5MzA3MzgwMA==&mid=200744934&idx=1&sn=e8f9df7021c8d7936ec824c8d1aea87f#rd";
                    return;
                }

                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/giftmarket/wap/taocode",
                    data: {gid: gid, aid: aid, openid: openid, token: token},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        if (result.rs == '-99999') {
                            alert("系统维护中");
                            return;
                        }
                        if (result.status_code == '0') {
                            alert(result.msg)
                            return;
                        }
                        if (result.status_code == '1') {
                            var arr = new Array();
                            for (var i = 0; i < result.result.length; i++) {
                                arr[i] = result.result[i].snValue1;
                            }
                            window.location.href = joyconfig.URL_WWW + "/giftmarket/wap/taocoderesult?list=" + arr + "&aid=" + aid;
                        }
                        if (result.status_code == '-11') {
                            window.location.href = "http://mp.weixin.qq.com/s?__biz=MjM5MzA3MzgwMA==&mid=200744934&idx=1&sn=e8f9df7021c8d7936ec824c8d1aea87f#rd";
                            return;
                        }
                    }
                });
            }, myGiftLoading: function (callback) {
                var curNum = $("#wapCurPage").val();
                var maxNum = $("#wapMaxPage").val();
                if (parseInt(maxNum) <= parseInt(curNum)) {
                    return;
                }
                curNum = parseInt(curNum) + parseInt(1);
                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/json/giftmarket/wap/mygift",
                    data: {p: curNum},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    }
                });
            }
        }

        var callback = {
            waploadinglistCallBack: function (result) {
                var uno = $("#uno").val();
                var token = $("#token").val();
                var openid = $("#openid").val();
                var content = "";
                var exclusive = "";
                for (var index = 0; index < result.result.length; index++) {
                    var className = '';

                    if (result.result[index].news) {
                        className = "class='newest'";
                    }
                    if (result.result[index].hot) {
                        className = "class='received'";
                    }
                    var date = new Date(result.result[index].exDate);

                    // if (result.result[index].weixinExclusive == "0") {
                    content += "<a href='/giftmarket/wap/giftdetail?aid=" + result.result[index].gid + "&uno=" + uno + "&token=" + token + "&openid=" + openid + "'" + className + "  ><img src='" + result.result[index].gipic + "'><div>" +
                        " <p>" + result.result[index].title + "</p>" +
                        " <p><span>有效期：" + gshtime(date) + "</span> <span>剩余：" + result.result[index].sn + "/" + result.result[index].cn + "</span></p>" +
                        "</div></a>";
                    // }
                    // if (result.result[index].weixinExclusive == "1") {
                    //     exclusive += "<a href='/giftmarket/wap/giftdetail?aid=" + result.result[index].gid + "&uno=" + uno + "&token=" + token + "&openid=" + openid + "'" + className + "><img src='" + result.result[index].gipic + "'><div>" +
                    //             " <p>" + result.result[index].title + "</p>" +
                    //             " <p><span>有效期：" + gshtime(date) + "</span> <span>剩余：" + result.result[index].sn + "/" + result.result[index].cn + "</span></p>" +
                    //             "</div></a>";
                    // }

                }
                $("#wapgiftmarketlist").append(content);
                // $("#exclusive").before(exclusive);
                $("#wapCurPage").val(result.page.curPage);
                $("#wapMaxPage").val(result.page.maxPage);
                if (result.page.curPage >= result.page.maxPage) {
                    $("#logdingWap").css("display", "none");
                    var nowant = '<div class="nowant"> <p>木有你想要的？马上向着迷许愿吧 （＞﹏＜）</p> <a href="' + joyconfig.URL_WWW + '/giftmarket/wap/reserve">礼包许愿</a> </div>';
                    $("#wapgiftmarketlist").after(nowant);

                }
                var loading = document.getElementById('logdingWap'),
                    icon = loading.getElementsByTagName('i')[0],
                    txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '点击加载更多';
            }, hotloaddingCallBack: function (result) {

                var uno = $("#uno").val();
                var token = $("#token").val();
                var openid = $("#openid").val();
                var content = "";
                for (var index = 0; index < result.result.length; index++) {
                    content += "<a href='/giftmarket/wap/giftdetail?aid=" + result.result[index].gid + "&uno=" + uno + "&token=" + token + "&openid=" + openid + "'><img src='" + result.result[index].gipic + "'><div>" +
                        " <p>" + result.result[index].title + "</p>" +
                        " <p><span>有效期：" + result.result[index].exDate + "</span> <span>剩余：" + result.result[index].sn + "/" + result.result[index].cn + "</span></p>" +
                        "</div></a>";
                }

                $("#wapgiftmarketlist").append(content);
                $("#wapCurPage").val(result.page.curPage);
                $("#wapMaxPage").val(result.page.maxPage);
//                if (result.page.curPage >= result.page.maxPage) {
//                    $("#hotactivityloadding").css("display", "none");
                var nowant = "<div style='text-align:center; padding:10px 0'><a class='chakan-btn' href='" + joyconfig.URL_WWW + "/giftmarket/wap?openid=" + openid + "&token=" + token + "'>更多热门礼包</a></div>";
                $("#wapgiftmarketlist").after(nowant);

//                }
                var loading = document.getElementById('hotactivityloadding'),
                    icon = loading.getElementsByTagName('i')[0],
                    txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '点击加载更多';
            }, searchResultListCallBack: function (result) {
                var uno = $("#uno").val();
                var content = "";
                var token = result.result.token == null ? "" : result.result.token;
                var openid = result.result.openid == null ? "" : result.result.openid;
                for (var index = 0; index < result.result.list.length; index++) {
                    content += "<a href='/giftmarket/wap/giftdetail?aid=" + result.result.list[index].gid + "&uno=" + uno + "&token=" + token + "&openid=" + openid + "'><img src='" + result.result.list[index].gipic + "'><div>" +
                        " <p>" + result.result.list[index].title + "</p>" +
                        " <p><span>有效期：" + result.result.list[index].exDate + "</span> <span>剩余：" + result.result.list[index].sn + "/" + result.result.list[index].cn + "</span></p>" +
                        "</div></a>";
                }
                $("#wapgiftmarketlist").append(content);
                $("#wapCurPage").val(result.result.page.curPage);
                $("#wapMaxPage").val(result.result.page.maxPage);
                if (result.result.page.curPage >= result.result.page.maxPage) {
                    $("#logdingSearch").css("display", "none");
                }
                var loading = document.getElementById('logdingSearch'),
                    icon = loading.getElementsByTagName('i')[0],
                    txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '点击加载更多';

            }, checkBindPhoneCallBack: function (result) {
                var aid = $("#activityid").val();
                var gid = $("#gid").val();
                var openid = $("#openid").val();
                var token = $("#token").val();
                if (openid.length == 0 || token.length == 0) {
                    window.location.href = "http://mp.weixin.qq.com/s?__biz=MjM5MzA3MzgwMA==&mid=200744934&idx=1&sn=e8f9df7021c8d7936ec824c8d1aea87f#rd";
                    return;
                }
                if (result.msg == '-9') {
                    if (confirm("请绑定手机号，将可一键领取礼包")) {
                        window.location.href = joyconfig.URL_WWW + "/giftmarket/wap/bindophonepage?aid=" + aid + "&openid=" + openid + "&token=" + token;
                    }
                } else {
                    $.ajax({
                        type: "POST",
                        url: joyconfig.URL_WWW + "/giftmarket/wap/getcode",
                        data: {gid: gid, aid: aid, openid: openid, token: token},
                        success: function (req) {
                            var result = eval('(' + req + ')');
                            if (result.rs == '-99999') {
                                alert("系统维护中。");
                                return;
                            }
                            if (result.status_code == '0') {
                                alert(result.msg);
                                return;
                            }
                            if (result.status_code == '1') {
                                var dto = result.result[0];
//                                        window.location.href = joyconfig.URL_WWW + "/giftmarket/wap/getcoderesult?code=" + dto.snValue1;
                                $("#codevalue").html(dto.snValue1);
                                $("#hidebox").css("display", "none");
                                $("#showbox").css("display", "block");
                                document.documentElement.scrollTop = 0;
                                document.body.scrollTop = 0;
                            }
                            if (result.status_code == '-11') {
                                window.location.href = "http://mp.weixin.qq.com/s?__biz=MjM5MzA3MzgwMA==&mid=200744934&idx=1&sn=e8f9df7021c8d7936ec824c8d1aea87f#rd";
                                return;
                            }
                        }
                    });
                }
            }, BindPhoneCallBack: function (result) {

                if (result.msg == '1') {
                    var aid = $("#aid").val();
                    var openid = $("#openid").val();
                    var token = $("#token").val();
                    window.location.href = joyconfig.URL_WWW + "/giftmarket/wap/giftdetail?aid=" + aid + "&token=" + token + "&openid=" + openid;
                }
                if (result.msg == '-3') {
                    alert("验证码错误，请重新输入");
                    return;
                }
                if (result.msg == '-4') {
                    alert("验证码不能为空");
                    return;
                }
                if (result.msg == '-5') {
                    alert("手机号码不能为空");
                    return;
                }
                if (result.msg == '-6') {
                    alert("您还没有发送验证码或验证码已经过期，请重新发送");
                    return;
                }
                if (result.msg == '-7') {
                    alert("该号码已被绑定");
                    return;
                }
            }, taoCodeCallBack: function (result) {

            }, myGiftLoadingCallBack: function (result) {
                var uno = $("#uno").val();
                var content = "";
                for (var index = 0; index < result.result.length; index++) {
                    content += "<a href='/giftmarket/wap/giftdetailresult?aid=" + result.result[index].aid + "&uid=" + result.result[index].lid + "&uno=" + uno + "'><img src='" + result.result[index].gipic + "'><div>" +
                        " <p>" + result.result[index].title + "</p>" +
                        " <p><span>有效期：" + result.result[index].endTime + "</span> </p>" +
                        "</div></a>";
                }
                $("#mygiftlist").append(content);
                $("#wapCurPage").val(result.page.curPage);
                $("#wapMaxPage").val(result.page.maxPage);
                var loading = document.getElementById('mygiftloading'),
                    icon = loading.getElementsByTagName('i')[0],
                    txt = loading.getElementsByTagName('b')[0];
                icon.style.display = 'none';
                txt.innerHTML = '点击加载更多';
                if (result.page.curPage >= result.page.maxPage) {
                    $("#mygiftloading").css("display", "none");
                    $("#chakan").css("display", "block");
                }
            }
        }
    });
    function calSendTime() {
        var prefix = '重新发送(';
        var suffix = ')'
        var sendTime = $('#send').val();
        sendTime = sendTime.replace(prefix, '');
        sendTime = sendTime.replace(suffix, '');
        sendTime = parseInt(sendTime);
        sendTime--;
        if (sendTime >= 0) {
            $('#send').val('重新发送(' + sendTime + ')');
        } else {
            var phone = $("#phone").val();
            var aid = $("#aid").val();
            $('#send').val('重新发送');
            $("#send").click(function () {
                $.ajax({
                    type: "POST",
                    url: joyconfig.URL_WWW + "/giftmarket/wap/jsonbindphone",
                    data: {phone: phone, aid: "1"},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        if (result.result.rs == "1") {
                            $('#send').val('重新发送(30)');
                            $('#send').unbind("click");
                            interval = setInterval(calSendTime, 1000);
                            return;
                        }
                        if (result.result.rs == "-2") {
                            alert("今日已发送五次短信");
                            return;
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        alert(XMLHttpRequest.status);
                        alert(XMLHttpRequest.readyState);
                        alert(textStatus);
                    }
                });
            });
            $('#sendinput').attr('disabled', false);
            clearInterval(interval);
        }
    }


    function gshtime(time) {
        var year = time.getFullYear();       //年</span>
        var month = time.getMonth() + 1;  //月
        var day = time.getDate();         //日
        var hh = time.getHours();       //时
        var mm = time.getMinutes();    //分
        var ss = time.getSeconds();    //分
        var str = year + "-";
        if (month < 10) {
            str += "0";
            str += month + "-";
        } else {
            str += month + "-";
        }
        if (day < 10) {
            str += "0";
            str += day + " ";
        } else {
            str += day + " ";
        }
        if (hh < 10) {
            str += "0";
            str += hh + ":";
        } else {
            str += hh + ":";
        }
        if (mm < 10) {
            str += "0";
            str += mm + ":";
        } else {
            str += mm + ":";
        }
        if (ss < 10) {
            str += "0";
            str += ss + ".0";
        } else {
            str += ss + ".0";
        }

        return (str);
    }
});





