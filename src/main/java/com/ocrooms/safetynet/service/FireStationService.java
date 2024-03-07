package com.ocrooms.safetynet.service;

import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.repository.FireStationRepository;
import com.ocrooms.safetynet.service.exceptions.ItemAlreadyExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class FireStationService {
    private final FireStationRepository fireStationRepository;

    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    public Set<Firestation> index() {
        return fireStationRepository.getAll();
    }

    public Firestation show(String address, Integer station) {
        return fireStationRepository.getByAddressAndStation(address, station);
    }

    public Firestation create(Firestation firestation) {

        if (fireStationRepository.findAny(firestation).isPresent()) {
            throw new ItemAlreadyExists("The fire station already exist : " + firestation);
        } else {
            log.info("Fire station created with success");
            return fireStationRepository.save(firestation);
        }
    }

    public void update(String address, Firestation.FirestationUpdateRequest firestationUpdateRequest) {

        Firestation fireStationToUpdate = fireStationRepository.getByAddressAndStation(address, firestationUpdateRequest.getOldStation());
        fireStationToUpdate.setStation(firestationUpdateRequest.getNewStation());

        log.info("Fire station updated with success: Address=" + address + "old station=" + firestationUpdateRequest.getOldStation());

    }

    public void delete(String address, Integer station) {
        fireStationRepository.delete(address, station);
        log.info("Fire station deleted with success:  address=" + address + "station=" + station);

    }


}
