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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;商务经理
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>商务经理</h3>

                    <p>• 工作地点：北京 </p>
                    <p>• 工作经验：2年以上 </p>
                    <p>• 工作性质：全职 </p>
                    <p>• 最低学历：本科 </p>

                    <li class="listf clearfix">
                        <b>• 岗位职责：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>通过无线渠道的拓展、管理、投放及结算，与应用市场、开发者合作，提升APP流量；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>
                                负责广告的投放及后期的数据分析、监测；
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>
                                跟据业务规划负责拓展和维护合作伙伴关系，制定商务拓展业务推广合作流程及规范；
                            </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>
                                开发利用各种品牌、平台、机构的合作关系，提升apps的品牌曝光量与用户活跃度；
                            </dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>
                                挖掘各媒体、渠道及运营的需求，深入挖掘并整合各方面优势资源；
                            </dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>
                                负责所有合作上的跟进工作（包括合同拟定、合作接入、对接跟进、后期维护）；
                            </dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职资格：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>
                                专科或以上学历，熟悉和了解移动互联网，喜欢从事BD相关的工作；
                            </dd>
                            <dt>2.</dt>
                            <dd>
                                2年以上业务拓展、商务合作经验；
                            </dd>
                            <dt>3.</dt>
                            <dd>
                                熟悉各种手机客户端推广渠道，丰富的行业渠道资源，并有成功案例；
                            </dd>
                            <dt>4.</dt>
                            <dd>
                                优秀的商务谈判能力，善于抓住客户需求，沟通说服力强；
                            </dd>
                            <dt>5.</dt>
                            <dd>
                                了解客户需求和市场动态，有无线端策划、推广、组织、实施经验；
                            </dd>
                            <dt>6.</dt>
                            <dd>
                                较强的协调策划能力，团队管理能力，思维活跃，创新能力强；
                            </dd>
                            <dt>7.</dt>
                            <dd>
                                乐观开朗积极上进；工作勤奋主动，有较强的团队精神。
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
