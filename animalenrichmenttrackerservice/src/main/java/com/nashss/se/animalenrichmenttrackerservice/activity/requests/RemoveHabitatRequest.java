package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * RemoveHabitatRequest object class.
 */
@JsonDeserialize(builder = RemoveHabitatRequest.class)
public class RemoveHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;

    /**
     * creates RemoveHabitatRequest object for removing a habitat using the habitatId and keeperManagerId.
     *
     * @param habitatId habitatId to specify which habitat to be removed.
     * @param keeperManagerId keeperManagerId to specify which habitat to be removed.
     */
    public RemoveHabitatRequest(String habitatId, String keeperManagerId) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "RemoveHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
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

        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }

        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }

        public RemoveHabitatRequest build() {
            return new RemoveHabitatRequest(habitatId, keeperManagerId);
        }
    }
}
