package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.UpdateHabitatEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.UpdateHabitatEnrichmentActivityResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Implementation of the UpdateHabitatEnrichmentActivityActivity for the AnimalEnrichmentTrackerServices's
 * UpdateHabitatEnrichmentActivity API.
 * <p>
 * This API allows a keeper manager to update an EnrichmentActivity in a habitat's list of
 * completedEnrichments saved in DDB, as well as to the master list of EnrichmentActivities.
 */
public class UpdateHabitatEnrichmentActivityActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates new UpdateHabitatEnrichmentActivityActivity.
     * @param habitatDao data access object to access the habitat's table.
     * @param enrichmentActivityDao data access object to access the enrichmentActivities table.
     */
    @Inject
    public UpdateHabitatEnrichmentActivityActivity(HabitatDao habitatDao,
                                                   EnrichmentActivityDao enrichmentActivityDao) {
        this.habitatDao = habitatDao;
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of completedEnrichments, updating the activity that
     * matches the requested activityId, and saving the new list within the habitat.
     * Updates the EnrichmentActivity saved on the master EnrichmentActivities DDB list.
     * <p>
     * returns the saved habitat's list of completed enrichments.
     * <p>
     * If the keeper adding the enrichment is not the owner of the habitat, throws a UserSecurityException.
     * <p>
     * If the activity to be updated is not currently on a habitat, throws EnrichmentActivityNotOnHabitatException.
     * <p>
     * If the activityId does not exist in the habitat's list of completedEnrichments, throws an
     * EnrichmentActivityNotFoundException.
     *
     * @param updateHabitatEnrichmentActivityRequest request object containing the habitatId, keeperManagerId,
     *                                              activityId, and attributes to be updated.
     * @return updateHabitatEnrichmentActivityResult result object containing the habitat's list of
     * completed Enrichments.
     */
    public UpdateHabitatEnrichmentActivityResult handleRequest(final UpdateHabitatEnrichmentActivityRequest
                                                                       updateHabitatEnrichmentActivityRequest) {
        log.info("Recieved UpdateHabitatEnrichmentActivityRequest {}", updateHabitatEnrichmentActivityRequest);

        String habitatId = updateHabitatEnrichmentActivityRequest.getHabitatId();
        Habitat habitat = habitatDao.getHabitat(habitatId);

        if (!habitat.getKeeperManagerId().equals(updateHabitatEnrichmentActivityRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to update it's enrichment activity.");
        }

        String activityId = updateHabitatEnrichmentActivityRequest.getActivityId();
        EnrichmentActivity activityToUpdate = enrichmentActivityDao.getEnrichmentActivity(activityId);

        if (!activityToUpdate.getOnHabitat()) {
            throw new EnrichmentActivityNotOnHabitatException("Activity with id [" + activityId + "] is not " +
                    "currently on a habitat, and cannot be updated.");
        }

        activityToUpdate.setIsComplete(updateHabitatEnrichmentActivityRequest.getIsComplete());
        activityToUpdate.setKeeperRating(updateHabitatEnrichmentActivityRequest.getKeeperRating());
        activityToUpdate.setDateCompleted(updateHabitatEnrichmentActivityRequest.getDateCompleted());
        activityToUpdate = enrichmentActivityDao.saveEnrichmentActivity(activityToUpdate);

        List<EnrichmentActivity> activityList = getCurrEnrichmentsAndCopy(habitat);

        EnrichmentActivity habitatActivityToUpdate = null;
        for (EnrichmentActivity activity : activityList) {
            if (activity.getActivityId().equals(activityId)) {
                habitatActivityToUpdate = activity;
            }
        }

        if (Objects.isNull(habitatActivityToUpdate)) {
            throw new EnrichmentActivityNotFoundException("Activity with id [" + activityId + "] does not exist " +
                    "in habitat[" + habitatId + "].");
        }

        activityList.remove(habitatActivityToUpdate);
        activityList.add(0, activityToUpdate);

        List<EnrichmentActivityModel> activityModelList =
                new ModelConverter().toEnrichmentActivityModelList(activityList);

        habitat.setCompletedEnrichments(activityList);
        habitat = habitatDao.saveHabitat(habitat);

        return UpdateHabitatEnrichmentActivityResult.builder()
                .withCompletedEnrichments(activityModelList)
                .build();

    }

    /**
     * private helper method to extract the habitat's list of current enrichments, and copies to a new list.
     *
     * @param habitat habitat to pull current list of enrichments from.
     * @return copied list of current enrichment activities.
     */
    private static List<EnrichmentActivity> getCurrEnrichmentsAndCopy(Habitat habitat) {
        List<String> acceptableEnrichments = habitat.getAcceptableEnrichmentIds();

        if (acceptableEnrichments == null || acceptableEnrichments.isEmpty()) {
            acceptableEnrichments = new ArrayList<>();
        }

        List<EnrichmentActivity> currEnrichments = habitat.getCompletedEnrichments();

        if (currEnrichments == null) {
            currEnrichments = new ArrayList<>();
        }

        return new ArrayList<>(currEnrichments);
    }
}
