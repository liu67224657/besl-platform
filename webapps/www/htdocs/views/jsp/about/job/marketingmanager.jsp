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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;市场经理
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>市场经理</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作经验：4年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：是</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责公司产品做市场规划和市场目标的制定。</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>制定并实施营销计划方案，对方案进行全方位的跟踪，指导相关效果分析和数据统计；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>带领公司的整个产品市场运作，包括公司现存的市场推广，广告投放，活动策划组织与总结工作；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>需定时对市场部员工进行专业知识的培训；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>追踪行业市场动态，提出相应竞争策略，为高层决策提供依据。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>本科学历，有4年以上互联网行业或游戏行业市场，产品推广经验；</dd>
                            <dt>2.</dt>
                            <dd>对市场策略分析、市场策划、推广和促销活动的专业操作方式熟知，有良好的BD能力</dd>
                            <dt>3.</dt>
                            <dd>熟悉互联网特点及线下运作规则，对提升业务影响力、塑造品牌建设有丰富经验； </dd>
                            <dt>4.</dt>
                            <dd>有开拓创新意识，对互联网新兴业务有浓厚兴趣并愿为之付出努力，有敏锐的市场意识；</dd>
                            <dt>5.</dt>
                            <dd>执行管理能力强，协调沟通能力强；有绝对工作热情和创业精神，有自己独特见解和市场分析及创意。</dd>
                            <dt>6.</dt>
                            <dd>有良好的团队合作及独立工作能力，目标导向，能主动和快速学习。</dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <br><br>

            <div class="sendresume">
                <span><img width="16" height="16" src="http://lib.joyme.com/static/theme/default/img/zlzp.jpg"><a target="_blank" href="http://special.zhaopin.com/sh/2014/11027/qhzmwl071856/">去智联招聘投简历</a></span>
                <span><a target="_blank" href="http://ehr.goodjobs.cn/show.php?corpID=31654">去新安人才网简历</a></span>
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
