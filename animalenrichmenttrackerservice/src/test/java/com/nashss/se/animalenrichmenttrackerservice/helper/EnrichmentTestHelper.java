package com.nashss.se.animalenrichmenttrackerservice.helper;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;

import java.time.LocalDate;

public class EnrichmentTestHelper {
    private EnrichmentTestHelper() {
    }

    public static Enrichment generateEnrichment(int sequenceNumber) {
        Enrichment enrichment = new Enrichment();
        enrichment.setEnrichmentId("0" + sequenceNumber);
        enrichment.setName("test enrich name");
        enrichment.setDescription("new test description for enrichment");
        enrichment.setDateCompleted(LocalDate.now());
        enrichment.setKeeperRating(8);
        return enrichment;
    }

}
