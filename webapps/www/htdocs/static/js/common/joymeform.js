define(function (require, exports, module) {
    var $ = require('./jquery-1.5.2');
    require('./jquery.form');

    var defaultOption = {
        formid:'',
        paramObj:null,
        beforeSend:null,
        success:function(req,paramObj){},
        complete:null
    };
    var joymeform = {

        form:function(option) {
             option = $.extend({}, defaultOption, option);

            if(option.beforeSend!=null && option.beforeSend()===false){
                    return;
            }

             var formObj=$("#"+option.formid);

            formObj.ajaxForm(function(req) {
                  option.success(req,option.paramObj);

                 if(option.complete!=null){
                    option.complete(req,option.paramObj);
                 }
            });
            formObj.submit();
        }
    }
    return joymeform;
});






