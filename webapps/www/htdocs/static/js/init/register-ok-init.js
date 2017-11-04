define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../../third/swfupload/swfupload');
    var handler = require('../../third/swfupload/handlers_faceimg_userdetail');
    var common = require('../common/common');
    var headicon = require('../page/headicon');
    var header = require('../page/header');
    var register = require('../page/register');
    require('../common/tips');

    var swfu;

    $(document).ready(function() {
        var settings = {
            flash_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
            flash9_url : joyconfig.URL_LIB + "/static/third/js/swfupload/swfupload_fp9.swf",
            upload_url : joyconfig.urlUpload + "/json/upload/face",
            post_params : {
                "at" : joyconfig.token
            },
            file_size_limit : 1024 * 1024 * 8 + "",
            file_types : "*.jpg;*.png;*.gif;*.bmp",
            file_types_description : "All Files",
            file_upload_limit : 0,
            file_queue_limit : 1,

            debug: false,

            // Button settings
            button_image_url: joyconfig.URL_LIB + "/static/theme/default/img/upicbnt.png",
            button_width: "146",
            button_height: "26",
            button_placeholder_id: "spanButtonPlaceHolder",
            button_cursor: SWFUpload.CURSOR.HAND,
            moving_average_history_size: 40,


            // The event handler functions are defined in handlers.js
            swfupload_preload_handler : handler.preLoad,
            swfupload_load_failed_handler : handler.loadFailed,
            file_dialog_complete_handler: handler.fileDialogComplete,
            upload_start_handler : handler.uploadStartRegOk,
            upload_progress_handler : handler.uploadProgress,
            upload_success_handler : handler.uploadSuccessRegOk,
            upload_complete_handler : handler.uploadComplete,

            custom_settings : {
                resource_path : joyconfig.DOMAIN,
                tdFilesUploaded : document.getElementById("tdFilesUploaded"),
                cancelButtonId : "btnCancel"
                //tdErrors : document.getElementById("tdErrors")
            }
        };
        $(".headt").css('height','59px');
        swfu = new SWFUpload(settings);

        $(".regsave").bind("click", function() {
            register.saveInfo();
        });
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm');

});