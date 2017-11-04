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
        file_size_limit : 2 * 1024 + "",
        file_types : "*.jpg;*.png;",
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
        if (provinceInitId != '-1' && provinceInitId != '36') {
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
            if ($(this).val() != "-1" && $(this).val() != '36') {
                //调用ajax方法查询
                $.post("/json/profile/customize/getsubregionbypid", {regionid:$(this).val()}, function(data) {
                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj.status_code == '1') {
                        $("#city").html('<option value="-1" id="city_init">请选择</option>');
                        $.each(jsonObj.result, function(i, val) {
                            var str = '<option value="' + val.regionId + '">' + val.regionName + '</option>';
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
            desc = desc.replace(/(\s+)/g, "");
            if (desc.length == 0 || desc.length > 18) {
                alertOption.text = "认证信息不能为空，长度最多18个字";
                alertOption.title = "认证信息"
                joymealert.alert(alertOption);
                return false;
            }
            if (!ajaxverify.verifyDesc(desc)) {
                alertOption.text = "认证信息含有敏感内容";
                alertOption.title = "认证信息"
                joymealert.alert(alertOption);
                return false;
            }

            var contacts = $('#input_contacts').val();
            contacts = contacts.replace(/(\s+)/g, "");
            var nameReg = /^[a-zA-Z\u4e00-\u9fa5]+$/;
            if (contacts.length == 0 || !nameReg.test(contacts) || contacts.length > 18) {
                alertOption.text = "联系人不能为空，且只能由18个字以内的英文或汉字组成";
                alertOption.title = "联系人"
                joymealert.alert(alertOption);
                return false;
            }
            if (!ajaxverify.verifyDesc(contacts)) {
                alertOption.text = "联系人含有敏感内容";
                alertOption.title = "联系人"
                joymealert.alert(alertOption);
                return false;
            }

            var emailReg = /\b(^['_A-Za-z0-9-]+(\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$)\b/;
            var email = $('#input_email').val();
            if (email.length == 0 || email.length > 40) {
                alertOption.text = "邮箱不为空，且最多40个字符";
                alertOption.title = "邮箱"
                joymealert.alert(alertOption);
                return false;
            }
            if (!emailReg.test(email)) {
                alertOption.text = "邮箱格式不正确";
                alertOption.title = "邮箱"
                joymealert.alert(alertOption);
                return false;
            }

            var qqReg = /^[0-9]+$/;
            var qq = $('#input_qq').val();
            qq = qq.replace(/(\s+)/g, "");
            if (!qqReg.test(qq) || qq.length < 5 || qq.length > 20) {
                alertOption.text = "QQ必须是5-20个字符长度的数字";
                alertOption.title = "QQ"
                joymealert.alert(alertOption);
                return false;
            }

            var phoneReg = /^([0-9]+(([-]*)|([+]*))[0-9]+)+$/;
            var phone = $('#input_phone').val();
            phone = phone.replace(/(\s+)/g, "");
            if (phone.length < 6 || phone.length > 20 || !phoneReg.test(phone)) {
                alertOption.text = "电话格式不正确(必须以数字开头和结尾，6-20个字符长度，中间可以有“-”或“+”隔开)";
                alertOption.title = "电话"
                joymealert.alert(alertOption);
                return false;
            }

            var category = $('#input_category').val();
            if (category != '0') {
                var company = $('#input_company').val();
                company = company.replace(/(\s+)/g, "");
                if (company.length == 0 || company.length > 18) {
                    alertOption.text = "公司不能为空，且18个字以内";
                    alertOption.title = "公司"
                    joymealert.alert(alertOption);
                    return false;
                }

                var province = Number($('#province').val());
                if (province < 0) {
                    alertOption.text = "请正确选择公司省份";
                    alertOption.title = "省份"
                    joymealert.alert(alertOption);
                    return false;
                }

                var city = Number($('#city').val());
                if (city < 0) {
                    alertOption.text = "请正确选择公司城市";
                    alertOption.title = "城市"
                    joymealert.alert(alertOption);
                    return false;
                }
            }

            var pic = $('#input_icon').val();
            if (pic.length == 0) {
                alertOption.text = "身份证/执照，不能为空";
                alertOption.title = "身份证/执照"
                joymealert.alert(alertOption);
                return false;
            }
        });

        //回车事件
        $("#input_desc").keydown(function(e) {
            if (e.keyCode == 13) {
                $("#form-submit").submit();
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
                    alertOption.text = "登录失败，请重新登陆！";
                    alertOption.title = "登录"
                    joymealert.alert(alertOption);
                } else {
                    if (jsonData.msg == '') {
                        alertOption.text = "请检查您上传的附件是否符合要求";
                        alertOption.title = "上传"
                        joymealert.alert(alertOption);
                    } else {
                        alertOption.text = jsonData.msg;
                        alertOption.title = "上传"
                        joymealert.alert(alertOption);
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
            $('#input_desc').focus();
        } catch (ex) {
            this.debug(ex);
        }
    }

//    require.async('../common/google-statistics');
//    require.async('../common/bdhm')
});





