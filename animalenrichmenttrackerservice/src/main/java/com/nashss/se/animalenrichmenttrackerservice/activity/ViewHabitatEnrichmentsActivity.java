package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatEnrichmentsRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatEnrichmentsResult;
import com.nashss.se.animalenrichmenttrackerservice.comparators.EnrichmentDateComparator;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Enrichment;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ViewHabitatEnrichmentsActivity for the AnimalEnrichmentTrackerServices's
 * ViewHabitatEnrichments API.
 * <p>
 * This API allows a keeper manager to view a given habitat's saved list of enrichments in DDB.
 */
public class ViewHabitatEnrichmentsActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;


    /**
     * Instantiates a new ViewHabitatEnrichmentsActivity object.
     *
     * @param habitatDao HabitatDao to access the Habitats table.
     */
    @Inject
    public ViewHabitatEnrichmentsActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * This method handles the incoming request by retrieving a habitat from the DB.
     * <p>
     * It then returns the habitat's list of completed enrichment activites, sorted by date.
     * <p>
     * If the habitat does not exist, will throw a HabitatNotFoundException.
     *
     * @param viewHabitatEnrichmentsRequest request object containing the requested habitatId.
     * @return viewHabitatEnrichmentsResult result object containing the habitat's
     * list of API defined {@link EnrichmentModel}
     */
    public ViewHabitatEnrichmentsResult handleRequest(final ViewHabitatEnrichmentsRequest
                                                              viewHabitatEnrichmentsRequest) {
        log.info("Recieved ViewHabitatEnrichmentsRequest {}", viewHabitatEnrichmentsRequest);

        Habitat habitat = habitatDao.getHabitat(viewHabitatEnrichmentsRequest.getHabitatId());
        List<Enrichment> completedEnrichments = habitat.getCompletedEnrichments();
        List<EnrichmentModel> enrichmentModels = new ModelConverter().toEnrichmentModelList(completedEnrichments);

        enrichmentModels.sort(new EnrichmentDateComparator());
        Collections.reverse(enrichmentModels);

        return ViewHabitatEnrichmentsResult.builder()
                .withCompletedEnrichments(enrichmentModels)
                .build();
    }
}
