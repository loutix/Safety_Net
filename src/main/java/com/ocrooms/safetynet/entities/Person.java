package com.ocrooms.safetynet.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Person extends Id {


    @NotNull
    @Length(min = 2, max = 20)
    private String address;

    @NotNull
    @Length(min = 2, max = 20)
    private String city;

    @NotNull
    @Length(min = 2, max = 20)
    private String zip;

    @NotNull
    @Length(min = 5, max = 30)
    private String phone;

    @NotNull
    @Email
    private String email;


    public void trimProperties() {
        address = address.trim();
        city = city.trim();
        zip = zip.trim();
        phone = phone.trim();
        email = email.trim();
    }

}
