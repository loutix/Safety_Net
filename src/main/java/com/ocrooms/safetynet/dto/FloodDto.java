package com.ocrooms.safetynet.dto;

import lombok.Data;

import java.util.List;

@Data
public class FloodDto {
    String address;
    List<FloodPersonDto> personListFlooded;

    public FloodDto(String address, List<FloodPersonDto> floodPersonDto) {
        this.address = address;
        this.personListFlooded = floodPersonDto;
    }
}
