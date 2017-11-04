<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <tr>
        <td width="80" align="right" class="edit_table_defaulttitle_td">发文的博客：</td>
        <td nowrap class="edit_table_value_td">
            <input name="afrkey01" type="text" class="default_input_singleline" size="64"
                   maxlength="128" value="${afrkey01}" id="afrkey01"><br>
            * 博客的域名，使用英文逗号","分割，为空则表示所有人发的文章都可能填充过来。
        </td>
    </tr>
    <tr>
        <td width="80" align="right" class="edit_table_defaulttitle_td">含有关键字：</td>
        <td nowrap class="edit_table_value_td">
            <input name="afrkey02" type="text" class="default_input_singleline" size="64"
                   maxlength="128" value="${afrkey02}" id="afrkey02"><br>
            * 文章标题、内容、标签中包含的关键词，使用英文逗号","分割填写，为空则表示所有文章。
        </td>
    </tr>
    <tr>
        <td width="80" align="right" class="edit_table_defaulttitle_td">文章类型：</td>
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
        <td width="80" align="right" class="edit_table_defaulttitle_td">发布类型：</td>
        <td nowrap class="edit_table_value_td">
            原文<input type="checkbox" name="afrkey04" value="org"
                       <c:if test="${afrkey04s['org'] == 'org'}">checked</c:if>/>,
            或转贴<input type="checkbox" name="afrkey04" value="fwd"
                        <c:if test="${afrkey04s['fwd'] == 'fwd'}">checked</c:if>/>
        </td>
    </tr>
</table>



