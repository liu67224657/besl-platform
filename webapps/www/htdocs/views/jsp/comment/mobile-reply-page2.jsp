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
    <title>${commentBean.title}_${jmh_title}</title>
    <meta name="viewport" content="width=device.width, initial-scale=1.0, user-scalable=no">
    <link href="${URL_LIB}/static/theme/default/css/wapcomment.css" rel="stylesheet" type="text/css">
    <link href="http://reswiki1.joyme.com/css/wxwiki/css/wxcomment.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var reurl = encodeURIComponent(window.location.href);
            var host = window.location.host.substr(window.location.host.indexOf('.'));
            var loginPath = 'http://passport' + host + '/';

            if ($('div.fr a').length > 0) {
                $('div.fr a').attr('href', '' + loginPath + 'auth/loginwappage?reurl=' + reurl + '');
            }
            if ($('div.loginbtn a').length > 0) {
                $('div.loginbtn a').attr('href', '' + loginPath + 'auth/loginwappage?reurl=' + reurl + '');
            }
        });
    </script>
</head>
<body>
<div class="topbar clearfix">
    <div class="fr">
        <!--<a href="#">登录</a>
               已登录 -->
        <c:choose>
            <c:when test="${userSession!=null}">
                <div class="logined">${userSession.nick}</div>
            </c:when>
            <c:otherwise>
                <a href="">登录</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div id="head" class="clearfix">
    &lt; <a href="${commentBean.uri}">${commentBean.title}</a>
</div>
<!-- 主评论框 -->
<c:choose>
    <c:when test="${userSession!=null}">
        <div class="main-comment">
            <div class="replybox" style="display:block">
                <form action="${URL_WWW}/comment/reply/mobilepost" id="form_mobile_comment">
                    <input type="hidden" name="unikey" value="${unikey}" id="input_hidden_unikey"/>
                    <input type="hidden" name="domain" value="${domain}" id="input_hidden_domain"/>
                    <input type="hidden" name="body" value="" id="input_hidden_body"/>

                    <div style="margin-bottom:6px;"><textarea placeholder="说点儿什么吧"
                                                              id="comentbody"></textarea></div>
           <span style="color: #f00; "
                 id="reply_error"><c:if test="${fn:length(errorMessage) > 0}">
               <fmt:message key="${errorMessage}" bundle="${userProps}"/></c:if></span>
                    <input type="submit" id="submit_comment" value="评论"/>
                </form>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="loginbtn">
            <a href="">点击登录，马上回复</a>
        </div>
    </c:otherwise>
</c:choose>

