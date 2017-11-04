/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-7-3
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
(function () {
    seajs.config({
        preload: [
//                    joyconfig.URL_LIB + '/static/js/common/jquery-1.5.2.js'
//                    joyconfig.URL_LIB + '/static/js/common/common.js'
        ],
//                alias: {
//                    'jquery': joyconfig.URL_LIB + '/static/js/common/jquery-1.5.2.js'
//                } ,
        map: [
            [/^(.*\/static\/js\/(?:page|biz|init)\/.*?)([^\/]*\.js)$/i, '$1$2?t=' + jsconfig.version]
        ]
    });
})()