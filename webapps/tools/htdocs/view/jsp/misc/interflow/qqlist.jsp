<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.tools.default" var="def"/>
<fmt:setBundle basename="hotdeploy.i18n.tools.error" var="error"/>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>贴吧、QQ帐号管理</title>
    <link href="/static/include/css/default.css" rel="stylesheet" type="text/css">
    <script language="JavaScript" type="text/JavaScript" src="/static/include/js/default.js"></script>
    <script type="text/javascript" src="/static/include/js/jquery.js"></script>
    <script type="text/javascript" src="/static/include/js/common.js"></script>

    <script type="text/javascript" src="/static/include/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/fileprogress.js"></script>
    <script type="text/javascript" src="/static/include/swfupload/inputexcelhandler.js"></script>

    <script language="JavaScript" type="text/JavaScript">
        $(document).ready(function () {
            var coustomSwfu = new SWFUpload(coustomSettings);

            $("#excel_file").change(function () {
                var filepath = $(this).val();
                filepath = filepath.substring(filepath.lastIndexOf('.') + 1, filepath.length)
                if (filepath != 'xls' && filepath != 'xlsx') {
                    alert("只能导入.xls或.xlsx格式的文件");
                    return;
                }
                $('#file_path').val($(this).val());
                $('input[name=filepath]').val($(this).val());
            });

            $('#sheet').change(function () {
                $("input[name=sheet]").val($(this).val());
            });

        });

        var coustomSettings = {
            upload_url: "/misc/qq/input",
            post_params: {
                "at": "${at}"
            },

            // File Upload Settings
            file_size_limit: "8 MB",    // 2MB
            file_types: "*.xls",
            file_types_description: "请选择音频文件",
            file_queue_limit: 1,

            file_dialog_complete_handler: fileDialogComplete,

            // Button Settings
            button_image_url: "/static/images/uploadbutton.png",
            button_placeholder_id: "upload_button",
            button_width: 61,
            button_height: 22,
            moving_average_history_size: 40,


            // Flash Settings
            flash_url: "/static/include/swfupload/swfupload.swf",
            flash9_url: "/static/include/swfupload/swfupload_fp9.swf",

            custom_settings: {},
            // Debug Settings
            debug: false}
    </script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td height="22" class="page_navigation_td">>> 运营维护 >> 贴吧/QQ帐号管理 >> QQ群帐号列表</td>
</tr>
<tr>
<td height="100%" valign="top"><br>
<table width="20%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td class="list_table_header_td">QQ帐号列表</td>
        <br/>
        <span style="color: #ff0000">使用前请认真阅读使用文档：</span><a
            href="http://wiki.enjoyf.com/index.php?title=Pcwebtiebaqq#.E4.BD.BF.E7.94.A8.E8.AF.B4.E6.98.8E"
            target="_blank">http://wiki.enjoyf.com/index.php?title=Pcwebtiebaqq#.E4.BD.BF.E7.94.A8.E8.AF.B4.E6.98.8E</a>
    </tr>
