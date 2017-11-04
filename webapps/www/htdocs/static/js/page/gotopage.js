define(function (require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');

    var gotopage = {
        initgoto:function() {
            $("#gotoPage").bind('click', function() {
                if (common.validateNumber($('#validatePageNo').val(), $('#maxPageNo').val())) {
                   window.location.href=$('#gotoPageUrl').val()+'p='+$('#validatePageNo').val();
                } else {
                    var joymealert = require('../common/joymealert');
                    var alertOption = {text:'您填写的页码不存在',tipLayer:false};
                    joymealert.alert(alertOption);
                }
            });
        }

    }

    return gotopage;

});