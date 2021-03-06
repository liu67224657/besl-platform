<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<!--长文发布 begin-->
<c:if test="${fn:length(blogContent.rootContent.subject)>0}">
    <h3>
            <a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
               target="_blank" title="${blogContent.rootContent.subject}">
                <c:choose>
                    <c:when test="${fn:length(blogContent.rootContent.subject)>30}">
                        <c:out value="${fn:substring(blogContent.rootContent.subject,0,29)}"/>…
                    </c:when>
                    <c:otherwise>
                        <c:out value="${blogContent.rootContent.subject}"/>
                    </c:otherwise>
                </c:choose>
            </a>
    </h3>
</c:if>
<!-- 图片begin -->
<c:choose>
    <c:when test="${blogContent.rootContent.contentType.hasApp() && blogContent.rootContent.apps!=null && fn:length(blogContent.rootContent.apps.apps)>0}">
        <div class="longmode clearfix">
            <div class="tappic">
                <c:forEach var="app" items="${blogContent.rootContent.apps.apps}">
                    <a href="${app.resourceUrl}" target="_blank"><img src="${uf:parseBFace(app.appSrc)}">
                        <span class="pmask"></span></a>
                </c:forEach>
            </div>
                ${blogContent.rootContent.content}<br/>
            <c:if test="${fn:endsWith(blogContent.rootContent.content,'...')}">
                <br/><a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                        target="_blank">未完，阅读全文…</a>
            </c:if>
        </div>
    </c:when>
    <c:otherwise>
        <p>
                ${blogContent.rootContent.content}
            <c:if test="${fn:endsWith(blogContent.rootContent.content,'...')}">
                <br/><a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                        target="_blank">未完，阅读全文…</a>
            </c:if>
        </p>
        <c:if test="${fn:length(blogContent.rootContent.images.images)>0}">
            <c:choose>
                <c:when test="${fn:length(blogContent.rootContent.images.images)==1}">
                    <c:forEach var="img" items="${blogContent.rootContent.images.images}">
                        <c:if test="${img.w>100 || img.h>100}">
                            <div class="single_pic" id="img_${blogContent.rootContent.contentId}"
                                 data-cid="${blogContent.rootContent.contentId}" data-cuno="${blogContent.rootContent.uno}">
                                <ul>
                                    <li><p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                                original="<c:out value="${uf:parseSSFace(img.ss)}"/>" data-jw="${img.w}"
                                                data-jh="${img.h}"/></p></li>
                                </ul>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <ul class="multipic clearfix" data-cid="${blogContent.rootContent.contentId}" data-cuno="${blogContent.rootContent.uno}">
                        <c:set value="0" var="showNo"/>
                        <c:forEach var="img" items="${blogContent.rootContent.images.images}" varStatus="status">
                            <c:if test="${img.w>100 || img.h>100}">
                            <c:choose>
                                <c:when test="${showNo<=2}">
                                    <li>
                                        <p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                                original="${uf:parseSSFace(img.m)}" data-jw="${img.w}"
                                                data-jh="${img.h}"/></p>
                                        <c:set value="${showNo+1}" var="showNo"/>
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
                            </c:if>
                        </c:forEach>
                    </ul>
                    <c:if test="${fn:length(blogContent.content.images.images)>3}">
                        <p>（共${fn:length(blogContent.content.images.images)}张，点击图片查看更多）</p>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </c:if>

        <c:if test="${fn:length(blogContent.rootContent.videos.videos)>0}">
            <c:forEach var="video" items="${blogContent.rootContent.videos.videos}">
                <div class="single_video">
                    <a name="preivewvideo" href="javascript:void(0);" data-flashurl="${video.flashUrl}">
                        <img width="188" height="141" src="${video.url}">
                    </a>
                    <a name="preivewvideo" class="video_btn" title="播放" href="javascript:void(0)"
                       onclick="javascript:pBigVideo('${blogContent.rootContent.contentId}');"></a>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${fn:length(blogContent.rootContent.audios.audios)>0}">
            <c:choose>
                <c:when test="${(blogContent.rootContent.content=='' || fn:length(blogContent.rootContent.content)==0 )
                                && (blogContent.rootContent.subject=='' || fn:length(blogContent.rootContent.subject)==0 )
                                             && fn:length(blogContent.rootContent.videos.videos)==0
                                             && fn:length(blogContent.rootContent.images.images)==0}">
                    <!--只有音乐-->
                    <c:forEach var="audio" items="${blogContent.rootContent.audios.audios}">
                        <div class="single_music">
                            <object align="middle" width="283" height="33"
                                    classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                                    codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                                <param name="allowScriptAccess" value="sameDomain">
                                <param name="movie" value="${audio.flashUrl}">
                                <param name="wmode" value="Transparent">
                                <embed width="283" height="33" wmode="Transparent" type="application/x-shockwave-flash"
                                       src="${audio.flashUrl}">
                            </object>
                        </div>
                        <div class="single_music_view clearfix">
                            <div style="background: url(${URL_LIB}/static/theme/default/img/loading.gif) 60px 65px no-repeat">
                                <img width="141" height="147" class="lazy"
                                     src="${URL_LIB}/static/theme/default/img/pixel.gif"
                                     original="${uf:parseAudioM(audio.url)}">
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach var="audio" items="${blogContent.rootContent.audios.audios}">
                        <div class="single_music">
                            <object align="middle" width="283" height="33"
                                    classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                                    codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                                <param name="allowScriptAccess" value="sameDomain">
                                <param name="movie" value="<c:out value="${audio.flashUrl}"/>">
                                <param name="wmode" value="Transparent">
                                <embed width="283" height="33" wmode="Transparent" type="application/x-shockwave-flash"
                                       src="<c:out value="${audio.flashUrl}"/>">
                            </object>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:if>
    </c:otherwise>
</c:choose>
<!--音频ending -->
<!--长文发布 ending-->