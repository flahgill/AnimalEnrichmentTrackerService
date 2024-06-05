package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import java.util.List;

/**
 * SearchEnrichmentActivitiesResult object class.
 */
public class SearchEnrichmentActivitiesResult {
    private final List<EnrichmentActivityModel> enrichmentActivities;

    /**
     * creates SearchEnrichmentActivitiesResult object to search through the saved DDB of EnrichmentActivities.
     *
     * @param enrichmentActivities the returned list of EnrichmentActivities.
     */
    private SearchEnrichmentActivitiesResult(List<EnrichmentActivityModel> enrichmentActivities) {
        this.enrichmentActivities = enrichmentActivities;
    }

    public List<EnrichmentActivityModel> getEnrichmentActivities() {
        return enrichmentActivities;
    }

    @Override
    public String toString() {
        return "SearchEnrichmentActivitiesResult{" +
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
        public SearchEnrichmentActivitiesResult build() {
            return new SearchEnrichmentActivitiesResult(enrichmentActivities);
        }
    }
}
