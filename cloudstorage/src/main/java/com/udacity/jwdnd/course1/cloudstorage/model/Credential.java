package com.udacity.jwdnd.course1.cloudstorage.model;

public class Credential {
    private Integer credentialId;
    private String url;
    private String username;
    private String key;
    private String password;
    private Integer userId;

    public Credential(String url, String username, String key, String password, Integer userId) {
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "credentialId=" + credentialId +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", key='" + key + '\'' +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                '}';
    }

    public Credential() {
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
