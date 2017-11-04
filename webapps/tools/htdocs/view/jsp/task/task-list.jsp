<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>后台数据管理、评论审核列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(function () {
            window.allFilter = '${allFilter}';
            window.dateFilter = '${dateFilter}';
            window.dateEndFilter = '${dateEndFilter}';
            window.typeFilter='${typeFilter}';

            var time = new Date();
            if (dateFilter == '') {
                dateFilter = time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate();
            }

            if (dateEndFilter == '') {
                dateEndFilter = time.getFullYear() + '-' + (time.getMonth() + 1) + '-' + time.getDate();
            }

            $("#typeFilter").val(typeFilter);
            $("#dateFilter").datebox("setValue", dateFilter);
            $("#dateEndFilter").datebox("setValue", dateEndFilter);

            $("#allFilter").on("change", function () {
                if ($("#allFilter").prop("checked")) {
                    $("#dateFilter").datebox({disabled: true});
                    $("#dateEndFilter").datebox({disabled: true});
                    //在两次选择全部和取消 选择全部之间 如果提交了查询，可以记住以前输入的数据
                    $("#groupid").after('<input type="hidden" name="dateFilter" id="dateFilterSubstitute" value="' + dateFilter + '" />');
                    $("#groupid").after('<input type="hidden" name="dateEndFilter" id="dateEndFilterSubstitute"  value="' + dateEndFilter + '" />');
                } else {
                    $("#dateFilter").datebox({disabled: false});
                    $("#dateFilter").datebox("setValue", dateFilter);
                    $("#dateEndFilter").datebox({disabled: false});
                    $("#dateEndFilter").datebox("setValue", dateEndFilter);
                    //在两次选择全部和取消 选择全部之间 如果提交了查询，可以记住以前输入的数据
                    $("#dateFilterSubstitute").remove();
                    $("#dateEndFilterSubstitute").remove();
                }
            });
            if (allFilter == 'on') {
                $("#allFilter").prop("checked", true);
                $("#allFilter").change();
            }

            $('#form_submit').submit(function () {

                //显示全部 未勾选时才校验两个日期
                if ($("#allFilter").prop("checked")) {
                    $("#form_submit").submit();
                }

                var beginTime = $('#dateFilter').datetimebox('getValue');
                var endTime = $('#dateEndFilter').datetimebox('getValue');
                var sTime = '';
                var eTime = '';
                var patternBeginTime = /(\d{4})-(\d{1,2})-(\d{1,2})/;
                if (patternBeginTime.test(beginTime)) {
                    sTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3, 0, 0, 0).getTime();
                }
                var patternEndTime = /(\d{4})-(\d{1,2})-(\d{1,2})/;
                if (patternEndTime.test(endTime)) {
                    eTime = new Date(RegExp.$1, (parseInt(RegExp.$2) - 1), RegExp.$3, 0, 0, 0).getTime();
                }
                if (sTime == '' || eTime == '' || eTime - sTime < 0) {
                    alert("结束时间需要大于开始时间");
                    return false;
                }

                $("#form_submit").submit();
            });

        });

        //全选，取消全选，记住以前选择的日期
        function onSelectBegin(date) {
            dateFilter = formatTime(date);
            //  $('#result').text(date)
        }
        function onSelectEnd(date) {
            dateEndFilter = formatTime(date);
            //  $('#result').text(date)
        }


        function formatTime(date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
        }

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <c:if test="${taskgroup.appKey == '17yfn24TFexGybOF0PqjdY'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸任务管理</td>
        </c:if>
        <c:if test="${taskgroup.appKey == '08pkvrWvx5ArJNvhYf19kN'}">
            <td height="22" class="page_navigation_td">>> 运营维护 >> app任务管理 >> 优酷合作任务管理</td>
        </c:if>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">
                        <a href="/task/group/${taskgroup.appKey}?platform=${taskgroup.appPlatform.code}">
                            任务组列表 </a> &nbsp; >> ${taskgroup.taskGroupName} &nbsp;
                    </td>
                </tr>
            </table>
            <table width="50%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td>任务组信息</td>
                    <td>ID：${taskgroup.taskGroupId}&nbsp;&nbsp;&nbsp;&nbsp;
                        名称：${taskgroup.taskGroupName}&nbsp;&nbsp;&nbsp;&nbsp;
                        APPKEY：${taskgroup.appKey}&nbsp;&nbsp;&nbsp;&nbsp;
                        平台：<fmt:message key="joymeapp.platform.${taskgroup.appPlatform.code}" bundle="${def}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                </tr>
            </table>
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td width="30px"></td>
                </tr>
                <tr>
                    <%--<td width="80" align="center">搜索条件</td>--%>
                    <td>
                        <form action="/joyme/task/list?appkey=${appkey}" method="post" id="form_submit_search">
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <%--todo 查询的条件form--%>
                                    <%--<td width="80" align="center">--%>
                                    <%--<input name="Button" type="submit" class="default_button" value=" 搜索 ">--%>
                                    <%--</td>--%>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form method="post" action="/task/createpage">
                            <input type="hidden" name="groupid" value="${taskgroup.taskGroupId}"/>
                            <table>
                                <tr>
                                    <td>
                                        <input type="submit" name="button" class="default_button" value="添加任务"/>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                    <c:if test="${taskgroup.type.code =='common'}">
                        <td>
                            <form action="/task/list" method="post" id="form_submit">
                                <table>
                                    <tr>
                                        <input type="hidden" value="${groupid}" name="groupid" id="groupid"/>
                                        <td height="1" class="default_line_td">
                                            从:
                                        </td>
                                        <td height="1">
                                            <input type="text" class="easyui-datebox" editable="false" id="dateFilter"
                                                   data-options="onSelect:onSelectBegin"
                                                   name="dateFilter"/>
                                        </td>

                                        <td height="1" class="default_line_td">
                                            到:
                                        </td>
                                        <td height="1">
                                            <input type="text" class="easyui-datebox" editable="false"
                                                   id="dateEndFilter" name="dateEndFilter"
                                                   data-options="onSelect:onSelectEnd"/>
                                        </td>


                                        <td height="1">
                                            <input type="checkbox" name="allFilter" id="allFilter"/>
                                        </td>
                                        <td height="1" class="default_line_td">
                                            显示所有日期
                                        </td>
                                        <td height="1" class="default_line_td">
                                            &nbsp;&nbsp;查看类型:
                                        </td>
                                        <td height="1">
                                            <select name="typeFilter" id="typeFilter">
                                                <option value="">全部</option>
                                                <option value="n"><fmt:message key="joyme.task.status.n" bundle="${def}"/> </option>
                                                <option value="y"><fmt:message key="joyme.task.status.y" bundle="${def}"/> </option>
                                                <option value="ing"><fmt:message key="joyme.task.status.ing" bundle="${def}"/> </option>
                                            </select>
                                        </td>
                                        <td width="50px">
                                            <input type="submit" name="button" value="查询" class="default_button"/>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="50">任务ID</td>
                    <td nowrap align="center">任务动作</td>
                    <td nowrap align="center">名称</td>
                    <td nowrap align="center">任务类型</td>
                    <td nowrap align="center">奖励选项</td>
                    <td nowrap align="center">完成次数</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">是否自动发送奖励</td>
                    <td nowrap align="center">任务完成验证</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                    <c:choose>
                        <c:when test="${taskgroup.type.code =='sign'}">
                            <td nowrap align="center">添加信息</td>
                            <td nowrap align="center">修改信息</td>
                        </c:when>
                        <c:otherwise>
                            <td nowrap align="center">开始时间</td>
                            <td nowrap align="center">结束时间</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>
                    <td height="1" colspan="13" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${not empty list}">
                        <c:forEach items="${list}" var="task" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap align="center">${task.taskId}</td>
                                <td nowrap align="center">
                                    <fmt:message key="task.action.type.${task.taskAction.code}" bundle="${def}"/>
                                </td>
                                <td nowrap align="center"><a class="link_title" href="javascript:void(0);"
                                                             title="${task.taskDesc}"> ${task.taskName} </a></td>
                                <td nowrap align="center"><fmt:message key="joyme.task.type.${task.taskType.code}"
                                                                       bundle="${def}"/>
                                </td>
                                <td nowrap align="center">
                                    <c:choose>
                                        <c:when test="${task.taskAward==null}">无</c:when>
                                        <c:otherwise>
                                            类型：<fmt:message key="task.award.type.${task.taskAward.type}"
                                                            bundle="${def}"/>&nbsp;&nbsp;
                                            值：${task.taskAward.value}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td nowrap align="center" width="30">${task.overTimes}</td>
                                <td nowrap align="center">
                                    <a href="/task/sort/up?taskid=${task.taskId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&allFilter=${allFilter}&dateFilter=${dateFilter}&dateEndFilter=${dateEndFilter}&typeFilter=${typeFilter}">
                                        <img src="/static/images/icon/up.gif"/>
                                    </a>
                                    <a href="/task/sort/down?taskid=${task.taskId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&allFilter=${allFilter}&dateFilter=${dateFilter}&dateEndFilter=${dateEndFilter}&typeFilter=${typeFilter}">
                                        <img src="/static/images/icon/down.gif"/>
                                    </a>
                                </td>
                                <td nowrap align="center"><c:choose>
                                                                    <c:when test="${task.autoSendAward}">是</c:when>
                                                                    <c:otherwise>否</c:otherwise>
                                                                </c:choose></td>
                                <td nowrap align="center"><c:choose><c:when test="${task.taskVerifyId==0}">按用户</c:when>
                                                                                                    <c:otherwise>按设备</c:otherwise>
                                                                                                </c:choose></td>
                                <td nowrap align="center"  <c:choose>
                                    <c:when test="${task.removeStatus.code eq 'n'}">style="color: #008000;" </c:when>
                                    <c:otherwise>style="color: #ff0000;"</c:otherwise>
                                </c:choose>>
                                    <fmt:message key="joyme.task.status.${task.removeStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap align="center">
                                    <a href="/task/modifypage?taskid=${task.taskId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&allFilter=${allFilter}&dateFilter=${dateFilter}&dateEndFilter=${dateEndFilter}&typeFilter=${typeFilter}">编辑</a>
                                    &nbsp;
                                    <c:choose>
                                        <c:when test="${task.removeStatus.code eq 'n'}">
                                            <a href="/task/remove?taskid=${task.taskId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&allFilter=${allFilter}&dateFilter=${dateFilter}&dateEndFilter=${dateEndFilter}&typeFilter=${typeFilter}">删除</a>
                                        </c:when>
                                        <c:when test="${task.removeStatus.code eq 'ing'}">
                                            <a href="/task/publish?taskid=${task.taskId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&allFilter=${allFilter}&dateFilter=${dateFilter}&dateEndFilter=${dateEndFilter}&typeFilter=${typeFilter}">发布</a>
                                            <a href="/task/remove?taskid=${task.taskId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&allFilter=${allFilter}&dateFilter=${dateFilter}&dateEndFilter=${dateEndFilter}&typeFilter=${typeFilter}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/task/recover?taskid=${task.taskId}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}&allFilter=${allFilter}&dateFilter=${dateFilter}&dateEndFilter=${dateEndFilter}&typeFilter=${typeFilter}">恢复</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <c:choose>
                                    <c:when test="${taskgroup.type.code =='sign'}">
                                        <td nowrap align="center">${task.createUserId}/<fmt:formatDate
                                                value="${task.createTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td nowrap align="center">${task.modifyUserId}/<fmt:formatDate
                                                value="${task.modifyTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td nowrap align="center">
                                            <c:if test="${not empty task.beginTime}"> <fmt:formatDate
                                                    value="${task.beginTime}"
                                                    pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </c:if></td>
                                        <td nowrap align="center"><c:if test="${not empty task.endTime}">
                                            <fmt:formatDate
                                                    value="${task.endTime}"
                                                    pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </c:if></td>
                                    </c:otherwise>
                                </c:choose>

                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="13" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="13" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="13" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="13">
                            <LABEL>
                                <pg:pager url="/task/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="groupid" value="${groupid}"/>
                                    <pg:param name="typeFilter" value="${typeFilter}"/>
                                    <pg:param name="allFilter" value="${allFilter}"/>
                                    <pg:param name="dateFilter" value="${dateFilter}"/>
                                    <pg:param name="dateEndFilter" value="${dateEndFilter}"/>
                                    <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspgnoincludejquery.jsp" %>
                                </pg:pager>
                            </LABEL>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>