package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;

/**
 * AddEnrichmentActivityToHabitatRequest object class.
 */
@JsonDeserialize(builder = AddEnrichmentActivityToHabitatRequest.Builder.class)
public class AddEnrichmentActivityToHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;
    private final String enrichmentId;
    private final LocalDate dateCompleted;
    private final int keeperRating;
    private final String isComplete;

    /**
     * creates AddEnrichmentActivityToHabitatRequest object for adding to a habitat's list of completedEnrichments.
     *
     * @param habitatId the habitatId to specify which habitat to add the enrichment to.
     * @param keeperManagerId the keeperManagerId to authenticate the user.
     * @param enrichmentId the enrichmentId to specify which enrichment should be added.
     * @param dateCompleted the dateCompleted which user will input.
     * @param keeperRating the keeperRating which user will input.
     */
    private AddEnrichmentActivityToHabitatRequest(String habitatId, String keeperManagerId, String enrichmentId,
                                                  LocalDate dateCompleted, int keeperRating, String isComplete) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
        this.enrichmentId = enrichmentId;
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

    public String getEnrichmentId() {
        return enrichmentId;
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
        return "AddEnrichmentToHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                ", keeperManagerId='" + keeperManagerId + '\'' +
                ", enrichmentId='" + enrichmentId + '\'' +
                ", dateCompleted=" + dateCompleted +
                ", keeperRating=" + keeperRating +
                ", isComplete=" + isComplete +
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
        private String enrichmentId;
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
        public Builder withEnrichmentId(String enrichmentId) {
            this.enrichmentId = enrichmentId;
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
        public AddEnrichmentActivityToHabitatRequest build() {
            return new AddEnrichmentActivityToHabitatRequest(habitatId, keeperManagerId, enrichmentId,
                    dateCompleted, keeperRating, isComplete);
        }
    }
}
