<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8" %>
    <%@ taglib prefix="s" uri="/struts-tags" %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>角色分配菜单权限</title>
    <link rel='stylesheet' href='${ctx}/static/include/css/defaultmain.css' type='text/css'/>
    <link href="/static/include/css/dtree.css" rel="stylesheet" type="text/css"/>
    <link rel='stylesheet' href='${ctx}/static/include/dhtmltree/css/dhtmlxtree.css' type='text/css'/>
    <script src="${ctx}/static/include/dhtmltree/js/dhtmlxcommon.js" type="text/javascript"></script>
    <script src="${ctx}/static/include/dhtmltree/js/dhtmlxtree.js" type="text/javascript"></script>
    <script src="${ctx}/static/include/dhtmltree/js/dhtmlxtree_json.js" type="text/javascript"></script>
    <script src="/static/include/js/treeview.js" type="text/javascript"></script>

    <script type="text/javascript">
        var youngxmlHttp = null;
        function loadMenuList() {
            tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", -2);
            tree.setImagePath("${ctx}/static/include/dhtmltree/imgs/csh_yellowbooks/");
            tree.enableCheckBoxes(1);
            tree.enableThreeStateCheckboxes(1);//支持选中了孩子，父亲自动被选中
            tree.setDataMode("json");
            tree.loadJSArrayFile("/privilege/roles/systemrolesmenua", loadMenuListBack);
        }
        function loadMenuListBack() {
            document.getElementById("treeboxbox_tree_alt").innerHTML = "正在设置已有菜单，请稍后....";
            try {//异步获取该角色具有的菜单
                youngxmlHttp = getXMLHTTPObj();
                if (youngxmlHttp && youngxmlHttp.readyState != 0) {
                    youngxmlHttp.abort();
                }
                var rid = ${entity.rid};
                youngxmlHttp = getXMLHTTPObj();//获取XMLHTTP对象
                if (youngxmlHttp) {
                    var url = "${ctx}/privilege/roles/loaduserrolemenudata";
                    var params = "rid=" + rid;
                    //打开连接
                    youngxmlHttp.open("post", url, true);
                    //设置回调函数
                    youngxmlHttp.onreadystatechange = setTreeBack;
                    //发送请求
                    youngxmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                    youngxmlHttp.send(params);
                } else {
                    alert("异步获取该角色具有的菜单，XMLHTTP对象创建失败");
                }
            } catch(e) {
                alert("异步获取该角色具有的菜单失败");
            }
            tree.setCheck(tree.getSelectedItemId(), true);
        }

        function setTreeBack() {
            if (youngxmlHttp.readyState == 4) {
                var reslut = eval(youngxmlHttp.responseText);
                if (reslut.length > 0) {
                    for (var i = 0; i < reslut.length; i++) {
                        var itemid = reslut[i];
                        if (!tree.hasChildren(itemid)) {//如果这个节点没有孩子节点，才选中它，这样会自动也把他的所有父节点都选中
                            tree.setCheck(reslut[i], true);
                            tree.openItem(reslut[i]);
                        }
                    }
                    tree.openItem(-1);
                    document.getElementById("treeboxbox_tree_alt").innerHTML = "请查看菜单";
                } else {
                    document.getElementById("treeboxbox_tree_alt").innerHTML = "<font color='red'> 该角色没有分配资源</font>";
                }
            }

        }

        //保存选中的菜单
        function saveMenu() {
            //var menus = tree.getAllChecked();//这个方法只能取到所选中的孩子节点，取不到目录
            var menus = tree.getAllCheckedBranches();//可以取到所有的选中的节点包括目录
//            if (menus == null || menus.length == 0) {
//                alert("请选择菜单");
//                return;
//            }

            document.getElementById("opmessage").innerHTML = "正在保存，请稍后....";
            var rid = ${entity.rid};
            try {
                youngxmlHttp = getXMLHTTPObj();
                if (youngxmlHttp && youngxmlHttp.readyState != 0) {
                    youngxmlHttp.abort();
                }
                youngxmlHttp = getXMLHTTPObj();//获取XMLHTTP对象
                if (youngxmlHttp) {
                    var time = Math.random();
                    //构造查询连接字符串
                    var url = "${ctx}/privilege/roles/saverolesmenu";
                    //alert("rid==" + rid + "  menus===" + menus);
                    var params = "rid=" + rid + "&menuids=" + menus;
                    //打开连接
                    youngxmlHttp.open("post", url, true);
                    //设置回调函数
                    youngxmlHttp.onreadystatechange = saveMenuBack;
                    //发送请求
                    youngxmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                    youngxmlHttp.send(params);
                    document.getElementById("submit1").disabled = true;
                    document.getElementById("submit2").disabled = true;
                } else {
                    alert("XMLHTTP对象创建失败");
                }
            } catch(e) {
                alert("保存角色菜单权限失败");
            }
        }
        function saveMenuBack() {
            if (youngxmlHttp.readyState == 4) {
                var reslut = youngxmlHttp.responseText;

                var resultJson = eval('(' + reslut + ')');
//                alert(reslut);
//                alert(resultJson.status_code);
                if (resultJson.status_code == "1") {
                    document.getElementById("opmessage").innerHTML = "保存成功";
                } else {
                    document.getElementById("opmessage").innerHTML = "保存失败";
                    document.getElementById("submit1").disabled = false;
                    document.getElementById("submit2").disabled = false;
                }
            }
        }
        function getXMLHTTPObj() {//得到xmlhttprequest对象
            var C = null;
            try {
                C = new ActiveXObject("Msxml2.XMLHTTP");
            } catch(e) {
                try {
                    C = new ActiveXObject("Microsoft.XMLHTTP");
                } catch(sc) {
                    C = null;
                }
            }
            if (!C && typeof XMLHttpRequest != "undefined") {
                C = new XMLHttpRequest();
            }
            return C;
        }
    </script>
