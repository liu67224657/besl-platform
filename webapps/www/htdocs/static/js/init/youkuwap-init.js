define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var joymealert = require('../common/joymealert');
    var alertOption = {
        tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };

    var userAgentInfo = navigator.userAgent.toLowerCase();
    $(document).ready(function () {
        $("#exchange-btn").click(function (e) {
            //倒计时中
            var dom = $('#p_sec_djs');
            if (dom != null && dom != undefined && dom.length > 0) {
                return false;
            }
            //如果是在端外打开
            if (userAgentInfo.indexOf('youku hd') == -1 && userAgentInfo.indexOf('youku') == -1) {
                alert("请先登录");
                return false;
            }
            var isLogin = jsconfig.isLogin;
            if (isLogin == 'false') {
                return;
            }
            if (e && e.preventDefault) {
                e.preventDefault();
            } else {
                window.event.returnValue = false;
            }

            var secid = $(this).attr('data-secid');
            var profileid = $("[name='profileid']").val();
            var aid = $("[name='aid']").val();
            var sn = $("[name='sn']").val();
            var dtopoint = $("[name='dtopoint']").val();
            var userpoint = $("[name='userpoint']").val();
            var endTime = $("[name='endTime']").val();
            var allowExchangeStatus = $("[name='allowExchangeStatus']").val();
            if (endTime == "true") {
                $('.Threebox').find("span").text("活动已过期");
                $('.Threebox').show().delay(3000).hide(0);
                return;
            }
            if (allowExchangeStatus == '-1') {
                $('.Threebox').find("span").text("您已经兑换过该商品～");
                $('.Threebox').show().delay(3000).hide(0);

                return;
            } else if (allowExchangeStatus == '-2') {
                $('.Threebox').find("span").text("今天已经兑换过该商品～");
                $('.Threebox').show().delay(3000).hide(0);
                return;
            } else if (allowExchangeStatus == '-3') {
                $('.Threebox').find("span").text("前不久已经兑换过该商品，过一会再来看看吧～");
                $('.Threebox').show().delay(3000).hide(0);
                return;
            }
            if (parseInt(sn) < parseInt(1)) {
                $('.Threebox').find("span").text("该商品被兑换光了哦～");
                $('.Threebox').show().delay(3000).hide(0);
                return;
            }
            if (parseInt(userpoint) < parseInt(dtopoint)) {
                $("#gopointwall").addClass("close");
                $(".mark-box").css("display", "block");
                return;
            }
            $("#cofirmshopdiv").addClass("close");
            $(".mark-box").css("display", "block");
        });

        $("#getgoods").click(function (e) {
            if (e && e.preventDefault) {
                e.preventDefault();
            } else {
                window.event.returnValue = false;
            }

            var goodstype = $("[name='goodstype']").val();
            if (goodstype != 1) {
                getAddress();
            } else {
                exchangeOfGoods();
            }
        });
    });
    $("#submitAddress").click(function () {
        var name = $("[name='name']").val();
        var profileId = $("[name='profileId']").val();
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
            $('.Threebox').find("span").text("请填写完整信息");
            $('.Threebox').show().delay(3000).hide(0);
            return;
        } else {
//                address = province + city + town + address;
            $.ajax({
                        type: "POST",
                        url: jsconfig.YK_DOMAIN + "/youku/api/giftmarket/modifyaddress",
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
    var secid = $('#exchange-btn').attr('data-secid');
    if (!flag) {
        flag = true;
        $.ajax({
                    type: "POST",
                    url: jsconfig.YK_DOMAIN+"/youku/api/giftmarket/exchange",
                    data: {appkey: appkey, profileid: profileId, aid: aid, gid: gid, type: 2, secid:secid},
//                async:false,
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        if (result.rs == '-99999') {
                            $('.Threebox').find("span").text("系统维护中");
                            $('.Threebox').show().delay(3000).hide(0);
                            return;
                        }
                        if (result.status_code == '0') {
                            $("#cofirmshopdiv").removeClass("close");
                            $(".mark-box").css("display", "none");
                            if (result.msg == '您的节扌…积分余额不足，请加油赚积分吧！') {
                                $("#gopointwall").addClass("close");
                                $(".mark-box").css("display", "block");
                            } else {
                                $('.Threebox').find("span").text(result.msg);
                                $('.Threebox').show().delay(3000).hide(0);
                            }

                        } else {
                            var dto = result.result[0];
                            if (dto.goodsType == 1) {
                                $("#cofirmshopdiv").removeClass("close");
                                $(".mark-box").css("display", "none");
                                $("#exchange").css("display", "block");
                                $("#wrapper").css("display", "none");

                                $("#kahao").text(dto.itemValue1);
                                if (dto.itemValue2 != '' && dto.itemValue2 != null) {
                                    $("#password").attr('style', 'visibility: visible;');
                                    $("#passwordspan").text(dto.itemValue2);
                                }
                                $('#a_to_use').attr('href', '/youku/webview/giftmarket/mygiftdetail?aid='+dto.goodsId+'&consumeorder='+dto.consumeOrder);
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

                                address = '<div class="dialog close" > <h1>下单成功</h1>' +
                                        '<p class="confirm-text2">兑换成功，我们会尽快为您发货，订单详情请到【个人中心】查询' +
                                        '<div class="dialog-btn" ><a href="javascript:cancelReserve();"  style="border-left:none;" class="gain">知道了</a>' +
                                        '</div></div>';
                                $("#wrapper").append(address);
                                var sn = $("[name='sn']").val();
                                var sn2 = parseInt(sn) - 1;
                                $("[name='sn']").val(sn2);
                                var status = $("[name='exchangeStatus']").val();
                                if (status != '1') {
                                    $("[name='allowExchangeStatus']").val(status);
                                }
                            }
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
                url: jsconfig.YK_DOMAIN+"/youku/api/giftmarket/getaddress",
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

                            var address = '<div class="dialog close" > <h1>使用默认地址</h1>' +
                                    '<p class="confirm-text2" style="text-align:left;">收货人：' + data.result.address.contact + '' +
                                    '<br/>地址:' + data.result.address.province + data.result.address.city + data.result.address.county + data.result.address.address + '' +
                                    '<br/>电话:' + data.result.address.phone + '' +
                                    '<div class="dialog-btn"><a href="javascript:oldAddress();"  class="gain">使用此地址</a><a href="javascript:writeAddress();"class="gain" >使用新地址</a></a>' +
                                    '</div></div>'
                            $("#wrapper").append(address);
                        }
                    }
                }
            });
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

function cancelReserve() {
    $(".mark-box").css("display", "none");
    $(".dialog").removeClass("close");

}



