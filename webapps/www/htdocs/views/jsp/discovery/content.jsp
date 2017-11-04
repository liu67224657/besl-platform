<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<div id="content_wall_area" class="pic_see_con">
<div class="pic_see_wrapcon">
<div class="pic_seewrap" id="seewrap">
    <div class="pic_opreat">
        <div class="see_operate">
            <c:if test="${contentidx != null}">
                <div class="flip">
                    <a href="javascript:void(0)" id="precontent" data-idx="${contentidx}">上一篇</a><em>|</em>
                    <a href="javascript:void(0)" id="nextcontent" data-idx="${contentidx}">下一篇</a>
                </div>
            </c:if>
            <a href="javascript:void(0)" id="see_close" class="see_close"></a>
        </div>
    </div>
</div>
<c:choose>
<c:when test="${blogContent.content!=null && !(blogContent.content.removeStatus.code eq 'y')}">
<div class="see_area01" id="sea_area">
<div class="pic_see_btn">
    <a href="javascript:void(0);" class="pic_pinglun" id="showReplyOnWall" title="评论"></a>
    <a href="javascript:void(0);"
       class="<c:choose><c:when test="${blogContent.favorite}">pic_love_on</c:when><c:otherwise>pic_love</c:otherwise></c:choose>"
       id="fav_wall_link" data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}"></a>
</div>
<div class="see_content">
<div class="area noborder">
    <c:choose>
        <c:when test="${blogContent.content.contentType.hasPhrase()}">
            <div class="see_about">
                    <span class="by_author">作者：<a href="${URL_WWW}/people/${blog.domain}" target="_blank">${blog.screenName}</a></span>
                                <span class="from">
       <c:choose>
           <c:when test="${blogContent.board!=null}">
                       来自：<a href="${URL_WWW}/group/${blogContent.board.gameCode}/talk" target="_blank">${blogContent.board.gameName}小组</a>
           </c:when>
           <c:otherwise>
               来自：着迷网
           </c:otherwise>
       </c:choose>
    </span>
                    <span class="time"><a href="${URL_WWW}/note/${blogContent.content.contentId}"
                                          target="_blank">${dateutil:parseDate(blogContent.content.publishDate)}</a></span>
            </div>
            <!--短文格式-->
            <p>${blogContent.content.content}</p>
            <!--图片显示-->
            <c:if test="${fn:length(blogContent.content.images.images)>0}">
                <c:forEach var="img" items="${blogContent.content.images.images}">
                    <div class="blogarticle_img">
                        <a href="<c:out value="${uf:parseBFace(img.m)}"/>" target="_blank">
                            <img src="<c:out value="${uf:parseMFace(img.m)}"/>">
                        </a>
                    </div>
                </c:forEach>
            </c:if>
            <!--视频-->
            <c:if test="${fn:length(blogContent.content.videos.videos)>0}">
                <c:forEach var="video" items="${blogContent.content.videos.videos}">
                    <div class="single_videoview clearfix">
                        <object width="528" height="420" align="middle"
                                codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0"
                                classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000">
                            <param value="true" name="AllowFullScreen">
                            <param value="sameDomain" name="allowScriptAccess">
                            <param value="${video.flashUrl}" name="movie">
                            <param value="Transparent" name="wmode">
                            <embed width="528" height="420" src="${video.flashUrl}"
                                   type="application/x-shockwave-flash" wmode="Transparent"
                                   allowfullscreen="true">
                        </object>
                    </div>
                </c:forEach>
            </c:if>
            <!--视频 ending-->

            <!--音频 -->
            <c:if test="${fn:length(blogContent.content.audios.audios)>0}">
                <c:forEach var="audio" items="${blogContent.content.audios.audios}">
                    <div class="single_music">
                        <object align="middle" width="283" height="33"
                                classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                                codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                            <param name="allowScriptAccess" value="sameDomain">
                            <param name="movie" value="${audio.flashUrl}">
                            <param name="wmode" value="Transparent">
                            <embed width="283" height="33" wmode="Transparent"
                                   type="application/x-shockwave-flash"
                                   src="${audio.flashUrl}">
                        </object>
                    </div>
                    <div class="single_music_view clearfix">
                        <img width="141" height="147" src="${uf:parseAudioM(audio.url)}">
                    </div>
                </c:forEach>
            </c:if>
        </c:when>
        <c:otherwise>
            <!--长文发布 begin-->
            <h3><c:out value="${blogContent.content.subject}"/></h3>

            <div class="see_about">
                    <span class="by_author">作者：<a href="${URL_WWW}/people/${blog.domain}" target="_blank">${blog.screenName}</a></span>
                                <span class="from">
       <c:choose>
           <c:when test="${blogContent.board!=null}">
                       来自：<a href="${URL_WWW}/group/${blogContent.board.gameCode}/talk" target="_blank">${blogContent.board.gameName}小组</a>
           </c:when>
           <c:otherwise>
               来自：着迷网
           </c:otherwise>
       </c:choose>
    </span>
                    <span class="time"><a href="${URL_WWW}/note/${blogContent.content.contentId}"
                                          target="_blank">${dateutil:parseDate(blogContent.content.publishDate)}</a></span>
            </div>
            <p>
                    ${blogContent.content.content}
            </p>
            <!--长文发布 ending-->
        </c:otherwise>
    </c:choose>
    <div class="area_ft">
        <div class="operate">
            <ul>
                <li id="wallDescription">
                    <%--本文已被阅读${blogContent.content.viewTimes}次--%>
                    <c:if test="${blogContent.content.favorTimes>0}">
                        <span>本文已被<a href="javascript:void(0);" name="favDetailLink"
                                  data-cid="${blogContent.content.contentId}"
                                  data-cuno="${blogContent.content.uno}"><b>${blogContent.content.favorTimes}</b>人</a>喜欢</span>
                    </c:if></li>
            </ul>
        </div>
    </div>
