package com.ocrooms.safetynet.entities;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firestation {

    @NotNull
    @Length(min = 1, max = 20)
    private String address;

    @NotNull
    @Range(min = 1, max = 20)
    private Integer station;

    @Data
    @AllArgsConstructor
    public static class FirestationUpdateRequest {
        @NotNull
        @Range(min = 1, max = 20, message = "Old station number should be at minimum 1")
        private Integer oldStation;

        @NotNull
        @Range(min = 1, max = 20, message = "New station number should be at minimum 1")
        private Integer newStation;
    }
}
