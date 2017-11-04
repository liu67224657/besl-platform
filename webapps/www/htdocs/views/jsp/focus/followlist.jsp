<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/views/jsp/common/meta.jsp" %>
    <title>我的关注 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/follow_init');
    </script>
    <script type="text/javascript">
        var typeLength =${fn:length(cateList)};
         var currentCateId=0;
        var currentPage=${page.curPage};
        var totalRows=${page.totalRows};
        <c:if test="${tHighlight!='all' && tHighlight!='none'}">
           var currentCateId=${tHighlight};
        </c:if>
    </script>
</head>

<body>
<!--头部开始-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<!--头部结束-->
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area con_blog clearfix">
            <div class="friend-title-noline">你关注了<span id="follow_title_count"><c:out value="${userSession.countdata.focusSum}"/></span>人</div>
            <div class="group-box" id="categroy_list">
                 <c:choose>
                    <c:when test="${tHighlight=='all'}">
                        <a href="javascript:void(0);" class="hover">全部</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/social/follow/list" >全部</a>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${tHighlight=='none'}">
                        <a href="javascript:void(0);" class="hover">未分组</a>
                    </c:when>
                    <c:otherwise>
                        <a href="/social/follow/grouplist">未分组</a>
                    </c:otherwise>
                </c:choose>
                <c:forEach var="cate" items="${cateList}" varStatus="st" begin="0" end="3">
                    <c:choose>
                        <c:when test="${tHighlight!='none' && tHighlight!='all' &&  tHighlight eq cate.value.cateId}">
                            <a href="javascript:void(0);" title="<c:out value="${cate.value.cateName}"/>" class="hover">
                                <c:choose>
                                    <c:when test="${fn:length(cate.value.cateName)<5}">
                                        <c:out value="${cate.value.cateName}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${fn:substring(cate.value.cateName,0,4)}"/>...
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="/social/follow/grouplist?cateId=<c:out value="${cate.value.cateId}"/>" title="<c:out value="${cate.value.cateName}"/>">
                                <c:choose>
                                    <c:when test="${fn:length(cate.value.cateName)<5}">
                                        <c:out value="${cate.value.cateName}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${fn:substring(cate.value.cateName,0,4)}"/>...
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${fn:length(cateList)>4}">
                    <div class="group-more-box">
                        <a class="group-more" href="javascript:void(0);" id="g_menu_more">更多</a>
                        <div class="group-more-select" id="g_more_select" style="display:none">
                            <ul class="group_more" id="ul_g_menu">
                            <c:forEach var="cate" items="${cateList}" varStatus="st" begin="4">
                                <li><a href="${URL_WWW}/social/follow/grouplist?cateId=<c:out value="${cate.value.cateId}"/>&hideCate=true"><c:choose>
                                    <c:when test="${fn:length(cate.value.cateName)<6}">
                                        <c:out value="${cate.value.cateName}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${fn:substring(cate.value.cateName,0,5)}"/>...
                                    </c:otherwise>
                                    </c:choose>
                                </li></a>
                            </c:forEach>
                        </ul>
                        </div>
                    </div>
                </c:if>
                <div class="create-group"><a class="submitbtn" id="but_add_type" href="javascript:void(0);"><span>创建分组</span></a></div>
            </div>
            <div class="group-box-low clearfix">
                <a href="javascript:void(0);"><span class="groupiconh"></span>该分组下共<span id="group_count"><c:out value="${page.totalRows}"/></span>人</a>
                <c:if test="${tHighlight!=null && tHighlight!='all' && tHighlight!='none'}">
                    <a href="javascript:void(0);" title="" id="modify_cate"><span class="groupiconp"></span>修改分组名称</a>
                    <a href="javascript:void(0);" id="del_cate"><span class="groupiconx"></span>删除该分组</a>
                </c:if>
            </div>
            <c:choose>
                <c:when test="${fn:length(list)>0}">
                    <%@ include file="/views/jsp/focus/profile-list.jsp" %>
                </c:when>
                <c:otherwise>
                    <div class="uncontent clearfix" ><fmt:message key="follow.my.empty" bundle="${userProps}"/></div>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${tHighlight=='all'}">
                    <c:set var="pageurl" value="${ctx}/social/follow/list"/>
                </c:when>
                <c:otherwise>
                    <c:set var="pageurl" value="${ctx}/social/follow/grouplist"/>
                    <c:if test="${fn:length(tHighlight)>0 && tHighlight!='none'}">
                        <c:set var="pageparam" value="cateId=${tHighlight}"/>
                     </c:if>
                </c:otherwise>
            </c:choose>
            <%@ include file="/views/jsp/page/page.jsp" %>
        </div>
        <div class="con_ft"></div>
    </div>
    <!--conleft结束-->
    <%@ include file="/views/jsp/tiles/rightmenu.jsp" %>
    <!--conright结束-->
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
