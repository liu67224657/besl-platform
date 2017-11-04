<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加游戏标签</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>

    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script>
        $(document).ready(function () {
            $("#sub").click(function () {
                var uid = $("#vertualUid").val();
                var replyTEXT = $("[name='replytext']").val();
                if (uid == '') {
                    alert("请选择虚拟用户");
                    return false;
                }

                if (replyTEXT == '') {
                    alert("请填写回复内容");
                    return false;
                }

                var strlen = isChinese($("[name='replytext']"));
                strlen = Math.ceil(strlen / 2);
                if (strlen <= 1) {
                    alert("最少要输入两个字");
                    return false;
                }
                if (strlen > 140) {
                    alert("最多只能输入140个字，现在字数" + strlen);
                    return false;
                }

                var textare = {text: replyTEXT};
                var text = JSON.stringify(textare);
                var id = $("[name='unikey']").val()
                var lock = false;
                if (!lock) {
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: "/apiwiki/comment/reply/comment",
                        dataType: "json",
                        data: {uid: uid, text: text, unikey: id},
                        success: function (req) {
                            var result = req;
                            if (result.rs == 1) {
                                lock = false;
                                alert("发布成功");
                                window.location.href = "/apiwiki/comment/createpage?id=" + id;
                            } else {
                                alert("发布失败");
                            }
                        }
                    });
                }


            });


            $("#addvertual").click(function () {
                $("#reset").click(function () {
                    $('.easyui-datetimebox').datetimebox('clear');
                    $("#form_submit :text").prop("value", '');
                    $("#form_submit input[name='gameDbId']").prop("value", '');
                });

                $('#dgrules').datagrid({
                    url: '/apiwiki/comment/get/vertualProfileList',
                    pagination: true,
                    rownumbers: false,
                    singleSelect: true,
                    top: '30px',
                    width: 'auto',
                    height: 'auto',
                    striped: true,
                    idField: 'appId',
                    pageList: [10, 20],
                    fitColumns: true,
                    loadMsg: '数据加载中请稍后……',
                    columns: [
                        [
                            {field: 'id', title: 'UID', width: 60},
                            {field: 'nick', title: '昵称', width: 100},
                            {
                                field: 'icon', title: '头像', width: 60,
                                formatter: function (value, row, index) {
                                    return "<img src=" + row.icon + " width='40' height='40>'";
                                }
                            },
                            {field: 'ck', checkbox: true}
                        ]
                    ]
                });

                $('#chooseWindow').window('open')
            });


//            $("#buttonInWindow").click(function () {
//
//                var url = '/json/gameclient/clientline/wikiaddgame';
//                if ($.trim($("#gameNameInWindow").val()) != '') {
//
//                    url += '?gameName=' + $.trim($("#gameNameInWindow").val());
//                }
//                $('#dgrules').datagrid('options').url = url;
//                $("#dgrules").datagrid('reload');
//
//            });

            //弹窗中选择一个游戏后
            $("#selectone").click(function () {

                var item = $('#dgrules').datagrid('getSelected');
                var id = item.id;
                var nick = item.nick;
                $("#vertualName").text(nick);
                $("#vertualUid").val(id);
                $('#chooseWindow').window('close');
            });
        });

        function isChinese(str) { //判断是不是中文
            var oVal = str.val();
            var oValLength = 0;
            oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
            return oValLength;

        }

    </script>
</head>

<body>
<div id="chooseWindow" class="easyui-window" title="请选择虚拟用户"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false"
     style="width:750px; top:50px;">
    <%--昵称：&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
    <%--<input type="text" id="gameNameInWindow" size="10"/>--%>
    <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
    <%--<input type="button" id="buttonInWindow" value="查询"/>--%>
    <table id="dgrules" data-options="
              rownumbers:false,
              autoRowHeight:true,
              pagination:true">
    </table>
    <div style="text-align:center">
        <div class=" clear mt20" style=" margin:0 auto;">
            <input type="button" onclick="$('#chooseWindow').window('close');" value="取消"/>
            <input type="button" value="选择" id="selectone" value="保存"/></div>
    </div>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 点评详情 >> 回复点评</td>
    </tr>
    <tr>

        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">回复点评</td>
                </tr>
                <tr>
                    <td>
                        <table border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="1" colspan="3" class="edit_table_header_td">
                                    <input type="hidden" value="${id}" name="unikey"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="edit_table_defaulttitle_td" width="100" height="1">选择虚拟用户</td>
                                <td class="edit_table_value_td">
                                    <span id="vertualName">未选择</span>
                                    <input type="hidden" value="" id="vertualUid" name="uid"/>
                                    <input type="button" id="addvertual" class="default_button" value="选择"/>
                                </td>
                                <td id="msg_nick" class="detail_table_value_td">*必选</td>
                            </tr>
                            <tr>
                                <td class="edit_table_defaulttitle_td" width="100" height="1">回复内容</td>
                                <td class="edit_table_value_td">
                                    <textarea cols="35" rows="10" name="replytext"></textarea>
                                </td>
                                <td id="msg_icon" class="detail_table_value_td">*必填</td>
                            </tr>
                            <tr>
                                <td height="1" colspan="3" class="edit_table_value_td" align="center"><input
                                        type="button" name="button" id="sub" class="default_button" value="提交"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>