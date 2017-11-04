function Slider(ops) {
    this.outer = ops.wrapper;
    this.btn = ops.btn;
    this.list = ops.data;
    this.sp = ops.change;
    this.init();   // 初始化
    //this.go();
    this.bindDom();
}
Slider.prototype.init = function () {
    this.per = window.innerHeight / window.innerWidth;//屏幕    高/宽
    this.winw = window.innerWidth;//屏幕宽
    this.idx = 0;                 //索引初始化
    this.lis = this.outer.getElementsByTagName('li');//所有li
    var iw = this.outer.getElementsByTagName('img');//所有图片
    var uid = $('#input_hidden_uid').val();
    var lastIdx = -1;
    var storage = null;
    if (!supports_html5_storage()) {
    } else {
        storage = window.localStorage;
        var now = new Date().toLocaleDateString();
//        if (storage.getItem("likepic_" + now + "_" + uid) != null && storage.getItem("likepic_" + now + "_" + uid) != undefined && !isNaN(storage.getItem("likepic_" + now + "_" + uid))) {
//            lastIdx = parseInt(storage.getItem("likepic_" + now + "_" + uid));
//        }
    }
    for (i = 0; i < this.lis.length; i++) {
        var cid = $('ul.mi-friend-pic li').eq(i).attr('data-pic');
        if (storage.getItem("likedpic_" + uid + "_" + cid) != null && storage.getItem("likedpic_" + uid + "_" + cid) != undefined && !isNaN(storage.getItem("likedpic_" + uid + "_" + cid))) {
            $('ul.mi-friend-pic li').eq(i).remove();
        }
    }
    this.lis = this.outer.getElementsByTagName('li');//所有li
    for (i = 0; i < this.lis.length; i++) {                 //li初始化
        if (i == (lastIdx + 1)) {
            $('ul.mi-friend-pic li').eq(i).attr('name', 'current');
            var pic = $('ul.mi-friend-pic li').eq(i).find('img[name=image]').attr('data-image');
            $('ul.mi-friend-pic li').eq(i).find('img[name=image]').attr('src', pic);
            var userIcon = $('ul.mi-friend-pic li').eq(i).find('img[name=usericon]').attr('data-usericon');
            $('ul.mi-friend-pic li').eq(i).find('img[name=usericon]').attr('src', userIcon);
            $('ul.mi-friend-pic li').eq(i).find('p[name=profile-info]').attr("style", 'display:block;');
            var cid = $('ul.mi-friend-pic li').eq(i).attr('data-pic');
            var hasCheck;
            if (storage.getItem("likedpic_" + now + "_" + uid + "_" + cid) != null && storage.getItem("likedpic_" + now + "_" + uid + "_" + cid) != undefined && !isNaN(storage.getItem("likedpic_" + now + "_" + uid + "_" + cid))) {
                hasCheck = storage.getItem("likedpic_" + now + "_" + uid + "_" + cid);
                $('ul.mi-friend-pic li').eq(i).attr('check', hasCheck);
                if (hasCheck == 2) {
                    $('.mi-friend-yes').addClass('like');
                    $('.mi-friend-no').removeClass('like');
                } else if (hasCheck == 1) {
                    $('.mi-friend-yes').removeClass('like');
                    $('.mi-friend-no').addClass('like');
                } else {
                    $('.mi-friend-yes,.mi-friend-no').removeClass('like');
                }
            }
            this.idx = i;
        }
        this.lis[i].style.webkitTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
        this.lis[i].style.mozTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
        this.lis[i].style.msTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
        this.lis[i].style.oTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
    }
    for (a = 0; a < iw.length; a++) {               //图片初始化
        iw[a].style.width = '100%';
    }
}

