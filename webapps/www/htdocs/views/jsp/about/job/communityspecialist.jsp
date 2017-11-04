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

        <div class="jobdetail-title"><a href="http://www.joyme.com/about/job/zhaopin">招聘职位</a>&nbsp;&gt;&nbsp;社区运营专员
        </div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
            <div class="u_fans u_fansz">
                <ul class="zp">
                    <h3>社区运营专员</h3>

                    <p>• 工作地点：芜湖 </p>

                    <p>• 工作经验：不限 </p>

                    <p>• 工作性质：全职 </p>

                    <p>• 最低学历：大专 </p>

                    <p>• 招聘人数：36人</p>

                    <p>• 管理经验：否</p>
                    <li class="listf clearfix">
                        <b>• 工作职责:</b>
                        <dl>
                            <dt>1.</dt>
                            <dd>负责运营维护玩家QQ群和百度贴吧，激励和培养新用户，提高用户的活跃度和粘性；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd>有敏感的热门内容挖掘能力，并能通过包装与推广带动提高QQ群人数以及贴吧关注度；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>热爱互联网工作对手机游戏有一定的了解和认知；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd>具有优秀的执行和营销能力，能够独立策划、组织玩家活动；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd>思维活跃，对玩家需求具有极强的洞察力和判断力；</dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd>制造玩家关注话题，及时与玩家进行互动，提高玩家活跃度，从而提高网站品牌及产品知名度；</dd>
                        </dl>
                        <dl>
                            <dt>7.</dt>
                            <dd>及时更新和完善推广渠道内容，锁定目标用户，培养核心用户，增加推广的宽度及深度。</dd>
                        </dl>
                    </li>
                    <li class="listf clearfix">
                        <br><b> • 任职要求：</b><br>
                        <dl>
                            <dt>1.</dt>
                            <dd>大专以上学历，热爱互联网工作，兴趣广泛，对手机游戏有一定的了解和认知；</dd>
                            <dt>2.</dt>
                            <dd>了解百度贴吧和QQ群的推广方式；</dd>
                            <dt>3.</dt>
                            <dd>性格开朗有耐性，能够承受绩效考核的压力，愿意接受新事物；</dd>
                            <dt>4.</dt>
                            <dd>工作积极主动、认真细心踏实、团队意识强、具有很好的沟通能力。</dd>
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
