package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewAllEnrichmentActivitiesRequest object class.
 */
public class ViewAllEnrichmentActivitiesRequest {
    private final String isComplete;

    /**
     * creates ViewAllEnrichmentActivitiesRequest object for retrieving a list of enrichment activities based on
     * completion status.
     *
     * @param isComplete completion status of the activity.
     */
    private ViewAllEnrichmentActivitiesRequest(String isComplete) {
        this.isComplete = isComplete;
    }

    public String getIsComplete() {
        return isComplete;
    }

    @Override
    public String toString() {
        return "ViewAllEnrichmentActivitiesRequest{" +
                "isComplete='" + isComplete + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String isComplete;

        public Builder withIsComplete(String isComplete) {
            this.isComplete = isComplete;
            return this;
        }

        public ViewAllEnrichmentActivitiesRequest build() {
            return new ViewAllEnrichmentActivitiesRequest(isComplete);
        }
    }
}
