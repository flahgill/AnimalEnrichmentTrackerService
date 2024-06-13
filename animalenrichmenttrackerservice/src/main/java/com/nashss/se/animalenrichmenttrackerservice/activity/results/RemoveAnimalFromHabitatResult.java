package com.nashss.se.animalenrichmenttrackerservice.activity.results;

import com.nashss.se.animalenrichmenttrackerservice.models.AnimalModel;

/**
 * RemoveAnimalFromHabitatResult object class.
 */
public class RemoveAnimalFromHabitatResult {
    private AnimalModel animalModel;

    /**
     * creates AddAnimalToHabitatResult object for retrieving a habitat's list of animals after removing an animal.
     *
     * @param animalModel animal removed from habitat.
     */
    private RemoveAnimalFromHabitatResult(AnimalModel animalModel) {
        this.animalModel = animalModel;
    }

    public AnimalModel getAnimalModel() {
        return animalModel;
    }

    @Override
    public String toString() {
        return "RemoveAnimalFromHabitatResult{" +
                "animalModel=" + animalModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AnimalModel animalModel;
        public Builder withAnimalModel(AnimalModel animalModel) {
            this.animalModel = animalModel;
            return this;
        }
        public RemoveAnimalFromHabitatResult build() {
            return new RemoveAnimalFromHabitatResult(animalModel);
        }
    }
}
