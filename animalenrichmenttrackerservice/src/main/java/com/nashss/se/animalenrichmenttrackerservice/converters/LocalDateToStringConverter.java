package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;
import java.util.Objects;

public class LocalDateToStringConverter implements DynamoDBTypeConverter<String, LocalDate> {

    @Override
    public String convert(LocalDate localDate) {
        return localDate.toString();
    }

    @Override
    public LocalDate unconvert(String s) {
        if (!Objects.isNull(s) && !s.isEmpty()) {
            return LocalDate.parse(s);
        }
        return null;
    }
}
