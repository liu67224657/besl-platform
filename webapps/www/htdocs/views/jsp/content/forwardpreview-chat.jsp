<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<!--短文格式-->
<div>
    <p>${blogContent.rootContent.content}</p>
</div>
<!--图片显示-->
<c:if test="${fn:length(blogContent.rootContent.images.images)>0}">
    <c:choose>
        <c:when test="${fn:length(blogContent.rootContent.images.images)==1}">
            <c:forEach var="img" items="${blogContent.rootContent.images.images}">
            <div class="single_pic" id="img_${blogContent.rootContent.contentId}" data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}">
                    <ul>
                        <li><p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                    original="${uf:parseSSFace(img.ss)}" data-jw="${img.w}" data-jh="${img.h}"/></p>
                        </li>
                    </ul>
            </div>
             </c:forEach>
        </c:when>
        <c:otherwise>
            <%--<div class="cont_pic_s" id="img_${blogContent.rootContent.contentId}">--%>
            <ul class="multipic clearfix" data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}">
                <c:forEach var="img" items="${blogContent.rootContent.images.images}" varStatus="status">
                    <c:choose>
                        <c:when test="${status.index<=2}">
                            <li>
                                <p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                        original="${uf:parseSSFace(img.s)}" data-jw="${img.w}" data-jh="${img.h}"/></p>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li style="display:none">
                                <p><img class="lazy" src="${URL_LIB}/static/theme/default/img/loading.gif"
                                        original="${uf:parseMFace(img.m)}" data-jw="${img.w}" data-jh="${img.h}"/></p>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </ul>
            <c:if test="${fn:length(blogContent.rootContent.images.images)>3}">
                <p>（共${fn:length(blogContent.rootContent.images.images)}张，点击图片查看更多）</p>
            </c:if>
            <%--</div>--%>
        </c:otherwise>
    </c:choose>
</c:if>
<!--视频-->
<c:if test="${fn:length(blogContent.rootContent.videos.videos)>0}">
    <c:forEach var="video" items="${blogContent.rootContent.videos.videos}">
        <div class="single_video">
            <a name="preivewvideo" href="javascript:void(0)" data-flashurl="${video.flashUrl}">
                <img width="188" height="141" src="${video.url}">
            </a>
            <a name="preivewvideo" class="video_btn" title="播放" href="javascript:void(0)"
               data-flashurl="${video.flashUrl}"></a>
        </div>
    </c:forEach>
</c:if>
<!--视频 ending-->

<!--音频 -->
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
                        <img width="141" height="147" class="lazy" src="${URL_LIB}/static/theme/default/img/pixel.gif"
                             original="${uf:parseAudioM(audio.url)}">
                    </div>
                </div>
                <%--<div class="cont_music">--%>
                <%--<div class="cont_music_left">--%>
                <%--<a href="javascript:void(0)">--%>
                <%--<span><img width="150" src="${uf:parseAudioM(audio.url)}"></span>--%>
                <%--</a>--%>
                <%--</div>--%>
                <%--<div class="cont_music_right">--%>
                <%--<h2><c:out value="${audio.title}"/></h2>--%>
                <%--<object width="283" height="33" align="middle"--%>
                <%--classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"--%>
                <%--codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">--%>
                <%--<param name="allowScriptAccess" value="sameDomain">--%>
                <%--<param name="movie"--%>
                <%--value="<c:out value="${audio.flashUrl}"/>">--%>
                <%--<param name="wmode" value="Transparent">--%>
                <%--<embed width="283" height="33" wmode="Transparent"--%>
                <%--type="application/x-shockwave-flash"--%>
                <%--src="<c:out value="${audio.flashUrl}"/>">--%>
                <%--</object>--%>
                <%--</div>--%>
                <%--</div>--%>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <%--<div class="cont_music">--%>
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
                <%--<div class="cont_music_right">--%>
                <%--<object width="283" height="33" align="middle"--%>
                <%--classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"--%>
                <%--codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">--%>
                <%--<param name="allowScriptAccess" value="sameDomain">--%>
                <%--<param name="movie"--%>
                <%--value="<c:out value="${audio.flashUrl}"/>">--%>
                <%--<param name="wmode" value="Transparent">--%>
                <%--<embed width="283" height="33" wmode="Transparent"--%>
                <%--type="application/x-shockwave-flash"--%>
                <%--src="<c:out value="${audio.flashUrl}"/>">--%>
                <%--</object>--%>
                <%--</div>--%>
            </c:forEach>
            <%--</div>--%>
        </c:otherwise>
    </c:choose>
</c:if>

<c:if test="${blogContent.rootContent.vote !=null}">
    <div class="poll_box_normal" id="forward_${blogContent.content.contentId}_${blogContent.rootContent.contentId}">
        <c:choose>
            <c:when test="${fn:length(blogContent.rootContent.vote.voteSubject.imageSet.images)>0}">
                <c:forEach var="img" items="${blogContent.rootContent.vote.voteSubject.imageSet.images}">
                    <img class="lazy" src="${URL_LIB}/static/theme/default/img/loading-big.gif"
                         original="<c:out value="${uf:parseSSFace(img.ss)}"/>" data-jw="${img.w}"
                         data-jh="${img.h}" width="102" height="101"/>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <img src="${URL_LIB}/static/theme/default/img/poll_box_normal_ico.jpg">
            </c:otherwise>
        </c:choose>
        <h3><c:out value="${jstr:subStr(blogContent.rootContent.vote.voteSubject.subject,15,'…')}"></c:out></h3>
        <c:forEach var="option" items="${blogContent.rootContent.vote.voteOptionMap}" varStatus="status">
            <c:if test="${status.index<2}">
            <p>· <c:out value="${jstr:subStr(option.value.description,12,'…')}"></c:out></p>
            </c:if>
        </c:forEach>
        <c:if test="${fn:length(blogContent.rootContent.vote.voteOptionMap)>2}">
            <p>...</p>
        </c:if>
        <a class="submit_poll" name="prePartVote" data-domid="forward_${blogContent.content.contentId}_${blogContent.rootContent.contentId}" data-did="${blogContent.rootContent.contentId}" data-duno="${blogContent.rootContent.uno}" data-sid="${blogContent.rootContent.vote.voteSubject.subjectId}"><span>投 票</span></a>
    </div>
</c:if>