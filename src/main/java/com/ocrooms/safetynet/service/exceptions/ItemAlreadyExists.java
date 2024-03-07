package com.ocrooms.safetynet.service.exceptions;

public class ItemAlreadyExists extends RuntimeException {
    public  ItemAlreadyExists(String s) {
        super(s);
    }
}
