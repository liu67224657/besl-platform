<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>商品管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {

            $("#checkall").bind("click", function () {
                if ($('#checkall').is(":checked")) {
                    $("[name=box]:checkbox").attr("checked", true);
                } else {
                    $("[name=box]:checkbox").attr("checked", false);
                }
            });

            $("#checkinverse").bind("click", function () {
                $("[name=box]:checkbox").each(function () {
                    $(this).attr("checked", !$(this).attr("checked"));
                });
            });
            var type = "${type}";
            if (type == '1') {
                alert("导出成功");
                return;
            }
            if (type == "-1") {
                alert("导出失败");
                return;
            }
            if (type == "0") {
                alert("没有可导出的数据");
                type = "";
                return;
            }

        });
        function exreport() {
            var result = new Array();
            $("[name=box]:checkbox").each(function () {
                if ($(this).is(":checked")) {
                    result.push($(this).attr("value"));
                }
            });
            if (result == "") {
                alert("请至少选择一条记录导出");
                return false;
            }

            $('#input_hidden_ids').val(result.join('@'))
        }
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 礼包预定列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">商品列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="13" class="error_msg_td">
                            <fmt:message key="${errorMsg}" bundle="${error}"/>
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
                        <form action="/gift/reserve/list" method="post" id="form_submit">
                            <table>
                                <tr>
                                    <td height="1"></td>
                                    <td height="1" class="default_line_td">
                                        起始时间:
                                    </td>
                                    <td height="1">
                                        <input type="text" class="Wdate"
                                               onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',vel:'startdate',autoPickDate:true})"
                                               readonly="readonly" name="startdate" id="input_text_startdate"
                                               value="<fmt:formatDate value="${startdate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>

                                    </td>
                                    <td height="1"></td>
                                    <td height="1" class="default_line_td">
                                        结束时间:
                                    </td>
                                    <td height="1">

                                        <input type="text" class="Wdate"
                                               onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',vel:'enddate',autoPickDate:true})"
                                               readonly="readonly" name="enddate" id="input_text_enddate"
                                               value="<fmt:formatDate value="${enddate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    </td>

                                    <td>
                                        <p:privilege name="/gift/reserve/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td>
                        <form action="/gift/reserve/export" method="post" onsubmit="return exreport();">
                            <input type="checkbox" name="all" id="checkall"/>全选
                            <input type="checkbox" name="inverse" id="checkinverse"/>反选
                            <input type="hidden" name="replyids" id="input_hidden_ids">

                            <input type="submit" value="导出"/>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/gift/reserve/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td lass="default_line_td"></td>
                    </tr>
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width=""></td>
                        <td nowrap align="left" width="">ID</td>
                        <td nowrap align="left" width="">礼包名称</td>
                        <td nowrap align="left" width="">预定时间</td>
                        <td nowrap align="left" width="">预定渠道</td>
                        <td nowrap align="left" width="">预定人</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                        <input type="checkbox" name="box" value="${dto.giftReserveId}"/>
                                    </td>
                                    <td nowrap>
                                            ${dto.giftReserveId}
                                    </td>
                                    <td nowrap>
                                            ${dto.giftName}
                                    </td>
                                    <td nowrap>
                                            ${dto.createTime}
                                    </td>
                                    <td nowrap>
                                            ${dto.loginDomain.code}
                                    </td>
                                    <td nowrap>
                                        <c:forEach items="${mapProfile}" var="map">
                                            <c:if test="${dto.profileId==map.value.profileId}">${map.value.nick}</c:if>
                                        </c:forEach>
                                    </td>
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
                                <pg:pager url="/gift/reserve/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </td>
                        </tr>
                    </c:if>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>