package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts between Data and API models.
 */
public class ModelConverter {
    /**
     * Converts a provided {@link Habitat} into a {@link HabitatModel} representation.
     *
     * @param habitat the habitat to convert
     * @return the converted habitat
     */
    public HabitatModel toHabitatModel(Habitat habitat) {
        List<String> species = null;
        if (habitat.getSpecies() != null) {
            species = new ArrayList<>(habitat.getSpecies());
        }

        List<String> animalsInHabitat = null;
        if (habitat.getAnimalsInHabitat() != null) {
            animalsInHabitat = new ArrayList<>(habitat.getAnimalsInHabitat());
        }

        List<String> acceptableEnrichmentIds = null;
        if (habitat.getAcceptableEnrichmentIds() != null) {
            acceptableEnrichmentIds = new ArrayList<>(habitat.getAcceptableEnrichmentIds());
        }

        List<EnrichmentActivity> completedEnrichments = null;
        if (habitat.getCompletedEnrichments() != null) {
            completedEnrichments = new ArrayList<>(habitat.getCompletedEnrichments());
        }

        return HabitatModel.builder()
                .withHabitatId(habitat.getHabitatId())
                .withIsActive(habitat.getIsActive())
                .withHabitatName(habitat.getHabitatName())
                .withSpecies(species)
                .withKeeperManagerId(habitat.getKeeperManagerId())
                .withTotalAnimals(habitat.getTotalAnimals())
                .withAnimalsInHabitat(animalsInHabitat)
                .withAcceptableEnrichmentIds(acceptableEnrichmentIds)
                .withCompletedEnrichments(completedEnrichments)
                .build();
    }

    /**
     * Converts a provided {@link EnrichmentActivity} into a {@link EnrichmentActivityModel} representation.
     *
     * @param enrichmentActivity the Enrichment to convert to EnrichmentModel
     * @return the converted EnrichmentModel with fields mapped from Enrichment
     */
    public EnrichmentActivityModel toEnrichmentActivityModel(EnrichmentActivity enrichmentActivity) {
        return EnrichmentActivityModel.builder()
                .withActivityId(enrichmentActivity.getActivityId())
                .withEnrichmentId(enrichmentActivity.getEnrichmentId())
                .withActivityName(enrichmentActivity.getActivityName())
                .withKeeperRating(enrichmentActivity.getKeeperRating())
                .withDescription(enrichmentActivity.getDescription())
                .withDateCompleted(enrichmentActivity.getDateCompleted())
                .withHabitatId(enrichmentActivity.getHabitatId())
                .withIsComplete(enrichmentActivity.getIsComplete())
                .withOnHabitat(enrichmentActivity.getOnHabitat())
                .build();
    }

    /**
     * Converts a list of EnrichmentActivities to a list of EnrichmentActivityModels.
     *
     * @param enrichmentActivities The EnrichmentActivity's to convert to EnrichmentActivityModels
     * @return The converted list of EnrichmentActivityModels
     */
    public List<EnrichmentActivityModel> toEnrichmentActivityModelList(List<EnrichmentActivity> enrichmentActivities) {
        List<EnrichmentActivityModel> enrichmentModels = new ArrayList<>();

        for (EnrichmentActivity activity : enrichmentActivities) {
            enrichmentModels.add(toEnrichmentActivityModel(activity));
        }

        return enrichmentModels;
    }

    /**
     * Converts a list of Habitats to a list of HabitatModels.
     *
     * @param habitats The Habitats to convert to HabitatModels
     * @return The converted list of HabitatModels
     */
    public List<HabitatModel> toHabitatModelList(List<Habitat> habitats) {
        List<HabitatModel> habitatModels = new ArrayList<>();

        for (Habitat habitat : habitats) {
            habitatModels.add(toHabitatModel(habitat));
        }

        return habitatModels;
    }
}
