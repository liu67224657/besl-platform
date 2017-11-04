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
<div class="content clearfix">
    <div class="baike3 clearfix"  id="sortable">

        <h2 class="newBaikeTitle">正在编辑 ${game.resourceName}攻略百科</h2>

        <div class="bk_btn" id="baike_nav">
            <a href="javascript:void(0);" class="add_bk" id="add_baike_one">添加一级标题</a>
            <a href="javascript:void(0);" class="save_bk" name="save_baike"
               <c:if test="${fn:length(baikeDTOList)==0}">style="display:none"</c:if>>保存修改</a>
        </div>

        <%--1级--%>
        <c:forEach var="baike" items="${baikeDTOList}">
            <div class="bk_con  mt17" id="baike_one_title_${baike.category.categoryId}">
                <div class="bk_hd" dislevel="1">
                    <h3>
                        <b>${baike.category.categoryName}</b>
                                     <span class="operate_bk">
                                    <a class="bk_delete" href="javascript:void(0);" name="del_one_item" title="删除"></a>
                                    <a class="bk_editxt" href="javascript:void(0);" name="edit_item" title="编辑"></a>
                                </span>
                    </h3>

                              <span class="dengji">
                                      <a href="javascript:void(0);"
                                         <c:if test="${fn:length(baike.itemsByCategory)!=0}">style="display:none"</c:if>
                                         class="two_bk" name="add_two_item"><i></i>二级</a>

                                       <a href="javascript:void(0);"
                                          <c:if test="${fn:length(baike.children)!=0}">style="display:none"</c:if>
                                          class="three_bk" name="add_three_item"><i></i>三级</a>

                             </span>
                </div>
                    <%--1级 下的3级--%>
                <c:if test="${fn:length(baike.itemsByCategory)>0}">
                    <div class="bk_bd" name="baike_box_13">
                        <c:forEach var="items" items="${baike.itemsByCategory}">
                                        <span class="bk_list  <c:if test="${items.displayInfo!=null && fn:length(items.displayInfo.extraField1)>0 && fn:toLowerCase(items.displayInfo.extraField1)=='#cc0000'}">on</c:if>"
                                              id="baike_three_title_${items.itemId}" dislevel="3">
                                            <b><a href="${items.displayInfo.linkUrl}">${items.displayInfo.subject}</a></b>
                                            <span class="operate_bk">
                                                        <a class="bk_delete" href="javascript:void(0);" title="删除"
                                                           name="del_three_item"></a>
                                                        <a class="bk_editxt" href="javascript:void(0);" name="edit_item"
                                                           title="编辑"></a>
                                                    </span>
                                        </span>
                        </c:forEach>
                    </div>
                </c:if>
                    <%--1级 下的2级--%>
                <c:if test="${fn:length(baike.children)>0}">
                    <div class="bk_bd bk_pd01" name="baike_box_23">
                        <c:forEach var="children" items="${baike.children}">
                            <div name="sortableTwo">
                                <div class="bk_bd_hd"
                                     id="baike_two_title_${children.category.categoryId}"
                                     dislevel="2">
                                    <h3><b>${children.category.categoryName}</b>
                                            <span class="operate_bk">
                                                        <a class="bk_delete" href="javascript:void(0);" title="删除"
                                                           name="del_two_item"></a>
                                                        <a class="bk_editxt" href="javascript:void(0);" name="edit_item"
                                                           title="编辑"></a>
                                                    </span></h3>
                                                    <span class="dengji">
                                                        <a href="javascript:void(0);" class="three_bk"
                                                           name="add_twoToThree_item"><i></i>三级</a>
                                                    </span>
                                </div>
                                    <%--2级 下的3级--%>
                                <c:if test="${fn:length(children.itemsByCategory)>0}">
                                    <div class="bk_bd_bd" name="three_item_box">
                                        <c:forEach var="items" items="${children.itemsByCategory}">
                                                    <span class="bk_list <c:if test="${items.displayInfo!=null && fn:length(items.displayInfo.extraField1)>0 && fn:toLowerCase(items.displayInfo.extraField1)=='#cc0000'}">on</c:if>"
                                                          id="baike_three_title_${items.itemId}"
                                                          dislevel="3">
                                                        <b><a href="${items.displayInfo.linkUrl}">${items.displayInfo.subject}</a></b>
                                                        <span class="operate_bk">
                                                        <a class="bk_delete" name="del_twoToThree_item"
                                                           href="javascript:void(0)" title="删除"></a>
                                                        <a class="bk_editxt" href="javascript:void(0)" name="edit_item"
                                                           title="编辑"></a>
                                                    </span>
                                                    </span>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
                    <%--如果为空--%>
                <c:if test="${fn:length(baike.children)==0&&fn:length(baike.itemsByCategory)==0}">
                    <div class="bk_bd"></div>
                </c:if>

            </div>
        </c:forEach>
        <div id="baiketree_bottom"></div>
        <div class="bk_btn">
            <a href="javascript:void(0);" class="save_bk"
               <c:if test="${fn:length(baikeDTOList)==0}">style="display:none"</c:if>
               name="save_baike">保存修改</a>

            <form id="form_edit" action="${URL_WWW}/game/${game.gameCode}/baike/edit" method="post">
                <input name="baike" type="hidden" value="" id="baikeObj"/>
            </form>
        </div>
    </div>
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