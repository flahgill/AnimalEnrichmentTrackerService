package com.nashss.se.animalenrichmenttrackerservice.activity;

import com.nashss.se.animalenrichmenttrackerservice.activity.requests.ViewHabitatEnrichmentActivitesRequest;
import com.nashss.se.animalenrichmenttrackerservice.activity.results.ViewHabitatEnrichmentActivitesResult;
import com.nashss.se.animalenrichmenttrackerservice.comparators.EnrichmentActivityDateComparator;
import com.nashss.se.animalenrichmenttrackerservice.converters.ModelConverter;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.HabitatDao;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.EnrichmentActivity;
import com.nashss.se.animalenrichmenttrackerservice.dynamodb.models.Habitat;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Implementation of the ViewHabitatEnrichmentActivitiesActivity for the AnimalEnrichmentTrackerServices's
 * ViewHabitatEnrichmentActivities API.
 * <p>
 * This API allows a keeper manager to view a given habitat's saved list of enrichment activites in DDB.
 */
public class ViewHabitatEnrichmentActivitiesActivity {
    private final Logger log = LogManager.getLogger();
    private final HabitatDao habitatDao;


    /**
     * Instantiates a new ViewHabitatEnrichmentActivitiesActivity object.
     *
     * @param habitatDao HabitatDao to access the Habitats table.
     */
    @Inject
    public ViewHabitatEnrichmentActivitiesActivity(HabitatDao habitatDao) {
        this.habitatDao = habitatDao;
    }

    /**
     * This method handles the incoming request by retrieving a habitat from the DB.
     * <p>
     * It then returns the habitat's list of completed enrichment activites, sorted by date.
     * <p>
     * If the habitat does not exist, will throw a HabitatNotFoundException.
     *
     * @param viewHabitatEnrichmentActivitesRequest request object containing the requested habitatId.
     * @return viewHabitatEnrichmentsResult result object containing the habitat's
     * list of API defined {@link EnrichmentModel}
     */
    public ViewHabitatEnrichmentActivitesResult handleRequest(final ViewHabitatEnrichmentActivitesRequest
                                                              viewHabitatEnrichmentActivitesRequest) {
        log.info("Recieved ViewHabitatEnrichmentsRequest {}", viewHabitatEnrichmentActivitesRequest);

        Habitat habitat = habitatDao.getHabitat(viewHabitatEnrichmentActivitesRequest.getHabitatId());
        List<EnrichmentActivity> completedEnrichments = habitat.getCompletedEnrichments();
        List<EnrichmentActivityModel> enrichmentActivityModels =
                new ModelConverter().toEnrichmentActivityModelList(completedEnrichments);

        enrichmentActivityModels.sort(new EnrichmentActivityDateComparator());
        Collections.reverse(enrichmentActivityModels);

        return ViewHabitatEnrichmentActivitesResult.builder()
                .withCompletedEnrichments(enrichmentActivityModels)
                .build();
    }
}
