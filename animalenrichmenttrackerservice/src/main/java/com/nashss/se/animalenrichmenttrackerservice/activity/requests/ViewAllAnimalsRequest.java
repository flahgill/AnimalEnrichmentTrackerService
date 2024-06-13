package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewAllAnimalsRequest object class.
 */
public class ViewAllAnimalsRequest {
    private final String isActive;

    /**
     * creates ViewAllAnimalsRequest object for retrieving all animals based on the active status.
     *
     * @param isActive the active status specified by user.
     */
    private ViewAllAnimalsRequest(String isActive) {
        this.isActive = isActive;
    }

    public String getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "ViewAllAnimalsRequest{" +
                "isActive='" + isActive + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String isActive;
        public Builder withIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }
        public ViewAllAnimalsRequest build() {
            return new ViewAllAnimalsRequest(isActive);
        }
    }
}
