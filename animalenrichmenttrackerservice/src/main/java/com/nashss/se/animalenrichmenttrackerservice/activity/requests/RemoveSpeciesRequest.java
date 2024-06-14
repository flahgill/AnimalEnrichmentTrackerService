package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * RemoveSpeciesRequest object class.
 */
@JsonDeserialize(builder = RemoveSpeciesRequest.Builder.class)
public class RemoveSpeciesRequest {
    private final String speciesToRemove;
    private final String habitatId;
    private final String keeperManagerId;

    /**
     * creates RemoveSpeciesRequest object for removing a species from a habitat's saved list of species using the
     * habitatId and keeperManagerId.
     *
     * @param speciesToRemove species to remove.
     * @param habitatId habitatId to retrieve the habitat.
     * @param keeperManagerId keeperManagerId to authenticate the user.
     */
    private RemoveSpeciesRequest(String speciesToRemove, String habitatId, String keeperManagerId) {
        this.speciesToRemove = speciesToRemove;
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getSpeciesToRemove() {
        return speciesToRemove;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "RemoveSpeciesRequest{" +
                "speciesToRemove='" + speciesToRemove + '\'' +
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
        private String speciesToRemove;
        private String habitatId;
        private String keeperManagerId;
        public Builder withSpeciesToRemove(String speciesToRemove) {
            this.speciesToRemove = speciesToRemove;
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
        public RemoveSpeciesRequest build() {
            return new RemoveSpeciesRequest(speciesToRemove, habitatId, keeperManagerId);
        }
    }
}
