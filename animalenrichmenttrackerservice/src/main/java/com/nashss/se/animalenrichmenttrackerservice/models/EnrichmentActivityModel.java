package com.nashss.se.animalenrichmenttrackerservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Objects;

public class EnrichmentActivityModel {

    private final String activityId;
    private final String enrichmentId;
    private final String activityName;
    private final int keeperRating;
    private final String description;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final LocalDate dateCompleted;
    private final String habitatId;
    private final String isComplete;
    private final Boolean onHabitat;

    private EnrichmentActivityModel(String activityId, String enrichmentId, String activityName, int keeperRating,
                                    String description, LocalDate dateCompleted, String habitatId, String isComplete,
                                    Boolean onHabitat) {
        this.activityId = activityId;
        this.enrichmentId = enrichmentId;
        this.activityName = activityName;
        this.keeperRating = keeperRating;
        this.description = description;
        this.dateCompleted = dateCompleted;
        this.habitatId = habitatId;
        this.isComplete = isComplete;
        this.onHabitat = onHabitat;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getEnrichmentId() {
        return enrichmentId;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getKeeperRating() {
        return keeperRating;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public Boolean getOnHabitat() {
        return onHabitat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnrichmentActivityModel that = (EnrichmentActivityModel) o;
        return keeperRating == that.keeperRating &&
                Objects.equals(activityId, that.activityId) &&
                Objects.equals(enrichmentId, that.enrichmentId) &&
                Objects.equals(activityName, that.activityName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(dateCompleted, that.dateCompleted) &&
                Objects.equals(habitatId, that.habitatId) &&
                Objects.equals(isComplete, that.isComplete) &&
                Objects.equals(onHabitat, that.onHabitat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, enrichmentId, activityName, keeperRating,
                description, dateCompleted, habitatId, isComplete, onHabitat);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String activityId;
        private String enrichmentId;
        private String activityName;
        private int keeperRating;
        private String description;
        private LocalDate dateCompleted;
        private String habitatId;
        private String isComplete;
        private Boolean onHabitat;
        public Builder withActivityId(String activityId) {
            this.activityId = activityId;
            return this;
        }
        public Builder withEnrichmentId(String enrichmentId) {
            this.enrichmentId = enrichmentId;
            return this;
        }
        public Builder withActivityName(String activityName) {
            this.activityName = activityName;
            return this;
        }
        public Builder withKeeperRating(int keeperRating) {
            this.keeperRating = keeperRating;
            return this;
        }
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }
        public Builder withDateCompleted(LocalDate dateCompleted) {
            this.dateCompleted = dateCompleted;
            return this;
        }
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public Builder withIsComplete(String isComplete) {
            this.isComplete = isComplete;
            return this;
        }
        public Builder withOnHabitat(Boolean onHabitat) {
            this.onHabitat = onHabitat;
            return this;
        }
        public EnrichmentActivityModel build() {
            return new EnrichmentActivityModel(activityId, enrichmentId, activityName, keeperRating,
                    description, dateCompleted, habitatId, isComplete, onHabitat);
        }
    }
}
