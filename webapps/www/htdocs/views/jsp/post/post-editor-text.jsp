<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<%--<div id="div_post_text" <c:if test="${textHide!=null && textHide}">style="display:none"</c:if>>--%>
<div id="div_post_text" style="display:none">
    <form action="${ctx}/json/content/post/text" method="post" id="posttext" onsubmit="return false;">
        <input type="hidden" name="ecateid" value="${viewCategory.categoryId}"/>

        <%--文本框开始--%>
        <div class="editcon clearfix" id="editcon">
            <%--标题框--%>
            <div class="edit_title">
                标题:<input name="blogsubject" type="text" class="edittext" id="blogSubject" style="color: rgb(204, 204, 204);" value="给你的文章加个标题吧"/>
            </div>
            <%--标题框结束--%>
            <div id="d_editor" class="editor">
                <%--切换模式--%>
                <div class="pump_up">
                    <a id="link_chatmodel" href="javascript:void(0)" class="talk_icon" title="点击进短文章发布模式"></a>
                </div>
                <%--长文文本框--%>
                <div class="edit_hd">

                    <a href="javascript:void(0)" onclick="return false;" id="editor_b" class="long_b" title="加粗"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_split" class="long_fen" title="分割线"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_mood" class="long_face" title="表情"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_pic" class="long_pic" title="图片"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_link" class="long_piclink" title="图片链接"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_audio" class="long_music" title="音乐"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_video" class="long_video" title="视频"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_tag" class="long_tag" title="标签"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_friend" class="long_at" title="@朋友"></a>
                    <a href="javascript:void(0)" onclick="return false;" id="editor_app" class="long_app" title="APP"></a>


                </div>
                <div class="edit_bd">
                    <textarea id="text_content" name="content"></textarea>
                </div>
                <%--长文文本框结束--%>
            </div>
            <%--长文更长模式--%>
            <div class="unfold">
                <a href="javascript:void(0)" class="unfold_bd" id="edit_text_height">
                    <span class="unfold_btn"></span>
                </a>
            </div>
            <%--长文更长模式结束--%>
        </div>
        <%--文本框结束--%>
        <%--发布按钮--%>
        <div class="edit">
            <div class="publish">
                <input id="post_text_submit" name="" type="button" class="publish_btn" value=""/>
            </div>
            <c:if test="${openSync}">
                <span class="tongbu"><input type="checkbox" id="publish_s" class="publish_s" name="sync"
                <c:choose>
                    <c:when test="${fn:length(syncProviderSet)>0}">checked="true"</c:when>
                    <c:otherwise>disabled="true"</c:otherwise>
                </c:choose>>
                    <label for="publish_s"><span>同步</span></label>
                    <em class="install" id="text_syn_install" title="同步设置"></em>
                </span>
            </c:if>
        </div>
        <%--发布按钮结束--%>
    </form>
</div>