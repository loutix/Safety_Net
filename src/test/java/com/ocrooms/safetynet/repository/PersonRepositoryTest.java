package com.ocrooms.safetynet.repository;

import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.mapper.DataObject;
import com.ocrooms.safetynet.service.JsonService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonRepositoryTest {

    PersonRepository personRepository;
    @Mock
    JsonService jsonService;
    Set<Person> personSet = new HashSet<>();
    Person person1 = new Person();
    Person person2 = new Person();
    Person person3 = new Person();

    @BeforeEach
    public void init() {
        personRepository = new PersonRepository(jsonService);

        person1.setFirstName("user1");
        person1.setLastName("spring");
        person1.setAddress("11 rue des champs");
        person1.setCity("Paris");
        person1.setZip("222222");
        person1.setPhone("888-8888-8888");
        person1.setEmail("user1@gmail.com");

        person2.setFirstName("user2");
        person2.setLastName("summer");
        person2.setAddress("33 rue des champs");
        person2.setCity("Marseille");
        person2.setZip("33333");
        person2.setPhone("9999-9999");
        person2.setEmail("user2@gmail.com");

        person3.setFirstName("user2");
        person3.setLastName("summer");
        person3.setAddress("33 rue des champs");
        person3.setCity("Marseille");
        person3.setZip("33333");
        person3.setPhone("9999-9999");
        person3.setEmail("user2@gmail.com");

        personSet.add(person1);
        personSet.add(person2);
        personSet.add(person3);
    }

    @Test
    @DisplayName("Test Show all persons")
    public void getAll() {

        //WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        Set<Person> result = personRepository.getAll();
        //THEN
        assertEquals(personSet, result);
        verify(jsonService, times(1)).getData();
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        //GIVEN

        //WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        Stream<Person> resultStream = personRepository.findAll();
        Set<Person> result = resultStream.collect(Collectors.toSet());

        //THEN
        assertEquals(personSet, result);
        verify(jsonService, times(1)).getData();

    }

    @Test
    @DisplayName("Test findAny-found")
    void testFindAnyPersonFound() {
        //GIVEN
        String id = "user1-spring";

        // WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        Optional<Person> result = personRepository.findAny(id);

        //THEN
        assertTrue(result.isPresent());
        assertEquals(Optional.of(person1), result);
        verify(jsonService, times(1)).getData();

    }

    @Test
    @DisplayName("Test findAny-not-found")
    void testFindAnyPersonNotFound() {
        //GIVEN
        String id = "user2-spring";

        // WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        Optional<Person> result = personRepository.findAny(id);

        //THEN
        assertTrue(result.isEmpty());
        assertEquals(Optional.empty(), result);
        verify(jsonService, times(1)).getData();

    }

    @Test
    @DisplayName("TestFindById-found")
    void testFindByAddressFound() {
        //GIVEN
        String id = "user1-spring";

        // WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        Optional<Person> result = personRepository.findById(id);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(Optional.of(person1), result);
        verify(jsonService, times(1)).getData();
    }

    @Test
    @DisplayName("TestGetByAddress-found")
    void testGetByAddressFound() {
        //GIVEN
        String id = "user1-spring";

        // WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));

        Person result = personRepository.getPersonById(id);
        // THEN
        assertEquals(person1, result);
        verify(jsonService, times(1)).getData();

    }

    @Test
    @DisplayName("TestGetByAddress-found")
    void testGetByAddressNotFound() {
        //GIVEN
        String id = "user2-spring";

        // WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        // THEN
        assertThrows(ItemNotFoundException.class, () -> personRepository.getPersonById(id));
        verify(jsonService, times(1)).getData();

    }

    @Test
    @DisplayName("testSave")
    void save() {
        //GIVEN
        Person person4 = new Person();
        person4.setFirstName("user3");
        person4.setLastName("summer2");
        person4.setAddress("33 rue des champs");
        person4.setCity("Marseille");
        person4.setZip("33333");
        person4.setPhone("9999-9999");
        person4.setEmail("user2@gmail.com");

        // WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        Person result = personRepository.save(person4);
        Person getResult = personRepository.getPersonById("user3-summer2");

        // THEN
        assertEquals(person4, result);
        assertEquals(person4, getResult);
        verify(jsonService, times(2)).getData();
    }

    @Test
    @DisplayName("testDelete")
    void delete() {
        //GIVEN
        String id = "user1-spring";
        // WHEN
        when(jsonService.getData()).thenReturn(new DataObject(personSet, null, null));
        personRepository.delete(id);
        Optional<Person> result = personRepository.findById(id);

        // THEN
        assertTrue(result.isEmpty());
        verify(jsonService, times(2)).getData();
    }


}