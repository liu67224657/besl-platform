define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };

    $(document).ready(function () {
        header.noticeSearchReTopInit();

        $('.exchange-now').click(function () {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }

            var consumePoint = Number($(this).attr('data-cpoint'));
            var pointAmount = Number($(this).attr('data-pointamount'));

            if (pointAmount < consumePoint) {
                alertOption.text = '您的节扌…积分余额不足，请加油赚积分吧！';
                alertOption.title = '积分余额不足';
                joymealert.alert(alertOption);
                return false;
            }

            var goodsId = $(this).attr('data-gsid');
            var activityId = $(this).attr('data-aid');
            biz.exchange(goodsId, activityId, callback.exchangeCallback);
        });

    })

    var bizLock;
    var biz = {
        exchange: function (goodsId, activityId, callback) {
            if (bizLock) {
                return;
            }
            bizLock = true;
            $.ajax({type: "POST",
                        url: joyconfig.URL_WWW + "/json/coin/exchange",
                        data: {gsid: goodsId, aid: activityId},
                        success: function (req) {
                            var result = eval('(' + req + ')');
                            callback(result, activityId);
                        },
                        complete: function () {
                            bizLock = false;
                        }
                    });

        }
    }


    var callback = {
        exchangeCallback: function (result, activityId) {
            if (result.status_code == '-1') {
                loginBiz.maskLoginByJsonObj();
                return false;
            } else if (result.status_code == '0') {
                alertOption.text = result.msg;
                alertOption.title = '兑换失败';
            } else if (result.status_code == '1') {
                var dto = result.result[0];
                //实物
                var successText = '';
                if (dto.goodsType == 1) {
                    successText = '恭喜您成功兑换了' + dto.goodsName + '，具体使用方法请查看系统通知<br/>';
                    successText += dto.itemName1 + ':' + dto.itemValue1 + '<br/>';
                    if (dto.itemName2 != null && dto.itemValue2 != null) {
                        successText += ' ' + dto.itemName2 + ':' + dto.itemValue2 + '<br/>';
                    }
                } else {
                    successText = '恭喜您成功兑换了' + dto.goodsName + '<br/>我们将为您发送一条系统通知，请按照系统通知的内容，尽快留下您的联系信息，我们将及时为您发出礼品！';
                }

                alertOption.text = '<div class="publicuse clearfix">' +
                        '<img src="' + joyconfig.URL_LIB + '/static/img/alert-success.png" style="float:left; margin:16px 10px 16px 16px;">' +
                        '<p style="text-align:left; float:left; line-height:24px; padding-top:15px;word-wrap:break-word; width:310px" id="p_alert_text">' + successText + '</p>' +
                        '</div>'

                alertOption.title = '兑换成功';
                alertOption.width = 464;
                var goodsId = $(this).attr('data-gsid');
                alertOption.callbackFunction = function () {
                    window.location.href = joyconfig.URL_WWW + '/coin/' + activityId;
                }
            }

            joymealert.alert(alertOption);
        }
    }
});





