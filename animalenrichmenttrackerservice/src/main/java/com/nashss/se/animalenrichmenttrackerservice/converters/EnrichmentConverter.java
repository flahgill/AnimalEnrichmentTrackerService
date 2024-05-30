package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentSerializationException;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;

public class EnrichmentConverter implements DynamoDBTypeConverter<String, List<Enrichment>> {
    private final ObjectMapper mapper;

    /**
     * Instantiates new EnrichmentConverter object.
     */
    public EnrichmentConverter() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String convert(List<Enrichment> object) {
        try {
            String json = mapper.writeValueAsString(object);
            System.out.println("Serialized json: " + json);
            return json;
        } catch (JsonProcessingException e) {
            System.out.println("Serialization error: " + e.getMessage());
            throw new EnrichmentSerializationException("Enrichment failed to deserialize.", e);
        }
    }

    @Override
    public List<Enrichment> unconvert(String object) {
        TypeReference<List<Enrichment>> ref = new TypeReference<List<Enrichment>>() {
        };
        try {
            System.out.println("Deserializing json: " + object);
            return mapper.readValue(object, ref);
        } catch (JsonProcessingException e) {
            System.out.println("Deserialization error: " + e.getMessage());
            throw new EnrichmentSerializationException("Enrichment failed to be created from " +
                    "String representation.", e);
        }
    }
}
