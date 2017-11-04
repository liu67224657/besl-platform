<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <link href="http://static.joyme.com/app/wanba/20150915/css/style.css" rel="stylesheet"/>
    <title>玩霸金秋大答谢</title>
    <style>
        .Threebox {
            width: 60%;
            display: none;
            position: fixed;
            top: 50%;
            left: 22%;
            position: fixed;
            z-index: 1000;
        }

        .Threebox span {
            color: #fff;
            line-height: 18px;
            font-size: 14px;
            padding: 10px;
            display: block;
            text-align: center;
            background: rgba(0, 0, 0, 0.8);
            border-radius: 5px;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
            -o-border-radius: 5px;
            -ms-border-radius: 5px;
        }
    </style>
</head>
<body>

<div class="Threebox"><span></span></div>
<div id="_sharebtn_status" style="display: none;">yes</div>
<div id="_title" style="display: none;">幸福就是边玩游戏边吃大闸蟹</div>
<div id="_desc" style="display: none;">玩霸发大闸蟹啦，快来试试手气吧。</div>
<div id="_clientpic" style="display: none;">${URL_LIB}/static/theme/wap/images/wanba/zq2015/share_png.jpg</div>
<div id="_share_task" style="display: none;">wanba_five_month_task</div>
<div id="_share_url" style="display: none;">${short_url}</div>

<div class="wrapper">
    <div class="cont">
        <div id="zhuanpan">
            <img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/title1.jpg" alt="">

            <div class="dial-box">
                <div class="dial-cont">
                    <div class="lotCon">
                        <div class="prize prize1">
                            <div class="prizebg">大闸蟹</div>
                        </div>
                        <div class="prize prize2">
                            <div class="prizebg">迷豆</div>
                        </div>
                        <div class="prize prize3">
                            <div class="prizebg">着迷公仔</div>
                        </div>
                        <div class="prize prize4">
                            <div class="prizebg">迷豆</div>
                        </div>
                        <div class="prize prize5">
                            <div class="prizebg">着迷logo帽</div>
                        </div>
                        <div class="prize prize6">
                            <div class="prizebg">迷豆</div>
                        </div>
                    </div>
                    <div class="lotTxt">
                        <p class="txt1"><span>大闸蟹</span></p>

                        <p class="txt2"><span>迷豆</span></p>

                        <p class="txt3"><span>着迷公仔</span></p>

                        <p class="txt4"><span>迷豆</span></p>

                        <p class="txt5"><span>着迷logo帽</span></p>

                        <p class="txt6"><span>迷豆</span></p>
                    </div>
                    <div class="lotImg">
                        <p class="img6"><img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/spoil2.png" alt="">
                        </p>

                        <p class="img1"><img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/spoil1.png" alt="">
                        </p>

                        <p class="img2"><img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/spoil2.png" alt="">
                        </p>

                        <p class="img3"><img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/spoil4.png" alt="">
                        </p>

                        <p class="img4"><img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/spoil2.png" alt="">
                        </p>

                        <p class="img5"><img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/spoil3.png" alt="">
                        </p>
                    </div>
                </div>
                <!--抽奖按钮-->
                <div class="dial-btn"></div>
                <!--抽奖按钮-->
            </div>
            <div class="dial-role">
                <img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/role.png" alt="">
            </div>
            <!--弹窗-->
            <div class="dialog"></div>
            <!--在抽一次-->
            <div class="dialog-cont" id="dialog-cont1">
                <cite class="dialog-close"></cite>
                <span id="spantext"></span>

                <div class="dialog-btn">
                    <a href="javascript:shareToFriend();" class="tall-btn"></a><!--告诉朋友-->
                    <a href="javascript:closewindow();" class="two-btn"></a><!--再抽一次-->
                </div>
            </div>

            <div class="dialog-cont shareto" id="dialog-cont2">
                <!-- <cite class="dialog-close"></cite> -->
                <span id="dialog-span">分享给朋友可以再抽1次<br/>（最多5次）</span>

                <div class="dialog-line">
                    <em></em>
                </div>
            </div>
        </div>
        <div id="address" style="display:none;">
            <img src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/title2.jpg" alt="">
            <span class="awards"></span>

            <div class="address-cont">
                <p class="address-tit"><span>请填写您的地址，</span>以便快递小哥把礼品送到您的手中。</p>

                <div class="address-inp">
                    <p><span>姓名</span><input type="text" id="name"></p>

                    <p><span>手机</span><input type="text" id="phone"></p>

                    <p><span>地址</span><input type="text" name="address"></p>
                </div>
                <p class="address-text">我们将在活动结束后14个工作日内将礼品寄出。<br/>听说加玩霸读者交流群（321182655）可以查单号噢！</p>
                <a href="javascript:void(0);" class="address-btn"><img
                        src="${URL_LIB}/static/theme/wap/images/wanba/zq2015/sure-btn.png"
                        alt=""></a>
            </div>
        </div>

    </div>
