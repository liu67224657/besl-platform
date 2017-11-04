<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords"
          content="<c:choose><c:when test="${fn:length(group.seoKeyWords)>0}">${group.seoKeyWords}</c:when><c:otherwise>着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人</c:otherwise></c:choose>">
    <meta name="description"
          content="<c:choose><c:when test="${fn:length(group.seoDescription)>0}">${group.seoDescription}</c:when><c:otherwise>着迷网（Joyme.com）是一个以游戏为主题的游戏玩家社区，记录你的游戏生活和情感 ，相遇结交志同道合的朋友，互动属于自己的游戏文化 ，有趣、新鲜的游戏话题，每天等你来讨论!,着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人</c:otherwise></c:choose>"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${group.resourceName}_小组_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="sqcontent  square_hd01 clearfix">
<div class="location"><span>当前位置：</span><a href="${URL_WWW}">首页</a> &gt; <a href="${URL_WWW}/group">小组</a>&gt; ${group.resourceName}</div>
<div class="groupcon">
<div class="sqt" style="margin-top:0"></div>
<div class="sqc clearfix">

<!-- left -->
<div class="game-group-box-left">
<%@ include file="/views/jsp/group/group-info.jsp" %>
<div class="stab_hd clearfix">
    <ul class="stablist">
        <li>
            <a href="${URL_WWW}/group/${group.gameCode}/talk" class="on">
                <span>讨论区</span>
            </a>
        </li>
        <li>
            <a href="${URL_WWW}/group/${group.gameCode}/ess">
                <span>精华</span>
            </a>
        </li>
    </ul>
    <a class="send" id="postTalk" href="javascript:void(0)">发帖</a>
</div>

<div class="game-group-article">
    <c:choose>
    <c:when test="${fn:length(contentList)>0}">
    <ul>
        <li class="noborder">
            <div class="li-part1"><b>标题</b></div>
            <div class="li-part2"><b>作者</b></div>
            <div class="li-part3"><b>最后回复</b></div>
        </li>
    </ul>

    <%@ include file="/views/jsp/content/content-grouplist.jsp" %>

    <!-- 分页 -->
    <div class="pagecon clearfix">
        <c:set var="pageurl" value="${URL_WWW}/group/${group.gameCode}/talk"/>
        <%@ include file="/views/jsp/page/page.jsp" %>
    </div>
    </c:when>
    <c:otherwise>
         <p style="line-height:120px; text-align:center">
             <fmt:message key="group.content.empty" bundle="${userProps}"/>
         </p>
    </c:otherwise>
    </c:choose>
</div>
        <!-- 发布新帖 -->
    <div class="add-new-article">
        <!-- 里面的内容都是套用线上的 -->
        <div class="issue clearfix">
            <div class="issue_hd">发表新帖</div>
            <div class="issue_bd" id="post_area">
                <div class="edit ">
                    <%@ include file="/views/jsp/post/post-talk.jsp" %>
                    <div class="clearfix"></div>
                </div>
            </div>
            <!--issue_bd-->
        </div>
    </div>
</div>

<!-- right -->
<%@ include file="/views/jsp/group/group-right.jsp" %>

</div>
<div class="sqb"></div>
</div>
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>


<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/group-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>