

//图片轮换
function InitSwitchObj() {
    IMGINDEX = $("#IMG_INDEX_UL_LIST_1");
    $("#IMG_UL_LIST_1").css("width",670*$("#IMG_UL_LIST_1").find("li").size()+"px");
    currIndex = 0;
    currImgIndex = IMGINDEX.find("li:eq(" + currIndex + ")");
    currImgLi = $("#IMG_UL_LIST_1").find("li:eq(" + currIndex + ")");
    currImgLi.show();
    ImageAutoSwitch();
    ImageManualSwitch();
    ImageStopSwitch();
}

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
//自動定時切換。
function ImageAutoSwitch() {
    if (imageTimer != null) {
        clearInterval(imageTimer);
    }
    imageTimer = setInterval("IntervalImage()", 3000);
}
//手動切換圖片
function ImageManualSwitch() {
    IMGINDEX.find("li").each(function(i) {
        $(this).hover(function() {
            if (imageTimer != null) {
                clearInterval(imageTimer);
            }
            currIndex = i;
            ImageSwitchChange();
        }, function() {
            ImageAutoSwitch();
        });

    });
}
//鼠標放到圖片上的換，停止切換
function ImageStopSwitch() {
    $("#IMG_UL_LIST_1").find("li").each(function() {
        $(this).hover(function() {
            if (imageTimer != null) {
                clearInterval(imageTimer);
            }
        }, function() {
            ImageAutoSwitch();
        });
    });

}
//自动切换图片
function IntervalImage() {
    currIndex = parseFloat(currIndex);
    currIndex = currIndex + 1;
    //如果切換到最大數量的時候則從頭開始
    if (currIndex >= $("#IMG_UL_LIST_1").find("li").length) {
        currIndex = 0;
    }
    //將原來的現實圖片索引的背景透明
    ImageSwitchChange();
}
//切換圖片的時候，響應的圖片要實現漸變效果
function ImageSwitchChange() {
    currImgIndex = IMGINDEX.find("li:eq(" + currIndex + ")");
    imgLi = $("#IMG_UL_LIST_1").find("li:eq(" + currIndex + ")");
    $("#IMG_INDEX_UL_LIST_1").find("li a").removeClass("on").eq(currIndex).addClass("on");
    $("#IMG_INDEX_TEXT").find("a").hide().hide().eq(currIndex).fadeIn(1000);
    if (currImgLi != null) {
         currImgLi.hide();
        imgLi.fadeIn(1000);
    }
    currImgLi = imgLi;
}
