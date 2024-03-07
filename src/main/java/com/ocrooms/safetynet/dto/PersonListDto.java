package com.ocrooms.safetynet.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class PersonListDto {

    long nbrMajor;
    long nbrMinor;

    List<PersonDto> personDtoList;

    public PersonListDto(List<PersonDto> personDtoList, long nbrMajor) {
        this.nbrMajor = nbrMajor;
        this.nbrMinor = personDtoList.size() - nbrMajor;
        this.personDtoList = personDtoList;
    }
}
