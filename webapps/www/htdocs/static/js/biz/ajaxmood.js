/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-4
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var moodHtml = {
        integrateFaceMethod:function (callback, offset, textareaID,popConfig,id,te,pos) {
            $("#replyUploadImageDialog").remove();
            if (window.facelist) {
                callback(facelist, offset, textareaID,popConfig,id,te,pos);
            } else {
                window.facelist;
                $.ajax({
                            type: "POST",
                            url: "/json/mood",
                            success:function(data) {
                                var jsonObj = eval('(' + data + ')');
                                if (jsonObj.status_code == "1") {
                                    window.facelist = jsonObj.result[0];
                                    callback(facelist, offset, textareaID,popConfig,id,te,pos);
                                } else {
                                    window.facelist = "网络忙,暂时找不到表情!";
                                    callback(facelist, offset, textareaID,popConfig,id,te,pos);
                                }
                            }
                        });
            }
        }


    }
    return moodHtml;
})