package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import java.util.List;

/**
 * RemoveAnimalFromHabitatResult object class.
 */
public class RemoveAnimalFromHabitatResult {
    private List<String> animalsInHabitat;

    /**
     * creates AddAnimalToHabitatResult object for retrieving a habitat's list of animals after removing an animal.
     *
     * @param animalsInHabitat list of animals to be returned.
     */
    private RemoveAnimalFromHabitatResult(List<String> animalsInHabitat) {
        this.animalsInHabitat = animalsInHabitat;
    }

    public List<String> getAnimalsInHabitat() {
        return animalsInHabitat;
    }

    @Override
    public String toString() {
        return "RemoveAnimalFromHabitatResult{" +
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
        public RemoveAnimalFromHabitatResult build() {
            return new RemoveAnimalFromHabitatResult(animalsInHabitat);
        }
    }
}
