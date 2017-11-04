define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var regexShortUrl = new RegExp('(?:https?)://(?:(?:(?!https?://)[a-zA-z0-9])+)\\.(?:(?:(?!https?://)[a-zA-z0-9])+)(?:\\.(?:(?!https?://)[a-zA-z0-9])+)?(?:\\.(?:(?!https?://)[a-zA-z0-9])+)?(?:(?!https?://)[a-zA-z0-9/%?=&#!])*(?:\\.(?!https?://)[a-zA-Z])?', 'g');
    var province = null;
    var common = {
        endsWith:function(str, suffix) {
            return str.indexOf(suffix, str.length - suffix.length) !== -1;
        },

        //校验分页跳转到输入的数值是否合法
        validateNumber:function(num, maxPageNo) {
            var re = /^[1-9]+[0-9]*]*$/;
            if (re.test(num)) {
                if (num == 0 || parseInt(num) > parseInt(maxPageNo)) {
                    return false;
                }
            } else {
                return false;
            }
            return true;

        },

        checkAtSize:function(text, successCallback, failedCallback) {
            $.ajax({
                        url: "/json/atme/size",
                        type:'post',
                        data:{context:text},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                failedCallback(resMsg);
                            } else {
                                successCallback(resMsg);
                            }
                        }
                    });
        },
        //返回1失败，返回0成功
        checkAtNicks:function(text, successCallback, failedCallback) {
            $.ajax({
                        url: "/json/atme/nicks",
                        type:'post',
                        data:{context:text},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '1') {
                                failedCallback(resMsg);
                            } else {
                                successCallback(resMsg);
                            }
                        }
                    });
        } ,

        endsWithBr:function(str) {
            return /(?:<br[^/>]*\/?>)$/.test(str);
        },

        getRegionByid:function(regionId) {
            if (regionId == '' || regionId == -1) {
                return '';
            }

            if (province == null) {
                $.ajax({url:'/json/common/getregion',
                            data:{},
                            async:false,
                            success:function(req) {
                                province = eval('(' + req + ')');
                            }});
            }

            if (province[regionId] == null) {
                return '';
            }

            return province[regionId].regionName;
        },

        getInputLength:function(str) {
            if (str == null || str.length == 0) {
                return false;
            }
            var len = str.length;
            var reLen = 0;
            for (var i = 0; i < len; i++) {
                if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) {
                    // 全角
                    reLen += 1;
                } else {
                    reLen += 0.5;
                }
            }

            //处理链接字数
            var matches = str.match(regexShortUrl);
            if (matches != null) {
                for (var i = 0; i < matches.length; i++) {
                    var link = matches[0];
                    reLen = reLen - (link.length - 19) / 2;
                }
            }
            return Math.ceil(reLen);
        },
        //得到字符串的长度（英文0.5）
        strLen:function(str) {
            if (str == null || str.length == 0) {
                return 0;
            }
            var len = str.length;
            var reLen = 0;
            for (var i = 0; i < len; i++) {
                if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) {
                    // 全角
                    reLen += 1;
                } else {
                    reLen += 0.5;
                }
            }
            return Math.ceil(reLen);
        },

        subStr:function(str, length) {
            var a = str.match(/[^\x00-\xff]|\w{1,2}/g);
            return a.length < length ? str : a.slice(0, length).join("");
        },

        //输入字数验证(中文+字母)
        checkInputLength:function (num, contentId, numId) {
            var inputNum = this.getInputLength($("#" + contentId).val())
            inputNum = !inputNum ? 0 : inputNum;
            if (inputNum > num) {
                $("#" + numId).html("已超过<b style='color:red'>" + (Math.ceil(inputNum) - num) + "</b>字");
            } else {
                $("#" + numId).html("还可以输入<b>" + (num - Math.floor(inputNum)) + "</b>字");
            }
        },
        /**
         * 通过JSON返回对象中的status_code判断是否跳到登录页面
         *
         * @param jsonObj msg包含错误信息,reuslt[0]包含来源地址
         */
        locationLoginByJsonObj:function(jsonObj) {
            if (jsonObj == null || jsonObj.status_code == '-1') {
                window.location.href = "/loginpage?reurl=" + encodeURIComponent(window.location.href);
                return;
            }
        },
        /**
         * 验证域名规则:只允许英文、数字和横线
         * @param str
         */
        validateDomain:function(str) {
            var result = true;
            result = /^[\da-zA-Z-]+$/.test($.trim(str));
            return result;
        },
        //点击标签跳转
        clickTagFavorite:function(key) {
            window.location.href = '/search/content/' + encodeURIComponent(encodeURIComponent(key)) + '/?srid=a'
        },

        //生成图片地址
        genImgDomain:function (imgsrc, DOMAIN) {
            if (imgsrc == null || imgsrc.length == 0) {
                return joyconfig.URL_LIB + '//static/theme/default/img/default.jpg';
            }

            if (imgsrc.indexOf("http://") == 0) {
                return imgsrc;
            }

            if (imgsrc.indexOf("/r") || imgsrc.indexOf("\\r")) {
                var rxxx = imgsrc.substr(1, 4);
                //resource processor
                rxxx = this.getRxxx(rxxx);
//        var vr = imgsrc.substr(5);
                var vr = imgsrc;
                return "http://up" + rxxx.substring(1, rxxx.length) + "\." + DOMAIN + vr;
            }
        },
        //replace shutdown resource
        getRxxx:function (rxxx) {
            var downResource = eval('(' + joyconfig.shutDownRDomain + ')');
            $.each(downResource, function(i, val) {
                if (val.rd == rxxx) {
                    rxxx = val.reRd;
                    return false;
                }
            });
            return rxxx;
        },

        genImgBySuffix:function (imgpath, DOMAIN, suffix, usersex) {
            if (imgpath == null || imgpath.length == 0) {
                var picurl = joyconfig.URL_LIB + '/static/theme/default/img/head_is_s.jpg';
                if (usersex == '1') {
                    picurl = joyconfig.URL_LIB + '/static/theme/default/img/head_boy_s.jpg';
                } else if (usersex == '0') {
                    picurl = joyconfig.URL_LIB + '/static/theme/default/img/head_girl_s.jpg';
                }
                return picurl;
            }

            var url = this.genImgDomain(imgpath, DOMAIN);
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
        },
        genImgUrlBySuffix:function (imgUrl, suffix) {
            var exname = imgUrl.substring(imgUrl.lastIndexOf("."));
            var sNametou
            if (imgUrl.lastIndexOf("_") == -1) {
                sNametou = imgUrl.substring(0, imgUrl.lastIndexOf("."));
            } else {
                sNametou = imgUrl.substring(0, imgUrl.lastIndexOf("_"));
            }

            if (suffix != null && suffix.length != 0) {
                sNametou += '_' + suffix + exname;
            } else {
                sNametou += exname;
            }

            return sNametou;
        },

        parseMimg:function (imgpath, DOMAIN) {
            return this.genImgBySuffix(imgpath, DOMAIN, 'M');
        },

        parseSimg:function (imgpath, DOMAIN, usersex) {
            return this.genImgBySuffix(imgpath, DOMAIN, 'S', usersex);
        },

        parseSSimg:function (imgpath, DOMAIN) {
            return this.genImgBySuffix(imgpath, DOMAIN, 'SS');
        },

        parseBimg:function (imgpath, DOMAIN) {
            return this.genImgBySuffix(imgpath, DOMAIN, '');
        },

        joymeImgPrefix:function (src) {
            var joymeImgPrefix = /http:\/\/r\d\d\d\.joyme\.\w+(\/.*)/i;
            var match = joymeImgPrefix.exec(src);
            return match[1];
        },
        //截取文件名
        subfilename:function (imgname, l) {
            var splitarr = imgname.split(".");
            var typestr = "." + splitarr[splitarr.length - 1];
            var ps = imgname.indexOf(typestr);
            var namestr = imgname.substr(0, ps);
            if (namestr.length > l) {
                return namestr.substr(0, l - 1) + "..." + namestr.charAt(namestr.length - 1) + typestr;
            }
            return imgname;
        },

        increaseCount: function (numId, numCount) {
            var spanItem = $('#' + numId);
            $.each(spanItem, function(i, val) {
                val.innerHTML = (parseInt(val.innerHTML.replace(/[^\d]/g, '')) + numCount);
//                var mum = parseInt(val.textContent);
//                if (!isNaN(mum)) {
//
//                }
            });

        },
        increaseCountByDom: function (dom, numCount) {
            var dom = dom;
            if (dom.length == 0) {
                return;
            }
            var orgNum = 0;
            var numStr = dom[0].innerHTML.replace(/[^\d]/g, '');
            if (numStr != null && numStr.length > 0) {
                orgNum = parseInt(numStr);
            }

            dom.text(orgNum + numCount);
        },

        coinFormat:function(coinNum) {
            if (coinNum <= 9999) {
                return '积分：' + coinNum;
            } else if (coinNum >= 99999999) {
                return '积分：9999万';
            } else {
                coinNum = parseInt(coinNum / 10000);
                return '积分：' + coinNum + "万";
            }
        } ,
        getTimes:function (jqObj) {
            var text = jqObj.text();
            var matches = text.match(/\((\d+)\)/);
            if (matches != null) {
                var times = parseInt(matches[1]);
                return times == NaN ? 0 : times;
            }
        },
        isValidDate:function (DateStr) {
            var str = common.convDate(DateStr, "-");
            if ((str.length != 8) || !common.checkNumber(str))
                return false;
            var year = str.substring(0, 4);
            var month = str.substring(4, 6);
            var day = str.substring(6, 8);
            var dayOfMonth = new Array(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
            if ((month < 1) || (month > 12))
                return false;
            if ((day < 1) || (day > dayOfMonth[month - 1]))
                return false;
            if (!common.checkLeapYear(year) && (month == 2) && (day == 29))
                return false;
            return true;
        },
        convDate:function (sDate, sSep) {
            var pos = 0;
            var str = sDate;
            var len = str.length;
            if ((len < 8) || (len > 10)) {
                return str;
            }
            else if (str.indexOf(sSep) == 4) {
                pos = str.indexOf(sSep, 5);
                if (pos == 6) {
                    if (len == 8) {
                        return str.substring(0, 4) + "0" + str.substring(5, 6) + "0" + str.substring(7, 8);
                    }
                    else {
                        return str.substring(0, 4) + "0" + str.substring(5, 6) + str.substring(7, 9);
                    }
                }
                else if (pos == 7) {
                    if (len == 9) {
                        return str.substring(0, 4) + str.substring(5, 7) + "0" + str.substring(8, 9);
                    }
                    else {
                        return str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10);
                    }
                }
                else {
                    return str;
                }
            }
            else {
                return str;
            }
        },
        checkNumber:function (str) {
            var i;
            var len = str.length;
            var chkStr = "1234567890";
            if (len == 1) {
                if (chkStr.indexOf(str.charAt(i)) < 0) {
                    return false;
                }
            } else {
                if ((chkStr.indexOf(str.charAt(0)) < 0)) {
                    return false;
                }
                for (i = 1; i < len; i++) {
                    if (chkStr.indexOf(str.charAt(i)) < 0) {
                        return false;
                    }
                }
            }
            return true;
        },
        checkLeapYear:function (year) {
            if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                return true;
            }
            return false;
        },

        comptime:function(beginTime, endTime) {  //YYYY-MM-DD HH:mm:ss
            if (beginTime > endTime) {
                return -1;
            } else if (beginTime == endTime) {
                return 0;
            } else {
                return 1;
            }
        },

        date2str:function(d) {
            var ret = d.getFullYear() + "-"
            ret += ("00" + (d.getMonth() + 1)).slice(-2) + "-"
            ret += ("00" + d.getDate()).slice(-2) + " "
            ret += ("00" + d.getHours()).slice(-2) + ":"
            ret += ("00" + d.getMinutes()).slice(-2) + ":"
            ret += ("00" + d.getSeconds()).slice(-2)
            return   ret;
        },

