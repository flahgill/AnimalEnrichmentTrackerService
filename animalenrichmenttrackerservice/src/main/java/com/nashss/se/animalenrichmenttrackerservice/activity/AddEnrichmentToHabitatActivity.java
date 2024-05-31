package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddEnrichmentToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddEnrichmentToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UnsuitableEnrichmentForHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the AddAnimalToHabitatActivity for the AnimalEnrichmentTrackerServices's
 * AddAnimalToHabitat API.
 * <p>
 * This API allows a keeper manager to add an animal to a habitat's list of animals saved in DDB.
 */
public class AddEnrichmentToHabitatActivity {
    private Logger log = LogManager.getLogger();
    private HabitatDao habitatDao;
    private EnrichmentDao enrichmentDao;

    /**
     * Instantiates new AddEnrichmentToHabitatActivity.
     * @param habitatDao data access object to access habitats table.
     * @param enrichmentDao data access object to access enrichments table.
     */
    @Inject
    public AddEnrichmentToHabitatActivity(HabitatDao habitatDao, EnrichmentDao enrichmentDao) {
        this.habitatDao = habitatDao;
        this.enrichmentDao = enrichmentDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of completedEnrichments, adding the new enrichment,
     * and saving the new list within the habitat.
     * <p>
     * returns the saved habitat's updated list of enrichments
     * <p>
     * If the enrichment added does not coincide with the habitat's list of acceptableEnrichmentIds,
     * throws an UnsuitableEnrichmentForHabitatException.
     * <p>
     * If the keeper adding the enrichment is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param addEnrichmentToHabitatRequest request object containing the habitatId, enrichmentId, and other enrichment
     *                                      attributes.
     * @return addEnrichmentToHabitatResult result object containing the update list of completedEnrichments.
     */
    public AddEnrichmentToHabitatResult handleRequest(final AddEnrichmentToHabitatRequest
                                                              addEnrichmentToHabitatRequest) {
        log.info("Recieved AddEnrichmentToHabitatRequest {}", addEnrichmentToHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(addEnrichmentToHabitatRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(addEnrichmentToHabitatRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to add a new enrichment activity to it.");
        }

        String enrichId = addEnrichmentToHabitatRequest.getEnrichmentId();

        List<Enrichment> updatedEnrichments = getCurrEnrichmentsAndCopy(habitat, enrichId);

        Enrichment enrichToPull = enrichmentDao.getEnrichment(enrichId);

        Enrichment enrichToAdd = new Enrichment();
        enrichToAdd.setEnrichmentId(enrichToPull.getEnrichmentId());
        enrichToAdd.setName(enrichToPull.getName());
        enrichToAdd.setDescription(enrichToPull.getDescription());
        enrichToAdd.setDateCompleted(addEnrichmentToHabitatRequest.getDateCompleted());
        enrichToAdd.setKeeperRating(addEnrichmentToHabitatRequest.getKeeperRating());

        updatedEnrichments.add(0, enrichToAdd);

        List<EnrichmentModel> updatedEnrichmentModels = new ModelConverter().toEnrichmentModelList(updatedEnrichments);

        habitat.setCompletedEnrichments(updatedEnrichments);
        habitat = habitatDao.saveHabitat(habitat);

        return AddEnrichmentToHabitatResult.builder()
                .withCompletedEnrichments(updatedEnrichmentModels)
                .build();
    }

    /**
     * private helper method to extract the habitat's list of current enrichments, and copies to a new list.
     * Also, checks if enrichment activity is acceptable to provide to the habitat.
     *
     * @param habitat habitat to pull current list of enrichments from.
     * @param enrichId enrichmentId of enrichment to be added.
     * @return copied list of current enrichment activities.
     */
    private static List<Enrichment> getCurrEnrichmentsAndCopy(Habitat habitat, String enrichId) {
        List<String> acceptableEnrichments = habitat.getAcceptableEnrichmentIds();

        if (acceptableEnrichments.isEmpty() || acceptableEnrichments == null) {
            acceptableEnrichments = new ArrayList<>();
        }

        if (!habitat.getAcceptableEnrichmentIds().contains(enrichId)) {
            throw new UnsuitableEnrichmentForHabitatException("Enrichment Activity with Id [" + enrichId +
                    "] is not suitable for this habitat.");
        }

        List<Enrichment> currEnrichments = habitat.getCompletedEnrichments();

        if (currEnrichments == null) {
            currEnrichments = new ArrayList<>();
        }

        return new ArrayList<>(currEnrichments);
    }

}
