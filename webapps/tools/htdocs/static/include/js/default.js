/////////////////////////////////////////////////////////////////////////
///////				Globe Define;			/////////
/////////////////////////////////////////////////////////////////////////
var lastmousex;
var lastmousey;
//var gs_siteurl = "www.html.com";
var curpopupwindow = null;

var isIE = (document.all ? true : false);

function setlastmouseposition(e) {
    if (navigator.appName.indexOf("Microsoft") != -1) {
        e = window.event;
    }
    lastmousex = e.screenX;
    lastmousey = e.screenY;
}

function closepopwin() {
    if (curpopupwindow != null) {
        if (!curpopupwindow.closed) {
            curpopupwindow.close();
        }
        curpopupwindow = null;
    }
}

function selectPopContact(sDest, sValue) {
    eval("window.opener." + sDest).value = sValue;
    window.close();
}
//Check functions;
///////////////////////////////////////////////////////////////////////////////////////
//
/*********************
 *Yinpengyi
 *2003-01-29
 *********************/
function stringlencheck(as_string, ai_maxlen) {
    var li_stringlen = 0;
    var li_temp = 0;
    alert(as_string.length);
    for (i = 0; as_string.charAt(i) != 0; i++) {
        //li_stringlen++;
        //*
        alert(as_string.charAt(i));
        if (as_string.charAt(i) < 0) {
            li_temp = li_temp + 1;
            li_stringlen = li_stringlen + 2;
        } else {
            li_stringlen = li_stringlen + 1;
        }
        if (li_temp == 2) {

            li_temp = 0;
        }
        //*/
    }

    if (li_stringlen > ai_maxlen) {
        alert(li_stringlen + "longer");
    }
}

///////////////////////////////////////////////////////////
function showMobileSchWin(returnSelectName, objectPath) {
    closepopwin();

    var popWinHref = "";
    var popWinRef = "";

    popWinHref = objectPath;

    popWinHref = popWinHref + "?returnSelectName=" + returnSelectName;

    /////////////////////////////////////////////

    var popWinWidth = 350;
    var popWinHeight = 500;

    if (lastmousex + popWinWidth > screen.width + 10) {
        lastmousex = screen.width - popWinWidth - 10;
    }
    lastmousex = lastmousex - 5;

    if (lastmousey + popWinHeight > screen.height + 10) {
        lastmousey = screen.height - popWinHeight - 60;
    }


    popWinRef = popWinRef + "width=" + popWinWidth + ",height=" + popWinHeight + ",";
    popWinRef = popWinRef + "left=" + lastmousex + ",top=" + lastmousey + ",";
    popWinRef = popWinRef + "resizable=yes,scrollbars=yes,status=no,toolbar=no,systemmenu=no,location=no,borderSize=thin";

    //////////////////////////////////////////
    curpopupwindow = window.open(popWinHref, "refWindow", popWinRef, false);
    curpopupwindow.parentWin = this;
}

function returnpopwindata(as_code, as_name, as_returncodeitem, as_returnnameitem, as_returnform) {
    if (as_returnform == null) {
        as_returnform = "dataform";
    }
    eval("window.opener.document." + as_returnform + "." + as_returncodeitem).value = as_code;
    eval("window.opener.document." + as_returnform + "." + as_returnnameitem).value = as_name;
    window.close();
}

function returnpopwinselectdata(value, name, returnselectname) {
    window.opener.addListElt(value, name, returnselectname);
}

//
function addListElt(value, name, returnselectname) {
    var inExist = 0;
    var oSelect = eval(returnselectname);
    for (i = 0; i < oSelect.options.length; i++) {
        if (oSelect.options[i].value == value) {
            inExist = 1;
        }
    }

    var newOption = new Option(name, value);
    if (inExist == 0) {
        oSelect.options[oSelect.options.length] = newOption;
    }
}

function removeSelectList(selectName) {
    var oSelect = eval(selectName);
    for (i = oSelect.length - 1; i > -1; i--) {
        if (oSelect.options[i].selected) {
            oSelect.options[i] = null;
        }
    }
}

