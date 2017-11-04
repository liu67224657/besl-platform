/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-6-5
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var line = $("#movieLine>li"); //控制用li集合
    var BoxHeight = -302; //图片框高度
    var rollanimate = {
        rollanimate:function() {
            $("#movieBox>div:first").css("top", 0);
            $("#movieBox>div:first").clone().appendTo("#movieBox").css('top', BoxHeight + 'px');
            $("#movieLine>li").bind('click', function() {
                window.clearTimeout(line.timerred);
                window.clearTimeout(line.timerBlack);
                //操作选项卡
                var picDom = $("#movieBox>div").eq($(this).index());//要显示的div
                if (!picDom.is(":animated")) {//判断是否在动画过程中
                    $(this).find('div').remove();
                    $(this).nextAll().find('div').remove();
                    $(this).append('<div class="red" style="left:0"></div>');
                    $(this).prevAll().append('<div class="red" style="left:0"></div><div class="black" style="left:0"></div>');
                    //操作大图
                    $("#movieBox>div").each(function() {//设置当前显示的div
                        if ($(this).css('top') == 0) {
                            $(this).css('z-index', 1);
                            return false;
                        }
                    });
                    picDom.siblings().css({'z-index':1});
                    picDom.css('z-index', 2).animate({'top':0}, function() {
                        $(this).siblings().css({'top':BoxHeight + 'px'});
                    });
                }
            })
            moveSpan()
        },
        tabs:function(tabTit, on, tabCon) {
            $(tabCon).each(function() {
                $(this).children().eq(0).show();
            });
            $(tabTit).each(function() {
                $(this).children().eq(0).addClass(on);
            });
            $(tabTit).children().hover(function() {
                $(this).addClass(on).siblings().removeClass(on);
                var index = $(tabTit).children().index(this);
                $(tabCon).children().eq(index).show().siblings().hide();
            });
        }

    };
    window.lineNum = 0;
    window.lineSpeed = 0;
    window.lineSpeed1 = 0;


    function moveSpan() {
        line.timerred = window.setTimeout(function() {
            if (window.lineSpeed == 0) {
                $(line[window.lineNum]).append('<div class="red">&nbsp;</div>');
            }
            var spanDom = $(line[window.lineNum]).find('div:first')
            spanDom.css('left', spanDom.position().left + 2 + 'px');
            window.lineSpeed++
            if (spanDom.position().left < 0) {
                moveSpan();
            } else {
//                alert(lineSpeed)
                window.lineSpeed1 = 0;
                move(0, 'left', window.lineNum);  //黑色进度条启动
//                    window.clearTimeout(line.timerBlack);
                //控制大图方法
                window.lineNum++;
                window.lineSpeed = 0;
                if (window.lineNum == 5) {
                    setTimeout(function() {
                        $("#movieBox>div").eq(window.lineNum).css('z-index', 2).animate({'top':0}, function() {
                            $(this).siblings().css({'top':BoxHeight + 'px','z-index':1});
                            $("#movieBox div:first").css('top', 0);
                            $(this).css({'top':BoxHeight + 'px','z-index':1});
                        })
                        line.find('div').remove();
                        window.lineNum = 0;
                        moveSpan();
                    }, 900);
                } else {
                    $("#movieBox>div").eq(window.lineNum).css('z-index', 2).animate({'top':0}, function() {
                        $(this).siblings().css({'top':BoxHeight + 'px','z-index':1});
                    });
                    moveSpan();
                }
            }
        }, 40);
    }

    function move(target, cssAttr, num) {
        var dom = $(line[num]);
        if (window.lineSpeed1 == 0) {
            dom.append('<div class="black">&nbsp;</div>');
        }
        var oDivCore = dom.find('div.black')
        if (!core(target, cssAttr, oDivCore)) {
            line.timerBlack = window.setTimeout(function() {
                window.lineSpeed1++;
                move(target, cssAttr, num);
            }, 10);
        }
    }

    function core(target, cssAttr, oDiv) {
        if (oDiv.position() == null) {
            return false;
        }
        var nSpeed = (target - parseInt(oDiv.position().left)) / 6;
        nSpeed = nSpeed > 0 ? Math.ceil(nSpeed) : Math.floor(nSpeed);
        oDiv.css('left', oDiv.position().left + nSpeed + 'px');
        if (nSpeed == 0) {
            oDiv.css('left', 0);
            return true;
        }
        return false;
    }

    return rollanimate;

})