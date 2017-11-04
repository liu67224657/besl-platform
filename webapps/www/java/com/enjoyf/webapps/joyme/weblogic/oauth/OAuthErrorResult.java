/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.webapps.joyme.weblogic.oauth;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-14 下午6:11
 * Description:
 */
public class OAuthErrorResult {
    private String error;
    private int error_code;
    private String error_description;


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
