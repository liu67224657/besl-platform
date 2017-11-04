define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var customizebiz = {
        //保存互动设置
        saveUserDefine:function(hintmyfeedback, hintmyfans, hintmyletter, hintmynotice, allowreplay, allowletter, hintat, authorRelation, contentType,callback) {
            $.post("/json/profile/customize/setuserdefine", {hintmyfeedback:hintmyfeedback, hintmyfans:hintmyfans, hintmyletter:hintmyletter,
                        hintmynotice:hintmynotice,allowreplay:allowreplay,allowletter:allowletter,
                        hintat:hintat,authorRelation:authorRelation,contentType:contentType}, function(data) {
                var jsonObj = eval('(' + data + ')');
                callback(jsonObj);
            });
        }


    }
    return customizebiz;
});