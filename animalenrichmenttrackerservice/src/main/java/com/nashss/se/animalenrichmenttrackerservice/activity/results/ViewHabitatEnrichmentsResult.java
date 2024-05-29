package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * ViewHabitatEnrichmentsResult object class.
 */
@JsonSerialize()
public class ViewHabitatEnrichmentsResult {
    private final List<EnrichmentModel> completedEnrichments;

    /**
     * creates ViewHabitatEnrichmentsResult object containing a habitat's list of completed enrichment activities.
     *
     * @param completedEnrichments the list of completed enrichment activities for a given habitat.
     */
    private ViewHabitatEnrichmentsResult(List<EnrichmentModel> completedEnrichments) {
        this.completedEnrichments = completedEnrichments;
    }

    public List<EnrichmentModel> getCompletedEnrichments() {
        return completedEnrichments;
    }

    @Override
    public String toString() {
        return "ViewHabitatEnrichmentsResult{" +
                "completedEnrichments=" + completedEnrichments +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    @JsonPOJOBuilder
    public static class Builder {
        private List<EnrichmentModel> completedEnrichments;
        public Builder withCompletedEnrichments(List<EnrichmentModel> completedEnrichments) {
            this.completedEnrichments = completedEnrichments;
            return this;
        }
        public ViewHabitatEnrichmentsResult build() {
            return new ViewHabitatEnrichmentsResult(completedEnrichments);
        }
    }
}
