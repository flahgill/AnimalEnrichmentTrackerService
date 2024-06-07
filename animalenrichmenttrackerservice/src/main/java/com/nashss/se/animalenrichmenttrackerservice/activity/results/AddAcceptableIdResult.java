package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * AddAcceptableIdResult object class.
 */
public class AddAcceptableIdResult {
    private final List<String> acceptableEnrichmentIds;

    /**
     * creates AddAcceptableIdResult object to add to a habitat's list of acceptable enrichment ids.
     *
     * @param acceptableEnrichmentIds the persisted list of acceptable ids after a new addition.
     */
    private AddAcceptableIdResult(List<String> acceptableEnrichmentIds) {
        this.acceptableEnrichmentIds = acceptableEnrichmentIds;
    }

    public List<String> getAcceptableEnrichmentIds() {
        return acceptableEnrichmentIds;
    }

    @Override
    public String toString() {
        return "AddAcceptableIdResult{" +
                "acceptableEnrichmentIds=" + acceptableEnrichmentIds +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<String> acceptableEnrichmentIds;
        public Builder withAcceptableEnrichmentIds(List<String> acceptableEnrichmentIds) {
            this.acceptableEnrichmentIds = acceptableEnrichmentIds;
            return this;
        }
        public AddAcceptableIdResult build() {
            return new AddAcceptableIdResult(acceptableEnrichmentIds);
        }
    }
}
