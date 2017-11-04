<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>注册 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<!--头部开始-->
<div id="joyme-head-2013">
    <div class="head-2013-box clearfix">
        <a id="joyme-logo" href="http://www.joyme.com" alt="着迷网"></a>

        <div class="head-2013-left">
            <span><a href="http://www.joyme.com">首页</a></span>
            <span><a href="http://www.joyme.com/news/official/">手游资讯</a></span>
            <span><a href="http://wiki.joyme.com/">WIKI</a></span>
            <span><a href="http://www.joyme.com/gift">礼包中心</a></span>
            <span><a href="http://bbs.joyme.com/">论坛</a></span>
            <span><a href="http://html.joyme.com/mobile/gameguides.html">应用</a></span>
        </div>
        <div class="head-2013-right">
            <!-- 搜索框 -->
            <div class="search-new">
                <input id="stype" type="hidden" value="" name="stype">
                <input id="txt_search" type="text" class="search-new-textInput" value="找找你感兴趣的..."
                       onblur="if(this.value==''){this.value='找找你感兴趣的...'; this.style.color='#989898';}"
                       onfocus="if(this.value=='找找你感兴趣的...'){this.value=''; this.style.color='#666';}"
                       autocomplete="off" name="key">
                <input id="sericon" class="search-new-submit" type="button" value="">

                <div id="search_tips" class="search_sth" style="display:none">
                    <div class="searcht"></div>
                    <div class="searchc">
                        <ul>
                            <h3>请选择搜索范围</h3>
                            <li id="content">含"<b></b>"的文章 >></li>
                            <li id="profile">含"<b></b>"的人 >></li>
                            <li id="group">含"<b></b>"的小组 >></li>
                            <li id="game">含"<b></b>"的游戏 >></li>
                        </ul>
                    </div>
                    <div class="searchb"></div>
                </div>
            </div>

            <div class="unlogin">
                <a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=${reurl}"
                   class="qq" title="qq号码登录" rel="nofollow"></a>
                <a href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=${reurl}"
                   class="weibo" title="新浪微博登录" rel="nofollow"></a>
                <a href="http://www.joyme.com/registerpage">注册</a>
                <a href="http://www.joyme.com/loginpage?reurl=${reurl}" name="slideLogin">登录</a>
            </div>
        </div>
        <!-- 弹出菜单 -->
    </div>
    <span id="memo_f"></span>
    <span id="login_f"></span>
</div>
<!--头部结束-->

<div class="content clearfix">
    <div class="reg-left">
        <h2>注册着迷</h2>

        <p>哦哦！真高兴你能来！(๑>◡<๑)<br/>你可以用以下账号直接登录着迷，一分钟完成注册！</p>

        <div><a href="http://passport.${DOMAIN}/auth/thirdapi/qq/bind?reurl=${reurl}" class="reg-sns-qq">QQ帐号登录</a><a
                href="http://passport.${DOMAIN}/auth/thirdapi/sinaweibo/bind?reurl=${reurl}" class="reg-sns-weibo">新浪微博登录</a>
        </div>
    </div>
    <div class="reg-right">
        <h2>登录着迷</h2>

        <p>如果你是老用户，请点击&nbsp;<a href="http://www.joyme.com/loginpage?reurl=${reurl}">这里登录</a></p>
    </div>
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/register-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>
