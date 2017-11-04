define(function(require, exports, module) {
    var $ = require('../common/jquery-1.5.2');
    var ajaxverify = {
        verifyPwd:function(value) {
            var result = true;

            $.ajax({
                        url: "/json/validate/pwd",
                        type:'post',
                        data:{userpwd:value},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,

        verifySysWord:function(value) {
            var result = true;

            $.ajax({
                        url: "/json/validate/sysword",
                        type:'post',
                        data:{word:value},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,
        verifyPost:function (value) {
            var result = true;
            $.ajax({
                        url: "/json/validate/postword",
                        type:'post',
                        data:{word:value},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });
            return result;
        }
        ,
        //昵称是否存在，修改页面uno不为空
        verifyNickNameExists:function(nickname) {
            var result = true;

            $.ajax({
                        type: 'post',
                        url: '/json/validate/nicknameexists',
                        data: {nickname:nickname},
                        async:false,
                        success:function(req) {
                            var resultMsg = eval('(' + req + ')');
                            if (resultMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,
        //是否符合昵称规则
        verifyNickNameRegex:function(nickname) {
            var result = true;

            $.ajax({
                        url: "/json/validate/checkNickName",
                        type:'post',
                        data:{nickname:nickname},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,
        //描述是否含有敏感内容
        verifyDesc:function(desc) {
            var result = true;

            $.ajax({
                        url: "/json/validate/desc",
                        type:'post',
                        data:{desc:desc},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,
        //域名验证
        verifyDomain:function(domain) {
            var result = true;

            $.ajax({
                        url: "/json/validate/sysdomain",
                        type:'post',
                        data:{domain:domain},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,
        verifyDomainExists:function(domain, uno) {
            var result = true;

            $.ajax({
                        url: "/json/validate/blogexists",
                        type:'post',
                        data:{domain:domain,uno:uno},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,

        verifyUserCateExists:function (value, uno) {
            var result = true;
            $.ajax({
                        url: "/json/validate/usertypeexists",
                        type:'post',
                        data:{cateName:value,uno:uno},
                        async:false,
                        dataType:'JSON',
                        success:function(req) {
                            if (req.status_code == '0') {
                                result = false;
                            }
                        }
                    });
            return result;
        }
        ,

        verifyTypeLength:function (typeName) {
            if (typeName.length > 8) {
                return false;
            }
            return true;
        }
        ,

        verifyUserCateCount:function (count) {
            if (count >= 10) {
                return false;
            }
            return true;
        }
        ,

        verifyUseridExists:function(userid) {
            var result = true;
            $.ajax({
                        url: "/json/validate/userexists",
                        type:'post',
                        data:{userid:userid},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '0') {
                                result = false;
                            }
                        }
                    });

            return result;
        }
        ,

        //文章是否已删除
        verifyContentIsRemove:function(contentId, contentUno) {
            var isRemove = false;
            $.ajax({
                        url: "/json/content/contentexists",
                        type:'post',
                        data:{contentUno:contentUno,contentId:contentId},
                        async:false,
                        dataType:'JSON',
                        success:function(req) {
                            if (req.status_code == '0') {
                                isRemove = true;
                            }
                        }
                    });
            return isRemove;
        }
        ,

        verifyInviteTiltie:function(title) {
            var result = false;
            $.ajax({
                        url: "/json/validate/invite/title",
                        type:'post',
                        data:{title:title},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            if (resMsg.status_code == '1') {
                                result = true;
                            }
                        }
                    });
            return result;
        },
        validateDomain:function(str) {
            var result = true;
            result = /^[\da-zA-Z\-]+$/.test($.trim(str));
            return result;
        },
        verifyTagsByJquery: function (tagsJq) {
            var _this = this;
            var result = true;
            $.each(tagsJq, function(i, val) {
                if (!_this.verifyPost(val.value)) {
                    result = false;
                    return result;
                }
            });
            return  result;
        },
        verifyTags: function (tags) {
            var _this = this;
            var result = true;
            var tagArray = tags.split(',');
            $.each(tagArray, function(i, val) {
                if (!_this.verifyPost(val)) {
                    result = false;
                    return result;
                }
            });
            return  result;
        },
        verifyPrivilege: function (rps) {
            var result = null;
            rps = false || rps;
            $.ajax({
                        url: "/json/validate/hasprivilage",
                        type:'post',
                        data:{rps:rps},
                        async:false,
                        success:function(req) {
                            var resMsg = eval('(' + req + ')');
                            result = resMsg;
                        }
                    });
            return result;
        }
    }
    return ajaxverify;
})