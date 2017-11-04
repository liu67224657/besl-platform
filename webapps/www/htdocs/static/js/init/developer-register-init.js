define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var register = require('../page/register');
    require('../common/tips');
    require('../common/jquery.cookie')($);

    $(document).ready(function () {

        //回车事件
        $("#userpwd").keyup(function(e) {
            if (e.keyCode == 13) {
                $("#form-submit").submit();
            }
        });

        $('#form-submit').bind('submit', function() {
            register.turnOffDefaultTips($('#nickname'));
        });
        $("#nickname").live("blur", function() {
            register.turnOffDefaultTips($(this));
        });

        $("#userid").live("focus",
                function() {
                    register.turnDefaultTips($(this));
                }).live("blur", function() {
                    register.turnOffDefaultTips($(this));
                });

        $("#userpwd").live("focus",
                function() {
                    register.turnDefaultTips($(this));
                }).live("blur", function() {
                    register.turnOffDefaultTips($(this));
                });

        $('.submitbtn').click(function () {
            $('#form-submit').submit();
        });

        $("#form-submit").validate({
                    rules: {
                        userid: {required:true, email:true,rangelength:[6,40],checkUserid:[]},
                        userpwd: {required: true, rangelength:[6,18], checkPwd:""},
                        nickname:{required:true,inputLength:"",verifyWord:"",nickNameExists:"",checkNickName:""}
                    },
                    messages: {
                        userid: {required:tipsText.userSet.user_email_notnull, email:tipsText.userSet.user_email_wrong,rangelength:tipsText.userSet.user_email_dev_length},
                        userpwd: {required:tipsText.userSet.user_userpwd_notnull, rangelength:tipsText.userSet.user_userpwd_length,checkPwd:tipsText.userSet.userset_password_exist_space},
                        nickname: {required:tipsText.userSet.user_nickname_notnull,inputLength:tipsText.userSet.user_nickname_length,verifyWord:tipsText.nickName.user_nickname_illegl}
                    },
                    showErrors: showErrors
                });

    });

    //自定义用户名验证方法
    $.validator.addMethod("checkUserid", function(value, element, params) {
        return ajaxverify.verifyUseridExists(value);
    }, tipsText.userSet.user_email_exists);

    $.validator.addMethod('verifyWord', function(value, element, params) {
        return ajaxverify.verifySysWord(value);
    }, tipsText.userDomainName.user_blogdomain_illegl);

    $.validator.addMethod('checkPwd', function(value, element, params) {
        return ajaxverify.verifyPwd(value);
    }, tipsText.userSet.userset_password_exist_space);

    $.validator.addMethod('inputLength', function(value, element, params) {
        var result = true;
        var inputNum = common.getInputLength($("#nickname").val());
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum < 2 || inputNum > 16) {
            result = false;
        }
        return result;
    });
    $.validator.addMethod('nickNameExists', function(value, element, params) {
        var result = true;
        var inputNum = common.getInputLength($("#nickname").val());
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum >= 2 && inputNum <= 16) {
            value = value.replace(/(^\s+)|(\s+$)/g, "");
            result = !ajaxverify.verifyNickNameExists(value, '');
        }
        return result;
    }, tipsText.userSet.user_nickname_exists);
    //checkNickName
    $.validator.addMethod('checkNickName', function(value, element, params) {
        var inputNum = common.getInputLength($("#nickname").val());
        var result = true;
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum >= 2 && inputNum <= 16) {
            value = value.replace(/(^\s+)|(\s+$)/g, "");
            result = ajaxverify.verifyNickNameRegex(value);
        }
        return result;
    }, tipsText.userSet.user_word_illegl);


//自定义提示方法
    function showErrors() {
        //验证失败处理
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            var elename = this.idOrName(error.element);
            register.turnErrorTips(elename, error.message);
        }
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            register.turnSucTips(suc.id);
        }
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});
