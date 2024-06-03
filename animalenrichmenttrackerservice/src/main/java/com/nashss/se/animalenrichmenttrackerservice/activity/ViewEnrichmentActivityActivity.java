package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewEnrichmentActivityRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewEnrichmentActivityResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Implementation of the ViewEnrichmentActivityActivity for the AnimalEnrichmentTrackerServices's
 * ViewEnrichmentActivity API.
 * <p>
 * This API allows a keeper manager to view an EnrichmentActivity saved in DDB.
 */
public class ViewEnrichmentActivityActivity {
    private final Logger log = LogManager.getLogger();
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates a new ViewEnrichmentActivityActivity object.
     *
     * @param enrichmentActivityDao the data access object to access the EnrichmentActivities DDB table.
     */
    @Inject
    public ViewEnrichmentActivityActivity(EnrichmentActivityDao enrichmentActivityDao) {
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * handles the incoming request by retrieving an EnrichmentActivity with the provided activity ID from the request.
     * <p>
     * returns the saved EnrichmentActivity.
     *
     * @param viewEnrichmentActivityRequest request object containing the request activityId.
     * @return viewEnrichmentActivityResult result object containing the API defined {@link EnrichmentActivityModel}
     */
    public ViewEnrichmentActivityResult handleRequest(final ViewEnrichmentActivityRequest
                                                              viewEnrichmentActivityRequest) {
        log.info("Recieved ViewEnrichmentActivityRequest {}", viewEnrichmentActivityRequest);

        EnrichmentActivity activity =
                enrichmentActivityDao.getEnrichmentActivity(viewEnrichmentActivityRequest.getActivityId());
        EnrichmentActivityModel activityModel = new ModelConverter().toEnrichmentActivityModel(activity);

        return ViewEnrichmentActivityResult.builder()
                .withEnrichmentActivity(activityModel)
                .build();
    }
}
