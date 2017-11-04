<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="Keywords" content="${seoDTO.keywords}">
    <meta name="description" content="${seoDTO.desc} ${jmh_title}"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${seoDTO.title}_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/style.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>                                   <c:import url="/views/jsp/passport/header.jsp"/>

<div class="articlepage-white-bg">
<c:import url="/views/jsp/passport/header.jsp"/>
<%--<c:if test="${gamemenu!=null}">--%>
    <%--<div class="location-2th">--%>
        <%--<div>当前位置：<a href="${URL_WWW}">首页</a>&nbsp;&gt;&nbsp;<a href="${URL_WWW}/game">游戏</a>&nbsp;&gt;&nbsp;<a--%>
                <%--href="${URL_WWW}/game/${gamemenu.gameCode}">${gamemenu.gameName}</a>&nbsp;&gt;&nbsp;正文--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</c:if>--%>

<div class="index-2013-box clearfix">
<div class="article-column-left">

<!-- 引用线上的 -->
<%--<div class="blogarticle">--%>
<%--<c:choose>--%>
    <%--<c:when test="${blogContent.content.publishType.code eq 'org'}">--%>
        <%--<!--不是转贴-->--%>
        <%--<c:choose>--%>
            <%--<c:when test="${blogContent.content!=null && !(blogContent.content.removeStatus.code eq 'y')}">--%>
                <%--<c:choose>--%>
                    <%--<c:when test="${blogContent.content.contentType.hasPhrase()}">--%>
                        <%--<%@ include file="/views/jsp/blog/b01/blog-content-phrase.jsp" %>--%>
                    <%--</c:when>--%>
                    <%--<c:otherwise>--%>
                        <%--<!--长文发布 begin-->--%>
                        <%--<%@ include file="/views/jsp/blog/b01/blog-content-text.jsp" %>--%>
                        <%--<!--长文发布 ending-->--%>
                    <%--</c:otherwise>--%>
                <%--</c:choose>--%>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
                <%--<div class="contentremove">--%>
                    <%--<p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>--%>
                <%--</div>--%>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
        <%--<!--转发原文头像-->--%>
        <%--<dl class="article-author-intro clearfix">--%>
            <%--<dt class="personface">--%>
                <%--<a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" class="tag_cl_left">--%>
                    <%--<img height="58px" width="58px"--%>
                         <%--src="<c:out value='${uf:parseFacesInclude(blogContent.profile.blog.headIconSet,blogContent.profile.detail.sex,"s" , true, 0, 1)[0]}'/>"></a>--%>
            <%--</dt>--%>
            <%--<dd>--%>
                <%--<h3><a href="${URL_WWW}/people/${blogContent.profile.blog.domain}" name="atLink"--%>
                       <%--title="<c:out value="${blogContent.profile.blog.screenName}"/>">${blogContent.profile.blog.screenName}</a><span>转发自</span><a--%>
                        <%--href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}" name="atLink"--%>
                        <%--title="<c:out value="${blogContent.rootProfile.blog.screenName}"/>">@<c:out--%>
                        <%--value="${blogContent.rootProfile.blog.screenName}"/></a></h3>--%>

                <%--<p>--%>
                    <%--<span>${dateutil:parseDate(blogContent.content.publishDate)}</span>--%>
                    <%--<a href="javascript:void(0);" name="reply_link">我也要评论<c:if--%>
                            <%--test="${blogContent.content.replyTimes>0}">(${blogContent.content.replyTimes})</c:if></a><a--%>
                        <%--href="javascript:void(0);" name="reply_link" title="快速评论" class="comment-icon"></a>--%>
                <%--</p>--%>

                <%--<div id="content_source"><c:if test="${blogContent.board!=null}">--%>
                    <%--<em>来自：<a href="${URL_WWW}/group/${blogContent.board.gameCode}/talk"--%>
                              <%--target="_blank">${blogContent.board.gameName}小组</a></em>--%>
                <%--</c:if></div>--%>
            <%--</dd>--%>
        <%--</dl>--%>
        <%--<dl>--%>
            <%--<dd class="textcon">--%>
                <%--<p class="discuss_txt" id="forward_content_area">${blogContent.content.content}</p>--%>
                <%--<c:if test="${fn:length(blogContent.content.images.images)>0}">--%>
                    <%--<c:forEach var="img" items="${blogContent.content.images.images}">--%>
                        <%--<div class="blogarticle_img">--%>
                            <%--<a href="<c:out value="${uf:parseBFace(img.m)}"/>" target="_blank">--%>
                                <%--<img src="<c:out value="${uf:parseMFace(img.m)}"/>">--%>
                            <%--</a>--%>
                        <%--</div>--%>
                    <%--</c:forEach>--%>
                <%--</c:if>--%>
                <%--<div class="disarea clearfix">--%>
                    <%--<div class="discuss_corner">--%>
                        <%--<span class="discorner"></span>--%>
                    <%--</div>--%>
                    <%--<div class="discuss_area clearfix">--%>
                        <%--<c:choose>--%>
                            <%--<c:when test="${blogContent.rootContent!=null && !(blogContent.rootContent.removeStatus.code eq 'y')}">--%>
                                <%--<c:choose>--%>
                                    <%--<c:when test="${blogContent.rootContent.contentType.hasPhrase()}">--%>
                                        <%--<%@ include file="/views/jsp/blog/b01/blog-content-forwardphrase.jsp" %>--%>
                                    <%--</c:when>--%>
                                    <%--<c:otherwise>--%>
                                        <%--<!--转贴长文发布 begin-->--%>
                                        <%--<%@ include file="/views/jsp/blog/b01/blog-content-forwardtext.jsp" %>--%>
                                        <%--<!--转贴长文发布 ending-->--%>
                                    <%--</c:otherwise>--%>
                                <%--</c:choose>--%>
                            <%--</c:when>--%>
                            <%--<c:otherwise>--%>
                                <%--<div class="contentremove">--%>
                                    <%--<p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>--%>
                                <%--</div>--%>
                            <%--</c:otherwise>--%>
                        <%--</c:choose>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</dd>--%>
        <%--</dl>--%>
        <%--<!-- 转帖 -->--%>
    <%--</c:otherwise>--%>
