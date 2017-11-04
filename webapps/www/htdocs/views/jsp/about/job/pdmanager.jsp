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
        <div class="jobdetail-title"><a href="${ctx}/about/job/zhaopin">招聘职位</a>&nbsp;>&nbsp;产品经理</div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:hr@enjoyfound.com"><span>立即申请</span></a></div>
           <div class="u_fans u_fansz">
            <ul class="zp">
            <h3>产品经理</h3>
              <p>·工作地点：北京 </p>
              <p>·招聘人数：1人</p>
             
              <li class="listf clearfix">
               <b>·职位描述:</b>
                <dl>
                  <dt>1.</dt>
                  <dd>互联网社区产品的功能设计，设计文档撰写；与开发、视觉等部门，进行所负责产品功能的制作跟进；</dd>
                </dl>
                <dl>
                  <dt>2.</dt>
                  <dd>监控各个产品模块的运营状况，根据反馈和团队制定解决方案和相关优化措施；</dd>
                </dl>
                <dl>
                  <dt>3.</dt>
                  <dd>活用数据、调查等手段，分析研究自身产品和指定产品的用户行为; </dd>
                </dl>
                 <dl>
                  <dt>4.</dt>
                  <dd>接受互联网产品设计相关的培训. </dd>
                </dl>
                
              </li>
              <li class="listf clearfix">
             <b> ·职位要求：</b><br/>
<dl>
  <dt>1.</dt>
  <dd>本科以上，专业不限，经验不限，有在互联网行业大展拳脚之理想；</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd>了解互联网，常用1-2款SNS或微博类社交网站，能够说清这些社交网站的优缺点，玩过游戏超过15款（平台、年代、种类不限）
</dd>
</dl>
<dl>
  <dt>3.</dt>
  <dd>熟悉word、excel或同等功能的工具软件；</dd>
</dl>
<dl>
  <dt>4.</dt>
  <dd>学习性强、行动力强、沟通顺畅、尊重团队，有独立思考和主动推动能力；</dd>
</dl>
<dl>
  <dt>5.</dt>
  <dd> 有一定互联网产品从业经验优先，计算机、心理学、新闻传播相关专业毕业者优先。</dd>
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
