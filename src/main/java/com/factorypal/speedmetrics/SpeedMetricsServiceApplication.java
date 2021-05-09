package com.factorypal.speedmetrics;

import com.factorypal.speedmetrics.repository.MachineDataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = MachineDataRepository.class)
public class SpeedMetricsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeedMetricsServiceApplication.class, args);
	}

}
