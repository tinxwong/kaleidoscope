package com.xnew.kaleidoscope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KaleidoscopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KaleidoscopeApplication.class, args);
        System.out.println("服务启动成功...");
    }

}
