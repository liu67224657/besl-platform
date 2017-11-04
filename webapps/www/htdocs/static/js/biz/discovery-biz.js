define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var discovery = {
        loadLayout:function (index, callback) {
            $.post("/json/discovery/layout", {idx:index}, function(req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg != null || resMsg.status_code == '1') {
                    callback(resMsg);
                }
            });
        },
        loadDisplayLayout:function (index,magazineCode, callback) {
            $.post("/json/magazine/"+magazineCode, {idx:index}, function(req) {
                var resMsg = eval('(' + req + ')');
                if (resMsg != null || resMsg.status_code == '1') {
                    callback(resMsg);
                }
            });
        },

        packegPreview:function(cuno, cid, contentIdx, successCallback, errorcallback) {
            var dataObj;
            if(contentIdx != ''){
                dataObj = {cuno:cuno,cid:cid,idx:contentIdx};
            }else{
                dataObj = {cuno:cuno,cid:cid};
            }
            $.ajax({
                        url:'/json/discovery/preview',
                        data: dataObj,
                        type:'post',
                        success:function(data) {
                            successCallback(data);
                        },
                        error:function(){
                            errorcallback(cuno, cid, successCallback);
                        }
                    });
        },
        preContent:function(magazineCode,contentIdx,successCallback, errorCallback){
            $.ajax({
                url:'/json/magazine/prev',
                data: {idx:contentIdx,code:magazineCode},
                type:'post',
                success:function(data) {
                    successCallback(data);
                },
                error:function(){
                    errorCallback(magazineCode, contentIdx, successCallback,data);
                }
            });
        },
        nextContent:function(magazineCode,contentIdx,successCallback, errorCallback){
            $.ajax({
                url:'/json/magazine/next',
                data: {idx:contentIdx,code:magazineCode},
                type:'post',
                success:function(data) {
                    successCallback(data);
                },
                error:function(){
                    errorCallback(magazineCode, contentIdx, successCallback);
                }
            });
        }
    }

    return discovery;
})
        ;






