
package com.ocrooms.safetynet.service;

import com.ocrooms.safetynet.dto.*;
import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.repository.FireStationRepository;
import com.ocrooms.safetynet.repository.MedicalRecordsRepository;
import com.ocrooms.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    SecurityService securityService;

    @Mock
    FireStationRepository fireStationRepository;
    @Mock
    PersonRepository personRepository;

    @Mock
    MedicalRecordsRepository medicalRecordsRepository;

    Set<Firestation> firestationSet = new HashSet<>();
    Firestation fireStation1 = new Firestation();
    Firestation fireStation2 = new Firestation();

    private Person person1;
    private Person person2;
    private Person person3;

    //    Set<MedicalRecord> medicalRecordSet = new HashSet<>();
    private MedicalRecord medicalRecord1;
    private MedicalRecord medicalRecord2;

    private MedicalRecord medicalRecord3;

    @BeforeEach
    public void init() {
        securityService = new SecurityService(fireStationRepository, personRepository, medicalRecordsRepository);

        fireStation1.setAddress("11 rue des champs");
        fireStation1.setStation(1);

        fireStation2.setAddress("33 rue des champs");
        fireStation2.setStation(2);

        firestationSet.add(fireStation1);
        firestationSet.add(fireStation2);


        person1 = new Person();
        person1.setFirstName("user1");
        person1.setLastName("spring");
        person1.setAddress("11 rue des champs");
        person1.setCity("Londres");
        person1.setZip("222222");
        person1.setPhone("888-8888-8888");
        person1.setEmail("user1@gmail.com");

        person2 = new Person();
        person2.setFirstName("user2");
        person2.setLastName("summer");
        person2.setAddress("33 rue des champs");
        person2.setCity("milan");
        person2.setZip("33333");
        person2.setPhone("9999-9999");
        person2.setEmail("user2@gmail.com");

        person3 = new Person();
        person3.setFirstName("user3");
        person3.setLastName("winter");
        person3.setAddress("11 rue des champs");
        person3.setCity("bagdad");
        person3.setZip("00112333");
        person3.setPhone("123123-123123");
        person3.setEmail("user3@gmail.com");

        medicalRecord1 = new MedicalRecord();
        medicalRecord1.setFirstName("user1");
        medicalRecord1.setLastName("spring");
        medicalRecord1.setBirthdate(LocalDate.of(2010, 1, 2)); // Assuming a minor
        medicalRecord1.setMedications(new ArrayList<>());
        medicalRecord1.setAllergies(new ArrayList<>());

        medicalRecord2 = new MedicalRecord();
        medicalRecord2.setFirstName("user2");
        medicalRecord2.setLastName("summer");
        medicalRecord2.setBirthdate(LocalDate.of(2000, 2, 3)); // Assuming an adult
        medicalRecord2.setMedications(new ArrayList<>());
        medicalRecord2.setAllergies(new ArrayList<>());

        medicalRecord3 = new MedicalRecord();
        medicalRecord3.setFirstName("user3");
        medicalRecord3.setLastName("winter");
        medicalRecord3.setBirthdate(LocalDate.of(2015, 3, 4)); // Assuming an adult
        medicalRecord3.setMedications(new ArrayList<>());
        medicalRecord3.setAllergies(new ArrayList<>());


    }

    @Test
    @DisplayName("Return a list of persons who are served by the station number")
    public void searchFireStation() {
        // GIVEN
        Integer station = 1;

        PersonDto personDto1 = new PersonDto(person1);
        PersonDto personDto2 = new PersonDto(person3);

        List<PersonDto> personDtoList = new ArrayList<>(List.of(personDto1, personDto2));
        PersonListDto personListDto = new PersonListDto(personDtoList, 2);

        // WHEN
        when(fireStationRepository.findAllByStation(station)).thenReturn(Stream.of(fireStation1));
        when(personRepository.findAll()).thenReturn(Stream.of(person1, person3));
        when(medicalRecordsRepository.findAllByPerson(person1)).thenReturn(Stream.of(medicalRecord1));
        when(medicalRecordsRepository.findAllByPerson(person3)).thenReturn(Stream.of(medicalRecord3));

        // WHEN
        PersonListDto result = securityService.searchFirestation(station);
        System.out.println(result.getPersonDtoList());
        // THEN
        assertEquals(2, result.getPersonDtoList().size());
        assertEquals(2, result.getNbrMinor());
        assertEquals(personListDto.getPersonDtoList(), result.getPersonDtoList());

        verify(fireStationRepository, times(1)).findAllByStation(station);
        verify(personRepository, times(1)).findAll();
        verify(medicalRecordsRepository, times(2)).findAllByPerson(any());
        verify(medicalRecordsRepository, times(2)).findAllByPersonDto(any());
    }


    @Test
    @DisplayName("Return a children list living at this address")
    public void searchChildAlert() {
        // GIVEN
        String address = "11 rue des champs";

        when(personRepository.findAllByAddress(address)).thenReturn(Stream.of(person1, person3));
        when(medicalRecordsRepository.findAllByPerson(person1)).thenReturn(Stream.of(medicalRecord1));
        when(medicalRecordsRepository.findAllByPerson(person3)).thenReturn(Stream.of(medicalRecord3));

        List<ChildDto> result = securityService.searchChildAlert(address);

        // THEN
        assertEquals(2, result.size());
        ChildDto childDto1 = result.getFirst();
        assertEquals("user1", childDto1.getFirstName());
        assertEquals("spring", childDto1.getLastName());
        assertEquals(14, childDto1.getAge());

        ChildDto childDto2 = result.get(1);
        assertEquals("user3", childDto2.getFirstName());
        assertEquals("winter", childDto2.getLastName());

        verify(personRepository, times(1)).findAllByAddress(address);
        verify(medicalRecordsRepository, times(1)).findAllByPerson(person1);
        verify(medicalRecordsRepository, times(1)).findAllByPerson(person3);
    }

    @Test
    @DisplayName("Return a list of phone numbers served by the fire station number")
    public void searchPhoneAlert() {
        // GIVEN
        Integer station = 1;

        when(fireStationRepository.findAllByStation(station)).thenReturn(Stream.of(fireStation1));
        when(personRepository.findAllByFireStation(fireStation1)).thenReturn(Stream.of(person1, person3));

        // WHEN
        Set<String> result = securityService.searchPhoneAlert(station);

        // THEN
        assertEquals(2, result.size());

        verify(fireStationRepository, times(1)).findAllByStation(station);
        verify(personRepository, times(1)).findAllByFireStation(fireStation1);
    }

    @Test
    @DisplayName("Return a list of persons living at this address")
    public void searchFire() {

        List<Integer> stationNumber = new ArrayList<>(fireStation1.getStation());

        //GIVEN
        String address = "11 rue des champs";

        Stream<Firestation> firestationStream = Stream.of(fireStation1, fireStation2);
        PersonAddressStationDto personDto1 = new PersonAddressStationDto(person1, stationNumber, medicalRecord1);
        PersonAddressStationDto personDto2 = new PersonAddressStationDto(person3, stationNumber, medicalRecord3);
        List<PersonAddressStationDto> dtoList = new ArrayList<>(List.of(personDto1, personDto2));

        //WHEN
        when(fireStationRepository.findAll()).thenReturn(firestationStream);
        when(personRepository.findAllByAddress(address)).thenReturn(Stream.of(person1, person3));
        when(medicalRecordsRepository.findAllByPerson(person1)).thenReturn(Stream.of(medicalRecord1));
        when(medicalRecordsRepository.findAllByPerson(person3)).thenReturn(Stream.of(medicalRecord3));

        List<PersonAddressStationDto> result = securityService.searchFire(address);

        // THEN
        assertEquals(dtoList.size(), result.size());

        PersonAddressStationDto dto1 = result.get(0);
        PersonAddressStationDto dto2 = result.get(1);

        assertEquals("spring", dto1.getLastName());
        assertEquals("888-8888-8888", dto1.getPhone());

        assertEquals("winter", dto2.getLastName());
        assertEquals("123123-123123", dto2.getPhone());

        verify(fireStationRepository, times(1)).findAll();
        verify(personRepository, times(1)).findAllByAddress(address);
        verify(medicalRecordsRepository, times(1)).findAllByPerson(person1);
        verify(medicalRecordsRepository, times(1)).findAllByPerson(person3);
    }

    @Test
    @DisplayName("Return  a list persons served by the fire station List number.")
    public void searchFlood() {

        //GIVEN
        List<Integer> stations = new ArrayList<>(List.of(2));
        FloodPersonDto floodPersonDto = new FloodPersonDto(person2, medicalRecord2);
        List<FloodPersonDto> floodPersonDtoList = new ArrayList<>(List.of(floodPersonDto));
        //WHEN
        when(fireStationRepository.findAll()).thenReturn(firestationSet.stream());
        when(personRepository.findAllByAddress(anyString())).thenReturn(Stream.of(person2));
        when(medicalRecordsRepository.findAllByPerson(person2)).thenReturn(Stream.of(medicalRecord2));

        List<FloodDto> result = securityService.searchFlood(stations);

        //THEN
        assertEquals(1, result.size());
        FloodDto dto = result.getFirst();

        assertEquals("33 rue des champs", dto.getAddress());
        assertEquals(floodPersonDtoList, dto.getPersonListFlooded());

        verify(fireStationRepository, times(1)).findAll();
        verify(personRepository, times(1)).findAllByAddress(anyString());
        verify(medicalRecordsRepository, times(1)).findAllByPerson(person2);

    }

    @Test
    @DisplayName("Return persons info from the same family.")
    public void searchPersonInfo() {
        //GIVEN
        Optional<String> firstName = Optional.of("user1");
        String lastName = "spring";

        PersonInfoDto personDto = new PersonInfoDto(person1, medicalRecord1);
        List<PersonInfoDto> personInfoDtoList = new ArrayList<>(List.of(personDto));

        //WHEN
        when(personRepository.findAllByLastName(lastName)).thenReturn(Stream.of(person1));
        when(medicalRecordsRepository.findAllByPerson(person1)).thenReturn(Stream.of(medicalRecord1));

        List<PersonInfoDto> result = securityService.searchPersonInfo(firstName, lastName);

        //THEN
        assertEquals(1, result.size());
        assertEquals(personInfoDtoList, result);
        verify(personRepository, times(1)).findAllByLastName(lastName);
        verify(medicalRecordsRepository, times(1)).findAllByPerson(person1);

    }

    @Test
    @DisplayName("Return all emails of the city.")
    public void searchEmail() {
        //GIVEN
        String city = "Londres";

        //WHEN
        when(personRepository.findAllByCity(city)).thenReturn(Stream.of(person1));
        Set<String> result = securityService.searchEmail(city);

        //THEN
        assertEquals(1, result.size());
        assertTrue(result.contains(person1.getEmail()));
        verify(personRepository, times(1)).findAllByCity(city);
    }


}