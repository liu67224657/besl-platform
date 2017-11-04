define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var atmebiz = {
        removeAt:function(tid,directId,callback){
            $.post("/json/atme/remove",{tid:directId},function(data){
                var jsonObj = eval('('+ data +')');
                callback(tid,jsonObj);
            });
        }

    }

    return atmebiz;
});