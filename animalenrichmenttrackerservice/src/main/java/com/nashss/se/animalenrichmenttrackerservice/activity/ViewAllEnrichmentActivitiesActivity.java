package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAllEnrichmentActivitiesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAllEnrichmentActivitiesResult;
import com.nashss.se.animalenrichmenttrackerservice.comparators.EnrichmentActivityDateComparator;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;

import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ViewAllEnrichmentActivitiesActivity for the AnimalEnrichmentTrackerServices's
 * ViewAllEnrichmentActivities API.
 * <p>
 * This API allows a keeper manager to view a all enrichmentActivities saved in DDB.
 */
public class ViewAllEnrichmentActivitiesActivity {
    private final Logger log = LogManager.getLogger();
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates new ViewAllEnrichmentActivitiesActivity.
     *
     * @param enrichmentActivityDao data access object to access EnrichmentActivities DDB table.
     */
    @Inject
    public ViewAllEnrichmentActivitiesActivity(EnrichmentActivityDao enrichmentActivityDao) {
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * handles the incoming request by retrieving all enrichment activities based on the user's input of
     * completion status.
     * <p>
     * It then returns a list of enrichment activities.
     *
     * @param viewAllEnrichmentActivitiesRequest request object containing the completion status.
     * @return viewAllEnrichmentActivitiesResult result object containing a list of API defined
     * {@link EnrichmentActivityModel}
     */
    public ViewAllEnrichmentActivitiesResult handleRequest(final ViewAllEnrichmentActivitiesRequest
                                                                   viewAllEnrichmentActivitiesRequest) {
        log.info("Recieved ViewAllEnrichmentActivitiesRequest {}", viewAllEnrichmentActivitiesRequest);

        String completeStatusRequest = viewAllEnrichmentActivitiesRequest.getIsComplete();
        if (completeStatusRequest == null) {
            completeStatusRequest = "complete";
        }

        List<EnrichmentActivity> activities = enrichmentActivityDao.getAllEnrichmentActivities(completeStatusRequest);
        List<EnrichmentActivityModel> activityModels = new ModelConverter().toEnrichmentActivityModelList(activities);
        activityModels.sort(new EnrichmentActivityDateComparator());
        Collections.reverse(activityModels);

        return ViewAllEnrichmentActivitiesResult.builder()
                .withEnrichmentActivities(activityModels)
                .build();
    }
}
