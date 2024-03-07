package com.ocrooms.safetynet.repository;

import com.ocrooms.safetynet.dto.PersonDto;
import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.service.JsonService;
import com.ocrooms.safetynet.service.exceptions.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MedicalRecordsRepository {

    private final JsonService jsonService;

    public Set<MedicalRecord> getAll() {
        return jsonService.getData().getMedicalrecords();
    }

    public Stream<MedicalRecord> findAll() {
        return getAll().parallelStream();
    }

    public Optional<MedicalRecord> findAny(String id) {
        return findAll()
                .filter(medicalRecord -> medicalRecord.getId().equalsIgnoreCase(id)).findAny();
    }

    public Optional<MedicalRecord> findById(String id) {
        return findAll()
                .filter(medicalRecord -> medicalRecord.getId().equalsIgnoreCase(id))
                .reduce((a, b) -> {
                    throw new RuntimeException("The medicalrecords ID is not found: " + id);
                });
    }

    public MedicalRecord getMedicalRecordById(String id) {
        return findById(id)
                .orElseThrow(() -> new ItemNotFoundException("The person ID is not found : " + id));
    }

    public MedicalRecord save(MedicalRecord medicalrecords) {
        try {
            this.getAll().add(medicalrecords);
            log.info("new medical record saved with success" + medicalrecords);
            return medicalrecords;
        } catch (Exception e) {
            throw new RuntimeException("Error: New medical record no save");
        }
    }

    public void delete(String id) {
        getMedicalRecordById(id);
        if (!getAll().removeIf(medicalRecord -> medicalRecord.getId().equalsIgnoreCase(id))) {
            throw new RuntimeException("Error the medical record is not deleted" + id);
        }
    }

    public Stream<MedicalRecord> findAllByPerson(Person person){
        return findAll()
                .filter(medicalrecords -> medicalrecords.getId().equals(person.getId()));
    }

    public Stream<MedicalRecord> findAllByPersonDto(PersonDto personDto){
        return findAll()
                .filter(medicalrecords -> medicalrecords.getId().equals(personDto.getId()));
    }


}
