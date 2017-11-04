<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="robots" content="noindex.nofollow">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <meta name="Keywords" content="手机游戏礼包领取,手游兑换码,手游激活码">
    <meta name="description"
          content="着迷网礼包中心为手游玩家提供各种手游礼包,其中包括手机游戏激活码,特权码,测试礼包,新手礼包,兑换码，节假日礼包等,还有多种着迷专属礼包等你拿,多种手游礼包尽在着迷网!"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>着迷网</title>
    <link href="${URL_LIB}/static/theme/default/css/header.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="http://static.joyme.com/pc/cms/jmsy/page/css/video.css?v20151228" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>

</head>
<body>
<div id="joyme-wrapper">
    <!--header-->
    <c:import url="/views/jsp/passport/new-header.jsp"/>
    <!--header==end-->
    <!--joyme标题-->
    <div class="joyme-bt fn-nav">

    </div>
    <!--joyme标题end-->
    <!--joyme-center-->
    <div class="joyme-center joyme-center-list">
        <!--第四块-->
        <div class="joyme-box-four joyme-zzmj-box fn-clear">
            <!--joyme-zzmj-->
            <div class="joyme-video-list joyme-zzmj fn-left">
                <h2 class="joyme-title fn-clear"><span class="fn-left">测试页面</span>

                    <p class="title-p"></p></h2>
                <div class="joyme-video-ul fn-clear">
                    <div class="pt-tabMenu fn-clear">
                    </div>
                    <ul class="com-tab">
                        <c:if test="${list != null && list.size() > 0}">
                            <c:forEach items="${list}" var="item" varStatus="st">
                                <li style="width: 240px;height:260px;">
                                    <video width="240px" height="140px" controls="controls" poster="${item.uri}?vframe/jpg/offset/1" preload="none">
                                        <source src="${item.uri}" type="video/mp4" />
                                    </video>
                                    <p style="height: inherit;word-break: break-all">
                                        <b>${item.name}</b><br/>
                                        <span>${item.desc}</span>
                                    </p>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ul>
                </div>
                <!--pagecon-->
                <div class="pagecon clearfix">
                    <c:set var="pageurl" value="${URL_WWW}/comment/videos"/>
                    <%@ include file="/views/jsp/page/page.jsp" %>
                </div>
                <!--pagecon==end-->
            </div>
            <!--joyme-zzmj==end-->
            <!--右-->

            <!--右end-->
        </div>
        <!--第四块 end-->
    </div>
    <!-- footer Start -->
    <%@ include file="/views/jsp/tiles/new-footer.jsp" %>
    <!--返回顶部-->
    <div class="retuen-top" style="left: 1261.5px; display: none;">
        <span class="rt-btn">返回顶部</span>
    </div>
    <!--返回顶部-->
    <!--footer==end-->
</div>
</body>
</html>
