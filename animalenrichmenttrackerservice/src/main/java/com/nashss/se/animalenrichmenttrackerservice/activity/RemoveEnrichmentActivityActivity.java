package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.RemoveEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.RemoveEnrichmentActivityResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.exceptions.EnrichmentActivityCurrentlyOnHabitatException;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the RemoveEnrichmentActivity for the AnimalEnrichmentTrackerService API.
 *
 * This API allows the keeper to remove one of their saved EnrichmentActivities.
 */
public class RemoveEnrichmentActivityActivity {
    private final Logger log = LogManager.getLogger();
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates a new RemoveEnrichmentActivityActivity object.
     *
     * @param enrichmentActivityDao data access object to access the EnrichmentActivities DDB table.
     */
    @Inject
    public RemoveEnrichmentActivityActivity(EnrichmentActivityDao enrichmentActivityDao) {
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * handles incoming request by removing the enrichment activity from the database.
     * <p>
     * If the activity is not found, will throw an EnrichmentActivityNotFoundException.
     * <p>
     * If the activity is currently on a habitat, will throw EnrichmentActivityCurrentlyOnHabitatException.
     *
     * @param removeEnrichmentActivityRequest request object containing the activityId.
     * @return removeEnrichmentActivityResult result object containing the API defined {@link EnrichmentActivityModel}
     */
    public RemoveEnrichmentActivityResult handleRequest(final RemoveEnrichmentActivityRequest
                                                                removeEnrichmentActivityRequest) {
        log.info("Recieved RemoveEnrichmentActivityRequest {}", removeEnrichmentActivityRequest);

        String activityId = removeEnrichmentActivityRequest.getActivityId();
        EnrichmentActivity eaToRemove = enrichmentActivityDao.removeEnrichmentActivity(activityId);

        if (eaToRemove.getOnHabitat()) {
            throw new EnrichmentActivityCurrentlyOnHabitatException("activity with id [" + activityId + "] is " +
                    "currently in use by habitat[" + eaToRemove.getHabitatId() + "].");
        }

        eaToRemove = enrichmentActivityDao.removeEnrichmentActivity(activityId);

        EnrichmentActivityModel eaModel = new ModelConverter().toEnrichmentActivityModel(eaToRemove);

        return RemoveEnrichmentActivityResult.builder()
                .withErichmentActivities(eaModel)
                .build();
    }
}
