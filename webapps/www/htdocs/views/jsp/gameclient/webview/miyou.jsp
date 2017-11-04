<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1x.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<%@ include file="/views/jsp/common/jsconfig.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta http-equiv="Cache-Control" content="no-Cache"/>
    <meta http-equiv="Cache-Control" content="max-age=0"/>
    <title>迷友圈</title>
    <link href="${URL_LIB}/static/theme/wap/css/wanba/common.css" rel="stylesheet"
          type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wanba/demo.css" rel="stylesheet"
          type="text/css">

    <%--<script src="http://static.joyme.com/static/js/common/zepto.min.js"></script>--%>
    <%--<script src="${URL_LIB}/static/js/common/dropload.js"></script>--%>
    <%--<script src="${URL_LIB}/static/js/common/zepto.loadmore.js"></script>--%>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/miyouscroll.js"></script>
    <script src="http://static.joyme.com/app/wanba/js/zepto.min.js"></script>
    <script src="http://static.joyme.com/app/wanba/js/dropload.js"></script>
    <script src="http://static.joyme.com/app/wanba/js/zepto.loadmore.js"></script>
    <%--<script type="text/javascript" src="D:/static/app/wanba/js/miyouscroll.js"></script>--%>


    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);
        $.cookie = function(name, value, options) {
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
                        var cookie = $.trim(cookies[i]);
                        if (cookie.substring(0, name.length + 1) == (name + '=')) {
                            cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                            break;
                        }
                    }
                }
                return cookieValue;
            }
        };
        $(document).ready(function() {

            $("#loginmiyou").click(function() {
                _jclient.showLogin();
            });

            $("#postmiyou").on("touchstart", function() {
                _jclient.showPublish();
            });

            <c:forEach items="${commentHistories}" var="item">
            var commentId = '${item.objectId}';
            $("[name='" + commentId + "']").each(function() {
                $(this).addClass("active");
//                $(this).attr("href", "javascript:void(0);");
            });

            </c:forEach>
            var miyoumodifytime = '${profilesum.modifyTimeJson.miyouModifyTime}';
            var uidparam = '${uid}';
            var cookieValue = $.cookie(uidparam);
            if (cookieValue != null && cookieValue != miyoumodifytime) {
                $("#removered").addClass("reddot");
            }
            $("#removered").on('touchstart', function() {
                $.cookie(uidparam, miyoumodifytime, {expires:10});
                $("#removered").removeClass("reddot");
            });

        });
    </script>

</head>
<body>
<div class="Threebox"><span></span></div>
<div class="outer">
<div class="mi-tab-menu">
    <div class="mi-tab-menu">
        <div class="mi-tab-tit">
            <span>迷友圈</span><a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/miyou/list?retype=mymiyou"
                               id="removered"></a>
        </div>
    </div>
</div>

