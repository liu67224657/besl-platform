<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>添加推荐游戏</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>
    <style type="text/css">
        .raceShow {
            background-color: #ccc; /* #ffffff*/
            border: solid 1px #ccc;
            position: absolute;
            display: none;
            width: 500px;
            height: 300px;
            padding: 5px;
            top: 100px;
            left: 30%
        }
    </style>
    <script>
        $(document).ready(function () {
            $('#form_submit').bind('submit', function () {


            });
            //弹出层
            $("#btnShowDiv").click(function (event) {
                $("#racePop").show(300);
            });
            //隐藏层
            $("#aClose").click(function (event) {
                $("#racePop").hide(300);
            });
            $("#rClose").click(function (event) {
                $("#recoverDiv").hide(300);
            });

            $("#btnRecoverDiv").click(function () {
                $("#recoverDiv").show(300);
            });

        });
        function deleteReply(id, commentId, rid) {
            if (confirm("是否删除该评论")) {
                window.location.href = "/apiwiki/comment/delete/reply?id=" + id + "&commentId=" + commentId + "&rid=" + rid;
            }
        }
        function updateHighQuality(id, status) {
            window.location.href = "/apiwiki/comment/updatehighQuality?id=" + id + "&status=" + status;
        }

        function deleteComment(id) {
            var reason = $("#deleteReason").val();
            var strlen = isChinese($("#deleteReason"));
            strlen = Math.ceil(strlen / 2);
            if (strlen <= 1) {
                alert("最少要输入两个字");
                return false;
            }
            if (strlen > 140) {
                alert("最多只能输入140个字，现在字数" + strlen);
                return false;
            }
            if (confirm("确认删除该点评，原因为：" + reason)) {
                window.location.href = "/apiwiki/comment/deleteComment?id=" + id + "&reason=" + reason;
            }
        }

        function recoverComment(id, uid, gameId) {
            var reason = $("#recoverReason").val();
            var strlen = isChinese($("#recoverReason"));
            strlen = Math.ceil(strlen / 2);
            if (strlen <= 1) {
                alert("最少要输入两个字");
                return false;
            }
            if (strlen > 140) {
                alert("最多只能输入140个字，现在字数" + strlen);
                return false;
            }
            if (confirm("确认恢复该点评，原因为：" + reason)) {
                $.ajax({
                    type: "POST",
                    url: "/apiwiki/comment/recoverComment",
                    dataType: "json",
                    data: {id: id, reason: reason, uid: uid, gameId: gameId},
                    success: function (req) {
                        var result = req;
                        if (result.rs == 1) {
                            window.location.href = "/apiwiki/comment/list";
                        } else {
                            if (result.error == 'error.comment.has.exist') {
                                alert("该用户在此游戏下已经有其他点评存在，无法恢复");
                            } else if (result.error == 'only.recover.once') {
                                alert("每条点评只能恢复一次");
                            }
                        }
                    }
                });
            }
        }

        function isChinese(str) { //判断是不是中文
            var oVal = str.val();
            var oValLength = 0;
            oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
            return oValLength;

        }
    </script>
    <style type="text/css">

    </style>
</head>
<body>
<div id="racePop" class="raceShow">
    <div class="div" style="width: 200px; height:100px;">
        <a id="aClose" style="cursor:pointer;">[关闭]</a>
        <h2> 删除点评</h2>
        <textarea cols="60" rows="10" id="deleteReason"></textarea>
        <input type="button" onclick="deleteComment('${id}');" value="删除并通知用户"/>
    </div>
</div>

<div id="recoverDiv" class="raceShow">
    <div class="div" style="width: 200px; height:100px;">
        <a id="rClose" style="cursor:pointer;">[关闭]</a>
        <h2> 恢复点评</h2>
        <textarea cols="60" rows="10" id="recoverReason"></textarea>
        <input type="button"
               onclick="recoverComment('${id}','${commentDetailDTO.comment.uid}','${commentDetailDTO.comment.gameId}');"
               value="恢复并通知用户"/>
    </div>
