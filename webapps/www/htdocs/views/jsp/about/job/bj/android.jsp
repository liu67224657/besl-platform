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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;android开发工程师（高级）
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>android开发工程师（高级）</h3>

                    <p>• 工作地点：北京 </p>
                    <p>• 工作经验：3年以上 </p>
                    <p>• 工作性质：全职 </p>
                    <p>• 最低学历：本科 </p>

                    <li class="listf clearfix">
                        <b>• 岗位职责：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>全程参与android版本产品需求，功能等定义。</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>
                                担任产品开发任务，可以独立完成android产品的开发。
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>
                                参与团队技术交流和分享活动，促进团队共同进步。
                            </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>对互联网应用有独到的见解与认识，追求良好的用户体验。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职资格：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>
                                计算机及相关专业本科；
                            </dd>
                            <dt>2.</dt>
                            <dd>
                                3年以上android开发工作经验，有android实际产品发布开发经验或参与过大型复杂项目的开发工作并担任重要角色。
                            </dd>
                            <dt>3.</dt>
                            <dd>
                                熟悉Android架构和相关技术，基于Android平台系统进行应用开发；
                            </dd>
                            <dt>4.</dt>
                            <dd>
                                能独立进行Android应用功能的设计、开发、调试、发布；
                            </dd>
                            <dt>5.</dt>
                            <dd>
                                对软件产品有强烈的责任心，具备良好的沟通能力和优秀的团队协作能力；
                            </dd>
                            <dt>6.</dt>
                            <dd>
                                有创业激情，认真踏实，热爱移动互联网，有积极的工作态度和良好的团队合作精神；
                            </dd>
                            <dt>7.</dt>
                            <dd>
                                熟练掌握Objective-C，能够熟练使用Xcode及Instruments工具。
                            </dd>
                            <dt>8.</dt>
                            <dd>
                                熟练掌握TCP/IP，UDP协议，对socket长连接机制有深入的了解。
                            </dd>
                            <dt>9.</dt>
                            <dd>
                                熟练掌握各种音频、视频格式的存储、转码、解码。
                            </dd>
                            <dt>10.</dt>
                            <dd>
                                熟练掌握XMPP协议。
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
