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
                <li><a class="on" href="${URL_WWW}/profile/favorite/${profile.blog.uno}"><span><c:choose><c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">我</c:when><c:when test="${profile.detail.sex!=null && profile.detail.sex eq '0'}">她</c:when><c:otherwise>他</c:otherwise></c:choose>的喜欢</span></a> </li>
                <li><a href="${URL_WWW}/profile/experience/${profile.blog.uno}"><span>个人资料</span></a></li>
            </ul>
        </div>
        <div class="con_wrap"></div>
        <div class="con_hd01"></div>
        <div class="con_area con_l clearfix">
            <c:if test="${fn:length(blogList)<1}">
                <div class="addgameshowlist companny_mg clearfix">
                    <c:choose>
                        <c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                            <c:set var="who" value="你"/>
                        </c:when>
                        <c:when test="${profile.detail.sex!=null && profile.detail.sex eq '0'}">
                            <c:set var="who" value="她"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="who" value="他"/>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                                <c:when test="${userSession!=null && userSession.blogwebsite.uno==profile.blog.uno}">
                                    <fmt:message key="blog.my.fav.empty" bundle="${userProps}"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="blog.other.fav.empty" bundle="${userProps}">
                                        <fmt:param value="${who}"/>
                                    </fmt:message>
                                </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            <c:forEach var="blogContent" items="${blogList}">
            <c:if test="${blogContent.content!=null && !(blogContent.content.removeStatus.code eq 'y')}">
            <div id="conent_${blogContent.content.contentId}" class="area blogarea">
             <dl>
            <dd class="textcon">
                <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" name="atLink"   class="author" title="<c:out value="${blogContent.profile.blog.screenName}"/>"><c:out value="${blogContent.profile.blog.screenName}"/> </a>
                <c:if test="${blogContent.profile.detail.verifyType !=null && blogContent.profile.detail.verifyType.code != 'n'}">
                    <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}"
                       class="${blogContent.profile.detail.verifyType.code}vip"
                       title="<fmt:message key="verify.profile.${blogContent.profile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                </c:if>
                <a class="chakan" data-cid="${blogContent.content.contentId}" href="${URL_WWW}/note/${blogContent.content.contentId}" target="_blank" style="display:none">查看全文</a>
            <c:choose>
            <c:when test="${blogContent.content.publishType.code eq 'org'}">
            <!--不是转贴-->
            <c:choose>
            <c:when test="${blogContent.content.contentType.hasPhrase()}">
                <%@ include file="/views/jsp/content/contentpreview-chat.jsp" %>
            </c:when>
            <c:otherwise>
                <%@ include file="/views/jsp/content/contentpreview-text.jsp" %>
            </c:otherwise>
            </c:choose>
            </c:when>
            <c:otherwise>
            <p class="discuss_txt">${blogContent.content.content}</p>
            <div class="disarea clearfix">
            <div class="discuss_corner">
            <span class="discorner"></span>
            </div>
            <div class="discuss_area clearfix">
            <%--<div class="shadow"></div>--%>
                <c:choose>
                    <c:when test="${blogContent.rootContent!=null && !(blogContent.rootContent.removeStatus.code eq 'y')}">
            转发自<a class="author" href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}" name="atLink" title="<c:out value="${blogContent.rootProfile.blog.screenName}"/>">@<c:out value="${blogContent.rootProfile.blog.screenName}"/></a>
                        <c:if test="${blogContent.rootProfile.detail.verifyType !=null && blogContent.rootProfile.detail.verifyType.code != 'n'}">
                            <a href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}"
                               class="${blogContent.rootProfile.detail.verifyType.code}vip"
                               title="<fmt:message key="verify.profile.${blogContent.rootProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                        </c:if>
                        <a class="chakan" data-cid="${blogContent.rootContent.contentId}"  href="${URL_WWW}/note/${blogContent.rootContent.contentId}" target="_blank" style="display:none">查看全文</a>
                    <!--短文格式-->
                <c:choose>
                    <c:when test="${blogContent.rootContent.contentType.hasPhrase()}">
                        <%@ include file="/views/jsp/content/forwardpreview-chat.jsp" %>
                    </c:when>
                    <c:otherwise>
                        <%@ include file="/views/jsp/content/forwardpreview-text.jsp" %>
                    </c:otherwise>
                </c:choose>
                        <c:if test="${blogContent.rootFavorite}">
                            <p class="favor_time normal"><span class="love_time">于
                                <fmt:formatDate value="${blogContent.rootFavoriteDate}" type="both" pattern="yyyy年MM月dd日"/>喜欢</span>
                            </p>
                        </c:if>
            <!--发布footer-->
            <div class="area_ft">
                <span class="time">${dateutil:parseDate(blogContent.rootContent.publishDate)}</span>
                <span class="from">
                   <c:choose>
                       <c:when test="${blogContent.rootBoard!=null}">
                          来自：<a href="${URL_WWW}/group/${blogContent.rootBoard.gameCode}/talk" target="_blank">${blogContent.rootBoard.gameName}小组</a>
                       </c:when>
                       <c:otherwise>
                           来自：着迷网
                       </c:otherwise>
                   </c:choose>
                </span>
                <div class="operate">
                    <ul>
                    <c:if test="${userSession!=null && blogContent.rootContent.uno eq userSession.blogwebsite.uno && blogContent.rootContent.voteSubjectId == null}">
                          <li><a href="${ctx}/content/edit/${blogContent.rootContent.uno}/${blogContent.rootContent.contentId}" class="listedit" title="编辑">编辑</a><em>|</em></li>
                    </c:if>
                        <li>
                            <a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                               class="share" title="评论">评论(${blogContent.rootContent.replyTimes})</a></li>
                        <li>
                            <a href="javascript:void(0);" name="favLink" class="favlink" title="<c:choose><c:when test="${!blogContent.rootFavorite}">喜欢</c:when><c:otherwise>取消喜欢</c:otherwise></c:choose>"
                               data-cid="${blogContent.rootContent.contentId}" data-cuno="${blogContent.rootContent.uno}">
                                <c:choose>
                                    <c:when test="${!blogContent.rootFavorite}"><i class="step1"></i></c:when>
                                    <c:otherwise><i class="step7"></i></c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${blogContent.rootContent.favorTimes<=0}"><span>喜欢</span></c:when>
                                    <c:otherwise><span>${blogContent.rootContent.favorTimes}</span></c:otherwise>
                                </c:choose>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
                    </c:when>
                    <c:otherwise>
                        <div class="contentremove">
                            <p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            </div>
            </c:otherwise>
            </c:choose>
            <!--标签样式-->
            <%--<c:if test="${blogContent.content.contentTag.tags!=null && fn:length(blogContent.content.contentTag.tags)>0}">--%>
                <%--<div class="cont_tags">--%>
                    <%--<p>--%>
                        <%--<c:forEach var="tag" items="${blogContent.content.contentTag.tags}">--%>
                            <%--<a href="${URL_WWW}/search/s/${tag}/">#<c:out value="${tag}"/></a>--%>
                        <%--</c:forEach>--%>
                    <%--</p>--%>
                <%--</div>--%>
            <%--</c:if>--%>
            <c:if test="${blogContent.favorite}">
            <p class="favor_time normal"> <span class="love_time">于<fmt:formatDate value="${blogContent.favoriteDate}" type="both" pattern="yyyy年MM月dd日"/>喜欢</span></p>
            </c:if>
            <!--发布footer-->
            <div class="area_ft">
                <span class="time">${dateutil:parseDate(blogContent.content.publishDate)}</span>
                <span class="from">
                   <c:choose>
                       <c:when test="${blogContent.board!=null}">
                           来自：<a href="${URL_WWW}/group/${blogContent.board.gameCode}/talk" target="_blank">${blogContent.board.gameName}小组</a>
                       </c:when>
                       <c:otherwise>
                           来自：着迷网
                       </c:otherwise>
                   </c:choose>
                </span>
                <div class="operate">
                    <ul>
                    <c:if test="${userSession!=null && blogContent.content.uno eq userSession.blogwebsite.uno}">
                        <c:if test="${blogContent.content.voteSubjectId == null}">
                        <li>
                            <c:choose>
                            <c:when test="${blogContent.content.publishType.code eq 'fwd'}">
                                <a href="javascript:void(0)" id="edit_forward_${blogContent.content.contentId}"
                                   data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}"
                                   class="listedit" title="编辑">编辑</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${ctx}/content/edit/${blogContent.content.uno}/${blogContent.content.contentId}"
                                   class="listedit" title="编辑">编辑</a>
                            </c:otherwise>
                        </c:choose><em>|</em></li>
                        </c:if>
                        <li><a id="link_del_${blogContent.content.contentId}" data-cid="${blogContent.content.contentId}"
                           data-uno="${blogContent.content.uno}" href="javascript:void(0)" class="remove" title="删除">删除</a><em>|</em></li>
                    </c:if>
                        <li><a href="javascript:void(0);" name="replyLink" class="share" title="评论" data-itype="reply"
                               data-cid="${blogContent.content.contentId}"
                               data-cuno="${blogContent.content.uno}">评论(${blogContent.content.replyTimes})</a></li>
                        <li>
                            <a href="javascript:void(0);" name="favLink" class="favlink" title="<c:choose><c:when test="${!blogContent.favorite}">喜欢</c:when><c:otherwise>取消喜欢</c:otherwise></c:choose>"
                               data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}">
                                <c:choose>
                                    <c:when test="${!blogContent.favorite}"><i class="step1"></i></c:when>
                                    <c:otherwise><i class="step7"></i></c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${blogContent.content.favorTimes<=0}"><span>喜欢</span></c:when>
                                    <c:otherwise><span>${blogContent.content.favorTimes}</span></c:otherwise>
                                </c:choose>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            </dd>
             </dl>
            <div style="clear:both"></div>
            </div>
            </c:if>
            </c:forEach>
            <c:set var="pageurl" value="${URL_WWW}/profile/favorite/${profile.blog.uno}"/>
            <c:if test="${fn:length(type)>0}">
                <c:set var="pageparam" value="type=${type}"/>
            </c:if>
            <%@ include file="/views/jsp/page/goto.jsp" %>
        </div>
        <div class="con_ft"></div>
    </div>
    <!--cont_left-->
    <%@ include file="bloglist-right.jsp" %>
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/blogfav-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();

    var currentProfile={
        uno:'${profile.blog.uno}'};
</script>
<%--blog index--%>
<script type="text/javascript">
    var BFD_ITEM_INFO = {
        artic_userId:"${profile.blog.uno}",
        star_name: "${profile.blog.screenName}",
        artic_domain:"${profile.blog.domain}",
        star_link:"${URL_WWW}/people/${profile.blog.domain}",
        star_signat:"${profile.blog.description}",
        user_id:"${empty userSession.blogwebsite.uno ? 0 : userSession.blogwebsite.uno}",
        client:"Ctest_30"
    };
</script>

</body>

</html>
