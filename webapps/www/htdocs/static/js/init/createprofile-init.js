define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var header = require('../page/header');
    require('../common/tips');

    $(document).ready(function() {
        $("#createprofileform").bind('submit', function() {
            var screenName = $("#screenname").val();
            if (screenName == null || screenName.length == 0) {
                errorTips(tipsText.userSet.user_nickname_notnull);
                return false;
            }

            var inputNum = common.getInputLength(screenName);
            inputNum = !inputNum ? 0 : inputNum;
            if (inputNum < 2 || inputNum > 16) {
                errorTips(tipsText.userSet.user_nickname_length);
                return false;
            }

            if (!ajaxverify.verifySysWord(screenName)) {
                errorTips(tipsText.userSet.user_nickname_illegl);
                return false;
            }

            if (ajaxverify.verifyNickNameExists(screenName,'')) {
                errorTips(tipsText.userSet.user_nickname_has_exists);
                return false;
            }

        })

        $("#a_reg").bind("click", function() {
            $("#createprofileform").submit();
        });

        //回车事件
        $("#screenname").keyup(function(e) {
            if (e.keyCode == 13) {
                $("#createprofileform").submit();
            }
        });

        header.noticeSearchReTopInit();
    });

    function errorTips(msg) {
        //验证失败处理
        $("#tipstext").text(msg);
    }


    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});