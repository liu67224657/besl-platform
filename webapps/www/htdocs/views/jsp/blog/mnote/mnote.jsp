<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="Keywords" content="${seoDTO.keywords}">
    <meta name="description" content="${seoDTO.desc} ${jmh_title}"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${seoDTO.title}_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/joymemobile.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<div id="wrap-article">
	<c:choose>
    <c:when test="${blogContent.content!=null && !(blogContent.content.removeStatus.code eq 'y')}">
        <c:choose>
            <c:when test="${blogContent.content.contentType.hasPhrase()}">
                <%@ include file="/views/jsp/blog/mnote/mnote-phrase.jsp" %>
            </c:when>
            <c:otherwise>
                <%@ include file="/views/jsp/blog/mnote/mnote-text.jsp" %>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <div class="contentremove">
            <p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>
        </div>
    </c:otherwise>
</c:choose>
    <%@ include file="/views/jsp/blog/mnote/mfooter.jsp" %>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" id="bdshare_js" data="type=slide&amp;img=8&amp;pos=right&amp;uid=6528936"></script>
<script type="text/javascript" id="bdshell_js"></script>
</body>
</html>
