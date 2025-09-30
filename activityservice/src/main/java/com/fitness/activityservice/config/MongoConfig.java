// In MongoConfig.java
package com.fitness.activityservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}