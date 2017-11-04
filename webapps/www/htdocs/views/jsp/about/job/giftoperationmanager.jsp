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

     <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;网站运营经理（礼包商城运营方向）</div>
		<div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
			<div class="u_fans u_fansz">
					<ul class="zp">
						<h3>网站运营经理（礼包商城运营方向）</h3>
						<p>• 工作地点：北京 </p>
						<p>• 工作经验：3年以上 </p>
						<p>• 工作性质：全职 </p>
						<p>• 最低学历：本科 </p>
						<p>• 招聘人数：1人</p>
						<p>• 管理经验：是</p>
						<li class="listf clearfix">
							<b>• 工作职责:</b>
							<dl>
								<dt>1.</dt>
								<dd>负责网站日常运营管理，运营状态监控、运营数据分析、网站用户行为分析，需求分析和竞争对手网站分析等。</dd>
							</dl>
							<dl>
								<dt>2.</dt>
								<dd>负责网站礼包商城的建设和运营，根据公司经营和战略需要及市场反应，策划相应礼包，积分等活动并执行</dd>
							</dl>
							<dl>
								<dt>3.</dt>
								<dd>对活动效果做好监控和评估，保证活动吸引力，改进活动策略并对活动结果负责。</dd>
							</dl>
							<dl>
								<dt>4.</dt>
								<dd>负责礼包商城的管理，定期对用户兑换产品进行统计，并安排发货事宜，监控产品库存</dd>
							</dl>
							<dl>
								<dt>5.</dt>
								<dd>收集相关行业信息、定期做用户调查并及时发现用户需求，及时了解业界发展趋势，为网站运营决策提供依据。</dd>
							</dl>
							<dl>
								<dt>6.</dt>
								<dd>完成上级交办的其他工作</dd>
							</dl>
						</li>
						<li class="listf clearfix">
							<br><b> • 任职要求：</b><br>
							<dl>
								<dt>1. </dt>
								<dd>本科及以上学历，3年以上网站运营工作经验，有游戏礼包商城运营经验或者电子商务网站积分商城运营经验者优先。</dd>
								<dt>2. </dt>
								<dd>对24小时运营有客观的认识。</dd>
								<dt>3. </dt>
								<dd>具备良好的策划能力、执行能力、数据挖掘能力，分析能力和解决问题能力；</dd>
								<dt>4. </dt>
								<dd>具备良好的团队协作、组织协调、沟通交流的能力</dd>
								<dt>5. </dt>
								<dd>对游戏有热情，喜爱游戏行业，富有创业激情。</dd>
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
