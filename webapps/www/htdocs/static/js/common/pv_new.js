/*
 *
 * This code is data acquisition code for www.joyme.com web traffic analysis platform.
 * www.enjoyfound.com
 */
var locationId;
function lz_encode(str) {
    var e = "", i = 0;

    for (i = 0; i < str.length; i++) {
        if (str.charCodeAt(i) >= 0 && str.charCodeAt(i) <= 255) {
            e = e + escape(str.charAt(i));
        } else {
            e = e + str.charAt(i);
        }
    }
    return e;
}

// 获取屏幕尺寸
function lz_get_screen() {
    var c = "";

    if (self.screen) {
        c = screen.width + "x" + screen.height;
    }
    return c;
}

// 返回客户端操作系统
function get_OS() {
    var sUserAgent = window.navigator.userAgent;

    var isWin = (window.navigator.platform == "Win32")
        || (window.navigator.platform == "Windows");
    var isMac = (window.navigator.platform == "Mac68K")
        || (window.navigator.platform == "MacPPC")
        || (window.navigator.platform == "Macintosh");
    var isUnix = (window.navigator.platform == "X11") && isWin && isMac;
    var isLinux = (String(navigator.platform).indexOf("Linux") > -1);

    var sUserAgent = navigator.userAgent.toLowerCase();
    var bIsIpad = sUserAgent.match(/ipad/i) == "iPad";
    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphoneOs";
    var bIsMidp = sUserAgent.match(/midp/i) == "midp";
    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
    var bIsAndroid = sUserAgent.match(/android/i) == "android";
    var bIsCE = sUserAgent.match(/windows ce/i) == "windowsCe";
    var bIsWM = sUserAgent.match(/windows mobile/i) == "windowsMobile";

    if (isUnix)
        return "Unix";
    if (isLinux)
        return "Linux";

    if (isMac) {
        var isMac68K = sUserAgent.indexOf("Max_68000") > -1
            || sUserAgent.indexOf("68K") > -1;
        if (isMac68K)
            return "Mac68K";
        var isMacPPC = sUserAgent.indexOf("Mac_PowerPC") > -1
            || sUserAgent.indexOf("PPC") > -1;
        if (isMacPPC)
            return "MacPPC";
        return "Mac";
    }
    if (isWin) {
        var isWin95 = sUserAgent.indexOf("Win95") > -1
            || sUserAgent.indexOf("Windows 95") > -1;
        if (isWin95)
            return "Win95";
        var isWin98 = sUserAgent.indexOf("Win98") > -1
            || sUserAgent.indexOf("Windows 98") > -1;
        if (isWin98)
            return "Win98";
        var isWinME = sUserAgent.indexOf("Win 9x 4.90") > -1
            || sUserAgent.indexOf("Windows ME") > -1;
        if (isWinME)
            return "WinMe";
        var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1
            || sUserAgent.indexOf("Windows 2000") > -1;
        if (isWin2K)
            return "Win2000";
        var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1
            || sUserAgent.indexOf("Windows 2003") > -1;
        if (isWin2003)
            return "Win2003";
        var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1
            || sUserAgent.indexOf("Windows XP") > -1;
        if (isWinXP)
            return "WinXP";
        var isWinVista = sUserAgent.indexOf("Windows NT 6.0") > -1
            || sUserAgent.indexOf("Windows Vista") > -1;
        if (isWinVista)
            return "WinVista";
        var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1
            || sUserAgent.indexOf("Windows 7") > -1;
        if (isWin7)
            return "Win7";
        var isWinNT4 = sUserAgent.indexOf("WinNT") > -1
            || sUserAgent.indexOf("WindowsNT") > -1
            || sUserAgent.indexOf("WinNT4.0") > -1
            || sUserAgent.indexOf("Windows NT 4.0")
            && (!isWinME && !isWin2K && !isWinXP && !isWin2003
            && !isWinVista && !isWin7);
        if (isWinNT4)
            return "WinNT";

    }

    if (bIsIpad) return "ipad";
    if (bIsIphoneOs) return "iphone os";
    if (bIsAndroid) return "android";
    if (bIsCE) return "windows ce";
    if (bIsWM) return "windows mobile";

    return "None";
}

