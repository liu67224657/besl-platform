<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<td width="120" align="right" class="edit_table_defaulttitle_td">选择Line：</td>
<td nowrap class="edit_table_value_td">
    <c:if test="${errorMsgMap['itemCode']!=null}">
        <span class="error_msg_td"><fmt:message key="${errorMsgMap['itemCode']}" bundle="${error}"/></span><br>
    </c:if>
    <select name="itemId" class="default_select_single">
        <option value="0">--不选择，根据模板的要求创建新的Line。--</option>
        <c:forEach items="${preparedItems}" var="item">
            <option value="${item.id}" <c:if test="${itemId == item.id}">selected="true"</c:if>>
                    ${item.name}
            </option>
        </c:forEach>
    </select>
</td>