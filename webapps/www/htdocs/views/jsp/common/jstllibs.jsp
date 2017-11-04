<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dateutil" uri="/WEB-INF/tags/dateutil.tld" %>
<%@ taglib prefix="content" uri="/WEB-INF/tags/content.tld" %>
<%@ taglib prefix="mood" uri="/WEB-INF/tags/mood.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tags/sys-tag.tld" %>
<%@ taglib prefix="uf" uri="/WEB-INF/tags/userface.tld" %>
<%@ taglib prefix="icon" uri="/WEB-INF/tags/icon.tld" %>
<%@ taglib prefix="jstr" uri="/WEB-INF/tags/stringutil.tld" %>
<%@ taglib prefix="qnuptk" uri="/WEB-INF/tags/qnuptk.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="userSession" value="${sessionScope.susercenter}"/>
<c:set var="domainSuffix" value=".joyme.com"/><c:set var="version" value="160815"/>
<c:set var="jmh_title" value="着迷网Joyme.com"/>
<c:set var="urlUpload" value="${st:randUploadDomain()}"/>
<c:set var="shutDownRDomain" value="${st:downResourceDomainsJson()}"/>
<c:set var="openSync" value="${st:openSyncProvider()}"/>
    <st:SysConstant var="URL_WWW"/>
<st:SysConstant var="URL_LIB"/>
<st:SysConstant var="URL_STATIC"/>
<st:SysConstant var="URL_M"/>
<st:SysConstant var="DOMAIN"/>
<st:SysConstant var="URL_UC"/>