Slider.prototype.bindDom = function () {
    var self = this;
    var scale = this.winw;
    var outer = this.outer;
    var list = outer.getElementsByTagName('li');
    var sp = this.sp;
    var btn = this.btn;
    //var li=sp.getElementsByTagName('li');
    var starthander = function (evt) {
        evt.preventDefault();
        self.startX = evt.touches[0].pageX;
        self.offsetX = 0;
        self.startTime = new Date() * 1;
    }
    var movehander = function (evt) {
//        evt.preventDefault();
        self.offsetX = evt.touches[0].pageX - self.startX;
        var p = self.idx - 1;
        var c = p + 3;
    }
    var endhander = function (evt) {
        var boundary = scale / 6;
        var endTime = new Date() * 1;
        var lis = outer.getElementsByTagName('li');
    }

    var tabLike = function () {
        timer = null;
        var lis = outer.getElementsByTagName('li');
        var l = lis.length;
        if (self.idx <= l - 1) {
            var logindomain = $('#input_hidden_logindomain').val();
            if (logindomain == null || logindomain == undefined || logindomain == 'client' || logindomain == '') {
                showLogin();
                return;
            }
            if ($(this).hasClass('mi-friend-btn-no')) {
                if ($('.mi-friend-yes').hasClass('like')) {
                    $('.mi-friend-no').removeClass('like');
                    btn.off('touchstart');
                    reportUnLike(btn, tabLike, self, self.idx);
                } else if ($('.mi-friend-no').hasClass('like')) {
                    $('.mi-friend-yes').removeClass('like');
                    btn.off('touchstart');
                    reportUnLike(btn, tabLike, self, self.idx);
                } else {
                    lis[self.idx].setAttribute('check', 1);
                    btn.off('touchstart');
                    reportUnLike(btn, tabLike, self, self.idx);
                }
            } else {
                if ($('.mi-friend-no').hasClass('like')) {
                    $('.mi-friend-yes').removeClass('like')
                    btn.off('touchstart');
                    reportLike(btn, tabLike, self, self.idx);
                } else if ($('.mi-friend-yes').hasClass('like')) {
                    $('.mi-friend-no').removeClass('like');
                    btn.off('touchstart');
                    reportLike(btn, tabLike, self, self.idx);
                } else {
                    lis[self.idx].setAttribute('check', 2);
                    btn.off('touchstart');
                    reportLike(btn, tabLike, self, self.idx);
                }
            }
        }
    };
    $.each(btn, function () {
        $(this).on('touchstart', tabLike)
    });
    outer.addEventListener('touchstart', starthander, false);
    outer.addEventListener('touchmove', movehander, false);
    outer.addEventListener('touchend', endhander, false);
};

