package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentSerializationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class EnrichmentActivityConverter implements DynamoDBTypeConverter<String, List<EnrichmentActivity>> {
    private final ObjectMapper mapper;

    /**
     * Instantiates new EnrichmentActivityConverter object.
     */
    public EnrichmentActivityConverter() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String convert(List<EnrichmentActivity> object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EnrichmentSerializationException("EnrichmentActivity failed to deserialize.", e);
        }
    }

    @Override
    public List<EnrichmentActivity> unconvert(String object) {
        TypeReference<List<EnrichmentActivity>> ref = new TypeReference<List<EnrichmentActivity>>() {
        };
        try {
            return mapper.readValue(object, ref);
        } catch (JsonProcessingException e) {
            throw new EnrichmentSerializationException("EnrichmentActivity failed to be created from " +
                    "String representation.", e);
        }
    }
}
