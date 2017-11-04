define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var loginBiz = require('../biz/login-biz');
    var atmeBiz = require('../biz/atme-biz');
    var joymealert = require('../common/joymealert');

    var atme = {
        bindRemoveAt:function() {
            $("a[name=removeAt]").live("click", function() {
                var removeAtObj = $(this);
                joymealert.confirm({offset:'',tipLayer:true,text:'确定删除该信息？',submitFunction:function() {
                            atmeBiz.removeAt(removeAtObj.attr("data-tid"), removeAtObj.attr("data-did"), atme.removeAtCallback);
                        }});
            });
        },
        removeAtCallback:function(tid, resultJson) {
            loginBiz.maskLoginByJsonObj(resultJson);
            if (resultJson.status_code == '1') {
                $("#" + tid).remove();
            } else if (resultJson.status_code == '0') {
                var alertOption = {text:'删除失败',tipLayer:true,textClass:"tipstext"};
                joymealert.alert(alertOption);
            }
        }

    }
    return atme;
});