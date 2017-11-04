<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta name="wap-font-scale" content="no">
    <title>${activity.title}</title>
    <link rel="stylesheet" href="http://static.${DOMAIN}/app/wanba/point/css/common.css?v=20161222">
    <link rel="stylesheet" href="http://static.${DOMAIN}/app/wanba/point/css/activity.css">
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
    </script>
</head>
<body>
<div class="advertising">
    <div class="adver-con">
        <a href="javascript:;">
            <img src="http://static.joyme.alpha/app/wanba/point/images/wanba_bottom_download.png">
        </a>
    </div>
</div>
<div class="open-in-browser">
    <span class="browser-warn"></span>
</div>

<div id="wrapper">
    <div class="banner">
        <cite><img src="${activity.pic}?imageView/2/w/1000"></cite>
        <font class="limit">${activity.title}</font>
    </div>
    <!-- 活动介绍 开始 -->
    <div class="acti-des pag-hor-15">
        <h2><i></i>活动介绍</h2>

        <p>${activity.desc}</p>
    </div>
    <!-- 活动介绍 结束 -->


    <c:if test="${activity!=null  && activity.profiledtos.size()>0}">
        <!-- 活动嘉宾 开始 -->
        <div class="acti-guest border-bot">
            <h2>活动嘉宾</h2>

            <div class="guest-cont ">
                <div class="guest-list-box">
                    <div class="guest-list fn-clear">
                        <c:forEach items="${activity.profiledtos}" var="dto" varStatus="st">
                            <a href="jmwanba://jt=61&ji=${activity.tagid}">
							<span>
                                <c:choose>
                                    <c:when test="${not empty dto.icon}">
                                        <img src="${dto.icon}">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${URL_LIB}/hotdeploy/static/img/wanba_default.png">
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${dto.vtype>0}">
								    <i></i>
                                </c:if>
							</span>
                                <font class="limit">${dto.nick}</font>
                                <b class="limit-line">${dto.vdesc}</b>
                                <em>问TA</em>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <!-- 活动嘉宾 结束 -->
    <!-- 问答列表 开始 -->
    <c:if test="${rows!=null && rows.size()>0}">
        <div class="question-list list-item">
            <c:forEach items="${rows}" var="dto">
                <a href="http://api.${DOMAIN}/wanba/webview/ask/share/qdetail?qid=${dto.question.question.questionid}&appkey=3iiv7VWfx84pmHgCUqRwun" class="border-bot">
                    <div class="ques-top fn-clear">
                        <span class="fn-l ques-vector"><i></i>${dto.question.answerprofile.nick} 向 ${dto.question.gametag.tagname} 提问</span>
                        <span class="fn-r ques-follow">${dto.question.question.followsum}人关注</span>
                    </div>
                    <p>${dto.question.question.title}</p>

                    <div class="ques-bot fn-clear">
                        <span class="fn-l que-time">${dateutil:parseWanbaDate(dto.question.question.questiontime)}</span>
                        <c:if test="${dto.question.question.accepaid>0}">
                            <span class="fn-r que-solve">已解决</span>
                        </c:if>
                        <c:if test="${dto.question.question.accepaid<=0}">
                            <span class="fn-r que-unsolve">未解决</span>
                        </c:if>
                    </div>
                </a>
            </c:forEach>

        </div>
    </c:if>
    <!-- 问答列表 结束 -->
