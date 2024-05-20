package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

/**
 * RemoveHabitatResult object class.
 */
public class RemoveHabitatResult {
    private final HabitatModel habitat;

    /**
     * creates RemoveHabitatResult object for removing a habitat using the habitatId and keeperManagerId.
     *
     * @param habitat to be removed.
     */
    public RemoveHabitatResult(HabitatModel habitat) {
        this.habitat = habitat;
    }

    public HabitatModel getHabitat() {
        return habitat;
    }

    @Override
    public String toString() {
        return "RemoveHabitatResult{" +
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

        public RemoveHabitatResult build() {
            return new RemoveHabitatResult(habitat);
        }
    }
}
