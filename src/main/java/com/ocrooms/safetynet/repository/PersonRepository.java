package com.ocrooms.safetynet.repository;

import com.ocrooms.safetynet.entities.Firestation;
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
public class PersonRepository {

    private final JsonService jsonService;

    public Set<Person> getAll() {
        return jsonService.getData().getPersons();
    }

    public Stream<Person> findAll() {
        return getAll().parallelStream();
    }

    public Optional<Person> findAny(String id) {
        return findAll()
                .filter(person -> person.getId().equalsIgnoreCase(id)).findAny();
    }

    public Optional<Person> findById(String id) {
        return findAll()
                .filter(person -> person.getId().equalsIgnoreCase(id))
                .reduce((a, b) -> {
                    throw new RuntimeException("The person ID is not unique: " + id);
                });
    }

    public Person getPersonById(String id) {
        return findById(id)
                .orElseThrow(() -> new ItemNotFoundException("The person ID is not found : " + id));
    }

    public Person save(Person person) {
        this.getAll().add(person);
        log.info("new person saved with success" + person);
        return person;
    }

    public void delete(String id) {
        getAll().removeIf(person -> person.getId().equalsIgnoreCase(id));
    }


    public Stream<Person> findAllByAddress(String address) {
        return findAll()
                .filter(person -> person.getAddress().equals(address));
    }

    public Stream<Person> findAllByCity(String city) {
        return findAll()
                .filter(person -> person.getCity().equals(city));
    }

    public Stream<Person> findAllByLastName(String lastName) {
        return findAll()
                .filter(person -> person.getLastName().equals(lastName));
    }

    public Stream<Person> findAllByFireStation(Firestation firestation) {
        return findAll()
                .filter(person -> person.getAddress().equals(firestation.getAddress()));
    }

    public Stream<Person> findAllByMedicalRecordLastName(MedicalRecord medicalrecords) {
        return findAll()
                .filter(per -> per.getLastName().equals(medicalrecords.getLastName()));
    }

}
