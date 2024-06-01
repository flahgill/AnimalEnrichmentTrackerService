package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentActivityTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.EnrichmentTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.helper.HabitatTestHelper;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
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
    void toEnrichmentActivity_withAttributes_convertsToEnrichmentActivityModel() {
        // GIVEN
        EnrichmentActivity activity = EnrichmentActivityTestHelper.generateEnrichmentActivity(1);

        // WHEN
        EnrichmentActivityModel model = modelConverter.toEnrichmentActivityModel(activity);

        // THEN
        assertEquals(activity.getEnrichmentId(), model.getEnrichmentId());
        assertEquals(activity.getDescription(), model.getDescription());
        assertEquals(activity.getName(), model.getName());
        assertEquals(activity.getDateCompleted(), model.getDateCompleted());
        assertEquals(activity.getKeeperRating(), model.getKeeperRating());
    }

    @Test
    void toEnrichmentActivityModelList_withEnrichmentActivities_convertsToEnrichmentActivityModelList() {
        // GIVEN
        List<EnrichmentActivity> activities = new ArrayList<>();
        int numOfEnrich = 3;
        for (int i = 0; i < numOfEnrich; i++) {
            activities.add(EnrichmentActivityTestHelper.generateEnrichmentActivity(i));
        }

        // WHEN
        List<EnrichmentActivityModel> modelList = modelConverter.toEnrichmentActivityModelList(activities);

        // THEN
        assertEquals(modelList.size(), activities.size());
        EnrichmentActivity firstEnrich = activities.get(0);
        EnrichmentActivityModel firstEnrichModel = modelList.get(0);
        assertEquals(firstEnrich.getEnrichmentId(), firstEnrichModel.getEnrichmentId());
        assertEquals(firstEnrich.getName(), firstEnrichModel.getName());
        assertEquals(firstEnrich.getKeeperRating(), firstEnrichModel.getKeeperRating());
        assertEquals(firstEnrich.getDateCompleted(), firstEnrichModel.getDateCompleted());
        assertEquals(firstEnrich.getDescription(), firstEnrichModel.getDescription());
    }
}
