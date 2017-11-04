define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var register = require('../page/register');
    require('../common/tips');
    require('../common/jquery.cookie')($);
//    $('.headt').css({'height':'57px','margin-bottom':'50px'})
//    if (completeStatus != "n" && (blogwebsite != null && blogwebsite != "")) {
//        $("#regiter_moveBox").css('left', '-910px');
//    } else {
//        $("#regiter_moveBox").css('left', 0);
//    }
    $(document).ready(function () {
        $(".text").live("focus",
                function() {
                    register.turnDefaultTips($(this));
                }).live("blur", function() {
                    register.turnOffDefaultTips($(this));
                });

        var joymevalidate = $("#regForm").validate({
                    rules: {
                        userid: {required:true, email:true,checkUserid:[]},
                        userpwd: {required: true, rangelength:[6,18]},
                        ls:{required:true},
                        nickname:{required:true,inputLength:"",verifyWord:"",nickNameExists:"",checkNickName:""},
                        blogdomain: {required: true, rangelength: [5,20],regexDomain:true,verifyDomain:"",blogDomainExists:""}//使用自定义验证方法
                    },
                    messages: {
                        userid: {required:tipsText.userSet.user_email_notnull, email:tipsText.userSet.user_email_wrong},
                        userpwd: {required:tipsText.userSet.user_userpwd_notnull, rangelength:tipsText.userSet.user_userpwd_length},
                        ls:{required:'不同意服务条款?'},
                        nickname: {required:tipsText.userSet.user_nickname_notnull,inputLength:tipsText.userSet.user_nickname_length,verifyWord:tipsText.nickName.user_nickname_illegl},
                        blogdomain: {required:tipsText.userDomainName.user_blogdomain_notnull, rangelength:tipsText.userDomainName.user_blogdomain_length}

                    },
                    showErrors: showErrors//使用自定义的提示方法
                });

        $("#a_reg").click(function() {
            $("#a_reg").hide();
            $("#reg_loding").show();
            if (joymevalidate.form()) {
                $('#regForm').submit();
            } else {
                $("#a_reg").show();
                $("#reg_loding").hide();
            }
        })
        $('#userid').focus();
    });

    //自定义用户名验证方法
    $.validator.addMethod("checkUserid", function(value, element, params) {
        return ajaxverify.verifyUseridExists(value);
    }, tipsText.userSet.user_email_exists);

    $.validator.addMethod('verifyWord', function(value, element, params) {
        return ajaxverify.verifySysWord(value);
    }, tipsText.userDomainName.user_blogdomain_illegl);


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
            result = ajaxverify.verifyNickNameRegex(value);
        }
        return result;
    }, tipsText.userSet.user_word_illegl);

    $.validator.addMethod('verifyDomain', function(value, element, params) {
        var result = true;
        return ajaxverify.verifyDomain(value);
    }, tipsText.userDomainName.user_blogdomain_illegl);

    $.validator.addMethod('blogDomainExists', function(value, element, params) {
        var result = true;
        return !ajaxverify.verifyDomainExists(value, '');
    }, tipsText.userDomainName.user_blogdomain_exists);


    $.validator.addMethod("regexDomain", function(value, element, params) {
        return ajaxverify.validateDomain(value);
    }, tipsText.userDomainName.user_blogdomain_wrong);


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
