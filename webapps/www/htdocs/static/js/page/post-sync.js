/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-7
 * Time: 上午10:11
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var postBiz = require('../biz/post-biz');

    var postSync = {
        getSyncHtml:function() {
            var resultMsg = postBiz.getBindSync();
            return initBindSyncHtml(resultMsg);
        }
    }

    function initBindSyncHtml(resultMsg) {
        var html = '';
        html += '<div class="tbcon">';
        if (resultMsg.result == 0) {
            html += '<p>您还没有绑定任何网站，无法同步</p>';

        } else {
            html += '<ul class="clearfix">';
            $.each(resultMsg.result, function(i, val) {
                html += '<li class="bind_' + val.code + '"></li>';
            })
            html += '</ul>';
        }
        html += '<a href="/profile/customize/bind" target="_blank">修改同步设置>></a></div>';
        return html;
    }

    return postSync;
});