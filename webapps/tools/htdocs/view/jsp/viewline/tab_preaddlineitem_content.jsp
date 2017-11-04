<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <form action="/viewline/addlineitem" method="post" name="addlineitemform" id="addlineitemform">
        <input name="lineId" type="hidden" value="${line.lineId}"/>
        <%--<input name="lineItemTypeCode" type="hidden" value="${line.lineItemType.code}"/>--%>
        <input name="step" type="hidden" value="1"/>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">文章URL地址：</td>
            <td class="edit_table_value_td">
                <c:if test="${errorMsgMap['srcId1'] != null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['srcId1']}" bundle="${error}"/></span><br>
                </c:if>
                <input name="srcId1" type="text" class="default_input_singleline" size="60" maxlength="256" value="${srcId1}">* 博客单页的地址。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">选择有效状态：</td>
            <td class="edit_table_value_td">
                <select name="validStatus" class="default_select_single">
                    <option value="valid">有效</option>
                    <option value="invalid">无效</option>
                </select>* 默认为有效状态。
            </td>
        </tr>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">描述：</td>
            <td class="edit_table_value_td">
                <c:if test="${errorMsgMap['itemDesc'] != null}">
                    <span class="error_msg_td"><fmt:message key="${errorMsgMap['itemDesc']}" bundle="${error}"/></span><br>
                </c:if>
                <textarea rows="3" cols="100" name="itemDesc"
                          class="default_input_singleline">${lineItem.itemDesc}</textarea>
                <%--* 此处文字内容、数量，根据页面显示需要自由定义。--%>
            </td>
        </tr>
        <tr>
            <td height="1" colspan="2" class="default_line_td"></td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr align="center">
            <td colspan="2">
                <input name="Submit" type="submit" class="default_button" value="提交">
                <input name="Reset" type="button" class="default_button" value="返回" onclick="javascript:window.history.go(-1);">
            </td>
        </tr>
    </form>
</table>
