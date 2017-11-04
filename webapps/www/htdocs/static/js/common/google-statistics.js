/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 11-11-21
 * Time: 下午6:24
 * To change this template use File | Settings | File Templates.
 */

/*谷歌搜索用*/
//var _gaq = _gaq || [];
//_gaq.push(['_setAccount', 'UA-27088945-1']);
//_gaq.push(['_setDomainName', 'joyme.com']);
//_gaq.push(['_trackPageview']);

//(function () {
//    var ga = document.createElement('script');
//    ga.type = 'text/javascript';
//    ga.async = true;
//    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
//    var s = document.getElementsByTagName('script')[0];
//    s.parentNode.insertBefore(ga, s);
//})();
/*谷歌搜索用*/


/*baidu搜索用
 var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://" : " http://");
 document.write(unescape("%3Cscript src='" + _bdhmProtocol + "hm.baidu.com/h.js%3F0a10eee10a39c33d4a8a61d3ad35a96f' type='text/javascript'%3E%3C/script%3E"));
 baidu搜索用*/

define(function(require, exports) {
    var global = this;
    var _gaq = [];
    _gaq.push(['_setAccount', 'UA-27088945-1']);
    _gaq.push(['_setDomainName', 'joyme.com']);
    _gaq.push(['_trackPageview']);
    global._gaq = _gaq;
    var src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    require.async(src);

});