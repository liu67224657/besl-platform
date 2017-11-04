<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>招贤纳士 ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>
</head>
<body>
<%@ include file="/views/jsp/tiles/header-only-icon.jsp" %>
<div class="content about-bg clearfix">
    <%@ include file="/views/jsp/help/left.jsp" %>
    <div class="about-right">
        <div class="jobdetail-title"><a href="${ctx}/about/job/zhaopin">招聘职位</a>&nbsp;>&nbsp;招聘经理</div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:hr@enjoyfound.com"><span>立即申请</span></a></div>
           <div class="u_fans u_fansz">
                        <ul class="zp"><h3>招聘经理 </h3>
                          <p>·工作地点：北京 </p>
                          <p>·招聘人数：1人</p>
                          
                          <li class="listf clearfix">
                          <b>·职位描述:</b>
                            <dl>
                              <dt>1.</dt>
                              <dd>制订并执行招聘计划，进行工作分析，完成职位说明书体系建设，建立招聘流程体系并持续优化改进；</dd>
                            </dl>
                            <dl>
                              <dt>2.</dt>
                              <dd>	负责各部门招聘需求信息的采集、分析、整理，并制定和组织实施招聘计划；</dd>
                            </dl>
                            <dl>
                              <dt>3.</dt>
                              <dd>挖掘、建立和维护多招聘渠道；</dd>
                            </dl>
                            <dl>
                              <dt>4.</dt>
                              <dd>发布职位信息，进行简历甄别、人才测评、面试、录用等工作，寻找合适人员满足业务发展的需要；</dd>
                            </dl>
                            <dl>
                              <dt>5.</dt>
                              <dd>规划并组织实施校园招聘；</dd>
                            </dl>
                            <dl>
                              <dt>6.</dt>
                              <dd>建立并完善简历库和储备人才库，按期提交招聘分析报告；</dd>
                            </dl>
                            <dl>
                              <dt>7.</dt>
                              <dd>完成上级交办的其它工作。</dd>
                            </dl>
                          </li>
                          <li class="listf clearfix">
                         <b> ·任职要求：</b><br/>
<dl>
  <dt>1.</dt>
  <dd><b>必要条件</b></dd>
</dl>

<dl>
  <dt></dt>
  <dd>本科及以上学历，五年以上招聘工作经验； 
<br/>熟悉企业的招聘流程及各种招聘渠道，熟练掌握各种招聘技巧，能独立开展招聘工作，对人才的发现与引进、组织与人员调整有丰富的实践经验；
<br/>具备较强推动力、表达与沟通协作的能力、分析与解决问题的能力；
<br/>有IT、互联网行业招聘经验者优先。
</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd><b>优先条件</b></dd>
</dl>

<dl>
  <dt></dt>
  <dd>有猎头从业经验者优先；
<br/>有组织执行校园招聘经验者优先；
<br/>有职位相关专业论坛或社区资源者优先。

</dd>
</dl>

                          </li>
       
                        </ul>
                      </div>
        </div>
        <!--jobdetail-content结束-->
    </div>
    <!--about-right结束-->
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
