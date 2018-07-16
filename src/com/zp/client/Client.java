/*
 * client
 *
 * @author zhaopeng
 * @date 18-7-15
 */
package com.zp.client;

import com.zp.client.controller.Controller;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private Controller controller;
    private Pattern pattern;

    public Client(){
        this.controller = new Controller();
        this.pattern = Pattern.compile("^([a-z]{1,10}) ([^ ].*)");
    }

    public void start() {
        while (true) {
            try {
                System.out.println("文件管理系统：");
                System.out.println("请输入指令");
                Scanner scanner = new Scanner(System.in);
                String operator = scanner.nextLine();
                Matcher matcher = pattern.matcher(operator);
                if (matcher.find()) {
                    switch (matcher.group(1)) {
                        case "upload":
                            controller.upload(matcher.group(2));
                            break;
                        default:
                            System.out.println("错误的指令！");
                            break;
                    }
                }
                else {
                    System.out.println("错误的指令格式！");
                }
            } catch (Exception err) {
                err.printStackTrace();
                break;
            }
        }
    }
}
