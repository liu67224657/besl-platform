<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>积分墙app信息管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script type="text/javascript">
        var originalAppkeys = "";
        $(document).ready(function (e) {
            $.ajaxSetup({
                async: false
            });


            $("#firstTable").datagrid({
                url: '/json/point/pointwall/wall/getwalls',
                pagination: true,
                rownumbers: false,
                singleSelect: false,
                checkOnSelect: false,
                selectOnCheck: false,
                width: 'auto',
                height: 'auto',
                striped: true,
                idField: 'appkey',
                pageList: [10, 20],
                fitColumns: true,
                loadMsg: '数据加载中请稍后……',
                columns: [[
                    {field: 'appkey', title: 'appkey', width: 100},
                    {field: 'appKeyName', title: '积分墙所属app的名称', width: 100},
                    {field: 'appNum', title: '当前含有app的数量', width: 100},
                    {field: 'ck', checkbox: true}
                ]],
                onLoadSuccess: function (data) {
                    if (data) {
                        //  $("#firstTable").datagrid('uncheckAll');
                        $.each(data.rows, function (index, item) {
                            if (item.flag == 1) {
                                $('#firstTable').datagrid('checkRow', index);
                            }
                            if (originalAppkeys.indexOf(item.appkey) == -1) {

                                //这个判断用以解决一种莫名其妙的情况，正常 来说这是不可能的，非空一定会以逗号结尾的。
                                if (originalAppkeys.length > 0 && originalAppkeys.charAt(originalAppkeys.length - 1) != ',') {
                                    originalAppkeys += ',';
                                }
                                originalAppkeys += item.appkey + ",";

                            }
                        });

                    }

                }
            });


            $("#sortPolicy").click(function () {
                if ($("#sortPolicy").val() == "manual") {
                    $("#displayOrder").attr("type", "text");
                } else {

                    $("#displayOrder").attr("type", "hidden");

                }


            });


            $(".zxx_text_overflow").each(function () {
                var maxwidth = 23;
                if ($(this).text().length > maxwidth) {
                    $(this).attr("title", $(this).text());
                    $(this).text($(this).text().substring(0, maxwidth));
                    $(this).html($(this).html() + "...");
                }
            });


            $("#tosave").click(function () {
                var sortPolicy = $("#sortPolicy").val();
                var displayOrder = $.trim($("#displayOrder").val());
                var pointAmount = $.trim($("#pointAmount").val());
                var removeStatus = $.trim($("#removeStatus").val());
                var hotStatus = $.trim($("#hotStatus").val());
                var appIdInWindow = $.trim($("#appIdInWindow").val());
                var platformInWindow = $.trim($("#platformInWindow").val());
                var updatePolicy = $.trim($("#updatePolicy").val());

                if (sortPolicy == 'manual') {

                    if (displayOrder == '' || displayOrder == 0 || isNaN(displayOrder)) {
                        alert("排序值不能为空或者为0,且必须是数字!");
                        return false;
                    }
                    if (displayOrder > 2147483647) {
                        alert("排序值必须小于2147483647!");
                        return false;

                    }

                }

                if (pointAmount == '' || pointAmount == 0 || isNaN(pointAmount)) {
                    alert("下载可获得点数不能为空或者为0,且必须是数字!");
                    return false;
                }


                if (pointAmount > 2147483647) {
                    alert("下载可获得点数必须小于2147483647!");
                    return false;

                }


                var selectedAppkeys = "";
                var checkedItems = $('#firstTable').datagrid('getChecked');


                if (checkedItems.length > 0) {
                    $.each(checkedItems, function (index, item) {
                        selectedAppkeys += item.appkey + ",";

                    });

                    selectedAppkeys = selectedAppkeys.substr(0, selectedAppkeys.length - 1);

                }

                pointAmount = parseInt(pointAmount);
                displayOrder = parseInt(displayOrder);


                if (originalAppkeys.length <= 0) {
                    alert("请先在广告联盟管理中添加积分墙,然后再使用此功能.")
                    return false;
                }

                if (originalAppkeys.charAt(originalAppkeys.length - 1) == ",") {
                    originalAppkeys = originalAppkeys.substr(0, originalAppkeys.length - 1);

                }

                var queryData = new Object();
                queryData.selectedAppkeys = selectedAppkeys;
                queryData.originalAppkeys = originalAppkeys;
                queryData.sortPolicy = sortPolicy;
                queryData.displayOrder = displayOrder;
                queryData.hotStatus = hotStatus;
                queryData.pointAmount = pointAmount;
                queryData.status = removeStatus;
                queryData.appId = appIdInWindow;
                queryData.platform = platformInWindow;
                queryData.updatePolicy = updatePolicy;
                $.post("/json/point/pointwall/wall/updateappstowalls", queryData, function (data, textStatus) {

                    if (data.operationStatus == 1) {
                        alert("操作成功!");
                    }
                    else {
                        alert("操作失败!");
                    }
                }, "json");

                $('#chooseWindow').window('close');

                $("#firstTable").datagrid('clearChecked');   //防止影响接下来编辑另一个app
            });


        });

        function toShowWindow(appid, packageName, platform, initScore) {
            var title = "请选择将appId为" + appid + "包名为\"" + packageName + "\"的应用添加到的积分墙(可多选,反选会从该积分墙删除)";


            $("#chooseWindow").window({title: title});
            $("#appIdInWindow").val(appid);
            $("#platformInWindow").val(platform);
            $("#pointAmount").val(initScore);

            originalAppkeys = "";  //每当编辑一个新的app时，将含有所有appkeys的结果重置。

            //   url: '/json/point/pointwall/wall/getwalls?appId=' + appid+'&guid='+new Date().getTime(),


            $('#firstTable').datagrid('uncheckAll');


            //下次打开停留在第一页
            $('#firstTable').datagrid('load', {
                appId: appid,
                guid: new Date().getTime()
            });
            $('#chooseWindow').window('open');
        }


        function deleteAppByAppId(appId) {
            var msg = "您确定要删除app_id为" + appId + "的app吗？\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/point/pointwall/app/delete?appId=" + appId;


            }
        }


    </script>

