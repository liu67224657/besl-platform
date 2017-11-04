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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;人事主管
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>人事主管</h3>

                    <p>• 工作地点：北京 </p>

                    <p>• 工作经验：2年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：是</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>全面负责北京公司薪资的核算与发放；负责员工的个税上报；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>根据国家有关法规和政策，审定工伤、医疗、养老、失业、生育和公积金等项目和支出水平，为公司员工办理社会保险的缴纳和管理工作；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>员工福利的策划及方案设计；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>协助上级经理制定员工的工资、奖金、福利等人力资源费用预算和报酬分配方案；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>负责填制和分析各类人事统计报表，协助上级经理处理其他人事相关工作；</dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>人力资源信息化系统的及时维护，人事档案资料管理；</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>本科以上学历；二年以上薪酬福利相关岗位工作经验，100人上公司薪酬福利实务操作经验；</dd>
                            <dt>2.</dt>
                            <dd>具备良好的沟通协调能力，工作认真细致，有条理性、逻辑性，良好的职业素养和职业操守，能够迅速掌握与公司业务有关的各种知识； </dd>
                            <dt>3.</dt>
                            <dd>有较强的职业性和执行能力，情商高，能熟练的使用各种办公软件。 </dd>
                            <dt>4.</dt>
                            <dd>有严密的逻辑思维能力和全面的分析判断能力，较强的统筹协调能力和口头表达能力</dd>
                            <dt>5.</dt>
                            <dd>有耐心，抗压能力强。</dd>
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
