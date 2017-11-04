<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<ul>
    <c:forEach var="itemContent" items="${contentList}" varStatus="st">
        <li <c:if test="${st.last}">class="noborder"</c:if>>
            <div class="li-part1">
                <div class="wlist1">
                    <h3>
                        <c:if test="${itemContent.lineItem.displayType.isTop()}">
                            <span class="list_top"></span>
                        </c:if>
                        <c:choose>
                            <c:when test="${itemContent.blogContent.content.contentType.hasPhrase()}">
                                <c:choose>
                                    <c:when test="${itemContent.voteDto!=null}">
                                        <c:choose>
                                            <c:when test="${itemContent.voteDto.vote.expired}"><em
                                                    class="poll_finish">已完成</em></c:when>
                                            <c:when test="${itemContent.voteDto.voteUserRecord!=null}"><em
                                                    class="poll_finish">已投票</em></c:when>
                                            <c:otherwise><em
                                                    class="poll_working">进行中</em></c:otherwise>
                                        </c:choose>
                                        <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                           target="_blank"><c:out
                                                value="${itemContent.voteDto.vote.voteSubject.subject}"/></a>
                                        <em class="poll_icon"></em>
                                        <c:if test="${itemContent.blogContent.content.relationSet.groupPointRelation!=null}"><span
                                                class="scores">+${itemContent.blogContent.content.relationSet.groupPointRelation.relationValue}</span></c:if>
                                    </c:when>
                                    <c:otherwise><a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                                    target="_blank">${itemContent.blogContent.content.content}</a></c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}"
                                   target="_blank">
                                    <c:out value="${itemContent.blogContent.content.subject}"/>
                                </a>
                                <c:if test="${itemContent.blogContent.content.relationSet.groupPointRelation!=null}"><span
                                        class="scores">+${itemContent.blogContent.content.relationSet.groupPointRelation.relationValue}</span></c:if>

                            </c:otherwise>
                        </c:choose>
                        <c:if test="${itemContent.lineItem.displayType.isEssential()}"><span class="best"></span></c:if>

                        <span class="replyhits">${itemContent.blogContent.content.replyTimes}回帖</span>
                    </h3>
                </div>
            </div>
            <div class="li-part2">
                <a href="${URL_WWW}/people/${itemContent.blogContent.profile.blog.domain}">${jstr:subStr(itemContent.blogContent.profile.blog.screenName,8,'…')}</a>

                <p>${dateutil:parseDate(itemContent.blogContent.content.publishDate)}</p>
            </div>
            <div class="li-part3">
                <c:choose>
                    <c:when test="${itemContent.contentInteraction!=null}">
                        <a href="${URL_WWW}/people/${itemContent.interactionProfileBlog.domain}"
                           title="${itemContent.interactionProfileBlog.screenName}">
                            <c:choose>
                                <c:when test="${itemContent.interactionProfileBlog==null}">——</c:when>
                                <c:otherwise>${jstr:subStr(itemContent.interactionProfileBlog.screenName,8,'…')}</c:otherwise>
                            </c:choose></a>

                        <p>${dateutil:parseDate(itemContent.contentInteraction.createDate)}</p>
                    </c:when>
                    <c:otherwise>
                        <a href="${URL_WWW}/people/${itemContent.blogContent.profile.blog.domain}"
                           title="${itemContent.blogContent.profile.blog.screenName}">${jstr:subStr(itemContent.blogContent.profile.blog.screenName,8,'…')}</a>

                        <p>${dateutil:parseDate(itemContent.blogContent.content.publishDate)}</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </li>
    </c:forEach>
</ul>