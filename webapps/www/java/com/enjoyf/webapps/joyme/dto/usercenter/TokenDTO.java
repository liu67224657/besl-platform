package com.enjoyf.webapps.joyme.dto.usercenter;

/**
 * Created by ericliu on 14/10/22.
 */
public class TokenDTO {
    private String token;
    private int expires;

    public TokenDTO(String token, int expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }
}
