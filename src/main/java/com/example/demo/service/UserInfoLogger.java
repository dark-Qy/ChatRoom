package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class UserInfoLogger {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoLogger.class);
    private static final Marker USERINFO_MARKER = MarkerFactory.getMarker("USERINFO");

    public void logUserInfo() {
        // 记录带有 USERINFO Marker 的日志信息
        logger.info(USERINFO_MARKER, "This is a user info log message.");
    }

    public static void main(String[] args) {
        UserInfoLogger userInfoLogger = new UserInfoLogger();
        userInfoLogger.logUserInfo();
    }
}