<!-- 评论列表 -->
<div style="min-height:25rem;">
    <c:choose>
        <c:when test="${rows.size()>0}">
            <c:forEach items="${rows}" var="replyDto">
                <div class="comment-list" name="remove_reply_${replyDto.reply.reply.rid}">

                    <!-- 主楼评论信息 -->
                    <div class="aut">
                        <div class="fl">
                            <span class="user">${replyDto.reply.user.name}</span>
                        </div>
                        <div class="fr">
                        <span class="dz" name="replayagree"
                              data-rid="${replyDto.reply.reply.rid}"
                              id="replyagree_${replyDto.reply.reply.rid}"> ${replyDto.reply.reply.agree_sum}</span>
                            <c:if test="${userSession != null && userSession.uno eq replyDto.reply.user.uno}">
                                <span class="sc" name="deleteReply" data-rid=${replyDto.reply.reply.rid}>删除</span>
                            </c:if>

                            <span class="reply" onclick="changeReply(this)" name="clickreply">回复</span>
                        </div>
                    </div>
                    <div class="comment-content">
                        <p>${(replyDto.reply.reply.body == null ? '' : replyDto.reply.reply.body.text)}</p>
                    </div>

                    <!-- 回复主楼评论(默认隐藏) -->
                    <div class="replybox" id="replybox_${replyDto.reply.reply.rid}">
                        <div style="margin-bottom:6px;"><textarea placeholder="说点儿什么吧"
                                                                  id="textarea_${replyDto.reply.reply.rid}"></textarea>
                        </div>
                        <span style="color: #f00;" id="reply_error_${replyDto.reply.reply.rid}"></span>
                        <input type="submit" value="回复" data-oid="${replyDto.reply.reply.rid}"
                               id="m_reply_${replyDto.reply.reply.rid}"
                               data-pid=""
                               name="m_replay"/>
                    </div>
                    <!-- 已回复主楼评论的   楼中楼 -->
                    <div class="replyed" id="replyed_${replyDto.reply.reply.rid}">
                        <c:choose>
                            <c:when test="${fn:length(replyDto.subreplys.rows)> 0}">
                                <c:forEach items="${replyDto.subreplys.rows}" var="childReplyDto">
                                    <!-- 第一条回复 -->
                                    <div class="replyed-list" name="remove_reply_${childReplyDto.reply.rid}" id>
                                        <div class="aut">
                                            <div class="fl">
                                                <span class="user"><i>•</i>${childReplyDto.user.name}:</span>
                                            </div>
                                            <div class="fr">
                                            <span class="dz" name="replayagree"
                                                  id="replyagree_${childReplyDto.reply.rid}"
                                                  data-rid="${childReplyDto.reply.rid}"
                                                    >${childReplyDto.reply.agree_sum}</span>
                                                <c:if test="${userSession != null && userSession.uno eq childReplyDto.user.uno}">
                                                    <span class="sc" name="deleteReply"
                                                          data-rid="${childReplyDto.reply.rid}"
                                                          data-oid="replyDto.reply.reply.rid">删除</span>
                                                </c:if>
                                                <span class="reply" onclick="changeReply(this)"
                                                      name="clickreply">回复</span>
                                            </div>
                                        </div>
                                        <div class="comment-content">
                                            <c:if test="${childReplyDto.puser != null && fn:length(childReplyDto.puser.name) > 0}">@${childReplyDto.puser.name}：</c:if>${(childReplyDto.reply.body == null ? '' : childReplyDto.reply.body.text)}
                                        </div>
                                        <div class="replybox" id="m_replybox_${childReplyDto.reply.rid}">
                                            <div style="margin-bottom:6px;"><textarea
                                                    placeholder="说点儿什么吧"
                                                    id="child_textarea_${childReplyDto.reply.rid}">@${childReplyDto.user.name}：</textarea>
                                            </div>
                                        <span style="color: #f00; "
                                              id="child_reply_error_${childReplyDto.reply.rid}"></span>
                                            <input type="submit" value="回复"
                                                   data-oid="${replyDto.reply.reply.rid}"
                                                   data-pid="${childReplyDto.reply.rid}"
                                                   data-pname="${childReplyDto.user.name}"
                                                   id="m_reply_${replyDto.reply.reply.rid}"
                                                   name="m_replay"/>
                                        </div>
                                    </div>

                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:if test="${replyDto.subreplys.page.maxPage > 0}">
                                    <div class="replyed" id="replyed_${replyDto.reply.reply.rid}">
                                        <div class="replyed-list">
                                            <p>
                                                当前页的评论已经被删除~
                                            </p>
                                        </div>
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>

                        <div id="appenddiv_${replyDto.reply.reply.rid}"></div>
                        <c:if test="${replyDto.subreplys.page.maxPage > 1}">

                            <div class="pages pages2">

                                <c:choose>
                                    <c:when test="${replyDto.subreplys.page.curPage>1}">
                                        <div><a href="javascript:void(0)" name="childReplay"
                                                data-oid="${replyDto.reply.reply.rid}"
                                                data-p="1"
                                                data-repsum=${replyDto.subreplys.page.totalRows}>首页</a></div>
                                        <div><a href="javascript:void(0)" name="childReplay"
                                                data-oid="${replyDto.reply.reply.rid}"
                                                data-p="${replyDto.subreplys.page.curPage-1}"
                                                data-repsum=${replyDto.subreplys.page.totalRows}>上一页</a></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div><a href="javascript:void(0)" class="disabled">首页</a></div>
                                        <div><a href="javascript:void(0)" class="disabled">上一页</a></div>
                                    </c:otherwise>
                                </c:choose>


                                <div>${replyDto.subreplys.page.curPage}/${replyDto.subreplys.page.maxPage}</div>
                                <c:choose>
                                    <c:when test="${replyDto.subreplys.page.curPage<replyDto.subreplys.page.maxPage}">
                                        <div><a href="javascript:void(0)" name="childReplay"
                                                data-oid="${replyDto.reply.reply.rid}"
                                                data-p="${replyDto.subreplys.page.curPage+1}"
                                                data-repsum=${replyDto.subreplys.page.totalRows}>下一页</a></div>
                                        <div><a href="javascript:void(0)" name="childReplay"
                                                data-oid="${replyDto.reply.reply.rid}"
                                                data-p="${replyDto.subreplys.page.maxPage}"
                                                data-repsum=${replyDto.subreplys.page.totalRows}>尾页</a></div>
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
                <div class="replyed" id="replyed_${replyDto.reply.reply.rid}">
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
                <div class="replyed" id="replyed_${replyDto.reply.reply.rid}">
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
                <div><a href="${URL_WWW}/comment/reply/mobilepage?unikey=${unikey}&pnum=1&domain=${domain}">首页</a></div>
                <div>
                    <a href="${URL_WWW}/comment/reply/mobilepage?unikey=${unikey}&pnum=${page.curPage-1}&domain=${domain}">上一页</a>
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
                    <a href="${URL_WWW}/comment/reply/mobilepage?unikey=${unikey}&pnum=${page.curPage+1}&domain=${domain}">下一页</a>
                </div>
                <div>
                    <a href="${URL_WWW}/comment/reply/mobilepage?unikey=${unikey}&pnum=${page.maxPage}&domain=${domain}">尾页</a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</c:if>
</div>


<script type="text/javascript">
    document.getElementById('loginedbtn').addEventListener('touchend', loginedhandle, false);
    function loginedhandle(e) {
        e.stopPropagation();
        var t = document.getElementById('loginedbox');
        if (t.style.display !== 'block') {
            t.style.display = 'block';
        } else {
            t.style.display = 'none';
        }

        document.addEventListener('touchend', changeLoginedBox, false);
    }

    function changeLoginedBox() {
        document.getElementById('loginedbox').style.display = 'none';
        document.removeEventListener('touchend', changeLoginedBox, false);
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


</div>
</div>


<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/comment-reply-mobile-init.js');
</script>
</body>
</html>