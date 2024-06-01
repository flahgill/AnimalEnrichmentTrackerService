package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;

import java.util.List;

/**
 * AddEnrichmentActivityToHabitatResult object class.
 */
public class AddEnrichmentActivityToHabitatResult {
    private final List<EnrichmentActivityModel> completedEnrichments;

    /**
     * creates AddEnrichmentActivityToHabitatResult object persisting the list of completedEnrichments of a habitat
     * after a new addition.
     *
     * @param completedEnrichments the list of completedEnrichments for a given habitat.
     */
    private AddEnrichmentActivityToHabitatResult(List<EnrichmentActivityModel> completedEnrichments) {
        this.completedEnrichments = completedEnrichments;
    }

    public List<EnrichmentActivityModel> getCompletedEnrichments() {
        return completedEnrichments;
    }

    @Override
    public String toString() {
        return "AddEnrichmentToHabitatResult{" +
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
        public AddEnrichmentActivityToHabitatResult build() {
            return new AddEnrichmentActivityToHabitatResult(completedEnrichments);
        }
    }
}
