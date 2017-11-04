<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dateutil" uri="/WEB-INF/tags/dateutil.tld" %>
<%@ taglib prefix="st" uri="/WEB-INF/tags/sys-tag.tld" %>
<%@ taglib prefix="uf" uri="/WEB-INF/tags/userface.tld" %>
<%@ taglib prefix="icon" uri="/WEB-INF/tags/icon.tld" %>
<%@ taglib prefix="jstr" uri="/WEB-INF/tags/stringutil.tld" %>
<%@ taglib prefix="ykt" uri="/WEB-INF/tags/youkutag.tld" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%--<c:set var="userSession" value="${sessionScope.susercenter}"/>--%>
<c:set var="domainSuffix" value=".joyme.com"/>
<c:set var="isLogin" value="${ykt:isLogin(pageContext.request)}"/>
<c:set var="user_nickname" value="${jstr:decodeUrl(cookie['jmuc_nn'].value,'UTF-8')}"/>
<c:set var="user_uno" value="${cookie['jmuc_uno'].value}"/>
<c:set var="user_uid" value="${cookie['jmuc_u'].value}"/>
<c:set var="user_profileid" value="${cookie['jmuc_pid'].value}"/>
<c:set var="user_token" value="${cookie['jmuc_token'].value}"/>

<st:SysConstant var="URL_WWW"/>
<st:SysConstant var="URL_LIB"/>
<st:SysConstant var="DOMAIN"/>
<st:SysConstant var="YK_DOMAIN"/>