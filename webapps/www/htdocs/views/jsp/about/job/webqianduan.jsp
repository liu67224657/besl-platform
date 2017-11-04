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
        <div class="jobdetail-title"><a href="${ctx}/about/job/zhaopin">招聘职位</a>&nbsp;>页面制作</div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:jobs@staff.joyme.com"><span>立即申请</span></a></div>
           <div class="u_fans u_fansz">
          
            <ul class="zp"><h3>页面制作</h3>
              <p>·工作地点：北京</p>
              <p>·招聘人数：1人</p>
             
              <li class="listf clearfix">
               <b>·职位描述:</b>
                <dl>
                  <dt>1.</dt>
                  <dd>对UI设计的结果进行页面制作（CSS+html+JS）；</dd>
                </dl>
                <dl>
                  <dt>2.</dt>
                  <dd>为网站/客户端的页面提供持续优化方案；</dd>
                </dl>
                 <dl>
                  <dt>1.</dt>
                  <dd>配合程序进行代码的调整；</dd>
                </dl>
                 <dl>
                  <dt>2.</dt>
                  <dd>bug修复，浏览器兼容性调优；</dd>
                </dl>
              </li>
              <li class="listf clearfix">
               <b> ·职位要求：</b><br/>
               <dl>
                 <dt></dt>
                 <dd><strong>1.必要条件</strong></dd>
               </dl>
  <dl>          
  <dt></dt>
  <dd>教育水平：大专以上学历；
<br/>2、专业：美术设计、计算机专业；
<br/>3、经验：2年以上互联网交互产品设计工作经历，3年以上互联网产品使用经验
<br/>4、技能：互联网使用技巧、美工制作软件、网页设计与编程软件等
<br/>5、个人素质：善于沟通、善于交际、善于分析、团队合作
</dd>
</dl>

 <dl>
                 <dt></dt>
                 <dd><b>2.优先条件</b></dd>
               </dl>       
<dl>
  <dt>
  </dt>
  <dd>
  1、有SNS工作经验者优先考虑；</dd>
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
