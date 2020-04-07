package com.thoughtworks;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Login {
    private final int MAX_LOGIN_COUNTER = 3;

    public void loginInfo() {
        Scanner sc = new Scanner(System.in);
        String loginInfo = sc.next();

        if (!Pattern.matches(".*,.*", loginInfo)) {
            System.out.println("格式错误\n" +
                    "请按正确格式输入用户名和密码：");
            loginInfo();
        }

        String[] loginInfoArr = loginInfo.split(",");
        String userName = loginInfoArr[0];
        String password = loginInfoArr[1];
        System.out.println(loginInfoArr.length);

        if (!isLoginInfoNormative(userName, password)) {
            System.out.println("格式错误\n" +
                    "请按正确格式输入用户名和密码：");
            loginInfo();
        }
        loginCertification(userName, password);
    }

    public boolean isLoginInfoNormative(String loginUserName, String loginPassword) {
        if (!Pattern.matches(".{2,10}", loginUserName) ||
                !Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", loginPassword)) {
            return false;
        }
        return true;
    }

    public void loginCertification(String userName, String password) {
        UserInfo userInfo = JDBCOperation.getUserInfoByName(userName);
        if (userInfo == null) {
            System.out.println("您输入的用户名或密码错误！\n请重新输入用户名和密码");
            loginInfo();
        } else if(userInfo.getLocked() == 1) {
            System.out.println("您已" + MAX_LOGIN_COUNTER + "次输错密码，账号被锁定");
        } else if(Objects.equals(userInfo.getPassword(), password)) {
            loginSuccess(userInfo);
        } else {
            loginFailed(userInfo, userName);
        }
    }

    public void loginSuccess(UserInfo userInfo) {
        JDBCOperation.updateLoginCounter(true, userInfo.getUserName());
        System.out.println(String.format("%s，欢迎回来！\n您的手机号是%s，邮箱是%s",
                userInfo.getUserName(), userInfo.getTel(), userInfo.getEmail()));
    }

    public void loginFailed(UserInfo userInfo, String userName) {
        JDBCOperation.updateLoginCounter(false, userInfo.getUserName());
        if (userInfo.getLoginCounter() == 1) {
            JDBCOperation.lockAccount(userName);
            System.out.println("您已" + MAX_LOGIN_COUNTER + "次输错密码，账号被锁定");
            return;
        }
        System.out.println("您输入的用户名或密码错误，还有" + (userInfo.getLoginCounter() - 1) + "次机会！\n请重新输入用户名和密码");
        loginInfo();
    }
}
