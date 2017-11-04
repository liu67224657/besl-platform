<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body style="max-width: 400px;max-height: 300px; overflow: hidden;">
<div id="content" class="content clearfix">
    <div class="conleft">
        <div class="area blogarticle">
            <c:forEach var="audio" items="${blogContent.content.audios.audios}">
                <div class="single_music">
                    <object align="middle" width="283" height="33"
                            classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                            codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                        <param name="allowScriptAccess" value="sameDomain">
                        <param name="movie" value="${audio.flashUrl}">
                        <param name="wmode" value="Transparent">
                        <embed width="283" height="33" wmode="Transparent"
                               type="application/x-shockwave-flash"
                               src="${audio.flashUrl}">
                    </object>
                </div>
                <div class="single_music_view clearfix">
                    <img width="141" height="147" src="${uf:parseAudioM(audio.url)}">
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
