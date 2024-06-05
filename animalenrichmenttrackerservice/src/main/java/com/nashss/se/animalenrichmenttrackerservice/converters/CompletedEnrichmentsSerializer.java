package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CompletedEnrichmentsSerializer extends JsonSerializer<EnrichmentActivity> {
    @Override
    public void serialize(EnrichmentActivity enrichment, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeStringField("enrichmentId", enrichment.getEnrichmentId());
        gen.writeStringField("name", enrichment.getActivityName());
        gen.writeNumberField("keeperRating", enrichment.getKeeperRating());
        gen.writeStringField("description", enrichment.getDescription());
        gen.writeStringField("dateCompleted", enrichment.getDateCompleted().toString());
        gen.writeEndObject();
    }
}


