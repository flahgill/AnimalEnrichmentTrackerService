package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewAnimalsForHabitatRequest object class.
 */
public class ViewAnimalsForHabitatRequest {
    private final String habitatId;

    /**
     * creates ViewAnimalsForHabitat object for retrieving a habitat's list of animals using the habitatId.
     *
     * @param habitatId the habitatId used to retrieve the habitat
     */
    private ViewAnimalsForHabitatRequest(String habitatId) {
        this.habitatId = habitatId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    @Override
    public String toString() {
        return "ViewAnimalsForHabitat{" +
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
        public ViewAnimalsForHabitatRequest build() {
            return new ViewAnimalsForHabitatRequest(habitatId);
        }
    }
}
