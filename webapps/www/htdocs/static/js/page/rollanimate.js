/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-6-5
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    window.lineNum = 0;
    window.rollTimeId = 0;
    var imgCount= $('a[id^=roll_img_]').length;
    var rollanimate = {
        rollanimate:function() {
            showImage();

            $('#[id^=roll_button_]').bind('mouseenter',
                    function() {
                        var animateId = $(this).attr('data-no');
                        if (animateId == null) {
                            return;
                        }
                        clearInterval(window.rollTimeId);
                        window.lineNum = parseInt(animateId);
                        $(this).attr('class', 'current').siblings().removeClass();
                        $('#roll_img_' + animateId).show().siblings('a').css('display', 'none');
                    }).bind('mouseout', function() {
                        showImage();
                    });
        }
    };


    function showImage() {
        window.rollTimeId = setInterval(function() {
            window.lineNum = window.lineNum >= imgCount ? 0 : window.lineNum + 1;
            var animateDom = $('[id^=roll_img_' + window.lineNum + ']');
            animateDom.siblings('a').css('display', 'none');
            animateDom.show();
            $('#roll_button_' + window.lineNum).attr('class', 'current').siblings().removeClass();
        }, 4000);
    }

    return rollanimate;

})