<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device.width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta http-equiv="Cache-Control" content="no-Cache"/>
    <meta http-equiv="Cache-Control" content="max-age=0"/>
    <title>${title}</title>
    <link href="${URL_LIB}/static/theme/wap/css/wanba/common.css" rel="stylesheet"
          type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wanba/demo.css" rel="stylesheet"
          type="text/css">
    <link href="http://static.joyme.com/app/wanba/css/details.css" rel="stylesheet"
          type="text/css">
    <script src="${URL_LIB}/static/js/common/zepto.min.js"></script>
    <script src="${URL_LIB}/static/js/common/iscroll.js"></script>
    <%--<script src="${URL_LIB}/static/js/common/isload.js"></script>--%>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        var browser = {
            versions: function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                return {//移动终端浏览器版本信息
                    trident: u.indexOf('Trident') > -1, //IE内核
                    presto: u.indexOf('Presto') > -1, //opera内核
                    webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                    gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                    mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
                    ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
                    iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
                    iPad: u.indexOf('iPad') > -1, //是否iPad
                    webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
                };
            }(),
            language: (navigator.browserLanguage || navigator.language).toLowerCase()
        }
        $(document).ready(function() {
            <c:forEach items="${commentHistories}" var="item">
            var commentId = '${item.objectId}';
            $("[name='" + commentId + "']").each(function() {
                $(this).addClass("active");
                $(this).attr("href", "javascript:void(0);");
            });

            </c:forEach>

            $(".large-load").click(function() {
                var img = $('.mi-tit-box').find('em').children('img').attr('src');
                if (browser.versions.ios) {
                    _jclient.saveImage("src=" + img, function (response) {
                        if (response) {
                            $('.Threebox').find("span").text("保存成功");
                            $('.Threebox').show().delay(3000).hide(0);
                        }
                    });
                }
                if (browser.versions.android) {
                    _jclient.saveImage("src=" + img);
                }
            });

            $('.load-btn').on('touchstart', function() {
                if (is_weixn()) {
                    $('.popup').addClass('show');
                } else {
                    window.location.href = "http://www.joyme.com/appclick/scnmxeqw";
                }
            });

        });

        function is_weixn() {
            var ua = navigator.userAgent.toLowerCase();
            if (ua.match(/MicroMessenger/i) == "micromessenger") {
                return true;
            } else {
                return false;
            }
        }
    </script>

</head>
<body>
<div class="Threebox"><span></span></div>
<%--<div id="_report" style="display: none;">report</div>--%>
<!--wrapper-->
<div id="wrapper">
<div class="popup">
    <cite><img src="${URL_LIB}/static/theme/wap/images/popup.jpg" alt=""></cite>
</div>
<div class="loading">

