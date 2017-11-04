<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>一天一块苹果表</title>
    <link href="${URL_LIB}/static/theme/wap/css/lottery/common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/lottery/dial.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div id="_sharebtn_status" style="display: none;">yes</div>
<div id="_title" style="display: none;">还排队买表吗？赶紧来抽一个吧！</div>
<div id="_desc" style="display: none;">穷玩车，富玩表。玩霸大回馈，苹果表天天送，50万壕礼大家拿，速来抢！</div>
<div id="_clientpic" style="display: none;">${URL_LIB}/static/theme/wap/images/lottery/share_png.png</div>
<div id="_share_task" style="display: none;">wanba_five_month_task</div>
<div id="_share_url" style="display: none;">${short_url}</div>

<div id="wrapper">
    <!--lottery-->
    <div id="lottery" class="action">
        <div class="lotBg">
            <div class="lotCon">
                <div class="prize prize1">
                    <div class="prizebg"></div>
                </div>
                <div class="prize prize2">
                    <div class="prizebg"></div>
                </div>
                <div class="prize prize3">
                    <div class="prizebg"></div>
                </div>
                <div class="prize prize4">
                    <div class="prizebg"></div>
                </div>
                <div class="prize prize5">
                    <div class="prizebg"></div>
                </div>
                <div class="prize prize6">
                    <div class="prizebg"></div>
                </div>
            </div>
            <div class="lotTxt">
                <p class="txt1"><span>谢谢</span></p>

                <p class="txt2"><span>一等奖</span></p>

                <p class="txt3"><span>二等奖</span></p>

                <p class="txt4"><span>三等奖</span></p>

                <p class="txt5"><span>四等奖</span></p>

                <p class="txt6"><span>五等奖</span></p>
            </div>
            <div class="lotImg">
                <p class="img6"><span>精美<br/>纪念品</span></p>

                <p class="img1"><span>参与</span></p>

                <p class="img2"><img src="${URL_LIB}/static/theme/wap/images/lottery/1_dj.png" title="" alt=""/></p>

                <p class="img3"><img src="${URL_LIB}/static/theme/wap/images/lottery/2_dj.png" title="" alt=""/></p>

                <p class="img4"><span>限量游戏周边</span></p>

                <p class="img5"><span>游戏<br/>好礼</span></p>
            </div>
        </div>
        <button class="lotCenter" name="" disabled="false">抽奖</button>
    </div>
    <!--lottery==end-->
    <!--sildeBox-->
    <div class="sildeBox">
        <p>中奖名单：</p>
        <ul class="sildeUl">
            <c:if test="${logList != null && logList.size() > 0}">
                <c:forEach items="${logList}" var="userLog">
                    <li>${userLog}</li>
                </c:forEach>
            </c:if>
        </ul>
    </div>
    <!--sildeBox end-->
    <!--wp-bg-->
    <div class="wp-bg" style="display:none">
        <div class="transparency-bg btn-close"></div>
        <!--wp-box==分享-->
        <div id="wp-b1" class="wp-box wp-b1" style="display:none">
            <h3>分享一下攒人品，再来一发！</h3>

            <p>
                <span class="btn-handle btn-close">再试一次</span>
                <span class="btn-fx" name="_popup_box">分享得机会</span>
            </p>
        </div>
        <!--wp-box==end-->
        <!--wp-box==登录-->
        <div id="wp-b2" class="wp-box wp-b2" style="display:none">
            <h3>亲，登录支持一下我撒！</h3>

            <p id="b2-p"><code>3</code>秒后跳转登录</p>
        </div>
        <!--wp-box==end-->
        <!--wp-box==再试一次-->
        <div id="wp-b3" class="wp-box wp-b3" style="display:none">
            <h2>机会已用完</h2>

            <h3>马上分享，就能获得抽奖机会！</h3>

            <p>
                <span class="btn-handle btn-close">知道了</span>
                <span class="btn-fx" name="_popup_box">分享</span>
            </p>
        </div>
        <div id="wp-b5" class="wp-box wp-b5" style="display:none">
            <h2>活动须知</h2>

            <div>
                <p>1、每天启动应用可获得2次抽奖机会，仅限当日使用</p>

                <p>2、分享活动或中奖结果,将额外获得1次抽奖机会，仅限当日使用用</p>

                <p>3、中奖用户需正确填写收货地址，如不填写，奖品将回滚至奖池</p>

                <p>4、因收货地址无法核实或核实有误导致无法发放奖品的，中奖结果无效</p>

                <p>5、中奖信息核实无误的，奖品将在活动结束， 30日内进行发放</p>

                <p>6、大陆地区免物流费用，港澳台和海外的中奖用户自理运费</p>

                <p>特别提醒：</p>

                <p>所有恶意刷取中奖机会的的行为，中奖结果无效并直接取消抽奖资格</p>
            </div>
            <p>本活动最终解释权归属着迷网所有</p>
        </div>
        <!--wp-box==end-->
        <!--wp-box==保存成功-->
        <div id="wp-b6" class="wp-box wp-b6" style="display:none">
            <h3></h3>
        </div>

        <div id="wp-b7" class="wp-box wp-b1" style="display:none">
            <h3>攒攒运气，明天再来吧！</h3>

            <p>
                <span class="btn-handle btn-close">知道了</span>
                <span class="btn-fx" name="_popup_box">分享</span>
            </p>
        </div>
        <div id="wp-b8" class="wp-box wp-b3" style="display:none">
            <h2></h2>

            <h3>请到Appstore下载，参与活动，<br/>安卓和其他渠道用户敬请期待！</h3>

            <p>
                <span class="btn-handle btn-close">知道了</span>
                <span class="btn-fx" name="_popup_box">分享</span>
            </p>
        </div>
        <!--wp-box==end-->
        <!--分享-->
        <div class="popup_box" style="display:none;">
            <img src="${URL_LIB}/static/theme/wap/images/lottery/share.png" alt="">
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        var non_touch = false;

        var silde = {
            num: 1,
            len: $('.sildeUl>li').length,
            h: $('.sildeUl>li:first').height(),
            timer: null,
            int: function () {
                if (silde.len > 1) {
                    $('.sildeUl').height(silde.h * silde.len);
                    silde.timer = setInterval(function () {
                        $('.sildeUl').stop().animate({'marginTop': -silde.h * silde.num}, 500, function () {
                            $(this).find('li').slice(0, silde.num).appendTo($(this))
                            $(this).css('marginTop', 0)
                        });
                    }, 2000);
                }
            }
        };
        var choujiang = {
            int: function () {
                $(".lotCenter").on('touchstart', function (e) {
                    e.stopPropagation();
                    e.preventDefault();
                    if (non_touch) {
                        return;
                    }
                    non_touch = true;
//                    var jparam;
//                    try {
//                        jparam = _jclient.getJParam();
//                    } catch (e) {
//                    }
                    var ajaxTimeoutTest = $.ajax({
                        url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/task/taskdownload",
                        type: "POST",
                        timeout: 5000,
                        data: {JParam: '${JParam}'},
                        dataType: "json",
                        success: function (resMsg) {
                            if (resMsg.rs == '1') {
                                var award = resMsg.result;
                                if (award == null || award.lotteryAwardLevel <= 0) {

                                    if (award != null && (award.lotteryTimes == '1' || award.lotteryTimes == '2' || award.lotteryTimes == '3' || award.lotteryTimes == '4' || award.lotteryTimes == '5' || award.lotteryTimes == '6' || award.lotteryTimes == '7' ||  parseInt(award.lotteryTimes)>7)) {
                                        choujiang.fenxiang(award.lotteryTimes);
                                        non_touch = false;
                                    } else {
                                        $('#lottery').addClass('action');
                                        var deg = 3660;
                                        var style = '<style id="rotation_style">.lotBg{-webkit-animation:lotCenter 5s ease 0.2s forwards;}@-webkit-keyframes lotCenter {0%{-webkit-transform:rotate(0deg);}100%{-webkit-transform:rotate(' + deg + 'deg);}</style>';
                                        if ($("#rotation_style").length > 0) {
                                            $("#rotation_style").remove();
                                        }
                                        $(".lotBg").append(style);
                                        setTimeout(function () {
                                            $('.prize1').addClass('sel');
                                            $('#lottery').removeClass('action');
                                        }, 5000);
                                        setTimeout(function () {
                                            choujiang.fenxiang(0);
                                            $('.prize1').removeClass('sel');
                                            if ($("#rotation_style").length > 0) {
                                                $("#rotation_style").remove();
                                            }
                                            non_touch = false;
                                        }, 7000);
                                    }

                                } else {
                                    $('#lottery').addClass('action');
                                    var deg = 3600 + (360 - 60 * (award.lotteryAwardLevel - 1));
                                    var style = '<style id="rotation_style">.lotBg{-webkit-animation:lotCenter 5s ease 0.2s forwards;}@-webkit-keyframes lotCenter {0%{-webkit-transform:rotate(0deg);}100%{-webkit-transform:rotate(' + deg + 'deg);}</style>';
                                    $(".lotBg").append(style);
                                    setTimeout(function () {
                                        $('.prize' + (award.lotteryAwardLevel + 1)).addClass('sel');
                                        $('#lottery').removeClass('action');
                                    }, 5000);
                                    setTimeout(function () {
                                        $('.prize' + (award.lotteryAwardLevel + 1)).removeClass('sel');
                                        if ($("#rotation_style").length > 0) {
                                            $("#rotation_style").remove();
                                        }
                                        non_touch = false;
                                        window.location.href = "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/task/share?JParam=${JParam}&type=win&lotteryAwardId=" + award.lotteryAwardId;
                                    }, 7000);
                                }
                            } else if (resMsg.rs == '-1') {
                                non_touch = false;
                                $('#lottery').removeClass('action');
                                choujiang.login();
                            } else if (resMsg.rs == '2') {
                                $('#lottery').removeClass('action');
                                $('.wp-bg,#wp-b8').show();
                                non_touch = false;
                            } else if (resMsg.rs == '-10104') {
                                $('#lottery').removeClass('action');
                                $('#b2-p').remove();
                                $('#wp-b2').find('h3').html('当前用户不存在，请重新登录！');
                                $('.wp-bg,#wp-b2').show();
                                setTimeout(function () {
                                    $('.wp-bg,#wp-b2').hide();
                                    $('#wp-b2').find('h3').html("亲，登陆支持一下我撒！");
                                    $('#wp-b2').append('<p id="b2-p"><code>3</code>秒后跳转登录</p>');
                                    non_touch = false;
                                }, 2000);
                            } else if (resMsg.rs == '-100') {
                                $('#lottery').removeClass('action');
                                $('#b2-p').remove();
                                $('#wp-b2').find('h3').html('APPKEY错误！');
                                $('.wp-bg,#wp-b2').show();
                                setTimeout(function () {
                                    $('.wp-bg,#wp-b2').hide();
                                    $('#wp-b2').find('h3').html("亲，登陆支持一下我撒！");
                                    $('#wp-b2').append('<p id="b2-p"><code>3</code>秒后跳转登录</p>');
                                    non_touch = false;
                                }, 2000);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            if (textStatus == "timeout") {
                                ajaxTimeoutTest.abort();
                                $('#lottery').removeClass('action');
                                $('#b2-p').remove();
                                $('#wp-b2').find('h3').html('请求超时！');
                                $('.wp-bg,#wp-b2').show();
                                setTimeout(function () {
                                    $('.wp-bg,#wp-b2').hide();
                                    $('#wp-b2').find('h3').html("亲，登陆支持一下我撒！");
                                    $('#wp-b2').append('<p id="b2-p"><code>3</code>秒后跳转登录</p>');
                                    non_touch = false;
                                }, 2000);
                            }
                        }
                    });
                });
            },
            //请登录
            login: function () {
                $('#lottery').removeClass('action');
                var num = 3;
                var times = null;
                $('.wp-bg,#wp-b2').show();
                times = setInterval(function () {
                    num--;
                    $('#wp-b2').find('code').text(num);
                    if (num == 0) {
                        $('#wp-b2').find('code').text(3);
                        clearInterval(times);
                        $('.wp-bg,#wp-b2').hide();
                        try {
                            _jclient.showLogin();
                        } catch (e) {
                        }
                    }
                }, 1000);
                $('.btn-close').on('touchstart', function (e) {
                    clearInterval(times);
                    e.stopPropagation();
                    e.preventDefault();
                    $('.wp-bg,#wp-b2').hide();
                    $('#lottery').addClass('action');
                });
            },
            //分享
            fenxiang: function (times) {
                $('#lottery').removeClass('action');
                if (times == '1' || times == '2' || times == '3' || times == '4' || times == '5' || times == '6') {
                    $('.wp-bg,#wp-b3').show();
                    $('.btn-close').on('touchstart', function (e) {
                        e.stopPropagation();
                        e.preventDefault();
                        $('.wp-bg,#wp-b3').hide();
                        $('#lottery').addClass('action');
                    });
                } else if (times == '7' || parseInt(times)>7) {
                    $('.wp-bg,#wp-b7').show();
                    $('.btn-close').on('touchstart', function (e) {
                        e.stopPropagation();
                        e.preventDefault();
                        $('.wp-bg,#wp-b7').hide();
                        $('#lottery').addClass('action');
                    });
                } else {
                    $('.wp-bg,#wp-b1').show();
                    $('.btn-close').on('touchstart', function (e) {
                        e.stopPropagation();
                        e.preventDefault();
                        $('.wp-bg,#wp-b1').hide();
                        $('#lottery').addClass('action');
                    });
                }
            }
        }
        silde.int();
        choujiang.int();//中奖的位置1-6

        //分享
        $("span[name=_popup_box]").on('touchstart', function () {
            $('#lottery').removeClass('action');
            $("#wp-b1,#wp-b2,#wp-b3,#wp-b5,#wp-b6,#wp-b7,#wp-b8").hide();
            $(".wp-bg,.popup_box").show();
            $('.btn-close').on('touchstart', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(".wp-bg,.popup_box").hide();
                $('#lottery').addClass('action');
            });
        });

        $('.btn-close').on('touchstart', function (e) {
            e.stopPropagation();
            e.preventDefault();
            $('.wp-bg').hide();
            $('#lottery').addClass('action');
        });
    });
</script>
</body>


</html>