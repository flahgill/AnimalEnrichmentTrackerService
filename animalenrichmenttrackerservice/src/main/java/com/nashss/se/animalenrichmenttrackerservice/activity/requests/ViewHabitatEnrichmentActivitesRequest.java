package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewHabitatEnrichmentActivitesRequest object class.
 */
public class ViewHabitatEnrichmentActivitesRequest {
    private final String habitatId;

    /**
     * creates ViewHabitatEnrichmentActivitesRequest object for retrieving a habitat's list of completed enrichment activities.
     *
     * @param habitatId the habitatId to look up.
     */
    private ViewHabitatEnrichmentActivitesRequest(String habitatId) {
        this.habitatId = habitatId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    @Override
    public String toString() {
        return "ViewHabitatEnrichmentsRequest{" +
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
        public ViewHabitatEnrichmentActivitesRequest build() {
            return new ViewHabitatEnrichmentActivitesRequest(habitatId);
        }
    }


}
