<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<%
	request.setAttribute("decorator","none");
	response.setHeader("Cache-Control","no-cache");//http1.1
	response.setHeader("Pragma","no-cache");//http1.0
	response.setDateHeader("Expires",0);
 %>
<script type="text/javascript">
        var unfocusCallback=unRemoveCallBack;
</script>
<c:forEach var="blogContent" items="${blogList}">
<div id="conent_${blogContent.content.contentId}" class="tag_cont_list" onmouseout="hideInfo('<c:out value="${blogContent.content.contentId}"/>');">
<div class="tag_cl_left" onmouseover="javascript:displayInfo($(this),'<c:out value="${blogContent.content.contentId}"/>','<c:out value="${blogContent.profile.blog.uno}"/>');">
    <img src="<c:out value='${uf:parseFacesInclude(blogContent.profile.blog.headIconSet,blogContent.profile.detail.sex,"s" , true, 0, 1)[0]}'/>" width="58px" height="58px"/>
</div>
<div class="tag_cl_right">
<h3><a href="${URL_WWW}/people/${blogContent.profile.blog.domain}"><c:out value="${blogContent.profile.blog.screenName}"/></a>：</h3>
    <span class="right"><a href="/note/${blogContent.content.contentId}" target="_blank">查看全文</a></span>
<c:choose>
<c:when test="${blogContent.content.publishType.code eq 'org'}">
    <!--不是转贴-->
    <c:choose>
      <c:when test="${blogContent.content!=null && !(blogContent.content.removeStatus.code eq 'y')}">
    <!--文字格式-->
    <div class="cont_text">
        <h2>${blogContent.content.subject}</h2>
        <p>${blogContent.content.content}</p>
    </div>
    <!--图片显示-->
    <c:if test="${fn:length(blogContent.content.images.images)>0}">
        <c:choose>
            <c:when test="${fn:length(blogContent.content.images.images)==1}">
                <div class="cont_text_cont" onclick="pOneImg('img_${blogContent.content.contentId}');" id="img_${blogContent.content.contentId}">
                    <c:forEach var="img" items="${blogContent.content.images.images}">
                        <img src="<c:out value="${uf:parseSSFace(img.ss)}"/>"/>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="cont_pic_s" onclick="pManyImg('img_${blogContent.content.contentId}')" id="img_${blogContent.content.contentId}">
                    <ul class="clearfix">
                        <c:forEach var="img" items="${blogContent.content.images.images}" varStatus="status">
                            <li <c:if test="${status.index>4}">style="display:none"</c:if>><p><img src="${uf:parseSFace(img.m)}"/></p></li>
                        </c:forEach>
                    </ul>
                <c:if test="${fn:length(blogContent.content.images.images)>5}">
                    <p>（共${fn:length(blogContent.content.images.images)}张，点击图片查看更多）</p>
                </c:if>
                </div>

            </c:otherwise>
        </c:choose>
    </c:if>

    <!--视频-->

    <c:if test="${fn:length(blogContent.content.videos.videos)>0}">
    <div class="cont_video" id="vc_${blogContent.content.contentId}">
        <c:forEach var="video" items="${blogContent.content.videos.videos}">
                <div class="cont_video_left" id="vs_${blogContent.content.contentId}">
                    <a href="javascript:void(0)" onclick="javascript:pBigVideo('${blogContent.content.contentId}');">
                        <img width="165" height="124" src="${video.url}"/>
                        <input type="hidden" id="hid_vf_${blogContent.content.contentId}" name="videoflash" value="${video.flashUrl}"/>
                    </a>
                    <a title="播放" class="video_btn" href="javascript:void(0)"  onclick="javascript:pBigVideo('${blogContent.content.contentId}');"></a>
                </div>
        </c:forEach>
    </div>
    </c:if>
    <!--视频 ending-->

    <!--音频 -->
    <c:if test="${fn:length(blogContent.content.audios.audios)>0}">
        <c:choose>
            <c:when test="${(blogContent.content.content=='' || fn:length(blogContent.content.content)==0 )
                                             && fn:length(blogContent.content.videos.videos)==0
                                             && fn:length(blogContent.content.images.images)==0}">
                <!--只有音乐-->
                <c:forEach var="audio" items="${blogContent.content.audios.audios}">
                    <div class="cont_music">
                        <div class="cont_music_left">
                            <a href="javascript:void(0)">
                                <span><img width="150" src="${uf:parseAudioM(audio.url)}"></span>
                            </a>
                        </div>
                        <div class="cont_music_right">
                            <h2><c:out value="${audio.title}"/></h2>
                            <object width="283" height="33" align="middle"
                                    classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                                    codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                                <param name="allowScriptAccess" value="sameDomain">
                                <param name="movie"
                                       value="<c:out value="${audio.flashUrl}"/>">
                                <param name="wmode" value="Transparent">
                                <embed width="283" height="33" wmode="Transparent"
                                       type="application/x-shockwave-flash"
                                       src="<c:out value="${audio.flashUrl}"/>">
                            </object>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="cont_music">
                    <c:forEach var="audio" items="${blogContent.content.audios.audios}">
                        <div class="cont_music_right">
                            <object width="283" height="33" align="middle"
                                    classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                                    codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                                <param name="allowScriptAccess" value="sameDomain">
                                <param name="movie"
                                       value="<c:out value="${audio.flashUrl}"/>">
                                <param name="wmode" value="Transparent">
                                <embed width="283" height="33" wmode="Transparent"
                                       type="application/x-shockwave-flash"
                                       src="<c:out value="${audio.flashUrl}"/>">
                            </object>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>
      </c:when>
      <c:otherwise>
          <div class="cont_text">
                <p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p>
          </div>
      </c:otherwise>
    </c:choose>
