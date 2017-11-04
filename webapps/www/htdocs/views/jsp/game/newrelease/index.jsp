<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <meta name="Keywords" content="着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人">
    <meta name="description"
          content="着迷网（Joyme.com）是一个以游戏为主题的游戏玩家社区，记录你的游戏生活和情感 ，相遇结交志同道合的朋友，互动属于自己的游戏文化 ，有趣、新鲜的游戏话题，每天等你来讨论!,着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人">
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon">
    <title>着迷网Joyme.com</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>

</head>
<body>
<!--header-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<!-- header结束 -->
<!-- 手游预告开始 -->
<!-- slogan -->
<div class="post-game-slogan"><h1>让最好的游戏，找到最好的渠道</h1></div>
<!-- 游戏类别导航 -->
<div class="post-game-category">
    <ul class="clearfix" id="ul_tag_top">
        <li class="<c:if test="${tagId==null && !unIsTop}">current</c:if>" name="li_top_tag"><a
                href="javascript:void(0);" tag_top_id="" name="top_tag">全部</a></li>
        <c:forEach items="${newGameTagTopList}" var="tag" varStatus="st">
            <li class="<c:if test="${tagId==tag.newReleaseTagId}">current</c:if>" name="li_top_tag">
                <a href="javascript:void(0);" name="top_tag" tag_top_id="${tag.newReleaseTagId}">${tag.tagName}</a>
            </li>
        </c:forEach>
        <li class="<c:if test="${unIsTop!=null && unIsTop}">current</c:if>" name="li_top_tag"><a
                href="javascript:void(0);"
                name="top_tag" tag_top_id=""
                unistop="true" class="noborder">其他</a>
        </li>
    </ul>
</div>
<div class="post-game-box clearfix">
    <!-- left -->
    <div class="posted-game clearfix">
        <!-- 分类 -->
        <div class="posted-game-category">
            <h2>新移动游戏预告平台</h2>
            <ul>
                <c:forEach items="${displayOrderTypeCollection}" var="ordertype">
                    <li><a href="javascript:void(0);" name="li_order" order="${ordertype.value}"><fmt:message
                            key="gameres.newgame.displayordertype.${ordertype.value}" bundle="${userProps}"/></a></li>
                </c:forEach>
            </ul>
            <div>
                <label><input type="checkbox" name="coopratetype" value="1"
                              <c:if test="${cooprateType.hasExclusive()}">checked="checked"</c:if>>独代</label>&nbsp;&nbsp;&nbsp;
                <label><input type="checkbox" name="coopratetype" value="2"
                              <c:if test="${cooprateType.hasBenefit()}">checked="checked"</c:if>>分成</label>&nbsp;&nbsp;&nbsp;
            </div>
        </div>

        <!-- 游戏列表 -->
        <div class="posted-game-list">
            <c:choose><c:when test="${list.size()>0}">
                <c:forEach items="${list}" var="dto">
                    <dl>
                        <dt><a href="/newrelease/detail?infoid=${dto.newRelease.newReleaseId}"><img
                                src="${dto.newRelease.newGameIcon}"></a></dt>
                        <dd class="dd1">
                            <h2>
                                <a href="/newrelease/detail?infoid=${dto.newRelease.newReleaseId}">${dto.newRelease.newGameName}</a>
                            </h2>

                            <p>
                                <c:if test="${dto.newRelease.cooprateType.hasExclusive()}">独代&nbsp;</c:if>
                                <c:if test="${dto.newRelease.cooprateType.hasBenefit()}">分成&nbsp;</c:if>
                            </p>

                            <p><fmt:formatDate value="${dto.newRelease.publishDate}" pattern="yyyy年MM月"/>上市</p>

                            <p>${dto.newRelease.companyName}</p>

                            <p><c:forEach items="${dto.newReleaseTagList}"
                                          var="tag">${tag.tagName}&nbsp;</c:forEach></p>
                        </dd>
                        <dd class="dd2">${dto.city.cityName}</dd>
                    </dl>
                </c:forEach>
            </c:when>
                <c:otherwise>
                    <dl>没有您要搜索的数据</dl>
                </c:otherwise></c:choose>
        </div>
        <!-- 分页 -->
        <div class="pagecon clearfix" id="div_page">
            <c:set var="pageurl" value="${ctx}/newrelease"/>
            <c:if test="${pageParam.length()>0}">
                <c:set var="pageparam" value="${pageParam}"/>
            </c:if>
            <%@ include file="/views/jsp/page/page.jsp" %>
        </div>
    </div>
    <!-- right -->
    <div class="posted-game-right">
        <a href="/newrelease/createpage" class="post-game-btn" id="a_button">提交游戏报告</a>

        <!-- 热门标签 -->
        <div class="posted-game-item">
            <h2>热门<span>标签</span></h2>

            <div class="post-game-hot-tag">
                <ul>
                    <c:forEach items="${newGameTagHotList}" var="tag" varStatus="st">
                        <li class="" name="li_hot_tag"><a href="javascript:void(0);" <c:choose>
                            <c:when test="${st.index%6==0}">
                                style="background:#68a2d0"
                            </c:when>
                            <c:when test="${st.index%6==1}">
                                style="background:#9c82cc"
                            </c:when>
                            <c:when test="${st.index%6==2}">
                                style="background:#fb8466"
                            </c:when>
                            <c:when test="${st.index%6==3}">
                                style="background:#81cda6"
                            </c:when>
                            <c:when test="${st.index%6==4}">
                                style="background:#68a2d0"
                            </c:when>
                            <c:when test="${st.index%6==5}">
                                style="background:#68a2d0"
                            </c:when>
                        </c:choose> tag_hot_id="${tag.newReleaseTagId}">${tag.tagName}</a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <!-- 所在城市 -->
        <div class="posted-game-item">
            <h2>所在<span>城市</span></h2>

            <div class="post-game-city">
                <ul>
                    <li><a href="javascript:void(0);" name="li_city" class="<c:if test="${cityId==null}">current</c:if>"
                           city_id="">全部</a></li>
                    <c:forEach items="${cityList}" var="city" varStatus="st">
                        <li><a href="javascript:void(0);" name="li_city"
                               class="<c:if test="${cityId==city.cityId}">current</c:if>"
                               city_id="${city.cityId}">${city.cityName}</a></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/newrelease-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>

</html>
