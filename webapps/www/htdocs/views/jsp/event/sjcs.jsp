<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/views/jsp/common/meta.jsp" %>
    <title>${jmh_title}</title>
    <link href="${URL_LIB}/static/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/default/css/home.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/event/sjcs/css/active.css?${version}" rel="stylesheet" type="text/css" />
    <link href="${URL_LIB}/static/default/css/mask.css?${version}" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery-1.5.2.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery.form.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jquery.validate.min.js"></script>
    <script type="text/javascript" src="${URL_LIB}/static/js/jmdialog/jmdialog.js"></script>
     <link href="${URL_LIB}/static/default/css/mask.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${URL_LIB}/static/js/common.js?${version}"></script>
    <script>
        $(function(){
           $('#query_link').click(function(){
              $.post('${URL_WWW}/json/invite/query',{},function(req){
                  var jsonObj=eval('('+req+')');
                  if(jsonObj.status_code='1'){
                      if(jsonObj.result.length>0){
                          $('#result_tips').html('您已经成功邀请了 '+jsonObj.result.length+' 个好友，邀请更多的好友可以提高中奖几率喔！');
                      }else{
                          $('#result_tips').html('您还没有成功邀请过好友<br\/>赶快邀请好友参加活动吧');
                      }
                      $.JMDialog({
                                  ___title:"信息提示",
                                  ___showTitle:false,
                                  ___showbg:true,
                                  ___content:"id:query_result"
                              });
                  }
              })
           });
        });
    </script>
</head>
<body>
<div id="wraper">
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div id="content" style="min-height:0px"><table id="__01" width="961" height="700" border="0" cellpadding="0" cellspacing="0" class="" >
        <tr>
            <td colspan="3">
                <img src="${URL_LIB}/static/event/sjcs/img/sj_01.png" width="960" height="56" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="56" alt=""></td>
        </tr>
        <tr>
            <td colspan="3">
                <img src="${URL_LIB}/static/event/sjcs/img/sj_02.png" width="960" height="56" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="56" alt=""></td>
        </tr>
        <tr>
            <td colspan="3">
                <img src="${URL_LIB}/static/event/sjcs/img/sj_03.png" width="960" height="56" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="56" alt=""></td>
        </tr>
        <tr>
            <td colspan="3">
                <img src="${URL_LIB}/static/event/sjcs/img/sj_04.png" width="960" height="56" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="56" alt=""></td>
        </tr>
        <tr>
            <td colspan="3">
                <img src="${URL_LIB}/static/event/sjcs/img/sj_05.png" width="960" height="56" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="56" alt=""></td>
        </tr>
        <tr>
            <td rowspan="9">
                <img src="${URL_LIB}/static/event/sjcs/img/sj_06.png" width="46" height="420" alt=""></td>
            <td rowspan="8"  class="tdcss">
                    <div class="tdcon">
                     <dl>
                     <dt>1.</dt>
                     <dd>玩家们只需要通过本页邀请好友来到着迷网就有机会获得珍贵的<a href="http://www.joyme.com/search/content/%25E5%259C%25A3%25E5%25A2%2583%25E4%25BC%25A0%25E8%25AF%25B4/" target="_blank">圣境传说</a>激活码。</dd>
                     </dl>
                      <dl>
                     <dt>2.</dt>
                     <dd>2011年11月29日至12月2日，我们将在参与活动的玩家中每天抽取10名玩家，赠送一枚内测激活码。</dd>
                     </dl>
                      <dl>
                     <dt>3.</dt>
                     <dd>只需成功邀请一个好友来到着迷网，就可以每天参与抽奖活动。邀请的好友越多，中奖机会越高，不过不要为了邀请数量而作弊喔。</dd>
                     </dl>
                      <dl>
                     <dt>4.</dt>
                     <dd>邀请好友数量最先达到10人的前10名玩家，将可以直接获得一枚内测激活码。</dd>
                     </dl>
                       <dl>
                     <dt>5.</dt>
                     <dd>"<a href="http://fahao.joyme.com/" target="_blank">着迷发号中心</a>"会每天公布前一日中奖玩家的名字，激活码会通过着迷网私信发给玩家。</dd>
                     </dl>
                 </div>

               <div class="btn">
                   <a href="javascript:void(0)" class="btn01" id="query_link">查询抽奖资格</a>
                   <a href="${URL_WWW}/invite/invitepage" class="btn02">立即邀请好友</a>
               </div>
                </td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_08.png" width="400" height="53" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="53" alt=""></td>
        </tr>
        <tr>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_09.png" width="400" height="52" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="52" alt=""></td>
        </tr>
        <tr>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_10.png" width="400" height="53" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="53" alt=""></td>
        </tr>
        <tr>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_11.png" width="400" height="52" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="52" alt=""></td>
        </tr>
        <tr>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_12.png" width="400" height="53" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="53" alt=""></td>
        </tr>
        <tr>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_13.png" width="400" height="52" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="52" alt=""></td>
        </tr>
        <tr>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_14.png" width="400" height="53" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="53" alt=""></td>
        </tr>
        <tr>
            <td rowspan="2">
                <img src="${URL_LIB}/static/event/sjcs/img/sj_15.png" width="400" height="52" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="8" alt=""></td>
        </tr>
        <tr>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/sj_16.png" width="514" height="44" alt=""></td>
            <td>
                <img src="${URL_LIB}/static/event/sjcs/img/line.gif" width="1" height="44" alt=""></td>
        </tr>
    </table>
        <div id="query_result" style="display:none;">
        <div  class="fc" >
            <div class="fc_con">
                <p id="result_tips"></p>
                <a href="javascript:void(0)" onclick="hideMask();">确认</a>
            </div>
        </div>
        </div>
      </div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>

<script type="text/javascript" src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main(33);
</script>


<div style="width:0px; height:0px; overflow:hidden;">
    <script src="http://s19.cnzz.com/stat.php?id=3214014&web_id=3214014&show=pic" language="JavaScript"></script>
</div>

</body>
</html>