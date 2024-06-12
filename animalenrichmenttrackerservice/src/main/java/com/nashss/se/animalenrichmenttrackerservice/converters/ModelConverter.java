package com.nashss.se.animalenrichmenttrackerservice.converters;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Animal;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;
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
                .withKeeperName(habitat.getKeeperName())
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
     * converts a provided {@link Enrichment} into a {@link EnrichmentModel} representation.
     *
     * @param enrichment the enrichment to convert to a enrichmentModel.
     * @return the converted enrichmentModel.
     */
    public EnrichmentModel toEnrichmentModel(Enrichment enrichment) {
        return EnrichmentModel.builder()
                .withEnrichmentId(enrichment.getEnrichmentId())
                .withActivityName(enrichment.getActivityName())
                .withDescription(enrichment.getDescription())
                .build();
    }

    /**
     * converts a list of enrichments to a list of enrichmentModels.
     *
     * @param enrichments the list of enrichments to convert to enrichmentModels.
     * @return the converted list of enrichmentModels.
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
    public List<HabitatModel> toHabitatModelList(List<Habitat> habitats) {
        List<HabitatModel> habitatModels = new ArrayList<>();

        for (Habitat habitat : habitats) {
            habitatModels.add(toHabitatModel(habitat));
        }

        return habitatModels;
    }

    /**
     * Converts a provided {@link Animal} into an {@link AnimalModel} representation.
     *
     * @param animal animal object to be converted to an AnimalModel.
     * @return the converted AnimalModel object.
     */
    public AnimalModel toAnimalModel(Animal animal) {
        return AnimalModel.builder()
                .withAnimalId(animal.getAnimalId())
                .withAnimalName(animal.getAnimalName())
                .withAge(animal.getAge())
                .withSex(animal.getSex())
                .withSpecies(animal.getSpecies())
                .withIsActive(animal.getIsActive())
                .withHabitatId(animal.getHabitatId())
                .withOnHabitat(animal.getOnHabitat())
                .build();
    }

    /**
     * Converts a provided list of Animal objects to a list of AnimalModels.
     * @param animals list of animals to be converted to a list of AnimalModels.
     * @return the converted list of AnimalModels.
     */
    public List<AnimalModel> toAnimalModelList(List<Animal> animals) {
        List<AnimalModel> animalModels = new ArrayList<>();

        for (Animal animal : animals) {
            animalModels.add(toAnimalModel(animal));
        }

        return animalModels;
    }
}
