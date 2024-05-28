package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewInactiveHabitatsRequest object class.
 */
public class ViewInactiveHabitatsRequest {
    private final String isActive;

    /**
     * creates ViewInactiveHabitatsRequest object for retrieving a keeper's list of habitats.
     *
     * @param isActive the active status used to retrieve a list of habitats
     */
    private ViewInactiveHabitatsRequest(String isActive) {
        this.isActive = isActive;
    }

    public String getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "ViewInactiveHabitatsRequest{" +
                "isActive='" + isActive + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder{
        private String isActive;
        public Builder withIsActive(String isActive) {
            this.isActive = isActive;
            return this;
        }
        public ViewInactiveHabitatsRequest build() {
            return new ViewInactiveHabitatsRequest(isActive);
        }
    }
}
