package com.ocrooms.safetynet.dto;

import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import lombok.Data;

import java.util.List;

@Data
public class ChildDto {
    String firstName;
    String lastName;
    int age;

    List<Person> family;

    public ChildDto(MedicalRecord medicalrecords, List<Person> family) {
        this.firstName = medicalrecords.getFirstName();
        this.lastName = medicalrecords.getLastName();
        this.age =medicalrecords.calculateAge();
        this.family = family;
    }

}
