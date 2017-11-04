define(function (require, exports, module) {
    require('../../third/jcrop/jquery.Jcrop.css');
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.form');
    var common = require('../common/common');
    var headiconbiz = require('../biz/headicon-biz');
    require('../../third/jcrop/jquery.Jcrop.min')($);
    var joymealert = require('../common/joymealert');

    var jcrop_api, boundx, boundy;

    var headicon = {
        //取消头像上传
        cancelHeadIcon: function () {
            $('#headIconSetDiv').css("display", "none");
            $('#previewDiv').html('');
            $('#previewSetDiv').html('');
        },
        //图片上传成功后的设置
        initHeadIcon: function (jsonData) {
            var imgSrc = common.genImgDomain(jsonData.result[0].b, joyconfig.DOMAIN);
            //jcrop_api.release();
            $('#previewDiv').html('<p><img id="preview" src="' + imgSrc + '"/></p>');
            $('#previewSetDiv').html('<span><img id="face_img_id" src="' + imgSrc + '"/></span>');
            $("#tumName").val(jsonData.result[0].b);
            this.initJcrop();
            $("#thumbHref").removeClass('graybtn').addClass('submitbtn');
            $("#thumbHref").live('click', function () {
                thumbnailHeadIcon(loadingThumb, showHeadIcon, uploadComplete, errorCallback);
            });
            $("#btnCancel").live('click', function () {
                headicon.cancelHeadIcon();
            });

            $('.custom-img-advance').click();
            $('.custom-img-advance').css('display', 'block');
        },
        //图片上传成功后的设置
        initHeadIconRegOk: function (jsonData) {
            var imgSrc = common.genImgDomain(jsonData.result[0].b, joyconfig.DOMAIN);
            //jcrop_api.release();
            $('#previewDiv').html('<p><img id="preview" src="' + imgSrc + '"/></p>');
            $('#previewSetDiv').html('<span><img id="face_img_id" src="' + imgSrc + '"/></span>');
            $("#tumName").val(jsonData.result[0].b);
            this.initJcrop();
            $("#thumbHref").removeClass('graybtn').addClass('submitbtn');
            $("#thumbHref").live('click', function () {
                thumbnailHeadIcon(loadingThumb, showHeadIconRegOk, uploadComplete, errorCallback);
            });
            $("#btnCancel").live('click', function () {
                headicon.cancelHeadIcon();
            });

            $('.custom-img-advance').click();
            $('.custom-img-advance').css('display', 'block');
        },
        //上传头像前的设置 user define
        resetHeadIcon: function () {
            $('#headIconSetDiv').css("display", "block");
            $("#thumbHref").removeClass('submitbtn').addClass('graybtn');
            $("#thumbHref").die('click');
            $('#previewDiv').html('<p></p>');
            $('#previewSetDiv').html('<p></p>');
            if (jcrop_api != null) {
                jcrop_api.destroy();
            }

        },
        //上传头像前的设置 regOk
        resetHeadIconRegOk: function () {
            //隐藏显示层
            $('#headIconDiv').css("display", "none");
            $('#headIconSetDiv').css("display", "block");
            $("#thumbHref").removeClass('submitbtn').addClass('graybtn');
            $("#thumbHref").die('click');
            $('#previewDiv').html('<p></p>');
            $('#previewSetDiv').html('<p></p>');
            if (jcrop_api != null) {
                jcrop_api.destroy();
            }
        },
        initJcrop: function () {
            //$('.requiresjcrop').hide();
            $('#face_img_id').Jcrop({
                allowSelect: false,
                onSelect: jCropcallback,
                aspectRatio: 1,
                minSize: [58, 58],
                setSelect: [0, 0, 200, 200],
                onChange: jCropcallback
                //onRelease: releaseCheck
            }, function () {
                var bounds = this.getBounds();
                boundx = bounds[0];
                boundy = bounds[1];
                jcrop_api = this;
                //jcrop_api.animateTo([100,100,400,300]);
                //$('.requiresjcrop').show();
            });

        },

        resetUpLoad: function () {
            if ($('#.set_avatar_show ul li[name=delHeadicon]').size() < 8) {
                $("#upButton").css("display", "block");
                $("#szlistBtn").show();
            } else {
                $("#upButton").css("display", "none");
                $("#szlistBtn").hide();

            }
        },
        //保存头像
        saveHeadIcons: function () {
            $("#headIconForm").ajaxForm(function (data) {
                var jsonObj = eval('(' + data + ')');
                common.locationLoginByJsonObj(jsonObj);
                //弹出层提示
                if (jsonObj.status_code == "1") {
                    //jmAlert("头像保存成功", true);
                } else {
                    var alertOption = {text: jsonObj.msg, tipLayer: true, textClass: "tipstext"};
                    joymealert.alert(alertOption);
                }
            });
            $("#headIconForm").submit();
        },

        saveHeadIconsRegOk: function () {
            headiconbiz.save($("#headIconInput").val());
        },

        checkFileSize: function (file) {
            var flag = true;
            if (file.size >= 1024 * 1024 * 8) {
                flag = false;
            }
            return flag;
        },

        confirmRemoveHeadIcon: function (jqObj) {
            //confirm
            var joymealert = require('../common/joymealert');
            var offSet = jqObj.offset();
            var tips = '确定要删除此头像吗?'

            var removeHeadicon = function () {
                jqObj.remove();
                headicon.saveHeadIcons();
                headicon.resetUpLoad();
            }

            var confirmOption = {
                confirmid: "removeHeadIcon",
                offset: "Custom",
                offsetlocation: [offSet.top, offSet.left + 95],
                text: tips,
                width: 229,
                submitButtonText: '确 定',
                submitFunction: removeHeadicon,
                cancelButtonText: '取 消',
                cancelFunction: null};
            joymealert.confirm(confirmOption);

        }

    }

    var thumbnailHeadIcon = function (loadingCallback, callback, completeCallback, errorCallback) {
        headiconbiz.thumbnail($("#tumName").val(), $('#x1').val(), $('#y1').val(), $('#x2').val(), $('#y2').val(), $('#ow').val(), $('#oh').val(), loadingCallback, callback, completeCallback, errorCallback);
    };

    var loadingThumb = function () {
        $('#thumbHref').attr('class', 'loadbtn').html('<span><em class="loadings"></em>保存中</span>')
    };

    var uploadComplete = function () {
        $('#thumbHref').attr('class', 'submitbtn').html('<span>保存头像</span>');
    };

    var errorCallback = function () {
        $('#headicon_error').html('保存失败，请重试');
    }

    //剪切头像成功回调
    var showHeadIcon = function (jqObj) {

        if (jqObj.status_code == '-1') {
            common.locationLoginByJsonObj(jqObj);
            return;
        } else if (jqObj.rs == '1') {
            //剪切图片成功----显示在最后一个位置
            var imgSrc = jqObj.url;
//                common.genImgDomain(jqObj.result[0].m, joyconfig.DOMAIN);
            var str = '<li name="delHeadicon"><a href="javascript:void(0)">' +
                '<img src="' + imgSrc + '" width="90px" height="90px"/></a>' +
                '<input type="hidden" value="' + jqObj.url + '" name="headIcon"/>' +
                '</li>'

            $('#szlistBtn').before(str);
            //判断是否已经上传了8个头像
            headicon.resetUpLoad();
            jcrop_api.destroy();
            $('#headIconSetDiv').css("display", "none");
            //同步数据库
            headicon.saveHeadIcons();
        } else {
            $('#headicon_error').html(jqObj.msg);
        }
    };

    var showHeadIconRegOk = function (jsonObj) {
        if (jsonObj.status_code == '-1') {
            common.locationLoginByJsonObj(jsonObj);
            return;
        } else if (jsonObj.status_code == '1') {
            //  todo
            $("#headIconInput").val(jsonObj.result[0].b);
            //剪切图片成功----显示
            $("#headIcon")[0].src = common.genImgDomain(jsonObj.result[0].m, joyconfig.DOMAIN) + '?' + Math.random();
            jcrop_api.destroy();
            $('#headIconSetDiv').css("display", "none");
            $('#headIconDiv').css("display", "block");
            //同步数据库
            headicon.saveHeadIconsRegOk();
        } else {
            $('#headicon_error').html(jqObj.msg);
        }
    };

    var jCropcallback = function (c) {
        if (parseInt(c.w) > 0) {
            var rx = 150 / c.w;
            var ry = 150 / c.h;
            $('#preview').css({
                width: Math.round(rx * boundx) + 'px',
                height: Math.round(ry * boundy) + 'px',
                marginLeft: '-' + Math.round(rx * c.x) + 'px',
                marginTop: '-' + Math.round(ry * c.y) + 'px'
            });
        }

        $('#x1').val(c.x);
        $('#y1').val(c.y);
        $('#x2').val(c.x2);
        $('#y2').val(c.y2);
        $('#ow').val(c.w);
        $('#oh').val(c.h);
    };

    return headicon;
});
