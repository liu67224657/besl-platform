<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html>
<head>
    <link rel='stylesheet' href='${ctx}/static/include/css/defaultmain.css' type='text/css'/>
    <script language=JavaScript src="/static/include/js/checkValue.js"></script>
    <script type="text/javascript" src="/static/include/msgbox/ymPrompt.js"></script>
    <script src="/static/include/js/jquery.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            $(".blue_center tr:even").addClass("ouh");
            $(".blue_center tr").mouseover(function() {
                $(this).addClass("over");
            })
                    .mouseout(function() {
                        $(this).removeClass("over");
                    })

        });


    </script>
    <script type="text/javascript">
        //chechbox全部选择
        function selectAll(obj, itemName) {
            var boxes = document.getElementsByName(itemName);
            for (var i = 0; i < boxes.length; i++) {
                boxes[i].checked = obj.checked;
            }
        }

        function saveForm() {
            var uno = $("#uno").val();
            var ids = "";

            $.each($("input[id='opid_checkbox']"), function(i, val) {
                if (val.checked) {
                    ids = val.value + "," + ids;
                }
            })
            $.ajax({
                        type:"POST",
                        url:"/privilege/user/saveuserroles",
                        data:"uno=" + uno + "&ids=" + ids,
                        success:function(data) {
                            var resultJson = eval('(' + data + ')');
                            if (resultJson.status_code == '1') {
                                alert("保存角色成功！");
                            } else {
                                alert("保存角色失败！");
                            }
                        }
                    }
            )
        }

    </script>
</head>

<body>
<div class="title">
    系统管理>> 权限管理>> 管理员管理
</div>
<form id="fm" name="fm" action="/privilege/user/saveuserroles" method="post">
    <input type="hidden" name="opid_hidden" id="opid_hidden"/>
    <input type="hidden" name="uno" id="uno" value="${entity.uno}"/>
    <TABLE class="blue_center">
        <thead>
        <TD>UNO</TD>
        <TD>用户ID</TD>
        <TD>用户名称</TD>
        <TD>状态</TD>
        </thead>
        <tr class="listTableTrValue">
            <td align="left">${entity.uno}</td>
            <td align="left">${entity.userid}</td>
            <td align="left">${entity.username}</td>
            <td align="left">
                <c:if test="${entity.ustatus.code=='y'}">可用</c:if>
                <c:if test="${entity.ustatus.code=='n'}">不可用</c:if>
            </td>
        </tr>
    </TABLE>

    <table class="titleTable">
        <TR class="titleTable">
            <TD class="titleTableTdR" align="right">
                <p:privilege name="privilege/user/saveuserroles">
                    <INPUT type="button" name="button" value="确 定" class="input_submit" onclick="saveForm()"/>
                </p:privilege>
                &nbsp;
                <input class="input_submit" type="button" name="bt_continue" value="关 闭"
                       onclick="javascript:window.close();">
            </TD>
        </TR>
    </table>

    <div id="mltips" align="center">
        <c:if test="${message != null}">

            <fmt:message key="${message}" bundle="${userProps}">${message}</fmt:message>
        </c:if>
    </div>
    <TABLE class="blue_center">
        <tr class="listTableTrValue">
            <thead>
            <td><input type="checkbox" id="checkAll_bom" name="checkAll_bom"
                       onclick="selectAll(this, 'opid_checkbox')"/>选项
            </td>
            <td>角色名字</td>
            <td>角色描述</td>
            <td>角色类型</td>
            <td>角色状态</td>
            </thead>
            <c:forEach items="${listRoles}" var="ct" varStatus="st">
        <tr class="listTableTrValue">
            <td>
                <input type="checkbox" name="opid_checkbox" id="opid_checkbox" value="${ct.rid}"
                        <c:forEach items="${entity.privilegeRoleses}" var="privilegeRole">
                            <c:if test="${ct.rid==privilegeRole.rid}">checked</c:if>
                        </c:forEach> />
            </td>
            <td>
                    ${ct.roleName}
            </td>
            <td>
                    ${ct.description}
            </td>
            <td>
                <table>
                    <c:if test="${ct.type.code==1}">菜单</c:if>
                    <c:if test="${ct.type.code==2}">动作</c:if>
                </table>
            </td>
            <td>
                <c:if test="${ct.status.code=='y'}">可用</c:if>
                <c:if test="${ct.status.code=='n'}">不可用</c:if>
            </td>

        </tr>
        </c:forEach>

    </TABLE>

</form>
</body>

</html>