</table>
<table width="1000" border="0" cellspacing="0" cellpadding="0">
    <form action="/misc/qq/list" method="post" id="form_submit_search">
        <tr>
            <td width="80" align="center">搜索条件</td>
            <td>
                <table width="100%" border="0" cellspacing="1" cellpadding="0">
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">帐号ID：</td>
                        <td colspan="3">
                            <input type="text" name="qid" value="${qid}"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">QQ号：</td>
                        <td colspan="3">
                            <input type="text" name="qac" value="${qac}" size="100"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">游戏名：</td>
                        <td>
                            <input type="text" name="gname" value="${gname}"/>
                        </td>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">人数：</td>
                        <td>
                            <select name="qunum">
                                <option value="" selected>请选择</option>
                                <option value="0-199" <c:if test="${qunum eq '0-199'}">selected="selected"</c:if>>0-199</option>
                                <option value="200-499" <c:if test="${qunum eq '200-499'}">selected="selected"</c:if>>200-499</option>
                                <option value="500-999" <c:if test="${qunum eq '500-999'}">selected="selected"</c:if>>500-999</option>
                                <option value="1000-2000" <c:if test="${qunum eq '1000-2000'}">selected="selected"</c:if>>1000-2000</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">活跃度：</td>
                        <td>
                            <select name="qlevel">
                                <option value="" selected>请选择</option>
                                <option value="活跃" <c:if test="${qlevel eq '活跃'}">selected="selected"</c:if>>活跃</option>
                                <option value="不活跃" <c:if test="${qlevel eq '不活跃'}">selected="selected"</c:if>>不活跃</option>
                            </select>
                        </td>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">厂商：</td>
                        <td>
                            <input type="text" name="qmanu" value="${qmanu}"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">游戏类型：</td>
                        <td>
                            <select name="gcate">
                                <option value="" selected>请选择</option>
                                <option value="ACT 动作游戏" <c:if test="${gcate eq 'ACT 动作游戏'}">selected="selected"</c:if>>ACT 动作游戏</option>
                                <option value="ARPG动作角色扮演" <c:if test="${gcate eq 'ARPG动作角色扮演'}">selected="selected"</c:if>>ARPG动作角色扮演</option>
                                <option value="AVG 冒险游戏" <c:if test="${gcate eq 'AVG 冒险游戏'}">selected="selected"</c:if>>AVG 冒险游戏</option>
                                <option value="AAVG动作冒险游戏" <c:if test="${gcate eq 'AAVG动作冒险游戏'}">selected="selected"</c:if>>AAVG动作冒险游戏</option>
                                <option value="FPS第一人称射击" <c:if test="${gcate eq 'FPS第一人称射击'}">selected="selected"</c:if>>FPS第一人称射击</option>
                                <option value="FTG 格斗游戏" <c:if test="${gcate eq 'FTG 格斗游戏'}">selected="selected"</c:if>>FTG 格斗游戏</option>
                                <option value="MUG 音乐游戏" <c:if test="${gcate eq 'MUG 音乐游戏'}">selected="selected"</c:if>>MUG 音乐游戏</option>
                                <option value="PUZ 益智类游戏" <c:if test="${gcate eq 'PUZ 益智类游戏'}">selected="selected"</c:if>>PUZ 益智类游戏</option>
                                <option value="RAC 赛车游戏" <c:if test="${gcate eq 'RAC 赛车游戏'}">selected="selected"</c:if>>RAC 赛车游戏</option>
                                <option value="RPG 角色扮演游戏" <c:if test="${gcate eq 'RPG 角色扮演游戏'}">selected="selected"</c:if>>RPG 角色扮演游戏</option>
                                <option value="RTS 即时战略游戏" <c:if test="${gcate eq 'RTS 即时战略游戏'}">selected="selected"</c:if>>RTS 即时战略游戏</option>
                                <option value="SLG 战棋游戏" <c:if test="${gcate eq 'SLG 战棋游戏'}">selected="selected"</c:if>>SLG 战棋游戏</option>
                                <option value="SPG 体育运动游戏" <c:if test="${gcate eq 'SPG 体育运动游戏'}">selected="selected"</c:if>>SPG 体育运动游戏</option>
                                <option value="STG 射击游戏" <c:if test="${gcate eq 'STG 射击游戏'}">selected="selected"</c:if>>STG 射击游戏</option>
                                <option value="AVG恋爱游戏" <c:if test="${gcate eq 'AVG恋爱游戏'}">selected="selected"</c:if>>AVG恋爱游戏</option>
                                <option value="FLY 模拟飞行" <c:if test="${gcate eq 'FLY 模拟飞行'}">selected="selected"</c:if>>FLY 模拟飞行</option>
                                <option value="SIM 模拟经营" <c:if test="${gcate eq 'SIM 模拟经营'}">selected="selected"</c:if>>SIM 模拟经营</option>
                                <option value="卡牌类RPG" <c:if test="${gcate eq '卡牌类RPG'}">selected="selected"</c:if>>卡牌类RPG</option>
                                <option value="其他" <c:if test="${gcate eq '其他'}">selected="selected"</c:if>>其他</option>
                            </select>
                        </td>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">网游/单机：</td>
                        <td>
                            <select name="gtype">
                                <option value="" selected>请选择</option>
                                <option value="网游" <c:if test="${gtype eq '网游'}">selected="selected"</c:if>>网游</option>
                                <option value="单机" <c:if test="${gtype eq '单机'}">selected="selected"</c:if>>单机</option>
                                <option value="其它" <c:if test="${gtype eq '其它'}">selected="selected"</c:if>>其它</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">平台：</td>
                        <td>
                            <select name="qplat">
                                <option value="" selected>请选择</option>
                                <option value="IOS" <c:if test="${qplat eq 'IOS'}">selected="selected"</c:if>>IOS</option>
                                <option value="Android" <c:if test="${qplat eq 'Android'}">selected="selected"</c:if>>Android</option>
                                <option value="I/A双平台" <c:if test="${qplat eq 'I/A双平台'}">selected="selected"</c:if>>I/A双平台</option>
                                <option value="PC" <c:if test="${qplat eq 'PC'}">selected="selected"</c:if>>PC</option>
                                <option value="网页" <c:if test="${qplat eq '网页'}">selected="selected"</c:if>>网页</option>
                                <option value="家用机" <c:if test="${qplat eq '家用机'}">selected="selected"</c:if>>家用机</option>
                                <option value="掌机" <c:if test="${qplat eq '掌机'}">selected="selected"</c:if>>掌机</option>
                                <option value="其他" <c:if test="${qplat eq '其他'}">selected="selected"</c:if>>其他</option>
                            </select>
                        </td>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">题材：</td>
                        <td>
                            <select name="qtheme">
                                <option value="" selected>请选择</option>
                                <option value="动漫" <c:if test="${qtheme eq '动漫'}">selected="selected"</c:if>>动漫</option>
                                <option value="游戏" <c:if test="${qtheme eq '游戏'}">selected="selected"</c:if>>游戏</option>
                                <option value="影视" <c:if test="${qtheme eq '影视'}">selected="selected"</c:if>>影视</option>
                                <option value="其他" <c:if test="${qtheme eq '其他'}">selected="selected"</c:if>>其他</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">负责人：</td>
                        <td>
                            <input type="text" name="qduty" value="${qduty}"/>
                        </td>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">发布区域：</td>
                        <td>
                            <select name="qarea">
                                <option value="" selected>请选择</option>
                                <option value="中国大陆" <c:if test="${qarea eq '中国大陆'}">selected="selected"</c:if>>中国大陆</option>
                                <option value="中国台湾" <c:if test="${qarea eq '中国台湾'}">selected="selected"</c:if>>中国台湾</option>
                                <option value="中国香港" <c:if test="${qarea eq '中国香港'}">selected="selected"</c:if>>中国香港</option>
                                <option value="欧美" <c:if test="${qarea eq '欧美'}">selected="selected"</c:if>>欧美</option>
                                <option value="日韩" <c:if test="${qarea eq '日韩'}">selected="selected"</c:if>>日韩</option>
                                <option value="其他" <c:if test="${qarea eq '其他'}">selected="selected"</c:if>>其他</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="100" align="right" class="edit_table_defaulttitle_td">状态：</td>
                        <td colspan="3">
                            <select name="qstatus">
                                <option value="" selected>请选择</option>
                                <c:choose>
                                    <c:when test="${qstatus eq 'removed'}">
                                        <option value="removed" selected="selected">删除</option>
                                        <option value="valid">可用</option>
                                    </c:when>
                                    <c:when test="${qstatus eq 'valid'}">
                                        <option value="removed">删除</option>
                                        <option value="valid" selected="selected">可用</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="removed">删除</option>
                                        <option value="valid">可用</option>
                                    </c:otherwise>
                                </c:choose>

                            </select>
                        </td>
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
<table width="900" border="0" cellspacing="0" cellpadding="0" height="1">
    <tr>
        <td height="1" width="100">
            <a href="/misc/qq/createpage">
                <p:privilege name="/misc/qq/createpage">
                    <input type="button" class="default_button" value="添加帐号"/>
                </p:privilege>
            </a>
        </td>
        <td height="1" width="600">
            <p:privilege name="/misc/qq/input">
                <span id="upload_button" class="upload_button">Upload</span>
            </p:privilege>

            <p:privilege name="/misc/qq/output">
                <a href="/misc/qq/output?qid=${qid}&qac=${qac}&gname=${gname}&qduty=${qduty}&qtheme=${qtheme}&gtype=${gtype}&gcate=${gcate}&qplat=${qplat}&qarea=${qarea}&qunum=${qunum}&qmanu=${qmanu}&qstatus=${qstatus}&qlevel=${qlevel}"><input
                        type="button" value="Download"/></a>
            </p:privilege>
            <span>*若非Chrome、ie浏览器无法上传、下载，请换Chrome或ie试一试，推荐Chrome。</span>
        </td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td height="1" class="default_line_td"></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="0">
    <c:if test="${fn:length(errorMsg)>0}">
        <tr>
            <td height="1" colspan="19" class="error_msg_td">
                <fmt:message key="${errorMsg}" bundle="${error}"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td height="1" colspan="19" class="default_line_td"></td>
    </tr>
    <tr class="list_table_title_tr">
        <td nowrap width="">帐号ID</td>
        <td nowrap width="">游戏名</td>
        <td nowrap width="">QQ群名称</td>
        <td nowrap width="">QQ群号</td>
        <td nowrap width="">群主</td>
        <td nowrap width="">分类</td>
        <td nowrap width="">负责人</td>
        <td nowrap width="">人数范围</td>
        <td nowrap width="">活跃度</td>
        <td nowrap width="">状态</td>
        <td nowrap width="">操作</td>
        <td nowrap width="">厂商</td>
        <td nowrap width="">游戏类别</td>
        <td nowrap width="">是否网游</td>
        <td nowrap width="">平台</td>
        <td nowrap width="">题材</td>
        <td nowrap width="">发布地区</td>
        <td nowrap width="">创建信息</td>
        <td nowrap width="">修改信息</td>
    </tr>
    <tr>
        <td height="1" colspan="19" class="default_line_td"></td>
    </tr>
    <c:choose>
        <c:when test="${list.size() > 0}">
            <c:forEach items="${list}" var="interflow" varStatus="st">
                <tr
                <c:choose><c:when test="${st.index % 2 == 0}"> class="list_table_opp_tr"</c:when><c:otherwise>
                    class="list_table_even_tr"</c:otherwise></c:choose>>
                <td nowrap>${interflow.interflowId}</td>
                <td nowrap>${interflow.gameName}</td>
                <td nowrap>${interflow.name}</td>
                <td nowrap>${interflow.account}</td>
                <td nowrap>${interflow.lord}</td>
                <td nowrap><c:choose><c:when test="${interflow.type.code == 1}">贴吧</c:when><c:when
                        test="${interflow.type.code == 2}">QQ群</c:when></c:choose></td>
                <td nowrap>${interflow.duty}</td>
                <td nowrap>${interflow.userNumber}</td>
                <td nowrap>${interflow.level}</td>
                <td nowrap>
                    <c:choose>
                        <c:when test="${interflow.removeStatus.code eq 'valid'}">
                            <span style="color: #008000">可用</span>
                        </c:when>
                        <c:when test="${interflow.removeStatus.code eq 'removed'}">
                            <span style="color: #ff0000">删除</span>
                        </c:when>
                    </c:choose>
                </td>
                <td nowrap>
                    <a href="/misc/qq/modifypage?ifid=${interflow.interflowId}">编辑</a>&nbsp;&nbsp;
                    <c:choose>
                        <c:when test="${interflow.removeStatus.code eq 'valid'}"><a
                                href="/misc/qq/remove?ifid=${interflow.interflowId}&qid=${qid}&qac=${qac}&gname=${gname}&qduty=${qduty}&qtheme=${qtheme}&gtype=${gtype}&gcate=${gcate}&qplat=${qplat}&qarea=${qarea}&qunum=${qunum}&qmanu=${qmanu}&qstatus=${qstatus}&qlevel=${qlevel}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}">删除</a></c:when>
                        <c:when test="${interflow.removeStatus.code eq 'removed'}"><a
                                href="/misc/qq/recover?ifid=${interflow.interflowId}&qid=${qid}&qac=${qac}&gname=${gname}&qduty=${qduty}&qtheme=${qtheme}&gtype=${gtype}&gcate=${gcate}&qplat=${qplat}&qarea=${qarea}&qunum=${qunum}&qmanu=${qmanu}&qstatus=${qstatus}&qlevel=${qlevel}&pager.offset=${page.startRowIdx}&maxPageItems=${page.pageSize}">恢复</a></c:when>
                    </c:choose>
                </td>
                <td nowrap>${interflow.manufacturer}</td>
                <td nowrap>${interflow.gameCategory}</td>
                <td nowrap>${interflow.gameType}</td>
                <td nowrap>${interflow.platform}</td>
                <td nowrap>${interflow.theme}</td>
                <td nowrap>${interflow.publishArea}</td>
                <td nowrap><fmt:formatDate value="${interflow.createDate}"
                                           pattern="yyyy-MM-dd HH:mm:ss"/><br/>${interflow.createUser}</td>
                <td nowrap><fmt:formatDate value="${interflow.modifyDate}"
                                           pattern="yyyy-MM-dd HH:mm:ss"/><br/>${interflow.modifyUser}</td>
                </tr>
            </c:forEach>
            <tr>
                <td height="1" colspan="19" class="default_line_td"></td>
            </tr>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="19" class="error_msg_td">暂无数据!</td>
            </tr>
        </c:otherwise>
    </c:choose>
    <tr>
        <td colspan="19" height="1" class="default_line_td"></td>
    </tr>
    <c:if test="${page.maxPage > 1}">
        <tr class="list_table_opp_tr">
            <td colspan="19">
                <pg:pager url="/misc/qq/list"
                          items="${page.totalRows}" isOffset="true"
                          maxPageItems="${page.pageSize}"
                          export="offset, currentPageNumber=pageNumber" scope="request">
                    <pg:param name="qid" value="${qid}"/>
                    <pg:param name="qac" value="${qac}"/>
                    <pg:param name="qlevel" value="${qlevel}"/>
                    <pg:param name="gname" value="${gname}"/>
                    <pg:param name="qduty" value="${qduty}"/>
                    <pg:param name="qtheme" value="${qtheme}"/>
                    <pg:param name="gtype" value="${gtype}"/>
                    <pg:param name="gcate" value="${gcate}"/>
                    <pg:param name="qplat" value="${qplat}"/>
                    <pg:param name="qarea" value="${qarea}"/>
                    <pg:param name="qunum" value="${qunum}"/>
                    <pg:param name="qmanu" value="${qmanu}"/>
                    <pg:param name="qstatus" value="${qstatus}"/>
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