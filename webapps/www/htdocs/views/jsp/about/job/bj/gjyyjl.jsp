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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;运营高级经理
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>运营高级经理</h3>

                    <p>• 工作地点：北京 </p>
                    <p>• 工作经验：3-5年 </p>
                    <p>• 工作性质：全职 </p>
                    <p>• 最低学历：本科 </p>

                    <li class="listf clearfix">
                        <b>• 岗位职责：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责App的推广和日常运营工作，通过网络和线下手段进行APP推广，实现下载量、安装量、活跃度目标；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>
                                对运营数据、用户行为数据等进行分析和挖掘，提升运营质量，提升APP用户量和活跃度；
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>
                                运营数据监控分析、竞争对手产品分析，根据数据结论对产品的发展进行把控，并形成分析报告；
                            </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>
                                负责团队建设、团队培训和日常工作开展；制订、完善、贯彻实施运营管理制度、流程；
                            </dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>
                                针对目前及未来增值业务市场发展趋势，结合市场热点，策划、执行、推进公司的业务运营战略、流程与计划，组织协调公司内外资源、实现公司的运营目标。
                            </dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职资格：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>
                                本科以上学历，两款以上移动App的运营经验（或一年以上APP推广工作经验）；
                            </dd>
                            <dt>2.</dt>
                            <dd>
                                熟悉移动互联网行业，熟悉各种软件商店、论坛、手机厂商或渠道商，了解无线产品各市场的规则，熟悉iOS和Android平台及App产品，对App的推广和运营有自己的认识；
                            </dd>
                            <dt>3.</dt>
                            <dd>
                                熟悉App的各类指标、收集用户行为数据以及相应统计方法；
                            </dd>
                            <dt>4.</dt>
                            <dd>
                                具备较强的市场分析、营销、策划、推广能力，熟知移动营销的基本方法；
                            </dd>
                            <dt>5.</dt>
                            <dd>
                                有较强的沟通能力以及处理问题的能力，抗压能力较强。
                            </dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <br><br>

            <div class="sendresume">
                <span><img width="16" height="16" src="http://lib.joyme.com/static/theme/default/img/zlzp.jpg"><a target="_blank" href="http://special.zhaopin.com/sh/2014/zlzp042150/">去智联招聘投简历</a></span>
            <span>
            <img width="11" height="16" src="http://lib.joyme.com/static/theme/default/img/51job.jpg">
            <a target="_blank" href="http://search.51job.com/list/co,c,2581201,000000,10,1.html">去51job投简历</a></span>
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
