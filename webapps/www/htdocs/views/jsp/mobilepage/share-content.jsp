<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="Keywords" content="着迷、着迷网、游戏社区、游戏攻略、游戏交友">
    <meta name="description" content="来着迷小组寻找推荐游戏、实用攻略、向游戏达人提问、结交志同道合的朋友。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>着迷分享_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/share.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/joymemobile.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>

<div id="wrap-snsLogin">

    <div class="joymeMobile-title-bg">
        <h1 class="joymeMobile-title-2">推荐到<fmt:message key="share.provider.${bindDomain}" bundle="${userProps}"/><span>你好，${userSession.blogwebsite.screenName}</span>
        </h1>
    </div>

    <div class="share-box">
        <form action="share" method="post" id="share_form">
            <h2>#${shareInfo.shareTopic.shareTopic}#</h2>
            <input type="hidden" name="title" value="${shareInfo.shareBody.shareSubject}"/>
            <input type="hidden" name="topic" value="${shareInfo.shareTopic.shareTopic}"/>
            <input type="hidden" name="pic" value="${shareInfo.shareBody.picUrl}"/>
            <input type="hidden" name="url" value="${shareInfo.baseInfo.shareSource}"/>
            <input type="hidden" name="shareid" value="${shareInfo.baseInfo.shareId}"/>

            <div class="textbox">
                <textarea name="body" id="textarea_sharebody">${shareInfo.shareBody.shareBody}</textarea>
            </div>
            <p>你已经登录着迷，推荐后将获得积分奖励！<a href="${URL_WWW}/giftmarket" class="tips">什么是积分<em></em>？</a></p>

            <div class="btn-box">
                <c:choose>
                    <c:when test="${bindDomain=='qweibo'}">
                        <a href="javascript:void(0)" class="data-submit btn btn-2" data-key="${bindDomain}">推荐到腾讯微博</a>
                    </c:when>
                    <c:when test="${bindDomain=='qq'}">
                        <a href="javascript:void(0)" class="data-submit btn btn-2" data-key="${bindDomain}">推荐到QQ空间</a>
                    </c:when>
                    <c:when test="${bindDomain=='sinaweibo'}">
                        <a href="javascript:void(0)" class="data-submit btn btn-2" data-key="${bindDomain}">推荐到新浪微博</a>
                    </c:when>
                </c:choose>

            </div>
            <p>如果这不是你的常用账号，你可以更换账号</p>

            <div class="btn-box">

                <a href="${URL_WWW}/mloginpage?reurl=${reurl}" class="btn btn-3">更换帐号</a></div>
        </form>
    </div>

    <br/>
    <br/>
</div>

</div>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    var actionPrefix = joyconfig.URL_WWW + '/mshare/content/';
    seajs.use('${URL_LIB}/static/js/init/share-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>