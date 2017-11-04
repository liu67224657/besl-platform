define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var joymealert = require('../common/joymealert');
//    var loginBiz = require('../biz/login-biz');
    var contentReg = /[^\u4e00-\u9fa5A-Za-z0-9\-_]+/;
    $().ready(function () {
        header.noticeSearchReTopInit();

        $('#add_menu').click(function() {
            if ($('[id^=tr_menu]').length >= 6) {
                joymealert.alert({text:'最多只能添加6个导航！'});
                return;
            }
            biz.createMenu(gameCode, callback.addMenu);
        });

        $('input[name=mtype]').live('click', function() {
            var menuType = $(this).val();
            $('[id^=div_fill_]').css('display', 'none');
            $('#div_fill_' + menuType).css('display', '');
        });

        $('a[name=link_delmenu]').click(function() {
            var menuid = $(this).attr('data-mid');
            biz.delMenu($(this), menuid);
        });

        $('a[name=link_modifymenu]').click(function() {
            var menuid = $(this).attr('data-mid');
            biz.modifyMenu(gameCode, menuid, callback.modifyMenu);
        });

        $('a[name=link]').click(function() {
            var menuid = $(this).attr('data-mid');
            biz.modifyMenu(gameCode, menuid, callback.modifyMenu);
        });

    });

    var biz = {
        createMenu:function(gamecode, callback) {
            $.post('/json/game/addmenu', {gc:gamecode}, function(req) {
                var result = eval('(' + req + ')');
                if (result.status_code == '0') {
                    joymealert.alert({text:result.msg});
                    return;
                }
                callback(result);
            })
        },
        delMenu:function(delDom, menuid) {
            var confirmOption = {
                title:'删除导航',
                text:'',
                width:308,
                confirmid:'add_content_st1',
                html:'<form action="' + joyconfig.URL_WWW + '/game/' + gameCode + '/edit/delmenu" method="post" id="delcontentform">' +
                        '<input type="hidden" name="mid" value="' + menuid + '" />' +
                        '<div class="bk_tc">' +
                        '<div class="delete_alert">确定删除该导航吗？</div>' +
                        '<div style="display:none;" id="addTips" class="tipstext"></div>' +
                        '</div>' +
                        '</form>',
                submitFunction:function() {
                    $('#delcontentform').submit();
                }
            }
            joymealert.prompt(confirmOption);

        },
        modifyMenu:function(gamecode, menuid, callback) {
            $.post('/json/game/modifymenu', {gc:gamecode,mid:menuid}, function(req) {
                var result = eval('(' + req + ')');
                if (result.status_code == '0') {
                    joymealert.alert({text:result.msg});
                    return;
                }
                callback(result);
            })
        }
    }

    var callback = {
        addMenu:function(result) {
            var modSelectHtml = '';
            var radioHtml = '';
            if (result.result != null && result.result.length > 0) {
                var optionStr = '';
                for (var i = 0; i < result.result.length; i++) {
                    var modObj = result.result[i];
                    optionStr += '<option value="' + modObj.archPoint + '">' + modObj.archPointName + '</option>';
                }
                modSelectHtml = '<div id="div_fill_ap" style="margin-bottom:6px;display:none" class="bk_p clearfix">' +
                        '<span style="width:66px; text-align:right">栏目选择：</span>' +
                        '<select name="ap" style="width:202px; padding:4px 2px;">' +
                        optionStr +
                        '</select>' +
                        '</div>';
                radioHtml = '<label style="margin-left:20px"><input id="radio_ap" type="radio" name="mtype" value="ap" style="float:none; vertical-align:middle"> 栏目</label>';
            }

            var confirmOption = {
                title:'添加导航',
                text:'',
                width:312,
                confirmid:'add_menu',
                html: '<form action="' + joyconfig.URL_WWW + '/game/' + gameCode + '/edit/addmenu" method="post" id="form_addmenu">' +
                        '<div class="bk_tc">' +
                        '<div style="margin-bottom:10px;" class="bk_p clearfix">' +
                        '<span style="width:66px; text-align:right">导航名称：</span>' +
                        '<input id="inputname" name="mname" type="text" style="width:190px; margin-right:10px;" class="bk_txt">' +
                        '<div class="tipstext" id="mname_error" style="display:none;padding-left: 76px; clear: both;"></div></div>' +
                        '<div style="margin-bottom:6px; padding-left:6px;" class="bk_p clearfix">' +
                        '<label><input id="radio_link" type="radio" name="mtype" value="link" style="float:none; vertical-align:middle" checked="checked"> 链接地址</label>' +
                        radioHtml +
                        '</div>' +
                        '<div style="margin-bottom:6px; padding-left:6px;" class="bk_p clearfix">' +
                        '<span style="color:#9f9f9f">提示：添加链接地址XXXXXXXX</span>' +
                        '</div>' +
                        '<div  id="div_fill_link" style="margin-bottom:6px;" class="bk_p clearfix">' +
                        '<span style="width:66px; text-align:right">链接地址：</span>' +
                        '<input type="text" name="link" id="input_menulink" style="width:190px; margin-right:10px;" class="bk_txt">' +
                        '<div class="tipstext" id="mlink_error" style="display:none;padding-left: 76px; clear: both;"></div></div>' +
                        modSelectHtml +
                        '</div>' +
                        '</form>',
                submitFunction:function() {
                    var mname = $("#inputname").val();
                    if (mname.length == 0) {
                        $("#mname_error").text('请输入导航名称').show();
                        return true;
                    }
                    if (mname.length > 4) {
                        $("#mname_error").text('导航菜单值允许使用汉字、数字、“-”、“_”最长不超过4个汉字').show();
                        return true;
                    }
                    if (contentReg.test(mname)) {
                        $("#mname_error").text('请输入合法的导航名称').show();
                        return true;
                    }

                    if (($('input[name=mtype]:checked').length == 0 || $('input[name=mtype]:checked').val() == 'link') && $('#input_menulink').val().length == 0) {
                        $("#mlink_error").text('请输入合法链接').show();
                        $("#one_title").focus();
                        return true;
                    }

                    $('#form_addmenu').submit();
                }
            }
            joymealert.prompt(confirmOption);
        },
        modifyMenu:function(result) {
            var menuObj = result.result[0];
            var moduleObj = result.result[1];

            var isAnchorPoint = menuObj.displayInfo.extraField1 != null && menuObj.displayInfo.extraField1 == 'ap';

            var modSelectHtml = '';
            var modRadioHtml = '';
            if (moduleObj != null && moduleObj.length > 0) {
                var optionStr = '';
                for (var i = 0; i < moduleObj.length; i++) {
                    var modObj = moduleObj[i];
                    optionStr += '<option value="' + modObj.archPoint + '" ' + (modObj.archPoint == menuObj.displayInfo.extraField2 ? 'selected' : '') + '>' + modObj.archPointName + '</option>';
                }
                modSelectHtml = '<div id="div_fill_ap" style="margin-bottom:6px;' + (isAnchorPoint ? '' : 'display:none') + '" class="bk_p clearfix">' +
                        '<span style="width:66px; text-align:right">栏目选择：</span>' +
                        '<select name="mid" style="width:202px; padding:4px 2px;">' +
                        optionStr +
                        '</select>' +
                        '</div>';
                modRadioHtml = '<label style="margin-left:20px"><input id="radio_ap" type="radio" name="mtype" value="ap" ' + (isAnchorPoint ? 'checked' : '') + ' style="float:none; vertical-align:middle"> 栏目</label>';
            }

            var radioHtml = '';
            radioHtml += '<label><input id="radio_link" type="radio" name="mtype" value="link" style="float:none; vertical-align:middle" ' + (isAnchorPoint ? '' : 'checked') + '>链接地址</label>';
            radioHtml += modRadioHtml;
            var linkHtml = '<div  id="div_fill_link" style="margin-bottom:6px;' + (isAnchorPoint ? 'display:none' : '') + '" class="bk_p clearfix">' +
                    '<span style="width:66px; text-align:right">链接地址：</span>' +
                    '<input type="text" name="link" value="' + (menuObj.displayInfo.linkUrl != null ? menuObj.displayInfo.linkUrl : '') + '" id="input_menulink" style="width:190px; margin-right:10px;" class="bk_txt">' +
                    '<div class="tipstext" id="mlink_error" style="display:none;padding-left: 76px; clear: both;"></div></div>';
            linkHtml += modSelectHtml;

            var confirmOption = {
                title:'编辑导航',
                text:'',
                width:312,
                confirmid:'modify_menu',
                html: '<form action="' + joyconfig.URL_WWW + '/game/' + gameCode + '/edit/modifymenu" method="post" id="form_modifymenu">' +
                        '<input type="hidden"  name="menuid" value="' + menuObj.itemId + '">' +
                        '<div class="bk_tc">' +
                        '<div style="margin-bottom:10px;" class="bk_p clearfix">' +
                        '<span style="width:66px; text-align:right">导航名称：</span>' +
                        '<input id="inputname" name="mname" value="' + menuObj.displayInfo.subject + '" type="text" style="width:190px; margin-right:10px;" class="bk_txt">' +
                        '<div class="tipstext" id="mname_error" style="display:none;padding-left: 76px; clear: both;"></div></div>' +
                        '<div style="margin-bottom:6px; padding-left:6px;" class="bk_p clearfix">' +
                        radioHtml +
                        '</div>' +
                        '<div style="margin-bottom:6px; padding-left:6px;" class="bk_p clearfix">' +
                        '<span style="color:#9f9f9f">提示：添加链接地址XXXXXXXX</span>' +
                        '</div>' +
                        linkHtml +
                        '</div>' +
                        '</form>',
                submitFunction:function() {
                    var mname = $("#inputname").val();
                    if (mname.length == 0) {
                        $("#mname_error").html('请输入导航名称').show();
                        return true;
                    }
                    if (mname.length > 4) {
                        $("#mname_error").html('导航菜单值允许使用汉字、数字、“-”、“_”最长不超过4个汉字').show();
                        return true;
                    }
                    if (contentReg.test(mname)) {
                        $("#mname_error").text('请输入合法的导航名称').show();
                        return true;
                    }

                    if (($('input[name=mtype]:checked').length == 0 || $('input[name=mtype]:checked').val() == 'link') && $('#input_menulink').val().length == 0) {
                        $("#mlink_error").text('请输入合法链接').show();
                        return true;
                    }

                    $('#form_modifymenu').submit();
                }
            }
            joymealert.prompt(confirmOption);
        }
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});