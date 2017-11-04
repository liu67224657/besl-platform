define(function (require, exports, module) {
    var host = window.location.host.substr(window.location.host.indexOf('.'));
    var wwwHost = 'http://www' + host + '/';
    var apiHost = 'http://api' + host + '/';
    var likeBlock = false;
    var unLikeBlock = false;

    $(document).ready(function () {
        var thisGame = $('#praise-btn').attr('data-game');
        var likeCookie = getCookie('jm_likegame_'+thisGame);
        if(likeCookie == thisGame){
            $('#praise-btn').addClass('active');
            $('#favor-cite').remove();
		}
        //喜欢
		$('#praise-btn').on('click', function() {
			var gameId = $(this).attr('data-game');
			if (gameId != undefined && gameId != '') {
				likeGame(gameId);
			}
		});
        var unLikeCookie = getCookie('jm_unlikegame_'+thisGame);
        if(unLikeCookie == thisGame){
            $('#bad-btn').addClass('active');
            $('#unfavor-cite').remove();
        }

        //不喜欢
        $('#bad-btn').on('click', function () {
            var gameId = $(this).attr('data-game');
            if(gameId != undefined && gameId != ''){
                unLikeGame(gameId);
            }
        });
    });

    function likeGame(gameid) {
        var likeCookie = getCookie('jm_likegame_'+gameid);
        if(likeCookie == gameid){
            alert('您已经发表过观点！');
            return;
        }
        if (likeBlock) {
            alert('操作太快了，请稍候');
            return;
        }
        likeBlock = true;
        $.ajax({
            url: apiHost + "collection/api/likegame",
            type: "post",
            data: {gameid: gameid},
            dataType: "jsonp",
            jsonpCallback: "likegamecallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '-1001') {
                    alert('参数错误，无法完成此操作');
                    likeBlock = false;
                    return;
                } else if (resMsg.rs == '-60001') {
                    alert('游戏不存在或已下架，无法完成此操作');
                    likeBlock = false;
                    return;
                } else if (resMsg.rs == '1') {
                    setCookie('jm_likegame_'+gameid, gameid, host);
                    var num = $('#favor-sum').text();
                    if(num==''||num==undefined){
                    	num='0';
                    }
                    $('#favor-sum').text(parseInt(num)+1);
                    setTimeout(function(){$('#favor-cite').remove()}, 1000);

                    likeBlock = false;
                } else {
                    alert('系统错误');
                    likeBlock = false;
                    return;
                }
            },
            complete: function () {
                likeBlock = false;
                return;
            },
            error: function () {
                alert('获取失败，请刷新');
                likeBlock = false;
                return;
            }
        });
    }

    function unLikeGame(gameid) {
        var unLikeCookie = getCookie('jm_unlikegame_'+gameid);
        if(unLikeCookie == gameid){
            alert('您已经发表过观点！');
            return;
        }
        if (unLikeBlock) {
            alert('操作太快了，请稍候');
            return;
        }
        unLikeBlock = true;
        $.ajax({
            url: apiHost + "collection/api/unlikegame",
            type: "post",
            data: {gameid: gameid},
            dataType: "jsonp",
            jsonpCallback: "unlikegamecallback",
            success: function (req) {
                var resMsg = req[0];
                if (resMsg.rs == '-1001') {
                    alert('参数错误，无法完成此操作');
                    unLikeBlock = false;
                    return;
                } else if (resMsg.rs == '-60001') {
                    alert('游戏不存在或已下架，无法完成此操作');
                    unLikeBlock = false;
                    return;
                } else if (resMsg.rs == '1') {
                	
                    setCookie('jm_unlikegame_'+gameid, gameid, host);
                    var num = $('#unfavor-sum').text();
                    if(num==''||num==undefined){
                    	num='0';
                    }
                    $('#unfavor-sum').text(parseInt(num)+1);
                    setTimeout(function(){$('#unfavor-cite').remove()}, 1000);
                    unLikeBlock = false;
                } else {
                    alert('错误');
                    unLikeBlock = false;
                    return;
                }
            },
            complete: function () {
                unLikeBlock = false;
                return;
            },
            error: function () {
                alert('获取失败，请刷新');
                unLikeBlock = false;
                return;
            }
        });
    }

    function getCookie(objName) {
        var arrStr = document.cookie.split("; ");
        for (var i = 0; i < arrStr.length; i++) {
            var temp = arrStr[i].split("=");
            if (temp[0] == objName && temp[1] != '\'\'' && temp[1] != "\"\"") {
                return unescape(temp[1]);
            }
        }
        return null;
    }

    function setCookie(key, value, env) {
        var cookie = "";
        if (!!key)
            cookie += key + "=" + escape(value) + ";path=/;domain="+env+";";
        document.cookie = cookie;
    }
});







