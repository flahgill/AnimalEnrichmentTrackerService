package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

/**
 * UpdateHabitatResult object class.
 */
public class UpdateHabitatResult {
    private final HabitatModel habitat;

    /**
     * creates UpdateHabitatResult object for updating a habitat using the habitatId.
     *
     * @param habitat the updated habitat to persist.
     */
    private UpdateHabitatResult(HabitatModel habitat) {
        this.habitat = habitat;
    }

    public HabitatModel getHabitat() {
        return habitat;
    }

    @Override
    public String toString() {
        return "UpdateHabitatResult{" +
                "habitat=" + habitat +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HabitatModel habitat;
        public Builder withHabitat(HabitatModel habitat) {
            this.habitat = habitat;
            return this;
        }
        public UpdateHabitatResult build() {
            return new UpdateHabitatResult(habitat);
        }
    }
}