</div>
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    function getDateDiff(dateTimeStamp) {
        var minute = 1000 * 60;
        var hour = minute * 60;
        var day = hour * 24;
        var now = new Date().getTime();
        var diffValue = now - dateTimeStamp;
        if (diffValue < 0) {
            return;
        }
        var dayC = diffValue / day;
        var hourC = diffValue / hour;
        var minC = diffValue / minute;
        if (dayC >= 1) {
            result = new Date(dateTimeStamp).Format("yyyy-MM-dd");
        } else if (hourC >= 1) {
            result = "" + parseInt(hourC) + "小时前";
        } else if (minC >= 1) {
            result = "" + parseInt(minC) + "分钟前";
        }
        return result;
    }
    var pnum = 1;
    fnMenu = {
        'isLoading': false,
        'isLastpage': false,
        'loadTime': null,
        // 动态获取滚动区域元素宽度 （例：活动嘉宾区域宽度）
        getWidth: function (config) {
            var widthBox = config.widthBox,
                    widthEle = config.widthEle,
                    OneW = widthEle.outerWidth(true),
                    len = widthEle.length,
                    factW = OneW * len;
            widthBox.width(factW);
        },
        loadComment: function (config) {

            $.ajax({
                url: 'http://api.' + joyconfig.DOMAIN + '/wanba/api/activity/listbytag',
                data: {tagid: '62', pnum: (pnum + 1)},
                type: 'POST',
                dataType: "json",
                success: function (req) {
                    pnum = pnum + 1;
                    if (req.result.page.lastPage == true) {
                        fnMenu.isLastpage = true;
                    }
                    if (req.rs == "1") {
                        var str = "";
                        for (var i = 0; i < req.result.rows.length; i++) {
                            var obj = req.result.rows[i];
                            str += '<a href="http://api.${DOMAIN}/wanba/webview/ask/share/qdetail?appkey=3iiv7VWfx84pmHgCUqRwun&qid='+obj.question.questionid+'" class="border-bot">';
                            str += '<div class="ques-top fn-clear">';
                            str += '<span class="fn-l ques-vector"><i></i>' + obj.question.answerprofile.nick + ' 向 ' + obj.question.gametag.tagname + ' 提问</span>';
                            str += '<span class="fn-r ques-follow">' + obj.question.question.followsum + '人关注</span>';
                            str += '</div>';
                            str += '<p>' + obj.question.question.title + '</p>';
                            str += '<div class="ques-bot fn-clear">';
                            str += '<span class="fn-l que-time">' + getDateDiff(obj.question.question.questiontime) + '</span>';
                            if (obj.question.question.accepaid > 0) {
                                str += '<span class="fn-r que-solve">已解决</span>';
                            } else {
                                str += '<span class="fn-r que-unsolve">未解决</span>';
                            }
                            str += '</div>';
                            str += '</a>';
                        }
                        $('.' + config).append(str);
                    }
                    clearTimeout(fnMenu.loadTime);
                    $('.loading').remove();
                    fnMenu.isLoading = false;
                }, error: function () {
                    fnMenu.isLoading = false;
                    clearTimeout(fnMenu.loadTime);
                    $('.loading').remove();
                }
            })


        },
        //滚动加载
        scroll_load: function (parentBox, className) {
            var className = className, parentBox = parentBox;
            $("#" + parentBox).scroll(function (ev) {
                ev.stopPropagation();
                ev.preventDefault();
                var sTop = $("#" + parentBox)[0].scrollTop + 5;
                var sHeight = $("#" + parentBox)[0].scrollHeight;
                var sMainHeight = $("#" + parentBox).height();
                var sNum = sHeight - sMainHeight;
                var loadTips = "";
                if (fnMenu.isLastpage && !fnMenu.isLoading) {
                    fnMenu.isLoading = true;
                    loadTips = '<div class="loading"><span>没有更多了</span></div>';
                    $('.' + className).append(loadTips);
                    fnMenu.loadTime = setTimeout(function () {
                        fnMenu.isLoading = false;
                        clearTimeout(fnMenu.loadTime);
                        $('.loading').remove();
                    }, 2000);
                    return;
                } else {
                    loadTips = '<div class="loading"><span><i></i>正在加载...</span></div>';
                }
                if (sTop >= sNum && !fnMenu.isLoading) {
                    fnMenu.isLoading = true;
                    $('.' + className).append(loadTips);
                    fnMenu.loadTime = setTimeout(function () {
                        fnMenu.loadComment(className);
                    }, 500);
                }
                ;
            });
        },
        // 倒计时
        timeOut: function (parent, time) {
            var parents = parent;
            var timeNum = time;
            var times = null;
            var hourbox = parents.find('.hour'),
                    minutebox = parents.find('.minute');
            secondbox = parents.find('.second');
            function zero(num) {
                // console.log(num)
                if (num < 10) {
                    return '0' + num;
                } else if (num >= 10) {
                    return '' + num;
                }
            };
            times = setInterval(function () {
                checkTime();
            }, 1000);
            function checkTime() {
                var future = Date.parse(timeNum);
                var now = new Date();
                var nowTime = now.getTime();
                var mistiming = (future - nowTime) / 1000,
                        h = zero(parseInt((mistiming) / 60 / 60 % 24)),
                        f = zero(parseInt((mistiming / 60) % 60)),
                        m = zero(parseInt(mistiming % 60));

                getTimes(h, f, m);
                if (future < nowTime) {
                    clearInterval(times);
                    parents.find('code').html('00');
                    return false;
                }
                ;
            };

            function getTimes(h, f, m) {
                hourbox.html(h);
                minutebox.html(f);
                secondbox.html(m)
            };
        },
        oAudio: function () {
            var audioBox = $('.audio-box'),
                    audioEle = audioBox.find('audio');
            inta = true;
            audioBox.click(function(){
                var $this = $(this),
                        actAudio = $this.find('audio')[0];
                audioEle.parent('.audio-box').find('i').removeClass('on');
                audioEle.each(function(){
                    var eventTester = function(e){
                        actAudio.addEventListener(e,function(){
                            $(actAudio).parent('.audio-box').find('i').removeClass('on');
                        },false);
                    }
                    eventTester("ended");
                    if(this == actAudio){
                        if(this.paused){
                            actAudio.load();
                            this.play();
                            $(actAudio).parent('.audio-box').find('i').addClass('on');
                        }else{
                            audioEle.parent('.audio-box').find('i').removeClass('on');
                            this.pause();
                        }
                    }else{
                        this.pause();
                    }
                });
            });
        },

        int: function (parent, time) {
            fnMenu.getWidth({widthBox: $('.guest-list '), widthEle: $('.guest-list a')});
            window.onload = function () {
                fnMenu.scroll_load('wrapper', 'list-item');
            };
            fnMenu.oAudio();
        }
    }
    fnMenu.int();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/wap/wanba/share.js"></script>
</body>
</html>
