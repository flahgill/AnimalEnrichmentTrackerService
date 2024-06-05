package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.EnrichmentModel;

import java.util.List;

/**
 * SearchEnrichmentsResult object class.
 */
public class SearchEnrichmentsResult {
    private final List<EnrichmentModel> enrichments;

    /**
     * creates SearchEnrichmentsResult object to search through the saved DDB of Enrichments.
     *
     * @param enrichments returned list of enrichments.
     */
    private SearchEnrichmentsResult(List<EnrichmentModel> enrichments) {
        this.enrichments = enrichments;
    }

    public List<EnrichmentModel> getEnrichments() {
        return enrichments;
    }

    @Override
    public String toString() {
        return "SearchEnrichmentsResult{" +
                "enrichments=" + enrichments +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private List<EnrichmentModel> enrichments;
        public Builder withEnrichments(List<EnrichmentModel> enrichments) {
            this.enrichments = enrichments;
            return this;
        }
        public SearchEnrichmentsResult build() {
            return new SearchEnrichmentsResult(enrichments);
        }
    }
}
