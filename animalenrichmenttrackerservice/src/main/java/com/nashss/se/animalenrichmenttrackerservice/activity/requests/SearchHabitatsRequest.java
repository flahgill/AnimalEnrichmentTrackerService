package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * SearchHabitatsRequest object class.
 */
public class SearchHabitatsRequest {

    private final String criteria;

    /**
     * creates SearchHabitatsRequest object to search through the saved DDB of Habitats.
     *
     * @param criteria the search criteria.
     */
    private SearchHabitatsRequest(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    @Override
    public String toString() {
        return "SearchHabitatsRequest{" +
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
        public SearchHabitatsRequest build() {
            return new SearchHabitatsRequest(criteria);
        }
    }
}
