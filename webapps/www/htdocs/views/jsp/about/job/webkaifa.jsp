<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/views/jsp/common/taglibs.jsp"%>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps" />
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
            <h3 class="hr_zp"><a href="/about/job/zhaopin">招聘职位</a> &gt; 前端开发工程师</h3>
          </div>
          <div class="u_fans u_fansz">
          <h3>前端开发工程师</h3>
           <div class="shenqing rel_cont"><a href="mailto:hr@enjoyfound.com" class="rel_but">立即申请</a></div>
            <ul class="zp">
              <p><b>·</b>工作地点：<strong>北京</strong> </p>
              <p><b>·</b>招聘人数：<strong>2人</strong></p>
              <p><b>·</b>职位描述:</p>
              <li class="listf">
                <dl>
                  <dt>1.</dt>
                  <dd>网站前台页面逻辑代码的编写；</dd>
                </dl>
                <dl>
                  <dt>2.</dt>
                  <dd> 按照网站整体设计要求严格展现；</dd>
                </dl>
                <dl>
                  <dt>3.</dt>
                  <dd>同网站后台工程师紧密衔接，严格遵循接口调用方法；</dd>
                </dl>
                 <dl>
                  <dt>4.</dt>
                  <dd>严格限制网页内无关代码的出现量，认真优化实现方式；</dd>
                </dl>
                <dl>
                  <dt>5.</dt>
                  <dd>及时准确的编写技术文档。</dd>
                </dl>
              </li>
              <li class="clear">
              职位要求：<br/>
<dl>
  <dt>1.</dt>
  <dd> 专科以上，2年以上相关工作经验；</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd> 精通JSP网页编程技术；
</dd>
</dl>
<dl>
  <dt>3.</dt>
  <dd>精通JavaScript、CSS编程及应用，擅长Ajax等页面信息异步展现技术；</dd>
</dl>
<dl>
  <dt>4.</dt>
  <dd>熟练使用DreamWeaver等常用工具；</dd>
</dl>

<dl>
  <dt>5.</dt>
  <dd>熟悉软件工程思想；</dd>
</dl>
<dl>
  <dt>6.</dt>
  <dd>明晰界面设计与实现系统的关系和操作方法；</dd>
</dl>
<dl>
  <dt>7.</dt>
  <dd>积极向上，有良好的人际沟通能力，良好的工作协调能力，踏实肯干的工作精神；</dd>
</dl>
<dl>
  <dt>8.</dt>
  <dd>有大型社交网站程序开发经验者，优先考虑。</dd>
</dl>


              </li>
              <li class="clear">

             优先条件：<br/>
<dl>
  <dd>有在国内知名大型网站担任过相关职位的优先。</dd>
</dl></li>
            </ul>
          </div>
        </div>

    <%@ include file="/views/jsp/help/left.jsp" %></div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>


</body>
</html>
