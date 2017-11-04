<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="">
    <meta name="description"
          content=""/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${forignContent.contentTitle}_${jmh_title}</title>

    <meta name="viewport" content="width=device.width, initial-scale=1.0, user-scalable=no">

    <link href="${URL_LIB}/static/theme/default/css/wapcomment.css" rel="stylesheet" type="text/css">
    <link href="http://reswiki1.joyme.com/css/wxwiki/css/wxcomment.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
</head>
<body>
<div class="topbar clearfix">
    <div class="fr">
        <!--<a href="#">登录</a>
               已登录 -->
        <c:choose>
            <c:when test="${userSession!=null}">
                <div class="logined">
                        ${userSession.nick}，<a
                        href="http://passport.${DOMAIN}/logout?reurl=${URL_WWW}/mobile/comment/recentlist?cid=${forignContent.contentId}">退出</a>


                </div>

            </c:when>
            <c:otherwise>
                <a href="${URL_WWW}/mloginpage?reurl=${URL_WWW}/mobile/comment/recentlist?cid=${forignContent.contentId}">登录</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div id="head" class="clearfix">
    <input type="hidden" name="curl" value="${forignContent.contentUrl}" id="input_hidden_curl"/>
    <input type="hidden" name="cid" value="${forignContent.contentId}"
           id="input_hidden_cid"/>

    &lt; <a href="${forignContent.contentUrl}">${forignContent.contentTitle}</a>

</div>

<!-- 主评论框 -->
<c:choose>
    <c:when test="${userSession!=null}">
        <div class="main-comment">
            <div class="replybox" style="display:block">
                <form action="${URL_WWW}/mobile/comment/post" id="form_mobile_comment">
                    <input type="hidden" name="curl" value="${forignContent.contentUrl}"/>
                    <input type="hidden" name="cid" id="contentid" value="${forignContent.contentId}"/>
                    <input type="hidden" name="channel" value="${channel}"/>

                    <div style="margin-bottom:6px;"><textarea placeholder="说点儿什么吧" name="body"
                                                              id="comentbody">${body}</textarea></div>
           <span style="color: #f00; "
                 id="reply_error"><c:if test="${fn:length(error) > 0}"><fmt:message key="${error}"
                                                                                    bundle="${userProps}"/></c:if></span>
                    <input type="submit" value="评论"/>
                </form>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="loginbtn">
            <a href="${URL_WWW}/mloginpage?reurl=${URL_WWW}/mobile/comment/recentlist?cid=${forignContent.contentId}">点击登录，马上回复</a>
        </div>
    </c:otherwise>
</c:choose>

