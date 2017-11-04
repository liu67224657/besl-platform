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
    <meta name="Keywords" content="游戏活动、新游内侧、激活码、新手码、注册码、邀请码、周边、手办">
    <meta name="description" content="来着迷网参加游戏活动、社区活动，领取热门新游戏激活码、游戏周边、手办模型玩具。"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>热门活动_${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<%--头部--%>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>

<div class="wrapper clearfix">
 <div class="con">
      ${recommend}
    <div class="con_hd"></div>

    <div class="con_area con_area_love clearfix">
        <ul class="lovecon_list">
          <c:forEach var="activity" items="${activityList}">
              <c:if test="${activity.displayInfo!=null}">
                  <li class="clearfix">
                  <div class="loveimg">
                      <a href="${activity.displayInfo.linkUrl}" target="_blank">
                          <img src="${uf:parseOrgImg(activity.displayInfo.iconUrl)}" width="224" height="111" alt="<c:out value="${activity.displayInfo.subject}"/>"/>
                      </a>
                  </div>
                  <div class="lovecon_bd">
                      <div class="a_love_tit">
                          <h3><a href="${activity.displayInfo.linkUrl}" target="_blank"><c:out value="${activity.displayInfo.subject}"/></a>
                              <c:if test="${activity.displayType!=null && activity.displayType.isNew()}">
                                 <i class="new"></i>
                              </c:if>
                              <c:if test="${activity.displayType!=null && activity.displayType.isHot()}">
                                 <i class="hot"></i>
                              </c:if>
                          </h3>
                           <c:if test="${activity.displayInfo!=null && fn:length(activity.displayInfo.extraField1)>0}">
                          <span class="more">发起人：${activity.displayInfo.extraField1}</span>
                           </c:if>
                       </div>
                      <div class="love_txt">
                          <p>
                              ${activity.displayInfo.desc}
                          </p>

                          <div class="love_chakan">
                              <a href="${activity.displayInfo.linkUrl}" target="_blank" class="details_love">查看详情</a>

                              <c:if test="${activity.displayInfo!=null && fn:length(activity.displayInfo.extraField2)>0}">
                                  <a href="${activity.displayInfo.extraField2}" target="_blank" class="love_result">查看获奖结果</a>
                              </c:if>
                          </div>
                      </div>
                  </div>
              </li>
              </c:if>
          </c:forEach>
        </ul>

        <%--分页--%>
        <input type="hidden" id="hidden_pageNo" value="${page.curPage}"/>
        <input type="hidden" id="hidden_totalRows" value="${page.totalRows}"/>
            <span id="pagearea">
                 <c:set var="pageurl" value="/event"/>
                 <%@ include file="/views/jsp/page/pagenoend.jsp" %>
            </span>
    </div>
    <div class="con_ft"></div>
  </div>
<!--右侧开始-->
<div class="sidebar">
    ${bulletin}
    <!--官方微博-->
    <div class="sidebar_love">
        <div class="side_love_pd">
            <h3>关注着迷网官方微博:</h3>

            <p>
                <span class="sina_follow">
                <iframe width="63" height="24" frameborder="0" allowtransparency="true" marginwidth="0" marginheight="0"
                        scrolling="no" border="0"
                        src="http://widget.weibo.com/relationship/followbutton.php?language=zh_cn&width=63&height=24&uid=2253167477&style=1&btn=red&dpc=1"></iframe>
                    </span>
                <span>
                    <iframe width="73" height="24"
                            src="http://follow.v.t.qq.com/index.php?c=follow&a=quick&name=joymeweb&style=5&t=1344495192599&f=0"
                            frameborder="0" scrolling="auto" marginwidth="0" marginheight="0"
                            allowtransparency="true"></iframe>
                </span>
            </p>
        </div>
    </div>

   <div class="sidebar_cooperation">
     <div class="side_love_pd">
          <h3>商务合作请发送至：</h3>
          <p class="cooper_txt">  contactus@joyme.com</p>
     </div>
   </div>
    <%--热部署广告--%>
    <%@ include file="/hotdeploy/views/jsp/activity/activity-right-ad.jsp" %>
  </div>
</div>

<%@ include file="/views/jsp/tiles/footer.jsp" %>

<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/activity-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
