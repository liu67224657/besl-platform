<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <form action="/viewline/addlineitem" method="post" name="addlineitemform" id="addlineitemform">
        <input name="lineCode" type="hidden" value="${line.lineCode}"/>
        <input name="lineItemTypeCode" type="hidden" value="${line.lineItemType.code}"/>
        <tr>
            <td width="120" align="right" class="edit_table_defaulttitle_td">Line编码：</td>
            <td class="edit_table_value_td">
                <input name="srcId1" type="input" class="default_input_singleline" size="32" maxlength="36">
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
                <input name="Reset" type="reset" class="default_button" value="重置">
            </td>
        </tr>
    </form>
</table>
