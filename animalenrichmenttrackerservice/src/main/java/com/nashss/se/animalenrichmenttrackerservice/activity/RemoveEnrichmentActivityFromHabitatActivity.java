package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveEnrichmentActivityFromHabitatRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveEnrichmentActivityFromHabitatResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityNotFoundException;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.UserSecurityException;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RemoveEnrichmentActivityFromHabitatActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates new RemoveEnrichmentActivityFromHabitatActivity.
     * @param habitatDao data access object to access habitats table.
     * @param enrichmentActivityDao data access object to access enrichmentActivities table.
     */
    @Inject
    public RemoveEnrichmentActivityFromHabitatActivity(HabitatDao habitatDao,
                                                       EnrichmentActivityDao enrichmentActivityDao) {
        this.habitatDao = habitatDao;
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of completedEnrichments, removing the activity that
     * matches the requested activityId, and saving the new list within the habitat.
     * Does not remove from master list of EnrichmentActivities in DDB, but does mark as 'incomplete'.
     * <p>
     * returns the saved habitat's updated list of completed enrichments
     * <p>
     * If the keeper adding the enrichment is not the owner of the habitat, throws a UserSecurityException.
     * <p>
     * If the activityId does not exist in the habitat's list of completedEnrichments, throws an
     * EnrichmentActivityNotFoundException.
     *
     * @param removeEnrichmentActivityFromHabitatRequest request object containing the habitatId, keeperManagerId, and
     *                                                   activityId.
     * @return removeEnrichmentActivityFromHabitatResult result object containing the habitat's list of
     * completeEnrichments.
     */
    public RemoveEnrichmentActivityFromHabitatResult handleRequest(final RemoveEnrichmentActivityFromHabitatRequest
                                                                           removeEnrichmentActivityFromHabitatRequest) {
        log.info("Revieced RemoveEnrichmentActivityFromHabitatRequest {}",
                removeEnrichmentActivityFromHabitatRequest);

        Habitat habitat = habitatDao.getHabitat(removeEnrichmentActivityFromHabitatRequest.getHabitatId());

        if (!habitat.getKeeperManagerId().equals(removeEnrichmentActivityFromHabitatRequest.getKeeperManagerId())) {
            throw new UserSecurityException("You must own this habitat to remove an enrichment activity from it.");
        }

        List<EnrichmentActivity> activityList = getCurrEnrichmentsAndCopy(habitat);

        EnrichmentActivity activityToRemove = null;
        for (EnrichmentActivity activity : activityList) {
            String activityIdToRemove = removeEnrichmentActivityFromHabitatRequest.getActivityId();
            if (activity.getActivityId().equals(activityIdToRemove)) {
                activityToRemove = activity;
            }
        }

        if (Objects.isNull(activityToRemove)) {
            throw new EnrichmentActivityNotFoundException("Activity with id [" +
                    removeEnrichmentActivityFromHabitatRequest.getActivityId() + "] does not exist in habitat[" +
                    removeEnrichmentActivityFromHabitatRequest.getHabitatId() + "].");
        }

        activityList.remove(activityToRemove);

        activityToRemove.setIsComplete("incomplete");
        activityToRemove.setOnHabitat(false);
        enrichmentActivityDao.saveEnrichmentActivity(activityToRemove);

        List<EnrichmentActivityModel> activityModelList =
                new ModelConverter().toEnrichmentActivityModelList(activityList);

        habitat.setCompletedEnrichments(activityList);
        habitat = habitatDao.saveHabitat(habitat);

        return RemoveEnrichmentActivityFromHabitatResult.builder()
                .withCompleteEnrichments(activityModelList)
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
