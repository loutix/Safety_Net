package com.ocrooms.safetynet.dto;

import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import lombok.Data;

import java.util.List;

@Data
public class PersonInfoDto {
    String firstName;
    String lastName;
    String address;
    Integer age;
    String email;
    List<String> medications;
    List<String> allergies;


    public PersonInfoDto(Person person, MedicalRecord medicalrecords) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.age = medicalrecords.calculateAge();
        this.email = person.getEmail();
        this.medications = medicalrecords.getMedications();
        this.allergies = medicalrecords.getAllergies();
    }
}
