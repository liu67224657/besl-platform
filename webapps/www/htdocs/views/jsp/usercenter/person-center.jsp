<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"
          name="viewport">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <title>
        个人中心首页
    </title>

    <link href="//cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/screen.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/common.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/index.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/setting.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/grzx.css">
    <link rel="stylesheet" href="${URL_STATIC}/pc/userEncourageSys/css/joymedialog.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/head-skin.css">
    <link rel="stylesheet" type="text/css" href="${URL_STATIC}/pc/userEncourageSys/css/userInfocc.css">

    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded',
                function () {
                    document.addEventListener('touchstart',
                            function () {
                                return false
                            },
                            true)
                },
                true);
        var userid = '${userinfoDTO.uid}';
        var profileId = '${userinfoDTO.profileId}';
    </script>
</head>

<body ontouchstart="">
<%@ include file="user-center-header.jsp" %>
<div class="container">
    <div class=" personal-info fn-clear col-md-12">
        <div class="fn-clear">
            <div class="col-md-11 info-r fn-clear">

                <div class="info-user  fn-clear">
                    <div>
                        <a href="${URL_UC}/usercenter/customize/modifyhead" class="user-login user-head-img"
                           data-id="${userinfoDTO.profileId}">
                            <img src="${userinfoDTO.icon}"
                                 alt="">
                            <c:if test="${not empty userinfoDTO.vtype&& userinfoDTO.vtype>0}">
                                <cite class="vip" title="${userinfoDTO.vdesc}">
                                </cite>
                            </c:if>

                            <span class="head-dec-def <c:if test="${not empty userinfoDTO.headskin}">head-dec-0${userinfoDTO.headskin}</c:if>">
                                    </span>
                        </a>
                        <div class="user-intro-con">
                            <cite class="user-des fn-clear">
                                <font class="nickname">
                                    ${userinfoDTO.nick}
                                </font>
                                <c:if test="${userinfoDTO.sex eq '1'}">
                                    <em class="user-sex  man">
                                    </em>
                                </c:if>
                                <c:if test="${userinfoDTO.sex eq '0'}">
                                    <em class="user-sex female">
                                    </em>
                                </c:if>
                                <a href="${URL_UC}/usercenter/customize/personinfo" class="link-set web-hide">
                                </a>
                            </cite>
                            <c:set var="desc" value="一句话介绍一下自己吧。"/>
                            <c:if test="${not empty userinfoDTO.desc}">
                                <c:set var="desc" value="${userinfoDTO.desc}"/>
                            </c:if>
                            <a href="javascript:;" class="user-intr" title="${desc}">
                                ${desc}
                            </a>
                            <%--<div class="web-jifen web-show">--%>
                                <%--当前--%>
                                <%--<i name="userpoint">--%>
                                    <%--${userPoint.userPoint}--%>
                                <%--</i>--%>
                                <%--积分，今日获得--%>
                                <%--<i name="todaypoint">--%>
                                    <%--${todayPorint}--%>
                                <%--</i>--%>
                                <%--积分--%>
                            <%--</div>--%>
                        </div>
                    </div>
                </div>
                <div class="info-edit-num web-hide">
                    <cite class="count-num">
                        <a href="http://wiki.${DOMAIN}/home/index.php?title=%E7%89%B9%E6%AE%8A:%E7%9D%80%E8%BF%B7%E8%B4%A1%E7%8C%AE&userid=${userinfoDTO.uid}"
                           target="_blank">
                            <font><i><em id="totalEdits">0</em></i>次</font>
                            总共编辑
                        </a>
                    </cite>
                    <cite class="day-num">
                        <font><i><em id="todayEdits">0</em></i>次</font>
                        今日编辑
                    </cite>
                </div>
            </div>

            <div class="col-md-11 active-num-wrap web-show">
                <div class="active-num">
                    <a href="/usercenter/fans/mylist">${userinfoDTO.fans}<br/>粉丝</a>
                    <cite></cite>
                    <a href="/usercenter/follow/mylist">${userinfoDTO.follows}<br/>关注</a>
                    <cite></cite>
                    <a href="http://wiki.${DOMAIN}/home/index.php?title=%E7%89%B9%E6%AE%8A:%E7%9D%80%E8%BF%B7%E8%B4%A1%E7%8C%AE&userid=${userinfoDTO.uid}">${userinfoDTO.edits}<br/>总共编辑</a>
                    <cite></cite>
                    <%--<a href="javascript:;">${userPoint.prestige}<br/>声望</a>--%>
                </div>
            </div>
            <!--移动端的膜拜-->
            <%--<c:if test="${not empty worshipProfileList}">--%>
                <%--<ul class="info0 web-show fn-clear">--%>
                    <%--<li class="fl prostrate-num">--%>
                        <%--<c:choose>--%>
                            <%--<c:when test="${totalWorshipNum>99}">--%>
                                <%--(99+)--%>
                            <%--</c:when>--%>
                            <%--<c:otherwise>--%>
                                <%--(${totalWorshipNum})--%>
                            <%--</c:otherwise>--%>
                        <%--</c:choose>--%>
                    <%--</li>--%>
                    <%--<li class="fr list-prostraters">--%>
                        <%--<div class="prostraters-direction prostraters-left " style="display: none;">--%>
                        <%--</div>--%>
                        <%--<div class="prostraters-direction prostraters-right" style="display: block;">--%>
                        <%--</div>--%>
                        <%--<div class="prostraters-box">--%>
                            <%--<ul class="clearfix prostraters-ul">--%>
                                <%--<c:forEach items="${worshipProfileList}" var="worshipProfile">--%>
                                    <%--<li class="prostraters fl">--%>
                                        <%--<a href="${URL_UC}/usercenter/page?pid=${worshipProfile.profileId}"--%>
                                           <%--target="_blank">--%>
                                            <%--<img class="prostraters-img" src="${worshipProfile.icon}">--%>
                                        <%--</a>--%>
                                    <%--</li>--%>
                                <%--</c:forEach>--%>
                            <%--</ul>--%>
                        <%--</div>--%>
                    <%--</li>--%>
                <%--</ul>--%>
            <%--</c:if>--%>
        </div>
    </div>
    <div class="">
        <div class="col-md-9">
            <div id="main" class="pag-hor-20 tab-box">
                <div class="trends-nav fn-clear tab-tit">
                    <a href="javascript:;" class="on">我的动态<i></i></a>
                    <a href="javascript:;">我的WIKI<i></i></a>
                    <a href="javascript:;">好友动态<i></i></a>
                </div>
                <div class="tab-con">
                    <!--我的动态 开始-->
                    <div class="my-trends-con on">
                        <ul class="my-trends-list">
                            <c:choose>
                                <c:when test="${not empty myTimeLineRows}">
                                    <c:forEach items="${myTimeLineRows}" var="userTimeline" varStatus="st">
                                        <li><p><i>我</i>&nbsp;
                                            <c:choose>
                                                <c:when test="${userTimeline.actionType.code eq 'focus_user'}">
                                                    <c:choose>
                                                        <c:when test="${fn:contains(userTimeline.extendBody,'关注了')}">
                                                            ${userTimeline.extendBody}
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${userTimeline.actionType.value}
                                                            &nbsp;<a href="${URL_UC}/usercenter/page?pid=${userTimeline.extendBody}" target="_blank">${userinfMap[userTimeline.extendBody].nick}</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    ${userTimeline.extendBody}
                                                </c:otherwise>
                                            </c:choose>

                                            <b class="time-stamp">
                                                <fmt:formatDate value="${userTimeline.createTime}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                            </b>
                                        </p>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${not empty myTimeLinePage&& myTimeLinePage.maxPage>1}">
                                        <div class="more-con" id="myTimeLine" style="display:block;">
                                            <button onclick="myTimeLine('')">
                                                点击加载更多...
                                            </button>
                                        </div>
                                    </c:if>

                                </c:when>
                                <c:otherwise>
                                    <div class="no-date">
                                        您在WIKI中什么都没有做还要什么动态！~？
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                    <!--我的WIKI 开始-->
                    <div class="trends-con">
                        <div class="no-date" id="wikinodata">
                            您还没有关注，贡献或管理任何WIKI
                        </div>

                        <!--管理WIKI 开始-->
                        <div class="manage-list" style="display: none" id="manageDiv">
                            <h3 class="list-tit glwiki-tit"><i></i>管理Wiki</h3>
                            <ul class="manage-item  fn-clear" id="manageUl">
                            </ul>
                            <div class="more-con" id="manageWikiMore" style="display:block;">
                                <button onclick="loadMoreManageWiki()">
                                    点击加载更多...
                                </button>
                            </div>
                        </div>
                        <!--管理WIKI 结束-->
                        <!--贡献的wiki 开始 -->
                        <div class="tj-list " id="contributeDiv" style="display: none">
                            <h3 class="list-tit gxwiki-tit">
                                <i>
                                </i>
                                贡献的wiki
                            </h3>
                            <ul class="list-item fn-clear " id="contributeUl">

                            </ul>
                            <div class="more-con" id="contributewikiMore" style="display:block;">
                                <button onclick="loadMoreoCntributeWiki()">
                                    点击加载更多...
                                </button>
                            </div>
                        </div>
                        <!--贡献的wiki 结束 -->
                        <!--关注的wiki 开始 -->
                        <div class="tj-list " id="followDiv" style="display:none;">
                            <h3 class="list-tit gzwiki-tit">
                                <i>
                                </i>
                                关注的wiki
                            </h3>
                            <ul class="list-item fn-clear row" id="followUl">

                            </ul>
                            <div class="more-con" id="followwikiMore" style="display:block;">
                                <button onclick="loadMoreFollowWiki()">
                                    点击加载更多...
                                </button>
                            </div>
                        </div>
                        <!--关注的wiki 结束 -->
                        <!--热门WIKI推荐 开始-->
                        <div class="tj-list " id="hotWikiDiv" style="display: none">
                            <h3 class="list-tit hotwiki-tit">
                                <i>
                                </i>
                                热门WIKI推荐
                            </h3>
                            <ul class="list-item fn-clear row" id="hotWikiUl">

                            </ul>
                            <div class="more-con">
                                <a href="http://wiki.joyme.com">
                                    更多WIKI
                                </a>
                            </div>
                        </div>
                        <!--热门WIKI推荐 结束-->
                    </div>
                    <!-- 好友动态 开始-->
                    <div class="friend-trends-con">
                        <ul class="friend-trends-list">
                            <c:choose>
                                <c:when test="${not empty friendTimeLineRows}">
                                    <c:forEach items="${friendTimeLineRows}" var="friendTimeline" varStatus="st">
                                        <li class="fn-clear">
                                            <div class="user-img">
                                                <cite>
                                                    <img src=" ${userinfMap[friendTimeline.destProfileid].icon}"
                                                         alt="img">
                                                    <span class="focus-def <c:if test="${not empty userinfMap[friendTimeline.destProfileid].headskin}">focus-dec-0${userinfMap[friendTimeline.destProfileid].headskin}</c:if>">  </span>
                                                    <c:if test="${not empty userinfMap[friendTimeline.destProfileid].vtype && userinfMap[friendTimeline.destProfileid].vtype>0}"><span
                                                            class="vip icon-vip"
                                                            title="${userinfMap[friendTimeline.destProfileid].vdesc}"></span></c:if>
                                                </cite>
                                            </div>
                                            <div class="col-md-12 user-text">
                                                <p>
                                                    <i class="user-name">
                                                        <a href="${URL_UC}/usercenter/page?pid=${userinfMap[friendTimeline.destProfileid].profileId}"
                                                           target="_blank">${userinfMap[friendTimeline.destProfileid].nick}</a>
                                                    </i>
                                                    <c:choose>
                                                        <c:when test="${friendTimeline.actionType.code eq 'focus_user'}">
                                                            <c:choose>
                                                                <c:when test="${fn:contains(friendTimeline.extendBody,'关注了')}">
                                                                    ${friendTimeline.extendBody}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${friendTimeline.actionType.value}
                                                                    &nbsp;<a href="${URL_WWW}/usercenter/page?pid=${friendTimeline.extendBody}" target="_blank">${userinfMap[friendTimeline.extendBody].nick}</a>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${friendTimeline.extendBody}
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <b class="time-stamp">
                                                        <fmt:formatDate value="${friendTimeline.createTime}"
                                                                        pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                                    </b>
                                                </p>
                                            </div>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${not empty friendTimeLinePage&& friendTimeLinePage.maxPage>1}">
                                        <div class="more-con" id="friendTimeLine" style="display:block;">
                                            <button onclick="friendTimeLine()">
                                                点击加载更多...
                                            </button>
                                        </div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <div class="no-date">
                                        您还没有任何好友动态，快去多关注一些好友吧。
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3 web-hide ">
            <div id="sidebar">
                <%--<div class="curren_fen"> //todo 用戶激励体系删除--%>
                    <%--<p>--%>
                        <%--<a class="jifen-link" href="${URL_UC}/usercenter/mypoint/list"> 当前--%>
                            <%--<i name="userpoint">--%>
                                <%--${userPoint.userPoint}--%>
                            <%--</i>--%>
                            <%--积分 ，今日获得--%>
                            <%--<i name="todaypoint">--%>
                                <%--${todayPorint}--%>
                            <%--</i>--%>
                            <%--积分--%>
                        <%--</a>--%>
                    <%--</p>--%>

                <%--</div>--%>
                <%--<div class="famous_like"> //todo 用戶激励体系删除 --%>
                    <%--<div class="sw_mb">--%>
                        <%--<span><em> ${userPoint.prestige}</em><br/>声望</span>--%>
                        <%--<cite></cite>--%>
                        <%--<span><em>${userPoint.worshipNum} </em> <br/>被膜拜</span>--%>
                    <%--</div>--%>
                    <%--<c:if test="${not empty worshipProfileList}">--%>
                        <%--<ul class="info0 fn-clear">--%>
                            <%--<li class="fl prostrate-num">--%>
                                <%--<c:choose>--%>
                                    <%--<c:when test="${totalWorshipNum>99}">--%>
                                        <%--(99+)--%>
                                    <%--</c:when>--%>
                                    <%--<c:otherwise>--%>
                                        <%--(${totalWorshipNum})--%>
                                    <%--</c:otherwise>--%>
                                <%--</c:choose>--%>
                            <%--</li>--%>
                            <%--<li class="fr list-prostraters">--%>
                                <%--<div class="prostraters-direction prostraters-left " style="display: none;">--%>
                                <%--</div>--%>
                                <%--<div class="prostraters-direction prostraters-right" style="display: block;">--%>
                                <%--</div>--%>
                                <%--<div class="prostraters-box">--%>
                                    <%--<ul class="clearfix prostraters-ul">--%>
                                        <%--<c:forEach items="${worshipProfileList}" var="worshipProfile">--%>
                                            <%--<li class="prostraters fl">--%>
                                                <%--<a href="${URL_UC}/usercenter/page?pid=${worshipProfile.profileId}">--%>
                                                    <%--<img class="prostraters-img" src="${worshipProfile.icon}">--%>
                                                <%--</a>--%>
                                            <%--</li>--%>
                                        <%--</c:forEach>--%>
                                    <%--</ul>--%>
                                <%--</div>--%>
                            <%--</li>--%>
                        <%--</ul>--%>
                    <%--</c:if>--%>
                <%--</div>--%>
                <!--我的关注 开始-->
                <c:if test="${not empty followList}">
                    <div class="fans-box fn-clear pag-hor-20">
                        <h3>
                            我的关注
                            <a href="/usercenter/follow/mylist" class="fn-right">
                                更多
                            </a>
                        </h3>
                        <div class="row fans-con">
                            <c:forEach items="${followList}" var="tempProfile" varStatus="st">
                                <a class=" col-md-3"
                                   href="/usercenter/page?pid=${tempProfile.profileId}">
                                    <cite class="user-head-img" data-id="${tempProfile.profileId}">
                                        <img src="${tempProfile.icon}" alt="img"/>

                                        <span class="focus-def <c:if test="${not empty tempProfile.headskin}">focus-dec-0${tempProfile.headskin}</c:if>">
                                                                            </span>
                                        <c:if test="${not empty tempProfile.vtype&&tempProfile.vtype>0}">
                                            <span class="vip icon-vip" title="${tempProfile.vdesc}"></span>
                                        </c:if>
                                    </cite>
                                    <font>
                                            ${tempProfile.nick}
                                    </font>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <!--我的关注 结束-->
                <!--我的粉丝 开始-->
                <c:if test="${not empty fansList }">
                    <div class="fans-box fn-clear pag-hor-20">
                        <h3>
                            我的粉丝
                            <a href="/usercenter/fans/mylist" class="fn-right">
                                更多
                            </a>
                        </h3>
                        <div class="row fans-con">
                            <c:forEach items="${fansList}" var="tempProfile" varStatus="st">
                                <a href="/usercenter/page?pid=${tempProfile.profileId}" class="col-md-3">
                                    <cite class="user-head-img " data-id="${tempProfile.profileId}">
                                        <img src="${tempProfile.icon}" alt="img">
                                        <span class="focus-def <c:if test="${not empty tempProfile.headskin}">focus-dec-0${tempProfile.headskin}</c:if>">
                                        </span>
                                        <c:if test="${not empty tempProfile.vtype&&tempProfile.vtype>0}">
                                            <span class="vip icon-vip" title="${tempProfile.vdesc}"></span>
                                        </c:if>
                                    </cite>
                                    <font>
                                            ${tempProfile.nick}
                                    </font>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <!--我的粉丝 结束-->
                <!--初心者 开始-->


            </div>
        </div>
    </div>
