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
		<div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;php开发工程师</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>php开发工程师</h3>
						<p>·工作地点：北京 </p>
						<p>·工作经验：2年以上 </p>
						<p>·最低学历：本科 </p>
						<p>·工作性质：全职 </p>
						<p>·招聘人数：1人</p>
						<p>·管理经验：否</p>
						<li class="listf clearfix">
							<b>· 职位描述：</b>
							<dl>
								<dt>1.</dt>
								<dd>与产品、UI、UE进行良好沟通，能快速理解各方需求，并进行相应的模块设计与开发；</dd>
								<dt>2.</dt>
								<dd>在系统设计、功能设计方面有自己的见解，并在适当的时候表达自己的意见；</dd>
								<dt>3.</dt>
								<dd>能够进行数据库设计、接口设计等研发文档的编写，负责对所属模块进行单元测试；</dd>
								<dt>4.</dt>
								<dd>工作中有强烈的责任心，在线上和测试环境中遇到问题时能主动积极地快速定位和解决问题；</dd>
								<dt>5.</dt>
								<dd>完成直接上级领导安排的其它工作。</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> · 任职要求：</b><br>
							<p>必要条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>计算机或相关专业本科及以上学历，2年以上PHP开发经验；</dd>
								<dt>2.	</dt>
								<dd>熟练掌握Linux+Nginx+MySQL+PHP环境的Web应用开发，熟练掌握MySQL数据库设计开发；</dd>
								<dt>3.	</dt>
								<dd>熟练掌握HTML、CSS、Javascript、ajax，有一定的跨浏览器开发经验，具备优化Web应用程序性能经验；</dd>
								<dt>4.</dt>
								<dd>熟悉Unix/Linux操作环境，熟悉常用的Linux命令；</dd>
								<dt>5.	</dt>
								<dd>具备良好的代码编程习惯、较强的文档编写能力、学习能力和沟通能力；</dd>
								<dt>6.	</dt>
								<dd>熟悉Zend Studio， 能用Zend Studio， Xdebug者优先。</dd>
								<dt>7.	</dt>
								<dd>熟悉分布式，负载均衡等技术者优先。</dd>
								<dt>8.	</dt>
								<dd>有创业激情，认真踏实，热爱移动互联网，有积极的工作态度和良好的团队合作精神；</dd>
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
