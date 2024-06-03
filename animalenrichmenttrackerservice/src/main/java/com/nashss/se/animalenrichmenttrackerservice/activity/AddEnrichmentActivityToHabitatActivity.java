package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.AddEnrichmentActivityToHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.AddEnrichmentActivityToHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UnsuitableEnrichmentForHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import com.nashss.se.animalenrichmenttrackerservice.utils.ServiceUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the AddEnrichmentActivityToHabitatActivity for the AnimalEnrichmentTrackerServices's
 * AddEnrichmentActivityToHabitat API.
 * <p>
 * This API allows a keeper manager to add a completed EnrichmentActivity to a habitat's list of
 * completedEnrichments saved in DDB.
 */
public class AddEnrichmentActivityToHabitatActivity {
    private Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final EnrichmentDao enrichmentDao;
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates new AddEnrichmentToHabitatActivity.
     * @param habitatDao data access object to access habitats table.
     * @param enrichmentDao data access object to access enrichments table.
     * @param enrichmentActivityDao data acces object to access enrichmentActivities table
     */
    @Inject
    public AddEnrichmentActivityToHabitatActivity(HabitatDao habitatDao, EnrichmentDao enrichmentDao,
                                                  EnrichmentActivityDao enrichmentActivityDao) {
        this.habitatDao = habitatDao;
        this.enrichmentDao = enrichmentDao;
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of completedEnrichments,
     * adding the new enrichment activity, and saving the new list within the habitat.
     * Also saves the new enrichment activity to the master EnrichmentActivities table.
     * <p>
     * returns the saved habitat's updated list of enrichments
     * <p>
     * If the enrichment added does not coincide with the habitat's list of acceptableEnrichmentIds,
     * throws an UnsuitableEnrichmentForHabitatException.
     * <p>
     * If the keeper adding the enrichment is not the owner of the habitat, throws a UserSecurityException.
     *
     * @param addEnrichmentActivityToHabitatRequest request object containing the habitatId,
     *                                              enrichmentId, and other enrichment attributes.
     * @return addEnrichmentToHabitatResult result object containing the update list of completedEnrichments.
     */
    public AddEnrichmentActivityToHabitatResult handleRequest(final AddEnrichmentActivityToHabitatRequest
                                                              addEnrichmentActivityToHabitatRequest) {
        log.info("Recieved AddEnrichmentActivityToHabitatRequest {}", addEnrichmentActivityToHabitatRequest);

        String habitatId = addEnrichmentActivityToHabitatRequest.getHabitatId();
        Habitat habitat = habitatDao.getHabitat(habitatId);

        if (!habitat.getKeeperManagerId().equals(addEnrichmentActivityToHabitatRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to add a new enrichment activity to it.");
        }

        String enrichId = addEnrichmentActivityToHabitatRequest.getEnrichmentId();

        List<EnrichmentActivity> updatedEnrichments = getCurrEnrichmentsAndCopy(habitat, enrichId);

        Enrichment enrichToPull = enrichmentDao.getEnrichment(enrichId);

        EnrichmentActivity activityToAdd = new EnrichmentActivity();
        activityToAdd.setActivityId(ServiceUtils.generateId());
        activityToAdd.setEnrichmentId(enrichToPull.getEnrichmentId());
        activityToAdd.setName(enrichToPull.getName());
        activityToAdd.setDescription(enrichToPull.getDescription());
        activityToAdd.setDateCompleted(addEnrichmentActivityToHabitatRequest.getDateCompleted());
        activityToAdd.setKeeperRating(addEnrichmentActivityToHabitatRequest.getKeeperRating());
        activityToAdd.setHabitatId(habitatId);
        activityToAdd.setIsComplete(addEnrichmentActivityToHabitatRequest.getIsComplete());

        enrichmentActivityDao.saveEnrichmentActivity(activityToAdd);

        updatedEnrichments.add(0, activityToAdd);

        List<EnrichmentActivityModel> updatedEnrichmentModels =
                new ModelConverter().toEnrichmentActivityModelList(updatedEnrichments);

        habitat.setCompletedEnrichments(updatedEnrichments);
        habitat = habitatDao.saveHabitat(habitat);

        return AddEnrichmentActivityToHabitatResult.builder()
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
    private static List<EnrichmentActivity> getCurrEnrichmentsAndCopy(Habitat habitat, String enrichId) {
        List<String> acceptableEnrichments = habitat.getAcceptableEnrichmentIds();

        if (acceptableEnrichments == null || acceptableEnrichments.isEmpty()) {
            acceptableEnrichments = new ArrayList<>();
        }

        if (!habitat.getAcceptableEnrichmentIds().contains(enrichId)) {
            throw new UnsuitableEnrichmentForHabitatException("Enrichment Activity with Id [" + enrichId +
                    "] is not suitable for this habitat.");
        }

        List<EnrichmentActivity> currEnrichments = habitat.getCompletedEnrichments();

        if (currEnrichments == null) {
            currEnrichments = new ArrayList<>();
        }

        return new ArrayList<>(currEnrichments);
    }

}
