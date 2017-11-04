<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title></title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <style type="text/css">
        .td_cent {
            text-align: center;
            vertical-align: middle
        }
    </style>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {
//                if ($.trim($("#desc").val()) == "") {
//                    $("#desc").focus();
//                    alert('请填写帖子内容');
//                    return false;
//                }
                if ($("#comment_num").val() != "") {
                    if (isNaN($("#comment_num").val())) {
                        alert("评论数需要填写数字。");
                        $("#comment_num").focus();
                        return false;
                    }
                }
                if ($("#red_num").val() != "") {
                    if (isNaN($("#red_num").val())) {
                        alert("阅读数需要填写数字。");
                        $("#red_num").focus();
                        return false;
                    }
                }
                if ($("#agree_num").val() != "") {
                    if (isNaN($("#agree_num").val())) {
                        alert("点赞数需要填写数字。");
                        $("#agree_num").focus();
                        return false;
                    }
                }
            });
        });
    </script>
</head>
<body>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸迷友圈主贴</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">玩霸迷友圈主贴</td>
                </tr>
            </table>
            <form action="/comment/bean/modify" method="post" id="form_submit">
                <input type="hidden" name="commentid" size="48" value="${tagDedearchives.dede_archives_id}"/>
                <input type="hidden" name="id" size="48" value="${tagDedearchives.id}"/>
                <input type="hidden" name="checkTagId" size="48" value="${checkTagId}"/>
                <input type="hidden" name="pageofferset" size="48" value="${pageofferset}"/>

                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td height="1" colspan="3" class="default_line_td"></td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent" width="100">
                            帖子内容
                        </td>
                        <td height="1">
                            <textarea name="desc" id="desc" cols="50" rows="10">${commentBean.description}</textarea>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            评论数
                        </td>
                        <td height="1">
                            <input id="comment_num" type="text" name="comment_num"
                                   onkeyup="value=value.replace(/[^\d]/g,'')" size="48"
                                   value="${commentBean.commentSum}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            阅读数
                        </td>
                        <td height="1">
                            <input id="red_num" type="text" name="red_num" onkeyup="value=value.replace(/[^\d]/g,'')"
                                   size="48"
                                   value="${commentBean.longCommentSum}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            点赞数
                        </td>
                        <td height="1">
                            <input id="agree_num" type="text" name="agree_num"
                                   onkeyup="value=value.replace(/[^\d]/g,'')" size="48"
                                   value="${commentBean.scoreCommentSum}"/>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>
                    <tr>
                        <td height="1" class="default_line_td td_cent" width="150">
                            所属标签
                        </td>
                        <td height="1">
                            <c:forEach items="${animeTagList}" var="tag">
                                <input type="checkbox" value="${tag.tag_id}" name="tag_id"
                                       <c:forEach
                                               items="${tagDedearchiveses}" var="archive">
                                       <c:if
                                               test="${tag.tag_id==archive.tagid}">checked</c:if>
                                </c:forEach>> ${tag.tag_name}<br>
                            </c:forEach>
                        </td>
                        <td height="1" class=>
                        </td>
                    </tr>

                    <tr>
                        <td height="1" class="default_line_td td_cent">
                            是否显示推荐标签：
                        </td>
                        <td height="1" class="">
                            <c:if test="${commentBean.oneUserSum==0}">
                                <input type="radio" name="oneUserSum" value='0' checked="checked"/>否
                                <input type="radio" name="oneUserSum" value='-1'/>是
                            </c:if>
                            <c:if test="${commentBean.oneUserSum==-1 || commentBean.oneUserSum==1}">
                                <input type="radio" name="oneUserSum" value='0'/>否
                                <input type="radio" name="oneUserSum" value='-1' checked="checked"/>是
                            </c:if>
                        </td>
                    </tr>


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