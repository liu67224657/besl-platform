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
            <h3 class="hr_zp"><a href="/about/job/zhaopin">招聘职位</a> &gt;IPhone应用开发工程师</h3>
          </div>
          <div class="u_fans u_fansz">
            <div class="shenqing rel_cont"><a href="mailto:hr@enjoyfound.com" class="rel_but">立即申请</a></div>
            <ul class="zp">
            <h3>IPhone应用开发工程师</h3>
              <p><b>·</b>工作地点：<strong>北京</strong> </p>
              <p><b>·</b>招聘人数：<strong>2人</strong></p>
              <p><b>·</b>职位描述:</p>
              <li class="listf">
                <dl>
                  <dt>1.</dt>
                  <dd>全程参与手机App产品需求，功能等定义；</dd>
                </dl>
                <dl>
                  <dt>2.</dt>
                  <dd> 担任App产品开发任务，可以独立完成App的开发；</dd>
                </dl>
                <dl>
                  <dt>3.</dt>
                  <dd>参与团队技术交流和分享活动，促进团队共同进步；</dd>
                </dl>
                 <dl>
                  <dt>4.</dt>
                  <dd>对互联网应用有独到的见解与认识，追求良好的用户体验。</dd>
                </dl>

              </li>
              <li class="clear">
              职位要求：<br/>
<dl>
  <dt>1.</dt>
  <dd> 精通Objective-C语言开发；</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd> 熟悉iOS SDK，掌握Xcode对面向对象编程；
</dd>
</dl>
<dl>
  <dt>3.</dt>
  <dd>半年以上iOS开发经验，能独立开发iPhone App； </dd>
</dl>
<dl>
  <dt>4.</dt>
  <dd>对软件产品有强烈的责任心，具备良好的沟通能力和优秀的团队协作能力。</dd>
</dl>



              </li>
              <li class="clear">优先条件：<br/>
<dl>
  <dd>有成功发布的App者优先。</dd>
</dl></li>
            </ul>
          </div>
        </div>
    <%@ include file="/views/jsp/help/left.jsp" %></div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
