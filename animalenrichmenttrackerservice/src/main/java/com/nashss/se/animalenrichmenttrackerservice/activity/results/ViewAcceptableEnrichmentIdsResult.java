package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * ViewAcceptableEnrichmentIdsResult object class.
 */
public class ViewAcceptableEnrichmentIdsResult {
    private final List<String> acceptableEnrichmentIds;

    /**
     * creates ViewAcceptableEnrichmentIdsResult object for retrieving a habitat's list of acceptable enrichment Ids
     * using the habitatId.
     *
     * @param acceptableEnrichmentIds returned list of habitat's acceptable enrichment Ids.
     */
    private ViewAcceptableEnrichmentIdsResult(List<String> acceptableEnrichmentIds) {
        this.acceptableEnrichmentIds = acceptableEnrichmentIds;
    }

    public List<String> getAcceptableEnrichmentIds() {
        return acceptableEnrichmentIds;
    }

    @Override
    public String toString() {
        return "ViewAcceptableEnrichmentIdsResult{" +
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
        public ViewAcceptableEnrichmentIdsResult build() {
            return new ViewAcceptableEnrichmentIdsResult(acceptableEnrichmentIds);
        }
    }
}
