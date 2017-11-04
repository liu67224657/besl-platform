<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>在积分墙'${wallName}'中填加新的app</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {

            $("#addnewapp").click(function () {
                $('#dgrules').datagrid({
                    url: '/json/point/pointwall/wall/toaddapp?appkey=${appkey}',
                    pagination: true,
                    rownumbers: false,
                    singleSelect: true,
                    width: 'auto',
                    height: 'auto',
                    striped: true,
                    idField: 'appId',
                    pageList: [10,20,30,40],
                    fitColumns: true,
                    loadMsg: '数据加载中请稍后……',
                    columns: [[
                        {field: 'appId', title: 'app的ID', width: 60},
                        {field: 'packageName', title: '包名或bundleid', width: 100},
                        {field: 'appName', title: 'appName', width: 100},
                        {field: 'verName', title: '版本', width: 60},
                        {
                            field: 'appIcon', title: 'icon', width: 60,
                            formatter: function (value, row, index) {
                                return "<img src=" + row.appIcon + " width='40' height='40>'";

                            }
                        },
                        {field: 'sponsorName', title: '广告主名字', width: 120},
                        {
                            field: 'platform', title: '所属平台', width: 100,
                            formatter: function (value, row, index) {
                                if (value == 0) {
                                    return "ios"
                                } else if (value == 1) {
                                    return "android";
                                }
                                else
                                    return row.platform;
                            }
                        },
                        {field: 'initScore', title: '可获得积分', width: 80},
                        {field: 'ck', checkbox: true}
                    ]]
                });

                $('#chooseWindow').window('open');
            });

            $("#selectone").click(function () {

                var item = $('#dgrules').datagrid('getSelected');
                $("#form_submit input[name='appId']").val(item.appId);
                $("#form_submit input[name='packageName']").val(item.packageName);

                $("#form_submit input[name='appName']").val(item.appName);

                $("#platform").prop('value', item.platform);
                $("#pointAmount").val(item.initScore);

                $("#form_submit input[name='platform']").val(item.platform);
                $('#chooseWindow').window('close');
            });

            $("#submit").click(function () {

                var appId = $("#form_submit input[name='appId']").val();
                var displayOrder = $.trim($("#displayOrder").val());

                var pointAmount = $.trim($("#pointAmount").val());


                if (appId == "") {
                    alert("请选择一个app,然后再点击提交!");
                    return false;
                } else if (displayOrder == '' || displayOrder == 0 || isNaN(displayOrder)) {
                    alert("排序值不能为空或者为0,且必须是数字!");
                    return false;
                } else if (pointAmount == '' || pointAmount == 0 || isNaN(pointAmount)) {
                    alert("下载可获得点数不能为空或者为0,且必须是数字!");
                    return false;
                } else {
                    $("#form_submit").submit();
                }
            });

        });


    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 我的模块管理 >> 积分墙管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">在积分墙'${wallName}'中添加新的app</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(nameExist)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${nameExist}</td>
                    </tr>
                </c:if>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">
                            <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                        </td>
                    </tr>
                </c:if>

                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/point/pointwall/wall/appcreate" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <input type="hidden" name="appId" value=""/>
                    <input type="hidden" name="appkey" value="${appkey}"/>
                    <input type="hidden" name="platform" value=""/>
                    <tr>
                        <td height="1" class="default_line_td">
                            所在积分墙的appkey:
                        </td>
                        <td height="1">
                            <input type="text" size="32" readonly="readonly" disabled="disabled"
                                   value="<c:out value='${appkey}' escapeXml='true' />"/> *不可修改
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            含用积分墙的应用名称:
                        </td>
                        <td height="1">
                            <input type="text" size="32" readonly="readonly" disabled="disabled"
                                   value="<c:out value='${appKeyName}' escapeXml='true' />"/> *不可修改
                        </td>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="button" id="addnewapp" name="button" value="选择要添加的app"/>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            包名或者bundleid:
                        </td>
                        <td height="1">
                            <input type="text" name="packageName" size="32" readonly="readonly" disabled="disabled"/>*自动填写

                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            app的名称:
                        </td>
                        <td height="1">
                            <input type="text" name="appName" size="32" readonly="readonly" disabled="disabled"/>*自动填写
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            所属平台(平台 0--ios,1--android):
                        </td>
                        <td height="1">
                            <select id="platform" readonly="readonly" disabled="disabled">
                                <option value="0">ios</option>
                                <option value="1">android</option>
                            </select> *自动填写
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            排序值:
                        </td>
                        <td height="1">
                            <input type="text" size="32" maxlength="20" id="displayOrder" name="displayOrder"
                                   value="0"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            是否热门:
                        </td>
                        <td height="1">
                            <select name="hotStatus">
                                <option value="1">热门</option>
                                <option value="0">非热门</option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            下载可获得点数:
                        </td>
                        <td height="1">
                            <input type="text" name="pointAmount" id="pointAmount" size="32" value="0"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            status:
                        </td>
                        <td height="1">
                            <select name="status">
                                <option value="valid">显示</option>
                                <option value="invalid">不显示</option>
                                <option value="removed">已删除</option>
                            </select>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" id="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>

<div id="chooseWindow" class="easyui-window" title="选择要填加的app"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false" style="width:750px;">

    <table id="dgrules" data-options="
              rownumbers:false,
              autoRowHeight:true,
              pagination:true">
    </table>
    <div style="text-align:center">
        <div class=" clear mt20" style=" margin:0 auto;"><input type="button"
                                                                onclick="$('#chooseWindow').window('close');"
                                                                value="取消"/> <input type="button" value="选择"
                                                                                    id="selectone" /></div>
    </div>
</div>


</body>
</html>