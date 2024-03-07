package com.ocrooms.safetynet.controller;

import com.ocrooms.safetynet.dto.*;
import com.ocrooms.safetynet.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
public class SecurityController {

    private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping(path = "firestation")
    public @ResponseBody PersonListDto searchFirestation(@RequestParam int station) {
        log.info("GET/fire-station?station=" + station);
        return this.securityService.searchFirestation(station);
    }

    @GetMapping(path = "childAlert")
    public @ResponseBody List<ChildDto> searchChildAlert(@RequestParam String address) {
        log.info("GET/child-alert?address=" + address);
        return this.securityService.searchChildAlert(address);
    }

    @GetMapping(path = "phoneAlert")
    public @ResponseBody Set<String> searchPhoneAlert(@RequestParam Integer firestation) {
        log.info("GET/phone-alert?firestation=" + firestation);
        return this.securityService.searchPhoneAlert(firestation);
    }

    @GetMapping(path = "fire")
    public @ResponseBody List<PersonAddressStationDto> searchFire(@RequestParam String address) {
        log.info("GET/fire?address=" + address);
        return this.securityService.searchFire(address);
    }

    @GetMapping(path = "flood/stations")
    public @ResponseBody List<FloodDto> searchFlood(@RequestParam List<Integer> stations) {
        log.info("GET/flood/stations?<address>=" + stations);
        return this.securityService.searchFlood(stations);
    }

    @GetMapping(path = "personInfo")
    public @ResponseBody List<PersonInfoDto> searchPersonInfo(@RequestParam(required = false) Optional<String> firstName, @RequestParam String lastName) {
        log.info("GET/person-info?firstName=" + firstName + "&+lastName=" + lastName);
        return this.securityService.searchPersonInfo(firstName, lastName);
    }

    @GetMapping(path = "communityEmail")
    public @ResponseBody Set<String> searchEmail(@RequestParam String city) {
        log.info("GET/community-email?city=" + city);
        return this.securityService.searchEmail(city);
    }


}