<!-- 评论列表 -->
<div style="min-height:25rem;">
<c:choose>
    <c:when test="${list.size()>0}">
        <c:forEach items="${list}" var="replyDto">
            <div class="comment-list" name="remove_reply_${replyDto.reply.reply.replyId}">

                <!-- 主楼评论信息 -->
                <div class="aut">
                    <div class="fl">
                        <span class="user">${replyDto.reply.profile.name}</span>
                    </div>
                    <div class="fr">
                        <span class="dz" name="replayagree"
                              data-rid="${replyDto.reply.reply.replyId}"
                              id="replyagree_${replyDto.reply.reply.replyId}"> ${replyDto.reply.reply.agreeNum}</span>
                        <c:if test="${userSession != null && userSession.uno eq replyDto.reply.reply.replyUno}">
                            <span class="sc" name="deleteReply" data-rid=${replyDto.reply.reply.replyId}>删除</span>
                        </c:if>

                        <span class="reply" onclick="changeReply(this)" name="clickreply">回复</span>
                    </div>
                </div>
                <div class="comment-content">
                    <p>${replyDto.reply.reply.body}</p>
                </div>

                <!-- 回复主楼评论(默认隐藏) -->
                <div class="replybox" id="replybox_${replyDto.reply.reply.replyId}">
                    <div style="margin-bottom:6px;"><textarea placeholder="说点儿什么吧"
                                                              id="textarea_${replyDto.reply.reply.replyId}"></textarea>
                    </div>
                    <span style="color: #f00;" id="reply_error_${replyDto.reply.reply.replyId}"></span>
                    <input type="submit" value="回复" data-rootid="${replyDto.reply.reply.replyId}"
                           data-cid="${forignContent.contentId}" id="m_reply_${replyDto.reply.reply.replyId}"
                           data-pid=""
                           name="m_replay"/>
                </div>
                <!-- 已回复主楼评论的   楼中楼 -->
                <div class="replyed" id="replyed_${replyDto.reply.reply.replyId}">
                    <c:choose>
                        <c:when test="${fn:length(replyDto.reReplys.rows)> 0}">
                            <c:forEach items="${replyDto.reReplys.rows}" var="childReplyDto">
                                <!-- 第一条回复 -->
                                <div class="replyed-list" name="remove_reply_${childReplyDto.reply.replyId}" id>
                                    <div class="aut">
                                        <div class="fl">
                                            <span class="user"><i>•</i>${childReplyDto.profile.name}:</span>
                                        </div>
                                        <div class="fr">
                                            <span class="dz" name="replayagree"
                                                  id="replyagree_${childReplyDto.reply.replyId}"
                                                  data-rid="${childReplyDto.reply.replyId}"
                                                    >${childReplyDto.reply.agreeNum}</span>
                                            <c:if test="${userSession != null && userSession.uno eq childReplyDto.reply.replyUno}">
                                                <span class="sc" name="deleteReply"
                                                      data-rid=${childReplyDto.reply.replyId}>删除</span>
                                            </c:if>
                                                <span class="reply" onclick="changeReply(this)"
                                                      name="clickreply">回复</span>
                                        </div>
                                    </div>
                                    <div class="comment-content">
                                        <c:if test="${childReplyDto.parentProfile != null && fn:length(childReplyDto.parentProfile.name) > 0}">@${childReplyDto.parentProfile.name}：</c:if>${childReplyDto.reply.body}
                                    </div>
                                    <div class="replybox" id="m_replybox_${childReplyDto.reply.replyId}">
                                        <div style="margin-bottom:6px;"><textarea
                                                placeholder="说点儿什么吧"
                                                id="child_textarea_${childReplyDto.reply.replyId}">@${childReplyDto.profile.name}：</textarea>
                                        </div>
                                        <span style="color: #f00; "
                                              id="child_reply_error_${childReplyDto.reply.replyId}"></span>
                                        <input type="submit" value="回复"
                                               data-rootid="${replyDto.reply.reply.replyId}"
                                               data-cid="${forignContent.contentId}"
                                               data-pid="${childReplyDto.reply.replyId}"
                                               data-pname="${childReplyDto.profile.name}"
                                               id="m_reply_${replyDto.reply.reply.replyId}"
                                               name="m_replay"/>
                                    </div>
                                </div>

                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${replyDto.reReplys.page.maxPage > 0}">
                                <div class="replyed" id="replyed_${replyDto.reply.reply.replyId}">
                                    <div class="replyed-list">
                                        <p>
                                            当前页的评论已经被删除~
                                        </p>
                                    </div>
                                </div>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                    <div id="appenddiv_${replyDto.reply.reply.replyId}"></div>
                    <c:if test="${replyDto.reReplys.page.maxPage > 1}">

                        <div class="pages pages2">

                        <c:choose>
                            <c:when test="${replyDto.reReplys.page.curPage>1}">
                                <div><a href="javascript:void(0)" name="childReplay"
                                        data-rid="${replyDto.reply.reply.replyId}"
                                        data-p="1"
                                        data-repsum=${replyDto.reReplys.page.totalRows}>首页</a></div>
                                <div><a href="javascript:void(0)" name="childReplay"
                                        data-rid="${replyDto.reply.reply.replyId}"
                                        data-p="${replyDto.reReplys.page.curPage-1}"
                                        data-repsum=${replyDto.reReplys.page.totalRows}>上一页</a></div>
                            </c:when>
                            <c:otherwise>
                                <div><a href="javascript:void(0)" class="disabled">首页</a></div>
                                <div><a href="javascript:void(0)" class="disabled">上一页</a></div>
                            </c:otherwise>
                        </c:choose>


                        <div>${replyDto.reReplys.page.curPage}/${replyDto.reReplys.page.maxPage}</div>
                        <c:choose>
                            <c:when test="${replyDto.reReplys.page.curPage<replyDto.reReplys.page.maxPage}">
                                <div><a href="javascript:void(0)" name="childReplay"
                                        data-rid="${replyDto.reply.reply.replyId}"
                                        data-p="${replyDto.reReplys.page.curPage+1}"
                                        data-repsum=${replyDto.reReplys.page.totalRows}>下一页</a></div>
                                <div><a href="javascript:void(0)" name="childReplay"
                                        data-rid="${replyDto.reply.reply.replyId}"
                                        data-p="${replyDto.reReplys.page.maxPage}"
                                        data-repsum=${replyDto.reReplys.page.totalRows}>尾页</a></div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div><a href="javascript:void(0)" class="disabled">下一页</a></div>
                                <div><a href="javascript:void(0)" class="disabled">尾页</a></div>

                            </c:otherwise>
                        </c:choose>


                    </c:if>
                </div>
                <!-- 分页 -->

            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${page.maxPage>0}">
                <div class="comment-list">
                    <div class="replyed" id="replyed_${replyDto.reply.reply.replyId}">
                        <div class="replyed-list">
                            <p>
                                当前页的评论已经被删除~
                            </p>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="comment-list">
                    <div class="replyed" id="replyed_${replyDto.reply.reply.replyId}">
                        <div class="replyed-list">
                            <p>
                                暂无评论！
                            </p>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
