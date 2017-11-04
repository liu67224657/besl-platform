define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var common = require('../common/common');
    var customize = require('../page/customize');
    var joymealert = require('../common/joymealert');
    var alertOption = {title: '获取验证码', tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 270
    };
    var interval
    $(document).ready(function () {
        header.noticeSearchReTopInit();
        if ($('#send').attr('disabled')) {
            interval = setInterval(calSendTime, 1000);
        }
        var reg = /^[1][3-9][0-9]{9}$/;
        $('#send').click(function () {
            var phone = $("#phone").val();
            if (phone == '' || !reg.test(phone)) {
                alertOption.title = '提示信息';
                alertOption.text = "请填写正确的手机号码";
                joymealert.alert(alertOption);
                return;
            }
            var checkphonebool = false;
            $.ajax({type: "POST",
                url: "/profile/mobile/checkphone",
                async: false,
                data: {phone: phone},
                success: function (req) {
                    var result = eval('(' + req + ')');
                    alertOption.text = result.msg;
                    if (result.status_code == '0') {
                        alertOption.title = '提示信息';
                        alertOption.text = result.msg;
                        joymealert.alert(alertOption);
                        checkphonebool = true;
                    }
                }
            });
            if (checkphonebool) {
                return;
            }
            alertOption.text = '<table width="100%">' +
                '<tbody><tr>' +
                '<td><input type="text" id="input_vcode" style="height:22px; float:none; border:1px solid #c1c3c0; width:148px; margin-bottom:4px;"><br /><span style=" color: #999;">请输入图片中的信息</span></td>' +
                '<td><a href="javascript:void(0)" onclick="javascript:document.getElementById(\'img_validatecode\').src=\'/validate/simgcode?\'+Math.random();return false;"><img src="/validate/simgcode" id="img_validatecode" width="84" height="24" style="margin-bottom:4px;"></a><br><a href="javascript:void(0)" onclick="javascript:document.getElementById(\'img_validatecode\').src=\'/validate/simgcode?\'+Math.random();return false;">换一张</a></td>' +
                '</tr>' +

                '</tbody></table>';
            alertOption.submitFunction = function () {
                var vcode = $('#input_vcode').val();
                $('#verifyform').attr('action', "/profile/mobile/send");
                $('#vcode').val(vcode);
                $('#verifyform').submit();
            }
            joymealert.confirm(alertOption);
        });

        $("#formsubmit").click(function () {

            var phone = $("#phone").val();
            var verifycode = $("#verifycode").val();
            if (phone == '' || !reg.test(phone)) {
                alertOption.title = '提示信息';
                alertOption.text = "请填写正确的手机号码！";
                joymealert.alert(alertOption);
                return;
            }
            if (verifycode == '') {
                alertOption.title = '提示信息';
                alertOption.text = "请输入手机验证码！";
                joymealert.alert(alertOption);
                return;
            }
            $('#verifyform').attr('action', "/profile/mobile/verify");
            $("#verifyform").submit();
        });
        $("#modifyformsubmit").click(function () {
//            if ($('#send').attr('disabled')) {
//                return false;
//            }

            var phone = $("#phone").val();
            var verifycode = $("#verifycode").val();
            if (phone == '' || !reg.test(phone)) {
                alertOption.title = '提示信息';
                alertOption.text = "请填写正确的手机号码！";
                joymealert.alert(alertOption);
                return;
            }
            if (verifycode == '') {
                alertOption.title = '提示信息';
                alertOption.text = "请输入手机验证码！";
                joymealert.alert(alertOption);
                return;
            }
            $('#verifyform').attr('action', "/profile/mobile/modify");
            $("#verifyform").submit();
        });

        $("#removebind").click(function () {
            alertOption.title = '提示信息';
            alertOption.text = '确定要取消绑定吗？';
            alertOption.submitButtonText = "确定";
            alertOption.cancelButtonText = "取消";
            alertOption.submitFunction = function unbind() {
                window.location = "/profile/mobile/unbind";

            };
            joymealert.confirm(alertOption);
        });

        $("#modifyphone").click(function () {
            window.location = "/profile/mobile/modifypage";
        });
        $("#canclebtn").click(function () {
            window.location = "/profile/mobile/gopage";
        })
    });


    function calSendTime() {
        var prefix = '发送验证码(';
        var suffix = ')'
        var sendTime = $('#send').val();
        sendTime = sendTime.replace(prefix, '');
        sendTime = sendTime.replace(suffix, '');
        sendTime = parseInt(sendTime);
        sendTime--;
        if (sendTime >= 0) {
            $('#send').val('发送验证码(' + sendTime + ')');
        } else {
            $('#send').val('发送验证码').attr('disabled', false);
            $('#send_info').remove();
            clearInterval(interval);
        }
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm');
})
;