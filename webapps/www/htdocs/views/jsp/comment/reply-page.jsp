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
    <title>${resultDTO.title}_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/article-comment.css" rel="stylesheet" type="text/css">
</head>
<body style="background:#fff">
<!--头部开始-->
<c:import url="/views/jsp/passport/header.jsp"/>
<!-- 面包屑 -->
<div class="location-2th">
    <div>
        当前位置：<a href="${URL_WWW}">着迷网</a> &gt; <a href="${resultDTO.uri}">${resultDTO.title}</a>
        &gt; 评论
    </div>
</div>
<!-- 内容开始 -->
<div class="subpage-wrapper subpage-comment clearfix">
<!-- 左侧 -->
<div class="subpage-left">

<!-- 标题 -->
<h2 class="commend-topical"><a href="${resultDTO.uri}">${resultDTO.title}</a></h2>

<div class="fabucon">
    <h2 class="mygroup-title"><span class="talk-about-icon"></span></h2>

    <div id="send_box_small" class="sendBox-small"
         <c:if test="${fn:length(errorMessage) > 0}">style="display: none;"</c:if>>
        <input id="send_box_body" value="我来说两句"/>
    </div>
</div>
<div
        <c:if test="${fn:length(errorMessage) <= 0}">style="display: none;"</c:if> class="comment clearfix"
        id="div_post_area">
    <!--发布开始-->
    <div class="conmentbd clearfix" id="post_area">
        <!-- photo js css end -->
        <div class="talk ">
            <div id="div_post_chat">

                <form id="form_comment" method="post" action="${URL_WWW}/comment/reply/post">
                    <div class="talk ">
                        <div class="talk_wrapper clearfix">
                            <input type="hidden" name="unikey" value="${uniKey}" id="input_hidden_unikey"/>
                            <input type="hidden" name="domain" value="${domain}" id="input_hidden_domain"/>
                            <input type="hidden" name="body" value="" id="input_hidden_body"/>
                            <textarea style="font-family:Tahoma, '宋体';" warp="off" class="talk_text" rows=""
                                      cols="" id="textarea_comment_body" name="text"></textarea>

                            <div style="display:none" id="rel_preview" class="preview clearfix">
                                <ul id="ul_preview">
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="edit">
                        <%--<div class="edittool">--%>
                            <%--<a title="表情" id="faceShow" class="talk_face" onclick="return false;"--%>
                               <%--href="javascript:void(0);">表情</a>--%>
                        <%--</div>--%>
                                <span style="color: #f00; padding-top: 2px; float: left;margin-top: 10px;padding-left: 10px;"
                                      id="reply_error"><c:if test="${fn:length(errorMessage) > 0}">
                                    <fmt:message key="${errorMessage}" bundle="${userProps}"/></c:if></span>

                        <div class="publish">
                            <input type="submit" value=""
                            <c:choose>
                                   <c:when test="${fn:length(errorMessage) > 0}">class="publishon" </c:when>
                                   <c:otherwise>class="publish_btn"</c:otherwise>
                            </c:choose> id="comment_submit" name="">
                        </div>
                    </div>
                </form>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="commentline"></div>
        <!--发布结束-->
    </div>
</div>
<!-- 回帖排序 -->
<div class="comment-sort">
    <div class="comment-sort-btn" style="display: none;">
        <input type="hidden" name="flag" value="${flag}" id="input_hidden_flag"/>
        <span><fmt:message key="comment.tab.${flag}" bundle="${userProps}"/></span><em></em>

        <div class="comment-sort-box">
            <c:choose>
                <c:when test="${flag=='recent'}"><a class="current" href="#">最新<i>√</i></a></c:when>
                <c:otherwise><a
                        href="${URL_WWW}/comment/reply/page?unikey=${uniKey}&domain=${domain}&flag=recent">最新<i>√</i></a></c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${flag=='oldest'}"><a class="current" href="#">最早<i>√</i></a></c:when>
                <c:otherwise><a
                        href="${URL_WWW}/comment/reply/page?unikey=${uniKey}&domain=${domain}&flag=oldest">最早<i>√</i></a></c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${flag=='hot'}"><a class="current" href="#">最热<i>√</i></a></c:when>
                <c:otherwise><a
                        href="${URL_WWW}/comment/reply/page?unikey=${uniKey}&domain=${domain}&flag=hot">最热<i>√</i></a></c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<!-- 用户回帖列表 (也是套用线上的) -->
