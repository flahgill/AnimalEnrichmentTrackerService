package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewUserHabitatsRequest object class.
 */
public class ViewUserHabitatsRequest {
    private final String keeperManagerId;

    /**
     * creates ViewUserHabitatsRequest object for retrieving a keeper's list of habitats.
     *
     * @param keeperManagerId the keeper's Id used to retrieve a list of habitats
     */
    public ViewUserHabitatsRequest(String keeperManagerId) {
        this.keeperManagerId = keeperManagerId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "ViewUserHabitatsRequest{" +
                "keeperManagerId='" + keeperManagerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String keeperManagerId;

        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }

        public ViewUserHabitatsRequest build() {
            return new ViewUserHabitatsRequest(keeperManagerId);
        }
    }
}
