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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;商务拓展专员
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>商务拓展专员</h3>

                    <p>·工作地点：北京 </p>

                    <p>·工作经验：1年以上 </p>

                    <p>·最低学历：大专 </p>

                    <p>·工作性质：全职 </p>

                    <p>·招聘人数：1人</p>

                    <p>·管理经验：否</p>
                    <li class="listf clearfix">
                        <b>· 职位描述：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责与各手游厂商的联系与开拓，并随时收集手游厂商的最新信息；</dd>
                            <dt>2.</dt>
                            <dd>根据已确定的合作模式，开展对于手机游戏厂商的商务拓展，完成各产品线的发展目标；</dd>
                            <dt>3.</dt>
                            <dd>深挖合作方需求，根据运营效果，调整优化合作模式；</dd>
                            <dt>4.</dt>
                            <dd>负责落实公司商务模式执行，维护客户关系，促成战略合作达成；</dd>
                            <dt>5.</dt>
                            <dd>定期收集整理合作产品上线效果数据，并对数据进行有效的分析和评估，为项目的有效执行和调整提供建议。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> · 任职要求：</b><br>

                        <p>必要条件：</p>
                        <dl>
                            <dt>1.</dt>
                            <dd>专科或以上学历，熟悉和了解游戏圈喜欢从事与游戏相关的工作；</dd>
                            <dt>2.</dt>
                            <dd>一年以上业务拓展、商务合作经验优先；</dd>
                            <dt>3.</dt>
                            <dd>充满激情、勤奋刻苦、独立开展工作能力强、悟性高、善于总结、能承受压力；</dd>
                            <dt>4.</dt>
                            <dd>熟悉手游厂商，有手游媒体从业经验者优先。</dd>
                        </dl>
                        <p><br/>优先条件：</p>
                        <dl>
                            <dt>1.</dt>
                            <dd>游戏媒体工作经验</dd>
                            <dt>2.</dt>
                            <dd>熟悉手游市场及有厂商资源</dd>
                            <dt>3.</dt>
                            <dd>商务谈判的成功案例</dd>
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
