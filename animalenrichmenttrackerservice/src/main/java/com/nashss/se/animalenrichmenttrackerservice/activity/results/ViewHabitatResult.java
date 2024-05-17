package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

/**
 * viewHabitatResult object class.
 */
public class ViewHabitatResult {
    private final HabitatModel habitat;

    /**
     * creates viewHabitatResult object containing the retrieved habitat.
     *
     * @param habitat the HabitatModel as a result of retrieving the habitat from DDB
     */
    public ViewHabitatResult(HabitatModel habitat) {
        this.habitat = habitat;
    }

    public HabitatModel getHabitat() {
        return habitat;
    }

    @Override
    public String toString() {
        return "viewHabitatResult{" +
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

        public ViewHabitatResult build() {
            return new ViewHabitatResult(habitat);
        }
    }
}
