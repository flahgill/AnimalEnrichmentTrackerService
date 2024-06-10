package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * RemoveAcceptableIdResult object class.
 */
public class RemoveAcceptableIdResult {
    private List<String> acceptableEnrichmentIds;

    /**
     * creates RemoveAcceptableIdResult object for retrieving a habitat's list of acceptable enrichment ids after
     * removing an id.
     *
     * @param acceptableEnrichmentIds list of acceptable enrichment ids to be returned.
     */
    private RemoveAcceptableIdResult(List<String> acceptableEnrichmentIds) {
        this.acceptableEnrichmentIds = acceptableEnrichmentIds;
    }

    public List<String> getAcceptableEnrichmentIds() {
        return acceptableEnrichmentIds;
    }

    @Override
    public String toString() {
        return "RemoveAcceptableIdResult{" +
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
        public RemoveAcceptableIdResult build() {
            return new RemoveAcceptableIdResult(acceptableEnrichmentIds);
        }
    }
}
