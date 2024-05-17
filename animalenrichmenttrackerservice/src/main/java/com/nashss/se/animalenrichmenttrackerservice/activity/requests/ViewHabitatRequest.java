package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * viewHabitatRequest object class.
 */
public class ViewHabitatRequest {
    private final String habitatId;

    /**
     * creates viewHabitatRequest object for retrieving a habitat using the habitatId.
     *
     * @param habitatId the habitatId used to retrieve the habitat
     */
    public ViewHabitatRequest(String habitatId) {
        this.habitatId = habitatId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    @Override
    public String toString() {
        return "viewHabitatRequest{" +
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

        public ViewHabitatRequest build() {
            return new ViewHabitatRequest(habitatId);
        }
    }
}
