<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>工作在着迷 ${jmh_title}</title>
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
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="about-bg clearfix">
	<!-- 左侧导航 -->
		<div class="about-nav">
			<ul>
				<li><a href="${URL_WWW}/help/aboutus">关于着迷</a><span></span></li>
				<li><a href="${URL_WWW}/about/contactus">商务合作</a><span></span></li>
				<li class="current"><a href="${URL_WWW}/about/job/zhaopin">工作在着迷</a><span></span></li>
				<li><a href="${URL_WWW}/help/law">法律声明</a><span></span></li>
				<li><a href="${URL_WWW}/help/service">服务条款</a><span></span></li>

				<li><a href="${URL_WWW}/about/press">媒体报道</a><span></span></li>
                <li><a href="${URL_WWW}/help/law/parentsprotect">家长监护</a><span></span></li>
			</ul>
		</div>

	<!-- 职位详情 -->
    <div class="job-detail">
		<h2 class="about-title"><span class="about-title-text-5">工作在着迷</span></h2>
		<div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;社区运营编辑</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>社区运营编辑</h3>
						<p>·工作地点：北京 </p>
						<p>·工作经验：1年以上 </p>
						<p>·最低学历：大专 </p>
						<p>·工作性质：全职 </p>
						<p>·招聘人数：1人</p>
						<p>·管理经验：否</p>
						<li class="listf clearfix">
							<b>· 职位描述：</b>
							<dl>
								<dt>1.</dt>
								<dd>策划并组织着迷网社区活动，活跃用户气氛。</dd>
								<dt>2.</dt>
								<dd>组建并管理线上虚拟用户团队，为着迷网内容产生提供支持。</dd>
								<dt>3.</dt>
								<dd>管理社区板块，完善社区板块内容，推广社区相关活动和内容。</dd>
								<dt>4.</dt>
								<dd>发布板块内容贴，填充板块内容。</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> · 任职要求：</b><br>
							<p>必要条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>了解游戏；至少有一个平台的游戏精通，包括TVGAME、网络游戏、电脑单机游戏、手机游戏、掌机游戏；对游戏有足够的自我认知和评判；能够了解游戏领域中比较知名的游戏和游戏厂商；至少有3年以上的游龄。</dd>
								<dt>2.	</dt>
								<dd>了解社区论坛，有过一年以上社区论坛管理层面（版主以上）工作经验者优先；对社区论坛有足够的了解，懂得如何带动社区人气和组织管理虚拟团队。</dd>
								<dt>3.	</dt>
								<dd>有一定的文字功底，能够编写活动策划和虚拟小组制度。</dd>
								<dt>4.	</dt>
								<dd>有较强的执行力，能够按时按量的组织相关社区活动，并总结活动内容和结果，做出汇报。</dd>
								<dt>5.</dt>
								<dd>有较长时间的互联网经验，熟悉QQ、微博、论坛等常用的社交平台，熟悉哔哩哔哩、acfun等视频站点，并能够从中获取相关潮流信息。</dd>
								<dt>6.	</dt>
								<dd>性格开朗，外向，脾气好，能够活跃气氛，不极端主义，无游戏领域偏见，不冲动，能够妥善处理各方面人际关系。</dd>
							</dl>
						</li>
				</ul>
			</div>
			<br><br>
            <div class="sendresume">
         		<span><img width="16" height="16" src="http://lib.joyme.com/static/theme/default/img/zlzp.jpg"><a href="http://special.zhaopin.com/sh/2014/zlzp042150/" target="_blank">去智联招聘投简历</a></span>
         		<span><img width="11" height="16" src="http://lib.joyme.com/static/theme/default/img/51job.jpg"><a href="http://search.51job.com/list/co,c,2581201,000000,10,1.html" target="_blank">去51job投简历</a></span>
         	</div>
        </div>
	</div>

</div>
<!--content结束-->
<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
