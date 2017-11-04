define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var contentPreview = {
        agree:function(unikey, domain, rid, callback) {
            $.post("/jsoncomment/reply/agree", {unikey:unikey,domain:domain,rid:rid}, function(req) {
                var resMsg = eval('(' + req + ')');
                callback(unikey, domain, rid, resMsg);
            });
        },

        post:function(param, callback) {
            $.post("/json/comment/post", {cid:param.cid,pid:param.pid,rid:param.rid,body:param.body,pic:param.pic}, function(req) {
                var resMsg = eval('(' + req + ')');
                callback(param.rid,param.pid,resMsg);
            });
        },
        rePage:function(pageDom,param, callback) {
            $.post("/json/comment/replist", {cid:param.cid,rid:param.rid,repsum:param.repsum,p:param.p}, function(req) {
                var resMsg = eval('(' + req + ')');
                callback(pageDom,resMsg);
            });
        },
        remove:function(rid, rootId, callback){
            $.post("/json/comment/remove", {rid:rid}, function(req) {
                var resMsg = eval('(' + req + ')');
                callback(rid, rootId, resMsg);
            });
        }
    }
    return contentPreview;
});