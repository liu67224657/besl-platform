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

    	<div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;商务合作主管</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>商务合作主管</h3>
						<p>• 工作地点：北京 </p>
						<p>• 工作经验：2年以上 </p>
						<p>• 工作性质：全职 </p>
						<p>• 最低学历：大专 </p>
						<p>• 招聘人数：1人</p>
						<p>• 管理经验：否</p>
						<li class="listf clearfix">
							<b>• 工作职责:</b>
							<dl>
								<dt>1.</dt>
								<dd>通过与各媒体、渠道合作，提升网站流量</dd>
							</dl>
							<dl>
								<dt>2.</dt>
								<dd>负责广告的投放及后期的数据分析、监测</dd>
							</dl>
							<dl>
								<dt>3.</dt>
								<dd>跟据业务规划负责拓展和维护合作伙伴关系；</dd>
							</dl>
							<dl>
								<dt>4.</dt>
								<dd>定商务拓展业务推广合作流程及规范；</dd>
							</dl>
							<dl>
								<dt>5.</dt>
								<dd>挖掘各媒体、渠道及运营的需求，深入挖掘并整合各方面优势资源；</dd>
							</dl>
							<dl>
								<dt>6.</dt>
								<dd>负责所有合作上的跟进工作（包括合同拟定、合作接入、对接跟进、后期维护）</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> • 任职要求：</b><br>
							<p>必要条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>专科或以上学历，熟悉和了解游戏圈喜欢从事与游戏相关的工作；</dd>
								<dt>2.	</dt>
								<dd>2年以上业务拓展、商务合作经验；</dd>
								<dt>3.	</dt>
								<dd>充满激情、勤奋刻苦、独立开展工作能力强、悟性高、善于总结、能承受压力；</dd>
								<dt>4.	</dt>
								<dd>熟悉手游市场，有手游媒体相关资源和从业经验</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<p>优先条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>游戏媒体工作经验</dd>
								<dt>2. </dt>
								<dd>熟悉手游市场及有厂商资源</dd>
								<dt>3. </dt>
								<dd>商务谈判的成功案例</dd>
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
