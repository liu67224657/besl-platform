define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var noticeBiz = require('../biz/notice-biz');

    var intervalid;
    var notice = {
        initNotice:function() {
            //小纸条
            if (joyconfig.joyuserno != null && joyconfig.joyuserno.length > 0) {
                var _this = this;
                setTimeout(function() {
                    _this.loadMemo();
                }, 5 * 1000);
                intervalid = setInterval(this.loadMemo, 1000 * 60 * 3);
            }

            $("#stopMemo").live("click", function() {
                notice.stopMemo();
            });
            $("#clearNoticeByType").live("click", function() {
                notice.clearNoticeByType($(this).attr("type"));
            });
        },
        loadMemo:function() {
            noticeBiz.loadNotice(notice.loadMemoCallback);
        },
        loadMemoCallback:function(resultMsg) {
            var status = resultMsg.status_code;
            var notices = resultMsg.result;
            if (status == '1' && notices.length != 0 && window.location.href.indexOf("guide") == -1) {
                if ($("#memo_message").length == 0) {
                    $('#memo_f').replaceWith('<div id="memo_message" class="informationtip" style="display:none;">' +
                            '<div class="tipst"></div>' +
                            '<div class="tipsc"></div>' +
                            '<div class="tipsb"></div>' +
                            '</div>');
                    $("#memo_message").css('left', $("#header_func_link").offset().left - 50 + 'px');
                    $("#memo_message>div.tipsc").append('<a class="close" href="javascript:void(0)" title="关闭" id="stopMemo"></a>');
//                    $('#memo_message').css("top", '95px');
                } else {
                    $("#memo_message>div.tipsc p").remove();
                }

                $.each(notices, function(i, val) {
                    switch (val.noticeType.code) {
                        case 'nf':
                            $("#memo_message>div.tipsc").append('<p>' + val.count + '位新粉丝，<a href="/message/memo/readmemo?typeCode=' + val.noticeType.code + '">查看我的粉丝</a></p>');
                            break;
                        case 'nm':
                            $("#memo_message>div.tipsc").append('<p>' + val.count + '条新私信，<a href="/message/memo/readmemo?typeCode=' + val.noticeType.code + '">查看收信箱</a></p>');
                            break;
                        case 'nb':
                            $("#memo_message>div.tipsc").append('<p>' + val.count + '条新通知，<a href="/message/memo/readmemo?typeCode=' + val.noticeType.code + '">查看</a></p>');
                            break;
                        case 'nr':
                            $("#memo_message>div.tipsc").append('<p>' + val.count + '条新评论，<a href="/message/memo/readmemo?typeCode=' + val.noticeType.code + '">查看评论</a></p>');
                            break;
                        case 'na':
                            $("#memo_message>div.tipsc").append('<p>' + val.count + '条@提到我，<a href="/message/memo/readmemo?typeCode=' + val.noticeType.code + '">查看@提到我的</a></p>');
                            break;
//                        case 'bc':
//                            $("#memo_message>div.tipsc").append('<p id="bc">你的积分 <em>+' + val.count + '</em></p>');
//                            break;
                        case 'nc':
                            notice.homeContentNotice(val);
                            break;
                        default:
                            break;
                    }
                });

                if ($('#memo_message').is(':hidden')) {
                    if ($("#memo_message>div.tipsc>p").size() > 0) {
                        $('#memo_message').slideDown();
                    }
                } else {
                    $("#memo_message").slideUp();
                }
            }
        },
        stopMemo:function () {
            noticeBiz.stopMemo(notice.stopMemoCallback);
        },
        clearNoticeByType:function (typeCode) {
            noticeBiz.clearNoticeType(typeCode, notice.clearNoticeByTypeCallback)
        },
        clearNoticeByTypeCallback:function(typeCode, resultMsg) {
            if (resultMsg.status_code == '1') {
                if ($("#memo_message p").length < 2) {
                    $('#memo_message').slideUp();
                }
                $("#" + typeCode).remove();
            }
        },
        stopMemoCallback:function(resultMsg) {
            if (resultMsg.status_code == '1') {
                $("#memo_message").slideUp(function() {
                    $("#memo_message").children().remove();
                });
            }
        },
        homeContentNotice:function(val) {
            if (window.memoContent) {
                $("#memo_content").html('<a href="javascript:void(0)" id="loadmemocontent"> 有' + val.count + '篇新文章，点击查看</a>');
                if ($('#memo_content').is(':hidden')) {
                    $('#memo_content').slideDown();
                }
                $("#loadmemocontent").die().bind('click', function() {
                    noticeBiz.loadMemoContent({ count:val.count, callback:notice.loadMemoContentCallback,loadfunction:notice.loadMemoContentLoad});
                });
            }
        },
        loadMemoContentLoad:function() {
            $("#memo_content").html('<div class="tipsload"></div>');
        },
        loadMemoContentCallback:function(jsonObj, param) {
            if (jsonObj.status_code != '1') {
                $('#memo_content').html('加载失败，请<a id="memo_content_reload" href="javascript:void(0);">重试</a>');
                $('#memo_content_reload').one('click', function() {
                    noticeBiz.loadMemoContent({ count:param.count, callback:notice.loadMemoContentCallback,loadfunction:notice.loadMemoContentLoad});
                });
                return;
            }
            var contentgenerator = require('./post-contentgenerator');

            var blogContentList = jsonObj.result[0];
            var contentListHtml = '';
            $.each(blogContentList, function(i, val) {
                var cotnentObj = contentgenerator.generator(val);
                contentListHtml += cotnentObj.contentHtml;
            });

            $('#memo_content').slideUp();
            $('#memo_content').after(contentListHtml);
            if (contentListHtml.length > 0) {
                $('.area:hidden').fadeIn();
                $(".lazy").scrollLoading();
            }
        }

    }
    return notice;
});