</head>
<body onload="loadMenuList()">
<div class="title">
    系统管理>> 权限管理>> 操作角色管理
</div>
<form id="fm" name="fm" action="" method="post">
    <TABLE class="blue_center">
        <thead>
        <TD>
            角色ID
        </TD>
        <TD>
            角色名字
        </TD>
        <TD>
            角色描述
        </TD>
        <TD>
            角色类型
        </TD>
        <TD>
            角色状态
        </TD>
        </thead>

        <tr class="listTableTrValue">
            <td align="left">${entity.rid}</td>
            <td align="left">${entity.roleName}</td>
            <td align="left">${entity.description}</td>
            <td align="left">
                <c:if test="${entity.type.code == 1}">菜单</c:if>
                <c:if test="${entity.type.code == 2}">菜单Action</c:if>
            </td>
            <td align="left">
                <c:if test="${entity.status.code =='y'}">可用</c:if>
                <c:if test="${entity.status.code =='n'}">不可用</c:if>
            </td>
        </tr>

    </TABLE>
    <table class="titleTable">
        <TR class="titleTable">
            <TD class="titleTableTdR" align="right">
                <INPUT id="submit1" type="button" name="submit" value="确 定" class="input_submit" onclick="saveMenu()"/>
                &nbsp;
                <input class="input_submit" type="button" name="bt_continue" value="关 闭"
                       onclick="javascript:window.close();">
            </TD>
        </TR>
    </table>

    <table class="titleTable">
        <TR class="titleTable">
            <TD>
                菜单资源列表
            </TD>
            <TD>
                <font color="red"> <span id="opmessage"></span></font>
            </TD>
        </TR>
    </table>

    <TABLE class="listTable">
        <TR class="listTableTrTitle">
            <td valign="top">
                <div id="treeboxbox_tree_alt"
                     style="width:auto; height:auto;background-color:#f5f5f5;border :1px solid Silver;">
                    请稍候正在获取数据生成菜单树
                </div>
                <div id="treeboxbox_tree"
                     style="width:auto; height:auto;background-color:#f5f5f5;border :1px solid Silver;">
                </div>
            </td>
        </TR>
    </TABLE>

    <table class="titleTable">
        <TR class="titleTable">
            <TD class="titleTableTdR" align="right">
                <INPUT id="submit2" type="button" name="submit" value="确 定" class="input_submit" onclick="saveMenu()"/>
                &nbsp;
                <input class="input_submit" type="button" name="bt_continue" value="关 闭"
                       onclick="javascript:window.close();">
            </TD>
        </TR>
    </table>
</form>
</body>

</html>
