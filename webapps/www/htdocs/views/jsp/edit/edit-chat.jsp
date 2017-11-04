<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<div id="div_post_chat"
        <c:choose>
            <c:when test="${blogContent.content.contentType.hasPhrase()}">style="display:block" </c:when>
            <c:otherwise>style="display:none"</c:otherwise>
        </c:choose>>
    <form action="${ctx}/content/edit/chat/${contentUno}/${contentId}" method="post" id="postchat">
        <input type="hidden" name="returnUrl" value="${returnUrl}"/>
        <input type="hidden" name="contentId" value="${blogContent.content.contentId}"/>

        <div class="talk ">
            <div class="talk_wrapper clearfix">
                <textarea name="content" style="font-family:Tahoma, '宋体';" id="chat_content" cols="" rows="" class="talk_text"><c:if
                        test="${blogContent.content.contentType.hasPhrase()}"><c:out
                        value="${blogContent.content.content}"/></c:if></textarea>

                <div class="openup">
                    <a href="javascript:void(0)" onclick="return false;" title="展开进入长文发布模式，支持更多功能" class="talk_icon" id="link_textmodel"></a>
                </div>
                <div class="preview clearfix" id="rel_preview" <c:choose>
                    <c:when test="${blogContent.content.contentType.hasImage() || blogContent.content.contentType.hasAudio() || blogContent.content.contentType.hasVideo()}">style="display:block;"
                    </c:when>
                    <c:otherwise>style="display:none;"</c:otherwise>
                </c:choose>>
                    <ul id="ul_preview">
                        <c:if test="${blogContent.content.contentType.hasPhrase()}">
                <!-- 图片 -->
                <c:if test="${fn:length(blogContent.content.images.images)>0}">
                    <c:forEach var="img" items="${blogContent.content.images.images}" varStatus="status">
                        <li id="preview_photo_${status.index+1}">
                            <input type="hidden" name="picurl_s" value="${img.s}"/>
                            <input type="hidden" name="picurl_m" value="${img.m}"/>
                            <input type="hidden" name="picurl_b" value="${img.url}"/>
                            <input type="hidden" name="picurl_ss" value="${img.ss}"/>
                            <input type="hidden" name="picurl" value="${uf:parseSSFace(img.ss)}" />
                            <input type="hidden" name="w" value="${img.w}"/>
                            <input type="hidden" name="h" value="${img.h}"/>
                            <c:set var="imgLen" value="${fn:length(img.m)}"/>
                            <a class="picreview" title="Chrysanthemum.jpg" href="javascript:void(0)">${fn:substring(img.m, imgLen-10, imgLen)}</a>
                            <a class="close" title="取消" href="javascript:void(0)"></a>
                        </li>
                    </c:forEach>
                </c:if>
                <!-- 视频 -->
                <c:if test="${fn:length(blogContent.content.videos.videos)>0}">
                    <c:forEach var="video" items="${blogContent.content.videos.videos}">
                        <li id="li_video_preview" >
                            <c:set var="videoLen" value="${fn:length(video.title)}"/>
                            <a href="javascript:void(0)" title="${video.title}" class="video">${fn:substring(video.title, videoLen-10, videoLen)}</a>
                            <a class="close" title="取消" href="javascript:void(0)"></a>
                            <input type="hidden" name="videoUrl" value="${video.flashUrl}"/>
                            <input type="hidden" name="videoAlbum" value="${video.url}"/>
                            <input type="hidden" name="videoTitle" value="${video.title}"/>
                            <input type="hidden" name="viorgurl" value="${video.orgUrl}"/>
                            <input type="hidden" name="vtime" value="${video.vTime}"/>
                        </li>
                    </c:forEach>
                </c:if>
                <!--音频 -->
                <c:if test="${fn:length(blogContent.content.audios.audios)>0}">
                    <c:forEach var="audio" items="${blogContent.content.audios.audios}" varStatus="st">
                        <li id="li_audio_preview" >
                            <a class="musicreview" href="javascript:void(0)">${audio.title}</a>
                            <a class="close" title="取消" href="javascript:void(0)"></a>
                            <input type="hidden" name="audioUrl" value="${audio.flashUrl}"/>
                            <input type="hidden" name="audioAlbum" value="${audio.url}"/>
                            <input type="hidden" name="audioTitle" value="${audio.title}"/>
                        </li>
                    </c:forEach>
                </c:if>
            </c:if>
                    </ul>
                </div>
            </div>
        </div>
        <div class="edit">
            <div class="edittool">
                <a href="javascript:void(0)" onclick="return false;" class="talk_face" id="faceShow">表情</a>
                <a href="javascript:void(0)" onclick="return false;" class="talk_pic" id="pic_more">图片</a>
                    <span class="pic_more" style="display:none;">
                        <a href="javascript:void(0)" onclick="return false;"  class="talk_pic_hover" id="relPhoto">图片</a><br/>
                        <a href="javascript:void(0)" onclick="return false;"  class="talk_pic_link" id="relLinkphoto">链接</a>
                    </span>
                <a href="javascript:void(0)" onclick="return false;" class="talk_music" id="relMusic">音乐</a>
                <a href="javascript:void(0)" onclick="return false;" class="talk_video" id="relVideo">视频</a>
                <a href="javascript:void(0)" onclick="return false;" class="talk_tag" id="relTag">标签</a>
                <a href="javascript:void(0)" onclick="return false;" class="talk_friend" id="relFriend">朋友</a>
                <a href="javascript:void(0)" onclick="return false;" class="talk_app" id="relApp">APP</a>
                <a title="投票" class="poll_btn" href="${URL_WWW}/vote/post/page" target="_blank" id="relVote">投票</a>
            </div>
            <div class="publish">
                        <input name="" id="edit_chat_submit" type="button" class="publish_btn" value=""/>
            </div>

        </div>
    </form>
</div>


