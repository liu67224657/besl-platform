<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="KeyWords" content="${group.seoKeyWords}">
    <meta name="Description" content="${group.seoDescription}"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${group.resourceName} ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area  con_pd01 clearfix">
            <!--game-navigation-->
            <%@ include file="/views/jsp/group/group-navigation.jsp" %>
            <!--stab_hd-->
            <div class="stab_bd">
                <div class="baike clearfix">
                    <div class="tit_baike clearfix">

                        <h3>
                            <c:choose>
                                <c:when test="${fn:length(baikeDTOList)>0}">
                                   ${group.resourceName}攻略百科
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="baike.content.empty" bundle="${userProps}"/>
                                </c:otherwise>
                            </c:choose>
                        </h3>

                        <c:choose>
                            <c:when test="${baikePrivacy}">
                                <a href="/baike/${group.gameCode}/editpage" class="edit_baike">编辑百科</a>
                            </c:when>
                            <c:otherwise>
                                    <c:if test="${fn:length(modifyProfile)>0}">
                                <div class="editor_con clearfix">
                                    <strong>最近编辑者：</strong>
                                    <c:forEach var="profile" items="${modifyProfile}" varStatus="st">
                                        <a href="${URL_WWW}/people/${profile.blog.domain}" target="_blank" name="atLink"
                                           title="${profile.blog.screenName}">
                                                ${profile.blog.screenName}</a><c:if
                                            test="${!st.last}">，</c:if></c:forEach>
                                </div>
                            </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>
                        <c:if test="${fn:length(baikeDTOList)>0}">
                    <c:forEach var="baike" items="${baikeDTOList}">
                        <div class="bk_con  mt17">
                            <div class="bk_hd"><h3>${baike.category.categoryName}</h3></div>
                            <c:if test="${fn:length(baike.itemsByCategory)>0}">
                                <div class="bk_bd">
                                    <c:forEach var="items" items="${baike.itemsByCategory}">
                                        <span class="bk_list"><a href="${items.displayInfo.linkUrl}" target="_blank"  <c:if test="${items.displayInfo!=null && fn:length(items.displayInfo.extraField1)>0}">style="color:${items.displayInfo.extraField1}"</c:if>>${items.displayInfo.subject}</a></span>
                                    </c:forEach>
                                </div>
                            </c:if>
                            <c:if test="${fn:length(baike.children)>0}">
                                <div class="bk_bd">
                                    <c:forEach var="children" items="${baike.children}">
                                        <div class="bk_bd_hd"><h3>${children.category.categoryName}</h3></div>
                                        <c:if test="${fn:length(children.itemsByCategory)>0}">
                                            <div class="bk_bd_bd">
                                                <c:forEach var="items" items="${children.itemsByCategory}">
                                                    <span class="bk_list">
                                                        <a href="${items.displayInfo.linkUrl}" target="_blank" <c:if test="${items.displayInfo!=null && fn:length(items.displayInfo.extraField1)>0}">style="color:${items.displayInfo.extraField1}"</c:if>>${items.displayInfo.subject}</a></span>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>

                        </c:if>
                </div>
                <!--baike-->
            </div>
        </div>
        <!--con_area-->
        <div class="con_ft"></div>
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