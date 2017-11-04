var IS_NULL     =	0X80; //	10000000
var IS_FULL     =	0X40; //	01000000
var IS_HALF     =	0X20; //	00100000
var IS_ASCII    =	0X10; //	00010000
var IS_NUM      =	0X08; //	00001000
var IS_DATE     =	0X04; //	00000100
var IS_PHONE    =	0X02; //	00000010
var IS_EMAIL    =	0X01; //	00000001
var IS_NOT_NULL =	0X00; //	00000000

var MSG_CHECK_SUCCESS = "Check Success";

function getAppVer() {
	var sVer = navigator.appVersion;
	var nVer = sVer.indexOf("MSIE");
	var appVer = "";
	if (nVer > 0) {
		appVer = "M" + sVer.substring(nVer + 5, nVer + 9);
	}
	else {
		appVer = "N" + sVer.substring(0, 4);
	}
	if (appVer.charAt(4) == " ") {
		appVer = appVer.substring(0, 4) + "0";
	}
	return appVer;
}

var appVer = getAppVer();

function checkSpaceChar(c) {
	return ((c == ' ') || (c == '\t') || (c == '　'));
}

function trim(str) {
	var len = str.length;
	var begin = 0;
	var end = len -1;
	var chkOne = true;
	var chkTwo = true;
   	if ((appVer.charAt(0) == 'M') || (appVer > "N4.03")) {
   		for (begin; (begin < len) && checkSpaceChar(str.charAt(begin)); begin++);
		for (end; (end >= 0) && checkSpaceChar(str.charAt(end)); end--);
	}
	else if (appVer.charAt(0) == 'N') {
		while (chkOne || chkTwo) {
			if (begin < len) {
				if (checkSpaceChar(str.charAt(begin))) {
					begin++;
					chkOne = true;
				}
				else {
					chkOne = false;
				}
			}
			else {
				chkOne = false;
			}
			if (begin < len - 1) {
				if (str.substring(begin, begin + 2) == "　") {
					begin += 2;
					chkTwo = true;
				}
				else {
					chkTwo = false;
				}
			}
			else {
				chkTwo = false;
			}
		}
		chkOne = true;
		chkTwo = true;
		while (chkOne || chkTwo) {
			if (end > -1) {
				if (checkSpaceChar(str.charAt(end))) {
					end--;
					chkOne = true;
				}
				else {
					chkOne = false;
				}
			}
			else {
				chkOne = false;
			}
			if (end > 0) {
				if (str.substring(end - 1, end + 1) == "　") {
					end -= 2;
					chkTwo = true;
				}
				else {
					chkTwo = false;
				}
			}
			else {
				chkTwo = false;
			}
		}
	}
	if (begin > end) {
		return "";
	}
	return str.substring(begin,end + 1);
}
function checkMZenKaKu(c) {
	var tmp = escape(c);
	if (tmp.length == 1) {
		return false;
	}
	return (tmp.charAt(1) == 'u');
}

function checkNZenKaKu(c) {
    var str = escape(c);
    if (str.charAt(0) != '%') {
        return false;
	}
    else if (str.charAt(1) == '8') {
        return true;
	}
    else if (str.charAt(1) == '9') {
        return true;
	}
    else if (str.charAt(1) == 'E') {
        return true;
	}
    else if (str.charAt(1) == 'F') {
        return true;
	}
    else{
        return false;
    }
}


function getLength(sCheck) {
   	if ((appVer.charAt(0) == 'M') || (appVer > "N4.05")) {
    	var n = 0;
    	var str = sCheck;
    	var len = str.length;
	for (var i = 0; i < len; i++) {
       	    n += checkMZenKaKu(str.charAt(i)) ? 2 : 1;
            // n += (escape(str.charAt(i)).charAt(1)=='u')?2:1
       	}
	return n;
   	}
   	else if (appVer.charAt(0) == 'N') {
	  	return sCheck.length;
    }
}

function checkFull(sCheck) {
    var i = 0;
    var str = sCheck;
    str = toHankaku(str);
    var len = str.length;
   	if ((appVer.charAt(0) == 'M') || (appVer > "N4.05")) {
	    for (i = 0; i < len; i++) {
       		if (!checkMZenKaKu(str.charAt(i))) {
       			return false;
       		}
    	}
   	} else if (appVer.charAt(0) == 'N') {
   		if ((len % 2) == 1) {
   			return false;
   		}
	    for (i = 0; i < len / 2; i++) {
       		if (!checkNZenKaKu(str.charAt(i * 2))) {
       			return false;
       		}
       	}
    }
  	return true;
}

