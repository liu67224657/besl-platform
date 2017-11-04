/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-14
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var pop = require('../common/jmpopup');
    var postbiz = require('../biz/post-biz');
    var joymealert = require('../common/joymealert');
    var tag = require('./tag');
    var common = require('../common/common');
    var postapp = {
        appinit:function(appDom, config, appId, hideEditorFocusBtn) {
            var offset = appDom.offset();

            var popConfig = {forclosed :true,
                containTitle : true,
                popwidth: 380,
                offsetlocation:[offset.top + 31,offset.left],
                containFoot:false,
                hideCallback:function() {
                    hideEditorFocusBtn()
                    $('#error_app').text('');
                    $('#text_ios_url').val('');
                }
            };
            var popConfig = $.extend(config, popConfig);
            if ($("#" + appId).length > 0) {
                pop.resetOffset(popConfig, appId);
            } else {
                var htmlObj = new Object();
                htmlObj['id'] = appId;
                htmlObj['html'] = '<div class="appcon"><p><input type="text" name="" style="color:#CCCCCC" class="apptext" value="请输入苹果应用地址" id="text_ios_url">' +
                        '<a class="submitbtn" id="link_post_ios"><span>确 定</span></a></p>' +
                        '<div class="appabout clearfix"><p id="error_app" class="tipstext"></p>' +
                        '<div class="apprelcon">' +
                        '<dl><dt>如何获取苹果应用地址？ </dt>' +
                        '<dd>● 打开itunes的app store</dd>' +
                        '<dd>●  找到想发布的应用</dd>' +
                        '<dd>● 右键点击logo拷贝链接 &nbsp;&nbsp;&nbsp;粘贴到上面的输入框中 </dd>' +
                        '</dl></div><a href="javascript:void(0)">' +
                        '<img src="' + joyconfig.URL_LIB + '/static/theme/default/img/appdemo.jpg"></a><p></p></div></div>';
                htmlObj['title'] = 'APP应用 '
                pop.popupInit(popConfig, htmlObj);

            }
            $("#text_ios_url").focus();
        },
        postAppMask : function(resMsg, url) {
            if (resMsg.status_code == '0') {
                $('#ios_error').text(resMsg.msg);
                return;
            } else {
                $(".pop").hide();
                loadAppMask(resMsg, 'relAppDialog', url)
            }
        }
    }

    var loadAppMask = function (appObj, appid, url) {
        $("#" + appid).remove();
        window.isOut = null;
        var device = '';
        $.each(appObj.deviceSet, function(i, val) {
            if (val.code.toLowerCase() == 'ipad') {
                device += '<span class="ipad"></span>';
            } else if (val.code.toLowerCase() == 'iphone') {
                device += '<span class="iphone"></span>';
            }
        });
        var rating = appObj.currentRating * 13;
        var ratingHtml = '<span class="star" style="width:' + rating + 'px;display:inline-block"></span>';

        var screenShot = '';
        if (appObj.screenShot.length > 0) {
            screenShot += '<div class="appchoose clearfix"><h3>选择您要插入的图片</h3><div class="choose_appic" id="appShow"><ul class="long_list_ok clearfix">';
            for (var i = 0; i < appObj.screenShot.length; i++) {
                screenShot += '<li><p><a class="current" href="javascript:void(0);" title="' + appObj.screenShot[i] + '"><img src="' + appObj.screenShot[i] + '" width="150" height="114"></a>' +
                        '<input class="checkbtn" name="appScreenShot" type="checkbox" checked="checked" value="' + appObj.screenShot[i] + '" /></p></li>';
            }
            screenShot += '</ul></div></div>';
        }
        var imgCount = common.regImgNum(CKEDITOR.instances['text_content']);
        var iosBox = '<div class="appuse"><div class="appwrapper clearfix"><div class="appwraper">' +
                '<h3>' + appObj.appName + '</h3>' +
                '<div class="aapl"><div class="aaplmask"></div><img src="' + appObj.icon + '" width="177" height="178"></div>' +
                '<div class="appcontent">' +
                '<p>类别：' + appObj.appCategory + '</p>' +
                '<p>价格：' + appObj.price + '</p>' +
                '<p>评分：' + ratingHtml + '' + '</p>' +
                '<p>大小：' + appObj.fileSize + '</p>' +
                '<p class="pc">适用机型：' + device + '</p>' +
                '<div class="price"></div></div></div>' +
                '<dl class="tuijian clearfix">' +
                '<dt>推荐理由</dt>' +
                '<dd><textarea id="iosDesc" rows="2" cols="" class="appitt" style="font-family:Tahoma, \'宋体\';"></textarea></dd>' +
                '</dl>' + screenShot + '</div> ';
        var appJmPopConfig = {
            pointerFlag : false,//是否有指针
            tipLayer : true,//是否遮罩
            containTitle : true,//包含title
            containFoot : true,//包含footer
            forclosed:true,
            offset:"",
            popwidth:588,
            popscroll:true,
            isremovepop:true,
            allowmultiple:true,
            isfocus:false,
            popzindex:18500
        };
        var htmlObj = new Object();
        htmlObj['id'] = 'appUpload';
        htmlObj['html'] = iosBox;
        htmlObj['input'] = '<div class="longpic_ft clearfix">' +
                '<span><input type="checkbox" checked="checked" id="appChkAll"></span>' +
                '<span>全选&nbsp;</span>' +
                '<span class="limit" id="ioslimit">你只能保留' + (40 - imgCount) + '张图片<b>您已选择了' + appObj.screenShot.length + '张图片</b> </span>' +
                '<span class="ft_btn"><a class="submitbtn" id="butPostApp"><span>确定</span></a><a class="graybtn" id="closeAppUpload"><span>放弃</span></a></span></div>';
        htmlObj['title'] = '苹果应用';
        pop.popupInit(appJmPopConfig, htmlObj);
        if (appObj.screenShot.length > (40 - imgCount)) {
            $('#pastePrompt').css('color', 'red');
        }
        $('#butPostApp').bind('click', function() {
            uploadApp(appObj, url);
        })
        $("#closeAppUpload").one('click', function() {
            pop.hidepopById('appUpload', true, true, null);
        })
        $("#appChkAll").die().live('click', function() {
            if ($(this).attr("checked")) {
                $("#appShow ul li p").find('input').attr('checked', 'checked');
            } else {
                $("#appShow ul li p").find('input').removeAttr('checked');
            }
        });
        $("#appShow ul li p a").die().live('click', function() {
            if ($(this).next('input').attr('checked')) {
                $(this).next('input').attr('checked', '');
            } else {
                $(this).next('input').attr('checked', 'checked');
            }
            $("#appShow ul li").each(function() {
                if ($(this).find('input').attr('checked')) {
                    $("#appChkAll").attr('checked', 'checked');
                } else {
                    $("#appChkAll").removeAttr('checked');
                    return false;
                }
            });
            if ($("#appShow ul li input:checked").size() > (40 - imgCount)) {
                $("#ioslimit b").text('您已选择了' + $("#appShow ul li input:checked").size() + '张图片').css("color", "#ff4200");
            } else {
                $("#ioslimit b").text('您已选择了' + $("#appShow ul li input:checked").size() + '张图片').css("color", "#5b5b5b");
            }
        })
    }
    var uploadApp = function (appObj, url) {
        var iosContentDesc = $('#iosDesc').val().toString().replace(/(\r)*\n/g, "<br />").replace(/\s/g, " ");
        var screenShot = [];
        $("input[name='appScreenShot']").each(function(i, val) {
            if ($(this).attr("checked")) {
                screenShot.push(val.value);
            }
        });
        var imgCount = screenShot.length + ((blogContent == null || blogContent.image == null || blogContent.image.length == 0) ? 0 : blogContent.image.length);
        if (imgCount > 40) {
            joymealert.alert({text:'您只能上传40张图片',tipLayer:true,textClass:"tipstext"});
            return false;
        }
        pop.hidepopById('appUpload', true, true, null);
        var appJmPopConfig = {
            pointerFlag : false,//是否有指针
            tipLayer : true,//是否遮罩
            containTitle : true,//包含title
            containFoot : false,//包含footer
            forclosed:false,
            offset:"",
            popwidth:240,
            popscroll:true,
            isremovepop:true,
            allowmultiple:true,
            isfocus:false,
            popzindex:18500
        };
        var htmlObj = new Object();
        htmlObj['id'] = 'appUpload';
        htmlObj['html'] = '<div class="publicuse">' +
                '<p><img src="' + joyconfig.URL_LIB + '/static/theme/default/img/loadingS.gif" /> &nbsp; 处理中,请稍候...</p>' +
                '</div>';
        htmlObj['title'] = '<em>提示信息</em>';
        pop.popupInit(appJmPopConfig, htmlObj);

        var deviceSet = [];
        $.each(appObj.deviceSet, function(i, val) {
            deviceSet.push(val.code);
        });

        postbiz.uploadIosInsertEditor(appObj, screenShot, deviceSet, iosContentDesc, url, uploadAppSuccess, uploadAppError);
    }
    var uploadAppSuccess = function(resultMsg, iosContentDesc, url, appObj) {
        var iosObj = resultMsg.result[0];
        iosObj.contentDesc = iosContentDesc;
        iosObj.resourceUrl = url;
        iosObj.iosDesc = appObj.desc;
        iosObj.appName = appObj.appName;
        iosObj.appCategory = appObj.appCategory;
        praseIOS(iosObj);
        pop.hidepopById('appUpload', true, true, null)
    }
    var uploadAppError = function(resultMsg) {
        pop.hidepopById('appUpload', true, true, null);
        var alertOption = {text:'',tipLayer:true,textClass:"tipstext"}
        if (resultMsg.msg != null) {
            alertOption.text = resultMsg.msg;
        } else {
            alertOption.text = '链接失效';
        }

        joymealert.alert(alertOption);
    }

    var praseIOS = function (iosObj) {
        var appCotnent = '';
        blogContent.ios = {icon:iosObj.iconSrc,card:iosObj.appCardSrc,url:iosObj.resourceUrl,desc:encodeURIComponent(iosObj.iosDesc)};
        var common = require('../common/common')
        var cardSrc = common.parseBimg(blogContent.ios.card, joyconfig.DOMAIN);
        appCotnent = '<div><p><img src="' + cardSrc + '" joymet="app" joymeappt="ios" joymed="' + blogContent.ios.desc + '" joymei="' + blogContent.ios.icon + '" joymec="' + blogContent.ios.card + '" joymer="' + blogContent.ios.url + '" title="' + iosObj.appName + '"/></p></div>'

        if (iosObj.contentDesc != null && iosObj.contentDesc.length != 0) {
            appCotnent += iosObj.contentDesc + '<br/>';
        }

        appCotnent += '<div>';

        $.each(iosObj.screenShot, function(i, val) {
            if (blogContent.image.length == 20) {
                return;
            }
            var src = common.parseSSimg(val['ss'], joyconfig.DOMAIN);
            blogContent.image.push({key:imgLocaL,value:{url:val['b'],src:src,desc:''}});
            appCotnent += '<img src="' + src + '" joymet="img" joymed=""  joymeh="'+val['h']+'" joymeu="' + val['b'] + '" joymew="'+val['w']+'"/>&nbsp;<br />';
        });
        appCotnent += '</div>';
        $("#tags_input_text").val('');
        var tagId = $('#posttext').children('input[name=tags]').length;
        tag.initTagItem('text',  '苹果应用', '苹果应用', 'posttext');
        tag.initTagItem('text', iosObj.appCategory, iosObj.appCategory, 'posttext');
        var blogSubject = $('#blogSubject').val();
        if (blogSubject.length == 0 || blogSubject == '给你的文章加个标题吧') {
            $('#blogSubject').val('【苹果应用-' + iosObj.appCategory + '】' + iosObj.appName).css('color', '#5B5B5B');
        }
        CKEDITOR.instances['text_content'].insertHtml(appCotnent);

    }

    return postapp;
});