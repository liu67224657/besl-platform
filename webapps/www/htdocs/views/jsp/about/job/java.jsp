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
            <h3 class="hr_zp"><a href="/about/job/zhaopin">招聘职位</a> &gt; JAVA开发工程师 </h3>
          </div>
          <div class="u_fans u_fansz">
          <div class="shenqing rel_cont"><a href="mailto:hr@enjoyfound.com" class="rel_but">立即申请</a></div>
            <ul class="zp">
            <h3>JAVA开发工程师 </h3>
              <p><b>·</b>工作地点：<strong>北京</strong> </p>
              <p><b>·</b>招聘人数：<strong>2人</strong></p>
              <p><b>·</b>职位描述:</p>
              <li class="listf">
                <dl>
                  <dt>1.</dt>
                  <dd>网站后台逻辑代码的编写；</dd>
                </dl>
                <dl>
                  <dt>2.</dt>
                  <dd> 按照网站整体架构设计要求实现框架结构；</dd>
                </dl>
                <dl>
                  <dt>3.</dt>
                  <dd>为前端开发提供详细准确的接口，对数据库的访问操作精炼、高效；</dd>
                </dl>
                 <dl>
                  <dt>4.</dt>
                  <dd>积极合理的实现缓存、多线程调度策略；</dd>
                </dl>
                <dl>
                  <dt>5.</dt>
                  <dd>及时准确的编写技术文档；</dd>
                </dl>
              </li>
              <li class="clear">
              职位要求：<br/>
<dl>
  <dt>1.</dt>
  <dd> 计算机及相关专业本科，2年以上相关工作经验；</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd> 精通J2EE框架下B/S网站编程技术，编程风格清晰；
</dd>
</dl>
<dl>
  <dt>3.</dt>
  <dd>熟悉软件工程思想和基本设计模式；</dd>
</dl>
<dl>
  <dt>4.</dt>
  <dd>熟练配置、使用Apache、Jboss等常用服务软件，熟悉SQL语言编写；</dd>
</dl>

<dl>
  <dt>5.</dt>
  <dd>有创业激情，认真踏实，热爱互联网；有积极的工作态度和良好的团队合作精神；</dd>
</dl>
<dl>
  <dt>6.</dt>
  <dd>有大型社交网站程序开发经验者，优先考虑；</dd>
</dl>


              </li>

            </ul>
          </div>
        </div>

    <%@ include file="/views/jsp/help/left.jsp" %></div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>


</body>
</html>
