package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * RemoveAnimalFromHabitatRequest object class.
 */
@JsonDeserialize(builder = RemoveAnimalFromHabitatRequest.Builder.class)
public class RemoveAnimalFromHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String animalId;

    /**
     * creates RemoveAnimalFromHabitatRequest object for removing an animal from a habitat
     * using the habitatId and keeperManagerId.
     *
     * @param habitatId the habitatId to identify which habitat to remove the animal from.
     * @param keeperManagerId the keeperManagerId to identify the keeper who can remove the animal.
     * @param animalId the animalId to remove from the habitat.
     */
    private RemoveAnimalFromHabitatRequest(String habitatId, String keeperManagerId, String animalId) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.animalId = animalId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public String getAnimalId() {
        return animalId;
    }

    @Override
    public String toString() {
        return "RemoveAnimalFromHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", animalId='" + animalId + '\'' +
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
        private String animalId;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withAnimalId(String animalId) {
            this.animalId = animalId;
            return this;
        }
        public RemoveAnimalFromHabitatRequest build() {
            return new RemoveAnimalFromHabitatRequest(habitatId, keeperManagerId, animalId);
        }
    }
}
