package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * viewHabitatRequest object class.
 */
public class ViewHabitatRequest {
    private final String habitatId;
    private final String keeperManagerId;

    /**
     * creates viewHabitatRequest object for retrieving a habitat using the habitatId and keeperManagerId.
     *
     * @param habitatId the habitatId used to retrieve the habitat
     * @param keeperManagerId the keeperManagerId used to retrieve the habitat
     */
    public ViewHabitatRequest(String habitatId, String keeperManagerId) {
        this.habitatId = habitatId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getHabitatId() {
        return habitatId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "viewHabitatRequest{" +
                "habitatId='" + habitatId + '\'' +
                "keeperManagerId='" + keeperManagerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String habitatId;
        private String keeperManagerId;

        public Builder withHabitatId(String habitatId) {
            this.habitatId = habitatId;
            return this;
        }

        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }

        public ViewHabitatRequest build() {
            return new ViewHabitatRequest(habitatId, keeperManagerId);
        }
    }
}
