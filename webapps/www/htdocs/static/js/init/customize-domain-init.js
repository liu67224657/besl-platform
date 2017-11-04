define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.validate.min');
    var common = require('../common/common');
    var ajaxverify = require('../biz/ajaxverify');
    var customize = require('../page/customize');
    var header = require('../page/header');
    var tips = require('../common/tips')
    require.async('../common/tips');
    $(document).ready(function() {
        $("#form_domaininfo").validate({
                    rules: {
                        blogdomain: {required: true, rangelength: [5,20],regBlog:true,blogExists:""}//使用自定义验证方法
                    },
                    messages: {
                        blogdomain: {required:tipsText.userDomainName.user_blogdomain_notnull, rangelength:tipsText.userDomainName.user_blogdomain_length}
                    },
                    showErrors: customize.showValidateTips//使用自定义的提示方法
                });

        $("#savedomaininfo").bind("click", function() {
            customize.saveDomainInfo();
        });

        header.noticeSearchReTopInit();

    });

    $.validator.addMethod("regBlog", function(value, element, params) {
        return common.validateDomain(value);
    }, tips.userDomainName.user_blogdomain_wrong);

//    $.validator.addMethod('verifyDomain', function(value, element, params) {
//        return ajaxverify.verifyDomain(value);
//    }, tips.userDomainName.user_blogdomain_illegl);

    $.validator.addMethod('blogExists', function(value, element, params) {
        return !ajaxverify.verifyDomainExists(value, joyconfig.joyuserno);
    }, tips.userDomainName.user_blogdomain_exists);

    require.async('../common/google-statistics');
    require.async('../common/bdhm');
});