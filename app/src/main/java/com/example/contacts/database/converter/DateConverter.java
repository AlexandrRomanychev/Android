package com.example.contacts.database.converter;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public DateConverter(){

    }
    @TypeConverter
    public Long dateToTimestamp(String date) {
        if (date == null) {
            return null;
        } else {
            try {
                Date localDate = new SimpleDateFormat("dd.mm.yyyy").parse(date);
                return localDate.getTime();
            } catch (ParseException e) {
                return null;
            }
        }
    }

    @TypeConverter
    public String dateToString(Long date){
        if (date == null)
            return null;
        else{
            Date localDate = new Date(date);
            DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
            return dateFormat.format(localDate);
        }
    }
}