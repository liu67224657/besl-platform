<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--短文格式-->
<div>
    <p>${blogContent.rootContent.content}</p>
</div>
<!--图片显示-->
<c:if test="${fn:length(blogContent.rootContent.images.images)>0}">
    <c:forEach var="img" items="${blogContent.rootContent.images.images}">
        <div class="blogarticle_img">
            <a href="<c:out value="${uf:parseBFace(img.m)}"/>" target="_blank">
            <img src="<c:out value="${uf:parseMFace(img.m)}"/>">
            </a>
        </div>
    </c:forEach>
</c:if>
<!--视频-->
<c:if test="${fn:length(blogContent.rootContent.videos.videos)>0}">
    <c:forEach var="video" items="${blogContent.rootContent.videos.videos}">
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
<c:if test="${fn:length(blogContent.rootContent.audios.audios)>0}">
    <c:forEach var="audio" items="${blogContent.rootContent.audios.audios}">
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

    <c:if test="${blogContent.rootContent.voteSubjectId !=null && voteDto !=null}">
        <div id="vote_${blogContent.rootContent.contentId}_submit">
        <c:choose>
            <c:when test="${voteDto.vote.voteSubject.choiceNum>1}">
                <h3><c:out
                        value="${voteDto.vote.voteSubject.subject}"></c:out>(最多可选${voteDto.vote.voteSubject.choiceNum}项)
                </h3>
            </c:when>
            <c:otherwise>
                <h3><c:out value="${voteDto.vote.voteSubject.subject}"></c:out>(单选)</h3>
            </c:otherwise>
        </c:choose>
        <c:if test="${voteDto.vote.voteSubject.direction!=null}">
            <p>${voteDto.vote.voteSubject.direction}</p>
        </c:if>
        <c:choose>
            <c:when test="${fn:length(voteDto.vote.voteSubject.imageSet.images)>0}">
                <c:forEach var="img" items="${voteDto.vote.voteSubject.imageSet.images}">
                    <p class="img">
                        <img src="<c:out value="${uf:parseMFace(img.ss)}"/>" data-jw="${img.w}"
                        data-jh="${img.h}"/>
                    </p>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <%--<img src="${URL_LIB}/static/theme/default/img/poll_box_normal_ico.jpg">--%>
            </c:otherwise>
        </c:choose>
        <c:if test="${voteDto.vote.expired || voteDto.voteUserRecord!=null || voteDto.vote.voteSubject.voteVisible.code =='all'}">
            <div class="poll_hits_box clearfix"><span
                    class="poll_hits">${voteDto.vote.voteSubject.voteNum}人参与投票</span></div>
        </c:if>

        <div class="poll_content">
            <ul>
                <c:forEach var="voteOption" items="${voteDto.vote.voteOptionMap}" varStatus="status">
                    <li
                    <c:if test="${voteOption.value.checked}">class="poll_selected"</c:if>
                    name="licurrent">
                    <c:choose>
                        <c:when test="${voteDto.vote.expired || voteDto.voteUserRecord!=null}">
                            <c:choose>
                                <c:when test="${voteDto.vote.voteSubject.choiceNum>1}">
                                    <label><c:if test="${!voteOption.value.checked}"><input type="checkbox"
                                                                                            name="optionId"
                                                                                            disabled="disabled"></c:if>&nbsp;<c:out
                                            value="${voteOption.value.description}"></c:out></label>
                                </c:when>
                                <c:otherwise>
                                    <label><c:if test="${!voteOption.value.checked}"><input type="radio"
                                                                                            name="optionId"
                                                                                            disabled="disabled"></c:if>&nbsp;<c:out
                                            value="${voteOption.value.description}"></c:out></label>
                                </c:otherwise>
                            </c:choose>
                            <!-- 进度条 -->
                            <c:choose>
                                <c:when test="${voteDto.vote.voteNum>0}">
                                    <div class="processBar">
                                                    <span><em
                                                            style="width:${voteOption.value.optionNum/voteDto.vote.voteNum*100}%"
                                                            class="processBg_${status.index%5+1}"></em></span>${voteOption.value.optionNum}票(<fmt:formatNumber
                                            type="percent"
                                            pattern="0.00">${voteOption.value.optionNum/voteDto.vote.voteNum*100}</fmt:formatNumber>%)
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="processBar">
                                        <span><em style="width:0%" class="processBg_1"></em></span>0人(0%)
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${voteDto.vote.voteSubject.choiceNum>1}">
                                    <label><input type="checkbox" name="optionId"
                                                  value="${voteOption.value.optionId}">&nbsp;<c:out
                                            value="${voteOption.value.description}"></c:out></label>
                                </c:when>
                                <c:otherwise>
                                    <label><input type="radio" name="optionId"
                                                  value="${voteOption.value.optionId}">&nbsp;<c:out
                                            value="${voteOption.value.description}"></c:out></label>
                                </c:otherwise>
                            </c:choose>

                            <!-- 进度条 -->
                            <c:if test="${voteDto.vote.voteSubject.voteVisible.code =='all'}">
                                <c:choose>
                                    <c:when test="${voteDto.vote.voteNum>0}">
                                        <div class="processBar">
                                                        <span><em
                                                                style="width:${voteOption.value.optionNum/voteDto.vote.voteNum*100}%"
                                                                class="processBg_${status.index%5+1}"></em></span>${voteOption.value.optionNum}票(<fmt:formatNumber
                                                type="percent"
                                                pattern="0.00">${voteOption.value.optionNum/voteDto.vote.voteNum*100}</fmt:formatNumber>%)
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="processBar">
                                            <span><em style="width:0%" class="processBg_1"></em></span>0人(0%)
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                    </li>
                </c:forEach>
                <!-- 投票后可查看结果 -->
                <c:if test="${!voteDto.vote.expired && voteDto.vote.voteSubject.voteVisible.code=='voted' && voteDto.voteUserRecord==null}">
                    <li class="poll_notice">投票后可查看结果</li>
                </c:if>
                <!-- 投票后可查看结果 -->
                <li>
                    <p class="next">
                        <c:choose>
                            <c:when test="${voteDto.vote.expired}">
                                <a class="disable_poll"><span>已结束</span></a>
                            </c:when>
                            <c:when test="${!voteDto.vote.expired && voteDto.voteUserRecord!=null}">
                                <a class="disable_poll"><span>已投票</span></a>
                            </c:when>
                            <c:otherwise>
                                <a data-duno="${blogContent.rootProfile.blog.uno}"
                                   data-did="${blogContent.rootContent.contentId}"
                                   data-sid="${voteDto.vote.voteSubject.subjectId}"
                                   data-domid="vote_${blogContent.rootContent.contentId}"
                                   data-choicenum="${voteDto.vote.voteSubject.choiceNum}" name="submit_poll"
                                   class="submit_poll"><span>投 票</span></a>
                                <c:if test="${openSync}">
                                                  <span class="y_zhuanfa">
                                                  <input type="checkbox" class="publish_s" name="sync"
                                                  <c:choose>
                                                      <c:when test="${fn:length(syncProviderSet)>0}">checked="true"</c:when>
                                                      <c:otherwise>disabled="true"</c:otherwise>
                                                  </c:choose>>
                                                  <span>同步</span>
                                                  <em class="install"></em>
                                                  </span>
                                </c:if>
                            </c:otherwise>
                        </c:choose>

                    </p>
                </li>
            </ul>
            <c:if test="${voteDto.userRecordDTOList!=null && fn:length(voteDto.userRecordDTOList)>0}">
                <div class="poll_info">
                    <c:forEach var="userRecordDTO" items="${voteDto.userRecordDTOList}">
                        <p><a href="javascript:void(0)" name="atLink"
                              title="<c:out value="${userRecordDTO.blog.screenName}"/>"><c:out
                                value="${userRecordDTO.blog.screenName}"/></a>
                            <span>投票给</span>
                            <c:forEach var="voteRecord"
                                       items="${userRecordDTO.voteUserRecord.recordSet.records}"
                                       varStatus="status">
                                <c:out value="${voteRecord.optionValue}"/><c:if test="${!status.last}">、</c:if>
                            </c:forEach>
                            <span>${userRecordDTO.voteUserRecord.dateStr}</span>
                        </p>
                    </c:forEach>
                </div>
            </c:if>
        </div>
        </div>
    </c:if>
<div class="area_ft">
    <span class="time">${dateutil:parseDate(blogContent.rootContent.publishDate)}</span>
    <c:if test="${blogContent.rootBoard!=null}">
            <span class="from">
            来自：<a href="${URL_WWW}/group/${blogContent.rootBoard.gameCode}/talk" target="_blank">${blogContent.rootBoard.gameName}小组</a>
            </span>
        </c:if>
    <div class="operate">
        <ul>
            <li>
                <a href="${URL_WWW}/note/${blogContent.rootContent.contentId}"
                   class="share" title="评论">评论(${blogContent.rootContent.replyTimes})</a></li>
            <li>
        </ul>
    </div>
</div>


