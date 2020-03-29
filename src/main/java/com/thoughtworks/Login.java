package com.thoughtworks;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Login {
//    private String userName;
//    private String password;
    private final int MAX_LOGIN_COUNTER = 3;

    public void loginInfo() {
        Scanner sc = new Scanner(System.in);
        String loginInfo = sc.next();
        String[] loginInfoArr = loginInfo.split(",");
        String loginUserName = loginInfoArr[0];
        String loginPassword = loginInfoArr[1];
        String userName = loginUserName;
        String password = loginPassword;

        if (!isLoginInfoNormative(userName, password)) {
            return;
        };
        loginCertification(userName, password);
    }

    public boolean isLoginInfoNormative(String loginUserName, String loginPassword) {
        if (!Pattern.matches(".{2,10}", loginUserName) ||
            !Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", loginPassword)) {
            System.out.println("格式错误\n" +
                               "请按正确格式输入用户名和密码：");
            loginInfo();
            return false;
        }
        return  true;
    }

    public void loginCertification(String userName, String password) {
        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        String sql = null;
        try {
            conn = JDBCUtils.getConnection();
            sql = "SELECT tel, email, password, loginCounter, locked FROM account WHERE user_name = ?;";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, userName);
            rs = ptmt.executeQuery();
            boolean isMatchUserName = rs.next();

            if (isMatchUserName && rs.getInt("locked") == 1) {
                System.out.println("您已" + MAX_LOGIN_COUNTER + "次输错密码，账号被锁定");
                return;
            }

            int loginCounter = rs.getInt("loginCounter");

            if (isMatchUserName && Objects.equals(rs.getString("password"), password)) {
                loginSuccess(conn, rs, userName);
            } else if(isMatchUserName && !Objects.equals(rs.getString("password"), password)) {
                loginFailed(conn, loginCounter,userName);
            } else {
                System.out.println("您输入的用户名或密码错误！\n请重新输入用户名和密码");
                loginInfo();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(rs, ptmt, conn);
        }
    }

    public void loginSuccess(Connection conn, ResultSet rs, String userName) throws SQLException {
        String tel = rs.getString("tel");
        String email = rs.getString("email");
        String sql = "UPDATE account SET loginCounter = 0 WHERE user_name = ?;";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, userName);
        ptmt.executeUpdate();
        System.out.println(String.format("%s，欢迎回来！\n您的手机号是%s，邮箱是%s",
                userName, tel, email));
    }

    public void loginFailed(Connection conn, int loginCounter, String userName) throws SQLException {
        String sql = "UPDATE account SET loginCounter = loginCounter - 1 WHERE user_name = ?;";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, userName);
        ptmt.executeUpdate();
        if (loginCounter == 1) {
            lockAccount(conn, userName);
            return;
        }
        System.out.println("您输入的用户名或密码错误，还有" + (loginCounter - 1) + "次机会！\n请重新输入用户名和密码");
        loginInfo();
    }

    public void lockAccount(Connection conn, String userName) throws SQLException {
        String sql = "UPDATE account SET locked = '1' WHERE user_name = ?;";
        PreparedStatement ptmt = conn.prepareStatement(sql);
        ptmt.setString(1, userName);
        ptmt.executeUpdate();
        System.out.println("您已" + MAX_LOGIN_COUNTER + "次输错密码，账号被锁定");
    }
}
