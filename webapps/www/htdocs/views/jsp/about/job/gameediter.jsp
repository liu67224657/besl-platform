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
        <div class="jobdetail-title"><a href="${ctx}/about/job/zhaopin">招聘职位</a>&nbsp;>&nbsp;游戏编辑</div>
        <div class="jobdetail-content">
            <div class="job-apply"><a class="submitbtn" href="mailto:hr@enjoyfound.com"><span>立即申请</span></a></div>
           <div class="u_fans u_fansz">
                        <ul class="zp"><h3>游戏编辑  </h3>
                          <p>·工作地点：北京 </p>
                          <p>·招聘人数：若干</p>
                          
                          <li class="listf clearfix">
                          <b>·职位描述:</b>
                            <dl>
                              <dt>1.</dt>
                              <dd>管理、联络、招募内容兼职人员，发布工作任务、审核兼职工作完成情况；</dd>
                            </dl>
                            <dl>
                              <dt>2.</dt>
                              <dd>发掘用户自创内容，对既有内容加工整理，实现再次呈现；</dd>
                            </dl>
                            <dl>
                              <dt>3.</dt>
                              <dd>维护主题杂志、推荐位日常更新；</dd>
                            </dl>
                            <dl>
                              <dt>4.</dt>
                              <dd>通过审核后台审核用户发布内容有无违规情况，并执行相应操作。</dd>
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
  <dd>大专以上学历，3年以上游戏行业媒体经验；<br/> 
熟悉互联网和新媒体传播方式，能够发现并准确把握行业热点； 
<br/>具备较强的文字功底，擅于对内容收集整理； 
<br/>注重内容细节对用户的影响力，具备团队合奏精神。
 
</dd>
</dl>
<dl>
  <dt>2.</dt>
  <dd><b>优先条件</b></dd>
</dl>

<dl>
  <dt></dt>
  <dd>有丰富的内容传播操作经验及相关合作资源。
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
