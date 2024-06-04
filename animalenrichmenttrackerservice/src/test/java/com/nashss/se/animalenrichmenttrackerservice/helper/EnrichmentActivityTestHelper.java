package com.nashss.se.animalenrichmenttrackerservice.helper;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;

import java.time.LocalDate;

public class EnrichmentActivityTestHelper {

    private EnrichmentActivityTestHelper() {
    }

    public static EnrichmentActivity generateEnrichmentActivity(int sequenceNumber) {
        EnrichmentActivity activity = new EnrichmentActivity();
        activity.setActivityId("123");
        activity.setEnrichmentId("0" + sequenceNumber);
        activity.setName("test enrich name");
        activity.setDescription("new test description for enrichment");
        activity.setKeeperRating(5);
        activity.setDateCompleted(LocalDate.of(2024, 5, 31));
        activity.setIsComplete("complete");
        activity.setOnHabitat(true);
        return activity;
    }
}
