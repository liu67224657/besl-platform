/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-2
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var params = new Object();
    var popUp = {
        popupInit : function(popConfig, htmlObj) {

            popConfig = initConfig(popConfig);
            if (!htmlObj) {
                return;
            }
            if (!popConfig.allowmultiple) {
                $(".pop").hide();
                if ($("#" + htmlObj['id']).length > 0) {
                    showLayer(popConfig);
                    $("#" + htmlObj['id']).show();
                    return;
                }
            }
            var popHtml = '<div class="$class$" id="$id$" style="display:none;position:absolute;_position:absolute; z-index:' + popConfig.popzindex + ';">';
            if (popConfig.pointerFlag && popConfig.pointdir == "up") {
                popHtml += '<div class="corner"></div>';
            } else if (popConfig.pointerFlag && popConfig.pointdir == "down") {
                popHtml += '<div class="cornerdowm"></div>';
            }
            if (popConfig.containTitle) {
                popHtml += '<div class="hd clearfix">$title$';
                if (popConfig.forclosed) {
                    popHtml += '<a class="close" id="closePop_' + htmlObj['id'] + '" onclick="return false;" href="javascript:void(0)"></a>';
                    $('#closePop_' + htmlObj['id']).live('click', function() {
//                        if (popConfig.hideCallback != null) {
//                            try {
//                                popConfig.hideCallback();
//                            } catch(ex) {
//                                console.log("message:" + ex.message + " line:" + ex.lineNumber);
//                            }
//                        }
                        var removeObj = {};
                        removeObj['id'] = $(this).parent().parent().attr("id");
                        popUp.hidepop(removeObj, popConfig);
//                        if (popConfig.isremovepop) {
//                            $('#closePop_' + htmlObj['id']).remove();
//                        }
//                        if ($("#blackDom").length > 0 && popConfig.tipLayer) {
//                            $("#blackDom").fadeOut().remove();
//                        }
                    })
                }
                popHtml += '</div>';
            }

            var footerHtml = '';
            if (popConfig.containFoot) {
                footerHtml = '<div class="ft">$input$</div>';
            }
            popHtml += '<div class="bd clearfix">$html$</div>' +
                    footerHtml +
                    '</div>';
            integratepop(popConfig, htmlObj, popHtml);

            if (popConfig.showTime != null && typeof(popConfig.showTime) == 'number' && popConfig.forclosed && popConfig.showTime > 0) {
                setTimeout(function() {
                    params.showTimeoutId = $('#closePop_' + htmlObj['id']).click();
                }, popConfig.showTime);
            }

        } ,

        popupInitHtmlStr : function(popConfig, html) {
            var _this = this;
            popConfig = initConfig(popConfig);
            if (html == '') {
                return;
            }
            if (!popConfig.allowmultiple) {
                $(".pop").hide();
                if ($("#" + popConfig.id).length > 0) {
                    showLayer(popConfig);
                    $("#" + popConfig.id).show();
                    return;
                }
            }
            if (popConfig.forclosed) {
                $('#closePop_' +  popConfig.id).die().live('click', function() {
                    var removeObj = {};
                    removeObj['id'] = $(this).parent().parent().attr("id");
                    popUp.hidepop(removeObj, popConfig);

                    if (params.showTimeoutId != null) {
                        clearTimeout(params.showTimeoutId);
                    }
                })
            }
            htmlStr(html, popConfig,  popConfig.id);
        } ,

        hidepop:function(removeObj, popConfig) {
            if (popConfig.hideCallback != null) {
                try {
                    popConfig.hideCallback();
                } catch(ex) {
                    console.log("message:" + ex.message + " line:" + ex.lineNumber);
                }
            }
            if ($("#blackDom").length > 0 && popConfig.tipLayer) {
                $("#blackDom").fadeOut().remove();
            }
            $("#" + removeObj['id']).hide();
            if (popConfig.isremovepop) {
                $("#" + removeObj['id']).remove();
            }
        },
        hidepopById:function(id, isRemove, removeBlack, hideCallback) {
            if (hideCallback != null) {
                hideCallback();
            }
            if ($("#blackDom").length > 0 && removeBlack) {
                $("#blackDom").fadeOut().remove();
            }
            $("#" + id).hide();
            if (isRemove) {
                $("#" + id).remove();
            }
        },
        hidepopByJqObj:function(JqObj, isRemove, removeBlack, hideCallback) {
            if (hideCallback != null) {
                hideCallback();
            }
            if ($("#blackDom").length > 0 && removeBlack) {
                $("#blackDom").fadeOut().remove();
            }
            JqObj.hide();
            if (isRemove) {
                JqObj.remove();
            }
        },

        showpop:function(popConfig, id) {
            if (popConfig.animate == 'slide') {
                $("#" + id).slideDown();
            } else if (popConfig.animate == 'fade') {
                $("#" + id).fadeIn();
            } else {
                $("#" + id).show();
            }
        },
        resetOffset:function(popConfig, htmlId) {
            $(".pop").hide();
//            popConfig.isfocus = true;
            if (window.isOut == null) {
                window.isOut = true;
            }
            htmlStr("", popConfig, htmlId);
            this.showpop(popConfig, htmlId);
        },
        resetOffsetById:function(popConfig, id) {
            $("#" + id).hide();
//            popConfig.isfocus = true;
            if (window.isOut == null) {
                window.isOut = true;
            }
            htmlStr("", popConfig, id);
            this.showpop(popConfig, id);
        }
    };
    var integratepop = function(popConfig, htmlObj, popHtml) {
        if (popConfig.containTitle) {
            popHtml = popHtml.replace(/\$title\$/g, htmlObj['title'])
        } else {
            popHtml = popHtml.replace(/\$title\$/g, '')
        }
        if (popConfig.containFoot) {
            popHtml = popHtml.replace(/\$input\$/g, htmlObj['input'])
        } else {
            popHtml = popHtml.replace(/\$input\$/g, '');
        }
        if (popConfig.addClass && popConfig.className != "") {
            popHtml = popHtml.replace(/\$class\$/g, popConfig.className)
        } else {
            popHtml = popHtml.replace(/\$class\$/g, 'pop');
        }
        popHtml = popHtml.replace(/\$id\$/g, htmlObj['id'])
        popHtml = popHtml.replace(/\$html\$/g, htmlObj['html']);
        htmlStr(popHtml, popConfig, htmlObj['id']);
    };
    var showLayer = function(popConfig) {
        if (popConfig.tipLayer) {
            $("body").append("<div id='blackDom'></div>");
            var blackDomZindex = popConfig.popzindex - 1;
            $("#blackDom").css({opacity:"0","background":"#000",width:$(window).width(),height:$(document).height(),position: "absolute",top:0, left:0,'z-index':blackDomZindex}).animate({opacity:"0.3"});
        }
    }
    var htmlStr = function(popHtml, popConfig, htmlId) {
        showLayer(popConfig);
        if (popHtml.length != 0) {
            if (popConfig.afterDom != null) {
                popConfig.afterDom.after(popHtml)
            } else {
                $("body").append(popHtml);
            }
            if (popConfig.animate == 'slide') {
                $("#" + htmlId).slideDown();
            } else if (popConfig.animate == 'fade') {
                $("#" + htmlId).fadeIn();
            } else {
                $("#" + htmlId).show();
            }
            if (popConfig.showFunction != null) {
                popConfig.showFunction();
            }
        }
        if (popConfig.isfocus) {
            window.isOut = true;
            $("#" + htmlId).live('mouseenter mouseleave', function(event) {
                if (event.type == "mouseleave") {
                    window.isOut = true;
                } else if (event.type == "mouseenter") {
                    isOut = false;
                }
            });
        } else {
            $("#" + htmlId).die('mouseenter mouseleave');
            window.isOut = null;
        }
        if (popConfig.offset != "" || popConfig.offset.constructor == String) {
            var popBox = $("#" + htmlId);
            switch (popConfig.offset) {
                case("left-top")://左上角
                    popBox.css({ left:0,top:0, width: popConfig.popwidth + "px"})
                    break;
                case("left-bottom")://左下角
                    popBox.css({left:0,bottom:0, width: popConfig.popwidth + "px"});
                    break;
                case("right-top")://右上角
                    popBox.css({right:0,top:0, width:popConfig.popwidth + "px"});
                    break;
                case("right-bottom")://右下角
                    popBox.css({right:0, bottom:0, width: + popConfig.popwidth + "px"});
                    break;
                case("middle-top")://居中置顶
                    popBox.css({left:"50%", marginLeft: + -parseInt(popBox.width() / 2) + "px",top:0, width: popConfig.popwidth + "px"});
                    break;
                case("middle-bottom")://居中置低
                    popBox.css({left:"50%", marginLeft: -parseInt(popBox.width() / 2) + 'px',bottom:0, width: popConfig.popwidth + "px"});
                    break;
                case("left-middle")://左边居中
                    popBox.css({left:0, top:"50%" ,width: popConfig.popwidth + "px"});
                    break;
                case("right-middle")://右边居中
                    popBox.css({right:0, top:0,marginTop: (-parseInt(popBox.height() / 2)) + 'px', width: popConfig.popwidth + "px"});
                    break;
                case("Custom"):
                    if (popConfig.offsetlocation == null) {
                        break;
                    }
                    popBox.css({top:parseInt(popConfig.offsetlocation[0]) + 'px', left: parseInt(popConfig.offsetlocation[1]) + 'px', width: popConfig.popwidth + "px"});
                    break;
                default://默认为居中
                    var topLine = 0
                    if (popConfig.isHeader) {
                        topLine = ($(window).height() - popBox.height()) / 2 + $(window).scrollTop() - $("body").data('scrolltopLater') || 0
                    } else if (!popConfig.isHeader && popConfig.isHeader != undefined) {
                        topLine = ($(window).height() - popBox.height()) / 2;
                    } else {
                        topLine = ($(window).height() - popBox.height()) / 2 + $(window).scrollTop();
                    }
                    popBox.css({left: ($(window).width() / 2 - popConfig.popwidth / 2) + 'px', top:topLine + 'px', width: + popConfig.popwidth + 'px'});
                    break;
            }
        }
        return popHtml;
    }

    var initConfig = function(popConfig) {
        return $.extend({},{
            pointerFlag : false,//是否有指针
            pointdir : 'up',//指针方向
            tipLayer : false,//是否遮罩
            containTitle : false,//包含title
            containFoot : false,//包含footer
            offset:"",//定位位置
            offsetlocation:null,
            forclosed:false,   //是否可关闭
            popwidth:"",    //定义宽度
            addClass:false,
            className:"",
            popscroll:false,//是否跟随滚动 ,
            allowmultiple:true,//是否允许多个
            isremovepop:false,//是否删除
            isfocus:false,    //点击窗口外是否隐藏
            hideCallback:null,
            animate:'',
            showFunction:null,
            afterDom:null,
            popzindex:16001,
            showTime:false
        },popConfig);
    }
    return popUp;
})