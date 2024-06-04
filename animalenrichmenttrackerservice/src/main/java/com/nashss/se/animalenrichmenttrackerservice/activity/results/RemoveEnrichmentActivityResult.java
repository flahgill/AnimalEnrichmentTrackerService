package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

/**
 * RemoveEnrichmentActivityResult object class.
 */
public class RemoveEnrichmentActivityResult {
    private final EnrichmentActivityModel activityModel;

    /**
     * creates RemoveEnrichmentActivityResult object for removing an EnrichmentActivity (hard delete).
     *
     * @param enrichmentActivities returned list of enrichmentActivities to persist after removal.
     */
    private RemoveEnrichmentActivityResult(EnrichmentActivityModel enrichmentActivities) {
        this.activityModel = enrichmentActivities;
    }

    public EnrichmentActivityModel getActivityModel() {
        return activityModel;
    }

    @Override
    public String toString() {
        return "RemoveEnrichmentActivityResult{" +
                "activityModel=" + activityModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private EnrichmentActivityModel activityModel;
        public Builder withErichmentActivities(EnrichmentActivityModel activityModel) {
            this.activityModel = activityModel;
            return this;
        }
        public RemoveEnrichmentActivityResult build() {
            return new RemoveEnrichmentActivityResult(activityModel);
        }
    }
}
