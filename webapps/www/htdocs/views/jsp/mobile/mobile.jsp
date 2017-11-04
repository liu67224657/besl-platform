<%--
  Created by IntelliJ IDEA.
  User: zhaoxin
  Date: 12-7-2
  Time: 下午2:57
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>chinajoy手机客户端下载 - 着迷网Joyme.com - 游戏爱好者的乐园 </title>   
   <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/css/cj.css?${version}" rel="stylesheet" type="text/css"/> 
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<div class="cj_wrap">
  <div class="cj_con">
    <div class="cj01"></div>
    <div class="cj02"></div>
    <div class="cj03"></div>
    <div class="cj04"></div>
    <div class="cj05"></div>
    <div class="cj06">
         <a href="http://www.joyme.com/click/xxzf2m-xxzfgg-xxzf27" class="iphone">苹果版下载</a>
          <a href="http://www.joyme.com/click/xxzf2m-xxzfgg-xxzf2n" class="android">安卓版下载</a>
    </div>
    <div class="cj07">
    <img src="${URL_LIB}/static/img/cj/cj_00.jpg" width="988" height="55" border="0" usemap="#Map"  alt="下载到PC安装" title="下载到PC安装"/>
    <map name="Map" id="Map">
      <area shape="rect" coords="474,4,632,31" href="http://www.joyme.com/click/xxzf2m-xxzfgg-xxzfgq" />
    </map>
    </div>
    <div class="cj08"></div>
    <div class="cj09"></div>
    <div class="cj10"></div>
    <div class="cj11"></div>
    <div class="cj12"></div>
    <div class="cj13"></div>
    <div class="cj14"></div>
  </div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<!--分析代码放在页尾-->

<script src="http://lib.joyme.com/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main(66);
</script>
<script type="text/javascript" src="http://lib.joyme.com/static/js/common/google-statistics-noseajs.js"></script>
<script type="text/javascript" src="http://lib.joyme.com/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>