</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 我的模块管理 >> 热门应用管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"> app信息管理</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${fn:length(errorFK)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                            <c:out value="${errorFK}" escapeXml="true"/>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td>
                        <form action="/point/pointwall/app/list" method="post">
                            <table width="800px">
                                <tr>
                                    <td height="1" class="default_line_td" >
                                        应用的包名或者bundleid:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td" width="50px">
                                        <input type="text" name="packageName" value="${packageName}"/>
                                    </td>
                                    <td height="1" class="default_line_td">
                                        所属广告主的名字:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td" width="50px">
                                        <input type="text" name="sponsorName" value="${sponsorName}"/>
                                    </td>
                                    <td height="1" class=>
                                    </td>
                                    <td height="1" class="default_line_td" >
                                        应用名称:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td" width="50px">
                                        <input type="text" name="appSearchName" value="${appSearchName}"/>
                                    </td>
                                    <td height="1" class=>
                                    </td>
                                    <td width="50px">
                                        <input type="submit" name="button" value="查询"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                    <td>

                        <table>
                            <tr>
                                <td>
                                    <form method="post" id="create_app_form"
                                          action="/point/pointwall/app/createpage">

                                        <input type="submit" name="button" value="添加新的app"/>
                                    </form>
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="left" width="">表的主键</td>
                    <td nowrap align="left" width="">包名或者bundleid</td>
                    <td nowrap align="left" width="">应用名称</td>
                    <td nowrap align="left" width="">app的版本</td>
                    <td nowrap align="center" width="">所在平台</td>
                    <td nowrap align="left" width="">icon</td>
                    <td nowrap align="left" width="">描述</td>
                    <td nowrap align="left" width="">所属广告主</td>
                    <td align="center" width="60">下载的URL</td>
                    <td align="left" width="60">广告主的url</td>
                    <td nowrap align="left" width="">可以获得的积分值</td>
                    <td nowrap align="left" width="">创建时间</td>
                    <td nowrap align="center" width="180">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="14" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <c:if test="${item.removeStatus=='valid'}">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap><c:out value="${item.appId}" escapeXml="true"/></td>
                                    <td class="zxx_text_overflow"><c:out value="${item.packageName}"
                                                                         escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.appName}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.verName}" escapeXml="true"/></td>
                                    <td nowrap>
                                        <c:choose>
                                            <c:when test="${item.platform==0}">
                                                ios
                                            </c:when>
                                            <c:when test="${item.platform==1}">
                                                android
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${item.platform}" escapeXml="true"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td nowrap><img src="${item.appIcon}" width="50" height="50"></td>
                                    <td nowrap><c:out value="${item.appDesc}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.sponsorName}" escapeXml="true"/></td>
                                    <td class="zxx_text_overflow"><c:out value="${item.downloadUrl}"
                                                                         escapeXml="true"/></td>
                                    <td class="zxx_text_overflow"><c:out value="${item.reportUrl}"
                                                                         escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.initScore}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.createTime}" escapeXml="true"/></td>
                                    <td nowrap width="180">
                                        <a href="/point/pointwall/app/modifypage?appId=${item.appId}">编辑</a>
                                        <a href='javascript:;' onclick='deleteAppByAppId(${item.appId});'>删除</a>
                                        &nbsp;
                                        <a href="javascript:;"
                                           onclick="toShowWindow('${item.appId}','${item.packageName}','${item.platform}','${item.initScore}');">批量添加到积分墙</a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="14" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="14" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="14" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/point/pointwall/app/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="packageName" value="${packageName}"/>
                                <pg:param name="sponsorName" value="${sponsorName}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>

<div id="chooseWindow" class="easyui-window" title="请选择要将添加到的积分墙(可多选,反选会从该积分墙删除)"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false"
     style="width: 684px; top: 171px; left: 302px">

    <table width="100%" border="0" cellspacing="1" cellpadding="0">
        <tr>
            <input type="hidden" id="appIdInWindow" value="0"/>
            <input type="hidden" id="platformInWindow" value="0"/>
            <td height="1" class="default_line_td">
                排序值:
            </td>
            <td height="1">
                <select name="sortPolicy" id="sortPolicy">
                    <option value="auto">自动(当前时间）</option>
                    <option value="manual">手动</option>
                </select>
                <input type="hidden" size="16" maxlength="16" id="displayOrder" name="displayOrder"
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
                <select name="hotStatus" id="hotStatus">
                    <option value="0">非热门</option>
                    <option value="1">热门</option>
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
                <input type="text" name="pointAmount" id="pointAmount" size="16" maxlength="16" value="0"/>*必填项
            </td>
            <td height="1" class=>
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
            <td height="1" class=>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td">
                以上设置对于已经含有此app的积分墙:
            </td>
            <td height="1">
                <select name="updatePolicy" id="updatePolicy">
                    <option value="unchanged">无变化</option>
                    <option value="change">更新</option>
                </select>*必填项
            </td>
            <td height="1" class=>
            </td>
        </tr>
    </table>
    <table id="firstTable" data-options="rownumbers:false,autoRowHeight:true,pagination:true">
    </table>
    <div style="text-align:center">
        <div class=" clear mt20" style=" margin:0 auto;">
            <input type="button" onclick="$('#chooseWindow').window('close');" value="取消"/>
            <input type="button" value="选择" id="tosave"/>
        </div>
    </div>
</div>


</body>
</html>