package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * RemoveAnimalRequest object class.
 */
public class RemoveAnimalRequest {
    private final String animalId;
    private final String keeperManagerId;

    /**
     * creates RemoveAnimalRequest object for removing an animal (hard delete).
     *
     * @param animalId animalId to specify the animal to remove.
     */
    private RemoveAnimalRequest(String animalId, String keeperManagerId) {
        this.animalId = animalId;
        this.keeperManagerId = keeperManagerId;
    }

    public String getAnimalId() {
        return animalId;
    }

    public String getKeeperManagerId() {
        return keeperManagerId;
    }

    @Override
    public String toString() {
        return "RemoveAnimalRequest{" +
                "animalId='" + animalId + '\'' +
                "keeperManagerId='" + keeperManagerId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String animalId;
        private String keeperManagerId;
        public Builder withAnimalId(String animalId) {
            this.animalId = animalId;
            return this;
        }
        public Builder withKeeperManagerId(String keeperManagerId) {
            this.keeperManagerId = keeperManagerId;
            return this;
        }
        public RemoveAnimalRequest build() {
            return new RemoveAnimalRequest(animalId, keeperManagerId);
        }
    }
}
