package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

/**
 * UpdateHabitatRequest object class.
 */
@JsonDeserialize(builder = UpdateHabitatRequest.Builder.class)
public class UpdateHabitatRequest {
    private final String habitatId;
    private final String habitatName;
    private final List<String> species;
    private final String keeperManagerId;

    /**
     * creates UpdateHabitatRequest object for updating a habitat using the habitatId.
     *
     * @param habitatId the habitatId to find the correct habitat to update.
     * @param habitatName the habitatName to update.
     * @param species the species to update.
     * @param keeperManagerId the keeperManagerId to avoid security concerns.
     */
    private UpdateHabitatRequest(String habitatId, String habitatName, List<String> species,
                                 String keeperManagerId) {
        this.habitatId = habitatId;
        this.habitatName = habitatName;
        this.species = species;
        this.keeperManagerId = keeperManagerId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getHabitatName() {
        return habitatName;
    }

    public List<String> getSpecies() {
        return species;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "UpdateHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", habitatName='" + habitatName + '\'' +
                ", species=" + species +
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
        private String habitatName;
        private List<String> species;
        private String keeperManagerId;

        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withHabitatName(String habitatName) {
            this.habitatName = habitatName;
            return this;
        }
        public Builder withSpecies(List<String> species) {
            this.species = species;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public UpdateHabitatRequest build() {
            return new UpdateHabitatRequest(habitatId, habitatName, species, keeperManagerId);
        }

    }
}