<c:choose>
    <c:when test="${resultDTO != null && resultDTO.mainreplys != null && resultDTO.mainreplys.rows.size() > 0}">
        <c:forEach items="${resultDTO.mainreplys.rows}" var="replyDto">
            <div name="cont_cmt_list_${replyDto.reply.reply.rid}" class="area blogopinion clearfix ">
                <dl>
                    <dt class="personface">
                        <a title="${replyDto.reply.user.name}" name="atLink" href="javascript:void(0);">
                            <img width="58" height="58" class="user" src="${replyDto.reply.user.icon}">
                        </a>
                    </dt>
                    <dd class="textcon discuss_building">
                        <em>
                            <c:choose>
                                <c:when test="${flag=='hot'}"></c:when>
                                <c:otherwise>#${replyDto.reply.reply.floor_num}</c:otherwise>
                            </c:choose>
                        </em>
                        <a title="${replyDto.reply.user.name}" class="author" name="atLink"
                           href="javascript:void(0);">${replyDto.reply.user.name}</a>
                        <c:if test="${replyDto.reply.user.verify != null && replyDto.reply.user.verify != 'n'}">
                            <a title="<fmt:message key="
                               verify.profile.${replyDto.reply.user.verify}" bundle="${userProps}"/>" class="${replyDto.reply.user.verify}vip" href="javascript:void(0);"></a>
                        </c:if>

                        <p>${(replyDto.reply.reply.body == null ? '' : replyDto.reply.reply.body.text)}</p>

                        <div class="discuss_bdfoot">${replyDto.reply.reply.post_date}&nbsp;
                            <a href="javascript:void(0);" id="agreelink_${replyDto.reply.reply.rid}"
                               data-rid="${replyDto.reply.reply.rid}" class="dianzan">
                            </a>
                            <span id="agreenum_${replyDto.reply.reply.rid}">
                                <a href="javascript:void(0);" name="agree_num"
                                   data-rid="${replyDto.reply.reply.rid}"><c:if test="${replyDto.reply.reply.agree_sum>0}">(${replyDto.reply.reply.agree_sum})</c:if></a>
                            </span>
                            <c:if test="${userSession != null && userSession.uno eq replyDto.reply.user.uno}">
                                <a href="javascript:void(0);" class="remove"
                                   data-rid="${replyDto.reply.reply.rid}">删除</a>&nbsp;
                            </c:if>
                            <c:choose>
                                <c:when test="${replyDto.subreplys.rows.size() > 0 || replyDto.subreplys.page.maxPage>1}">
                                    <a name="link_toggle_recomment"
                                       id="link_toggle_recomment_close_${replyDto.reply.reply.rid}"
                                       data-reply-sum="${replyDto.reply.reply.sub_reply_sum}" href="javascript:void(0);"
                                       class="putaway">收起回复</a>
                                </c:when>
                                <c:otherwise>
                                    <a name="link_toggle_recomment"
                                       id="link_toggle_recomment_open_${replyDto.reply.reply.rid}"
                                       data-reply-sum="${replyDto.reply.reply.sub_reply_sum}"
                                       href="javascript:void(0);">回复</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="discuss_bd_list discuss_border" <c:choose>
                            <c:when test="${fn:length(replyDto.subreplys.rows)> 0 || replyDto.subreplys.page.maxPage >1}">style="display:block;"</c:when>
                            <c:otherwise>style="display:none;"</c:otherwise></c:choose>>
                            <div id="children_reply_list_${replyDto.reply.reply.rid}">
                                <c:choose>
                                    <c:when test="${fn:length(replyDto.subreplys.rows)> 0}">
                                        <c:forEach items="${replyDto.subreplys.rows}" var="childReplyDto">
                                            <div style="" name="cont_cmt_list_${childReplyDto.reply.rid}"
                                                 class="conmenttx clearfix">
                                                <div class="conmentface">
                                                    <div class="commenfacecon">
                                                        <a href="javascript:void(0);"
                                                           title="${childReplyDto.user.name}" name="atLink"
                                                           class="cont_cl_left">
                                                            <img width="33px" height="33px"
                                                                 src="${childReplyDto.user.icon}"/>
                                                        </a>
                                                    </div>
                                                </div>
                                                <div class="conmentcon">
                                                    <a title="${childReplyDto.user.name}" name="atLink"
                                                       href="javascript:void(0);">${childReplyDto.user.name}</a>
                                                    <c:if test="${childReplyDto.user.verify != null && childReplyDto.user.verify != 'n'}">
                                                        <a title="<fmt:message key="
                                                           verify.profile.${childReplyDto.user.verify}"
                                                        bundle="${userProps}"/>"
                                                        class="${childReplyDto.user.verify}vip"
                                                        href="javascript:void(0);"></a>
                                                    </c:if>
                                                    <c:if test="${childReplyDto.puser != null && fn:length(childReplyDto.puser.name) > 0}">@${childReplyDto.puser.name}</c:if>：${(childReplyDto.reply.body == null ? '' : childReplyDto.reply.body.text)}
                                                    <div class="commontft clearfix">
                                                        <span class="reply_time">${childReplyDto.reply.post_date}</span>
                                                <span class="delete">
                                                    <a href="javascript:void(0);"
                                                       id="agreelink_${childReplyDto.reply.rid}"
                                                       data-rid="${childReplyDto.reply.rid}" class="dianzan">
                                                    </a>
                                                    <span id="agreenum_${childReplyDto.reply.rid}">
                                                        <a href="javascript:void(0);" name="agree_num"
                                                           data-rid="${childReplyDto.reply.rid}"><c:if test="${childReplyDto.reply.agree_sum>0}">(${childReplyDto.reply.agree_sum})</c:if>
                                                        </a>
                                                    </span>&nbsp;
                                                    <c:if test="${userSession != null && userSession.uno eq childReplyDto.user.uno}">
                                                        <a href="javascript:void(0);" class="remove"
                                                           data-rid="${childReplyDto.reply.rid}"
                                                           data-oid="${replyDto.reply.reply.rid}">删除</a>&nbsp;
                                                    </c:if>
                                                    <a href="javascript:void(0);" class="repost"
                                                       data-oid="${replyDto.reply.reply.rid}"
                                                       data-rid="${childReplyDto.reply.rid}"
                                                       data-name="${childReplyDto.user.name}">回复</a>
                                                </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${replyDto.subreplys.page.maxPage > 0}">
                                                <p>
                                                    当前页的评论已经被删除~
                                                </p>
                                            </c:when>
                                            <c:otherwise>
                                                <p>
                                                    目前没有评论，欢迎你发表评论~
                                                </p>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <c:if test="${replyDto.subreplys.page.maxPage>1}">
                                <c:set var="ajaxpage" value="${replyDto.subreplys.page}"/>
                                <%@ include file="/views/jsp/page/replypage.jsp" %>
                            </c:if>

                            <div id="post_recomment_area_${replyDto.reply.reply.rid}" class="discuss_reply">
                                <a class="discuss_text01" href="javascript:void(0);" name="replypost_mask"
                                   id="replypost_mask_${replyDto.reply.reply.rid}">我也说一句</a>

                                <div style="display:none" class="discuss_reply reply_box01">
                                    <textarea warp="off" style="font-family:Tahoma, '宋体';"
                                              class="discuss_text focus"
                                              id="textarea_recomment_body_${replyDto.reply.reply.rid}"
                                              name="content"></textarea>

                                    <div class="related clearfix" name="pinglun">
                                        <%--<a href="javascript:void(0)" title="表情"--%>
                                           <%--id="childrenreply_mood_${replyDto.reply.reply.rid}"--%>
                                           <%--class="commenface"></a>--%>

                                        <div class="transmit clearfix">
                                                <span style="color: #f00; padding-top: 10px;margin-top:10px;padding-left: 10px;"
                                                      id="post_callback_msg_${replyDto.reply.reply.rid}"
                                                      name="post_callback_msg"></span>
                                            <a class="submitbtn fr" data-oid="${replyDto.reply.reply.rid}"
                                               name="submit_recomment"
                                               id="submit_recomment_${replyDto.reply.reply.rid}">
                                                <span id="pinglun_${replyDto.reply.reply.rid}">评 论</span></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </dd>
                </dl>
            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${page.maxPage > 0}">
                <div name="cont_cmt_list_${replyDto.reply.reply.replyId}" class="area blogopinion clearfix ">
                    当前页的评论已经被删除~
                </div>
            </c:when>
            <c:otherwise>
                <div name="cont_cmt_list_${replyDto.reply.reply.replyId}" class="area blogopinion clearfix ">
                    目前没有评论，欢迎你发表评论~
                </div>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<!-- 分页 -->
<div class="pagecon clearfix">
    <c:set var="pageurl" value="${URL_WWW}/comment/reply/page"/>
    <c:set var="pageparam" value="unikey=${uniKey}&domain=${domain}&flag=${flag}"/>
    <%@ include file="/views/jsp/page/comment-page.jsp" %>
</div>
</div>
<!-- 右侧 -->
<c:choose>
    <c:when test="${fn:length(rightHtml) > 0}">
        <div class="subpage-right">
                ${rightHtml}
        </div>
    </c:when>
    <c:otherwise>
        <%@ include file="/hotdeploy/views/jsp/comment/reply-page-right.jsp" %>
    </c:otherwise>
</c:choose>

</div>
<!-- footer -->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/comment-reply-init.js');
</script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/bdhm-noseajs.js"></script>
</body>
</html>