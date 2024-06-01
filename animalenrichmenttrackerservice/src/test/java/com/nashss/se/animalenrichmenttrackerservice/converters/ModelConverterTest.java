package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelConverterTest {
    private ModelConverter modelConverter = new ModelConverter();

    @Test
    void toHabitatModel_withAttributes_convertsHabitat() {
        Habitat habitat = HabitatTestHelper.generateHabitat();

        HabitatModel habitatModel = modelConverter.toHabitatModel(habitat);
        assertEquals(habitat.getHabitatId(), habitatModel.getHabitatId());
        assertEquals(habitat.getHabitatName(), habitatModel.getHabitatName());
        assertEquals(habitat.getKeeperManagerId(), habitatModel.getKeeperManagerId());
        assertEquals(habitat.getTotalAnimals(), habitatModel.getTotalAnimals());
        assertEquals(habitat.getSpecies(), habitatModel.getSpecies());
        assertEquals(habitat.getAcceptableEnrichmentIds(), habitatModel.getAcceptableEnrichmentIds());
        assertEquals(habitat.getCompletedEnrichments(), habitatModel.getCompletedEnrichments());
    }

    @Test
    void toHabitatModel_withNullSpecies_convertsHabitat() {
        Habitat habitat = HabitatTestHelper.generateHabitat();
        habitat.setSpecies(null);

        HabitatModel habitatModel = modelConverter.toHabitatModel(habitat);
        assertEquals(habitat.getHabitatId(), habitatModel.getHabitatId());
        assertEquals(habitat.getHabitatName(), habitatModel.getHabitatName());
        assertEquals(habitat.getKeeperManagerId(), habitatModel.getKeeperManagerId());
        assertEquals(habitat.getTotalAnimals(), habitatModel.getTotalAnimals());
        assertEquals(habitat.getAcceptableEnrichmentIds(), habitatModel.getAcceptableEnrichmentIds());
        assertEquals(habitat.getCompletedEnrichments(), habitatModel.getCompletedEnrichments());
        assertNull(habitatModel.getSpecies());
    }

    @Test
    void toEnrichment_withAttributes_convertsToEnrichmentModel() {
        // GIVEN
        Enrichment enrichment = EnrichmentTestHelper.generateEnrichment(1);

        // WHEN
        EnrichmentModel enrichmentModel = modelConverter.toEnrichmentActivityModel(enrichment);

        // THEN
        assertEquals(enrichment.getEnrichmentId(), enrichmentModel.getEnrichmentId());
        assertEquals(enrichment.getDescription(), enrichmentModel.getDescription());
        assertEquals(enrichment.getName(), enrichmentModel.getName());
        assertEquals(enrichment.getDateCompleted(), enrichmentModel.getDateCompleted());
        assertEquals(enrichment.getKeeperRating(), enrichmentModel.getKeeperRating());
    }

    @Test
    void toEnrichmentModelList_withEnrichments_convertsToEnrichmentModelList() {
        // GIVEN
        List<Enrichment> enrichmentsList = new ArrayList<>();
        int numOfEnrich = 3;
        for (int i = 0; i < numOfEnrich; i++) {
            enrichmentsList.add(EnrichmentTestHelper.generateEnrichment(i));
        }

        // WHEN
        List<EnrichmentModel> enrichmentModelList = modelConverter.toEnrichmentActivityModelList(enrichmentsList);

        // THEN
        assertEquals(enrichmentModelList.size(), enrichmentsList.size());
        Enrichment firstEnrich = enrichmentsList.get(0);
        EnrichmentModel firstEnrichModel = enrichmentModelList.get(0);
        assertEquals(firstEnrich.getEnrichmentId(), firstEnrichModel.getEnrichmentId());
        assertEquals(firstEnrich.getName(), firstEnrichModel.getName());
        assertEquals(firstEnrich.getKeeperRating(), firstEnrichModel.getKeeperRating());
        assertEquals(firstEnrich.getDateCompleted(), firstEnrichModel.getDateCompleted());
        assertEquals(firstEnrich.getDescription(), firstEnrichModel.getDescription());
    }
}
