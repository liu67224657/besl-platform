define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('./login-biz');

    var scrollBiz = {

        scrollHomePage:function (param) {

            $.ajax({
                        url:"/json/home",
                        data:{p:param.pageNo,screen:param.screenNo,totalRows:param.totalRows},
                        type:'post',
                        beforeSend:function() {
                            param.loadfunction();
                        },
                        success:function (data) {
                            var jsonObj = eval('(' + data + ')');
                            if (jsonObj.status_code == '-1') {
                                var loginOption = {
                                    referer:window.location.href
                                };
                                login.maskLogin(loginOption);
                                return;
                            }

                            param.callback(jsonObj, param);
                        }});
        }
    }
    return scrollBiz;
});






