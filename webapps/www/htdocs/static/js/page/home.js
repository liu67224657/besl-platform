/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var pop = require('../common/jmpopup');
    var followBiz = require('../biz/follow-biz.js');
    var joymealert = require('../common/joymealert');
    require('../common/jquery.cookie')($);

    var home = {
        showAllJoinedGroup:function() {
            $("#show_more_group_list").live("click", function() {
                $("#group_list").find("li").toggleClass(function(i,val) {
                    if (i > 7) {
                        return "hide";
                    }
                });
                var className = $(this).attr('class');
                if (className == 'open') {
                    $(this).attr('class', 'close').text('关闭');
                } else {
                    $(this).attr('class', 'open').text('展开');
                }
            });
        },
        postBox:function(post_area) {
            $('#send_box_body').live("click", function() {
                $('#send_box_small').css("display", "none");
                $(post_area).css("display", "block");
                $('#chat_content').focus();
            });
            $("#mini_editor_app").live("click", function() {
                $('#send_box_small').css("display", "none");
                $(post_area).css("display", "block");
                $("#relApp").click();
            });
            $("#mini_editor_video").live("click", function() {
                $('#send_box_small').css("display", "none");
                $(post_area).css("display", "block");
                $("#relVideo").click();
            });
            $("#mini_editor_photo").live("click", function() {
                $('#send_box_small').css("display", "none");
                $(post_area).css("display", "block");
                $("#relPhoto").click();
            });

        },
        addGroup:function() {
            $("#add_group").live("click", function() {
                $("<div id='blackDom'></div>").appendTo("body").css({opacity:"0","background":"#000",width:$(window).width() + 20 + "px",height:$(document).height() + "px",position: "absolute",top:0, left:0,'z-index':"18000"}).animate({opacity:"0.5"});
                $("body").append("<div style='position: absolute;' id='previewBox'></div>");
                if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) {
                    $("body").css({"position":"absolute",width:$(window).width() - 1 + "px"});
                    $("#previewBox").attr('style', "position:absolute;z-index:18001;top:" + $(window).scrollTop() + "px;left:" + ($(window).width() - 1010) / 2 + "px;width:990px;height:10px;border:1px solid #ccc;zoom:1");
                    setTimeout(function() {
                        document.body.parentNode.style.overflowY = "hidden";
                        document.body.parentNode.style.overflowX = "hidden";
                    }, 1000);
                    $("#previewBox").animate({height:$(window).height() + "px"}, 200, function() {
                        createDom();
                        packegPreview(postPreviewFun, loadDisError);
                    })
                } else {
                    $("#previewBox").css({"position":"fixed","_position":"absolute","z-index":"18001","top":$(window).height() / 2 + "px",left:($(window).width() - 1010) / 2 + "px",width:1010 + "px",height:"10px",border:"0px solid #ccc"})
                    $("#previewBox").animate({height:($(window).height() - 40) + "px",top:"90px"}, 200, function() {
                        createDom();
                        packegPreview(postPreviewFun, loadDisError);
                    });
                }

            });
        }
