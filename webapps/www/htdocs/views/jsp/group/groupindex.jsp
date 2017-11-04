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
    <meta name="Keywords" content="着迷、着迷网、游戏社区、游戏攻略、游戏交友">
    <meta name="description" content="来着迷小组寻找推荐游戏、实用攻略、向游戏达人提问、结交志同道合的朋友。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>小组_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>

<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<div class="sqcontent  square_hd01 clearfix">
   <div class="groupcapition">
       <h3 class="group_hd">小组<span class="little_title">发现共同兴趣、结交游戏同好、解答游戏问题</span></h3>
    </div>
  <div class="groupcon">
      <div class="sqc clearfix">
          <div class="group_left">
              <h4> 推荐小组</h4>
              <ul class="group_list clearfix">
                  <li class="clearfix">
                      <c:forEach var="gameElement" items="${gameList}" varStatus="st">
                      <div class="group <c:if test="${st.index%2==0}">mr70</c:if>">
                          <a href="${URL_WWW}/group/${gameElement.code}" target="_blank"><img
                                  src="${uf:parseOrgImg(gameElement.thumbimg)}"></a>

                          <div class="group_con">
                              <h3><a href="${URL_WWW}/group/${gameElement.code}"
                                     target="_blank" title="${gameElement.title}">
                                  <c:choose>
                                      <c:when test="${fn:length(gameElement.title)>15}">
                                          ${fn:substring(gameElement.title,0,15)}…
                                      </c:when>
                                      <c:otherwise>
                                          ${gameElement.title}
                                      </c:otherwise>
                                  </c:choose></a></h3>
                              <p>
                              <c:choose>
                                  <c:when test="${fn:length(gameElement.desc)>30}">
                                      ${fn:substring(gameElement.desc,0,29)}…
                                  </c:when>
                                  <c:otherwise>
                                      ${gameElement.desc}
                                  </c:otherwise>
                              </c:choose>
                              </p>

                              <p class="g_p">
                                  <c:choose>
                                      <c:when test="${gameElement.relationFlag==null || gameElement.relationFlag=='' ||gameElement.relationFlag=='0' || gameElement.relationFlag=='-1'}">
                                          <a class="joinbtn" name="followGroup"  data-gid="${gameElement.elementId}" href="javascript:void(0);"><span>+加入</span></a>
                                      </c:when>
                                      <c:otherwise>
                                          <a class="joinedbtn" href="javascript:void(0);"><span>已加入</span></a>
                                      </c:otherwise>
                                  </c:choose>
                                  ${gameElement.replyTimes+gameElement.postTimes}帖子
                              </p>
                          </div>
                      </div>
                      <c:if test="${st.index%2==1}"></li>
                  <li class="clearfix"></c:if>
                      </c:forEach>
                  </li>
              </ul>
          </div>
          <!--group_left-->

          <div class="group_right">
              <div class="addNewItemBox">
				<h2>我来创建小组...</h2>
				<p>
					<a href="http://www.joyme.com/note/1bLNJPBiV1-WbZ6zyInnbT" target="_blank" class="addNewItemBtn addNewItemBtn-newGroup">创建小组</a>
				</p>
			  </div>
              <h4>热门帖子</h4>
              <ul class="organize">
                <c:forEach var="itemContent" items="${contentList}" varStatus="st">
                <li class="clearfix">
                   <h5><a href="${URL_WWW}/note/${itemContent.blogContent.content.contentId}" target="_blank">
                       <c:choose>
                            <c:when test="${itemContent.blogContent.content.contentType.hasPhrase()}">
                               ${itemContent.blogContent.content.content}
                            </c:when>
                            <c:otherwise>
                               <c:out value="${itemContent.blogContent.content.subject}"/>
                            </c:otherwise>
                       </c:choose></a>
                   </h5>
                   <p>
                       <c:choose>
                           <c:when test="${itemContent.blogContent.board!=null}">
                                  <span class="o_from">来自：<a href="${URL_WWW}/group/${itemContent.blogContent.board.gameCode}/talk"  target="_blank" title="${itemContent.blogContent.board.gameName}"><c:choose><c:when test="${fn:length(itemContent.blogContent.board.gameName)>10}">${fn:substring(itemContent.blogContent.board.gameName,0,10)}</c:when><c:otherwise>${itemContent.blogContent.board.gameName}</c:otherwise></c:choose></a></span>
                           </c:when>
                           <c:otherwise>
                               <span class="o_from">来自：着迷网</span>
                           </c:otherwise>
                       </c:choose>
                       <span class="o_reply">${itemContent.blogContent.content.replyTimes}回贴</span>
                   </p>
                </li>
                </c:forEach>
              </ul>
          </div>
          <!--group_right-->
      </div>
      <div class="sqb"></div>
   </div>
</div>


<!--页尾开始-->
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/groupindex-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
