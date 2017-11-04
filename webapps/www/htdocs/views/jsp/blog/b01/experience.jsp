<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>${profile.blog.screenName} ${webSiteInfo.screenName} ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>

</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <%@ include file="blog-headicon.jsp"%>
        <div class="con_ft"></div>
        <div class="con_tab clearfix">
            <ul>
                <li><a href="${URL_WWW}/people/${profile.blog.domain}"><span>全部动态</span></a></li>
                <li><a href="${URL_WWW}/profile/favorite/${profile.blog.uno}"><span><c:choose><c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">我</c:when><c:when test="${profile.detail.sex!=null && profile.detail.sex eq '0'}">她</c:when><c:otherwise>他</c:otherwise></c:choose>的喜欢</span></a> </li>
                <li><a href="${URL_WWW}/profile/experience/${profile.blog.uno}" class="on"><span>个人资料</span></a></li>
            </ul>
        </div>
        <div class="con_wrap"></div>
        <div class="con_hd01"></div>
        <div class="con_area con_l clearfix">
            <!-- 个人信息展示  -->
            <c:if test="${(userSession==null || userSession.blogwebsite.uno!=profile.blog.uno)&& (!profileExperienceDTO.showExpSchool || fn:length(profileExperienceDTO.schoolExp)==0) &&(!profileExperienceDTO.showExpComp || fn:length(profileExperienceDTO.companyExp)==0) && fn:length(playedGameList)==0}">
                <p class="addgameshowlist companny_mg clearfix"><c:choose><c:when
                        test="${profile.detail.sex==null || profile.detail.sex=='1'}">他</c:when><c:when
                        test="${profile.detail.sex=='0'}">她</c:when></c:choose>还没有设置任何资料哦！</p>
            </c:if>
            <c:if test="${(userSession!=null && userSession.blogwebsite.uno==profile.blog.uno) || (profileExperienceDTO.showExpSchool && fn:length(profileExperienceDTO.schoolExp)>0)||(profileExperienceDTO.showExpComp && fn:length(profileExperienceDTO.companyExp)>0)}">
                <div class="data_set_blog">
                    <c:if test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                        <div class="set_date_about"><a href="${ctx}/profile/customize/experience">设置个人信息</a></div>
                    </c:if>
                    <c:choose>
                        <c:when test="${profileExperienceDTO.showExpSchool && fn:length(profileExperienceDTO.schoolExp)>0}">
                            <div class="set_adduserdata clearfix">
                                <p>学校</p>

                                <div class="set_adduserdata_list">
                                    <ul>
                                        <c:forEach var="exp" items="${profileExperienceDTO.schoolExp}" varStatus="st">
                                            <li><c:out value="${exp.expName}"/></li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                                    <p class="experience_tips">你还没有设置个人学校信息，<a href="${ctx}/profile/customize/experience">马上设置</a>
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <%--<p class="experience_tips">该用户未设置个人学校信息或者设置了查看权限</p>--%>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${profileExperienceDTO.showExpComp && fn:length(profileExperienceDTO.companyExp)>0}">
                            <div class="set_adduserdata clearfix">
                                <p>单位</p>

                                <div class="set_adduserdata_list">
                                    <ul>
                                        <c:forEach var="exp" items="${profileExperienceDTO.companyExp}" varStatus="st">
                                            <li><c:out value="${exp.expName}"/></li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                                    <p class="experience_tips">你还没有设置个人单位信息，<a href="${ctx}/profile/customize/experience">马上设置</a>
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <%--<p class="experience_tips">该用户未设置个人单位信息或者设置了查看权限</p>--%>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <!-- playedgame -->
            <c:if test="${(userSession!=null && userSession.blogwebsite.uno==profile.blog.uno) ||fn:length(playedGameList)>0}">
                <div class="addgameshowlist companny_mg clearfix">
                    <c:if test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                        <div class="set_date_about">
                            <a href="${ctx}/profile/customize/game">设置游戏资料</a>
                        </div>
                    </c:if>
                    <c:choose>
                        <c:when test="${fn:length(playedGameList)>0}">
                            <c:forEach var="game" items="${playedGameList}" varStatus="st">
                                <h3>${game.gameName}</h3>
                                <c:if test="${fn:length(game.gameRoleSet.gameRoles)>0}">
                                    <ul>
                                        <c:forEach var="role" items="${game.gameRoleSet.gameRoles}" varStatus="status">
                                            <li>
                                                <p>${role.roleName}</p>

                                                <p>${role.serverArea}</p>

                                                <p>${role.guild}</p>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                                    你还没有设置游戏角色资料，<a href="${ctx}/profile/customize/game">马上设置</a>
                                </c:when>
                                <c:otherwise>
                                    该用户未添加任何游戏角色资料
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
        <div class="con_ft"></div>
    </div>
    <%@ include file="bloglist-right.jsp" %>
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/blog-init.js')
</script>

<script type="text/javascript">
    var ct = "Ctest_30";
    if (joyconfig.DOMAIN == "joyme.com") {
        ct = "Czhaomi";
    }
    ;
    var BFD_ITEM_INFO = {
        artic_userId:"${profile.blog.uno}",
        star_name: "${profile.blog.screenName}",
        artic_domain:"${profile.blog.domain}",
        star_link:"${URL_WWW}/people/${profile.blog.domain}",
        star_signat:"${profile.blog.description}",
        user_id:"${empty userSession.blogwebsite.uno ? 0 : userSession.blogwebsite.uno}",
        client:ct
    };
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
