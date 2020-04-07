package com.thoughtworks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCOperation {
    public static void save(UserInfo userInfo, int MAX_LOGIN_COUNTER) {
        PreparedStatement ptmt = null;
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO account(user_name, tel, email, password, loginCounter) VALUES(?, ?, ?, ?, ?)";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, userInfo.getUserName());
            ptmt.setString(2, userInfo.getTel());
            ptmt.setString(3, userInfo.getEmail());
            ptmt.setString(4, userInfo.getPassword());
            ptmt.setInt(5, MAX_LOGIN_COUNTER);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(ptmt, conn);
        }
        System.out.println(userInfo.getUserName() + "，恭喜你注册成功！");
    }

    public static UserInfo getUserInfoByName(String userName) {
        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT tel, email, password, loginCounter, locked FROM account WHERE user_name = ?;";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, userName);
            rs = ptmt.executeQuery();

            if (rs.next()) {
                UserInfo userInfo = new UserInfo(userName,
                        rs.getString("tel"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("loginCounter"),
                        rs.getInt("locked"));
                return userInfo;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(rs, ptmt, conn);
        }
    }

    public static void updateLoginCounter(Boolean isSuccess, String userName) {
        Connection conn = null;
        PreparedStatement ptmt = null;
        String sql = null;
        try {
            conn = JDBCUtils.getConnection();
            if (isSuccess) {
                sql = "UPDATE account SET loginCounter = 0 WHERE user_name = ?;";
            } else {
                sql = "UPDATE account SET loginCounter = loginCounter - 1 WHERE user_name = ?;";
            }
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, userName);
            ptmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(ptmt, conn);
        }
    }

    public static void lockAccount(String userName) {
        Connection conn = null;
        PreparedStatement ptmt = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "UPDATE account SET locked = '1' WHERE user_name = ?;";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, userName);
            ptmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(ptmt, conn);
        }
    }
}
