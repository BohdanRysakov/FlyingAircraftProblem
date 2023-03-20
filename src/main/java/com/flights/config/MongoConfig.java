package com.flights.config;

import com.flights.repositories.AirplaneRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = AirplaneRepository.class)
@Configuration
public class MongoConfig {

}
