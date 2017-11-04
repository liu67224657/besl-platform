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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;副总经理
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>副总经理</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作经验：8年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>根据集团发展目标，制定和实施芜湖公司总体战略，组织实施公司战略目标；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>根据集团年度计划，制定和实施芜湖公司年度经营计划，并跟进实施过程，对结果负全面责任；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>建立良好的沟通渠道，定期向董事会汇报经营战略和计划执行情况并保持对政府，客户及合作伙伴的良好沟通，协调各部门关系；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>建立健全公司统一、高效的组织体系和工作体系，领导营造企业文化氛围，塑造和强化公司价值观；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>主持芜湖公司业务经营工作。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>40岁以下，全日制重点本科及以上学历，MBA优先；</dd>
                            <dt>2.</dt>
                            <dd>具有8年以上大型互联网、移动互联网，游戏行业工作经验，其中有从事过大中型网站运营、市场、产品、技术等相关综合管理工作；</dd>
                            <dt>3.</dt>
                            <dd>有实际移动互联网或者游戏策划运营成功案例，具有丰富的市场策划、营销推广工作经验； </dd>
                            <dt>4.</dt>
                            <dd>对互联网有深刻的理解，熟悉移动互联，并对其发展及趋势有较深入的认知，能够根据用户需求及市场变化迅速做出反应；</dd>
                            <dt>5.</dt>
                            <dd>正直、诚信、具有优秀的沟通协调能力及较强的抗压能力。</dd>
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
