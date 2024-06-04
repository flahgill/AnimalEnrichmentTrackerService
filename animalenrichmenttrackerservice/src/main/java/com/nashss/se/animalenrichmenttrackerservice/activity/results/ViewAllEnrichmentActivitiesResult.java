package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import java.util.List;
/**
 * ViewAllEnrichmentActivitiesResult object class.
 */
public class ViewAllEnrichmentActivitiesResult {
    private final List<EnrichmentActivityModel> enrichmentActivities;

    /**
     * creates ViewAllEnrichmentActivitiesResult object for retrieving a list of enrichment activities based on
     * completion status.
     *
     * @param enrichmentActivities the returned list of enrichmentActivities.
     */
    private ViewAllEnrichmentActivitiesResult(List<EnrichmentActivityModel> enrichmentActivities) {
        this.enrichmentActivities = enrichmentActivities;
    }

    public List<EnrichmentActivityModel> getEnrichmentActivities() {
        return enrichmentActivities;
    }

    @Override
    public String toString() {
        return "ViewAllEnrichmentActivitiesResult{" +
                "enrichmentActivities=" + enrichmentActivities +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<EnrichmentActivityModel> enrichmentActivities;
        public Builder withEnrichmentActivities(List<EnrichmentActivityModel> enrichmentActivities) {
            this.enrichmentActivities = enrichmentActivities;
            return this;
        }
        public ViewAllEnrichmentActivitiesResult build() {
            return new ViewAllEnrichmentActivitiesResult(enrichmentActivities);
        }
    }
}
