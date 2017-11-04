define(function(require, exports, module) {
    var joymealert = require('../common/joymealert');

    var obj = new Object();
    obj['@qq.com'] = 'http://mail.qq.com';
    obj['@163.com'] = 'http://mail.163.com'
    obj['@126.com'] = 'http://mail.126.com'
    obj['@sina.com'] = 'http://mail.sina.com'
    obj['@yahoo.com.cn'] = 'http://mail.yahoo.com'
    obj['@yahoo.cn'] = 'http://mail.yahoo.com'
    obj['@vip.qq.com'] = 'http://mail.vip.qq.com'
    obj['@sohu.com'] = 'http://mail.sohu.com'
    obj['@yeah.net'] = 'http://mail.yeah.com'
    obj['@tom.com'] = 'http://mail.tom.com'
    obj['@yahoo.com'] = 'http://mail.yahoo.com'
    obj['@live.cn'] = 'http://mail.live.com'
    obj['@21cn.com'] = 'http://mail.21cn.com'
    obj['@hotmail.com'] = 'http://hotmail.com'
    obj['@gmail.com'] = 'http://gmail.com'

    /**
     * 通过email得到邮件服务器，如果没在服务器列表中返回false,在服务器列表中返回地址
     * @param email
     */
    var mail = {

        getMailServer:function (email) {

            var atIdx = email.indexOf('@');

            if (atIdx == false) {
                return false;
            }
            var mailName = email.substr(atIdx);
            var url = obj[mailName];
            if (url == null) {
                url = false;
            }
            return url;
        },

        /**
         * 跳转到别的邮件服务器
         * @param mail
         */
        hrefMail:function (mail) {
            var mailServer = this.getMailServer(mail);
            if (mailServer) {
                window.open(mailServer);
            } else {
                joymealert.alert({text:'无法打开邮件地址'});
            }

        }
    }

    return mail;
});
