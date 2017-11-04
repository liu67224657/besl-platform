define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var customize = require('../page/customize');
    var header = require('../page/header');
    require.async('../common/tips');

    $(document).ready(function() {
        var getsrt = [];
        getsrt.gameName = "游戏名称";
        getsrt.serverAreas = "所在的大区/服务器";
        getsrt.roleNames = "角色名称";
        getsrt.guilds = "角色所在的公会/帮派";

        $("input[name=gameName]").live("focusin focusout", function(event) {
            if (event.type == 'focusin') {
                if ($(this).val() == getsrt.gameName) {
                    $(this).val('');
                    $(this).css("color", "#494949")
                }
            } else {
                if ($(this).val() == "") {
                    $(this).val(getsrt.gameName);
                    $(this).css("color", "#ccc")
                }
            }
        });
        $("#gameRoles dt input").live('focusin focusout', function(event) {
            var gameTagVal = $(this).val();
            if (event.type == 'focusin') {
                switch ($(this).index()) {
                    case 0:
                        if (gameTagVal == getsrt.serverAreas) {
                            $(this).val('')
                            $(this).css("color", "#494949")
                        }
                        break;
                    case 2:
                        if (gameTagVal == getsrt.roleNames) {
                            $(this).val('')
                            $(this).css("color", "#494949")
                        }
                        break;
                    case 4:
                        if (gameTagVal == getsrt.guilds) {
                            $(this).val('')
                            $(this).css("color", "#494949")
                        }
                        break;
                }
            } else {
                switch ($(this).index()) {
                    case 0:
                        if (gameTagVal == "") {
                            $(this).val(getsrt.serverAreas)
                            $(this).css("color", "#ccc")
                        }
                        break;
                    case 2:
                        if (gameTagVal == "") {
                            $(this).val(getsrt.roleNames)
                            $(this).css("color", "#ccc")
                        }
                        break;
                    case 4:
                        if (gameTagVal == "") {
                            $(this).val(getsrt.guilds)
                            $(this).css("color", "#ccc")
                        }
                        break;
                }
            }
        });
        $("#addGame a").live("click", function() {
            customize.addGame(getsrt);
        });
        $("#saveGame").live("click", function() {
            customize.saveGame(getsrt);
        });
        $("#cancelGame").live("click", function() {
            customize.cancelGame(getsrt);
        });
        $("a[name=updateGame]").live("click", function() {
            customize.updateGame($(this).attr("id"),getsrt);
        });
        $("#addRole").live("click", function() {
            customize.addRole(getsrt);
        });
        $("#uploadGame").live('click',function(){
            customize.uploadGame($(this).children('span').attr("id"),getsrt)
        })
        $("#removeGameTag").live("click", function() {
            customize.removeGameTag($(this));
        });
        $("#delGameTag").live("click",function(){
            customize.delGameTag($(this).attr("title"));
        })
        $("#cancelUpGame").live("click",function(){
            customize.cancelUpGame($(this).children('span').attr("id"));
        })
        header.noticeSearchReTopInit();

    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});