<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>修改绑定的手机号</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/edit-select.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/setting.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/joymedialog.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">

    <style>
        .step-1, .step-2, .setp-3 {
            display: none;
        }
    </style>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

    </script>

</head>
<body>
<%@ include file="user-center-header.jsp" %>
<script src="//captcha.luosimao.com/static/dist/api.js"></script>
<!-- 内容区域 开始 -->
<div class="container">
    <div class="row">
        <div class="setting-con">
            <%@ include file="../customize/customize-general-left.jsp" %>
            <div class="col-md-9 pag-hor-20">
                <div class="setting-r">
                    <h3 class="setting-tit web-hide">修改绑定的手机号</h3>

                    <div class="change-num-con">
                        <div class="chanhe-tit fn-clear">
                            <a href="javascript:;" class="<c:if test="${step eq '1'}">on</c:if>" id="yanzheng">
                                <cite><i class="web-hide"></i><i class="fa fa-check web-show"></i>1</cite>
                                验证账号
                            </a>
                            <a href="javascript:;" class=" <c:if test="${step eq '2'}">on</c:if>" id="xiugai">
                                <cite><i class="web-hide"></i><i class="fa fa-check web-show"></i>2</cite>
                                修改手机号码
                            </a>
                            <a href="javascript:;" class=" <c:if test="${step eq '3'}">on</c:if>" id="success">
                                <cite><i class="web-hide"></i><i class="fa fa-check web-show"></i>3</cite>
                                更换成功
                            </a>
                        </div>
                        <div class="chanhe-con">

                            <input type="hidden" id="mobile" name="mobile" value="${profile.mobile}">
                            <input type="hidden" id="profileId" name="profileId" value="${profile.profileId}">
                            <input type="hidden" value="${step}" name="step">


                            <div class="step-1" <c:if test="${step eq '1'}">style="display: block;" </c:if>>
                                <p>我们将向下方的手机号发送验证码，请在下方输入您收到的验证码</p>

                                <div id="captcha-box">
                                    <div class="captcha-box"
                                         style="display: block;z-index: 99999;position:static;margin: 0 0 0 10px;">
                                        <div class="l-captcha" data-site-key="533a7e232fb9134c30928ceebad087ef"
                                             data-width="250" data-callback="getResponse"></div>
                                    </div>
                                </div>
                                <div>
                                    <span>验证方式：</span>
                                    <cite>使用<b>${hidephone}</b>验证</cite>
                                    <button id="sendCode1" class="send-code hadSent" disabled="disabled">发送验证码</button>
                                    <input type="hidden" name="lsmresponse" id="lsmresponse">
                                </div>
                                <div>
                                    <span>验证码：</span>
                                    <input type="text" class="w-130" name="oldTelCode" id="oldTelCode">
                                </div>
                                <button class="next-icon" id="nextst">下一步</button>
                            </div>

                            <div class="step-2" <c:if test="${step eq '2'}">style="display: block;" </c:if>>
                                <p>请填写新的手机号</p>

                                <div id="captcha-box-new"></div>
                                <div>
                                    <span>手机号：</span>
                                    <input type="text" class="w-130" name="newTelephone" value="" id="newTelephone"
                                           maxlength="11" onkeyup="value=value.replace(/[^\d]|_/ig,'')"
                                           onblur="value=value.replace(/[^\d]|_/ig,'')">
                                    <button id="sendCode2" class="send-code hadSent" disabled="disabled">发送验证码</button>
                                </div>
                                <div>
                                    <span>验证码：</span>
                                    <input type="text" class="w-130" name="verifyCode" id="verifyCode" value="">
                                </div>
                                <button class="next-icon" id="updatephone">下一步</button>
                            </div>
                            <div class="setp-3" <c:if test="${step eq '3'}">style="display: block;" </c:if>>
                                <div>
                                    <p>恭喜您，已经成功更换绑定手机！</p>
                                    <font>您的新手机号：<b id="newmobile"></b></font>
                                    <a href="${URL_UC}/usercenter/account/safe">
                                        <button class="finish-icon">完成</button>
                                    </a>
                                </div>

                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="user-center-footer.jsp" %>
<!-- 内容区域 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/openwindow.js"></script>

