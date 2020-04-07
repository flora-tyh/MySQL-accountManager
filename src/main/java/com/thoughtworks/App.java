package com.thoughtworks;

import java.util.Scanner;

//建表语句在resources
public class App {
    public static void main(String[] args) {
        boolean isServer = true;
        while (isServer) {
//      1.展示首页
            showMainPage();
//      2.选择需要的服务
            String selectService = selectService();
//      3.执行需要的服务
            switch (selectService) {
                case "1":
                    register();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    System.out.println("退出");
                    isServer = false;
                    break;
                default:
                    System.out.println("错误的输入，请重新输入");
            }
        }
    }

    public static void showMainPage() {
        System.out.println("1.注册\n" +
                "2.登录\n" +
                "3.退出");
    }

    public static String selectService() {
        System.out.println("请输入你的选择(1~3)：");
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }

    public static void register() {
        System.out.println("请输入注册信息(格式：用户名,手机号,邮箱,密码)：");

        Register register = new Register();
        register.inputRegisterInfo();
    }

    public static void login() {
        System.out.println("请输入用户名和密码(格式：用户名,密码)：");

        Login login = new Login();
        login.loginInfo();
    }
}
