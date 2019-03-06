package com.erhui.official;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.erhui.official.dao")
public class OfficialApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfficialApplication.class, args);
	}

}