// 返回浏览器版本
function lz_get_browser_version() {
    var ua = "";
    var n = navigator;

    if (n.userAgent) {
        ua = n.userAgent.toLowerCase();
    }
    var Sys = {};
    var s;
    (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : (s = ua
        .match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : (s = ua
        .match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : (s = ua
        .match(/camino\/([\d.]+)/)) ? Sys.camino = s[1] : (s = ua
        .match(/gecko\/([\d.]+)/)) ? Sys.gecko = s[1] : (s = ua
        .match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : (s = ua
        .match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

    // 以下进行测试
    if (Sys.ie)
        return 'IE: ' + Sys.ie;
    if (Sys.firefox)
        return 'Firefox: ' + Sys.firefox;
    if (Sys.chrome)
        return 'Chrome: ' + Sys.chrome;
    if (Sys.opera)
        return 'Opera: ' + Sys.opera;
    if (Sys.camino)
        return 'Camino: ' + Sys.camino;
    if (Sys.gecko)
        return 'Gecko: ' + Sys.gecko;
    if (Sys.safari)
        return 'Safari: ' + Sys.safari;
    return "None";
}

// 返回浏览器的名称
function lz_get_app() {
    var a = "";
    var n = navigator;

    if (n.appName) {
        a = n.appName;
    }
    return a;
}

function lz_c_ctry_top_domain(str) {
    var pattern = "/^aero$|^cat$|^coop$|^int$|^museum$|^pro$|^travel$|^xxx$|^com$|^net$|^gov$|^org$|^mil$|^edu$|^biz$|^info$|^name$|^ac$|^mil$|^co$|^ed$|^gv$|^nt$|^bj$|^hz$|^sh$|^tj$|^cq$|^he$|^nm$|^ln$|^jl$|^hl$|^js$|^zj$|^ah$|^hb$|^hn$|^gd$|^gx$|^hi$|^sc$|^gz$|^yn$|^xz$|^sn$|^gs$|^qh$|^nx$|^xj$|^tw$|^hk$|^mo$|^fj$|^ha$|^jx$|^sd$|^sx$/i";

    if (str.match(pattern)) {
        return 1;
    }

    return 0;
}

function lz_c_ctry_domain(str) {
    var pattern = "/^ac$|^ad$|^ae$|^af$|^ag$|^ai$|^al$|^am$|^an$|^ao$|^aq$|^ar$|^as$|^at$|^au$|^aw$|^az$|^ba$|^bb$|^bd$|^be$|^bf$|^bg$|^bh$|^bi$|^bj$|^bm$|^bo$|^br$|^bs$|^bt$|^bv$|^bw$|^by$|^bz$|^ca$|^cc$|^cd$|^cf$|^cg$|^ch$|^ci$|^ck$|^cl$|^cm$|^cn$|^co$|^cr$|^cs$|^cu$|^cv$|^cx$|^cy$|^cz$|^de$|^dj$|^dk$|^dm$|^do$|^dz$|^ec$|^ee$|^eg$|^eh$|^er$|^es$|^et$|^eu$|^fi$|^fj$|^fk$|^fm$|^fo$|^fr$|^ly$|^hk$|^hm$|^hn$|^hr$|^ht$|^hu$|^id$|^ie$|^il$|^im$|^in$|^io$|^ir$|^is$|^it$|^je$|^jm$|^jo$|^jp$|^ke$|^kg$|^kh$|^ki$|^km$|^kn$|^kp$|^kr$|^kw$|^ky$|^kz$|^la$|^lb$|^lc$|^li$|^lk$|^lr$|^ls$|^lt$|^lu$|^lv$|^ly$|^ga$|^gb$|^gd$|^ge$|^gf$|^gg$|^gh$|^gi$|^gl$|^gm$|^gn$|^gp$|^gq$|^gr$|^gs$|^gt$|^gu$|^gw$|^gy$|^ma$|^mc$|^md$|^mg$|^mh$|^mk$|^ml$|^mm$|^mn$|^mo$|^mp$|^mq$|^mr$|^ms$|^mt$|^mu$|^mv$|^mw$|^mx$|^my$|^mz$|^na$|^nc$|^ne$|^nf$|^ng$|^ni$|^nl$|^no$|^np$|^nr$|^nu$|^nz$|^om$|^re$|^ro$|^ru$|^rw$|^pa$|^pe$|^pf$|^pg$|^ph$|^pk$|^pl$|^pm$|^pr$|^ps$|^pt$|^pw$|^py$|^qa$|^wf$|^ws$|^sa$|^sb$|^sc$|^sd$|^se$|^sg$|^sh$|^si$|^sj$|^sk$|^sl$|^sm$|^sn$|^so$|^sr$|^st$|^su$|^sv$|^sy$|^sz$|^tc$|^td$|^tf$|^th$|^tg$|^tj$|^tk$|^tm$|^tn$|^to$|^tp$|^tr$|^tt$|^tv$|^tw$|^tz$|^ua$|^ug$|^uk$|^um$|^us$|^uy$|^uz$|^va$|^vc$|^ve$|^vg$|^vi$|^vn$|^vu$|^ye$|^yt$|^yu$|^za$|^zm$|^zr$|^zw$/i";

    if (str.match(pattern)) {
        return 1;
    }

    return 0;
}

// 获取访问来源
function getReferrer() {
    var referrer = '';
    try {
        referrer = top.document.referrer;
    } catch (e) {
        if (parent) {
            try {
                referrer = parent.document.referrer;
            } catch (e2) {
                referrer = '';
            }
        }
    }
    if (referrer === '') {
        referrer = document.referrer;
    }

    return referrer;
}
// 获取域名如：www.baidu.com－>baidu.com
function lz_get_domain(host) {
    var d = host.replace(/^www\./, "");
    return d;
    var ss = d.split(".");
    var l = ss.length;

    if (l == 3) {
        if (lz_c_ctry_top_domain(ss[1]) && lz_c_ctry_domain(ss[2])) {
        } else {
            d = ss[1] + "." + ss[2];
        }
    } else if (l >= 3) {

        var ip_pat = "^[0-9]*\.[0-9]*\.[0-9]*\.[0-9]*$";

        if (host.match(ip_pat)) {
            return d;
        }

        if (lz_c_ctry_top_domain(ss[l - 2]) && lz_c_ctry_domain(ss[l - 1])) {
            d = ss[l - 3] + "." + ss[l - 2] + "." + ss[l - 1];
        } else {
            d = ss[l - 2] + "." + ss[l - 1];
        }
    }

    return d;
}

window.onbeforeunload = function() {
    var url = document.location.href;
    url = lz_encode(String(url));

    if (!window.pvclick) {
        var action = "http://www.joyme.test:8081/report/close.do?click=" + window.pvclick +
            "&url=" + url + "&timestamp=" + (new Date().getTime());
        document.write("<img src=\"" + action + "\" border=\"0\" width=\"0\" height=\"0\" >");
    }
}


window.onclick = function() {
    window.pvclick = true;
}


function lz_main(fid, stats) {
    locationId = fid;

    //get domain
    var host = document.location.host;
    var domain = lz_get_domain(host.toLocaleLowerCase());

    //get refer
    var ref = getReferrer();
    ref = lz_encode(String(ref));


    //get screen
    var screen = lz_get_screen();
    screen = lz_encode(String(screen));

    //get browser
    var app = lz_get_app();
    app = lz_encode(String(app));

    //get brwoser version
    var browser_version = lz_get_browser_version();
    browser_version = lz_encode(String(browser_version));

    var url = document.location.href;
    url = lz_encode(String(url));

    //get system
    var os = get_OS();
    os = lz_encode(String(os));
    var date = new Date();

    var cookie = new Cookies();


    var pvintime=cookie.get('pv.int');

    //普通设置
    cookie.set("pv.os", os);
    var d = new Date(Date.parse(date) + 1000 * 60 * 60 * 24 * 30);
    cookie.set("pv.int", date.getTime(), d, joyconfig.DOMAIN, "/");
    cookie.set("pv.et", app, d, joyconfig.DOMAIN, "/");
    cookie.set("pv.ev", browser_version, d, joyconfig.DOMAIN, "/");
    cookie.set("pv.scn", screen, d, joyconfig.DOMAIN, "/");



//    document.cookie = 'pv.os=' + os;
//    document.cookie = 'pv.int=' + (date.getTime());
//    document.cookie = 'pv.et=' + app;
//    document.cookie = 'pv.ev=' + browser_version;
//    document.cookie = 'pv.scn=' + screen;
//
//    document.cookie = 'expires=' + new Date(Date.parse(date) + 1000 * 60 * 60 * 24 * 30).toGMTString();
//    document.cookie = 'domain=' + joyconfig.DOMAIN;
//    document.cookie = 'path=/';

    var action = "http://www.joyme.test:8081/report/pv.do"
    var dest = action + "?ref=" + ref + "&url=" + url+"&pv.int="+pvintime;

    document.write("<img src=\"" + dest + "\" border=\"0\" width=\"0\" height=\"0\" >");
}

function Cookies() {
    this.get = function(key) {
        var cookie = document.cookie;
        var cookieArray = cookie.split(';');
        var val = "";
        for (var i = 0; i < cookieArray.length; i++) {
            if (cookieArray[i].trim().substr(0, key.length) == key) {
                val = cookieArray[i].trim().substr(key.length + 1);
                break;
            }
        }
        return unescape(val);
    };
    this.getChild = function(key, childKey) {
        var child = this.get(key);
        var childs = child.split('&');
        var val = "";

        for (var i = 0; i < childs.length; i++) {
            if (childs[i].trim().substr(0, childKey.length) == childKey) {
                val = childs[i].trim().substr(childKey.length + 1);
                break;
            }
        }
        return val;
    };
    this.set = function(key, value) {
        var cookie = "";
        if (!!key && !!value)
            cookie += key + "=" + escape(value) + ";";
        if (!!arguments[2])
            cookie += "expires=" + arguments[2].toGMTString() + ";";
        if (!!arguments[3])
            cookie += "domain=" + arguments[3] + ";";
        if (!!arguments[4])
            cookie += "path=" + arguments[4] + ";";
        document.cookie = cookie;
    };
    this.remove = function(key) {
        var date = new Date();
        date.setFullYear(date.getFullYear() - 1);
        var cookie = " " + key + "=;expires=" + date + ";"
        document.cookie = cookie;
    }
}

