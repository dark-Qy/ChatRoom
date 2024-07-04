package com.example.demo;

import com.example.demo.config.UserConfig;
import com.example.demo.config.UserConfigLoader;
import com.example.demo.service.OnlineUserManager;
import com.example.demo.models.User;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@EnableConfigurationProperties
public class Demo1Application {

    private OnlineUserManager onlineUserManager;
    private final UserConfig userConfig = new UserConfig();

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Demo1Application.class, args);
        Demo1Application app = new Demo1Application(); // 创建 Demo1Application 的实例
        app.setApplicationContext(context); // 通过方法设置 ApplicationContext
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入命令:");
        while (true) {
            String command = scanner.nextLine();
            switch (command.toLowerCase()) {
                case "list":
                    app.listOnlineUsers();
                    break;
                case "listall":
                    app.listAllUsers();
                    break;
                case "quit":
                    app.quitSystem();
                    break;
                default:
                    System.out.println("未知命令，请重新输入！");
                    break;
            }
        }
    }

    // 用于注入ApplicationContext
    public void setApplicationContext(ApplicationContext context) {
        this.onlineUserManager = context.getBean(OnlineUserManager.class);
    }

    public void listOnlineUsers() {
        onlineUserManager.listOnlineUsers();
    }

    public List<String> getlistOnlineUsers() {
        return onlineUserManager.getOnlineUsersList();
    }

    public static void listAllUsers() {
        Constructor constructor = new Constructor(UserConfig.class);
        Yaml yaml = new Yaml(constructor);

        try (InputStream inputStream = UserConfigLoader.class.getClassLoader().getResourceAsStream("user.yml")) {
            if (inputStream == null) {
                throw new RuntimeException("无法找到user.yml文件");
            }
            UserConfig userConfig = yaml.load(inputStream);

            if (userConfig == null || userConfig.getUsers() == null) {
                System.out.println("No users found in user.yml");
                return;
            }

            for (User user : userConfig.getUsers()) {
                System.out.println("username:"+user.getUsername());
            }


        } catch (Exception e) {
            throw new RuntimeException("读取YAML文件时出错", e);
        }
    }

    private static void quitSystem() {
        System.out.println("退出系统...");
        System.exit(0);
    }


}
