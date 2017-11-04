<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/views/jsp/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/views/jsp/common/meta.jsp"%>
<title>招贤纳士 ${jmh_title}</title>
<link href="${libdomain}/static/default/css/core.css" rel="stylesheet" type="text/css"/>
<link href="${libdomain}/static/default/css/home.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${libdomain}/static/js/jquery-1.5.2.js"></script>
<script type="text/javascript" src="${libdomain}/static/js/common.js"></script>
 <script>
				$(document).ready(function(){
						$(".search_text").focus(function(){
							  $(".serach_tips").show();
						  });
						$(".search_text").blur(function(){
							  $(".serach_tips").hide();
						  });
						$(".serach_tips li").hover(function(){
							  $(this).addClass("hover");
						  }, function(){
							 $(this).removeClass("hover");
						  });
						$(".user_tool li").hover(function(){
							  $(this).addClass("hover");
						  }, function(){
							 $(this).removeClass("hover");
						  });
						$(".user_tags li").hover(function(){
							  $(this).addClass("hover");
						  }, function(){
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
            <h3 class="hr_zp"><a href="/about/job/zhaopin">招聘职位</a> &gt; UE设计师 </h3>
          </div>
          <div class="u_fans u_fansz">
           <div class="shenqing rel_cont"><a href="mailto:hr@enjoyfound.com" class="rel_but">立即申请</a></div>
            <ul class="zp">
            <h3>UE设计师</h3>
              <p><b>·</b>工作地点：<strong>北京</strong> </p>
              <p><b>·</b>招聘人数：<strong>1人</strong></p>
              <p><b>·</b>职位描述:</p>
              <li class="listf">
                <dl>
                  <dt>1.</dt>
                  <dd>参与产品的规划和构思，负责新功能规划中的交互细节设计；</dd>
                </dl>
                <dl>
                  <dt>2.</dt>
                  <dd> 审核产品线内的交互实现质量，对线上功能的交互细节做持续优化；</dd>
                </dl>
                <dl>
                  <dt>3.</dt>
                  <dd> 制定产品的信息架构规范，基础页面布局和交互方式、原则； </dd>
                </dl>
                <dl>
                  <dt>4.</dt>
                  <dd> 定期分析各类新网站的交互方式变化，建立交互研究项目。 </dd>
                </dl>
              </li>
              <li class="clear">
              职位要求：<br/>
<dl>
  <dt>1.</dt>
  <dd> 2年以上相关经验，大专以上学历；</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd> 对交互设计过程有深入的了解，可以独立完成整个设计过程（需求分析、信息架构、流程图、线框图等等交互设计方法能熟练应用）；
</dd>
</dl>
<dl>
  <dt>3.</dt>
  <dd> 对各类网站及大众软件动向有灵敏的触觉，并能第一时间尝试和分析，乐于动手实践；</dd>
</dl>
<dl>
  <dt>4.</dt>
  <dd> 熟悉各类SNS和微博类社区产品，喜欢游戏 ；</dd>
</dl>
<dl>
  <dt>5.</dt>
  <dd> 强逻辑思维能力，熟练掌握业务需求分析、产品需求分解的技巧 ；</dd>
</dl>
<dl>
  <dt>6.</dt>
  <dd> 主动性高，理解力强、善于沟通与协调，很强的文字表达能力；</dd>
</dl>
<dl>
  <dt>7.</dt>
  <dd> 具备以用户为中心的思想，良好合作态度及团队精神，并富有工作激情、创造力和责任感，能承受高强度的工作压力。</dd>
</dl>

              </li>
              <li class="clear">

             优先条件：<br/>
<dl>
  <dd>具有大型网站的UI设计经验，兼具网络软件界面设计者经验优先</dd>
</dl></li>
            </ul>
          </div>
        </div>
    <%@ include file="/views/jsp/help/left.jsp" %></div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>


</body>
</html>
