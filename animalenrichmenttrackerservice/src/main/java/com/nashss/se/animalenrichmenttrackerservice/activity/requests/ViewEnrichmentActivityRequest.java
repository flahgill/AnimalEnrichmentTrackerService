package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewEnrichmentActivityRequest object class.
 */
public class ViewEnrichmentActivityRequest {
    private final String activityId;

    /**
     * creates ViewEnrichmentActivityRequest object for retrieving an EnrichmentActivity using the activityId.
     *
     * @param activityId the activityId used to retrieve the EnrichmentActivity.
     */
    private ViewEnrichmentActivityRequest(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityId() {
        return activityId;
    }

    @Override
    public String toString() {
        return "ViewEnrichmentActivityRequest{" +
                "activityId='" + activityId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String activityId;

        public Builder withActivityId(String activityId) {
            this.activityId = activityId;
            return this;
        }

        public ViewEnrichmentActivityRequest build() {
            return new ViewEnrichmentActivityRequest(activityId);
        }
    }
}
