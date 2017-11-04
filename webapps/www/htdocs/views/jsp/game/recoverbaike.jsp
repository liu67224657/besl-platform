<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="KeyWords" content="${game.seoKeyWords}">
    <meta name="Description" content="${game.seoDescription}"/>
    <link rel="icon" href="${URL_LIB}/static/img/favicon.ico" type="image/x-icon"/>
    <title>${game.resourceName} ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<div class="wrapper clearfix">
    <div class="con">
        <div class="con_hd"></div>
        <div class="con_area  con_pd01 clearfix">
            <!--game-navigation-->
            <%@ include file="/views/jsp/game/game-navigation.jsp" %>
            <!--stab_hd-->
            <div class="stab_bd">
                <div class="baike clearfix" id="sortable">
                    <div class="tit_baike">
                        <h3>${game.resourceName}攻略百科</h3>
                    </div>
                    <div class="bk_btn" id="baike_nav">
                        <a href="javascript:void(0);" class="add_bk" id="add_baike_one">添加一级标题</a>
                        <a href="javascript:void(0);" class="save_bk" name="save_baike">保存修改</a>
                    </div>

                  ${recovertStr}

                    <div class="bk_btn">
                        <a href="javascript:void(0);" class="save_bk" name="save_baike">保存修改</a>

                        <form id="form_edit" action="${URL_WWW}/baike/${game.gameCode}/edit" method="post">
                            <input name="baike" type="hidden" value="" id="baikeObj"/>
                        </form>
                    </div>
                </div>
                <!--baike-->
            </div>
        </div>
        <!--con_area-->
        <div class="con_ft"></div>
    </div>
    <%@ include file="/views/jsp/game/game-right.jsp" %>

</div>
<%@ include file="/views/jsp/tiles/footer.jsp" %>
<script src="${URL_LIB}/static/js/common/seajs.js"></script>
<script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
<script>
    seajs.use('${URL_LIB}/static/js/init/baike-init.js')
</script>
<script src="${URL_LIB}/static/js/common/pv.js"></script>
<script type="text/javascript">
    lz_main();
</script>

</body>
</html>