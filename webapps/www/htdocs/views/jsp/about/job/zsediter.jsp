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
                        <h3 class="hr_zp"><a href="zhaopin.html">招聘职位</a> &gt;资深编辑   </h3>
                      </div>
                      <div class="u_fans u_fansz">
                        <div class="shenqing rel_cont"><input type="button" onclick="javascript:savePostChat()" value="立即申请" class="rel_but" id="postChat" name=""></div>
                        <ul class="zp"><h3>资深编辑          </h3>
                          <p><b>·</b>工作地点：<strong>北京</strong> </p>
                          <p><b>·</b>招聘人数：<strong>1人</strong></p>
                          <p><b>·</b>职位描述:</p>
                          <li class="listf">
                            <dl>
                              <dt>1.</dt>
                              <dd>收集整理优秀游戏相关文章汇总，制作专题；</dd>
                            </dl>
                            <dl>
                              <dt>2.</dt>
                              <dd>捕捉最新游戏时讯并发布；</dd>
                            </dl>
                            <dl>
                              <dt>3.</dt>
                              <dd>撰写最新游戏评测。</dd>
                            </dl>
                           
                          </li>
                          <li class="clear">
                          任职要求：<br/>
<dl>
  <dt>1.</dt>
  <dd>大专以上学历，2年以上游戏杂志/网站编辑经验；</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd>接触游戏的时间超过五年，了解各类型游戏；
</dd>
</dl>
<dl>
  <dt>3.</dt>
  <dd>熟悉游戏产业环境、游戏历史及文化，了解玩家心理，并有独立的见解；</dd>
</dl>
<dl>
  <dt>4.</dt>
  <dd>文笔流畅、思维活跃，同时具有较强的策划能力，擅长话题捕捉和专题制作；</dd>
</dl>
<dl>
  <dt>5.</dt>
  <dd>熟悉互联网、媒体，阅读面广，思维活跃，具备较强的资料搜集、整理、再创能力；</dd>
</dl>
<dl>
  <dt>6.</dt>
  <dd>日语或英语读写能力较强、担任过游戏专题站站长者优先。</dd>
</dl>
                          </li>
       
                        </ul>
                      </div>
                    </div>
                    <div id="cont_right">
                      <div class="help">
                        <div class="ht"></div>
                        <div class="hc">
                          <ul>
                            <li><a href="#">关于着迷</a></li>
                            <li><a href="#">服务条款</a></li>
                            <li><a href="#"><b>招贤纳士</b></a></li>
                            <li><a href="#">意见反馈</a></li>
                            <li class="noborder"><a href="#">帮助说明</a></li>
                          </ul>
                        </div>
                        <div class="hb"></div>
                      </div>
                    </div>
                  </div>
    <%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>


</body>
</html>
