define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var customize = require('../page/customize');
    var header = require('../page/header');
    require('../common/tips');
    require('../common/jquery.dropdownlist')($)
    $(document).ready(function() {
        common.checkInputLength(140, 'description', 'description_num');
        $("#description").keydown(function() {
            common.checkInputLength(140, 'description', 'description_num');
        });

        $("#description").keyup(function() {
            common.checkInputLength(140, 'description', 'description_num');
        });
        $("#provinceId").ylDropDownList();

        //为省列表绑定js方法，获取区域列表
        $("#provinceId").change(function() {
            if ($(this).val() != "-1") {
                //调用ajax方法查询
                $.post("/json/profile/customize/getsubregionbypid", {regionid:$(this).val()}, function(data) {
                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj.status_code == '1') {
                        $("#cityid").html('<option value="-1" id="cityid_init">请选择</option>');
                        $.each(jsonObj.result, function(i, val) {
                            var str = '<option value="' + val.regionId + '">' + val.regionName + '</option>';
                            $("#cityid_init").after(str);
                        });
                        $("#cityid").next('div').remove();
                        $("#cityid").ylDropDownList();
                    }
                });
            } else {
                $("#cityid").html('<option value="-1" id="cityid_init">请选择</option>');
            }

        });

        //若省份不为-1,选择城市selected

        customize.setCity($("#joycityid").val());

        $("#form_baseinfo").validate({
                    rules: {
                        description:{verifyWord:""},
                        nickname:{required:true,inputLength:"",verifyWord:"",nickNameExists:""}
                    },
                    messages: {
                        description:{verifyWord:tipsText.userSet.user_word_illegl},
                        nickname: {required:tipsText.userSet.user_nickname_notnull,inputLength:tipsText.userSet.user_nickname_length,verifyWord:tipsText.nickName.user_nickname_illegl}
                    },
                    showErrors: customize.showValidateTips//使用自定义的提示方法
                });

        $("#savebaseinfo").bind("click", function() {
            customize.saveBaseInfo();
        });
        header.noticeSearchReTopInit();
    });

    $.validator.addMethod('inputLength', function(value, element, params) {
        var result = true;
        var inputNum = common.getInputLength($("#nickname").val());
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum < 2 || inputNum > 16) {
            result = false;
        }
        return result;
    });

    $.validator.addMethod('verifyWord', function(value, element, params) {
        return ajaxverify.verifySysWord(value);
    });


    $.validator.addMethod('nickNameExists', function(value, element, params) {
        var result = true;
        var inputNum = common.getInputLength($("#nickname").val());
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum >= 2 && inputNum <= 16) {
            result = !ajaxverify.verifyNickNameExists(value, joyconfig.joyuserno);
        }
        return result;
    }, tipsText.userSet.user_nickname_exists);

//    //checkNickName
//    $.validator.addMethod('checkNickName', function(value, element, params) {
//        var inputNum = common.getInputLength($("#nickname").val());
//        var result = true;
//        inputNum = !inputNum ? 0 : inputNum;
//        if (inputNum >= 2 && inputNum <= 16) {
//            result = ajaxverify.verifyNickNameRegex(value);
//        }
//        return result;
//    }, tipsText.userSet.user_word_illegl);

    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});