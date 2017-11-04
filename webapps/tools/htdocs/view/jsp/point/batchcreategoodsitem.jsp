<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <title>后台数据管理-批量发码</title>
    <script type="text/javascript">
        function textAreaRow(textarea_id, input_id) {
            var text = document.getElementById(textarea_id).value;
            var index = 0;
            var lastIndex = 0;
            while (true) {
                lastIndex = text.indexOf("\n", lastIndex + 1);
                if (lastIndex == -1) {
                    break;
                }
                index++;

            }
            index++;
            document.getElementById(input_id).value = index;
            if (text == "") {
                document.getElementById(input_id).value = 0;
            }
            $("#import_error").text("");

        }

        $(document).ready(function() {
            $('#form_batch_create').submit(function() {
                var snValue1Size = $('#sn_value1_number').val();
                var sn_name1 = $('#sn_name1').val();

                var allowCreateSize=$('#goods_amount').val()-$('#goods_restamount').val();
                if(snValue1Size>allowCreateSize){
                    $("#import_error").text("只能批量导入"+allowCreateSize+"张");
                    return false;
                }
                if(snValue1Size>500){
                    $("#import_error").text("只能批量导入 500 张");
                    return false;
                }
                if (snValue1Size == 0) {
                    $("#import_error").text("至少要填写列表一导入的激活码");
                    return false;
                }

                if (sn_name1 == '') {
                    $("#import_error").text("至少要填写列表一的名称");
                    return false;
                }

                var snValue2Size = $('#sn_value2_number').val();
                var sn_name2 = $('#sn_name2').val();
                if (snValue2Size > 0) {
                    if (sn_name2 == '') {
                        $("#import_error").text("请填写列表二的名称");
                        return false;
                    }
                    if( snValue1Size != snValue2Size){
                        $("#import_error").text("左右俩个列表数不一致，请核对后在提交");
                          return false;
                    }
                }
            });
        })
    </script>
</head>

<body>
<form id="form_batch_create" name="batch_create" action="/point/goodsitem/create" method="post">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
            <td height="22" class="page_navigation_td">>> 运营维护 >> 礼包信息管理 >> 积分兑换库存管理</td>
        </tr>
        <tr>
            <td><br></td>
        </tr>
        <tr>
            <td height="1" class="default_line_td" colspan="2"></td>
        </tr>
        <tr>
            <td width="100%">
                <table width="100%">
                    <tr>
                        <td class="edit_table_defaulttitle_td" align="right" width="120px">导入：</td>
                        <td class="edit_table_value_td">
                            <table>
                                <tr>
                                    <td colspan="2">
                                        商品名称&nbsp;&nbsp;：<b>${goods.activitySubject}</b>
                                        <input id="goodsid" name="goodsid" type="hidden" value="${goods.activityGoodsId}"/>
                                        商品总数：<input id="goods_amount" name="restAmount" type="text" readonly="" size="10" value="${goods.goodsAmount}"/>
                                        商品剩余数量：<input id="goods_restamount" name="restAmount" type="text" readonly="" size="10" value="${goods.goodsResetAmount}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        列表名称一&nbsp;&nbsp;
                                        <input id="sn_name1" name="snname1" type="text" size="10" value="激活码"/>&nbsp;
                                        共计&nbsp;
                                        <input id="sn_value1_number" type="text" readonly="readonly"
                                               style="color: green;width: 40px;border: 0px" value="0"/>&nbsp;个
                                    </td>
                                    <td>
                                        列表名称二&nbsp;&nbsp;
                                        <input id="sn_name2" name="snname2" type="text" size="10" style="" value="密码"/>&nbsp;
                                        共计&nbsp;
                                        <input id="sn_value2_number" type="text" readonly="readonly"
                                               style="color: green;width: 40px;border: 0px" value="0"/>&nbsp;个
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <textarea id="sn_value1" name="snvalue1"
                                                  onkeyup="textAreaRow('sn_value1','sn_value1_number')"
                                                  class="default_input_multiline" cols="60"
                                                  rows="15">${sn_value1}</textarea>
                                    </td>
                                    <td>
                                        <textarea id="sn_value2" name="snvalue2"
                                                  onkeyup="textAreaRow('sn_value2','sn_value2_number')"
                                                  class="default_input_multiline" cols="60"
                                                  rows="15">${sn_value2}</textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span id="import_error" style="color: red;"></span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>
                        <td class="default_line_td" colspan="2"></td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2">
                            <input id="send_button" type="submit" class="default_button" value="批量导入">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</form>
</body>
</html>