define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var joymealert = require('../common/joymealert');
    var handler = require('../../third/swfupload/gamehandler');
    require('../../third/swfupload/swfupload');
    require('../../third/swfupload/swfupload.queue');
    require('../../third/swfupload/fileprogress');
    $().ready(function () {
        header.noticeSearchReTopInit();

        $('#add_image').click(function() {
            biz.addHead(gameCode);
        });

        $('.editor-v-btn1').click(function() {
            var itemid = $(this).attr('data-itemid');
            biz.modifyHead(itemid, callback.modifycallback);
        });

        $('.editor-v-btn2').click(function() {
            var itemid = $(this).attr('data-itemid');
            biz.delHead(gameCode,itemid);
        });

        $('#remove_headSrc').live('click', function() {
            $('#image_area').html('<em style="color:#55779E">上传中</em>').hide();
            $('object[id^=SWFUpload_]').show();
        });

    });

    var biz = {
        addHead:function(gamecode) {
            var confirmOption = {
                title:'添加头图',
                text:'',
                width:308,
                confirmid:'add_head_image',
                html:'<form id="form_addheadImage" action="' + joyconfig.URL_WWW + '/game/' + gamecode + '/edit/addheadimage">' +
                        '<div class="bk_tc">' +
                        '<div class="bk_p clearfix" style="left:30px; margin-bottom:6px;">' +
                        '<span style="width:66px; text-align:right">头图名称：</span>' +
                        '<input type="text" name="name" class="bk_txt" style="width:190px">' +
                        '<div class="tipstext" id="error" style="display:none;padding-left: 76px; clear: both;"></div></div>' +
                        '<div class="bk_p clearfix" style="left:30px; margin-bottom:6px;">' +
                        '<span style="width:66px; text-align:right">链接地址：</span>' +
                        '<input type="text" name="link" class="bk_txt" style="width:190px">' +
                        '</div>' +
                        '<div style="padding:2px 0 0 76px;">' +
                        '<span id="upload_image">上传图片</span>' +
                        '<p id="image_area" style="display:none;padding-top:6px;"><em style="color:#55779E">上传中</em></p>' +
                        '<p style="padding-top:6px;"><em style="color:#999">(头图最佳尺寸为670*240px)</em></p>' +
                        '</div>' +
                        '</div>' +
                        '</form>',
                submitFunction:function() {
                    var src = $('#headSrc').val();
                    if (src == null) {
                        $("#error").text('请上传一张670*240的图片').show();
                        return true;
                    }

                    $('#form_addheadImage').submit();
                },
                showFunction:function() {
                    if (window.swfheadImage != null) {
                        window.swfheadImage.destroy();
                    }
                    initSwfuHead();
                }
            }
            joymealert.prompt(confirmOption);
        },
        modifyHead:function(headid, callback) {
            $.post('/json/game/getheaditem', {itemid:headid}, function(req) {
                var result = eval('(' + req + ')');
                if (result.status_code == '0') {
                    joymealert.alert({text:result.msg});
                    return;
                }
                callback(result);
            })
        },
        delHead:function(gamecode,headid) {
            var confirmOption = {
                title:'删除头图',
                text:'',
                width:308,
                confirmid:'add_head_image',
                html:'<form id="form_delheadImage" action="' + joyconfig.URL_WWW + '/game/' + gamecode + '/edit/delheadimage">' +
                        '<input type="hidden" id="imageid" name="imageid" value="' + headid + '" />' +
                        '<div class="delete_alert">' +
                        '<p style="line-height:24px; padding-top:14px;">确定要将头图删除吗？<br>删除后将无法恢复</p>' +
                        '</div>' +
                        '</form>',
                submitFunction:function() {
                    $('#form_delheadImage').submit();
                },
                showFunction:function() {
                    if (window.swfheadImage != null) {
                        window.swfheadImage.destroy();
                    }
                    initSwfuHead();
                }
            }
            joymealert.prompt(confirmOption);
        }
    };

    var callback = {
        modifycallback:function(result) {
            var item = result.result[0];
            if (item == null) {
                joymealert.alert({text:'编辑失败，头图不存在'});
                return;
            }

            var confirmOption = {
                title:'编辑头图',
                text:'',
                width:308,
                confirmid:'add_head_image',
                html:'<form id="form_modifyheadImage" action="' + joyconfig.URL_WWW + '/game/' + gameCode + '/edit/modifyheadimage">' +
                        '<input type="hidden" id="imageid" name="imageid" value="' + item.itemId + '" />' +
                        '<div class="bk_tc">' +
                        '<div class="bk_p clearfix" style="left:30px; margin-bottom:6px;">' +
                        '<span style="width:66px; text-align:right">头图名称：</span>' +
                        '<input type="text" name="name" value="' + item.displayInfo.subject + '" class="bk_txt" style="width:190px">' +
                        '<div class="tipstext" id="error" style="display:none;padding-left: 76px; clear: both;"></div></div>' +
                        '<div class="bk_p clearfix" style="left:30px; margin-bottom:6px;">' +
                        '<span style="width:66px; text-align:right">链接地址：</span>' +
                        '<input type="text" name="link" value="' + item.displayInfo.linkUrl + '" class="bk_txt" style="width:190px">' +
                        '</div>' +
                        '<div style="padding:2px 0 0 76px;">' +
                        '<span id="upload_image">上传图片</span>' +
                        '<p id="image_area" style="padding-top:6px;">' +
                        '<input type="hidden" id="headSrc" name="src" value="' + item.displayInfo.iconUrl + '" />' +
                        '<em style="color:#55779E">' + subfilename(item.displayInfo.iconUrl, 5) + '</em>' +
                        '<i id="remove_headSrc" style="cursor:pointer; margin-left:4px;">X</i>' +
                        '<p style="padding-top:6px;"><em style="color:#999">(头图最佳尺寸为670*240px)</em></p>' +
                        '</div>' +
                        '</div>' +
                        '</form>',
                submitFunction:function() {
                    var src = $('#headSrc').val();
                    if (src == null) {
                        $("#error").text('请上传一张670*240的图片').show();
                        return true;
                    }

                    $('#form_modifyheadImage').submit();
                },
                showFunction:function() {
                    if (window.swfheadImage != null) {
                        window.swfheadImage.destroy();
                    }
                    initSwfuHead();
                    $('object[id^=SWFUpload_]').hide();
                }
            }
            joymealert.prompt(confirmOption);

        }
    }

    var subfilename = function (imgname, l) {
        var splitarr = imgname.split(".");
        var typestr = "." + splitarr[splitarr.length - 1];
        var ps = imgname.indexOf(typestr);
        var namestr = imgname.substr(0, ps);
        if (namestr.length > l) {
            return  "…" + namestr.charAt(namestr.length - 1) + typestr;
        }
        return imgname;
    }

    function initSwfuHead() {
        var swfuHeadSettings = {
            upload_url :joyconfig.urlUpload + "/json/upload/orgupload",
            post_params : {
                "at" : joyconfig.token
            },
            // File Upload Settings
            file_size_limit : "3 MB",
            file_types : "*.jpg;*.png;*.gif",
            file_types_description : "请选择图片",
            file_upload_limit : 0,
            file_queue_limit : 0,
            file_dialog_complete_handler: handler.fileDialogComplete,
            upload_start_handler : handler.uploadStart,
            upload_success_handler : handler.uploadSuccess,
            file_queue_error_handler : handler.fileQueueError,

            // Button Settings
            button_image_url : joyconfig.URL_LIB + "/static/theme/default/img/upload-btn.jpg",
            button_placeholder_id : "upload_image",
            button_width: 56,
            button_height: 20,
            button_window_mode: SWFUpload.WINDOW_MODE.OPAQUE,
            button_cursor: SWFUpload.CURSOR.HAND,
            button_action:SWFUpload.BUTTON_ACTION.SELECT_FILE,//控制选择文件窗口.选择文件个数 -100选择一个文件 -110选择多个文件
            // Flash Settings
            flash_url : joyconfig.URL_LIB + "/static/third/swfupload/swfupload.swf",
            flash9_url : joyconfig.URL_LIB + "/static/third/js/swfupload/swfupload_fp9.swf",

            custom_settings : {
                subfilenameFunction:subfilename
            }
        }
        window.swfheadImage = new SWFUpload(swfuHeadSettings);
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});