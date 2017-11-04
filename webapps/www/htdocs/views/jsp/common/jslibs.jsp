<%@ include file="/views/jsp/common/jstllibs.jsp" %>
var joyconfig = {
infostatus : '${infostatus}',
ctx : '${ctx}',
URL_WWW : '${URL_WWW}',
URL_LIB : '${URL_LIB}',
DOMAIN : '${DOMAIN}',
urlUpload : '${urlUpload}',
joyuserno : '<c:out value="${userSession.uno}"/>',
joyblogname: '${fn:escapeXml(userSession.nick)}',
joyblogdomain : '${userSession.domain}',
joysex:'${userSession.sex}',
joyheadimg : '${userSession.icon}',
ctrlFlag : false,
shutDownRDomain : '${shutDownRDomain}',
version:${version}
}

