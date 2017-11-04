<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>WIKI大端首页模块管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

        });
    </script>
</head>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 着迷APP >> WIKI大端管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>首页模块列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
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
            <table width="90%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/wikiapp/index/list" method="post" id="form_search">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td align="right" class="edit_table_defaulttitle_td">平台：</td>
                                    <td>
                                        <select id="select_platform" name="platform">
                                            <option value="">请选择</option>
                                            <c:forEach items="${platformSet}" var="plat">
                                                <option value="${plat.code}"
                                                        <c:if test="${plat.code == platform}">selected="selected"</c:if>>
                                                    <fmt:message key="joymeapp.platform.${plat.code}" bundle="${def}"/>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="90%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/wikiapp/index/createpage" method="post" id="form_create">
                    <tr>
                        <td><input type="hidden" name="platform" value="${platform}"/>
                            <input name="Button" type="submit" class="default_button" value=" 添加模块 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center">ID</td>
                    <td nowrap align="center">标题</td>
                    <td nowrap align="center">唯一码</td>
                    <td nowrap align="center">ICON</td>
                    <td nowrap align="center">类型</td>
                    <td nowrap align="center">平台</td>
                    <td nowrap align="center">排序</td>
                    <td nowrap align="center">状态</td>
                    <td nowrap align="center">操作</td>
                    <td nowrap align="center">修改信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="6" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list != null && list.size() > 0}">
                        <c:forEach items="${list}" var="line" varStatus="st">
                            <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                <td nowrap>
                                        ${line.lineId}
                                </td>
                                <td nowrap>
                                        <a href="/joymeapp/wikiapp/index/detail?linecode=${line.code}">${line.lineName}</a>
                                </td>
                                <td nowrap>
                                        ${line.code}
                                </td>
                                <td nowrap>
                                        <img src="${line.bigpic}" height="24" width="24"/>
                                </td>
                                <td nowrap><fmt:message key="client.item.type.${line.itemType.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <fmt:message key="joymeapp.platform.${line.platform}" bundle="${def}"/>
                                </td>
                                <td height="1">
                                    <form action="/joymeapp/wikiapp/index/sort" method="post">
                                        <input type="hidden" name="lineid" value="${line.lineId}"/>
                                        <input type="hidden" name="linecode" value="${line.code}"/>
                                        <input type="hidden" name="linetype" value="${line.lineType.code}"/>
                                        <input type="hidden" name="platform" value="${line.platform}"/>
                                        <input id="input_old_order_${st.index}" name="oldorder" value="${line.display_order}" readonly="readonly"/>
                                        <input id="input_order_modify_${st.index}" type="button" class="default_button" value="修改" onclick="$('#input_order_save_'+${st.index}).show();$(this).hide();$('#input_order_new_'+${st.index}).show()"/>
                                        <input id="input_order_save_${st.index}" type="submit" class="default_button" value="保存" hidden="hidden" onclick="$('#input_order_modify_'+${st.index}).show();$(this).hide();$('#input_order_new_'+${st.index}).hide()"/>
                                        <input id="input_order_new_${st.index}" name="neworder" value="" type="text" hidden="hidden"/>
                                    </form>
                                </td>
                                <td
                                        <c:choose>
                                            <c:when test="${line.validStatus.code == 'valid'}">style="color: #008000"</c:when>
                                            <c:when test="${line.validStatus.code == 'removed'}">style="color: #ff0000"</c:when>
                                        </c:choose>>
                                    <fmt:message key="client.line.status.${line.validStatus.code}" bundle="${def}"/>
                                </td>
                                <td nowrap>
                                    <a href="/joymeapp/wikiapp/index/modifypage?linecode=${line.code}">编辑</a>
                                    <c:choose>
                                        <c:when test="${line.validStatus.code == 'valid'}">
                                            <a href="/joymeapp/wikiapp/index/remove?linecode=${line.code}&lineid=${line.lineId}&linetype=${line.lineType.code}&platform=${line.platform}">删除</a>
                                        </c:when>
                                        <c:when test="${line.validStatus.code == 'removed'}">
                                            <a href="/joymeapp/wikiapp/index/recover?linecode=${line.code}&lineid=${line.lineId}&linetype=${line.lineType.code}&platform=${line.platform}">恢复</a>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td nowrap>
                                    <fmt:formatDate value="${line.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    <br/>${line.updateUserid}
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td height="1" colspan="10" class="default_line_td"></td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="10" class="error_msg_td">暂无数据!</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="10" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <LABEL>
                                <pg:pager url="/joymeapp/wikiapp/index/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="platform" value="${platform}"/>
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