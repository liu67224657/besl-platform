define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var joymealert = require('../common/joymealert');

    var gameEditObj = {
        addContent:function() {
            var confirmOption = {
                title:'添加文章',
                text:'',
                width:308,
                confirmid:'add_content_st1',
                html: '<div class="bk_tc">' +
                        '<div class="bk_p clearfix">' +
                        '<span>文章链接</span><input type="text" class="bk_txt" id="input_content_url" /></div>' +
                        '<div class="tipstext" id="addTips" style="display:none;"></div></div>',
                submitFunction:function() {
                    var curl = $("#input_content_url").val();
                    if (curl.length == 0) {
                        $("#addTips").text('请输入标题').show();
                        $("#one_title").focus();
                        return true;
                    }

                    var contentReg = /\/note\/[0-9a-zA-Z\-_]{22}/;
                    if (curl.match(contentReg) == null) {
                        $("#addTips").text('请输入合法的文章链接').show();
                        $("#one_title").focus();
                        return true;
                    }

                    biz.previewContent(curl, moduleId, callback.previewContent);
                    return true;
                }
            }
            joymealert.prompt(confirmOption);
        },
        delContent:function(delDom) {
            var contentid = delDom.attr('data-cid');
            var confirmOption = {
                title:'删除文章',
                text:'',
                width:308,
                confirmid:'add_content_st1',
                html:'<form action="' + joyconfig.URL_WWW + '/game/' + gameCode + '/edit/delcontent" method="post" id="delcontentform">' +
                        '<input type="hidden" name="cid" value="' + contentid + '" />' +
                        '<input type="hidden" name="mid" value="' + moduleId + '" />' +
                        '<input type="hidden" name="p" value="' + curPage + '" />' +
                        '<div class="bk_tc">' +
                        '<div class="delete_alert">确定将这篇文章从此栏目中删除吗？</div>' +
                        '<div style="display:none;" id="addTips" class="tipstext"></div>' +
                        '</div>' +
                        '</form>',
                submitFunction:function() {
                    $('#delcontentform').submit();
                }
            }
            joymealert.prompt(confirmOption);
        },
        editContent:function(editDom) {
            var contentid = editDom.attr('data-cid');
            var subject = editDom.attr('data-title');
            var domain = editDom.attr('data-domain');
            var confirmOption = {
                title:'编辑文章',
                text:'',
                width:308,
                confirmid:'add_content_st1',
                html:'<form action="' + joyconfig.URL_WWW + '/game/' + gameCode + '/edit/editcontent" method="post" id="editcontentform">' +
                        '<input type="hidden" name="cid" value="' + contentid + '" />' +
                        '<input type="hidden" name="mid" value="' + moduleId + '" />' +
                        '<input type="hidden" name="p" value="' + curPage + '" />' +
                        '<div class="bk_tc">' +
                        '<div class="bk_p clearfix">' +
                        '<span>文章链接</span><input type="text" class="bk_txt" value="' + joyconfig.URL_WWW + '/note/' + contentid + '" readonly /></div>' +
                         '<div class="bk_p clearfix">' +
                        '<span>文章标题</span><input type="text" name="subject" class="bk_txt" id="input_content_subject" value="' + subject + '"/></div>' +
                        '<div class="tipstext" id="addTips" style="display:none;""></div></div></form>',
                submitFunction:function() {
                    $('#editcontentform').submit();
                }
            }
            joymealert.prompt(confirmOption);
        },

        addMenu:function() {
            var confirmOption = {
                title:'添加导航',
                text:'',
                width:308,
                confirmid:'add_menu',
                html: '<div class="bk_tc">' +
                        '<div style="margin-bottom:10px;" class="bk_p clearfix">' +
                        '<span style="width:66px; text-align:right">导航名称：</span>' +
                        '<input id="inputname" name="mname" type="text" style="width:190px; margin-right:10px;" class="bk_txt">' +
                        '</div>' +
                        '<div style="margin-bottom:6px; padding-left:6px;" class="bk_p clearfix">' +
                        '<label><input type="radio" name="mtype" value="" style="float:none; vertical-align:middle" checked="checked"> 链接地址</label>' +
                        '<label style="margin-left:20px"><input type="radio" name="mtype" value="ap"  style="float:none; vertical-align:middle"> 栏目</label>' +
                        '</div>' +
                        '<div style="margin-bottom:6px; padding-left:6px;" class="bk_p clearfix">' +
                        '<span style="color:#9f9f9f">提示：添加链接地址XXXXXXXX</span>' +
                        '</div>' +
                        '<div style="margin-bottom:6px;" class="bk_p clearfix">' +
                        '<span style="width:66px; text-align:right">链接地址：</span>' +
                        '<input type="text" style="width:190px; margin-right:10px;" class="bk_txt">' +
                        '</div>' +
//                        '<div style="margin-bottom:6px;" style="display:none" class="bk_p clearfix">' +
//                        '<span style="width:66px; text-align:right">栏目选择：</span>' +
//                        '<select style="width:202px; padding:4px 2px;">' +
//                        '<option value="">游戏介绍</option>' +
//                        '<option value="">保卫萝卜</option>' +
//                        '<option value="">攻略百科</option>' +
//                        '</select>' +
//                        '</div>' +
                        '</div>',
                submitFunction:function() {
                    var mname = $("#inputname").val();
                    if (mname.length == 0) {
                        $("#addTips").text('请输入标题').show();
                        $("#one_title").focus();
                        return true;
                    }

                    var contentReg = /[^\u4e00-\u9fa5A-Za-z0-9]+/;
                    if (mname.test(contentReg) == null) {
                        $("#addTips").text('请输入合法的文章链接').show();
                        $("#one_title").focus();
                        return true;
                    }
                    return true;
                }
            }
            joymealert.prompt(confirmOption);
        }
    }

    var biz = {
        previewContent:function(curl, mid, callback) {
            $.post('/json/game/content/preview', {curl:curl,mid:mid}, function(req) {
                var result = eval('(' + req + ')');
                if (result.status_code == '0') {
                    $("#addTips").text(result.msg).show();
                    return;
                }
                callback(result);
            })
        }
    }

    var callback = {
        previewContent:function(result) {
            $('#cancel_submit_buutton').click();

            var content = result.result[0];
            var confirmOption = {
                title:'添加文章',
                text:'',
                width:308,
                confirmid:'add_content_st1',
                html:'<form action="' + joyconfig.URL_WWW + '/game/' + gameCode + '/edit/addcontent" method="post" id="addcontentform">' +
                        '<input type="hidden" name="cid" value="' + content.contentId + '" />' +
                        '<input type="hidden" name="mid" value="' + moduleId + '" />' +
                        '<div class="bk_tc">' +
                        '<div class="bk_p clearfix">' +
                        '<span>文章链接</span><input type="text" class="bk_txt" value="' + joyconfig.URL_WWW + '/note/' + content.contentId + '" readonly /></div>' +
                        '<div class="bk_p clearfix">' +
                        '<span>文章标题</span><input type="text" name="subject" class="bk_txt" id="input_content_subject" value="' + (content.subject==null?'':content.subject) + '"/></div>' +
                        '<div class="tipstext" id="addTips" style="display:none;""></div></div></form>',
                submitFunction:function() {
                    $('#addcontentform').submit();
                }
            }
            joymealert.prompt(confirmOption);

        }
    }

    return gameEditObj;
});