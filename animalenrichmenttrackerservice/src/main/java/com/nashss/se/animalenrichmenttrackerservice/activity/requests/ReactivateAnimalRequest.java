package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * ReactivateAnimalRequest object class.
 */
@JsonDeserialize(builder = ReactivateAnimalRequest.Builder.class)
public class ReactivateAnimalRequest {
    private final String animalId;
    private final String habitatId;
    private final String keeperManagerId;

    /**
     * creates ReactivateAnimalRequest object for re-adding a removed animal to a habitat.
     *
     * @param animalId animalId to specify which animal to reactivate.
     * @param habitatId the habitatId to specify which habitat to add the animal to.
     * @param keeperManagerId the keeperManagerId to authenticate the user.
     */
    private ReactivateAnimalRequest(String animalId, String habitatId, String keeperManagerId) {
        this.animalId = animalId;
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getAnimalId() {
        return animalId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "ReactivateAnimalRequest{" +
                "animalId='" + animalId + '\'' +
                ", habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    @JsonPOJOBuilder
    public static class Builder {
        private String animalId;
        private String habitatId;
        private String keeperManagerId;
        public Builder withAnimalId(String animalId) {
            this.animalId = animalId;
            return this;
        }
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public ReactivateAnimalRequest build() {
            return new ReactivateAnimalRequest(animalId, habitatId, keeperManagerId);
        }
    }
}
