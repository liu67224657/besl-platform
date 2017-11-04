define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var scrollBiz = require('../biz/scrollpage-biz');
    var contentgenerator = require('./post-contentgenerator');

    var loadLock = false;
    var islastPage = false;
    var scrollObj = {
        scrollPage:function(pageNo, totalRows, scrollNo, homeLoading, homescRollCallback) {
            if (loadLock || islastPage) {
                return;
            }
            var documentHeight = parseInt($(document).height());
            var windowHeight = parseInt($(window).height());
            var scrollTop = parseInt($(window).scrollTop());

            if (documentHeight <= scrollTop + windowHeight + 1) {
                if (scrollNo < 3) {
                    scrollBiz.scrollHomePage({ pageNo:pageNo, screenNo: scrollNo,totalRows:totalRows, callback:homescRollCallback,loadfunction:homeLoading});
                } else {
                    $('#pagearea').css('display', '');
                }
            }
        },

        homeLoading: function() {
            loadLock = true;
            if ($('#contentloading').length == 0) {
                $('.area:last').after('<div id="contentloading" class="loading">正在加载，请稍候</div>');
            } else {
                $('#contentloading').replaceWith('<div id="contentloading" class="loading">正在加载，请稍候</div>');
            }
        },


        homescRollCallback : function(jsonObj, param) {
            loadLock = false;
            $("#hidden_scrollNo").val(++param.screenNo);
            if (jsonObj.status_code != '1') {
                $('#contentloading').replaceWith('<div id="contentloading" class="load_error">加载失败，请<a id="scroll_retry" href="javascript:void(0);">重试</a></div>');
                $('#scroll_retry').one('click', function() {
                    scrollBiz.scrollHomePage(param);
                });
                return;
            }

            if (jsonObj.msg == '-1') {
                $('#contentloading').remove();
                return;
            }

            var blogContentList = jsonObj.result[0];
            var contentListHtml = '';
            $.each(blogContentList, function(i, val) {
                var cotnentObj = contentgenerator.generator(val,false);
                contentListHtml += cotnentObj.contentHtml;
            });
            $('#contentloading').replaceWith(contentListHtml);
            //关于lazyload定位
            var windowTop = document.body.scrollTop + document.documentElement.scrollTop;
//            $(".lazy").scrollLoading();
            window.scrollTo(0, windowTop);
            //定位结束
//            if (contentListHtml.length > 0) {
//                $('.area:hidden').fadeIn();
//            }

            var pageObj = jsonObj.result[1];
            if (pageObj.curPage >= pageObj.maxPage) {
                islastPage = true;
                $('#pagearea').css('display', '');
            }

        }
    }
    return scrollObj;
});
