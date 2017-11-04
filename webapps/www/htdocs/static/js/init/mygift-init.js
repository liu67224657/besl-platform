define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    var common = require('../common/common');
    var joymealert = require('../common/joymealert');
    var alertOption = {tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };
    $(document).ready(function(){
        header.noticeSearchReTopInit();
        bindCopyButton();

        $('.send-to-mobile').click(function() {
            var index = $(this).attr('data-id');
            var code = $('#em_code_'+ index).text();
            var gid = $(this).attr('data-gid');
            var aid = $(this).attr('data-aid');
            var lid = $(this).attr('data-lid');
            biz.sendShortMessage(code, gid, aid, lid, callback.sendShortMessageCallback)
        });
    });
    function bindCopyButton() {
        var index = Number(0);
        $('.copy-num').each(function(){
            $(this).find('a.copycode').bind("click", {id:index}, copyCode);
            index = index + 1;
        });

    }

    function copyCode(evnt) {
        biz.clicpGetCode(evnt.data.id);
    }

    var biz = {
        clicpGetCode:function (id) {
            var _this = biz;
            var inviteLink = $('#em_code_' + id);
            if (common.clipToClipboard(inviteLink.text())) {
                var codeValue = inviteLink.text();
                joymealert.alert({text:'复制成功，请赶快去游戏中使用吧～～'});
                //inviteLink.text('复制成功，请赶快去游戏中使用吧～～');
                setTimeout(function() {
                    _this.recoverInviteLink(id, codeValue);
                }, 2500);
            } else {
                joymealert.alert({text:'您的浏览器不支持粘贴，请手动复制，辛苦啦～'});
                inviteLink[0].select();
            }
        },
        recoverInviteLink:function(id, codeValue) {
            var inputInviteLink = $('#em_code_' + id);
            inputInviteLink.text(codeValue);
        },
        sendShortMessage:function(code, gid, aid, lid, callback) {
            $.ajax({type: "POST",
                        url: "/json/gift/sendmobile",
                        data: {code: code, gid: gid, aid: aid, lid: lid},
                        success: function (req) {
                            var result = eval('(' + req + ')');
                            callback(result);
                        }
                    });
        }
    }

      var callback = {
        sendShortMessageCallback:function(result) {
            if (result.status_code == '0') {
                alertOption.text = result.msg;
                alertOption.title = '发送失败';
                joymealert.alert(alertOption);
                return false;
            }else if (result.status_code == '1'){
                alertOption.text = result.msg;
                alertOption.title = '发送成功';
                joymealert.alert(alertOption);
                return false;
            }
        }
    }
});