define(function (require, exports, module) {
        var $ = require('../common/jquery-1.5.2');
        var header = require('../page/header');
        var loginBiz = require('../biz/login-biz');
        var common = require('../common/common');
        var joymealert = require('../common/joymealert');
        var jmpopup = require('../common/jmpopup');
        var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
            width: 400
        };
        var aid = $('#aid').val();
        $(document).ready(function () {
            header.noticeSearchReTopInit();
            $('#getCode').click(function () {
                var goodsId = $("#gid").val();
                biz.getcode(goodsId, aid, callback.getcodeCallback);
            });

            $("#taoCode").click(function () {
                var goodsId = $("#gid").val();
                biz.taocode(goodsId, aid, callback.taocodeCallback);
            });

            $("#taoCode2").click(function () {
                var goodsId = $("#gid").val();
                biz.taocode(goodsId, aid, callback.taocodeCallback);
            });
            $("#copy-button").click(function () {

                biz.clicpGetCode("");
            });

            $("#getCodeSlideUp").click(function () {
                $("#layer").slideToggle();
            });
            $("#taoCodeSlideUp").click(function () {
                $("#layer2").slideToggle();
            });

            $('#phone-button').click(function () {
                var code = $('#codeValue').text();
                var gid = $("#gid").val();
                var lid = $(this).attr("data-lid");
                biz.sendShortMessage(code, gid, aid, lid, callback.sendShortMessageCallback)
            });

        });
        //给每个复制按钮绑定事件
        function bindCopyButton() {
            var size = $(".spanSize").size();
            for (var i = 0; i < size; i++) {
                $("#copyButuon" + i).bind("click", {id: i}, copyCode);
            }
        }

        function copyCode(evnt) {
            biz.clicpGetCode(evnt.data.id);
        }


        //淘号倒计时
        function codeWaitTime() {
            $("#taoCode").unbind("click");
            $("#taoCode2").unbind("click");

            var i = 15;
            var intervalId = window.setInterval(function () {
                $("#taoCodeSetTime").text(i + "秒钟后才可再次淘号哦");
                i = i - 1;
                if (i < 0) {
                    window.clearInterval(intervalId);
                    $("#taoCodeSetTime").text("您可以重新淘号了哦～");
                    i = 15;
                    $("#taoCode").click(function () {
                        var goodsId = $("#gid").val();
                        biz.taocode(goodsId, aid, callback.taocodeCallback);
                    });
                    $("#taoCode2").click(function () {
                        var goodsId = $("#gid").val();
                        biz.taocode(goodsId, aid, callback.taocodeCallback);
                    });
                }

            }, 1000);
        }

        var bool = false;
        var bool2 = true;
        var biz = {
            getcode: function (goodsId, aid, callback) {

                var endTime = $("#timeOut").val();
                var sl = 16 * 60 * 60 * 1000;
                endTime = new Date(endTime);
                var time = endTime.getTime() + sl;
                endTime = new Date(time);
                var now = new Date();
                if (now > endTime) {
                    alertOption.text = '对不起，该活动已经过期了哦～请看看别的活动吧';
                    alertOption.title = '过期了╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                    return false;
                }
                rn = $("#resetNum").val();

                if (rn == 0) {
                    alertOption.text = '对不起，号被领光了，别灰心，有空再来看看吧！';
                    alertOption.title = '领光了╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                    return false;
                }
                if (bool) {
                    alertOption.text = '不要太着急，两次领号之间不能低于15秒哦！';
                    alertOption.title = '稍等片刻╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                    return;
                }

                if (bool2) {
                    $.ajax({type: "POST",
                        url: "/json/gift/getcode",
                        data: {gsid: goodsId, aid: aid},
                        success: function (req) {
                            var result = eval('(' + req + ')');
                            callback(result);
                        }
                    });
                }
                var i = 15;
                var intervalId = window.setInterval(function () {
                    i = i - 1;
                    if (i < 0) {
                        window.clearInterval(intervalId);
                        i = 15;
                        bool = false;
                        bool2 = true;
                    }
                }, 1000);

            },
            taocode: function (goodsId, aid, callback) {
                var endTime = $("#timeOut").val();
                var sl = 16 * 60 * 60 * 1000;
                endTime = new Date(endTime);
                var time = endTime.getTime() + sl;
                endTime = new Date(time);
                var now = new Date();
                if (now > endTime) {
                    alertOption.text = '对不起，该活动已经过期了哦～请看看别的活动吧';
                    alertOption.title = '过期了╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                    return false;
                }
                bizLock = true;
                $.ajax({type: "POST",
                    url: "/json/gift/taoCode",
                    data: {gsid: goodsId, aid: aid},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    }
                });
            },
            clicpGetCode: function (id) {
                var _this = biz;
                var inviteLink = $('#codeValue' + id);
                if (common.clipToClipboard(inviteLink.text())) {
                    var codeValue = inviteLink.text();
                    inviteLink.text('复制成功，请赶快去游戏中使用吧～～');
                    setTimeout(function () {
                        _this.recoverInviteLink(id, codeValue);
                    }, 2500);
                } else {
                    joymealert.alert({text: '您的浏览器不支持粘贴，请手动复制，辛苦啦～'});
//                inviteLink[0].select();
                }
            },
            recoverInviteLink: function (id, codeValue) {
                var inputInviteLink = $('#codeValue' + id);
//            var linkid = inputInviteLink.attr("data-id");
                inputInviteLink.text(codeValue);
            },
            sendShortMessage: function (code, gid, aid, lid, callback) {
                $.ajax({type: "POST",
                    url: "/json/gift/sendmobile",
                    data: {code: code, gid: gid, aid: aid, lid: lid},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    }
                });
            }

        }

        function loginDiv() {
            $('.login-box').show();
            $('.mask-box').show();
//        var aid = $("#aid").val();
//        var jmpopupConfig = {
//            pointerFlag : false,
//            pointdir : 'up',
//            tipLayer : true,
//            containTitle : true,
//            containFoot : true,
//            offset:'center',
//            offsetlocation:[-1,0],
//            forclosed:true,
//            popwidth:401,
//            addClass:false,
//            className:"",
//            popscroll:true,
//            allowmultiple:true,
//            isremovepop:true,
//            isfocus:true
//            //showFunction:option.showFunction
//        }
//        var htmlObj = new Object();
//        htmlObj['id'] = "joymeconfirm_";
//        htmlObj['html'] = '<div class="pop" id="joymealert_" style="position: absolute; z-index: 100; width: 400px; z-index:16001">' +
//                '' +
//                '<div class="bd clearfix">' +
//                '<div class="sns-login"><p>您必须用以下社交网络登录才能参加领卡</p><div><a href="http://passport.' + joyconfig.DOMAIN + '/auth/thirdapi/qq/bind?rurl=' + joyconfig.URL_WWW + '/gift/' + aid + '" class="sns-qq"></a><a href="http://passport.' + joyconfig.DOMAIN + '/auth/thirdapi/sinaweibo/bind?rurl=' + joyconfig.URL_WWW + '/gift/' + aid + '" class="sns-weibo"></a></div>' +
//                '<dl><dt>注意：</dt><dd>如果您的QQ号码和微博账号没有绑定过邮箱账号，此次登录将会注册一个新账号。</dd></dl></div></div><div class="hd"><a href="' + joyconfig.URL_WWW + '/profile/customize/bind" class="fr">老用户去绑定社交网络&gt;&gt;</a></div></div>';
//        htmlObj['title'] = '<em>社交网络登录</em>';
//        htmlObj['input'] = '';
//        jmpopup.popupInit(jmpopupConfig, htmlObj);
        }

        var callback = {
            getcodeCallback: function (result) {
                if (result.rs == "-99999") {
                    alertOption.text = "系统正在维护中。";
                    alertOption.title = "维护中";
                    joymealert.alert(alertOption);
                    return false;
                }
                if (result.status_code == '0') {
                    alertOption.text = result.msg;
                    alertOption.title = '领取失败╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                    return false;
                } else if (result.status_code == "-1") {
                    loginDiv();
                } else if (result.status_code == '1') {
                    bool = true;
                    bool2 = false;
                    var dto = result.result[0];
                    $("#codeTitle").text(dto.goodsName);
                    $("#codeValue").text(dto.snValue1);
                    $("#phone-button").attr("data-lid", dto.userExchangeLogId);
                    $("#getcodeDiv").css("display", "block");
                    $("#taocodeDiv").css("display", "none");

                    var rn = $("#resetNum2").text();
                    var cn = $("#countNum").text();

                    $("#resetNum2").text(rn - 1);
                    $("#resetNum").text(rn - 1)
                    var count = ((rn - 1) / cn * 100);
                    count = count + "%";
                    $("#jdtStyle").css("width", count);


                } else if (result.status_code == '-2') {
                    loginDiv();
                } else if (result.status_code == '-4') {
                    alertOption.text = result.msg;
                    alertOption.title = '领取失败╮(╯_╰)╭';
                    joymealert.alert(alertOption);

                } else if (result.status_code == '-5') {
                    alertOption.text = result.msg;
                    alertOption.title = '领取失败╮(╯_╰)╭';
                    joymealert.alert(alertOption);

                } else if (result.status_code == '-6') {
                    alertOption.text = result.msg;
                    alertOption.title = '领取失败╮(╯_╰)╭';
                    joymealert.alert(alertOption);

                } else if (result.status_code == '-7') {
                    alertOption.text = '对不起，该活动已经过期了哦～请看看别的活动吧';
                    alertOption.title = '过期了╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                } else if (result.status_code == '-8') {
                    alertOption.text = result.msg;
                    alertOption.title = '领取失败╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                } else if (result.status_code = "-9") {
                    alertOption.title = '提示信息';
                    alertOption.text = '您需要绑定手机号才可以领取礼包，是否跳转到绑定页面？';
                    alertOption.submitButtonText = "是";
                    alertOption.cancelButtonText = "否";
                    alertOption.submitFunction = function bind() {
                        window.location = "/profile/mobile/gopage?reurl=" + encodeURIComponent(window.location.href);

                    };
                    joymealert.confirm(alertOption);
                }

            },
            taocodeCallback: function (result) {
                if (result.rs == "-99999") {
                    alertOption.text = "系统正在维护中。";
                    alertOption.title = "维护中";
                    joymealert.alert(alertOption);
                    return false;
                }
                if (result.status_code == '0') {
                    alertOption.text = result.msg;
                    alertOption.title = '淘号失败╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                    return false;
                } else if (result.status_code == '1') {
                    var dto = result.result;
                    $("#item-ul").remove();
                    var ul = $("<ul class='fn-clear' id='item-ul'></ul>");
                    for (var i = 0; i < dto.length; i++) {
                        var li = $("<li class='spanSize'><code id='codeValue" + i + "'>" + dto[i].snValue1 + "</code><cite id='copyButuon" + i + "'>复制</cite></li>");
                        $(ul).append(li);
                    }
                    $(".taoagain").before(ul);
                    $("#taocodeDiv").css("display", "block");
                    $("#getcodeDiv").css("display", "none");

                    bindCopyButton();
                    codeWaitTime();
                } else if (result.status_code == "-1") {
                    loginDiv();
                    return false;
                } else if (result.status_code == "-2") {

                    loginDiv();
                    //joymealert.showsubBin(alertOption);
                    return false;
                } else if (result.status_code == '-7') {
                    alertOption.text = '对不起，该活动已经过期了哦～请看看别的活动吧';
                    alertOption.title = '过期了╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                }

            },
            sendShortMessageCallback: function (result) {
                if (result.status_code == '0') {
                    alertOption.text = result.msg;
                    alertOption.title = '发送失败';
                    joymealert.alert(alertOption);
                    return false;
                } else if (result.status_code == '1') {
                    alertOption.text = result.msg;
                    alertOption.title = '发送成功';
                    joymealert.alert(alertOption);
                    return false;
                }
            }
        }
    }
);








