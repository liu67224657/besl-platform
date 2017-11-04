define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var loginBiz = require('../biz/login-biz');
    var joymealert = require('../common/joymealert');
    require('../../third/swfupload/swfupload');
    require('../../third/swfupload/swfupload.queue');
    require('../../third/swfupload/fileprogress');
    var common = require('../common/common');
    require('../common/tips');
    require('../../third/ui/jquery.ui.min')($);

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
        file_size_limit : 80 * 80 + "",
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
        button_placeholder_id: "upload_button_icon",

        // The event handler functions are defined in handlers.js
        file_dialog_complete_handler: fileDialogComplete,
        upload_start_handler : uploadStart,
//            upload_progress_handler : uploadProgress,
        upload_success_handler : uploadSuccess,
        upload_complete_handler : uploadComplete,

        custom_settings : {
        }
    };
    var swfu = new SWFUpload(settings);

    var settingspic = {
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
        button_placeholder_id: "upload_button_pic_set",

        // The event handler functions are defined in handlers.js
        file_dialog_complete_handler: fileDialogCompletePic,
        upload_start_handler : uploadStartPic,
//            upload_progress_handler : uploadProgress,
        upload_success_handler : uploadSuccessPic,
        upload_complete_handler : uploadCompletePic,

        custom_settings : {
        }
    };
    var swfupic = new SWFUpload(settingspic);

    var index = Number(0);

    $(document).ready(function () {
        $('#a_button').click(function() {
            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }
        });

        $('#input_text_city').hide();
        $('#select_city').change(function() {
            if (this.value == "0") {
                $('#input_text_city').show();
            }
            else {
                $('#input_text_city').hide();
            }
        });

        $('#a_delete_pic').click(function() {
            if (index > 1) {
                $('#div_img>img:last-child').remove();
                index -= 1;
            } else if(index==1){

                $('#div_img>img:last-child').attr('src', '${URL_LIB}/static/theme/default/img/default.jpg');
                index = 0;
            }
            var picSet = new Array();
            $('img[name=imgpic]').each(function() {
                picSet.push($(this).attr('src'));
            });
            $('#input_pic_set').attr('value', picSet.join('@'));

        });

        $('#form_submit_create').bind('submit', function () {

            if (joyconfig.joyuserno == '') {
                loginBiz.maskLoginByJsonObj();
                return false;
            }

            var gameName = $('#input_text_game_name').val();
            if (gameName.length == 0 || gameName.length > 10) {
                alertOption.text = "游戏名不能为空，并且不超过10个字";
                alertOption.title = "游戏名称"
                joymealert.alert(alertOption);
                return false;
            }

            var icon = $('#input_icon').val();
            if (icon.length == 0) {
                alertOption.text = "请上传游戏图标";
                alertOption.title = "游戏图标"
                joymealert.alert(alertOption);
                return false;
            }

            var result = new Array();
            var i = Number(0);
            $("input[name = tag]:checkbox").each(function () {
                if ($(this).is(":checked")) {
                    result.push($(this).attr("value"));
                    i += 1;
                }
            });
            if (i <= 0 || i > 5) {
                alertOption.text = "至少选择1个标签，最多5个";
                alertOption.title = "游戏标签"
                joymealert.alert(alertOption);
                return false;
            }
            $('#input_hidden_tag').val(result.join(','));

            var companyName = $('#input_text_company_name').val();
            if (companyName.length == 0 || companyName.length > 20) {
                alertOption.text = "必须输入团队/公司名称，并且不超过20个字";
                alertOption.title = "团队/公司"
                joymealert.alert(alertOption);
                return false;
            }

            var peopleNumType = $('#select_people_num_type').val();
            if (peopleNumType.length == 0) {
                alertOption.text = "请选择团队/公司人数";
                alertOption.title = "团队/公司人数"
                joymealert.alert(alertOption);
                return false;
            }

            var cityId = $('#select_city').val();

            if (cityId.length == 0) {
                alertOption.text = "请选择城市";
                alertOption.title = "所在城市"
                joymealert.alert(alertOption);
                return false;
            }
            if ($('#orther_city').attr('selected') == true) {
                var city = $('#input_text_city').val();
                if (city.length == 0) {
                    alertOption.text = "请填写城市";
                    alertOption.title = "所在城市"
                    joymealert.alert(alertOption);
                    return false;
                }
            } else {
                $('#input_text_city').hide();
            }

            var coopType = Number(0);
            $("input[name = coopratetype]:checkbox").each(function () {
                if ($(this).is(":checked")) {
                    coopType += Number($(this).attr("value"));
                }
            });
            if (coopType == 0) {
                alertOption.text = "请选择合作方式，可以多选";
                alertOption.title = "合作方式"
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

            var phoneReg = /^([0-9]+(([-]*)|([+]*))[0-9]+)+$/;
            var phone = $('#input_phone').val();
            if (phone.length == 0 || !phoneReg.test(phone)) {
                alertOption.text = "电话格式不正确(必须以数字开头和结尾，中间可以有“-”或“+”隔开)";
                alertOption.title = "电话"
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

            var year = $('#input_yyyy').val();
            var month = $('#input_MM').val();
            if (year.length == 0 || month.length == 0) {
                alertOption.text = "请正确填写发行时间";
                alertOption.title = "发行时间"
                joymealert.alert(alertOption);
                return false;
            }
            $('#input_hidden_pub_date').val(year + '-' + month + '-01');

            var publishAreaType = Number(0);
            $("input[name = publisharea]:checkbox").each(function () {
                if ($(this).is(":checked")) {
                    publishAreaType += Number($(this).attr("value"));
                }
            });
            if (publishAreaType == 0) {
                alertOption.text = "请选择发行范围，可以多选";
                alertOption.title = "发行范围"
                joymealert.alert(alertOption);
                return false;
            }

            var picUrl = new Array();
            $('img[name=imgpic]').each(function() {
                picUrl.push($(this).attr('src'));
            });
            var picStr = $('#input_pic_set').val(picUrl.join('@'));
            if (index == 0) {
                alertOption.text = "请上传游戏截图";
                alertOption.title = "游戏截图"
                joymealert.alert(alertOption);
                return false;
            }

            var gamedesc = $('#textarea_game_desc').val();
            if (gamedesc.length == 0) {
                alertOption.text = "请填写游戏简介";
                alertOption.title = "游戏简介"
                joymealert.alert(alertOption);
                return false;
            }

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

    function fileDialogCompletePic(numFilesSelected, numFilesQueued) {
        try {
            this.startUpload();
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadStartPic(file) {
        $('#loading_pic_set').css('display', '');
    }

    function uploadSuccessPic(file, serverData) {

        try {
            var jsonData = eval('(' + serverData + ')');
            if (jsonData.status_code == "1") {
                var largeLogoSrc = common.genImgDomain(jsonData.result[0], joyconfig.DOMAIN);
                if (index == 0) {
                    $('#div_img>img:last-child').attr('src', largeLogoSrc);
                    $('#input_pic_set').attr('value', largeLogoSrc);
                } else if (index > 0 && index <= 5) {
                    $('#div_img').append('<img src="" name="imgpic"/>');
                    $('#div_img>img:last-child').attr('src', largeLogoSrc);
                    var result = new Array();
                    $('img[name=imgpic]').each(function() {
                        result.push($(this).attr('src'));
                    });
                    $('#input_pic_set').val(result.join('@'));
                } else if (index > 5) {
                    alertOption.text = "图片数量不能超过6张";
                    alertOption.title = "游戏截图"
                    joymealert.alert(alertOption);
                    return false;
                }
                index += 1;
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

    function uploadCompletePic(file) {
        try {
            if (this.getStats().files_queued <= 0) {
                $('#loading_pic_set').css('display', 'none');
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

//    require.async('../common/google-statistics');
//    require.async('../common/bdhm')
});





