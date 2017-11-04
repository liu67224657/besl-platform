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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;美术设计部经理
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>美术设计部经理</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <li class="listf clearfix">
                        <b>• 岗位职责：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd> 带领并指导每款产品的创意构思及执行，保证并监督设计部的作品质量；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>
                                负责带领创意团队并对部门成员进行专业培训和指导；
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>
                                根据公司各项目要求及特点组织研讨、确定设计方案，带领设计团队保质保量按时完成设计任务；
                            </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>负责管理策划工作，把关设计团队作品，确定项目的整体设计风格；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>制定、规范规划设计部操作流程及工作程序，协调部门工作、学习。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职资格：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>
                                5年以上相关工作经验,美工基础过硬，擅长网页，美工设计，对色彩敏感；
                            </dd>
                            <dt>2.</dt>
                            <dd>
                                有成功作品，有大型门户网站或移动端策划、设计、制作经验；
                            </dd>
                            <dt>3.</dt>
                            <dd>
                                精通HTML语言、CSS、javascript ；精通WEB标准（DIV+CSS）；
                            </dd>
                            <dt>4.</dt>
                            <dd>
                                精通平面设计软件：Photoshop、CarelDraw；熟练使用网页制作三剑客：Dreamweaver、Flash、Fireworks；
                            </dd>
                            <dt>5.</dt>
                            <dd>
                                对游戏或者动漫有一定的热爱和了解。
                            </dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <br><br>

            <div class="sendresume">
                <span><img width="16" height="16" src="http://lib.joyme.com/static/theme/default/img/zlzp.jpg"><a target="_blank" href="http://special.zhaopin.com/sh/2014/11027/qhzmwl071856/">去智联招聘投简历</a></span>
                <span>
                    <img width="16" height="16" src="https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1691690209,808162055&fm=58">
                    <a target="_blank" href="http://ehr.goodjobs.cn/show.php?corpID=31654">去新安人才网简历</a></span>
            </div>
        </div>
    </div>

</div>
<!--content结束-->
<div class="piccon"></div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
</body>
</html>