<%--</c:choose>--%>

<!-- 原文footer -->
<c:if test="${fn:length(contentGameList)>0}">
    <div class="article-relevancegame">
        <h2 class="article-relevancegame-title">本文提到的游戏</h2>
        <a name="gamelist" id="gamelist"></a>
        <ul>
            <c:forEach var="game" items="${contentGameList}" varStatus="st">
                <li <c:if test="${st.index%2==1}">cclass="bg"</c:if>>
                    <a href="${URL_WWW}/game/${game.gameCode}" class="game-pic">
                        <c:forEach items="${game.icon.images}" var="icon">
                            <img src="${uf:parseOrgImg(icon.ll)}" width="50" height="50"/><span></span>
                        </c:forEach>
                    </a>

                    <div>
                        <h2><a href="${URL_WWW}/game/${game.gameCode}">${game.resourceName}</a></h2>

                        <p><span><c:if
                                test="${game.deviceSet!=null && fn:length(game.deviceSet.deviceSet)>0}">平台：<c:forEach
                                items="${game.deviceSet.deviceSet}" var="device" varStatus="st" begin="0"
                                end="2">${device.code}<c:if test="${!st.last}">/</c:if></c:forEach></c:if></span>
                            <span><c:if test="${game.categorySet!=null && fn:length(game.categorySet.categorySet)>0}">类型：<c:forEach
                                    items="${game.categorySet.categorySet}" var="category" varStatus="st" begin="0"
                                    end="2">${category.code}<c:if test="${!st.last}">/</c:if></c:forEach></c:if></span>
                        </p>
                    </div>
                    <c:choose>
                        <c:when test="${game.gameProperties!=null && fn:length(game.gameProperties.channels)>0}">
                            <c:forEach var="channel" items="${game.gameProperties.channels}" begin="0" end="0">
                                <a href="${channel.value}" target="_blank" class="game-download-link">下载</a>
                            </c:forEach>
                        </c:when>
                        <c:otherwise><span class="game-download-nolink">暂无下载</span></c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>
    </div>