<div class="inner <c:if test="${empty retype || retype=='miyou'}">active</c:if> " id="inner">

    <ul id="lists" class="lists">
        <c:forEach items="${miyoulist}" var="item">
            <li class="item">
                <div class="mi-tit-box <c:if test="${item.oneUserSum eq '-1'}">stick</c:if>">
                    <h2>
                        <c:forEach items="${profiles}" var="profile">
                            <c:if test="${item.uri eq profile.profileId}">
                                <cite><img class="lazy" src="${URL_LIB}/static/theme/default/images/data-bg.gif"
                                           data-src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"
                                           alt=""></cite>
                                <span>${profile.nick}</span>
                            </c:if>
                        </c:forEach>

                    </h2>
                    <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}">
                        <p> ${item.description}</p>
                        <c:if test="${not empty item.pic}"> <em><b class="ll-num">${item.longCommentSum}</b>
                            <img class="lazy" src="${URL_LIB}/static/theme/default/images/data-bg.gif"
                                 data-src="${item.pic}?imageMogr2/size-limit/800k" alt=""></em> </c:if>
                        <c:if test="${not empty hotReply}">
                            <c:forEach items="${hotReply}" var="hotReply">

                                <c:if test="${item.commentId eq hotReply.commentId}">
                                    <div class="mi-text-sp">
                                        <c:forEach items="${profiles}" var="profile">
                                            <c:if test="${hotReply.replyProfileId eq profile.profileId}">
                                                <cite><img class="lazy"
                                                           src="${URL_LIB}/static/theme/default/images/data-bg.gif"
                                                           data-src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"
                                                           alt=""></cite>
                                            </c:if>
                                        </c:forEach>
                                        <span>${hotReply.body.text}</span>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </c:if>

                    </a>

                    <p class="mi-tab-btn">
                        <a href="javascript:nativeShare('${item.description}','${item.description}','${item.pic}','${item.commentId}')"><span>分享</span></a>
                        <a href="http://api.${DOMAIN}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"><span
                                class="ll-num">
                            <c:choose>
                                <c:when test="${item.commentSum==0}">
                                    评论
                                </c:when>
                                <c:otherwise>
                                    ${item.commentSum}
                                </c:otherwise>
                            </c:choose></span></a>

                        <a href="javascript:agreeMiyou('${item.commentId}')" name="${item.commentId}"
                           class="zan"><span class="ll-num"
                                             id="agreesum${item.commentId}"
                                             name="agreesum${item.commentId}">
                                 <c:choose>
                                     <c:when test="${item.scoreCommentSum==0}">
                                         赞
                                     </c:when>
                                     <c:otherwise>
                                         ${item.scoreCommentSum}
                                     </c:otherwise>
                                 </c:choose></span></a>
                    </p>
                </div>
            </li>
        </c:forEach>


    </ul>
    <input type="hidden" value="${miyoupage.curPage}" id="miyouCurPage"/>
    <input type="hidden" value="${miyoupage.maxPage}" id="miyouMaxPage"/>


</div>
<!--ui-scroller-->
<%--<div class="ui-scroller <c:if test="${retype=='mymiyou'}">active</c:if> " id="js-scroller-two">--%>
<%--<div>--%>


<%--<c:choose>--%>
<%--<c:when test="${empty logindomain || logindomain=='client'}">--%>
<%--<div class="no-login">--%>
<%--<a href="javascript:void(0);" id="loginmiyou" class="ui-border-radius">立即登录</a>--%>
<%--<span>记录你的精彩</span>--%>
<%--</div>--%>
<%--<ul id="miyouul">--%>
<%--<c:forEach items="${miyoulist}" var="item">--%>
<%--<li>--%>
<%--<div class="mi-tit-box">--%>
<%--<h2>--%>
<%--<c:forEach items="${profiles}" var="profile">--%>
<%--<c:if test="${item.uri eq profile.profileId}">--%>
<%--<cite><img--%>
<%--src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"--%>
<%--alt=""></cite>--%>
<%--<span>${profile.nick}</span>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>

<%--</h2>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}">--%>
<%--<p> ${item.description}</p>--%>
<%--<em><b class="ll-num">${item.longCommentSum}</b><img src="${item.pic}" alt=""></em>--%>
<%--<c:if test="${not empty hotReply}">--%>
<%--<c:forEach items="${hotReply}" var="hotReply">--%>
<%--<c:if test="${item.commentId eq hotReply.commentId}">--%>
<%--<div class="mi-text-sp">--%>
<%--<c:forEach items="${profiles}" var="profile">--%>
<%--<c:if test="${hotReply.replyProfileId eq profile.profileId}">--%>
<%--<cite><img--%>
<%--src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"--%>
<%--alt=""></cite>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--<span>${hotReply.body.text}</span>--%>
<%--</div>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--</c:if>--%>

