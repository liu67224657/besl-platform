function showTips(text, time) {
    var tip = $('#tip');
    if (tip.hasClass('show')) {
        return;
    }

    tip.html(text).addClass('show');
    var timeId = setTimeout(function () {
        tip.html("").removeClass('show');
        clearTimeout(timeId);
    }, time);

}