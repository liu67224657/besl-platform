<script type="text/javascript">
    var jsconfig = {
        ctx: '${ctx}',
        YK_DOMAIN: '${YK_DOMAIN}',
        joyuserno: '${user_uno}',
        uid: '${user_uid}',
        uid: '<c:out value="${userSession.uid}"/>',
        joyblogname: '${fn:escapeXml(user_nickname)}',
        token: '${user_token}',
        isLogin: '${isLogin}',
        version: '${version}'
    }
</script>