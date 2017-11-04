<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>积分墙列表信息</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function (e) {

            <c:forEach items="${types}" var="item" >
            $("#pointKey").prepend("<option value='${item.code}'>${item.name}</option>");
            </c:forEach>

            $("#pointKey").val("${pointKey}");

            <c:forEach items="${shopTypes}" var="item" >
            $("#searchShopKey").append("<option value='${item.code}'>${item.name}</option>");
            </c:forEach>

            $("#searchShopKey").val("${searchShopKey}");

        });
        function deleteWallByAppkey(appkey) {
            var msg = "您确定要删除appkey为" + appkey + "的积分墙吗？\n\n请确认！";
            if (confirm(msg) == true) {
                window.location.href = "/point/pointwall/wall/delete?appkey=" + appkey;


            }
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
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"> 积分墙列表管理</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorFK)>0}">
                    <tr>
                        <td height="1" colspan="11" class="error_msg_td">
                            <c:out value="${errorFK}" escapeXml="true"/>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="11" class="error_msg_td">
                            <c:out value="${errorMsg}" escapeXml="true"/>
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
                        <form action="/point/pointwall/wall/list" method="post">
                            <table width="980px">
                                <tr>
                                    <td height="1" class="default_line_td">
                                        appkey:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <input type="text" name="appkey" size="15" value="${appkey}"/>
                                    </td>

                                    <td height="1" class="default_line_td">
                                        积分归属类型:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="pointKey" id="pointKey">
                                            <option value="">全部</option>
                                        </select>
                                    </td>

                                    <td height="1" class="default_line_td">
                                        商城类型:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <select name="searchShopKey" id="searchShopKey">
                                            <option value="">全部</option>
                                        </select>
                                    </td>

                                    <td height="1" class="default_line_td">
                                        含有积分墙的应用名称:
                                    </td>
                                    <td height="1" class="edit_table_defaulttitle_td">
                                        <input type="text" name="appKeyName" size="15" value="${appKeyName}"/>
                                    </td>

                                    <td>
                                        <input type="submit" name="button" value="查询"/>
                                    </td>
                                    <td>
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
                                          action="/point/pointwall/wall/createpage">

                                        <input type="submit" name="button" value="添加新的积分墙"/>
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
                    <td nowrap align="left" width="">appkey</td>
                    <td nowrap align="left" width="">含有积分墙的应用名称</td>
                    <td nowrap align="left" width="">含有的App数量</td>
                    <td nowrap align="left" width="">积分归属类型</td>
                    <td nowrap align="left" width="">商城类型</td>
                    <td nowrap align="center" width="">积分墙货币名称</td>
                    <td nowrap align="center" width="">所属模板</td>
                    <td nowrap align="center" width="">操作</td>
                </tr>
                <tr>
                    <td height="1" colspan="8" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="item" varStatus="st">
                            <c:if test="${item.flag==0}">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap><c:out value="${item.appkey}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.appKeyName}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.appNum}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.pointKeyName}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.shopKeyName}" escapeXml="true"/></td>
                                    <td nowrap><c:out value="${item.wallMoneyName}" escapeXml="true"/></td>
                                    <td nowrap><c:choose><c:when test="${not empty item.template}"><a
                                            href="/static/images/pointwall/template${item.template}.PNG"
                                            target="_blank"><fmt:message
                                            key="pointwall.template.${item.template}"
                                            bundle="${def}"/></a></c:when><c:otherwise></c:otherwise></c:choose></td>
                                    <td nowrap align="center">
                                        <a href="/point/pointwall/wall/modifypage?appkey=${item.appkey}">编辑</a>
                                        &nbsp;&nbsp; <a href="javascript:;"
                                                        onclick="deleteWallByAppkey('${item.appkey}');">删除</a>
                                        &nbsp;&nbsp;<a
                                            href="/joymeapp/app/modifypage?appid=${item.appkey}&returnFlag=toPointWall">配置"我的"</a>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="8" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="8" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="11">
                            <pg:pager url="/point/pointwall/wall/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="currentPageNumber" value="${page.curPage}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <pg:param name="appkey" value="${appkey}"/>
                                <pg:param name="appKeyName" value="${appKeyName}"/>
                                <pg:param name="searchShopKey" value="${searchShopKey}"/>
                                <pg:param name="pointKey" value="${pointKey}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>