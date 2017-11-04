<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta content=" initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="${URL_LIB}/static/theme/default/css/wca.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <title>${activity.title}</title>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);

        var uno = '${uno}';
        var lock = false;

        $(document).ready(function() {
            $("a[name=agree]").click(function (e) {
                e.preventDefault();
                if (lock) {
                    return false;
                }

                lock = true;
                var div = $(this).find("div")[0];
                var cid = $(this).attr("data-cid");
                var hasAgree = $(div).hasClass("vv"); //已经赞过
                var action = (uno != null && uno.length > 0 && hasAgree) ? 0 : 1;

                $.ajax({
                            url: '/json/kada/agree',
                            type: 'post',
                            data: {'uno': uno, 'action': action,'cid':cid},
                            dataType: "json",
                            success: function (data) {
                                if (data.rs == "1") {
                                    if (action == 1) {
                                        div.className = "z-btn vv";
                                        var num = parseInt(div.innerText) + 1;
                                        div.innerText = num;
                                    } else {
                                        div.className = "z-btn";
                                        var num = parseInt(div.innerText) - 1;
                                        div.innerText = num;
                                    }
                                }
                            },
                            complete: function () {
                                lock = false;
                            }
                        })
            });
        });


    </script>
</head>
<body style="background:#242429">
<article class="item1">
    <section>
        <img src="${activity.iosBigPic}">
    </section>
    <section class="title">
        <div class="t1">
            <h2>${activity.title}</h2>
            <%--<span>参与人数：<strong>${activity.useSum}</strong></span>--%>
        </div>
        <div class="t2">${activity.description}</div>
    </section>
</article>

<article class="item2">
    <h2>活动照片</h2>
    <article class="piclist3">
        <c:forEach var="dto" items="${list}">
            <div>
                <!--
                <a href="${URL_WWW}/kada/activity/contentpage/${dto.content.cid}?uno=${uno}">
                -->
                <a href="joymekada://joymeapp?msgtype=4&info=${dto.content.cid}">
                    <div class="box">
                        <div><img src="${dto.profile.icon}">${dto.profile.username}</div>
                        <img class="t" src="${dto.content.pic.pic_s}">
                    </div>
                </a>

                <a href="#" name="agree" data-cid="${dto.content.cid}">
                    <div class="z-btn <c:if test="${dto.content.isagree}">vv</c:if>"
                         data-cid="${dto.content.cid}">${dto.content.agreenum}</div>
                </a>
            </div>
        </c:forEach>
    </article>
</article>
<c:if test="${page.maxPage>1}">
    <div class="getmore">
        <c:if test="${page.curPage>1}">
            <a href="${URL_WWW}/kada/activity/wcapage?aid=${activity.activityId}&uno=${uno}&pagenum=${page.curPage-1}">上一页</a>
        </c:if>

        <c:if test="${page.curPage<page.maxPage}">
            <a href="${URL_WWW}/kada/activity/wcapage?aid=${activity.activityId}&uno=${uno}&pagenum=${page.curPage+1}">下一页</a>
        </c:if>
    </div>
</c:if>

</body>
</html>