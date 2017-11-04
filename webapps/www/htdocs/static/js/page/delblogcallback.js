/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('../biz/login-biz');
    var del = {
        delContentCallback:function(cid) {
             $('#conent_'+cid).slideUp(function(){
                 $(this).remove();
             })
        }
    }

    return del;
});