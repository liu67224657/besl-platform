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

    <%--<script src="${URL_LIB}/static/js/common/seajs.js"></script>--%>
    <%--<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>--%>
    <%--<script>--%>
    <%--seajs.use("${URL_LIB}/static/js/init/common-init.js")--%>
    <%--</script>--%>
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
        <li class="<c:if test="${unIsTop!=null && unIsTop}">current</c:if>" name="li_top_tag"><a href="javascript:void(0);"
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
                <label><input type="checkbox" name="coopratetype" value="1">独代</label>&nbsp;&nbsp;&nbsp;
                <label><input type="checkbox" name="coopratetype" value="2">分成</label>&nbsp;&nbsp;&nbsp;
            </div>
        </div>

        <!-- 游戏列表 -->
        <div class="posted-game-detail-intro">
            <a id="a_modify" href="/newrelease/modifypage?infoid=${dto.newRelease.newReleaseId}" class="modify">[编辑游戏]</a>
            <dl>
                <dt><img src="${dto.newRelease.newGameIcon}"></dt>
                <dd>
                    <h2>${dto.newRelease.newGameName}</h2>

                    <h3>游戏简介：</h3>

                    <p>${dto.newRelease.newGameDesc}</p>

                    <div class="wiki-share-type1-center">
                        <span>把这个游戏分享到：</span>
                        <a target="_blank" href="${URL_WWW}/share/newrelease/sinaweibo/bind?sid=${dto.newRelease.shareId}" class="share_sina"></a>
                        <a target="_blank" class="share_tengxun" href="${URL_WWW}/share/newrelease/qweibo/bind?sid=${dto.newRelease.shareId}"></a>
                        <a target="_blank" class="share_qq" href="${URL_WWW}/share/newrelease/qq/bind?sid=${dto.newRelease.shareId}"></a>
                    </div>
                </dd>
            </dl>
        </div>
        <div class="posted-game-detail-img">
            <div class="posted-game-detail-img-box">
                <a href="javascript:void(0)" id="scroll-btn-left"></a>
                <a href="javascript:void(0)" id="scroll-btn-right"></a>
                <div>
                    <!-- 轮播时，控制ul的left值 -->
                    <ul id="ul_pic">
                        <c:forEach items="${dto.newRelease.newGamePicSet.picSet}" var="pic" varStatus="st">
                            <li <c:choose><c:when test="${st.index==0}">class="current" style="display: block;"</c:when><c:otherwise>style="display: none;"</c:otherwise></c:choose> id="${st.index}"><img src="${pic.picUrl}"></li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
        <div class="posted-game-company">
            <h2>商务合作信息</h2>

            <p><label>合作方式 ：</label><span><c:if test="${dto.newRelease.cooprateType.hasExclusive()}">独代&nbsp;</c:if>
                            <c:if test="${dto.newRelease.cooprateType.hasBenefit()}">分成&nbsp;</c:if></span></p>

            <p><label>预发布时间 ：</label><span><fmt:formatDate value="${dto.newRelease.publishDate}"
                                                           pattern="yyyy年MM月"/>上线</span></p>

            <p><label>游戏发行范围 ：</label><span><c:if test="${dto.newRelease.publishArea.hasInternal()}">国内&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasAsia()}">亚洲&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasSouthEastAsia()}">东南亚&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasJapanKorea()}">日韩&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasNorthAmerica()}">北美&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasSouthAmerica()}">南美&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasWestAsia()}">西亚&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasAfrican()}">非洲&nbsp;</c:if>
                                    <c:if test="${dto.newRelease.publishArea.hasEurope()}">欧洲</c:if></span></p>

            <p><label>企业/团队 ：</label><span>${dto.newRelease.companyName}</span></p>

            <p><label>团队规模 ：</label><span><fmt:message
                    key="gameres.newgame.peoplenumtype.${dto.newRelease.peopleNumType.value}"
                    bundle="${userProps}"/></span></p>

            <p><label>联 系 人 ：</label><span>${dto.newRelease.contacts}</span></p>

            <p><label>邮 箱 ：</label><span>${dto.newRelease.email}</span></p>

            <p><label>联系电话 ：</label><span>${dto.newRelease.phone}</span></p>

            <p><label>Q Q ：</label><span>${dto.newRelease.qq}</span></p>

            <p><label>所在城市 ：</label><span>${dto.city.cityName}</span></p>

            <p><label>标 签 ：</label><span><c:forEach items="${dto.newReleaseTagList}"
                                                    var="tag">${tag.tagName}&nbsp;</c:forEach></span></p>
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
    seajs.use('${URL_LIB}/static/js/init/newrelease-detail-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>

</html>
