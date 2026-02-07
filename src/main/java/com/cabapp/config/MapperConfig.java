package com.cabapp.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean

    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // FIX: Enable STRICT matching to prevent 'vehicleId' -> 'id' mapping errors
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper;
    }
}