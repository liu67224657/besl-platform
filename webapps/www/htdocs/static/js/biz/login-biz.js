define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var pop = require('../common/jmpopup');
    var joymeform = require('../common/joymeform');
    var common = require('../common/common');
    require('../common/jquery.validate.min');
    require('../common/jquery.form');
    require('../common/tips');
    window.loginLock = false;
    var defaultLoginOption = {
        jqid: null,
        trigger: 'click',
        referer: null,
        callbackfunction: null,
        args: [],
        title: '请先登录着迷网',
        thirdauthrurl: window.location.href,
        headStyle: 'position:fixed',
        id: 'mask_login'
    };
    var login = {
        bindMaskLogin: function () {
            $("a[name=maskLogin]").die().live("click", function () {
                var loginOption = {
                    referer: window.location.href
                };
                loginMaskInit(loginOption);
            });

            $("a[name=slideLogin]").die().live("click", function () {
                var location = ''
                if (document.location.href.lastIndexOf('/index') != -1 || document.location.href.lastIndexOf('/loginpage') != -1) {
                    location = joyconfig.URL_WWW;
                } else {
                    location = window.location.href;
                }

                var left = $(this).offset().left - 528;
                var loginOption = {
                    referer: location,
                    title: '登录着迷网',
                    isHeader: true,
                    headStyle: 'position:absolute;left:' + left + 'px',
                    id: 'mask_slogin'
                };
                loginSlideInit(loginOption);
            });
        },

        bindMaskLoginOnBlog: function () {
            $("#maskLogin").die().live("click", function () {
                var loginOption = {
                    callbackfunction: function () {
                        $("#reply_content").removeAttr("disabled");
                        $("#maskLogin").parent().remove();

                        var navDom = $(".headb");
                        var logoDom = $('.head_t');

                        var sTop = $(window).scrollTop();
                        if (sTop == undefined || sTop < 57) {
                            logoDom.attr("style", 'margin-bootom:39px');
                            navDom.attr('style', 'position:fixed;');
                        } else {
                            logoDom.attr("style", 'margin-bootom:0px');
                            navDom.attr('style', 'position:relative;');

                        }
                    },
                    args: []
                };
                loginMaskInit(loginOption);
            });
        },
        bindMaskLoginOnGame: function () {
            $("#maskLogin").die().live("click", function () {
                var loginOption = {
                    callbackfunction: function () {
                        $("#post_text_submit").removeAttr("disabled");
                        $("#maskLogin").parent().remove();
                        $("#text_syn_install_later").attr('id', 'text_syn_install')
                    },
                    args: []
                };
                loginMaskInit(loginOption);
            });
        },
        /**
         * 通过JSON返回对象中的status_code判断是否跳到登录页面
         *
         * @param jsonObj msg包含错误信息,reuslt[0]包含来源地址
         */
        locationLoginByJsonObj: function (jsonObj) {
            if (jsonObj == null || jsonObj.status_code == '-1') {
                window.location.href = "/loginpage?reurl=" + encodeURIComponent(window.location.href);
                return;
            }
        },
        maskLoginByJsonObj: function (jsonObj) {
            if (jsonObj == null || jsonObj.status_code == '-1') {
                var loginOption = {
                    referer: window.location.href
                };
                loginMaskInit(loginOption);
                return false;
            }
            return true;
        },
        maskLogin: function (loginOption) {
            loginMaskInit(loginOption);
        },
        checkLogin: function (jqObj) {
            if (joyconfig.joyuserno == '') {
                var loginOption = {
                    jqid: jqObj.attr("id"),
                    trigger: 'click'
                };
                this.maskLogin(loginOption);
                return false;
            } else {
                return true;
            }
        },
        regReturn: function (url) {
            if (joyconfig.joyuserno != null && joyconfig.joyuserno != '') {
                var args = getRequestParam(url);
                if (args['jqid'] != null && args['jqid'] != '') {
//                    setTimeout(function() {
                    window.scrollTo(args['scrolltop'], args['scrolltop']);
                    $("#" + args['jqid']).trigger(args['trigger']);
//                    }, 200);
                }
            }
        },
        initLogin: function (jsonObj) {
            var result = jsonObj.result[0];
            var obj = {token: result.authToken.token,
                joyuserno: result.accountUno,
                joyblogname: result.blogwebsite.screenName,
                joyblogdomain: result.blogwebsite.domain, joyheadimg: result.blogwebsite.headIcon,
                joysex: result.userDetailinfo.sex}
            joyconfig = $.extend(joyconfig, obj);
            $('#header_nav_list li:eq(2)').before('<li><a href="' + joyconfig.URL_WWW + '/home" class="listbold"><span>个人中心</span></a></li>');
            $("#tab_login_list").remove();
            var logoutReUrl = window.location.href;
            if (logoutReUrl.lastIndexOf('/discovery/hot') > 0) {
                logoutReUrl = '/discovery/hot';
            } else if (logoutReUrl.lastIndexOf('/discovery') > 0) {
                logoutReUrl = '/discovery';
            }

            var headIcon = '';
            if (result.blogwebsite.headIconSet != null && result.blogwebsite.headIconSet.iconSet != null
                && result.blogwebsite.headIconSet.iconSet.length > 0) {
                for (var i = 0; i < result.blogwebsite.headIconSet.iconSet.length; i++) {
                    var icon = result.blogwebsite.headIconSet.iconSet[i];
                    if (icon.validStatus) {
                        headIcon = icon.headIcon;
                        break;
                    }
                }
            }

            var screenName = result.blogwebsite.screenName;
            if (common.strLen(screenName) > 8) {
                screenName = common.subStr(screenName, 8);
            }

            $('#head_nav_right').html('<div class="menu"><a href="javascript:void(0);"' +
                'id="header_func_link">' + screenName + '<span></span></a>' +
                '<a href="' + joyconfig.URL_WWW + '/people/' + result.blogwebsite.domain + '" target="_blank">' +
                '<img src="' + common.parseSimg(headIcon, joyconfig.DOMAIN) + '" width="33" height="33">' +
                '</a></div><ul class="nav_list"><li class="nav_line">&nbsp;</li><li><a href="http://www.joyme.com/invite/invitepage"><span><em></em>邀请好友</span></a></li></ul>');
            $('.headb').append('<div class="nav_item" style="display:none" id="header_func_area">' +
                '<div class="itemt"></div>' +
                '<div class="itemc">' +
                '<ul>' +
                '<li class="first"><a href="' + joyconfig.DOMAIN + '/people/' + result.blogwebsite.domain + '">我的博客</a></li>' +
                '<li><a href="' + joyconfig.URL_WWW + '/profile/customize">设置</a></li>' +
                '<li><a href="http://passport.' + joyconfig.DOMAIN + '/auth/logout?reurl=' + logoutReUrl + '">退出</a></li>' +
                '</ul>' +
                '</div>' +
                '<div class="itemb"></div>' +
                '</div>');
            $("#reg_tips").remove();
        }
    }

    var loginMaskInit = function (loginOption) {
        var popConfig = {
            pointerFlag: false,//是否有指针
            tipLayer: true,//是否遮罩
            containTitle: true,//包含title
            containFoot: false,//包含footer
            forclosed: true,
            offset: "",
            popwidth: 584,
            popscroll: true,
            allowmultiple: false,
            isremovepop: true,
            isHeader: false,
            hideCallback: function () {
                common.setOverflowY.show();
            }
        };
        loginOption = $.extend({}, defaultLoginOption, loginOption);
        initLoginByConfig(popConfig, loginOption);
    }

    var loginSlideInit = function (loginOption) {
        var loginDom = $('#' + loginOption.id);
        if (loginDom.length == 0) {
            loginOption = $.extend({}, defaultLoginOption, loginOption);
            var html = initLoginHtml(loginOption);
            $('#login_f').replaceWith(html);
            loginDom = $('#' + loginOption.id);
            loginDom.slideDown();
            $('#closePop_' + loginOption.id).die().live('click', function () {
                loginDom.slideUp();
            })
            loginValidateInit(loginOption);
            $("#loginMaskReg").bind("click", function () {
                loginRegisterLink(loginOption)
            });
            $("#MaskReg").bind("click", function () {
                loginRegisterLink(loginOption)
            })
        } else {
            loginDom.slideDown();
        }
    }

    var initLoginByConfig = function (popConfig, loginOption) {
        common.setOverflowY.hide();
        $('#mask_slogin').hide();
        var html = initLoginHtml(loginOption);
        popConfig.id = loginOption.id;
        pop.popupInitHtmlStr(popConfig, html);
        loginValidateInit(loginOption);
        $("#loginMaskReg").bind("click", function () {
            loginRegisterLink(loginOption)
        });
        $("#MaskReg").bind("click", function () {
            loginRegisterLink(loginOption)
        })
    };
    var loginRegisterLink = function (loginOption) {
        var url = '';
        if (loginOption.referer != null && loginOption.referer != '') {
            url = loginOption.referer
        } else {
            url = window.location.href;
        }

        var paramStr = '';
        if (loginOption.jqid != null && loginOption.jqid != '') {
            paramStr = "jqid=" + loginOption.jqid + "&trigger=" + loginOption.trigger + "&scrolltop=" + $(window).scrollTop();
            if (/(\?[.]+=)/.test(url)) {
                url = url + "&" + paramStr;
            } else {
                url = url + "?" + paramStr;
            }
        }

        window.location.href = joyconfig.URL_WWW + "/registerpage?referer=" + encodeURIComponent(url);
    }
    var loginValidateInit = function (loginOption) {
        $("#maskLoginForm").validate({
            rules: {
                userid: {required: true, email: true},
                password: {required: true, rangelength: [6, 18]}
            },
            messages: {
                userid: {required: tipsText.userLogin.user_email_notnull, email: tipsText.userLogin.user_email_wrong},
                password: {required: tipsText.userSet.user_userpwd_notnull, rangelength: tipsText.userSet.user_userpwd_length}
            },
            showErrors: showErrors//自定义错误提示
        });

        $("#log_in_dl").die().live("click", function () {
            ajaxlogin(loginOption);
        });

        $("#password").live("keyup keydown", function (event) {
            if (event.keyCode == 13) {
                ajaxlogin(loginOption);
            }
        });
    }

    var showErrors = function () {
        //验证失败处理
        if (this.errorList.length > 0) {
            window.loginLock = false;
            recoverBut();
            for (var i = 0; this.errorList[i]; i++) {
                var error = this.errorList[i];
                var elename = this.idOrName(error.element);
                $('#' + elename + "Tips").html('<span><em>' + error.message + '</em></span>');
            }
        }
        //验证成功处理
        for (var i = 0; this.successList[i]; i++) {
            var suc = this.successList[i];
            $('#' + suc.id + "Tips").html('');
        }
    }

    var ajaxlogin = function (loginOption) {
        var formOption = {
            formid: 'maskLoginForm',
            beforeSend: function () {
                if (window.loginLock) {
                    return false;
                }
                window.loginLock = true;
                loadingBut();
                return true;
            },
            success: function (req, paramObj) {
                var jsonObj = eval('(' + req + ')');
                //弹出层提示
                if (jsonObj.status_code == "1") {
                    login.initLogin(jsonObj);
                    if ($("#blackDom").length > 0) {
                        $("#blackDom").fadeOut().remove();
                    }
                    $("#mask_login").remove();

                    common.setOverflowY.show();

                    if (loginOption.jqid != null && loginOption.jqid != "") {
                        $("#" + loginOption.jqid).trigger(loginOption.trigger);
                    } else if (loginOption.referer != null && loginOption.referer != "") {
                        window.location.href = loginOption.referer;
                    }
                    if (loginOption.callbackfunction != null) {
                        loginOption.callbackfunction.apply(null, loginOption.args);
                    }
                    // reg tips
                    $("#reg_tips").remove();
                } else {
                    recoverBut();
                    $("#passwordTips").html('<span><em>' + jsonObj.msg + '</em></span>');
                }
            },
            complete: function (req, paramObj) {
                window.loginLock = false;
            }
        };

        joymeform.form(formOption);
    }

    var loadingBut = function () {
        var loginBut = $("#log_in_dl");
        loginBut.attr('class', 'loadbtn').html('<span>登录中…</span>');
        if ($('#login_loading').length == 0) {
            loginBut.before('<span id="login_loading" class="loadings"></span>');
        }
    }

    var recoverBut = function () {
        var loginBut = $("#log_in_dl");
        loginBut.attr('class', 'graybtn').html('<span>登 录</span>');
        $('#login_loading').remove();
    }

    var initLoginHtml = function (loginOption) {
        if (/msie 6/i.test(navigator.userAgent)) {
            loginOption.headStyle += '_position:absolute;'
        }

        var thirdApiOuthRurl = loginOption.thirdauthrurl;
        if (thirdApiOuthRurl == null) {
            thirdApiOuthRurl = window.location.href;
        }
        if (thirdApiOuthRurl.indexOf(joyconfig.URL_WWW + "/index") > -1) {
            thirdApiOuthRurl = '';
        }

        var html = '<div id="' + loginOption.id + '" class="wlogin_con" style="display:none;' + loginOption.headStyle + '; z-index:16002;">' +
            '<div class="wlogin_t"></div>' +
            '<div class="wlogin_c clearfix">' +
            '<a href="javascript:void(0)" id="closePop_' + loginOption.id + '" class="close"></a>' +
            '<div class="blog_login wlogin_l"><h3>登录着迷</h3>' +
            '<form action="/json/login" method="post" id="maskLoginForm">' +
            '<p>邮箱：<input type="text" class="inputtextbtn" id="userid" name="userid"></p>' +
            '<p class="error" id="useridTips"></p>' +
            '<p>密码：<input type="password" class="inputtextbtn" id="password" name="password"></p>' +
            '<p class="error" id="passwordTips"></p><p class="logintop">' +
            '<label><input type="checkbox" class="checkbox" name="persistent" checked="checked" value="1">记住我的登录状态</label>' +
            '<span class="blogright">' +
            '<a href="/security/pwd/forgot">忘记密码</a>' +
            '</span></p>' +
            '<div class="blog_ft">' +
            '<a class="graybtn" id="log_in_dl"><span>登 录</span></a>' +
            '<p>还没有账号，现在<a href="javascript:void(0)" id="loginMaskReg">注册</a></p>' +
            '</div>' +
            '</form>' +
            '</div>' +
            '<div class="wlogin_r">' +
            '<p>还没有着迷账号？ <a href="javascript:void(0)" id="MaskReg">立即注册</a></p>' +
            '<p>你还可以用以下方式直接登录：' +
            '<span class="cooper clearfix"> ' +
            '<a class="qq" title="qq" href="http://passport.'+joyconfig.DOMAIN+'/auth/thirdapi/qq/bind?reurl=' + encodeURIComponent(thirdApiOuthRurl) + '"></a>' +
            '<a class="weibo" title="新浪微博" href="http://passport.'+joyconfig.DOMAIN+'/auth/thirdapi/sinaweibo/bind?reurl=' + encodeURIComponent(thirdApiOuthRurl) + '"></a>' +
            '</span>' +
            '</p>' +
            '</div>' +
            '</div>' +
            '<div class="wlogin_b"></div></div>';
        return html;
    }

    var getRequestParam = function (url) {
        var args = new Object();
        if (url.indexOf("jqid") != -1) {
            var intPos = url.indexOf("?");
            var strRight = url.substr(intPos + 1);
            var arrTmp = strRight.split("&");
            for (var i = 0; i < arrTmp.length; i++) {
                var dIntPos = arrTmp[i].indexOf("=");
                var paraName = arrTmp[i].substr(0, dIntPos);
                var paraData = arrTmp[i].substr(dIntPos + 1);
                args[paraName] = paraData;
            }
        }
        return args;
    }

    return login;
});