function checkEngNum(str) {
	if( str == null || str == "" ){
		return true;
	}

	var c = new RegExp();
	c = /^[\d|a-zA-Z]+$/;
	if (c.test(str))
		return true;
	else
		return false;
}

function replaceStr(str, sFnd, sRep) {
	var sTmp = "";
   	var endIndex = 0;
  	var beginIndex = 0;
   	var len = sFnd.length;
  	while (endIndex >= 0) {
    	endIndex = str.indexOf(sFnd ,beginIndex);
    	if (endIndex >= 0) {
      		sTmp += str.substring(beginIndex, endIndex) + sRep;
       		beginIndex = endIndex + len;
    	}
    	else if (beginIndex >= 0) {
       		sTmp += str.substring(beginIndex);
      		break;
    	}
  	}
  	return sTmp;
}

function replaceCommas(str) {
	if (str == "") {
		return str;
	}
	str = replaceStr(str, "'", "'");
	str = replaceStr(str, '"', '"');
	return str;
}

function checkASCII(sCheck) {
	var str = escape(sCheck);
	var chkStr = "89abcdefABCDEF"
	var i = str.indexOf("%");
   	if ((appVer.charAt(0) == 'M') || (appVer > "N4.05")) {
		while ((i >= 0) && (i < str.length)) {
			if (str.charAt(i+1) == "u") {
				return false;
			}
			i = str.indexOf("%", i+1);
		}
   	}
   	else if (appVer.charAt(0) == 'N') {
		while ((i >= 0) && (i < str.length)) {
			if (chkStr.indexOf(str.charAt(i+1)) >= 0) {
				return false;
			}
			i = str.indexOf("%", i+1);
		}
    }
    return true;
}

//林波:校验句点"."
function checkdoc(str) {
    var i;
    var len = str.length;

    var dPos = str.indexOf(".");

        var chkStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_@.";

        if ((dPos == len - 1) || (str.charAt(len - 1) == ".")) {
                return false;
        }

//	while (aPos > dPos) {
//		dPos = str.indexOf(".", dPos + 1);
//		if (dPos < 0) {
//	Remove by SJNS zq in order to use mail like admin@mailhost
//			return false;
//
//		}
//	}
    for (i = 0; i < len; i++) {
            if (chkStr.indexOf(str.charAt(i)) < 0) {
                    return false;
            }
    }
        return true;
}


function checkNumber(str) {
    var i;
    var len = str.length;
    var chkStr = "1234567890";
    if (len == 1) {
	if (chkStr.indexOf(str.charAt(i)) < 0) {
	    return false;
	}
    } else {
	if ((chkStr.indexOf(str.charAt(0)) < 0) ) {
	    return false;
	}
	 for (i = 1; i < len; i++) {
		if (chkStr.indexOf(str.charAt(i)) < 0) {
			    return false;
		}
	}
    }
    return true;
}
//function checkNumbers(str) {
//    var i;
//    var len = str.length;
//    var chkStr = "1234567890";
//    if (len == 1) {
//	if (chkStr.indexOf(str.charAt(i)) < 0) {
//	    return false;
//	}
//    }
//	 for (i = 1; i < len; i++) {
//		if (chkStr.indexOf(str.charAt(i)) < 0) {
//			    return false;
//		}
//	}
//    }
//    return true;
//}

function checkNumberString(value)
{
	if( value == null || value == "" ){
		return true;
	}

	var c = new RegExp();
	c = /^\d+$/;
	if (!c.test(value))
		return false;
	else
		return true;
}


function formatDate(sYear, sMonth, sDay) {
	if (sMonth.length == 1) {
		sMonth = "0" + sMonth;
	}
	if (sDay.length == 1) {
		sDay = "0" + sDay;
	}
	return sYear + sMonth + sDay;
}

function convDate(sDate, sSep) {
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
}

function checkLeapYear(year) {
	if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
		return true;
	}
	return false;
}

function getSysDate() {
	var today  = new Date();
	var nYear  = today.getYear();
	var nMonth = today.getMonth() + 1;
	var nDay   = today.getDate();
	var sToday = "";
	if (nYear < 1000) {
		sToday += "" + (1900 + nYear);
	}
	else {
		sToday += nYear;
	}
	if (nMonth < 10) {
		sToday += "0" + nMonth;
	}
	else {
		sToday += nMonth;
	}
	if (nDay < 10) {
		sToday += "0" + nDay;
	}
	else {
		sToday += nDay;
	}
	return sToday;
}

