package com.s4;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class S4Demo extends SpringBootServletInitializer {

	public static void main(String[] args)  {
		BasicConfigurator.configure();
		SpringApplication.run(S4Demo.class, args);
	}

}
