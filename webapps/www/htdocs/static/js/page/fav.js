define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var loginBiz = require('../biz/login-biz');
    var favbiz = require('../biz/fav-biz');
    var ajaxVerify = require('../biz/ajaxverify');
    var joymealert = require('../common/joymealert');
    var discovery = require('./discovery');
    require('../common/tips');
    var fav = {
        initFavorite:function() {
            $("a[name=favLink]").live('click', function() {
                if (loginBiz.checkLogin($(this))) {
                    if ($(this).attr("class") == "save") {
                        favbiz.favorite($(this).attr("data-id"), $(this).attr("data-uno"), favoriteCallback);
                    }
                }
            });
        },
        initFavoritePreview:function() {
            $("a[name=favLink]").live('click', function() {
                if (joyconfig.joyuserno == '') {
                    //todo
//                    loginBiz.maskLogin();
                    discovery.appendLogin(favbiz.favorite, [$(this).attr("data-id"),$(this).attr("data-uno"),favoritepreviewCallback]);
                    return;
                }
                if ($(this).attr("class") == "see_love") {
                    favbiz.favorite($(this).attr("data-id"), $(this).attr("data-uno"), favoritepreviewCallback);
                }
            });
        },
        initRemoveFavorite:function() {
            $("a[name=favLink]").live('click', function() {
                var cid = $(this).attr("data-id");
                var confirmfunc = function() {
                    favbiz.removeFavorite(cid, removeFavoriteCallback);
                }
                if ($(this).attr("class") == "save_on") {
                    var offSet = $(this).offset();
                    var tips = '确定要取消收藏吗?';
                    var confirmOption = {
                        confirmid:cid,
                        offset:"Custom",
                        offsetlocation:[offSet.top,offSet.left],
                        text:tips,
                        width:229,
                        submitButtonText:'确 定',
                        submitFunction:confirmfunc,
                        cancelButtonText:'取 消',
                        cancelFunction:null};
                    joymealert.confirm(confirmOption);

                }
            });
        },

        favorite:function(contentId, contentUno) {
            if (ajaxVerify.verifyContentIsRemove(contentUno, contentId)) {
                var alertOption = {text:'文章已被作者删除，不能收藏', tipLayer:true, textClass:"tipstext"}
                joymealert.alert(alertOption);
                return;
            }
            favbiz.favorite(contentId, contentUno, favoriteCallback);
        },

        removeFavorite:function(contentId) {
            favbiz.removeFavorite(contentId, removeFavoriteCallback);
        }
    }

    var favoriteCallback = function(contentId, resultJson) {
        if (resultJson.status_code == '1') {
            releaseFavNum(contentId, "已收藏", "save", 1);
        }else if(resultJson.status_code == '2'){
            releaseFavNum(contentId, "已收藏", "save", 0);
        }else if (resultJson.status_code == '-1') {
            loginBiz.maskLoginByJsonObj(resultJson);
        } else if (resultJson.status_code == '-5') {
            var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                tipLayer:true,
                textClass:"tipstext",
                callbackFunction:function() {
                    if (resultJson.result != null && resultJson.result.length > 0) {
                        window.location.href = resultJson.result[0];
                    }
                }};
            joymealert.alert(alertOption);
        }
    }
    var favoritepreviewCallback = function(contentId, resultJson) {
        if (resultJson.status_code == '1') {
            releaseFavNum(contentId, "", "see_love_on", 1);
        } else if (resultJson.status_code == '-1') {
            loginBiz.maskLoginByJsonObj(resultJson);
        } else if (resultJson.status_code == '-5') {
            var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                tipLayer:true,
                textClass:"tipstext",
                callbackFunction:function() {
                    if (resultJson.result != null && resultJson.result.length > 0) {
                        window.location.href = resultJson.result[0];
                    }
                }};
            joymealert.alert(alertOption);
        }
    }
    var removeFavoriteCallback = function(contentId, resultJson) {
        if (resultJson.status_code == '1') {
            $("#conent_" + contentId).remove();
            releaseFavNum(contentId, "收藏", "save", -1);
        } else if (resultJson.status_code == '-1') {
            loginBiz.maskLoginByJsonObj(resultJson);
        } else if (resultJson.status_code == '-5') {
            var alertOption = {text:tipsText.profile.user_ipforbidden_forbidlogin,
                tipLayer:true,
                textClass:"tipstext",
                callbackFunction:function() {
                    if (resultJson.result != null && resultJson.result.length > 0) {
                        window.location.href = resultJson.result[0];
                    }
                }};
            joymealert.alert(alertOption);
        }
    }

    var releaseFavNum = function(contentId, title, className, num) {
        if ($('a[name=favLink][data-id=' + contentId + ']').length > 0) {
            $.each($('a[name=favLink][data-id=' + contentId + ']'), function(i, val) {
                val.title = title;
                val.className = className;
                val.innerHTML = title + '(' + (parseInt(val.innerHTML.replace(/[^\d]/g, '')) + num) + ') ';
            });
        }
    }

    return fav;
});
