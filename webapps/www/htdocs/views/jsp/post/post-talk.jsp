<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setAttribute("decorator", "none");
    response.setHeader("Cache-Control", "no-cache");//http1.1
    response.setHeader("Pragma", "no-cache");//http1.0
    response.setDateHeader("Expires", 0);
%>
<div id="div_post_text" style="position:relative;">
    <c:choose>
            <c:when test="${userSession!=null && (groupUser==null || groupUser.validStatus.code!=1)}">
                <div class="wrapper_unlogin" id="wrapper_unlogin" style="top: 44px; left: 0px; width: 644px; height: 290px; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; border: 1px solid rgb(201, 201, 201); background-color: rgb(255, 255, 255); line-height: 263px; background-position: initial initial; background-repeat: initial initial;">
                    <fmt:message key="post.groupuser.notvalid" bundle="${userProps}"/>
                </div>
            </c:when>
            <c:otherwise>
                <form action="${URL_WWW}/json/content/post/text" method="post" id="posttext" onsubmit="return false;">
                    <input type="hidden" name="categoryid" value="${viewCategory.categoryId}"/>
                    <c:if test="${group != null}">
                        <input type="hidden" name="resourceid" value="${group.resourceId}"/>
                    </c:if>
                    <c:if test="${isnotice != null}">
                        <input type="hidden" name="isnotice" value="${isnotice}"/>
                        <input type="hidden" name="cmname" value="${cmname}"/>
                    </c:if>
                    <c:if test="${game != null}">
                        <input type="hidden" name="gameid" value="${game.resourceId}"/>
                    </c:if>
                        <%--文本框开始--%>
                    <div class="editcon clearfix" id="editcon">
                            <%--标题框--%>
                        <div class="edit_title">
                            标题:<input name="blogsubject" type="text" class="edittext" id="blogSubject"
                                      style="color: rgb(204, 204, 204);" value="给你的文章加个标题吧"/>
                        </div>
                            <%--标题框结束--%>
                        <div id="d_editor" class="editor">
                                <%--长文文本框--%>
                            <div class="edit_hd">

                                <a href="javascript:void(0)" onclick="return false;" id="editor_b" class="long_b" title="加粗"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_split" class="long_fen" title="分割线"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_mood" class="long_face" title="表情"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_pic" class="long_pic" title="图片"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_link" class="long_piclink" title="图片链接"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_audio" class="long_music" title="音乐"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_video" class="long_video" title="视频"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_tag" class="long_tag" title="标签"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_friend" class="long_at" title="@朋友"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_app" class="long_app" title="APP"></a>
                                <a title="投票" class="editor_poll_btn"  href="${URL_WWW}/vote/post/page?groupcode=${group.gameCode}"></a>
                                <a href="javascript:void(0)" onclick="return false;" id="editor_game" class="long_game" title="游戏"></a>
                            </div>
                            <div class="edit_bd">
                                <textarea id="text_content" name="content"></textarea>
                            </div>
                                <%--长文文本框结束--%>
                        </div>
                            <%--长文更长模式--%>
                        <div class="unfold">
                            <a href="javascript:void(0)" class="unfold_bd" id="edit_text_height">
                                <span class="unfold_btn"></span>
                            </a>
                        </div>
                            <%--长文更长模式结束--%>
                    </div>
                        <%--文本框结束--%>
                        <%--发布按钮--%>
                    <div class="edit">
                        <c:if test="${fn:length(verifyText)>0}">
                            <span class="tips_red">${verifyText}</span>
                        </c:if>
                        <div class="fl" style="line-height:24px">
                            <span style="vertical-align:middle">验证码：</span>
                            <img src="/validate/imgcode" style="vertical-align:middle" id="img_validatecode" onclick="javascript:document.getElementById('img_validatecode').src='/validate/imgcode?'+Math.random();return false;"/>
                            <input id="valimg" name="valimg" type="text" class="" value="" style="width:50px; padding:4px 0; vertical-align:middle"/>
                            <a href="javascript:void(0)" onclick="javascript:document.getElementById('img_validatecode').src='/validate/imgcode?'+Math.random();return false;" >换一张？</a>
                            <br/>
                            <span style="vertical-align:middle; color:#999; margin-left:50px; ">这是一个常用成语，请输入图片中“？”的文字</span>
                        </div>
                        <div class="publish">
                            <input id="post_text_submit" name="" type="button" class="publish_btn" value=""/>
                        </div>

                        <c:if test="${openSync}">
                <span class="tongbu"><input type="checkbox" id="publish_s" class="publish_s" name="sync"
                <c:choose>
                                            <c:when test="${fn:length(syncProviderSet)>0}">checked="true"</c:when>
                                            <c:otherwise>disabled="true"</c:otherwise>
                </c:choose>>
                    <label for="publish_s"><span>同步</span></label>
                    <em class="install" id="text_syn_install" title="同步设置"></em>
                </span>
                        </c:if>
                    </div>
                        <%--发布按钮结束--%>
                </form>
            </c:otherwise>
    </c:choose>

</div>