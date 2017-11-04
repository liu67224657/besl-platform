/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-5
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var delBiz = require('../biz/delblog-biz');
    var delCallback = require('./delblogcallback');
    var joymealert = require('../common/joymealert');
    var login = require('../biz/login-biz');

    var delBlog = {
        bindDel:function() {
            $("a[id^=link_del_]").live('click', function() {
                if (login.checkLogin($(this))) {
                    var cid = $(this).attr("data-cid");
                    var uno = $(this).attr("data-uno");

                    var delFunction = function() {
                        delBiz.deleteContent(cid, uno, delCallback.delContentCallback);
                    }

                    var offSet = $(this).offset();
                    var confirmOption = {
                        offset:"Custom",
                        offsetlocation:[offSet.top - 115,offSet.left - 229 / 2],
                        text: '确定要删除该文章吗?',
                        width:229,
                        submitButtonText:'删 除',
                        submitFunction:delFunction,
                        cancelButtonText:'取 消',
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);
                }
            })
        }
    }
    return delBlog;
});