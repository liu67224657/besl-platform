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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;测试工程师
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>测试工程师</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <li class="listf clearfix">
                        <b>• 岗位职责：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd> 制定、编写软件测试用例；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>
                                按时完成软件测试工作任务；
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>
                                编写测试文档、测试报告、提交测试结果；
                            </dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>
                                研究软件测试方法，不断优化测试工作；
                            </dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>
                                准确地定位并跟踪问题，推动问题及时合理地解决；
                            </dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职资格：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>
                                一年以上软件测试经验，精通移动互联网产品测试；
                            </dd>
                            <dt>2.</dt>
                            <dd>
                                熟悉软件开发生命周期，有良好的测试理论基础，熟悉测试流程和规范、熟练使用常用测试工具。
                            </dd>
                            <dt>3.</dt>
                            <dd>
                                有自动化测试、性能测试经验者优先。
                            </dd>
                            <dt>4.</dt>
                            <dd>
                                工作责任心强，细致，耐心，有较强的抗压能力和学习能力；
                            </dd>
                            <dt>5.</dt>
                            <dd>
                                较强的发现问题、分析问题的能力；较强的语言表达能力和文档撰写能力；
                            </dd>
                            <dt>6.</dt>
                            <dd>
                                热爱互联网；有积极的工作态度和良好的团队合作精神。
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
