package com.ocrooms.safetynet.service;

import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.repository.FireStationRepository;
import com.ocrooms.safetynet.service.exceptions.ItemAlreadyExists;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FireStationServiceTest {

    FireStationService fireStationService;

    Set<Firestation> firestationSet = new HashSet<>();
    Firestation fireStation1 = new Firestation();
    Firestation fireStation2 = new Firestation();
    @Mock
    FireStationRepository fireStationRepository;

    @BeforeEach
    public void init() {
        fireStationService = new FireStationService(fireStationRepository);

        fireStation1.setAddress("Paris");
        fireStation1.setStation(1);

        fireStation2.setAddress("Marseille");
        fireStation2.setStation(2);

        firestationSet.add(fireStation1);
        firestationSet.add(fireStation2);
    }

    @Test
    @DisplayName("Show all fire stations")
    public void indexFireStations() {

        //GIVEN

        //WHEN
        when(fireStationRepository.getAll()).thenReturn(firestationSet);

        Set<Firestation> allFireStations = fireStationService.index();
        //THEN
        assertEquals(firestationSet, allFireStations);
        verify(fireStationRepository, times(1)).getAll();
    }

    @Test
    @DisplayName("Show a fire station")
    public void ShowFireStation() {
        //GIVEN
        String address = "Paris";
        Integer station = 1;

        //WHEN
        when(fireStationRepository.getByAddressAndStation(anyString(), anyInt())).thenReturn(fireStation1);
        Firestation fireStationShow = fireStationService.show(address, station);

        //THEN
        assertEquals(fireStation1, fireStationShow);
        verify(fireStationRepository, times(1)).getByAddressAndStation(address, station);
    }

    @Test
    @DisplayName("Create a fire station")
    public void createNewFireStation() {
        //GIVEN
        Firestation newFirestation = new Firestation();
        newFirestation.setAddress("Nantes");
        newFirestation.setStation(2);

        //WHEN
        when(fireStationRepository.findAny(newFirestation)).thenReturn(Optional.empty());

        when(fireStationRepository.save(newFirestation)).thenReturn(newFirestation);
        Firestation firestationCreated = fireStationService.create(newFirestation);

        //THEN
        assertEquals(newFirestation, firestationCreated);
        verify(fireStationRepository, times(1)).findAny(newFirestation);
        verify(fireStationRepository, times(1)).save(newFirestation);
    }

    @Test
    @DisplayName("Fire station already exist")
    public void createButFireStationExist() {
        //GIVEN
        Firestation newFirestation = new Firestation();
        newFirestation.setAddress("Paris");
        newFirestation.setStation(1);

        //WHEN
        when(fireStationRepository.findAny(newFirestation)).thenReturn(Optional.of(fireStation1));

        //THEN
        assertThrows(ItemAlreadyExists.class, () -> fireStationService.create(newFirestation));
        verify(fireStationRepository, times(1)).findAny(newFirestation);
    }

    @Test
    @DisplayName("Update a new fire station")
    public void updateFireStation() {

        //GIVEN
        Firestation.FirestationUpdateRequest firestationUpdateRequest = new Firestation.FirestationUpdateRequest(1, 10);

        Firestation firestationUpdated = new Firestation("Paris", 10);

        //WHEN
        when(fireStationRepository.getByAddressAndStation(anyString(), anyInt())).thenReturn(fireStation1);
        fireStationService.update("Paris", firestationUpdateRequest);

        //THEN
        assertEquals(firestationUpdated, fireStation1);
        verify(fireStationRepository, times(1)).getByAddressAndStation(anyString(), anyInt());
    }

    @Test
    @DisplayName("Delete a fire station")
    public void deleteFireStation() {
        //GIVEN
        String address = "Marseille";
        Integer station = 2;
        //WHEN
        doNothing().when(fireStationRepository).delete(address, station);
        fireStationService.delete(address, station);
        //THEN
        verify(fireStationRepository, times(1)).delete(anyString(), anyInt());
    }


}