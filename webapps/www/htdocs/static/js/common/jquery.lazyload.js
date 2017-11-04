/*
 * Lazy Load - jQuery plugin for lazy loading images
 *
 * Copyright (c) 2007-2009 Mika Tuupola
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Project home:
 *   http://www.appelsiini.net/projects/lazyload
 *
 * Version:  1.5.0
 *
 */
define(function() {
    return function($) {
        (function($) {
            $.fn.scrollLoading = function(options) {
                var defaults = {
                    attr: "original"
                };
                var params = $.extend({}, defaults, options || {});
                params.cache = [];
                $(this).each(function() {
                    var node = this.nodeName.toLowerCase(), url = $(this).attr(params["attr"]);
                    if (!url) {
                        return;
                    }
                    //重组
                    var data = {
                        obj: $(this),
                        tag: node,
                        url: url
                    };
                    params.cache.push(data);
                });

                //动态显示数据
                var loading = function() {
                    if(params.cache==null || params.cache.length==0){
                        $(window).unbind(".lazyload");
                    }

                    var st = $(window).scrollTop(), sth = st + $(window).height();
                    for (var i = 0; i < params.cache.length; i++) {
                        var data = params.cache[i];
                        if(!data){
                            continue;
                        }

                        var o = data.obj, tag = data.tag, url = data.url;
                        if (o) {
                            var post = o.offset().top;
                            var posb = post + o.height();
                            if ((post >= st && post <= sth) || (posb >= st && posb <= sth)) {
                                //在浏览器窗口内
                                if (tag === "img") {
                                    //图片，改变src
                                    o.attr("src", url);
                                } else {
                                    o.load(url);
                                }
                                params.cache.splice(i, 1);
                                i--;
                            }
                        }
                    }
                    return false;
                };

                //加载完毕即执行
                loading();
                //事件触发 滚动执行
                $(window).bind("scroll.lazyload",function(){
                    _delay(loading);
                });

                var _delay = function(run) {
                    clearTimeout(this._lltimer);
                    var oThis = this, delay = this.delay;
                    if (this._lazyloadlock) {//防止连续触发
                        this._lltimer = setTimeout(function() {
                            _delay(run);
                        }, delay);
                    } else {
                        this._lazyloadlock = true;
                        run();
                        setTimeout(function() {
                            oThis._lazyloadlock = false;
                        }, delay);
                    }
                }
            };
        })(jQuery);

    }
});