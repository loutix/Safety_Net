package com.ocrooms.safetynet.mapper;

import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataObject {
    Set<Person> persons;
    Set<Firestation> firestations;
    Set<MedicalRecord> medicalrecords;
}

