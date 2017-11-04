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
    <script type="text/javascript">

    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营管理 >> 条目管理 >> 商品管理</td>
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
                        <form method="post" action="/point/goods/createpage">
                            <table>
                                <tr>
                                    <td>
                                        <p:privilege name="/point/goods/createpage">
                                        <input type="submit" name="button" class="default_button" value="添加商品"/>
                                        </p:privilege>
                                    <td>
                                <tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>
            <form action="/point/goods/list" method="post">
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
                        <td nowrap align="left" width="">商品ID</td>
                        <td nowrap align="left" width="">商品名称</td>
                        <td nowrap align="left" width="">领取礼品后分享内容</td>
                        <td nowrap align="left" width="">商品类型</td>
                        <td nowrap align="left" width="">商品描述</td>
                        <td nowrap align="left" width="">商品图片</td>
                        <%--<td nowrap align="left" width="130">商品过期时间</td>--%>
                        <td nowrap align="left" width="">商品总量</td>
                        <td nowrap align="left" width="">剩余数量</td>
                        <td nowrap align="left" width="">消费积分</td>
                        <td nowrap align="left" width="">消费次数类型</td>
                        <td nowrap align="left" width="">是否新品</td>
                        <td nowrap align="left" width="">是否热门</td>
                        <td nowrap align="left" width="">状态</td>
                        <td nowrap align="left" width="">创建人信息</td>
                        <td nowrap align="left" width="">排序操作</td>
                        <td nowrap align="left" width="">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="13" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${list.size() > 0}">
                            <c:forEach items="${list}" var="goods" varStatus="st">
                                <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>
                                            ${goods.goodsId}
                                    </td>
                                    <td nowrap>
                                        <input type="hidden" name="goodsid" value="${goods.goodsId}"/>
                                            ${goods.goodsName}
                                    </td>
                                    <td nowrap>
                                        <c:forEach items="${baseInfoList}" var="baseinfo">
                                            <c:if test="${baseinfo.shareId==goods.shareId}">
                                                ${baseinfo.shareKey}
                                            </c:if>
                                        </c:forEach>

                                    </td>
                                    <td nowrap><fmt:message key="goods.type.${goods.goodsType.code}"
                                                            bundle="${def}"/></td>
                                    <td nowrap>${goods.goodsDesc}</td>
                                    <td nowrap><img width="100" height="100" id="img_pic" src="${goods.goodsPic}"/></td>
                                        <%--<c:if test="${goods.goodsExpireDate!=null}">
                                            <td nowrap>
                                                <fmt:formatDate value="${goods.goodsExpireDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </td>
                                        </c:if>--%>
                                    <td nowrap>${goods.goodsAmount}</td>
                                    <td nowrap>${goods.goodsResetAmount}</td>
                                    <td nowrap>${goods.goodsConsumePoint}</td>
                                    <td nowrap><fmt:message key="consumetimes.type.${goods.consumeTimesType.code}"
                                                            bundle="${def}"/></td>
                                    <td nowrap>
                                        <c:if test="${goods.isNew}">是</c:if>
                                        <c:if test="${!goods.isNew}">否</c:if>
                                    </td>
                                    <td nowrap>
                                        <c:if test="${goods.isHot}">是</c:if>
                                        <c:if test="${!goods.isHot}">否</c:if>
                                    </td>
                                    <td nowrap
                                            <c:choose>
                                                <c:when test="${goods.validStatus.code eq 'valid'}">
                                                    style="color: #008000;"
                                                </c:when>
                                                <c:otherwise>
                                                    class="error_msg_td"
                                                </c:otherwise>
                                            </c:choose>>
                                         <fmt:message key="exchangegoods.validstatus.${goods.validStatus.code}" bundle="${def}"/>
                                    </td>
                                    <td>${goods.createUserId}<br/>${goods.createIp}
                                        <br/><fmt:formatDate value="${goods.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                    <td align="center">
                                        <p:privilege name="/point/goods/sort/up">
                                            <a href="/point/goods/sort/up?goodsid=${goods.goodsId}"><img
                                                    src="/static/images/icon/up.gif"></a>
                                        </p:privilege>
                                        &nbsp;
                                        <p:privilege name="/point/goods/sort/down">
                                            <a href="/point/goods/sort/down?goodsid=${goods.goodsId}"><img
                                                    src="/static/images/icon/down.gif"></a>
                                        </p:privilege>
                                    </td>
                                    <td nowrap>
                                        <c:choose>
                                            <c:when test="${goods.validStatus.code eq 'valid'}">
                                                <p:privilege name="/point/goods/modifypage">
                                                    <a href="/point/goods/modifypage?goodsid=${goods.goodsId}">编辑</a>
                                                </p:privilege>
                                                <p:privilege name="/point/goods/delete">
                                                    <a href="/point/goods/delete?goodsid=${goods.goodsId}&goodstype=${gtype}">删除</a>
                                                </p:privilege>
                                            </c:when>
                                            <c:when test="${goods.validStatus.code eq 'invalid'}">
                                                <p:privilege name="/point/goods/modifypage">
                                                    <a href="/point/goods/modifypage?goodsid=${goods.goodsId}">编辑</a>
                                                </p:privilege>
                                                <a href="/point/goods/recover?goodsid=${goods.goodsId}&goodstype=${gtype}">恢复</a>
                                            </c:when>
                                            <c:otherwise>
                                                <p:privilege name="/point/goods/recover">
                                                    <a href="/point/goods/recover?goodsid=${goods.goodsId}&goodstype=${gtype}">恢复</a>
                                                </p:privilege>
                                            </c:otherwise>
                                        </c:choose>
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
                                <pg:pager url="/point/goods/list"
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