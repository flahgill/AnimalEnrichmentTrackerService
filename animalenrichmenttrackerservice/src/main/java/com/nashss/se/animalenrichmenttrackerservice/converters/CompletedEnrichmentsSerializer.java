package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;

import java.io.IOException;

public class CompletedEnrichmentsSerializer extends JsonSerializer<EnrichmentActivity> {
    @Override
    public void serialize(EnrichmentActivity enrichment, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("enrichmentId", enrichment.getEnrichmentId());
        gen.writeStringField("name", enrichment.getName());
        gen.writeNumberField("keeperRating", enrichment.getKeeperRating());
        gen.writeStringField("description", enrichment.getDescription());
        gen.writeStringField("dateCompleted", enrichment.getDateCompleted().toString());
        gen.writeEndObject();
    }
}