<%--</a>--%>
<%--</div>--%>
<%--<p class="mi-tab-btn">--%>
<%--<a href="javascript:nativeShare('${item.description}','${item.description}','${item.pic}','${item.commentId}')"><span>分享</span></a>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"><span--%>
<%--class="ll-num">--%>
<%--<c:choose>--%>
<%--<c:when test="${item.commentSum==0}">--%>
<%--评论--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.commentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>

<%--<a href="javascript:agreeMiyou('${item.commentId}')" name="${item.commentId}"--%>
<%--class="zan"><span class="ll-num"--%>
<%--id="agreesum${item.commentId}"--%>
<%--name="agreesum${item.commentId}">--%>
<%--<c:choose>--%>
<%--<c:when test="${item.scoreCommentSum==0}">--%>
<%--赞--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.scoreCommentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>
<%--</p>--%>
<%--</li>--%>
<%--</c:forEach>--%>
<%--</ul>--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--<c:choose>--%>
<%--<c:when test="${empty mymiyouList}">--%>
<%--<div class="no-data">--%>
<%--<cite><img src="${URL_LIB}/static/theme/default/images/no-data-icon.png" alt=""></cite>--%>
<%--<span>你还没有发布迷友圈</span>--%>
<%--</div>--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--<c:if test="${not empty todayList}">--%>
<%--<h2 class="mi-tit t1">--%>
<%--<span><b>今天</b></span>--%>
<%--</h2>--%>
<%--</c:if>--%>
<%--<ul id="thelist1">--%>
<%--<c:forEach items="${todayList}" var="item">--%>
<%--<li>--%>
<%--<div href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"--%>
<%--class="mi-tit-box">--%>
<%--<h2 class="mi-tit-time"><fmt:formatDate value="${item.createTime}"--%>
<%--pattern="HH:mm"/></h2>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}">--%>
<%--<p>${item.description}</p>--%>
<%--<em><b class="ll-num">${item.longCommentSum}</b><img src="${item.pic}" alt=""></em>--%>
<%--<c:if test="${not empty hotReply}">--%>
<%--<c:forEach items="${hotReply}" var="hotReply">--%>
<%--<c:if test="${item.commentId eq hotReply.commentId}">--%>
<%--<div class="mi-text-sp">--%>
<%--<c:forEach items="${profiles}" var="profile">--%>
<%--<c:if test="${hotReply.replyProfileId eq profile.profileId}">--%>
<%--<cite><img--%>
<%--src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"--%>
<%--alt=""></cite>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--<span>${hotReply.body.text}</span>--%>
<%--</div>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--</c:if>--%>

<%--</a>--%>
<%--</div>--%>
<%--<p class="mi-tab-btn">--%>
<%--<a href="javascript:nativeShare('${item.description}','${item.description}','${item.pic}','${item.commentId}')"><span>分享</span></a>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"><span--%>
<%--class="ll-num">  <c:choose>--%>
<%--<c:when test="${item.commentSum==0}">--%>
<%--评论--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.commentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>

<%--<a href="javascript:agreeMiyou('${item.commentId}')"--%>
<%--name="${item.commentId}"--%>
<%--class="zan"><span class="ll-num"--%>
<%--id="agreesum${item.commentId}"--%>
<%--name="agreesum${item.commentId}">--%>
<%--<c:choose>--%>
<%--<c:when test="${item.scoreCommentSum==0}">--%>
<%--赞--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.scoreCommentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose>--%>
<%--</span></a>--%>
<%--</p>--%>
<%--</li>--%>
<%--</c:forEach>--%>
<%--</ul>--%>