Slider.prototype.go = function (idx, n) {
    var scale = this.winw;
    var cidx;
    var btn = this.btn;
    var lis = this.outer.getElementsByTagName('li');
    var iw = this.outer.getElementsByTagName('img');//所有图片
    //var li=this.sp.getElementsByTagName('li');
    var len = lis.length;
    if (idx == 0 && n == -1) {
    } else if (idx == len - 1 && n == 1) {
        var uid = $('#input_hidden_uid').val();
        var storage = window.localStorage;
        var now = new Date().toLocaleDateString();
        var num = parseInt(storage.getItem("likepic_" + now + "_" + uid));
        if (num >= '29') {
            $('div.mi_friend_tip span').html('今日血条已空，等你明日满血复活！');
            $('div.mi_friend_tip').show(100).delay(1800).hide(100);
        } else {
            location.reload();
//            var appkey = $("#input_hidden_appkey").val();
//            var platform = $("#input_hidden_platform").val();
//            var bool = true;
//            $.ajax({ url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/profile/likepageloading",
//                        type: "POST",
//                        timeout: 5000,
//                        data: {uid: uid,appkey: appkey, platform: platform},
//                        dataType: "json",
//                        success: function (result) {
//                            if (result.rs == '1') {
//
//                                var list = result.result.list;
//                                var lastIdx = parseInt($('ul.mi-friend-pic li:last').attr('data-idx'));
//                                var content = "";
//                                for (var i = 0; i < list.length; i++) {
//                                    content = "<li data-idx='" + (lastIdx + (i + 1)) + "' data-u='" + list[i].profiledto.uid + "' data-pici='" + list[i].picdto.picid + "' " +
//                                            "style='-webkit-transform: translate3d(0px, 0px, 0px); -webkit-transition: -webkit-transform 0.2s " +
//                                            "ease-out; transition: -webkit-transform 0.2s ease-out;'>" +
//                                            "  <a href='javascript:void(0);'><img data-image='" + list[i].picdto.picurl + "' style='width: 100%;'name='image'/>" +
//                                            " <p name='profile-info'style='display: none;'><cite class='fl'> <img name='usericon' src='" + list[i].profiledto.iconurl + "'" +
//                                            "alt='" + list[i].profiledto.iconurl + "' style='width: 100%;'/> </cite><span class='fl'><b>" + list[i].profiledto.nick + "</b><br>";
//                                    if (list[i].gamedto.name != null && list[i].gamedto.name != "") {
//                                        content += "<em>正在玩&nbsp;" + list[i].gamedto.name + "</em>"
//                                    } else {
//                                        content += "<em>游戏空窗期</em>"
//                                    }
//                                    content += " </p></a></li>";
//                                    $(".mi-friend-pic").append(content);
//                                }
//                                for (i = 0; i < lis.length; i++) {                 //li初始化
//                                    if (i == (lastIdx + 1)) {
//                                        $('ul.mi-friend-pic li').eq(i).attr('name', 'current');
//                                        var pic = $('ul.mi-friend-pic li').eq(i).find('img[name=image]').attr('data-image');
//                                        $('ul.mi-friend-pic li').eq(i).find('img[name=image]').attr('src', pic);
//                                        $('ul.mi-friend-pic li').eq(i).find('p[name=profile-info]').attr("style", 'display:block;');
//                                        var cid = $('ul.mi-friend-pic li').eq(i).attr('data-pic');
//                                        $('.mi-friend-yes,.mi-friend-no').removeClass('like');
//                                       this.idx = i;
//                                        alert("this.idx="+this.idx);
//                                    }
//                                    lis[i].style.webkitTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
//                                    lis[i].style.mozTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
//                                    lis[i].style.msTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
//                                    lis[i].style.oTransform = 'translate3d(' + (i - lastIdx - 1) * this.winw + 'px,0,0)';
//                                }
//                                for (var a = 0; a < iw.length; a++) {               //图片初始化
//                                    iw[a].style.width = '100%';
//                                }
//                            }
//                        }});
        }
    }
    lis = this.outer.getElementsByTagName('li');
    //var li=this.sp.getElementsByTagName('li');
    len = lis.length;
    if (typeof n == 'number') {
        cidx = n;
    } else if (typeof n == 'string') {
        cidx = idx + n * 1;
    }

    if (cidx > len - 1) {
        cidx = len - 1
    } else if (cidx < 0) {
        cidx = 0;
    }

    if (idx == cidx) {
    } else {
        lis[idx].removeAttribute('name');
        lis[cidx].setAttribute('name', 'current');
        var pic = $('ul.mi-friend-pic li').eq(cidx).find('img[name=image]').attr('data-image');
        $('ul.mi-friend-pic li').eq(cidx).find('img[name=image]').attr('src', pic);
        var userIcon = $('ul.mi-friend-pic li').eq(cidx).find('img[name=usericon]').attr('data-usericon');
        $('ul.mi-friend-pic li').eq(cidx).find('img[name=usericon]').attr('src', userIcon);
    }

    if (lis[cidx].getAttribute('check') == 2) {
        $('.mi-friend-yes').addClass('like');
        $('.mi-friend-no').removeClass('like');
    } else if (lis[cidx].getAttribute('check') == 1) {
        $('.mi-friend-yes').removeClass('like');
        $('.mi-friend-no').addClass('like');
    } else {
        $('.mi-friend-yes,.mi-friend-no').removeClass('like');
    }

    //li[cidx].style.background='#CC3300';
    lis[cidx].style.webkitTransition = '-webkit-transform 0.2s ease-out';
    lis[cidx - 1] && (lis[cidx - 1].style.webkitTransition = '-webkit-transform 0.2s ease-out');
    lis[cidx + 1] && (lis[cidx + 1].style.webkitTransition = '-webkit-transform 0.2s ease-out');
    lis[cidx].style.webkitTransform = 'translate3d(0,0,0)';
    lis[cidx - 1] && (lis[cidx - 1].style.webkitTransform = 'translate3d(-' + scale + 'px,0,0)');
    lis[cidx + 1] && (lis[cidx + 1].style.webkitTransform = 'translate3d(' + scale + 'px,0,0)');

    lis[cidx].style.mozTransition = '-moz-transform 0.2s ease-out';
    lis[cidx - 1] && (lis[cidx - 1].style.mozTransition = '-moz-transform 0.2s ease-out');
    lis[cidx + 1] && (lis[cidx + 1].style.mozTransition = '-moz-transform 0.2s ease-out');
    lis[cidx].style.mozTransform = 'translate3d(0,0,0)';
    lis[cidx - 1] && (lis[cidx - 1].style.mozTransform = 'translate3d(-' + scale + 'px,0,0)');
    lis[cidx + 1] && (lis[cidx + 1].style.mozTransform = 'translate3d(' + scale + 'px,0,0)');

    lis[cidx].style.msTransition = '-ms-transform 0.2s ease-out';
    lis[cidx - 1] && (lis[cidx - 1].style.msTransition = '-ms-transform 0.2s ease-out');
    lis[cidx + 1] && (lis[cidx + 1].style.msTransition = '-ms-transform 0.2s ease-out');
    lis[cidx].style.msTransform = 'translate3d(0,0,0)';
    lis[cidx - 1] && (lis[cidx - 1].style.msTransform = 'translate3d(-' + scale + 'px,0,0)');
    lis[cidx + 1] && (lis[cidx + 1].style.msTransform = 'translate3d(' + scale + 'px,0,0)');

    lis[cidx].style.oTransition = '-o-transform 0.2s ease-out';
    lis[cidx - 1] && (lis[cidx - 1].style.oTransition = '-o-transform 0.2s ease-out');
    lis[cidx + 1] && (lis[cidx + 1].style.oTransition = '-o-transform 0.2s ease-out');
    lis[cidx].style.oTransform = 'translate3d(0,0,0)';
    lis[cidx - 1] && (lis[cidx - 1].style.oTransform = 'translate3d(-' + scale + 'px,0,0)');
    lis[cidx + 1] && (lis[cidx + 1].style.oTransform = 'translate3d(' + scale + 'px,0,0)');

    $('ul.mi-friend-pic li').eq(cidx).find('p[name=profile-info]').attr("style", 'display:block;');

    this.idx = cidx;
//    alert("this.idx2=" + this.idx);
}

