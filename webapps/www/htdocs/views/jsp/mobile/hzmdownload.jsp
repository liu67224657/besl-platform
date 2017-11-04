<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <script type="text/javascript">
        <c:choose>
        <c:when test="${code=='wxdh'}">
        window.location.href = "itms-services://?action=download-manifest&url=https://dn-joymeapp.qbox.me/haizeimi-ent-wxdh-1.0.16.plist"
        </c:when>
        <c:when test="${code=='wbdh'}">
        window.location.href = "itms-services://?action=download-manifest&url=https://dn-joymeapp.qbox.me/haizeimi-ent-wbdh-1.0.16.plist"
        </c:when>
        <c:when test="${code=='qqun'}">
        window.location.href = "itms-services://?action=download-manifest&url=https://dn-joymeapp.qbox.me/haizeimi-ent-qqun-1.0.16.plist"
        </c:when>
        <c:when test="${code=='luntan'}">
        window.location.href = "itms-services://?action=download-manifest&url=https://dn-joymeapp.qbox.me/haizeimi-ent-luntan-1.0.16.plist"
        </c:when>
        <c:otherwise>
        window.location.href = "itms-services://?action=download-manifest&url=https://dn-joymeapp.qbox.me/haizeimi-ent-joyme-1.0.20.plist"
        </c:otherwise>
        </c:choose>
    </script>
</head>
<body></body>
</html>