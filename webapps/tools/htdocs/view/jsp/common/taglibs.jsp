<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="uf" uri="/WEB-INF/tags/userface.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tags/sys-tag.tld" %>
<%@ taglib prefix="p" uri="/WEB-INF/tags/privilege.tld" %>
<%@ taglib prefix="pg" uri="/pager-taglib.tld" %>
<%@ taglib prefix="date" uri="/WEB-INF/tags/date.tld" %>

<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="toolsProps"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="errorProps"/>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="infostatus" value="${sessionScope.infostatus}"/>
<c:set var="currentUser" value="${sessionScope.current_user}"/>
<c:set var="domainSuffix" value=".joyme.com"/>
<c:set var="version" value=""/>
<c:set var="jmh_title" value="着迷网 - 后台管理工具"/>
<st:SysConstant var="URL_TOOLS"/>
<st:SysConstant var="URL_WWW"/>
<st:SysConstant var="URL_LIB"/>
<st:SysConstant var="DOMAIN"/>
<c:set var="urlUpload" value="${st:randUploadDomain()}"/>
<c:set var="shutDownRDomain" value="${st:downResourceDomainsJson()}"/>
<c:set var="at" value="${sessionScope.upload_token.token}"/>

<script type="text/javascript">
    var infostatus = '${infostatus}';
    var ctx = '${ctx}';
    var URL_TOOLS = '${URL_TOOLS}';
    var URL_WWW = '${URL_WWW}';
    var URL_LIB = '${URL_LIB}';
    var DOMAIN = '${DOMAIN}';
    var uploaddomain = '${urlUpload}';
    var shutDownRDomain = '${shutDownRDomain}';
    var at='${at}';
</script>
