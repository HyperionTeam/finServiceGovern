package io.hyperion.govMailNotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GovMailNotifyMain {
	public static void main(String[] args) {
		SpringApplication.run(GovMailNotifyMain.class, args);
	}
}
