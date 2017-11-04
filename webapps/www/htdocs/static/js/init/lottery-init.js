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

        $("#Lottery-btn").click(function () {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
            biz.lottery(callback.lotteryCallBack);
        });

        var bizLock;
        var biz = {
            lottery: function (callback) {
                if (bizLock) {
                    return;
                }
                bizLock = true;
                $.ajax({type: "POST",
                    url: joyconfig.URL_WWW + "/huodong/chinajoy2014conference/jsonlottery",
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        callback(result);
                    },
                    complete: function () {
                        bizLock = false;
                    }
                });
            }
        }

        var callback = {
            lotteryCallBack: function (result) {
                if (result.rs == '0') {
                    alertOption.text = result.msg;
                    alertOption.title = '抽奖失败╮(╯_╰)╭';
                    joymealert.alert(alertOption);
                    return false;
                } else if (result.rs == "-1") {
                    loginDiv();
                } else if (result.rs == '1') {
                    var content = "";
                    if (result.result == null || result.result.length == 0) {
                        alertOption.text = '<li><p align="center">很遗憾，您没能中奖，敬请关注着迷更多精彩内容~</p></li>';
                        alertOption.title = '未中奖╮(╯_╰)╭';
                        joymealert.alert(alertOption);
                        return false;
                    } else {
                        var awardEntry = result.result;
                        if (awardEntry.award == null) {
                            alertOption.text = '<li><p align="center">很遗憾，您没能中奖，敬请关注着迷更多精彩内容~</p></li>';
                            alertOption.title = '未中奖╮(╯_╰)╭';
                            joymealert.alert(alertOption);
                            return false;
                        } else {
                            var levelHtml = '';
                            if (awardEntry.award.lotteryAwardLevel == 1) {
                                levelHtml = '一等奖';
                            } else if (awardEntry.award.lotteryAwardLevel == 2) {
                                levelHtml = '二等奖';
                            } else if (awardEntry.award.lotteryAwardLevel == 3) {
                                levelHtml = '三等奖';
                            } else if (awardEntry.award.lotteryAwardLevel == 4) {
                                levelHtml = '四等奖';
                            } else if (awardEntry.award.lotteryAwardLevel == 5) {
                                levelHtml = '参与奖';
                            }

                            var messageHtml = '<p>前往&nbsp;<a href="' + joyconfig.URL_WWW + '/message/notice/list">消息中心</a>&nbsp;查看中奖记录</p>';
                            if (awardEntry.award.lotteryAwardType.code == 0) {
                                if (awardEntry.awardItem == null) {
                                    alertOption.text = '<li><p align="center">很遗憾，您没能中奖，敬请关注着迷更多精彩内容~</p></li>';
                                    alertOption.title = '未中奖╮(╯_╰)╭';
                                    joymealert.alert(alertOption);
                                    return false;
                                } else {
                                    alertOption.text = '<li><p align="center">恭喜您，获得了&nbsp;' +
                                        '<span style="color: #ff0000;">' +
                                        levelHtml +
                                        '</span>' +
                                        '&nbsp;' + awardEntry.award.lotteryAwardName + '</p>' +
                                        '<p>兑换码：' + awardEntry.awardItem.value1 + '</p>' +
                                        '<p>请您保存好兑换码，按照活动说明兑换奖品</p>' +
                                        messageHtml +
                                        '</li>';
                                    alertOption.title = '中奖啦o(∩_∩)o ';
                                    joymealert.alert(alertOption);
                                    return false;
                                }
                            } else {
                                alertOption.text = '<li><p align="center">恭喜您，获得了&nbsp;' +
                                    '<span style="color: #ff0000;">' +
                                    levelHtml +
                                    '</span>' +
                                    '&nbsp;' + awardEntry.award.lotteryAwardName + '</p>' +
                                    '<p>请您按照活动说明，联系相关人员，兑换奖品</p>' +
                                    messageHtml +
                                    '</li>';
                                alertOption.title = '中奖啦o(∩_∩)o ';
                                joymealert.alert(alertOption);
                                return false;
                            }
                        }
                    }
                }
            }
        }
    });
});





