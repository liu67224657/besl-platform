<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@ page import="com.enjoyf.platform.util.http.AppUtil" %>
<%response.setStatus(200);%>
<%
    Throwable ex = null;
    if (exception != null)
        ex = exception;
    if (request.getAttribute("javax.servlet.error.exception") != null)
        ex = (Throwable) request.getAttribute("javax.servlet.error.exception");

    //记录日志
    Logger logger = LoggerFactory.getLogger("500.jsp");
    logger.error(ex.getMessage(), ex);

%>

<%
    if (!AppUtil.checkIsAndroid(request) && !AppUtil.checkIsIOS(request)) {
%>

        <head>
            <title>500 - ${jmh_title}</title>
            <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
            <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
            <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
            <script src="${URL_LIB}/static/js/common/seajs.js"
                    data-main="${URL_LIB}/static/js/init/common-init"></script>
        </head>
        <body>
        <!--头部开始-->
        <c:import url="/views/jsp/passport/header.jsp"/>
        <!--头部结束-->
        <!--中间部分开始-->
        <div class="content clearfix">
            <div class="page_error_500">
                <div class="page_error_con">
                    <p> 囧~</p>

                    <h3>对不起！系统错误</h3>
                </div>
                <div class="page_error_area">
                    <a href="${URL_WWW}" class="see_auto">去首页</a>
                </div>
            </div>
        </div>
        <!--中间部分结束-->
        <!--页尾开始-->
        <%@include file="/views/jsp/tiles/footer.jsp" %>
        <script src="${URL_LIB}/static/js/common/pv.js"></script>
        <!--页尾结束-->
        </body>
        <script>
            window.setTimeout(function(){
                window.location.href="http://www.joyme.com";
            },3000);
        </script>
        </html>
<% } else {%>
        <!doctype html>
        <html lang="en">
        <head>
            <meta charset="utf-8">
            <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
            <meta content="yes" name="apple-mobile-web-app-capable">
            <meta content="black" name="apple-mobile-web-app-status-bar-style">
            <meta content="telephone=no" name="format-detection">
            <title>500</title>
            <link href="${URL_LIB}/static/theme/wap/css/errorpage.css" rel="stylesheet" type="text/css">
        <body>
        <div id="wrapper">
            <div class="no-net">
                <b><img src="${URL_LIB}/static/theme/wap/images/no-net.jpg" alt=""></b>

                <p>页面加载失败</p>

                <p>你访问的页面出了点小插曲</p>
            </div>
        </div>
        </body>
        <script>
            window.setTimeout(function(){
                window.location.href="http://m.joyme.com";
            },3000);
        </script>
        </html>
<%}%>
