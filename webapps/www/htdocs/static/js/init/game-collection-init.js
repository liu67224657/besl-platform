define(function (require, exports, module) {
    var joymealert = require('../common/joymealert');
    var alertOption = {
        tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };

    var host = window.location.host.substr(window.location.host.indexOf('.'));
    var wwwHost = 'http://www' + host + '/';
    var apiHost = 'http://api' + host + '/';
    var passportHost = 'http://passport' + host + "/";

    $(document).ready(function () {
        $('#search-btn').on('click', function(){
            var name = $('#search-name').val();
            if(name != undefined){
                name = name.replace(/^\s+|\s+$/g, '');
            }
            if(name != '' && name != '请输入要查找的游戏名称'){
                window.location.href = wwwHost + 'collection/genre/name:'+name;
            }
        });
        $('#search-name').keydown(function(e){
            if(e.keyCode == 13){
                var name = $(this).val();
                if(name != undefined){
                    name = name.replace(/^\s+|\s+$/g, '');
                }
                if(name != '' && name != '请输入要查找的游戏名称'){
                    window.location.href = wwwHost + 'collection/genre/name:'+name;
                }
            }
        });
        //平台
        $('span[name = genre_platformtype]').on('click', function () {
            var platform = $(this).attr('data-platformtype');
            if(platform == ''){
                window.location.href = wwwHost + 'collection/genre';
            }else{
                window.location.href = wwwHost + 'collection/genre/p'+platform;
            }
        });
        $('a[name = genre_platform]').on('click', function () {
            var platform = $(this).attr('data-platform');
            if(platform == ''){
                window.location.href=wwwHost + 'collection/genre';
            }else{
                window.location.href=wwwHost + 'collection/genre/p'+platform;
            }
        });
        //联网
        $('a[name = genre_net]').on('click', function () {
            var net = $(this).attr('data-net');
            if(net == ''){
                window.location.href=wwwHost + "collection/genre";
            }else{
                window.location.href=wwwHost + "collection/genre/n" +net;
            }
        });
        //语言
        $('a[name = genre_language]').on('click', function () {
            var language =  $(this).attr('data-language');
            if(language == ''){
                window.location.href=wwwHost + "collection/genre";
            }else{
                window.location.href=wwwHost + "collection/genre/l"+language;
            }
        });
        //类型
        $('a[name = genre_category]').on('click', function () {
            var category = $(this).attr('data-category');
            if(category == ''){
                window.location.href=wwwHost + "collection/genre";
            }else{
                window.location.href=wwwHost + "collection/genre/c"+category;
            }
        });
        //题材
        $('a[name = genre_theme]').on('click', function () {
            var theme = $(this).attr('data-theme');
            if(theme == ''){
                window.location.href = wwwHost + "collection/genre";
            }else{
                window.location.href = wwwHost + "collection/genre/t"+theme;
            }
        });
    });
});





