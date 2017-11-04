define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');

    var attabiz = {
        ajaxPostAtHtml:function() {
            var postAtHtml = '';
            $.ajax({
                        url: "/ajax/atme/postat",
                        type:'post',
                        async:false,
                        success:function(data) {
                            postAtHtml = data;
                        }
                    });
            return postAtHtml;
        }

    }

    return attabiz;
});