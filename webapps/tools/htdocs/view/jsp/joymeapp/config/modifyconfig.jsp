<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加配置</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷APP >> APP配置列表</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加配置</td>
                </tr>
            </table>
            <form action="/joymeapp/config/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td">
                            <input type="hidden" name="configid" value="${config.configId}"/>
                            <input type="hidden" name="appkey" value="${config.appKey}"/>
                            <input type="hidden" name="platform" value="${config.platform.code}"/>
                            <input type="hidden" name="version" value="${config.version}"/>
                            <input type="hidden" name="channel" value="${config.channel}"/>
                            <input type="hidden" name="enterprise" value="${config.enterpriseType.code}"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            应用:
                        </td>
                        <td height="1">
                             ${app.appName}(${app.appId})
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            平台:
                        </td>
                        <td height="1">
                            <fmt:message key="joymeapp.platform.${config.platform.code}" bundle="${def}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            版本:
                        </td>
                        <td>
                            ${config.version}
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            渠道:
                        </td>
                        <td>
                            <fmt:message key="joymeapp.channel.type.${config.channel}" bundle="${def}"/>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            是否是企业版:
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${config.enterpriseType.code == 1}">否</c:when>
                                <c:when test="${config.enterpriseType.code == 2}">是</c:when>
                                <c:otherwise></c:otherwise>
                            </c:choose>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            审核期间广告:
                        </td>
                        <td>
                            <input id="input_ad" type="text" name="defad_url" value="${config.info.defad_url}" size="50"/>
                            <br/>
                            <span style="color: #ff0000">审核期间广告不为空，审核通过后，需手动改为空</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            摇一摇开关:
                        </td>
                        <td>
                            <select name="shake_open">
                                <option value="false" <c:if test="${config.info.shake_open == 'false'}">selected="selected"</c:if>>关</option>
                                <option value="true" <c:if test="${config.info.shake_open == 'true'}">selected="selected"</c:if>>开</option>
                            </select><span style="color: #ff0000">控制"摇一摇"是否出现</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            礼包中心开关:
                        </td>
                        <td>
                            <select name="gift_open">
                                <option value="false" <c:if test="${config.info.gift_open == 'false'}">selected="selected"</c:if>>关</option>
                                <option value="true" <c:if test="${config.info.gift_open == 'true'}">selected="selected"</c:if>>开</option>
                            </select><span style="color: #ff0000">控制"礼包中心"是否打开</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            玩霸小红点轮询时间:
                        </td>
                        <td>
                            <input id="reddot_interval" type="text" name="reddot_interval" value="${config.info.reddot_interval}" size="50"/>
                            <br/>
                            <span style="color: #ff0000">默认：30S 单位秒</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td" width="100">
                            玩霸摇一摇采用形式:
                        </td>
                        <td>
                            <c:if test="${config.info.shake_version!=1}">
                                <select name="shake_version">
                                    <option value="0" selected="selected">native</option>
                                    <option value="1">wap</option>
                                </select>
                            </c:if>
                            <c:if test="${config.info.shake_version==1}">
                                <select name="shake_version">
                                    <option value="0">native</option>
                                    <option value="1" selected="selected">wap</option>
                                </select>
                            </c:if>
                            <span style="color: #ff0000">默认：natvie</span>
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <p:privilege name="/joymeapp/config/secret">
                        <tr>
                            <td height="1" class="default_line_td">
                                秘钥:
                            </td>
                            <td height="1" class="edit_table_defaulttitle_td">
                                <input id="ios" type="text" name="appsecret" size="40" value="${config.appSecret}"/>
                                <a href="/joymeapp/app/modifypage?appid=${app.appId}">修改秘钥</a>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </p:privilege>
                    <tr>
                        <td height="1" class="default_line_td">
                            上传的bucekt:
                        </td>
                        <td height="1" class="edit_table_defaulttitle_td">
                            <input id="bucket" type="text" name="bucket" size="40" value="${config.bucket}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                    <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>
                </table>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascript:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>