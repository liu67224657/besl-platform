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
    <title>中奖啦</title>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <link href="${URL_LIB}/static/theme/wap/css/lottery/common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/lottery/dial.css" rel="stylesheet" type="text/css">
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
<div id="_title" style="display: none;">嗯哼，我是这样抽到${award.lotteryAwardName}的！</div>
<div id="_desc" style="display: none;">穷玩车，富玩表。玩霸大回馈，苹果表天天送，50万壕礼大家拿，速来抢！</div>
<div id="_clientpic" style="display: none;">${URL_LIB}/static/theme/wap/images/lottery/share_png.png</div>
<div id="_share_task" style="display: none;">wanba_five_month_task</div>
<div id="_share_url" style="display: none;">${short_url}</div>

<div id="wrapper" class="wrap">
    <div class="Prizebox">
        <div class="Prize1" style="display:none;"><img src="${URL_LIB}/static/theme/wap/images/lottery/Prize1.png"
                                                       alt=""></div>
        <div class="Prize2" style="display:none;"><img src="${URL_LIB}/static/theme/wap/images/lottery/Prize2.png"
                                                       alt=""></div>
        <div class="Prize3" style="display:none;"><img src="${URL_LIB}/static/theme/wap/images/lottery/Prize3.png"
                                                       alt=""></div>
        <div class="Prizeimg">
            <p id="awardname"></p>

            <h1 id="_lottery">马上领奖</h1>

            <h2 id="_popup_box">马上显摆 再抽一次</h2>
            <span><a href="javascript:;" id="huodong">活动须知</a></span>
        </div>
    </div>
    <div id="topBar">
        <c:if test="${gamedb!=null}">
            <div class="ad_box">
                <span class="logo"><img src="${gamedb.gameIcon}" alt="${gamedb.gameName}"
                                        title="${gamedb.gameName}"></span>

                <div class="tit">
                    <em>${gamedb.gameName}</em>
                    <span>${gamedb.recommendReason}</span>
                </div>
                <div class="dw-btn" id="dw-btn"><a class="icon-phone" href="${downLoadUrl}"
                                                   title="${gamedb.recommendReason}">下载</a></div>
            </div>
        </c:if>
    </div>
</div>

<div class="wp-bg" style="display:none">
    <div class="transparency-bg btn-close"></div>
    <!--wp-box==end-->
    <!--wp-box==保存成功-->
    <div class="wp-box wp-b4" style="display:none">
        <h2>保存成功</h2>

        <h3>活动结束后30天内配送，敬请期待！</h3>

        <p>
            <span class="btn-handle" id="b4-share">马上分享，获得新的抽奖机会！</span>
        </p>

        <div id="b4-lotagin"><span>再抽一次</span></div>
    </div>
    <!--wp-box==end-->
    <!--wp-box==保存成功-->
    <div class="wp-box wp-b5" style="display:none">
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

    <!--wp-box==地址-->
    <div class="wp-box wp-b6" style="display: none">
        <div class="address">
            <table>
                <tbody>
                <tr>
                    <td>姓名</td>
                    <td><span class="input_txt"><input type="text" value="" name="name" id="name"/></span></td>
                </tr>
                <tr>
                    <td>手机号码</td>
                    <td><span class="input_txt"><input type="text" value="" name="phone" id="phone"/></span></td>
                </tr>
                <tr>
                    <td>详细地址</td>
                    <td><span class="textarea_box"><textarea name="address" id="address"></textarea></span></td>
                </tr>
                </tbody>
            </table>
        </div>

        <p><span class="btn-handle" id="btn-save">保存</span></p>

        <p id="_tips"></p>
    </div>
    <div class="wp-box wp-b2" style="display:none">
        <h3></h3>
        <p></p>
    </div>
    <!--分享-->
    <div class="popup_box" style="display:none;">
        <img src="${URL_LIB}/static/theme/wap/images/lottery/share.png" alt="">
    </div>
</div>

