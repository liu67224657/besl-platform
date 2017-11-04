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
		<div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;专区编辑</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>专区编辑</h3>
						<p>·工作地点：北京 </p>
						<p>·工作经验：无</p>
						<p>·最低学历：大专 </p>
						<p>·工作性质：全职 </p>
						<p>·招聘人数：4人</p>
						<p>·管理经验：否</p>
						<li class="listf clearfix">
							<b>· 职位描述：</b>
							<dl>
								<dt>1.</dt>
								<dd>针对特定手机网游，使用wiki技术制作游戏的资料专区；</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> · 任职要求：</b><br>
							<p>必要条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>大专以上学历</dd>
								<dt>2.	</dt>
								<dd>热爱游戏且了解游戏历史和文化，熟悉各类游戏，尤其是手机网游或PC网游</dd>
								<dt>3.	</dt>
								<dd>擅长使用excel，善于整理游戏数据或攻略</dd>
								<dt>4.</dt>
								<dd>了解游戏玩家的需求，擅长和游戏玩家交流</dd>
								<dt>5.	</dt>
								<dd>勤恳，做事有耐心</dd>
							</dl>
							<p><br />优先条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>做过游戏wiki或者了解html语言</dd>
								<dt>2.	</dt>
								<dd>有任何一种语言编程基础</dd>
								<dt>3.	</dt>
								<dd>游戏专区编辑经验</dd>
								<dt>4.</dt>
								<dd>游戏策划经验</dd>
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
