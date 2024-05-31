package com.nashss.se.animalenrichmenttrackerservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.Objects;

public class EnrichmentModel {

    private final String enrichmentId;
    private final String name;
    private final int keeperRating;
    private final String description;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private LocalDate dateCompleted;

    private EnrichmentModel(String enrichmentId, String name, int keeperRating,
                            String description, LocalDate dateCompleted) {
        this.enrichmentId = enrichmentId;
        this.name = name;
        this.keeperRating = keeperRating;
        this.description = description;
        this.dateCompleted = dateCompleted;
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
        EnrichmentModel that = (EnrichmentModel) o;
        return Objects.equals(enrichmentId, that.enrichmentId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrichmentId, name);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String enrichmentId;
        private String name;
        private int keeperRating;
        private String description;
        private LocalDate dateCompleted;

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
        public EnrichmentModel build() {
            return new EnrichmentModel(enrichmentId, name, keeperRating,
                    description, dateCompleted);
        }
    }
}