//        ,
//        groupGuide:function(isClick) {
//            if (Sys.ie != "6.0") {
//                if (isClick == 'false') {
//                    window.scrollTo(0, 0);
//                    document.body.parentNode.style.overflowY = "hidden";
//                    $('body').append('<div class="fugai" id="fugai"></div>')
//                    $('#fugai').css({'height':$(document).height() + 'px', 'width':$(window).width() + 'px'});
//                    $('body').append('<div class="step06" id="step06"><a href="javascript:void(0)"></a></div>');
//                    $('#step06').css('left', $('#content').offset().left - 31 + 'px');
//                    $("#step06 a").one('click', function () {
//                        $.ajax({
//                                    url:'/json/home/insertgrouptip',
//                                    type:'post'
//                                });
//                        document.body.parentNode.style.overflowY = "scroll";
//                        $(this).parent().remove();
//                        $('#fugai').remove();
//                    })
//                }
//            }
//        }
    }

    var packegPreview = function(successCallback, errorcallback) {
        $.ajax({
                    url:'/json/home/group',
                    type:'post',
                    success:function(data) {
                        successCallback(data);
                    },
                    error:function() {
                        errorcallback(successCallback);
                    }
                });
    }

    var createDom = function() {

        var loadingText = '<div class="chooseGroupBox">' +
                '<div class="chooseGroupBox-top"></div>' +
                '<h2 class="chooseGroupBox-title">' +
                '添加小组<a href="javascript:void(0)" id="see_close" class="see_close"></a>' +
                '</h2>' +

                '<div class="chooseGroup clearfix">' +
                '<div id="sea_area" style="height:380px; border-top:1px solid #f8f8f8; width:100%; position: relative; background:#fff;">' +
                '<div class="see_loading" id="previewLoading" >' +
                '<p>正在载入...</p>' +
                '</div>' +
                '</div>' +
                '</div>' +

                '<div class="chooseGroupBox-bottom"></div>' +
                '</div>;'


        $("#previewBox").append(loadingText);
//                    $('#previewLoading').css('margin-top', $("#sea_area").height() / 2 - $("#previewLoading").height() + 'px');
        $('#previewLoading').css('margin-top', '150px');
        $('#see_close').bind('click', function() {
            $.event.trigger("ajaxStop")//停止加载ajax
            $("#previewBox").hide().remove();
            $('#blackDom').hide().remove();
        });
    }

    var loadDisError = function(successCallback) {
        $("#previewLoading").css('background', 'none').html('<p>读取失败，请<a href="javascript:void(0)" id="resubDis">重试</a></p>');
        $("#resubDis").one('click', function() {
            packegPreview(successCallback, loadDisError);
        })
    }
    var postPreviewFun = function(data) {
        var bodyTop = document.documentElement.scrollTop || window.pageYOffset;
        $("#previewBox").html(data);
        livePreview(bodyTop);
    }

    var livePreview = function(bodyTop) {
        var headHeight = 57;
        $("#see_close").unbind().bind('click', function() {
            var afterBodyTop = $("#previewBox").data('bodyTop');
            $("#previewBox div").hide();
            $("#previewBox").hide().remove();
            $("#blackDom").hide().remove();

            document.body.parentNode.style.overflowY = "scroll";
            document.body.parentNode.style.overflowX = "hidden";
            $("body").css({"position":"static"});
            $.browser.version == "7.0" ? $("body").scrollTop(afterBodyTop) : $(document).scrollTop(afterBodyTop);
            $("#previewBox").removeData('bodyTop');
//            if (bodyTop >= 0 && bodyTop < headHeight) {
//                $(".headb").css('top', headHeight - bodyTop)
//            } else {
//                $(".headb").css({'top': 0})
//            }
        });

        $("a[name=followGroup]").die().live("click", function () {
            followBiz.ajaxFollowBoard($(this), $(this).attr("data-gid"), followGroupCallback);
        });


        var followGroupCallback = function (dom, id, resultMsg) {
            if (resultMsg.status_code == '1') {
                var liObj = dom.parent();
                dom.remove();
                liObj.append('<span class="hasjoined">已加入</span>');
                var groupName = $.trim($(liObj).children("h2").children("a").html());
                var groupLink = $(liObj).children("h2").children("a").attr("href");

                var shortGroupName = groupName;
                if (groupName.length > 5) {
                    shortGroupName = groupName.substring(0, 5) + "…";
                }

                var groupHtml = '<li class=" ">•<a target="_blank" href="' + groupLink + '" title="' + groupName + '"> ' + shortGroupName + ' </a></li>';
                var noJoinArea = $('#nojoingroup');
                if (noJoinArea.length > 0) {
                    noJoinArea.replaceWith('<ul id="group_list" class="clearfix">' + groupHtml + '</ul>' +
                            '<div class="clearfix"><a href="javascript:void(0);" href="javascript:void(0);" id="add_group">+ 添加</a></div>')
                } else {
                    $("#group_list li:first").before(groupHtml);
                }

                var li = $("#group_list").find("li");
                if (li.size() > 8) {
                    li.each(function(i) {
                        $(this).removeClass();
                    });
                    $('#show_more_group_list').attr('class', 'close').text('关闭');
                }
            } else {
                var alertOption = {text:resultMsg.msg, tipLayer:true, textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        };

        if (bodyTop == undefined) {
            bodyTop = 0;
        }
        if (!($.browser.msie && ($.browser.version == "6.0") && !$.support.style)) {
            $("body").css({"position":"fixed",'top': '-' + bodyTop + 'px',width:$(window).width() + "px","background-attachment":"fixed"});
            document.body.parentNode.style.overflowY = "hidden";
//            if (bodyTop >= 0 && bodyTop < headHeight) {
//                $(".headb").css({'top': headHeight - bodyTop + 'px','width':$(".headt").width()})
//            } else {
//                setTimeout(function() {
//                    $(".headb").css({'top': 0,'width':$(".headt").width() + 20})
//                }, 0)
//            }
        }
//        $('.headt,.headb').css('width', $(window).width() + 'px');
        $("#previewBox").data('bodyTop', bodyTop);
    }


    return home;
})
