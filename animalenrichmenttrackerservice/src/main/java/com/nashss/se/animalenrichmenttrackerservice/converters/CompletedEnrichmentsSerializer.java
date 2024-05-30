package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;

import java.io.IOException;

public class CompletedEnrichmentsSerializer extends JsonSerializer<Enrichment> {
    @Override
    public void serialize(Enrichment enrichment, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("enrichmentId", enrichment.getEnrichmentId());
        gen.writeStringField("name", enrichment.getName());
        gen.writeNumberField("keeperRating", enrichment.getKeeperRating());
        gen.writeStringField("description", enrichment.getDescription());
        gen.writeStringField("dateCompleted", enrichment.getDateCompleted().toString());
        gen.writeEndObject();
    }
}


