package ru.numbDev.XmlGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class XmlGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlGeneratorApplication.class, args);
	}

}
