package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
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

        List<Enrichment> completedEnrichments = null;
        if (habitat.getCompletedEnrichments() != null) {
            completedEnrichments = new ArrayList<>(habitat.getCompletedEnrichments());
        }

        return HabitatModel.builder()
                .withHabitatId(habitat.getHabitatId())
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
     * Converts a provided {@link Enrichment} into a {@link EnrichmentModel} representation.
     *
     * @param enrichment the Enrichment to convert to EnrichmentModel
     * @return the converted EnrichmentModel with fields mapped from Enrichment
     */
    public EnrichmentModel toEnrichmentModel(Enrichment enrichment) {
        return EnrichmentModel.builder()
                .withEnrichmentId(enrichment.getEnrichmentId())
                .withName(enrichment.getName())
                .withKeeperRating(enrichment.getKeeperRating())
                .withDescription(enrichment.getDescription())
                .withDateCompleted(enrichment.getDateCompleted())
                .build();
    }

    /**
     * Converts a list of Enrichments to a list of EnrichmentModels.
     *
     * @param enrichments The Enrichments to convert to EnrichmentModels
     * @return The converted list of EnricmentModels
     */
    public List<EnrichmentModel> toEnrichmentModelList(List<Enrichment> enrichments) {
        List<EnrichmentModel> enrichmentModels = new ArrayList<>();

        for (Enrichment enrichment : enrichments) {
            enrichmentModels.add(toEnrichmentModel(enrichment));
        }

        return enrichmentModels;
    }

    /**
     * Converts a list of Habitats to a list of HabitatModels.
     *
     * @param habitats The Habitats to convert to HabitatModels
     * @return The converted list of HabitatModels
     */
    public List<HabitatModel> toPlaylistModelList(List<Habitat> habitats) {
        List<HabitatModel> habitatModels = new ArrayList<>();

        for (Habitat habitat : habitats) {
            habitatModels.add(toHabitatModel(habitat));
        }

        return habitatModels;
    }
}
