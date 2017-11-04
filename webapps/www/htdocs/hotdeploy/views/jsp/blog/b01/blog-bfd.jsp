<style>
   .userbd a.bfd_img_logo{padding:0px;float:right;display:block;width:101px;height:13px;margin:5px 3px 0 0;background:url(http://static.baifendian.com/themes/logo/bfd_logo_ch.png) no-repeat;_background:url(http://static.baifendian.com/themes/logo/bfd_logo_ch.gif) no-repeat;overflow:hidden;}
    .userbd a.bfd_img_logo:hover{background:url(http://static.baifendian.com/themes/logo/bfd_logo_ch.png) no-repeat 0 -14px;_background:url(http://static.baifendian.com/themes/logo/bfd_logo_ch.gif) no-repeat 0 -14px;}
</style>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <!--猜你喜欢开始-->
    <div class="usercon mt15" id="viewAlsoViewId" style="display: none">
        <div class="userhd clearfix">
            <h3> / 你可能还喜欢 </h3>
        </div>
        <div class="userbd clearfix">
            <ul class="userlist" id="bodyViewAlsoViewId">
            </ul>
            <a href="http://www.baifendian.com" target="_blank" class="bfd_img_logo" title="百分点推荐引擎"></a>
        </div>
        <div ></div>

    </div>

<script type="text/javascript">
    var ct ="Ctest_30";
    if(joyconfig.DOMAIN == "joyme.com"){
        ct ="Czhaomi";
    };
    var BFD_ITEM_INFO = {
        artic_userId:"${blogContent.content.uno}",
        artic_v_type:"${blogContent.profile.detail.verifyType.getCode()}",
        artic_id:"${blogContent.content.contentId}",
        artic_name:"${blogContent.content.subject}",
        artic_url:"${URL_WWW}/note/${blogContent.content.contentId}",
        artic_categ:"",
        artic_tag:[<c:forEach var="tag" items="${blogContent.content.contentTag.tags}" varStatus="st">"${tag}"<c:if test="${st.index<blogContent.content.contentTag.tags.size()-1}">,</c:if></c:forEach>],
        user_id:"${empty userSession.blogwebsite.uno ? 0 : userSession.blogwebsite.uno}",
        client:ct
    };
</script>





