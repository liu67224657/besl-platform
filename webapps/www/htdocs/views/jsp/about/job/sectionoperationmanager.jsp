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

     	<div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;专区运营经理</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>专区运营经理</h3>
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
								<dd>专区维护<br />1、	带领运营编辑团队对重点游戏专区的资料收集、内容更新及维护； <br />2、	制作相关专题，包含细分类游戏分类专题及重要更新专题； <br />3、	定期针对所负责的专区制作专题或热门活动；<br />4、	根据统计数据、竞品动态、用户反馈等信息提炼优化产品的可行性意见，优化专区，并推动落实；</dd>
							</dl>
							<dl>
								<dt>2.</dt>
								<dd>部门管理<br />1、	负责本部门员工及日常管理工作、制定年度计划、运营策略，并负责推动实施与执行； <br />2、	制定部门运营任务，确定运营重点，并量化，细化工作；<br />3、	检查部门人员工作结果，找出不足，及时纠正；</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> • 任职要求：</b><br>
							<p>必要条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>本科以上学历，三年以上游戏专区编辑运营经验，要求至少一个游戏专区运营的成功案例；</dd>
								<dt>2.	</dt>
								<dd>具备良好的文字组织能力和写作能力，具有丰富的游戏专题制作、评测/攻略/活动软文编写的经验；</dd>
								<dt>3.	</dt>
								<dd>有手机游戏行业的经验和案例则优先考虑；</dd>
								<dt>4.	</dt>
								<dd>认同公司企业文化，忠诚度高；</dd>
								<dt>5.	</dt>
								<dd>对游戏充满热情，喜爱游戏行业，经常访问主流移动游戏网站，有游戏媒体有充分的了解；</dd>
								<dt>6.	</dt>
								<dd>积极、诚信、抗压能力强，能够适应高强度的工作状态；</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br /><p>优先条件：</p>
							<dl>
								<dt>1. </dt>
								<dd>具有丰富的手游编辑团队管理经验</dd>
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
