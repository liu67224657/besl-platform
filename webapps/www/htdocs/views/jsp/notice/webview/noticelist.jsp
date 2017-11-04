<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>评论&回复</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet"
          href="${URL_STATIC}/pc/userEncourageSys/css/dropload.css">
    <link rel="stylesheet" type="text/css"
          href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
            document.addEventListener('touchstart', function () {
                return false
            }, true)
        }, true);


    </script>
</head>
<body>
<!-- 导航 开始 -->
<c:import url="/views/jsp/usercenter/user-center-header.jsp"/>
<!-- 导航 结束 -->
<!-- 内容区  开始 -->
<div class="container">
    <div class="row">
        <!-- 左侧区域 开始 -->
        <div class="col-md-9">
            <div id="main">
                <div class="zan-list-box ">
                    <h1 class="page-h1 pag-hor-20 fn-clear">
                        评论&回复
                        <span class="del-all fn-right" id="notice_delete"> <i class="fa fa-trash-o"></i>清空所有</span></h1>
                    <ul class="list-item ">
                        <c:choose>
                            <c:when test="${empty list}">
                                <div class="no-data">
                                    <cite class="no-data-img"></cite>
                                    <p></p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${list}" var="notice">
                                    <li>
                                        <div class="list-item-l">
                                            <cite class="user-head-img"  data-id="${profileMap[notice.body.destProfileId].profileId}">
                                                <a href="${URL_UC}/usercenter/page?pid=${profileMap[notice.body.destProfileId].profileId}"   target="_blank">
                                                    <img src="${profileMap[notice.body.destProfileId].icon}">
                                                    <c:if test="${profileMap[notice.body.destProfileId].vtype>0}">
                                                        <span class="user-vip" title="${profileMap[notice.body.destProfileId].vdesc}"></span>
                                                    </c:if>
                                                </a>
                                                <c:if test="${not empty profileMap[notice.body.destProfileId].headskin}">
                                                    <span class="dianzan-def focus-dec-0${profileMap[notice.body.destProfileId].headskin}"> </span>
                                                </c:if>
                                            </cite>

                                        </div>
                                        <div class="list-item-r situatio-one">
                                            <div class="item-r-name ">
                                                <a href="${URL_UC}/usercenter/page?pid=${profileMap[notice.body.destProfileId].profileId}"   target="_blank">${profileMap[notice.body.destProfileId].nick}</a>在“${notice.body.pageurl}”中
                                                <c:if test="${notice.body.wikiNoticeDestType.code eq 1}">
                                                    发表了评论：
                                                </c:if>
                                                <c:if test="${notice.body.wikiNoticeDestType.code eq 2}">
                                                    回复了我：
                                                </c:if>
                                                <c:if test="${notice.body.wikiNoticeDestType.code eq 3}">
                                                    回复了<a href="${URL_UC}/usercenter/page?pid=${profileMap[notice.body.otherProfileId].profileId}" target="_blank">${profileMap[notice.body.otherProfileId].nick}</a>：
                                                </c:if>
                                            </div>
                                            <div class="item-r-text">
                                                    ${notice.body.contenturl}
                                            </div>
                                            <div class="item-r-other fn-clear">
                                                <b class="from-wiki">出自：${notice.body.desc}</b>
                                                <b class="time-stamp">
                                                    <fmt:formatDate value="${notice.createTime}"
                                                                    pattern="yyyy-MM-dd HH:mm:ss"/>
                                                </b>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
                <div class="pagecon clearfix">
                    <c:set var="pageurl" value="${URL_UC}/usercenter/notice/${noticeType}"/>
                    <%@ include file="/views/jsp/page/wiki-page.jsp" %>
                </div>
            </div>
        </div>
        <!-- 左侧区域 结束 -->
        <c:import url="/views/jsp/notice/webview/notice-right.jsp"/>

    </div>
</div>
<!-- 内容区  结束 -->
<!-- 页脚 开始 -->
<input type="hidden" id="noticetype" value="${noticeType}"/>
<input type="hidden" value="${page.curPage}" id="curpage"/>
<input type="hidden" value="${page.maxPage}" id="maxpage"/>
<%@ include file="/views/jsp/usercenter/user-center-footer.jsp" %>

<!-- 页脚 结束 -->
<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>

<script type="text/javascript"
        src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script type="text/javascript"
        src="${URL_STATIC}/pc/userEncourageSys/js/dropload.min.js"></script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js">
</script>
<script src="${URL_LIB}/static/js/usercenter/action.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/personal_dec.js"></script>
<script>
    $(document).ready(function () {
        var login = '${isnotlogin}'; //如果没有登录弹登录框
        if (login == 'true') {
            loginDiv();
        }
    });
</script>
</body>
</html>