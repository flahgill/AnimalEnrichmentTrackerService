package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import java.util.List;

/**
 * UpdateHabitatEnrichmentActivityResult object class.
 */
public class UpdateHabitatEnrichmentActivityResult {
    private final List<EnrichmentActivityModel> completedEnrichments;

    /**
     * creates UpdateHabitatEnrichmentActivityResult object persisting the list of completedEnrichments of a habitat
     * after updating an EnrichmentActivity.
     *
     * @param completedEnrichments the list of completedEnrichments for a given habitat.
     */
    private UpdateHabitatEnrichmentActivityResult(List<EnrichmentActivityModel> completedEnrichments) {
        this.completedEnrichments = completedEnrichments;
    }

    public List<EnrichmentActivityModel> getCompletedEnrichments() {
        return completedEnrichments;
    }

    @Override
    public String toString() {
        return "UpdateHabitatEnrichmentActivityResult{" +
                "completedEnrichments=" + completedEnrichments +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<EnrichmentActivityModel> completedEnrichments;
        public Builder withCompletedEnrichments(List<EnrichmentActivityModel> completedEnrichments) {
            this.completedEnrichments = completedEnrichments;
            return this;
        }
        public UpdateHabitatEnrichmentActivityResult build() {
            return new UpdateHabitatEnrichmentActivityResult(completedEnrichments);
        }
    }
}
