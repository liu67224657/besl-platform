define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var header = require('../page/header');
    $(document).ready(function() {
        //小纸条 搜索 返回
        header.noticeSearchReTopInit();
    });
    require.async('../common/google-statistics');
    require.async('../common/bdhm')
});