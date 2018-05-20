package com.johnfnash.learn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.johnfnash.learn.mapper") //��Ӷ�mapper��ɨ��@MapperScan
public class SpringBootMybatisAnnoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootMybatisAnnoApplication.class, args);
	}
}
