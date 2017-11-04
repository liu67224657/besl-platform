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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;招聘主管（偏技术岗位招聘）
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>招聘主管（偏技术岗位招聘）</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作经验：2年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>制订并执行招聘计划，进行工作分析，完成职位说明书体系建设，建立招聘流程体系并持续优化改进；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>负责技术部门招聘需求信息的采集、分析、整理，并制定和组织实施招聘计划；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>挖掘、建立和维护多招聘渠道；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>发布职位信息，进行简历甄别、人才测评、面试、录用等工作，寻找合适人员满足业务发展的需要；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>建立并完善简历库和储备人才库，按期提交招聘分析报告； </dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>完成上级交办的其它工作。 </dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>本科及以上学历，人力资源管理相关专业优先；</dd>
                            <dt>2.</dt>
                            <dd>2年左右互联网行业招聘相关工作经验，熟悉IT人才的招聘渠道或具有资源；</dd>
                            <dt>3.</dt>
                            <dd>了解技术人员如移动端开发，网页设计等岗位的招聘要求，至少有1年相同岗位的招聘经验；</dd>
                            <dt>4.</dt>
                            <dd>熟悉企业的招聘流程，熟练掌握各种招聘技巧，能独立开展招聘工作；</dd>
                            <dt>5.</dt>
                            <dd>具备较强推动力、执行力，表达与沟通协作的能力、分析与解决问题的能力。</dd>
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
