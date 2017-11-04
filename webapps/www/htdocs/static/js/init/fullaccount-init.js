define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var register = require('../page/register');
    var header = require('../page/header');
    require('../common/tips');
    (function () {
        header.noticeSearchReTopInit();
    })()

//    if(!$("#useridTips").is(":hidden")){

//    }
    $(document).ready(function () {
        $(".text").live("click",
            function () {
                register.turnDefaultTips($(this));
            }).live("blur", function () {
                register.turnOffDefaultTips($(this));
            });

        var joymevalidate = $("#perForm").validate({
            rules:{
                userid:{required:true, email:true, checkUserid:[]},
                userpwd:{required:true, rangelength:[6, 18]}
            },
            messages:{
                userid:{required:tipsText.userSet.user_email_notnull, email:tipsText.userSet.user_email_wrong},
                userpwd:{required:tipsText.userSet.user_userpwd_notnull, rangelength:tipsText.userSet.user_userpwd_length}
            },
            showErrors:showErrors//使用自定义的提示方法
        });
        $("#link_upload_audio").click(function () {
            $("#perForm").submit();
        })
        $("#skip").click(function () {
            var rurl = $("#rurl").val();
            window.location.href = rurl;
        })
        $('#userid').click().focus();
    });

    //自定义用户名验证方法
    $.validator.addMethod("checkUserid", function (value, element, params) {
        return ajaxverify.verifyUseridExists(value);
    }, tipsText.userSet.user_email_exists);

    $.validator.addMethod('verifyWord', function (value, element, params) {
        return ajaxverify.verifySysWord(value);
    }, tipsText.userDomainName.user_blogdomain_illegl);


    $.validator.addMethod('inputLength', function (value, element, params) {
        var result = true;
        var inputNum = common.getInputLength($("#nickname").val());
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum < 2 || inputNum > 16) {
            result = false;
        }
        return result;
    });

    $.validator.addMethod("regexDomain", function (value, element, params) {
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
//        $("#userid").focus();
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            register.turnSucTips(suc.id);
        }

    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});
