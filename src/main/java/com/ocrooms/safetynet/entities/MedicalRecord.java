package com.ocrooms.safetynet.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicalRecord extends Id {

    @NotNull
    @JsonFormat(pattern = "MM/dd/yyyy")
    private LocalDate birthdate;

    private List<String> medications;

    private List<String> allergies;

    public int calculateAge() {
        return birthdate.until(LocalDate.now()).getYears();
    }

    public boolean isMajor() {
        return calculateAge() >= 19;
    }

    public boolean isMinor() {
        return !isMajor();
    }

}
