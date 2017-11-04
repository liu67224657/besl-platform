/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-4
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var pop = require('../common/jmpopup');
    var ajaxmood = require('./ajaxmood');
    var common = require('../common/common');
    var config = {
        pointerFlag : true,//是否有指针
        pointdir : 'up',//指针方向
        tipLayer : false,//是否遮罩
        containTitle : true,//包含title
        containFoot : false,//包含footer
        offset:"Custom",
        forclosed:true,
        popwidth:450,
        allowmultiple:false,
        isremovepop:false,
        isfocus:true,
        popzindex:16002
    };
    var mood = {
        docFaceBind:function (showBtn, textareaID, popConfig, te) {
            var pos = te.getSelection();
            $("#" + textareaID).blur();
            var offset = $(showBtn).offset();
            offset.top += 31;
            popConfig = $.extend({offsetlocation:[offset.top,offset.left]}, config, popConfig);
            ajaxmood.integrateFaceMethod(this.loadFace, offset, textareaID, popConfig, null, te, pos);
        },
        docFaceBindOnReply:function (showBtn, textareaID, popConfig, te) {
            var pos = te.getSelection();
            $("#" + textareaID).blur();
            var offset = $(showBtn).offset();
            offset.top -= 12;
            popConfig = $.extend({offsetlocation:[offset.top,offset.left - 9]}, config, popConfig);
            ajaxmood.integrateFaceMethod(this.loadFace, offset, textareaID, popConfig, showBtn, te, pos);
        },
//        docFaceBindOnWall:function (showBtn, textareaID, popConfig, te) {
//            var pos = te.getSelection();
//            $("#" + textareaID).blur();
//            var offset = $(showBtn).offset();
//            offset.top += 24;
//            popConfig = $.extend({offsetlocation:[offset.top,offset.left - 10],afterDom:$('#content_wall_area')}, config, popConfig);
//            ajaxmood.integrateFaceMethod(this.loadFace, offset, textareaID, popConfig, showBtn, te, pos);
//        },
        loadFace:function(htmlStrObj, offset, textareaID, popConfig, other, te, pos) {
            var moodId = "mood";
            showMoodHtml(moodId, htmlStrObj, popConfig);
            if (other != null && other.length > 0) {
//                if ($('#content_wall_area').length > 0) {
//                    showmoodForPostFixed(other, popConfig, moodId);
//                } else {
                    showmoodForOther(other, popConfig, moodId, 0);
//                }
            }
            $(".face_btn a").die().live('click', function(event) {
                fillMoodByDiv(moodId, textareaID, $(this).attr("title"), te, pos);
            });
            $('a[id^=moodgroup_]').die().live('click', function() {
                var group = $(this).attr('data-group');
                var moodGroup = $('div[name=mood_' + group + ']');
                moodGroup.css('display', '').siblings().css('display', 'none');
                $(this).parent().attr('class', 'tabon').siblings().removeClass();
            });
        },
//        loadTopFace:function(htmlStrObj, offset, textareaID, popConfig, other, te, pos){
//           var moodId = "mood";
//            showMoodHtml(moodId, htmlStrObj, popConfig);
//            if (other != null && other.length > 0) {
//                if (window.navigator.userAgent.indexOf('MSIE 7.0') > 0) {
//                    showmoodForIE7(other, popConfig, moodId);
//                } else {
//                    showmoodForOther(other, popConfig, moodId);
//                }
//            }
//            $(".face_btn a").die().live('click', function(event) {
//                fillMoodByDiv(moodId, textareaID, $(this).attr("title"), te, pos);
//            });
//            $('a[id^=moodgroup_]').die().live('click', function() {
//                var group = $(this).attr('data-group');
//                var moodGroup = $('div[name=mood_' + group + ']');
//                moodGroup.css('display', '').siblings().css('display', 'none');
//                $(this).parent().attr('class', 'tabon').siblings().removeClass();
//            });
//        },

        showFace:function(dom, id) {
            pop.showpop(null, id);
        },
        hideFace:function() {
            pop.hidepopById('mood', true, false, null);
        },
        hideFaceById:function(moodDiv) {
            pop.hidepopById(moodDiv, true, false, null);
        },
        editorFace:function(showBtn, oEditor, popConfig) {
            var offset = $(showBtn).offset();
            offset.top += 31;
            popConfig = $.extend({offsetlocation:[offset.top,offset.left]}, config, popConfig);
            ajaxmood.integrateFaceMethod(this.loadoEditorFace, offset, oEditor, popConfig);
        },
        loadoEditorFace:function(htmlStrObj, offset, oEditor, popConfig) {
            showMoodHtml('mood_oEditor', htmlStrObj, popConfig, true);

            $(".face_btn a").die().live('click', function(event) {
                filloEditor(oEditor, $(this).attr("title"), 'mood_oEditor');
            });

            $('a[id^=moodgroup_]').die().live('click', function() {
                var group = $(this).attr('data-group');
                var moodGroup = $('div[name=mood_' + group + ']');
                moodGroup.css('display', '').siblings().css('display', 'none');
                $(this).parent().attr('class', 'tabon').siblings().removeClass();
            });
        },
        replyFace:function(showBtn, textareaID, cid) {
            var offset = $(showBtn).offset();
            var popConfig = {};
            offset.top += 31;
            popConfig = $.extend({offsetlocation:[offset.top,offset.left]}, config, popConfig);
            ajaxmood.integrateFaceMethod(this.loadReplyFace, offset, textareaID, popConfig, cid);
        },
        loadReplyFace:function(htmlStrObj, offset, textareaID, popConfig, id) {
            var rMoodid = "mood_reply_" + id
            showMoodHtml(rMoodid, htmlStrObj, popConfig, false);

            //焦点失去隐藏判断
            $("body").die().live('mousedown', function() {
                if (window.isOut) {
                    $("#" + rMoodid).hide();
                    isOut = true;
                }
            });
            $(".face_btn a").die().live('click', function(event) {
                fillMoodByDiv(rMoodid, textareaID, $(this).attr("title"));
                event = event || window.event;
                if (event.stopPropagation) { //W3C阻止冒泡方法
                    event.stopPropagation();
                } else {
                    event.cancelBubble = true; //IE阻止冒泡方法
                }
            })
            $('a[id^=moodgroup_]').die().live('click', function() {
                var group = $(this).attr('data-group');
                var moodGroup = $('div[name=mood_' + group + ']');
                moodGroup.css('display', '').siblings().css('display', 'none');
                $(this).parent().attr('class', 'tabon').siblings().removeClass();
            });
        }
    }
    var fillMoodByDiv = function (moodDiv, fillMoodByDiv, moody, te, pos) {
        var str = '[' + moody + ']';
        if (pos != undefined) {
            te.setSelectionRange(pos.selectionStart, pos.selectionEnd)
        } else {
            var len = $("#" + fillMoodByDiv).val().length;
            te.setSelectionRange(len, len)
        }
        te.insertData(str);
        mood.hideFaceById(moodDiv);
        if ($("#faceShow").length > 0) {
            var editclass = $("#faceShow").attr('class');
            if (editclass.indexOf('_on') != -1) {
                $("#faceShow").removeClass().addClass(editclass.substr(0, editclass.length - 3));
            }
        }

    }
    var filloEditor = function(oEditor, str, moodDiv) {
        oEditor.insertHtml('[' + str + ']');
        $("#" + moodDiv).hide();
        var editclass = $("#editor_mood").attr('class');
        if (editclass.indexOf('_on') != -1) {
            $("#editor_mood").removeClass().addClass(editclass.substr(0, editclass.length - 3));
        }
    }

    var showMoodHtml = function(moodId, htmlStrObj, popConfig) {
        var htmlObj = new Object();
        htmlObj['id'] = moodId;

        var smileyHtml = '';
        var smileyLink = '';
        if (htmlStrObj['smiley'] != null && htmlStrObj['smiley'].length > 0) {
            smileyLink = '<li class="tabon"><a id="moodgroup_smiley" data-group="smiley" href="javascript:void(0);" onclick="return false;">默认</a></li>'
            var smileyHtml = "<div name='mood_smiley' class='face_btn'>";
            $.each(htmlStrObj['smiley'], function(i, val) {
                smileyHtml += '<a href="javascript:void(0)" onclick="return false;" title="' + val.code + '"><img src="' + joyconfig.URL_LIB + val.imgUrl + '" width="28" height="28" /></a>';
            })
            smileyHtml += "</div>";
        }

        var pansiteHtml = '';
        var pansiteLink = '';
        if (htmlStrObj['pansite'] != null && htmlStrObj['pansite'].length > 0) {
            pansiteLink = '<li><a id="moodgroup_pansite" data-group="pansite" href="javascript:void(0);" onclick="return false;">潘斯特一代目</a></li>';
            pansiteHtml = "<div name='mood_pansite' class='face_btn' style='display:none'>";
            $.each(htmlStrObj['pansite'], function(i, val) {
                pansiteHtml += '<a href="javascript:void(0)" onclick="return false;" title="' + val.code + '"><img src="' + joyconfig.URL_LIB + val.imgUrl + '" width="28" height="28" /></a>';
            })
            pansiteHtml += "</div>";
        }

        var erdaimuHtml = '';
        var erdaimuLink = '';
        if (htmlStrObj['erdaimu'] != null && htmlStrObj['erdaimu'].length > 0) {
            erdaimuLink = '<li><a id="moodgroup_erdaimu" data-group="erdaimu" href="javascript:void(0);" onclick="return false;">潘斯特二代目</a></li>';
            erdaimuHtml += "<div name='mood_erdaimu' class='face_btn' style='display:none'>";
            $.each(htmlStrObj['erdaimu'], function(i, val) {
                erdaimuHtml += '<a href="javascript:void(0)" onclick="return false;" title="' + val.code + '"><img src="' + joyconfig.URL_LIB + val.imgUrl + '" width="28" height="28" /></a>';
            })
            erdaimuHtml += "</div>";
        }

        htmlObj['html'] = smileyHtml + pansiteHtml + erdaimuHtml;
        htmlObj['title'] = '<div class="pichd"><ul>' + smileyLink + erdaimuLink + pansiteLink + '</ul></div>';
        pop.popupInit(popConfig, htmlObj);
    }
    var showmoodForPostFixed = function(other, popConfig, moodId) {
        showmoodForOther(other, popConfig, moodId, 5);
//        var moodDom = $("#" + moodId);
//        var moodstyle = moodDom.attr('style');
//        moodDom.removeAttr('style').attr('style', moodstyle.replace('absolute', 'fixed'));
    }

    var showmoodForOther = function(other, popConfig, moodId, topFixNo) {

        var distance = $(other).offset().top - $(window).scrollTop();

        var moodDom = $("#" + moodId);
        if (distance > 342) {
            moodDom.find('.corner').remove();
            moodDom.append('<div class="cornerdowm"></div>');
            moodDom.css({top: (popConfig.offsetlocation[0] - 270 - topFixNo) + 'px'});
        } else {
            moodDom.find('.cornerdowm').remove();
            moodDom.append('<div class="corner"></div>');
            moodDom.css({top: (popConfig.offsetlocation[0] + 34) + 'px'});
        }

    }
    return mood;
})