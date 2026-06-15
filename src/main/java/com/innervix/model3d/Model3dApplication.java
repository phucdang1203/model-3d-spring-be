package com.innervix.model3d;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.innervix.model3d")
public class Model3dApplication {

    public static void main(String[] args) {
        SpringApplication.run(Model3dApplication.class, args);
    }
}
