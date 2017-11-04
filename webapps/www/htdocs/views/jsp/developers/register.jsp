<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>注册</title>
    <script charset="utf-8" src="/static/include/kindeditor/kindeditor-min.js"></script>
    <script charset="utf-8" src="/static/include/kindeditor/lang/zh_CN.js"></script>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/core.css?${version}"/>
    <link rel="stylesheet" type="text/css" href="${URL_LIB}/static/theme/default/css/newgames.css?${version}"/>
</head>
<body>
<!-- topbar -->
<c:import url="/tiles/gamedbheader?redr=${requestScope.browsersURL}"/>

<!-- content -->
<div class="newgames-content clearfix">
    <h2 class="newgames-title">注册邮箱帐号</h2>

    <div class="newgame-reg">
        <form action="/developers/register" method="post" id="form-submit">
            <table width="100%" class="post-newgame" style="border:none">
                <tbody>
                <tr>
                    <td>
                        <input id="inviteid" name="inviteid" value="${inviteId}" type="hidden"/>
                        <input id="reurl" name="reurl" value="${reurl}" type="hidden"/>
                    </td>
                </tr>
                <tr>
                    <th style="padding-top:6px;" valign="top" width="90">名称：</th>
                    <td>
                        <input type="text" value="${nickname}" class="inputs-1" name="nickname" id="nickname"
                               placeholder="名称将显示为在着迷的账号昵称">

                        <div class="tips" style="padding-top:4px;">建议优先填写品牌名称而非公司名称，如“着迷网”、“EA中国”之类</div>
                        </br>
                        <div class="error" style="padding-top:4px;" id="nicknameTips">
                            <c:if test="${fn:length(emailMsg)>0}">
                                <fmt:message key="${nickMsg}" bundle="${userProps}"/>
                            </c:if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="padding-top:6px;" valign="top" width="90">注册邮箱：</th>
                    <td>
                        <input type="text" value="${userid}" class="inputs-1" name="userid" id="userid"></br>
                        <div class="error" style="padding-top:4px;" id="useridTips">
                            <c:if test="${fn:length(emailMsg)>0}">
                                <fmt:message key="${emailMsg}" bundle="${userProps}"/>
                            </c:if>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="padding-top:6px;" valign="top" width="90">注册密码：</th>
                    <td>
                        <input type="password" value="" class="inputs-1" name="userpwd" id="userpwd"></br>
                        <div class="error" style="padding-top:4px;" id="userpwdTips">
                            <c:if test="${fn:length(passwordMsg)>0}">
                                <fmt:message key="${passwordMsg}" bundle="${userProps}"/>
                            </c:if>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="send-newgame-btn"><a class="submitbtn" href="javascript:void(0)"><span>注册</span></a></div>
        </form>
    </div>

    <div class="board"><img src="${URL_LIB}/static/theme/default/img/board.jpg"></div>
</div>

<!-- 页脚 -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/developer-register-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>