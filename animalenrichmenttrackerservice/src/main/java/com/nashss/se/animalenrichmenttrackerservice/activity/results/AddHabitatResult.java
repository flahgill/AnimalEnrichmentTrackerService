package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.HabitatModel;

public class AddHabitatResult {
    private final HabitatModel habitat;

    private AddHabitatResult(HabitatModel habitat) {
        this.habitat = habitat;
    }

    public HabitatModel getHabitat() {
        return habitat;
    }

    @Override
    public String toString() {
        return "AddHabitatResult{" +
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

        public AddHabitatResult build() {
            return new AddHabitatResult(habitat);
        }
    }
}
