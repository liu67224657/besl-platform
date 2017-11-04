<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>招贤纳士 ${jmh_title}</title>
    <link href="${libdomain}/static/default/css/core.css" rel="stylesheet" type="text/css"/>
    <link href="${libdomain}/static/default/css/home.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${libdomain}/static/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" src="${libdomain}/static/js/common.js"></script>
    <script>
        $(document).ready(function () {
            $(".search_text").focus(function () {
                $(".serach_tips").show();
            });
            $(".search_text").blur(function () {
                $(".serach_tips").hide();
            });
            $(".serach_tips li").hover(function () {
                $(this).addClass("hover");
            }, function () {
                $(this).removeClass("hover");
            });
            $(".user_tool li").hover(function () {
                $(this).addClass("hover");
            }, function () {
                $(this).removeClass("hover");
            });
            $(".user_tags li").hover(function () {
                $(this).addClass("hover");
            }, function () {
                $(this).removeClass("hover");
            });
        });
    </script>
</head>
<body>
<div id="wraper">
    <%@ include file="/views/jsp/help/header.jsp" %>
    <div id="content">
        <div id="cont_left">
            <div class="u_fans_t">
                <h3 class="hr_zp"><a href="/about/job/zhaopin">招聘职位</a>&gt; 用户体验设计经理</h3>
            </div>
            <div class="u_fans u_fansz">
                <div class="shenqing rel_cont"><a href="mailto:hr@enjoyfound.com" class="rel_but">立即申请</a></div>
                <ul class="zp">
                    <h3>用户体验设计经理</h3>

                    <p><b>·</b>工作地点：<strong>北京</strong></p>

                    <p><b>·</b>招聘人数：<strong>1人</strong></p>

                    <p><b>·</b>职位描述:</p>
                    <li class="listf">
                        <dl>
                            <dt>1.</dt>
                            <dd>领导设计组的视觉设计（UI）、交互细节设计（UE）、前端设计（CSS\DIV）工作；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd> 负责网站的视觉基调定义，确定视觉细节的风格走向；</dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>参与各个阶段的版本计划制定，并安排对应的视觉、交互、前端工作；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd> 领导视觉组不断对现有网站的视觉架构、相关的前端代码架构做出优化调整；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd> 定期分析各类新网站的视觉、交互、前端设计动态，安排研究项目。</dd>
                        </dl>

                    </li>
                    <li class="clear">
                        职位要求：<br/>
                        <dl>
                            <dt>1.</dt>
                            <dd> 3年以上相关经验，本科以上学历，美术或设计类专业毕业；</dd>
                        </dl>
                        <dl>
                            <dt>2.</dt>
                            <dd> 有丰富的设计知识、设计经验、对流行趋势敏锐的洞察力，对GUI设计趋势有灵敏触觉和领悟能力，能够独立给产品定义视觉基调、并推动设计执行团队的设计能力；
                            </dd>
                        </dl>
                        <dl>
                            <dt>3.</dt>
                            <dd>对交互设计（UE）、前端设计（CSS\DIV）过程有深入的了解；</dd>
                        </dl>
                        <dl>
                            <dt>4.</dt>
                            <dd> 熟悉各类SNS和微博类社区产品，喜欢游戏 ；</dd>
                        </dl>
                        <dl>
                            <dt>5.</dt>
                            <dd> 强逻辑思维能力，熟练掌握业务需求分析、产品需求分解的技巧 ，学习能力强，时间管理能力强；
                            </dd>
                        </dl>
                        <dl>
                            <dt>6.</dt>
                            <dd> 具备以用户为中心的思想，良好合作沟通能力、团队精神，能承受高强度的工作压力。
                            </dd>
                        </dl>
                    </li>
                    <li class="clear">

                        优先条件：<br/>
                        <dl>
                            <dd>有在国内知名大型网站担任过相关职位的优先。</dd>
                        </dl>
                    </li>
                </ul>
            </div>
        </div>
        <%@ include file="/views/jsp/help/left.jsp" %>
    </div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