<input type="hidden" value="${profileId}" id="input_profileid"/>
</body>
<script type="text/javascript">
    $(document).ready(function () {
        var profileId = $('#input_profileid').val();
        if(${award==null || award.lotteryAwardLevel<=0}){
            window.location.href = "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/task/taskgetcode?JParam=${JParam}";
        }
        var level = "${award.lotteryAwardLevel}";
        if (level == "1") {
            $(".Prize1").show();
            $(".Prize2,.Prize3").hide();
            $("#awardname").html("一等奖：${award.lotteryAwardName}");
        } else if (level == "2") {
            $(".Prize2").show();
            $(".Prize1,.Prize3").hide();
            $("#awardname").html("二等奖：${award.lotteryAwardName}");
        } else if(level == "3" || level == "4" || level == "5"){
            $(".Prize3").show();
            $(".Prize2,.Prize1").hide();
            if (level == "3") {
                $("#awardname").html("三等奖：${award.lotteryAwardName}");
            } else if (level == "4") {
                $("#awardname").html("四等奖：${award.lotteryAwardName}");
            } else if (level == "5") {
                $("#awardname").html("五等奖：${award.lotteryAwardName}");
            }
        }else{
            window.location.href = "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/task/taskgetcode?JParam=${JParam}";
        }
        //活动须知
        $("#huodong").on('touchstart', function () {
            $(".wp-bg,.wp-b5").show();
            $(".wp-b4,.wp-b6,.wp-b2,.popup_box").hide();
            $('.btn-close').on('touchstart', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(".wp-bg,.wp-b5").hide();
            });
        });

        //领奖
        $("#_lottery").on('touchstart', function () {
            $(".wp-bg,.wp-b6").show();
            $(".wp-b4,.wp-b5,.wp-b2,.popup_box").hide();
            $("#name").focus();
            $('.btn-close').on('touchstart', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(".wp-bg,.wp-b6").hide();
            });
        });

        //分享
        $("#_popup_box").on('touchstart', function () {
            $(".wp-bg,.popup_box").show();
            $(".wp-b4,.wp-b5,.wp-b6,.wp-b2").hide();
            $('.btn-close').on('touchstart', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(".wp-bg,.popup_box").hide();
            });
        });

        //分享
        $("#b4-share").on('touchstart', function () {
            $(".wp-bg,.wp-b4,.wp-b5,.wp-b6,.wp-b2").hide();
            $(".wp-bg,.popup_box").show();
            $('.btn-close').on('touchstart', function (e) {
                e.stopPropagation();
                e.preventDefault();
                $(".wp-bg,.popup_box").hide();
            });
        });

        //回活动页
        $('#b4-lotagin').on('touchstart', function () {
            try {
                window.location.href = "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/task/taskgetcode?JParam=${JParam}";
            } catch (e) {
            }
        });

        //保存收货信息
        $("#btn-save").on('touchstart', function () {
            var name = $("#name").val();
            if (name.trim() == "") {
                $("#_tips").html("姓名不能为空");
                return false;
            }
            var phoneReg = /^([0-9]+(([-]*)|([+]*))[0-9]+)+$/;
            var phone = $("#phone").val();
            phone = phone.replace(/(\s+)/g, "");
            if (phone.length < 6 || phone.length > 20 || !phoneReg.test(phone)) {
                $("#_tips").html("手机号码未填写或不正确");
                return false;
            }
            var address = $("#address").val();
            if (address.trim() == "") {
                $("#_tips").html("详细地址不能为空");
                return false;
            }
            $("#_tips").html("");
            var ajaxTimeoutTest = $.ajax({
                url: "http://api." + joyconfig.DOMAIN + "/joymeapp/my/modifyaddress",
                type: "POST",
                timeout: 5000,
                data: {profileid: profileId, name: name, phone: phone, address: address},
                dataType: "json",
                success: function (resMsg) {
                    if (resMsg.rs == '1') {
                        $(".wp-b6").hide();
                        $(".wp-bg,.wp-b4").show();
                    } else {
                        return;
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    if (textStatus == "timeout") {
                        $('.wp-b2').find('h3').html("请求超时！");
                        $('.wp-bg,.wp-b2').show();
                        setTimeout(function () {
                            $('.wp-bg,.wp-b2').hide();
                            $('.wp-b2').find('h3').html("");
                        }, 2000);
                    }
                    return;
                }
            });
        });
    });

</script>
</html>