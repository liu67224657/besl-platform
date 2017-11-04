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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;Linux运维工程师
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>Linux运维工程师</h3>

                    <p>• 工作地点：北京 </p>

                    <p>• 工作经验：3年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责公司整体网络环境的运维工作，保障公司网络安全、高效运行</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>负责公司数据中心机房的运维工作</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>负责总公司与各地分公司之间网络的互联互通</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>管理维护公司所有的网络设备，如Cisco或H3C的路由器、交换机、防火墙等，负责设备的配置及故障排除 </dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>管理维护数据中心机房的服务器 </dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>监控数据中心机房的运行状态，如温湿度、电力系统、空调系统等</dd>
                        </dl>
                        <dl>
                            <dt>7.</dt>
                            <dd>监控、检查相关的日志记录，定期提供运维报告</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>计算机、网络或相关专业大学本科及以上学历，3年以上网络运维相关工作经验</dd>
                            <dt>2.</dt>
                            <dd>熟悉TCP/IP路由与交换技术，熟悉网络架构，熟悉当前主流的网络产品，如Cisco、H3C、Juniper等，能够熟练的对其进行配置、排错</dd>
                            <dt>3.</dt>
                            <dd>精通linux，精通shell脚本的编写，精通配置和搭建各种服务器（防火墙、应用服务器，数据库），熟悉LVS等负载均衡技术的配置和部署 </dd>
                            <dt>4.</dt>
                            <dd>熟悉RIP、EIGRP、OSPF、IS-IS、PIM等路由协议，掌握其原理</dd>
                            <dt>5.</dt>
                            <dd>熟悉主流的网络安全产品与安全技术，能够独立对其进行配置、排错 </dd>
                            <dt>6.</dt>
                            <dd>有良好的团队精神和沟通能力，强烈的责任心，较强的学习和创新能力</dd>
                            <dt>7.</dt>
                            <dd>精通nginx集群搭建。</dd>
                            <dt>8.</dt>
                            <dd>熟悉sql的安装、维护。</dd>
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
