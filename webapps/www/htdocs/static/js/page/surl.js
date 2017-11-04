define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var surlBiz = require('../biz/surl-biz');
    var surl = {
        surlBind:function() {
            $('a[name=surltag]').live('mouseenter',
                    function() {
                        var skey = $(this).attr('data-skey');
                        surlBiz.loadSurl($(this), skey);
                    }).live('mouseleave', function() {
                        surlBiz.stoploadSurl();
                    });
        }

    }
    return surl;
});