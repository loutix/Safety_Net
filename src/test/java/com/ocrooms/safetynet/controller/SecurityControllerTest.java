package com.ocrooms.safetynet.controller;

import com.ocrooms.safetynet.dto.*;
import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityController.class)
class SecurityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;
    private final List<Person> personList = new ArrayList<>();
    private final List<MedicalRecord> medicalRecordList = new ArrayList<>();
    List<String> medications = new ArrayList<>(List.of("abc:def"));
    List<String> allergies = new ArrayList<>(List.of("pistaches"));
    private final List<Firestation> firestationsList = new ArrayList<>();

    @BeforeEach
    public void setUpTest() {

        // persons
        Person newPerson1 = new Person("11 street", "Cityville", "11111", "555-1234", "test1@example.com");
        newPerson1.setFirstName("Paul");
        newPerson1.setLastName("Ka");
        personList.add(newPerson1);

        Person newPerson2 = new Person("12 street", "Lyon", "77777", "4444-1234", "test2@example.com");
        newPerson2.setFirstName("John");
        newPerson2.setLastName("Kid");
        personList.add(newPerson2);

        Person newPerson3 = new Person("13 street", "paris", "88888", "3333-1234", "test3@example.com");
        newPerson3.setFirstName("Peet");
        newPerson3.setLastName("Poule");
        personList.add(newPerson3);


        // medical records
        MedicalRecord newMr1 = new MedicalRecord(LocalDate.of(2000, 1, 11), medications, allergies);
        newMr1.setFirstName(newPerson1.getFirstName());
        newMr1.setLastName(newPerson1.getLastName());
        medicalRecordList.add(newMr1);

        MedicalRecord newMr2 = new MedicalRecord(LocalDate.of(2001, 2, 12), medications, allergies);
        newMr2.setFirstName(newPerson2.getFirstName());
        newMr2.setLastName(newPerson2.getLastName());
        medicalRecordList.add(newMr2);

        MedicalRecord newMr3 = new MedicalRecord(LocalDate.of(2002, 2, 13), medications, allergies);
        newMr3.setFirstName(newPerson3.getFirstName());
        newMr3.setLastName(newPerson3.getLastName());
        medicalRecordList.add(newMr3);

        firestationsList.add(new Firestation("Paris", 1));
        firestationsList.add(new Firestation("Bordeaux", 2));
        firestationsList.add(new Firestation("Lyon", 3));
        firestationsList.add(new Firestation("Lyon", 4));
    }


    @Test
    @DisplayName("Return a list of persons who are served by the station number")
    public void testGetPersonListServedByStationNumber() throws Exception {

        Integer station = 1;

        PersonDto personDto = new PersonDto(personList.getFirst());
        List<PersonDto> personDtoList = new ArrayList<>(List.of(personDto));

        PersonListDto personListDto = new PersonListDto(personDtoList, 1);

        when(securityService.searchFirestation(any(Integer.class))).thenReturn(personListDto);

        mockMvc.perform(get("/firestation")
                        .param("station", String.valueOf(station)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personDtoList[0].firstName").value("Paul"))
                .andExpect(jsonPath("$.personDtoList[0].lastName").value("Ka"))
                .andExpect(jsonPath("$.personDtoList[0].address").value("11 street"))
                .andExpect(jsonPath("$.personDtoList[0].phone").value("555-1234"));
        verify(securityService, times(1)).searchFirestation(any(Integer.class));
    }

    @Test
    @DisplayName("Return a children list living at this address")
    public void testGetAChildrenList() throws Exception {

        String address = "Paris";

        ChildDto childDto = new ChildDto(medicalRecordList.getFirst(), personList);
        List<ChildDto> childDtoList = new ArrayList<>(List.of(childDto));

        when(securityService.searchChildAlert(any(String.class))).thenReturn(childDtoList);

        mockMvc.perform(get("/childAlert")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value("Paul"))
                .andExpect(jsonPath("$.[0].lastName").value("Ka"))
                .andExpect(jsonPath("$.[0].family.size()").value(3))
                .andDo(print());
        verify(securityService, times(1)).searchChildAlert(any(String.class));
    }


    @Test
    @DisplayName("Return a list of phone numbers served by the fire station number")
    public void testGetAListOhPhoneNumber() throws Exception {

        Integer station = 1;
        Set<String> phoneNumbersList = new HashSet<>(List.of("111", "222", "333"));

        when(securityService.searchPhoneAlert(any(Integer.class))).thenReturn(phoneNumbersList);

        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", String.valueOf(station)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value("111"))
                .andExpect(jsonPath("$.[1]").value("222"))
                .andExpect(jsonPath("$.[2]").value("333"))
                .andExpect(jsonPath("$.size()").value(phoneNumbersList.size()))
                .andDo(print());
        verify(securityService, times(1)).searchPhoneAlert(any(Integer.class));

    }


    @Test
    @DisplayName("Get list of persons living at this address")
    void testGetPeronWhoLivesAtThisAddress() throws Exception {

        String address = "11 street";
        List<Integer> station = List.of(firestationsList.getFirst().getStation());

        PersonAddressStationDto dto = new PersonAddressStationDto(personList.getFirst(), station, medicalRecordList.get(0));
        List<PersonAddressStationDto> dtoList = new ArrayList<>(List.of(dto));


        when(securityService.searchFire(anyString())).thenReturn(dtoList);

        mockMvc.perform(get("/fire")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].lastName").value(personList.get(0).getLastName()))
                .andExpect(jsonPath("$.[0].stationNumber").value(station.get(0)))
                .andExpect(jsonPath("$.[0].medications").value(medicalRecordList.get(0).getMedications()))
                .andExpect(jsonPath("$.[0].allergies").value(medicalRecordList.get(0).getAllergies()))
                .andExpect(jsonPath("$.size()").value(dtoList.size()))
                .andDo(print());
        verify(securityService, times(1)).searchFire(any(String.class));
    }


    @Test
    @DisplayName("Get list of persons served by the fire station List number.")
    void testGetPeronListFlood() throws Exception {

        List<Integer> stationsList = List.of(1, 2, 3);
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.addAll("stations", stationsList.stream().map(Object::toString).collect(Collectors.toList()));


        List<FloodPersonDto> floodPersonDtoList = new ArrayList<>();
        FloodPersonDto floodPersonDto = new FloodPersonDto(personList.get(0), medicalRecordList.get(0));
        floodPersonDtoList.add(floodPersonDto);


        String address = personList.get(0).getAddress();

        List<FloodDto> FloodDtoList = new ArrayList<>();
        FloodDtoList.add(new FloodDto(address, floodPersonDtoList));


        when(securityService.searchFlood(stationsList)).thenReturn(FloodDtoList);

        mockMvc.perform(get("/flood/stations").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].address").value(address))
                .andExpect(jsonPath("$.[0].personListFlooded").isArray())
                .andExpect(jsonPath("$.size()").value(floodPersonDtoList.size()))
                .andDo(print());

        verify(securityService, times(1)).searchFlood(anyList());
    }


    @Test
    @DisplayName("Get list info of person from the same family")
    void testGetPeronInfoFromSameFamily() throws Exception {
        String firstName = "Paul";
        String lastName = "ka";

        List<PersonInfoDto> personInfoDtoList = new ArrayList<>();
        PersonInfoDto personInfoDto_1 = new PersonInfoDto(personList.get(0), medicalRecordList.get(0));
        personInfoDtoList.add(personInfoDto_1);

        System.out.println(personInfoDtoList);

        when(securityService.searchPersonInfo(any(Optional.class), any(String.class))).thenReturn(personInfoDtoList);

        mockMvc.perform(get("/personInfo")
                        .param("firstName", firstName)
                        .param("lastName", lastName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].firstName").value(personInfoDto_1.getFirstName()))
                .andExpect(jsonPath("$.[0].lastName").value(personInfoDto_1.getLastName()))
                .andExpect(jsonPath("$.size()").value(personInfoDtoList.size()));

        verify(securityService, times(1)).searchPersonInfo(any(Optional.class), any(String.class));
    }


    @Test
    @DisplayName("Get list of email address")
    void testGetAllEmailByCity() throws Exception {
        String city = "Paris";

        Set<String> emailList = new HashSet<>(List.of("test1@gmail.com", "test2@gmail.com", "test3@gmail.com"));

        when(securityService.searchEmail(any(String.class))).thenReturn(emailList);
        mockMvc.perform(get("/communityEmail")
                        .param("city", city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(emailList.size()))
                .andDo(print());
        verify(securityService, times(1)).searchEmail(any(String.class));
    }
}