package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ReAddEnrichmentActivityToHabitatRequest object class.
 */
public class ReAddEnrichmentActivityToHabitatRequest {
    private final String habitatId;
    private final String activityId;
    private final String keeperManagerId;

    /**
     * creates ReAddEnrichmentActivityToHabitatRequest object for re-adding a removed activity to a habitat's
     * list of completedEnrichments.
     *
     * @param habitatId Id of habitat to re-add the activity to.
     * @param activityId Id of activity to re-add.
     * @param keeperManagerId Id of the keeper to authenticate the request.
     */
    private ReAddEnrichmentActivityToHabitatRequest(String habitatId, String activityId, String keeperManagerId) {
        this.habitatId = habitatId;
        this.activityId = activityId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "ReAddEnrichmentActivityToHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String habitatId;
        private String activityId;
        private String keeperManagerId;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withActivityId(String activityId) {
            this.activityId = activityId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public ReAddEnrichmentActivityToHabitatRequest build() {
            return  new ReAddEnrichmentActivityToHabitatRequest(habitatId, activityId, keeperManagerId);
        }

    }
}
