/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-2-28
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var surlT;
    var surlLoader = {
        loadSurl:function (jqSurlLinkDom, skey) {
            if (jqSurlLinkDom.attr('title') != null && jqSurlLinkDom.attr('title').length != 0) {
                return;
            }
            this.stoploadSurl();
            surlT = setTimeout(function () {
                $.post('/json/shorturl/geturl', {urlKey:skey}, function(req) {
                    var jsonResult = eval('(' + req + ')');
                    if (jsonResult.status_code == '1') {
                        var titleAttr = jsonResult.msg;
                        jqSurlLinkDom.attr('title', titleAttr);
                    }
                })
            }, 500)
        },

        stoploadSurl:function() {
            clearTimeout(surlT);
        }
    }
    return surlLoader
});
