package com.nashss.se.animalenrichmenttrackerservice.activity.requests;

/**
 * ViewAnimalRequest object class.
 */
public class ViewAnimalRequest {
    private final String animalId;

    /**
     * creates ViewAnimalRequest object for retrieving an animal using the animalId.
     *
     * @param animalId animalId used to retrieve animal.
     */
    private ViewAnimalRequest(String animalId) {
        this.animalId = animalId;
    }

    public String getAnimalId() {
        return animalId;
    }

    @Override
    public String toString() {
        return "ViewAnimalRequest{" +
                "animalId='" + animalId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String animalId;
        public Builder withAnimalId(String animalId) {
            this.animalId = animalId;
            return this;
        }
        public ViewAnimalRequest build() {
            return new ViewAnimalRequest(animalId);
        }
    }
}
