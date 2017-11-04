package com.enjoyf.platform.service.point;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: ericliu
 * Date: 13-5-27
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */
public class WanbaPointType implements Serializable {
    //对应积分
    public static final int PHONE =500;//手机绑定或注册
    public static final int THIRD =100; //第三方登录
    public static final int SIGN =40; //签到
    public static final int REPLY =5; //评论
    public static final int AGREE =1; //点赞
    public static final int SHARE =10; //分享


}
