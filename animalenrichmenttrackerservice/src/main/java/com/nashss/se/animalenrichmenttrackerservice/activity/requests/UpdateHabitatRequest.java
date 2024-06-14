package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * UpdateHabitatRequest object class.
 */
@JsonDeserialize(builder = UpdateHabitatRequest.Builder.class)
public class UpdateHabitatRequest {
    private final String habitatId;
    private final String habitatName;
    private final String keeperManagerId;
    private final String isActive;

    /**
     * creates UpdateHabitatRequest object for updating a habitat using the habitatId.
     *
     * @param habitatId the habitatId to find the correct habitat to update.
     * @param habitatName the habitatName to update.
     * @param keeperManagerId the keeperManagerId to avoid security concerns.
     */
    private UpdateHabitatRequest(String habitatId, String habitatName,
                                 String keeperManagerId, String isActive) {
        this.habitatId = habitatId;
        this.habitatName = habitatName;
        this.keeperManagerId = keeperManagerId;
        this.isActive = isActive;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getHabitatName() {
        return habitatName;
    }


    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public String getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "UpdateHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", habitatName='" + habitatName + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String habitatId;
        private String habitatName;
        private String keeperManagerId;
        private String isActive;

        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withHabitatName(String habitatName) {
            this.habitatName = habitatName;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }
        public UpdateHabitatRequest build() {
            return new UpdateHabitatRequest(habitatId, habitatName, keeperManagerId, isActive);
        }

    }
}
