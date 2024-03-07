package com.ocrooms.safetynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocrooms.safetynet.entities.MedicalRecord;
import com.ocrooms.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    private final Set<MedicalRecord> medicalRecordset = new HashSet<>();
    @Autowired
    private ObjectMapper objectMapper;

    List<String> medications = new ArrayList<>(List.of("abc:def"));
    List<String> allergies = new ArrayList<>(List.of("pistaches"));

    @BeforeEach
    public void setUpTest() {

        MedicalRecord newMr1 = new MedicalRecord(LocalDate.of(2000, 1, 11), new ArrayList<>(), new ArrayList<>());
        newMr1.setFirstName("Paul");
        newMr1.setLastName("ka");
        medicalRecordset.add(newMr1);

        MedicalRecord newMr2 = new MedicalRecord(LocalDate.of(2001, 2, 12), new ArrayList<>(), new ArrayList<>());
        newMr2.setFirstName("John");
        newMr2.setLastName("Kid");
        medicalRecordset.add(newMr2);

        MedicalRecord newMr3 = new MedicalRecord(LocalDate.of(2002, 2, 13), medications, allergies);
        newMr3.setFirstName("Peet");
        newMr3.setLastName("Poule");
        medicalRecordset.add(newMr3);
    }


    @Test
    @DisplayName(("Get all medical records"))
    public void testGetMedicalRecords() throws Exception {

        when(medicalRecordService.index()).thenReturn(medicalRecordset);

        mockMvc.perform(get("/medical-record"))
                .andExpect(jsonPath("$.size()").value(medicalRecordset.size()))
                .andExpect(status().isOk());

        verify(medicalRecordService, times(1)).index();
    }


    @Test
    @DisplayName(("Show a specific medical record"))
    public void testShowMedicalRecord() throws Exception {

        String id = "Peet-Poule";

        MedicalRecord getMedicalRecord = medicalRecordset.stream().filter(medicalRecord -> medicalRecord.getId().equals(id)).findAny().orElse(null);

        when(medicalRecordService.show(id)).thenReturn(getMedicalRecord);

        assert getMedicalRecord != null;
        mockMvc.perform(get("/medical-record/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getMedicalRecord.getId()))
                .andExpect(jsonPath("$.medications").value(getMedicalRecord.getMedications()))
                .andExpect(jsonPath("$.allergies").value(getMedicalRecord.getAllergies()))
                .andExpect(status().isOk());

        verify(medicalRecordService, times(1)).show(id);
    }


    @Test
    @DisplayName(("Create a  medical record"))
    public void testCreatePerson() throws Exception {
        MedicalRecord newMr = new MedicalRecord();
        newMr.setFirstName("Albert");
        newMr.setLastName("Camus");
        newMr.setBirthdate(LocalDate.of(2020, Month.JANUARY, 8));
        newMr.setMedications(medications);
        newMr.setMedications(allergies);

        medicalRecordset.add(newMr);

        when(medicalRecordService.create(any(MedicalRecord.class))).thenReturn(newMr);
        mockMvc.perform(post("/medical-record")
                        .content(objectMapper.writeValueAsString(newMr))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newMr.getId()))
                .andExpect(jsonPath("$.medications").value(newMr.getMedications()))
                .andExpect(jsonPath("$.allergies").value(newMr.getAllergies()));

        verify(medicalRecordService, times(1)).create(any(MedicalRecord.class));
    }


    @Test
    @DisplayName(("Update a medical record"))
    public void testUpdateMedicalRecord() throws Exception {

        String id = "Peet-Poule";

        MedicalRecord updatedMedicalRecord = medicalRecordset.stream().filter(medicalRecord -> medicalRecord.getId().equals(id)).findAny().orElse(null);

        assert updatedMedicalRecord != null;
        updatedMedicalRecord.setBirthdate(LocalDate.of(2022, Month.FEBRUARY, 9));

        doNothing().when(medicalRecordService).update(id, updatedMedicalRecord);

        mockMvc.perform(put("/medical-record/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedMedicalRecord))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(medicalRecordService, times(1)).update(anyString(), any(MedicalRecord.class));

    }

    @Test
    @DisplayName(("Delete a medical record"))
    public void testDeleteMedicalRecord() throws Exception {

        String id = "Peet-Poule";

        doNothing().when(medicalRecordService).delete(id);
        mockMvc.perform(delete("/medical-record/{id}", id))
                .andExpect(status().isNoContent());
        verify(medicalRecordService, times(1)).delete(anyString());
    }

}