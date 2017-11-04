<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8" %>
    <%@ taglib prefix="s" uri="/struts-tags" %>
    <%
        //remove cache
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <link href="/static/include/css/style_min.css" rel="stylesheet" type="text/css">
    <script src="/static/include/js/validation_cn.js" type="text/javascript"></script>
    <script src="/static/include/js/prototype.js" type="text/javascript"></script>
    <script src="/static/include/js/effects.js" type="text/javascript"></script>

    <style>
        body td {
            color: #333;
            font-family: Arial, Helvetica, sans-serif;
            font-size: 10pt;
        }

        .validation-advice {
            margin: 5px 0;
            padding: 5px;
            background-color: #DFF3FB;
            color: #FF3300;
            font-weight: bold;
        }
    </style>
    <script type="text/javascript">
        function window_onload() {
            //表单的第一个元素获得焦点
            Form.focusFirstElement("form1");
        }

        function checkNum(){
           var re = /^[1-9]+[0-9]*]*$/;
           //判断正整数 /^[0-9]+.?[0-9]*$/
            var input = document.getElementById("parentid");
            var input2 = document.getElementById("orderfield");
           if (!re.test(input.value) || !re.test(input2.value))
           {
                alert("[父资源]和[排序字段]的输入类型必须为整数");
               return false;
            } else{
               return true;
           }
        }
    </script>
    <title>后台数据管理、添加新管理员</title>

    <script language="JavaScript" type="text/JavaScript">
        function back() {
            window.location.href = "/privilege/res/reslist";
        }
        function window_onload() {
            //表单的第一个元素获得焦点
            Form.focusFirstElement("form1");
        }
    </script>
</head>

<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">系统管理 >> 权限管理 >> 操作权限管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加资源</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <form name="form1" action="/privilege/res/saverespage" method="POST">
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">资源名称：</td>
                        <td class="edit_table_value_td">
                            <input name="rsname" type="text" class="default_input_singleline" id="rsname"
                                   value="${entity.rsname}"
                                   size="40" maxlength="64">
                            <c:if test="${errorMsgMap.containsKey('rsname')}">
                                <fmt:message key="${errorMsgMap['rsname']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>

                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">资源地址：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="rsurl" type="text" class="default_input_singleline" size="40"
                                   maxlength="200"
                                   value="${entity.rsurl}"
                                   id="rsurl" cssClass="validate-url" title="填写资源的链接地址">

                            <c:if test="${errorMsgMap.containsKey('rsurl.duplication')}">
                                <fmt:message key="${errorMsgMap['rsurl.duplication']}" bundle="${error}"></fmt:message>
                            </c:if>
                            <c:if test="${errorMsgMap.containsKey('rsurl.null')}">
                                <fmt:message key="${errorMsgMap['rsurl.null']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">资源级别：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="rslevel" id="rslevel" title="资源级别不能为空">
                                <option value="" selected="true">--请选择--</option>
                                <option value="1" <c:if
                                        test="${entity.rslevel.code==1}"> selected="true"</c:if>>一级
                                </option>
                                <option value="2" <c:if
                                        test="${entity.rslevel.code==2}"> selected="true"</c:if>>二级
                                </option>
                                <option value="3" <c:if
                                        test="${entity.rslevel.code==3}"> selected="true"</c:if>>三级
                                </option>
                                <option value="4" <c:if
                                        test="${entity.rslevel.code==4}"> selected="true"</c:if>>四级
                                </option>
                            </select>
                            <c:if test="${errorMsgMap.containsKey('rslevel')}">
                                <fmt:message key="${errorMsgMap['rslevel']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">资源状态：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="status" id="status" title="资源状态不能为空">
                                <option value="">--请选择${entity.status.code}--</option>
                                <option value="y" <c:if test="${entity.status.code=='y'}">selected="true"</c:if>>启用
                                </option>
                                <option value="n" <c:if test="${entity.status.code=='n'}">selected="true"</c:if>>停用
                                </option>
                            </select>
                            <c:if test="${errorMsgMap.containsKey('status')}">
                                <fmt:message key="${errorMsgMap['status']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">父资源：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="parentid" type="text" class="default_input_singleline" size="40"
                                   maxlength="64"
                                   value="${entity.parentid}"
                                   id="parentid" title="填写父资源的ID数值类型">
                            <c:if test="${errorMsgMap.containsKey('parentid')}">
                                <fmt:message key="${errorMsgMap['parentid']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">排序字段：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="orderfield" type="text" class="default_input_singleline" size="40"
                                   maxlength="64"
                                   value="${entity.orderfield}" class="required"
                                   id="orderfield" title="填写数值类型的排序字段">
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">图标地址：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="iconurl" type="text" class="default_input_singleline" size="40"
                                   maxlength="64"
                                   value="${entity.iconurl}"
                                   id="iconurl" title="填写字符串类型">
                        </td>
                    </tr>

                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">资源类型：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="rstype" id="rstype" title="资源类型不能为空">
                                <option value="">--请选择--</option>
                                <option value="1" <c:if
                                        test="${entity.rstype.code=='1'}"> selected="true" </c:if>>菜单
                                </option>
                                <option value="2" <c:if
                                        test="${entity.rstype.code=='2'}"> selected="true" </c:if>>动作
                                </option>
                            </select>
                            <c:if test="${errorMsgMap.containsKey('rstype')}">
                                <fmt:message key="${errorMsgMap['rstype']}" bundle="${error}"></fmt:message>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">是否菜单：</td>
                        <td nowrap class="edit_table_value_td">
                            <select name="ismenu" id="ismenu">
                                <option value="">--请选择--</option>
                                <option value="y" <c:if test="${entity.ismenu.code=='y'}">selected="true"</c:if>>是
                                </option>
                                <option value="n" <c:if test="${entity.ismenu.code=='n'}">selected="true"</c:if>>不是
                                </option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="120" align="right" class="edit_table_defaulttitle_td">资源描述：</td>
                        <td nowrap class="edit_table_value_td">
                            <input name="description" type="text" class="default_input_singleline" size="40"
                                   maxlength="64"
                                   value="${entity.description}"
                                   id="description">
                        </td>
                    </tr>

                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr align="center">
                        <td colspan="4">
                            <input name="Submit" type="submit" class="default_button" value="提交" onclick="return checkNum();">
                            <input name="Reset" type="reset" class="default_button" value="返回" onclick="back();">
                        </td>
                    </tr>
                </form>
            </table>

        </td>
    </tr>
</table>
</body>
</html>