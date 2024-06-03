package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;

/**
 * UpdateHabitatEnrichmentActivityRequest object class.
 */
@JsonDeserialize(builder = UpdateHabitatEnrichmentActivityRequest.Builder.class)
public class UpdateHabitatEnrichmentActivityRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String activityId;
    private final LocalDate dateCompleted;
    private final int keeperRating;
    private final String isComplete;

    /**
     * creates UpdateHabitatEnrichmentActivityRequest object for updating an Enrichment Activity.
     *
     * @param habitatId the habitatId to specify which habitat to update.
     * @param keeperManagerId the keeperManagerId to authenticate the user.
     * @param activityId the activityId to specify which activity to update.
     * @param dateCompleted the dateCompleted which user will input.
     * @param keeperRating the keeperRating which user will input.
     * @param isComplete the isComplete which user will input.
     */
    private UpdateHabitatEnrichmentActivityRequest(String habitatId, String keeperManagerId, String activityId,
                                                   LocalDate dateCompleted, int keeperRating, String isComplete) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.activityId = activityId;
        this.dateCompleted = dateCompleted;
        this.keeperRating = keeperRating;
        this.isComplete = isComplete;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    public String getActivityId() {
        return activityId;
    }

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public int getKeeperRating() {
        return keeperRating;
    }

    public String getIsComplete() {
        return isComplete;
    }

    @Override
    public String toString() {
        return "UpdateHabitatEnrichmentActivityRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", dateCompleted=" + dateCompleted +
                ", keeperRating=" + keeperRating +
                ", isComplete='" + isComplete + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Buidler
    public static Builder builder() {
        return new Builder();
    }
    @JsonPOJOBuilder
    public static class Builder {
        private String habitatId;
        private String keeperManagerId;
        private String activityId;

        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dateCompleted;
        private int keeperRating;
        private String isComplete;

        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }

        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public Builder withActivityId(String activityId) {
            this.activityId = activityId;
            return this;
        }
        public Builder withDateCompleted(LocalDate dateCompleted) {
            this.dateCompleted = dateCompleted;
            return this;
        }
        public Builder withKeeperRating(int keeperRating) {
            this.keeperRating = keeperRating;
            return this;
        }
        public Builder withIsComplete(String isComplete) {
            this.isComplete = isComplete;
            return this;
        }
        public UpdateHabitatEnrichmentActivityRequest build() {
            return new UpdateHabitatEnrichmentActivityRequest(habitatId, keeperManagerId, activityId, dateCompleted,
                    keeperRating, isComplete);
        }
    }
}
