package com.bitforce.tuteme.configuration.jwt;

import java.util.Date;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Date expandTime;

    public JwtAuthenticationResponse(String accessToken, Date expandTime) {
        this.accessToken = accessToken;
        this.expandTime = expandTime;
    }

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpandTime() {
        return expandTime;
    }

    public void setExpandTime(Date expandTime) {
        this.expandTime = expandTime;
    }
}
