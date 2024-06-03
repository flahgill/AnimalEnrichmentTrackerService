package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import java.util.List;

/**
 * RemoveEnrichmentActivityFromHabitatResult object class.
 */
public class RemoveEnrichmentActivityFromHabitatResult {
    private final List<EnrichmentActivityModel> completedEnrichments;

    /**
     * creates RemoveEnrichmentActivityFromHabitatResult object persisting the list of completedEnrichments of a habitat
     * after removing an activity.
     *
     * @param completedEnrichments habitat's list of completedEnrichments
     */
    private RemoveEnrichmentActivityFromHabitatResult(List<EnrichmentActivityModel> completedEnrichments) {
        this.completedEnrichments = completedEnrichments;
    }

    public List<EnrichmentActivityModel> getCompletedEnrichments() {
        return completedEnrichments;
    }

    @Override
    public String toString() {
        return "RemoveEnrichmentActivityFromHabitatResult{" +
                "completedEnrichments=" + completedEnrichments +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<EnrichmentActivityModel> completedEnrichments;
        public Builder withCompleteEnrichments(List<EnrichmentActivityModel> completedEnrichments) {
            this.completedEnrichments = completedEnrichments;
            return this;
        }
        public RemoveEnrichmentActivityFromHabitatResult build() {
            return new RemoveEnrichmentActivityFromHabitatResult(completedEnrichments);
        }
    }
}
