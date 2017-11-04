<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%@ include file="/views/jsp/post/post-chat.jsp" %>
<div class="clearfix"></div>
<%@ include file="/views/jsp/post/post-text.jsp" %>
<div class="clearfix"></div>
<c:if test="${!userSession.userDetailinfo.completeStatus.getCode() eq 'n'}">
<div class="commentline"></div>
</c:if>