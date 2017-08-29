package io.hyperion.managerPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ManagerPlatformMain {
	public static void main(String[] args) {
		SpringApplication.run(ManagerPlatformMain.class, args);
	}
}
