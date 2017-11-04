define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var loginBiz = require('../biz/login-biz');
    var customize = require('../page/customize');
    var ajaxverify = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');
    require('../../third/swfupload/swfupload');
    require('../../third/swfupload/swfupload.queue');
    require('../../third/swfupload/fileprogress');
    var common = require('../common/common');
    require('../common/tips');
    require('../../third/ui/jquery.ui.min')($);
    require('../common/jquery.dropdownlist')($)
    require('../common/jquery.validate.min');

    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    var settings = {
        flash_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
        flash9_url : joyconfig.URL_LIB + "/static/third/js/swfupload/swfupload_fp9.swf",
        upload_url : joyconfig.urlUpload + "/json/upload/orgupload",
        post_params : {
            "at" : joyconfig.token
        },
        file_size_limit : "",
        file_types : "*.jpg;*.png;*.gif",
        file_types_description : "All Files",
        file_upload_limit : 0,
        file_queue_limit : 1,

        debug: false,

        // Button settings
        button_image_url:joyconfig.URL_LIB + '/static/theme/default/img/upload_pic_button.jpg',
        button_width: 52,
        button_height: 26,
        moving_average_history_size: 40,

        button_action : SWFUpload.BUTTON_ACTION.SELECT_FILE,
        button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
        button_cursor: SWFUpload.CURSOR.HAND,
        button_placeholder_id: "upload_button",

        // The event handler functions are defined in handlers.js
        file_dialog_complete_handler: fileDialogComplete,
        upload_start_handler : uploadStart,
//        upload_progress_handler : uploadProgress,
        upload_success_handler : uploadSuccess,
        upload_complete_handler : uploadComplete,

        custom_settings : {
            resource_path : joyconfig.DOMAIN,
            tdFilesUploaded : document.getElementById("tdFilesUploaded"),
            cancelButtonId : "btnCancel"
        }
    };
    var swfu = new SWFUpload(settings);

    $(document).ready(function () {

        $("#province").ylDropDownList();
        var initCityId = $('#input_city').val();
        var provinceInitId = $('#province').val();
        if (provinceInitId != '-1') {
            $.post("/json/profile/customize/getsubregionbypid", {regionid:provinceInitId}, function(data) {
                var jsonObj = eval('(' + data + ')');
                if (jsonObj.status_code == '1') {
                    $("#city").html('<option value="-1" id="city_init">请选择</option>');
                    $.each(jsonObj.result, function(i, val) {
                        if (val.regionId == initCityId) {
                            var str = '<option selected="selected" value="' + val.regionId + '">' + val.regionName + '</option>';
                        } else {
                            var str = '<option value="' + val.regionId + '">' + val.regionName + '</option>';
                        }
                        $("#city_init").after(str);
                    });
                    $("#city").next('div').remove();
                    $("#city").ylDropDownList();
                }
            });
        }

        //为省列表绑定js方法，获取区域列表
        $("#province").change(function() {
            if ($(this).val() != "-1") {
                //调用ajax方法查询
                $.post("/json/profile/customize/getsubregionbypid", {regionid:$(this).val()}, function(data) {
                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj.status_code == '1') {
                        $("#city").html('<option value="-1" id="city_init">请选择</option>');
                        $.each(jsonObj.result, function(i, val) {
                            if (val.regionName == initCityName) {
                                var str = '<option selected="selected" value="' + val.regionId + '">' + val.regionName + '</option>';
                            } else {
                                var str = '<option value="' + val.regionId + '">' + val.regionName + '</option>';
                            }
                            $("#city_init").after(str);
                        });
                        $("#city").next('div').remove();
                        $("#city").ylDropDownList();
                    }
                });
            } else {
                $("#city").html('<option value="-1" id="city_init">请选择</option>');
                $("#city").next('div').remove();
            }

        });

        customize.setCity($("#joycityid").val());

        $('#form-submit').bind('submit', function () {

            var desc = $('#input_desc').val();
            if (desc.length == 0) {
                alertOption.text = "认证信息不能为空";
                alertOption.title = "认证信息"
                joymealert.alert(alertOption);
                return false;
            }

            var contacts = $('#input_contacts').val();
            var nameReg = /^[a-zA-Z\u4e00-\u9fa5]+$/;
            if (contacts.length == 0 || !nameReg.test(contacts)) {
                alertOption.text = "联系人不能为空，且只能由英文或汉字组成";
                alertOption.title = "联系人"
                joymealert.alert(alertOption);
                return false;
            }

            var emailReg = /\b(^['_A-Za-z0-9-]+(\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$)\b/;
            var email = $('#input_email').val();
            if (!emailReg.test(email)) {
                alertOption.text = "邮箱格式不正确";
                alertOption.title = "邮箱"
                joymealert.alert(alertOption);
                return false;
            }

            var qqReg = /^[0-9]+$/;
            var qq = $('#input_qq').val();
            if (!qqReg.test(qq)) {
                alertOption.text = "请填写QQ，且只能是数字";
                alertOption.title = "QQ"
                joymealert.alert(alertOption);
                return false;
            }

            var phoneReg = /^([0-9]+(([-]*)|([+]*))[0-9]+)+$/;
            var phone = $('#input_phone').val();
            if (phone.length == 0 || !phoneReg.test(phone)) {
                alertOption.text = "电话格式不正确(必须以数字开头和结尾，中间可以有“-”或“+”隔开)";
                alertOption.title = "电话"
                joymealert.alert(alertOption);
                return false;
            }

            var company = $('#input_company').val();
            if (company.length == 0) {
                alertOption.text = "公司不能为空";
                alertOption.title = "公司"
                joymealert.alert(alertOption);
                return false;
            }

            var province = Number($('#province').val());
            if (province < 0) {
                alertOption.text = "公司地址不能为空";
                alertOption.title = "地址"
                joymealert.alert(alertOption);
                return false;
            }

        });
        $('.submitbtn').click(function () {
            $('#form-submit').submit();
        });
    });

    function fileDialogComplete(numFilesSelected, numFilesQueued) {
        try {
            this.startUpload();
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadStart(file) {
        $('#loading').css('display', '');
    }

//    function uploadProgress(file, bytesLoaded) {
//
//        try {
//            var percent = Math.ceil((bytesLoaded / file.size) * 100);
//
//            var progress = new FileProgress(file, this.customSettings.upload_target);
//            progress.setProgress(file.id, percent);
//            progress.setStatus(" Uploading...");
//            progress.toggleCancel(true, this);
//        } catch (ex) {
//            this.debug(ex);
//        }
//    }

    function uploadSuccess(file, serverData) {
        try {
            var jsonData = eval('(' + serverData + ')');
            if (jsonData.status_code == "1") {
                var largeLogoSrc = common.genImgDomain(jsonData.result[0], joyconfig.DOMAIN);
                $('#img_icon').attr('src', largeLogoSrc);
                $('#input_icon').attr('value', largeLogoSrc);
            } else {
                if (jsonData.msg == 'token_faild') {
                    alert('登录失败');
                } else {
                    if (jsonData.msg == '') {
                        alert('上传失败');
                    } else {
                        alert(jsonData.msg);
                    }
                }
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadComplete(file) {
        try {
            if (this.getStats().files_queued <= 0) {
                $('#loading').css('display', 'none');
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

//    require.async('../common/google-statistics');
//    require.async('../common/bdhm')
});





