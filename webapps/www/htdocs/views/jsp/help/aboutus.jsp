<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>关于着迷 ${jmh_title}</title>
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

<!-- 关于着迷 -->
<div class="about-bg clearfix">
	<!-- 左侧导航 -->
		<div class="about-nav">
			<ul>
				<li class="current"><a href="${URL_WWW}/help/aboutus">关于着迷</a><span></span></li>
                <li><a href="${URL_WWW}/help/milestone">着迷里程碑</a><span></span></li>
				<li><a href="${URL_WWW}/about/contactus">商务合作</a><span></span></li>
				<li><a href="${URL_WWW}/about/job/zhaopin">工作在着迷</a><span></span></li>
				<li><a href="${URL_WWW}/help/law">法律声明</a><span></span></li>
				<li><a href="${URL_WWW}/help/service">服务条款</a><span></span></li>
				<li><a href="${URL_WWW}/about/press">媒体报道</a><span></span></li>
                <li><a href="${URL_WWW}/help/law/parentsprotect">家长监护</a><span></span></li>
			</ul>
		</div>

	<!-- 关于我们 -->
	<div class="about-us">
		<div class="about-us-item-1">
			<p>人们喜欢游戏，但讨厌沉迷游戏的人。着迷是一个介于冷静与沉迷之间的词汇，身为一批前游戏开发者，我们深知着迷的力量。</p>
			<p>每个孤独的心灵，因为游戏，不再被冷酷的世界夺走乐趣。卖东西的零售业者、穿梭在队列与交通工具的白领们、城墙下安静的老人……在旁观者看来残酷至极的孤独景观下，游戏在陪伴着他们。</p>
			<p>我们认识的一位国外朋友告诉我们，海啸后，大量灾民是用游戏度过了十几个不眠之夜。游戏拯救了他们，本可能被孤独和恐惧侵犯的，心灵的乐土。</p>
			<p>这位朋友告诉我们，她永远都会感谢在第一个夜晚，在她裹在捐来的睡袋里默默哭泣时，递给她一个掌机，并且手把手教她玩游戏的小男孩。</p>
			<p>那个着迷于游戏，并最终拯救了别人的孩子，也许就是我们最想替每个玩家找到的人。</p>
		</div>
		<div class="about-us-item-2"></div>
		<div class="about-us-item-3">            
            <p>着迷是中国第一家专注于手游领域的创新型服务平台，为手游用户及厂商提供个性化、价值化、互动性的一站式服务。</p>
<p>着迷由美国艺电（简称EA）前中国Lead Producer陈阳于2011年4月创立。凭借国际化运作的管理团队和强大的技术研发力量，2014年2月，着迷完成第二轮融资，由复星集团旗下昆仲资本领投，蓝驰创投追投，融资金额达1.3亿元人民币。</p>
<p>秉承“游戏即服务”的理念，着迷为用户提供丰富的手游资讯、手游选择、手游攻略、手游活动、手游兴趣社交、手游泛娱乐等游戏生活与文化相关的服务，坚持原创风格，以全面、专业、高互动性的内容，多角度、多层次地服务最广泛的手游用户。</p>
<p>作为中国最具用户价值的手游营销服务平台，着迷为手游厂商提供精准的用户分析、定制化的新游传播、推广及发行服务。</p>
<p>着迷以全球化的视野及深刻的用户洞察，将持续性地创新服务方式，拓展服务内容，专注移动互联网领域各种应用，致力成为中国最具影响力的手游综合服务平台。</p>

            
		</div>
	</div>

</div>
<!--content结束-->

<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>