//为textarea定位
        cursorPosition : {
            get: function (textarea) {
                var rangeData = {text: "", start: 0, end: 0 };

                if (textarea.setSelectionRange) { // W3C
                    textarea.focus();
                    textarea.value = textarea.value + '';
                    rangeData.start = textarea.selectionStart;
                    rangeData.end = textarea.selectionEnd;
                    rangeData.text = (rangeData.start != rangeData.end) ? textarea.value.substring(rangeData.start, rangeData.end) : "";
                } else if (document.selection) { // IE
                    textarea.focus();
                    textarea.value = textarea.value + '';
                    var i,
                            oS = document.selection.createRange(),
                        // Don't: oR = textarea.createTextRange()
                            oR = document.body.createTextRange();
                    oR.moveToElementText(textarea);

                    rangeData.text = oS.text;
                    rangeData.bookmark = oS.getBookmark();

                    // object.moveStart(sUnit [, iCount])
                    // Return Value: Integer that returns the number of units moved.
                    for (i = 0; oR.compareEndPoints('StartToStart', oS) < 0 && oS.moveStart("character", -1) !== 0; i ++) {
                        // Why? You can alert(textarea.value.length)
                        if (textarea.value.charAt(i) == '\r') {
                            i ++;
                        }
                    }
                    rangeData.start = i;
                    rangeData.end = rangeData.text.length + rangeData.start;
                }

                return rangeData;
            },
            set: function (textarea, rangeData) {
                var oR, start, end;
                textarea.focus();
                if (!rangeData) {
                    alert("You must get cursor position first.")
                }
                if (textarea.setSelectionRange) { // W3C
                    textarea.setSelectionRange(rangeData.start, rangeData.end);
                } else if (textarea.createTextRange) { // IE
                    oR = textarea.createTextRange();

                    // Fixbug : ues moveToBookmark()
                    // In IE, if cursor position at the end of textarea, the set function don't work
                    if (textarea.value.length === rangeData.start) {
                        //alert('hello')
                        oR.collapse(false);
                        oR.select();
                    } else {
                        oR.moveToBookmark(rangeData.bookmark);
                        oR.select();
                    }
                }
            },
            add: function (textarea, rangeData, text) {
                var oValue, nValue, oR, sR, nStart, nEnd, st;
                this.set(textarea, rangeData);

                if (textarea.setSelectionRange) { // W3C
                    oValue = textarea.value;
                    nValue = oValue.substring(0, rangeData.start) + text + oValue.substring(rangeData.end);
                    nStart = nEnd = rangeData.start + text.length;
                    st = textarea.scrollTop;
                    textarea.value = nValue;
                    // Fixbug:
                    // After textarea.values = nValue, scrollTop value to 0
                    if (textarea.scrollTop != st) {
                        textarea.scrollTop = st;
                    }
                    textarea.setSelectionRange(nStart, nEnd);
                } else if (textarea.createTextRange) { // IE
                    sR = document.selection.createRange();
                    sR.text = text;
                    sR.setEndPoint('StartToEnd', sR);
                    sR.select();
                }
            },
            _setCurPos: function(index, elem) {
                elem.focus();
                if (elem.setSelectionRange) {
                    elem.setSelectionRange(index, index);
                } else if (elem.createTextRange) {
                    var range = elem.createTextRange();
                    range.collapse(true);
                    range.moveEnd('character', index);
//                    range.moveStart('character', index);
                    range.select();
                }
                elem.focus();
            },
            focusTextarea:function(dom) {
                if (dom == null) {
                    return;
                }
                try {
                    dom.focus()
                } catch(e) {
                }
                var len = dom.value.length;
                if (document.selection) {
                    var sel = dom.createTextRange();
                    sel.moveStart('character', len);
                    sel.collapse();
                    sel.select();
                } else if (typeof  dom.selectionStart == 'number' && typeof  dom.selectionEnd == 'number') {
                    dom.selectionStart = dom.selectionEnd = len;
                }
            }
        } ,
