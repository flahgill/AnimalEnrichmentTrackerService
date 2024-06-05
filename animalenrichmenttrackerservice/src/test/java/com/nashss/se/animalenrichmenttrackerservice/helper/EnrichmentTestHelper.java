package com.nashss.se.animalenrichmenttrackerservice.helper;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;

public class EnrichmentTestHelper {
    private EnrichmentTestHelper() {
    }

    public static Enrichment generateEnrichment(int sequenceNumber) {
        Enrichment enrichment = new Enrichment();
        enrichment.setEnrichmentId("0" + sequenceNumber);
        enrichment.setActivityName("test enrich name");
        enrichment.setDescription("new test description for enrichment");
        return enrichment;
    }

}
