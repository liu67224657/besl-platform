/**
 * CoryRight 2011 fivewh.com
 */
package com.enjoyf.platform.webapps.common.oauth;

/**
 * @Auther: <a mailto="yinpengyi@fivewh.com">Yin Pengyi</a>
 * Create time: 11-11-10 下午2:33
 * Description:
 */
public class OAuthException extends Exception {
    //
    private String error;
    private int errorCode;

    //static oauth exception.
    public static OAuthException REDIRECT_URI_MISMATCH = new OAuthException("redirect_uri_mismatch ", 21322);

    public static OAuthException INVALID_REQUEST = new OAuthException("invalid_request ", 21323);

    public static OAuthException INVALID_CLIENT = new OAuthException("invalid_client ", 21324);

    public static OAuthException INVALID_GRANT = new OAuthException("invalid_grant ", 21325);

    public static OAuthException UNAUTHORIZED_CLIENT = new OAuthException("unauthorized_client ", 21326);

    public static OAuthException UNSUPPORTED_GRANT_TYPE = new OAuthException("unsupported_grant_type ", 21328);

    public static OAuthException UNSUPPORTED_RESPONSE_TYPE = new OAuthException("unsupported_response_type ", 21329);

    public static OAuthException ACCESS_DENIED = new OAuthException("access_denied ", 21330);

    public static OAuthException TEMPORARILY_UNAVAILABLE = new OAuthException("temporarily_unavailable ", 21331);

    //oauth 2
    public static OAuthException OAUTHINFO_CREATE = new OAuthException("oauthinfo_create", 21332);

    public static OAuthException GET_OAUTHINFO_BYRERESHTOKEN = new OAuthException("get_oauthinfo_byrereshtoken ", 21333);

    public static OAuthException GET_OAUTHINFO_BYACCESSTOKEN = new OAuthException("get_oauthinfo_byaccesstoken ", 21334);
    //
    public OAuthException(String e, int code) {
        this.error = e;
        this.errorCode = code;
    }

    public String getError() {
        return error;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
