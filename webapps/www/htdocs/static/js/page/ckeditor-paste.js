/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-14
 * Time: 下午4:37
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var pop = require('../common/jmpopup');
    var postbiz = require('../biz/post-biz');
    var joymealert = require('../common/joymealert');
    var pastetext = "";
    var joymeImgRegex = /^(?:http:\/\/r\d\d\d\.joyme)/;
    var pasteprocesse = {
        pasteInit:function(evt, oEditor) {
            var ckdata = evt.data;
            var textbox = ckdata['html'];
            var imgnum = common.regImgNum(oEditor);//获得富文本框的图片数
            var aftertext = rehtmlproperty(textbox);
            aftertext = replaceMood(aftertext);
            pastetext = aftertext;
            ckdata['html'] = "";
            var onlyText = aftertext.replace(/<[^/>]*\/?>/, '');
            if (onlyText.length < 30000) {
                var regBatImg = /<img(?![^<>]*data-cke-saved-joymet=\"(?:img|game)\").*?>/gi;
//判断不是word.腾讯剪切等内容
                var pasteObj = pastecontentgenerator(aftertext);//获得展示上传html和图片数组
                aftertext = pasteObj['aftertext'];
                if (regBatImg.test(aftertext) && aftertext.indexOf("<w:WordDocument>") == -1 && aftertext.indexOf('data:image/png;') == -1 && aftertext.indexOf('data:image/jpeg;') == -1) {
                    if (pasteObj['imgArray'] != null && pasteObj['imgArray'].length > 0) {//如果图片数大于0.启动上传
                        pastePopup(aftertext, pasteObj, imgnum);
                    } else { //否则.直接插入内容
                        CKEDITOR.instances['text_content'].insertHtml(aftertext);
                        CKEDITOR.instances['text_content'].getSelection().scrollIntoView();
                    }
                } else {
                    if (aftertext.indexOf('<o:OfficeDocumentSettings>') != -1) {
                        CKEDITOR.instances['text_content'].insertHtml($(aftertext).text());
                        CKEDITOR.instances['text_content'].getSelection().scrollIntoView();
                    } else {
                        CKEDITOR.instances['text_content'].insertHtml(aftertext);
                        CKEDITOR.instances['text_content'].getSelection().scrollIntoView();
                    }
                }
            } else {
                joymealert.alert({text:'粘贴内容请不要超过30000字',tipLayer:false,textClass:"tipstext"});
            }
        }
    }
    var pastePopup = function(aftertext, pasteObj, imgnum) {
        var htmlObj = {};
        var pasteJmPopConfig = {
            pointerFlag : false,//是否有指针
            tipLayer : true,//是否遮罩
            containTitle : true,//包含title
            containFoot : true,//包含footer
            forclosed:true,
            offset:"",
            popwidth:586,
            popscroll:true,
            isremovepop:true,
            allowmultiple:true,
            isfocus:false
        };
        htmlObj['id'] = 'pasteBox';
        htmlObj['html'] = '<ul class="long_list_paste clearfix" id="pasteShow"></ul>';
        htmlObj['title'] = '<em>选择要放入文章的图片</em>'
        htmlObj['input'] = '<div class="longpic_ft clearfix">' +
                '<span id="pasteCheckBox"><input type="checkbox" checked="checked"> 全选 &nbsp; 你只能保留' + (40 - imgnum) + '张图片 ' +
                '<b>您已选择了' + pasteObj['imgArray'].length + '张图片</b> </span>' +
                '<span class="ft_btn"><a href="javascript:void(0)" class="submitbtn" id="pastesubmit"><span>确定</span></a> <a href="javascript:void(0)" class="submitbtn" id="pastecancel"><span>放弃</span></a></span></div> ';
        pop.popupInit(pasteJmPopConfig, htmlObj);//粘贴层,展示完毕
        $("#pasteCheckBox input:checkbox").focus();
        setTimeout(function() {
            $("#pasteShow").append(pasteObj['html']);
        }, 200);
// .error(function() {
// $(this).attr('src', joyconfig.URL_LIB + 'static/theme/default/img/default.jpg');
// })
        $("#pasteShow").first('li').find("p input").focus();
        $("#pasteShow").scrollTop(0);
//增加选择checkbox事件
        liveCheckPicFun(aftertext, pasteObj, imgnum);
        $("#pastecancel").die().live('click', function() {
            pop.hidepopById('pasteBox', true, true, giveuppaste);
        })
//粘贴
        if (pasteObj['imgArray'].length > (40 - imgnum)) {
            $("#pasteCheckBox b").css("color", "#ff4200");
            return;
        } else {
            livepasteSubmit(aftertext, pasteObj, imgnum);
        }
    }
//注册提交事件
    var livepasteSubmit = function(aftertext, pasteObj, imgnum) {
        $("#pastesubmit").die().live('click', function() {
            insertCkeditor(aftertext, pasteObj, imgnum);
        });
    }
//放弃粘贴图片事件
    var giveuppaste = function() {
        var cancelText = pastetext.replace(/<\/?img[^>]*>/gi, '')
        CKEDITOR.instances['text_content'].insertHtml(cancelText);
        diepasteSubmit();
    }
    var diepasteSubmit = function() {
        $("#pastesubmit").die();
    }
    var rehtmlproperty = function(textbox) {
        var retext = ""
        retext = textbox.replace(/<\/?script[^>]*>|<\/?u[^>]*>|<\/?dt[^>]*>|<\/?dl[^>]*>|<\/?dd[^>]*>|<p[^>]*>|<\/?a[^>]*>|<\/?font[^>]*>|<\/?table[^>]*>|<\/?tbody[^>]*>|<tr[^>]*>|<\/?th[^>]*>|<\/?td[^>]*>|<\/?em[^>]*>|<h[1-6][^>]*>|<\/?select[^>]*>|<li[^>]*>|<\/?option[^>]*>|<div[^>]*>|<\/?span[^>]*>|<\/?strong[^>]*>|<\/?textarea[^>]*>|<input[^>]*\/?>|&nbsp;|<\/?object[^>]*\/?>|<\/?param[^>]*\/?>|<\/?embed[^>]*\/?>|<\/?iframe[^>]*>/gi, '');
        retext = retext.replace(/<\/h[1-6]>|<\/tr>|<\/li>|<\/div>|<\/p>|<p\/>|<br[^>]*\/?>/gi, "<br />");
        retext = retext.replace(/align="center"/gi, 'align="left"');
        retext = retext.replace(/\s?(?:alt|title)="[^"]+"\s?/gi, ' ');//去掉img的alt属性
        retext = retext.replace(/<\/?[wom]:[^/>]*\/?>/gi, '');
        retext = retext.replace(/<\/?xml[^/>]*\/?>/gi, '');//去掉word
        retext = retext.replace(/<img src="file:[^>]*"\/?>/gi, '');//去掉word
        return retext;
    }
//将表情图片转换回文字
    var replaceMood = function (textbox) {
        var boxListNum = textbox.match(/<\s?img[^>]*>/gi);
        if (boxListNum != null && boxListNum.length > 0) {
            var mood = common.getMood();
            for (var i = 0; i < boxListNum.length; i++) {
                var x = $(boxListNum[i]).attr("src");
                if (!(mood['pansite'][x] == undefined)) {
                    textbox = textbox.replace(boxListNum[i], mood['pansite'][x]);
                }
                if (!(mood['smiley'][x] == undefined)) {
                    textbox = textbox.replace(boxListNum[i], mood['smiley'][x]);
                }
                if (!(mood['erdaimu'][x] == undefined)) {
                    textbox = textbox.replace(boxListNum[i], mood['erdaimu'][x]);
                }
            }
            return textbox;
        }
        return textbox;
    }
//整合粘贴层html
    var pastecontentgenerator = function(aftertext) {
        var imgObj = {};
        var imgSrc = "";
        var boxListNum = aftertext.match(/<\s?img[^>]*>/gi);//将数据中图片提出并转为数组
        if (boxListNum != null) {
            boxListNum = common.delRepeat(boxListNum); //数组去重
            var imgMap = {};
            for (var i = 0; i < boxListNum.length; i++) {
                var joymeImgObj=$(boxListNum[i]);
                if(joymeImgObj.attr('joymet')!=null && joymeImgObj.attr('joymet')=='game'){
                    continue;
                }

                var src = joymeImgObj.attr("src");
                //joyme tupian
                if (joymeImgRegex.test(src)) {
                    joymeImgObj = $(boxListNum[i]);
                    var joymeu = common.parseBimg(src, joyconfig.DOMAIN).replace(/^(?:http:\/\/r\d\d\d\.joyme.[^/]+)/, "");
                    var joymesrc = common.parseSSimg(src, joyconfig.DOMAIN);
                    var width = joymeImgObj.attr('data-jw') != null ? joymeImgObj.attr('data-jw') : 100;
                    var height = joymeImgObj.attr('data-jh') != null ? joymeImgObj.attr('data-jh') : 100;
                    var newHtml = '<img src="' + joymesrc + '" joymet="img" joymed="" joymeh="' + height + '" joymeu="' + joymeu + '" joymew="' + width + '"/>'
                    aftertext = aftertext.replace(boxListNum[i], newHtml);
                    blogContent.image.push({key:imgLocaL++,value:{url:joymeu,src:joymesrc,desc:''}});
                    continue;
                }
                if (imgObj['imgArray'] == null) {
                    imgObj['imgArray'] = new Array();
                }
                imgObj['imgArray'][i] = boxListNum[i];
                var imgSuffixReg = /\.(?:jpg|jpeg|gif|png)$/gi
                if (imgSuffixReg.test(src)) {
                    if (src.indexOf('baidu.com') != -1) {//处理防盗链网站图片
                        imgSrc += "<li><p><a href='javascript:void(0)' title=" + src + " >"
                                + "<img src =\"" + joyconfig.URL_LIB + '/static/theme/default/img/obtainimgerror.jpg' + "\" width=\"150\" height=\"114\" /></a></p></li>";
                    } else {
                        imgSrc += "<li><p><a href='javascript:void(0)' title=" + src + " ><img src =\"" + $(boxListNum[i]).attr("src") + "\" width=\"150\" height=\"114\" /></a><input value=\"" + src + "\" class='checkbtn' type='checkbox' checked='checked' /></p></li>";
                    }
                } else {
                    imgSrc += "<li><p><a href='javascript:void(0)' title=" + src + " class=\"current\" ><img src =\"" + joyconfig.URL_LIB + "/static/theme/default/img/obtainimgerror.jpg\" width=\"150\" height=\"114\" /></a></p></li>";
                }
                imgMap[src] = {orgObj:boxListNum[i]};
            }
            imgObj['map'] = imgMap;
        }
        imgObj['html'] = imgSrc;
        //aftertext = aftertext.replace(/style=\"[^\"]*\"/ig, '');
        imgObj['aftertext'] = aftertext;
        return imgObj;
    }
//insert ckeditor fun
    var insertCkeditor = function(aftertext, pasteObj, imgnum) {
        var imageArray = [];//选中图片的数组
        $("#pasteShow li p").find("input:checked").each(function(i) {
            imageArray[i] = {srcKey:$(this).val()};
        });
        if (imageArray.length > (40 - imgnum)) {
            joymealert.alert({text:'您还能上传' + (40 - imgnum) + '张图片,请重新选择',tipLayer:false,textClass:"tipstext"});
// pop.hidepopById('pasteBox', true, false);
            setTimeout(function() {
                pastePopup(aftertext, pasteObj, imgnum)
            }, 200)
            return;
        }
        if (imageArray.length == 0) {
            var cancelText = aftertext.replace(/<\/?img[^>]*>/gi, '')
            CKEDITOR.instances['text_content'].insertHtml(cancelText);
            pop.hidepopById('pasteBox', true, true, null);
            diepasteSubmit();
        } else {
            var waitHtml = '<div class="bombcon pic_tc"><ul class="tclist clearfix"><li class="load"><img src="' + joyconfig.URL_LIB + '/static/theme/default/img/loadingS.gif"></li><li>' +
                    '<span id="postimagenum">图片处理中，请稍候...(1\/' + imageArray.length + ')</span></li></ul><div class="tc_ft"><a href="javascript:void(0)" id="cancelCtrlV" class="submitbtn"><span>取消</span></a></div></div>';
            $("#pasteBox").html(waitHtml);
            $("#pasteBox").css({width:'195px'});
            $("#pasteBox").css({left: ($(window).width() / 2 - $("#pasteBox").width() / 2) + "px",top: ($(window).height() - parseInt($('#pasteBox').height())) / 2 + $(document).scrollTop() + "px"});
//上传图片方法
            insertimagecallback(imageArray, insertimagecallback, 0, aftertext, pasteObj, null, null);
            $("#cancelCtrlV").die().live('click', function() {
                window.cancelUploadCtrlV = true;
                insertPasteObj(pasteObj, aftertext, imageArray);
            })
        }
    }
//上传图片方法
    var insertimagecallback = function(imageArray, insertimagecallback, i, aftertext, pasteObj, ajaxData) {
        if (!window.cancelUploadCtrlV) {
            if (ajaxData != null) {
                imageArray[i - 1]['ajaxData'] = ajaxData;
            }
            if (i == imageArray.length) {
                insertPasteObj(pasteObj, aftertext, imageArray);
            } else {
                $("#postimagenum").text('图片处理中，请稍候...(' + (i + 1) + '/' + imageArray.length + ')');
                postbiz.insertCkimagePost(imageArray, insertimagecallback, i, aftertext, pasteObj, ajaxData)
            }
        } else {
            window.cancelUploadCtrlV = false;
            pasteObj['map'] = null;
            return;
        }
    }
    var insertPasteObj = function(pasteObj, aftertext, imageArray) {
        for (var j = 0; j < imageArray.length; j++) {
            var ajaxData = imageArray[j]['ajaxData'];
            if (ajaxData == null) {
                continue;
            }
            blogContent.image.push({key:imgLocaL++,value:{url:ajaxData['b'],src:imgSrc,desc:''}});
            var imgSrc = common.parseSSimg(ajaxData['m'], joyconfig.DOMAIN);
            pasteObj['map'][imageArray[j]['srcKey']]['newObj'] = '<img src="' + imgSrc + '" joymet="img" joymed="" joymeh="' + ajaxData['h'] + '" joymeu="' + ajaxData['b'] + '" joymew="' + ajaxData['w'] + '"/><br />';
        }
        for (var i in pasteObj['map']) {
            if (pasteObj['map'][i]['newObj'] == undefined) {
                aftertext = aftertext.replace(pasteObj['map'][i]['orgObj'], '');
            } else {
                aftertext = aftertext.replace(pasteObj['map'][i]['orgObj'], pasteObj['map'][i]['newObj']);
            }
        }
        var contentText = CKEDITOR.instances['text_content'].getData();
        if (contentText != null && contentText != '' && !common.endsWithBr(contentText)) {
            aftertext = '<br />' + aftertext;
        }
        CKEDITOR.instances['text_content'].insertHtml(aftertext);
        pop.hidepopById('pasteBox', true, true, null);
        diepasteSubmit();
    }
    var pasteJoymeHmtl = function(imglist, aftertext) {
        if (imglist != undefined) {
            if ((blogContent.image.length + imglist.length) > 40) {
                joymealert.alert({text:'您选择的图片过多,请重新粘贴!',tipLayer:false,textClass:"tipstext"});
                return;
            } else {
                for (var i = 0; i < imglist.length; i++) {
                    var imgSrc = common.parseSSimg($(imglist[i]).attr("src"), joyconfig.DOMAIN);
                    aftertext = aftertext.replace(imglist[i], '<img src="' + imgSrc + '" joymet="img" joymed="" joymeh="" joymeu="' + common.joymeImgPrefix(imgSrc) + '" joymew="" /><br />');
                }
                CKEDITOR.instances['text_content'].insertHtml(aftertext);
            }
        } else {
            CKEDITOR.instances['text_content'].insertHtml(aftertext);
        }
    }
//注册图片选择方法
    var liveCheckPicFun = function(aftertext, pasteObj, imgnum) {
        $("#pasteCheckBox input").die().live('click', function() {
            if ($(this).attr("checked")) {
                $("#pasteShow li").each(function() {
                    $(this).find('input').attr("checked", "checked");
                });
            } else {
                $("#pasteShow li").each(function() {
                    $(this).find('input').removeAttr("checked");
                });
            }
            if ($("#pasteShow li input:checked").size() > (40 - imgnum)) {
                $("#pasteCheckBox").children('b').text('您已选择了' + $("#pasteShow li input:checked").size() + '张图片').css("color", "#ff4200");
                diepasteSubmit();
            } else {
                $("#pasteCheckBox").children('b').text('您已选择了' + $("#pasteShow li input:checked").size() + '张图片').css("color", "#5b5b5b");
                livepasteSubmit(aftertext, pasteObj, imgnum);
            }
        });
        $("#pasteShow li p a img").live('click', function() {
            if ($(this).parent().parent().children('input').attr("checked")) {
                $(this).parent().parent().children('input').removeAttr("checked");
            } else {
                $(this).parent().parent().children('input').attr("checked", "checked");
            }
            if ($("#pasteShow li input:checked").size() > (40 - imgnum)) {
                $("#pasteCheckBox").children('b').text('您已选择了' + $("#pasteShow li input:checked").size() + '张图片').css("color", "#ff4200");
                diepasteSubmit()
            } else {
                $("#pasteCheckBox").children('b').text('您已选择了' + $("#pasteShow li input:checked").size() + '张图片').css("color", "#5b5b5b");
                livepasteSubmit(aftertext, pasteObj, imgnum);
            }
        })
        $("#pasteShow li input").live('click', function() {
            if ($("#pasteShow li input:checked").size() > (40 - imgnum)) {
                $("#pasteCheckBox").children('b').text('您已选择了' + $("#pasteShow li input:checked").size() + '张图片').css("color", "#ff4200");
                diepasteSubmit();
            } else {
                $("#pasteCheckBox").children('b').text('您已选择了' + $("#pasteShow li input:checked").size() + '张图片').css("color", "#5b5b5b");
                livepasteSubmit(aftertext, pasteObj, imgnum);
            }
        })
    }
    return pasteprocesse;
})