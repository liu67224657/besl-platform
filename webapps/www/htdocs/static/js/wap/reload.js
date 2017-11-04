//下拉
function pullDownAction(retype) {
    setTimeout(function () {
        var uno = $("[name='uno']").val();
        var appkey = $("[name='appkey']").val();

//        window.location.reload();
//        myScroll.refresh();	//加载完成后，重新刷新一次加载内容
//        myScroll2.refresh();
        window.location.href = "/joymeapp/gameclient/webview/giftmarket/list?uno=" + uno + "&appkey=" + appkey + "&retype=" + retype;

    }, 1000);

}
//上拉
function pullUpAction(config) {
    var i,el,alist;

    var appkey = $("[name='appkey']").val();
    var profileId = $("[name='profileId']").val();
    var uno = $("[name='uno']").val();
    if (config.objId == 'libao-Main') {
        var curNum = $("#giftWapCurPage").val();
        var maxNum = $("#giftWapMaxPage").val();
        if (parseInt(maxNum) <= parseInt(curNum)) {
            document.getElementById("libao-pullUp").querySelector('.pullUpLabel').innerHTML = '没有了╮(╯_╰)╭';
            document.getElementById("libao-pullUp").className = '';
            return;
        }
        curNum = parseInt(curNum) + parseInt(1);
        $.ajax({
                    type: "POST",
                    url: "/json/gameclient/webview/giftmarket/newsclientgiftlist",
                    data: {count: 15, pnum: curNum, profileid: profileId, appkey: appkey},
                    success: function (req) {
                        var result = eval('(' + req + ')');
                        $("#giftWapCurPage").val(result.result.page.curPage);
                        $("#giftWapMaxPage").val(result.result.page.maxPage);
                        for (var index = 0; index < result.result.rows.length; index++) {
                            var content = "<a href='http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/giftmarket/giftdetail?appkey=" + appkey + "&profileid=" + profileId + "&aid=" + result.result.rows[index].gid + "&type=1&uno=" + uno + "'><dt class='fl'>";
                            if (result.result.rows[index].reserveType == 1) {
                                content += "<cite class='yyz'>预约中</cite>"
                            } else {
                                if (result.result.rows[index].weixinExclusive == 2) {
                                    content += "<cite class='dj'>独家</cite>"
                                }
                            }
                            content += "<p><img src='" + result.result.rows[index].gipic + "'></p></dt>" +
                                    " <dd class='fl'><div class='fl'><h1 class='cut_out2'>" + result.result.rows[index].title + "</h1> <span> " + result.result.rows[index].desc + "<b>&nbsp;</b></span> <span><b>&nbsp;</b></span></div>";
                            if (result.result.rows[index].reserveType == 1) {
                                content += " <span class='min_btn yy'>约</span>";
                            } else {
                                if (result.result.rows[index].sn == 0) {
                                    content += "<span class='min_btn th'>淘</span>";
                                } else {
                                    content += "<span class='min_btn lh'>领</span>";
                                }
                            }
                            content += " </dd></a>";
                            alist = document.createElement(config.type);
                            alist.innerHTML = content; // + (++generatedCount);
                            alist.className = config.getClassName;

                            var hotactivity = result.result.rows[index].hotActivity;
                            if (hotactivity == 0) {
                                el = document.getElementById("libao-Main");
                                el.appendChild(alist, el.childNodes[0]);
                            } else {
                                el = document.getElementById("hot-Main");
                                el.appendChild(alist, el.childNodes[0]);
                            }
                        }
                        myScroll.refresh();	//加载完成后，重新刷新一次加载内容
                    }
                });

    } else {
        alert("迷豆 ");
    }
//    for (i = 0; i < 4; i++) {
//
//        alist = document.createElement(config.type);
//        alist.innerHTML = config.data; // + (++generatedCount);
//        alist.className = config.getClassName;
//        el.appendChild(alist, el.childNodes[0]);
//    }

}

//判断动作
function loaded(config) {
    var pullDownEl, pullDownOffset, pullUpEl, pullUpOffset,generatedCount = 0;
    pullDownEl = document.getElementById(config.pullDown);
    pullDownOffset = $("#" + config.pullDown).height();

    pullUpEl = document.getElementById(config.pullUp);
    pullUpOffset = $("#" + config.pullUp).height();
    myScroll = new iScroll(config.wrapper, {
                useTransition: true,
                topOffset: pullDownOffset,
                onRefresh: function () {
                    if (pullDownEl.className.match('loading')) {
                        pullDownEl.className = '';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开手刷新╮(╯_╰)╭';
                    } else if (pullUpEl.className.match('loading')) {
                        pullUpEl.className = '';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '下拉加载更多..';
                    }
                },
                onScrollMove: function () {
                    if (this.y > 5 && !pullDownEl.className.match('flip')) {
                        pullDownEl.className = 'flip';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开手刷新╮(╯_╰)╭';
                        this.minScrollY = 0;
                    } else if (this.y < 5 && pullDownEl.className.match('flip')) {
                        pullDownEl.className = '';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '快使劲拉我O(∩_∩)O';
                        this.minScrollY = -pullDownOffset;
                    } else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
                        pullUpEl.className = 'flip';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '下拉加载更多..';
                        this.maxScrollY = this.maxScrollY;
                    } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
                        pullUpEl.className = '';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '下拉加载更多..';
                        this.maxScrollY = pullUpOffset;
                    }
                },
                onScrollEnd: function () {
                    if (pullDownEl.className.match('flip')) {
                        pullDownEl.className = 'loading';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '刷新中..';
                        pullDownAction('gift');
                    } else if (pullUpEl.className.match('flip')) {
                        pullUpEl.className = 'loading';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '努力加载中...';
                        //加载数据
                        pullUpAction({
                                    objId:config.Main,
                                    type:config.type,
                                    data:config.data,
                                    getClassName:config.getClassName
                                });
                    }
                }
            });
    myScroll.refresh();
    setTimeout(function () {
        document.getElementById(config.wrapper).style.left = '0';
    }, 500);
}

