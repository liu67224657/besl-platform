define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('./login-biz');
    var ajaxverify = require('./ajaxverify');
    var common = require('../common/common');
    window.replyLock = false;

    var replyBiz = {
        getreplyobject:function(dom, cid, cuno, loadingCallback, callback) {
            $.ajax({
                        type: "POST",
                        url: "/json/reply/getblogreplys",
                        data: {cid:cid,cuno:cuno},
                        success: function(req) {
                            var resultMsg = eval('(' + req + ')')
//                            var resultMsg = data.result[0];
                            callback(dom, cid, cuno, resultMsg)
                        },
                        beforeSend:function() {
                            if (loadingCallback != null) {
                                loadingCallback(dom, cid);
                            }
                        }
                    });
        },
        checkAtSize:function(paramObj,callback, callback1, replyDom, showAtSizeTips,focusDom) {
            $.ajax({
                        url: "/json/atme/size",
                        type:'post',
                        data:{context:paramObj.replycontent},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                showAtSizeTips(paramObj, callback1, replyDom, resMsg,focusDom);
                            } else {
                                callback(paramObj, callback1, replyDom,focusDom);
                            }
                        }
                    });
        },
        checkAtNicks:function(paramObj, transfercallback, replyDom, showAtNicksTips,focusDom) {
            $.ajax({
                        url: "/json/atme/nicks",
                        type:'post',
                        data:{context:paramObj.replycontent},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '1') {
                                showAtNicksTips(paramObj, replyDom, resMsg, transfercallback,focusDom);
                            } else {
                                transfercallback(paramObj, replyDom,focusDom);
                            }
                        }
                    });
        } ,
        reply:function(config, callback, liveSubmitFun) {

            var replyContent = config.replyContent;
            var cid = config.cid;
            var cuno = config.cuno;
            var pid = config.pid;
            var puno = config.puno;
            var forwardroot = config.forwardroot;
            var replyroot = config.replyroot;
            var paramObj = {cid:cid,cuno:cuno,pid:pid,puno:puno,forwardroot:forwardroot,replyroot:replyroot,replycontent:replyContent}

            $.post("/json/reply/publish", paramObj, function(req) {
                var jsonObj = eval('(' + req + ')');
                callback(jsonObj, config);
                liveSubmitFun();
            });
        },
        commonreply:function(paramObj, loadingCallback, callback, replyDom) {
            if (window.replyLock) {
                return false;
            }

            $.ajax({
                        url: "/json/reply/publish",
                        type:'post',
                        data:paramObj,
                        success:function(req) {
                            var jsonObj = eval('(' + req + ')');
                            callback(jsonObj, paramObj, replyDom)
                        },
                        beforeSend:function() {
                            window.replyLock = true;
                            loadingCallback(replyDom);
                        },
                        error:function(){
                           window.replyLock = false;
                        },
                        complete:function(){
                            window.replyLock = false;
                        }
                    });
        },
        deleteReply:function(paramObj, callback) {
            var replyid = paramObj.replyid;
            var cid = paramObj.cid;
            var cuno = paramObj.cuno;
            $.post("/json/reply/delete", {replyid:replyid ,cid:cid,cuno:cuno}, function(req) {
                var jsonObj = eval('(' + req + ')');
                callback(jsonObj, replyid, cid);
            });
        }
    }

    return replyBiz;
});