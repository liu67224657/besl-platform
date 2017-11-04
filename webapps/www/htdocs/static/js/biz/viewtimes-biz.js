/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var viewtimesbiz = {
        ajaxAddViewTime:function(contentId, uno) {
            $.post('/json/content/viewnum', {contentId:contentId,contentUno:uno}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (resultMsg == null || resultMsg.status_code == '-1') {
//                    alert(false);
                }

            });
        }
    }

    return viewtimesbiz;
});