</div>
<!--tz-cont-->
<section class="tz-cont share" style="bottom:0; background:none;">
    <!--ui-scroller-->
    <div class="ui-scroller" id="js-scroller">
        <div class="scroller-box-color">
            <!--下载-->
            <div class="shaer-top clearfix small">
                <cite><img src="${URL_LIB}/static/theme/wap/images/syhbcon.png" alt=""></cite>

                <h2>
                    <b>着迷玩霸</b>
                    <span>玩家称霸 Go Fighting!</span>
                </h2>
                <a href="javascript:void(0);" class="load-btn">下载</a>
            </div>
            <ul>
                <li class="tz-oneself">
                    <div class="mi-tit-box">
                        <h2>
                            <c:forEach items="${profiles}" var="profile">
                                <c:if test="${commentBean.uri eq profile.profileId}">
                                    <cite><img
                                            src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"
                                            alt=""></cite>
                                    <span>${profile.nick}</span>
                                </c:if>
                            </c:forEach>
                        </h2>
                       <article class="details-cont">
                        <p>${commentBean.description}</p>
                            <c:forEach items="${pics}" var="pic">
                                <cite><img alt="" class="lazy" src="${pic}"
                                           data-src="${pic}"/> </cite>
                            </c:forEach>

                        </article>
                    </div>
                    <p class="mi-tab-btn  two">
                        <a href="javascript:void(0);"><span class="ll-num">  <c:choose>
                            <c:when test="${commentBean.commentSum==0}">
                                评论
                            </c:when>
                            <c:otherwise>
                                ${commentBean.commentSum}
                            </c:otherwise>
                        </c:choose></span></a>
                        <a href="javascript:void(0);"
                           name="${commentBean.commentId}"
                           class="zan"><span class="ll-num"
                                             id="agreesum${commentBean.commentId}"
                                             name="agreesum${commentBean.commentId}"> <c:choose>
                            <c:when test="${commentBean.scoreCommentSum==0}">
                                赞
                            </c:when>
                            <c:otherwise>
                                ${commentBean.scoreCommentSum}
                            </c:otherwise>
                        </c:choose></span></a>
                    </p>
                </li>
            </ul>
            <!--module-comment-->
            <div class="module-comment clearfix">
                <c:if test="${not empty hotList}">
                    <!--hot-tit-->
                    <div class='info-bar hot-tit'><span>神评论</span></div>
                    <div class='comment-box clearfix' id='hot-comment'>

                        <!--module-list-->
                        <c:forEach items="${hotList}" var="item">
                            <div class='module-list'>
                                <div class='module-infobox'>
                                    <cite class='user-headImg'><img src='${item.reply.user.icon}' alt=''
                                                                    title=''/></cite>

                                    <div class='module-shop'>
                                        <div><em>${item.reply.user.name}</em><span class='module-reply fr'>回复</span><a
                                                href="javascript:void(0);"
                                                class='module-zan fr ll-num'
                                                name="${item.reply.reply.rid}">${item.reply.reply.agree_sum}</a>
                                        </div>
                                        <div class='module-date'><span>${item.reply.reply.post_date}</span></div>
                                    </div>
                                </div>
                                <div class='module-txt'>
                                        ${item.reply.reply.body.text}
                                </div>
                            </div>
                        </c:forEach>
                        <!--module-list==end-->
                    </div>
                </c:if>
                <!--hot-tit==end-->
                <!--all-tit-->
                <c:if test="${not empty mainReplyRows&& not empty mainReplyRows.rows}">
                    <c:choose>
                        <c:when test="${fn:length(mainReplyRows.rows)>10}">
                            <div class='info-bar all-tit border-t'><span>更多评论</span></div>
                        </c:when>
                        <c:otherwise>
                            <div class='info-bar all-tit border-t'><span>全部评论</span></div>
                        </c:otherwise>
                    </c:choose>

                    <div class='comment-box' id='all-comment'>
                        <!--module-list-->
                        <c:forEach items="${mainReplyRows.rows}" var="item">
                            <c:if test="${empty hotMap[item.reply.reply.rid]}">
                                <div class='module-list'>
                                    <div class='module-infobox'>
                                        <cite class='user-headImg'><img src='${item.reply.user.icon}' alt=''
                                                                        title=''/></cite>

                                        <div class='module-shop'>
                                            <div><em>${item.reply.user.name}</em><span
                                                    class='module-reply fr'>回复</span><a
                                                    href="javascript:void(0);"
                                                    class='module-zan fr ll-num'
                                                    name="${item.reply.reply.rid}">${item.reply.reply.agree_sum}</a>
                                            </div>
                                            <div class='module-date'><span>${item.reply.reply.post_date}</span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class='module-txt'> ${item.reply.reply.body.text} </div>
                                </div>
                            </c:if>
                        </c:forEach>
                        <!--module-list==end-->
                            <span style="display:none;" id="hotId">
                                <c:forEach items="${hotMap}" var="entry">
                                    ${entry.value},
                                </c:forEach>
                            </span>

                    </div>
                </c:if>
                <c:if test="${empty mainReplyRows||empty mainReplyRows.rows}">
                    <div class="module-comment clearfix">
                        <div class="none-comment info-bar">
                            <cite><img src="${URL_LIB}/static/theme/default/images/no-comm.png" alt=""></cite>
                            <span>别让卤煮寂寞太久！</span>
                        </div>
                    </div>
                </c:if>
                <!--all-tit==end-->
            </div>
            <div id="pullUp1" style="display:none;">
                <span class="pullUpIcon"></span><span class="pullUpLabel">加载也是个正经事儿</span>

                <div class="spinner">
                    <div class="double-bounce1"></div>
                    <div class="double-bounce2"></div>
                </div>
            </div>
            <!--module-comment==end-->
        </div>

    </div>

    <input type="hidden" value="0" id="miyouCurPage"/>
    <input type="hidden" value="0" id="miyouMaxPage"/>
    <!--ui-scroller-->
