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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;java开发工程师
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>java开发工程师</h3>

                    <p>• 工作地点：北京 </p>

                    <p>• 工作经验：5年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>理解需求人员编写的需求文档，并进行数据结构、组件和程序模块等的设计工作；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>依据详细设计，使用专业的工具、技术方法进行软件开发</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>提交代码前的CODE REVIEW。 </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>紧急BUG的对应和处理。 </dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>日常运营中JAVA环境的分析报告。 </dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>本科或以上学历，计算机相关专业。5年相关开发工作经验</dd>
                            <dt>2.</dt>
                            <dd>精通java core编程，架构，动手能力强，反应快。  2.0含义；</dd>
                            <dt>3.</dt>
                            <dd>精通或理解高性能开发技术，精通基于互联网和移动互联网开发的几项基本技能，如：负载均衡，分布式，大数据量保存，大并发等。 </dd>
                            <dt>4.</dt>
                            <dd>熟练使用Spring开源技术，熟练使用jstl/jsp等前端技术； </dd>
                            <dt>5.</dt>
                            <dd>熟悉一种以上数据库并熟练运用SQL，具备Mysql数据库开发经验者优先； </dd>
                            <dt>6.</dt>
                            <dd>熟悉一种Nosql数据库，清楚nosql数据库集群的搭建和优缺点，具备mongodb开发经验者有限；</dd>
                            <dt>7.</dt>
                            <dd>熟悉tomcat/resin/WebLogic等web服务器之一； </dd>
                            <dt>8.</dt>
                            <dd>熟悉JDK者优先。</dd>
                            <dt>9.</dt>
                            <dd>熟悉Ehcache、Memcache等，对缓存有一定了解者</dd>
                            <dt>10.</dt>
                            <dd>有框架设计，代码优化，性能优化，数据库优化能力者优先</dd>
                            <dt>11.</dt>
                            <dd>有测试经验者优先 </dd>
                            <dt>12.</dt>
                            <dd>熟悉ngnix/apache等开源http服务者优先。</dd>
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
