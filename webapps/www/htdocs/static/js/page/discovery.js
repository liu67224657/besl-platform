define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var discoveryBiz = require('../biz/discovery-biz');
    var discoveryCallback = require('./discoverycallback');
    var joymealert = require('../common/joymealert');
    var rollId;
    var common = require('../common/common');
    var tag = require('./tag');
    require('../common/jquery.validate.min');
    require('../common/jquery.form');
    require('../common/tips');
    var discovery = {
        roll:function() {
            rollId = setInterval(function() {
                discoveryBiz.loadDiscovery($('#discovery_num').val(), discoveryCallback.rollCallback);
            }, 10000);
            return rollId;
        },

        stopRoll:function() {
            clearInterval(rollId);
        },

        bindStopRoll:function() {
            var _this = this;
            $('.linebox>a').live('mouseenter',
                    function(event) {
                        clearInterval(rollId);
                    }).live('mouseleave', function() {
                        rollId = _this.roll();
                    });
        },
        initLayout:function(index) {
            loadKey = false;
            discoveryBiz.loadLayout(index, discovery.initLayoutStr);
        },
        initDisplay:function(index, magazineCode) {
            loadKey = false;
            discoveryBiz.loadDisplayLayout(index, magazineCode, discovery.initLayoutStr);
        },
        initImageDisplay:function(index, magazineCode) {
            loadKey = false;
            discoveryBiz.loadDisplayLayout(index, magazineCode, discovery.initImageLayoutStr);
        },
        initVideoDisplay:function(index, magazineCode) {
            loadKey = false;
            discoveryBiz.loadDisplayLayout(index, magazineCode, discovery.initVideoLayoutStr);
        },
        initLayoutStr:function(jsonData) {
            if (jsonData != null || jsonData.status_code == '1') {
                var blockStr = '';
                var idStr = '';
                $.each(jsonData.result, function(i, val) {
                    blockStr = '';
                    seeIndex = val.idx;
                    if (loadDebug) {
                        idStr = idStr + val.idx + ",";
                    }


                    var layoutClass = 'scan_bs1';
                    if (val.layoutType.code == 'layout1') {
                        layoutClass = 'scan_bs1';
                    }
                    if (val.layoutType.code == 'layout2') {
                        layoutClass = 'scan_bs2';
                    }
                    var blockHtmlList = val.wallBlockDTOList;
                    $.each(blockHtmlList, function(b, blockDto) {
                        blockStr = blockStr + blockDto.html;
                    });

                    var layoutStr = '<div class="scan_con" id="block_' + offSetIndex + '">' +
                            '<div class="scant"></div>' +
                            '<div class="scanc clearfix">' +
                            '<div class="' + layoutClass + '">' +
                            blockStr +
                            '</div>' +
                            '</div>' +
                            '<div class="scanb"></div>' +
                            '</div>' +
                            '<div class="bkcon">' +
                            '<div class="stit"></div>' +
                            '</div>';
                    $("#loading").before(layoutStr);
                    if ($("div[id^=block_]").length >= 100) {
                        $("#loading").prev().remove();
                        $("#loading").remove();

                    }
                    if (offSetIndex == 0) {
                        window.offSetTop[window.offSetIndex] = $("#block_" + offSetIndex).offset().top;
                    } else {
                        window.offSetTop[window.offSetIndex] = $("#block_" + offSetIndex).offset().top - 50;
                    }
                    window.offSetIndex++;
                });
            }
            if (jsonData.result.length == 0) {
                $("#loading").prev().remove();
                $("#loading").remove();
            } else {
                loadKey = true;
            }

            if (loadDebug) {
                $("#memo_message").show().append("<p>" + idStr + "</p>");
            }

        },
        initImageLayoutStr:function(jsonData) {
            var idStr = '';
            if (jsonData != null || jsonData.status_code == '1') {
                $.each(jsonData.result, function(i, val) {

                    seeIndex = val.idx;
                    if (loadDebug) {
                        idStr = idStr + val.idx + ",";
                    }

                    var layoutStr = '<div class="picsee_con" id="block_' + offSetIndex + '">' +
                            val.layoutHtml +
                            '</div>' +
                            '<div class="picsee_tit"></div>';
                    $("#loading").before(layoutStr);
                    if ($("div[id^=block_]").length >= 100) {
                        $("#loading").prev().remove();
                        $("#loading").remove();
                    }
                    if (offSetIndex == 0) {
                        window.offSetTop[window.offSetIndex] = $("#block_" + offSetIndex).offset().top;
                    } else {
                        window.offSetTop[window.offSetIndex] = $("#block_" + offSetIndex).offset().top - 50;
                    }
                    window.offSetIndex++;
                });
            }
            if (jsonData.result.length == 0) {
                $("#loading").prev().remove();
                $("#loading").remove();
            } else {
                loadKey = true;
            }

            if (loadDebug) {
                $("#memo_message").show().append("<p>" + idStr + "</p>");
            }

        },
        initVideoLayoutStr:function(jsonData) {
            var idStr = '';
            if (jsonData != null || jsonData.status_code == '1') {
                $.each(jsonData.result, function(i, val) {

                    seeIndex = val.idx;
                    if (loadDebug) {
                        idStr = idStr + val.idx + ",";
                    }

                    var layoutStr = '<div class="see_video_content mt15 clearfix" id="block_' + offSetIndex + '">' +
                            val.layoutHtml +
                            '</div>' +
                            '<div class="picsee_tit"></div>';
                    $("#loading").before(layoutStr);
                    if ($("div[id^=block_]").length >= 100) {
                        $("#loading").prev().remove();
                        $("#loading").remove();
                    }
                    if (offSetIndex == 0) {
                        window.offSetTop[window.offSetIndex] = $("#block_" + offSetIndex).offset().top;
                    } else {
                        window.offSetTop[window.offSetIndex] = $("#block_" + offSetIndex).offset().top - 50;
                    }
                    window.offSetIndex++;
                });
            }
            if (jsonData.result.length == 0) {
                $("#loading").prev().remove();
                $("#loading").remove();
            } else {
                loadKey = true;
            }

            if (loadDebug) {
                $("#memo_message").show().append("<p>" + idStr + "</p>");
            }

        },
        alertPreviewBox:function(cuno, cid, contentIdx) {
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
                    createDom()
                    discoveryBiz.packegPreview(cuno, cid, contentIdx, postPreviewFun, loadDisError);
                })
            } else {
                $("#previewBox").css({"position":"fixed","_position":"absolute","z-index":"18001","top":$(window).height() / 2 + "px",left:($(window).width() - 1010) / 2 + "px",width:1010 + "px",height:"10px",border:"0px solid #ccc"})
                $("#previewBox").animate({height:($(window).height() - 40) + "px",top:"15px"}, 200, function() {
                    createDom()
                    discoveryBiz.packegPreview(cuno, cid, contentIdx, postPreviewFun, loadDisError);
                });
            }
            function createDom() {
                var loadingText = '<div class="pic_see_con">' +
                        '<div class="pic_seewrap">' +
                        '<div class="pic_opreat">' +
                        '<div class="see_operate">' +
                        '<a href="javascript:void(0)" title="取消" id="see_close" class="see_close"></a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '<div id="sea_area" class="see_area01" style="height:' + ($(window).height() - 69) + 'px">' +
                        '<div class="see_loading" id="previewLoading" >' +
                        '<p>正在载入...</p>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
                $("#previewBox").append(loadingText);
                $('#previewLoading').css('margin-top', $("#sea_area").height() / 2 - $("#previewLoading").height() + 'px');
                $('#see_close').bind('click', function() {
                    $.event.trigger("ajaxStop")//停止加载ajax
                    $("#previewBox").hide().remove();
                    $('#blackDom').hide().remove();
                    recoverHead();
                });
            }
        },
        appendLogin:function(callback, args) {
            loginPreviewLive(callback, args);
            if ($("#see_login").length > 0) {
                showLoginLayer();
            } else {
                var loginStr = '<form action="/json/login" method="post" id="maskLoginForm"><div class="wrap picwrap_mt" id="see_login"><div class="see_login"><div class="logonver">' +
                        '邮箱 <input type="text" class="see_input mr10" name="userid">' +
                        '密码 <input type="password" class="see_input" name="password">' +
                        '</div>' +
                        '<a id="log_in_dl" class="graybtn seebtn"><span>登 录</span></a>' +
                        '<a class="seebtn" href="/registerpage">新用户注册</a><div class="errorlogoin">' +
                        '<span class="tipstext" id="useridTips"></span>' +
                        '<span class="tipstext mima" id="passwordTips"></span>' +
                        '</div>' +
                        '</div>' +
                        '</div></form>';
                $(loginStr).appendTo("#seewrap").fadeIn();
                $("#sea_area").height($("#sea_area").height() - 71);
                $("#maskLoginForm").validate({
                            rules: {
                                userid: {required:true,email:true},
                                password: {required: true,rangelength: [6,18]}
                            },
                            messages: {
                                userid: {required:tipsText.userLogin.user_email_notnull, email:tipsText.userLogin.user_email_wrong},
                                password:{required:tipsText.userSet.user_userpwd_notnull,rangelength:tipsText.userSet.user_userpwd_length}
                            },
                            showErrors: showErrors//自定义错误提示
                        });
                //回车事件
                $("input[name='password']").keyup(function(e) {
                    if (e.keyCode == 13) {
                        $("#log_in_dl").click();
                    }
                });
                $("input[name='userid']").focus();
            }
        },
        preContent:function(magazineCode, contentIdx) {
            preLoadContent();
            discoveryBiz.preContent(magazineCode, contentIdx, loadContentSuccess, loadPreContentError);
        },
        nextContent:function(magazineCode, contentIdx) {
            preLoadContent();
            discoveryBiz.nextContent(magazineCode, contentIdx, loadContentSuccess, loadNextContentError);
        }
    }
    var preLoadContent = function() {
        var loadingText = '<div class="pic_see_con"><div class="pic_see_wrapcon">' +
                '<div class="pic_seewrap">' +
                '<div class="pic_opreat">' +
                '<div class="see_operate">' +
                '<a href="javascript:void(0)" title="取消" id="see_close" class="see_close"></a>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div id="sea_area" class="see_area01" style="height:' + ($(window).height() - 69) + 'px">' +
                '<div class="see_loading" id="previewLoading" >' +
                '<p>正在载入...</p>' +
                '</div>' +
                '</div></div>' +
                '</div>';
        $("#previewBox").html(loadingText);
    }

    var loadPreContentError = function(magazineCode, contentIdx, successCallback) {
        $("#previewLoading").css('background', 'none').html('<p>读取失败，请<a href="javascript:void(0)" id="resubDis">重试</a></p>');
        $("#resubDis").one('click', function() {
            discoveryBiz.preContent(magazineCode, contentIdx, successCallback, loadPreContentError);
        });
    }

    var loadNextContentError = function(magazineCode, contentIdx, successCallback) {
        $("#previewLoading").css('background', 'none').html('<p>读取失败，请<a href="javascript:void(0)" id="resubDis">重试</a></p>');
        $("#resubDis").one('click', function() {
            discoveryBiz.nextContent(magazineCode, contentIdx, successCallback, loadPreContentError);
        });
    }

    var loadContentSuccess = function(data) {
        $("#previewBox").html(data);
        document.body.parentNode.style.overflowX = "hidden";
        $("#sea_area").height($(window).height() - 69).css("overflow-y", "atuo");
        var bodyTop = document.documentElement.scrollTop || window.pageYOffset;
        $("#sea_area").bind('scroll', function() {
            var seetop = $(this).scrollTop();
            $(".pic_see_btn").css('top', seetop + 25 + 'px')
            $("#mood").hide();
        });
        preNextLive(bodyTop);
    }

    var postPreviewFun = function(data) {
        var bodyTop = document.documentElement.scrollTop || window.pageYOffset;
        $("#previewBox").html(data);
        document.body.parentNode.style.overflowX = "hidden";
        $("#sea_area").height($(window).height() - 69).css("overflow-y", "atuo");
        $("#sea_area").bind('scroll', function() {
            var seetop = $(this).scrollTop();
            $(".pic_see_btn").css('top', seetop + 25 + 'px')
            $("#mood").hide();
        });
        livePreview(bodyTop);
    }
    var loadDisError = function(cuno, cid, successCallback) {
        $("#previewLoading").css('background', 'none').html('<p>读取失败，请<a href="javascript:void(0)" id="resubDis">重试</a></p>');
        $("#resubDis").one('click', function() {
            discoveryBiz.packegPreview(cuno, cid, successCallback, loadDisError);
        })
    }
    var loginPreviewLive = function(callback, args) {
        $("#log_in_dl").die().live('click', function() {
            ajaxlogin(callback, args);
        })
    }
    var livePreview = function(bodyTop) {
        var headHeight = 57;
        $("#see_close").unbind().bind('click', function() {
            var afterBodyTop = $("#previewBox").data('bodyTop');
            $("#previewBox div").hide();
            /* $("#previewBox").animate({height:10 + "px",top:$(window).height() / 2}).fadeOut(function() {
             $("#blackDom").hide().remove();
             $(this).remove();
             });*/
            $("#previewBox").hide().remove();
            $("#blackDom").hide().remove();
            $('#mood').remove();
            $("#forward_sync_info").hide();
            $("#loveUserBox").hide().remove();
            document.body.parentNode.style.overflowY = "scroll";
            document.body.parentNode.style.overflowX = "hidden";
            $("body").css({"position":"static"});
//            $(window).scrollTop()
//            $("body").css("top", "0");
            $.browser.version == "7.0" ? $("body").scrollTop(afterBodyTop) : $(document).scrollTop(afterBodyTop);
            $("#previewBox").removeData('bodyTop');
            if (bodyTop >= 0 && bodyTop < headHeight) {
                $(".headb").css('top', headHeight - bodyTop)
            } else {
                $(".headb").css({'top': 0})
            }
            recoverHead();
        });
        if (bodyTop == undefined) {
            bodyTop = 0;
        }
        if (!($.browser.msie && ($.browser.version == "6.0") && !$.support.style)) {
//            $("body").css({"position":"fixed",'top': '-' + bodyTop + 'px',width:$(window).width() + "px","background-attachment":"fixed"});
            document.body.parentNode.style.overflowY = "hidden";
            if (bodyTop >= 0 && bodyTop < headHeight) {
//                $(".headb").css({'top': headHeight - bodyTop + 'px','width':$(".headt").width()})
            } else {
//                setTimeout(function() {
//                    $(".headb").css({'top': 0,'width':$(".headt").width()})
//                }, 0)
            }
        }
        $('.headt,.headb').css('width', $(window).width() + 'px');
        $("#previewBox").data('bodyTop', bodyTop);
        //$("#previewBox .lazy").lazyload({container:'#sea_area',effect : window.navigator.userAgent.indexOf('MSIE 7.0') > 0 ? "show" : "fadeIn"});
    }

    var preNextLive = function(bodyTop) {
        var headHeight = 57;
        $("#see_close").unbind().bind('click', function() {
            var afterBodyTop = $("#previewBox").data('bodyTop');
            $("#previewBox div").hide();
            /* $("#previewBox").animate({height:10 + "px",top:$(window).height() / 2}).fadeOut(function() {
             $("#blackDom").hide().remove();
             $(this).remove();
             });*/
            $("#previewBox").hide().remove();
            $("#blackDom").hide().remove();
            $("#loveUserBox").hide().remove();
            $('#mood').remove();
            $("#forward_sync_info").hide();
            document.body.parentNode.style.overflowY = "scroll";
            document.body.parentNode.style.overflowX = "hidden";
            $("body").css({"position":"static"});
//            $(window).scrollTop()
//            $("body").css("top", "0");
            $.browser.version == "7.0" ? $("body").scrollTop(afterBodyTop) : $(document).scrollTop(afterBodyTop);
            $("#previewBox").removeData('bodyTop');
            if (bodyTop >= 0 && bodyTop < headHeight) {
                $(".headb").css('top', headHeight - bodyTop)
            } else {
                $(".headb").css({'top': 0})
            }
            recoverHead();
        });
    }

    var showErrors = function() {
        //验证失败处理
        for (var i = 0; this.errorList[i]; i++) {
            var error = this.errorList[i];
            var elename = this.idOrName(error.element);
            $('#' + elename + "Tips").html('<em>' + error.message + '</em>');
        }
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            $('#' + suc.name + "Tips").html('');
        }
    }
    var ajaxlogin = function(callback, args) {
        $("#maskLoginForm").ajaxForm(function(req) {
            var jsonObj = eval('(' + req + ')');
            //弹出层提示
            if (jsonObj.status_code == "1") {
                var login = require('../biz/login-biz')
                login.initLogin(jsonObj);
                showLoginLayer();
                callback.apply(null, args)
            } else {
                $("#passwordTips").html('<span><em>' + jsonObj.msg + '</em></span>');
            }
        });
        $("#maskLoginForm").submit();
    }
    var showLoginLayer = function() {
        $("#see_login").toggle();
        if (!$("#see_login").is(":hidden")) {
            $("#sea_area").height($("#sea_area").height() - 71);
        } else {
            $("#sea_area").height($("#sea_area").height() + 71);
        }
    }
    var recoverHead = function() {
        var navDom = $(".headb");
        var logoDom = $('.head_t');
        var scrollTop = $(window).scrollTop();
        if (scrollTop == undefined) {
            logoDom.attr("style", 'margin-bootom:0px');
            navDom.attr('style', 'position:relative;');
            return;
        }

        if (scrollTop > 57) {
            logoDom.attr("style", 'margin-bootom:39px');
            navDom.attr('style', 'position:fixed;');
        } else {
            logoDom.attr("style", 'margin-bootom:0px');
            navDom.attr('style', 'position:relative;');
        }
    }
    return discovery
})
