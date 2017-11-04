define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var noticeBiz = {
        loadNotice:function(callback) {
            $.post("/json/message/memo", {}, function(data) {
                var resultMsg = eval('(' + data + ')');
                callback(resultMsg);
            });
        },
        clearNoticeType:function(typeCode,callback) {
            $.post("/json/message/memo/clearnotice", {typeCode:typeCode}, function(data) {
                var resultMsg = eval('(' + data + ')');
                callback(typeCode,resultMsg);
            });
        },
        stopMemo:function(callback) {
            $.post("/json/message/memo/stopmemo", {}, function(data) {
                var resultMsg = eval('(' + data + ')');
                callback(resultMsg);
            });
        },
        loadMemoContent:function (param) {
            $.ajax({
                        url:"/json/home/memocontent",
                        data:{count:param.count},
                        type:'post',
                        beforeSend:function() {
                            param.loadfunction();
                        },
                        success:function (data) {
                            var jsonObj = eval('(' + data + ')');
                            if (jsonObj.status_code == '-1') {
                                var loginOption = {
                                    referer:window.location.href
                                };
                                login.maskLogin(loginOption);
                                return;
                            }

                            param.callback(jsonObj, param);
                        }});
        }

    }

    return noticeBiz;
});
