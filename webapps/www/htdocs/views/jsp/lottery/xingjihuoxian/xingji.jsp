<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>玩《星际火线》，拿好礼</title>
    <meta name="Description" content=""/>
    <meta name="Keywords" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <meta name="format-detection" content="telephone=no"/>
    <link rel="stylesheet" href="${URL_LIB}/static/css/activity/xinjihuoxian/style.css?2016062411">
    <script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function (e) {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
            document.getElementById('wrap').style.zoom = e.target.activeElement.clientWidth / 375;
        });

    </script>
    <script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
<!--
<input name="openid" id="openid" size="40" placeholder="自定义用户id">-->
<div id="wrap">
    <div class="main">
        <div class="dial">
            <div class="my">我的奖品</div>
            <div class="dial_box"></div>
            <div class="dial_btn"></div>
        </div>
        <div class="prize">
            <ul class="prize_box">
                <li>恭喜<span>lo****17  抽中了</span>激活码</li>
                <li>恭喜<span>zx****38 抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>lu****e2乖 抽中了</span>激活码</li>
                <li>恭喜<span>lei****564  抽中了</span>激活码</li>
                <li>恭喜<span>nic****123  抽中了</span>话费充值卡50元</li>
                <li>恭喜<span>wan****815  抽中了</span>激活码</li>
                <li>恭喜<span>mi1****757抽中了</span>激活码</li>
                <li>恭喜<span>zj****xue  抽中了</span>激活码</li>
                <li>恭喜<span>Ga****uo 抽中了</span>京东卡100元</li>
                <li>恭喜<span>Kk****888 抽中了</span>激活码</li>
                <li>恭喜<span>yi****59 抽中了</span>激活码</li>
                <li>恭喜<span>GTA**464 抽中了</span>激活码</li>
                <li>恭喜<span>Zin****.0 抽中了</span>激活码</li>
                <li>恭喜<span>angl****512抽中了</span>激活码</li>
            </ul>
        </div>
        <div class="bg"><img src="http://api.joyme.com/static/img/activity/xinjihuoxian/bg.jpg" alt=""></div>
    </div>
    <div class="pop_box" style="display:none">
        <div class="pop pop1" style="display:none">
            <p></p>
            <cite class="sure_btn"></cite>
        </div>
        <div class="pop2" style="display:none">
            <p>
                <input type="text" class="iphone">
                <input type="text" class="qq">
                <input type="text" class="address">
            </p>
            <cite class="submit_btn"></cite>
        </div>
        <div class="pop pop3" style="display:none">
            <p>抱歉！您已经抽取过该礼包了！请继续关注“公众号”，更多福利等你来拿！</p>
            <cite class="close_btn"></cite>

        </div>
        <div class="my_box" style="display:none;position:absolute;max-height:300px; overflow-y:auto">
            <cite class="close_btn"></cite>
            <!-- <span>活动结束后，获奖名单将在公众号统一公布，请扫描页面下方二维码关注哦！</span> -->
            <div id="myLottery"></div>
        </div>
    </div>
    <div class="tip" style="display:none;">
        <span>请搜索“xjhx666”，关注星际火线官方微信公众号，参加活动! </span>
    </div>
    <div class="hint" style="display:none;"></div>
