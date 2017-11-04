<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="ajaxPageStart" value="${ajaxpage.curPage/5+1}" />
<c:set var="ajaxPageEnd" value="${ajaxPageStart+5}" />
<c:if test="${ajaxPageEnd>ajaxpage.maxPage}">
   <c:set var="ajaxPageEnd" value="${ajaxpage.maxPage}" />
</c:if>

<div id="chidren_page_area" class="discuss_page" data-start="1" data-end="${ajaxPageEnd}">
     <c:if test="${ajaxpage.curPage != 1}">
        <a name="chidren_reply_page" href="javascript:void(0)" data-pno="1" data-oid="${replyDto.reply.reply.rid}">首页</a>
        <a name="chidren_reply_page" href="javascript:void(0)" data-pno="${ajaxpage.curPage-1}" data-oid="${replyDto.reply.reply.rid}">上一页</a>
    </c:if>
    <c:forEach var="pageNo" begin="${ajaxPageStart}" end="${ajaxPageEnd}">
        <c:choose>
            <c:when test="${pageNo==ajaxpage.curPage}">
                <b name="chidren_reply_page" data-pno="${pageNo}" data-oid="${replyDto.reply.reply.rid}">${pageNo}</b>
            </c:when>
            <c:otherwise>
                <a name="chidren_reply_page" href="javascript:void(0)" data-pno="${pageNo}" data-oid="${replyDto.reply.reply.rid}">${pageNo}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${ajaxpage.curPage != ajaxpage.maxPage}">
        <a name="chidren_reply_page" href="javascript:void(0)" data-pno="${ajaxpage.curPage+1}" data-oid="${replyDto.reply.reply.rid}">下一页</a>
        <a name="chidren_reply_page" href="javascript:void(0);" data-pno="${ajaxpage.maxPage}" data-oid="${replyDto.reply.reply.rid}">末页</a>
    </c:if>
</div>