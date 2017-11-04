<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="errordef"/>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加点评</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>

    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.min.js"></script>
    <script type="text/javascript" src="/static/include/js/easyui/jquery.easyui.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/static/include/js/easyui/themes/default/easyui.css"/>
    <script type="text/javascript" src="/static/include/js/easyui/locale/easyui-lang-zh_CN.js"></script>

    <script>
        $(document).ready(function () {
            $('#formPost').bind('submit', function () {
                var uid = $("#vertualUid").val();
                if (uid == '') {
                    alert("请选择虚拟用户");
                    return false;
                }

                var gameId = $("#gameId").val();
                if (gameId == '') {
                    alert("请选择游戏");
                    return false;
                }

                var score = $("[name='score']").val();
                if (score == '') {
                    alert("请输入评分");
                    return false;
                }
                if (isNaN(score)) {
                    alert("评分只能是数字");
                    return false;
                }
                if (score < 1 || score > 10) {
                    alert("只能输入10及10以内的数字");
                    return false;
                }

                var body = $("[name='body']").val();
                var strlen = isChinese($("[name='body']"));
                strlen = Math.ceil(strlen / 2);

                if (strlen > 140) {
                    alert("最多只能输入140个字，现在字数" + strlen);
                    return false;
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

            $("#addGame").click(function () {
                $("#reset").click(function () {
                    $('.easyui-datetimebox').datetimebox('clear');
                    $("#form_submit :text").prop("value", '');
                    $("#form_submit input[name='gameDbId']").prop("value", '');
                });

                $('#dgrulesGame').datagrid({
                    url: '/apiwiki/comment/get/gameList',
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
                            {field: 'id', title: 'id', width: 60},
                            {field: 'name', title: '游戏名称', width: 100},
                            {field: 'aliasName', title: '别名', width: 100},
                            {field: 'gameTag', title: '标签', width: 100},
                            {field: 'ck', checkbox: true}
                        ]
                    ]
                });

                $('#chooseGameWindow').window('open')
            });


            $("#buttonInWindow").click(function () {
                var type = $("#gameType").val();
                var url = '/apiwiki/comment/get/gameList';
                if ($.trim($("#gameNameInWindow").val()) != '') {
                    url += '?searchText=' + $.trim($("#gameNameInWindow").val() + "&type=" + type);
                }
                $('#dgrulesGame').datagrid('options').url = url;
                $("#dgrulesGame").datagrid('reload');

            });

            //弹窗中选择一个游戏后
            $("#selectone").click(function () {
                var item = $('#dgrules').datagrid('getSelected');
                var id = item.id;
                var nick = item.nick;
                $("#vertualName").text(nick);
                $("#vertualUid").val(id);
                $('#chooseWindow').window('close');
            });

            $("#selectGame").click(function () {
                var item = $('#dgrulesGame').datagrid('getSelected');
                var id = item.id;
                var nick = item.name;
                $("#gameName").text(nick);
                $("#gameId").val(id);
                $('#chooseGameWindow').window('close');
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

<div id="chooseGameWindow" class="easyui-window" title="请选择游戏"
     data-options="modal:true,closed:true,collapsible:false,minimizable:false,maximizable:false"
     style="width:750px; top:50px;">
    <select id="gameType">
        <option value="2">游戏名称</option>
        <option value="1">游戏ID</option>
    </select>
    <input type="text" id="gameNameInWindow" size="10"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" id="buttonInWindow" value="查询"/>
    <table id="dgrulesGame" data-options="
              rownumbers:false,
              autoRowHeight:true,
              pagination:true">
    </table>
    <div style="text-align:center">
        <div class=" clear mt20" style=" margin:0 auto;">
            <input type="button" onclick="$('#chooseGameWindow').window('close');" value="取消"/>
            <input type="button" value="选择" id="selectGame" value="保存"/></div>
    </div>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 点评详情 >> 添加点评</td>
    </tr>
    <tr>

        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">添加点评</td>
                </tr>
                <tr>
                    <td>
                        <form action="/apiwiki/comment/postcomment" method="post" id="formPost">
                            <table border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td height="1" colspan="3" class="edit_table_header_td">
                                        <c:if test="${not empty error}">
                                            <span style="color:red">
                                                     <fmt:message key="${error}" bundle="${errordef}"/>
                                            </span>
                                        </c:if>
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
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">选择游戏</td>
                                    <td class="edit_table_value_td">
                                        <span id="gameName">未选择</span>
                                        <input type="hidden" value="" id="gameId" name="gameid"/>
                                        <input type="button" id="addGame" class="default_button" value="选择"/>
                                    </td>
                                    <td class="detail_table_value_td">*必选</td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">输入评分</td>
                                    <td class="edit_table_value_td">
                                        <input type="text" name="score"/>
                                    </td>
                                    <td class="detail_table_value_td">*必填 只能输入10及10以内的数字</td>
                                </tr>
                                <tr>
                                    <td class="edit_table_defaulttitle_td" width="100" height="1">输入评价</td>
                                    <td class="edit_table_value_td">
                                        <textarea cols="35" rows="10" name="body"></textarea>
                                    </td>
                                    <td id="msg_icon" class="detail_table_value_td">*必填</td>
                                </tr>
                                <tr>
                                    <td height="1" colspan="3" class="edit_table_value_td" align="center"><input
                                            type="submit" name="button" id="sub" class="default_button" value="提交"/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>