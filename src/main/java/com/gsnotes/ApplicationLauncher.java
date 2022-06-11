package com.gsnotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ApplicationLauncher {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());


	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		LOGGER.error("me error");
		return application.sources(ApplicationLauncher.class);
	}

	public static void main(String[] args) throws Exception {
		
		SpringApplication.run(ApplicationLauncher.class, args);
	}

}
