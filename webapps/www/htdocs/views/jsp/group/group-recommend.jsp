<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<div class="chooseGroupBox">
<div class="chooseGroupBox-top"></div>

<h2 class="chooseGroupBox-title">
	添加小组<a href="javascript:void(0)" id="see_close" class="see_close"></a>
</h2>

<div class="chooseGroup clearfix">
	<ul class="clearfix">
        <c:forEach items="${groups}" var="group">
        <li>
        	<a href="${URL_WWW}/group/${group.code}" target="_blank"><img width="54" height="70" src="${uf:parseOrgImg(group.thumbimg)}" /></a>
            <h2><a href="${URL_WWW}/group/${group.code}" target="_blank" title="${group.title}">
                <c:choose>
                    <c:when test="${fn:length(group.title)>9}">
                        ${fn:substring(group.title,0,9)}
                    </c:when>
                    <c:otherwise>
                        ${group.title}
                    </c:otherwise>
                </c:choose>
            </a></h2>
            <p>
                <c:choose>
                    <c:when test="${fn:length(group.desc)>30}">
                        ${fn:substring(group.desc,0,29)}…
                    </c:when>
                    <c:otherwise>
                        ${group.desc}
                    </c:otherwise>
                </c:choose>
            </p>
            <c:choose>
                <c:when test="${group.relationFlag==null || group.relationFlag=='' ||group.relationFlag=='0' || group.relationFlag=='-1'}">
                    <a class="nojoin" name="followGroup"  data-gid="${group.elementId}" href="javascript:void(0);"><span>加入</span></a>
                </c:when>
                <c:otherwise>
                    <span class="hasjoined">已加入</span>
                </c:otherwise>
            </c:choose>
        </li>
        </c:forEach>
        <li>
        	<a href="${URL_WWW}/group" class="moregroup">更多小组...</a>
        </li>
    </ul>
</div>

<div class="chooseGroupBox-bottom"></div>
</div>