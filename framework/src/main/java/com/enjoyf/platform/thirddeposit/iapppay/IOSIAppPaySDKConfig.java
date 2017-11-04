package com.enjoyf.platform.thirddeposit.iapppay;

/**
 * @Auther: <a mailto="ericliu@straff.joyme.com">ericliu</a>
 * Create time: 15/9/15
 * Description:
 */

/**
 * 应用接入iAppPay云支付平台sdk集成信息
 */
public class IOSIAppPaySDKConfig {

    /**
     * 应用名称：
     * 应用在iAppPay云支付平台注册的名称
     */
    public final static String APP_NAME = "苹果应用";

    /**
     * 应用编号：
     * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成
     */
    public final static String APP_ID = "300281023";

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
    public final static String APPV_KEY = "MIICXAIBAAKBgQDFNQIbQtU6445ZCJNEpg7pEDXlTTGZLlTFtew6KmT4MM0ia812a0a6cHyScqj32M+eQWD2/oZctXDPFydChIHXvyZS8HaIOYNjfPvWgPwnmFfiCRQokDdWtendBhOmwrS7V5KwDoLSdIlUT6/+V6nFeL/P0OhovtbCQNfaC3NXLwIDAQABAoGAdJZfFXDQkQ0hBd/8+LPX0s/DnTmi3fW3Jl/kNYsEwHpRfE0RXjYTzXtdGlNxM33ZWK08PynAk6Ss+bGtgzTFcZUNne+EO+qLurzto7kPH6AhNVNEJEI0VypwDMb9/NTULiJEtL3nYytcJXOGaiquMfvM0HBOj3UkD+QckhEFjCkCQQD6mwFuKSiefiakh+PxUw6oOhetyuItQdeIiHCZSskmKv5vPZUrVFDZjLUYyFMaAzwSqL4DQjNzMyiw7cyW/bfzAkEAyXO+XQrjTcUr36VR0GZhAj3F/h0RkMNxqrMq7Sw22dcs/1wl+iDDOWbE/0kaLUTFSZS4FPmggD40NioJWQ4O1QJBAM2TVDQK/XAuqgJ0Gw+/A5oKtZvyf2GCzo0I591IFQQDZt7YFMMLkpszqudehZUkLCZeWCkSKGSLBglFuXvvO/MCQB0ksnBxZ8rTcHJtTzWv6e7SEvedKvJfY+1G+DnRgKnpGChlI1VZMgT22h74a2ILJeNsMWlPZ6nF1fXUBQAuKXUCQFgCCz034yxl1QW+FIdsY+sEnOO8VESCLUZk8IqcPBpbtQIEpf0+CpTZSQX43PB0nIytySVEeygiFsBbDWzeAHc=";

    /**
     * 平台公钥：
     * 用于商户应用对接收平台的数据进行解密
     */
    public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC45tUCB0OXfIOxMF4ouh58uit2t5td1n0nB9fM7NOeP0hYoO/qm2GqqeHPd4oHZHC4M1yO4ga3rw8k/zf+4FIXYuG1042NYWJS+Aoy9AhyyFfH5fE3lFzeAovRvqRcWXxh0VZ4MXUXlA1SrU6e6UYLhQ6pxDnIBpNmKd045cZuIwIDAQAB";
}
