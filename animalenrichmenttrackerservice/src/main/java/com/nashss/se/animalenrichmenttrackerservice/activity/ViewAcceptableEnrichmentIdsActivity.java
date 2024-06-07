package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewAcceptableEnrichmentIdsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewAcceptableEnrichmentIdsResult;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ViewAcceptableEnrichmentIdsActivity for the AnimalEnrichmentTrackerServices's
 * ViewAcceptableEnrichmentIds API.
 * <p>
 * This API allows a keeper manager to view a habitat's list of acceptable enrichment Ids saved in DDB.
 */
public class ViewAcceptableEnrichmentIdsActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;

    /**
     * Instantiates a new ViewAcceptableEnrichmentIdsActivity.
     *
     * @param habitatDao the data access object to access the Habitats DDB table.
     */
    @Inject
    public ViewAcceptableEnrichmentIdsActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * handles the incoming request by retrieving a habitat's list of acceptable enrichment Ids with the provided
     * habitatId from the request.
     *
     * @param viewAcceptableEnrichmentIdsRequest request object containing the habitatId.
     * @return ViewAcceptableEnrichmentIdsResult result object containing the list of acceptable enrichmentIds, sorted.
     */
    public ViewAcceptableEnrichmentIdsResult handleRequest(final ViewAcceptableEnrichmentIdsRequest
                                                                   viewAcceptableEnrichmentIdsRequest) {
        log.info("Recieved ViewAcceptableEnrichmentIdsRequest {}", viewAcceptableEnrichmentIdsRequest);

        Habitat habitat = habitatDao.getHabitat(viewAcceptableEnrichmentIdsRequest.getHabitatId());
        List<String> acceptableEnrichIds = habitat.getAcceptableEnrichmentIds();

        List<String> sortedIds = new ArrayList<>(acceptableEnrichIds);
        Collections.sort(sortedIds);

        return ViewAcceptableEnrichmentIdsResult.builder()
                .withAcceptableEnrichmentIds(sortedIds)
                .build();
    }

}