//过滤通配符
        trimTag: function (str) {
            str = $.trim(str);
            return str.replace(/[%+'+\\+*+\/+_+(+)+"]/g, "");
            return str;
        } ,
        trimStr:function (str) {
            return str.replace(/\s+/g, "");
        },
        //获取url
        /*
         * var Request = new Object();
         Request = GetRequest();
         var 参数1,参数2,参数3,参数N;
         参数1 = Request['参数1'];
         参数2 = Request['参数2'];
         参数3 = Request['参数3'];
         参数N = Request['参数N'];
         * */
        GetRequest:function () {
            var url = location.search; //获取url中"?"符后的字串
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for (var i = 0; i < strs.length; i ++) {
                    theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
                }
            }
            return theRequest;
        },
        clipToClipboard:function (txt) {
            if (window.clipboardData) {
                window.clipboardData.setData("text", txt);
                return true;
            } else if (window.netscape) {
                if (!clipByFFClipboard(txt)) {
//                    alert("您的浏览器不支持粘贴，请按Ctrl+c 将信息粘贴到您指定的位置。");
                    return false;
                } else {
                    return true;
                }
            } else {
//                alert("您的浏览器不支持粘贴，请按Ctrl+c 将信息粘贴到您指定的位置。");
                return false;
            }
        },

        getFFClipboardText:function () {
            try {
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
            } catch (e) {
                return false;
//        alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");
            }
            var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
            if (!clip)
                return;
            var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
            if (!trans)
                return;
            trans.addDataFlavor('text/unicode');
            clip.getData(trans, clip.kGlobalClipboard);
            var str = new Object();
            var len = new Object();
            try {
                trans.getTransferData('text/unicode', str, len);
            }
            catch(error) {
                return false;
            }
            if (str) {
                if (Components.interfaces.nsISupportsWString) str = str.value.QueryInterface(Components.interfaces.nsISupportsWString);
                else if (Components.interfaces.nsISupportsString) str = str.value.QueryInterface(Components.interfaces.nsISupportsString);
                else str = null;
            }
            if (str) {
                return str.data.substring(0, len.value / 2);
            }
        },
        regImgNum:function (editor) {
            var editorValue = editor.getData();
            //将数量反解析成blogContent
            if (editorValue.length == 0) {
                return 0;
            } else {
                blogContent.image = new Array();
                //正则得到编辑器中所有的图片元素的数量
                var imgRegex = /<img\s+joymed=\"([^>]*)\"\s+joymet=\"img\"\s+joymeu=\"([^>]+)\"\s+(?:style="[^>"]*"\s+)?src=\"([^>]+)\"\s*(\/>)/g;
                var matches = editorValue.match(imgRegex);
                if (matches != null) {
                    for (var i = 0; i < matches.length; i++) {
                        var imgHtml = matches[i];
                        var parternElement = new RegExp(imgRegex.source);
                        var joymeImgEle = parternElement.exec(imgHtml);
                        blogContent.image.push({key:i,value:{url:joymeImgEle[2],src:joymeImgEle[3],desc:''}});
                    }
                }
                return blogContent.image.length;
            }
        },
        getMood : function () {
            var moodObject = new Object();
            moodObject.pansite = {};
            moodObject.smiley = {};
            moodObject.erdaimu = {};
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/6.gif"] = "[P大笑]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/52.gif"] = "[P愤怒]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/106.gif"] = "[P微笑]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/98.gif"] = "[P喜欢]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/104.gif"] = "[P脸红]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/39.gif"] = "[P挖鼻孔]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/31.gif"] = "[P生气]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/94.gif"] = "[P难过]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/97.gif"] = "[P无语]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/4.gif"] = "[P囧]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/47.gif"] = "[P酷]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/103.gif"] = "[P不爽]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/96.gif"] = "[P吐血]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/93.gif"] = "[P尴尬]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/99.gif"] = "[P专家]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/2.gif"] = "[P偷笑]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/18.gif"] = "[P红心]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/9.gif"] = "[P心碎]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/16.gif"] = "[P玫瑰]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/22.gif"] = "[P菜刀]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/24.gif"] = "[P顶]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/13.gif"] = "[P晕]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/10.gif"] = "[P怒]";
//            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/23.gif"] = "[P鄙视]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/42.gif"] = "[P哈哈]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/7.gif"] = "[P委屈]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/46.gif"] = "[P向往]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/33.gif"] = "[P美味]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/32.gif"] = "[P财迷]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/20.gif"] = "[P害羞]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/14.gif"] = "[P汗]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/15.gif"] = "[P爆发]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/65.gif"] = "[P惨]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/5.gif"] = "[P大哭]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/45.gif"] = "[P衰]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/29.gif"] = "[P阴险]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/105.gif"] = "[P吐舌]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/102.gif"] = "[P哼]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/35.gif"] = "[P吃惊]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/100.gif"] = "[P萌]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/101.gif"] = "[P兔子]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/1.gif"] = "[P笑眯眯]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/26.gif"] = "[P困]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/27.gif"] = "[P听不懂]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/28.gif"] = "[P吐]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/21.gif"] = "[P鬼脸]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/50.gif"] = "[P感谢]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/12.gif"] = "[P愁]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/58.gif"] = "[P雷]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/60.gif"] = "[P思考]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/63.gif"] = "[P石化]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/66.gif"] = "[P怨]";
            moodObject.pansite[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/pansite/67.gif"] = "[P宅]";
            //新表情
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/6.gif"] = "[大笑]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/52.gif"] = "[愤怒]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/106.gif"] = "[可爱]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/98.gif"] = "[喜欢]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/103.gif"] = "[不爽]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/93.gif"] = "[尴尬]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/2.gif"] = "[偷笑]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/94.gif"] = "[难过]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/97.gif"] = "[闭嘴]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/24.gif"] = "[顶]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/47.gif"] = "[酷]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/18.gif"] = "[红心]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/9.gif"] = "[心碎]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/2.gif"] = "[菜刀]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/23.gif"] = "[鄙视]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/7.gif"] = "[委屈]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/46.gif"] = "[向往]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/32.gif"] = "[财迷]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/20.gif"] = "[害羞]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/15.gif"] = "[爆发]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/5.gif"] = "[大哭]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/45.gif"] = "[衰]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/11.gif"] = "[哼]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/35.gif"] = "[吃惊]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/1.gif"] = "[笑眯眯]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/27.gif"] = "[听不懂]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/21.gif"] = "[鬼脸]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/12.gif"] = "[愁]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/58.gif"] = "[雷]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/53.gif"] = "[思考]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/104.gif"] = "[发呆]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/105.gif"] = "[一会就来]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/106_0.gif"] = "[加油]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/107.gif"] = "[眨眼]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/108.gif"] = "[不好]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/109.gif"] = "[好的]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/110.gif"] = "[再见]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/111.gif"] = "[礼物]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/112.gif"] = "[手机]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/113.gif"] = "[饮料]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/114.gif"] = "[红酒]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/115.gif"] = "[胜利]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/116.gif"] = "[鼓掌]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/117.gif"] = "[阳光]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/118.gif"] = "[雨天]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/119.gif"] = "[音乐]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/120.gif"] = "[晚安]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/121.gif"] = "[节日]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/122.gif"] = "[彩虹]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/123.gif"] = "[握手]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/124.gif"] = "[星星]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/125.gif"] = "[休息一下]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/126.gif"] = "[蛋糕]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/127.gif"] = "[慢]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/128.gif"] = "[牛粪]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/down.gif"] = "[下载]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/mic.gif"] = "[请发言]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/professor.gif"] = "[男嘉宾]";
            moodObject.smiley[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/smiley/women.gif"] = "[女嘉宾]";

            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/1.gif"] = "[p2hi]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/2.gif"] = "[p2ok]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/3.gif"] = "[p2sayno]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/4.gif"] = "[p2被插]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/5.gif"] = "[p2鼻涕]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/6.gif"] = "[p2抽泣]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/7.gif"] = "[p2达利]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/8.gif"] = "[p2打滚]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/9.gif"] = "[p2打脸]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/10.gif"] = "[p2大哭]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/11.gif"] = "[p2呃]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/12.gif"] = "[p2感动眼]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/13.gif"] = "[p2嗨森嗨森]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/14.gif"] = "[p2话说]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/15.gif"] = "[p2奸笑]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/16.gif"] = "[p2娇气]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/17.gif"] = "[p2冷场]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/18.gif"] = "[p2扭捏]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/19.gif"] = "[p2飘过]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/20.gif"] = "[p2撒花]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/21.gif"] = "[p2少来]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/22.gif"] = "[p2睡觉]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/23.gif"] = "[p2送你鬼脸]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/24.gif"] = "[p2他怎么想]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/25.gif"] = "[p2通体汗]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/26.gif"] = "[p2偷笑]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/27.gif"] = "[p2吐血]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/28.gif"] = "[p2我潜水]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/29.gif"] = "[p2无视]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/30.gif"] = "[p2星星眼]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/31.gif"] = "[p2有种打我]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/32.gif"] = "[p2仔细看]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/33.gif"] = "[p2这个呐]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/34.gif"] = "[p2中枪]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/35.gif"] = "[p2祝贺]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/36.gif"] = "[p2假发]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/37.gif"] = "[p2新八]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/38.gif"] = "[p2银桑]";
            moodObject.erdaimu[joyconfig.URL_LIB + "/hotdeploy/static/img/mood/erdaimu/39.gif"] = "[p2神乐]";
            return moodObject;
        },
        //数组去重
        delRepeat : function(array) {
            var newArray = [];
            var provisionalTable = {};
            for (var i = 0, item; (item = array[i]) != null; i++) {
                if (!provisionalTable[item]) {
                    newArray.push(item);
                    provisionalTable[item] = true;
                }
            }
            return newArray;
        },
        //返回joymeu的方法
        joymeImgPrefix:function (src) {
            var joymeImgPrefix = /http:\/\/r\d\d\d\.joyme\.\w+(\/.*)/i;
            var match = joymeImgPrefix.exec(src);
            return match[1];
        } ,getBlogContent:function (str) {
            str = str.replace(/[ | ]*\n/g, ''); //去除行尾空白
            str = str.replace(/&nbsp;/g, '');//去掉&nbsp;
            str = str.replace(/<br\s+\/>|<BR\s+\/>|<br\s+>/g, '');//去掉br
            str = $.trim(str);
            return str;
        },
        joymeCtrlEnter:function (event, fun) {
            var keyNum = event.which;
            if (keyNum == 17) {
                joyconfig.ctrlFlag = true;
            }
            if ((joyconfig.ctrlFlag || event.ctrlKey && joyconfig.ctrlFlag) && keyNum == 13) {
                if (typeof fun == "object") {
                    fun.click();
                } else {
                    fun();
                }
                joyconfig.ctrlFlag = false;
            }
            setTimeout(function() {
                if (keyNum != 17) {
                    joyconfig.ctrlFlag = false;
                }
            }, 50);
        },
        isCurrentDay:function(year, month, day) {
            var now = new Date();
            var nowYear = now.getFullYear();
            var nowMonth = (now.getMonth() + 1);
            nowMonth = nowMonth.toString().length == 1 ? '0' + nowMonth : nowMonth;
            var nowDay = now.getDate();
            return nowYear == year && nowMonth == month && nowDay == day;
        },
        isCurrentYear:function(year) {
            var now = new Date();
            var nowYear = now.getFullYear();
            return nowYear == year;
        },
        joymeShake:function(ele, cls, times) {//闪烁元素,替换样式,次数
            ele.removeClass('white');
            var i = 0,t = false ,o = ele.attr("class") + " ",c = "",times = times || 2;
            if (t) {
                ele.removeClass('white');
                return;
            } else {
                t = setInterval(function() {
                    i++;
                    c = i % 2 ? o + 'red' : o + 'white';
                    ele.attr("class", c);
                    if (i == 2 * times) {
                        clearInterval(t);
                        ele.removeClass('red');
                    } else {
                        ele.removeClass('white');
                    }
                }, 150);
            }
        },
        Browser:{
            isMozilla :(typeof document.implementation != 'undefined') &&
                    (typeof document.implementation.createDocument != 'undefined') &&
                    (typeof HTMLDocument != 'undefined'),
            isIE :window.ActiveXObject ? true : false,
            isFirefox :(navigator.userAgent.toLowerCase().indexOf("firefox") != -1),
            isSafari:(navigator.userAgent.toLowerCase().indexOf("safari") != -1),
            isOpera : (navigator.userAgent.toLowerCase().indexOf("opera") != -1)
        },
        obj2Param: function (param, key) {
            var paramStr = "";
            if (param instanceof String || param instanceof Number || param instanceof Boolean) {
                paramStr += "&" + key + "=" + encodeURIComponent(param == "" ? null : param);
            } else {
                $.each(param, function(i) {
                    var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                    paramStr += '&' + parseParam(this, k);
                });
            }
            return paramStr.substr(1);
        },
        obj2Str:function(obj) {
            switch (typeof(obj)) {
                case 'object':
                    var ret = [];
                    if (obj instanceof Array) {
                        for (var i = 0, len = obj.length; i < len; i++) {
                            ret.push(this.obj2Str(obj[i]));
                        }
                        return '[' + ret.join(',') + ']';
                    }
                    else if (obj instanceof RegExp) {
                        return obj.toString();
                    }
                    else {
                        for (var a in obj) {
                            ret.push(a + ':' + this.obj2Str(obj[a]));
                        }
                        return '{' + ret.join(',') + '}';
                    }
                case 'function':
                    return 'function() {}';
                case 'number':
                    return obj.toString();
                case 'string':
                    return "\"" + obj.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function(a) {
                        return ("\n" == a) ? "\\n" : ("\r" == a) ? "\\r" : ("\t" == a) ? "\\t" : "";
                    }) + "\"";
                case 'boolean':
                    return obj.toString();
                default:
                    return obj.toString();
            }
        },
        joymeDate:function(date) {
            var dateArray = date.split('-');
            var year = dateArray[0]
            var month = dateArray[1]
            var dayArray = dateArray[2].split(' ');
            var day = dayArray[0];
            var hhmm = dayArray[1];
            if (common.isCurrentDay(year, month, day)) {
                return '今天 ' + hhmm;
            } else {
                if (common.isCurrentYear(year)) {
                    return  month + "月" + day + "日";
                }

                return year + "-" + month + "-" + day + "-";
            }
        },
        joymeShortDate:function(date) {
            var dateArray = date.split('-');
            var year = dateArray[0]
            var month = dateArray[1]
            var dayArray = dateArray[2].split(' ');
            var day = dayArray[0];
            var hhmm = dayArray[1];
            if (common.isCurrentDay(year, month, day)) {
                return hhmm;
            } else {

                return  month + "-" + day;
            }
        },
        jmPostConfirm:function(confirmText) {
            window.onbeforeunload = function() {
                try {
                    var chatText = $('#chat_content').val();
                    var textText = CKEDITOR.instances['text_content'] != null ? CKEDITOR.instances['text_content'].getData() : '';
                    var textSubject = $("#blogSubject").val();

                    var contentEmpty = true;
                    if (chatText != null && chatText.length > 0) {
                        contentEmpty = false;
                    } else if (textText != null && textText.length > 0) {
                        contentEmpty = false;
                    } else if (textSubject != null && textSubject.length > 0 && textSubject != '给你的文章加个标题吧') {
                        contentEmpty = false;
                    } else if (blogContent != null) {
                        if (blogContent.image != null && blogContent.image.length > 0) {
                            contentEmpty = false;
                        } else if (blogContent.video != null) {
                            contentEmpty = false;
                        } else if (blogContent.audio != null) {
                            contentEmpty = false;
                        } else if (blogContent.ios != null) {
                            contentEmpty = false;
                        }
                    }

                    confirmText = (confirmText != null && confirmText.length > 0) ? confirmText : '未发布的内容将会丢失'
                    if (!contentEmpty) {
                        return confirmText;
                    }
                } catch(e) {
                }
            };
        },
        contentValidate:{
            textHasVideo:function(content) {
                var videoRegex = /<img[^>]+joymet=\"video\"[^>]+>/g;
                return  videoRegex.test(content);
            },
            textHasImage:function(content) {
                var imgRegex = /<img[^>]+joymet=\"img\"[^>]+>/g;
                return imgRegex.test(content);
            }
        },

        setOverflowY :{
            show:function() {
                var temp_h1 = document.body.clientHeight;
                var temp_h2 = document.documentElement.clientHeight;
                var isXhtml = (temp_h2 <= temp_h1 && temp_h2 != 0) ? true : false;
                var htmlbody = isXhtml ? document.documentElement : document.body;
                if (htmlbody.style.overflowY == 'scroll') {
                    return;
                }

                htmlbody.style.overflowY = "scroll";
                $("body").css({"position":"static","overflow-x":'visible'});
                var afterBodyTop = $("body").data('scrolltopLater') || 0;
                $.browser.version == "7.0" ? $("body").scrollTop(afterBodyTop) : $(document).scrollTop(afterBodyTop);
            },
            hide :function() {
                var temp_h1 = document.body.clientHeight;
                var temp_h2 = document.documentElement.clientHeight;
                var isXhtml = (temp_h2 <= temp_h1 && temp_h2 != 0) ? true : false;
                var htmlbody = isXhtml ? document.documentElement : document.body;
                if (htmlbody.style.overflowY == 'hidden') {
                    return;
                }
                var scrollTop = document.documentElement.scrollTop || window.pageYOffset;
                htmlbody.style.overflow = "hidden";
                var widthOfMasklong = 0;
                if ($("body").data('widthOfMasklong')) {
                    widthOfMasklong = $("body").data('widthOfMasklong') - 17;
                } else {
                    $("body").data('widthOfMasklong', $("body").width());
                    widthOfMasklong = $("body").width() - 17;
                }
                if (navigator.userAgent.toLowerCase().indexOf("chrome") == -1) {
                    $("body").css({"position":"fixed",'top': '-' + scrollTop + 'px',width:widthOfMasklong + "px","background-attachment":"fixed"});
                }
                $("body").data('scrolltopLater', scrollTop);

            }
        }
    }

    function clipByFFClipboard(txt) {
        try {
            netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
        } catch (e) {
//            alert("您的浏览器不支持粘贴，请按Ctrl+c 将邀请链接粘贴到您指定的位置。");
            return false;
        }
        var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);
        if (!clip) {
            return false;
        }

        var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);
        if (!trans) {
            return false;
        }
        trans.addDataFlavor('text/unicode');
        var str = new Object();
        var len = new Object();
        var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);
        var copytext = txt;
        str.data = copytext;
        trans.setTransferData("text/unicode", str, copytext.length * 2);
        var clipid = Components.interfaces.nsIClipboard;
        if (!clip) {
            return false;
        }
        clip.setData(trans, null, clipid.kGlobalClipboard);
        return true;
    }

    (function() {
        function log(s) {
            return;
            if (window.console)
                console.log(s);
        }

        var OLD_IE = !window.getSelection,IE = window.ActiveXObject;

        /**
         * author:yiminghe@gmail.com
         * @refer:http://yiminghe.javaeye.com/blog/508999
         * @param textarea
         */
        function TextareaEditor(textarea) {
            if (textarea != null) {
                this.textarea = textarea;
                if (IE) {
                    var savedRange;
                    textarea.onmousedown
                        //onfocus 也要存储，如果直接外部设置selection，则也要讲设置后产生的range存起来
                            = textarea.onfocus
                            = textarea.onmouseup
                            = textarea.onkeydown
                            = textarea.onkeyup
                            = function() {
                        var r = document.selection.createRange();
                        //当从 console 过来点击页面时，textarea focus 事件被触发但是范围却不是textarea！
                        if (r.parentElement() == textarea) savedRange = r;
                        log("savedRange : " + event.type + " : " + r.parentElement().nodeName);
                    };
                    textarea.onfocusin = function() {
                        var r = document.selection.createRange();
                        log("onfocusin" + " : " + r.parentElement().nodeName);
                        //log(document.activeElement.outerHTML);;
                        savedRange && savedRange.select();
                    };
                    textarea.onblur = function() {
                        log("blur");
                    };

                    textarea.onfocusout = function() {
                        log("onfocusout");
                        return;
                        savedRange = document.selection.createRange();
                        log("focusout " + " : " + savedRange.parentElement().outerHTML);
                        log(document.activeElement.outerHTML);
                    }
                }
            }
        }


        TextareaEditor.prototype = {
            constructor:TextareaEditor,
            getSelection:OLD_IE ? function() {
                var textarea = this.textarea;
                textarea.focus();
                var pos = {},
                        i,
                        range = document.selection.createRange();
                //parentElement : 获取给定文本范围的父元素。
                if (textarea != range.parentElement()) {
                    log(range.parentElement().outerHTML);
                    return;
                }
                // create a selection of the whole textarea
                var range_all = document.body.createTextRange();
                //moveToElementText	Moves the text range so that
                //the start and end positions of the range encompass the text in the given element.
                range_all.moveToElementText(textarea);
                //alert(range_all.text.length);
                //alert(textarea.value.length);
                // calculate selection start point by moving beginning of range_all to beginning of range
                //看这 http://msdn.microsoft.com/en-us/library/ms536373%28VS.85%29.aspx
                // Compare the start of the TextRange object with the start of the oRange parameter.
                //
                //StartToEnd
                //   Compare the start of the TextRange object with the end of the oRange parameter.
                //StartToStart
                //   Compare the start of the TextRange object with the start of the oRange parameter.
                //EndToStart
                //   Compare the end of the TextRange object with the start of the oRange parameter.
                //EndToEnd
                //   Compare the end of the TextRange object with the end of the oRange parameter.
                //
                //-1
                //   The end point of the object is further to the left than the end point of oRange.
                //0
                //   The end point of the object is at the same location as the end point of oRange.
                //1
                //   The end point of the object is further to the right than the end point of oRange.
                for (var sel_start = 0;
                     range_all.compareEndPoints('StartToStart', range) < 0;
                     sel_start++) {
                    //每次越过了 \r\n，text.value里 \r\n 算两个
                    range_all.moveStart('character', 1);
                }
                //debugger
                //alert(sel_start);
                //alert(textarea.value.substring(0,sel_start));
                // get number of line breaks from textarea start to selection start and add them to sel_start
                for (i = 0;
                     i <= sel_start;
                     i++) {
                    if (textarea.value.charAt(i) == '\n') {
                        sel_start++;
                    }
                }
                pos.selectionStart = sel_start;
                // create a selection of the whole textarea
                range_all = document.body.createTextRange();
                range_all.moveToElementText(textarea);
                // calculate selection end point by moving beginning of range_all to end of range
                var flag = 0;
                for (var sel_end = 0;
                     (flag = range_all.compareEndPoints('StartToEnd', range)) < 0;
                     sel_end++) {

                    if (textarea.value.charAt(sel_end) == '\n') {
                        sel_end++;
                    }
                    range_all.moveStart('character', 1);
                }
                //光标不可能停在\r,\n之间
                if (textarea.value.charAt(sel_end) == '\n') {
                    sel_end++;
                }
                pos.selectionEnd = sel_end;
                // get selected and surrounding text
                return pos;
            } : function() {
                var textarea = this.textarea;
                textarea.focus();
                return {
                    selectionStart:textarea.selectionStart,
                    selectionEnd:textarea.selectionEnd
                };
            },
            setSelectionRange:OLD_IE ? function(start, end) {
                log("setSelectionRange start");
                var v = this.textarea.value,range = this.textarea.createTextRange();
                range.collapse(true);
                start = getLengthForRange(v, start);
                end = getLengthForRange(v, end);
                range.moveEnd("character", end);
                range.moveStart("character", start);
                //select 附带 focus 效果哦
                //内部应该是：
                //1.focus
                //2.select
                range.select();
                log("setSelectionRange end");
            } : function(start, end) {
                this.textarea.setSelectionRange(start, end);
                this.textarea.focus();
            },
            insertData :OLD_IE ? function(text) {
                var textarea = this.textarea;
                //textarea.focus();
                var range = document.selection.createRange();
                range.text = text;
            } : function(text) {
                if (window.navigator.userAgent.indexOf('MSIE 9.0') > 0) {
                    var textarea = this.textarea;
                    textarea.focus();
                    var range = document.selection.createRange();
                    range.text = text;
                    setTimeout(function() {
                        range.move('character', text.length);
                        range.select();
                        range.move('character', -text.length)
                        range.select();
                    }, 100);
                }
                else {
                    var textarea = this.textarea,
                            value = textarea.value;
                    textarea.focus();
                    var range = this.getSelection();
                    var start = value.substring(0, range.selectionStart);
                    var end = value.substring(range.selectionEnd, value.length);
                    var sl = textarea.scrollLeft,st = textarea.scrollTop;
                    textarea.value = start + text + end;
                    textarea.scrollLeft = sl;
                    textarea.scrollTop = st;
                    var np = start.length + text.length;
                    this.setSelectionRange(np, np);
                }
            }
        };
        function getLengthForRange(text, v) {
            return text.substring(0, v).replace(/\r\n/g, "\n").length;
        }

        window.TextareaEditor = TextareaEditor;
    })();
    return common;
});
