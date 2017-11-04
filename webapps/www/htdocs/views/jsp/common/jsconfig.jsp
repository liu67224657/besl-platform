<script type="text/javascript">
    var joyconfig = {
        ctx: '${ctx}',
        URL_WWW: '${URL_WWW}',
        URL_LIB: '${URL_LIB}',
        URL_STATIC:'${URL_STATIC}',
        URL_M:'${URL_M}',
        DOMAIN: '${DOMAIN}',
        urlUpload: '${urlUpload}',
        joyuserno: '<c:out value="${userSession.uno}"/>',
        uid: '<c:out value="${userSession.uid}"/>',
        joyblogname: '${fn:escapeXml(userSession.nick)}',
        joyblogdomain: '${userSession.domain}',
        joyheadimg: '${userSession.icon}',
        joysex: '${userSession.sex}',
        ctrlFlag: false,
        shutDownRDomain: '${shutDownRDomain}',
        token: '${userSession.token}',
        version:${version}
    }
    var Sys = {};
    var ua = navigator.userAgent.toLowerCase();
    var s;
    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
            (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
                    (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
                            (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
                                    (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
</script>