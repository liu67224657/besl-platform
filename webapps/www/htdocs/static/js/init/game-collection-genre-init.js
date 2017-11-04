define(function (require, exports, module) {
    var joymealert = require('../common/joymealert');
    var alertOption = {
        tipLayer: true, alertFooter: true, alertButtonText: '确 定', timeOutMills: -1,
        width: 400
    };

    var host = window.location.host.substr(window.location.host.indexOf('.'));
    var wwwHost = 'http://www' + host + '/';
    var apiHost = 'http://api' + host + '/';
    var getGameListBlock = false;

    $(document).ready(function () {
        $('#search-btn').on('click', function(){
            var name = $('#search-name').val();
            if(name != undefined){
                name = name.replace(/^\s+|\s+$/g, '');
            }
            if(name != '' && name != '请输入要查找的游戏名称'){
                window.location.href = wwwHost + "collection/genre/name:"+name;
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
        $('#genre_allplatform').on('click', function(){
            $('span[name = genre_platformtype].check').removeClass('check');
            $('a[name = genre_platform].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var net = $('a[name = genre_net].active').attr('data-net');
            var language = $('a[name = genre_language].active').attr('data-language');
            var category = $('a[name = genre_category].active').attr('data-category');
            var theme = $('a[name = genre_theme].active').attr('data-theme');
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = wwwHost + "collection/genre/"+param;
        });

        $('span[name = genre_platformtype]').on('click', function () {
            if($(this).attr('data-click') == 'false'){
                return;
            }
            $('span[name = genre_platformtype].check').removeClass('check');
            $('a[name = genre_platform].active').removeClass('active');
            $(this).addClass('check');

            var param = "";
            var platformType = $(this).attr('data-platformtype');
            var platform = $('a[name = genre_platform].active').attr('data-platform');
            var net = $('a[name = genre_net].active').attr('data-net');
            var language = $('a[name = genre_language].active').attr('data-language');
            var category = $('a[name = genre_category].active').attr('data-category');
            var theme = $('a[name = genre_theme].active').attr('data-theme');
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = wwwHost + "collection/genre/"+param;
        });
        $('a[name = genre_platform]').on('click', function () {
            if($(this).attr('data-click') == 'false'){
                return;
            }
            $('a[name = genre_platform].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('span[name = genre_platformtype].check').attr('data-platformtype');
            var platform = $(this).attr('data-platform');
            var net = $('a[name = genre_net].active').attr('data-net');
            var language = $('a[name = genre_language].active').attr('data-language');
            var category = $('a[name = genre_category].active').attr('data-category');
            var theme = $('a[name = genre_theme].active').attr('data-theme');
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = wwwHost + "collection/genre/"+param;
        });
        //联网
        $('a[name = genre_net]').on('click', function () {
            if($(this).attr('data-click') == 'false'){
                return;
            }
            $('a[name = genre_net].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('span[name = genre_platformtype].check').attr('data-platformtype');
            var platform = $('a[name = genre_platform].active').attr('data-platform');
            var net = $(this).attr('data-net');
            var language = $('a[name = genre_language].active').attr('data-language');
            var category = $('a[name = genre_category].active').attr('data-category');
            var theme = $('a[name = genre_theme].active').attr('data-theme');
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = wwwHost + "collection/genre/"+param;
        });
        //语言
        $('a[name = genre_language]').on('click', function () {
            if($(this).attr('data-click') == 'false'){
                return;
            }
            $('a[name = genre_language].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('span[name = genre_platformtype].check').attr('data-platformtype');
            var platform = $('a[name = genre_platform].active').attr('data-platform');
            var net = $('a[name = genre_net].active').attr('data-net');
            var language = $(this).attr('data-language');
            var category = $('a[name = genre_category].active').attr('data-category');
            var theme = $('a[name = genre_theme].active').attr('data-theme');
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = wwwHost + "collection/genre/"+param;
        });
        //类型
        $('a[name = genre_category]').on('click', function () {
            if($(this).attr('data-click') == 'false'){
                return;
            }
            $('a[name = genre_category].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('span[name = genre_platformtype].check').attr('data-platformtype');
            var platform = $('a[name = genre_platform].active').attr('data-platform');
            var net = $('a[name = genre_net].active').attr('data-net');
            var language = $('a[name = genre_language].active').attr('data-language');
            var category = $(this).attr('data-category');
            var theme = $('a[name = genre_theme].active').attr('data-theme');
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = wwwHost + "collection/genre/"+param;
        });
        //题材
        $('a[name = genre_theme]').on('click', function () {
            if($(this).attr('data-click') == 'false'){
                return;
            }
            $('a[name = genre_theme].active').removeClass('active');
            $(this).addClass('active');

            var param = "";
            var platformType = $('span[name = genre_platformtype].check').attr('data-platformtype');
            var platform = $('a[name = genre_platform].active').attr('data-platform');
            var net = $('a[name = genre_net].active').attr('data-net');
            var language = $('a[name = genre_language].active').attr('data-language');
            var category = $('a[name = genre_category].active').attr('data-category');
            var theme = $(this).attr('data-theme');
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            if(platform != '' && platform != undefined){
                param+=("p"+platform+"_");
            }else if(platformType != '' && platformType != undefined){
                param+=("p"+platformType+"_");
            }
            if(net != '' && net != undefined){
                param+=("n"+net+"_");
            }
            if(language != '' && language != undefined){
                param += ("l"+language+"_");
            }
            if(category != '' && category != undefined){
                param += ("c"+category+"_");
            }
            if(theme != '' && theme != undefined){
                param += ('t' + theme + "_");
            }
            if(sort != '' && sort != undefined){
                param += ("s" + sort+"_");
            }
            param += ("page:"+1);
            window.location.href = wwwHost + "collection/genre/"+param;
        });
        //排序
        $('a[name = genre_sort]').on('click', function () {
        	
            var param = "";
            var sort = $(this).attr('data-sort');
            var name = $('#search-name').val();
            if(name != undefined && name != '请输入要查找的游戏名称' && name.replace(/^\s+|\s+$/g, '') != ''){
                param = "name:"+name.replace(/^\s+|\s+$/g, '')+"_";
                if(sort != '' && sort != undefined){
                    param += ("s" + sort+"_");
                }
                param += ("page:"+1);
                if($(this).hasClass('active')==false){
                    $('a[name = genre_sort].active').removeClass('active');
                    $(this).addClass('active');
                    window.location.href = wwwHost + "collection/genre/"+param;
                }
            }else{
                var platformType = $('span[name = genre_platformtype].check').attr('data-platformtype');
                var platform = $('a[name = genre_platform].active').attr('data-platform');
                var net = $('a[name = genre_net].active').attr('data-net');
                var language = $('a[name = genre_language].active').attr('data-language');
                var category = $('a[name = genre_category].active').attr('data-category');
                var theme = $('a[name = genre_theme].active').attr('data-theme');

                if(platform != '' && platform != undefined){
                    param+=("p"+platform+"_");
                }else if(platformType != '' && platformType != undefined){
                    param+=("p"+platformType+"_");
                }
                if(net != '' && net != undefined){
                    param+=("n"+net+"_");
                }
                if(language != '' && language != undefined){
                    param += ("l"+language+"_");
                }
                if(category != '' && category != undefined){
                    param += ("c"+category+"_");
                }
                if(theme != '' && theme != undefined){
                    param += ('t' + theme + "_");
                }
                if(sort != '' && sort != undefined){
                    param += ("s" + sort+"_");
                }
                param += ("page:"+1);
                if($(this).hasClass('active')==false){
                    $('a[name = genre_sort].active').removeClass('active');
                    $(this).addClass('active');
                    window.location.href = wwwHost + "collection/genre/"+param; 	
                }
            }
        });
        //分页
        $('li[name=li_page]').on('click', function () {
            var p = parseInt($(this).attr('data-page'));

            var param = "";
            var sort = $('a[name = genre_sort].active').attr('data-sort');
            var name = $('#search-name').val();
            if(name != undefined && name != '请输入要查找的游戏名称' && name.replace(/^\s+|\s+$/g, '') != ''){
                param = "name:"+name.replace(/^\s+|\s+$/g, '')+"_";
                if(sort != '' && sort != undefined){
                    param += ("s" + sort+"_");
                }
                param += ("page:"+p);
                window.location.href = wwwHost + "collection/genre/"+param;
            }else{
                var platformType = $('span[name = genre_platformtype].check').attr('data-platformtype');
                var platform = $('a[name = genre_platform].active').attr('data-platform');
                var net = $('a[name = genre_net].active').attr('data-net');
                var language = $('a[name = genre_language].active').attr('data-language');
                var category = $('a[name = genre_category].active').attr('data-category');
                var theme = $('a[name = genre_theme].active').attr('data-theme');

                if(platform != '' && platform != undefined){
                    param+=("p"+platform+"_");
                }else if(platformType != '' && platformType != undefined){
                    param+=("p"+platformType+"_");
                }
                if(net != '' && net != undefined){
                    param+=("n"+net+"_");
                }
                if(language != '' && language != undefined){
                    param += ("l"+language+"_");
                }
                if(category != '' && category != undefined){
                    param += ("c"+category+"_");
                }
                if(theme != '' && theme != undefined){
                    param += ('t' + theme + "_");
                }
                if(sort != '' && sort != undefined){
                    param += ("s" + sort+"_");
                }
                param += ("page:"+p);
                window.location.href = wwwHost + "collection/genre/"+param;
            }
        });
    });
});



