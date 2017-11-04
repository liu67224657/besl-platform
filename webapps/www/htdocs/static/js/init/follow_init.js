define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var validate = require('../common/jquery.validate.min');
    var common = require('../common/common');
    var header = require('../page/header');
    var verify = require('../biz/ajaxverify');

    var card = require('../page/card');

    var follow = require('../page/follow');
    var followCallback = require('../page/followcallback');

    require('../common/tips');

    $().ready(function() {
        //头像名片
        card.bindCard(followCallback.followCallBack, followCallback.unFollowAndRemoveOnList);
        //焦点失去隐藏判断
        $("body").die().live('mousedown', function() {
            if (window.isOut) {
                $(".pop").hide();
                hideFocusBtn();
                isOut = true;
            }
        });

        $("#g_menu_more").click(function() {
            $("#g_more_select").slideToggle("fast");
        });

        $('a[id^=follow_usercate_]').click(function() {
            var id = $(this).attr('id');
            var uno = id.substr('follow_usercate_'.length, id.length - 'follow_usercate_'.length);
            follow.sildeUserCateMenu(uno);
        })

        //加关注
        $("#but_add_type").click(function() {
            follow.addType();
            setTimeout(function() {
                $("#cateName").focus()
            }, 400);
        });
        $('#submit_addtype').live('click', function() {
            $('#form_addtype').validate({
                        rules: {
                            cateName: {required:true, maxlength:8,verifyWord:'',verifyTypeExists:''}
                        },
                        messages: {
                            cateName: {required:tipsText.userGroup.usertype_typename_notnull,
                                maxlength:tipsText.userGroup.usertype_typename_maxlength
                            }
                        },
                        showErrors: showErrors//使用自定义的提示方法
                    });

            $('#form_addtype').submit();
        });

        $('#modify_cate').click(function() {
            if (currentCateId != undefined) {
                var currentCateName = $.trim($('#categroy_list>a[class=hover]').attr('title'));

                follow.modifyCate(currentCateId, currentCateName);
                setTimeout(function() {
                    $("#cateName").focus()
                }, 400);
            }

        });
        $('#submit_modifytype').live('click', function() {
            $('#form_modifytype').validate({
                        rules: {
                            cateName: {required:true, maxlength:8,verifyWord:'',verifyTypeExists:''}
                        },
                        messages: {
                            cateName: {required:tipsText.userGroup.usertype_typename_notnull,
                                maxlength:tipsText.userGroup.usertype_typename_maxlength
                            }
                        },
                        showErrors: showErrors//使用自定义的提示方法
                    });

            $('#form_modifytype').submit();
        });

        $('#del_cate').click(function() {
//            if (!confirm('确定要删除该分组吗？')) {
//                return false;
//            }
//            return true;
//            $(this).attr('disable','true');
            if (currentCateId != null && currentCateId > 0) {
                follow.delUserCategroy(currentCateId);
            }
        });

        $('a[id^=link_setdesc_]').click(function() {
            var id = $(this).attr('id');
            var uno = id.substr('link_setdesc_'.length, id.length - 'link_setdesc_'.length);
            var desc = $('#fdesc_' + uno).val();
            follow.setDesc(uno, desc, currentPage, totalRows);
        });

        $('#submit_setdesc').live('click', function() {
            $('#form_setdesc').validate({
                        rules: {
                            desc: {inputLength:'',verifyDescWord:''}
                        },
                        messages: {
                            desc: {inputLength:tipsText.joyfocus.user_demo_maxlength
                            }
                        },
                        showErrors: showErrors//使用自定义的提示方法
                    });
            $('#form_setdesc').submit();
        });

        $('.friend-box').live('mouseenter',
                function() {
                    var id = $(this).attr('id');
                    var uno = id.substr('socialprofile_'.length, id.length - 'socialprofile_'.length);
                    $('#cancel_' + uno).children('a').css('display', '');
                    $('#set_desc_' + uno).css('display', '');
                }).live('mouseleave', function() {
                    var id = $(this).attr('id');
                    var uno = id.substr('socialprofile_'.length, id.length - 'socialprofile_'.length);
                    $('#cancel_' + uno).children('a').css('display', 'none');
                    $('#set_desc_' + uno).css('display', 'none');
                });

        //取消关注
        $('a[id^=remove_follow_]').live('click', function() {
            var id = $(this).attr('id');
            var uno = id.substr('remove_follow_'.length, id.length - 'remove_follow_'.length);

            var nickName = $('#hid_nickname_' + uno).val();
            follow.unfollow($(this), nickName, uno, followCallback.unFollowAndRemoveOnList);
//            follow.showUnfocusMask(uno);
//            followBiz.ajaxUnFocus(uno, followCallback.unFollowAndRemoveOnList);
        });

        //关注列表中的分组设置
        $('.fenzuinput').click(function() {
            var id = $(this).attr('id');
            var params = id.split('_');
            var uno = params[1];
            var cId = params[2];
            var cateName = $(this).next().text();
            addOrdeleteUserType(uno, cId, cateName, followCallback.joinUserCate, followCallback.unjoinUserCate);
        });

        $('.fenzu-add').click(function() {

            $(this).css('display', 'none').next().css('display', '');
            $(this).parent().prev().addClass('noline');
        });

        $('.sbumitlistcreatecate').click(function() {
            var id = $(this).attr('id');
            var uno = id.substr('submit_lcreatecate_'.length, id.length - 'submit_lcreatecate_'.length);
            follow.submitListUserCate(uno);
        });

        $('.canclelistcreatecate').click(function() {
            var id = $(this).attr('id');
            var uno = id.substr('cancel_lcreatecate_'.length, id.length - 'cancel_lcreatecate_'.length);
            $('#form_createtype_' + uno)[0].reset();
            $('#div_form_' + uno).css('display', 'none')
            $('#list_createusercate_' + uno).css('display', '').parent().prev().removeClass('noline');
        });
        header.noticeSearchReTopInit();

    });

    function addOrdeleteUserType(destUno, typeid, typeName, joinCallback, unjoinCallback) {
        var chk = $('#chktype_' + destUno + '_' + typeid);
        if (chk.attr('checked') == true) {
            var resMsg = follow.addFocusUserType(destUno, typeid, typeName, joinCallback);
        } else {
            follow.removeFocusUserType(destUno, typeid, unjoinCallback);
        }
    }

    validate.validator.addMethod('verifyWord', function(value, element, params) {
        return verify.verifyPost(value);
    }, tipsText.userGroup.usertype_typename_illegl);

    validate.validator.addMethod('verifyTypeExists', function(value, element, params) {
        return verify.verifyUserCateExists(value, joyconfig.joyuseuno);
    }, tipsText.userGroup.usertype_typename_hasexitsts);

    $.validator.addMethod('verifyDescWord', function(value, element, params) {
        return verify.verifyPost(value);
    }, tipsText.joyfocus.user_demo_illegl);

    $.validator.addMethod('inputLength', function(value, element, params) {
        var result = true;
        var inputNum = common.getInputLength(value);
        inputNum = !inputNum ? 0 : inputNum;
        if (inputNum > 8) {
            result = false;
        }
        return result;
    });

    function showErrors() {
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            $("#mask_error").html(error.message);
        }
    }
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});