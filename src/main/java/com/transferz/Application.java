package com.transferz;

import com.transferz.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class Application
{
	public static void main(final String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}