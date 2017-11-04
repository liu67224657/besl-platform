function createSwfUploadObj(option) {
    var settings = {
        upload_url : urlUpload + "/json/upload/orgupload",
        post_params : {
            "at" : at
        },

        // File Upload Settings
        file_size_limit : "8 MB",    // 2MB
        file_types : "*.jpg;*.png;*.gif",
        file_types_description : "请选择图片",
        file_queue_limit : 1,

        file_dialog_complete_handler : option.fileDialogComplete,
        upload_start_handler:  option.uploadStart,
        upload_success_handler : option.uploadSuccess,
        upload_complete_handler : option.uploadComplete,

        // Button Settings
        button_image_url : "/static/images/uploadbutton.png",
        button_placeholder_id : option.buttonId,
        button_width: 61,
        button_height: 22,
        moving_average_history_size: 40,

        // Flash Settings
        flash_url : "/static/include/swfupload/swfupload.swf",
        flash9_url : "/static/include/swfupload/swfupload_fp9.swf",

        custom_settings:{imgId:option.imgId,corpObj:option.corpObj},

        // Debug Settings
        debug: false };
    var swfu = new SWFUpload(settings);

    return swfu;
}

var defaultOption = {
    areaId :'interface',
    sectionid:'anim_buttons',
    title:  '动态选图',
    corp:{
        '380x210（首页推荐图1）': [0,0,380,210],
        '380x320(首页推荐图2)': [0,0,380,320],
        '185x100（首页推荐图3）': [0,0,185,100],
        '130x98(条目文章图片)': [0,0,130,98],
        '80x90（首页文章图片）': [0,0,80,90],
        '188x141（列表显示图片）': [0,0,188,141]
    }
}

function createJCropArea(option) {
    var option = $.extend({}, defaultOption, option);

    $('#' + option.areaId).append(
            $('<fieldset style="white-space: normal"></fieldset>').attr('id', option.sectionid).append(
                    $('<legend></legend>').append(option.title)
            )
    );
    for (var ckey in option.corp) {
        $('#' + option.sectionid).append(
                $('<button name="selectbutton" />').append(ckey).click(function() {
                    var key = $(this).text();
                    option.corpclickHandler(option.corp[key], option.corpObj)
                })
        );
    }
    $('#' + option.sectionid).append(
            $('<button name="selectbutton"></button>').append('任意比例').click(function() {
                option.corpObj.animateTo(
                        [0,0,70,70],
                        function() {
                            option.corpObj.setOptions({ aspectRatio: 0 });
                            option.corpObj.release();
                        }
                );
                return false;
            })
    );

    $('#' + option.areaId).show();
}



