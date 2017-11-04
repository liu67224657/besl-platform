define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var contentTypeBiz = require('../biz/contenttype-biz.js');

    var discovery = {
        rollCallback:function(resultMsg, start) {
            var disc = resultMsg.result;
            if (disc.length < 4) {
                start += 4
                return;
            }

            var boxIndex = start > 0 ? start - 1 : 0;
            start += 4;

            var str = '<div class="linebox">';
            $.each(disc, function(i, val) {
                var contType = val.contentType;

                //图片
                if (val.thumbImgLink != null && val.thumbImgLink.length > 0) {
                    str += '<a href="' + joyconfig.URL_WWW + '/note/' + val.contentId + '"  class="kanbox" title="' + val.wallContent + '">' +
                            '<div class="findimg">' +
                            '<p>'+'<img width="192" height="155" src="' + val.thumbImgLink + '"/>' +
                            '</p>' +
                            '</div>';
                    if(contentTypeBiz.hasVideo(contType.value) || contentTypeBiz.hasAudio(contType.value)){
                      str +='<div class="kanplay" href="javascript:void(0)" title="播放"></div>';
                    }
                    str += '<div class="findtxt">' +
                            '<span class="kanname">' + val.screenName + '</span>：<span>' + val.wallContent + '</span>' +
                            '</div>' +
                            '</a>';
                    ;
                }
                boxIndex++;
            });
            str += '</div>';
            $(str).appendTo($('#discovery_list'));
            $('.linebox').last().css("margin-top", "0px");
            $('.linebox').first().animate({marginTop:"10px",opacity:"0"}, 1000, function() {
                $('.linebox').first().next().css("margin-top", "250px");
                $('.linebox').first().remove();
            });
             return start;
        }
    }
    return discovery
});