define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var login = require('./login-biz');

    var card = {
        loadCardByNick:function (cardSource,cardId, nick, loadingCallback,callback, cardFollowCallback, cardUnoFollowCallback) {
            $.ajax({
                        url:'/json/card/nick',
                        data:{nick:nick},
                        type:'post',
                        cache:false,
                        async:false,
                        success:function(req) {
                            var reMsg = eval('(' + req + ')');
                            callback(reMsg, cardId, cardFollowCallback, cardUnoFollowCallback);
                        },
                        beforeSend:function(){
                            loadingCallback(cardSource,cardId);
                        }
                    });
        },
        getAtLink:function(nick, callback) {
            $.post("/json/nick/clickat", {nick:nick}, function(data) {
                var jsonObj = eval('(' + data + ')');
                callback(jsonObj);
            });
        }
    }

    return card;
});






