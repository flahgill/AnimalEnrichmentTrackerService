package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewSpeciesListRequest object class.
 */
public class ViewSpeciesListRequest {
    private final String habitatId;

    /**
     * creates ViewSpeciesListRequest object for retrieving a habitat's list of species using the habitatId.
     *
     * @param habitatId the habitatId used to retrieve the habitat.
     */
    private ViewSpeciesListRequest(String habitatId) {
        this.habitatId = habitatId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    @Override
    public String toString() {
        return "ViewSpeciesListRequest{" +
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
        public ViewSpeciesListRequest build() {
            return new ViewSpeciesListRequest(habitatId);
        }
    }
}
