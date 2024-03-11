package com.ocrooms.safetynet.repository;

import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.service.exceptions.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FireStationRepositoryTest {

    FireStationRepository fireStationRepository;
    Set<Firestation> fireStationSet = new HashSet<>();
    Firestation fireStation1 = new Firestation();
    Firestation fireStation2 = new Firestation();
    Firestation fireStation3 = new Firestation();
    Firestation fireStation4 = new Firestation();
    Firestation fireStation5 = new Firestation();

    @BeforeEach
    public void init() {
        fireStation1.setAddress("Paris");
        fireStation1.setStation(1);

        fireStation2.setAddress("Marseille");
        fireStation2.setStation(2);

        fireStation3.setAddress("Marseille");
        fireStation3.setStation(3);

        fireStation4.setAddress("Perpignan");
        fireStation4.setStation(1);

        fireStation5.setAddress("Perpignan");
        fireStation5.setStation(1);

        fireStationSet.add(fireStation1);
        fireStationSet.add(fireStation2);
        fireStationSet.add(fireStation3);
        fireStationSet.add(fireStation4);
        fireStationSet.add(fireStation5);

        fireStationRepository = new FireStationRepository(fireStationSet);
    }

    @Test
    @DisplayName("Test Show all fire stations")
    public void getAll() {

        //GIVEN

        //WHEN
        Set<Firestation> result = fireStationRepository.getAll();
        //THEN
        assertEquals(fireStationSet, result);
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        //GIVEN

        //WHEN
        Stream<Firestation> resultStream = fireStationRepository.findAll();
        Set<Firestation> result = resultStream.collect(Collectors.toSet());

        //THEN
        assertEquals(fireStationSet, result);
        assertEquals(fireStationSet.size(), result.size());
        assertTrue(fireStationSet.contains(fireStation1));
        assertTrue(fireStationSet.contains(fireStation2));
        assertTrue(fireStationSet.contains(fireStation3));
        assertTrue(fireStationSet.contains(fireStation4));
        assertTrue(fireStationSet.contains(fireStation5));

    }


    @Test
    @DisplayName("Test findAny-found")
    void testFindAnyFirestationFound() {
        //GIVEN
        Firestation fireStationToFind = new Firestation("Paris", 1);

        // WHEN
        Optional<Firestation> result = fireStationRepository.findAny(fireStationToFind);

        //THEN
        assertTrue(result.isPresent());
        assertEquals(Optional.of(fireStationToFind), result);
    }

    @Test
    @DisplayName("Test findAny-not-found")
    void testFindAnyFirestationNotFound() {
        //GIVEN
        Firestation fireStationToFind = new Firestation("new york", 10);

        // WHEN
        Optional<Firestation> result = fireStationRepository.findAny(fireStationToFind);

        //THEN
        assertTrue(result.isEmpty());
        assertEquals(Optional.empty(), result);

    }

    @Test
    @DisplayName("TestfindByAddress-found")
    void testFindByAddressFound() {
        //GIVEN
        String address = "Paris";

        // WHEN
        Optional<Firestation> result = fireStationRepository.findByAddress(address);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(Optional.of(fireStation1), result);
    }

    @Test
    @DisplayName("TestfindByAddress-Multiple responses")
    void testFindByAddressNotMultipleResponses() {
        //GIVEN
        String address = "Marseille";

        // WHEN

        // THEN
        assertThrows(RuntimeException.class, () -> fireStationRepository.findByAddress(address));

    }

    @Test
    @DisplayName("TestGetByAddress-found")
    void testGetByAddressFound() {

        //GIVEN
        String address = "Paris";

        // WHEN
        Firestation result = fireStationRepository.getByAddress(address);

        // THEN
        assertEquals(fireStation1, result);
    }

    @Test
    @DisplayName("TestGetByAddress-not-found")
    void testGetByAddressNotFound() {
        //GIVEN
        String address = "Londres";

        // WHEN

        // THEN
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> fireStationRepository.getByAddress(address));
        assertEquals("The fire station address is not found : " + address, thrown.getMessage());
    }

    @Test
    @DisplayName("testFindByAddressAndStation-found")
    void testFindByAddressAndStationFound() {
        //GIVEN
        String address = "Paris";
        Integer station = 1;
        // WHEN
        Optional<Firestation> result = fireStationRepository.findByAddressAndStation(address, station);

        // THEN
        assertTrue(result.isPresent());
        assertEquals(Optional.of(fireStation1), result);
    }

    @Test
    @DisplayName("testGetByAddressAndStation-found")
    void getByAddressAndStation() {
        //GIVEN
        String address = "Paris";
        Integer station = 1;
        // WHEN
        Firestation result = fireStationRepository.getByAddressAndStation(address, station);

        // THEN
        assertEquals(fireStation1, result);
    }

    @Test
    @DisplayName("testGetByAddressAndStation-not-found")
    void getByAddressAndStationNotFound() {
        //GIVEN
        String address = "Londres";
        Integer station = 10;
        // WHEN

        // THEN
        RuntimeException thrown = assertThrows(ItemNotFoundException.class, () -> fireStationRepository.getByAddressAndStation(address, station));
        assertEquals("The fire station address is not found : " + address + "with station number :" + station, thrown.getMessage());
    }

    @Test
    @DisplayName("testFindAllByStation")
    void findAllByStation() {
        //GIVEN
        Integer station = 1;
        // WHEN
        Stream<Firestation> result = fireStationRepository.findAllByStation(station);
        List<Firestation> resultToList = result.toList();

        // THEN
        assertEquals(2, resultToList.size());
        assertTrue(resultToList.contains(fireStation1));
        assertTrue(resultToList.contains(fireStation4));
        assertTrue(resultToList.contains(fireStation5));
    }

    @Test
    @DisplayName("testFindAllByPerson")
    void findAllByPerson() {
        //GIVEN
        Person person = new Person();
        person.setFirstName("user1");
        person.setLastName("spring");
        person.setAddress("Marseille");
        person.setCity("Londres");
        person.setZip("222222");
        person.setPhone("888-8888-8888");
        person.setEmail("user1@gmail.com");

        // WHEN
        Stream<Firestation> result = fireStationRepository.findAllByPerson(person);
        List<Firestation> resultToList = result.toList();

        // THEN
        assertEquals(2, resultToList.size());
        assertTrue(resultToList.contains(fireStation2));
        assertTrue(resultToList.contains(fireStation3));
        assertEquals(fireStation2, resultToList.get(0));
        assertEquals(fireStation3, resultToList.get(1));
    }

    @Test
    @DisplayName("testSave")
    void save() {
        //GIVEN
        Firestation firestation6 = new Firestation("Tokyo", 11);
        // WHEN
        Firestation result = fireStationRepository.save(firestation6);
        Firestation getResult = fireStationRepository.getByAddress("Tokyo");

        // THEN
        assertEquals(firestation6, result);
        assertEquals(firestation6, getResult);
    }

    @Test
    @DisplayName("testDelete")
    void delete() {
        //GIVEN
        String address = "Paris";
        Integer station = 1;
        // WHEN
        fireStationRepository.delete(address, station);
        Optional<Firestation> result = fireStationRepository.findByAddressAndStation(address, 1);

        // THEN
        assertTrue(result.isEmpty());
    }

}