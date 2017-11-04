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
    <c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div id="content">
        <div id="cont_left">
          <div class="u_fans_t">
            <h3 class="hr_zp"><a href="/about/job/zhaopin">招聘职位</a> &gt; 市场总监</h3>
          </div>
          <div class="u_fans u_fansz">
          <div class="shenqing rel_cont"><a href="mailto:hr@enjoyfound.com" class="rel_but">立即申请</a></div>
            <ul class="zp">
            <h3>市场总监</h3>
              <p><b>·</b>工作地点：<strong>北京</strong> </p>
              <p><b>·</b>招聘人数：<strong>1人</strong></p>
              <p><b>·</b>职位描述:</p>
              <li class="listf">
                <dl>
                  <dt>1.</dt>
                  <dd>公司高层管理职位，协助决策层制定公司发展战略，参与制定公司市场（品牌形象、用户量、市场地位等）
                    发展战略以及市场发展目标；</dd>
                </dl>
                <dl>
                  <dt>2.</dt>
                  <dd> 制定公司品牌管理策略，把握公司在行业中的发展方向，建立品牌管理和媒体沟通管理体系，维护并有效提
                    升公司品牌形象和品牌知名度，完成公司在行业中的市场定位； </dd>
                </dl>
                <dl>
                  <dt>3.</dt>
                  <dd> 建立新闻传播体系和危机公关控制体系，负责媒体与舆论的日常监测； </dd>
                </dl>
                <dl>
                  <dt>4.</dt>
                  <dd> 制定和实施年度市场策略、推广计划和渠道，及时提供市场反馈； </dd>
                </dl>
                <dl>
                  <dt>5.</dt>
                  <dd> 协助内容运营部门开展BD和用户拓展合作，整合公司资源进行战略层面的合作； </dd>
                </dl>
                <dl>
                  <dt>6. </dt>
                  <dd> 采取各种真实、有效的线上、线下推广手段提高公司网站和客户端的UV、PV、下载安装量； </dd>
                </dl>
                <dl>
                  <dt>7. </dt>
                  <dd> 规划和管理监督市场活动的预算和使用，合理有效执行广告和市场活动，节省重复市场投入； </dd>
                </dl>
                <dl>
                  <dt>8. </dt>
                  <dd> 负责管理、激励本部门员工，制定并实施绩效考核规范。 </dd>
                </dl>
              </li>
              <li class="clear">
              职位要求：<br/>
<dl>
  <dt>1.</dt>
  <dd> 本科及以上学历，中文、营销、管理类相关专业；</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd>  5年以上互联网行业市场策略、品牌、公关、用户市场推广等管理经验，2年以上市场总监工作经验；
</dd>
</dl>
<dl>
  <dt>3.</dt>
  <dd> 具有敏感的商业和市场意识，分析问题及解决问题的能力强，具有优秀的资源整合能力和业务推进能力；</dd>
</dl>
<dl>
  <dt>4.</dt>
  <dd>  有较强的市场能感知能力、丰富的市场推广与品牌建设能力；</dd>
</dl>
 <dl>
  <dt>5. </dt>
  <dd>  具备很强的策划能力，熟悉各媒体运作方式，有大型市场活动推广成功经验；
</dd>
</dl>
 <dl>
  <dt>6.  </dt>
  <dd> 出色的人际沟通能力、协调处理能力和商务谈判技巧、团队建设有组织开拓能力；
</dd>
</dl>
 <dl>
  <dt>7.  </dt>
  <dd>应聘者需提交可被证明的成功市场案例。
</dd>
</dl>

              </li>
              <li class="clear">

             优先条件：<br/>
<dl>
  <dd>同时拥有有大型互联网公司和媒体公关从业经验者优先。</dd>
</dl></li>

            </ul>
          </div>
        </div>

    <%@ include file="/views/jsp/help/left.jsp" %></div>
    <%@ include file="/views/jsp/common/footer.jsp" %>
</div>


</body>
</html>
