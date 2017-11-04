<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<!-- 内容展示list -->
<c:forEach var="atmeInfo" items="${atmeList}">
<c:choose>
<c:when test="${atmeInfo.contentType.getCode() eq 'content'}">
<c:set var="blogContent" value="${atmeInfo.blogContent}"></c:set>
<c:if test="${blogContent.content!=null && !(blogContent.content.removeStatus.code eq 'y')}">
<div id="${atmeInfo.tlId}" class="area clearfix">
<dl>
<dt class="personface"><a class="tag_cl_left" href="${URL_WWW}/people/${blogContent.profile.blog.domain}" name="atLink"
                          title="<c:out value="${blogContent.profile.blog.screenName}"/>"><img
        src="<c:out value='${uf:parseFacesInclude(blogContent.profile.blog.headIconSet,blogContent.profile.detail.sex,"s" , true, 0, 1)[0]}'/>"
        width="58px" height="58px"/></a>
</dt>
<dd class="textcon">
<a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" name="atLink" class="author"
   title="<c:out value="${blogContent.profile.blog.screenName}"/>">
    <c:out value="${blogContent.profile.blog.screenName}"/>
</a>
<c:if test="${blogContent.profile.detail.verifyType !=null && blogContent.profile.detail.verifyType.code!='n'}">
    <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}"
       class="${blogContent.profile.detail.verifyType.code}vip"
       title="<fmt:message key="verify.profile.${blogContent.profile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
</c:if>
<a class="chakan" href="${URL_WWW}/note/${blogContent.content.contentId}" target="_blank" style="display:none">查看全文</a>
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
        <!--图片显示-->
        <c:if test="${fn:length(blogContent.content.images.images)>0}">
            <c:choose>
                <c:when test="${fn:length(blogContent.content.images.images)==1}">
                    <div class="single_pic" id="img_${blogContent.content.contentId}"
                         data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}">
                        <c:forEach var="img" items="${blogContent.content.images.images}">
                            <ul>
                                <li><p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                            original="<c:out value="${uf:parseSSFace(img.ss)}"/>" data-jw="${img.w}"
                                            data-jh="${img.h}"/></p></li>
                            </ul>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <ul class="multipic clearfix" data-cid="${blogContent.content.contentId}"
                        data-cuno="${blogContent.content.uno}">
                        <c:forEach var="img" items="${blogContent.content.images.images}" varStatus="status">
                            <c:choose>
                                <c:when test="${status.index<=2}">
                                    <li>
                                        <p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                                original="${uf:parseSSFace(img.s)}" data-jw="${img.w}"
                                                data-jh="${img.h}"/></p>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li style="display:none">
                                        <p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                                original="${uf:parseMFace(img.m)}" data-jw="${img.w}"
                                                data-jh="${img.h}"/></p>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </ul>
                    <c:if test="${fn:length(blogContent.content.images.images)>3}">
                        <p>（共${fn:length(blogContent.content.images.images)}张，点击图片查看更多）</p>
                    </c:if>
                    <%--</div>--%>
                </c:otherwise>
            </c:choose>
        </c:if>
        <div class="disarea clearfix">
            <div class="discuss_corner">
                <span class="discorner"></span>
            </div>
            <div class="discuss_area clearfix">
                <c:choose>
                    <c:when test="${blogContent.rootContent!=null && !(blogContent.rootContent.removeStatus.code eq 'y')}">
                        转发自
                        <a class="author" href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}" name="atLink"
                           title="<c:out value="${blogContent.rootProfile.blog.screenName}"/>">
                            @<c:out value="${blogContent.rootProfile.blog.screenName}"/>
                        </a>
                        <c:if test="${blogContent.rootProfile.detail.verifyType !=null && blogContent.rootProfile.detail.verifyType.code!='n'}">
                            <a href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}"
                               class="${blogContent.rootProfile.detail.verifyType.code}vip"
                               title="<fmt:message key="verify.profile.${blogContent.rootProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                        </c:if>
                        <a class="chakan"
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
                        <!--发布footer-->
                        <div class="area_ft"><span
                                class="time">${dateutil:parseDate(blogContent.rootContent.publishDate)}</span>
                            <span class="from">
                                <c:choose>
                                    <c:when test="${blogContent.rootBoard!=null}">
                                        <c:choose>
                                            <c:when test="${blogContent.rootBoard.resourceDomain.code eq 'game'}">
                                                来自：<a href="${URL_WWW}/game/${blogContent.rootBoard.gameCode}/talk" target="_blank">${blogContent.rootBoard.gameName}</a>
                                            </c:when>
                                            <c:otherwise>
                                                来自：<a href="${URL_WWW}/board/${blogContent.rootBoard.gameCode}/talk" target="_blank">${blogContent.rootBoard.gameName}</a>
                                            </c:otherwise>
                                        </c:choose>
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
                            <p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <%--<div class="discuss_area">--%>
        <%--<div class="shadow"></div>--%>
        <%--<span class="corner"></span>--%>
        <%----%>
        <%--</div>--%>
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
<!--发布footer-->
<div class="area_ft"><span class="time">${dateutil:parseDate(blogContent.content.publishDate)}</span>
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
            <c:if test="${blogContent.content.uno eq userSession.blogwebsite.uno  && blogContent.content.voteSubjectId == null}">
                <li><c:choose>
                    <c:when test="${blogContent.content.publishType.code eq 'fwd'}">
                        <a href="javascript:void(0)" id="edit_forward_${blogContent.content.contentId}"
                           data-blogid="${blogContent.content.contentId}" data-bloguno="${blogContent.content.uno}"
                           class="listedit" title="编辑"></a>
                    </c:when>
                    <c:otherwise>
                        <a href="${ctx}/content/edit/${blogContent.content.uno}/${blogContent.content.contentId}"
                           class="listedit" title="编辑">编辑</a>
                    </c:otherwise>
                </c:choose><em>|</em></li>
            </c:if>
            <li><a name="removeAt" data-tid="${atmeInfo.tlId}" data-did="${atmeInfo.directId}" href="javascript:void(0)"
                   class="remove" title="删除">删除</a><em>|</em></li>
            <li><a href="javascript:void(0);" name="replyLink" class="share" title="评论" data-itype="reply"
                   data-cid="${blogContent.content.contentId}"
                   data-cuno="${blogContent.content.uno}">评论<c:if
                    test="${blogContent.content.replyTimes>0}">(<span name="ia_header_num_reply_${blogContent.content.contentId}">${blogContent.content.replyTimes}</span>)</c:if></a>
            </li>
            <li>
                <a href="javascript:void(0);" name="favLink" class="favlink"
                   title="<c:choose><c:when test="${!blogContent.favorite}">喜欢</c:when><c:otherwise>取消喜欢</c:otherwise></c:choose>"
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
</c:when>
<c:otherwise>
    <div class="mycomment_list atstyle clearfix" id="${atmeInfo.tlId}">
        <dl>
            <dt class="personface">
                <a href="${URL_WWW}/people/${atmeInfo.directProfile.blog.domain}" name="atLink"
                   title="<c:out value="${atmeInfo.directProfile.blog.screenName}"/>">
                    <img src="<c:out value='${uf:parseFacesInclude(atmeInfo.directProfile.blog.headIconSet,atmeInfo.directProfile.detail.sex,"s" , true, 0, 1)[0]}'/>"
                         width="58px" height="58px"/>
                </a>
            </dt>
            <dd class="mycomment">
                <p>
                    <a href="${URL_WWW}/people/${atmeInfo.directProfile.blog.domain}" name="atLink"
                       title="<c:out value="${atmeInfo.directProfile.blog.screenName}"/>">
                        <c:out value="${atmeInfo.directProfile.blog.screenName}"/>
                    </a>
                    <c:if test="${atmeInfo.directProfile.detail.verifyType !=null && atmeInfo.directProfile.detail.verifyType.code!='n'}">
                        <a href="${URL_WWW}/people/${atmeInfo.directProfile.blog.domain}"
                           class="${atmeInfo.directProfile.detail.verifyType.code}vip"
                           title="<fmt:message key="verify.profile.${atmeInfo.directProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                    </c:if> ：
                </p>

                <p>${atmeInfo.directContentReply.interactionContent}<em>(${dateutil:parseDate(atmeInfo.directContentReply.createDate)})</em>
                </p>
                <c:if test="${atmeInfo.directContentReply.interactionImages!=null && fn:length(atmeInfo.directContentReply.interactionImages.images)>0}">
                    <ul class="search_piclist clearfix">
                        <c:forEach var="img" items="${atmeInfo.directContentReply.interactionImages.images}">
                            <li style="display: list-item;">
                                <a href="javascript:void(0)" name="replyimgpreview">
                                    <c:choose>
                                        <c:when test="${img.h>60}">
                                            <img height="60px" data-jh="${img.h}" data-jw="${img.w}"
                                                 src="${uf:parseSSFace(img.ss)}">
                                        </c:when>
                                        <c:otherwise>
                                            <img data-jh="${img.h}" data-jw="${img.w}" src="${uf:parseSSFace(img.ss)}">
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <p class="c_text">
                    <c:choose>
                        <c:when test="${atmeInfo.parentContentReply!=null}">
                            <!-- 回复评论 -->
                            回复&nbsp;
                            <c:if test="${userSession.blogwebsite.uno == atmeInfo.parentProfile.blog.uno}">我</c:if>
                            <c:if test="${userSession.blogwebsite.uno != atmeInfo.parentProfile.blog.uno}">
                                <a href="${URL_WWW}/people/${atmeInfo.parentProfile.blog.domain}" name="atLink"
                                   title="<c:out value='${atmeInfo.parentProfile.blog.screenName}'/>">
                                    @<c:out value="${atmeInfo.parentProfile.blog.screenName}"/></a>
                                <c:if test="${atmeInfo.parentProfile.detail.verifyType !=null && atmeInfo.parentProfile.detail.verifyType.code!='n'}">
                                    <a href="${URL_WWW}/people/${atmeInfo.parentProfile.blog.domain}"
                                       class="${atmeInfo.parentProfile.detail.verifyType.code}vip"
                                       title="<fmt:message key="verify.profile.${atmeInfo.parentProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                                </c:if>
                            </c:if>
                            &nbsp;的评论：<a class="text_link" href="${URL_WWW}/note/${atmeInfo.relationContent.contentId}?rid=${atmeInfo.directId}#${atmeInfo.parentContentReply.interactionId}" target="_blank">"${atmeInfo.parentContentReply.interactionContent}"</a>
                        </c:when>
                        <c:when test="${atmeInfo.relationContent!=null && !(atmeInfo.relationContent.removeStatus.code eq 'y') && atmeInfo.parentContentReply==null}">
                            <!-- 评论文章 -->
                            评论&nbsp;
                            <c:if test="${userSession.blogwebsite.uno == atmeInfo.relationContent.uno}">我</c:if>
                            <c:if test="${userSession.blogwebsite.uno != atmeInfo.relationContent.uno}">
                                <a href="${URL_WWW}/people/${atmeInfo.relationProfile.blog.domain}" name="atLink"
                                   title="${atmeInfo.relationProfile.blog.screenName}">
                                    @<c:out value="${atmeInfo.relationProfile.blog.screenName}"/></a>
                                <c:if test="${atmeInfo.relationProfile.detail.verifyType !=null && atmeInfo.relationProfile.detail.verifyType.code!='n'}">
                                    <a href="${URL_WWW}/people/${atmeInfo.relationProfile.blog.domain}"
                                       class="${atmeInfo.relationProfile.detail.verifyType.code}vip"
                                       title="<fmt:message key="verify.profile.${atmeInfo.relationProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                                </c:if>
                            </c:if>
                            &nbsp;的文章：<a class="text_link" href="${URL_WWW}/note/${atmeInfo.relationContent.contentId}?rid=${atmeInfo.directId}#${atmeInfo.directId}" target="_blank">"<c:out
                                value="${atmeInfo.relationContent.subject}"/>"</a>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${atmeInfo.parentContentReply!=null}">
                                <fmt:message key="blog.replay.notexists" bundle="${userProps}"/>
                            </c:if>
                            <c:if test="${atmeInfo.parentContentReply==null}">
                                <fmt:message key="blog.content.not.exists" bundle="${userProps}"/>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </p>
            </dd>
            <dd class="mycomment_hf_tr">
                    <span>
                        <a href="javascript:void(0)" name="removeAt" data-tid="${atmeInfo.tlId}"
                           data-did="${atmeInfo.directId}">删除</a> |
                        <a href="javascript:void(0)" name="show_reply_onreply"
                           data-cid="${atmeInfo.relationContent.contentId}"
                           data-cuno="${atmeInfo.relationContent.uno}"
                                <c:choose>
                                    <c:when test="${fn:length(atmeInfo.parentContentReply.interactionId)>0}">
                                        data-rid="${atmeInfo.parentContentReply.interactionId}"
                                        data-runo="${atmeInfo.parentProfile.blog.uno}"
                                        data-pid="${atmeInfo.directId}"
                                        data-puno="${atmeInfo.directProfile.blog.uno}"
                                    </c:when>
                                    <c:otherwise>
                                        data-rid="${atmeInfo.directId}"
                                        data-runo="${atmeInfo.directProfile.blog.uno}"
                                    </c:otherwise>
                                </c:choose>
                           data-pname="${atmeInfo.directProfile.blog.screenName}">回复</a>
                    </span>
            </dd>
        </dl>
    </div>
</c:otherwise>
</c:choose>
</c:forEach>