</section>
<!--tz-cont-->
<!--alert-comment-->
<div class='alert-comment'>
    <div class='info-alert-box'>
        <!--info-beyond-->
        <div class='info-beyond'>
            <span></span>
        </div>
        <!--info-beyond==end-->
        <div class='info-alert-form'>
            <textarea class='info-textarea' placeholder='请输入内容，140字以内'></textarea>
        </div>
        <div class='info-alert-shop clearfix'>
            <span class='fl' style='display:none'>已超出<b class='inp-num'>0</b>个字</span>

            <div class='fr'>
                <span class="cancel-btn">取消</span>
                <span class="reply-btn">来一发</span>
            </div>
        </div>
    </div>
</div>
<!--alert-comment==end-->
<div class='wp-bg'></div>
<!--下载-->
<div class="shaer-top clearfix bt" style="z-index:999">
    <cite><img src="${URL_LIB}/static/theme/wap/images/syhbcon.png" alt=""></cite>

    <h2>
        <b>着迷玩霸</b>
        <span>玩家称霸 Go Fighting!</span>
    </h2>
    <a href="javascript:void(0);" class="load-btn">下载</a>
</div>
<!--大图-->
<div class="large-box">
    <div class="large-pic">
        <span></span>
    </div>
    <%--<div class="large-icon">--%>
    <%--<a href="javascript:void(0);" class="large-load"></a>--%>
    <%--<a href="javascript:nativeShare('${commentBean.description}','${commentBean.description}','${commentBean.pic}','${commentBean.commentId}')"--%>
    <%--class="large-collect"></a>--%>
    <%--</div>--%>
</div>
<!--大图-->
</div>

