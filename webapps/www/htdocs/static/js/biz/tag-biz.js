define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var tagbiz = {
        //删除标签--数据库中删除
        delTag:function(tagid, tagname,callback) {
            var resultMsg;

            $.ajax({
                        url: "/json/profile/tag/likedelete",
                        type:'post',
                        data:{tagid:tagid,tagName:tagname},
                        async:false,
                        success:function(req) {
                            resultMsg = eval('(' + req + ')');
                            callback(tagid, tagname,resultMsg);
                        }
                    });

            return resultMsg;
        },

        likeTag:function(tags,callback) {
            var resultMsg;

            $.ajax({
                        url: "/json/profile/tag/like",
                        type:'post',
                        data:{tags:tags},
                        async:false,
                        success:function(req) {
                            resultMsg = eval('(' + req + ')');
                            callback(resultMsg);
                        }
                    });

            return resultMsg;
        }

    }
    return tagbiz;
});