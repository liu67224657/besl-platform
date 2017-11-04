/**
 * Created by IntelliJ IDEA_
 * User: yugao
 * Date: 11-10-25
 * Time: 上午9:
 * To change this template use File | Settings | File Templates_
 */
define(function() {
    return tipsText = {
        //文章发布
        blogNew:{
            blog_new_audio_subject_maxlength :  '音频的标题长度是140字',
            blog_new_audio_content_maxlength : '音频内容太长请精简一下吧',
            blog_new_chat_content_notnull : '一句话的长度不能为空',
            blog_new_chat_content_maxlength : '一句话长度是140个字符',
            blog_new_photo_content_maxlength : '图片整体说明太长请精简一下吧',
            blog_new_photo_des_maxlength : '单个图片描述内容太长请精简一下吧',
            blog_new_vedio_subject_maxlength : '视频的标题长度是140字',
            blog_new_vedio_content_maxlength : '视频内容太长请精简一下吧',
            blog_new_text_subject_maxlength : '文章的标题请控制在140个字以内',
            blog_new_text_content_maxlength : '文章内容太长请精简一下吧',
            blog_new_content_illegl : '发布的文章中含有不适当内容',
            post_ios_url_illegl:'URL格式不正确，请重新输入',
            //新加无用
            blog_reply_content_notnull:'评论内容不能为空',
            blog_reply_origncontent_notexists:'原贴不存在不能评论',
            blog_reply_content_illegl:'评论含有不适当内容',
            blog_replay_delete_notpri:'该用户没有删除评论的权利',
            blog_zt_origncontent_notexists:'原贴不存在不能转贴'
        },
        profile:{
            user_profile_blog_hasbaned:'该用户已经被屏蔽不能访问！',
            user_profile_blog_forbidpost:'你已经被限制发言',
            user_ipforbidden_forbidlogin:'您的IP被限制登录！',
            user_ipforbidden_forbidpost:'您的IP被限制发言！'
        },
        //用户域名
        userDomainName:{
            user_blogdomain_notnull : '用户域名不能为空',
            user_blogdomain_length:'用户域名长度为5-20位',
            user_blogdomain_illegl:'此域名已经被注册',
            user_blogdomain_wrong:'只允许英文、数字和横线',
            user_blogdomain_exists:'域名已经存在，请换一个域名'
        },
        //搜索
        search:{
            search_word_error : '请输入要查找的内容'
        },
        //评论
        comment:{
            blog_reply_length:300,
            blog_pl_content_notnull : '评论内容不能为空',
            blog_pl_not_apply : '原作者设置了隐私权限,不能评论',
            blog_pl_content_maxlength : '评论内容不能超过300字(或600个字符)',
            blog_pl_illegl:'评论中含有不适当内容',
            image_reply:'只发图不解释'
        },
        //转帖
        turnPost:{
            blog_zt_content_maxlength : '转贴内容不能超过300字(或600个字符)',
            forward_content_illegl : '转贴含有不适当内容',
            forward_tag_illegl:'标签文字中含有不适当内容'
        },
        //用户设置
        userSet:{
            user_email_dev_length : '邮箱长度最多40位',
            userset_password_exist_space : '密码不符合要求',
            userset_password_notnull : '当前密码不能为空',
            user_userpwd_length : '用户密码长度为6-18位',
            userset_newpassword_notnull : '新密码不能为空',
            userset_repetpassword_notnull : '确认密码不能为空',
            user_userpwd_notnull : '密码不为空',
            userset_userpwd_notequals : '两次输入的密码不一致',
            userset_user_introduce_length : '简介长度最多允许140个字',
            user_nickname_notnull : '用户昵称不能为空',
            user_nickname_length : '昵称长度2-16个字(或4-32字符)',
            user_nickname_exists : '昵称已经存在，请换一个昵称',
            user_nickname_has_exists :'昵称也存在，请换一个昵称',
            user_email_notnull : '用户的邮箱不能为空',
            user_email_wrong : '用户邮箱格式错误',
            user_email_length : '邮箱长度最多64位',
            user_email_exists : '该注册邮箱已经存在，请换一个',
            user_email_illegl : '邮箱只允许英文、数字和横线',
            user_word_illegl : '含有不适当内容',
            user_experience_school_notnull : '请输入学校名称',
            user_experience_school_length : '请控制在20个字以内',
            user_experience_company_notnull : '请输入单位名称',
            user_experience_company_length : '请控制在20个字以内'
        },
        //找回密码
        userPwd:{
            user_pwd_forgot_notnull:'登录名不能为空',
            user_pwd_forgot_wrong:'登录名格式错误',
            user_pwd_forgot_notexists:'找不到该帐号,<a href="/register" >立即注册?</a>',
            user_pwd_forgot_success:'邮件已发送到{0}，请点击其中的链接重设密码。',
            user_pwd_reset_success:'新密码保存成功！请<a href="/login">重新登录</a>',
            user_pwd_reset_wrong:'参数错误，请检查链接地址是否正确._',
            user_pwd_reset_url_illegl:'链接无效，请重新找回密码。',
            user_pwd_forgot_mail_error:'邮箱格式不正确',
            user_pwd_forgot_mail_notnull:'邮件名不能为空'
        },
        //取视频
        userVideo:{
            user_video_parseerror:'视频地址无法识别，请重新输入吧。'
        },
        //用户登录相关
        userLogin:{
            user_email_wrong : '请输入正确的邮箱地址',
            user_email_notnull: '请输入你的邮箱',
            user_login_notnull:'用户名密码不能为空',
            user_login_wrong:'用户名或密码不正确',
            user_userpwd_notnull:'密码不为空',
            user_userpwd_length:'用户密码长度为6-18位',
            user_not_login:'用户未登录'
        },
        //用户分组
        userGroup:{
            usertype_typename_count : '用户分组不能超过10个',
            usertype_typename_notnull : '用户分组不能为空',
            usertype_typename_maxlength : '用户分组不能超过8个字',
            usertype_typename_illegl : '分组名称有不适当内容，请重新输入',
            usertype_typename_hasexitsts: '分组名称已经存在，换一个吧',
            usertype_typename_hasexitsts_short: '分组名称已经存在'
        },
        //用户域名
        useDOMAINName:{
            user_blogdomain_notnull : '用户域名不能为空',
            user_blogdomain_length : '用户域名长度为5-20位',
            user_blogdomain_illegl : '此域名已经被注册',
            user_blogdomain_wrong : '支持英文、数字和横线',
            user_blogdomain_exists : '域名已经存在，请换一个域名'
        },
        //昵称
        nickName:{
            user_nickname_illegl : '此昵称已被占用'
        },
        //信箱
        joyMessage:{
            message_receiver_empty : '收信人不能为空',
            message_receiver_notexists : '收信人不存在',
            message_content_empty : '发信内容不能为空',
            message_content_length : '发信内容最多300个字',
            message_body_illegl : '私信含有不适当内容，无法发送',
            message_content_empty:'发信内容不能为空',
            //新加无用内容
            message_receiver_self : '发信人不能给自己发送私信',
            message_error_notpri:'由于对方的隐私设置，你无法发送给TA消息',
            message_send_repeat:'相同内容发一次就够了',
            message_user_notexists:'抱歉没有找到该用户，请检查输入是否正确',
            message_user_hasnotpri:'抱歉，<a href="\#">该用户</a>暂时不接受任何私信，请尝试其他联系方式”',
            message_user_syserror:'抱歉，系统繁忙,'
        },
        //其他
        other:{
            game_scoring_surveyname_maxlength : '你填写的游戏名字太长了'
        },
        //申请注册码
        regcode:{
            regcode_apply_email_not_null:'申请注册码邮箱不能为空',
            regcode_apply_introduce_tips:'告诉我们您是谁，及您在游戏方面的爱好和上网经历，我们将据此发放注册码',
            regcode_apply_email_illegal:'申请邮箱格式不对',
            regcode_has_sending:'申请成功',
            regcode_introduce_maxlength:'申请理由在300字之内',
            regcode_email_maxlength:'申请邮箱在128字之内',
            regcode_has_used:'您的链接已失效，请<a href\="/register">重新申请</a>',
            regcode_not_exists:'注册码不存在，请<a href\="/register">重新申请</a>',
            regcode_not_valid:'您的链接已失效，请<a href\="/register" > 重新申请 < /a>',
            register_not_open:'注册还未开放，请<a href\="/register">申请</a>注册码'
        },
        //游戏打分中的提示信息
        game:{
            game_scoring_surveyname_maxlength:'你填写的游戏名字太长了',
            game_scoring_surveyname_notnull:'游戏名字不能为空',
            game_scoring_share_maxlength:'打分分享最长140个字',
            game_scoring_share_notnull:'请填写你的打分心得'
        },
        //邮箱
        email:{
            emailauth_error_paramerror:'参数错误，请核对您的链接',
            emailauth_error_authcoderror:'邮箱链接已经过期，请重新发送验证邮件',
            emailauth_error_timelimit:'请不要频繁发送邮件',
            emailauth_error_timeout:'链接已经过期请重新发送',
            emailauth_error_status:'邮件链接已经过期，请勿重复点击'
        },
//上传
        upload:{
            upload_image_success:'上传成功',
            upload_image_failed:'上传失败',
            upload_image_exname_error:'只能上传jpg、jpeg、gif、png类型的图片',
            upload_image_urllink_error:'请输入正确的链接',
            upload_image_out_size:'上传的图片大于8MB，请更换一张'
        },
//邀请
        invite:{
            invite_mail_not_null:'邮箱不能为空',
            invite_pwd_not_null:'密码不能为空',
            invite_provider_not_null:'请选择邮箱',
            invite_mails_not_null:'至少选择一个邮箱',
            invite_mail_illegl:'邮箱格式不正确',
            invite_mailtitle_out_length:'昵称请为2到16个字',
            invite_mailBody_out_length:'邀请信的内容请不要超过140个字'
        },
//系统
        joySystem:{
            system_error:'系统错误请联系相关人员'
        },
//
        joyfocus:{
            focus_uno_not_null:'至少选一个来关注吧',
            user_demo_maxlength:'备注名称请不要超过8个字',
            user_demo_notnull:'请填写备注名称',
            user_demo_illegl:'含有不适当内容'
        },

        //tag
        tag:{
            add_like_tag_empty:'您现在没有添加任何标签',
            add_like_tag_title:'我已经添加过的标签',
            max_length_error:'标签过长，最多14个字。',
            max_num_error:'标签太多了，最多20个。',
            tag_illegl:'标签中含有不适当内容',
            max_length:14,
            max_num:20
        },
        //vote
        vote:{
            vote_subject_empty:'投票主题不能为空',
            vote_subject_max_length:'投票主题最多30个字',
            vote_description_max_length:'投票说明最多500个字',
            vote_option_max_length:'选项最多可输入20个字',
            vote_option_limit_size_tips:'选项个数不能少于两个',
            vote_option_limit_size:'2',
            vote_option_max_size:'20',
            vote_subject_input_length:'30',
            vote_option_input_length:'20',
            vote_description_input_length:'500',
            vote_subject_illegal:'主题中含有不适当内容',
            vote_description_illegal:'投票说明中含有不适当内容',
            vote_option_option_illegal:'选项中含有不适当内容',
            vote_expired_date_illegal:'截止时间请输入yyyy-mm-dd格式',
            vote_expired_date_before_ill:'截止时间不能早于当前时间'
        }
    }
})