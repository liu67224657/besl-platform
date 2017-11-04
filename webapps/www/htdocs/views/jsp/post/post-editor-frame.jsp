<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<!-- photo js css end -->
<div class="talk ">
    <%@ include file="/views/jsp/post/post-editor-chat.jsp" %>
    <div class="clearfix"></div>
</div>
<div class="edit ">
    <%@ include file="/views/jsp/post/post-editor-text.jsp" %>
    <div class="clearfix"></div>
</div>
<c:if test="${!userSession.userDetailinfo.completeStatus.getCode() eq 'n'}">
    <div class="commentline"></div>
</c:if>