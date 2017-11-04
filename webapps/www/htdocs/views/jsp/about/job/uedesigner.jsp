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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;UE设计师
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>UE设计师</h3>

                    <p>• 工作地点：北京 </p>

                    <p>• 工作经验：2年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：本科 </p>

                    <p>• 招聘人数：1人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>对手分析，与竞争对手进行全方位比较，重点找出产品差异和分析对手优势；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>产品评估，及时了解市场行业动态，能够对市场进行调研分析，并撰写需求文档；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>用户分析确定用户群分类、特征、行为模式、体验目的、习惯，确定产品功能核心；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>根据产品运行状况，优化产品流程，提高产品用户体验；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>交互设计，协助产品经理规划产品功能及内容，理解交互设计完成DEMO制作；</dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>文档编撰，负责产品各类文档内容的编撰（如产品手册、帮助手册等）。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>本科及以上学历，设计类专业或心理学专业；</dd>
                            <dt>2.</dt>
                            <dd>3年以上互联网产品使用经验，2年以上互联网交互产品设计工作经历；</dd>
                            <dt>3.</dt>
                            <dd>具有优秀的文案功底；</dd>
                            <dt>4.</dt>
                            <dd>具有较强的洞察力，逻辑思考力，清晰的策划思路和创新意识；</dd>
                            <dt>5.</dt>
                            <dd>能够熟练使用Office、Axure RP、Mindjet MindManag等软件；</dd>
                            <dt>6.</dt>
                            <dd>具有较强的沟通协调及分析能力，较高的团队合作意识。</dd>
                            <dt>7.</dt>
                            <dd>心理学专业优先考虑 </dd>
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
