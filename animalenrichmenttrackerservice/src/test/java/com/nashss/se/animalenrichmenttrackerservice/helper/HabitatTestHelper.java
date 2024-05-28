package com.nashss.se.animalenrichmenttrackerservice.helper;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
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

        List<Enrichment> enrichments = new ArrayList<>();
        for (int i = 0; i < numOfEnrichments; i++) {
            enrichments.add(EnrichmentTestHelper.generateEnrichment(i));
        }
        habitat.setCompletedEnrichments(enrichments);
        habitat.setTotalAnimals(0);
        habitat.setAcceptableEnrichmentIds(List.of("01", "02", "06"));

        return habitat;
    }
}
