<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="w_search">
    <!-- 搜索选项 -->
    <div class="search-choose">
        <c:choose>
            <c:when test="${holdstype=='content'}"><a href="javascript:void(0);" class="current">文章</a></c:when>
            <c:otherwise><a href="${URL_WWW}/search/content/${key}">文章</a></c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${holdstype=='game'}"><a href="javascript:void(0);" class="current">游戏</a></c:when>
            <c:otherwise><a href="${URL_WWW}/search/game/${key}">游戏</a></c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${holdstype=='group'}"><a href="javascript:void(0);" class="current">小组</a></c:when>
            <c:otherwise><a href="${URL_WWW}/search/group/${key}">小组</a></c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${holdstype=='profile'}"><a href="javascript:void(0);" class="current">用户</a></c:when>
            <c:otherwise><a href="${URL_WWW}/search/profile/${key}">用户</a></c:otherwise>
        </c:choose>
    </div>

    <div class="w_searchcon">
        <input type="hidden" id="holdstype" value="${holdstype}" name="holdstype">
        <input type="text" class="w_text" value="${key}"
               onfocus="if(this.value=='找找你感兴趣的...'){this.value=''; this.style.color='#666';}"
               onblur="if(this.value==''){this.value='找找你感兴趣的...'; this.style.color='#989898';}" name="holdsearchkey"
               id="hold_txt_search" autocomplete="off" style="color: rgb(102, 102, 102);">
        <input type="button" class="w_searchbtn" id="holdsearchbtn"/>
    </div>
    <!-- 搜索提示框 -->
    <%--<div style="position: absolute; top: 77px; left: 78px; display: none;" class="search-tips" id="holdsearchtips">--%>
        <%--<div class="wsearc">--%>
            <%--<ul>--%>
                <%--<h3>请选择搜索范围</h3>--%>
                <%--<li id="sText"<c:if test="${holdstype=='sText'}"> class="hover"</c:if>>含"<b>${key}</b>"的文章 >></li>--%>
                <%--<li id="sBlog"<c:if test="${holdstype=='sBlog'}"> class="hover"</c:if>>含"<b>${key}</b>"的人 >></li>--%>
                <%--<li id="sBoard"<c:if test="${holdstype=='sBoard'}"> class="hover"</c:if>>含"<b>${key}</b>"的小组 >></li>--%>
            <%--</ul>--%>
        <%--</div>--%>
    <%--</div>--%>
</div>