</c:if>
<%--<c:if test="${blogContent.content.contentTag.tags!=null && fn:length(blogContent.content.contentTag.tags)>0}">--%>
<%--<div class="tags">--%>
<%--<p>--%>
<%--<c:forEach var="tag" items="${blogContent.content.contentTag.tags}">--%>
<%--<a href="${URL_WWW}/search/content/${tag}/">#<c:out value="${tag}"/></a>--%>
<%--</c:forEach>--%>
<%--</p>--%>
<%--</div>--%>
<%--</c:if>--%>

<!-- 原文footer -->
<%--</div>--%>
<!-- 文章被喜欢统计 -->
<br/>
<c:if test="${blogContent.content.relationSet.groupPointRelation!=null}">
    <div class="pingfen"><a href="#">评分<span>+${blogContent.content.relationSet.groupPointRelation.relationValue}</span></a>
    </div>
</c:if>
<div class="favorite-num" id="content_operate">
     <span id="favorite_detail" <c:if test="${blogContent.content.favorTimes==0}">style="display:none"</c:if>>本文已被<a
             href="javascript:void(0);" id="favDetailLink"
             data-cid="${blogContent.content.contentId}"
             data-cuno="${blogContent.content.uno}"><b>${blogContent.content.favorTimes}</b>人</a>喜欢
     </span>
    <c:if test="${blogContent.content.voteSubjectId == null &&(hasSuperPrivacy || (blogContent.content.uno eq userSession.blogwebsite.uno))}">
        <c:choose>
            <c:when test="${blogContent.content.publishType.code eq 'fwd'}">
                <a href="javascript:void(0)" id="edit_forward_${blogContent.content.contentId}"
                   data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}" title="编辑">编辑</a>
            </c:when>
            <c:otherwise>
                <a href="${URL_WWW}/content/edit/${blogContent.content.uno}/${blogContent.content.contentId}"
                   title="编辑">编辑</a>
            </c:otherwise>
        </c:choose>&nbsp;|&nbsp;
    </c:if>
    <c:if test="${blogContent.content.uno eq userSession.blogwebsite.uno}">
        <form style="display:inline" id="del_content_form"
              action="${ctx}/content/del/${blogContent.content.uno}/${blogContent.content.contentId}"
              method="post">
            <a id="link_del_content" href="javascript:void(0)" title="删除">删除</a>
        </form>
    </c:if>
</div>


<!-- 回复框 -->
<div id="div_ia_tab" class="partical_tab">
    <a name="reply_area_link"></a>
</div>
<c:if test="${blogContent.content.replyTimes==0}">
    <div id="noComments" class="noComments">目前没有评论，欢迎你发表观点</div>
