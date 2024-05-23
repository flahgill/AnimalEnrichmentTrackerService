package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * AddAnimalToHabitatRequest object class.
 */
@JsonDeserialize(builder = AddAnimalToHabitatRequest.Builder.class)
public class AddAnimalToHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String animalToAdd;

    /**
     * creates AddAnimalToHabitatRequest object for add to a  habitat's list of animals.
     *
     * @param habitatId the habitatId to identify which habitat to add the animal to.
     * @param animalToAdd the animal to add to the habitat.
     */
    private AddAnimalToHabitatRequest(String habitatId, String keeperManagerId, String animalToAdd) {
        this.animalToAdd = animalToAdd;
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getAnimalToAdd() {
        return animalToAdd;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "AddAnimalToHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", animalToAdd='" + animalToAdd + '\'' +
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
        private String animalToAdd;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }

        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withAnimalToAdd(String animalToAdd) {
            this.animalToAdd = animalToAdd;
            return this;
        }
        public AddAnimalToHabitatRequest build() {
            return new AddAnimalToHabitatRequest(habitatId, keeperManagerId, animalToAdd);
        }
    }
}
