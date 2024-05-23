package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * AddAnimalToHabitatResult object class.
 */
public class AddAnimalToHabitatResult {
    private final List<String> animalsInHabitat;

    /**
     * creates AddAnimalToHabitatResult object for retrieving a habitat's list of animals after a new addition.
     *
     * @param animalsInHabitat list of animals to be returned.
     */
    private AddAnimalToHabitatResult(List<String> animalsInHabitat) {
        this.animalsInHabitat = animalsInHabitat;
    }

    public List<String> getAnimalsInHabitat() {
        return animalsInHabitat;
    }

    @Override
    public String toString() {
        return "AddAnimalToHabitatResult{" +
                "animalsInHabitat=" + animalsInHabitat +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> animalsInHabitat;
        public Builder withAnimalsInHabitat(List<String> animalsInHabitat) {
            this.animalsInHabitat = animalsInHabitat;
            return this;
        }
        public AddAnimalToHabitatResult build() {
            return new AddAnimalToHabitatResult(animalsInHabitat);
        }
    }
}
