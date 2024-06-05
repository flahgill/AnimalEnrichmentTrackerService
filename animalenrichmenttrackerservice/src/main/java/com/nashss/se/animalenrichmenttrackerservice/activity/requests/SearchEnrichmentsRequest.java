package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * SearchEnrichmentsRequest object class.
 */
public class SearchEnrichmentsRequest {
    private final String criteria;

    /**
     * creates SearchEnrichmentsRequest object to search through the saved DDB of Enrichments.
     *
     * @param criteria the search criteria.
     */
    private SearchEnrichmentsRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchEnrichmentsRequest{" +
                "criteria='" + criteria + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String criteria;

        public Builder withCriteria(String criteria) {
            this.criteria = criteria;
            return this;
        }
        public SearchEnrichmentsRequest build() {
            return new SearchEnrichmentsRequest(criteria);
        }
    }
}
