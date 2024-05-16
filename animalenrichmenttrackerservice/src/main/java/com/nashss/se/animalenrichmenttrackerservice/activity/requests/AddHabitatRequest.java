package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;


@JsonDeserialize(builder = AddHabitatRequest.Builder.class)
public class AddHabitatRequest {
    private final String habitatName;
    private final String keeperManagerId;
    private final List<String> species;

    private AddHabitatRequest(String habitatName, String keeperManagerId, List<String> species) {
        this.habitatName = habitatName;
        this.keeperManagerId = keeperManagerId;
        this.species = species;
    }

    public String getHabitatName() {
        return habitatName;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public List<String> getSpecies() {
        return species;
    }

    @Override
    public String toString() {
        return "AddHabitatRequest{" +
                "habitatName='" + habitatName + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", species=" + species +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String habitatName;
        private String keeperManagerId;
        private List<String> species;

        public Builder withHabitatName(String habitatName) {
            this.habitatName = habitatName;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withSpecies(List<String> species) {
            this.species = species;
            return this;
        }
        public AddHabitatRequest build() {
            return new AddHabitatRequest(habitatName, keeperManagerId, species);
        }
    }
}