</div>
<input type="hidden" name="logindomain" value="${loginDomain}">
<input type="hidden" name="appkey" value="${appkey}">
<input type="hidden" name="platform" value="${platform}">
<input type="hidden" name="clientid" value="${clientid}">
<input type="hidden" name="channelid" value="${channelid}">
<input type="hidden" name="token" value="${token}">
<input type="hidden" name="uid" value="${uid}">
<input type="hidden" name="profileId" value="${profileId}">
</body>
<script src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script>
var non_touch = false;
$(document).ready(function() {
    $(".shareto").click(function() {
        $("#dialog-cont2").hide();
        $('.dialog').hide();
        removeRotate();
    })

    $('.dialog').on('click', function() {
        $(this).hide();
        $('.dialog-cont').hide();
        $('.shareto').hide();
        removeRotate();
    })
    var silde = {
        num:1,
        len:$('.sildeUl>li').length,
        h:$('.sildeUl>li:first').height(),
        timer:null,
        int:function() {
            $('.sildeUl').height(silde.h * silde.len);
            silde.timer = setInterval(function() {
                $('.sildeUl').stop().animate({'marginTop':-silde.h * silde.num}, 500, function() {
                    $(this).find('li').slice(0, silde.num).appendTo($(this))
                    $(this).css('marginTop', 0)
                });
            }, 2000);
        }
    };
    var choujiang = {
        int:function(classid) {
            var classid = classid;
            $(".dial-btn").on('click', function(e) {
                e.stopPropagation();
                e.preventDefault();

                var logindomain = $("[name='logindomain']").val();
                if (logindomain == '' || logindomain == 'client') {
                    _jclient.showLogin();
                    return;
                }
                if (non_touch) {
                    return;
                }

                non_touch = true;
                var appkey = $("[name='appkey']").val();
                var uid = $("[name='uid']").val();
                var platform = $("[name='platform']").val();
                var clientid = $("[name='clientid']").val();
                var channelid = $("[name='channelid']").val();
                var logindomain = $("[name='logindomain']").val();
                var token = $("[name='token']").val();
                var ajaxTimeoutTest = $.ajax({
                            url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/task/taskdownload",
                            type: "POST",
                            timeout: 30000,
                            data: {appkey:appkey,uid:uid,platform:platform,clientid:clientid,channelid:channelid,logindomain:logindomain,token:token},
                            dataType: "json",
                            success: function (resMsg) {
                                if (resMsg.rs == '1') {
                                    var award = resMsg.result;
                                    var awardLevel = award.lotteryAwardLevel;

                                    if (awardLevel < 0) {

                                        $(".dialog-btn").empty();
                                        var usertime = award.lotteryTimes;
                                        if (parseInt(usertime) > 7) {
                                            $('.dialog').show();
                                            $("#dialog-span").html("您今天的抽奖次数已用完<br/>请明天再来。")
                                            $("#dialog-cont2").show();
                                            $('#dialog-cont1').hide();
                                            non_touch = false;
                                        } else {
                                            $('.dialog').show();
                                            $("#dialog-cont2").show();
                                            $('#dialog-cont1').hide();
                                            non_touch = false;
                                        }
                                        return;
                                    } else if (awardLevel == '0') {
                                        var chars = [2,4,6];
                                        var random = Math.round(Math.random() * 2);
                                        classid = chars[random];
                                    } else if (awardLevel == '100') {
                                        //帽子
                                        $(".awards").addClass("logo");
                                        classid = 5;
                                    } else if (awardLevel == '300') {
                                        //迷仔
                                        $(".awards").addClass("gz");
                                        classid = 3;
                                    }
                                    $('.dial-box').addClass('action');
                                    var Pnum = $(".prize").length;
                                    var meannum = 360 / Pnum;

                                    var angle = parseInt(360 * 10 - (classid - 2) * meannum);
                                    if (Pnum < classid) {

                                    }
                                    $(".dial-cont").addClass('rotate');
                                    $(".dial-cont").css({'-webkit-transform':'rotate(' + angle + 'deg)','transform':'rotate(' + angle + 'deg)'});
                                    var t = setTimeout(function() {
                                        $('.prize' + (classid)).find('.prizebg').addClass('sel');
                                        if (awardLevel == '0') {
                                            $(".dialog-btn").empty();
                                            var href1 = '<a href="javascript:shareToFriend();" class="tall-btn"></a>';
                                            var href2 = '<a href="javascript:closewindow();" class="two-btn"></a>';
                                            $(".dialog-btn").append(href1);
                                            $(".dialog-btn").append(href2);
                                            $('#spantext').text("恭喜获得" + award.lotteryAwardDesc + "迷豆");
                                            $('.dialog').show();
                                            $('#dialog-cont1').show();
                                            _jclient.jump("jt=33&ji=" + award.lotteryAwardDesc);
                                        } else {
                                            $("#zhuanpan").css("display", "none");
                                            $("#address").css("display", "block");
                                        }
                                    }, 5000);
                                    setTimeout(function() {
                                        $('.prize').find('.prizebg').removeClass('sel');
                                        $('.dial-box').removeClass('action');
                                        $(".dial-cont").removeClass('rotate');
                                        non_touch = false;
                                    }, 7000);

                                } else if (resMsg.rs == '-1') {
                                    non_touch = false;
                                    _jclient.showLogin();
                                } else if (resMsg.rs == '2') {
                                    non_touch = false;
                                } else if (resMsg.rs == '-10104') {
                                    $('.Threebox').find("span").text("当前用户不存在，请重新登录！");
                                    $('.Threebox').show().delay(3000).hide(0);
                                    non_touch = false;
                                } else if (resMsg.rs == '-100') {
                                    $('.Threebox').find("span").text("APPKEY错误！");
                                    $('.Threebox').show().delay(3000).hide(0);
                                    non_touch = false;
                                }
                                if (resMsg.rs == "-201") {
                                    $('.Threebox').find("span").text("您使用的版本过低，请先升级客户端再来抽奖哟～");
                                    $('.Threebox').show().delay(3000).hide(0);
                                    non_touch = false;
                                }

                            }, error: function (XMLHttpRequest, textStatus, errorThrown) {
                                if (textStatus == "timeout") {
                                    ajaxTimeoutTest.abort();
                                    $('.Threebox').find("span").text("网络有点问题，请再试一遍哦~");
                                    $('.Threebox').show().delay(3000).hide(0);
                                    non_touch = false;
                                }
                            }});
            });


            //保存收货信息
            $(".address-btn").on('touchstart', function (e) {
                e.stopPropagation();
                e.preventDefault();
                var name = $("#name").val();
                var profileId = $("[name='profileId']").val();
                if (name.trim() == "") {
                    $('.Threebox').find("span").text("姓名不能为空");
                    $('.Threebox').show().delay(3000).hide(0);
                    return false;
                }
                var phoneReg = /^(1+(([-]*)|([+]*))[0-9]+)+$/;
                var phone = $("#phone").val();
                phone = phone.replace(/(\s+)/g, "");
                if (phone.length != 11 || !phoneReg.test(phone)) {
                    $('.Threebox').find("span").text("手机号码未填写或不正确");
                    $('.Threebox').show().delay(3000).hide(0);
                    return false;
                }
                var address = $("[name='address']").val();
                if (address.trim() == "") {
                    $('.Threebox').find("span").text("详细地址不能为空");
                    $('.Threebox').show().delay(3000).hide(0);
                    return false;
                }
                var ajaxTimeoutTest = $.ajax({
                            url: "http://api." + joyconfig.DOMAIN + "/joymeapp/my/modifyaddress",
                            type: "POST",
                            timeout: 5000,
                            data: {profileid: profileId, name: name, phone: phone, address: address},
                            dataType: "json",
                            success: function (resMsg) {
                                if (resMsg.rs == '1') {
                                    $(".dialog-btn").empty();
                                    $("#zhuanpan").css("display", "block");
                                    $("#address").css("display", "none");
                                    $('#spantext').html("将在活动结束后14个工作日内将礼品寄出<br />让朋友一起来玩吧");
                                    var ahref = "<a href='javascript:shareToFriend();' class='shareto-btn' ></a>";
                                    $(".dialog-btn").append(ahref);
                                    $('.dialog').show();
                                    $('#dialog-cont1').show();

                                    return;
                                } else {
                                    $(".dialog-btn").empty();
                                    $("#zhuanpan").css("display", "block");
                                    $("#address").css("display", "none");
                                    $('#spantext').html("地址保存失败<br />请加入玩霸读者交流群（321182655）<br/>联系管理员保存地址");
                                    var ahref = '<a href="javascript:closewindow();" class="two-btn"></a>';
                                    $(".dialog-btn").append(ahref);
                                    $('.dialog').show();
                                    $('#dialog-cont1').show();

                                    return;
                                }
                            },
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                if (textStatus == "timeout") {
                                    ajaxTimeoutTest.abort();
                                    $('.Threebox').find("span").text("网络有点问题，请再试一遍哦~");
                                    $('.Threebox').show().delay(3000).hide(0);
                                }
                                return;
                            }
                        });
            });
        }
    };
    silde.int();
    choujiang.int(1);//1大闸蟹==60° 2迷豆==360°  3着迷公仔==300°  4迷豆==240°  5着迷logo帽==180° 6迷豆==120°
    $(".two-btn").on('click', function() {
        $('.dialog').hide();
        $('#dialog-cont1').hide();
        removeRotate();
    });
    $('.dialog-close').on('click', function() {
        $('.dialog').hide();
        $('#dialog-cont1').hide();
        removeRotate();
    });

});
function closewindow() {
    $('.dialog').hide();
    $('#dialog-cont1').hide();
    removeRotate();
}
function shareToFriend() {
    $("#dialog-cont2").show();
    $('#dialog-cont1').hide();
    removeRotate();
}

function removeRotate() {
    $(".dial-cont").removeClass('rotate');
    $(".dial-cont").css({'-webkit-transform':'rotate(' + 0 + 'deg)','transform':'rotate(' + 0 + 'deg)'});
}
</script>
</html>
