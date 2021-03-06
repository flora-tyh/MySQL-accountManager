package com.thoughtworks;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Register {
    private final int MAX_LOGIN_COUNTER = 3;

    public void inputRegisterInfo() {
        Scanner sc = new Scanner(System.in);
        String registerInfo = sc.next();
        if(!isRegisterInfoComplete(registerInfo)) {
            System.out.println("请输入合法的注册信息:");
            inputRegisterInfo();
        };
        String[] registerInfoArr = registerInfo.split(",");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(registerInfoArr[0]);
        userInfo.setTel(registerInfoArr[1]);
        userInfo.setEmail(registerInfoArr[2]);
        userInfo.setPassword(registerInfoArr[3]);

        if (!isRegisterInfoNormative(userInfo.getUserName(), userInfo.getTel(), userInfo.getEmail(), userInfo.getPassword())) {
            inputRegisterInfo();
        };
        if (isRepeat(userInfo.getUserName())) {
            System.out.println("用户名" + userInfo.getUserName() + "已被注册！\n请重新输入:");
            inputRegisterInfo();
        };
        JDBCOperation.save(userInfo, MAX_LOGIN_COUNTER);
    }

    /**
     * 检查是否输入了完整的信息（userName,tel,email,password)
     */
    public boolean isRegisterInfoComplete(String registerInfo) {
        return Pattern.matches(".*,.*,.*,.*", registerInfo);
    }

    /**
     * 注册信息格式检查
     * @param loginUserName 用户名格式：2~10个任意字符
     * @param loginTel 手机号码格式：1开头的11位数字
     * @param loginEmail 邮箱格式：xx@xx
     * @param loginPassword 密码格式：8~16个字符，含有至少一个数字和一个字母
     */
    public boolean isRegisterInfoNormative(String loginUserName, String loginTel, String loginEmail, String loginPassword) {
        if (!Pattern.matches(".{2,10}", loginUserName)) {
            System.out.println("用户名不合法\n" +
                               "请输入合法的注册信息：");
            return false;
        }
        if (!Pattern.matches("^1[0-9]{10}", loginTel)) {
            System.out.println("手机号不合法\n" +
                               "请输入合法的注册信息：");
            return false;
        }
        if (!Pattern.matches(".*@.*", loginEmail)) {
            System.out.println("邮箱不合法\n" +
                               "请输入合法的注册信息：");
            return false;
        }
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$", loginPassword)) {
            System.out.println("密码不合法\n" +
                               "请输入合法的注册信息：");
            return false;
        }
        return true;
    }

    /**
     * @param loginUserName 检查注册用户名是否被占用
     */
    public boolean isRepeat(String loginUserName) {
        PreparedStatement ptmt = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "SELECT tel, email, password, loginCounter, locked FROM account WHERE user_name = ?;";
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, loginUserName);
            rs = ptmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.close(rs, ptmt, conn);
        }
    }
}