</div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >>点评管理 >> 点评详情</td>
    </tr>
    <tr>
        <td valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td"><c:choose>
                        <c:when test="${status eq 'valid'}">
                            点评详情
                        </c:when>
                        <c:otherwise>
                            已删除点评详情
                        </c:otherwise>
                    </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table border="0" cellspacing="0" cellpadding="0" width="80%">
                            <tr>
                                <td style="width: 60px;">


                                    <c:choose>
                                        <c:when test="${empty commentDetailDTO.comment.icon }">
                                            <img src="${URL_LIB}/hotdeploy/static/img/wanba_default.png" width="50"
                                                 height="50"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${commentDetailDTO.comment.icon}" width="50" height="50"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div style="line-height: 25px">
                                        <span>作者：${commentDetailDTO.comment.nick}</span>
                                        <br/>
                                        <span> 被点有用次数：${commentDetailDTO.comment.agreeNum}次</span>
                                        <br/>
                                        <span>回复：${commentDetailDTO.comment.replyNum}次</span>
                                        <br/>

                                        发表时间：
                                        <jsp:useBean id="dateValue" class="java.util.Date"/>
                                        <jsp:setProperty name="dateValue" property="time"
                                                         value="${commentDetailDTO.comment.time}"/>
                                        <fmt:formatDate value="${dateValue}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </div>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${status eq 'valid'}">
                                            <c:choose>
                                                <c:when test="${commentDetailDTO.comment.highQuality eq 0}">
                                                    <input type="button"
                                                           onclick="updateHighQuality('${commentDetailDTO.comment.id}',1)"
                                                           value="设为优质评论"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="button"
                                                           onclick="updateHighQuality('${commentDetailDTO.comment.id}',0)"
                                                           value="取消优质评论"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <input type="button" id="btnShowDiv" value="删除点评"/>
                                            <input type="button"
                                                   onclick="window.location.href='/apiwiki/comment/replyCommentPage?id=${commentDetailDTO.comment.id}'"
                                                   value="回复点评"/>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="button"
                                                   id="btnRecoverDiv"
                                                   value="恢复点评"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                            </tr>
                            <tr>
                                <td></td>
                                <td colspan="2">
                                    <div style="width: 100%; height:100%; min-height:100px;padding-top:10px;padding-left:15px; background-color:#eeeeee; ">
                                        内容:${commentDetailDTO.comment.body}
                                        <br/>
                                        <c:if test="${status eq 'removed'}">
                                            删除原因:${reason}
                                        </c:if>
                                    </div>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="80%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="4" class="default_line_td"></td>
                </tr>
                <c:if test="${status eq 'valid'}">
                    <tr class="list_table_title_tr">
                        <td nowrap align="center" width="50px">回复人</td>
                        <td nowrap align="center" width="">时间</td>
                        <td nowrap align="center" width="">内容</td>
                        <td nowrap align="center" width="70px">操作</td>
                    </tr>
                    <tr>
                        <td height="1" colspan="4" class="default_line_td"></td>
                    </tr>
                    <c:choose>
                        <c:when test="${not empty list }">
                            <c:forEach items="${list}" var="dto" varStatus="st">
                                <tr align="center"
                                    class="<c:choose><c:when test="${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                                    <td nowrap>${profileMap[dto.replyProfileId].nick}</td>

                                    <td nowrap class="name">${dto.createTime}</td>
                                    <td nowrap class="name">${dto.body.text}</td>
                                    <td nowrap class="name">
                                        <a href="javascript:deleteReply('${id}','${dto.commentId}','${dto.replyId}');">删除</a>
                                    </td>
                                </tr>
                            </c:forEach>

                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="error_msg_td">暂无数据!</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    <tr>
                        <td colspan="4" height="1" class="default_line_td"></td>
                    </tr>
                    <c:if test="${page.maxPage > 1}">
                        <tr class="list_table_opp_tr">
                            <td colspan="11">
                                <pg:pager url="/apiwiki/comment/createpage"
                                          items="${page.totalRows}" isOffset="true"
                                          maxPageItems="${page.pageSize}"
                                          export="offset, currentPageNumber=pageNumber" scope="request">
                                    <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                    <pg:param name="items" value="${page.totalRows}"/>
                                    <pg:param name="id" value="${id}"/>
                                    <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                                </pg:pager>
                            </td>
                        </tr>
                    </c:if>
                </c:if>
                <tr align="center">
                    <td colspan="3">
                        <input name="Reset" type="button" class="default_button" value="返回"
                               onclick="javascript:window.location.href='/apiwiki/comment/list?status=${status}'">
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>