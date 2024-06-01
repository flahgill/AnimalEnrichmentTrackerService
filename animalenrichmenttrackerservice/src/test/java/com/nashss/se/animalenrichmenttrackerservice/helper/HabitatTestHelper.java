package com.nashss.se.animalenrichmenttrackerservice.helper;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;

import java.util.ArrayList;
import java.util.List;

public class HabitatTestHelper {
    private HabitatTestHelper() {
    }

    public static Habitat generateHabitat() {
        return generateHabitatWithNEnrichments(1);
    }

    public static Habitat generateHabitatWithNEnrichments(int numOfEnrichments) {
        Habitat habitat = new Habitat();
        habitat.setHabitatId("id");
        habitat.setHabitatName("a habitat");
        habitat.setKeeperManagerId("Keeper123");
        habitat.setIsActive("active");
        habitat.setSpecies(List.of("Giraffe", "Takin", "Pandas"));

        List<EnrichmentActivity> activities = new ArrayList<>();
        for (int i = 0; i < numOfEnrichments; i++) {
            activities.add(EnrichmentActivityTestHelper.generateEnrichmentActivity(i));
        }
        habitat.setCompletedEnrichments(activities);
        habitat.setTotalAnimals(0);
        habitat.setAcceptableEnrichmentIds(List.of("01", "02", "06", "fake id"));
        // added 'fake id' for EnrichmentNotFound testing purposes

        return habitat;
    }
}
