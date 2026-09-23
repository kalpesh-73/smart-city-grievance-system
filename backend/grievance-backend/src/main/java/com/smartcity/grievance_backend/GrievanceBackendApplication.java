package com.smartcity.grievance_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.http.converter.json.GsonBuilderUtils;

@SpringBootApplication
public class GrievanceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrievanceBackendApplication.class, args);
		System.out.println("tomcat started");
	}

}