<!-- 分页-->
<c:if test="${page.maxPage>1}">
    <div class="pages">

        <c:choose>
            <c:when test="${page.curPage>1}">
                <div><a href="${URL_WWW}/mobile/comment/recentlist?cid=${forignContent.contentId}&p=1">首页</a></div>
                <div>
                    <a href="${URL_WWW}/mobile/comment/recentlist?cid=${forignContent.contentId}&p=${page.curPage-1}">上一页</a>
                </div>
            </c:when>
            <c:otherwise>
                <div><a href="javascript:void(0)" class="disabled">首页</a></div>
                <div><a href="javascript:void(0)" class="disabled">上一页</a></div>
            </c:otherwise>
        </c:choose>

        <div>${page.curPage}/${page.maxPage}</div>
        <c:choose>
            <c:when test="${page.curPage>=page.maxPage}">
                <div><a href="javascript:void(0)" class="disabled">下一页</a></div>
                <div><a href="javascript:void(0)" class="disabled">尾页</a></div>
            </c:when>
            <c:otherwise>
                <div>
                    <a href="${URL_WWW}/mobile/comment/recentlist?cid=${forignContent.contentId}&p=${page.curPage+1}">下一页</a>
                </div>
                <div>
                    <a href="${URL_WWW}/mobile/comment/recentlist?cid=${forignContent.contentId}&p=${page.maxPage}">尾页</a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</c:if>
</div>

<!-- 页脚 -->
<%--<div id="mwiki-footer" style="margin-top:20px;">--%>
<%--<h2>热门wiki</h2>--%>

<%--<p>--%>
<%--<a href="http://www.joyme.com/mwiki/mt/index.shtml">我叫MT</a><span>|</span>--%>
<%--<a href="http://www.joyme.com/mwiki/qhero/index.shtml">全民英雄</a><span>|</span>--%>
<%--<a href="http://www.joyme.com/mwiki/mkhx/index.shtml">魔卡幻想</a><span>|</span>--%>
<%--<a href="http://www.joyme.com/mwiki/mxm/index.shtml">怪物X联盟</a><span>|</span>--%>
<%--<a href="http://www.joyme.com/mwiki/hxyx/index.shtml">幻想英雄</a><span>|</span>--%>
<%--<a href="http://www.joyme.com/mwiki/ppsg/index.shtml">啪啪三国</a><span>|</span>--%>
<%--<a href="http://www.joyme.com/mwiki/dzm/index.shtml">大掌门</a><span>|</span>--%>
<%--<a href="http://www.joyme.com/mwiki/hh/index.shtml">你好英雄</a>--%>
<%--</p>--%>
<%--</div>--%>
<script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
<script type="text/javascript">
    document.getElementById('loginedbtn').addEventListener('touchend', loginedhandle, false);
    //    document.getElementById('loginedbtn').addEventListener('click', loginedhandle, false);
    function loginedhandle(e) {
        e.stopPropagation();
        var t = document.getElementById('loginedbox');
        if (t.style.display !== 'block') {
            t.style.display = 'block';
        } else {
            t.style.display = 'none';
        }
        ;
        document.addEventListener('touchend', changeLoginedBox, false);
//        document.addEventListener('click', changeLoginedBox, false);
    }
    ;

    function changeLoginedBox() {
        document.getElementById('loginedbox').style.display = 'none';
        document.removeEventListener('touchend', changeLoginedBox, false);
//        document.removeEventListener('click', changeLoginedBox, false);
    }

    function changeReply(tag) {
        var a = $(tag).parents('.replyed').find('.replybox');
        var b = $(tag).parents('.replyed-list').find('.replybox');

        if (a.length) {
            a.hide();
            b.show();
        } else {
            $(tag).parents('.comment-list').find('.replybox').eq(0).toggle();
        }

    }
</script>

<!-- 分页 -->
<%--<div class="pagecon clearfix">--%>
<%--<c:set var="pageurl" value="${URL_WWW}/comment/${flag}list"/>--%>
<%--<c:set var="pageparam" value="cid=${forignContent.contentId}"/>--%>
<%--<%@ include file="/views/jsp/page/page.jsp" %>--%>
<%--</div>--%>

</div>
</div>


<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/forign-comment-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>