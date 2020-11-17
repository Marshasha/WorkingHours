package com.example.workinghours.database.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter()
    public Long fromDate(Date date) {
        return date.getTime();
    }

    @TypeConverter()
    public Date toDate(Long value) {
        return new Date(value);
    }

}
