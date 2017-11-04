<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<td width="120" align="right" class="edit_table_defaulttitle_td">选择专题：</td>
<td nowrap class="edit_table_value_td">
    <c:if test="${errorMsgMap['itemId']!=null}">
        <span class="error_msg_td"><fmt:message key="${errorMsgMap['itemId']}" bundle="${error}"/></span><br>
    </c:if>
    <select name="itemId" class="default_select_single">
        <c:forEach items="${preparedItems}" var="item">
            <option value="${item.id}" <c:if test="${itemId == item.id}">selected="true"</c:if>>
                    ${item.name}
            </option>
        </c:forEach>
    </select>
</td>