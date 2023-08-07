package com.datasetgenerator.annotationtool.config;

import com.datasetgenerator.annotationtool.util.Interval;
import com.datasetgenerator.annotationtool.util.serializer.IntervalSerializer;
import com.datasetgenerator.annotationtool.util.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule intervalModule = new SimpleModule();
        intervalModule.addSerializer(Interval.class, new IntervalSerializer());
        objectMapper.registerModule(intervalModule);
        SimpleModule dateTimeModule = new SimpleModule();
        dateTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        objectMapper.registerModule(dateTimeModule);

        return objectMapper;
    }
}






