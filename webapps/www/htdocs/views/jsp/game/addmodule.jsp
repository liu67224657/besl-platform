<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="KeyWords"
          content="${game.resourceName}、${game.resourceName}下载、${game.resourceName}攻略、${game.resourceName}论坛">
    <meta name="Description" content="${game.resourceName}着迷游戏专区、${game.resourceName}游戏下载、攻略秘籍、问答、小组"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${game.resourceName}游戏百科及下载、攻略_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="sqcontent">
	<div class="sqt"></div>
    <div class="sqc">

        <h2 class="editor-baike-title">
            <a class="returnback" href="${URL_WWW}/game/${game.gameCode}">返回${game.resourceName}条目页&gt;&gt;</a>添加栏目</h2>

        <div class="editor-baike-bar">
             <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/headimagepage" >头图管理</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/descpage">编辑游戏介绍</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/recentpage" >编辑最近更新</a>--%>
            <%--<a href="${URL_WWW}/game/${game.gameCode}/edit/menupage" >导航设置</a>--%>
            <a href="javascript:void(0);" class="current">添加新栏目</a>
        </div>
        <div class="clear"></div>
        <form action="${URL_WWW}/game/${game.gameCode}/edit/addmodule" method="post" id="form_edit">
        <div class="editor-baike-box clearfix">
        	<div class="editor-baike-box-form">
            	<dl>
                	<dt style="padding-top:4px">栏&nbsp;目&nbsp;名&nbsp;称：</dt>
                    <dd>
                    	<input id="txt_name" name="mname" value="${moduleName}" type="text" class="input1">
                        <c:choose>
                        <c:when test="${fn:length(nameError)>0}">
                             <p id="name_msg" style="color:#fd5d51"><fmt:message key="${nameError}" bundle="${userProps}"/> </p>
                        </c:when>
                        <c:otherwise>
                             <p id="name_msg">只允许使用汉字、数字、“-”、“_”最长不超过10个汉字</p>
                        </c:otherwise>
                        </c:choose>
                    </dd>
                </dl>
                <dl>
                	<dt>栏&nbsp;目&nbsp;类&nbsp;型：</dt>
                    <dd>
                    	<select name="displaytype">
                            <c:forEach var="type" items="${displayTypeList}">
                                <option value="${type.code}" <c:if test="${fn:length(displayTypeCode)>0 && displayTypeCode==type.code}">selected</c:if> ><fmt:message key="gamemodule.displaytype.${type.code}.code" bundle="${userProps}"/></option>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                	<dt>内&nbsp;容&nbsp;类&nbsp;型：</dt>
                    <dd>
                    	<select name="posttype">
                        	<c:forEach var="type" items="${postTypeList}">
                                <c:if test="${type.code!='handbook'}">
                                <option value="${type.code}" <c:if test="${fn:length(postTypeCode)>0 && postTypeCode==type.code}">selected</c:if> ><fmt:message key="gamemodule.posttype.${type.code}.code" bundle="${userProps}"/></option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>
                <dl>
                	<dt>显&nbsp;示&nbsp;条&nbsp;数：</dt>
                    <dd class="dd1">
                    	<label><input type="radio" name="displaycount" value="2"> 2条</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="radio" name="displaycount" value="4"> 4条</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label><input type="radio" name="displaycount" value="8" checked="true"> 8条</label>
                    </dd>
                </dl>
            </div>
            <div class="editor-baike-box-shili">
            	<%--<h2>栏目类型预览</h2>--%>
                <%--&lt;%&ndash;todo&ndash;%&gt;--%>
            	<%--<img src="img/ff.jpg">--%>
            </div>
        </div>
        <div class="editor-baike-btn">
        	<input type="submit" class="save_btn" value=""/>
        </div>
        </form>
    </div>
    <div class="sqb"></div>
</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    var gameCode = '${gameCode}';
    seajs.use('${URL_LIB}/static/js/init/game-editmodule-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>