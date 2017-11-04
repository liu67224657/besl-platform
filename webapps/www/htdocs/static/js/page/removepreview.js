/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-27
 * Time: 下午5:04
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2')
    var removeObj = {
        cancelPhoto:function(imgLinkidx) {
            $.each(blogContent.image, function(i, val) {
                if (val.key == imgLinkidx) {
                    blogContent.image = $.grep(blogContent.image, function(n) {
                        return n.key != imgLinkidx;
                    });
                }
            });
        },
        cancelAudio:function() {
            blogContent.audio = null;
        },
        cancelVideo:function() {
            blogContent.video = null;
        }
    }
    return removeObj;
});