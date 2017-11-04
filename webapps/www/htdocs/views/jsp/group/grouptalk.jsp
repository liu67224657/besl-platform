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
    <meta name="Keywords" content="<c:choose><c:when test="${fn:length(group.seoKeyWords)>0}">${group.seoKeyWords}</c:when><c:otherwise>着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人</c:otherwise></c:choose>">
    <meta name="description" content="<c:choose><c:when test="${fn:length(group.seoDescription)>0}">${group.seoDescription}</c:when><c:otherwise>着迷网（Joyme.com）是一个以游戏为主题的游戏玩家社区，记录你的游戏生活和情感 ，相遇结交志同道合的朋友，互动属于自己的游戏文化 ，有趣、新鲜的游戏话题，每天等你来讨论!,着迷,着迷网,joyme,joyme.com,游戏,游戏社区,好玩,攻略,最新游戏,最热游戏,游戏资讯,达人,高手,游戏经历,游戏成绩,美图,游戏原声,代言人</c:otherwise></c:choose>"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${group.resourceName}_小组_${jmh_title}</title>
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
    <c:choose>
        <c:when test="${fn:length(contentList)>0}">
            <c:forEach var="itemContent" items="${contentList}" varStatus="st">
                <c:choose>
                    <c:when test="${itemContent.lineItem.displayType.isTop()}">
                        <div class="wsearch_list<c:if test="${st.index%2==0}"> w_listbg</c:if> clearfix">
                            <dl>
                                <dt>
                                    <span class="list_top"></span>
                                </dt>
                                <dd>
                                    <div class="wlistcon list_hotcon clearfix">
                                        <div class="wlistl">
                                            <h3>
                                                <c:choose>
                                                    <c:when test="${itemContent.blogContent.content.contentType.hasPhrase()}">
                                                        <c:choose>
                                                            <c:when test="${itemContent.voteDto!=null}">
                                                                <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                                   target="_blank">
                                                                    <c:out value="${itemContent.voteDto.vote.voteSubject.subject}"/>
                                                                </a>
                                                                <em class="poll_icon"></em>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                                   target="_blank">
                                                                        ${itemContent.blogContent.content.content}
                                                                </a>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                           target="_blank">
                                                            <c:out value="${itemContent.blogContent.content.subject}"/>
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </h3>
                                        </div>
                                        <div class="wlistr clearfix">
                                            <div class="running">
                                                    <%--<span class="huifu" title="阅读数">--%>
                                                    <%--<i></i>${itemContent.blogContent.content.viewTimes}--%>
                                                    <%--</span>--%>
                                                            <span class="zhanwei">
                                                               &nbsp;
                                                            </span>
                                                        <span class="pinglun" title="评论数" href="javascript:void(0)">
                                                            <i></i>${itemContent.blogContent.content.replyTimes}
                                                        </span>
                                            </div>
                                        </div>
                                    </div>
                                </dd>
                            </dl>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="wsearch_list<c:if test="${st.index%2==0}"> w_listbg</c:if> clearfix">
                            <dl>
                                <dt>
                                    <a href="${URL_WWW}/people/${itemContent.blogContent.profile.blog.domain}"
                                       name="atLink"
                                       title="<c:out value="${itemContent.blogContent.profile.blog.screenName}"/>"
                                       target="_blank">
                                                <span class="commenfacecon">
                                                <img width="33" height="33"
                                                     src="<c:out value='${uf:parseFacesInclude(itemContent.blogContent.profile.blog.headIconSet,itemContent.blogContent.profile.detail.sex,"s" , true,0,1)[0]}'/>">
                                                </span>
                                    </a>
                                </dt>
                                <dd>
                                    <div class="wlistcon clearfix">
                                        <div class="wlistl">
                                            <c:choose>
                                                <c:when test="${itemContent.blogContent.content.contentType.hasPhrase()}">
                                                    <c:choose>
                                                        <c:when test="${itemContent.voteDto!=null}">
                                                            <h3>
                                                                <c:choose>
                                                                    <c:when test="${itemContent.voteDto.vote.expired}"><em
                                                                            class="poll_finish">已完成</em></c:when>
                                                                    <c:when test="${itemContent.voteDto.voteUserRecord!=null}"><em
                                                                            class="poll_finish">已投票</em></c:when>
                                                                    <c:otherwise><em
                                                                            class="poll_working">进行中</em></c:otherwise>
                                                                </c:choose>
                                                                <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                                   target="_blank">
                                                                    <c:out value="${itemContent.voteDto.vote.voteSubject.subject}"/>
                                                                </a>
                                                                <em class="poll_icon"></em>
                                                            </h3>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <h3>
                                                                <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                                   target="_blank">
                                                                        ${itemContent.blogContent.content.content}
                                                                </a>
                                                                <c:if test="${itemContent.lineItem.displayType.isEssential()}">
                                                                    <span class="best"></span>
                                                                </c:if>
                                                            </h3>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <h3>
                                                        <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                           target="_blank">
                                                            <c:out value="${itemContent.blogContent.content.subject}"/>
                                                        </a>
                                                        <c:if test="${itemContent.lineItem.displayType.isEssential()}">
                                                            <span class="best"></span>
                                                        </c:if>
                                                    </h3>

                                                    <p>${itemContent.blogContent.content.content}</p>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="wlistr clearfix">
                                            <div class="running">
                                                    <%--<span class="huifu" title="阅读数">--%>
                                                    <%--<i></i>${itemContent.blogContent.content.viewTimes}--%>
                                                    <%--</span>--%>
                                                <span class="zhanwei">&nbsp; </span>
                                                            <span class="pinglun" title="评论数">
                                                                <i></i>${itemContent.blogContent.content.replyTimes}
                                                            </span>
                                            </div>
                                            <c:choose>
                                                <c:when test="${itemContent.contentInteraction != null}">
                                                    <span class="wtime">${dateutil:parseDate(itemContent.contentInteraction.createDate)}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="wtime">${dateutil:parseDate(itemContent.blogContent.content.publishDate)}</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:if test="${itemContent.interactionProfileBlog != null}">
                                                <span class="wname"><a href="${URL_WWW}/people/${itemContent.interactionProfileBlog.domain}" title="${itemContent.interactionProfileBlog.screenName}" target="_blank">${jstr:subStr(itemContent.interactionProfileBlog.screenName,6,'…')}</a></span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <c:if test="${itemContent.blogContent.content.contentType.hasImage()}">
                                        <ul class="search_piclist clearfix"
                                            data-domain="${itemContent.blogContent.profile.blog.domain}"
                                            data-cuno="${itemContent.blogContent.content.uno}"
                                            data-cid="${itemContent.blogContent.content.contentId}">
                                            <c:forEach var="img"
                                                       items="${itemContent.blogContent.content.images.images}"
                                                       varStatus="status">
                                                <c:if test="${status.index<=2}">
                                                    <li>
                                                        <a href="javascript:void(0)" name="imgpreview">
                                                            <img class='lazy' original="${uf:parseSSFace(img.s)}"
                                                                 data-jw="${img.w}" data-jh="${img.h}" width="80"
                                                                 height="60"
                                                                 src="${URL_LIB}/static/theme/default/img/loading-big.gif"/>
                                                        </a>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                    <c:if test="${itemContent.voteDto!=null && fn:length(itemContent.voteDto.vote.voteSubject.imageSet.images)>0}">
                                        <ul class="search_piclist clearfix"
                                            data-domain="${itemContent.blogContent.profile.blog.domain}"
                                            data-cuno="${itemContent.blogContent.content.uno}"
                                            data-cid="${itemContent.blogContent.content.contentId}">
                                            <c:forEach var="img"
                                                       items="${itemContent.voteDto.vote.voteSubject.imageSet.images}"
                                                       varStatus="status">
                                                <c:if test="${status.index<=2}">
                                                    <li>
                                                        <a href="javascript:void(0)" name="imgpreview">
                                                            <img class='lazy' original="${uf:parseSSFace(img.s)}"
                                                                 data-jw="${img.w}" data-jh="${img.h}" width="80"
                                                                 height="60"
                                                                 src="${URL_LIB}/static/theme/default/img/loading-big.gif"/>
                                                        </a>
                                                    </li>
                                                </c:if>
                                            </c:forEach>
                                        </ul>
                                    </c:if>
                                </dd>
                            </dl>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="pagecon clearfix" style="padding: 30px 30px">
                <dl>
                    <dd>
                        <div class="wlistcon clearfix">
                            <div class="wlist">
                                <h3>
                                    这小组居然还没有人来过！快来说点什么踩一踩！
                                </h3>
                            </div>
                        </div>
                    </dd>
                </dl>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<!-- page -->
<div class="pagecon clearfix">
    <c:set var="pageurl" value="${URL_WWW}/board/${group.gameCode}/talk"/>
    <%@ include file="/views/jsp/page/page.jsp" %>
</div>
<!-- talk post -->
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
<!--con_area-->
<div class="con_ft"></div>
</div>
<%@ include file="/views/jsp/group/group-right.jsp" %>

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