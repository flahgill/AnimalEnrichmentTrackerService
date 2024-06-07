package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewAcceptableEnrichmentIdsRequest object class.
 */
public class ViewAcceptableEnrichmentIdsRequest {
    private final String habitatId;

    /**
     * creates ViewAcceptableEnrichmentIdsRequest object for retrieving a habitat's list of acceptable enrichment Ids
     * using the habitatId.
     *
     * @param habitatId the habitatId to retrieve the habitat's list of acceptable enrichment Ids.
     */
    private ViewAcceptableEnrichmentIdsRequest(String habitatId) {
        this.habitatId = habitatId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    @Override
    public String toString() {
        return "ViewAcceptableEnrichmentIdsRequest{" +
                "habitatId='" + habitatId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String habitatId;
        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }
        public ViewAcceptableEnrichmentIdsRequest build() {
            return new ViewAcceptableEnrichmentIdsRequest(habitatId);
        }
    }
}
