<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>用户下载广告应用日志查询</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css" >
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {

            $("#export").on("click", function () {
                var form = $("<form>");
                form.attr('style', 'display:none');
                form.attr('target', '');
                form.attr('method', 'post');
                form.attr('action', "/point/pointwall/tasklog/export");

                addFormNode('taskId', form);
                addFormNode('clientId', form);
                addFormNode('profileid', form);
                addFormNode('appId', form);
                addFormNode('packageName', form);
                addFormNode('appkey', form);
                addFormNode('status', form);
                addFormNode('createIp', form);
                addFormNode('platform', form);
                addFormNode('createTimeLeft', form);
                addFormNode('createTimeRight', form);
                form.submit();
            });

            $("#resetButton").on("click", function () {

                $("#myform input[type='text']").attr('value', '');
                $("#createTimeLeft").datetimebox('setValue','');
                $("#createTimeRight").datetimebox('setValue','');


            });


        });

        function addFormNode(node, form) {
            var newNode = $('<input>');
            newNode.attr('type', 'hidden');
            newNode.attr('name', node);
            newNode.attr('value', $("#myform input[name='" + node + "']").val());
            form.append(newNode);
        }

    </script>

</head>
<body>
<table>
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 我的模块管理 >> 应用下载日志查询</td>
    </tr>

    <tr>
        <td class="list_table_header_td"> 用户下载广告应用日志查询</td>
    </tr>

    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="10" class="error_msg_td">
                <fmt:message key="${errorMsg}" bundle="${error}"/>
            </td>
        </tr>
    </c:if>

    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<form id="myform" action="/point/pointwall/tasklog/list" method="post">
    <table width="600px">
        <tr>
            <td height="1" class="default_line_td" width="100px">
                task_id:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="taskId" value="${taskId}"/>
            </td>
            <td height="1" class="default_line_td" width="100px">
                设备ID:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="clientId" value="${clientId}"/>
            </td>
            <td height="1" class="default_line_td" width="100px">
                用户的profileid:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="profileid" value="${profileid}"/>
            </td>
            <td height="1" class="default_line_td" width="100px">
                app_id:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="appId" value="${appId}"/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td" width="100px">
                包名或者bundleid:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="packageName" value="${packageName}"/>
            </td>
            <td height="1" class="default_line_td" width="100px">
                appkey:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="appkey" value="${appkey}"/>
            </td>
            <td height="1" class="default_line_td" width="100px">
                status
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="status" value="${status}"/>
            </td>
            <td height="1" class="default_line_td" width="100px">
                下载设备的IP:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="createIp" value="${createIp}"/>
            </td>
        </tr>
        <tr>
            <td height="1" class="default_line_td" width="100px">
                app所在平台
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="platform" value="${platform}"/>
            </td>

            <td height="1" class="default_line_td" width="100px">
                开始时间:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" class="easyui-datetimebox" editable="false" id="createTimeLeft"
                       name="createTimeLeft" value="${createTimeLeft}"/>
            </td>
            <td height="1" class="default_line_td" width="100px">
                结束时间:
            </td>
            <td height="1" class="edit_table_defaulttitle_td">
                <input type="text" name="createTimeRight" class="easyui-datetimebox" id="createTimeRight"
                       editable="false" value="${createTimeRight}"/>
            </td>
            <td>
                <input type="submit" id="submit" value="查询"/></td>
            <td>
                <input type="button" id="resetButton" value="重置" />
            </td>
            <td>
                <input type="button" id="export" name="export" value="导出excel"/></td>
            <td>
        </tr>
    </table>
</form>

<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap align="left" width="">task_id(主键)</td>
        <td nowrap align="left" width="">设备ID</td>
        <td nowrap align="center" width="">用户的profileid</td>
        <td nowrap align="left" width="">appid</td>
        <td nowrap align="left" width="">包名或者bundleid</td>
        <td nowrap align="left" width="">appkey</td>
        <td nowrap align="center" width="">status(1:用户下载,2:积分增加)</td>
        <td nowrap align="left" width="">记录创建时间</td>
        <td nowrap align="left" width="">下载设备的IP</td>
        <td nowrap align="center" width="">用户获得的积分值</td>
        <td nowrap align="center" width=""> 所在平台(0:ios,1:android)</td>
    </tr>
    <tr>
        <td height="1" colspan="14" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach items="${list}" var="item" varStatus="st">
                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                    <td nowrap><c:out value="${item.taskId}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.clientId}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.profileid}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.appId}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.packageName}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.appkey}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.status}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.createTime}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.createIp}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.pointAmount}" escapeXml="true"/></td>
                    <td nowrap><c:out value="${item.platform}" escapeXml="true"/></td>
                </tr>
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
                <pg:pager url="/point/pointwall/tasklog/list"
                          items="${page.totalRows}" isOffset="true"
                          maxPageItems="${page.pageSize}"
                          export="offset, currentPageNumber=pageNumber" scope="request">
                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                    <pg:param name="items" value="${page.totalRows}"/>
                    <pg:param name="taskId" value="${taskId}"/>
                    <pg:param name="clientId" value="${clientId}"/>
                    <pg:param name="profileid" value="${profileid}"/>
                    <pg:param name="appId" value="${appId}"/>
                    <pg:param name="packageName" value="${packageName}"/>
                    <pg:param name="appkey" value="${appkey}"/>
                    <pg:param name="status" value="${status}"/>
                    <pg:param name="createIp" value="${createIp}"/>
                    <pg:param name="platform" value="${platform}"/>
                    <pg:param name="createTimeLeft" value="${createTimeLeft}"/>
                    <pg:param name="createTimeRight" value="${createTimeRight}"/>
                    <%@ include file="/WEB-INF/jsp/toolspgwithnewversionjquery.jsp" %>
                </pg:pager>
            </td>
        </tr>
    </c:if>
</table>


</body>
</html>