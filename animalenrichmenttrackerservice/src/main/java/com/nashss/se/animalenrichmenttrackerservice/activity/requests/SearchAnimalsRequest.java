package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * SearchAnimalsRequest object class.
 */
public class SearchAnimalsRequest {
    private final String criteria;

    /**
     * creates SearchAnimalsRequest object to search through the saved DDB of Animals.
     *
     * @param criteria the search criteria.
     */
    private SearchAnimalsRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchAnimalsRequest{" +
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
        public SearchAnimalsRequest build() {
            return new SearchAnimalsRequest(criteria);
        }
    }
}
