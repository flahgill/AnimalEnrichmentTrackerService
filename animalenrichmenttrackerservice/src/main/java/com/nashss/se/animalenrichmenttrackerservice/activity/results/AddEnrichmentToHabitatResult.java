package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import java.util.List;

/**
 * AddEnrichmentToHabitatResult object class.
 */
public class AddEnrichmentToHabitatResult {
    private final List<EnrichmentModel> completedEnrichments;

    /**
     * creates AddEnrichmentToHabitatRequest object persisting the list of completedEnrichments of a habitat after a
     * new addition.
     *
     * @param completedEnrichments the list of completedEnrichments for a given habitat.
     */
    private AddEnrichmentToHabitatResult(List<EnrichmentModel> completedEnrichments) {
        this.completedEnrichments = completedEnrichments;
    }

    public List<EnrichmentModel> getCompletedEnrichments() {
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
        private List<EnrichmentModel> completedEnrichments;
        public Builder withCompletedEnrichments(List<EnrichmentModel> completedEnrichments) {
            this.completedEnrichments = completedEnrichments;
            return this;
        }
        public AddEnrichmentToHabitatResult build() {
            return new AddEnrichmentToHabitatResult(completedEnrichments);
        }
    }
}