<%--<c:if test="${not empty yesterdayList}">--%>
<%--<h2 class="mi-tit t2">--%>
<%--<span><b>昨天</b></span>--%>
<%--</h2>--%>
<%--</c:if>--%>
<%--<ul id="thelist2">--%>
<%--<c:forEach items="${yesterdayList}" var="item">--%>
<%--<li>--%>
<%--<div href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"--%>
<%--class="mi-tit-box">--%>
<%--<h2 class="mi-tit-time">昨天 <fmt:formatDate value="${item.createTime}"--%>
<%--pattern="HH:mm"/></h2>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}">--%>
<%--<p>${item.description}</p>--%>
<%--<em><b class="ll-num">${item.longCommentSum}</b><img src="${item.pic}" alt=""></em>--%>
<%--<c:if test="${not empty hotReply}">--%>
<%--<c:forEach items="${hotReply}" var="hotReply">--%>
<%--<c:if test="${item.commentId eq hotReply.commentId}">--%>
<%--<div class="mi-text-sp">--%>
<%--<c:forEach items="${profiles}" var="profile">--%>
<%--<c:if test="${hotReply.replyProfileId eq profile.profileId}">--%>
<%--<cite><img--%>
<%--src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"--%>
<%--alt=""></cite>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--<span>${hotReply.body.text}</span>--%>
<%--</div>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--</c:if>--%>

<%--</a>--%>
<%--</div>--%>
<%--<p class="mi-tab-btn">--%>
<%--<a href="javascript:nativeShare('${item.description}','${item.description}','${item.pic}','${item.commentId}')"><span>分享</span></a>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"><span--%>
<%--class="ll-num">  <c:choose>--%>
<%--<c:when test="${item.commentSum==0}">--%>
<%--评论--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.commentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>

<%--<a href="javascript:agreeMiyou('${item.commentId}')"--%>
<%--name="${item.commentId}"--%>
<%--class="zan"><span class="ll-num"--%>
<%--id="agreesum${item.commentId}"--%>
<%--name="agreesum${item.commentId}"> <c:choose>--%>
<%--<c:when test="${item.scoreCommentSum==0}">--%>
<%--赞--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.scoreCommentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>
<%--</p>--%>
<%--</li>--%>
<%--</c:forEach>--%>
<%--</ul>--%>

<%--<c:if test="${not empty dayList}">--%>
<%--<h2 class="mi-tit t3">--%>
<%--<span><b>前天</b></span>--%>
<%--</h2>--%>
<%--</c:if>--%>
<%--<ul id="thelist3">--%>
<%--<c:forEach items="${dayList}" var="item">--%>
<%--<li>--%>
<%--<div href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"--%>
<%--class="mi-tit-box">--%>
<%--<h2 class="mi-tit-time">前天<fmt:formatDate value="${item.createTime}"--%>
<%--pattern="HH:mm"/></h2>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}">--%>
<%--<p>${item.description}</p>--%>
<%--<em><b class="ll-num">${item.longCommentSum}</b><img src="${item.pic}" alt=""></em>--%>
<%--<c:if test="${not empty hotReply}">--%>
<%--<c:forEach items="${hotReply}" var="hotReply">--%>
<%--<c:if test="${item.commentId eq hotReply.commentId}">--%>
<%--<div class="mi-text-sp">--%>
<%--<c:forEach items="${profiles}" var="profile">--%>
<%--<c:if test="${hotReply.replyProfileId eq profile.profileId}">--%>
<%--<cite><img--%>
<%--src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"--%>
<%--alt=""></cite>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--<span>${hotReply.body.text}</span>--%>
<%--</div>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--</c:if>--%>

<%--</a>--%>
<%--</div>--%>
<%--<p class="mi-tab-btn">--%>
<%--<a href="javascript:nativeShare('${item.description}','${item.description}','${item.pic}','${item.commentId}')"><span>分享</span></a>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"><span--%>
<%--class="ll-num">  <c:choose>--%>
<%--<c:when test="${item.commentSum==0}">--%>
<%--评论--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.commentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>
<%--<a href="javascript:agreeMiyou('${item.commentId}')"--%>
<%--name="${item.commentId}"--%>
<%--class="zan"><span class="ll-num"--%>
<%--id="agreesum${item.commentId}"--%>
<%--name="agreesum${item.commentId}"> <c:choose>--%>
<%--<c:when test="${item.scoreCommentSum==0}">--%>
<%--赞--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.scoreCommentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>
<%--</p>--%>
<%--</li>--%>
<%--</c:forEach>--%>
<%--</ul>--%>

