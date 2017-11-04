/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-15
 * Time: 下午4:32
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common')
    var chatModel = {
        chatModel:function () {
            var blogContentText = common.getBlogContent(CKEDITOR.instances['text_content'].getData());
            $(".pop").hide();
            if (blogContentText == null || blogContentText == 0) {
                blogContent = {content:'',audio:null,video:null,image:new Array(),ios:null};
                $("#posttext").find("input[name='tags']").each(function() {
                    $(this).clone().appendTo("#postchat");
                    $(this).remove();
                });
                return true;
            }
            return false;
        }
    };
    return chatModel;
});
