<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/view/jsp/common/taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>没有权限页面</title>
		<link rel='stylesheet' href='${ctx}/static/include/css/privilege.css' type='text/css' />
		
<script type="text/javascript">
//没有权限返回
function back(){
	if(window.opener!= undefined){
		window.close();
	}else{
		history.back();
	}

}
</script>

	</head>
	<body>
		
		<table width="96%" border="0">
			<tr>
				<td width="20%" class="border_none"></td>
				<td width="10%" class="border_none">
					<img src="${ctx}/static/images/error.jpg">
				</td>
				<td width="70%" class="border_none">
					对不起，您没有此项操作权限。
				</td>
			</tr>
			<tr>
				<td width="20%" class="border_none"></td>
				<td height="40px" class="border_none" colspan="2">
					<input name="test" type="button" value="返回" class="input_submit"
						onclick="javascript:back();" />
				</td>
			</tr>
		</table>
	</body>
</html>