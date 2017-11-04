var likeBlock = false;
function likeGame(gameid) {
	var likeCookie = getCookie('jm_likegame_' + gameid);
	if (likeCookie == gameid) {
		$('#wp_comment_alert_no_button').attr('class','wp_comment_alert active');
		$('#wp_comment_tips_no_button').text('您已经发表过观点');
        setTimeout(function(){$('#wp_comment_alert_no_button').attr('class','wp_comment_alert');}, 1500);
		return;
	}
	if (likeBlock) {
		$('#wp_comment_alert_no_button').attr('class','wp_comment_alert active');
		$('#wp_comment_tips_no_button').text('操作太快了，请稍候!');
		setTimeout(function(){$('#wp_comment_alert_no_button').attr('class','wp_comment_alert');}, 1500);
		return;
	}
	likeBlock = true;
	$.ajax({
		url : apiHost + "collection/api/likegame",
		type : "post",
		data : {gameid : gameid},
		dataType : "jsonp",
		jsonpCallback : "likegamecallback",
		success : function(req) {
			var resMsg = req[0];
			if (resMsg.rs == '-1001') {
				$('#wp_comment_alert_no_button').attr('class','wp_comment_alert active');
				$('#wp_comment_tips_no_button').text('参数错误，无法完成此操作');
				setTimeout(function(){$('#wp_comment_alert_no_button').attr('class','wp_comment_alert');}, 1500);
				likeBlock = false;
				return;
			} else if (resMsg.rs == '-60001') {
				$('#wp_comment_alert_no_button').attr('class','wp_comment_alert active');
				$('#wp_comment_tips_no_button').text('游戏不存在或已下架，无法完成此操作');
				setTimeout(function(){$('#wp_comment_alert_no_button').attr('class','wp_comment_alert');}, 1500);
				likeBlock = false;
				return;
			} else if (resMsg.rs == '1') {
				setCookie('jm_likegame_' + gameid, gameid, host);
				likeBlock = false;
        		var num=$('#assist_sum').text().replace('赞','');
        		if(num=='赞'){
        			num=0;
        		}
        		$('#assist_sum').html('');
        		$('#assist_sum').html('<i>赞</i>'+(parseInt(num)+1))
				return;
			} else {
				$('#wp_comment_alert_no_button').attr('class','wp_comment_alert active');
				$('#wp_comment_tips_no_button').text('系统错误');
				setTimeout(function(){$('#wp_comment_alert_no_button').attr('class','wp_comment_alert');}, 1500);
				likeBlock = false;
				return;
			}
		},
		complete : function() {
			likeBlock = false;
			return;
		},
		error : function() {
			$('#wp_comment_alert_no_button').attr('class','wp_comment_alert active');
			$('#wp_comment_tips_no_button').text('获取失败，请刷新');
			setTimeout(function(){$('#wp_comment_alert_no_button').attr('class','wp_comment_alert');}, 1500);
			likeBlock = false;
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
		cookie += key + "=" + escape(value) + ";path=/;domain=" + env + ";";
	document.cookie = cookie;
}
