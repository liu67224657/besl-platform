/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 12-1-5
 * Time: 下午2:36
 * To change this template use File | Settings | File Templates.
 */
//生成图片地址
function genImgDomain(imgsrc, DOMAIN) {
    if (imgsrc.indexOf("http://") == 0) {
        return imgsrc;
    }

    if (imgsrc.indexOf("/r") || imgsrc.indexOf("\\r")) {
        var rxxx = imgsrc.substr(1, 4);
        rxxx = getRxxx(rxxx);
        return "http://" + rxxx + "\." + DOMAIN + imgsrc;
    }
}
//生成图片地址
function genAudioDomain(audioSrc, DOMAIN) {
    if (audioSrc.indexOf("http://") == 0) {
        return audioSrc;
    }

    if (audioSrc.indexOf("/r") || audioSrc.indexOf("\\r")) {
        var rxxx = audioSrc.substr(1, 4);
        rxxx = getRxxx(rxxx);
        return "http://" + rxxx + "\." + DOMAIN + audioSrc;
    }
}
//replace shutdown resource
function getRxxx(rxxx) {
    var downResource = eval('(' + shutDownRDomain + ')');
    $.each(downResource, function(i, val) {
        if (val.rd == rxxx) {
            rxxx = val.reRd;
            return false;
        }
    });
    return rxxx;
}

function genImgBySuffix(imgpath, DOMAIN, suffix) {
    var url = genImgDomain(imgpath, DOMAIN);
    var exname = url.substring(url.lastIndexOf("."));
    var sNametou
    if (url.lastIndexOf("_") == -1) {
        sNametou = url.substring(0, url.lastIndexOf("."));
    } else {
        sNametou = url.substring(0, url.lastIndexOf("_"));
    }

    if (suffix != null && suffix.length != 0) {
        sNametou += '_' + suffix + exname;
    } else {
        sNametou += exname;
    }

    return sNametou;
}
function parseGSLimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, 'GSL');
}

function parseGLLimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, 'GLL');
}

function parseSIimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, 'SI');
}

function parseIimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, 'I');
}

function parseMimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, 'M');
}

function parseSimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, 'S');
}

function parseSSimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, 'SS');
}

function parseBimg(imgpath, DOMAIN) {
    return genImgBySuffix(imgpath, DOMAIN, '');
}
