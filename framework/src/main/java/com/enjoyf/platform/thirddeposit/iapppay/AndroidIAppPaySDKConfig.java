package com.enjoyf.platform.thirddeposit.iapppay;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/9/15
 * Description:
 */

/**
 * 应用接入iAppPay云支付平台sdk集成信息
 */
public class AndroidIAppPaySDKConfig {
    /**
     * 应用名称：
     * 应用在iAppPay云支付平台注册的名称
     */
    public final static String APP_NAME = "着迷PC安卓";

    /**
     * 应用编号：
     * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成
     */
    public final static String APP_ID = "3002808173";

    /**
     * 商品编号：
     * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
     * 编号对应商品名称为：虚拟币
     */
    public final static int WARES_ID_1 = 1;

    /**
     * 应用私钥：
     * 用于对商户应用发送到平台的数据进行加密
     */
    public final static String APPV_KEY = "MIICXgIBAAKBgQCHMYNBUpuWaBG6kQW4lTKJ21lvftrSi9oEEHd6+25Sb0TufPP0w6yaIO8TyDykbFMdp1BBktqm4bMTxKof78EquNEsLRzPjGSkzZoPdkcGuVijV+bFm/cO5uQb6w2WGdUGqiIbu3jzXNUt0TxLYhvbamUdwArVeoN2tW/I34h2mQIDAQABAoGAZd/IoavHNKK9YVIw9nPvPnS9Q0hbSrc6FT/3WQQokRPp5LDiALqG0SoqHkUQdXJve3P+BTvNal2/XREghapca1ol8aho6KgqlCfI84IlTOwbMVMFYjv8faqgJuFKgVKvAU4KWLV1jdw2BE/lSlDmOwTNuTxKMghIIlTa1xLmQQECQQD3ppz0wN473qVM85LzcllzJHIaVQ9ttv/TACQFU6x1mANY3dXLiovThlktN4mKSuw8IyVFIR4HiOetAiKeXP3hAkEAi8BRvSoFzjkkV3WqCMpdsjubqsw22Ei/qXFjmm4be4CtOFr4EAcaeHoWHgKUNEf+zes5smsd/JNbdF5uJu7fuQJBAI1ENXRIMHzmXFSw/LpyGCRePdhXfR2Qrg2mnuWK/W128THYGQSqW2il0mjArWx9oJQNWE0+vup9ZeqCtUywrwECQQCCuHQ80y/3BHA/9uoDqvcVnrgvfTHYAcR1xIB8lX3TejuNOVpL87kI9Hn3vEXQ2X84Mk0X3SaBeXzUTvgOJubRAkEAkMK9uawUxQ4GM0ojnJUU2tUhz9jtgQc86gkB6BD5AkAEq4Jn2tfozag4uOSRGQZ4hPq16rB20cKu6vWjz+tHig==";

    /**
     * 平台公钥：
     * 用于商户应用对接收平台的数据进行解密
     */
    public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSNESKjWMNcSqdy5OylGyzNkr3iATXgQe2gKsi0WUHAi24zZ4Q4mxMFYVUPu5pQccW1E6lGZw2eFe2pqKNfosKm3G1Mccuu8iK40Uc3H1rsNshBWSKdzgPFHptfo3gY4g0WghwiVo9X7ake7XweFhtkvVy+T93JXUtdHqfc0GRhwIDAQAB";
}