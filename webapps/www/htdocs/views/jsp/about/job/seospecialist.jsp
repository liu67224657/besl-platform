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

       <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;SEO专员</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>SEO专员</h3>
						<p>• 工作地点：北京 </p>
						<p>• 工作经验：1年以上 </p>
						<p>• 工作性质：全职 </p>
						<p>• 最低学历：大专 </p>
						<p>• 招聘人数：1人</p>
						<p>• 管理经验：否</p>
						<li class="listf clearfix">
							<b>• 工作职责:</b>
							<dl>
								<dt>1.</dt>
								<dd>提高百度关键词排名、pr值、alexa排名</dd>
							</dl>
							<dl>
								<dt>2.</dt>
								<dd>负责网站的SEO效果监测；</dd>
							</dl>
							<dl>
								<dt>3.</dt>
								<dd>能够撰写利于seo优化的文章</dd>
							</dl>
							<dl>
								<dt>4.</dt>
								<dd>分析行业特点，同行网站的优化方法，制定中心网站的优化策略，跟进优化推广效果，并出具分析报告</dd>
							</dl>
							<dl>
								<dt>5.</dt>
								<dd>网站站内优化能力，掌握网站内容优化、关键词优化、内部链接优化、代码优化、图片优化技巧</dd>
							</dl>
							<dl>
								<dt>6.</dt>
								<dd>完成领导交办的其他任务</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> • 任职要求：</b><br>
							<dl>
								<dt>1. </dt>
								<dd>大专以上学历，专业不限，有从事友情链及外链发布，网站推广经验者优先考虑；</dd>
								<dt>2. </dt>
								<dd>熟练使用站长工具，评定网站权重，能够独立分析对方网站质量，权重。</dd>
								<dt>3. </dt>
								<dd>懂得SEO基本原理,了解推广基本术语如锚文本,,软文,伪原创</dd>
								<dt>4. </dt>
								<dd>了解各大推广平台的推广方法</dd>
								<dt>5. </dt>
								<dd>了解友情链接的互换要求及质量的把控</dd>
								<dt>6. </dt>
								<dd>具备不断挖掘和发现新的外链资源的能力，并对新的外链源进行筛选，选择优质的外链源。</dd>
								<dt>7. </dt>
								<dd>能够承受重复性工作的鼓噪与绩效考核的压力，并能够积极完成，达到绩效考核指标</dd>
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
