<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- 标题 -->
<dl class="article-author-intro clearfix">
    <dt class="personface">
        <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" class="tag_cl_left">
            <img height="58px" width="58px"
                 src="<c:out value='${uf:parseFacesInclude(blogContent.profile.blog.headIconSet,blogContent.profile.detail.sex,"s" , true, 0, 1)[0]}'/>"></a>
    </dt>
    <dd>
        <h2>${blogContent.content.subject}</h2>

        <p>
            <a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" class="user" name="atLink"
               title="<c:out value="${blogContent.profile.blog.screenName}"/>">${blogContent.profile.blog.screenName}</a><span>${dateutil:parseDate(blogContent.content.publishDate)}</span>
            <a href="javascript:void(0);" name="reply_link">我也要评论<c:if
                    test="${blogContent.content.replyTimes>0}">(${blogContent.content.replyTimes})</c:if></a><a
                href="javascript:void(0);" name="reply_link" title="快速评论" class="comment-icon"></a>
        </p>

        <div id="content_source"><c:if test="${blogContent.board!=null}">
            <em>来自：<a href="${URL_WWW}/group/${blogContent.board.gameCode}/talk"
                      target="_blank">${blogContent.board.gameName}小组</a></em>
        </c:if></div>
    </dd>
    <c:if test="${shareBaseInfo!=null}">
        <dd class="article-share">
            <div class="article-share-btn">
                <i>${shareBaseInfo.displayStyle}：</i>
                <a class="atc-sina" href="${URL_WWW}/share/content/sinaweibo/bind?sid=${shareBaseInfo.shareId}"
                   target="_blank"></a>
                <a class="atc-tengxun" href="${URL_WWW}/share/content/qweibo/bind?sid=${shareBaseInfo.shareId}"
                   target="_blank"></a>
                <a class="atc-qq" href="${URL_WWW}/share/content/qq/bind?sid=${shareBaseInfo.shareId}"
                   target="_blank"></a>
            </div>
        </dd>
    </c:if>
</dl>
<div class="blogarticle">
    ${blogContent.content.content}
</div>