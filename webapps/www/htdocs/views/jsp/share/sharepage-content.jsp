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
    <meta name="Keywords" content="着迷、着迷网、游戏社区、游戏攻略、游戏交友">
    <meta name="description" content="来着迷小组寻找推荐游戏、实用攻略、向游戏达人提问、结交志同道合的朋友。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>着迷分享_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/share.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<div id="wiki">
    <div class="login_logo"></div>
    <div id="wiki-share" class="clearfix">
        <h2 class="wiki-share-title">推荐到其他社区</h2>

        <form action="" method="post" id="share_form">
            <input type="hidden" name="topic" value="${shareInfo.shareTopic.shareTopic}"/>
            <input type="hidden" name="pic" value="${shareInfo.shareBody.picUrl}"/>
            <input type="hidden" name="url" value="${shareInfo.baseInfo.shareSource}"/>
           <input type="hidden" name="shareid" value="${shareInfo.baseInfo.shareId}"/>
            <div class="wiki-share-status">
                <span class="fl">#${shareInfo.shareTopic.shareTopic}#</span>还可以输入<em id="body_num">120</em>个字
            </div>
            <div class="wiki-share-box">
                <div class="clearfix">
                    <textarea name="body" id="textarea_sharebody">${shareInfo.shareBody.shareBody}</textarea>
                    <div class="wiki-share-img"><img src="${shareInfo.shareBody.picUrl}" width="130" height="100"></div>
                </div>
                <div class="wiki-share-btn clearfix">
                    <c:choose>
                        <c:when test="${!bindCodeSet.contains('qq')}">
                            <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=http://www.joyme.com/share/contentpage?sid=${shareInfo.baseInfo.shareId}&icn=true">推荐到QQ空间</a>
                        </c:when>
                        <c:otherwise>
                            <a href="javascript:void(0);" class="data-submit" data-key="qq">推荐到QQ空间</a>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${!bindCodeSet.contains('sinaweibo')}">
                            <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=http://www.joyme.com/share/contentpage?sid=${shareInfo.baseInfo.shareId}&icn=true">推荐到新浪微博</a>
                        </c:when>
                        <c:otherwise>
                            <a href="javascript:void(0);" class="data-submit" data-key="sinaweibo">推荐到新浪微博</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form>
        <c:choose>
            <c:when test="${userSession==null}">
                <div class="wiki-share-tips">登录得积分，推荐换好礼！</div>
        <div class="wiki-share-login-tips">用着迷帐号 <a href="${URL_WWW}/loginsimplepage?reurl=/share/contentpage?sid=${shareInfo.baseInfo.shareId}">登录</a> | 还没有帐号？<a
                href="${URL_WWW}/registerpage?reurl=/share/contentpage?sid=${shareInfo.baseInfo.shareId}">立即注册</a></div>
        <div class="wiki-share-platform">
            <span>你还可以用以下方式登录：</span>
            <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=/share/contentpage?sid=${shareInfo.baseInfo.shareId}&icn=true"
               class="wiki-share-qq"></a>
            <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=/share/contentpage?sid=${shareInfo.baseInfo.shareId}&icn=true"
               class="wiki-share-sina"></a>
        </div>
            </c:when>
            <c:otherwise>
               <div class="wiki-share-haslogined">你已经登录着迷，推荐后将获得积分奖励！<a href="${URL_WWW}/giftmarket">什么是积分<em></em>？</a></div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    var actionPrefix = joyconfig.URL_WWW + '/share/content/';
    seajs.use('${URL_LIB}/static/js/init/share-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
