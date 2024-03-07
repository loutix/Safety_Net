package com.ocrooms.safetynet.service;

import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.repository.PersonRepository;
import com.ocrooms.safetynet.service.exceptions.ItemAlreadyExists;
import com.ocrooms.safetynet.service.exceptions.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Set<Person> index() {
        return personRepository.getAll();
    }


    public Person show(String id) {
        return personRepository.getPersonById(id);
    }


    public Person create(Person person) {
        
        if (personRepository.findAny(person.getId()).isPresent()) {
            throw new ItemAlreadyExists("The person ID already exist : " + person.getId());
        } else {
            log.info("Person created with success") ;
            return personRepository.save(person);
        }
    }

    public void update(String id, Person person) {

        if (!id.equalsIgnoreCase(person.getId())) {
            throw new ItemNotFoundException("The route ID is different to @RequestBody ID");
        }

        Person personToUpdate = personRepository.getPersonById(id);

        person.trimProperties();

        personToUpdate.setAddress(person.getAddress());
        personToUpdate.setCity(person.getCity());
        personToUpdate.setZip(person.getZip());
        personToUpdate.setPhone(person.getPhone());
        personToUpdate.setEmail(person.getEmail());

        log.info("Person updated with success: ID=" + id);

    }

    public void deletePerson(String id) {
        personRepository.delete(id);
        log.info("Person deleted with success:  ID=" + id);
    }
}