<%--<c:if test="${not empty moredayList}">--%>
<%--<h2 class="mi-tit t4">--%>
<%--<span><b>较早之前</b></span>--%>
<%--</h2>--%>
<%--</c:if>--%>
<%--<ul id="thelist4">--%>
<%--<c:forEach items="${moredayList}" var="item">--%>
<%--<li>--%>
<%--<div href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"--%>
<%--class="mi-tit-box">--%>
<%--<h2 class="mi-tit-time"><fmt:formatDate value="${item.createTime}"--%>
<%--pattern="yyyy-MM-dd HH:mm"/></h2>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}">--%>
<%--<p>${item.description}</p>--%>
<%--<em><b class="ll-num">${item.longCommentSum}</b><img src="${item.pic}" alt=""></em>--%>
<%--<c:if test="${not empty hotReply}">--%>
<%--<c:forEach items="${hotReply}" var="hotReply">--%>
<%--<c:if test="${item.commentId eq hotReply.commentId}">--%>
<%--<div class="mi-text-sp">--%>
<%--<c:forEach items="${profiles}" var="profile">--%>
<%--<c:if test="${hotReply.replyProfileId eq profile.profileId}">--%>
<%--<cite><img--%>
<%--src="${icon:parseIcon(profile.icon,profile.sex, "")}?imageView2/1/w/100/h/100"--%>
<%--alt=""></cite>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--<span>${hotReply.body.text}</span>--%>
<%--</div>--%>
<%--</c:if>--%>
<%--</c:forEach>--%>
<%--</c:if>--%>

<%--</a>--%>
<%--</div>--%>
<%--<p class="mi-tab-btn">--%>
<%--<a href="javascript:nativeShare('${item.description}','${item.description}','${item.pic}','${item.commentId}')"><span>分享</span></a>--%>
<%--<a href="${URL_WWW}/joymeapp/gameclient/webview/miyou/miyoudetail?commentid=${item.commentId}"><span--%>
<%--class="ll-num">  <c:choose>--%>
<%--<c:when test="${item.commentSum==0}">--%>
<%--评论--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.commentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>
<%--<a href="javascript:agreeMiyou('${item.commentId}')"--%>
<%--name="${item.commentId}"--%>
<%--class="zan"><span class="ll-num"--%>
<%--id="agreesum${item.commentId}"--%>
<%--name="agreesum${item.commentId}"> <c:choose>--%>
<%--<c:when test="${item.scoreCommentSum==0}">--%>
<%--赞--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--${item.scoreCommentSum}--%>
<%--</c:otherwise>--%>
<%--</c:choose></span></a>--%>
<%--</p>--%>
<%--</li>--%>
<%--</c:forEach>--%>
<%--</ul>--%>

<%--</c:otherwise>--%>
<%--</c:choose>--%>
<%--</c:otherwise>--%>
<%--</c:choose>--%>

<%--<input type="hidden" value="${mymiyoupage.curPage}" id="mymiyouCurPage"/>--%>
<%--<input type="hidden" value="${mymiyoupage.maxPage}" id="mymiyouMaxPage"/>--%>


<%--</div>--%>
<%--<div class="add-btn">--%>
<%--<a href="javascript:void(0);" name="postmiyou">--%>
<%--<c:if test="${postguide=='true'}">--%>
<%--<em>分享快乐&nbsp;&nbsp;不做伸手党</em>--%>
<%--</c:if>--%>
<%--</a>--%>
<%--</div>--%>
<%--</div>--%>

<!--ui-scroller==end-->

