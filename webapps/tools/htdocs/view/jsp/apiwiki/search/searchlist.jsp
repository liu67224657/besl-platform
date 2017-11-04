<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>搜索推荐</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

            $('a[name=del]').click(function () {
                if (confirm('删除操作不可逆,确定删除?')) {
                    var searchtext = $(this).attr('data-text');
                    $.post("/apiwiki/search/save", {method: 'del', searchtext: searchtext}, function (req) {
                        window.location.reload();
                    });
                }
            });

            $('a[name=sort]').click(function () {
                var searchtext = $(this).attr('data-text');
                var sort = $(this).attr('data-sort');
                var otherText="";
                if(sort=="up"){
                    otherText=$(this).parent().parent().prev().find("td:first").find('span').html();
                }else{
                    otherText=$(this).parent().parent().next().find("td:first").find('span').html();
                }
                if(otherText==null){
                    if(sort=="up"){
                        alert("已经是第一条记录");
                        return false;
                    }else{
                        alert("已经是最后一条记录");
                        return false;
                    }
                }
                $.post("/apiwiki/search/save", {method: 'sort', searchtext: searchtext,othertext:otherText}, function (req) {
                    window.location.reload();
                })

            });

            $('#add_search').bind('click', function () {
                var searchText = $.trim($("#search").val());
                if (searchText == "") {
                    alert("关键字不能为空");
                    return false;
                }
                if (searchText.length > 15) {
                    alert("关键字不能超过15个字");
                    return false;
                }

                var checkpid = false;
                $("span[name='pid']").each(function (index, item) {
                    var val = $.trim($(this).html());
                    if (val == searchText) {
                        checkpid = true;
                        return false;
                    }
                });
                if (checkpid) {
                    alert("关键字已存在，请检查");
                    return false;
                }

                $.post("/apiwiki/search/save", {method: 'add', searchtext: searchText}, function (req) {
                    window.location.reload();
                })

            });
        });
    </script>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> Joymewiki管理 >>搜索推荐</td>
    </tr>
    <tr>
        <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td><input name="search" id="search" placeholder="关键字限15个字" size="50"/>
                        <input id="add_search" type="button" class="default_button" value="添加"></td>
                </tr>

            </table>

            <table width="500px" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="3" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap align="center" width="300px">关键字</td>
                    <td nowrap align="center" width="200px">操作</td>
                </tr>

                <c:forEach items="${listkey}" var="dto" varStatus="st">
                    <tr class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                        <td height="1" align="center">
                            <span name="pid">${dto.key}</span>
                        </td>
                        <td height="1" align="center">
                            <a href="javascript:void(0);" name="sort" data-text="${dto.key}" data-sort="up"><img
                                    src="/static/images/icon/up.gif"></a>&nbsp;&nbsp;
                            <a href="javascript:void(0);" name="sort" data-text="${dto.key}" data-sort="down"><img
                                    src="/static/images/icon/down.gif"></a>&nbsp;&nbsp;
                            <a href="javascript:void(0);" name="del" data-text="${dto.key}">删除</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
</table>
</body>
</html>