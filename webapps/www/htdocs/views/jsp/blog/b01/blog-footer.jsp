<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="footercon clearfix">
 <div class="footer">
     <%@ include file="/hotdeploy/views/jsp/tiles/all-rights-reserved.jsp" %>
     <span> 京ICP备11029291号</span>
    <a href="${ctx}/help/aboutus" target="_blank">关于着迷</a> |
    <a href="${ctx}/about/job/zhaopin" target="_blank">工作在着迷</a> |
    <a href="mailto:contactus@joyme.com">联系我们</a>|
     <a href="${URL_WWW}/sitemap.htm">网站地图</a>
 </div>
</div>
<div style="display:none">
<script src="http://s4.cnzz.com/stat.php?id=5437085&web_id=5437085" language="JavaScript"></script></div>
<div class="scroll_top" style="right: 112.5px;">
    <c:if test="${blogContent.content.contentType.hasGame()}">
	    <a class="tjgame_btn" href="#gamelist" title="提到的游戏"></a>
    </c:if>
	<a id="favButton" name="favButton" class="<c:choose><c:when test="${blogContent.favorite}">favorite_btn_on</c:when><c:otherwise>favorite_btn</c:otherwise></c:choose>" href="javascript:void(0)" title="<c:choose><c:when test="${!blogContent.rootFavorite}">喜欢</c:when><c:otherwise>取消喜欢</c:otherwise></c:choose>"
                   data-cid="${blogContent.content.contentId}"
                   data-cuno="${blogContent.content.uno}"></a>
    <a class="home_gotop" href="javascript:void(0)" id="linkHome" title="返回" style="display: none;"></a>
</div>

