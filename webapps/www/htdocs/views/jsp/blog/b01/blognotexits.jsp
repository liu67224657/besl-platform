<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/views/jsp/common/taglibs.jsp" %>
<fmt:setBundle basename="hotdeploy.i18n.www.user" var="userProps"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>页面不存在 - ${jmh_title}</title>
    <link href="${URL_LIB}/static/theme/default/css/core.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/global.css?${version}" rel="stylesheet" type="text/css"/>
    <link href="${URL_LIB}/static/theme/default/css/common.css?${version}" rel="stylesheet" type="text/css"/>
    <script src="${URL_LIB}/static/js/common/seajs.js"></script>
    <script src="${URL_LIB}/static/js/common/seajs-config.js"></script>
    <script>
        seajs.use('${URL_LIB}/static/js/init/home-init')
    </script>
</head>
<body>
<!--头部开始-->
<c:import url="/tiles/header?redr=${requestScope.browsersURL}"/>
<!--头部结束-->
<!--中间部分开始-->
<div class="content clearfix">
    <div class="normal_error">
        <h3>很抱歉，您想访问的页面不存在</h3>

        <p>
            您可以<br/>
            1. 请检查您输入的网址是否正确<br/>
            2. 返回<a href="${URL_WWW}/home">我的首页</a><br/>
            3. 去<a href="${URL_WWW}/discovery">随便看看</a>
            <%--3. 去<a href="${URL_WWW}/discovery" class="see_auto">随便看看</a>--%>
        </p>
    </div>
</div>
<!--中间部分结束-->
<!--页尾开始-->
<%@include file="/views/jsp/tiles/footer.jsp" %>
<!--页尾结束-->
</body>
<script type="text/javascript" charset="utf-8" src="http://static.baifendian.com/api/bcore.min.js"></script>
<script type="text/javascript">
    var BFD_ITEM_INFO = {
        bfd_del_item_id : new Array("${cid}"),    //删除已经下架的文章的id，可以是多个。
        client : "Czhaomi"
    };
    new $Core(function() {
        var _customid = BFD_ITEM_INFO.client;
        this.options.cid = _customid;
        var _ids = BFD_ITEM_INFO.bfd_del_item_id;
        for (var z = 0; z < _ids.length; z++) {
            this.push(new $Core.inputs.RmItem(_ids[z]));
        }
        this.invoke();
    });
</script>
</html>