function compareDateStr(dateStr1,dateStr2) {
  dateStr1 = convDate(dateStr1,"/");
  dateStr2 = convDate(dateStr2,"/");
  if (dateStr1 > dateStr2) {
    return 1;
  } else if (dateStr1 == dateStr2) {
    return 0;
  } else {
    return -1;
  }
  return dateStr1
}

function checkBeforeDate(str) {
	str = convDate(str, "/");
	if (str.length == 6) {
    	str += "01";
	}
	if (str >= getSysDate()) {
		return false;
	}
	return true;
}

function checkIsToday(str) {
	str = convDate(str, "/");
	if( str.length == 6 ) {
		str += "01";
	}
	if( str == getSysDate() ) {
		return true;
	}else {
		return false;
	}
}

function checkAfterDate(str) {
	str = convDate(str, "/");
	if (str.length == 6) {
    	str += "01";
	}
	if (str <= getSysDate()) {
		return false;
	}
	return true;
}

function checkDate(str) {
	str = convDate(str, "/");
	if ((str.length != 8) || !checkNumber(str))
		return false;
	var year  = str.substring(0, 4);
	var month = str.substring(4, 6);
	var day   = str.substring(6, 8);
	dayOfMonth = new Array(31,29,31,30,31,30,31,31,30,31,30,31);
	if ((month < 1) || (month > 12))
		return false;
	if ((day < 1) || (day > dayOfMonth[month - 1]))
		return false;
	if (!checkLeapYear(year) && (month == 2) && (day == 29))
		return false;
	return true;
}
//比较两个时间
//If fromDate <= toDate : return true ; else : return false
function compareDate(fromDate,toDate) {
	if( checkDate(fromDate) != true ) return false;
	if( checkDate(toDate) != true ) return false;
	fromDate = convDate(fromDate, "/");
	toDate = convDate(toDate, "/");
	if ((fromDate.length != 8) || !checkNumber(fromDate) || (toDate.length != 8) || !checkNumber(toDate))
		return false;
	if(fromDate <= toDate ){
		return true;
	}else{
		return false;
	}
}

function removeChar(str, c) {
	if( str == null || str == "" )
		return str;
	var i = str.indexOf(c);
	while (i >= 0) {
		str = str.substring(0, i) + str.substring(i + 1, str.length);
		i = str.indexOf(c);
	}
	return str;
}

function formatLargeNumber(str) {
	if( str == null || !checkNumber(str) )
		return str;
	var i = str.length;
	var output = "";

	while( i > 0 ) {
		if( i < 3 ) {
			output = str.substring(0, i) + output ;
			break;
		}else {
			i = i - 3;
			output = str.substring(i, i + 3) + output ;
			if( i > 0 ) {
				output = "," + output;
			}
		}
	}
	return output;
}

function checkPhone(str) {
	var i = str.indexOf("--");
	var len = str.length;
	if (i >= 0) {
		return false;
	}
	i = str.indexOf("-");
	if ((i == 0) || (i == len - 1)) {
		return false;
	}
	else if (i > 0) {
		i = str.lastIndexOf("-");
       	if (i == len - 1) {
              	return false;
		}
		str = removeChar(str, "-");
	}
	if (!checkNumber(str)) {
		return false;
	}
	else {
		return true;
	}
}

function checkEmail(str) {
    var i;
    var len = str.length;
    var aPos = str.indexOf("@");
    var dPos = str.indexOf(".");
    var aaPos = str.indexOf("@@");
    var adPos = str.indexOf("@.");
    var ddPos = str.indexOf("..");
    var daPos = str.indexOf(".@");
	var chkStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_@.";
	if ((aPos <= 0) || (aPos == len - 1) || (dPos <= 0) || (dPos == len - 1) ||	(adPos > 0) || (daPos > 0) ||
//      remove by SJNS/zq in order to use Mail at intranet like admin@mailhost, this mail address is ok.
	//if ((aPos <= 0) || (aPos == len - 1) || (dPos == len - 1) ||	(adPos > 0) || (daPos > 0) ||
		(str.charAt(len - 1) == "@") || (str.charAt(len - 1) == ".") || (aaPos > 0) || (ddPos > 0)) {
		return false;
	}
	if (str.indexOf("@", aPos + 1) > 0) {
		return false;
	}
	while (aPos > dPos) {
		dPos = str.indexOf(".", dPos + 1);
		if (dPos < 0) {
//	Remove by SJNS zq in order to use mail like admin@mailhost
			return false;

		}
	}
    for (i = 0; i < len; i++) {
    	if (chkStr.indexOf(str.charAt(i)) < 0) {
    		return false;
    	}
    }
	return true;
}

