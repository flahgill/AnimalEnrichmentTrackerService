package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchEnrichmentActivitiesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchEnrichmentActivitiesResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentActivityDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

import static com.nashss.se.animalenrichmenttrackerservice.utils.NullUtils.ifNull;

/**
 * Implementation of the SearchEnrichmentActivitiesActivity for the AnimalEnrichmentTrackerServices's
 * SearchEnrichmentActivities API.
 * <p>
 * This API allows any user to search through the EnrichmentActivities table saved in DDB.
 */
public class SearchEnrichmentActivitiesActivity {
    private final Logger log = LogManager.getLogger();
    private final EnrichmentActivityDao enrichmentActivityDao;

    /**
     * Instantiates a new SearchEnrichmentActivitiesActivity object.
     * @param enrichmentActivityDao the data access object to access the EnrichmentActivities DDB table.
     */
    @Inject
    public SearchEnrichmentActivitiesActivity(EnrichmentActivityDao enrichmentActivityDao) {
        this.enrichmentActivityDao = enrichmentActivityDao;
    }

    /**
     * This method handles the incoming request by searching for enrichment activities from the database.
     * <p>
     * Returns the matching enrichment activities, or an empty result list if none are found.
     *
     * @param searchEnrichmentActivitiesRequest request object containing the search criteria.
     * @return searchEnrichmentActivitiesResult result object containing the enrichment activities that match
     * the search criteria.
     */
    public SearchEnrichmentActivitiesResult handleRequest(final SearchEnrichmentActivitiesRequest
                                                                  searchEnrichmentActivitiesRequest) {
        log.info("Recieved SearchEnrichmentActivitiesRequest {}", searchEnrichmentActivitiesRequest);

        String criteria = ifNull(searchEnrichmentActivitiesRequest.getCriteria(), "");
        String[] criteriaArray = criteria.isBlank() ? new String[0] : criteria.split("\\s");

        List<EnrichmentActivity> results = enrichmentActivityDao.searchEnrichmentActivities(criteriaArray);
        List<EnrichmentActivityModel> activityModels = new ModelConverter().toEnrichmentActivityModelList(results);

        return SearchEnrichmentActivitiesResult.builder()
                .withEnrichmentActivities(activityModels)
                .build();
    }
}
