package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import java.time.LocalDate;

/**
 * RemoveEnrichmentFromHabitatRequest object class.
 */
public class RemoveEnrichmentFromHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String enrichmentId;
    private final LocalDate dateCompleted;

    /**
     * creates AddEnrichmentToHabitatRequest object for removing from a habitat's list of completedEnrichments.
     *
     * @param habitatId the habitatId to specify which habitat to remove the enrichment from.
     * @param keeperManagerId the keeperManagerId to authenticate the user.
     * @param enrichmentId the enrichmentId to specify which enrichment should be removed.
     * @param dateCompleted the dateCompleted to differentiate the enrichment being removed.
     */
    private RemoveEnrichmentFromHabitatRequest(String habitatId, String keeperManagerId, String enrichmentId,
                                               LocalDate dateCompleted) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.enrichmentId = enrichmentId;
        this.dateCompleted = dateCompleted;
    }
}
