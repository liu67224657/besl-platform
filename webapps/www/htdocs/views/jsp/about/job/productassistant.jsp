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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;产品专员（移动方向）
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>产品专员（移动方向）</h3>

                    <p>·工作地点：北京 </p>

                    <p>·工作经验：1年以上 </p>

                    <p>·最低学历：本科 </p>

                    <p>·工作性质：全职 </p>

                    <p>·招聘人数：1人</p>

                    <p>·管理经验：否</p>
                    <li class="listf clearfix">
                        <b>· 职位描述：</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>多个移动产品的开发、运营协调；</dd>
                            <dt>2.</dt>
                            <dd>根据用户反馈和产品策略，对移动产品不断进行功能优化；</dd>
                            <dt>3.</dt>
                            <dd>同类产品的信息日常调研；</dd>
                            <dt>4.</dt>
                            <dd>移动产品上线后的营销与流量优化。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> · 任职要求：</b><br>

                        <p>必要条件：</p>
                        <dl>
                            <dt>1.</dt>
                            <dd>大学本科以上学历，互联网或游戏公司工作经验一年以上；</dd>
                            <dt>2.</dt>
                            <dd>至少从事过运营、客服、产品、开发任何一项工作经历；</dd>
                            <dt>3.</dt>
                            <dd>良好的人格魅力和学习能力，协调和沟通能力强，较强的工作组织能力、责任心；</dd>
                            <dt>4.</dt>
                            <dd>熟悉移动设备和应用软件，至少拥有一台智能手机，能够分析3款以上的应用软件特色；</dd>
                            <dt>5.</dt>
                            <dd>玩游戏，至少深入玩过三款以上的游戏，类型不限；</dd>
                        </dl>
                        <p><br/>优先条件：</p>
                        <dl>
                            <dt>1.</dt>
                            <dd>有项目管理相关经验；</dd>
                            <dt>2.</dt>
                            <dd>有移动产品相关经验；</dd>
                            <dt>3.</dt>
                            <dd>有数据分析挖掘相关经验；</dd>
                            <dt>4.</dt>
                            <dd>有市场调查相关经验；</dd>
                            <dt>5.</dt>
                            <dd>有游戏行业相关经验。</dd>
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
