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
    private final String name;
    private final int keeperRating;
    private final String description;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final LocalDate dateCompleted;

    private EnrichmentActivityModel(String activityId, String enrichmentId, String name, int keeperRating,
                                    String description, LocalDate dateCompleted) {
        this.activityId = activityId;
        this.enrichmentId = enrichmentId;
        this.name = name;
        this.keeperRating = keeperRating;
        this.description = description;
        this.dateCompleted = dateCompleted;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getEnrichmentId() {
        return enrichmentId;
    }

    public String getName() {
        return name;
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
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(dateCompleted, that.dateCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId, enrichmentId, name, keeperRating, description, dateCompleted);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String activityId;
        private String enrichmentId;
        private String name;
        private int keeperRating;
        private String description;
        private LocalDate dateCompleted;
        public Builder withActivityId(String activityId) {
            this.activityId = activityId;
            return this;
        }
        public Builder withEnrichmentId(String enrichmentId) {
            this.enrichmentId = enrichmentId;
            return this;
        }
        public Builder withName(String name) {
            this.name = name;
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
        public EnrichmentActivityModel build() {
            return new EnrichmentActivityModel(activityId, enrichmentId, name, keeperRating,
                    description, dateCompleted);
        }
    }
}