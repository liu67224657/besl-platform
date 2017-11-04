package com.enjoyf.webapps.joyme.weblogic.chinajoy;

/**
 * Created by IntelliJ IDEA.
 * User: yongmingxu
 * Date: 12-5-25
 * Time: 上午10:44
 * To change this template use File | Settings | File Templates.
 */
public class HeaderParam {
    private String mClientType; //设备类型
    private String mHdType;//  设备型号
    private String mOsVersion;//操作系统版本号
    private String mSerial;  //序列号

    public String getmClientType() {
        return mClientType;
    }

    public void setmClientType(String mClientType) {
        this.mClientType = mClientType;
    }

    public String getmHdType() {
        return mHdType;
    }

    public void setmHdType(String mHdType) {
        this.mHdType = mHdType;
    }

    public String getmOsVersion() {
        return mOsVersion;
    }

    public void setmOsVersion(String mOsVersion) {
        this.mOsVersion = mOsVersion;
    }

    public String getmSerial() {
        return mSerial;
    }

    public void setmSerial(String mSerial) {
        this.mSerial = mSerial;
    }
    
    @Override
    public String toString(){
        String str = "m_client_type is "+mClientType+", "+
                "m_hd_type is "+mHdType+", "+
                "m_os_version is "+mOsVersion+", "+
                "m_serial is "+mSerial;
        return str;
    }
}
