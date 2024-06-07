package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * AddAcceptableIdRequest object class.
 */
@JsonDeserialize(builder = AddAcceptableIdRequest.Builder.class)
public class AddAcceptableIdRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String idToAdd;

    /**
     * creates AddAnimalToHabitatRequest object to add to a  habitat's list of acceptable enrichment ids.
     *
     * @param habitatId the habitatId to identify which habitat to add the acceptable id to.
     * @param keeperManagerId the keeper id to authenticate the user.
     * @param idToAdd the acceptable id to add.
     */
    private AddAcceptableIdRequest(String habitatId, String keeperManagerId, String idToAdd) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.idToAdd = idToAdd;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public String getIdToAdd() {
        return idToAdd;
    }

    @Override
    public String toString() {
        return "AddAcceptableIdRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", idToAdd='" + idToAdd + '\'' +
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
        private String idToAdd;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withIdToAdd(String idToAdd) {
            this.idToAdd = idToAdd;
            return this;
        }
        public AddAcceptableIdRequest build() {
            return new AddAcceptableIdRequest(habitatId, keeperManagerId, idToAdd);
        }
    }
}
