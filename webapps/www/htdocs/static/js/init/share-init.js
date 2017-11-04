define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    $(document).ready(function() {
        $('.data-submit').click(function() {
            var key = $(this).attr("data-key");

            var formObj = $('#share_form');

            formObj.attr('action', actionPrefix + key);
            formObj.submit();
        });
        checkCount($('#textarea_sharebody'));

        $('#textarea_sharebody').bind('keyup keydown', function() {
            checkCount($(this));
        });
    })

    function checkCount(dom) {
        var bodyNumDom = $('#body_num');
        bodyNumDom.html(120 - dom.val().length);
        if (dom.val().length > 120) {
            bodyNumDom.css('color', 'red');
            return false;
        } else {
            bodyNumDom.css('color', '');
        }

        return true;
    }

    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});