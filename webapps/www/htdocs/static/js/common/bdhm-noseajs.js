/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-6-26
 * Time: 下午1:51
 * To change this template use File | Settings | File Templates.
 */
<!-- Piwik -->
var _paq = _paq || [];
_paq.push(["setDocumentTitle", document.domain + "/" + document.title]);
_paq.push(['trackPageView']);
_paq.push(['enableLinkTracking']);
(function() {
    var u="//stat.joyme.com/";
    _paq.push(['setTrackerUrl', u+'piwik.php']);
    _paq.push(['setSiteId', 11]);
    var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
    g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
})();
document.write('<noscript><p><img src="//stat.joyme.com/piwik.php?idsite=11" style="border:0;" alt="" /></p></noscript>');
<!-- End Piwik Code -->
