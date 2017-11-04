define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    window.savethumbLock = false;
    var headiconbiz = {
        //剪切头像

        thumbnail:function(filename, x1, y1, x2, y2, w, h, loadingCallback, callback,completeCallback,errorCallback) {
            if (window.savethumbLock) {
                return false;
            }

            $.ajax({
                        url: "/json/upload/thumbnail",
                        type:'post',
                        data:{filename:filename,x1:x1,y1:y1,x2:x2,y2:y2,
                            w:w,h:h,at:joyconfig.token,uno:joyconfig.joyuserno,"appkey":"default"
                        },
                        success:function(data) {
                            var jsonObj = eval('(' + data + ')');
                            callback(jsonObj);
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback();
                            }
                        },
                        error:function() {
                           if (errorCallback != null) {
                                errorCallback();
                            }
                        },
                        complete:function() {
                            window.savethumbLock = false;
                            if (completeCallback != null) {
                                completeCallback();
                            }
                        }
                    });
        },
        //保存头像
        save:function(headIcon) {
            $.post("/json/profile/headicon/save", {headIcon:headIcon}, function(data) {
            });
        }



    }
    return headiconbiz;
});