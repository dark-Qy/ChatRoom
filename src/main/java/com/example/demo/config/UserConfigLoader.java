package com.example.demo.config;

import com.example.demo.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

@Configuration
public class UserConfigLoader {

    @Bean
    public UserConfig userConfig() {
        Constructor constructor = new Constructor(UserConfig.class);
        Yaml yaml = new Yaml(constructor);

        try (InputStream inputStream = UserConfigLoader.class.getClassLoader().getResourceAsStream("user.yml")) {
            if (inputStream == null) {
                throw new RuntimeException("无法找到user.yml文件");
            }

            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("读取YAML文件时出错", e);
        }
    }
}
