define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var favbiz = {
        favorite:function(cid,cuno,callback){
            $.post("/json/profile/favorite/like",{cid:cid,cuno:cuno},function(data){
                var jsonObj = eval('('+ data +')');
                callback(cid,jsonObj);
            });
        },
        removeFavorite:function(cid,callback){
            $.post("/json/profile/favorite/unlike",{cid:cid},function(data){
                var jsonObj = eval('('+ data +')');
                callback(cid,jsonObj);
            });
        }

    }

    return favbiz;
});