function loaded2(config) {
    var pullDownEl, pullDownOffset, pullUpEl, pullUpOffset,generatedCount = 0;
    pullDownEl = document.getElementById(config.pullDown);

    pullDownOffset = $("#" + config.pullDown).height();
    pullUpEl = document.getElementById(config.pullUp);
    pullUpOffset = $("#" + config.pullUp).height();

    myScroll2 = new iScroll(config.wrapper, {
                useTransition: true,
                topOffset: pullDownOffset,
                onRefresh: function () {
                    if (pullDownEl.className.match('loading')) {
                        pullDownEl.className = '';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开手刷新╮(╯_╰)╭';
                    } else if (pullUpEl.className.match('loading')) {
                        pullUpEl.className = '';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '下拉加载更多..';
                    }
                },
                onScrollMove: function () {
                    if (this.y > 5 && !pullDownEl.className.match('flip')) {
                        pullDownEl.className = 'flip';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '松开手刷新╮(╯_╰)╭';
                        this.minScrollY = 0;
                    } else if (this.y < 5 && pullDownEl.className.match('flip')) {
                        pullDownEl.className = '';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '快使劲拉我O(∩_∩)O';
                        this.minScrollY = -pullDownOffset;
                    } else if (this.y < (this.maxScrollY - 5) && !pullUpEl.className.match('flip')) {
                        pullUpEl.className = 'flip';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '下拉加载更多..';
                        this.maxScrollY = this.maxScrollY;
                    } else if (this.y > (this.maxScrollY + 5) && pullUpEl.className.match('flip')) {
                        pullUpEl.className = '';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '下拉加载更多..';
                        this.maxScrollY = pullUpOffset;
                    }
                },
                onScrollEnd: function () {
                    if (pullDownEl.className.match('flip')) {
                        pullDownEl.className = 'loading';
                        pullDownEl.querySelector('.pullDownLabel').innerHTML = '刷新中..';
                        pullDownAction('midou');
                    } else if (pullUpEl.className.match('flip')) {
                        pullUpEl.className = 'loading';
                        pullUpEl.querySelector('.pullUpLabel').innerHTML = '努力加载中...';
                        //加载数据
                        pullUpAction({
                                    objId:config.Main,
                                    type:config.type,
                                    data:config.data,
                                    getClassName:config.getClassName
                                });
                    }
                }
            });


    setTimeout(function () {
        document.getElementById(config.wrapper).style.left = '0';
    }, 500);
}
//礼包
function lbLoad() {
    setTimeout(loaded({
                wrapper:'libao-wrap',
                pullDown:'libao-pullDown',
                pullUp:'libao-pullUp',
                Main:'libao-Main',
                type:'dl',
                getClassName:'',
                data:'<a href="#"><dt class="fl"><cite class="yyz">预约中</cite><p><img src="images/title.png" alt=""></p></dt><dd class="fl"><div class="fl"><h1>我叫MT2</h1><span>钻石：<b>288</b></span><span>金币：<b>8888</b></span><span class="lb-mian-surplus">剩余：<b>7/8</b></span></div><span href="#" class="min_btn yy">约</span></dd></a>'
            }), 200);
}
//迷豆
function mdLoad() {

    setTimeout(loaded2({
                wrapper:'midou-wrap',
                pullDown:'midou-pullDown',
                pullUp:'midou-pullUp',
                Main:'midou-main',
                type:'div',
                data:'<a href="#"><cite><img src="images/picture.jpg" class="shop-img" alt=""></cite><div class="shop-box-text"><h2>《怪物x联盟》扑克</h2><h3><span>30</span>迷豆</h3></div><div class="shop-btn s1">奖品有限，速来参加</div></a>',
                getClassName:'shop-box'
            }), 200);
}
document.addEventListener('touchmove', function(e) {
    e.preventDefault();
}, false);
document.addEventListener('DOMContentLoaded', function() {
    mdLoad();
    lbLoad()
}, false);

