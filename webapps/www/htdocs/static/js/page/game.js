define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var common = require('../common/common');
    var joymealert = require('../common/joymealert');
    var follow = require('./follow');
    require('../common/tips');

    var game = {
        followGroup:function(){
            follow.followBoardBind();
            follow.unFollowBoardBind();
        },
        bindShowGameInfo:function() {
            $("#gameviewdown").die().live('click', function() {
                $("#game_web").toggle();
                $("#game_desc").toggle();
                $("#gameviewup").toggle();
                $("#gameviewdown").toggle();
            });
            $("#gameviewup").die().live('click', function() {
                $("#game_web").toggle();
                $("#game_desc").toggle();
                $("#gameviewdown").toggle();
                $("#gameviewup").toggle();
            });
        },
        userLoginSub:function() {
            if (joyconfig.joyuserno == '') {
                var login = require('../biz/login-biz');
                $('#div_post_text').append('<div class="wrapper_unlogin" id="wrapper_unlogin">' +
                        '您需要' +
                        '<a id="maskLogin" href="javascript:void(0)">登录</a>' +
                        '后才能发表新帖' +
                        '</div>');
                $("#wrapper_unlogin").css({
                            'position':'absolute',
                            'top':44,
                            'left':0,
                            'width':$("#editcon").width(),
                            'height':$("#editcon").height() + 27 + 'px',
                            'border-radius': '5px 5px 5px 5px',
                            'border': '1px solid #C9C9C9',
                            'background':'#fff',
                            'line-height':$("#editcon").height() + 'px'
                        });
                $("#post_text_submit").attr('disabled', 'disabled')
                login.bindMaskLoginOnGame();
            }
        },
        handleOther:function() {
            if(joyconfig.joyuserno==''){
                $("#text_syn_install").attr('id','text_syn_install_later');
            }
        }


    }

    return game;
});
