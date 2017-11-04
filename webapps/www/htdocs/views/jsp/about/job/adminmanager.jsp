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
		<div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;行政经理</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>行政经理</h3>
						<p>• 工作地点：北京 </p>
						<p>• 工作经验：3年或以上 </p>
						<p>• 工作性质：全职 </p>
						<p>• 最低学历：本科 </p>
						<p>• 招聘人数：1人</p>
						<p>• 管理经验：否</p>
						<li class="listf clearfix">
							<b>• 工作职责:</b>
							<dl>
								<dt>1.</dt>
								<dd>负责公司各项行政管理制度的制订、完善及监督执行</dd>
							</dl>
							<dl>
								<dt>2.</dt>
								<dd>负责拟定公司行政工作规划及落实 </dd>
							</dl>
							<dl>
								<dt>3.</dt>
								<dd>负责固定资产及行政后勤管理，创造和保持良好的工作环境</dd>
							</dl>
							<dl>
								<dt>4.</dt>
								<dd>负责大客户及重要来宾、领导的接待工作</dd>
							</dl>
							<dl>
								<dt>5.</dt>
								<dd>对业务部门的涉外事务提供有效支持</dd>
							</dl>
							<dl>
								<dt>6.</dt>
								<dd>完成总裁交办的其他工作的督办、协调及落实任务</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> • 任职要求：</b><br>
							<p>必要条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>本科以上学历；三年以上行政主管或以上相关岗位工作经验，有良好的英文能力。</dd>
								<dt>2.	</dt>
								<dd>熟悉现代企业管理模式，具备丰富的行政管理工作经验和组织协调能力/攻略/活动软文编写的经验；</dd>
								<dt>3.	</dt>
								<dd>有办公室装修，办公室选址，租赁相关工作经验</dd>
								<dt>4.	</dt>
								<dd>能熟练的使用各种办公软件。</dd>
								<dt>5.	</dt>
								<dd>有严密的逻辑思维能力和全面的分析判断能力，较强的统筹协调能力和口头表达能力</dd>
								<dt>6.	</dt>
								<dd>具有较强的工作热情和责任感，稳重、踏实、勤勉、敬业，做事严谨，能够适应较强工作压力。</dd>
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
