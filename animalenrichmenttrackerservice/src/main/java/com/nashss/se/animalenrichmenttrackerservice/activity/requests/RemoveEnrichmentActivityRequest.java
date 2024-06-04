package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * RemoveEnrichmentActivityRequest object class.
 */
public class RemoveEnrichmentActivityRequest {
    private final String activityId;
    private final String keeperManagerId;

    /**
     * creates RemoveEnrichmentActivityRequest object for removing an EnrichmentActivity (hard delete).
     *
     * @param activityId activityId to designate which activity to remove.
     * @param keeperManagerId keeperManagerId to authenticate user.
     */
    private RemoveEnrichmentActivityRequest(String activityId, String keeperManagerId) {
        this.activityId = activityId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "RemoveEnrichmentActivityRequest{" +
                "activityId='" + activityId + '\'' +
                "keeperManagerId='" + keeperManagerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String activityId;
        private String keeperManagerId;
        public Builder withActivityId(String activityId) {
            this.activityId = activityId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public RemoveEnrichmentActivityRequest build() {
            return new RemoveEnrichmentActivityRequest(activityId, keeperManagerId);
        }
    }


}
