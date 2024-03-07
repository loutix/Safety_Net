package com.ocrooms.safetynet.service;

import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.repository.PersonRepository;
import com.ocrooms.safetynet.service.exceptions.ItemAlreadyExists;
import com.ocrooms.safetynet.service.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    PersonService personService;
    @Mock
    PersonRepository personRepository;

    Person person1 = new Person();
    Person person2 = new Person();

    Set<Person> personSet = new HashSet<>();

    @BeforeEach
    public void init() {
        personService = new PersonService(personRepository);

        person1.setFirstName("user1");
        person1.setLastName("spring");
        person1.setAddress("11 rue des champs");
        person1.setCity("Londres");
        person1.setZip("222222");
        person1.setPhone("888-8888-8888");
        person1.setEmail("user1@gmail.com");

        person2.setFirstName("user2");
        person2.setLastName("summer");
        person2.setAddress("33 rue des champs");
        person2.setCity("milan");
        person2.setZip("33333");
        person2.setPhone("9999-9999");
        person2.setEmail("user2@gmail.com");

        personSet.add(person1);
        personSet.add(person2);
    }


    @Test
    @DisplayName("Show all persons")
    public void indexPerson() {
        //WHEN
        when(personRepository.getAll()).thenReturn(personSet);
        Set<Person> allPersons = personService.index();

        //THEN
        assertEquals(personSet, allPersons);
        verify(personRepository, times(1)).getAll();
    }

    @Test
    @DisplayName("Show a person")
    public void ShowPerson() {
        String id = "user1-spring";
        when(personRepository.getPersonById(any())).thenReturn(person1);
        Person personShow = personService.show(id);
        assertEquals(person1, personShow);
        verify(personRepository,times(1)).getPersonById(id);

    }

    @Test
    @DisplayName("Create a new person")
    public void createNewPerson() {
        //GIVEN
        Person newPerson = new Person();
        newPerson.setFirstName("Jojo");
        newPerson.setLastName("Winter");
        newPerson.setAddress("12 rue des bois");
        newPerson.setCity("Kansas");
        newPerson.setZip("111111");
        newPerson.setPhone("123-456-789");
        newPerson.setEmail("jojo@gmail.com");

        //WHEN
        when(personRepository.findAny(anyString())).thenReturn(Optional.empty());
        when(personRepository.save(newPerson)).thenReturn(newPerson);
        Person personCreated = personService.create(newPerson);

        //THEN
        assertEquals(newPerson, personCreated);
        verify(personRepository,times(1)).findAny(anyString());
        verify(personRepository,times(1)).save(newPerson);
    }

    @Test
    @DisplayName("Person already exist and can not be created")
    public void canNotCreatePersonAlreadyExist() {
        //GIVEN

        //WHEN
        when(personRepository.findAny(anyString())).thenReturn(Optional.of(person1));

        //THEN
        assertThrows(ItemAlreadyExists.class, ()->personService.create(person1));
        verify(personRepository,times(1)).findAny(anyString());
    }

    @Test
    @DisplayName("Update a new person")
    public void updatePerson() {

        //GIVEN
        String id = "user1-spring";
        Person person = person1;
        //WHEN
        when(personRepository.getPersonById(id)).thenReturn(person);
        personService.update(id, person);
        //THEN
        verify(personRepository, times(1)).getPersonById(id);
    }

    @Test
    @DisplayName("Person is not found to be updated")
    public void canNotUpdatePersonNotFound() {
        //GIVEN
        String id = "user2-winter";
        Person person = person1;
        //THEN
        assertThrows(ItemNotFoundException.class, ()->personService.update(id, person));
    }

    @Test
    @DisplayName("Delete a person")
    public void deletePerson() {
        //GIVEN
        String id = "user1-spring";
        //WHEN
        doNothing().when(personRepository).delete(id);
        personService.deletePerson(id);
        //THEN
        verify(personRepository, times(1)).delete(id);
    }


}
