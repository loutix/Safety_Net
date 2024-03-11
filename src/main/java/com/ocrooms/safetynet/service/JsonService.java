package com.ocrooms.safetynet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocrooms.safetynet.mapper.DataObject;
import com.ocrooms.safetynet.repository.FireStationRepository;
import com.ocrooms.safetynet.repository.MedicalRecordsRepository;
import com.ocrooms.safetynet.repository.PersonRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Getter
@Service
public class JsonService {

    private final DataObject data;

    @Autowired
    public JsonService(ResourceLoader resourceLoader, ObjectMapper objectMapper, FireStationRepository fireStationRepository, MedicalRecordsRepository medicalRecordsRepository, PersonRepository personRepository) throws IOException {

        Resource resource = resourceLoader.getResource("classpath:data.json");
        this.data = objectMapper.readValue(resource.getInputStream(), DataObject.class);

        fireStationRepository.initFirestationsData(data.getFirestations());
        medicalRecordsRepository.initMedicalRecordData(data.getMedicalrecords());
        personRepository.initPersonRecordData(data.getPersons());
    }

}