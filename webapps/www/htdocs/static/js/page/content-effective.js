define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var viewstimeBiz = require('../biz/viewtimes-biz');

    var contentPreview = {
        togglePreviewPic:function() {
            singlePic();
            mutilPic();
        },
        togglePreviewVideo:function() {
            showVideo();
        },
        toggleAllContent:function() {
            toggleAllContent();
        },
        toggleAllContentByBlog:function() {
            toggleAllContentByBlog();
        },
        togglePreviewPicOnSearch:function() {
            mutilPicOnSearch();
        },
        postTalk:function() {
            postTalk();
        }
    }

    function toggleAllContent() {
        $('.area').live('mouseenter',
                function() {
                    if ($(this).children()[0] == null) {
                        return;
                    }
                    var linkArea = $($(this).children()[0].children[1]);
                    var linkObj = linkArea.find('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = '';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = 'none';
                        }
                    }
                }).live('mouseleave', function() {
                    if ($(this).children()[0] == null) {
                        return;
                    }
                    var linkArea = $($(this).children()[0].children[1]);
                    var linkObj = linkArea.find('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = 'none';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = '';
                        }
                    }
                });

        $('.discuss_area').live('mouseenter',
                function() {
                    var linkArea = $(this);
                    var linkObj = linkArea.children('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = '';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = 'none';
                        }
                    }
                }).live('mouseleave', function() {
                    var linkArea = $(this);
                    var linkObj = linkArea.children('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = 'none';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = '';
                        }
                    }
                });
    }

    function toggleAllContentByBlog() {
        $('.area').live('mouseenter',
                function() {
                    var linkArea = $($(this).children()[0].children[0]);
                    var linkObj = $($(this).children()[0].children[0]).find('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = '';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = 'none';
                        }
                    }
                }).live('mouseleave', function() {
                    var linkArea = $($(this).children()[0].children[0]);
                    var linkObj = $($(this).children()[0].children[0]).find('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = 'none';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = '';
                        }
                    }
                });

        $('.discuss_area').live('mouseenter',
                function() {
                    var linkArea = $(this);
                    var linkObj = linkArea.children('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = '';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = 'none';
                        }
                    }
                }).live('mouseleave', function() {
                    var linkArea = $(this);
                    var linkObj = linkArea.children('.chakan')[0];
                    if (linkObj != undefined && linkObj != null) {
                        linkObj.style.display = 'none';
                        var favSpan = linkArea.find('.mylove')[0]
                        if (favSpan != undefined && favSpan != null) {
                            favSpan.style.display = '';
                        }
                    }
                });
    }

    function showVideo() {
        $('a[name=preivewvideo]').live('click', function() {
            var videoContainer = $(this).parent();
            var flashUrl = $(this).attr('data-flashurl');
            var imgSrc = videoContainer.find('img')[0].attributes['src'].value;
            videoContainer.attr('class', 'single_videofalsh clearfix');
            videoContainer.html('<a class="pack_up" href="javascript:void(0);" data-thumb="' + imgSrc + '" data-flashurl="' + flashUrl + '">收起</a>' +
                    '<div class="single_videoview"><object align="middle" width="528" height="420" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000">' +
                    '<param value="true" name="AllowFullScreen">' +
                    '<param value="sameDomain" name="allowScriptAccess">' +
                    '<param value="' + flashUrl + '" name="movie">' +
                    '<param value="Transparent" name="wmode">' +
                    '<embed width="528" height="420" ' +
                    'src="' + flashUrl + '" ' +
                    'type="application/x-shockwave-flash" wmode="Transparent" allowfullscreen="true">' +
                    '</object></div>');
        });

        $('.single_videofalsh>a:eq(0)').live('click', function() {
            var videoContainer = $(this).parent();
            videoContainer.attr('class', 'single_video');
            var flashUrl = $(this).attr('data-flashurl');
            var imgSrc = $(this).attr('data-thumb');
            videoContainer.html('<a name="preivewvideo" href="javascript:void(0);" data-flashurl="' + flashUrl + '">' +
                    '<img width="188" height="141" src="' + imgSrc + '">' +
                    '</a>' +
                    '<a name="preivewvideo" class="video_btn" title="播放" href="javascript:void(0)"  data-flashurl="' + flashUrl + '"></a>' +
                    '</div>');
        });
    }

    function singlePic() {
        $('.single_pic>ul>li>p>img').die().live('click', function() {
            var imgObj = $(this);
            var src = imgObj.attr('original');
            var loadingW = imgObj.attr('data-jw').length == 0 ? 0 : imgObj.attr('data-jw');
            var loadingH = imgObj.attr('data-jh').length == 0 ? 0 : imgObj.attr('data-jh');
            var loadingStyle = '';
            if (loadingW > 0 && loadingH > 0) {
                loadingStyle = 'width:' + loadingW + 'px;height:' + loadingH + 'px';
            }

            var mSrc = common.parseMimg(src, joyconfig.DOMAIN);
            var bSrc = common.parseBimg(src, joyconfig.DOMAIN);
            var imgUl = $(this).parent().parent().parent().parent();
            imgUl.attr('class', 'single_pic_view').html('<a class="look_over" target="_blank" href="' + bSrc + '">查看原图</a><div class="pic_loadbg" style="' + loadingStyle + '"><img class="lazy" src="' + mSrc + '" original="' + src + '" data-jw="' + loadingW + '" data-jh="' + loadingH + '"/></div>');
            //send add view times event
            viewstimeBiz.ajaxAddViewTime(imgUl.attr("data-cid"), imgUl.attr("data-cuno"));
        });

        $('.single_pic_view>div>img').die().live('click', function() {
            var imgObj = $(this);
            var src = imgObj.attr('original');
            var loadingW = imgObj.attr('data-jw').length == 0 ? 0 : imgObj.attr('data-jw');
            var loadingH = imgObj.attr('data-jh').length == 0 ? 0 : imgObj.attr('data-jh');
            var ssSrc = common.parseSSimg(src, joyconfig.DOMAIN);
            var imgContainer = $(this).parent().parent();
            imgContainer.attr('class', 'single_pic').html('');

            var isIE7 = window.navigator.userAgent.indexOf('MSIE 7.0') > 0;
            if (isIE7) {
                setTimeout(function() {
                    imgContainer.append('<ul><li><p><img  class="lazy" src="' + ssSrc + '" original="' + src + '" data-jw="' + loadingW + '" data-jh="' + loadingH + '"/></p></li></ul>')
                }, 200);
            } else {
                imgContainer.append('<ul><li><p><img  class="lazy" src="' + ssSrc + '" original="' + src + '" data-jw="' + loadingW + '" data-jh="' + loadingH + '"/></p></li></ul>');
            }

            window.scrollTo(0, imgContainer.offset().top - 100);
        });
    }

    function mutilPic() {
        $('.multipic>li>p>img').live('click', function() {
            var imgUl = $(this).parent().parent().parent();
            var imgLis = imgUl.children();
            $.each(imgLis, function(i, val) {
                var lowsrc = $(this).attr('src')
                var img = $(val.children[0].children[0]);
                var loadingW = img.attr('data-jw').length == 0 ? 0 : img.attr('data-jw');
                var loadingH = img.attr('data-jh').length == 0 ? 0 : img.attr('data-jh');
                var loadingStyle = '';
                if (loadingW > 0 && loadingH > 0) {
                    loadingStyle = 'width:' + loadingW + 'px;height:' + loadingH + 'px';
                }
                var src = img.attr('original');
                var mSrc = common.parseMimg(src, joyconfig.DOMAIN);
                var bSrc = common.parseBimg(src, joyconfig.DOMAIN);
                img.attr('src', mSrc);
                img.parent().html('<a class="look_over" target="_blank" href="' + bSrc + '">查看原图</a>' +
                        '<div class="pic_loadbg" style="' + loadingStyle + '"><img class="lazy" lowsrc="' + lowsrc + '" src="' + mSrc + '" original="' + src + '" data-jw="' + loadingW + '" data-jh="' + loadingH + '"/>' +
                        '</div>');
            });
            imgLis.css('display', '');
            imgUl.attr('class', 'multipic_view');
            //send add view times event
            viewstimeBiz.ajaxAddViewTime(imgUl.attr("data-cid"), imgUl.attr("data-cuno"));
        });

        $('.multipic_view>li>p>div>img').live('click', function() {
            var imgUl = $(this).parent().parent().parent().parent();
            var imgLis = imgUl.children();
            $.each(imgLis, function(i, val) {
                var img = $(val.children[0].children[1].children[0]);
                var loadingW = img.attr('data-jw').length == 0 ? 600 : img.attr('data-jw');
                var loadingH = img.attr('data-jh').length == 0 ? 600 : img.attr('data-jh');
                var originalSrc = img.attr('original');

                var ssSrc = common.parseSSimg(originalSrc, joyconfig.DOMAIN);
                if (i > 2) {
                    var mSrc = common.parseMimg(originalSrc, joyconfig.DOMAIN);
                    $(val).html('<p><img class="lazy" src="' + ssSrc + '" original="' + mSrc + '" data-jw="' + loadingW + '" data-jh="' + loadingH + '"/></p>').css('display', 'none');
                } else {
                    $(val).html('<p><img class="lazy" src="' + ssSrc + '" original="' + ssSrc + '" data-jw="' + loadingW + '" data-jh="' + loadingH + '"/></p>');
                }
            });
            imgUl.attr('class', 'multipic clearfix');
            window.scrollTo(0, imgUl.parent().offset().top - 130);
        });
    }

    function mutilPicOnSearch() {
        $('.search_piclist>li>a>img').live('click', function() {
            var imgUl = $(this).parent().parent().parent();
            imgUl.children().hide();
            imgUl.attr('class', 'search_single clearfix');
            var domain = imgUl.attr("data-domain");
            var cid = imgUl.attr("data-cid");
            imgUl.append('<li><img src="' + common.parseMimg($(this).attr("src"), joyconfig.DOMAIN) + '"><a href="' + joyconfig.URL_WWW + '/note/' + cid + '" target="_blank">查看原文</a></li>');
            //send add view times event
            viewstimeBiz.ajaxAddViewTime(imgUl.attr("data-cid"), imgUl.attr("data-cuno"));
        });

        $('.search_single>li>img').live('click', function() {
            var imgUl = $(this).parent().parent();
            $(this).parent().remove();
            imgUl.children().show();
            imgUl.attr('class', 'search_piclist clearfix');
            var docTop = imgUl.parent().offset().top - 50;
            window.scrollTo(0, docTop);
        });
    }

    function postTalk() {
        $("#postTalk").die().live("click", function() {
            window.scrollTo(0, $("#post_area").offset().top-300);
            //focus
            setTimeout(function() {
                CKEDITOR.instances['text_content'].focus();
            }, 600);
        });
    }

    return contentPreview;
});






