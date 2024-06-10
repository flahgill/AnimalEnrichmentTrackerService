package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * RemoveAcceptableIdRequest object class.
 */
@JsonDeserialize(builder = RemoveAcceptableIdRequest.Builder.class)
public class RemoveAcceptableIdRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String idToRemove;

    /**
     * creates RemoveAcceptableIdRequest object for removing an acceptable id from a habitat's saved list of acceptable
     * enrichment ids using the habitatId and keeperManagerId.
     *
     * @param habitatId the habitatId to identify which habitat to remove the acceptable id from.
     * @param keeperManagerId the keeperManagerId to authenticate the user removing the id.
     * @param idToRemove the acceptable id to remove from the habitat.
     */
    private RemoveAcceptableIdRequest(String habitatId, String keeperManagerId, String idToRemove) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.idToRemove = idToRemove;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public String getIdToRemove() {
        return idToRemove;
    }

    @Override
    public String toString() {
        return "RemoveAcceptableIdRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", idToRemove='" + idToRemove + '\'' +
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
        private String idToRemove;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withIdToRemove(String idToRemove) {
            this.idToRemove = idToRemove;
            return this;
        }
        public RemoveAcceptableIdRequest build() {
            return new RemoveAcceptableIdRequest(habitatId, keeperManagerId, idToRemove);
        }
    }
}
