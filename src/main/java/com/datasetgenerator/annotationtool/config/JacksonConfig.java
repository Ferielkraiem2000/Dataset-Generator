package com.datasetgenerator.annotationtool.config;

import com.datasetgenerator.annotationtool.util.Interval;
import com.datasetgenerator.annotationtool.util.serializer.IntervalSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Interval.class, new IntervalSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
