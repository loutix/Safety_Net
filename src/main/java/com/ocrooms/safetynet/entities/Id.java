package com.ocrooms.safetynet.entities;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class Id {

    @NotNull
    @Length(min = 2, max = 20)
    protected String firstName;

    @NotNull
    @Length(min = 2, max = 20)
    protected String lastName;

    public String getId() {
        return firstName + "-" + lastName;
    }

}
