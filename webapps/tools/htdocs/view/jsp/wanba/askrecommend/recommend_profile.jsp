<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>标签达人推荐</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {

            $('#form_submit').bind('submit', function () {

                var checkpid = false;
                var pidArr = new Array();
                var i=0;
                $("input[name='pid']").each(function(index,item){
                    var val = $.trim($(this).val());
                    if(val!=""){
                        for(var j=0;j<pidArr.length;j++){
                            if(pidArr[j]==val){
                                alert("有相同的元素请检查");
                                checkpid = true;
                                return false;
                            }
                        }
                        pidArr.push(val);
                    }
                });

                if(checkpid){
                    return false;
                }

            });
        });
    </script>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 玩霸-问答 >>标签达人推荐</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">标签达人推荐</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(msg)>0}">
                    <tr>
                        <td height="1" colspan="10" class="error_msg_td">
                                ${msg}
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <form action="/wanba/askrecommend/addrecommendprofile" method="post" id="form_submit">
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <c:forEach items="${strings}" var="dto" varStatus="st">
                        <tr>
                            <td height="1" class="default_line_td td_cent" width="100">
                                嘉宾${st.index+1}:
                            </td>
                            <td height="1">
                                <input  type="text" name="pid" size="48" value="${dto}"/>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:forEach items="${emptyList}" var="dto" varStatus="st">
                        <tr>
                            <td height="1" class="default_line_td td_cent" width="100">
                                嘉宾:
                            </td>
                            <td height="1">
                                <input  type="text" name="pid" size="48" value="${dto}"/>
                            </td>
                            <td height="1" class=>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr align="center">
                        <td colspan="3">
                            <input name="Submit" type="submit" class="default_button" value="提交">
                            <input name="Reset" type="button" class="default_button" value="返回"
                                   onclick="javascipt:window.history.go(-1);">
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>
</table>
</body>
</html>