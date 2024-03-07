package com.ocrooms.safetynet.service;

import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.repository.MedicalRecordsRepository;
import com.ocrooms.safetynet.repository.PersonRepository;
import com.ocrooms.safetynet.service.exceptions.ItemAlreadyExists;
import com.ocrooms.safetynet.service.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    public MedicalRecordService medicalRecordService;


    @Mock
    MedicalRecordsRepository medicalRecordsRepository;

    @Mock
    PersonRepository personRepository;

    Set<MedicalRecord> medicalRecordSet = new HashSet<>();
    MedicalRecord medicalRecord1 = new MedicalRecord();
    MedicalRecord medicalRecord2 = new MedicalRecord();
    Person person1 = new Person();

    @BeforeEach
    public void init() {
        this.medicalRecordService = new MedicalRecordService(medicalRecordsRepository, personRepository);

        medicalRecord1.setFirstName("user1");
        medicalRecord1.setLastName("spring");
        medicalRecord1.setBirthdate(LocalDate.of(2000, 1, 2));
        medicalRecord1.setMedications(new ArrayList<>(List.of("aznol:350mg", "hydrapermazol:100mg")));
        medicalRecord1.setAllergies(new ArrayList<>(List.of("nillacilan")));

        medicalRecord2.setFirstName("user2");
        medicalRecord2.setLastName("summer");
        medicalRecord2.setBirthdate(LocalDate.of(2001, 3, 4));
        medicalRecord2.setMedications(new ArrayList<>());
        medicalRecord2.setAllergies(new ArrayList<>());

        medicalRecordSet.add(medicalRecord1);
        medicalRecordSet.add(medicalRecord2);

        person1.setFirstName("user1");
        person1.setLastName("spring");
        person1.setAddress("11 rue des champs");
        person1.setCity("Londres");
        person1.setZip("222222");
        person1.setPhone("888-8888-8888");
        person1.setEmail("user1@gmail.com");
    }


    @Test
    @DisplayName("Get all medical records")
    public void indexMedicalRecords() {
        //GIVEN
        //WHEN
        when(medicalRecordsRepository.getAll()).thenReturn(medicalRecordSet);
        Set<MedicalRecord> allMedicalRecords = medicalRecordService.index();

        //THEN
        assertEquals(medicalRecordSet, allMedicalRecords);
        verify(medicalRecordsRepository, times(1)).getAll();

    }

    @Test
    @DisplayName("Show a medical record")
    public void showMedicalRecord() {
        //GIVEN
        String id = "user1-spring";
        //WHEN
        when(medicalRecordsRepository.getMedicalRecordById(id)).thenReturn(medicalRecord1);
        MedicalRecord medicalRecordShow = medicalRecordService.show(id);

        //THEN
        assertEquals(medicalRecord1, medicalRecordShow);
        verify(medicalRecordsRepository, times(1)).getMedicalRecordById(id);

    }

    @Test
    @DisplayName("Create a medical record")
    public void createMedicalRecord() {
        //GIVEN
        MedicalRecord newMr = new MedicalRecord();

        newMr.setFirstName("user3");
        newMr.setLastName("Winter");
        newMr.setBirthdate(LocalDate.of(2003, 4, 5));
        newMr.setMedications(new ArrayList<>());
        newMr.setAllergies(new ArrayList<>());


        //WHEN
        when(medicalRecordsRepository.findAny(anyString())).thenReturn(Optional.empty());
        when(personRepository.findById(anyString())).thenReturn(Optional.of(person1));
        when(medicalRecordsRepository.save(newMr)).thenReturn(newMr);

        MedicalRecord medicalRecordCreated = medicalRecordService.create(newMr);

        //THEN
        assertEquals(newMr, medicalRecordCreated);
        verify(medicalRecordsRepository, times(1)).findAny(anyString());
        verify(personRepository, times(1)).findById(anyString());
        verify(medicalRecordsRepository, times(1)).save(newMr);

    }

    @Test
    @DisplayName("Medical record already exist and can not be created")
    public void canNotCreateMedicalRecordAlreadyExist() {
        //GIVEN

        //WHEN
        when(medicalRecordsRepository.findAny(anyString())).thenReturn(Optional.of(medicalRecord1));

        //THEN
        assertThrows(ItemAlreadyExists.class, () -> medicalRecordService.create(medicalRecord1));
        verify(medicalRecordsRepository, times(1)).findAny(anyString());
    }

    @Test
    @DisplayName("Person not found and medical record can not be created")
    public void canNotCreateMedicalRecord() {
        //GIVEN

        //WHEN
        when(personRepository.findById(anyString())).thenReturn(Optional.empty());
        //THEN
        assertThrows(ItemNotFoundException.class, () -> medicalRecordService.create(medicalRecord1));
        verify(personRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Update a medical record")
    public void updateMedicalRecord() {
        //GIVEN
        String id = "user1-spring";

        MedicalRecord mrToUpdate = medicalRecord1;

        MedicalRecord bodyUpdate = new MedicalRecord();
        bodyUpdate.setFirstName("user1");
        bodyUpdate.setLastName("spring");
        bodyUpdate.setBirthdate(LocalDate.of(1900, 11, 12));
        bodyUpdate.setMedications(new ArrayList<>(List.of("aspirine")));
        bodyUpdate.setAllergies(new ArrayList<>(List.of("choux")));


        //updated
        mrToUpdate.setBirthdate(bodyUpdate.getBirthdate());
        mrToUpdate.setMedications(bodyUpdate.getMedications());
        mrToUpdate.setAllergies(bodyUpdate.getAllergies());


        //WHEN
        when(medicalRecordsRepository.getMedicalRecordById(id)).thenReturn(mrToUpdate);
        medicalRecordService.update(id, mrToUpdate);

        //THEN
        verify(medicalRecordsRepository, times(1)).getMedicalRecordById(id);

    }

    @Test
    @DisplayName("Path variable is different to the medical record ID")
    public void canNotUpdateMedicalRecord() {
        //GIVEN
        String id = "user1-spring";

        //WHEN

        //THEN
        assertThrows(ItemNotFoundException.class, () -> medicalRecordService.update(id, medicalRecord2));
    }


    @Test
    @DisplayName("Delete a person")
    public void deleteMedicalRecord() {

        String id = "user1-spring";
        doNothing().when(medicalRecordsRepository).delete(id);
        medicalRecordService.delete(id);
        verify(medicalRecordsRepository, times(1)).delete(id);

    }


}