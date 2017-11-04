/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-2-9
 * Time: 下午6:16
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    //保存當前圖片定時的對象
    var imageTimer = null;
    //當前的索引
    var currIndex = null;
    //當前圖片的索引
    var currImgIndex = null;
    //當前圖片
    var currImgLi = null;
    //記錄需要切換的圖片集合的索引。
    var IMGINDEX = null;

    var imgroll = {
        //图片轮换
        InitSwitchObj : function() {
            IMGINDEX = $("#rollLink");
//            $("#rollImg").css("width", 670 * $("#rollImg").find("li").size() + "px");
            currIndex = 0;
            currImgIndex = IMGINDEX.find("li:eq(" + currIndex + ")");
            currImgLi = $("#rollImg").find("li:eq(" + currIndex + ")");
            currImgLi.show();
            imgroll.ImageAutoSwitch();
            imgroll.ImageManualSwitch();
            imgroll.ImageStopSwitch();
        },
        //自動定時切換。
        ImageAutoSwitch:function() {
            if (imageTimer != null) {
                clearInterval(imageTimer);
            }
            imageTimer = setInterval(imgroll.IntervalImage, 3000);
        },
        //手動切換圖片
        ImageManualSwitch:function() {
            IMGINDEX.find("a").each(function(i) {
                $(this).hover(function() {
                    if (imageTimer != null) {
                        clearInterval(imageTimer);
                    }
                    currIndex = i;
                    imgroll.ImageSwitchChange();
                }, function() {
                    imgroll.ImageAutoSwitch();
                });

            });
        },
        //鼠標放到圖片上的換，停止切換
        ImageStopSwitch:function () {
            $("#rollImg").find("li").each(function() {
                $(this).hover(function() {
                    if (imageTimer != null) {
                        clearInterval(imageTimer);
                    }
                }, function() {
                    imgroll.ImageAutoSwitch();
                });
            });

        },
        //自动切换图片
        IntervalImage:function () {
            currIndex = parseFloat(currIndex);
            currIndex = currIndex + 1;
            //如果切換到最大數量的時候則從頭開始
            if (currIndex >= $("#rollImg").find("li").length) {
                currIndex = 0;
            }
            //將原來的現實圖片索引的背景透明
            imgroll.ImageSwitchChange();
        },
        //切換圖片的時候，響應的圖片要實現漸變效果
        ImageSwitchChange:function () {
            currImgIndex = IMGINDEX.find("li:eq(" + currIndex + ")");
            imgLi = $("#rollImg").find("li:eq(" + currIndex + ")");
            $("#rollLink").find("a").removeClass("hover").eq(currIndex).addClass("hover");
            $("#rollText").find("p").find("a").hide().eq(currIndex).fadeIn(1000);
            if (currImgLi != null) {
                currImgLi.hide();
                imgLi.fadeIn(1000);
            }
            currImgLi = imgLi;
        }
    }
    return imgroll;
})
        ;

