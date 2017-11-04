<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>通知列表</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script language="JavaScript" type="text/JavaScript">
        $(document).ready(function () {
            $('#form_submit_search').submit(function(){
                var text = $('#input_text').val().trim();
                if(text.length > 15){
                    alert("最多15个字");
                    return false;
                }
            });
            $("[name=send_click]").click(function () {
                if (this.style.color == 'grey') {
                    this.disabled = true;
                    return false;
                } else {
                    this.style.color = 'grey';
                }
            })
        });
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td height="22" class="page_navigation_td">>> 运营维护 >> 着迷手游画报管理 >> 玩霸消息中心列表</td>
    </tr>
    <tr>
        <td height="100%" valign="top"><br>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="list_table_header_td">>消息中心列表</td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <c:if test="${fn:length(errorMsg)>0}">
                    <tr>
                        <td height="1" colspan="14" class="error_msg_td">${errorMsg}</td>
                    </tr>
                </c:if>
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                <form action="/joymeapp/push/list" method="post" id="form_submit_search">
                    <tr>
                        <td width="80" align="center">搜索条件</td>
                        <input type="hidden" name="appid" value="${appId}"/>
                        <td>
                            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                                <tr>
                                    <td width="80" align="right" class="edit_table_defaulttitle_td">通知类型：</td>
                                    <td>
                                        <select name="pushlisttype" id="select_pushlisttype">
                                            <option value="0"
                                            <c:if test="${pushListType == 0}">selected="selected"</c:if>>推送消息
                                            </option>
                                            <option value="1"
                                            <c:if test="${pushListType == 1}">selected="selected"</c:if>>系统通知
                                            </option>
                                        </select>
                                    </td>
                                    <td width="80" align="right" class="edit_table_defaulttitle_td">信息搜索：</td>
                                    <td>
                                        <input type="text" name="searchtext" value="${searchText}" id="input_text"/>
                                    </td>
                                    <c:if test="${pushListType == 1}">
                                        <td width="80" align="right" class="edit_table_defaulttitle_td">排序：</td>
                                        <td>
                                            <select name="ordertype">
                                                <option value="createdate">创建时间
                                                </option>
                                                <option value="senddate">发布时间
                                                </option>
                                            </select>
                                        </td>
                                    </c:if>
                                </tr>
                            </table>
                        </td>
                        <td width="80" align="center">
                            <input name="Button" type="submit" class="default_button" value=" 搜索 ">
                        </td>
                    </tr>
                </form>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="20%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <form action="/joymeapp/push/createpage" method="post">
                            <input type="hidden" name="appid" value="${appId}"/>
                            <input type="hidden" name="pushlisttype" value="0">
                            <input type="submit" value="创建一条推送消息"/>
                        </form>
                    </td>
                    <td>
                        <form action="/joymeapp/push/createpage" method="post">
                            <input type="hidden" name="appid" value="${appId}"/>
                            <input type="hidden" name="pushlisttype" value="1">
                            <input type="submit" value="创建一条系统通知"/>
                        </form>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td height="1" class="default_line_td"></td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <tr class="list_table_title_tr">
                    <td nowrap width="30">消息ID</td>
                    <td width="30">消息ICON</td>
                    <td nowrap align="center" width="100">消息标题</td>
                    <td nowrap align="center" width="100">消息信息</td>
                    <td nowrap align="center" width="160">额外信息</td>
                    <td nowrap align="center" width="80">添加人</td>
                    <td nowrap align="center" width="80">APP</td>
                    <td nowrap align="center" width="80">平台</td>
                    <td nowrap align="center" width="80">状态</td>
                    <td nowrap align="center" width="100">操作</td>
                    <td nowrap aligh="center" width="100">类型</td>
                    <td nowrap align="center" width="100">创建信息</td>
                </tr>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                <c:choose>
                    <c:when test="${list.size() > 0}">
                        <c:forEach items="${list}" var="msg" varStatus="st">
                            <tr class="<c:choose><c:when test="
                            ${st.index % 2 == 0}">list_table_opp_tr</c:when><c:otherwise>list_table_even_tr</c:otherwise></c:choose>">
                <td nowrap>${msg.pushMessage.pushMsgId}</td>
                <td nowrap>${msg.pushMessage.msgIcon}</td>
                <td nowrap>${msg.pushMessage.msgSubject}</td>
                <td nowrap><textarea readonly="readonly" cols="20"
                                     rows="3">${msg.pushMessage.shortMessage}</textarea></td>
                <td nowrap>${msg.pushMessage.options.toJson()}</td>
                <td nowrap>${msg.pushMessage.createUserid}</td>
                <td nowrap>${msg.appName}</td>
                <td nowrap>
                    <c:choose>
                        <c:when test="${pushListType == 0 || pushListType == 3 || pushListType == 4}">
                            <c:choose>
                                <c:when test="${msg.pushMessage.appPlatform.code == 0}">
                                    IOS-appstore
                                </c:when>
                                <c:when test="${msg.pushMessage.appPlatform.code == 1}">
                                    安卓
                                </c:when>
                                <c:when test="${msg.pushMessage.appPlatform.code == 2}">
                                    IOS-企业版
                                </c:when>
                            </c:choose>
                        </c:when>
                        <c:when test="${pushListType == 1}">
                            <c:choose>
                                <c:when test="${msg.pushMessage.appPlatform.code == 0}">
                                    IOS
                                </c:when>
                                <c:when test="${msg.pushMessage.appPlatform.code == 1}">
                                    安卓
                                </c:when>
                            </c:choose>
                        </c:when>
                    </c:choose>
                </td>
                <td nowrap>
                    <c:choose>
                        <c:when test="${pushListType == 0 || pushListType == 3 || pushListType == 4}">
                            <c:choose>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'n'}">
                                    初始
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'rj'}">
                                    等待
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'er'}">
                                    <span style="color: red">失败</span>
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'ing'}">
                                    发送中
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'y'}">
                                    <span style="color: #008000">已发送</span>
                                </c:when>
                            </c:choose>
                        </c:when>
                        <c:when test="${pushListType == 1}">
                            <c:choose>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'n'}">
                                    <span>初始</span>
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'ing'}">
                                    <span style="color: #008000">使用中</span>
                                </c:when>
                            </c:choose>
                        </c:when>
                    </c:choose>
                </td>
                <td nowrap>
                    <c:choose>
                        <c:when test="${pushListType == 0 || pushListType == 3 || pushListType == 4}">
                            <c:choose>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'n'}">
                                    <a href="/joymeapp/push/send?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}"
                                       name="send_click">推送</a>
                                    <a href="/joymeapp/push/modifypage?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">编辑</a>
                                    <a href="/joymeapp/push/remove?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">删除</a>
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'rj'}">
                                    <a href="/joymeapp/push/detail?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">查看</a>
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'ing'}">
                                    <a href="/joymeapp/push/detail?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">查看</a>
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'y'}">
                                    <a href="/joymeapp/push/detail?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">查看</a>
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'er'}">
                                    <a href="/joymeapp/push/detail?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">查看</a>
                                </c:when>
                            </c:choose>
                        </c:when>
                        <c:when test="${pushListType == 1}">
                            <c:choose>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'n'}">
                                    <a href="/joymeapp/push/publish?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&platform=${msg.pushMessage.appPlatform.code}&searchtext=${searchText}">发布</a>
                                    <a href="/joymeapp/push/modifypage?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">编辑</a>
                                    <a href="/joymeapp/push/remove?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">删除</a>
                                    <a href="/joymeapp/push/createpage?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=0&icon=${msg.pushMessage.msgIcon}&subject=${msg.pushMessage.msgSubject}&shortmessage=${msg.pushMessage.shortMessage}&rtype=${msg.pushMessage.options.list.get(0).type}&info=${msg.pushMessage.options.list.get(0).info}&platform=${msg.pushMessage.appPlatform.code}">生成一条推送消息</a>
                                </c:when>
                                <c:when test="${msg.pushMessage.sendStatus.code == 'ing'}">
                                    <a href="/joymeapp/push/modifypage?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">编辑</a>
                                    <a href="/joymeapp/push/remove?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=${msg.pushMessage.pushListType.code}&searchtext=${searchText}">删除</a>
                                    <a href="/joymeapp/push/createpage?msgid=${msg.pushMessage.pushMsgId}&appid=${appId}&pushlisttype=0&icon=${msg.pushMessage.msgIcon}&subject=${msg.pushMessage.msgSubject}&shortmessage=${msg.pushMessage.shortMessage}&rtype=${msg.pushMessage.options.list.get(0).type}&info=${msg.pushMessage.options.list.get(0).info}&platform=${msg.pushMessage.appPlatform.code}">生成一条推送消息</a>
                                </c:when>
                            </c:choose>
                        </c:when>
                    </c:choose>
                </td>
                <td nowrap><fmt:message key="app.push.list.type.${msg.pushMessage.pushListType.code}" bundle="${def}"/>
                </td>
                <td>
                    <fmt:formatDate value="${msg.pushMessage.createDate}"
                                    pattern="yyyy-MM-dd HH:mm:ss"/><br/>${msg.pushMessage.createUserid}
                </td>
                </tr>
                </c:forEach>
                <tr>
                    <td height="1" colspan="10" class="default_line_td"></td>
                </tr>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="10" class="error_msg_td">暂无数据!</td>
                    </tr>
                </c:otherwise>
                </c:choose>
                <tr>
                    <td colspan="10" height="1" class="default_line_td"></td>
                </tr>
                <c:if test="${page.maxPage > 1}">
                    <tr class="list_table_opp_tr">
                        <td colspan="10">
                            <pg:pager url="/joymeapp/push/list"
                                      items="${page.totalRows}" isOffset="true"
                                      maxPageItems="${page.pageSize}"
                                      export="offset, currentPageNumber=pageNumber" scope="request">
                                <pg:param name="appid" value="${appId}"/>
                                <pg:param name="pushlisttype" value="${pushListType}"/>
                                <pg:param name="searchtext" value="${searchText}"/>
                                <pg:param name="maxPageItems" value="${page.pageSize}"/>
                                <pg:param name="items" value="${page.totalRows}"/>
                                <%@ include file="/WEB-INF/jsp/toolspg.jsp" %>
                            </pg:pager>
                        </td>
                    </tr>
                </c:if>
            </table>
        </td>
    </tr>
</table>
</body>
</html>