</div>
<script language="javascript">
var openId = "";
$(document).ready(function () {
    //设置openid
    openId = "${openid}";


//        $('#openid').bind('input propertychange', function () {
//            openId = $("#openid").val();
//        });

    var isWeiXin = {
        int: function () {
            var ua = window.navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == 'micromessenger') {
                return true;
            } else {
                return false;
            }
        }
    }
    if (!isWeiXin.int()) {
        $('.tip').show();
    }
    var silde = {
        num: 1,
        len: $('.prize_box>li').length,
        h: $('.prize_box>li:first').height(),
        timer: null,
        int: function () {
            $('.prize_box').height(silde.h * silde.len);
            silde.timer = setInterval(function () {
                $('.prize_box').stop().animate({'marginTop': -silde.h * silde.num}, 500, function () {
                    $(this).find('li').slice(0, silde.num).appendTo($(this))
                    $(this).css('marginTop', 0)
                });
            }, 2000);
        }
    };
    silde.int();
    var choujiang = {
        int: function () {
            var btn = $('.dial_btn');
            var box = $('.dial_box');
            var my_btn = $('.my');
            var Pnum = 6;
            var timer = null;
            choujiang.iTarget = true;
            btn.on('click', function (e) {
                e.stopPropagation();
                e.preventDefault();
                if (choujiang.iTarget) {
                    //抽奖
                    choujiang.iTarget = false;
                    $.ajax({
                        url: "http://api.joyme.com/lottery/xingji/action",
                        type: "POST",
                        timeout: 5000,
                        data: {openid: openId},
                        dataType: "json",
                        success: function (resMsg) {
                            //进行了抽奖
                            if (resMsg.rs == 1) {
                                var classid = ""; //几等奖
                                var jh_num = "";//激活码
                                //未中奖classid=4未中奖
                                if (typeof(resMsg.reuslt) == "undefined") {
                                    classid = 4;
                                } else {
                                    classid = resMsg.reuslt.level;
                                    if (classid == 2) {
                                        jh_num = resMsg.reuslt.value1;//激活码
                                    }
                                }

                                if (classid == -1000) {
                                    choujiang.iTarget = true;
                                    choujiang.hintShow('抱歉！您已经抽取过该礼包了！请关注"星际火线"公众号，更多福利等你来拿！');
                                    return;
                                } else if (classid == -2000) {
                                    choujiang.iTarget = true;
                                    choujiang.hintShow('活动已结束，谢谢参与');
                                    return;
                                }


                                var meannum = 360 / Pnum;
                                if (classid == 1) {
                                    classid == 7;
                                }
                                var angle = parseInt(360 * 4 + (classid - 1) * meannum);
                                if (Pnum < classid) {
                                    choujiang.dialogShow("奖品不存在")
                                }
                                box.addClass('rotate');
                                box.css({
                                    '-webkit-transform': 'rotate(' + angle + 'deg)',
                                    'transform': 'rotate(' + angle + 'deg)'
                                });
                                clearTimeout(timer);
                                timer = setTimeout(function () {
                                    choujiang.dialogShow(classid, jh_num);
                                    box.removeClass('rotate');
                                    box.css({
                                        '-webkit-transform': 'rotate(0deg)',
                                        'transform': 'rotate(0deg)'
                                    });
                                }, 3000)
                                choujiang.iTarget = true;
                            } else {
                                choujiang.iTarget = true;
                                choujiang.hintShow('请在微信中进行此抽奖~');
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            choujiang.iTarget = true;
                            if (textStatus == "timeout") {
                                choujiang.hintShow('网络有点问题，请再试一遍哦~');
                                box.removeClass('rotate');
                                box.css({'-webkit-transform': 'rotate(0deg)', 'transform': 'rotate(0deg)'});
                            }
                            return;
                        }
                    });
                }
            });
            my_btn.on('click', function () {
                //获取抽奖信息
                $.ajax({
                    url: "http://api.joyme.com/lottery/xingji/getlottery",
                    type: "POST",
                    timeout: 5000,
                    data: {openid: openId},
                    dataType: "json",
                    success: function (resMsg) {
                        if (typeof(resMsg.result) != undefined && resMsg.rs == 1) {
                            var prize = [
                                ["恭喜您！获得iphone6s", true],
                                ["恭喜您！获得激活码：<span>111111</span>", false],
                                ["恭喜您！获得京东卡100元", true],
                                ["谢谢参与", false],
                                ["恭喜您！获得星际火线鼠标垫", true],
                                ["恭喜您！获得话费充值卡50元", true]
                            ];
                            if (resMsg.result.length > 0) {
                                var str = '';
                                var index = 0;
                                for (var i = 0; i < resMsg.result.length; i++) {
                                    var lev = resMsg.result[i].lotteryAwardLevel;
                                    if (lev == 4 || lev == 0 || lev == 1) {
                                        continue;
                                    }
                                    if (index == 0) {
                                        str += '<p style="color:yellow;">活动结束后，获奖名单将在公众号统一公布，请扫描页面下方二维码关注哦！</p> ';
                                    }
                                    index++;

                                    if (lev == 2) {
                                        if (resMsg.result[i].lottery_code == null || resMsg.result[i].lottery_code == "") {
                                            continue;
                                        }
                                        str += '<span>恭喜您！获得激活码：<b>' + resMsg.result[i].lottery_code + '</b>,此激活码只适用于激活微信账号；请关注“星际火线”公众号，更多福利等你来拿！</span>';
                                    } else {
                                        str += '<span>' + prize[lev - 1][0] + '；您的奖品我们会在活动结束后一周内发送给您，请关注“星际火线”公众号，更多福利等你来拿！</span>';
                                    }
                                }
                                if (str == '') {
                                    str = "<span style='text-align:center;color:yellow;'>暂无奖品，活动结束后，获奖名单将在公众号统一公布，请扫描页面下方二维码关注哦！</span>";
                                }
                                $('#myLottery').html(str);
                            } else {
                                var str = "<span style='text-align:center;color:yellow;'>暂无奖品，活动结束后，获奖名单将在公众号统一公布，请扫描页面下方二维码关注哦！</span>";
                                $('#myLottery').html(str);
                            }

                            $('.my_box,.pop_box').show();
                        } else {
                            choujiang.hintShow('请在微信中查看~');
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        if (textStatus == "timeout") {
                            choujiang.hintShow('网络有点问题，请再试一遍哦~');
                        }
                        return;
                    }
                });

            });
            $('.close_btn').on('click', function () {
                $('.pop3,.pop_box,.my_box').hide();
            });
        },
        dialogShow: function (classid, jh_num) {
            var prize = [
                ["恭喜您！获得iphone6s", true],
                ["恭喜您！获得激活码：<span>" + jh_num + "</span>", false],
                ["恭喜您！获得京东卡100元", true],
                ["谢谢参与", false],
                ["恭喜您！获得星际火线鼠标垫", true],
                ["恭喜您！获得话费充值卡50元", true]
            ];
            if (classid == 2) {
                if(jh_num==null||jh_num==""){
                    $('.pop1').children('p').html(prize[3][0] + '！请继续关注“星际火线”公众号，更多福利等你来拿！');

                }else{
                    $('.pop1').children('p').html(prize[classid - 1][0] + '！此激活码只适用于激活微信账号，请下载游戏后在游戏内激活。详情请关注”星际火线“公众号');
                }

            } else {
                $('.pop1').children('p').html(prize[classid - 1][0] + '！请继续关注“星际火线”公众号，更多福利等你来拿！');

            }
            $('.pop_box,.pop1').show();
            $('.sure_btn').unbind('click').on('click', function () {
                if (prize[classid - 1][1]) {
                    $('.pop1').hide();
                    $('.pop2').show();
                } else {
                    $('.pop1,.pop_box').hide();
                }
            });
            $('.submit_btn').unbind('click').on('click', function () {
                var iphone_num = $('.iphone').val();
                var qq_num = $('.qq').val();
                var address_num = $('.address').val();
                var uid = openId;
                iphone_num = iphone_num.replace(/(\s+)/g, "");
                var phoneReg = /^(1+(([-]*)|([+]*))[0-9]+)+$/;
                if (iphone_num.length != 11 || !phoneReg.test(iphone_num)) {
                    choujiang.hintShow('您的手机号输入有误，请重新输入！');
                    return false;
                }
                if (qq_num.trim() == "") {
                    choujiang.hintShow('微信号不能为空');
                    return false;
                }
                if (address_num.trim() == "") {
                    choujiang.hintShow('收货地址不能为空');
                    return false;
                }

                //修改用户地址
                $.ajax({
                    url: "http://api.joyme.com/lottery/xingji/address",
                    type: "POST",
                    timeout: 5000,
                    data: {uid: uid, phone: iphone_num, address: address_num, qq: qq_num},
                    dataType: "json",
                    success: function (resMsg) {
                        choujiang.hintShow('提交成功');
                        $('.pop2,.pop_box').hide();
                        $('.iphone,.qq,.address').val('');
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        if (textStatus == "timeout") {
                            choujiang.hintShow('网络有点问题，请再试一遍哦~');
                        } else {
                            choujiang.hintShow('提交失败，请再试一遍哦~');
                        }
                        return;
                    }
                });
            })
        }, hintShow: function (text) {
            var $this = $('.hint');
            var timer = null;
            $this.text(text);
            $this.show();
            clearTimeout(timer);
            timer = setTimeout(function () {
                $this.hide();
            }, 2000)
        }
    };
    // choujiang.hintShow('您的手机号输入有误，请重新输入！');
    choujiang.int();//1iphone6s0°|360° 2激活码60°  3京东卡120°  4谢谢参与180°  5现金红包10元240° 6话费充值300°

    wx.config({
        debug: false,
        appId: '${appId}', // 必填，公众号的唯一标识
        timestamp: '${timestamp}', // 必填，生成签名的时间戳
        nonceStr: '${nonceStr}', // 必填，生成签名的随机串
        signature: '${signature}',// 必填，签名，见附录1
        jsApiList: [
            'onMenuShareTimeline',
            'onMenuShareAppMessage',
            'onMenuShareQQ',
            'onMenuShareQZone',
            'hideMenuItems'
        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    function _share() {
        $.ajax({
            url: "http://api.joyme.com/lottery/xingji/share",
            type: "POST",
            timeout: 5000,
            data: {openid: openId},
            dataType: "json",
            success: function (resMsg) {
                choujiang.hintShow('分享成功');
                $('.pop2,.pop_box').hide();
                $('.iphone,.qq,.address').val('');
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (textStatus == "timeout") {
                    choujiang.hintShow('网络有点问题，请再试一遍哦~');
                } else {
                    choujiang.hintShow('分享失败，请再试一遍哦~');
                }
                return;
            }
        });
    }

    wx.ready(function () {
        //分享到朋友圈
        wx.onMenuShareTimeline({
            title: '《星际火线》精英封测开启，豪礼任性送',
            link: 'http://t.cn/R5pK3dX',
            imgUrl: 'http://api.joyme.com/static/img/activity/xinjihuoxian/dial.png',
            success: function (res) {
                _share()
            },
            cancel: function (res) {
                choujiang.hintShow('取消分享');
            },
            fail: function (res) {
                choujiang.hintShow('分享失败');
            }
        });

        wx.onMenuShareAppMessage({
            title: '《星际火线》精英封测开启，豪礼任性送',
            desc: '星际火线限量封测送好礼，激活码、iPhone6s送不停！',
            link: 'http://t.cn/R5pK3dX',
            imgUrl: 'http://api.joyme.com/static/img/activity/xinjihuoxian/dial.png',
            success: function (res) {
                _share()
            },
            cancel: function (res) {
                choujiang.hintShow('取消分享');
            },
            fail: function (res) {
                choujiang.hintShow('分享失败');
            }
        });

        //分享到QQ
        wx.onMenuShareQQ({
            title: '《星际火线》精英封测开启，豪礼任性送',
            desc: '星际火线限量封测送好礼，激活码、iPhone6s送不停！',
            link: 'http://api.joyme.com/lottery/xingji/sharepage',
            imgUrl: 'http://api.joyme.com/static/img/activity/xinjihuoxian/dial.png',
            success: function (res) {
                _share()
            },
            cancel: function (res) {
                choujiang.hintShow('取消分享');
            },
            fail: function (res) {
                choujiang.hintShow('分享失败');
            }
        });

        //分享到QQ空间
        wx.onMenuShareQZone({
            title: '《星际火线》精英封测开启，豪礼任性送',
            desc: '星际火线限量封测送好礼，激活码、iPhone6s送不停！',
            link: 'http://api.joyme.com/lottery/xingji/sharepage',
            imgUrl: 'http://api.joyme.com/static/img/activity/xinjihuoxian/dial.png',
            success: function (res) {
                _share()
            },
            cancel: function (res) {
                choujiang.hintShow('取消分享');
            },
            fail: function (res) {
                choujiang.hintShow('分享失败');
            }
        });

        //隐藏菜单
        wx.hideMenuItems({
            menuList: [
                "menuItem:share:weiboApp"
            ]
        });
    });

});
</script>
</body>
</html>
