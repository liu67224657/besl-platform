<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>媒体报道 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use("${URL_LIB}/static/js/init/common-init.js")
    </script>
</head>
<body>
<c:import url="/views/jsp/passport/header.jsp"/>

<div class="content about-bg clearfix">
    <!-- 左侧导航 -->
    <div class="about-nav">
			<ul>
				<li><a href="${URL_WWW}/help/aboutus">关于着迷</a><span></span></li>
                <li><a href="${URL_WWW}/help/milestone">着迷里程碑</a><span></span></li>
				<li><a href="${URL_WWW}/about/contactus">商务合作</a><span></span></li>
				<li><a href="${URL_WWW}/about/job/zhaopin">工作在着迷</a><span></span></li>
				<li><a href="${URL_WWW}/help/law">法律声明</a><span></span></li>
				<li><a href="${URL_WWW}/help/service">服务条款</a><span></span></li>
				<li class="current"><a href="${URL_WWW}/about/press">媒体报道</a><span></span></li>
                <li><a href="${URL_WWW}/help/law/parentsprotect">家长监护</a><span></span></li>
			</ul>
		</div>
        <div class="about-media-report">
		<h2 class="about-title"><span class="about-title-text-1">媒体报道</span></h2>
		<ul>
            <li><a href="http://game.donews.com/201307/1563656.shtm" target="_blank">着迷网CEO陈阳：做深度的工具化精品攻略服务</a>(Donews 2013-7-30)</li>
         			<li><a href="http://game.163.com/13/0730/11/951CU7G300314J6K.html" target="_blank">着迷攻略for百万亚瑟王正式登陆APP Store</a>(网易 2013-7-30)</li>
         			<li><a href="http://games.sina.com.cn/y/n/2013-07-28/2025721864.shtml" target="_blank">着迷网陈阳：专注移动游戏攻略留住核心用户</a> (新浪2013-7-28)</li>
                     <li><a href="http://www.techweb.com.cn/people/2013-07-27/1312807.shtml" target="_blank">着迷网陈阳：游戏攻略App化仍是创业市场空白</a> (Techweb 2013-7-27)</li>
                     <li><a href="http://games.ifeng.com/special/2013chinajoy/fangtan/detail_2013_07/26/27964052_0.shtml" target="_blank">着迷网创始人陈阳：打造移动游戏攻略第一站</a> (凤凰2013-7-26)</li>
         			<li><a href="http://games.qq.com/a/20130618/020110.htm" target="_blank">着迷移动攻略应用 全面支持离线浏览</a> (腾讯 2013-6-18)</li>
         			<li><a href="http://game.163.com/13/0523/17/8VIVDOLF00314J6K.html" target="_blank">嘿！搜索游戏攻略已经过时了！</a> (网易2013-5-23)</li>
         			<li><a href="http://www.36kr.com/p/200981.html?vt=0" target="_blank">让游戏长尾市场有更好沉淀：“游戏界豆瓣”着迷网推出游戏WIKI、游戏条目2.0，引入众包形式做内容管理</a> (36氪 2013-1-24)</li>
         			<li><a href="http://game.donews.com/news/201301/1726173.html" target="_blank">游戏业界大佬扎堆着迷网，目的何在？</a>(Donews 2013-1-23)</li>
         			<li><a href="http://games.sina.com.cn/y/n/2012-12-26/1747680287.shtml" target="_blank">着迷网CEO陈阳：打造注重分享与自由的游戏社区</a>(新浪2012-12-26)</li>
         			<li><a href="http://www.techweb.com.cn/360web/news.php?cid=1254290&tab=2" target="_blank">着迷网陈阳：我和李学凌做的事不一样</a>(Techweb 2012-11-12)</li>
         			<li><a href="http://game.donews.com/news/201210/1683615.html" target="_blank">前EA高管创办着迷网 一个关于游戏的兴趣社区</a>(Donews 2012-10-25)</li>
		</ul>
            <div style="padding-top:20px;text-align: center;"><a href="${URL_WWW}/about/press">上一页</a></div>
	</div>

    <!--about-right结束-->
</div>
<!--content结束-->

<div class="piccon"></div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>

