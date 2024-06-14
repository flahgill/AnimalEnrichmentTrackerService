package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * AddSpeciesRequest object class.
 */
@JsonDeserialize(builder = AddSpeciesRequest.Builder.class)
public class AddSpeciesRequest {
    private final String speciesToAdd;
    private final String habitatId;
    private final String keeperManagerId;

    /**
     * creates AddSpeciesRequest object to add to a habitat's list of species.
     *
     * @param speciesToAdd the species to add to a habitat's saved list.
     * @param habitatId the habitatId to retrieve the habitat.
     * @param keeperManagerId the keeperManagerId to authenticate the user.
     */
    private AddSpeciesRequest(String speciesToAdd, String habitatId, String keeperManagerId) {
        this.speciesToAdd = speciesToAdd;
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getSpeciesToAdd() {
        return speciesToAdd;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "AddSpeciesRequest{" +
                "speciesToAdd='" + speciesToAdd + '\'' +
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
        private String speciesToAdd;
        private String habitatId;
        private String keeperManagerId;
        public Builder withSpeciesToAdd(String speciesToAdd) {
            this.speciesToAdd = speciesToAdd;
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
        public AddSpeciesRequest build() {
            return new AddSpeciesRequest(speciesToAdd, habitatId, keeperManagerId);
        }
    }

}
