define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var joymealert = require('../common/joymealert');
    var pop = require('../common/jmpopup');
    var login = require('../biz/login-biz');
    var gameid = "";
    var invite = {
        recoverInviteLink:function() {
            var inputInviteLink = $('#ipt_invite_link');
            var linkid = inputInviteLink.attr("data-id");
            inputInviteLink.val(joyconfig.URL_WWW + '/invite/' + linkid);
        },

        clicpInviteLink:function () {
            var _this = invite;
            var inviteLink = $('#ipt_invite_link')
            if (common.clipToClipboard(inviteLink.val())) {
                inviteLink.val('复制成功，通过QQ，MSN或者邮件把链接发给你的朋友');
                setTimeout(function() {
                    _this.recoverInviteLink();
                }, 2500);
            } else {
                joymealert.alert({text:'您的浏览器不支持粘贴，请按Ctrl+c 将信息粘贴到您指定的位置。'});
                inviteLink[0].select();
            }
        },
        gameTalkLink:function() {
            $("#create_game_invitelink").live('click', function() {
                var id = $(this).attr('data-gameid');
                gameid = id;
                if (joyconfig.joyuserno == '') {
                    login.maskLogin({callbackfunction:function() {
                                inviteBiz.createLinkByGame(id, gameTalkAlert);
                                $("#wrapper_unlogin").hide();
                                $("#wrapper_unlogin").hide().remove();
                            }
                            });
                } else {
                    if ($("#inviteFriends").length == 0) {
                        inviteBiz.createLinkByGame(id, gameTalkAlert);
                    } else {
                        return;
                    }
                }
            })
            $("#gameGuild").live('focusin focusout', function(event) {
                if (event.type == "focusin") {
                    if ($(this).val() == "填写公会名称") {
                        $(this).val("");
                        $(this).css('color', '#666');
                    } else {
                        $(this).css('color', '#666');
                    }
                } else if (event.type == "focusout") {
                    if ($(this).val() == "") {
                        $(this).val("填写公会名称")
                        $(this).css('color', '#ccc');
                    }
                }
            });
            $("#gameRoleName").live('focus blur', function(event) {
                if (event.type == "focusin") {
                    if ($(this).val() == "填写角色昵称") {
                        $(this).val("");
                        $(this).css('color', '#666');
                    }
                } else if (event.type == "focusout") {
                    if ($(this).val() == "") {
                        $(this).val("填写角色昵称")
                        $(this).css('color', '#ccc');
                    }
                }
            });
            $("#gameGuild").live('keyup keypress', function() {
                var guild = $.trim($(this).val()) == "填写公会名称" ? "" : $.trim($(this).val());
                var rolename = $.trim($("#gameRoleName").val()) == "填写角色昵称" ? "" : $.trim($("#gameRoleName").val());
                $("#gameText").text(gameTextFun(guild, rolename, $("#gameName").text()));
            })
            $("#gameRoleName").live('keyup keypress', function() {
                var guild = $.trim($("#gameGuild").val()) == "填写公会名称" ? "" : $.trim($("#gameGuild").val());
                var rolename = $.trim($(this).val()) == "填写角色昵称" ? "" : $.trim($(this).val());
                $("#gameText").text(gameTextFun(guild, rolename, $("#gameName").text()));
            })
            $("#inviteClip").live('click', function() {
                liveInviteThird('clip', gameid);
            });
            $("#inviteWeibo").live('click', function() {
                liveInviteThird("sina", gameid);
            });
            $("#inviteRenren").live('click', function() {
                liveInviteThird("renren", gameid);
            });
            $("#inviteQq").live('click', function() {
                liveInviteThird("qq", gameid);
            });
        }
    }
    var liveInviteThird = function(apiName, gameid) {
        var gameObject = {
            gameid:gameid,
            guilds:$.trim($("#gameGuild").val()) == "填写公会名称" ? "" : $("#gameGuild").val(),
            rolename:$.trim($("#gameRoleName").val()) == "填写角色昵称" ? "" : $("#gameRoleName").val()
        }
        shareThird(apiName, $("#inviteArea p:first").text(), gameObject, $("#inviteLink").text()) //剪切板用
    }

    var inviteBiz = {
        createLinkByGame:function(gameid, callback) {
            $.post("/json/invite/create/game", {gameid:gameid}, function(req) {
                var data = eval('(' + req + ')')
                callback(data);
            })
        }
    }

    function gameTalkAlert(data) {
        var inviteData = data.result[0];
        var htmlObj = new Object();
        htmlObj['id'] = 'inviteFriends';
        htmlObj['title'] = '邀请游戏好友'
        htmlObj['html'] = '<div class="invite">';
        if (inviteData.event != undefined) {
            htmlObj['html'] += '<div class="invite_active">' + inviteData.event + '</div>';
        }
        var gameGuild = '填写公会名称';
        var gameRoleName = '填写角色昵称';
        var guildStyle = "";
        var gameroleStyle = "";
        var gameText = "";
        if (inviteData.gameRole) {
            if (inviteData.gameRole.guild == "-") {
                inviteData.gameRole.guild = "";
            }
            if (inviteData.gameRole.roleName == "(无角色信息)") {
                inviteData.gameRole.roleName = ""
            }
            gameGuild = inviteData.gameRole.guild;
            gameRoleName = inviteData.gameRole.roleName
            if ($.trim(gameGuild) != "") {
                guildStyle = "color:#666"
            } else {
                gameGuild = "填写公会名称"
            }
            if ($.trim(gameRoleName) != "") {
                gameroleStyle = "color:#666"
            } else {
                gameRoleName = "填写角色昵称"
            }
            gameText = gameTextFun(inviteData.gameRole.guild, inviteData.gameRole.roleName, inviteData.gameName);
        } else {
            gameText = inviteData.gameName;
        }

        htmlObj['html'] += '<div class="invite_con">' +
                '<p>填写你的<span id="gameName">' + inviteData.gameName + '</span>游戏信息，被邀请人将会自动关注你和你的同伴</p>' +
                '<p class="clearfix"><input type="text" id="gameGuild" style="' + guildStyle + '" class="textinput" value="' + gameGuild + '"> <input type="text" id="gameRoleName" style="' + gameroleStyle + '" class="textinput" value="' + gameRoleName + '"></p>' +
                '<p class="mt25 clearfix"><b>分享以下内容：</b></p>' +
                '<div class="invite_area" id="inviteArea"><p id="inviteText"><span id="gameText"> ' + gameText + ' </span>在着迷网(Joyme.com)安家了，点击以下链接加入着迷，快速找到同伴。<span id="inviteLink">' + joyconfig.URL_WWW + '/invite/' + inviteData.inviteid + '/' + inviteData.gid +
                '</span></p><p class="right"><a id="inviteClip" href="javascript:void(0)">复制到剪贴板</a></p>' +
                '</div>' +
                '<div class="fengxiang clearfix"><span>把邀请发布到：</span><div class="cooper_login">' +
                '<a id="inviteWeibo" class="weibo" href="javascript:void(0);">&nbsp;</a>' +
                '<a id="inviteRenren" class="renren" href="javascript:void(0);">&nbsp;</a>' +
                '<a id="inviteQq" class="qq" href="javascript:void(0);">&nbsp;</a>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>';
        var popConfig = {
            pointerFlag : true,//是否有指针
            tipLayer : false,//是否遮罩
            containTitle : true,//包含title
            containFoot : false,//包含footer
            offsetlocation:[0,0],
            className:"",
            forclosed:true,
            popwidth:555 ,
            allowmultiple:false,
            isfocus:true,
            isremovepop:true,
            hideCallback:function() {
                liveInviteThird('', gameid)
            }
        }
        pop.popupInit(popConfig, htmlObj);
        if ($("#gameGuild").val() != "" || $("#gameGuild").val() != "填写公会名称") {
            $("#gameGuildText").text($("#gameGuild").val())
        }
        if ($("#gameRoleName").val() != "" || $("#gameRoleName").val() != "填写角色昵称") {
            $("#gameRoleNameText").text($("#gameRoleName").val())
        }

    }

    function gameTextFun(guild, rolename, gameNmae) {
        var gameText = ""
        if (guild == "" && rolename == "") {
            gameText = gameNmae + ' ';
        } else if (guild != "" && rolename == "") {
            gameText = gameNmae + ' ' + guild + '公会 ';
        } else if (guild == "" && rolename != "") {
            gameText = '我是 ' + rolename + '，' + gameNmae + ' ';
        } else if (guild != "" && rolename != "") {
            gameText = '我是 ' + guild + ' 公会的' + rolename + '，' + gameNmae + ' ';
        }
        return gameText;
    }

    function shareThird(apiName, linkInfo, gameRole, inviteLink) {
        if (gameRole != null && (gameRole.guilds != "" || gameRole.rolename != "")) {
            $.ajax({
                        type: "POST",
                        url: "/json/profile/palyedgame/increase",
                        data: {gameid:gameRole.gameid,rolename:gameRole.rolename,guilds:gameRole.guilds},
                        complete: function() {
                            var shareUrl = '';
                            if (apiName == 'qq') {
                                shareUrl = 'http://share.v.t.qq.com/index.php?c=share&a=index&url=' + encodeURIComponent(joyconfig.URL_WWW) + '&appkey=68ace50c15654ff38a0c850625c4c1f7&site=' + joyconfig.URL_WWW + '&title=' + encodeURIComponent(linkInfo);
                                window.location.href = shareUrl;
                            } else if (apiName == 'sina') {
                                shareUrl = 'http://v.t.sina.com.cn/share/share.php?url=&content=utf-8&appkey=1245341962&title=' + encodeURIComponent(linkInfo);
                                window.location.href = shareUrl;
                            } else if (apiName == 'renren') {
                                shareUrl = 'http://widget.renren.com/dialog/share?resourceUrl=' + encodeURIComponent(inviteLink) + '&srcUrl=' + encodeURIComponent(inviteLink) + '&title=' + encodeURIComponent('分享邀请链接 ' + inviteLink) + '&description=' + encodeURIComponent(linkInfo) + '&charset=UTF-8';
                                window.location.href = shareUrl;
                            } else if (apiName == 'clip') {
                                if (common.clipToClipboard(linkInfo)) {
                                    joymealert.alert({text:'剪切成功!'})
                                } else {
                                    joymealert.alert({text:'您的浏览器不支持粘贴，请按Ctrl+c 将信息粘贴到您指定的位置。',callbackFunction:function() {
                                                var selection = window.getSelection();
                                                selection.selectAllChildren(document.getElementById("inviteText"));
                                            }
                                            });
                                }
                            }
                        }
                    });
        } else {
            var shareUrl = '';
            if (apiName == 'qq') {
                shareUrl = 'http://share.v.t.qq.com/index.php?c=share&a=index&url=' + encodeURIComponent(joyconfig.URL_WWW) + '&appkey=68ace50c15654ff38a0c850625c4c1f7&site=' + joyconfig.URL_WWW + '&title=' + encodeURIComponent(linkInfo);
                window.location.href = shareUrl;
            } else if (apiName == 'sina') {
                shareUrl = 'http://v.t.sina.com.cn/share/share.php?url=&content=utf-8&appkey=1245341962&title=' + encodeURIComponent(linkInfo);
                window.location.href = shareUrl;
            } else if (apiName == 'renren') {
                shareUrl = 'http://widget.renren.com/dialog/share?resourceUrl=' + encodeURIComponent(inviteLink) + '&srcUrl=' + encodeURIComponent(inviteLink) + '&title=' + encodeURIComponent('分享邀请链接 ' + inviteLink) + '&description=' + encodeURIComponent(linkInfo) + '&charset=UTF-8';
                window.location.href = shareUrl;
            } else if (apiName == 'clip') {
                if (common.clipToClipboard(linkInfo)) {
                    joymealert.alert({text:'剪切成功!'})
                } else {
                    joymealert.alert({text:'您的浏览器不支持粘贴，请按Ctrl+c 将信息粘贴到您指定的位置。',callbackFunction:function() {
                                var selection = window.getSelection();
                                selection.selectAllChildren(document.getElementById("inviteText"));
                            }
                            });
                }
            }

        }

    }

    return invite

});