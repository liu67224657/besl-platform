/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-3-5
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
define(function() {
    return function($) {
        (function($) {
            $.fn.ylDropDownList = function(o) {
                o = $.extend({
                            arrowUrl: joyconfig.URL_LIB + "/static/theme/default/img/select_bg.jpg", //选择框右侧向下的箭头图片
                            arrowWidth: 22, //上述图片的宽
                            arrowHeight: 21, //上述图片的高
                            borderColor: "#C0C5C1", //选择框的边框颜色
                            dropDownListHeight: 180, //下拉列表的高度，超过此高度将出现垂直滚动条
                            hoverColor: "#f0f1f2", //下拉列表每项在鼠标经过时的背景色
                            currItemAddStyle: true
                        }, o || {});
                var ul, lis, currItem = $("<div  />"),
                        arrowHeight = parseInt(o.arrowHeight), arrowWidth = parseInt(o.arrowWidth), borderColor = o.borderColor,
                        select = $(this).hide(),
                        options = $("option", select),
                        optionsSize = options.size(),
                        dropDownListHeight = optionsSize * arrowHeight > parseInt(o.dropDownListHeight) ? parseInt(o.dropDownListHeight) : optionsSize * arrowHeight ,

                        getListItem = function(option) {
                            var currText = option.text(), currClass = $.trim(option.attr("class")), currStyle = $.trim(option.attr("style"));
                            return $("<div   />").text(currText).addClass(currClass).attr("style", currStyle);
                        },
                        getSelectedItem = function() {
                            return $("option:selected", select);

                        },
                        getListItems = function() {
                            var $ul = $("<ul class='ylDropDownUl' tabindex='0' />");
                            options.each(function() {
                                var li = $("<li />"), option = $(this);
                                li.append(getListItem(option));
                                $ul.append(li);
                            });
                            ul = $ul;
                            lis = $("li", ul);
                            return $ul;
                        },
                        setCurrItem = function() {
                            currItem.empty();
                            if (o.currItemAddStyle) {
                                currItem.append(getListItem(getSelectedItem()));
                            }
                            else {
                                currItem.text(getSelectedItem().text());
                            }
                        },
                        createElem = function() {
                            select.wrap("<div  />");
                            select.after(getListItems());
                            select.after(currItem);
                            setCurrItem();
                        },
                        setElem = function() {
                            currItem.css({ "display": "inline-block", "padding": "0 3px", "background-image": "url(" + o.arrowUrl + ")", "line-height": arrowHeight + "px", "background-repeat": "no-repeat", "background-position": "right top", "z-index": "1000","color":"#494949" ,"border": "1px solid " + borderColor })
                                    .height(arrowHeight);
                            ul.css({ "position": "absolute", "height": dropDownListHeight + "px", "line-height": arrowHeight + "px", "border": "1px solid " + borderColor, "border-top": "0", "padding": "0", "margin": "0", "list-style": "none", "background-color": "#fff", "z-index": "1001", "white-space": "nowrap", "overflow": "hidden", "overflow-y": "auto" });
                            select.parent().css({ "display": "inline-block", margin: "0 2px" }).height(25);
                            var width = ul.width() + 22;
                            ul.hide();
                            if ($.browser.msie && ($.browser.version == 7 || $.browser.version == 6)) {//hack ie7,ie6
                                select.parent().css({ "display": "inline", "zoom": "1", "vertical-align": "middle" })
                            }
                            ;
                            lis.css({ "padding-left": "4px", "padding-right": "22px", "color":"#494949" ,"line-height": arrowHeight + "px" }).height(arrowHeight);
                            currItem.width(width);
                            ul.width(width + 6);


                        },
                        setScrollTop = function(index) {
                            var visibleNum = parseInt(dropDownListHeight / arrowHeight);
                            ul.scrollTop((index - visibleNum + 1) * arrowHeight);

                        },
                        showUl = function() {
                            $(".ylDropDownUl").hide();
                            var index = options.index(getSelectedItem());
                            lis.css("background-color", "transparent");
                            lis.eq(index).css("background-color", o.hoverColor);
                            ul.show();
                            ul.focus();
                            setScrollTop(index);
                        },
                        hideUl = function() {
                            ul.hide();
                        },
                        toggleUl = function() {
                            if (ul.css("display") == "none") {
                                showUl();
                            } else {
                                hideUl();
                            }
                        },
                        setScroll = function(step) {
                            var index = options.index(getSelectedItem());
                            index = index + step;
                            if (index == -1)
                                index = optionsSize - 1;
                            currIndex = index % optionsSize;
                            lis.eq(currIndex).mouseover();

                            options.eq(currIndex).attr("selected", true);

                            setScrollTop(currIndex + 1);
                            setCurrItem();
                        },
                        bindEvents = function() {
                            $("html").click(function() {
                                hideUl();
                            });
                            currItem.click(
                                    function(event) {
                                        event.stopPropagation();
                                        toggleUl();

                                    }).keydown(function(event) {
                                        switch (event.keyCode) {
                                            case 40: // up
                                                event.preventDefault();
                                                showUl();
                                                break;

                                        }

                                    });
                            ul.click(function(event) {
                                event.stopPropagation();
                            })
                                    .keydown(function(event) {
                                        switch (event.keyCode) {
                                            case 38: // up
                                                event.preventDefault();
                                                setScroll(-1);
                                                break;
                                            case 40: // down
                                                event.preventDefault();
                                                setScroll(1);
                                                break;
                                            case 9: // tab
                                            case 13: // return
                                            case 27: //escape
                                                hideUl();
                                                break;
                                        }
                                    });
                            lis.click(
                                    function(event) {
                                        event.stopPropagation();
                                        var index = lis.index($(this));
                                        options.eq(index).attr("selected", true);
                                        setCurrItem();
                                        hideUl();
                                        select.change();

                                    }).mouseover(function() {
                                        lis.css("background-color", "transparent");
                                        $(this).css("background-color", o.hoverColor);
                                    });
                        };

                createElem();
                setElem();
                bindEvents();
                return $(this);
            };
        })(jQuery);
    }
});