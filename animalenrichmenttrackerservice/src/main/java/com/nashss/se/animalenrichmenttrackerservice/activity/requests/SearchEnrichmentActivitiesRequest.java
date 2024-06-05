package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * SearchEnrichmentActivitiesRequest object class.
 */
public class SearchEnrichmentActivitiesRequest {
    private final String criteria;

    /**
     * creates SearchEnrichmentActivitiesRequest object to search through the saved DDB of EnrichmentActivities.
     *
     * @param criteria the search criteria.
     */
    private SearchEnrichmentActivitiesRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchEnrichmentActivitiesRequest{" +
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
        public SearchEnrichmentActivitiesRequest build() {
            return new SearchEnrichmentActivitiesRequest(criteria);
        }
    }
}
