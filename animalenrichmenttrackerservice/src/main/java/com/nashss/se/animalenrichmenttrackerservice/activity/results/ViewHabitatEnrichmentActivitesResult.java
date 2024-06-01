package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentActivityModel;
import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * ViewHabitatEnrichmentActivitesResult object class.
 */
@JsonSerialize()
public class ViewHabitatEnrichmentActivitesResult {
    private final List<EnrichmentActivityModel> completedEnrichments;

    /**
     * creates ViewHabitatEnrichmentActivitesResult object containing a habitat's list of completed enrichment activities.
     *
     * @param completedEnrichments the list of completed enrichment activities for a given habitat.
     */
    private ViewHabitatEnrichmentActivitesResult(List<EnrichmentActivityModel> completedEnrichments) {
        this.completedEnrichments = completedEnrichments;
    }

    public List<EnrichmentActivityModel> getCompletedEnrichments() {
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
        private List<EnrichmentActivityModel> completedEnrichments;
        public Builder withCompletedEnrichments(List<EnrichmentActivityModel> completedEnrichments) {
            this.completedEnrichments = completedEnrichments;
            return this;
        }
        public ViewHabitatEnrichmentActivitesResult build() {
            return new ViewHabitatEnrichmentActivitesResult(completedEnrichments);
        }
    }
}
