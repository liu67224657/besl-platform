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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;法务经理
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>法务经理</h3>

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
                            <dd>为公司经营项目进行法律调研和法律审核，提供可行性、合法性分析和法律风险分析，保障经营项目运营的安全性和合法性。</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>收集，整理，保管与公司经营管理有关的法律、法规、政策文件资料，负责公司的法律事务档案管理。</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>负责公司各类法律文书及合同的拟定，审核，修改，管理等。</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>协助公司职能部门办理有关的法律事务并审查相关法律文件</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>代表公司处理各类诉讼或非诉讼法律事务，维护公司合法权益。</dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>对员工进行法律知识方面的培训</dd>
                        </dl>
                        <dl>
                            <dt>7.</dt>
                            <dd>完成上级交办的其它工作。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>本科或以上学历，法律相关专业。有良好的英文读写能力，特别是法律相关专业英文的读写能力。</dd>
                            <dt>2.</dt>
                            <dd>5年或以上互联网公司同岗任职相关工作经验，有各类合同拟定，审核，管理经验，能依法独立起草，修改法律文书特别是商务类合同。</dd>
                            <dt>3.</dt>
                            <dd>有一定的法律知识培训经验。 </dd>
                            <dt>4.</dt>
                            <dd>具有较强的服务意识、逻辑分析能力、判断能力、谈判技巧和沟通协调能力；具备独立思考并处理突发事件的能力；</dd>
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
