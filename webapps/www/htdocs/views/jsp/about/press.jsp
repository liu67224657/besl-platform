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
            <li><a href="http://games.qq.com/a/20150807/042003.htm" target="_blank">着迷CEO陈阳变身万磁王 亮相开放生态大会</a>(腾讯 2015-08-07)</li>
            <li><a href="http://game.people.com.cn/n/2015/0807/c213917-27428930.html" target="_blank">古永锵100亿做合一大生态，着迷陈阳C轮造强磁场</a>(人民网 2015-08-07)</li>
            <li><a href="http://36kr.com/p/5036119.html" target="_blank">【36氪独家】投资 AcFun、着迷、罗辑思维后，“合一集团” 对优酷土豆来说还真是好名字</a>(36氪 2015-08-06)</li>
            <li><a href="http://finance.ifeng.com/a/20150803/13885902_0.shtml?from=groupmessage&isappinstalled=0" target="_blank">着迷获优酷土豆集团C轮战略投资</a>(凤凰财经 2015-08-03)</li>
            <li><a href="http://36kr.com/p/5035990.html#rd" target="_blank">[36氪独家] 着迷C 轮引进优酷土豆战略投资，计划年底挂牌新三板</a>(36氪 2015-08-03)</li>
            <li><a href="http://www.joyme.com/news/official/201508/0285862.html" target="_blank">触摸IP共鸣的奇点，着迷CJ好容颜试水组合营销</a>(着迷网 2015-08-02)</li>
            <li><a href="http://finance.china.com/fin/sxy/201507/31/1503044.html" target="_blank">A站被查水表，着迷CJ好容颜女神被带走，这都什么情况？</a>(中华网 2015-07-31)</li>
            <li><a href="http://www.joyme.com/news/official/201507/3085473.html" target="_blank">打造Xbox中国玩家社区——着迷CEO陈阳谈主机未来</a>(着迷网 2015-07-30)</li>
            <li><a href="http://e.gmw.cn/2015-07/29/content_16463640.htm" target="_blank">魔导士为CJ好容颜冠军拍摄Coser大片，首度曝光!</a>(光明网 2015-07-29)</li>
            <li><a href="http://news.163.com/15/0728/13/AVK50C1K00014AEE.html" target="_blank">着迷CJ好容颜票选公布，携手育碧“舞力全开”引爆Chinajoy</a>(网易新闻 2015-07-28)</li>
            <li><a href="http://economy.gmw.cn/2015-07/17/content_16330221.htm" target="_blank">CJ好容颜活动持续升温，着迷老马独家爆料网红养成秘籍</a>(光明网 2015-07-17)</li>
            <li><a href="http://news.163.com/15/0716/17/AULNF14D00014AED.html" target="_blank">别盯着优衣库看了，着迷CJ好容颜有更刺激的！</a>(网易新闻 2015-07-16)</li>
            <li><a href="http://games.qq.com/a/20150709/037142.htm#p=1" target="_blank">魔导士吐豪言:势必捧红着迷“CJ好容颜”Coser</a>(腾讯游戏 2015-07-09)</li>
            <li><a href="http://news.163.com/15/0709/21/AU42RIGJ00014AED.html" target="_blank">如今网红流行这么玩，着迷CJ好容颜全是奇葩猛料！</a>(网易新闻 2015-07-09)</li>
            <li><a href="http://game.people.com.cn/n/2015/0701/c40130-27238141.html" target="_blank">着迷“CJ好容颜”大赛开启！不爆美照曝怪料！</a>(人民网 2015-07-01)</li>
            <li><a href="http://tech.huanqiu.com/news/2015-07/6823191.html" target="_blank">育碧+优酷助力着迷开启“CJ好容颜”大赛</a>(环球网 2015-07-01)</li>
            <li><a href="http://news.163.com/15/0522/18/AQ85F2MG00014AED.html" target="_blank">从着迷玩霸的华丽变身 看手游APP升级之路</a>(网易新闻 2015-05-22)</li>
            <li><a href="http://www.niaogebiji.com/article-7012-1.html" target="_blank">App Store新贵着迷玩霸，论黑马的正确姿势</a>(鸟哥笔记 2015-05-18)</li>
            <li><a href="http://www.199it.com/archives/342086.html" target="_blank">着迷联手优酷土豆 构建跨平台全游戏生态</a>(着迷网 2015-04-22)</li>
            <li><a href="http://www.joyme.com/news/blue/201504/2277609.html" target="_blank">着迷与优酷土豆达成独家战略合作</a>(着迷网 2015-04-22)</li>
            <li><a href="http://game.21cn.com/online/c/a/2015/0410/17/29372770.shtml" target="_blank">着迷网首页改版 UGC兴趣社群平台价值升级</a>(21CN 2015-04-10)</li>
            <li><a href="http://games.qq.com/a/20150303/053465.htm" target="_blank">着迷玩霸正式上线 伴随式服务玩家通关全程</a>(腾讯游戏 2015-03-03)</li>
            <li><a href="http://www.joyme.com/news/blue/201502/0669914.html" target="_blank">芜湖最具硅谷风格的公司长啥样? 一起探访着迷</a>(着迷网 2015-02-06)</li>
            <li><a href="http://games.qq.com/a/20150109/022384.htm" target="_blank">着迷蝉联金翎奖“最佳游戏网络媒体”大奖</a>(腾讯游戏 2015-01-09)</li>
            <li><a href="http://newshtml.iheima.com/2014/1229/148635.html" target="_blank">着迷WIKI：两年聚集百万用户的秘籍</a>(i黑马 2014-12-29)</li>
            <li><a href="http://tech.sina.com.cn/i/2014-12-23/doc-icczmvun4216970.shtml" target="_blank">着迷WIKI：靠兴趣社区降低获取用户成本</a>(新浪科技 2014-12-23)</li>
            <li><a href="http://epaper.nandu.com/epaper/d/html/2014-11/24/content_3348965.htm?div=-1" target="_blank">着迷：手游在线百科谋变联营</a>(南方都市报 2014-11-24)</li>
            <li><a href="http://www.cyzone.cn/a/20141110/265513.html#comments" target="_blank">兴趣社群：一个游戏玩家平台的另类突破</a>(创业邦 2014-11-10)</li>
            <li><a href="http://www.36kr.com/p/216462.html" target="_blank">思考Wiki：它是如何用兴趣聚集用户的</a>(36氪 2014-11-05)</li>
            <li><a href="http://gamasutra.com/view/news/228252/Creating_a_Wikia_alternative_for_video_games_in_China.php" target="_blank">Creating a Wikia alternative for video games in China</a>(Gamesutra 外媒 2014-10-21)</li>
            <li><a href="http://www.takefoto.cn/viewnews-193233.html" target="_blank">比Bigger更Bigger 着迷玩的是“兴趣”</a>(北京晚报 2014-10-14)</li>
            <li><a href="http://www.bbtnews.com.cn/news/2014-09/22000000741247.shtml" target="_blank">着迷借在线教育招揽IT英才</a>(北京商报 2014-09-22)</li>
            <li><a href="http://games.qq.com/a/20140919/023715.htm" target="_blank">着迷“iOS开发工程师培优计划”正式启动</a>(腾讯 2014-09-19)</li>
            <li><a href="http://apis.36kr.com/p/215411.html" target="_blank">“着迷网”陈阳：做公司，在合适的时间就该做合适的事</a>(36氪 2014-09-16)</li>
            <li><a href="http://www.bianews.com/news/44/n-438344.html" target="_blank">曾获1.3亿投资的着迷怎样了，专访CEO陈阳</a>(鞭牛士 2014-09-04)</li>
            <li><a href="http://tech.163.com/14/0804/19/A2R2A8F3000915BF.html" target="_blank">着迷陈阳：着迷是手游的一站式服务平台</a>(网易 2014-08-04)</li>
            <li><a href="http://games.ifeng.com/special/2014chinajoy/hangye/detail_2014_08/01/37691414_0.shtml" target="_blank">着迷陈阳：以技术手段提升服务效率</a>(凤凰 2014-08-01)</li>
            <li><a href="http://game.china.com/industry/news/11011446/20140708/18617519.html" target="_blank">着迷成为2014 CJ首要赞助商”打造游戏大轰趴</a>(中华网 2014-07-08)</li>
            <li><a href="http://www.eeo.com.cn/2014/0611/261779.shtml" target="_blank">手游门户落地芜湖 移动互联网“下沉”三线城市</a>(经济观察网 2014-6-11)</li>
            <li><a href="http://games.sina.com.cn/y/n/2014-06-10/1811790530.shtml" target="_blank">着迷：在正确的时间，做正确的事</a>(新浪 2014-6-10)</li>
            <li><a href="http://games.sina.com.cn/y/n/2014-06-10/1804790526.shtml" target="_blank">着迷正式落户芜湖 完成阶段性战略布局</a>(新浪 2014-6-10)</li>
            <li><a href="http://epaper.21cbh.com/html/2014-04/23/content_96841.htm?div=-1" target="_blank">手游产业链外延：“平台梦”背后的商机</a> (21世纪经济报道 2014-4-23)</li>
            <li><a href="http://tech.sina.com.cn/i/2014-03-27/00449274406.shtml" target="_blank">陈阳解读着迷网1.3亿元融资 流量失衡中有机会</a> (新浪 2014-3-27)</li>
            <li><a href="http://finance.chinanews.com/it/2014/03-24/5987257.shtml	" target="_blank">着迷网推一站式服务平台模式 重建手游行业生态圈</a> (中国新闻网 2014-3-24)</li>
            <li><a href="http://games.qq.com/a/20140324/018829.htm" target="_blank">着迷网手游服务平台模式 打破做不大魔咒</a> (腾讯 2014-3-24)</li>
            <li><a href="http://games.qq.com/a/20140110/008599.htm" target="_blank">着迷网荣获2013金翎奖最佳游戏网络媒体</a> (腾讯 2014-1-10)</li>
            <li><a href="http://game.163.com/13/0912/15/98J4ABE800314J6K.html" target="_blank">着迷网助力360首推《植物大战僵尸2》安卓中文版</a>(网易 2013-9-12)</li>
            <li><a href="http://games.sina.com.cn/m/n/2013-08-29/1328729839.shtml" target="_blank">着迷网业内首爆游戏端内嵌攻略引领手游服务新趋势</a>(新浪 2013-8-29)</li>
		</ul>
            <div style="padding-top:20px;text-align: center;"><a href="${URL_WWW}/about/press_2">下一页</a></div>
	</div>

    <!--about-right结束-->
</div>
<!--content结束-->

<div class="piccon"></div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>
</body>
</html>
