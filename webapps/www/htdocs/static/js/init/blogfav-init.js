/**
 * Created by IntelliJ IDEA.
 * User: yugao
 * Date: 12-2-3
 * Time: 下午1:56
 * To change this template use File | Settings | File Templates.
 */
define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    require('../common/jquery.lazyload')($);
    var card = require('../page/card');
    var header = require('../page/header');
    var follow = require('../page/follow');
    var followCallback = require('../page/followcallback');
    var blog = require('../page/blog');
    var msg = require('../page/message');
    var atta = require('../page/atta');
    var contentlist = require('../page/contentlist');

    $(document).ready(function() {
        $(".lazy").scrollLoading();
        card.bindCard(followCallback.followCallBack, followCallback.unFollowOnList);
        contentlist.blogfavlistBind();

        //头像放大
        blog.headiconInit();
        //私信
        msg.bindSendMsg();
        //@Ta
        atta.attaBind();
        //加关注
        follow.ajaxFollowBind(followCallback.followCallbackOnBlog);
        //取消关注
        follow.ajaxUnFollowBind(followCallback.unFollowOnBlog);
        //查看关注链接
        follow.followfanslink();
        header.noticeSearchReTopInit();

    });
    require.async('../../third/ckeditor/ckeditor');

    require.async('http://static1.baifendian.com/service/zhaomi/zm_blog_index.js');

    require.async('../common/google-statistics');
    require.async('../common/bdhm')

});