function removeAllList(selectName) {
    var oSelect = eval(selectName);
    for (i = oSelect.length - 1; i > -1; i--) {
        oSelect.options[i] = null;
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////

var months = new Array("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月");
//var months = new Array("01", "02", "03", "04", "05", "06", "07","08", "09", "10", "11", "12");
var daysInMonth = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
var displayMonth = new Date().getMonth();
var displayYear = new Date().getFullYear();
var displayDivName;
var displayElement;
today = new getToday();

function getIEPosX(elt) {
    return getIEPos(elt, "Left");
}

function getIEPosY(elt) {
    return getIEPos(elt, "Top");
}

function getIEPos(elt, which) {
    iPos = 0;
    while (elt != null) {
        iPos += elt["offset" + which];
        elt = elt.offsetParent;
    }
    return iPos
}

function getXBrowserRef(eltname) {
    return (isIE ? document.all[eltname].style : document.layers[eltname]);
}

function hideElement(eltname) {
    getXBrowserRef(eltname).visibility = 'hidden';
}

function moveBy(elt, deltaX, deltaY) {
    if (isIE) {
        elt.left = elt.pixelLeft + deltaX;
        elt.top = elt.pixelTop + deltaY;
    } else {
        elt.left += deltaX;
        elt.top += deltaY;
    }
}

function toggleVisible(imgname, eltname) {
    elt = getXBrowserRef(eltname);
    if (elt.visibility == 'visible' || elt.visibility == 'show') {
        elt.visibility = 'hidden';
    } else {
        fixPosition(imgname, eltname);
        elt.visibility = 'visible';
    }
}

function setPosition(elt, positionername, isPlacedUnder) {
    positioner = null;
    if (isIE) {
        positioner = document.all[positionername];
        elt.left = getIEPosX(positioner);
        elt.top = getIEPosY(positioner);
    } else {
        positioner = document.images[positionername];
        elt.left = positioner.x;
        elt.top = positioner.y;
    }
    if (isPlacedUnder) {
        moveBy(elt, 0, positioner.height);
    }
}

function getDays(month, year) {
    if (1 == month) {
        return ((0 == year % 4) && (0 != (year % 100))) || (0 == year % 400) ? 29 : 28;
    } else {
        return daysInMonth[month];
    }
}

function getToday() {
    this.now = new Date();
    this.year = this.now.getFullYear();
    this.month = this.now.getMonth();
    this.day = this.now.getDate();
}

function newCalendar(eltName, attachedElement) {
    if (attachedElement) {
        if (displayDivName && displayDivName != eltName) hideElement(displayDivName);
        displayElement = attachedElement;
    }

    displayDivName = eltName;
    today = new getToday();
    var parseYear = parseInt(displayYear + '');
    var newCal = new Date(parseYear, displayMonth, 1);
    var day = -1;
    var startDayOfWeek = newCal.getDay();
    if ((today.year == newCal.getFullYear()) && (today.month == newCal.getMonth())) {
        day = today.day;
    }
    var intDaysInMonth = getDays(newCal.getMonth(), newCal.getFullYear());
    var daysGrid = makeDaysGrid(startDayOfWeek, day, intDaysInMonth, newCal, eltName);

    if (isIE) {
        var elt = document.all[eltName];
        elt.innerHTML = daysGrid;
    } else {
        var elt = document.layers[eltName].document;
        elt.open();
        elt.write(daysGrid);
        elt.close();
    }
}

function incMonth(delta, eltName) {
    displayMonth += delta;
    if (displayMonth >= 12) {
        displayMonth = 0;
        incYear(1, eltName);
    } else if (displayMonth <= -1) {
        displayMonth = 11;
        incYear(-1, eltName);
    } else {
        newCalendar(eltName);
    }
}

function incYear(delta, eltName) {
    displayYear = parseInt(displayYear + '') + delta;
    newCalendar(eltName);
}

function makeDaysGrid(startDay, day, intDaysInMonth, newCal, eltName) {
    var ls_siteurl = "";
    var daysGrid;
    var month = newCal.getMonth();
    var year = newCal.getFullYear();
    var isThisYear = (year == new Date().getFullYear());
    var isThisMonth = (day > -1);
    daysGrid = '<table border=1 cellspacing=0 cellpadding=2><tr><td nowrap class="date_popwin">';
    //daysGrid += '<font face="courier new, courier" size=2>';
    daysGrid += '<a href="javascript:hideElement(\'' + eltName + '\')"><img src="/static/images/icon/close.gif" border="0" width="16" height="16"></a>';
    daysGrid += '&nbsp;&nbsp;';

    /////////////////////////////////////////
    daysGrid += '<a href="javascript:incMonth(-1,\'' + eltName + '\')">&laquo;</a>';
    daysGrid += '<b>';
    if (isThisMonth) {
        daysGrid += '<font color=red>' + months[month] + '</font>';
    } else {
        daysGrid += months[month];
    }
    daysGrid += '</b>';
    daysGrid += '<a href="javascript:incMonth(1,\'' + eltName + '\')">&raquo;</a>';
    daysGrid += '&nbsp;&nbsp;&nbsp;';
    daysGrid += '<a href="javascript:incYear(-1,\'' + eltName + '\')">&laquo;</a>';
    daysGrid += '<b>';
    if (isThisYear) {
        daysGrid += '<font color=red>' + year + '</font>';
    } else {
        daysGrid += '' + year;
    }
    daysGrid += '</b>';
    daysGrid += '<a href="javascript:incYear(1,\'' + eltName + '\')">&raquo;</a><br>';

    /////////////////////////////////////////
    daysGrid += '&nbsp;<font color=red><b>Su</font> Mo Tu We Th Fr <font color=red>Sa</b></font>&nbsp;<br>&nbsp;';

    var dayOfMonthOfFirstSunday = (7 - startDay + 1);
    for (var intWeek = 0; intWeek < 6; intWeek++) {
        var dayOfMonth;
        for (var intDay = 0; intDay < 7; intDay++) {
            dayOfMonth = (intWeek * 7) + intDay + dayOfMonthOfFirstSunday - 7;
            if (dayOfMonth <= 0) {
                daysGrid += "&nbsp;&nbsp; ";
            } else if (dayOfMonth <= intDaysInMonth) {
                var Color = "blue";
                if (day > 0 && day == dayOfMonth) {
                    Color = "red";
                }
                daysGrid += '<a href="javascript:setDay(';
                daysGrid += dayOfMonth + ',\'' + eltName + '\')" ';
                daysGrid += 'style="color:' + Color + '">';
                var dayString = dayOfMonth + "</a> ";
                if (dayString.length == 6) {
                    dayString = '0' + dayString;
                }
                daysGrid += dayString;
            }
        }
        if (dayOfMonth < intDaysInMonth) daysGrid += "<br>&nbsp;";
    }
    return daysGrid + "</td></tr></table>";
}

function setDay(day, eltName) {
    displayElement.value = displayYear + "-" + StringRight("00" + (displayMonth + 1), 2) + "-" + StringRight("00" + day, 2);
    hideElement(eltName);
}

function fixPosition(imgname, eltname) {
    elt = getXBrowserRef(eltname);
    positionerImgName = imgname;
    // hint: try setting isPlacedUnder to false
    isPlacedUnder = false;
    if (isPlacedUnder) {
        setPosition(elt, positionerImgName, true);
    } else {
        setPosition(elt, positionerImgName);
    }
}

function toggleDatePicker(imgname, eltName, formElt) {
    var x = formElt.indexOf('.');
    var formName = formElt.substring(0, x);
    var formEltName = formElt.substring(x + 1);
    newCalendar(eltName, document.forms[formName].elements[formEltName]);
    toggleVisible(imgname, eltName);
}
/////////////////////////////////////////////////////////////////////////////////////////////


/*********************
 *Yinpengyi
 *2003-01-29
 *********************/
function imginputcheck(as_string) {

    var ls_fileext = as_string.substring(as_string.length - 4, as_string.length);
    ls_fileext = ls_fileext.toLowerCase();
    if (!(ls_fileext == '.gif' || ls_fileext == '.jpg' || ls_fileext == '.bmp')) {

        alert("Sorry!You only be allowed to upload files such as '*.jpg','*.gif','*.bmp'!");
        return false;
    }
    return true;
}


function onlyNumericValid(checkObject) {
    var checkValue = checkObject.value;
    if (isNumericString(checkValue) == false) {
        checkObject.value = checkValue.substring(0, checkValue.length - 1);
    }
}

function stringLengthLimit(textObject, maxLength) {
    var checkValue = textObject.value;
    if (checkValue.length > maxLength) {
        textObject.value = checkValue.substring(0, maxLength - 1);
        textObject.focus();
    }
}

function isNumericString(srcString) {
    for (var i = 0; i < srcString.length; i++) {
        if (isNaN(srcString.charAt(i))) {//not a number;
            return false;
        }
    }
    return true;
}

function isDateString(srcString) {

    var tempDate = new Date(srcString);
    var y = tempDate.getFullYear();
    var m = tempDate.getMonth() + 1;
    var d = tempDate.getDate();
    var myday = y + "/" + m + "/" + d
    if (myday != srcString) {
        return false;
    }
    return true;
}

function isMobileString(srcString) {
    if (isNumericString(srcString) && srcString.length == 11 && srcString.substring(0, 2) == "13" && !(srcString.substring(0, 3) == "132") && !(srcString.substring(0, 3) == "134")) {
        // && !(srcString.substring(0,3) == "130") && !(srcString.substring(0,3) == "131") && !(srcString.substring(0,3) == "133")
        return true;
    } else {
        return false;
    }
}

function isMobileStrings(srcString) {
    var separated_values = srcString.split(",");
    for (var i = 0; i < separated_values.length; i++) {
        if (!(isNumericString(separated_values[i]) && separated_values[i].length == 11)) {
            return false;
        }
    }
    return true;
}

function getMobileNum(srcString) {
    var separated_values = srcString.split(",");
    return separated_values.length;
}

function isValidPasswd(srcString) {
    srcString = srcString.toLowerCase();
    if (srcString.length < 6 || srcString.length > 12) {
        return false;
    }
    for (var i = 0; i < srcString.length; i++) {
        if (!(srcString.charAt(i) >= "0" && srcString.charAt(i) <= "9") && !(srcString.charAt(i) >= "a" && srcString.charAt(i) <= "z")) {
            return false;
        }
    }
    return true;
}

function isPassFilter(srcString) {
    for (var i = 0; i < filterArray.length; i++) {
        if (srcString.indexOf(filterArray[i]) > -1) {
            return false;
        }
    }
    return true;
}

function checkall(checkboxs, curcheckbox) {
    var selectValue = curcheckbox.checked;
    document.getElementsByName("uncheck")[0].checked = false;
    if (!checkboxs.length) {
        checkboxs.checked = selectValue;
    }
    for (var i = 0; i < checkboxs.length; i++) {
        checkboxs[i].checked = selectValue;
    }
}


function convertcheck(checkboxs) {
    document.getElementsByName("selectall")[0].checked = false;
    if (!checkboxs.length) {
        checkboxs.checked = !checkboxs.checked;
    }
    for (var i = 0; i < checkboxs.length; i++) {
        checkboxs[i].checked = !checkboxs[i].checked;
    }
}

/*********************
 *Yinpengyi
 *2003-01-29
 *********************/
function moveup(ao_sourceselect) {
    var li_selectid;
    li_selectid = ao_sourceselect.selectedIndex;
    if (ao_sourceselect.length > 1 && li_selectid > 0) { //it have at least 2 items and not the first one is selected
        var prevOption = ao_sourceselect.options[li_selectid - 1];
        var newOption = new Option(prevOption.text, prevOption.value);
        var selectedOption = ao_sourceselect.options[li_selectid];

        ao_sourceselect.options[li_selectid - 1] = new Option(selectedOption.text, selectedOption.value);
        ao_sourceselect.options[li_selectid] = newOption;
        //
        ao_sourceselect.focus();
        ao_sourceselect.selectedIndex = li_selectid - 1;
    }
}


/*********************
 *Yinpengyi
 *2003-01-29
 *********************/
function movedown(ao_sourceselect) {
    var li_selectid;
    li_selectid = ao_sourceselect.selectedIndex;

    if (ao_sourceselect.length > 1 && li_selectid < ao_sourceselect.length - 1 && li_selectid >= 0) {	//it has at least 2 items and not the last item and selected one item;
        var nextOption = ao_sourceselect.options[li_selectid + 1];
        var newOption = new Option(nextOption.text, nextOption.value);
        var selectedOption = ao_sourceselect.options[li_selectid];
        //
        ao_sourceselect.options[li_selectid + 1] = new Option(selectedOption.text, selectedOption.value);
        ao_sourceselect.options[li_selectid] = newOption;
        //
        ao_sourceselect.focus();
        ao_sourceselect.selectedIndex = li_selectid + 1;
    }
}

function moveSelect(srcSelect, destSelect) {
    if (srcSelect.selectedIndex > -1) {
        for (i = 0; i < srcSelect.length; ++i) {
            var srcOption = srcSelect.options[i];
            if (srcOption.selected) {
                var newOption = new Option(srcOption.text, srcOption.value);
                destSelect.options[destSelect.options.length] = newOption;
            }
        }

        for (i = srcSelect.length - 1; i > -1; i--) {
            if (srcSelect.options[i].selected)
                srcSelect.options[i] = null;
        }
    }
}

function selectall(ao_select) {
    with (ao_select) {
        for (var i = 0; i < length; i++) {
            options[i].selected = true;
        }
    }
}

function StringRight($srcStr, $length) {
    return $srcStr.substring($srcStr.length - $length);
}

function submitTab($formName, $tabPrix, $curTab, $tabNum, $url) {
    var tabObject;

    var formObject;
    formObject = eval("document." + $formName);
    formObject.action = $url;

    for (var i = 1; i <= $tabNum; i++) {
        tabObject = eval("document." + $formName + "." + $tabPrix + i);

        if ($curTab - i == 0) {
            tabObject.className = "tab_active_button";

        } else {
            tabObject.className = "tab_noactive_button";
        }
    }

    return false;
}

function clickTab($tabPrix, $spanPrix, $curTab, $tabNum) {
    var tabObject;
    var spanObject;

    for (var i = 1; i <= $tabNum; i++) {
        tabObject = document.getElementById($tabPrix + i);
        spanObject = document.getElementById($spanPrix + i);

        if ($curTab == i) {
            spanObject.style.display = '';
            tabObject.className = "tab_active_button";
        } else {
            spanObject.style.display = 'none';
            tabObject.className = "tab_noactive_button";
        }
    }

    return false;
}

function hideSpan($spanObject, $checked) {
    if ($checked) {
        $spanObject.style.display = 'none';
    } else {
        $spanObject.style.display = '';
    }
}

function showSpan($spanObject, $checked) {
    if ($checked) {
        $spanObject.style.display = '';
    } else {
        $spanObject.style.display = 'none';
    }
}

function convertDate(dateString) {
    if (dateString) {
        var date = new Date(dateString.replace(/-/, "/"));

        return date;
    }
}

function isChinese(str) { //判断是不是中文
    var oVal = str.val();
    var oValLength = 0;
    oVal.replace(/n*s*/, '') == '' ? oValLength = 0 : oValLength = oVal.match(/[^ -~]/g) == null ? oVal.length : oVal.length + oVal.match(/[^ -~]/g).length;
    return oValLength;
}