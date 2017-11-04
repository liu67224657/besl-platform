/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('./login-biz');
    var delContent = {
        deleteContent:function(contentId, uno,callback) {
            $.post('/json/content/del', {cid:contentId,uno:uno}, function(req) {
                var resultMsg = eval('(' + req + ')');
                //todo
                login.maskLoginByJsonObj(resultMsg);
                if (resultMsg.status_code == '1') {
                    callback(contentId);
                } else if (resultMsg.status_code == '0') {
                    alert(resultMsg.msg);
                }
            });
        }
    }

    return delContent;
});