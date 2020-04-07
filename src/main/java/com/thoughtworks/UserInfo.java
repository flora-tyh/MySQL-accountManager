package com.thoughtworks;

public class UserInfo {
    private String userName;
    private String tel;
    private String email;
    private String password;
    private int loginCounter;
    private int locked;

    public UserInfo() {
    }

    public UserInfo(String userName, String tel, String email, String password, int loginCounter, int locked) {
        this.userName = userName;
        this.tel = tel;
        this.email = email;
        this.password = password;
        this.loginCounter = loginCounter;
        this.locked = locked;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoginCounter() {
        return loginCounter;
    }

    public void setLoginCounter(int loginCounter) {
        this.loginCounter = loginCounter;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }
}
