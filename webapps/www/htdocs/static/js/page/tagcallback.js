define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var joymealert = require('../common/joymealert');
    var login = require('../biz/login-biz');
    var alertOption = {};
    var tagCallback = {
        customizeDelTagCallback:function(tagId, tagName, resultMsg) {
            login.maskLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {
                $('a[name=tagdel][id=' + tagId + ']').parent().remove();
            } else {
                alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            }
        },

        searchTagDelTagCallback:function(tagId, tagName, resultMsg) {
            login.maskLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {
                if ($("#tag_coll").length > 0) {
                    $("#tag_coll").html('<a href="javascript:void(0);" class="favoritbtn" id="a_save_tag" tagid="' + tagId + '" tag_name="' + tagName + '"><span>收藏此标签</span></a>');
                }
                $("#a_tid" + tagId).remove();
            } else {
                alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        },
        searchGameDelTagCallback:function(tagId, tagName, resultMsg) {
            login.maskLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {
                if ($("#tag_coll").length > 0) {
                    $("#tag_coll").html('<a href="javascript:void(0);" class="favoritbtn" id="a_save_tag" tagid="' + tagId + '" tag_name="' + tagName + '"><span>收藏此游戏</span></a>');
                }
                $("#a_tid" + tagId).remove();
            } else {
                alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        },

        searchLikeTagCallback:function(resultMsg) {
           login.maskLoginByJsonObj(resultMsg);
            if (resultMsg.status_code == "1") {
                var str = '';
                for (var i = 0; i < resultMsg.result.length; i++) {
                    str = '<a id="a_tid' + resultMsg.result[i].tagId + '" href="/search/content/' + resultMsg.result[i].tag + '/?srid=a">' + resultMsg.result[i].tag + '</a>';
                    $("#liketags").append(str);
                }

                if ($("#tag_coll").length > 0) {
                    $("#tag_coll").html('<a class="favoritgraybtn"><span class="favoritgraybtn"></span></a>' +
                            '<a href="javascript:void(0);" id="a_del_tag" class="game_cancel" tagid="' + resultMsg.result[0].tagId + '" tag_name="' + resultMsg.result[0].tag + '"><span>取消</span></a>');
                }
            } else {
                alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        }
    }
    return tagCallback;
});