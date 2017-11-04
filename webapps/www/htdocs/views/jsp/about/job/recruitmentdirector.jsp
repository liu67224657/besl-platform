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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;招聘总监</div>
        <div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>招聘总监</h3>

                    <p>• 工作地点：北京 </p>

                    <p>• 工作经验：5年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：是</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>制订招聘计划、策划招聘活动，独立组织完成招聘、面试活动，并灵活处理招聘过程中的突发事件，确保招聘效果；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>可以独立设计面试方案，熟练地掌握基本面试考核的方法和技巧；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>根据公司政策和用人标准独立进行面试考核，确保完成新员工的招聘率达到公司的要求；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>及时进行招聘效果分析，提出优化招聘制度和流程的建议；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>定期对招聘工作进行总结，出具数据分析报告。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>人力资源或相关专业本科以上学历。熟悉互联网市场，熟悉企业的人力资源管理规章制度和国家的相关法律法规；</dd>
                            <dt>2.</dt>
                            <dd>了解行业内人才市场现状以及人才招聘流程； 熟悉各岗位用人标准，掌握招聘面试技能；</dd>
                            <dt>3.</dt>
                            <dd>5年以上人力资源招聘模块工作经验，有游戏公司、互联网新媒体公司工作经验者优先。</dd>
                            <dt>4.</dt>
                            <dd>拥有大型企业招聘管理经验，对大型企业的招聘体系和团队建设有系统的把握，并擅于公司招聘品牌的建立和推广；</dd>
                            <dt>5.</dt>
                            <dd>熟悉计算机操作办公软件及相关的人事管理软件；</dd>
                            <dt>6.</dt>
                            <dd>很强的业务敏感度，能够契合业务的发展方向进行人才资源的储备和猎取；</dd>
                            <dt>7.</dt>
                            <dd>优秀的沟通力、理解力、观察力及团队合作精神；</dd>
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
