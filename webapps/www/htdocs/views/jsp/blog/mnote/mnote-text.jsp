<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h1 class="article-title">${blogContent.content.subject}</h1>

<div class="article-time">${dateutil:parseDate(blogContent.content.publishDate)}</div>

<c:if test="${shareBaseInfo!=null}">
    <div class="aritcle-share">
        <span>${shareBaseInfo.displayStyle}：</span>
        <a class="sns-sina" href="${URL_WWW}/mshare/content/sinaweibo/bind?sid=${shareBaseInfo.shareId}"
           target="_blank"></a>
        <a class="sns-tengxun" href="${URL_WWW}/mshare/content/qweibo/bind?sid=${shareBaseInfo.shareId}"
           target="_blank"></a>
        <a class="sns-qq" href="${URL_WWW}/mshare/content/qq/bind?sid=${shareBaseInfo.shareId}" target="_blank"></a>
    </div>
</c:if>

<div class="article-content">
    ${blogContent.content.content}
</div>

