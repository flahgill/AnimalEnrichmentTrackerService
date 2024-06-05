package com.nashss.se.animalenrichmenttrackerservice.models;

import java.util.Objects;

public class EnrichmentModel {

    private final String enrichmentId;
    private final String activityName;
    private final String description;

    private EnrichmentModel(String enrichmentId, String activityName, String description) {
        this.enrichmentId = enrichmentId;
        this.activityName = activityName;
        this.description = description;
    }

    public String getEnrichmentId() {
        return enrichmentId;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDescription() {
        return description;
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
        return Objects.equals(enrichmentId, that.enrichmentId) && Objects.equals(activityName, that.activityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrichmentId, activityName);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String enrichmentId;
        private String activityName;
        private String description;

        public Builder withEnrichmentId(String enrichmentId) {
            this.enrichmentId = enrichmentId;
            return this;
        }
        public Builder withActivityName(String activityName) {
            this.activityName = activityName;
            return this;
        }
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EnrichmentModel build() {
            return new EnrichmentModel(enrichmentId, activityName, description);
        }
    }
}