<!--mi-tab-cont-->
<div class="add-btn">
    <a href="javascript:void(0);" id="postmiyou">
        <c:if test="${postguide=='true'}">
            <em>分享快乐&nbsp;&nbsp;不做伸手党</em>
        </c:if>
    </a>
</div>
</div>


<input type="hidden" id="uid" value="${uid}"/>
<input type="hidden" id="logindomain" value="${logindomain}"/>
<input type="hidden" id="retype" value="${retype}"/>
<input type="hidden" id="postcommentId" value="${commentId}"/>

<script type="text/javascript" src="http://static.joyme.com/app/wanba/js/lazyImg.js"></script>
<script>


    function browseNum() {
        $('.ll-num').each(function() {
            var num = $(this).text();
            if (isNaN(num)) {
                return;
            }
            num = parseInt(num);
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

    function agreeMiyou(commentId) {
        var uid = $("#uid").val();
        $.post("/joymeapp/gameclient/json/miyou/agree", {uid:uid,cid:commentId}, function(req) {
            var resMsg = eval('(' + req + ')');
            if (resMsg.rs == '1') {
                var agreeSum = $("#agreesum" + commentId).text();
                if (isNaN(agreeSum)) {
                    agreeSum = 0;
                }
                agreeSum = parseInt(agreeSum) + 1;

                $("[name='" + commentId + "']").each(function() {
                    $(this).addClass("active active-end");
//                    $(this).attr("href", "javascript:void(0);");
                });
                var sum = $("#agreesum" + commentId).text().trim()
                if (sum == '赞' || !isNaN(sum)) {
                    $("[name='agreesum" + commentId + "']").each(function() {
                        $(this).text(agreeSum);
                    });
                }

                return;
            } else if (resMsg.rs == '-40016') {
                $('.Threebox').find("span").text("已经点过赞了");
                $('.Threebox').show();
                var time = setTimeout(function() {
                    $('.Threebox').hide();
                    clearTimeout(time);
                }, 3000);
            } else if (resMsg.rs == '-10104') {
                $('.Threebox').find("span").text("参数为空");
                $('.Threebox').show();

                var time = setTimeout(function() {
                    $('.Threebox').hide();
                    clearTimeout(time);
                }, 3000);

            } else if (resMsg.rs == '-1000') {
                $('.Threebox').find("span").text("点赞失败");
                $('.Threebox').show();

                var time = setTimeout(function() {
                    $('.Threebox').hide();
                    clearTimeout(time);
                }, 3000);

            } else {
                $('.Threebox').find("span").text("点赞失败");
                $('.Threebox').show();
                var time = setTimeout(function() {
                    $('.Threebox').hide();
                    clearTimeout(time);
                }, 3000);

            }
        });
    }

    var bool1 = true;
    function nativeShare(title, content, pic, commentId) {
        if (bool1) {
            bool1 = false;
            var url = "http://api." + joyconfig.DOMAIN + "/joymeapp/gameclient/webview/miyou/share?commentid=" + commentId;
            var urlencoding = "";
            $.ajax({
                        type : 'POST',
                        url : '/joymeapp/gameclient/json/miyou/shorten?url=' + url,//source为新浪appkey，最好用自己的
                        success: function(req) {
                            var result = eval('(' + req + ')');
                            if (result.rs == '1') {
                                urlencoding = result.result;
                            } else {
                                urlencoding = url;
                            }
                            bool1 = true;
                            _jclient.share("title=" + title + "&content=" + content + "&picurl=" + encodeURIComponent(pic) + "&url=" + encodeURIComponent(urlencoding));
                        },
                        error:function(XMLHttpRequest, textStatus, errorThrown) {
                            urlencoding = url;
                            bool1 = true;
                            _jclient.share("title=" + title + "&content=" + content + "&picurl=" + encodeURIComponent(pic) + "&url=" + encodeURIComponent(urlencoding));
                        }
                    });

        }
    }


</script>


</body>
</html>