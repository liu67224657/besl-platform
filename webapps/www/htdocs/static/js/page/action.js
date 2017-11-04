document.onscroll = function () {
    var top = document.body.scrollTop;
    var returnbtn = document.getElementById('returntop');
    if (!returnbtn) return;
    if (top > 350) {
        if (returnbtn.style.display != 'block') {
            returnbtn.style.display = 'block';
        }
    } else {
        returnbtn.style.display = 'none';
    }

    if (document.getElementById('tc-form') && document.getElementById('tc-form').style.display === 'block') {
        returnbtn.style.display = 'none';
    }
};

function returnTop(ev) {
    var ev = ev || window.event;
    document.body.scrollTop = 0;
    document.getElementById('returntop').style.display = 'none';
    ev.preventDefault();
    ev.stopPropagation();
    return false;
}

function tc() {
    var a = document.getElementById('tc-place');
    var b = document.getElementById('tc-form');
    var c = document.getElementById('tc-btn');
    var d = document.getElementById('tc-textarea');
    var e = document.getElementById('tc-cancel');
    var f = document.getElementById('tc-submit');
    var g = document.getElementById('tc-layer');
    var h = document.getElementById('body-wrapper');

    touch.on(c, 'tap', function (ev) {
        a.style.display = 'none';
        b.style.display = 'block';
        h.style.display = 'none';
        g.style.display = 'block';
        d.setAttribute('autofocus', 'autofocus');
        try {
            document.getElementById('tc-print').style.display = 'none';
        } catch (e) {
        }
    });

//    touch.on(e, 'tap', function (ev) {
//        ev.preventDefault();
//        ev.stopPropagation();
//        a.style.display = 'block';
//        h.style.display = 'block';
//        b.style.display = 'none';
//        g.style.display = 'none';
//        d.blur();
//        try{
//            document.getElementById('tc-print').style.display = 'block';
//        }catch (e){}
//        return false;
//    });

    e.onclick = function (ev) {
        a.style.display = 'block';
        h.style.display = 'block';
        b.style.display = 'none';
        g.style.display = 'none';
        d.blur();
        try {
            document.getElementById('tc-print').style.display = 'block';
        } catch (e) {
        }
        ;
        var ev = ev || window.event;
        ev.preventDefault();
        ev.stopPropagation();
        return false;
    }

    touch.on(f, 'tap', function (ev) {
        //document.getElementById('tc-formtable').submit();
        /*
         * 发送吐槽内容
         */
        h.style.display = 'block';
        b.style.display = 'none';
        a.style.display = 'block';
        g.style.display = 'none';
    });
}

function fx() {
    var a = document.getElementById('tc-place');
    var b = document.getElementById('tx-btn');
    var c = document.getElementById('close-share');
    var d = document.getElementById('tc-share');
    var e = document.getElementById('body-wrapper');

    touch.on(b, 'tap', function () {
        a.style.display = 'none';
        //e.style.display = 'none';
        d.style.display = 'block';
        try {
            document.getElementById('tc-print').style.display = 'none';
        } catch (e) {
        }
    });

    touch.on(c, 'tap', function (ev) {
        a.style.display = 'block';
        e.style.display = 'block';
        d.style.display = 'none';
        try {
            var ev = ev || window.event;
            ev.preventDefault();
            ev.stopPropagation();
            document.getElementById('tc-print').style.display = 'block';
        } catch (e) {
        }
        ;
        return false;
    });

}

function set_score() {
    var a = document.getElementById('set-score');
    var b = a.getElementsByTagName('span');
    for (var i = 0; i < b.length; i++) {
        b[i].idx = i;
    }

    touch.on(a, 'tap', function (ev) {
        var t = ev.target;
        var idx = t.idx;
        /*
         * idx 就是当前用户的打分
         */
        if (t.nodeName.toLowerCase() === 'span') {
            for (var j = 0; j < b.length; j++) {
                if (j <= idx) {
                    b[j].className = 'set';
                } else {
                    b[j].className = '';
                }
            }
        }

        window.score = idx;
    });
}

function tc_input() {
    var a = document.getElementById('tc-textarea');
    var b = document.getElementById('char-num');
    var c = parseInt(b.innerHTML);
    var d = function (str) {
        return String(str).replace(/[^\x00-\xff]/g, 'aa').length
    };
    var tmp = '';
    var e =document.getElementById('tc-submit');

    a.addEventListener('input', function (ev) {
        var t = this.value;
        var l = Math.ceil(d(t) / 2);

        if (l <= c) {
            b.innerHTML = c - l;
            tmp = t;
            e.style.opacity="1";
            if (l === 0) {
                e.style.opacity="0.5";
            }
        } else {
            this.value = tmp;
            e.style.opacity="0.5";
        };

    }, false)
}

function tc_print(msg) {
    var timer = null;
    var idx = 0;
    var tag = [];
    var top = 0;

    var div1 = document.createElement('div');
    div1.id = 'tc-print';

    var div2 = document.createElement('div');
    div2.id = 'tc-print-box';

    for (var i = 0; i < msg.length; i++) {
        if (msg[i].special === true) {
            tag.push('<div class="open"><img src="' + msg[i].header + '"><p>' + msg[i].msg + '</p></div>');
        } else {
            tag.push('<div><img src="' + msg[i].header + '"><p>' + msg[i].msg + '</p></div>');
        }
    }

    function pushToDom() {
        div2.innerHTML = '';
        tag = tag.concat(tag);
        div2.innerHTML = tag.join('');
    }

    function roll() {
        var c = div2.getElementsByTagName('div');

        if (idx === tag.length - 4) {
            pushToDom();
        }

        top += c[idx].offsetHeight + 10;

        div2.style.webkitTransform = 'translate3d(0, -' + top + 'px, 0)';

        idx++;

    };

    pushToDom();
    div1.appendChild(div2);
    document.body.appendChild(div1);

    timer = setInterval(function () {
        roll()
    }, 2000);

    document.getElementById('tc-shut').addEventListener('touchstart', function (ev) {
        if (this.className !== 'close') {
            clearInterval(timer);
            this.className = 'close';
            div1.style.width = 0;
        } else {
            this.className = '';
            timer = setInterval(function () {
                roll()
            }, 2000);
            div1.style.width = '';
        }
    }, false)

}

