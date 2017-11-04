<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<c:choose>
    <c:when test="${key!=null}">
        <c:set var="slt" value="${key}"/>
    </c:when>
    <c:otherwise>
        <c:set var="slt" value="无搜索内容!"/>
    </c:otherwise>
</c:choose>
<c:set var="holdstype" value="content"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>搜索${slt} ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var key = '<c:out value="${key}"/>';
    </script>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="sqcontent clearfix">
    <div class="wsearcht"></div>
    <div class="wsearchcon clearfix">
        <div class="searchl">
            <%@ include file="/views/jsp/search/hold-search.jsp" %>
            <c:choose>
                <c:when test="${fn:length(blogList)>0}">
                    <div class="result-report">“<c:out value="${key}"/>”相关的帖子（${page.totalRows}）</div>
                    <div class="result-article">
                        <c:forEach var="blogContent" items="${blogList}" varStatus="status">
                            <div class="wsearch_list clearfix">
                                <dl>
                                    <dt>
                                        <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" name="atLink"
                                           title="<c:out value="${blogContent.profile.blog.screenName}"/>">
                                <span class="commenfacecon">
                                <img width="33" height="33"
                                     src="<c:out value='${uf:parseFacesInclude(blogContent.profile.blog.headIconSet,blogContent.profile.detail.sex,"s" , true,0,1)[0]}'/>">
                                </span>
                                        </a>
                                    </dt>
                                    <dd>
                                        <div class="wlistcon clearfix">
                                            <div class="wlistl">
                                                <c:choose>
                                                    <c:when test="${blogContent.content.contentType.hasPhrase()}">
                                                        <h3>
                                                            <a class="huifu"
                                                               href="${URL_WWW}/note/${blogContent.content.contentId}"
                                                               target="_blank">
                                                                    ${blogContent.content.content}
                                                            </a>
                                                        </h3>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <h3>
                                                            <a class="huifu"
                                                               href="${URL_WWW}/note/${blogContent.content.contentId}"
                                                               target="_blank">
                                                                <c:out value="${blogContent.content.subject}"/>
                                                            </a>
                                                        </h3>

                                                        <p>${blogContent.content.content}</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="wlistr clearfix">
                                                <div class="running">
                                                    <span class="zhanwei">&nbsp; </span>
                                              <span class="pinglun" title="评论数">
                                                  <i></i>${blogContent.content.replyTimes}
                                              </span>
                                                </div>
                                                <span class="wtime">${dateutil:parseDate(blogContent.content.publishDate)}</span>
                                            </div>
                                        </div>
                                        <c:if test="${blogContent.content.contentType.hasImage()}">
                                            <ul class="search_piclist clearfix"
                                                data-domain="${blogContent.profile.blog.domain}"
                                                data-cid="${blogContent.content.contentId}"
                                                data-cuno="${blogContent.content.uno}">
                                                <c:forEach var="img" items="${blogContent.content.images.images}"
                                                           varStatus="status">
                                                    <c:if test="${status.index<=2}">
                                                        <li>
                                                            <a href="javascript:void(0)" name="imgpreview">
                                                                <img src="${uf:parseSSFace(img.s)}" data-jw="${img.w}"
                                                                     data-jh="${img.h}" width="80" height="60"/>
                                                            </a>
                                                        </li>
                                                    </c:if>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                    </dd>
                                </dl>
                            </div>
                        </c:forEach>
                    </div>
                    <c:set var="pageurl" value="${URL_WWW}/search/content/${key}/"/>
                    <%@ include file="/views/jsp/page/page.jsp" %>
                </c:when>
                <c:otherwise>
                    <div class="noresult">
                        <div class="no1"><span class="tanh"></span>抱歉没有找到含有“<span><c:out value="${key}"/></span>”的文章
                        </div>
                        <p>• 请尝试用准确的关键词搜索，例如游戏名称等</p>

                        <p>• 用空格将多个关键词分开</p>

                        <p>• 看看输入的文字是否有误</p>
                    </div>
                </c:otherwise>
            </c:choose>

            <!--wsearch-->
        </div>
        <!--search right-->
        <%@ include file="/views/jsp/search/search-content-right.jsp" %>
        <!--search right-->
    </div>
    <!--searchc-->
    <div class="wsearchb"></div>

</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/search-content-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>