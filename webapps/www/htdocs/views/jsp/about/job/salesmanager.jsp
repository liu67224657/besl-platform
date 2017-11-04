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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;广告销售经理
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a href="mailto:jobs@staff.joyme.com" class="submitbtn"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>广告销售经理</h3>

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
                            <dd>负责行业销售目标及管理目标的达成。</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>拟定销售战略，制定销售计划,分解目标,计划为季度,月度并有效执行。</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>负责行业重点客户的维护,重点项目的跟踪管理。</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>拟定管理制度规定，流程并有效执行。</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>负责销售团队的日常工作指导,协调,监督与管理。</dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>竞品销售市场信息收集、分析与管理。</dd>
                        </dl>
                        <dl>
                            <dt>7.</dt>
                            <dd>合理安排并督导所管辖工作团队人员工作，并负责团队成员的培养；</dd>
                        </dl>
                        <dl>
                            <dt>8.</dt>
                            <dd>完成领导交办的其他工作。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>

                        <p>必要条件：</p>
                        <dl>
                            <dt>1.</dt>
                            <dd>本科以上学历，3年以上网站或者IT媒体广告销售工作者；</dd>
                            <dt>2.</dt>
                            <dd>能熟练使用计算机；</dd>
                            <dt>3.</dt>
                            <dd>熟悉互联网、游戏行业、网络广告的市场特点；</dd>
                            <dt>4.</dt>
                            <dd>良好的沟通能力，高度的责任心；</dd>
                            <dt>5.</dt>
                            <dd>具有相关行业经验和资源，丰富的媒体销售经验，与所负责客户有良好的合作关系。</dd>
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
