package com.example.demo.config;

import com.example.demo.models.User;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;

public class YamlReader {

    public static void main(String[] args) {
        // 使用自定义构造器注册YamlConfig类型
        Constructor constructor = new Constructor(UserConfig.class);
        Yaml yaml = new Yaml(constructor);

        // 读取YAML文件
        try (InputStream inputStream = YamlReader.class.getClassLoader().getResourceAsStream("user.yml")) {
            if (inputStream == null) {
                System.out.println("无法找到config.yaml文件");
                return;
            }

            // 解析YAML文件
            UserConfig yamlConfig = yaml.load(inputStream);

            printUsers(yamlConfig.getUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 打印用户信息
    private static void printUsers(List<User> users) {
        if (users != null) {
            for (User user : users) {
                System.out.println("username: " + user.getUsername() + ", password: " + user.getPassword());
            }
        } else {
            System.out.println("没有找到用户信息");
        }
    }
}