</div>


</div>
<%@ include file="user-center-footer.jsp" %>

<script type="text/javascript" src="http://static.joyme.com/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${URL_STATIC}/pc/userEncourageSys/js/openwindow.js"></script>

<script type="text/javascript"
        src="${URL_STATIC}/pc/userEncourageSys/js/bootstrap.js"></script>
<script src="${URL_STATIC}/pc/userEncourageSys/js/horizontalMove.js">
</script>
<script src="${URL_LIB}/static/js/usercenter/userWindow.js">
</script>
<script src="${URL_LIB}/static/js/usercenter/action.js">
</script>

<script type="text/javascript">
    var url = 'http://wiki.' + joyconfig.DOMAIN + '/home/joyme_api.php';
    $(document).ready(function () {
        $.ajax({
            url: url,
            type: "post",
            async: false,
            data: {'action': 'userwikiinfo', 'userid': userid},
            dataType: "jsonp",
            jsonpCallback: "testwiki",
            success: function (data) {
                var userprofiles = data.msg.userprofiles;
                $('#totalEdits').text(userprofiles.total_edit_count);
                $('#todayEdits').text(userprofiles.today_edit_count);
                var userwikis = data.msg.userwikis;
                var manageWikis = userwikis.manageWikis;
                var contributeWikis = userwikis.contributeWikis;
                var followWikis = userwikis.followWikis;
                var hotwikis = data.msg.hotwikis;
                var manageWikis_hasnext = userwikis.manageWikis_hasnext;
                var contributeWikis_hasnext = userwikis.contributeWikis_hasnext;
                var followWikis_hasnext = userwikis.followWikis_hasnext;
                if (userwikis != "") {
                    if (typeof(manageWikis) != "undefined" && manageWikis.length > 0) {
                        $('#wikinodata').css("display", "none");
                        $('#manageDiv').css("display", "block");
                        var innerHtml = '';
                        for (var i = 0; i < manageWikis.length; i++) {
                            innerHtml += '<li class="col-md-4">' +
                                    '<div class="manage-wiki">' +
                                    '<a href="' + manageWikis[i].site_url + '" target="_blank" class="mg-wiki-main fn-clear col-md-12">' +
                                    '<b class="manager web-hide">' + manageWikis[i].site_name + '</b>' +
                                    '<div class="col-md-12"><cite>' +
                                    '<img src="' + manageWikis[i].site_icon + '" alt=""> <i> ' + manageWikis[i].site_name + '</i></cite></div>' +
                                    '<div class="manager-text col-md-12"> <font> ' + manageWikis[i].site_name + '</font><span>页面总数量:' +
                                    manageWikis[i].page_count + '</span><span>编辑总次数:' +
                                    manageWikis[i].edit_count + '</span></div></a>' +
                                    '<a href="javascript:;" class="web-hide add-edit"><span> 关注人数：' +
                                    manageWikis[i].follow_usercount + ' </span> <span> 编辑人数：' +
                                    manageWikis[i].edituser_count + ' </span><span> 昨日编辑：' +
                                    manageWikis[i].yes_editcount + ' </span></a>' +
                                    '<i class="caret  count-icon web-hide"></i></div></li>';
                        }
                        $('#manageUl').append(innerHtml);
                        if (manageWikis_hasnext == '0') {
                            $('#manageWikiMore').css("display", "none");
                        }


                    }
                    if (typeof(contributeWikis) != "undefined" && contributeWikis.length > 0) {
                        $('#wikinodata').css("display", "none");
                        $('#contributeDiv').css("display", "block");
                        var innerHtml = '';
                        for (var i = 0; i < contributeWikis.length; i++) {
                            innerHtml += '<li class="col-md-4">' +
                                    '<a href="' + contributeWikis[i].site_url + '" target="_blank" >' +
                                    '<cite>' +
                                    '<img src="' + contributeWikis[i].site_icon + '" alt="img"> </cite>' +
                                    '<span><font>' + contributeWikis[i].site_name +
                                    '</font> <b>贡献总数:' +
                                    contributeWikis[i].offer_count + '</b>' +
                                    '<b> 昨日编辑：' +
                                    contributeWikis[i].yes_editcount + ' </b></span></a>' +
                                    '</li>';
                        }
                        $('#contributeUl').append(innerHtml);
                        if (contributeWikis_hasnext == '0') {
                            $('#contributewikiMore').css("display", "none");
                        }
                    }
                    if (typeof(followWikis) != "undefined" && followWikis.length > 0) {
                        $('#wikinodata').css("display", "none");
                        $('#followDiv').css("display", "block");
                        var innerHtml = '';
                        for (var i = 0; i < followWikis.length; i++) {
                            innerHtml += '<li class="col-md-4">' +
                                    '<a href="' + followWikis[i].site_url + '" target="_blank">' +
                                    '<cite>' +
                                    '<img src="' + followWikis[i].site_icon + '" alt="img"> </cite>' +
                                    '<span><font>' + followWikis[i].site_name +
                                    '</font> <b>页面总数:' +
                                    followWikis[i].page_count + '</b>' +
                                    '<b> 昨日编辑：' +
                                    followWikis[i].yes_editcount + ' </b></span></a>' +
                                    '</li>';
                        }
                        $('#followUl').append(innerHtml);
                        if (followWikis_hasnext == '0') {
                            $('#followwikiMore').css("display", "none");
                        }
                    }
                }
                if (typeof(followWikis) != "undefined" && followWikis.length > 2) {
                    return;
                }
                if (typeof(hotwikis) != "undefined" && hotwikis.length > 0) {
                    $('#wikinodata').css("display", "none");
                    $('#hotWikiDiv').css("display", "block");
                    var innerHtml = '';
                    for (var i = 0; i < hotwikis.length; i++) {
                        innerHtml += '<li class="col-md-4">' +
                                '<a href="' + hotwikis[i].site_url + '" target="_blank">' +
                                '<cite>' +
                                '<img src="' + hotwikis[i].icon + '" alt="img"> </cite>' +
                                '<span><font>' + hotwikis[i].site_name +
                                '</font> <b>页面总数:' +
                                hotwikis[i].page_count + '</b>' +
                                '<b> 昨日编辑：' +
                                hotwikis[i].yes_editcount + ' </b></span></a>' +
                                '</li>';
                    }
                    $('#hotWikiUl').append(innerHtml);
                }
            }
        });
        var pid = getCookie('jmuc_pid');
        //如果没有登录弹登录框
        if (pid == null) {
            loginDiv();
            return;
        }
    });


</script>
</body>

</html>