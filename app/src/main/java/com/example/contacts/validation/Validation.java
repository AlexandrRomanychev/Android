package com.example.contacts.validation;

public class Validation {

    private String date;
    private String name;

    private static final String DATE_PATTERN =
            "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";

    public Validation(String date, String name){
        this.date = date;
        this.name = name;
    }

    public void checkCorrectValidation(){

    }

    private boolean validateDateFormat(){
        return date.matches(DATE_PATTERN);
    }
}
