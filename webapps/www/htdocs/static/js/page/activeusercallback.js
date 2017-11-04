/**
 * User: zhaoxin
 * Date: 12-2-6
 * Time: 下午3:22
 */

define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('../biz/login-biz');
    var common = require('../common/common');
    var followBiz = require('../biz/follow-biz');
    var followCallback = require('./followcallback');

    var rightCallback = {

        loadActiveCallback:function (jsonObj) {
            if (jsonObj.status_code == '0') {
                return;
            }

            $('#is_active_display').replaceWith(
                    '<div class="usercon mt15">' +
                            '<div class="userhd clearfix">' +
                            '<h3> / 近期活跃用户排行榜</h3> ' +
                            '</div>' +
                            '<div id="ucfix_id" class="userbd clearfix"></div></div>');

            $.each(jsonObj.result, function(i, val) {
                var div_auc = '<div class="active_user clearfix">';
                var span_begin = '<span ';
                var span_class = 'class="personface">';
                var face_size = '58px';
                if (i != 0) {
                    span_class = 'class="commenfacecon">';
                    face_size = '33px';
                }

                var imgSrc = (val.profile.blog.headIcon == null || val.profile.blog.headIcon.length == 0) ?
                        joyconfig.URL_LIB + '/static/theme/default/img/default.jpg':common.parseSimg(val.profile.blog.headIcon, joyconfig.DOMAIN);
                var a_face = '<a href="' + joyconfig.URL_WWW + '/people/' + $.trim(val.profile.blog.domain) + '">';
                var img_face = '<img src="' + imgSrc + '" class="user" width="' + face_size + '" height="' + face_size + '"/>';
                var a_end = '</a>';
                var span_end = '</span> ';

                var screen_name=val.profile.blog.screenName.length>8?val.profile.blog.screenName.substring(0,8)+'...':val.profile.blog.screenName;
                var a_screen_name = '<a href="' + joyconfig.URL_WWW + '/people/' + $.trim(val.profile.blog.domain)  + '">' + screen_name + '</a>';

                var p = '';
                if (joyconfig.joyuserno != val.profile.blog.uno) {
                    if (val.relation == null) {
                        p = '<div>' +
                                '<a id="focus_' + val.profile.blog.uno + '"  class="add_attention"  href="javascript:void(0)" title="加关注" name="addactivefoucs" focusuno="' + val.profile.blog.uno + '"></a>' +
                                '</div>';
                    } else if (val.relation.srcStatus.code == 'n') {
                        p = '<div>' +
                                '<a id="focus_' + val.profile.blog.uno + '"  class="add_attention"  href="javascript:void(0)" title="加关注" name="addactivefoucs" focusuno="' + val.profile.blog.uno + '"></a>' +
                                '</div>';
                    } else if (val.relation.srcStatus.code == 'y' && val.relation.destStatus.code == 'n') {
                        p = '<div>' +
                                '<a id="focus_' + val.profile.blog.uno + '"  class="attentioned_nocancel" ></a>' +
                                '</div>';
                    } else {
                        p = '<div>' +
                                '<a id="focus_' + val.profile.blog.uno + '"  class="attentioned_nocancel"></a>' +
                                '</div>';
                    }
                }

                $('a[id^=focus_]').live('click', function() {
                    var id = $(this).attr('id');
                    var uno = id.substr('focus_'.length, id.length - 'focus_'.length);
                    followBiz.ajaxFocus(joyconfig.joyuserno, uno, followCallback.activeFolllowCallback);
                })

                $("#ucfix_id").append(div_auc + span_begin + span_class + a_face + img_face + a_end + span_end + a_screen_name + p);
            });


        },  //end loadAction

        saveTagCallback:function(jsonObj) {
            if (jsonObj.status_code == "1") {
                //todo alert
                //jmAlert("收藏成功", true);
                var tagObj = jsonObj.result[0];

                //如果标签数<10
                if ($("#tags a").length < 10) {
                    $("#tags").append('<a href="javascript:void(0)" id="a_id' + tagObj.tagId + '" href="${ctx}/search/content/' + tagObj.tag + '/?srid=a" >' + tagObj.tag + '</a>');
                }
                if ($("#tag_coll").length > 0) {
                    $("#tag_coll").html('<p><span class="favoritgraybtn">已收藏</span><a href="javascript:void(0);" class="game_cancel" name="a_del_tag" tagid="' + tagObj.tagId + '" tag_name="' + tagObj.tag + '">取消</a></p>');
                }
            } else {
                //todo alert
                //jmAlert(jsonObj.msg, false);
            }
        }
    }


    return rightCallback;
});