function supports_html5_storage() {
    try {
        return 'localStorage' in window && window['localStorage'] !== null;
    } catch (e) {
        return false;
    }
}

function reportLike(btn, tabLike, self, idx) {
//    alert(idx);
    var flag = false;
    var uid = $('#input_hidden_uid').val();
    var appkey = $('#input_hidden_appkey').val();
    var platform = $('#input_hidden_platform').val();
    var desUid = $('ul.mi-friend-pic li').eq(idx).attr('data-u');
    var storage = window.localStorage;
    var now = new Date().toLocaleDateString();
    var num = parseInt(storage.getItem("likepic_" + now + "_" + uid));

    if (num >= '29') {
        $('div.mi_friend_tip span').html('今日血条已空，等你明日满血复活！');
        $('div.mi_friend_tip').show(100).delay(1800).hide(100);
        btn.on('touchstart', tabLike);
        return;
    }

    //逻辑

    if (uid != null && desUid != null && uid != desUid) {
        var ajaxTimeoutTest = $.ajax({
            url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/profile/like",
            type: "POST",
            timeout: 5000,
            data: {uid: uid, desuid: desUid, appkey: appkey, platform: platform},
            dataType: "json",
            success: function (result) {
                if (result.rs == '1') {
//                            var lastIdx = parseInt($('ul.mi-friend-pic li:last').attr('data-idx'));
//                            var currentIdx = parseInt($('ul.mi-friend-pic li').eq(idx).attr('data-idx'));
//                            if (lastIdx == currentIdx) {
//                                currentIdx = lastIdx - 1;
//                            }
                    var hasCheck = $('ul.mi-friend-pic li').eq(idx).attr('check');
                    var cid = $('ul.mi-friend-pic li').eq(idx).attr('data-pic');

                    if (!supports_html5_storage()) {
                    } else {
                        if (storage.getItem("likepic_" + now + "_" + uid) != null && storage.getItem("likepic_" + now + "_" + uid) != undefined) {

                            storage.setItem("likepic_" + now + "_" + uid, num + 1);
                        } else {
                            storage.setItem("likepic_" + now + "_" + uid, 0);
                        }
                        if (storage.getItem('likedpic_' + now + "_" + uid + '_' + cid) != null || storage.getItem('likedpic_' + now + "_" + uid + '_' + cid) != undefined) {
                        } else {
                            $('.mi-friend-yes').addClass('like');
                            storage.setItem('likedpic_' + now + "_" + uid + '_' + cid, hasCheck);
                            storage.setItem('likedpic_' + uid + '_' + cid, hasCheck);
                        }
                    }

                    timer = setTimeout(function (idx) {
                        clearInterval(timer);
                        self.go(self.idx, '1');
                        btn.on('touchstart', tabLike);
                        var desUid = $('ul.mi-friend-pic li').eq(self.idx).attr('data-u');
                        jump(29, desUid);
                        return;
                    }, 1000);
                } else {
                    $('div.mi_friend_tip span').html('么么哒失败，请重试');
                    $('div.mi_friend_tip').show(100).delay(1800).hide(100);
                    btn.on('touchstart', tabLike);
                    return;
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $('div.mi_friend_tip span').html('么么哒失败，请重试');
                $('div.mi_friend_tip').show(100).delay(1800).hide(100);
                btn.on('touchstart', tabLike);
                return;
            }
        });
    }
}

function reportUnLike(btn, tabLike, self, idx) {
//    alert(idx);
    var uid = $('#input_hidden_uid').val();
    var storage = window.localStorage;
    var now = new Date().toLocaleDateString();
    var num = parseInt(storage.getItem("likepic_" + now + "_" + uid));

    if (num >= '29') {
        $('div.mi_friend_tip span').html('今日血条已空，等你明日满血复活！');
        $('div.mi_friend_tip').show(100).delay(1800).hide(100);
        btn.on('touchstart', tabLike);
        return;
    }
    //逻辑
    var ajaxTimeoutTest = $.ajax({
        url: "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/api/profile/unlike",
        type: "POST",
        data: {},
        timeout: 5000,
        dataType: "json",
        success: function (result) {

//                    var lastIdx = parseInt($('ul.mi-friend-pic li:last').attr('data-idx'));
//                    var currentIdx = parseInt($('ul.mi-friend-pic li').eq(idx).attr('data-idx'));
//                    if (lastIdx == currentIdx) {
//                        currentIdx = lastIdx - 1;
//                    }
            var hasCheck = $('ul.mi-friend-pic li').eq(idx).attr('check');
            var cid = $('ul.mi-friend-pic li').eq(idx).attr('data-pic');
            var storage = null;
            if (!supports_html5_storage()) {
            } else {
                storage = window.localStorage;
                var now = new Date().toLocaleDateString();
                if (storage.getItem("likepic_" + now + "_" + uid) != null && storage.getItem("likepic_" + now + "_" + uid) != undefined) {
                    var num = parseInt(storage.getItem("likepic_" + now + "_" + uid));
                    storage.setItem("likepic_" + now + "_" + uid, num + 1);
                } else {
                    storage.setItem("likepic_" + now + "_" + uid, 0);
                }

                if (storage.getItem('likedpic_' + now + "_" + uid + '_' + cid) != null || storage.getItem('likedpic_' + now + "_" + uid + '_' + cid) != undefined) {
                } else {
                    $('.mi-friend-no').addClass('like');
                    storage.setItem('likedpic_' + now + "_" + uid + '_' + cid, hasCheck);
                    storage.setItem('likedpic_' + uid + '_' + cid, hasCheck);
                }
            }
            timer = setTimeout(function (idx) {
                clearInterval(timer);
                self.go(self.idx, '1');
                btn.on('touchstart', tabLike);
                return;
            }, 1000);
        },
        error: function () {
            $('div.mi_friend_tip span').html('么么哒失败，请重试');
            $('div.mi_friend_tip').show(100).delay(1800).hide(100);
            btn.on('touchstart', tabLike);
            return;
        }
    });
}