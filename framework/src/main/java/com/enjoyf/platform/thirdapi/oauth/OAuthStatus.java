package com.enjoyf.platform.thirdapi.oauth;

/**
 * <p/>
 * Description:
 * </p>
 *
 * @author: <a href=mailto:ericliu@enjoyfound.com>liuhao</a>
 */
public class OAuthStatus {
    public static final OAuthStatus OAUTH_SUCCESS = new OAuthStatus(0, "sync_success");
    public static final OAuthStatus OAUTH_TOKEN_EXPIRE = new OAuthStatus(1, "sync_token_expire");
    public static final OAuthStatus OAUTH_TOKEN_FAILED = new OAuthStatus(2, "sync_token_falied");

    private String errorInfo;
    private int errorNum;

    public OAuthStatus(int errorNum, String errorInfo) {
        this.errorInfo = errorInfo;
        this.errorNum = errorNum;
    }

    public OAuthStatus(int errorNum) {
        this.errorNum = errorNum;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof OAuthStatus) {
            return errorNum == ((OAuthStatus) obj).getErrorNum();
        } else {
            return false;
        }
    }
}