</c:if>
<c:forEach var="interaction" items="${interactionList}" varStatus="st">
    <a name="${interaction.interaction.interactionId}"></a>

    <div class="area blogopinion clearfix " name="cont_cmt_list_${interaction.interaction.interactionId}">
        <dl>
            <dt class="personface">
                <a href="${URL_WWW}/people/${interaction.interactionProfile.blog.domain}" name="atLink"
                   title="${interaction.interactionProfile.blog.screenName}">
                    <img src="${uf:parseFacesInclude(interaction.interactionProfile.blog.headIconSet,interaction.interactionProfile.detail.sex,"s" , true, 0, 1)[0]}"
                         class="user" width="58" height="58">
                </a>
            </dt>
            <dd class="textcon discuss_building">
                <em>#${interaction.interaction.floorNo}</em>
                <a href="${URL_WWW}/people/${interaction.interactionProfile.blog.domain}" name="atLink"
                   class="author"
                   title="${interaction.interactionProfile.blog.screenName}">${interaction.interactionProfile.blog.screenName}</a>

                <c:if test="${interaction.interactionProfile.detail.verifyType !=null && interaction.interactionProfile.detail.verifyType.getCode()!='n'}">
                    <a href="${URL_WWW}/people/${interaction.interactionProfile.blog.domain}"
                       class="${interaction.interactionProfile.detail.verifyType.code}vip"
                       title="<fmt:message key="verify.profile.${interaction.interactionProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                </c:if>
                <p
                        <c:if test="${fn:length(rid)>0 && rid==interaction.interaction.interactionId}">style="font-weight:bold"</c:if>>${interaction.interaction.interactionContent} </p>
                <c:if test="${interaction.interaction.interactionImages!=null && fn:length(interaction.interaction.interactionImages.images)>0}">
                    <ul class="clearfix">
                        <c:forEach var="img" items="${interaction.interaction.interactionImages.images}">
                            <li style="display:list-item;">
                                <a href="${uf:parseBFace(img.m)}" target="_blank">
                                    <img data-jh="${img.h}" data-jw="${img.w}" src="${uf:parseMFace(img.m)}">
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <div class="discuss_bdfoot">
                        ${dateutil:parseDate(interaction.interaction.createDate)}
                    <c:if test="${ userSession!=null && (interaction.interaction.interactionUno eq userSession.blogwebsite.uno ||  profile.blog.uno eq userSession.blogwebsite.uno)}">
                        <a href="javascript:void(0)" name="del_reply" data-cid="${interaction.interaction.contentId}"
                           data-cuno="${interaction.interaction.contentUno}"
                           data-replyid="${interaction.interaction.interactionId}">删除</a> | </c:if>
                    <c:choose>
                        <c:when test="${interaction.childrenRows==null || fn:length(interaction.childrenRows.rows)==0}">
                            <a href="javascript:void(0);" name="togglechildrenreply_area"
                               data-rid="${interaction.interaction.interactionId}"
                               data-runo="${interaction.interaction.interactionUno}"
                               data-cid="${interaction.interaction.contentId}"
                               data-cuno="${interaction.interaction.contentUno}">回复</a>
                        </c:when>
                        <c:otherwise><a href="javascript:void(0);" name="togglechildrenreply_area" class="putaway"
                                        data-rid="${interaction.interaction.interactionId}"
                                        data-runo="${interaction.interaction.interactionUno}"
                                        data-cid="${interaction.interaction.contentId}"
                                        data-cuno="${interaction.interaction.contentUno}">收起回复</a></c:otherwise></c:choose>
                </div>
                    <%--楼中楼--%>
                <div class="discuss_bd_list discuss_border"
                     <c:if test="${interaction.childrenRows==null || fn:length(interaction.childrenRows.rows)==0}">style="display:none"</c:if>>
                    <div id="children_reply_list_${interaction.interaction.interactionId}">
                        <c:if test="${interaction.childrenRows!=null && fn:length(interaction.childrenRows.rows)>0}">
                            <c:forEach var="children" items="${interaction.childrenRows.rows}" varStatus="st">
                                <div class="conmenttx clearfix"
                                     name="cont_cmt_list_${children.interaction.interactionId}" style="">
                                    <div class="conmentface">
                                        <div class="commenfacecon">
                                            <a class="cont_cl_left" name="atLink"
                                               title="${children.interactionProfile.blog.screenName}"
                                               href="${URL_WWW}/people/${children.interactionProfile.blog.domain}">
                                                <img src="${uf:parseFacesInclude(children.interactionProfile.blog.headIconSet,children.interactionProfile.detail.sex,"s" , true, 0, 1)[0]}"
                                                     width="33px" height="33px"/>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="conmentcon">
                                        <a href="${URL_WWW}/people/${children.interactionProfile.blog.domain}"
                                           name="atLink"
                                           title="${children.interactionProfile.blog.screenName}">${children.interactionProfile.blog.screenName}</a>
                                        <c:if test="${children.interactionProfile.detail.verifyType !=null && children.interactionProfile.detail.verifyType.code!='n'}">
                                            <a href="${URL_WWW}/people/${children.interactionProfile.blog.domain}"
                                               class="${children.interactionProfile.detail.verifyType.code}vip"
                                               title="<fmt:message key="verify.profile.${children.interactionProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                                        </c:if>：${children.interaction.interactionContent}
                                        <div class="commontft clearfix"><span
                                                class="reply_time">${dateutil:parseDate(children.interaction.createDate)}</span>
                                            <span class="delete">
                                                <c:if test="${userSession!=null && (children.interaction.interactionUno eq userSession.blogwebsite.uno ||  profile.blog.uno eq userSession.blogwebsite.uno)}">
                                                    <a href="javascript:void(0)"
                                                       name="del_reply"
                                                       data-cid="${children.interaction.contentId}"
                                                       data-cuno="${children.interaction.contentUno}"
                                                       data-replyid="${children.interaction.interactionId}">删除</a> |
                                                </c:if>
                                                <a href="javascript:void(0);" name="childrenreply_area"
                                                   data-rid="${interaction.interaction.interactionId}"
                                                   data-runo="${interaction.interaction.interactionUno}"
                                                   data-pid="${children.interaction.interactionId}"
                                                   data-puno="${children.interaction.interactionUno}"
                                                   data-pname="${children.interactionProfile.blog.screenName}"
                                                   data-cid="${interaction.interaction.contentId}"
                                                   data-cuno="${interaction.interaction.contentUno}">回复</a>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:if>
                    </div>
                    <c:if test="${interaction.childrenRows!=null && interaction.childrenRows.page.maxPage>1}">
                        <c:set var="ajaxpage" value="${interaction.childrenRows.page}"/>
                        <%@ include file="/views/jsp/page/simplepageno.jsp" %>
                    </c:if>
                        <%--楼中楼回复--%>
                    <div class="discuss_reply" id="post_childreply_area_${interaction.interaction.interactionId}">
                        <a name="replypost_mask" href="javascript:void(0);" class="discuss_text01">我也说一句</a>

                        <div class="discuss_reply reply_box01" style="display:none">
                            <textarea name="content"
                                      id="childrenreply_textarea_${interaction.interaction.interactionId}"
                                      data-rid="${interaction.interaction.interactionId}" cols="" rows=""
                                      class="discuss_text focus" style="font-family:Tahoma, '宋体';"
                                      warp="off"></textarea>

                            <div class="related clearfix">
                                <a class="commenface" id="childrenreply_mood_${interaction.interaction.interactionId}"
                                   title="表情" href="javascript:void(0)"></a>

                                <div class="transmit clearfix">
                                    <a name="childreply_submit"
                                       data-rid="${interaction.interaction.interactionId}"
                                       data-runo="${interaction.interaction.interactionUno}"
                                       data-pid="${children.interaction.interactionId}"
                                       data-puno="${children.interaction.interactionUno}"
                                       data-cid="${interaction.interaction.contentId}"
                                       data-cuno="${interaction.interaction.contentUno}"
                                       class="submitbtn fr"><span>评 论</span></a>
                                            <span class="y_zhuanfa">
                                                <label><input type="checkbox" class="checktext" name="forwardRoot">同时转发到我的博客</label>
                                            </span>
                                </div>
                            </div>
                        </div>
                    </div>
            </dd>
        </dl>
    </div>
</c:forEach>
<!-- 分页 -->
<c:if test="${page.maxPage>1}">
    <c:set var="pagestyle" value="float:auto; padding:5px 0; clear:none"/>
    <c:set var="pageurl" value="/note/${blogContent.content.contentId}"/>
    <c:set var="anchorPoint" value="reply_area_link"></c:set>
    <%@include file="/views/jsp/page/goto.jsp" %>
</c:if>
<!-- 分页 -->
<!-- 评论 -->
<div class="blog_comment noborder" id="post_reply">
    <span id="reply_num" class="limited">还可输入<b>300</b>字</span>

    <div class="talk_wrapper clearfix">
        <input type="hidden" value="${blogContent.content.contentId}" id="hidden_cid"/>
        <input type="hidden" value="${blogContent.content.uno}" id="hidden_cuno"/>
        <c:if test="${blogContent.rootContent!=null && blogContent.rootContent.removeStatus.code=='n'}">
            <input type="hidden" value="${blogContent.rootContent.contentId}" id="hidden_rcid"/>
            <input type="hidden" value="${blogContent.rootProfile.blog.screenName}" id="hidden_rname"/>
            <input type="hidden" value="${blogContent.rootProfile.blog.domain}" id="hidden_rdomain"/>
        </c:if>
        <textarea
                <c:if test="${userSession.blogwebsite==null}">disabled="disabled"</c:if> name="content"
                id="reply_content" cols="" rows="" class="talk_text" style="font-family:Tahoma, '宋体';"
                warp="off"></textarea>
        <c:if test="${userSession.blogwebsite==null}">
            <div class="wrapper_unlogin">您需要<a href="javascript:void(0)" id="maskLogin">登录</a>后才能评论</div>
        </c:if>
    </div>
    <div class="related clearfix">
        <div class="transmit_pic clearfix" id="reply_image_${blogContent.content.contentId}">
            <a class="commenface" id="reply_mood" title="表情" href="javascript:void(0)"></a>

            <div class="t_pic" name="reply_image_icon">
                <a class="t_pic1" href="javascript:void(0)">图片</a>
            </div>
            <div style="display:none;" class="t_pic_more" name="reply_image_icon_more">
                <a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_blog_upload_img"
                   data-cid="reply_image_${blogContent.content.contentId}">图片</a>
                <a title="链接" class="t_more" href="javascript:void(0)" name="reply_blog_upload_img_link"
                   data-cid="reply_image_${blogContent.content.contentId}">链接</a>
            </div>
            <a id="reply_submit" class="submitbtn fr" data-cid="${blogContent.content.contentId}"
               data-cuno="${blogContent.content.uno}"><span>评 论</span></a>
        </div>
        <div class="transmit clearfix">
                      <span class="y_zhuanfa">
                          <label><input type="checkbox" class="checktext" id="check_forwardroot">同时转发到我的博客</label>
                          <span class="tongbu">
                          <label><input type="checkbox" class="publish_s" id="sync_forward"
                                        disabled="disabled"><span>同步</span></label>
                          <em class="install"></em>
                          </span>
                          <br>
                          <c:if test="${blogContent.rootContent!=null && blogContent.rootContent.removeStatus.code=='n'}">
                              <label for="check_replyroot"><input type="checkbox" class="checktext" id="check_replyroot"
                                                                  data-rcid="${blogContent.rootContent.contentId}">同时评论给原文作者
                                  <a href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}" name="atLink"
                                     title="<c:out value="${blogContent.rootProfile.blog.screenName}"/>">${blogContent.rootProfile.blog.screenName}</a></label>
                          </c:if>
                      </span>
        </div>
        <div id="reply_error"></div>
    </div>
</div>
</div>

<!-- 右侧内容 -->
<%@ include file="/views/jsp/blog/b01/blog-right.jsp" %>
</div>

</div>
<%@ include file="/views/jsp/blog/b01/blog-footer.jsp" %>
<!--content-->

<script src="${URL_LIB}/static/js/common/seajs.js" type="text/javascript"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js" type="text/javascript"></script>
<script type="text/javascript">
    var cid = '${blogContent.content.contentId}';
    var cuno = '${blogContent.content.uno}';
    var currentProfile = {
        uno:'${profile.blog.uno}'};
    seajs.use('${URL_LIB}/static/js/init/blog-b01-init.js');
</script>
<script src="${URL_LIB}/static/js/common/pv.js" type="text/javascript"></script>
<script type="text/javascript">
    lz_main();
</script>
<script type="text/javascript" id="bdshare_js" data="type=slide&amp;img=8&amp;pos=right&amp;uid=6528936"></script>
<script type="text/javascript" id="bdshell_js"></script>
<script type="text/javascript">
    var bds_config = {"snsKey":"{'tsina':'1245341962','tqq':'100292513','t163':'qCEuLkaOUGKXxUdm','tsohu':''}"};
    document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000);
</script>
</body>
</html>
