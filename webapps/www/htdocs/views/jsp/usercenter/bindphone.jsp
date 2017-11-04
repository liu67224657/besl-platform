<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>绑定手机</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/edit-select.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/setting.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/joymedialog.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
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
                    <h3 class="setting-tit web-hide">绑定手机</h3>

                    <div class="change-password-con">
                        <div>
                            <span>手机号：</span>
                            <input type="text" name="mobile" id="bmtel" maxlength="11"
                                   onkeyup="value=value.replace(/[^\d]|_/ig,'')"
                                   onblur="value=value.replace(/[^\d]|_/ig,'')">
                        </div>
                        <div>
                            <div class="captcha-box"
                                 style="display: block;z-index: 99999;position:static;margin: 0 0 0 10px;">
                                <div class="l-captcha" data-site-key="533a7e232fb9134c30928ceebad087ef" data-width="250"
                                     data-callback="getResponse"></div>
                            </div>
                        </div>
                        <div>
                            <span>验证码：</span>
                            <input type="text" class="w-130 test-code" name="mobilecode" id="bmmobilecode"
                                   maxlength="6">
                            <input type="hidden" name="lsmresponse" id="lsmresponse">
                            <button class="send-code sendVerifyRegMobileCode on hadSent" data-sendtype="bindmobile"
                                    id="sendCode" onclick="sendTelCode();">发送验证码
                            </button>
                        </div>
                        <div>
                            <span>密码：</span>
                            <input type="password" name="password" id="bmpassword" maxlength="16">
                        </div>
                        <div>
                            <span>再次输入：</span>
                            <input type="password" name="repassword" id="bmrepassword" maxlength="16">
                        </div>
                        <div class="btn-con">
                            <button class="btn-sure" type="button" id="bindmobile" onclick="bindTel()">绑定</button>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                var sendCode = false;
                function getResponse(response) {
                    $("#lsmresponse").val(response);
                    $("#sendCode").removeClass("on");
                    $("#sendCode").attr("disabled", false).removeClass("hadSent");
                    sendCode = false;
                }
            </script>
        </div>
    </div>
</div>

<%@ include file="user-center-footer.jsp" %>
<!-- 内容区域 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/openwindow.js"></script>
<script type="text/javascript">
    var timer, count = 60, txt0, count0;

    function bindTel() {
        var mobile = $.trim($('#bmtel').val());//手机号
        var password = $.trim($('#bmpassword').val());//密码
        var bmrepassword = $.trim($('#bmrepassword').val());//确认密码
        var mobilecode = $.trim($('#bmmobilecode').val());//验证码
        var lsmresponse = $('#lsmresponse').val();
        if (mobile == "") {
            mw.ugcwikiutil.msgDialog("手机号不能为空");
            return;
        }

        if (mobilecode == "") {
            mw.ugcwikiutil.msgDialog("验证码不能为空");
            return;
        }

        if (password == "") {
            mw.ugcwikiutil.msgDialog("密码不能为空");
            return;
        }
        if (password.length < 6) {
            mw.ugcwikiutil.msgDialog("密码长度不能小于6位");
            return;
        }

        if (bmrepassword == "") {
            mw.ugcwikiutil.msgDialog("确认密码不能为空");
            return;
        }
        if (bmrepassword.length < 6) {
            mw.ugcwikiutil.msgDialog("确认密码长度不能小于6位");
            return;
        }

        if (password != bmrepassword) {
            mw.ugcwikiutil.msgDialog("两次输入的密码不一致");
            return;
        }

        if (lsmresponse == '') {
            mw.ugcwikiutil.msgDialog("请进行人机验证！");
            return;
        }


        $.post('/servapi/auth/bind/mobile', {
            profileid: '${profile.profileId}',
            mobile: mobile,
            password: password,
            mobilecode: mobilecode,
            profilekey: 'www'
        }, function (data) {
            if (data != "") {
                var dataObj = eval("(" + data + ")");
                if (dataObj.rs == '1') {
                    mw.ugcwikiutil.ensureDialog("绑定成功", function () {
                        window.location.href = "${URL_UC}/usercenter/account/safe"
                    });
                } else if (dataObj.rs == '-10124') {
                    mw.ugcwikiutil.msgDialog("手机号已存在")
                } else if (dataObj.rs == '-10123') {
                    mw.ugcwikiutil.msgDialog("用户已绑定手机号，请先解绑")
                } else if (dataObj.rs == '-10206' || dataObj.rs == '-10204') {
                    mw.ugcwikiutil.msgDialog("验证码错误")
                } else {
                   // mw.ugcwikiutil.msgDialog(dataObj.msg);
                    window.location.href = "${URL_UC}/usercenter/account/safe"
                }
            }
        })

    }
    function sendTelCode() {
        var mobile = $('#bmtel').val();
        var lsmresponse = $('#lsmresponse').val();
        if (mobile == '' || mobile == null) {
            mw.ugcwikiutil.msgDialog("请输入手机号！");
            return;
        }
        if (mobile.length != 11) {
            mw.ugcwikiutil.msgDialog("请输入合法手机号！");
            return;
        }
        if (lsmresponse == '') {
            mw.ugcwikiutil.msgDialog("请进行人机验证！");
            return;
        }

        if (lsmresponse != '' && sendCode) {
            mw.ugcwikiutil.msgDialog("请进行人机验证");
            return;
        }


        $.ajax({
            url: '/servapi/auth/mobile/sendcode',
            data: {mobile: mobile, luotestresponse: lsmresponse,checkmobile:'true'},
            type: 'post',
            dataType: "json",
            success: function (data) {

                LUOCAPTCHA.reset();

                if (data.rs == '-10124') {
                    mw.ugcwikiutil.msgDialog("手机号已存在");
                    $('#lsmresponse').val("");
                    return;
                }

                if (!$("#sendCode").hasClass('hadSent')) {
                    sendCode = true;
                    $("#sendCode").attr("disabled", false);
                    // 发短信
                    count0 = count;
                    $("#sendCode").addClass('hadSent');
                    txt0 = $("#sendCode").text();
                    $("#sendCode").text("重新发送(" + count + ")");
                    timer = setInterval(function () {
                        count--;
                        if (count < 0) {
                            $("#sendCode").removeClass('hadSent').text("发送验证码").attr("disabled", false);
                            clearInterval(timer);
                            count = 60;
                            return;
                        }
                        $("#sendCode").text("重新发送(" + count + ")");
                    }, 1000)
                }

            }
        });
    }
</script>
</body>
</html>