<input type="hidden" id="uid" value="${uid}"/>
<input type="hidden" id="logindomain" value="${logindomain}"/>
<input type="hidden" id="appkey" value="${appkey}"/>
<input type="hidden" id="cid" value="${commentBean.commentId}"/>
<!--wrapper-->
<script>
function popup() {
//    $('.load-btn').on('tap', function() {
//        $('.popup').addClass('show');
//    });
    $('.popup').on('tap', function() {
        $('.popup').removeClass('show');
    });
}
popup();
function browseNum() {
    $('.ll-num').each(function() {
        var num = parseInt($(this).text());
        if (isNaN(num)) {
            return;
        }
        if (num >= 10000) {
            num = ($(this).text() / 10000).toFixed(1)
            var ind = num.indexOf('.');
            var lastNum = num.substr(ind + 1, 1);
            if (lastNum == '0') {
                num = num.substr(0, ind)
            }
            $(this).text(num + '万');
        } else {
            $(this).text(num);
        }
    })
}
browseNum();
function browsePicture(parentbox) {
    var $imgs = parentbox.find('em').children('img');

    $imgs.on('tap', function() {
        var url = '';
        url = $(this).attr('src');
        $('.large-pic>span').html('<img src="' + url + '" />')
        $('.large-box').show();
    });
    $('.large-pic').on('tap', function() {
        $('.large-box').hide();
    })
}
browsePicture($('.mi-tit-box'))
window.addEventListener('load', function() {
    //document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
    var pullUpEl1 = document.getElementById('pullUp1');
    pullUpOffset1 = pullUpEl1.offsetHeight;
    var myScroll = new iScroll('js-scroller', {
                vScroll:true,
                vScrollbar:false,
                useTransition: false,
                checkDOMChanges:true,
                onBeforeScrollStart: function (e) {
                    var target = e.target;
                    while (target.nodeType != 1) target = target.parentNode;
                    if (target.tagName != "SELECT" && target.tagName != "INPUT" && target.tagName != "TEXTAREA")
                        e.preventDefault();
                }, onRefresh: function () {
                    var curNum = $("#miyouCurPage").val();
                    var maxNum = $("#miyouMaxPage").val();
                    if (parseInt(maxNum) <= parseInt(curNum)) {
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '没有更多数据了';
                        pullUpEl1.querySelector('.spinner').style.display = "none";

                        return;
                    }
                    if (pullUpEl1.className.match('loading')) {
                        pullUpEl1.className = '';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '加载也是个正经事儿';
                    }
                },
                onScrollMove: function () {
                    if (this.y < (this.maxScrollY - 5) && !pullUpEl1.className.match('flip')) {
                        var curNum = $("#miyouCurPage").val();
                        var maxNum = $("#miyouMaxPage").val();
                        if (parseInt(maxNum) <= parseInt(curNum)) {
                            pullUpEl1.querySelector('.pullUpLabel').innerHTML = '没有更多数据了';
                            pullUpEl1.querySelector('.spinner').style.display = "none";

                            return;
                        }
                        pullUpEl1.className = 'flip';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '释放立即刷新';
                        this.maxScrollY = this.maxScrollY;
                    } else if (this.y > (this.maxScrollY + 5) && pullUpEl1.className.match('flip')) {
                        pullUpEl1.className = '';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '加载也是个正经事儿';
                        this.maxScrollY = pullUpOffset1;
                    }
                },
                onScrollEnd: function () {
                    if (pullUpEl1.className.match('flip')) {
                        pullUpEl1.className = 'loading';
                        pullUpEl1.querySelector('.pullUpLabel').innerHTML = '别闹走心加载中';
                        pullUpAction();
                    }
                }
            });
    var postbool = true;
    var commentTools = {
        'int':function() {
            commentTools.huifu();
            commentTools.txtTools();
            $(window).resize(function(e) {
                commentTools.windowHeight();
            });
        },
        //回复
        'huifu':function() {
            var wp_commentLink = $('.wp-comment a');

            var wp_bg = $('.wp-bg');
            var alert_comment = $('.alert-comment');
            var $textarea = $('.info-textarea');
            var $cancelBtn = $('.cancel-btn');
            var times = null;
            wp_commentLink.on('click', function(e) {
                e.preventDefault();
                var logindomain = $("#logindomain").val();
                if (logindomain == '' || logindomain == 'client') {
                    _jclient.showLogin();
                    return;
                }


                clearInterval(times);
                $(this).parent().hide();
                wp_bg.show();
                alert_comment.show();
                $textarea.focus();
            });
            $cancelBtn.on('click', function() {

                wp_bg.hide();
                alert_comment.hide();
                times = setInterval(function() {
                    $('.wp-comment').show(500);
                }, 600);
            });
            wp_bg.on('touchstart', function(ev) {
                ev.stopPropagation();
                ev.preventDefault()
            });
            wp_bg.on('touchmoveon', function(ev) {
                ev.stopPropagation();
                ev.preventDefault()
            });
        },
        //字数限制
//评论字数限制
        'txtTools':function() {
            var txtobj = {
                divName:'info-alert-box', //外层容器的class
                textareaName:'info-textarea',
                numName:'inp-num',
                num:140 //最大字数
            }
            var textareaFn = function() {
                var $onthis;
                var $divname = txtobj.divName;
                var $textareaName = txtobj.textareaName;
                var $numName = txtobj.numName;
                var $num = txtobj.num;
                var infotext = $('.' + $textareaName);
                var strlen = 0;
                var $replyBtn = $('.reply-btn');
                var $infoBeyond = $('.info-beyond');
                var ibTime = null;
                var $par;
                var $b;

                function isChinese(str) { //判断是不是中文
                    var oVal = str.val();
                    var oValLength = 0;
                    oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
                    return oValLength;

                }

                //提示2秒后关闭
                function textTips() {
                    $infoBeyond.show();
                    clearInterval(ibTime);
                    ibTime = setInterval(function() {
                        $infoBeyond.hide();
                    }, 3000);
                }

                ;
                function numChange() {
                    var setNum = '';
                    var txtval = $.trim(infotext.val());
                    strlen = isChinese(infotext);

                    strlen = Math.ceil(strlen / 2);
                    //超出文字提示
                    if ($num - strlen < 0) {
                        $par.show();
                        $par.html('已超出<b class=' + $numName + '>' + Math.abs($num - strlen) + '</b>个字');
                    } else {
                        if (strlen >= 130) {//剩余最后10个字时提示
                            $par.show();
                            $par.html('还可以输入<b class=' + $numName + '>' + ($num - strlen) + '</b>个字');
                        } else {
                            if ($par != null) {
                                $par.hide();
                            }
                        }
                    }
                    if ($b != null) {
                        $b.html($num - strlen);
                    }
                }

                ;
                //当字数小于2或大于140
                $replyBtn.on('touchstart', function() {
                    numChange();
                    var txtval = $.trim(infotext.val());
                    if (txtval.length <= 1) {
                        textTips();
                        $infoBeyond.html('<span>请输入至少2个字</span>');
                        return;
                    }

                    if (strlen > 140) {
                        textTips();
                        $infoBeyond.html('<span>请删减后再进行回复</span>');
                        return;
                    } else {
                        if (postbool) {
                            postbool = false;
                            var textare = infotext.val();
                            var uid = $("#uid").val();
                            var commentid = $("#cid").val();
                            var appkey = $("#appkey").val();
                            textare = {text:textare};
                            var text = JSON.stringify(textare);
                            $.ajax({
                                        type : "post",
                                        url:"/joymeapp/gameclient/json/miyou/comment",
                                        data:{uid:uid,cid:commentid,appkey: appkey,text:text},
                                        success : function(req) {
                                            var result = eval('(' + req + ')');
                                            if (result.rs == 1) {
                                                location.reload();
                                            } else if (result.rs == '-40005') {
                                                textTips();
                                                $infoBeyond.html('<span>文字不能为空</span>');
                                                return;
                                            } else if (result.rs = '-40017') {
                                                var keyword = result.result;
                                                textTips();
                                                $infoBeyond.html('<span>不要使用" ' + keyword + ' "等词汇，请修改</span>');
                                            }
                                            postbool = true;
                                        },error:function(req) {
                                            postbool = true;
                                        }
                                    });
                        }
                    }

                });
                var inp = 'input propertychange' || 'keyup';
                infotext.on(inp, function() {
                    $b = infotext.parents('.' + $divname).find('.' + $numName); //获取当前的数字
                    $par = $b.parent();
                    $onthis = $(this);
                    numChange();
                });
            }
            textareaFn();
        }

    }
    commentTools.int();
});
function pullUpAction() {
    var cid = $("#cid").val();
    var uid = $("#uid").val();
    var curNum = $("#miyouCurPage").val();
    var maxNum = $("#miyouMaxPage").val();
    if (curNum == '') {
        return;
    }
    if (parseInt(maxNum) <= parseInt(curNum)) {
        return;
    }
    curNum = parseInt(curNum) + parseInt(1);
    $.ajax({
                type : "post",
                url:"/joymeapp/gameclient/json/miyou/replylist",
                data:{cid:cid,count:15,pnum: curNum,uid:uid},
                success : function(req) {
                    var result = eval('(' + req + ')');
                    if (result.rs == '1') {
                        var hotId = $("#hotId").html().split(",");
                        var replylist = result.result.replylist;
                        for (var i = 0; i < replylist.length; i++) {
                            var bool = true;
                            for (var j = 0; j < hotId.length; j++) {
                                if (replylist[i].reply.reply.rid == hotId[j]) {
                                    bool = false;
                                }
                            }
                            if (bool) {
                                var content = "<div class='module-list'><div class='module-infobox'> <cite class='user-headImg'>" +
                                        "<img src=" + replylist[i].reply.user.icon + " > </cite><div class='module-shop'>" +
                                        "<div><em>" + replylist[i].reply.user.name + "</em><span class='module-reply fr'>回复</span>" +
                                        " <a href='javascript:agreeMiyou(" + '"' + cid + '"' + "," + '"' + replylist[i].reply.reply.rid + '"' + ")' class='module-zan fr ll-num' name='" + replylist[i].reply.reply.rid + "' >" + replylist[i].reply.reply.agree_sum + "</a></div><div class='module-date'><span>" + replylist[i].reply.reply.post_date + "</span>" +
                                        "</div></div></div><div class='module-txt'>" + replylist[i].reply.reply.body.text + "</div></div>";
                                $("#all-comment").append(content);
                            }
                        }
                        $("#miyouCurPage").val(result.page.curPage);
                        $("#miyouMaxPage").val(result.page.maxPage);

                        for (var i = 0; i < result.result.commentHistories.length; i++) {
                            var commentId = result.result.commentHistories[i].objectId;
                            $("[name='" + commentId + "']").each(function() {
                                $(this).addClass("active");
                                $(this).attr("href", "javascript:void(0);");
                            });
                        }
                        browseNum();
                    }

                }});
}
//function agreeMiyou(commentId, rid) {
//    var uid = $("#uid").val();
//
//    $.post("/joymeapp/gameclient/json/miyou/agree", {uid:uid,cid:commentId,rid:rid}, function(req) {
//        var resMsg = eval('(' + req + ')');
//        if (resMsg.rs == '1') {
//            var agreeSum = $("#agreesum" + commentId).text();
//            agreeSum = parseInt(agreeSum) + 1;
//            if (isNaN(agreeSum)) {
//                agreeSum = 0;
//            }
//            if (rid == '') {
//                $("[name='" + commentId + "']").each(function() {
//                    $(this).addClass("active");
//                    $(this).attr("href", "javascript:void(0);");
//                });
//                $("[name='agreesum" + commentId + "']").each(function() {
//                    $(this).text(agreeSum);
//                });
//            } else {
//                $("[name=" + rid + "]").each(function() {
//                    $(this).addClass("active");
//                    var sum = $(this).text();
//                    $(this).text(parseInt(sum) + 1);
//                    $(this).attr("href", "javascript:void(0);");
//                });
//            }
//
//            return;
//        } else if (resMsg.rs == '-40016') {
//            alert("已经点过赞了");
//            return;
//        } else if (resMsg.rs == '-10104') {
//            alert("参数为空");
//            return;
//        } else if (resMsg.rs == '-1000') {
//            alert("点赞失败");
//            return;
//        } else {
//            alert("点赞失败");
//            return;
//        }
//    });
//}

//function nativeShare(title, content, pic, commentId) {
//    var url = encodeURIComponent("http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=" + commentId);
//    _jclient.share("title=" + title + "&content=" + content + "&picurl=" + encodeURIComponent(pic) + "&url=" + url);
//}


</script>
</body>
</html>