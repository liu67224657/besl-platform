!function(){"use strict";function t(e,o){function i(t,e){return function(){return t.apply(e,arguments)}}var r;if(o=o||{},this.trackingClick=!1,this.trackingClickStart=0,this.targetElement=null,this.touchStartX=0,this.touchStartY=0,this.lastTouchIdentifier=0,this.touchBoundary=o.touchBoundary||10,this.layer=e,this.tapDelay=o.tapDelay||200,this.tapTimeout=o.tapTimeout||700,!t.notNeeded(e)){for(var a=["onMouse","onClick","onTouchStart","onTouchMove","onTouchEnd","onTouchCancel"],c=this,s=0,u=a.length;u>s;s++)c[a[s]]=i(c[a[s]],c);n&&(e.addEventListener("mouseover",this.onMouse,!0),e.addEventListener("mousedown",this.onMouse,!0),e.addEventListener("mouseup",this.onMouse,!0)),e.addEventListener("click",this.onClick,!0),e.addEventListener("touchstart",this.onTouchStart,!1),e.addEventListener("touchmove",this.onTouchMove,!1),e.addEventListener("touchend",this.onTouchEnd,!1),e.addEventListener("touchcancel",this.onTouchCancel,!1),Event.prototype.stopImmediatePropagation||(e.removeEventListener=function(t,n,o){var i=Node.prototype.removeEventListener;"click"===t?i.call(e,t,n.hijacked||n,o):i.call(e,t,n,o)},e.addEventListener=function(t,n,o){var i=Node.prototype.addEventListener;"click"===t?i.call(e,t,n.hijacked||(n.hijacked=function(t){t.propagationStopped||n(t)}),o):i.call(e,t,n,o)}),"function"==typeof e.onclick&&(r=e.onclick,e.addEventListener("click",function(t){r(t)},!1),e.onclick=null)}}var e=navigator.userAgent.indexOf("Windows Phone")>=0,n=navigator.userAgent.indexOf("Android")>0&&!e,o=/iP(ad|hone|od)/.test(navigator.userAgent)&&!e,i=o&&/OS 4_\d(_\d)?/.test(navigator.userAgent),r=o&&/OS [6-7]_\d/.test(navigator.userAgent),a=navigator.userAgent.indexOf("BB10")>0;t.prototype.needsClick=function(t){switch(t.nodeName.toLowerCase()){case"button":case"select":case"textarea":if(t.disabled)return!0;break;case"input":if(o&&"file"===t.type||t.disabled)return!0;break;case"label":case"iframe":case"video":return!0}return/\bneedsclick\b/.test(t.className)},t.prototype.needsFocus=function(t){switch(t.nodeName.toLowerCase()){case"textarea":return!0;case"select":return!n;case"input":switch(t.type){case"button":case"checkbox":case"file":case"image":case"radio":case"submit":return!1}return!t.disabled&&!t.readOnly;default:return/\bneedsfocus\b/.test(t.className)}},t.prototype.sendClick=function(t,e){var n,o;document.activeElement&&document.activeElement!==t&&document.activeElement.blur(),o=e.changedTouches[0],n=document.createEvent("MouseEvents"),n.initMouseEvent(this.determineEventType(t),!0,!0,window,1,o.screenX,o.screenY,o.clientX,o.clientY,!1,!1,!1,!1,0,null),n.forwardedTouchEvent=!0,t.dispatchEvent(n)},t.prototype.determineEventType=function(t){return n&&"select"===t.tagName.toLowerCase()?"mousedown":"click"},t.prototype.focus=function(t){var e;o&&t.setSelectionRange&&0!==t.type.indexOf("date")&&"time"!==t.type&&"month"!==t.type?(e=t.value.length,t.setSelectionRange(e,e)):t.focus()},t.prototype.updateScrollParent=function(t){var e,n;if(e=t.fastClickScrollParent,!e||!e.contains(t)){n=t;do{if(n.scrollHeight>n.offsetHeight){e=n,t.fastClickScrollParent=n;break}n=n.parentElement}while(n)}e&&(e.fastClickLastScrollTop=e.scrollTop)},t.prototype.getTargetElementFromEventTarget=function(t){return t.nodeType===Node.TEXT_NODE?t.parentNode:t},t.prototype.onTouchStart=function(t){var e,n,r;if(t.targetTouches.length>1)return!0;if(e=this.getTargetElementFromEventTarget(t.target),n=t.targetTouches[0],o){if(r=window.getSelection(),r.rangeCount&&!r.isCollapsed)return!0;if(!i){if(n.identifier&&n.identifier===this.lastTouchIdentifier)return t.preventDefault(),!1;this.lastTouchIdentifier=n.identifier,this.updateScrollParent(e)}}return this.trackingClick=!0,this.trackingClickStart=t.timeStamp,this.targetElement=e,this.touchStartX=n.pageX,this.touchStartY=n.pageY,t.timeStamp-this.lastClickTime<this.tapDelay&&t.preventDefault(),!0},t.prototype.touchHasMoved=function(t){var e=t.changedTouches[0],n=this.touchBoundary;return Math.abs(e.pageX-this.touchStartX)>n||Math.abs(e.pageY-this.touchStartY)>n?!0:!1},t.prototype.onTouchMove=function(t){return this.trackingClick?((this.targetElement!==this.getTargetElementFromEventTarget(t.target)||this.touchHasMoved(t))&&(this.trackingClick=!1,this.targetElement=null),!0):!0},t.prototype.findControl=function(t){return void 0!==t.control?t.control:t.htmlFor?document.getElementById(t.htmlFor):t.querySelector("button, input:not([type=hidden]), keygen, meter, output, progress, select, textarea")},t.prototype.onTouchEnd=function(t){var e,a,c,s,u,l=this.targetElement;if(!this.trackingClick)return!0;if(t.timeStamp-this.lastClickTime<this.tapDelay)return this.cancelNextClick=!0,!0;if(t.timeStamp-this.trackingClickStart>this.tapTimeout)return!0;if(this.cancelNextClick=!1,this.lastClickTime=t.timeStamp,a=this.trackingClickStart,this.trackingClick=!1,this.trackingClickStart=0,r&&(u=t.changedTouches[0],l=document.elementFromPoint(u.pageX-window.pageXOffset,u.pageY-window.pageYOffset)||l,l.fastClickScrollParent=this.targetElement.fastClickScrollParent),c=l.tagName.toLowerCase(),"label"===c){if(e=this.findControl(l)){if(this.focus(l),n)return!1;l=e}}else if(this.needsFocus(l))return t.timeStamp-a>100||o&&window.top!==window&&"input"===c?(this.targetElement=null,!1):(this.focus(l),this.sendClick(l,t),o&&"select"===c||(this.targetElement=null,t.preventDefault()),!1);return o&&!i&&(s=l.fastClickScrollParent,s&&s.fastClickLastScrollTop!==s.scrollTop)?!0:(this.needsClick(l)||(t.preventDefault(),this.sendClick(l,t)),!1)},t.prototype.onTouchCancel=function(){this.trackingClick=!1,this.targetElement=null},t.prototype.onMouse=function(t){return this.targetElement?t.forwardedTouchEvent?!0:t.cancelable&&(!this.needsClick(this.targetElement)||this.cancelNextClick)?(t.stopImmediatePropagation?t.stopImmediatePropagation():t.propagationStopped=!0,t.stopPropagation(),t.preventDefault(),!1):!0:!0},t.prototype.onClick=function(t){var e;return this.trackingClick?(this.targetElement=null,this.trackingClick=!1,!0):"submit"===t.target.type&&0===t.detail?!0:(e=this.onMouse(t),e||(this.targetElement=null),e)},t.prototype.destroy=function(){var t=this.layer;n&&(t.removeEventListener("mouseover",this.onMouse,!0),t.removeEventListener("mousedown",this.onMouse,!0),t.removeEventListener("mouseup",this.onMouse,!0)),t.removeEventListener("click",this.onClick,!0),t.removeEventListener("touchstart",this.onTouchStart,!1),t.removeEventListener("touchmove",this.onTouchMove,!1),t.removeEventListener("touchend",this.onTouchEnd,!1),t.removeEventListener("touchcancel",this.onTouchCancel,!1)},t.notNeeded=function(t){var e,o,i,r;if("undefined"==typeof window.ontouchstart)return!0;if(o=+(/Chrome\/([0-9]+)/.exec(navigator.userAgent)||[,0])[1]){if(!n)return!0;if(e=document.querySelector("meta[name=viewport]")){if(-1!==e.content.indexOf("user-scalable=no"))return!0;if(o>31&&document.documentElement.scrollWidth<=window.outerWidth)return!0}}if(a&&(i=navigator.userAgent.match(/Version\/([0-9]*)\.([0-9]*)/),i[1]>=10&&i[2]>=3&&(e=document.querySelector("meta[name=viewport]")))){if(-1!==e.content.indexOf("user-scalable=no"))return!0;if(document.documentElement.scrollWidth<=window.outerWidth)return!0}return"none"===t.style.msTouchAction||"manipulation"===t.style.touchAction?!0:(r=+(/Firefox\/([0-9]+)/.exec(navigator.userAgent)||[,0])[1],r>=27&&(e=document.querySelector("meta[name=viewport]"),e&&(-1!==e.content.indexOf("user-scalable=no")||document.documentElement.scrollWidth<=window.outerWidth))?!0:"none"===t.style.touchAction||"manipulation"===t.style.touchAction?!0:!1)},t.attach=function(e,n){return new t(e,n)},"function"==typeof define&&"object"==typeof define.amd&&define.amd?define(function(){return t}):"undefined"!=typeof module&&module.exports?(module.exports=t.attach,module.exports.FastClick=t):window.FastClick=t}();
var commonFn = {
    isPC: function () {
        var userAgentInfo = navigator.userAgent;
        var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");
        var flag = true;
        for (var v = 0; v < Agents.length; v++) {
            if (userAgentInfo.indexOf(Agents[v]) > 0) {
                flag = false;
                break;
            }
        }
        return flag;
    },
    isIpad: function () {
        var userAgentInfo = navigator.userAgent;
        if (userAgentInfo.indexOf("iPad") > -1) {
            return true
        }
        return false;
    },
    iosShu: function () {
        if (window.orientation == 0 || window.orientation == 180) {
            // ios 竖屏
            return true;
        }


    },
    iosHeng: function () {
        if (window.orientation == 90 || window.orientation == -90) {
            // ios 横屏
            return true;
        }
    }
}

