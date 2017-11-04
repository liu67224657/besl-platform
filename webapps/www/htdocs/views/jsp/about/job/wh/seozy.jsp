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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;SEO专员
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>SEO专员</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作经验：2年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：大专 </p>

                    <p>• 招聘人数：3人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责网站内部优化，对内容、构架及代码等进行优化，有效利于网站发展；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>负责搜索引擎优化，评估分析网站关键词，提升网站关键词的搜索和自然排名；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>跟踪分析引入的流量以及相关关键词的排名，形成阶梯性数据报告并提出后续优化方案；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>监测跟踪百度、谷歌等收录情况和排名效果，制定SEO月报总结和计划；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>合理构建网站外链，包括论坛、博客及软文外链等；</dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>熟悉论坛、博客、百度问答等营销方法，负责制定营销策略。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责网站内部优化，对内容、构架及代码等进行优化，有效利于网站发展；</dd>
                            <dt>2.</dt>
                            <dd>负责搜索引擎优化，评估分析网站关键词，提升网站关键词的搜索和自然排名；</dd>
                            <dt>3.</dt>
                            <dd>跟踪分析引入的流量以及相关关键词的排名，形成阶梯性数据报告并提出后续优化方案；</dd>
                            <dt>4.</dt>
                            <dd>监测跟踪百度、谷歌等收录情况和排名效果，制定SEO月报总结和计划；</dd>
                            <dt>5.</dt>
                            <dd>合理构建网站外链，包括论坛、博客及软文外链等；</dd>
                            <dt>6.</dt>
                            <dd>熟悉论坛、博客、百度问答等营销方法，负责制定营销策略。</dd>
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
