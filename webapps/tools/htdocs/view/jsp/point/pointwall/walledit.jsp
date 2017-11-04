<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>编辑积分墙</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">


        $(document).ready(function (e) {


            //积分归属类型
            <c:forEach items="${types}" var="item" >
            $("#pointKey").prepend("<option value='${item.code}'>${item.name}</option>");
            </c:forEach>

            <c:if test="${not empty  pointKey}" >
            $("#pointKey").val("${pointKey}");
            </c:if>

            //商城类型
            <c:forEach items="${shopTypes}" var="item" >
            $("#shopKey").prepend("<option value='${item.code}'>${item.name}</option>");
            </c:forEach>

            <c:if test="${not empty  shopKey}" >
            $("#shopKey").val("${shopKey}");
            </c:if>

            //添加新的app按扭
            $("#addnewapp").click(function () {
                window.location.href = "/point/pointwall/wall/appcreatepage?appkey=${appkey}";
            });


            $('#dgrules').datagrid({
                url: '/json/point/pointwall/wall/list?appkey=${appkey}',
                pagination: true,
                width: 'auto',
                height: 'auto',
                singleSelect: true,
                rownumbers: false,
                striped: true,
                idField: 'wallAppId',
                pageList: [10, 20, 30, 40],
                fitColumns: true,
                loadMsg: '数据加载中请稍后……',
                columns: [[{field: 'packageName', title: '包名或者bundleid', width: 120},
                    {field: 'appName', title: 'app的名字', width: 80},
                    {
                        field: 'appIcon', title: 'icon', width: 60,
                        formatter: function (value, row, index) {
                            return "<img src='" + row.appIcon + "' width='40' height='40' />";

                        }
                    },
                    {
                        field: 'platform', title: '所属平台', width: 50,
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
                    {field: 'displayOrder', title: '排序值', width: 50},
                    {
                        field: 'hotStatus', title: '是否热门', width: 60, formatter: function (value, rec) {
                        if (value == 1)
                            return "是";
                        else if (value == 0)
                            return "否";
                        else  return value;

                    }
                    },
                    {field: 'pointAmount', title: '下载可获得点数', width: 50},
                    {
                        field: 'status', title: '状态', width: 40, formatter: function (value, rec) {
                        if (value == 'valid')
                            return "显示";
                        else if (value == 'invalid')
                            return "不显示";
                        else if (value == 'removed')
                            return "已删除";
                        else  return value;

                    }
                    },
                    {
                        field: 'operation', title: '操作', width: 90, formatter: function (value, rec, index) {

                        var pageSize = $('#dgrules').datagrid('options')['pageSize'];
                        var result = "<a href='/point/pointwall/wall/appmodifypage?wallAppId=" + rec.wallAppId + "&appkey=${appkey}'>编辑  </a> " +
                                " &nbsp;&nbsp;<a href='javascript:;' onclick='deleteWallAppById(" + rec.wallAppId + ",&quot;" + rec.packageName + "&quot;,&quot;${appkey}&quot;)'>删除</a>";
                        if (index == 0 && pageSize > 1) {

                            result += "&nbsp;&nbsp;<a href='javascript:void(0);' onclick='toSwap(" + index + "," + (index + 1) + ");' >向下</a> ";

                        } else if (index == pageSize - 1 && pageSize > 1) {
                            result += "&nbsp;&nbsp;<a href='javascript:void(0);' onclick='toSwap(" + index + "," + (index - 1) + ");' >向上</a> ";

                        } else if (index > 0 && index < pageSize - 1 && pageSize > 2) {
                            result += "&nbsp;&nbsp;<a href='javascript:void(0);' onclick='toSwap(" + index + "," + (index + 1) + ");' >向下</a> ";
                            result += "&nbsp;&nbsp;<a href='javascript:void(0);' onclick='toSwap(" + index + "," + (index - 1) + ");' >向上</a> ";

                        }


                        return result;
                    }
                    }
                ]
                ]

            });

            //编辑积分墙的属性
            $("#submit").click(function () {


                var pointKey = $.trim($("#pointKey").val());
                var wallMoneyName = $.trim($("#wallMoneyName").val());
                var template = $("[name='template']").val();
                if (pointKey == '') {
                    alert("pointKey不能为空或者都是空格!");
                    return false;
                } else if (wallMoneyName == '') {
                    alert("平台货币名称不能为空或者都是空格!");
                    return false;
                } else if (template == '') {
                    alert("请选择一个模板!");
                    return false;
                } else {
                    document.forms[0].submit();
                }


            });

            //选择  全部 ios 或 android
            $("#toFilter").change(function () {

                if ($("#toFilter").val() == 0) {

                    $('#dgrules').datagrid('load', {
                        platform: 0
                    });

                } else if ($("#toFilter").val() == 1) {
                    $('#dgrules').datagrid('load', {
                        platform: 1
                    });
                }
                else {
                    $('#dgrules').datagrid('load', {
                        platform: 2
                    });

                }

            });


            //选择  全部 ios 或 android
            $("#toFilterInWindow").change(function () {
                $('#dg').datagrid('uncheckAll');
                if ($("#toFilterInWindow").val() == 0) {

                    $('#dg').datagrid('load', {
                        platform: 0
                    });

                } else if ($("#toFilterInWindow").val() == 1) {
                    $('#dg').datagrid('load', {
                        platform: 1
                    });
                }
                else {
                    $('#dg').datagrid('load', {
                        platform: 2
                    });

                }

            });
            $("#addsome").click(addsome);

            $("#selectsome").click(function () {
                var hotStatus = $.trim($("#hotStatus").val());
                var removeStatus = $.trim($("#removeStatus").val());


                var checkedItems = $('#dg').datagrid('getChecked');
                if (checkedItems.length < 1) {
                    alert("请至少选择一个app!");
                    return;
                }

                var rows = "";

                //appId_platform_pointAmount_hotStatus_status
                for (var i = 0; i < checkedItems.length; i++) {

                    rows += checkedItems[i].appId + "_" + checkedItems[i].platform + "_" + checkedItems[i].initScore + "_" + hotStatus + "_" + removeStatus + "@";

                }
                rows = rows.substr(0, rows.length - 1); //去掉最后一个@


                var queryData = {};
                queryData.appkey = '${appkey}';
                queryData.rows = rows;
                queryData.length = checkedItems.length;
                $.post("/json/point/pointwall/wall/batchaddtowall", queryData, function (data, textStatus) {

                    if (data.errorMsg === undefined) {
                        if ($("#toFilter").val() == 0) {

                            $('#dgrules').datagrid('load', {
                                platform: 0,
                                guid: new Date().getTime()
                            });

                        } else if ($("#toFilter").val() == 1) {
                            $('#dgrules').datagrid('load', {
                                platform: 1,
                                guid: new Date().getTime()
                            });
                        }
                        else {
                            $('#dgrules').datagrid('load', {
                                platform: 2,
                                guid: new Date().getTime()
                            });

                        }
                        alert("添加成功！");
                    } else {

                        alert("添加失败！");

                    }
                }, "json");


                $('#chooseWindow').window('close');
            });


        });

        function deleteWallAppById(wallAppId, packageName, appkey) {
            var msg = "您确定要从此积分墙(" + appkey + ")中移除包名为'" + packageName + "'的应用吗？\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/point/pointwall/wall/appdelete?wallAppId=" + wallAppId + "&appkey=" + appkey;


            }
        }
        function toSwap(indexA, indexB) {

            var rows = $("#dgrules").datagrid('getData').rows;
            var length = rows.length;


            var queryData = {};
            queryData.wallAppIdFirst = rows[indexA].wallAppId;
            queryData.displayOrderFirst = rows[indexA].displayOrder;
            queryData.wallAppIdSecond = rows[indexB].wallAppId;
            queryData.displayOrderSecond = rows[indexB].displayOrder;
            $.post("/json/point/pointwall/wall/swap", queryData, function (data, textStatus) {

                if (data.errorMsg === undefined) {
                    $("#dgrules").datagrid('reload');

                } else {

                    alert("排序失败！");

                }
            }, "json");

        }

        function addsome() {
            $('#dg').datagrid({
                url: '/json/point/pointwall/wall/toaddapp?appkey=${appkey}',
                pagination: true,
                rownumbers: false,
                singleSelect: true,
                checkOnSelect: false,
                selectOnCheck: false,
                width: 'auto',
                height: 'auto',
                striped: true,
                idField: 'appId',
                pageList: [10, 20],
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
            $('#dg').datagrid('uncheckAll');
            $('#chooseWindow').window('open');
        }


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
                    <td class="list_table_header_td">编辑积分墙</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(nameExist)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${nameExist}</td>
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
            <form action="/point/pointwall/wall/modify" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <input type="hidden" name="appkey" value="${appkey}"/>
                    <tr>
                        <td height="1" class="default_line_td">
                            积分墙appkey:
                        </td>
                        <td height="1">
                            <input type="text" size="32" readonly="readonly" disabled="disabled"
                                   value="<c:out value='${appkey}' escapeXml='true' />"/>*不可修改
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
                        <td height="1" class="default_line_td">
                            积分归属类型 :
                        </td>
                        <td height="1">
                            <select id="pointKey" name="pointKey"/>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            商城类型 :
                        </td>
                        <td height="1">
                            <select id="shopKey" name="shopKey"/>
                        <td height="1">
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            平台货币名称:
                        </td>
                        <td height="1">
                            <input type="text" name="wallMoneyName" id="wallMoneyName" size="32"
                                   value="<c:out value='${wallMoneyName}' escapeXml='true' />"/>*必填项
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            【我的】链接打开类型:
                        </td>
                        <td height="1">
                            <select name="template">
                                <option value="">请选择</option>
                                <option value="1" <c:if test="${pointwall.template=='1'}">selected </c:if>>模板1</option>
                                <option value="2" <c:if test="${pointwall.template=='2'}">selected </c:if>>模板2</option>
                                <option value="3" <c:if test="${pointwall.template=='3'}">selected </c:if>>模板3</option>
                            </select>
                            *【我的】链接会打开对应的模板
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td">
                            模板类型查看:
                        </td>
                        <td height="1">
                            <a href="/static/images/pointwall/template1.PNG" target="_blank">模板1</a>
                            <a href="/static/images/pointwall/template2.PNG" target="_blank">模板2</a>
                            <a href="/static/images/pointwall/template3.PNG" target="_blank">模板3</a>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" height="1" class="default_line_td"></td>
                    </tr>

                </table>

                <div align="center">

                    <input type="submit" id="submit" class="default_button" value="提交">
                    <input type="button" class="default_button" value="返回"
                           onclick="window.location.href = '/point/pointwall/wall/list';">

                </div>
                <table>
                    <tr>
                        <td height="1" class="default_line_td" width="200px">
                            属于该积分墙的app如下表所示:
                        </td>

                        <td height="1" class="default_line_td" width="200px">
                            选择要展示的平台:
                        </td>
                        <td>
                            <select id="toFilter" name="toFilter">
                                <option value="2">全部</option>
                                <option value="0">ios</option>
                                <option value="1">android</option>
                            </select>

                        </td>

                        <td>
                            <input type="button" id="addnewapp" name="button" value="添加一个app"/>
                            &nbsp;&nbsp;&nbsp; <input type="button" id="addsome" name="button" value="批量添加"/>
                        </td>
                    </tr>
                </table>

                <div id="chooseApps">
                    <table id="dgrules">
                    </table>

                </div>

            </form>
        </td>
    </tr>
</table>


<div id="chooseWindow" class="easyui-window" title="选择要填加的app"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false" style="width:750px;">

    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <td height="1" class="default_line_td" width="200px">
                筛选欲添加的app:
            </td>
            <td height="1">
                <select id="toFilterInWindow" name="toFilterInWindow">
                    <option value="2">全部</option>
                    <option value="0">ios</option>
                    <option value="1">android</option>
                </select>
            </td>
            <td height="1">

            </td>
        </tr>
        <tr>
            <td height="10" colspan="3">
                <hr/>
            </td>

        </tr>

        <tr>
            <td height="1" class="default_line_td">
                是否热门:
            </td>
            <td height="1">
                <select name="hotStatus" id="hotStatus">
                    <option value="0">非热门</option>
                    <option value="1">热门</option>
                </select>*必填项
            </td>
            <td height="1">
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                status:
            </td>
            <td height="1">
                <select name="status" id="removeStatus">
                    <option value="valid">显示</option>
                    <option value="invalid">不显示</option>
                    <option value="removed">已删除</option>
                </select>*必填项
            </td>
            <td height="1">
            </td>
        </tr>
    </table>

    <table id="dg" data-options="
              rownumbers:false,
              autoRowHeight:true,
              pagination:true">
    </table>
    <div style="text-align:center">
        <div class=" clear mt20" style=" margin:0 auto;"><input type="button"
                                                                onclick="$('#chooseWindow').window('close');"
                                                                value="取消"/> <input type="button" value="选择"
                                                                                    id="selectsome"/></div>
    </div>
</div>


</body>
</html>