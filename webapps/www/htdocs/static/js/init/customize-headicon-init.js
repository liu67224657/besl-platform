define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var headicon = require('../page/headiconqiniu');
    var header = require('../page/header');
    require('../common/tips');
    require('../../third/ui/jquery.ui.min')($);

    $(document).ready(function() {
        $('#headIconSetDiv').hide();
        //追加图片删除样式：X
        $("li[name=delHeadicon]").live('mouseenter mouseleave',
            function(event) {
                if (event.type == 'mouseenter') {
                    $(this).append('<div class="set_avatar_close"></div>');
                } else if (event.type == 'mouseleave') {
                    $(this).find('div').remove();
                }
            });
        $(".set_avatar_close").live("click", function() {
            headicon.confirmRemoveHeadIcon($(this).parent());
        });

        var Qiniu = new QiniuJsSDK();
        var uploader = Qiniu.uploader({
            runtimes: 'html5,flash,html4',    //上传模式,依次退化
            browse_button: 'upButton',       //上传选择的点选按钮，**必需**
            //uptoken_url: '/token',
            //Ajax请求upToken的Url，**强烈建议设置**（服务端提供）
            uptoken: $('#input_token').val(),
            //若未指定uptoken_url,则必须指定 uptoken ,uptoken由其他程序生成
            unique_names: true,
            // 默认 false，key为文件名。若开启该选项，SDK会为每个文件自动生成key（文件名）
            //save_key: true,
            // 默认 false。若在服务端生成uptoken的上传策略中指定了 `sava_key`，则开启，SDK在前端将不对key进行任何处理
            domain: 'http://joymepic.joyme.com/',
            //bucket,下载资源时用到，**必需**
            container: 'szlistBtn',           //上传区域DOM ID，默认是browser_button的父元素，
            max_file_size: '8mb',           //最大文件体积限制
            flash_swf_url: 'js/plupload/Moxie.swf',  //引入flash,相对路径
            max_retries: 3,                   //上传失败最大重试次数
            dragdrop: true,                   //开启可拖曳上传
            drop_element: 'szlistBtn',        //拖曳上传区域元素的ID，拖曳文件或文件夹后可触发上传
            chunk_size: '4mb',                //分块上传时，每片的体积
            auto_start: true,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
            init: {
                'FilesAdded': function (up, files) {
                    var i = 1;
                    plupload.each(files, function (file) {
                        files.splice(i,1);
                        // 文件添加进队列后,处理相关的事情
                    });
                },
                'BeforeUpload': function (up, file) {
                    var type = file.name.substring(file.name.lastIndexOf('.'),file.name.length).toUpperCase();
                    if(type != '.JPG' && type != '.PNG' && type != '.GIF' && type != '.JPEG'){
                        this.trigger('Error', {
                            code : -1,
                            message : "只能上传*.jpg,*.png,*.gif,*.jpeg的图片",
                            file : file
                        });
                        return false;
                    }
                    if (file.size >= 1024 * 1024 * 8) {
                        this.trigger('Error', {
                            code : -1,
                            message : "上传的图片大小不超过8mb",
                            file : file
                        });
                        return false;
                    }
                },
                'UploadProgress': function (up, file) {
                    // 每个文件上传时,处理相关的事情
                },
                'FileUploaded': function (up, file, info) {
                    try {
                        var domain = up.getOption('domain');
                        var res = Qiniu.parseJSON(info);
                        var imgLink = Qiniu.imageView2({
                            mode: 2,  // 缩略模式，共6种[0-5]
                            w: 245,   // 具体含义由缩略模式决定
                            h: 245,   // 具体含义由缩略模式决定
                            q: 100,   // 新图的图像质量，取值范围：1-100
                            format: 'jpg'  // 新图的输出格式，取值范围：jpg，gif，png，webp等
                        }, res.key);

                        headicon.initHeadIcon(imgLink);
                        $('#headIconSetDiv').show();
                    } catch (ex) {
                        this.debug(ex);
                    }
                },
                'Error': function (up, err, errTip) {
                    alert(errTip);
                    //上传出错时,处理相关的事情
                },
                'UploadComplete': function () {

                },
                'Key': function (up, file) {
                    // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                    // 该配置必须要在 unique_names: false , save_key: false 时才生效
                    var key = "";
                    // do something with key here
                    return key
                }
            }

        });

        $("#sortableul").not(".set_avatar_upbut").sortable({scrollSensitivity: 90,update:function() {
            headicon.saveHeadIcons();
        },'option':'original',placeholder: 'ui-state-highlight',opacity: 0.6,items:'> li[name=delHeadicon]'});

        $("#headIconSet span a img").live('mouseover moseout',
            function(event) {
                if (event.type == 'mouseover') {
                    $(this).parent().append('<em></em>');
                } else {
                    setTimeout(function() {
                        $(this).parent().find('em').remove();
                    }, 1000)
                }
            });

        $("#sortableul li em").live('click', function() {
            $(this).parent().parent().remove();
            headicon.saveHeadIcons();
            headicon.resetUpLoad();
        });
        header.noticeSearchReTopInit();
    });
});