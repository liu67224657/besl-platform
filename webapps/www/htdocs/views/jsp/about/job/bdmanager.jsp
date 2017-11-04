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

    	<div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;商务助理</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>商务助理</h3>
						<p>• 工作地点：北京 </p>
						<p>• 工作经验：无 </p>
						<p>• 工作性质：全职 </p>
						<p>• 最低学历：本科 </p>
						<p>• 招聘人数：1人</p>
						<p>• 管理经验：否</p>
						<li class="listf clearfix">
							<b>• 工作职责:</b>
							<dl>
								<dt>1.</dt>
								<dd>负责整理客户资料；</dd>
							</dl>
							<dl>
								<dt>2.</dt>
								<dd>协助部门总监完善部门规章制度和操作流程与规范，做好销售的后台支持；</dd>
							</dl>
							<dl>
								<dt>3.</dt>
								<dd>处理部门日常部分事务性工作。</dd>
							</dl>
							<dl>
								<dt>4.</dt>
								<dd>完成领导交办的其他工作。</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> • 任职要求：</b><br>
							<p>必要条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>本科以上学历，广告学、市场营销，电子商务管理或相关专业；</dd>
								<dt>2.	</dt>
								<dd>了解商务相关领域，熟悉互联网、游戏行业的市场特点；</dd>
								<dt>3.	</dt>
								<dd>熟练使用计算机办公软件，良好的文字撰写能力</dd>
								<dt>4.	</dt>
								<dd>良好的语言表达及较强的沟通能力，工作认真细致，积极进取，善于学习与创新</dd>
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
