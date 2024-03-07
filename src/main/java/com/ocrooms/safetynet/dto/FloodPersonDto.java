package com.ocrooms.safetynet.dto;

import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import lombok.Data;

import java.util.List;

@Data
public class FloodPersonDto {
    String lastName;
    Background background;
    String phone;
    Integer age;

    public FloodPersonDto(Person person, MedicalRecord medicalrecords) {
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
        this.age = medicalrecords.calculateAge();
        this.background = new Background(medicalrecords.getMedications(), medicalrecords.getAllergies());
    }

    @Data
    public static class Background {
        private List<String> medications;
        private List<String> allergies;

        public Background(List<String> medications, List<String> allergies) {
            this.medications = medications;
            this.allergies = allergies;
        }
    }
}
