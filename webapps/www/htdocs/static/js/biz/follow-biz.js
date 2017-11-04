define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('./login-biz');

    var follow = {
        ajaxFocus:function (uno, focusuno, callback) {
            if (uno == null || uno.length == 0) {
                login.maskLoginByJsonObj(null);
                return false;
            } else if (uno == focusuno) {
                return false;
            } else {
                $.post(joyconfig.ctx + "/json/social/follow", {focusuno:focusuno}, function(req) {
                    var resultMsg = eval('(' + req + ')');
                    if (!login.maskLoginByJsonObj(resultMsg)) {
                        return;
                    }
                    callback(focusuno, resultMsg);
                });
            }
        },

        ajaxUnfans:function (fansuno, callback) {
            $.post(joyconfig.ctx + '/json/social/fans/remove', {fansuno:fansuno}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (!login.maskLoginByJsonObj(resultMsg)) {
                    return;
                }
                callback(fansuno, resultMsg);
            });
        },

        ajaxUnFocus:function(focusuno, callback) {
            $.post(joyconfig.ctx + '/json/social/follow/unfollow', {focusuno:focusuno}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (!login.maskLoginByJsonObj(resultMsg)) {
                    return;
                }
                callback(focusuno, resultMsg);
            });
        },

        ajaxJoinCate:function(destUno, cateIds, typeName, callback) {
            $.post(joyconfig.ctx + '/json/profile/usertype/addtype', {destUno:destUno,cateId:cateIds}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (!login.maskLoginByJsonObj(resultMsg)) {
                    return;
                }
                callback(destUno, typeName, resultMsg);
            });
        },

        ajaxUnjoinCate:function (destUno, cateId, callback) {
            $.post(joyconfig.ctx + '/json/profile/usertype/removetype', {destUno:destUno,cateId:cateId}, function(req) {

                var resultMsg = eval('(' + req + ')');
                if (!login.maskLoginByJsonObj(resultMsg)) {
                    return;
                }
                callback(destUno, cateId, resultMsg)
            });
        },

        ajaxFollowBoard:function(dom, id, reason, callback) {
            $.post(joyconfig.ctx + '/json/group/join', {id:id,reason:reason}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (!login.maskLoginByJsonObj(resultMsg)) {
                    return;
                }
                callback(dom, id, resultMsg);
            });
        },

        ajaxUnFollowBoard:function(dom, id, callback) {
            $.post(joyconfig.ctx + '/json/group/unjoin', {id:id}, function(req) {
                var resultMsg = eval('(' + req + ')');
                if (!login.maskLoginByJsonObj(resultMsg)) {
                    return;
                }
                callback(dom, id, resultMsg);
            });
        }

    }

    return follow;
});






