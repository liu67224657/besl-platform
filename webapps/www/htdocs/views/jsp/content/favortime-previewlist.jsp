<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<c:forEach var="blogContent" items="${blogList}">
<c:if test="${blogContent.content!=null && !(blogContent.content.removeStatus.code eq 'y')}">
<div id="conent_${blogContent.content.contentId}" class="area clearfix">
<dl>
<dt class="personface"><a class="tag_cl_left"
                          href="${URL_WWW}/people/${blogContent.profile.blog.domain}" name="atLink"
                          title="<c:out value="${blogContent.profile.blog.screenName}"/>"><img
        src="<c:out value="${uf:parseFacesInclude(blogContent.profile.blog.headIconSet, blogContent.profile.detail.sex,'s', true, 0,1)[0]}"/>"
        width="58px" height="58px"/></a>
</dt>
<dd class="textcon">
    <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" name="atLink" class="author"
       title="<c:out value="${blogContent.profile.blog.screenName}"/>">
        <c:out value="${blogContent.profile.blog.screenName}"/>
    </a>
    <c:if test="${blogContent.profile.detail.verifyType !=null && blogContent.profile.detail.verifyType.code != 'n'}">
        <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}"
           class="${blogContent.profile.detail.verifyType.code}vip"
           title="<fmt:message key="verify.profile.${blogContent.profile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
    </c:if>
    <a class="chakan" data-cid="${blogContent.content.contentId}"
       href="${URL_WWW}/note/${blogContent.content.contentId}" target="_blank"
       style="display:none">查看全文</a>
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
                    <c:choose>
                        <c:when test="${blogContent.rootContent!=null && !(blogContent.rootContent.removeStatus.code eq 'y')}">
                            转发自
                            <a class="author"
                               href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}"
                               name="atLink"
                               title="<c:out value="${blogContent.rootProfile.blog.screenName}"/>">
                                @<c:out value="${blogContent.rootProfile.blog.screenName}"/>
                            </a>
                            <a class="chakan" data-cid="${blogContent.rootContent.contentId}"
                               href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                               target="_blank" style="display:none">查看全文</a>
                            <!--短文格式-->
                            <c:choose>
                                <c:when test="${blogContent.rootContent.contentType.hasPhrase()}">
                                    <%@ include file="/views/jsp/content/forwardpreview-chat.jsp" %>
                                </c:when>
                                <c:otherwise>
                                    <%@ include file="/views/jsp/content/forwardpreview-text.jsp" %>
                                </c:otherwise>
                            </c:choose>
                            <%--<!--标签样式-->--%>
                            <c:if test="${blogContent.rootFavorite}">
                                <p class="favor_time normal"><span class="love_time">于
                    <fmt:formatDate value="${blogContent.rootFavoriteDate}" type="both" pattern="yyyy年MM月dd日"/>喜欢</span>
                                </p>
                            </c:if>
                            <!--发布footer-->
                            <div class="area_ft"><span
                                    class="time">${dateutil:parseDate(blogContent.rootContent.publishDate)}</span>
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
                                        <c:if test="${blogContent.rootContent.uno eq userSession.blogwebsite.uno && blogContent.rootContent.voteSubjectId == null}">
                                            <li>
                                                <a href="${ctx}/content/edit/${blogContent.rootContent.uno}/${blogContent.rootContent.contentId}"
                                                   class="listedit" title="编辑">编辑</a><em>|</em></li>
                                        </c:if>
                                        <li>
                                            <a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                                               class="share" title="评论">评论<c:if
                                                    test="${blogContent.rootContent.replyTimes>0}">(<span name="ia_header_num_reply_${blogContent.rootContent.contentId}">${blogContent.rootContent.replyTimes}</span>)</c:if></a>
                                        </li>
                                        <li>
                                            <a href="javascript:void(0);" name="favLink" class="favlink"
                                               title="<c:choose><c:when test="${!blogContent.rootFavorite}">喜欢</c:when><c:otherwise>取消喜欢</c:otherwise></c:choose>"
                                               data-cid="${blogContent.rootContent.contentId}"
                                               data-cuno="${blogContent.rootContent.uno}">
                                                <c:choose>
                                                    <c:when test="${!blogContent.rootFavorite}"><i
                                                            class="step1"></i></c:when>
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
                                <p><fmt:message key="blog.content.not.exists"
                                                bundle="${userProps}"/></p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <%--<div class="discuss_area">--%>
            <%--<div class="shadow"></div>--%>
            <%--<span class="corner"></span>--%>

            <%--</div>--%>
        </c:otherwise>
    </c:choose>
    <!--标签样式-->
        <%--<c:if test="${blogContent.content.contentTag.tags!=null && fn:length(blogContent.content.contentTag.tags)>0}">--%>
        <%--<div class="cont_tags">--%>
        <%--<p>标签--%>
        <%--<c:forEach var="tag" items="${blogContent.content.contentTag.tags}">--%>
        <%--<a href="${ctx}/search/s/${tag}/">#<c:out value="${tag}"/></a>--%>
        <%--</c:forEach>--%>
        <%--</p>--%>
        <%--</div>--%>
        <%--</c:if>--%>
    <c:if test="${blogContent.favorite}">
        <p class="favor_time normal"><span class="love_time">于<fmt:formatDate
                value="${blogContent.favoriteDate}" type="both" pattern="yyyy年MM月dd日"/>喜欢</span></p>
    </c:if>
    <!--发布footer-->
    <div class="area_ft"><span
            class="time">${dateutil:parseDate(blogContent.content.publishDate)}</span>
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
                <c:if test="${blogContent.content.uno eq userSession.blogwebsite.uno}">
                    <c:if test="${blogContent.content.voteSubjectId == null}">
                        <c:choose>
                            <c:when test="${blogContent.content.publishType.code eq 'fwd'}">
                                <li><a href="javascript:void(0)"
                                       id="edit_forward_${blogContent.content.contentId}"
                                       data-cid="${blogContent.content.contentId}"
                                       data-cuno="${blogContent.content.uno}"
                                       class="listedit" title="编辑">编辑</a><em>|</em></li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a href="${ctx}/content/edit/${blogContent.content.uno}/${blogContent.content.contentId}"
                                       class="listedit" title="编辑">编辑</a> <em>|</em></li>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <li><a id="link_del_${blogContent.content.contentId}"
                           data-cid="${blogContent.content.contentId}"
                           data-uno="${blogContent.content.uno}" href="javascript:void(0)"
                           class="remove"
                           title="删除">删除</a><em>|</em></li>
                </c:if>
                <li><a href="javascript:void(0);" name="replyLink" class="share" title="评论"
                       data-itype="reply" data-cid="${blogContent.content.contentId}"
                       data-cuno="${blogContent.content.uno}">评论<c:if
                        test="${blogContent.content.replyTimes>0}">(${blogContent.content.replyTimes})</c:if> </a>
                </li>
                <li>
                    <a href="javascript:void(0);" name="favLink" class="favlink"
                       title="<c:choose><c:when test="${!blogContent.favorite}">喜欢</c:when><c:otherwise>取消喜欢</c:otherwise></c:choose>"
                       data-cid="${blogContent.content.contentId}"
                       data-cuno="${blogContent.content.uno}">
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