<script type="text/javascript">
    //发送验证码
    var sendCode = false;
    var sendcode = "${step}";
    function getResponse(response) {
        $("#lsmresponse").val(response);

        if (sendcode == "1") {
            $("#sendCode1").attr("disabled", false).removeClass("hadSent");
        } else {
            $("#sendCode2").attr("disabled", false).removeClass("hadSent");
        }
        sendCode = false;
    }
    var timer, count = 60, txt0, count0;
    $(document).ready(function () {


        $("#sendCode1,#sendCode2").bind('click', function (e) {
            e.preventDefault();
            var classtr = $(this).attr('id');
            var checkmobile = false;
            var mobile = "";
            if (classtr == "sendCode1") {
                mobile = $.trim($('#mobile').val());
            } else {
                mobile = $.trim($('#newTelephone').val());
                checkmobile =true;
            }

            var lsmresponse = $.trim($('#lsmresponse').val());
            if (lsmresponse == '') {
                mw.ugcwikiutil.msgDialog("请进行人机验证");
                return;
            }

            if (lsmresponse != '' && sendCode) {
                mw.ugcwikiutil.msgDialog("请进行人机验证");
                return;
            }

            if (mobile == '' || mobile == null) {
                mw.ugcwikiutil.msgDialog("请输入手机号");
                return;
            }

            if (mobile.length != 11) {
                mw.ugcwikiutil.msgDialog("手机号格式不正确");
                return;
            }


            $.ajax({
                url: '/servapi/auth/mobile/sendcode',
                data: {mobile: mobile, luotestresponse: lsmresponse,checkmobile:checkmobile},
                type: 'post',
                dataType: "json",
                success: function (data) {
                    LUOCAPTCHA.reset();

                    if (data.rs == '-10124') {
                        mw.ugcwikiutil.msgDialog("手机号已存在");
                        $('#lsmresponse').val("");
                        return;
                    }

                    if (!$("#" + classtr).hasClass('hadSent')) {
                        $("#" + classtr).attr("disabled", true);
                        sendCode = true;
                        // 发短信
                        count0 = count;
                        $("#" + classtr).addClass('hadSent');
                        txt0 = $("#" + classtr).text();
                        $("#" + classtr).text("重新发送(" + count + ")");
                        timer = setInterval(function () {
                            count--;
                            if (count < 0) {
                                $("#" + classtr).removeClass('hadSent').text("发送验证码").attr("disabled", false);
                                clearInterval(timer);
                                count = 60;
                                return;
                            }
                            $("#" + classtr).text("重新发送(" + count + ")");
                        }, 1000)
                    }

                }
            });

        });

        //第一个下一步
        $("#nextst").bind('click', function () {
            var mobile = $.trim($('#mobile').val());
            var mcode = $.trim($("#oldTelCode").val());
            var lsmresponse = $.trim($('#lsmresponse').val());

            if (mobile == '' || mobile == null) {
                mw.ugcwikiutil.msgDialog("请输入手机号");
                return;
            }

            if (mcode == "") {
                mw.ugcwikiutil.msgDialog("您还没有输入验证码");
                return;
            }

            $.ajax({
                url: '/usercenter/account/modifyphonestep1',
                data: {mobile: mobile, mcode: mcode},
                type: 'post',
                dataType: "json",
                success: function (data) {
                    if (data.rs == '-10206') {
                        mw.ugcwikiutil.msgDialog("验证码错误");
                    } else {
                        $(".step-1").hide();
                        $("#yanzheng").removeClass("on")
                        $(".step-2").show();
                        $("#xiugai").addClass("on");
                        var captchahtml = $("#captcha-box").html();
                        $("#captcha-box").html("");
                        $("#captcha-box-new").html(captchahtml);
                        sendcode = 2;
                        count = 60;
                        clearInterval(timer);
                    }
                }
            });

        });

        function modifyphone() {
            var newTelephone = $.trim($('#newTelephone').val());
            var verifyCode = $.trim($("#verifyCode").val());
            $.ajax({
                url: '/servapi/auth/modify/mobile',
                data: {
                    profileid: '${profile.profileId}',
                    logindomain: 'mobile',
                    oldmobile: '${profile.mobile}',
                    mobile: newTelephone
                },
                type: 'post',
                dataType: "json",
                success: function (data) {
                    if (data.rs == '-10206') {
                        mw.ugcwikiutil.msgDialog("验证码错误");
                    } else if (data.rs == '-10123' || data.rs == '-10124') {
                        mw.ugcwikiutil.msgDialog("手机号已存在");
                    } else if (data.rs < 0) {
                        mw.ugcwikiutil.msgDialog("绑定失败");
                    } else {
                        $("#newmobile").html(newTelephone.replace(/^(\d{3})\d{5}(\d+)/, "$1****$2"));
                        $(".step-2").hide();
                        $("#xiugai").removeClass("on")
                        $(".setp-3").show();
                        $("#success").addClass("on");
                    }
                }
            });
        }


        $("#updatephone").bind('click', function () {
            var newTelephone = $.trim($('#newTelephone').val());
            var verifyCode = $.trim($("#verifyCode").val());
            if (newTelephone == '' || newTelephone == null) {
                mw.ugcwikiutil.msgDialog("请输入手机号");
                return;
            }
            if (newTelephone.length != 11) {
                mw.ugcwikiutil.msgDialog("手机号格式不正确");
                return;
            }
            if (verifyCode == '' || verifyCode == null) {
                mw.ugcwikiutil.msgDialog("您还没有输入验证码");
                return;
            }

            $.ajax({
                url: '/servapi/auth/verify/mobile',
                data: {mobile: newTelephone, mobilecode: verifyCode},
                type: 'post',
                dataType: "json",
                success: function (data) {
                    if (data.rs == "1") {
                        modifyphone();
                    } else {
                        mw.ugcwikiutil.msgDialog("验证码错误")
                    }
                }
            });
        });

    });


</script>
</body>
</html>