package com.ocrooms.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocrooms.safetynet.mapper.DataObject;
import lombok.Getter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsonService {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Getter
    private DataObject data;

    public JsonService(ResourceLoader resourceLoader, ObjectMapper objectMapper) throws IOException {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;

        Resource resource = resourceLoader.getResource("classpath:data.json");
        this.data = objectMapper.readValue(resource.getInputStream(), DataObject.class);
    }
}