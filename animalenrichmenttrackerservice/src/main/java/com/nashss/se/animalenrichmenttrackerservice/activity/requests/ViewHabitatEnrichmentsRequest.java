package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewHabitatEnrichmentsRequest object class.
 */
public class ViewHabitatEnrichmentsRequest {
    private final String habitatId;

    /**
     * creates ViewHabitatEnrichmentsRequest object for retrieving a habitat's list of completed enrichment activities.
     *
     * @param habitatId the habitatId to look up.
     */
    private ViewHabitatEnrichmentsRequest(String habitatId) {
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
        public ViewHabitatEnrichmentsRequest build() {
            return new ViewHabitatEnrichmentsRequest(habitatId);
        }
    }


}
