var ostyle = '<style>' +
    '.download{width: 100%;height:55px;background:#fff;border-top: 1px solid #e4e4e4;position:fixed;bottom:0;left: 0;z-index:999;display:none;}' +
    '.closeImg{width: 15px;height: 15px;position: absolute;padding:20px 15px;}' +
    '.download dl{float: left;margin-top: 10px;}' +
    '.download dl dt{width: 35px;height: 35px;float: left;margin-right: 10px;padding-left: 45px;}' +
    '.download dl dt img{width: 100%;height:100%;}' +
    '.download dl dd{float: left;}' +
    '.download dl dd h6{font-size: 14px;line-height: 20px;}' +
    '.download dl dd h6 a{color: #1c1c1c;}' +
    '.download dl dd p{color: #a0a0a0;font-size: 12px;line-height: 15px;}' +
    '.downImg{width: 90px;height: 35px;background: #45b0f6;border-radius: 5px; float: right;margin-right: 15px;margin-top: 10px; z-index:999}' +
    '.downImg img{width: 15px;height: 15px;float: left;padding: 8px 10px 0px 20px;}' +
    '.downImg span{color: #fff;float: left;line-height: 35px;font-size: 14px;}' +
    '</style>';
$("head").append(ostyle);
$(document).ready(function () {
    //优酷控制游戏是否显示


    $('.closeImg').on('touchstart', function (ev) {
        ev.stopPropagation();
        ev.preventDefault();
        $('.download').hide();
        $("#footer").css("padding", "0");
    })

    function youkuconfig() {
        document.getElementById("_youkudownload").style.display='none';
        //document.getElementById("_youkudownload").style.display = 'block';
       // $("#footer").css("padding", "0 0 56px");
    }

    youkuconfig();
});