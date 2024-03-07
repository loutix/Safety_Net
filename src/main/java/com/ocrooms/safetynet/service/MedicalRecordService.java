package com.ocrooms.safetynet.service;

import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.repository.MedicalRecordsRepository;
import com.ocrooms.safetynet.repository.PersonRepository;
import com.ocrooms.safetynet.service.exceptions.ItemAlreadyExists;
import com.ocrooms.safetynet.service.exceptions.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class MedicalRecordService {


    private final MedicalRecordsRepository medicalRecordsRepository;
    private final PersonRepository personRepository;

    public MedicalRecordService(MedicalRecordsRepository medicalRecordsRepository, PersonRepository personRepository) {
        this.medicalRecordsRepository = medicalRecordsRepository;
        this.personRepository = personRepository;
    }


    public Set<MedicalRecord> index() {
        return medicalRecordsRepository.getAll();
    }


    public MedicalRecord show(String id) {
        return medicalRecordsRepository.getMedicalRecordById(id);
    }

    public MedicalRecord create(MedicalRecord medicalrecords) {

        //control if medical record already exist
        if (medicalRecordsRepository.findAny(medicalrecords.getId()).isPresent()) {
            throw new ItemAlreadyExists("The medical record ID already exist: " + medicalrecords.getId());
        }
        // control if person exist
        else if (personRepository.findById(medicalrecords.getId()).isEmpty()) {
            throw new ItemNotFoundException("Person not found, create person before his medical record: " + medicalrecords.getId());
        } else {
            log.info("Medical record created with success");
            return medicalRecordsRepository.save(medicalrecords);
        }

    }


    public void update(String id, MedicalRecord medicalrecords) {

        MedicalRecord medicalRecordToUpdate = medicalRecordsRepository.getMedicalRecordById(medicalrecords.getId());

        if (medicalrecords.getId().equalsIgnoreCase(id)) {
            medicalRecordToUpdate.setBirthdate(medicalrecords.getBirthdate());
            medicalRecordToUpdate.setMedications(medicalrecords.getMedications());
            medicalRecordToUpdate.setAllergies(medicalrecords.getAllergies());
            log.info("Medical record updated with success: ID=" + id);
        } else {
            throw new ItemNotFoundException("The @PathVariable ID different to the @RequestBody ID");
        }
    }

    public void delete(String id) {
        medicalRecordsRepository.delete(id);
        log.info("Person deleted with success:  ID=" + id);
    }


}
