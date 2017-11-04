define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var pop = require('../common/jmpopup');
    require('../biz/tag-biz');
    var verify = require('../biz/ajaxverify');
    var followBiz = require('../biz/follow-biz.js');
    var joymealert = require('../common/joymealert');
    var loginBiz = require('../biz/login-biz');

    var follow = {
        followfanslink: function () {
            $("a[name=followfans]").live("click", function () {
                var url = joyconfig.URL_WWW + $(this).attr("data-link");
                if (joyconfig.joyuserno == '') {
                    var loginOption = {
                        referer: url
                    };
                    loginBiz.maskLogin(loginOption);
                } else {
                    window.location.href = url;
                }
            });
        },
        ajaxFollowBind: function (callback) {
            $("a[name=follow]").live("click", function () {
                if (loginBiz.checkLogin($(this))) {
                    followBiz.ajaxFocus(joyconfig.joyuserno, $(this).attr('data-uno'), callback);
                }
            });
        },
        ajaxFollowSimpleBind: function (callback) {
            $("a[name=follow-simple]").live("click", function () {
                if (loginBiz.checkLogin($(this))) {
                    followBiz.ajaxFocus(joyconfig.joyuserno, $(this).attr('data-uno'), callback);
                }
            });
        },
        ajaxUnFollowBind: function (callback) {
            $("a[name=unfollow]").live("click", function () {
                if (loginBiz.checkLogin($(this))) {
                    followBiz.ajaxUnFocus($(this).attr('data-uno'), callback);
                }
            });
        },

        sildeUserCateMenu: function (uno) {
            $('.fenzu-select[id!=usercate_select_' + uno + ']').slideUp("fast");
            $("#usercate_select_" + uno).slideToggle("fast");
        },

        addType: function () {
            if (!verify.verifyUserCateCount(typeLength)) {
                alert(tipsText.userGroup.usertype_typename_count);
                return false;
            }
            var config = {
                pointerFlag: false, //是否有指针
                tipLayer: true, //是否遮罩
                containTitle: true, //包含title
                containFoot: true, //包含footer
                className: "",
                forclosed: true,
                popwidth: 299,
                allowmultiple: false
            };
            var htmlObj = new Object();
            htmlObj['id'] = "mask_modifyucate";
            htmlObj['html'] = '<div class="publicuse"><form id="form_addtype" action="/profile/usertype/add" method="post" target="_parent">' +
                    '<label>添加新分组</label>' +
                    '<input id="cateName" class="groupaddinput_l" type="text" name="cateName" type="text" value=""/>' +
                    '<input id="abc" name="abc" type="hidden" class="text" value=""/>' +
                    '<div class="groupaddwrong_l" id="mask_error">&nbsp;</div>' +
                    '</form></div>';
            htmlObj['title'] = '创建分组';
            htmlObj['input'] = '<a class="submitbtn" id="submit_addtype"><span>确 定</span></a><a class="graybtn" id="cancle_addtype"><span>取 消</span></a>';
            pop.popupInit(config, htmlObj);
            $('#cancle_addtype').live('click', function () {
                pop.hidepop(htmlObj, config);
            });
        },
        modifyCate: function (cateId, cateName) {
            var config = {
                pointerFlag: false, //是否有指针
                tipLayer: true, //是否遮罩
                containTitle: true, //包含title
                containFoot: true, //包含footer
                className: "",
                forclosed: true,
                popwidth: 299,
                allowmultiple: false
            };
            var htmlObj = new Object();
            htmlObj['id'] = "mask_modifycate";
            htmlObj['html'] = '<div class="publicuse"><form id="form_modifytype" action="/profile/usertype/updatename" method="post" target="_parent">' +
                    '<label>修改分组名称</label>' +
                    '<input id="cateName" name="cateName" type="text" class="groupaddinput_l" value="' + cateName + '"/>' +
                    '<input name="cateId" type="hidden" value="' + cateId + '"/><div class="groupaddwrong_l" id="mask_error"></div>' +
                    '</form></div>';
            htmlObj['title'] = '修改分组名称';
            htmlObj['input'] = '<a id="submit_modifytype" class="submitbtn" href="javascript:void(0);"><span>确 定</span></a>' +
                    '<a id="cancle_modifytype" class="graybtn" href="javascript:void(0);"><span>取 消</span></a>';
            pop.popupInit(config, htmlObj);
            $('#cancle_modifytype').live('click', function () {
                pop.hidepop(htmlObj, config);
            });
        },
        delUserCategroy: function (cateId) {
            var confirmOption = {
                text: '确定要删除该分组吗？',
                width: 229,
                submitButtonText: '确 定',
                submitFunction: function () {
                    window.location.href = '/profile/usertype/delete?cateId=' + cateId;
                },
                cancelButtonText: '取 消',
                cancelFunction: null};
            joymealert.confirm(confirmOption);
        },

        setDesc: function (destUno, desc, pageNo, totalRows) {
            var config = {
                pointerFlag: false, //是否有指针
                tipLayer: true, //是否遮罩
                containTitle: true, //包含title
                containFoot: true, //包含footer
                className: "",
                forclosed: true,
                popwidth: 299,
                allowmultiple: false
            };
            var htmlObj = new Object();
            htmlObj['id'] = "mask_setdesc";
            htmlObj['html'] = '<div class="publicuse"><form id="form_setdesc" action="/social/follow/updatedesc" method="post" target="_parent">' +
                    '<label>设置备注姓名</label>' +
                    '<input name="destUno" type="hidden" value="' + destUno + '"/>' +
                    '<input name="p" type="hidden" value="' + pageNo + '"/>' +
                    '<input name="totalRows" type="hidden" value="' + totalRows + '"/>' +
                    '<input class="groupaddinput_s" type="text" id="desc" name="desc" value="' + desc + '"/>' +
                    '<div id="mask_error" class="groupaddwrong_s">&nbsp;</div></form></div>';
            htmlObj['title'] = '设置备注姓名';
            htmlObj['input'] = '<a id="submit_setdesc" class="submitbtn" href="javascript:void(0);"><span>确 定</span></a>' +
                    '<a id="cancle_setdesc" class="graybtn" href="javascript:void(0);"><span>取 消</span></a>';
            var htmlStr = pop.popupInit(config, htmlObj);

            $('#cancle_setdesc').die().live('click', function () {
                pop.hidepop(htmlObj, config);
            });
        },
        submitListUserCate: function (uno) {
            if (!verify.verifyUserCateCount(typeLength)) {
                $('#error_type_' + uno).html(tipsText.userGroup.usertype_typename_count);
                return false;
            }
            var name = $('#cateame_' + uno).val();

            if (name == null || name.length == 0) {
                $('#error_type_' + uno).html(tipsText.userGroup.usertype_typename_notnull);
                return false;
            } else if (!verify.verifyPost(name)) {
                $('#error_type_' + uno).html(tipsText.userGroup.usertype_typename_illegl);
                return false;
            } else if (!verify.verifyUserCateExists(name, joyconfig.joyuserno)) {
                $('#error_type_' + uno).html(tipsText.userGroup.usertype_typename_hasexitsts_short);
                return false;
            } else if (!verify.verifyTypeLength(name)) {
                $('#error_type_' + uno).html(tipsText.userGroup.usertype_typename_maxlength);
                return false;
            }

            $('#form_createtype_' + uno).submit();
        },

        //添加被关注人
        addFocusUserType: function (destUno, cateId, typeName, callback) {
            var cateIdArray = new Array();
            $('input[type=checkbox][id^=chktype_' + destUno + ']:checked').each(function (i, val) {
                cateIdArray.push($(val).val());
            });
            var cateIds = cateIdArray.join(',');
            followBiz.ajaxJoinCate(destUno, cateIds, typeName, callback)

        },

        removeFocusUserType: function (destUno, cateId, callback) {
            followBiz.ajaxUnjoinCate(destUno, cateId, callback)
        },

        unfollow: function (jqSourceDom, nickName, focusuno, callback, tips) {
            if (tips == undefined || tips == null || tips.length == 0) {
                var tips = '确定移除' + nickName + '吗？';
            }
            initUnfollowMask(jqSourceDom, nickName, focusuno, followBiz.ajaxUnFocus, callback, tips);
        },

        removeFans: function (jqSourceDom, nickName, fansuno, callback, tips) {

            if (tips == undefined || tips == null || tips.length == 0) {
                var tips = '确定移除' + nickName + '吗？';
            }
            initUnfollowMask(jqSourceDom, nickName, fansuno, followBiz.ajaxUnfans, callback, tips);
        },


        indexGroupBind: function () {
            $("a[name=followGroup]").live("click", function () {
                followGroup.follow($(this), $(this).attr("data-gid"), callback.followGroupCallback);
            });
        },

        followBoardBind: function () {
            $("a[name=followGroup]").live("click", function () {
                if (joyconfig.joyuserno == '') {
                    loginBiz.maskLogin({referer: window.location.href});
                    return false;
                }

                followGroup.follow($(this), $(this).attr("data-gid"), callback.followBoardCallback);
            });
        },
        unFollowBoardBind: function () {
            $("a[name=unFollowGroup]").live("click", function () {
                if (joyconfig.joyuserno == '') {
                    loginBiz.maskLogin({referer: window.location.href});
                    return false;
                }
                followGroup.unfollow($(this), $(this).attr("data-gid"), callback.unFollowBoardCallback);
            });
        }
    }

    var followGroup = {
        follow: function (dom, gid, callback) {
            if (loginBiz.checkLogin(dom)) {
//                var confirmOption = {
//                    title:'加入小组',
//                    text: ' 小组管理员需要验证你的身份,请输入你的请求信息。<br/> <textarea name="reason"  id="reason" style="border-color:#494949;" rows="10" cols="50"></textarea> ',
//                    width: 400,
//                    submitButtonText: '确 定',
//                    submitFunction: reason,
//                    cancelButtonText: '取 消',
//                    cancelFunction: null};
//
//                joymealert.confirm(confirmOption);
                followBiz.ajaxFollowBoard(dom, gid, "", callback);
            }
//            function reason() {
//                var reason = $("#reason").val();
//
//            }

        },
        unfollow: function (dom, gid, callback) {
            if (loginBiz.checkLogin(dom)) {
                followBiz.ajaxUnFollowBoard(dom, gid, callback);
            }
        }
    };

    var callback = {
        followGroupCallback: function (dom, id, resultMsg) {
            if (resultMsg.status_code == '1') {
                var msg = resultMsg.msg != null && resultMsg.msg.length > 0 ? resultMsg.msg : '您的申请已经提交，请耐心等待吧';
                var alertOption = {text: msg, tipLayer: true, textClass: "tipstext",width:320};
                joymealert.alert(alertOption);
            } else {
                var alertOption = {text: resultMsg.msg, tipLayer: true, textClass: "tipstext",width:320};
                joymealert.alert(alertOption);
            }
        },

        followBoardCallback: function (dom, id, resultMsg) {
            if (resultMsg.status_code == '1') {
                var msg = resultMsg.msg != null && resultMsg.msg.length > 0 ? resultMsg.msg : '您的申请已经提交，请耐心等待吧';
                var alertOption = {text: msg, tipLayer: true, textClass: "tipstext",width:320};
                joymealert.alert(alertOption);
            } else if (resultMsg.status_code == '-1') {
                loginBiz.maskLogin({referer: window.location.href});
            } else {
                var alertOption = {text: resultMsg.msg, tipLayer: true, textClass: "tipstext",width:320};
                joymealert.alert(alertOption);
            }
        },

        unFollowBoardCallback: function (dom, id, resultMsg) {
            if (resultMsg.status_code == '1') {
                dom.parent().replaceWith('<a href="javascript:void(0);" onclick="return false;" name="followGroup" class="joingroup" data-gid="' + id + '">加入小组</a>');
            } else if (resultMsg.status_code == '-1') {
                loginBiz.maskLogin({referer: window.location.href});
            } else {
                var alertOption = {text: resultMsg.msg, tipLayer: true, textClass: "tipstext"};
                joymealert.alert(alertOption);
            }
        }
    };
    return follow;


    function initUnfollowMask(jqSourceDom, nickName, uno, bizFunction, callback, tips) {
        var offSet = jqSourceDom.offset();

        var submitFunction = function () {
            bizFunction(uno, callback);
        }
        var confirmOption = {
            offset: "Custom",
            offsetlocation: [offSet.top - 129, offSet.left],
            text: tips,
            width: 229,
            submitButtonText: '确 定',
            submitFunction: submitFunction,
            cancelButtonText: '取 消',
            cancelFunction: null};
        joymealert.confirm(confirmOption);
    }
});






