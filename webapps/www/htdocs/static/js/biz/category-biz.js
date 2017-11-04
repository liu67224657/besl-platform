define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var categoryBiz = {
        getCategoryPrivacy:function(jqObj,loadingCallback, callback) {
            $.ajax({
                        url:'/json/category/privacy',
                        type:'post',
                        data: {cid:jqObj.attr("data-cid"),cuno:jqObj.attr("data-cuno")},
                        success:function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jqObj,jsonObj);
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(jqObj);
                            }
                        }
                    });
        },
        gatherCategory:function(jqObj,paramObj,loadingCallback,callback){
            $.ajax({
                        url:'/json/category/gather',
                        type:'post',
                        data: {ids:paramObj.ids,allIds:paramObj.allIds,cid:paramObj.cid,cuno:paramObj.cuno},
                        success:function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jqObj,jsonObj);
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(jqObj);
                            }
                        }
                    });
        },
        categoryorder:function(jqObj,callback){
            $.ajax({
                        url:'/json/category/topadd',
                        type:'post',
                        data: {categoryid:jqObj.attr("data-categoryid"),cid:jqObj.attr("data-cid"),cuno:jqObj.attr("data-cuno")},
                        success:function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jqObj,jsonObj);
                        }
                    });
        },
        canceltop:function(jqObj,callback){
            $.ajax({
                        url:'/json/category/canceltop',
                        type:'post',
                        data: {lineid:jqObj.attr("data-lineid"),cid:jqObj.attr("data-cid"),cuno:jqObj.attr("data-cuno")},
                        success:function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jqObj,jsonObj);
                        }
                    });
        },
        toporder:function(jqObj,paramObj,callback){
            $.ajax({
                        url:'/json/category/toporder',
                        type:'post',
                        data: {lineid:paramObj.lineid,ordervalue:paramObj.ordervalue,cid:paramObj.cid,cuno:paramObj.cuno},
                        success:function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jqObj,paramObj,jsonObj);
                        }
                    });
        }

    }

    return categoryBiz;
});