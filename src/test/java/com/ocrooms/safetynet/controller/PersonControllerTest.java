package com.ocrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private final Set<Person> personSet = new HashSet<>();

    @BeforeEach
    public void setUpTest() {
        Person newPerson1 = new Person("11 street", "Cityville", "11111", "555-1234", "test1@example.com");
        newPerson1.setFirstName("Paul");
        newPerson1.setLastName("ka");
        personSet.add(newPerson1);

        Person newPerson2 = new Person("12 street", "Lyon", "77777", "4444-1234", "test2@example.com");
        newPerson2.setFirstName("John");
        newPerson2.setLastName("Kid");
        personSet.add(newPerson2);

        Person newPerson3 = new Person("13 street", "paris", "88888", "3333-1234", "test3@example.com");
        newPerson3.setFirstName("Peet");
        newPerson3.setLastName("Poule");
        personSet.add(newPerson3);
    }

    @Test
    @DisplayName(("Get all persons"))
    public void testGetPersons() throws Exception {

        when(personService.index()).thenReturn(personSet);
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(personSet.size()));

        verify(personService, times(1)).index();

    }


    @Test
    @DisplayName(("Show a specific person"))
    public void testShowPerson() throws Exception {

        String id = "Peet-Poule";

        Person getPerson = personSet.stream().filter(person -> person.getId().equals(id)).findAny().orElse(null);

        when(personService.show(id)).thenReturn(getPerson);
        assert getPerson != null;
        mockMvc.perform(get("/person/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getPerson.getId()))
                .andExpect(jsonPath("$.address").value(getPerson.getAddress()))
                .andExpect(jsonPath("$.city").value(getPerson.getCity()))
                .andExpect(jsonPath("$.zip").value(getPerson.getZip()))
                .andExpect(jsonPath("$.phone").value(getPerson.getPhone()))
                .andExpect(jsonPath("$.email").value(getPerson.getEmail()));
        //.andDo(print());
        verify(personService, times(1)).show(id);
    }

    @Test
    @DisplayName(("Create a  person"))
    public void testCreatePerson() throws Exception {

        Person newPerson = new Person("14 street", "Nantes", "999", "4444-1234", "test4@example.com");
        newPerson.setFirstName("Albert");
        newPerson.setLastName("Camus");
        newPerson.trimProperties();

        when(personService.create(any(Person.class))).thenReturn(newPerson);
        mockMvc.perform(post("/person")
                        .content(new ObjectMapper().writeValueAsString(newPerson))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newPerson.getId()))
                .andExpect(jsonPath("$.firstName").value(newPerson.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(newPerson.getLastName()))
                .andExpect(jsonPath("$.city").value(newPerson.getCity()))
                .andExpect(jsonPath("$.phone").value(newPerson.getPhone()))
                .andExpect(jsonPath("$.zip").value(newPerson.getZip()));

        verify(personService, times(1)).create(any(Person.class));
    }


    @Test
    @DisplayName(("Update a person"))
    public void testUpdatePerson() throws Exception {

        String id = "Peet-Poule";

        Person updatedPerson = personSet.stream().filter(per -> per.getId().equals(id)).findAny().orElse(null);
        assert updatedPerson != null;
        updatedPerson.setPhone("33333");
        updatedPerson.setCity("new york");
        updatedPerson.setEmail("123132@gmail.com");
        updatedPerson.trimProperties();

        doNothing().when(personService).update(id, updatedPerson);
        when(personService.show(id)).thenReturn(updatedPerson);

        mockMvc.perform(put("/person/{id}", id)
                        .content(new ObjectMapper().writeValueAsString(updatedPerson))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/person/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedPerson.getId()))
                .andExpect(jsonPath("$.address").value(updatedPerson.getAddress()))
                .andExpect(jsonPath("$.city").value(updatedPerson.getCity()))
                .andExpect(jsonPath("$.zip").value(updatedPerson.getZip()))
                .andExpect(jsonPath("$.phone").value(updatedPerson.getPhone()))
                .andExpect(jsonPath("$.email").value(updatedPerson.getEmail()))
                .andDo(print());
        verify(personService, times(1)).update(anyString(), any(Person.class));
        verify(personService, times(1)).show(id);
    }


    @Test
    @DisplayName(("Delete a  person"))
    public void testDeletePerson() throws Exception {

        String id = "Peet-Poule";

        doNothing().when(personService).deletePerson(id);
        mockMvc.perform(delete("/person/{id}", id))
                .andExpect(status().isNoContent());
        verify(personService, times(1)).deletePerson(id);
    }
}