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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;前台
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>前台</h3>

                    <p>• 工作地点：北京 </p>
                    <p>• 工作经验：不要求 </p>
                    <p>• 工作性质：全职 </p>
                    <p>• 最低学历：本科 </p>

                    <li class="listf clearfix">
                        <b>• 岗位职责：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责公司业务往来电话的转接，负责公司文件、信函，传真的打印、收发、登记和送交工作；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>
                                负责来访客人/应聘人员的接待，引领和内部通报工作；
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>
                                负责公司各项行政事务：邮递、办公用品的管理及办公设备的管理使用；
                            </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>
                                及时更新和管理员工通讯地址和电话号码等联系信息；
                            </dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>
                                预定火车票、飞机票、住宿酒店以及日常商务宴的饭店预定；
                            </dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>
                                办理新员工工卡，负责员工考勤管理、统计；
                            </dd>
                        </dl>
                        <dl>
                            <dt>7.</dt>
                            <dd>
                                完成领导交办的其他任务。
                            </dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职资格：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>
                                大专及以上学历，亲和力强、性格开朗、形象气质佳；
                            </dd>
                            <dt>2.</dt>
                            <dd>
                                熟练操作办公软件、办公设备（如复印件，传真机等）；
                            </dd>
                            <dt>3.</dt>
                            <dd>
                                工作热情积极、细致耐心，具有较强的沟通能力、人际能力、有团队精神和服务意识；
                            </dd>
                            <dt>4.</dt>
                            <dd>
                                接受优秀应届毕业生，实习生。
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
