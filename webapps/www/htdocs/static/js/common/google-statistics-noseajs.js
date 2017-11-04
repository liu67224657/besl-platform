/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-6-26
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-27088945-1']);
_gaq.push(['_setDomainName', 'joyme.com']);
_gaq.push(['_trackPageview']);

(function () {
    var ga = document.createElement('script');
    ga.type = 'text/javascript';
    ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(ga, s);
})();