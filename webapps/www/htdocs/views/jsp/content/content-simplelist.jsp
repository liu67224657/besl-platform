<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<c:forEach var="itemContent" items="${contentList}" varStatus="st">

    <div class="wsearch_list<c:if test="${st.index%2==0}"> w_listbg</c:if> clearfix">
        <dl>
            <dt>
                <a href="${URL_WWW}/people/${itemContent.blogContent.profile.blog.domain}" name="atLink"
                   title="<c:out value="${itemContent.blogContent.profile.blog.screenName}"/>" target="_blank">
                                        <span class="commenfacecon">
                                        <img class='lazy' width="33" height="33"
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
                                                <c:otherwise><em class="poll_working">进行中</em></c:otherwise>
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
                        <c:choose>
                            <c:when test="${itemContent.blogContent.board!=null}">
                                <span class="come_from">来自：<a
                                        href="${URL_WWW}/group/${itemContent.blogContent.board.gameCode}/talk"
                                        target="_blank"
                                        title="${itemContent.blogContent.board.gameName}"><c:choose><c:when
                                        test="${fn:length(itemContent.blogContent.board.gameName)>6}">${fn:substring(itemContent.blogContent.board.gameName,0,6)}</c:when><c:otherwise>${itemContent.blogContent.board.gameName}</c:otherwise></c:choose></a></span>
                            </c:when>
                            <c:otherwise>
                                <span class="come_from">来自：着迷网</span>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                <c:if test="${itemContent.blogContent.content.contentType.hasImage()}">
                    <ul class="search_piclist clearfix" data-domain="${itemContent.blogContent.profile.blog.domain}"
                        data-cuno="${itemContent.blogContent.content.uno}"
                        data-cid="${itemContent.blogContent.content.contentId}">
                        <c:forEach var="img" items="${itemContent.blogContent.content.images.images}"
                                   varStatus="status">
                            <c:if test="${status.index<=2}">
                                <li>
                                    <a href="javascript:void(0)" name="imgpreview">
                                        <img class='lazy' src="${uf:parseSSFace(img.s)}" data-jw="${img.w}"
                                             data-jh="${img.h}" width="80" height="60"/>
                                    </a>
                                </li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${itemContent.voteDto!=null && fn:length(itemContent.voteDto.vote.voteSubject.imageSet.images)>0}">
                    <c:forEach var="img" items="${itemContent.voteDto.vote.voteSubject.imageSet.images}"
                               varStatus="status">
                        <c:if test="${status.index<=2}">
                            <li>
                                <a href="javascript:void(0)" name="imgpreview">
                                    <img class='lazy' original="${uf:parseSSFace(img.s)}" data-jw="${img.w}"
                                         data-jh="${img.h}" width="80" height="60"
                                         src="${URL_LIB}/static/theme/default/img/loading-big.gif"/>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </c:if>
            </dd>
        </dl>
    </div>
</c:forEach>