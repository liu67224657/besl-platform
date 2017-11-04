<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>

    <div id="div_post_chat" <c:if test="${chatHide!=null && chatHide}">style="display:none"</c:if>>
        <form action="${ctx}/json/content/post/chat" method="post" id="postchat">
            <input type="hidden" name="ecateid" value="${viewCategory.categoryId}"/>
            <div class="talk ">
                <div class="talk_wrapper clearfix">
                    <textarea name="content" id="chat_content" cols="" rows="" class="talk_text" style="font-family:Tahoma, '宋体';"></textarea>

                    <div class="openup">
                        <a href="javascript:void(0)" onclick="return false;" title="展开进入长文发布模式，支持更多功能" class="talk_icon"
                           id="link_textmodel"></a>
                    </div>
                    <div class="preview clearfix" id="rel_preview" style="display:none">
                        <ul id="ul_preview">
                        </ul>
                    </div>
                </div>
            </div>
            <div class="edit">
                <div class="edittool">
                    <a href="javascript:void(0)" onclick="return false;" class="talk_face" id="faceShow" title="表情">表情</a>
                    <a href="#" class="talk_pic" id="pic_more">图片</a>
                    <span class="pic_more" style="display:none;">
                        <a href="javascript:void(0)" onclick="return false;" class="talk_pic" id="relPhoto" title="图片">图片</a><br/>
                        <a href="javascript:void(0)" onclick="return false;" class="talk_pic_link" id="relLinkphoto" title="图片链接">链接</a>
                    </span>
                    <a href="javascript:void(0)" onclick="return false;" class="talk_music" id="relMusic" title="音乐">音乐</a>
                    <a href="javascript:void(0)" class="talk_video" id="relVideo" title="视频">视频</a>
                    <a href="javascript:void(0)" class="talk_tag" id="relTag" title="标签">标签</a>
                    <a href="javascript:void(0)" class="talk_friend" id="relFriend" title="@朋友">朋友</a>
                    <a href="javascript:void(0)" class="talk_app" id="relApp" title="APP">APP</a>
                    <c:if test="${openSync}">
                    <span class="tongbu">|<input type="checkbox" id="publish_s" class="publish_s" name="sync"
                      <c:choose>
                   <c:when test="${fn:length(syncProviderSet)>0}">checked="true"</c:when>
                    <c:otherwise>disabled="true"</c:otherwise>
                      </c:choose>>
                        <label for="publish_s"><span>同步</span></label><em class="install" id="chat_syn_install" title="同步设置"></em>
                    </span>
                    </c:if>
                </div>
                <div class="publish">
                    <input name="" id="post_chat_submit" type="button" class="publish_btn" value="" />
                </div>

            </div>
        </form>
    </div>