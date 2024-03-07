package com.ocrooms.safetynet.dto;

import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import lombok.Data;

import java.util.List;

@Data
public class PersonAddressStationDto {

    String lastName;
    String phone;
    int age;
    List<Integer> stationNumber;
    List<String> medications;
    List<String> allergies;

    public PersonAddressStationDto(Person person, List<Integer> stationNumber, MedicalRecord medicalrecords) {
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
        this.age = medicalrecords.calculateAge();
        this.stationNumber = stationNumber;
        this.medications = medicalrecords.getMedications();
        this.allergies = medicalrecords.getAllergies();
    }
}