</div>



<div id="wall_ia_tab" style="padding-bottom:30px;margin-top:20px;">
    <div class="partical_tab">
        <ul>
            <li id="ia_tab_reply">
                   评论<c:if test="${blogContent.content.replyTimes>0}">（<span id="ia_header_num_reply">${blogContent.content.replyTimes}</span>）</c:if>
                    </li>
            </li>
        </ul>
    </div>
     <div id="reply_area">
        <c:choose>
        <c:when test="${fn:length(interactionList)==0}"><div id="noComments" class="noComments" >目前没有评论，欢迎你发表观点</div></c:when>
        <c:otherwise>
        <c:forEach var="interaction" items="${interactionList}" varStatus="st">
            <div class="area blogopinion clearfix " name="cont_cmt_list_${interaction.interaction.interactionId}">
                <dl>
                    <dt class="personface">
                        <a href="${URL_WWW}/people/${interaction.interactionProfile.blog.domain}" name="atLink"
                           title="${interaction.interactionProfile.blog.screenName}">
                            <img src="${uf:parseFacesInclude(interaction.interactionProfile.blog.headIconSet,interaction.interactionProfile.detail.sex,"s" , true, 0, 1)[0]}" class="user" width="58" height="58">
                        </a>
                    </dt>
                    <dd class="textcon discuss_building">
                        <em>#${interaction.interaction.floorNo}</em>
                        <a href="${URL_WWW}/people/${interaction.interactionProfile.blog.domain}" name="atLink"
                           class="author"
                           title="${interaction.interactionProfile.blog.screenName}">${interaction.interactionProfile.blog.screenName}</a>
                        <c:if test="${interaction.interactionProfile.detail.verifyType !=null && interaction.interactionProfile.detail.verifyType.code != 'n'}">
                            <a href="${URL_WWW}/people/${interaction.interactionProfile.blog.domain}"
                               class="${interaction.interactionProfile.detail.verifyType.code}vip"
                               title="<fmt:message key="verify.profile.${interaction.interactionProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                        </c:if>
                        <p>${interaction.interaction.interactionContent} </p>
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
                                <a href="javascript:void(0)" name="del_reply" data-cid="${interaction.interaction.contentId}"  data-cuno="${interaction.interaction.contentUno}" data-replyid="${interaction.interaction.interactionId}">删除</a> | </c:if>
                            <c:choose>
                                <c:when test="${interaction.childrenRows==null || fn:length(interaction.childrenRows.rows)==0}">
                                    <a href="javascript:void(0);" name="togglechildrenreply_area"
                                       data-rid="${interaction.interaction.interactionId}"
                                       data-runo="${interaction.interaction.interactionUno}"
                                       data-cid="${interaction.interaction.contentId}"
                                       data-cuno="${interaction.interaction.contentUno}">回复</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="javascript:void(0);" name="togglechildrenreply_area" class="putaway"
                                               data-rid="${interaction.interaction.interactionId}"
                                               data-runo="${interaction.interaction.interactionUno}"
                                               data-cid="${interaction.interaction.contentId}"
                                               data-cuno="${interaction.interaction.contentUno}">收起回复</a>
                                        </c:otherwise>
                                    </c:choose>
                        </div>
                            <%--楼中楼--%>
                        <div class="discuss_bd_list discuss_border" <c:if test="${interaction.childrenRows==null || fn:length(interaction.childrenRows.rows)==0}">style="display:none"</c:if>>
                            <div id="children_reply_list_${interaction.interaction.interactionId}">
                                <c:if test="${interaction.childrenRows!=null && interaction.childrenRows.page.totalRows>5}">
                                   剩余${interaction.childrenRows.page.totalRows-5}条评论， <a target="_blank" href="${URL_WWW}/note/${blogContent.content.contentId}">点击查看</a>
                                </c:if>
                            <c:if test="${interaction.childrenRows!=null && fn:length(interaction.childrenRows.rows)>0}">
                                <c:forEach var="children" items="${interaction.childrenRows.rows}" varStatus="st">
                                     <div class="conmenttx clearfix" name="cont_cmt_list_${children.interaction.interactionId}" style="">
                                         <div class="conmentface">
                                             <div class="commenfacecon">
                                                 <a class="cont_cl_left" name="atLink" title="${children.interactionProfile.blog.screenName}" href="${URL_WWW}/people/${children.interactionProfile.blog.domain}">
                                                     <img src="${uf:parseFacesInclude(children.interactionProfile.blog.headIconSet,children.interactionProfile.detail.sex,"s" , true, 0, 1)[0]}" width="33px" height="33px" />
                                                 </a>
                                             </div>
                                         </div>
                                    <div class="conmentcon">
                                        <a href="${URL_WWW}/people/${children.interactionProfile.blog.domain}" name="atLink" title="${children.interactionProfile.blog.screenName}">${children.interactionProfile.blog.screenName}</a>
                                        <c:if test="${children.interactionProfile.detail.verifyType !=null && children.interactionProfile.detail.verifyType.code != 'n'}">
                                            <a href="${URL_WWW}/people/${children.interactionProfile.blog.domain}"
                                               class="${children.interactionProfile.detail.verifyType.code}vip"
                                               title="<fmt:message key="verify.profile.${children.interactionProfile.detail.verifyType.code}" bundle="${userProps}"/>"></a>
                                        </c:if>：回复
                                        ${children.interaction.interactionContent}
                                        <div class="commontft clearfix"><span class="reply_time">${dateutil:parseDate(children.interaction.createDate)}</span>
                                            <span class="delete">
                                                <c:if test="${userSession!=null && (children.interaction.interactionUno eq userSession.blogwebsite.uno ||  profile.blog.uno eq userSession.blogwebsite.uno)}">
                                                    <a href="javascript:void(0)"
                                                       name="del_reply"
                                                       data-cid="${children.interaction.contentId}"
                                                       data-cuno="${children.interaction.contentUno}"
                                                       replyid="${children.interaction.interactionId}">删除</a> |
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
                            <%--楼中楼回复--%>
                            <div class="discuss_reply" id="post_childreply_area_${interaction.interaction.interactionId}">
                                <a name="replypost_mask" href="javascript:void(0);" class="discuss_text01">我也说一句</a>
                                 <div class="discuss_reply" style="display:none">
                                    <textarea name="content" id="childrenreply_textarea_${interaction.interaction.interactionId}" data-rid="${interaction.interaction.interactionId}" cols="" rows="" class="discuss_text focus" style="font-family:Tahoma, '宋体';" warp="off"></textarea>

                                    <div class="related clearfix">
                                        <div class="transmit clearfix" >
                                           <a class="commenface" id="childrenreply_mood_${interaction.interaction.interactionId}" title="表情"  href="javascript:void(0)"></a>
                                           <a name="childreply_submit"
                                                   data-rid="${interaction.interaction.interactionId}"
                                                   data-runo="${interaction.interaction.interactionUno}"
                                                   data-pid="${children.interaction.interactionId}"
                                                   data-puno="${children.interaction.interactionUno}"
                                                   data-cid="${interaction.interaction.contentId}"
                                                   data-cuno="${interaction.interaction.contentUno}" class="submitbtn fr"><span>评 论</span></a>
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
        </c:otherwise>
    </c:choose>
     </div>
    <c:if test="${blogContent.content.floorTimes>10}">
        <br/>
        <a href="${URL_WWW}/note/${blogContent.content.contentId}" target="_blank"
           class="more_dis">查看更多评论&gt;&gt;</a>
    </c:if>



    <div id="post_reply" class="blog_comment noborder" style="">
    <span class="limited" id="reply_num">还可输入<b>300</b>字</span>

    <div class="talk_wrapper clearfix" >
        <input type="hidden" value="${blogContent.content.contentId}" id="hidden_cid"/>
        <input type="hidden" value="${blogContent.content.uno}" id="hidden_cuno"/>
        <input type="hidden" value="${blog.screenName}" id="hidden_screenName"/>
        <textarea <c:if test="${userSession==null}">disabled="disabled"</c:if> class="talk_text" style="font-family:Tahoma, '宋体';" rows="" cols="" id="reply_content" name="content"></textarea>
        <c:if test="${userSession==null}">
            <div class="wrapper_unlogin">您需要<a href="javascript:void(0)" id="maskLoginOnWall">登录</a>后才能评论</div>
        </c:if>
    </div>
    <div class="related clearfix" style="position:static;">
        <div class="transmit_pic clearfix" style="position:relative;" id="reply_image_${blogContent.content.contentId}">
            <a id="reply_mood" class="commenface" title="表情" href="javascript:void(0)"></a>
            <div class="t_pic" name="reply_image_icon">
                <a class="t_pic1" href="javascript:void(0)">图片</a>
            </div>
            <div style="display:none;" class="t_pic_more" name="reply_image_icon_more">
                <a title="图片" class="t_pic1" href="javascript:void(0)" name="reply_upload_img" data-cid="reply_image_${blogContent.content.contentId}">图片</a>
                <a title="链接" class="t_more" href="javascript:void(0)" name="reply_upload_img_link" data-cid="reply_image_${blogContent.content.contentId}">链接</a>
            </div>
            <a class="submitbtn fr" id="reply_submit" data-cid="${blogContent.content.contentId}" data-cuno="${blogContent.content.uno}"><span>评 论</span></a>
        </div>
        <div class="transmit clearfix">
            <span class="y_zhuanfa">
                <label><input type="checkbox" id="check_forwardroot" class="checktext">同时转发到我的博客</label>
            </span>
            <span class="tongbu">
                <label><input type="checkbox" disabled="disabled" id="sync_forward" class="publish_s"><span>同步</span></label><em class="install"></em>
            </span>
        </div>
        <div id="reply_error"></div>
    </div>
</div>
      </div>
</div>
</c:when>
<c:otherwise>
    <div class="see_area01" id="sea_area">
        <div class="see_content">
            <p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>
        </div>
    </div>
</c:otherwise>
</c:choose>

</div>
</div>
<div class="bGradient"></div>
</div>
