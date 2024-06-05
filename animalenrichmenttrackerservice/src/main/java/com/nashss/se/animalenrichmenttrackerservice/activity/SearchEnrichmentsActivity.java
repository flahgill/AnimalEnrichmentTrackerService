package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.SearchEnrichmentsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.SearchEnrichmentsResult;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.EnrichmentDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

import static com.nashss.se.animalenrichmenttrackerservice.utils.NullUtils.ifNull;

/**
 * Implementation of the SearchEnrichmentsActivity for the AnimalEnrichmentTrackerServices's
 * SearchEnrichments API.
 * <p>
 * This API allows any user to search through the Enrichments table saved in DDB.
 */
public class SearchEnrichmentsActivity {
    private final Logger log = LogManager.getLogger();
    private final EnrichmentDao enrichmentDao;

    /**
     * Instantiates a new SearchEnrichmentsActivity object.
     *
     * @param enrichmentDao data access object to access the saved Enrichments DDB table.
     */
    @Inject
    public SearchEnrichmentsActivity(EnrichmentDao enrichmentDao) {
        this.enrichmentDao = enrichmentDao;
    }

    /**
     * This method handles the incoming request by searching for enrichments from the database.
     * <p>
     * Returns the matching enrichments, or an empty result list if none are found.
     *
     * @param searchEnrichmentsRequest reuqest object containing the search criteria.
     * @return searchEnrichmentsResult result object containing the enrichments that match the search criteria.
     */
    public SearchEnrichmentsResult handleRequest(final SearchEnrichmentsRequest searchEnrichmentsRequest) {
        log.info("Recieved SearchEnrichmentsRequest {}", searchEnrichmentsRequest);

        String criteria = ifNull(searchEnrichmentsRequest.getCriteria(), "");
        String[] criteriaArray = criteria.isBlank() ? new String[0] : criteria.split("\\s");

        List<Enrichment> results = enrichmentDao.searchEnrichments(criteriaArray);
        List<EnrichmentModel> enrichmentModels = new ModelConverter().toEnrichmentModelList(results);

        return SearchEnrichmentsResult.builder()
                .withEnrichments(enrichmentModels)
                .build();
    }

}