</c:when>
<c:otherwise>
    <div class="cont_text">
        <p>${blogContent.content.content}</p>
    </div>
    <!--转发-->
    <div class="cont_zf">
        <div class="cont_zft"></div>
        <c:choose>
            <c:when test="${blogContent.rootContent!=null && !(blogContent.rootContent.removeStatus.code eq 'y')}">
                <!--视频单个发布-->
                <h3>转发自<a href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}"><c:out
                        value="${blogContent.rootProfile.blog.screenName}"/></a>：</h3>
                <span class="right"><a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                                       target="_blank">查看全文</a></span>
                <!--文字格式-->
                <div class="cont_text">
                    <h2><c:out value="${blogContent.rootContent.subject}"/></h2>
                    <p>${blogContent.rootContent.content}</p>
                </div>

                <!--图片显示-->
                <c:if test="${fn:length(blogContent.rootContent.images.images)>0}">
                    <c:choose>
                        <c:when test="${fn:length(blogContent.rootContent.images.images)==1}">
                            <div class="cont_text_cont" onclick="pOneImg('img_${blogContent.content.contentId}');"
                                 id="img_${blogContent.content.contentId}">
                                <c:forEach var="img" items="${blogContent.rootContent.images.images}">
                                    <img src="<c:out value="${uf:parseSSFace(img.ss)}"/>"/>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="cont_pic_s" onclick="pManyImg('img_${blogContent.content.contentId}')"
                                 id="img_${blogContent.content.contentId}">
                                <ul>
                                    <c:forEach var="img" items="${blogContent.rootContent.images.images}" varStatus="status">
                                        <li <c:if test="${status.index>4}">style="display:none"</c:if>><p><img
                                                src="${uf:parseSFace(img.m)}"/></p></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>

                <!--视频-->
                <c:if test="${fn:length(blogContent.rootContent.videos.videos)>0}">
                    <c:forEach var="video" items="${blogContent.rootContent.videos.videos}">
                        <div class="cont_video" id="vc_r_${blogContent.content.contentId}_${blogContent.rootContent.contentId}">
                            <div class="cont_video_left" id="vs_r_${blogContent.content.contentId}_${blogContent.rootContent.contentId}">
                                <a href="javascript:void(0)"
                                   onclick="javascript:pBigVideo('r_${blogContent.content.contentId}_${blogContent.rootContent.contentId}');">
                                    <img width="165" height="124" src="${video.url}"/>
                                    <input type="hidden" id="hid_vf_r_${blogContent.content.contentId}_${blogContent.rootContent.contentId}" name="videoflash" value="${video.flashUrl}"/>
                                </a>
                                <a title="播放" class="video_btn" href="javascript:void(0)"
                                   onclick="javascript:pBigVideo('r_${blogContent.content.contentId}_${blogContent.rootContent.contentId}');"></a>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                <!--视频 ending-->

                <!--音频 -->
                <c:if test="${fn:length(blogContent.rootContent.audios.audios)>0}">
                    <c:choose>
                        <c:when test="${(blogContent.rootContent.content=='' || fn:length(blogContent.rootContent.content)==0 )
                                             && fn:length(blogContent.rootContent.videos.videos)==0
                                             && fn:length(blogContent.rootContent.images.images)==0}">
                            <!--只有音乐-->
                            <c:forEach var="audio" items="${blogContent.rootContent.audios.audios}">
                                <div class="cont_music">
                                    <div class="cont_music_left">
                                        <a href="javascript:void(0)">
                                            <span><img width="150" src="${uf:parseAudioM(audio.url)}"></span>
                                        </a>
                                    </div>
                                    <div class="cont_music_right">
                                        <h2><c:out value="${audio.title}"/></h2>
                                        <object width="283" height="33" align="middle"
                                                classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                                                codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                                            <param name="allowScriptAccess" value="sameDomain">
                                            <param name="movie"
                                                   value="<c:out value="${audio.flashUrl}"/>">
                                            <param name="wmode" value="Transparent">
                                            <embed width="283" height="33" wmode="Transparent"
                                                   type="application/x-shockwave-flash"
                                                   src="<c:out value="${audio.flashUrl}"/>">
                                        </object>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="cont_music">
                                <c:forEach var="audio" items="${blogContent.rootContent.audios.audios}">
                                    <div class="cont_music_right">
                                        <object width="283" height="33" align="middle"
                                                classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
                                                codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0">
                                            <param name="allowScriptAccess" value="sameDomain">
                                            <param name="movie"
                                                   value="<c:out value="${audio.flashUrl}"/>">
                                            <param name="wmode" value="Transparent">
                                            <embed width="283" height="33" wmode="Transparent"
                                                   type="application/x-shockwave-flash"
                                                   src="<c:out value="${audio.flashUrl}"/>">
                                        </object>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <!--发布footer-->
                <div class="cont_foot">
                    <div class="left">${dateutil:parseDate(blogContent.rootContent.publishDate)}&nbsp;&nbsp;来自：着迷网</div>
                    <div class="right">
                        <c:if test="${blogContent.rootContent.uno eq userSession.blogwebsite.uno}">
                            <span>
                                <a href="javascript:void(0)" onclick="javascript:editRoot('${blogContent.rootProfile.blog.uno}','${blogContent.rootContent.contentId}');">编辑</a>
                            </span>&nbsp;&nbsp;|&nbsp;&nbsp;
                        </c:if>
                        <span><a href="javascript:void(0)"
                                 onclick="maskShare('${blogContent.rootContent.contentId}','${blogContent.rootContent.uno}');">转帖(${blogContent.rootContent.forwardTimes})</a></span>&nbsp;&nbsp;|&nbsp;&nbsp;
                        <c:choose>
                            <c:when test="${blogContent.rootFavorite}">已收藏(<span id="fav_num_root_${blogContent.rootContent.contentId}">${blogContent.rootContent.favorTimes}</span>)</c:when>
                            <c:otherwise>
                                <a id="fav_l_root_${blogContent.rootContent.contentId}" href="javascript:void(0)"
                                   onclick="javascript:favorited('${blogContent.rootContent.contentId}','${blogContent.rootContent.uno}','fav_num_root_');">收藏
                                    (<span id="fav_num_root_${blogContent.rootContent.contentId}">${blogContent.rootContent.favorTimes}</span>)</a>
                            </c:otherwise>
                        </c:choose> &nbsp;&nbsp;|&nbsp;&nbsp;
                        <span>
                            <a href="javascript:void(0)" onclick="replyRoot('${blogContent.rootProfile.blog.uno}','${blogContent.rootContent.contentId}','${blogContent.rootProfile.blog.domain}')">评论(${blogContent.rootContent.replyTimes})</a>
                        </span>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="cont_text">
                <p><fmt:message key="blog.content.not.exists" bundle="${userProps}"/></p></div>
            </c:otherwise>
        </c:choose>
    </div>
