<!doctype html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/jstllibs.jsp" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <title>设置正在玩</title>
    <link href="${URL_LIB}/static/theme/wap/css/wap_common.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/wap/css/wap_style.css" rel="stylesheet" type="text/css">
    <link href="${URL_LIB}/static/theme/default/css/wap_xykc_loading.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${URL_LIB}/static/js/common/jquery-1.11.0.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/gameclient-common.js"></script>
    <script type="text/javascript">
        window.addEventListener('DOMContentLoaded', function () {
                  document.addEventListener('touchstart', function () {
                      return false
                  }, true)
              }, true);
    </script>
<body>
<div id="wrapper" class="wrapper">
    <div class="interest-tit" style="margin-top: 64">
        <h1>选择您感兴趣的游戏（<span class="interest-tit-num" id="interest-tit-num">0</span>/6）</h1>

        <h2>关注游戏，攻略、 福利活动抢先知</h2>
    </div>
    <div class="look-main clearfix">
        <c:forEach items="${gamelist}" var="game">
            <div class="look-main-block add-attention-block fl">
                <a href="javascript:void(0);" name="link_like" data-gameid="${game.game.gameid}">
                    <cite>
                        <img src="${game.game.iconurl}" alt="">
                        <c:choose>
                            <c:when test="${game.relationstatus==1}">
                                <span class="att2" id="span_like_${game.game.gameid}"><em>已关注</em></span>
                            </c:when>
                            <c:otherwise>
                                <span class="att1" id="span_like_${game.game.gameid}"><em>关注</em></span>
                            </c:otherwise>
                        </c:choose>
                    </cite>

                    <h2>${game.game.name}</h2>
                </a>
            </div>
        </c:forEach>
    </div>
    <div class="tip" id="tip"></div>

    <div class="spinnerbox" id="loading" style="display: none">
        <div class="spinner">
            <p>努力加载中</p>

            <div class="bounce1"></div>
            <div class="bounce2"></div>
            <div class="bounce3"></div>
        </div>
    </div>
</div>
<div class="finishBox">
     <div class="finishBtn">
         <a href="javascript:void(0);" id="link_submit">完成</a>
     </div>
 </div>

<%@ include file="/views/jsp/common/jsconfig.jsp" %>
  <script type="text/javascript">

      var uid = '${uid}';
      var idarray = new Array();

      <c:forEach items="${gameIdList}" var="game">
      idarray.push('${game.objectId}');
      </c:forEach>

      $(document).ready(function () {
          $('#interest-tit-num').html(idarray.length);
          $("a[name=link_like]").on("click", function () {
              $('#tip').html('').removeClass('show');
              var gameid = $(this).attr('data-gameid');
              var idx = idarray.indexOf(gameid);
              if (idx >= 0) {
                  if (idarray.length == 1) {
                      showTips('亲，请至少关注1个游戏', 2000);
                      return;
                  }
                  idarray.splice(idx, 1);
                  $('#span_like_' + gameid).attr('class', 'att1').html("<em>关注</em>");
              } else {
                  if (idarray.length >= 6) {
                      showTips('亲，请先关注6个游戏，稍后随时可以修改', 2000);
                      return;
                  }
                  idarray.push(gameid);
                  $('#span_like_' + gameid).attr('class', 'att2').html("<em>已关注</em>");
              }
              $('#interest-tit-num').html(idarray.length);
          });

          window.postlock = false;
          $('#link_submit').on("touchstart", function () {
              if (postlock) {
                  return;
              }
              if (idarray.length == 0) {
                  showTips('亲，请至少关注1个游戏', 2000);
                  return;
              }
              postlock = true;
              $('#loading').css('display', '');
              var t = setTimeout(function () {
                  $('#loading').css('display', 'none');
                  postlock = false;
                 // clearTimeout(t);
              }, 3000);
              $.ajax({
                  url: 'http://api.' + joyconfig.DOMAIN + "/joymeapp/gameclient/json/game/batchlike",
                  data: {uid: uid, gids: idarray.join(","), appkey: '${appkey}', platform: '${platform}'},
                  type: "POST",
                  success: function (request) {
                      $('#loading').css('display', 'none');
                      postlock = false;
                      _jclient.jump('jt=21');
                  },
                  complete: function (request) {
                      $('#loading').css('display', 'none');
                      postlock = false;
                  }
              });
          });
      })
  </script>

</body>
</html>