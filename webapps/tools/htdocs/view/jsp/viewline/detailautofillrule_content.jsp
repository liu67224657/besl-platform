<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td width="100" align="right" class="edit_table_defaulttitle_td">文章作者列表：</td>
        <td nowrap class="edit_table_value_td">
            ${afrkey01}
        </td>
    </tr>
    <tr>
        <td width="100" align="right" class="edit_table_defaulttitle_td">关键词列表：</td>
        <td nowrap class="edit_table_value_td">
            ${afrkey02}
        </td>
    </tr>
    <tr>
        <td width="100" align="right" class="edit_table_defaulttitle_td">文章类型：</td>
        <td nowrap class="edit_table_value_td">
            是一句话<input type="checkbox" name="afrkey03" value="1"
                       <c:if test="${afrkey03s['1'] == '1'}">checked</c:if>/>,
            并且是长文<input type="checkbox" name="afrkey03" value="16"
                        <c:if test="${afrkey03s['16'] == '16'}">checked</c:if>/>,
            并且含图片<input type="checkbox" name="afrkey03" value="2"
                        <c:if test="${afrkey03s['2'] == '2'}">checked</c:if>/>,
            并且含音乐<input type="checkbox" name="afrkey03" value="4"
                        <c:if test="${afrkey03s['4'] == '4'}">checked</c:if>/>,
            并且含视频<input type="checkbox" name="afrkey03" value="8"
                        <c:if test="${afrkey03s['8'] == '8'}">checked</c:if>/>,
            并且含APP<input type="checkbox" name="afrkey03" value="32"
                         <c:if test="${afrkey03s['32'] == '32'}">checked</c:if>/>
        </td>
    </tr>
    <tr>
        <td width="100" align="right" class="edit_table_defaulttitle_td">发布类型：</td>
        <td nowrap class="edit_table_value_td">
            原文<input type="checkbox" name="afrkey04" value="org"
                     <c:if test="${afrkey04s['org'] == 'org'}">checked</c:if>/>,
            或转贴<input type="checkbox" name="afrkey04" value="fwd"
                      <c:if test="${afrkey04s['fwd'] == 'fwd'}">checked</c:if>/>
        </td>
    </tr>
</table>