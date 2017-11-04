<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>商品管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $('#form_creategoodsitem').submit(function() {
                var selectGoodsid = $('#select_goodsid').val();
                if (selectGoodsid.length == 0) {
                    alert('请选择商品');
                    return false;
                }
                if (selectGoodsid.length > 0) {
                    $('#hidden_goods_id').val(selectGoodsid);
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 积分兑换库存管理</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">礼品列表</td>
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
                        <form action="/point/goodsitem/list" method="post">
                            <table width="100%">
                                <tr>
                                    <td height="" class="default_line_td">
                                        检索商品列表 ：
                                    </td>
                                    <td height="">
                                        <input type="text" name="subject" value="${subject}"/>
                                        <p:privilege name="/point/goodsitem/list">
                                            <input type="submit" name="button" class="default_button" value="检索"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                                <tr>
                                    <td height="1" class="default_line_td" >
                                        商品列表:
                                    </td>
                                    <td height="1">
                                        <select name="goodsid" id="select_goodsid">
                                            <option value="">请选择</option>
                                            <c:forEach var="goods" items="${goodsList}">
                                                <option value="${goods.activityGoodsId}"
                                                        <c:if test='${goods.activityGoodsId==goodsId}'>selected="selected" </c:if>>${goods.activitySubject}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        状态：
                                    </td>
                                    <td height="1">
                                        <select name="status">
                                            <option value="">请选择</option>
                                            <option value="y"
                                            <c:if test="${status eq 'y'}">selected='selected'</c:if> >已兑换
                                            </option>
                                            <option value="n"
                                            <c:if test="${status eq 'n'}">selected='selected'</c:if> >未兑换
                                            </option>
                                        </select>
                                    </td>
                                    <td height="1">
                                        <p:privilege name="/point/goodsitem/list">
                                            <input type="submit" name="button" class="default_button" value="查询"/>
                                        </p:privilege>
                                    </td>
                                </tr>
                            </table>
                        </form>
                        <form method="post" action="/point/goodsitem/createpage" id="form_creategoodsitem">
                            <table>
                                <tr>
                                    <td>
                                        <input type="hidden" id="hidden_goods_id" name="goodsid"
                                               value="${goods.activityGoodsId}"/>
                                        <p:privilege name="/point/goodsitem/createpage">
                                        <input type="submit" name="button" class="default_button" value="批量添加"/>
                                        </p:privilege>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                    <td>

                    </td>
                </tr>
            </table>
            <form action="/point/goodsitem/list" method="post">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:if test="${fn:length(errorMsg)>0}">
                        <tr>
                            <td height="1" colspan="13" class="error_msg_td">
                                <b><fmt:message key="${errorMsg}" bundle="${error}"/></b>
                            </td>
                        </tr>
                    </c:if>
                    <tr class="list_table_title_tr">
                        <td nowrap align="left" width="100">礼品名1</td>
                        <td nowrap align="left" width="100">礼品值1</td>
                        <td nowrap align="left" width="100">礼品名2</td>
                        <td nowrap align="left" width="100">礼品值2</td>
                        <td nowrap align="left" width="80">兑换状态</td>
                        <td nowrap align="left" width="100">兑换人</td>
                        <td nowrap align="left" width="150">兑换日期</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="goodsitem" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                        <input type="hidden" name="goodsitemid" value="${goodsitem.goodsItemId}"/>
                                        <input type="hidden" name="goodsid" value="${goodsitem.goodsId}"/>
                                            ${goodsitem.snName1}
                                    </td>
                                    <td nowrap>${goodsitem.snValue1}</td>
                                    <td nowrap>${goodsitem.snName2}</td>
                                    <td nowrap>${goodsitem.snValue2}</td>
                                    <td nowrap
                                            <c:choose>
                                                <c:when test="${goodsitem.exchangeStatus.code eq 'y'}">
                                                    class="error_msg_td"
                                                </c:when>
                                                <c:otherwise>
                                                    style="color: #008000;"
                                                </c:otherwise>
                                            </c:choose>>
                                        <fmt:message key="goodsitem.exchangeStatus.${goodsitem.exchangeStatus.code}"
                                                     bundle="${def}"/>
                                    </td>
                                    <td nowrap>
                                        <c:forEach items="${profileMap}" var="item">
                                            <c:if test="${item.profileId==goodsitem.profileId}">${item.nick}</c:if>
                                        </c:forEach>
                                    </td>
                                    <td nowrap><fmt:formatDate value="${goodsitem.consumeDate}"
                                                               pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td nowrap>

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
                                <pg:pager url="/point/goodsitem/list"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="goodsid" value="${goodsId}"/>
                                    <pg:param name="status" value="${status}"/>
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