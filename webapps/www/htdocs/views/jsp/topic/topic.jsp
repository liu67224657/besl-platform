<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/views/jsp/common/taglibs.jsp"%>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/views/jsp/common/meta.jsp"%>
<title>话题 ${jmh_title}</title>
<link href="${URL_LIB}/static/default/css/core.css" rel="stylesheet" type="text/css"/>
<link href="${URL_LIB}/static/default/css/home.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${URL_LIB}/static/js/jquery-1.5.2.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common.js"></script>
<script type="text/javascript" src="${URL_LIB}/static/js/common/google-statistics.js"></script>

</head>
<body>
<div id="wraper">
    <c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
    <div id="content">
        <div id="cont_left">
            <div class="u_fans">
                <div class="u_fans_t hr_t">
                    <h3 class="hr_fans">游戏那些事</h3>
                </div>

            <div class="poly">
                     <div class="poly_l">
                 <img src="${URL_LIB}/static/default/img/poly01.jpg" /></div>
              <div class="poly_r">
                <h3><a href="${ctx}/search/s/Cosplay/?srid=b" target="_blank">Cosplay美女帅哥</a></h3>
                 <p>爱一个角色的最高表现就是变成TA！分享那些漂亮或是搞怪的Cosplay吧！</p>
                   <div class="poly_tag">
                   <div class="poly_tag_l">标签：<a href="${ctx}/search/s/Cosplay/?srid=b" target="_blank">#Cosplay</a></div>
                   <div class="poly_tag_r"><a href="${ctx}/search/s/Cosplay/?srid=b" class="talk" target="_blank"></a><a href="${ctx}/search/s/Cosplay/?srid=b" target="_blank">去看看&gt;&gt;</a></div>
                   </div>

              </div>
              </div>

            <div class="poly">
                     <div class="poly_l">
                 <img src="${URL_LIB}/static/default/img/poly02.jpg" /></div>
              <div class="poly_r">
                <h3><a href="${ctx}/search/s/无尽的刷刷刷/?srid=b" target="_blank">无尽的刷刷刷</a></h3>
                 <p>近年来从网游到单机各种游戏都开始让玩家不停的喜刷刷，今天你刷了吗？</p>
                   <div class="poly_tag">
                   <div class="poly_tag_l">标签：<a href="${ctx}/search/s/无尽的刷刷刷/?srid=b" target="_blank">#无尽的刷刷刷</a></div>
                   <div class="poly_tag_r"><a href="${ctx}/search/s/无尽的刷刷刷/?srid=b" class="talk" target="_blank"></a><a href="${ctx}/search/s/无尽的刷刷刷/?srid=b" target="_blank">去看看&gt;&gt;</a></div>
                   </div>

              </div>
              </div>
              <div class="poly">
                     <div class="poly_l">
                 <img src="${URL_LIB}/static/default/img/poly03.jpg" /></div>
              <div class="poly_r">
                <h3><a href="${ctx}/search/s/%25E9%2582%25A3%25E4%25BA%259B%25E6%2580%25AA%25E7%2589%25A9%25E4%25BB%25AC/?srid=b" target="_blank">那些惹人爱恨的怪物们</a></h3>
                 <p>每个游戏中都有几只让你喜欢或是讨厌的不得了的怪物，看看这里有没有你见过的。</p>
                   <div class="poly_tag">
                   <div class="poly_tag_l">标签：<a href="${ctx}/search/s/%25E9%2582%25A3%25E4%25BA%259B%25E6%2580%25AA%25E7%2589%25A9%25E4%25BB%25AC/?srid=b" target="_blank">#那些怪物们</a></div>
                   <div class="poly_tag_r"><a href="${ctx}/search/s/%25E9%2582%25A3%25E4%25BA%259B%25E6%2580%25AA%25E7%2589%25A9%25E4%25BB%25AC/?srid=b" class="talk" target="_blank"></a><a href="${ctx}/search/s/%25E9%2582%25A3%25E4%25BA%259B%25E6%2580%25AA%25E7%2589%25A9%25E4%25BB%25AC/?srid=b" target="_blank">去看看&gt;&gt;</a></div>
                   </div>

              </div>
              </div>
              <div class="poly">
                     <div class="poly_l">
                 <img src="${URL_LIB}/static/default/img/poly04.jpg" /></div>
              <div class="poly_r">
                <h3><a href="${ctx}/search/s/%25E5%25AE%258C%25E7%25BE%258E%25E5%25BC%25BA%25E8%25BF%25AB%25E7%2597%2587/?srid=b" target="_blank">完美强迫症</a></h3>
                 <p>总想收齐金币有木有？总想全打三颗星有木有？你的完美强迫症都让你干过些什么？</p>
                   <div class="poly_tag">
                   <div class="poly_tag_l">标签：<a href="${ctx}/search/s/%25E5%25AE%258C%25E7%25BE%258E%25E5%25BC%25BA%25E8%25BF%25AB%25E7%2597%2587/?srid=b" target="_blank">#完美强迫症</a></div>
                   <div class="poly_tag_r"><a href="${ctx}/search/s/%25E5%25AE%258C%25E7%25BE%258E%25E5%25BC%25BA%25E8%25BF%25AB%25E7%2597%2587/?srid=b" class="talk" target="_blank"></a><a href="${ctx}/search/s/%25E5%25AE%258C%25E7%25BE%258E%25E5%25BC%25BA%25E8%25BF%25AB%25E7%2597%2587/?srid=b" target="_blank">去看看&gt;&gt;</a></div>
                   </div>

              </div>
              </div>
              <div class="poly noborder">
                     <div class="poly_l">
                 <img src="${URL_LIB}/static/default/img/poly05.jpg" /></div>
              <div class="poly_r">
                <h3><a href="${ctx}/search/s/%25E6%25B8%25B8%25E6%2588%258F%25E8%25B6%25A3%25E5%259B%25BE/?srid=b" target="_blank">游戏趣图大收集</a></h3>
                 <p>游戏就像生活，不经意之间就会遇到让你开怀大笑的场景，将它们分享给更多的朋友一起快乐吧。</p>
                   <div class="poly_tag">
                   <div class="poly_tag_l">标签：<a href="${ctx}/search/s/%25E6%25B8%25B8%25E6%2588%258F%25E8%25B6%25A3%25E5%259B%25BE/?srid=b" target="_blank">#游戏趣图大收集</a></div>
                   <div class="poly_tag_r"><a href="${ctx}/search/s/%25E6%25B8%25B8%25E6%2588%258F%25E8%25B6%25A3%25E5%259B%25BE/?srid=b" class="talk" target="_blank"></a><a href="${ctx}/search/s/%25E6%25B8%25B8%25E6%2588%258F%25E8%25B6%25A3%25E5%259B%25BE/?srid=b" target="_blank">去看看&gt;&gt;</a></div>
                   </div>

              </div>
              </div>
            </div>
        </div>

        <div id="cont_right">
           <div class="poly_right">
                <div class="polypic">
            <img src="${URL_LIB}/static/default/img/jingling.jpg"  />
            <p>迷之精灵</p></div>

          <div class="polycon">
              <p>这里是着迷网所有话题的聚集地，没想到你居然能发现这里，值得夸奖一下呦。<br/>
话题们虽然长相千奇百怪，性格也不尽相同，但是在本姑娘的<strong>残暴</strong>精心管理下，也乖乖的按时间顺序排成了一队。</p>
<p>
老大说：<br/>
想参与话题讨论的人，记得给新发表的博文<b>添加话题标签</b>。千万不要忘记喔~
</p>
<p>
还看不懂的话就<a href="http://www.joyme.com/note/2qWI6MCb99DUVTAN50ysCU" target="_blank">点这里</a></p>
            </div> </div>

        </div>
    </div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
</div>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>
</body>
</html>
