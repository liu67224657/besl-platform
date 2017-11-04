<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<%--<script type="text/javascript" src="${URL_LIB}/static/third/ckeditor/ckeditor.js"></script>--%>
<script>
    var editAudio;
    var editVideo;
    var editImages=new Array();
    var editTags=new Array();
    <c:forEach var="audio" items="${blogContent.content.audios.audios}">
        editAudio = {flash:'${audio.flashUrl}',album:'${audio.url}',title:''};
    </c:forEach>
    <c:forEach var="video" items="${blogContent.content.videos.videos}">
        editVideo = {flash:'${video.flashUrl}',album:'${video.url}',title:'',orgUrl:'${video.orgUrl}',vtime:'${video.vTime}'};
    </c:forEach>
    <c:forEach var="image" items="${blogContent.content.images.images}" varStatus="st">
        editImages.push({key:'${st.index}',value:{url:'${image.m}',src:'${uf:parseSSFace(image.ss)}',desc:'<c:if test="${fn:length(image.desc)>0}">${image.desc}</c:if>',w:'${image.w}',h:'${image.h}'}});
    </c:forEach>
      <c:forEach items="${blogContent.content.contentTag.tags}" var="tag" varStatus="status">
        editTags.push('${tag}');
     </c:forEach>
</script>

<%@ include file="/views/jsp/edit/edit-chat.jsp" %>
<div class="clearfix"></div>
<%@ include file="/views/jsp/edit/edit-text.jsp" %>
<div class="clearfix"></div>
<div class="commentline"></div>