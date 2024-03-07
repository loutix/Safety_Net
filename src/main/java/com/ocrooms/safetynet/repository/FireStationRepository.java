package com.ocrooms.safetynet.repository;

import com.ocrooms.safetynet.entities.Firestation;
import com.ocrooms.safetynet.entities.Person;
import com.ocrooms.safetynet.service.JsonService;
import com.ocrooms.safetynet.service.exceptions.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FireStationRepository {
    private final JsonService jsonService;

    public Set<Firestation> getAll() {
        log.info("Enter on method getAll");
        return jsonService.getData().getFirestations();
    }

    public Stream<Firestation> findAll() {
        log.info("Enter on method findAll");
        return getAll().parallelStream();
    }


    public Optional<Firestation> findAny(Firestation firestation) {
        log.info("Enter on method findAny with parameters : {}", firestation);
        return findAll()
                .filter(fire -> fire.equals(firestation)).findAny();
    }

    public Optional<Firestation> findByAddress(String address) {
        return findAll()
                .filter(firestation -> firestation.getAddress().equals(address))
                .reduce((a, b) -> {
                    throw new RuntimeException("Multiple responses");
                });
    }

    public Firestation getByAddress(String address) {
        return findByAddress(address)
                .orElseThrow(() -> new ItemNotFoundException("The fire station address is not found : " + address));
    }


    public Optional<Firestation> findByAddressAndStation(String address, Integer station) {
        return findAll()
                .filter(firestation -> firestation.getAddress().equalsIgnoreCase(address.trim()))
                .filter(firestation -> firestation.getStation().equals(station))
                .findAny();
    }

    public Firestation getByAddressAndStation(String address, Integer station) {
        return findByAddressAndStation(address, station)
                .orElseThrow(() -> new ItemNotFoundException("The fire station address is not found : " + address + "with station number :" + station));
    }

    public Stream<Firestation> findAllByStation(Integer station) {
        return findAll()
                .filter(firestation -> firestation.getStation().equals(station));
    }

    public Stream<Firestation> findAllByPerson(Person person) {
        return findAll()
                .filter(firestation -> firestation.getAddress().equals(person.getAddress()));
    }


    public Firestation save(Firestation firestation) {
        getAll().add(firestation);
        log.info("new fire station saved with success" + firestation);
        return firestation;
    }

    public void delete(String address, Integer station) {
        getAll().removeIf(firestation -> firestation.getAddress().equalsIgnoreCase(address) &&
                firestation.getStation().equals(station));
    }


}