</c:otherwise>
</c:choose>
<!--标签样式-->
    <%--<c:if test="${blogContent.content.contentTag.tags!=null && fn:length(blogContent.content.contentTag.tags)>0}">--%>
    <%--<div class="cont_tags">--%>
        <%--<p>标签--%>
            <%--<c:forEach var="tag" items="${blogContent.content.contentTag.tags}">--%>
                <%--<a href="${ctx}/search/s/${tag}/">#<c:out--%>
                        <%--value="${tag}"/></a>--%>
            <%--</c:forEach>--%>
        <%--</p>--%>
    <%--</div>--%>
    <%--</c:if>--%>
    <!--发布footer-->
    <div class="cont_foot">
        <div class="left">${dateutil:parseDate(blogContent.content.publishDate)}&nbsp;&nbsp;来自：着迷网</div>
        <div class="right">
            <c:if test="${blogContent.content.uno eq userSession.blogwebsite.uno}">
                <span><a href="${ctx}/content/edit/${blogContent.content.uno}/${blogContent.content.contentId}">编辑</a></span>&nbsp;&nbsp;|&nbsp;&nbsp;
                <span><a href="javascript:void(0)" onclick="javascript:ajaxdelblog('${blogContent.content.uno}','${blogContent.content.contentId}');">删除</a></span>&nbsp;&nbsp;|&nbsp;&nbsp;
            </c:if>
             <c:choose>
                    <c:when test="${blogContent.rootContent==null || blogContent.rootContent.removeStatus.code=='n'}">
                       <a href="javascript:void(0)" onclick="maskShare('${blogContent.content.contentId}','${blogContent.content.uno}');">转帖(<span id="forward_num_${blogContent.content.contentId}">${blogContent.content.forwardTimes}</span>)</a></span>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:void(0)" onclick="forwardError();">转帖(<span id="forward_num_${blogContent.content.contentId}">${blogContent.content.forwardTimes}</span>)</a></span>
                    </c:otherwise>
                </c:choose>
            &nbsp;&nbsp;|&nbsp;&nbsp;<span><a
                href="javascript:void(0)" class="comment_h" onclick="getReplys('${blogContent.content.contentId}','${blogContent.content.uno}','${blogContent.profile.blog.domain}')">评论(<span id="feedbacknum_${blogContent.content.contentId}">${blogContent.content.replyTimes}</span>)</a></span>&nbsp;&nbsp;|&nbsp;&nbsp;
                <c:choose>
                    <c:when test="${blogContent.favorite}">已收藏(<span id="fav_num_${blogContent.content.contentId}">${blogContent.content.favorTimes}</span>)</c:when>
                    <c:otherwise>
                    <a id="fav_l_${blogContent.content.contentId}" href="javascript:void(0)" onclick="javascript:favorited('${blogContent.content.contentId}','${blogContent.content.uno}','fav_num_');">收藏
                    (<span id="fav_num_${blogContent.content.contentId}">${blogContent.content.favorTimes}</span>)</a>
                    </c:otherwise>
                </c:choose>
                </div>
    </div>


    <!-- 评论层 -->
    <div class="cont_cmt" id="cont_cmt_${blogContent.content.contentId}">
        <div class="cont_cmt_top">
            <p style="display: block;"><a id="${blogContent.content.contentId}" class="c_face" href="javascript:void(0)" onclick="showFace('${blogContent.content.contentId}');"></a><textarea
                    class="text" onpropertychange="!('\v'=='v'|| navigator.userAgent.indexOf('Firefox')>0)?this.style.height=this.scrollHeight-8+'px':this.style.height=this.scrollHeight+'px';" oninput="!('\v'=='v'|| navigator.userAgent.indexOf('Firefox')>0)?this.style.height=this.scrollHeight-8+'px':this.style.height=this.scrollHeight+'px';" rows="" cols=""
                    name="replayContent" style="font-family:Tahoma, '宋体';" id="textarea_${blogContent.content.contentId}"></textarea><input type="button" value="评 论" class="c_but" name="" onclick="ajaxsavepl('${blogContent.content.contentId}','${blogContent.content.uno}')">
            </p>

            <p class="cmt_p">
                <label>
                    <input type="checkbox" name="forwardRoot" value="forwardRoot"/>
                    同时转发到我的博客</label>
            </p>
            <c:if test="${blogContent.rootContent!=null}">
            <p class="cmt_p">
                <label>
                    <input type="checkbox" name="replayRoot" value="replayRoot"/>
                    同时评论给原文作者 <a href="${URL_WWW}/people/${blogContent.rootProfile.blog.domain}"><c:out
                        value="${blogContent.rootProfile.blog.screenName}"/></a></label>
            </p>
            </c:if>
            <div class="face_div" style="top:30px;" id="face_div_${blogContent.content.contentId}">
            </div>
        </div>
    </div>


</div>
</div>
</c:forEach>