$(function(){
  FastClick.attach(document.body);
    // 登陆注册
    var aboutLogin = {
        init: function () {
            this.initEl();
            this.bindEvent();
        },
        initEl: function () {
            this.loginBtn = "login"; // 登陆
            this.registerBtn = "register";// 注册
            this.lostPassword = "get-password"; //忘记密码
            this.closeBtn = $('.close-icon');
            this.mask = $('.mask-login');

        },
        bindEvent: function () {
            var that = this;
            // 登陆
            $("." + this.loginBtn).click(function () {
                that.loginHandler(that.loginBtn);
            })
            // 注册
            $("." + this.registerBtn).click(function () {
                that.loginHandler(that.registerBtn);
            })
            // 忘记密码
            $("." + this.lostPassword).click(function () {
                that.loginHandler(that.lostPassword);
            });
            // close
            this.closeBtn.click(function () {
                that.mask.hide();
                $('.login-bg').hide();
                $('body').animate({scrollTop: 0}, 500);
            });
        },
        loginHandler: function (config) {
            var oEle = $('.' + config), oEleBox = $('.' + config + '-box'), oMask = $(oEleBox.parents('.mask-login'));
            this.mask.hide();
            oEleBox.show();
            oMask.show();
        }
    }
    // 登陆注册结束


    var mainJs = {
        init: function () {
            // 初始化登陆功能；
            aboutLogin.init();
            // 个人中心首页 横向切换
            if ($('.prostraters-box').length > 0) {
                $.horizontalMove({
                    parent: '.prostraters-box',
                    sonUl: '.prostraters-ul',
                    // 滑动的块
                    curShowNum: 3,
                    // 显示区内的个数；
                    leftBtn: '.prostraters-left',
                    rightBtn: '.prostraters-right',
                    sonMargin: 8
                });
            }
            this.initEl();
            this.render()
            this.bindEvent()
        },
        initEl: function () {
            this.doc = $(document);
            this.signInBtn = $(".user-sign"); //签到按钮
            // this.countIcon = $('.count-icon');// 个人中心页 管理wiki中的下拉
            this.clientH = $(window).height();
        },
        render: function () {
            this.resizeFn();
            !$('.top-icon').length && $('<div class="top-icon"></div>').appendTo($('body'));
        },
        bindEvent: function () {
          // 置顶
          var that = this;
          var _event = commonFn.isPC() ? 'scroll' : 'touchmove';
          // alert(_event);
          $(window).on(_event,function(e){
            // e.preventDefault();
            var docScroll = that.doc.scrollTop();
            if(docScroll >= that.clientH){
              $('.top-icon').show().unbind('click').click(function(){
                $('html,body').animate({
                  scrollTop:0
                },200)
                $(this).hide();
              });
              return;
            }
            $('.top-icon').hide();
          })
            // 签到
            var signLock = false;
            this.signInBtn.click(function () {
                var that = this;
                if (!$(this).hasClass('had-signin') && !signLock) {
                    signLock = true;
                    $.ajax({
                        url: '/joyme/json/usercenter/sign',
                        cache: false,
                        type: 'post',
                        dataType: "json",
                        success: function (data) {
                            if (data.rs == "1") {
                                if (data.result == -1) {
                                    signLock = false;
                                    $(that).addClass('had-signin');
                                    mw.ugcwikiutil.autoCloseDialog("已经签过到了");
                                    return;
                                }
                                $(that).addClass('had-signin');
                                $("[name='todaypoint']").each(function () {
                                    var todayPoint = parseInt(data.result) + parseInt($(this).text());
                                    $(this).text(todayPoint);
                                });
                                $("[name='userpoint']").each(function () {
                                    var userPoint = parseInt(data.result) + parseInt($(this).text());
                                    $(this).text(userPoint);
                                });
                                var text = "签到成功，积分+" + data.result;
                                mw.ugcwikiutil.autoCloseDialog(text);
                                signLock = false;

                            } else {
                                loginDiv();
                                signLock = false;
                                return
                            }
                        },
                        error: function () {

                        }

                    })
                }
            })
            // select下拉框
            this.selectMenu()
            // resize
            $(window).resize(function () {

            })

            // 个人中心页 管理wiki中的下拉
            $(document).on('click', '.manage-wiki i', function () {
                var addEdit = $(this).siblings('.add-edit');
                if (!$(this).hasClass('on')) {
                    $(this).addClass('on');
                    addEdit.show();
                } else {
                    $(this).removeClass('on');
                    addEdit.hide();
                }
            });

            // 个人中心页 tab切换(我的动态、我的wiki、好友动态)；
            // if(commonFn.isPC()){
            this.tabMenu({tabMain:$('.tab-box'),tabTit:$('.tab-tit'),tabCon:$('.tab-con'),activing:"on"});
            // }else{
            //   $('.tab-tit>a').on('touchend',function(){
            //     var index = $(this).index();
            //     $(this).addClass('on').siblings().removeClass('on');
            //     $('.tab-con>div').removeClass('on').eq(index).addClass('on');
            //   })
            // }

            // 关注落脚页 首次(m端) tab切换
            $('.first-ui-list').on('click', function (e) {
                if (!$(this).hasClass('on')) {
                    $(this).addClass('on').siblings('.first-ui-list').removeClass('on');
                }
            })

            // 我的积分
            $('.jifen-btn').click(function () {
                var index = $(this).index();
                $(this).addClass('on').siblings().removeClass('on');
                $('.tab-change-li').eq(index).addClass('on').siblings().removeClass('on');
            })

        },
        tabMenu: function (conf) {
            var tabTit = conf.tabTit,
                iconChild = tabTit.children(),
                tabCon = conf.tabcon,
                activing = conf.activing;
            iconChild.click(function () {
                var _this = $(this).index();
                conChild = $(this).parent(tabTit).siblings(tabCon).children();
                $(this).addClass(activing).siblings(iconChild).removeClass(activing);
                conChild.eq(_this).addClass(activing).siblings(conChild).removeClass(activing);
            });
        },
        resizeFn: function () {

        },
        selectMenu: function () {
            $(".select-area select").each(function () {
                var value = $(this).find("option:selected").text();
                $(this).parent(".select-area").find(".select-value").text(value);
            });
            $(".select-area select").change(function () {
                var value = $(this).find("option:selected").text();
                $(this).parent(".select-area").find(".select-value").text(value);
            });
        }

    }
    mainJs.init();
})


