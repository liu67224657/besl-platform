<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<c:forEach items="${discovery}" var="disc" varStatus="st">
    <c:choose>
        <c:when test="${disc.contentType.hasImage()}">
            <div class="find_box">
                <div class="wbox" title=""
                     onclick="gethref('<c:out value="${disc.contentId}"/>','<c:out value="${disc.domainName}"/>');">
                    <div class="find_img"><p><img src="<c:out value="${uf:parseSSFace(disc.thumbImgLink)}"/>"/>
                    </p></div>
                    <div class="find_txt">
                         ${disc.screenName}
                             <c:if test="${disc.verifyType !=null && disc.verifyType.code!= 'n'}">
                                 <a class="${disc.verifyType.code}vip" title="<fmt:message key="verify.profile.${disc.verifyType.code}" bundle="${userProps}"/>"></a>
                             </c:if>
                                    <%--<c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'p'}">--%>
                                        <%--<a class="vip" title="${vip_title}"></a>--%>
                                    <%--</c:if>--%>
                                    <%--<c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'c'}">--%>
                                        <%--<a class="tvip" title="${vipc_title}"></a>--%>
                                    <%--</c:if>--%>
                             ：${disc.wallContent}
                    </div>
                </div>
            </div>
        </c:when>
        <c:when test="${disc.contentType.hasVideo()}">
            <div class="find_box">
                <div class="wbox" title=""
                     onclick="gethref('<c:out value="${disc.contentId}"/>','<c:out value="${disc.domainName}"/>');">
                    <div class="find_img"><p><img src="<c:out value="${uf:parseSSFace(disc.thumbImgLink)}"/>"/>
                    <a class="video_btn" href="javascript:void(0)" title="播放" onclick="gethref('<c:out value="${disc.contentId}"/>','<c:out value="${disc.domainName}"/>');"></a>
                    </p></div>
                    <div class="find_txt">
                        ${disc.screenName}
                                    <c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'p'}">
                                        <a class="vip" title="${vip_title}"></a>
                                    </c:if>
                                    <c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'c'}">
                                        <a class="tvip" title="${vipc_title}"></a>
                                    </c:if>
                            ：${disc.wallContent}
                    </div>
                </div>
            </div>
        </c:when>
        <c:when test="${disc.contentType.hasAudio()}">
            <div class="find_box">
                <div class="wbox" title=""
                     onclick="gethref('<c:out value="${disc.contentId}"/>','<c:out value="${disc.domainName}"/>');">
                    <div class="find_img"><p><img src="<c:out value="${uf:parseSSFace(disc.thumbImgLink)}"/>"/>
                    </p></div>
                    <div class="find_txt">
                       ${disc.screenName}
                                    <c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'p'}">
                                        <a class="vip" title="${vip_title}"></a>
                                    </c:if>
                                    <c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'c'}">
                                        <a class="tvip" title="${vipc_title}"></a>
                                    </c:if>
                           ：${disc.wallContent}
                    </div>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div class="find_box">
                <div class="wbox" title=""
                     onclick="gethref('<c:out value="${disc.contentId}"/>','<c:out value="${disc.domainName}"/>');">
                    <div class="find_txt_only">
                       ${disc.screenName}
                                    <c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'p'}">
                                        <a class="vip" title="${vip_title}"></a>
                                    </c:if>
                                    <c:if test="${disc.verifyType !=null && disc.verifyType.getCode() eq 'c'}">
                                        <a class="tvip" title="${vipc_title}"></a>
                                    </c:if>
                           ：${disc.wallContent}
                    </div>
                </div>
            </div>

        </c:otherwise>
    </c:choose>
</c:forEach>