function d_p_tab() {
    var span = document.getElementById('d-p-tab').getElementsByTagName('span');

    span[0].addEventListener('touchstart', function () {
        document.getElementById('iphone').style.display = 'block';
        document.getElementById('ipad').style.display = 'none';
        span[1].className = '';
        this.className = 'sel';
    }, false);

    span[1].addEventListener('touchstart', function () {
        document.getElementById('iphone').style.display = 'none';
        document.getElementById('ipad').style.display = 'block';
        span[0].className = '';
        this.className = 'sel';
    }, false);
}

function contribution() {
    document.getElementById('tg-btn').addEventListener('touchstart', function () {
        document.getElementById('tg-layer').style.display = 'block';
    }, false);
    document.getElementById('tg-close-btn').addEventListener('click', function (ev) {
        var ev = ev || window.event;
        ev.preventDefault();
        ev.stopPropagation();
        document.getElementById('tg-layer').style.display = 'none';
        return false;
    }, false);
}


$.cookie = function (name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options = $.extend({}, options);
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};
//点赞
function like(replyId, type, url) {
    var a = $("#like" + replyId).attr("class");
    var b = $("#dislike" + replyId).attr("class");
    if (a == "sel" || b == 'sel') {
        alert("您已发表过态度");
        return;
    }


    $.ajax({
        url: url + '/json/comment/mobile/agree',
        type: 'post',
        async: false,
        data: {'rid': replyId, 'type': type},
        dataType: "json",
        success: function (data) {
            if (data.rs == '1') {
                if (type == 1) {
                    var likenum = $("#like" + replyId).html();
                    likenum = parseInt(likenum) + parseInt(1);
                    $("#like" + replyId).html(likenum);
                    $("#like" + replyId).addClass("sel");
                } else {
                    var likenum = $("#dislike" + replyId).html();
                    likenum = parseInt(likenum) + parseInt(1);
                    $("#dislike" + replyId).html(likenum);
                    $("#dislike" + replyId).addClass("sel");

                }
            } else {
            }
        }
    });
}

function loaddingCategory() {

    $("#categoryLoadding").unbind("click");
    var curNum = $("#curpage").val();
    var maxNum = $("#maxpage").val();
    var categoryId = $("#categoryId").val();
//   alert(joyconfig.URL_WWW);
    if (parseInt(maxNum) <= parseInt(curNum)) {
        return;
    }
    curNum = parseInt(curNum) + parseInt(1);
    $.ajax({ type: "POST",
        url: joyconfig.URL_WWW + "/json/comment/mobile/categorylist",
        data: {p: curNum, categoryId: categoryId},
        dataType: "json",
        success: function (data) {
            var page = data.result.page;
            var dto = data.result.dto;
            if (page.curPage >= page.maxPage) {
                $("#categoryLoadding").css("display", "none");
            }
            for (var i = 0; i < dto.gamedtolist.length; i++) {
                var str = '<div class="list" id="categoryinfo' + dto.gamedtolist[i].gamedbid + '">'
                    + '<a class="game-info" href=' + joyconfig.URL_WWW + '"/mobilegame/comment/' + dto.gamedtolist[i].gamedbid + '?lineid=' + dto.gamedtolist[i].lineid + '&contentid=' + dto.gamedtolist[i].contentid + '">'
                    + ' <img src="' + dto.gamedtolist[i].gameicon + '">'
                    + '<div>' +
                    '<div class="h2">' + dto.gamedtolist[i].gamename + '</div>' +
                    ' <p>' +
                    '<code>' + dto.gamedtolist[i].replynum + '评</code>' +
                    '<span class="score-1"><i style="width:' + dto.gamedtolist[i].average_score / 10 * 100 + '%"></i></span>' +
                    '<em>' + dto.gamedtolist[i].average_score + '</em>' +
                    '</p>' +
                    '</div>' +
                    '</a>' +
                    '</div>';
                var strappend = '';
                for (var j = 0; j < dto.gamedtolist[i].shortcommentlist.length; j++) {
                    strappend += '<div class="user-dp">'
                        + ' <img src="' + dto.gamedtolist[i].shortcommentlist[j].header + '">'
                        + '<div>' + dto.gamedtolist[i].shortcommentlist[j].msg + '</div>'
                        + '</div>';
                    $(str).append(strappend);
                }
                $("#listbox").append(str);
                $("#categoryinfo" + dto.gamedtolist[i].gamedbid).append(strappend);
            }
            $("#curpage").val(page.curPage);
            $("#maxpage").val(page.maxPage);
            var loading = document.getElementById('categoryLoadding'),
                icon = loading.getElementsByTagName('i')[0],
                txt = loading.getElementsByTagName('b')[0];
            icon.style.display = 'none';
            txt.innerHTML = '加载更多';
            $("#categoryLoadding").click(function () {
                loaddingCategory();
            });
        }
    });
}
$("#categoryLoadding").click(function () {
    loaddingCategory();
});

//index
function reloadtc_print(header, msg) {
    $("#tc-print").remove()
    var _obj = {
        'header': header,
        'msg': msg
    };
    tcMsg.push(_obj);
    tc_print(tcMsg); // 显示弹幕
}