function buildFormText(text) {
    var count = 2;
    var html = '<div class="joyme-dialog-popup-mc on">' +
        '<div class="joyme-dialog-wiki-popup">' +
        '<div class="joyme-dialog-warning">' +
        '<p><span>!</span><b>提示</b></p>' +
        '</div>' +
        '<div class="joyme-dialog-warn-con">' +
        '<div class="new-window-content">' +
        '<div class="new-window-line txt-center">' +
        '<span class="label-word">' + text + '</span>' +
        '<p class="countdown txt-center">' + count + 's后窗口自动关闭</p>' +
        '</div>' +
        '</div>' +
        '<div class="joyme-dialog-warn-close">' +
        '<p class="joyme-dialog-close-btn">' +
        '<a class="joyme-dialog-confirm">确定</a>' +
        '</p>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>';
    $(html).appendTo($('body'));
    var timer = setInterval(function () {

        if (count > 0) {
            count--;
            $('.countdown').text(count + 's后窗口自动关闭');
        } else {
            closeWindow();
            return;
        }
    }, 1000);
    $('.joyme-dialog-confirm').unbind('click').click(function () {
        closeWindow();
    })
    function closeWindow() {
        clearInterval(timer);
        timer = null;
        $('.joyme-dialog-popup-mc').remove();

    }
}