define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('../biz/login-biz');
    var common = require('../common/common');
    var joymealert = require('../common/joymealert');
    var card = require('./card');

    var followCallback = {
        followCallBack:function(focusuno, resMsg) {
            if (resMsg.status_code == '0') {
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            } else {
                //更新名片
                resetRelationOnCard(focusuno, resMsg);
                resetRelationOnList(focusuno, resMsg);
                followCallback.followSimpleCallBack(focusuno, resMsg);
                common.increaseCount('r_focus_num', 1);
            }
        },
        followSimpleCallBack:function(focusuno, resMsg) {
            if (resMsg.status_code == '0') {
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            } else {
                //更新名片
                resetRelationOnCard(focusuno, resMsg);
                resetRelationOnBlog(focusuno, resMsg);
                restRelationOnSimple(focusuno, resMsg);
                $('#link_uninterested_recommend_'+focusuno).remove();
                common.increaseCount('r_focus_num', 1);
            }
        },

        followOtherCallBack:function(focusuno, resMsg) {
            if (resMsg.status_code == '0') {
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            } else {
                //更新名片
                resetRelationOnCard(focusuno, resMsg);
                resetRelationOnList(focusuno, resMsg);
            }
        },

        fansfollowCallBack:function(focusuno, resMsg) {
            if (resMsg.status_code == '0') {
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            } else {
                //更新名片
                resetRelationOnCard(focusuno, resMsg);
                resetRelationOnFansList(focusuno, resMsg);
                common.increaseCount('r_focus_num', 1);
            }
        },

        activeFolllowCallback:function (focusuno, resMsg) {
            if (resMsg.status_code == '0') {
                var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
                return false;
            } else {
                if (resMsg.msg == 'e') {
                    $("#focus_" + focusuno).attr("class", "attentioned_nocancel");
                    // $("#focus_" + focusuno).html('相互关注');
                } else if (resMsg.msg == 't') {
                    $("#focus_" + focusuno).attr("class", "attentioned_nocancel");
                    //$("#focus_" + focusuno).html('已关注');
                }
            }
        }, //end ajaxActiveFocusCallback

        followCallbackOnBlog:function(focusuno, resMsg) {
            if (resMsg.status_code == '0') {
                if (resMsg.msg == 'exists') {
                    var alertOption = {text:resultMsg.msg,tipLayer:true,textClass:"tipstext"};
                    joymealert.alert(alertOption);
                }
            } else {
                //更新名片
                resetRelationOnCard(focusuno, resMsg);
                resetRelationOnBlog(focusuno, resMsg);
                if (joyconfig.joyuserno == currentProfile.uno) {
                    common.increaseCount('r_focus_num', 1);
                } else if (currentProfile.uno == focusuno) {
                    //你关注了别人
                    common.increaseCount('r_fans_num', 1);
                }
            }
        },
        followCallbackOnIndex:function(focusuno, resMsg) {
            resetRelationOnBlog(focusuno, resMsg)
        },

        ////////////////////////////////////////////////////////////
        unFollowAndRemoveOnList:function(focusuno, reMsg) {
            $("#socialprofile_" + focusuno).remove();
            common.increaseCount('r_focus_num', -1);
            common.increaseCount('group_count', -1);
            common.increaseCount('follow_title_count', -1);
            card.hideCard();
        },

        unFollowOnList:function (focusuno, resultMsg) {
            resetRelationOnCard(focusuno, resultMsg);
            resetRelationOnList(focusuno, resultMsg);
            common.increaseCount('r_focus_num', -1);
        } ,
        unFollowOtherOnList:function (focusuno, resultMsg) {
            resetRelationOnCard(focusuno, resultMsg);
            resetRelationOnList(focusuno, resultMsg);
        } ,

        unFollowOnFansList:function (focusuno, resultMsg) {
            resetRelationOnCard(focusuno, resultMsg);
            resetRelationOnFansList(focusuno, resultMsg);
            common.increaseCount('r_focus_num', -1);
        } ,

        unFollowOnBlog:function(focusuno, resultMsg) {
            resetRelationOnCard(focusuno, resultMsg);
            resetRelationOnBlog(focusuno, resultMsg);
            if (joyconfig.joyuserno == currentProfile.uno) {
                common.increaseCount('r_focus_num', -1);
            } else if (currentProfile.uno == focusuno) {
                //你关注了别人
                common.increaseCount('r_fans_num', -1);
            }
        },
        unfollowOnIndex:function(focusuno, resMsg) {
            resetRelationOnBlog(focusuno, resMsg)
        },

        unFansOnList:function(fansuno, resultMsg) {
            $("#socialprofile_" + fansuno).remove();
            card.hideCard();
            common.increaseCount('r_fans_num', -1);
        },

        //////////////////////////////////////////////////////////
        joinUserCate:function(destUno, cateName, reMsg) {
            login.locationLoginByJsonObj(reMsg);
            if (reMsg.status_code == '0') {
                return;
            }
            var spanUserCate = $($("#follow_usercate_" + destUno).find('span')[0]);

            var cateDisplay = $.trim(spanUserCate.text());
            if (cateDisplay == '未分组') {
                cateDisplay = cateName;
            } else {
                cateDisplay += ',' + cateName;
            }
            if (cateDisplay.length > 5) {
                cateDisplay = cateDisplay.substr(0, 5) + '…';
            }
            spanUserCate.text(cateDisplay);
        },

        unjoinUserCate:function(destUno, cId, reMsg) {
            login.locationLoginByJsonObj(reMsg);
            if (reMsg.status_code == '0') {
                return;
            }

            if (cId != null && cId == currentCateId) {
                $("#socialprofile_" + destUno).remove();
            }

            var spanUserCate = $($("#follow_usercate_" + destUno).find('span')[0]);
            var ckeckedUserCate = $('input[type=checkbox][id^=chktype_' + destUno + ']:checked');
            if (ckeckedUserCate.length == 0) {
                spanUserCate.text('未分组');
                return;
            }
            var groupText = '';
            $.each(ckeckedUserCate, function(i, val) {
                var cateLabel = $(val).next();
                if (i == 0) {
                    groupText += cateLabel.text();
                } else {
                    groupText += ',' + cateLabel.text();
                }

                if (groupText.length > 5) {
                    return false;
                }
            });
            if (groupText.length > 5) {
                groupText = groupText.substr(0, 5) + '…';
            }
            spanUserCate.text(groupText);
        }
    }

    function resetRelationOnList(focusuno, resultMsg) {
        var relation = $("#relation_" + focusuno + ">p");
        var cancelHtml = '';
        if (relation.length > 0) {
            if (resultMsg.msg == 'e') {
                relation.replaceWith('<p><span class="attentionclosely" title="相互关注"></span></p>');
                cancelHtml = '<a href="javascript:void(0)" style="display:none" id="remove_follow_' + focusuno + '"><span>取消关注</span></a>';
            } else if (resultMsg.msg == 't') {
                relation.html('<p><a href="javascript:void(0)" class="attentioned_nocancel" title="已关注"></a></p>');
                cancelHtml = '<a href="javascript:void(0)" style="display:none" id="remove_follow_' + focusuno + '"><span>取消关注</span></a>';
            } else if (resultMsg.msg == 'fan') {
                relation.html('<p><a id="follow_' + focusuno + '" href="javascript:void(0)" class="add_attention_ok"></a></p>');
            } else if (resultMsg.msg == 'f') {
                relation.html('<p><a id="follow_' + focusuno + '" href="javascript:void(0)" class="add_attention"></a></p>');
            }
            $('#cancel_' + focusuno).html(cancelHtml);
        }
    }

    function resetRelationOnFansList(focusuno, resultMsg) {
        var relation = $("#relation_" + focusuno + ">p");
        var cancelHtml = '';
        if (relation.length > 0) {
            if (resultMsg.msg == 'e') {
                relation.replaceWith('<p><span class="attentionclosely" title="相互关注"></span></p>');
            } else if (resultMsg.msg == 't') {
                relation.html('<p><a href="javascript:void(0)" class="attentioned_nocancel" title="已关注"></a></p>');
            } else if (resultMsg.msg == 'fan') {
                relation.html('<p><a id="follow_' + focusuno + '" href="javascript:void(0)" class="add_attention_ok"></a></p>');
            } else if (resultMsg.msg == 'f') {
                relation.html('<p><a id="follow_' + focusuno + '" href="javascript:void(0)" class="add_attention"></a></p>');
            }
            cancelHtml = '<a href="javascript:void(0)" style="display:none" id="remove_fans_' + focusuno + '"><span>移除粉丝</span></a>';
            $('#cancel_' + focusuno).html(cancelHtml);
        }
    }

    function resetRelationOnBlog(focusuno, resultMsg) {
        $('a[name$=follow][data-uno=' + focusuno + ']').each(function(i, val) {
            if (resultMsg.msg == 't') {
                val.title = "已关注";
                val.className = "attentioned";
                val.name = "unfollow";
            } else if (resultMsg.msg == 'e') {
                val.title = "相互关注";
                val.className = "attentionedall";
                val.name = "unfollow";
            } else if (resultMsg.msg == 'fan') {
                val.title = "加关注";
                val.className = "add_attention_ok";
                val.name = "follow";
            } else if (resultMsg.msg == 'f') {
                val.title = "加关注";
                val.className = "add_attention";
                val.name = "follow";
            }
        });
    }

    function restRelationOnSimple(focusuno, resultMsg) {
        $('a[name$=follow-simple][data-uno=' + focusuno + ']').each(function(i, val) {
            val.title = "已关注";
            val.className = "attentioned_nocancel";
        });
    }

//重新设置名片中的关系
    function resetRelationOnCard(focusuno, resultMsg) {
        var card_relation = $("#card_relation_" + focusuno);
        if (card_relation.length > 0) {
            var relationHtml = '';
            if (resultMsg.msg == 't') {
                relationHtml = '<a  class="attentioned" href="javascript:void(0);"></a>';
            } else if (resultMsg.msg == 'e') {
                relationHtml = '<a class="attentionedall" href="javascript:void(0);"></a>';
            } else if (resultMsg.msg == 'fan') {
                relationHtml = '<a class="add_attention_ok" href="javascript:void(0);"></a>';
            } else if (resultMsg.msg == 'f') {
                relationHtml = '<a class="add_attention" href="javascript:void(0);"></a>';
            }
            card_relation.html(relationHtml);
        }
    }

    function generatorUserCate(destUno) {
        $('input[type=checkbox][id^=chktype_' + destUno + ']:checked').length;
        cateDisplay += ',' + cateName;
    }

    return followCallback;
});










