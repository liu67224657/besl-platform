define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var pop = require('../common/jmpopup');
    var syncObj = {
        showSyncArea:function(dom) {
            var offset = dom.offset();
            var syncDivId = "sync_info_mask";
            var afterBodyTop = $("#previewBox").data('bodyTop') || 0;
            var popConfig = {
                pointerFlag : true,//是否有指针
                tipLayer : false,//是否遮罩
                offset:"Custom",
                containTitle : true,//包含title
                containFoot : false,//包含footer
                offsetlocation:[offset.top + 19 + afterBodyTop,offset.left-10],
                forclosed:true,
                popwidth:240 ,
                allowmultiple:true
            };
            if ($("#" + syncDivId).size() > 0) {
                pop.resetOffsetById(popConfig, syncDivId);
            } else {
                var resultMsg = ''
                $.ajax({
                            url:'/json/profile/sync/getprovider' ,
                            type:'POST',
                            async:false,
                            success:function(data) {
                                resultMsg = eval('(' + data + ')');
                            }
                        });
                var syncHtml = syncObj.initBindSyncHtml(resultMsg);

                var htmlObj = new Object();
                htmlObj['id'] = syncDivId;
                htmlObj['html'] = syncHtml;
                htmlObj['title'] = '您的内容将被同步到以下社区'
                pop.popupInit(popConfig, htmlObj);
            }
        },
        initBindSyncHtml:function(resultMsg) {
            var html = '';
            html += '<div class="tbcon">';
            if (resultMsg.result == 0) {
                html += '<p>您还没有绑定任何网站，无法同步</p>';

            } else {
                html += '<ul class="clearfix">';
                $.each(resultMsg.result, function(i, val) {
                    html += '<li class="bind_' + val.code + '"></li>';
                })
                html += '</ul>';
            }
            html += '<a href="/profile/customize/bind" target="_blank">修改同步设置>></a></div>';
            return html;
        } ,
        showSyncAreaOnWall:function(dom) {
            if ($("#sync_info_mask").length > 0) {
                $("#sync_info_mask").show();
            } else {
                if (joyconfig.joyuserno != "") {
                    var resultMsg = ''
                    $.ajax({
                                url:'/json/profile/sync/getprovider' ,
                                type:'POST',
                                async:false,
                                success:function(data) {
                                    resultMsg = eval('(' + data + ')');
                                }
                            });
                    var syncHtml = syncObj.initBindSyncHtml(resultMsg);
                    var domHtml = '<div style="width: 240px; position:absolute;" id="sync_info_mask" class="pop">' +
                            '<div class="hd clearfix">' +
                            '您的内容将被同步到以下社区<a href="javascript:void(0)" id="closePop_sync_info_mask" class="close"></a>' +
                            '</div>' +
                            '<div class="bd clearfix">' + syncHtml +
                            '</div>' +
                            '</div>';
                    dom.parent().parent().append('<div style="position:relative;" id="sync_mask"></div>');
                    $("#sync_mask").append(domHtml);
                    $("#sync_info_mask").css({"left":$("#sync_info_mask").width() - 32 + "px","top":5 - $("#sync_info_mask").height() - 30 + "px"})
                    $("#closePop_sync_info_mask").die().live('click', function() {
                        $("#sync_info_mask").css('display', 'none');
                    });
                }
            }
        }
    }
    return syncObj;
});