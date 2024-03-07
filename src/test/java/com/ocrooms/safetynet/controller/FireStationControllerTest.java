package com.ocrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.service.FireStationService;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    private final Set<Firestation> firestationsSet = new HashSet<>();

    @BeforeEach
    public void setUpTest() {
        firestationsSet.add(new Firestation("Paris", 1));
        firestationsSet.add(new Firestation("Bordeaux", 2));
        firestationsSet.add(new Firestation("Lyon", 3));
        firestationsSet.add(new Firestation("Lyon", 4));
    }

    @Test
    @DisplayName(("Get all firestations"))
    public void testGetFireStations() throws Exception {

        when(fireStationService.index()).thenReturn(firestationsSet);
        mockMvc.perform(get("/firestations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(firestationsSet.size()))
                .andExpect(jsonPath("$[0].address", is("Lyon")))
                .andExpect(jsonPath("$[0].station", is(3)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName(("Show a specific fire station"))
    public void testShowFireStations() throws Exception {

        String targetAddress = "Lyon";

        Integer targetStation = 3;

        Firestation getFireStation = firestationsSet.stream()
                .filter(firestation -> firestation.getAddress().equals(targetAddress.trim()))
                .filter(firestation -> firestation.getStation().equals(3)).findAny().get();

        when(fireStationService.show(targetAddress, targetStation)).thenReturn(getFireStation);
        mockMvc.perform(get("/firestations/show")
                        .param("address", targetAddress)
                        .param("station", String.valueOf(targetStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", is(targetAddress)))
                .andExpect(jsonPath("$.station", is(targetStation)))
                .andDo(print());
    }

    @Test
    @DisplayName(("Create a  fire station"))
    public void testCreateFireStation() throws Exception {

        Firestation newFirestation = new Firestation("Paris", 12);
        firestationsSet.add(newFirestation);

        when(fireStationService.create(any(Firestation.class))).thenReturn(newFirestation);
        mockMvc.perform(post("/firestations")
                        .content(new ObjectMapper().writeValueAsString(newFirestation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address", is("Paris")))
                .andExpect(jsonPath("$.station", is(12)));

        verify(fireStationService, times(1)).create(any(Firestation.class));
    }

    @Test
    @DisplayName(("Update a fire station"))
    public void testUpdateFireStation() throws Exception {

        String targetAddress = "Paris";
        Firestation.FirestationUpdateRequest updateFirestation = new Firestation.FirestationUpdateRequest(1, 10);

        doNothing().when(fireStationService).update(targetAddress, updateFirestation);
        mockMvc.perform(put("/firestations")
                        .param("address", targetAddress)
                        .content(new ObjectMapper().writeValueAsString(updateFirestation))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(fireStationService, times(1)).update(targetAddress, updateFirestation);
    }

    @Test
    @DisplayName(("Delete a  fire station"))
    public void testDeleteFireStation() throws Exception {

        String targetAddress = "Paris";
        Integer targetStation = 1;

        doNothing().when(fireStationService).delete(targetAddress, targetStation);
        mockMvc.perform(delete("/firestations")
                        .param("address", targetAddress)
                        .param("station", String.valueOf(targetStation)))
                .andExpect(status().isNoContent());

        verify(fireStationService, times(1)).delete(targetAddress, targetStation);
    }

}