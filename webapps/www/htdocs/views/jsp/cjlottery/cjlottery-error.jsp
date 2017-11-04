<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <title>着迷 cj2013</title>
    <link href="${URL_LIB}/static/theme/default/css/cj2013.css?${version}" rel="stylesheet" type="text/css">

    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<div id="cj2013-topline"></div>
<div id="cj2013-content">
    <div id="cj2013-banner"></div>
    <div class="cj2013-box">
        <h1 class="cj2013-title"></h1>

        <h2 class="cj2013-sub-title">对不起</h2>

        <p class="input-error" align="center"><c:if test="${fn:length(message)>0}"><span style="color: red"><fmt:message key="${message}" bundle="${userProps}"/></span></c:if></p>

        <div class="form-box">
            <form>
                <p>感谢您参加着迷网CHINAJOY 2013现场活动！</p>
                <br/>
                <br/>
                <br/>
                <p>着迷网积分专区：<a href="http://www.joyme.com/giftmarket/">http://www.joyme.com/giftmarket/</a></p>
                <br/>
                <p>着迷网首页：<a href="http://www.joyme.com/">http://www.joyme.com/</a></p>
            </form>
        </div>
    </div>
</div>
<div class="blank10"></div>

<script type="text/javascript">
    /*兼容IE9及以下不支持placeholder*/
    var testInput = document.createElement('input');
    if (typeof testInput['placeholder'] == 'undefined') {
        testInput = null;
        var input_email = document.getElementById('userid');
        var input_pwd = document.getElementById('password');
        creatPlaceHolder(input_email);
        creatPlaceHolder(input_pwd);
    }
    function creatPlaceHolder(tag) {
        var parent = tag.parentNode;
        parent.style.height = '40px';
        parent.style.position = 'relative';

        var phdText = document.createElement('span');
        phdText.innerHTML = tag.getAttribute('phd');
        phdText.style.position = 'absolute';
        phdText.style.left = '5px';
        phdText.style.top = '12px';
        phdText.style.color = '#757575';
        phdText.style.fontSize = '16px';
        phdText.style.zIndex = '1';

        parent.appendChild(phdText);

        tag.style.background = 'transparent';
        tag.style.position = 'absolute';
        tag.style.left = '0px';
        tag.style.top = '0px';
        tag.style.zIndex = '2';

        tag.onfocus = function() {
            phdText.style.display = 'none';
        }

        tag.onblur = function() {
            if (/^\s+$/.test(this.value) || this.value == '' || this.value == phdText.innerHTML) {
                this.value = '';
                phdText.style.display = '';
            }
        }
    }
    testInput = null;
</script>
<script src="${URL_LIB}/static/js/common/bdhm-noseajs.js" type="text/javascript"></script>
<script src="${URL_LIB}/static/js/common/google-statistics-noseajs.js" type="text/javascript"></script>
</body>
</html>