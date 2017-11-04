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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;专区运营专员
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>专区运营专员</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作经验：1年以上 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：大专 </p>

                    <p>• 招聘人数：31人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责手机游戏网站的文章编辑、内容更新、内容审核；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>负责手机游戏网站的资讯搜集、整理、上传；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>配合责任编辑进行日常工作；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>负责专区运营的工作，原创攻略撰写。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>大专以上学历，热爱互联网工作，如果有手机游戏行业的工作经验和案例则优先考虑；</dd>
                            <dt>2.</dt>
                            <dd>具备良好的文字功底和编写能力，有媒体编辑领域从业经验者优先；</dd>
                            <dt>3.</dt>
                            <dd>熟悉手机游戏行业，喜欢玩手机游戏、钻研手机游戏攻略，对编辑工作有强烈的兴趣；</dd>
                            <dt>4.</dt>
                            <dd>熟练操作常用办公软件，有一定的文档撰写能力和图片处理能力；</dd>
                            <dt>5.</dt>
                            <dd>具备优秀的执行力，善于团队协作。</dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <br><br>

            <div class="sendresume">
                <span><img width="16" height="16" src="http://lib.joyme.com/static/theme/default/img/zlzp.jpg"><a target="_blank" href="http://special.zhaopin.com/sh/2014/11027/qhzmwl071856/">去智联招聘投简历</a></span>
                <span><a target="_blank" href="http://ehr.goodjobs.cn/show.php?corpID=31654">去新安人才网简历</a></span>
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
