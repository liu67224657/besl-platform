define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/tips');
    var showBigPicTimer;
    var blog = {
        headiconInit:function(){
            $("#headIcons span a img").live('mouseover mouseout', function (event) {
                var bigPic = $(this);
                // $(bigPic).parent().parent().html()
                if (event.type == 'mouseover') {
                    showBigPicTimer = setTimeout(function () {
                        if ($("#showBigPic").length > 0) {
                            $("#showBigPic").remove();
                        }
                        $('body').append("<div id=\"showBigPic\" class=\"showBigPic\"></div>");
                        $(bigPic).parent().clone().appendTo("#showBigPic")
                        $("#showBigPic").css({top:$(bigPic).offset().top + "px", left:$(bigPic).offset().left + "px", "display":"block"});
                        $("#showBigPic a img").animate({width:"150px", height:"150px"}, 100);
                        $("#showBigPic").animate({"left":"-=32px", "top":"-=32px"}, 100);
                    }, 500)
                } else {
                    clearTimeout(showBigPicTimer)
                }
            });
            $("#showBigPic a img").live("mouseout", function () {
                $("#showBigPic").hide().remove();
            });
        }
    }
    return blog;
});