function checkItem(sCheck, nMinLen, nMaxLen, chkFlg) {
//var IS_NULL     =	0X80; //	10000000
//var IS_FULL     =	0X40; //	01000000
//var IS_HALF     =	0X20; //	00100000
//var IS_ASCII    =	0X10; //	00010000
//var IS_NUM      =	0X08; //	00001000
//var IS_DATE     =	0X04; //	00000100
//var IS_PHONE    =	0X02; //	00000010
//var IS_EMAIL    =	0X01; //	00000001
//var IS_NOT_NULL =	0X00; //	00000000
	var strLen = 0;

	if ((sCheck == null) || (sCheck == "")) {
		if ((chkFlg & 0x80) == 0x80) {
			return MSG_CHECK_SUCCESS;
		}
		else {
			return "是必须输入的项目！";
		}
	}
	if ((nMinLen > 0) || (nMaxLen > 0)) {
      	if ((appVer.charAt(0) == 'M') || (appVer > "N4.05")) {
//            strLen = sCheck.length;
 strLen = getLength(sCheck);
      	}else if (appVer.charAt(0) == 'N'){
            strLen = getLength(sCheck);
      	}
		if (nMinLen > 0) {
			if (nMinLen == nMaxLen) {
				if (strLen != nMinLen) {
					if ((chkFlg & 0x08) == 0x08) {
						return "的长度必须"+ nMinLen + "位！";
					}
					else {
						return "的长度必须"+ nMinLen + "位！";
					}
				}
			}
			else if (strLen < nMinLen) {
				if ((chkFlg & 0x08) == 0x08) {
					return "过于短小，最小长度为"+ nMinLen + "位！";
				}
				else {
					return "过于短小，最小长度为"+ nMinLen + "位！";
				}
			}
		}
		if (nMaxLen > 0) {
			if (strLen > nMaxLen) {
				// if ((chkFlg & 0x08) == 0x08) {
				// 	return "是数字项目";
				// }
				// else {
					return "超过了最大长度"+ nMaxLen + "位！";
				// }
			}
		}
	}


	if ((chkFlg & 0x10) == 0x10) {
		if (!checkASCII(sCheck)) {
			return "不正确！";
		}
	}

	if ((chkFlg & 0x08) == 0x08) {
		if (!checkNumber(sCheck)) {
			return "是数字项目！";
		}
	}

	if ((chkFlg & 0x04) == 0x04) {
		if (!checkDate(sCheck)) {
			return "是日期项目！";
		}
	}

	if ((chkFlg & 0x02) == 0x02) {
		if (!checkPhone(sCheck)) {
			return "不正确！";
		}
	}

	if ((chkFlg & 0x01) == 0x01) {
		if (!checkEmail(sCheck)) {
			return "不正确！";
		}
	}

	return MSG_CHECK_SUCCESS;
}

function check(sItemName, sCheck, nMinLen, nMaxLen, chkFlg) {
	sRet = checkItem(sCheck, nMinLen, nMaxLen, chkFlg);
	if (sRet != MSG_CHECK_SUCCESS) {
		sErr += sItemName + sRet + "\n";
		return false;
	}
	return true;
}

function trimItems(oFrm) {
	var i = 0;
	var type = "";
	for (i=0; i<oFrm.elements.length; i++) {
		type = oFrm.elements[i].type;
		if ((type == "text") || (type == "textarea")) {
			oFrm.elements[i].value = trim(oFrm.elements[i].value);
		}
	}
}

function getFileName(fullpath){
	var platform = navigator.platform;
	var fileseperator=(platform.indexOf("Win")==-1)?"/":"\\";
	var idx = fullpath.lastIndexOf(fileseperator);
	if(idx == -1){
		return fullpath;
	}
	else if(idx >= fullpath.length -1){
		return "";
	}
	else{
		return fullpath.substring(idx+1);
	}
}


///////////

function Alert(message, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"info.gif",message,title||"消息提示框"]);
}
function SucceedInfo(message, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"right.gif",message,title||"成功信息框"]);
}
function ErrorInfo(errorMessage, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"err.gif",errorMessage,title||"错误信息框"]);
}
function ConfirmInfo(message, width, height, title,func1,func2) {
	initPromptInfo([width,height,func1,func2,"ask.gif",message,title||"信息确认框",1]);
}
