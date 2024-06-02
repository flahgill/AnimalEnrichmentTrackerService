package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * RemoveEnrichmentActivityFromHabitatRequest object class.
 */
@JsonDeserialize(builder = RemoveEnrichmentActivityFromHabitatRequest.Builder.class)
public class RemoveEnrichmentActivityFromHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String activityId;

    /**
     * creates RemoveEnrichmentActivityFromHabitatRequest object for removing an activity
     * from a habitat's list of completedEnrichments.
     *
     * @param habitatId the habitatId to specify which habitat to remove the enrichment from.
     * @param keeperManagerId the keeperManagerId to authenticate the user.
     * @param activityId the activityId to specify which enrichmentActivity to remove from habitat.
     */
    private RemoveEnrichmentActivityFromHabitatRequest(String habitatId, String keeperManagerId, String activityId) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.activityId = activityId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public String getActivityId() {
        return activityId;
    }

    @Override
    public String toString() {
        return "RemoveEnrichmentActivityFromHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", activityId='" + activityId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    @JsonPOJOBuilder
    public static class Builder {
        private String habitatId;
        private String keeperManagerId;
        private String activityId;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withActivityId(String activityId) {
            this.activityId = activityId;
            return this;
        }
        public RemoveEnrichmentActivityFromHabitatRequest build() {
            return new RemoveEnrichmentActivityFromHabitatRequest(habitatId, keeperManagerId, activityId);
        }
    }
}
